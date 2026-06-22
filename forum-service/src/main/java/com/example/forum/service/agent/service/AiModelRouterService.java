package com.example.forum.service.agent.service;

import com.example.forum.service.agent.service.model.AiModel;
import reactor.core.publisher.Flux;

import java.util.List;
import java.util.Map;

public interface AiModelRouterService {

    Flux<String> streamChat(String modelName, List<Map<String, Object>> messages);

    List<String> listAvailableModels();
}
