package com.example.forum.web.front.agent.rest;

import com.example.forum.service.agent.common.result.Result;
import com.example.forum.service.agent.entity.ModelConfig;
import com.example.forum.service.agent.mapper.ModelConfigMapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/agent/model")
@RequiredArgsConstructor
public class ModelConfigController {

    private final ModelConfigMapper modelConfigMapper;

    @GetMapping("/list")
    public Result<List<ModelConfig>> list() {
        LambdaQueryWrapper<ModelConfig> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ModelConfig::getStatus, 1);
        List<ModelConfig> configs = modelConfigMapper.selectList(wrapper);
        configs.forEach(c -> c.setApiKey(null));
        return Result.ok(configs);
    }

    @PostMapping("/update")
    public Result<Void> update(@RequestBody ModelConfig config) {
        modelConfigMapper.updateById(config);
        return Result.ok();
    }
}
