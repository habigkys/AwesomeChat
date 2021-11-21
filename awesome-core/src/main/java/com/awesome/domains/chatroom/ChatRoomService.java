package com.awesome.domains.chatroom;

import com.awesome.domains.chatroom.entities.ChatRoomEntity;
import com.awesome.infrastructures.shared.chatroom.ChatMessageDTO;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Objects;

@Service
public class ChatRoomService {
  public ChatRoom create(String roomCreatorUserId, String name, Long roomMaxUserNum) {
    ChatRoom chatRoom = new ChatRoom();
    if (Objects.isNull(roomCreatorUserId)) {
      return chatRoom;
    }

    if (Objects.isNull(name)) {
      return chatRoom;
    }

    if (Objects.isNull(roomMaxUserNum)) {
      return chatRoom;
    }

    ChatRoomEntity entity = new ChatRoomEntity();
    entity.setRoomCreatorUserId(roomCreatorUserId);
    entity.setRoomName(name);
    entity.setRoomMaxUserNum(roomMaxUserNum);
    entity.setRoomCurUserNum(0L);
    chatRoom.setChatRoomEntity(entity);

    return chatRoom;
  }

  public ChatRoom enterRoom(ChatMessageDTO chatMessageDTO){
    ChatRoom chatRoom = new ChatRoom();

    return chatRoom;
  }

  @Transactional
  public void sendMessage(ChatMessageDTO message) {

  }

  public ChatRoom disconnectRoom(ChatMessageDTO message) {
    return null;
  }
}
