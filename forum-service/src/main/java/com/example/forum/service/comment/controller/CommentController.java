package com.example.forum.service.comment.controller;

import com.example.forum.service.comment.entity.Comment;
import com.example.forum.service.comment.service.CommentService;
import com.example.forum.service.user.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/comment")
public class CommentController {

    @Autowired
    private CommentService commentService;

    @PostMapping("/create")
    public ModelAndView create(@RequestParam("articleId") Long articleId, @RequestParam("content") String content, HttpSession session, ModelAndView modelAndView) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            modelAndView.setViewName("redirect:/user/login");
            return modelAndView;
        }
        Comment comment = new Comment();
        comment.setArticleId(articleId);
        comment.setUserId(user.getId());
        comment.setContent(content);
        commentService.create(comment);
        modelAndView.setViewName("redirect:/article/detail/" + articleId);
        return modelAndView;
    }
}
