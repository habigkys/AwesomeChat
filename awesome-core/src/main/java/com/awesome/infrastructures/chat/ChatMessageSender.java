package com.awesome.infrastructures.chat;

import com.awesome.infrastructures.shared.chatroom.ChatMessageDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

@Component
public class ChatMessageSender {
    @Autowired
    private SimpMessagingTemplate template;

    public void send(ChatMessageDTO message){
        template.convertAndSend("/sub/chat/room/" + message.getRoomId(), message);
    }
}
