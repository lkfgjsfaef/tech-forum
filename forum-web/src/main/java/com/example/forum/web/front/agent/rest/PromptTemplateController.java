package com.example.forum.web.front.agent.rest;

import com.example.forum.service.agent.common.result.Result;
import com.example.forum.service.agent.entity.PromptTemplate;
import com.example.forum.service.agent.mapper.PromptTemplateMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/agent/template")
@RequiredArgsConstructor
public class PromptTemplateController {

    private final PromptTemplateMapper promptTemplateMapper;

    @GetMapping("/list")
    public Result<List<PromptTemplate>> list() {
        return Result.ok(promptTemplateMapper.selectList(null));
    }
}
