package com.niit.agent.service.model;

import com.niit.agent.entity.ModelConfig;
import com.niit.agent.mapper.ModelConfigMapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class Glm46vModel extends AbstractOpenAiCompatibleModel {

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
        return "glm-4.6v";
    }

    @Override
    public String getModelName() {
        return "glm-4.6v";
    }

    private ModelConfig getConfig() {
        LambdaQueryWrapper<ModelConfig> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ModelConfig::getModelName, "glm-4.6v");
        return modelConfigMapper.selectOne(wrapper);
    }
}
