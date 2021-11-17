package com.awesome.domains.chatroom.services;

import com.awesome.domains.chatroom.dtos.ChatMessageDTO;
import com.awesome.domains.chatroom.dtos.ChatRoomDTO;
import com.awesome.domains.chatroom.entities.ChatRoomMessageDAO;
import com.awesome.domains.chatroom.entities.ChatRoomDAO;
import com.awesome.domains.chatroom.entities.ChatRoomEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ChatService {
    private final ChatRoomDAO chatRoomDAO;
    private final ChatRoomMessageDAO chatRoomMessageDAO;
}
