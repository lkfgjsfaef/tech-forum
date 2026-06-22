package com.example.forum.service.user.controller;

import com.example.forum.service.user.entity.User;
import com.example.forum.service.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/register")
    public String register() {
        return "user/register";
    }

    @PostMapping("/register")
    public ModelAndView register(User user, ModelAndView modelAndView) {
        if (userService.findByUsername(user.getUsername()) != null) {
            modelAndView.addObject("error", "用户名已存在");
            modelAndView.setViewName("user/register");
            return modelAndView;
        }
        if (userService.findByEmail(user.getEmail()) != null) {
            modelAndView.addObject("error", "邮箱已存在");
            modelAndView.setViewName("user/register");
            return modelAndView;
        }
        userService.register(user);
        modelAndView.setViewName("redirect:/user/login");
        return modelAndView;
    }

    @GetMapping("/login")
    public String login() {
        return "user/login";
    }

    @PostMapping("/login")
    public ModelAndView login(@RequestParam("username") String username, @RequestParam("password") String password, HttpSession session, ModelAndView modelAndView) {
        User user = userService.login(username, password);
        if (user == null) {
            modelAndView.addObject("error", "用户名或密码错误");
            modelAndView.setViewName("user/login");
            return modelAndView;
        }
        session.setAttribute("user", user);
        modelAndView.setViewName("redirect:/");
        return modelAndView;
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/user/login";
    }

    @GetMapping("/profile")
    public ModelAndView profile(HttpSession session, ModelAndView modelAndView) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            modelAndView.setViewName("redirect:/user/login");
            return modelAndView;
        }
        modelAndView.addObject("user", user);
        modelAndView.setViewName("user/profile");
        return modelAndView;
    }

    @PostMapping("/update")
    public ModelAndView update(User user, HttpSession session, ModelAndView modelAndView) {
        User currentUser = (User) session.getAttribute("user");
        if (currentUser == null) {
            modelAndView.setViewName("redirect:/user/login");
            return modelAndView;
        }
        user.setId(currentUser.getId());
        userService.update(user);
        session.setAttribute("user", userService.findById(currentUser.getId()));
        modelAndView.addObject("success", "更新成功");
        modelAndView.setViewName("user/profile");
        return modelAndView;
    }
}




