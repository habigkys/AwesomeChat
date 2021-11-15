package com.awesome.controllers.web;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@AllArgsConstructor
@Controller
public class WebProjectController {
    @GetMapping("/chat")
    public String chat(){
        return "chat/chat.html";
    }
}