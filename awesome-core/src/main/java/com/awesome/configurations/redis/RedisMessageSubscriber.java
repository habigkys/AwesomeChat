package com.awesome.configurations.redis;

import com.awesome.domains.chatroom.ChatRoom;
import com.awesome.domains.chatroom.entities.ChatRoomMessageEntity;
import com.awesome.infrastructures.chat.ChatMessageSender;
import com.awesome.infrastructures.shared.chatroom.ChatMessageDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class RedisMessageSubscriber implements MessageListener {
    private final ObjectMapper redisObjectMapper;
    private final ChatMessageSender chatMessageSender;

    /**
     * Redis에서 메시지가 발행(publish)되면 대기하고 있던 onMessage가 해당 메시지를 받아 처리한다.
     */
    @Override
    public void onMessage(Message message, byte[] pattern) {
        String chatRoomJson = message.toString();

        try {
            ChatRoom chatRoom = redisObjectMapper.readValue(chatRoomJson, ChatRoom.class);

            ChatRoomMessageEntity chatMessage = chatRoom.getChatRoomMessageEntities().get(0);
            chatMessageSender.send(ChatMessageDTO.convertEntityToDto(chatMessage));
        } catch (Exception e) {
            log.error(e.getMessage());
        }

        log.info("Message Body : {}", message.toString());
    }
}
