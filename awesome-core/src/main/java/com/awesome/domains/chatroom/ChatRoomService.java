package com.awesome.domains.chatroom;

import com.awesome.domains.chatroom.entities.ChatRoomEntity;
import com.awesome.domains.chatroom.entities.ChatRoomMessageEntity;
import com.awesome.domains.chatroom.entities.ChatRoomUserEntity;
import com.awesome.infrastructures.shared.chatroom.ChatMessageDTO;
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

  public ChatRoom create(String name, Long roomMaxUserNum) {
    ChatRoom chatRoom = new ChatRoom();

    if (Objects.isNull(name)) {
      return chatRoom;
    }

    if (Objects.isNull(roomMaxUserNum)) {
      return chatRoom;
    }

    ChatRoomEntity entity = new ChatRoomEntity();
    entity.setRoomId(uuidToBase64(UUID.randomUUID().toString()));
    entity.setRoomCreatorUserId("abc@abc.com");
    entity.setRoomName(name);
    entity.setRoomMaxUserNum(roomMaxUserNum);
    entity.setRoomCurUserNum(0L);
    chatRoom.setChatRoomEntity(entity);

    return chatRoom;
  }

  public ChatRoom enterRoom(ChatMessageDTO message){
    ChatRoom chatRoom = chatRoomRepository.findChatRoomById(message.getRoomId());

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
    }

    if(CollectionUtils.isNotEmpty(chatRoom.getChatRoomUserEntities())){
      List<ChatRoomUserEntity> userEntities = chatRoom.getChatRoomUserEntities();

      ChatRoomUserEntity chatRoomUserEntity = new ChatRoomUserEntity();
      chatRoomUserEntity.setRoomId(message.getRoomId());
      chatRoomUserEntity.setUserId(message.getMessageSendUserId());
      userEntities.add(chatRoomUserEntity);

      chatRoom.setChatRoomUserEntities(userEntities);

      message.setMessage(message.getMessageSendUserId() + "님이 채팅방에 참여하였습니다.");

      List<ChatRoomMessageEntity> messageEntities = new ArrayList<>();
      ChatRoomMessageEntity messageEntity = ChatMessageDTO.convertDtoToEntity(message);
      messageEntities.add(messageEntity);

      chatRoom.setChatRoomMessageEntities(messageEntities);
    }

    return chatRoom;
  }

  public ChatRoom sendMessage(ChatMessageDTO message) {
    ChatRoom chatRoom = chatRoomRepository.findChatRoomById(message.getRoomId());

    if(Objects.isNull(chatRoom.getChatRoomEntity())){
      throw new RuntimeException();
    }

    chatRoomRepository.appendChatRoomUser(chatRoom);

    // new Predicate 적어보기
    Optional<ChatRoomUserEntity> first = chatRoom.getChatRoomUserEntities().stream().filter(e ->
            Objects.equals(e.getUserId(), message.getMessageSendUserId())).findFirst();

    if(first.isEmpty()){
      throw new RuntimeException();
    }

    List<ChatRoomMessageEntity> messageEntities = new ArrayList<>();
    ChatRoomMessageEntity messageEntity = ChatMessageDTO.convertDtoToEntity(message);
    messageEntities.add(messageEntity);

    chatRoom.setChatRoomMessageEntities(messageEntities);

    return chatRoom;
  }

  public ChatRoom disconnectRoom(ChatMessageDTO message) {
    ChatRoom chatRoom = chatRoomRepository.findChatRoomById(message.getRoomId());

    if(CollectionUtils.isNotEmpty(chatRoom.getChatRoomUserEntities())){
      ChatRoomEntity chatRoomEntity = chatRoom.getChatRoomEntity();
      chatRoomEntity.setRoomCurUserNum(chatRoomEntity.getRoomCurUserNum() - 1);
      chatRoom.setChatRoomEntity(chatRoomEntity);

      ChatRoomUserEntity chatRoomUserEntity = new ChatRoomUserEntity();
      chatRoomUserEntity.setRoomId(message.getRoomId());
      chatRoomUserEntity.setUserId(message.getMessageSendUserId());
      List<ChatRoomUserEntity> userEntities = new ArrayList<>();
      userEntities.add(chatRoomUserEntity);

      chatRoom.setChatRoomUserEntities(userEntities);

      message.setMessage(message.getMessageSendUserId() + "님이 채팅방에서 나갔습니다.");

      List<ChatRoomMessageEntity> messageEntities = new ArrayList<>();
      ChatRoomMessageEntity messageEntity = ChatMessageDTO.convertDtoToEntity(message);
      messageEntities.add(messageEntity);

      chatRoom.setChatRoomMessageEntities(messageEntities);
    }

    return chatRoom;
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
