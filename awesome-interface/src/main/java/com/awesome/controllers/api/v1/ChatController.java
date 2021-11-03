package com.awesome.controllers.api.v1;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.bind.annotation.*;

@AllArgsConstructor
@RestController("/api/v1/chat")
public class ChatController {

    @GetMapping("/create")
    public String createChatRoom() {
        return null;
    }

    @GetMapping("/send")
    public String sendMsg() {
        return null;
    }

    @GetMapping("/{id}")
    public String joinChatRoom(@PathVariable Long id) {
        return null;
    }

    @Getter
    @Setter
    public static class ChatDTO {
        private Long id;

        private String userId;

        private String message;
    }
}