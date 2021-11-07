package com.awesome.controllers.api.v1;

import com.awesome.dtos.ChatMessage;
import com.awesome.dtos.ChatRoom;
import com.awesome.dtos.ChatService;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/chat")
public class ChatController {
    private final ChatService chatService;

    @PostMapping
    public ChatRoom doChat(@RequestBody ChatMessage chatMessage) {
        return chatService.doChat(chatMessage.getRoomName());
    }

    @GetMapping("/chats")
    public List<ChatRoom> findAllRoom() {
        return chatService.findAllRoom();
    }
}