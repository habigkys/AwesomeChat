package com.awesome.dtos;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.socket.WebSocketSession;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
public class ChatRoom {
    private String roomId;
    private Set<WebSocketSession> sessions = new HashSet<>();

    @Builder
    public ChatRoom(String roomId){
        this.roomId = roomId;
    }

    public void handleChat(WebSocketSession session, ChatMessage message, ChatService chatService){
        if(ChatMessage.MessageType.ENTER.equals(message.getMessageType())){
            sessions.add(session);
            message.setMessage(message.getUser() + " 님이 Join함");
        }

        System.out.println(message.getMessage());
        sendMessage(message, chatService);
    }

    public <T> void sendMessage(T message, ChatService chatService){
        sessions.parallelStream().forEach(session->chatService.sendMessage(session, message));
    }
}
