package com.awesome.domains.chatroom;

import com.awesome.domains.chatroom.entities.ChatRoomEntity;
import com.awesome.domains.chatroom.entities.ChatRoomMessageEntity;
import com.awesome.domains.chatroom.entities.vos.ChatRoomPageInfoVO;
import lombok.Data;

import java.util.List;

@Data
public class ChatRoom {
  private ChatRoomEntity chatRoomEntity;
  private List<ChatRoomMessageEntity> chatRoomMessageEntities;



  // -- Message Paging 지원을 위한 집합 하위 모델
  private boolean pageable;

  private ChatRoomPageInfoVO chatRoomMessagePageInfoVO;
}
