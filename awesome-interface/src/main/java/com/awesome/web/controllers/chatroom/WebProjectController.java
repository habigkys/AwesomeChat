package com.awesome.web.controllers.chatroom;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@AllArgsConstructor
@Controller
public class WebProjectController {
    @GetMapping("/chat")
    public String chat(){
        return "chat/chat.html";
    }
}