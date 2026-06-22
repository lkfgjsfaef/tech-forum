package com.example.forum.web.front.chat.view;

import com.example.forum.web.config.GlobalViewConfig;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import jakarta.annotation.Resource;

@Controller
@RequestMapping(path = "paismart")
public class PaiSmartViewController {
    @Resource
    private GlobalViewConfig globalViewConfig;

    @RequestMapping(path = {"", "/", "home"})
    public String index() {
        return "redirect:" + globalViewConfig.getPaiSmartUrl();
    }
}
