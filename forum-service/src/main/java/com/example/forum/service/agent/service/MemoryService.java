package com.example.forum.service.agent.service;

import com.example.forum.service.agent.entity.ChatSummary;

import java.util.List;
import java.util.Map;

public interface MemoryService {

    List<Map<String, Object>> buildContext(Long sessionId);

    void saveUserMessage(Long sessionId, String content);

    void saveAssistantMessage(Long sessionId, String content);

    void triggerSummaryIfNeeded(Long sessionId);

    ChatSummary getSummary(Long sessionId);

    void updateSummary(Long sessionId, String summary);
}
