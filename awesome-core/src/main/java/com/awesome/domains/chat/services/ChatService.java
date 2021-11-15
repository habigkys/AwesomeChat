package com.awesome.domains.chat.services;

import com.awesome.domains.chat.dtos.ChatMessageDTO;
import com.awesome.domains.chat.dtos.ChatRoomDTO;
import com.awesome.domains.chat.entities.ChatMessageDAO;
import com.awesome.domains.chat.entities.ChatRoomDAO;
import com.awesome.domains.chat.entities.ChatRoomEntity;
import com.awesome.domains.mapping.entities.ChatUserRoomDAO;
import com.awesome.domains.mapping.entities.ChatUserRoomEntity;
import com.google.common.collect.Lists;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ChatService {
    private final ChatRoomDAO chatRoomDAO;
    private final ChatMessageDAO chatMessageDAO;
    private final ChatUserRoomDAO chatUserRoomDAO;

    public List<ChatRoomDTO> findAllRooms(){
        return chatRoomDAO.findAll().stream().map(ChatRoomDTO::convertEntityToDto).collect(Collectors.toList());
    }

    public ChatRoomDTO findRoomById(String roomId){
        return ChatRoomDTO.convertEntityToDto(chatRoomDAO.findByRoomId(roomId));
    }

    public List<ChatRoomDTO> findRoomByUserId(String userId){
        List<String> roomIds = chatUserRoomDAO.findRoomIdsByUserId(userId);

        return chatRoomDAO.findAllByRoomIds(roomIds).stream().map(ChatRoomDTO::convertEntityToDto).collect(Collectors.toList());
    }

    @Transactional
    public ChatRoomDTO createChatRoom(String roomCreatorUserId, String name, Long roomMaxUserNum){
        ChatRoomEntity entity = new ChatRoomEntity();
        entity.setRoomId(UUID.randomUUID().toString());
        entity.setRoomCreatorUserId(roomCreatorUserId);
        entity.setRoomName(name);
        entity.setRoomMaxUserNum(roomMaxUserNum);
        entity.setRoomCurUserNum(0L);

        return ChatRoomDTO.convertEntityToDto(chatRoomDAO.save(entity));
    }

    @Transactional
    public void addUserToRoom(ChatMessageDTO chatMessageDTO){
        ChatUserRoomEntity entity = new ChatUserRoomEntity();
        entity.setRoomId(chatMessageDTO.getRoomId());
        entity.setUserId(chatMessageDTO.getMessageSendUserId());

        chatUserRoomDAO.save(entity);

        ChatRoomEntity toBeUpdate = chatRoomDAO.findByRoomId(chatMessageDTO.getRoomId());
        toBeUpdate.setRoomCurUserNum(toBeUpdate.getRoomCurUserNum()+1);

        chatRoomDAO.save(toBeUpdate);
    }

    @Transactional
    public void saveMessage(ChatMessageDTO chatMessageDTO){
        chatMessageDAO.save(ChatMessageDTO.convertDtoToEntity(chatMessageDTO));
    }

    @Transactional
    public void disconnectRoom(ChatMessageDTO chatMessageDTO){
        ChatRoomEntity toBeUpdate = chatRoomDAO.findByRoomId(chatMessageDTO.getRoomId());
        toBeUpdate.setRoomCurUserNum(toBeUpdate.getRoomCurUserNum()-1);

        chatRoomDAO.save(toBeUpdate);

        List<ChatUserRoomEntity> list = chatUserRoomDAO.findAllByRoomIdAndUserId(chatMessageDTO.getRoomId(), chatMessageDTO.getMessageSendUserId());
        chatUserRoomDAO.deleteAll(list);
    }
}
