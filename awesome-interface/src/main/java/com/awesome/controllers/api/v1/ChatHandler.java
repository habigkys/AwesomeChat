package com.awesome.controllers.api.v1;

import com.awesome.dtos.ChatMessage;
import com.awesome.dtos.ChatRoom;
import com.awesome.dtos.ChatService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class ChatHandler extends TextWebSocketHandler {
    private final ChatService chatService;
    private final ObjectMapper objectMapper;

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws IOException {
        String payload = message.getPayload();
        System.out.println("payload : " + payload);

        ChatMessage msg = objectMapper.readValue(payload, ChatMessage.class);
        ChatRoom room = chatService.findById(msg.getRoomId());

        room.handleChat(session, msg, chatService);
    }
}
