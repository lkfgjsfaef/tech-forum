package com.example.forum.service.agent.service.model;

import reactor.core.publisher.Flux;

import java.util.List;
import java.util.Map;

public interface AiModel {

    Flux<String> streamChat(List<Map<String, Object>> messages, String modelName);

    String getModelName();
}
