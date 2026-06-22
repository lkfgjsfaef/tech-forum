package com.example.forum.service.agent.service.model;

import com.example.forum.service.agent.entity.ModelConfig;
import com.example.forum.service.agent.mapper.ModelConfigMapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class Glm47Model extends AbstractOpenAiCompatibleModel {

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
        return "glm-4.7";
    }

    @Override
    public String getModelName() {
        return "glm-4.7";
    }

    private ModelConfig getConfig() {
        LambdaQueryWrapper<ModelConfig> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ModelConfig::getModelName, "glm-4.7");
        return modelConfigMapper.selectOne(wrapper);
    }
}
