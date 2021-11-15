package com.awesome.controllers.api.v1;

import com.awesome.domains.chatroom.dtos.ChatMessageDTO;
import com.awesome.domains.chatroom.services.ChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class ChatController {
    private final SimpMessagingTemplate template;
    private final ChatService chatService;

    @MessageMapping("/chat/enter")
    public void enter(ChatMessageDTO message){
        chatService.addUserToRoom(message);
        message.setMessage(message.getMessageSendUserId() + "님이 채팅방에 참여하였습니다.");
        template.convertAndSend("/sub/chat/room/" + message.getRoomId(), message);
    }

    @MessageMapping("/chat/message")
    public void message(ChatMessageDTO message){
        chatService.saveMessage(message);
        template.convertAndSend("/sub/chat/room/" + message.getRoomId(), message);
    }

    @MessageMapping("/chat/disconnect")
    public void disconnect(ChatMessageDTO message){
        chatService.disconnectRoom(message);
        message.setMessage(message.getMessageSendUserId() + "님이 채팅방에서 나갔습니다.");
        template.convertAndSend("/sub/chat/room/" + message.getRoomId(), message);
    }
}