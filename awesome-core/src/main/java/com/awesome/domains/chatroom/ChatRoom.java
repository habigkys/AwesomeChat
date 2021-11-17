package com.awesome.domains.chatroom;

import com.awesome.domains.chatroom.entities.ChatRoomEntity;
import com.awesome.domains.chatroom.entities.ChatRoomMessageEntity;
import com.awesome.domains.chatroom.entities.ChatRoomUserEntity;
import com.awesome.domains.chatroom.entities.vos.ChatRoomPageInfoVO;
import lombok.Data;

import java.util.List;

@Data
public class ChatRoom {
    private ChatRoomEntity chatRoomEntity;
    private List<ChatRoomMessageEntity> chatRoomMessageEntities;
    private List<ChatRoomUserEntity> chatUserEntities;
    private ChatRoomPageInfoVO chatRoomMessagePageInfoVO;
}
