package com.example.forum.web.front.chat.view;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(path = "chat")
public class ChatHomeViewController {

    @GetMapping
    public String chatPage() {
        return "views/chat-home/index";
    }
}
