package com.example.forum.service.article.controller;

import com.example.forum.service.article.entity.Article;
import com.example.forum.service.article.service.ArticleService;
import com.example.forum.service.user.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import jakarta.servlet.http.HttpSession;

import java.util.List;

@Controller
@RequestMapping("/legacy/article")
public class ArticleController {

    @Autowired
    private ArticleService articleService;

    @GetMapping("/list")
    public ModelAndView list(ModelAndView modelAndView) {
        List<Article> articles = articleService.findAll();
        modelAndView.addObject("articles", articles);
        modelAndView.setViewName("article/list");
        return modelAndView;
    }

    @GetMapping("/detail/{id}")
    public ModelAndView detail(@PathVariable("id") Long id, ModelAndView modelAndView) {
        Article article = articleService.findById(id);
        articleService.incrementViewCount(id);
        modelAndView.addObject("article", article);
        modelAndView.setViewName("article/detail");
        return modelAndView;
    }

    @GetMapping("/create")
    public ModelAndView create(HttpSession session, ModelAndView modelAndView) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            modelAndView.setViewName("redirect:/user/login");
            return modelAndView;
        }
        modelAndView.setViewName("article/create");
        return modelAndView;
    }

    @PostMapping("/create")
    public ModelAndView create(Article article, HttpSession session, ModelAndView modelAndView) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            modelAndView.setViewName("redirect:/user/login");
            return modelAndView;
        }
        article.setUserId(user.getId());
        articleService.create(article);
        modelAndView.setViewName("redirect:/article/list");
        return modelAndView;
    }

    @GetMapping("/edit/{id}")
    public ModelAndView edit(@PathVariable("id") Long id, HttpSession session, ModelAndView modelAndView) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            modelAndView.setViewName("redirect:/user/login");
            return modelAndView;
        }
        Article article = articleService.findById(id);
        if (!article.getUserId().equals(user.getId())) {
            modelAndView.setViewName("redirect:/article/list");
            return modelAndView;
        }
        modelAndView.addObject("article", article);
        modelAndView.setViewName("article/edit");
        return modelAndView;
    }

    @PostMapping("/update")
    public ModelAndView update(Article article, HttpSession session, ModelAndView modelAndView) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            modelAndView.setViewName("redirect:/user/login");
            return modelAndView;
        }
        Article existingArticle = articleService.findById(article.getId());
        if (!existingArticle.getUserId().equals(user.getId())) {
            modelAndView.setViewName("redirect:/article/list");
            return modelAndView;
        }
        articleService.update(article);
        modelAndView.setViewName("redirect:/article/detail/" + article.getId());
        return modelAndView;
    }

    @GetMapping("/my")
    public ModelAndView my(HttpSession session, ModelAndView modelAndView) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            modelAndView.setViewName("redirect:/user/login");
            return modelAndView;
        }
        List<Article> articles = articleService.findByUserId(user.getId());
        modelAndView.addObject("articles", articles);
        modelAndView.setViewName("article/my");
        return modelAndView;
    }
}
