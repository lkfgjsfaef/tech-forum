package com.example.forum.web.front.login.view;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * 登录页面控制册"
 */
@Controller
public class LoginViewController {

    @GetMapping("/login")
    public String loginPage() {
        return "views/login/index";
    }
}

