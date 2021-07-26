package com.awesome.controllers;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@AllArgsConstructor
@Controller
public class WebProjectController {
    @GetMapping("/")
    public String projectList(){
        return "project/list.html";
    }

    @GetMapping("/create")
    public String projectCreate(){
        return "project/create.html";
    }
}