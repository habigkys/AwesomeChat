package com.awesome.controllers.web;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@AllArgsConstructor
@Controller
public class WebProjectController {
    @GetMapping("/")
    public String projectList(){
        return "project/projectList.html";
    }

    @GetMapping("/users")
    public String userList(){
        return "user/userList.html";
    }

    @GetMapping("/userChat")
    public String userChat(){
        return "chat/userChat.html";
    }

    @GetMapping("/adminChat")
    public String adminChat(){
        return "chat/adminChat.html";
    }
}