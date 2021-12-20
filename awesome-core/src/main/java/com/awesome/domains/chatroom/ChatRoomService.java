package com.awesome.domains.chatroom;

import com.awesome.domains.chatroom.entities.ChatRoomEntity;
import com.awesome.domains.chatroom.entities.ChatRoomMessageEntity;
import com.awesome.domains.chatroom.entities.ChatRoomUserEntity;
import com.awesome.domains.chatroom.enums.MessageType;
import com.awesome.infrastructures.shared.chatroom.ChatDetailDTO;
import com.awesome.infrastructures.shared.chatroom.ChatMessageDTO;
import com.awesome.infrastructures.shared.chatroom.ChatMessageDetailDTO;
import com.awesome.infrastructures.shared.chatroom.ChatRoomDTO;
import com.google.common.collect.Lists;
import lombok.RequiredArgsConstructor;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
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

    if (userId == null) {
      userId = "7";
    }

    if (userName == null) {
      userName = "robin.son";
    }

    ChatRoomEntity entity = new ChatRoomEntity();
    entity.setRoomCreatorUserId(userId);
    entity.setRoomCreatorUserName(userName);
    entity.setRoomName(name);
    entity.setRoomMaxUserNum(roomMaxUserNum);
    entity.setRoomCurUserNum(1L);
    chatRoom.setChatRoomEntity(entity);

    return chatRoom;
  }

  @Transactional
  public void enterChatRoomInitCreater(ChatRoom chatRoom) {
    ChatRoomEntity chatRoomEntity = chatRoom.getChatRoomEntity();

    ChatRoomUserEntity chatRoomUserEntity = new ChatRoomUserEntity();
    chatRoomUserEntity.setRoomId(chatRoomEntity.getId());
    chatRoomUserEntity.setUserId(chatRoomEntity.getRoomCreatorUserId());
    chatRoomUserEntity.setUserName(chatRoomEntity.getRoomCreatorUserName());

    List<ChatRoomUserEntity> chatRoomUserEntities = Lists.newArrayList(chatRoomUserEntity);
    chatRoom.setChatRoomUserEntities(chatRoomUserEntities);

    ChatRoomMessageEntity message = new ChatRoomMessageEntity();
    message.setMessageType(MessageType.ENTER);
    message.setRoomId(chatRoomEntity.getId());
    message.setMessageSendUserId(chatRoomEntity.getRoomCreatorUserId());
    message.setMessageSendUserName(chatRoomEntity.getRoomCreatorUserName());
    message.setMessage(chatRoomEntity.getRoomCreatorUserName() + "님이 채팅방에 참여하였습니다.");

    List<ChatRoomMessageEntity> chatRoomMessageEntities = Lists.newArrayList(message);
    chatRoom.setChatRoomMessageEntities(chatRoomMessageEntities);

    chatRoomRepository.save(chatRoom);
  }

  public ChatRoom enterRoom(ChatMessageDTO message) {
    ChatRoom chatRoom = chatRoomRepository.findChatRoomById(message.getRoomId());
    message.setMessageType(MessageType.ENTER);

    if(CollectionUtils.isEmpty(chatRoom.getChatRoomUserEntities())){
      ChatRoomEntity chatRoomEntity = chatRoom.getChatRoomEntity();
      chatRoomEntity.setRoomCurUserNum(chatRoomEntity.getRoomCurUserNum() + 1);
      chatRoom.setChatRoomEntity(chatRoomEntity);

      ChatRoomUserEntity chatRoomUserEntity = new ChatRoomUserEntity();
      chatRoomUserEntity.setRoomId(message.getRoomId());
      chatRoomUserEntity.setUserId(message.getMessageSendUserId());
      chatRoomUserEntity.setUserName(message.getMessageSendUserName());
      List<ChatRoomUserEntity> userEntities = new ArrayList<>();
      userEntities.add(chatRoomUserEntity);

      chatRoom.setChatRoomUserEntities(userEntities);

      message.setMessage(message.getMessageSendUserName() + "님이 채팅방에 참여하였습니다.");

      List<ChatRoomMessageEntity> messageEntities = new ArrayList<>();
      ChatRoomMessageEntity messageEntity = ChatMessageDTO.convertDtoToEntity(message);
      messageEntities.add(messageEntity);

      chatRoom.setChatRoomMessageEntities(messageEntities);
    }else{
      List<ChatRoomUserEntity> chatRoomUserEntities = chatRoom.getChatRoomUserEntities();

      Optional<ChatRoomUserEntity> first = chatRoom.getChatRoomUserEntities().stream().filter(e ->
              Objects.equals(e.getUserId(), message.getMessageSendUserId())).findFirst();

      if (first.isEmpty()) {
        ChatRoomEntity chatRoomEntity = chatRoom.getChatRoomEntity();
        chatRoomEntity.setRoomCurUserNum(chatRoomEntity.getRoomCurUserNum() + 1);
        chatRoom.setChatRoomEntity(chatRoomEntity);

        ChatRoomUserEntity chatRoomUserEntity = new ChatRoomUserEntity();
        chatRoomUserEntity.setRoomId(message.getRoomId());
        chatRoomUserEntity.setUserId(message.getMessageSendUserId());
        chatRoomUserEntity.setUserName(message.getMessageSendUserName());

        chatRoomUserEntities.add(chatRoomUserEntity);

        List<ChatRoomMessageEntity> messageEntities = new ArrayList<>();
        message.setMessage(message.getMessageSendUserName() + "님이 채팅방에 참여하였습니다.");
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

  public void appendInitChatMessage(ChatRoom chatRoom){
    Pageable limit = PageRequest.of(0, 50);

    chatRoomRepository.appendEnterChatRoomMessage(chatRoom, limit);
  }

  public Boolean checkAlreadyJoined(Long roomId, String userId){
    ChatRoom chatRoom = chatRoomRepository.findChatRoomById(roomId);

    if(Objects.isNull(chatRoom.getChatRoomEntity())){
      return false;
    }

    if(Objects.isNull(chatRoom.getChatRoomUserEntities())){
      return false;
    }

    List<ChatRoomUserEntity> chatRoomUserEntities = chatRoom.getChatRoomUserEntities();

    for(ChatRoomUserEntity entity : chatRoomUserEntities){
      String entityUserId = entity.getUserId();
      if(entityUserId.equals(userId)){
        return true;
      }
    }

    return false;
  }

  public ChatRoom sendMessage(ChatMessageDTO message) {
    ChatRoom chatRoom = chatRoomRepository.findChatRoomById(message.getRoomId());
    message.setMessageType(MessageType.MESSAGE);

    if (Objects.isNull(chatRoom.getChatRoomEntity())) {
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

  public ChatMessageDetailDTO getLimitedSizeChatMessages(Long roomId, Long id){
    if(Objects.isNull(roomId)){
      return ChatMessageDetailDTO.empty();
    }

    Pageable limit = PageRequest.of(0, 50);

    ChatRoom chatRoom = chatRoomRepository.findChatRoomMessagePageable(roomId, id, limit);
    ChatMessageDetailDTO chatMessageDetailDTO = new ChatMessageDetailDTO();
    chatMessageDetailDTO.setNext(chatRoom.getChatRoomMessagePageInfoVO().hasNext());
    chatMessageDetailDTO.setMessages(chatRoom.getChatRoomMessageEntities().stream().map(ChatMessageDTO::convertEntityToDto).collect(Collectors.toList()));

    return chatMessageDetailDTO;
  }

  public ChatDetailDTO getLimitedSizeChatRooms(Long id, int size) {
    if (Objects.isNull(size)) {
      return ChatDetailDTO.empty();
    }

    if (size < 1) {
      return ChatDetailDTO.empty();
    }

    Pageable limit = PageRequest.of(0, size);

    Page<ChatRoom> chatRooms = Objects.isNull(id) ? chatRoomRepository.findInitAllChatRoom(limit) : chatRoomRepository.findAllChatRoomPageable(id, limit);

    ChatDetailDTO chatDetailDTO = new ChatDetailDTO();
    chatDetailDTO.setNext(chatRooms.hasNext());
    chatDetailDTO.setRooms(chatRooms.stream().map(e -> ChatRoomDTO.convertEntityToDto(e.getChatRoomEntity())).collect(Collectors.toList()));

    return chatDetailDTO;
  }

  public ChatDetailDTO getLimitedSizeOwnsChatRooms(Long id, int size, String userId) {

    if (Objects.isNull(size)) {
      return ChatDetailDTO.empty();
    }

    if (size < 1) {
      return ChatDetailDTO.empty();
    }

    Pageable limit = PageRequest.of(0, size);

    Page<ChatRoom> chatRooms = Objects.isNull(id) ? chatRoomRepository.findInitChatOwnedRoom(userId, limit) : chatRoomRepository.findAllOwnsChatRoomPageable(id, userId, limit);

    ChatDetailDTO chatDetailDTO = new ChatDetailDTO();
    chatDetailDTO.setNext(chatRooms.hasNext());
    chatDetailDTO.setRooms(chatRooms.stream().map(e -> ChatRoomDTO.convertEntityToDto(e.getChatRoomEntity())).collect(Collectors.toList()));

    return chatDetailDTO;
  }
}
