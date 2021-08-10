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
        return "project/list.html";
    }

    @GetMapping("/update")
    public String projectCreate(@PathVariable("projectId") Long projectId, Model model){
        model.addAttribute("projectId", projectId);
        return "project/update.html";
    }
}