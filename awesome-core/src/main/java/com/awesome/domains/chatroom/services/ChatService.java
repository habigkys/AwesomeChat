package com.awesome.domains.chatroom.services;

import com.awesome.domains.chatroom.ChatRoom;
import com.awesome.domains.chatroom.ChatRoomRepository;
import com.awesome.domains.chatroom.dtos.ChatMessageDTO;
import com.awesome.domains.chatroom.dtos.ChatRoomDTO;
import com.awesome.domains.chatroom.entities.ChatRoomMessageDAO;
import com.awesome.domains.chatroom.entities.ChatRoomDAO;
import com.awesome.domains.chatroom.entities.ChatRoomEntity;
import com.awesome.domains.mapping.entities.ChatUserRoomDAO;
import com.awesome.domains.mapping.entities.ChatUserRoomEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ChatService {

    private final ChatRoomRepository chatRoomRepository;

    public ChatRoom createChatRoom(String roomCreatorUserId, String name, Long roomMaxUserNum){
        ChatRoomEntity entity = new ChatRoomEntity();
        entity.setRoomId(UUID.randomUUID().toString());
        entity.setRoomCreatorUserId(roomCreatorUserId);
        entity.setRoomName(name);
        entity.setRoomMaxUserNum(roomMaxUserNum);
        entity.setRoomCurUserNum(0L);
        return new ChatRoom();
    }


//
//    @Transactional
//    public void disconnectRoom(ChatMessageDTO chatMessageDTO){
//        ChatRoomEntity toBeUpdate = chatRoomDAO.findByRoomId(chatMessageDTO.getRoomId());
//        toBeUpdate.setRoomCurUserNum(toBeUpdate.getRoomCurUserNum()-1);
//
//        chatRoomDAO.save(toBeUpdate);
//
//        List<ChatUserRoomEntity> list = chatUserRoomDAO.findAllByRoomIdAndUserId(chatMessageDTO.getRoomId(), chatMessageDTO.getMessageSendUserId());
//        chatUserRoomDAO.deleteAll(list);
//    }
}
