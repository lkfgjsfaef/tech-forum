package com.example.forum.service.agent.service.model;

import com.example.forum.service.agent.entity.ModelConfig;
import com.example.forum.service.agent.mapper.ModelConfigMapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class OpenAiModel extends AbstractOpenAiCompatibleModel {

    private final ModelConfigMapper modelConfigMapper;

    @Override
    protected String getEndpoint() {
        return getConfig().getEndpoint();
    }

    @Override
    protected String getApiKey() {
        return getConfig().getApiKey();
    }

    @Override
    protected String getActualModelName(String modelName) {
        return "gpt-3.5-turbo";
    }

    @Override
    public String getModelName() {
        return "openai";
    }

    private ModelConfig getConfig() {
        LambdaQueryWrapper<ModelConfig> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ModelConfig::getModelName, "openai");
        return modelConfigMapper.selectOne(wrapper);
    }
}
