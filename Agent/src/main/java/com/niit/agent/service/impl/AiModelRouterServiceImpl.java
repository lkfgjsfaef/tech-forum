package com.niit.agent.service.impl;

import com.niit.agent.service.AiModelRouterService;
import com.niit.agent.service.model.AiModel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
public class AiModelRouterServiceImpl implements AiModelRouterService {

    private final List<AiModel> models;
    private final Map<String, AiModel> modelMap;

    @Value("${ai.default-model:deepseek}")
    private String defaultModel;

    public AiModelRouterServiceImpl(List<AiModel> models) {
        this.models = models;
        this.modelMap = models.stream()
                .collect(Collectors.toMap(AiModel::getModelName, m -> m));
        log.info("已加载AI模型: {}", modelMap.keySet());
    }

    @Override
    public Flux<String> streamChat(String modelName, List<Map<String, Object>> messages) {
        String name = (modelName == null || modelName.isEmpty()) ? defaultModel : modelName;
        AiModel model = modelMap.get(name);
        if (model == null) {
            log.warn("模型[{}]不存在，使用默认模型[{}]", name, defaultModel);
            model = modelMap.get(defaultModel);
        }
        if (model == null) {
            return Flux.error(new RuntimeException("无可用的AI模型"));
        }
        log.info("使用模型: {}", model.getModelName());
        return model.streamChat(messages, name);
    }

    @Override
    public List<String> listAvailableModels() {
        return models.stream().map(AiModel::getModelName).collect(Collectors.toList());
    }
}
