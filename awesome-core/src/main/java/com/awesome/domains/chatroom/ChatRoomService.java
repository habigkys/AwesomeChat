package com.awesome.domains.chatroom;

import com.awesome.domains.chatroom.entities.ChatRoomEntity;
import com.awesome.domains.chatroom.entities.ChatRoomMessageEntity;
import com.awesome.domains.chatroom.entities.ChatRoomUserEntity;
import com.awesome.domains.chatroom.enums.MessageType;
import com.awesome.infrastructures.shared.chatroom.ChatDetailDTO;
import com.awesome.infrastructures.shared.chatroom.ChatMessageDTO;
import com.awesome.infrastructures.shared.chatroom.ChatRoomDTO;
import lombok.RequiredArgsConstructor;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Service;

import java.nio.ByteBuffer;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ChatRoomService {
  private final ChatRoomRepository chatRoomRepository;

  public ChatRoom create(String userId, String userName, String name, Long roomMaxUserNum) {
    ChatRoom chatRoom = new ChatRoom();

    if (Objects.isNull(name)) {
      return chatRoom;
    }

    if (Objects.isNull(roomMaxUserNum)) {
      return chatRoom;
    }

    ChatRoomEntity entity = new ChatRoomEntity();
    entity.setRoomCreatorUserId(userId);
    entity.setRoomCreatorUserName(userName);
    entity.setRoomName(name);
    entity.setRoomMaxUserNum(roomMaxUserNum);
    entity.setRoomCurUserNum(0L);
    chatRoom.setChatRoomEntity(entity);

    return chatRoom;
  }

  public ChatRoom enterRoom(ChatMessageDTO message){
    ChatRoom chatRoom = chatRoomRepository.findChatRoomById(message.getRoomId());
    message.setMessageType(MessageType.ENTER);

    if(CollectionUtils.isEmpty(chatRoom.getChatRoomUserEntities())){
      ChatRoomEntity chatRoomEntity = chatRoom.getChatRoomEntity();
      chatRoomEntity.setRoomCurUserNum(chatRoomEntity.getRoomCurUserNum() + 1);
      chatRoom.setChatRoomEntity(chatRoomEntity);

      ChatRoomUserEntity chatRoomUserEntity = new ChatRoomUserEntity();
      chatRoomUserEntity.setRoomId(message.getRoomId());
      chatRoomUserEntity.setUserId(message.getMessageSendUserId());
      List<ChatRoomUserEntity> userEntities = new ArrayList<>();
      userEntities.add(chatRoomUserEntity);

      chatRoom.setChatRoomUserEntities(userEntities);

      message.setMessage(message.getMessageSendUserId() + "님이 채팅방에 참여하였습니다.");

      List<ChatRoomMessageEntity> messageEntities = new ArrayList<>();
      ChatRoomMessageEntity messageEntity = ChatMessageDTO.convertDtoToEntity(message);
      messageEntities.add(messageEntity);

      chatRoom.setChatRoomMessageEntities(messageEntities);
    }else{
      List<ChatRoomUserEntity> chatRoomUserEntities = chatRoom.getChatRoomUserEntities();

      message.setMessage(message.getMessageSendUserName() + "님이 채팅방에 참여하였습니다.");

      Optional<ChatRoomUserEntity> first = chatRoom.getChatRoomUserEntities().stream().filter(e ->
              Objects.equals(e.getUserId(), message.getMessageSendUserId())).findFirst();

      if (first.isEmpty()) {
        ChatRoomEntity chatRoomEntity = chatRoom.getChatRoomEntity();
        chatRoomEntity.setRoomCurUserNum(chatRoomEntity.getRoomCurUserNum() + 1);
        chatRoom.setChatRoomEntity(chatRoomEntity);

        ChatRoomUserEntity chatRoomUserEntity = new ChatRoomUserEntity();
        chatRoomUserEntity.setRoomId(message.getRoomId());
        chatRoomUserEntity.setUserId(message.getMessageSendUserId());

        chatRoomUserEntities.add(chatRoomUserEntity);

        List<ChatRoomMessageEntity> messageEntities = new ArrayList<>();
        ChatRoomMessageEntity messageEntity = ChatMessageDTO.convertDtoToEntity(message);
        messageEntities.add(messageEntity);

        chatRoom.setChatRoomMessageEntities(messageEntities);
      }else{
        List<ChatRoomMessageEntity> messageEntities = new ArrayList<>();
        ChatRoomMessageEntity messageEntity = ChatMessageDTO.convertDtoToEntity(message);
        messageEntities.add(messageEntity);

        chatRoom.setChatRoomMessageEntities(messageEntities);
      }
    }

    return chatRoom;
  }

  public ChatRoom sendMessage(ChatMessageDTO message) {
    ChatRoom chatRoom = chatRoomRepository.findChatRoomById(message.getRoomId());
    message.setMessageType(MessageType.MESSAGE);

    if(Objects.isNull(chatRoom.getChatRoomEntity())){
      throw new RuntimeException();
    }
    /*
    chatRoomRepository.appendChatRoomUser(chatRoom);

    // new Predicate 적어보기
    Optional<ChatRoomUserEntity> first = chatRoom.getChatRoomUserEntities().stream().filter(e ->
            Objects.equals(e.getUserId(), message.getMessageSendUserId())).findFirst();

    if(first.isEmpty()){
      //todo
      throw new RuntimeException();
    }
    */

    List<ChatRoomMessageEntity> messageEntities = new ArrayList<>();
    ChatRoomMessageEntity messageEntity = ChatMessageDTO.convertDtoToEntity(message);
    messageEntities.add(messageEntity);

    chatRoom.setChatRoomMessageEntities(messageEntities);

    return chatRoom;
  }

  public ChatRoom disconnectRoom(ChatMessageDTO message) {
    ChatRoom chatRoom = chatRoomRepository.findChatRoomByIdAndUserId(message.getRoomId(), message.getMessageSendUserId());
    message.setMessageType(MessageType.OUT);

    if (CollectionUtils.isNotEmpty(chatRoom.getChatRoomUserEntities())) {
      ChatRoomEntity chatRoomEntity = chatRoom.getChatRoomEntity();
      chatRoomEntity.setRoomCurUserNum(chatRoomEntity.getRoomCurUserNum() - 1);
      chatRoom.setChatRoomEntity(chatRoomEntity);
      message.setMessage(message.getMessageSendUserName() + "님이 채팅방에서 나갔습니다.");

      List<ChatRoomMessageEntity> messageEntities = new ArrayList<>();
      ChatRoomMessageEntity messageEntity = ChatMessageDTO.convertDtoToEntity(message);
      messageEntities.add(messageEntity);

      chatRoom.setChatRoomMessageEntities(messageEntities);
    }
    return chatRoom;
  }

  public ChatDetailDTO getLimitedSizeChatRooms(Long id, int size) {
    ChatDetailDTO chatDetailDTO = new ChatDetailDTO();
    List<ChatRoom> chatRooms = new ArrayList<>();

    if (Objects.isNull(size)) {
      return chatDetailDTO;
    }

    if (size < 1){
      return chatDetailDTO;
    }

    chatRooms = chatRoomRepository.findAllChatRoomPageable(id, size);

    if(CollectionUtils.isEmpty(chatRooms)){
      chatRooms = chatRoomRepository.findInitChatRoom(id, size);
    }

    if(chatRooms.size() < size){
      chatDetailDTO.setNext("false");
    }

    chatDetailDTO.setRooms(chatRooms.stream().map(e -> ChatRoomDTO.convertEntityToDto(e.getChatRoomEntity())).collect(Collectors.toList()));

    return chatDetailDTO;
  }

  public ChatDetailDTO getLimitedSizeOwnsChatRooms(Long id, int size, String userId) {
    ChatDetailDTO chatDetailDTO = new ChatDetailDTO();
    List<ChatRoom> chatRooms = new ArrayList<>();

    if (Objects.isNull(size)) {
      return chatDetailDTO;
    }

    if (Objects.isNull(userId)) {
      return chatDetailDTO;
    }

    if (size < 1){
      return chatDetailDTO;
    }

    chatRooms = chatRoomRepository.findAllOwnsChatRoomPageable(id, userId, size);

    if(CollectionUtils.isEmpty(chatRooms)){
      chatRooms = chatRoomRepository.findInitChatOwnedRoom(id, userId, size);
    }

    if(chatRooms.size() < size){
      chatDetailDTO.setNext("false");
    }

    chatDetailDTO.setRooms(chatRooms.stream().map(e -> ChatRoomDTO.convertEntityToDto(e.getChatRoomEntity())).collect(Collectors.toList()));

    return chatDetailDTO;
  }

  private static String uuidToBase64(String str) {
    Base64 base64 = new Base64();
    UUID uuid = UUID.fromString(str);
    ByteBuffer bb = ByteBuffer.wrap(new byte[16]);
    bb.putLong(uuid.getMostSignificantBits());
    bb.putLong(uuid.getLeastSignificantBits());
    return base64.encodeBase64URLSafeString(bb.array());
  }
}
