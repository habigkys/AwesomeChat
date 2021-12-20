package com.awesome.api.controllers.chatroom.v1;

import com.awesome.domains.chatroom.ChatRoom;
import com.awesome.domains.chatroom.ChatRoomRepository;
import com.awesome.domains.chatroom.ChatRoomService;
import com.awesome.infrastructures.redis.RedisClient;
import com.awesome.infrastructures.shared.chatroom.ChatMessageDTO;
import com.awesome.infrastructures.shared.chatroom.WebSocketDefaultServiceUser;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.stereotype.Controller;

import java.util.Map;

@Controller
@RequiredArgsConstructor
public class ChatController {
    private final ChatRoomRepository chatRoomRepository;
    private final ChatRoomService chatRoomService;
    private final WebSocketDefaultServiceUser webSocketDefaultServiceUser;

    @Autowired
    private RedisClient redisClient;

    @Autowired
    private ChannelTopic topic;

    @MessageMapping("/chat/enter")
    public void enter(ChatMessageDTO message, SimpMessageHeaderAccessor headerAccessor) throws Exception{
        Map<String,Object> headerAttribute = headerAccessor.getSessionAttributes();
        message.setMessageSendUserId(webSocketDefaultServiceUser.getUuid());
        message.setMessageSendUserName(webSocketDefaultServiceUser.getName());

        ChatRoom chatRoom = chatRoomService.enterRoom(message);
        chatRoomRepository.save(chatRoom);
        // 저장 후 publish 구조    todo : redis List, 벌크 저장

        redisClient.publish(topic, chatRoom);
    }

    @MessageMapping("/chat/message")
    public void message(ChatMessageDTO message) throws Exception{
        message.setMessageSendUserId(webSocketDefaultServiceUser.getUuid());
        message.setMessageSendUserName(webSocketDefaultServiceUser.getName());

        ChatRoom chatRoom = chatRoomService.sendMessage(message);
        chatRoomRepository.save(chatRoom);
        // 저장 후 publish 구조    todo : redis List, 벌크 저장

        redisClient.publish(topic, chatRoom);
    }

    @MessageMapping("/chat/disconnect")
    public void disconnect(ChatMessageDTO message) throws Exception{
        message.setMessageSendUserId(webSocketDefaultServiceUser.getUuid());
        message.setMessageSendUserName(webSocketDefaultServiceUser.getName());

        ChatRoom chatRoom = chatRoomService.disconnectRoom(message);
        chatRoomRepository.save(chatRoom);
        chatRoomRepository.removeUser(chatRoom);
        // 저장 후 publish 구조    todo : redis List, 벌크 저장

        redisClient.publish(topic, chatRoom);
    }
}