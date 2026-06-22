package com.example.forum.service.tag.controller;

import com.example.forum.service.tag.entity.Tag;
import com.example.forum.service.tag.service.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Controller
@RequestMapping("/tag")
public class TagController {

    @Autowired
    private TagService tagService;

    @GetMapping("/list")
    public ModelAndView list(ModelAndView modelAndView) {
        List<Tag> tags = tagService.findAll();
        modelAndView.addObject("tags", tags);
        modelAndView.setViewName("tag/list");
        return modelAndView;
    }
}
