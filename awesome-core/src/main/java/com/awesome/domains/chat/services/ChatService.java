package com.awesome.domains.chat.services;

import com.awesome.domains.chat.dtos.ChatRoomDTO;
import com.awesome.domains.chat.entities.ChatRoomDAO;
import com.awesome.domains.chat.entities.ChatRoomEntity;
import com.awesome.domains.project.dtos.ProjectDTO;
import com.awesome.domains.project.entities.ProjectEntity;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ChatService {
    private final ChatRoomDAO chatRoomDAO;

    public List<ChatRoomDTO> findAllRooms(){
        return chatRoomDAO.findAll().stream().map(ChatRoomDTO::convertEntityToDto).collect(Collectors.toList());
    }

    public ChatRoomDTO findRoomById(String roomId){
        return ChatRoomDTO.convertEntityToDto(chatRoomDAO.findById(roomId));
    }

    public ChatRoomDTO createChatRoom(String roomCreatorUserId, String name, Long roomMaxUserNum){
        ChatRoomEntity entity = new ChatRoomEntity();
        entity.setId(UUID.randomUUID().toString());
        entity.setRoomCreatorUserId(roomCreatorUserId);
        entity.setRoomName(name);
        entity.setRoomMaxUserNum(roomMaxUserNum);
        entity.setRoomCurUserNum(0L);

        return ChatRoomDTO.convertEntityToDto(chatRoomDAO.save(entity));
    }
}
