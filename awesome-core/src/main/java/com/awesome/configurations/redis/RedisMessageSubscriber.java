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

import java.util.Objects;

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
        String chatMessageJson = message.toString();

        try {
            ChatMessageDTO chatMessageDTO = redisObjectMapper.readValue(chatMessageJson, ChatMessageDTO.class);

            if(!Objects.isNull(chatMessageDTO)){
                chatMessageSender.send(chatMessageDTO);
            }
        } catch (Exception e) {
            log.error(e.getMessage());
        }

        log.info("Message Body : {}", message.toString());
    }
}
