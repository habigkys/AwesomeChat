package com.awesome.controllers.api.v1;

import com.awesome.domains.chatroom.ChatRoom;
import com.awesome.domains.chatroom.ChatRoomRepository;
import com.awesome.domains.chatroom.ChatRoomService;
import com.awesome.infrastructures.redis.RedisClient;
import com.awesome.infrastructures.shared.chatroom.ChatMessageDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class ChatController {
    private final ChatRoomRepository chatRoomRepository;
    private final ChatRoomService chatRoomService;

    @Autowired
    private final RedisClient redisClient;

    @Autowired
    private ChannelTopic topic;

    @MessageMapping("/chat/enter")
    public void enter(ChatMessageDTO message) throws Exception{
        ChatRoom chatRoom = chatRoomService.enterRoom(message);
        chatRoomRepository.save(chatRoom);
        // 저장 후 publish 구조    todo : redis List, 벌크 저장

        redisClient.publish(topic, chatRoom);
    }

    @MessageMapping("/chat/message")
    public void message(ChatMessageDTO message) throws Exception{
        ChatRoom chatRoom = chatRoomService.sendMessage(message);
        chatRoomRepository.save(chatRoom);
        // 저장 후 publish 구조    todo : redis List, 벌크 저장

        redisClient.publish(topic, chatRoom);
    }

    @MessageMapping("/chat/disconnect")
    public void disconnect(ChatMessageDTO message) throws Exception{
        ChatRoom chatRoom = chatRoomService.disconnectRoom(message);
        chatRoomRepository.save(chatRoom);
        chatRoomRepository.removeUser(chatRoom);
        // 저장 후 publish 구조    todo : redis List, 벌크 저장

        redisClient.publish(topic, chatRoom);
    }
}