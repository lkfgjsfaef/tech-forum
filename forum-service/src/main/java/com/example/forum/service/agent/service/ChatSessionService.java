package com.example.forum.service.agent.service;

import com.example.forum.service.agent.entity.ChatSession;
import com.example.forum.service.agent.vo.SessionVO;

import java.util.List;

public interface ChatSessionService {

    ChatSession createSession(Long userId, String title, String systemPrompt);

    List<SessionVO> listSessions(Long userId);

    ChatSession getById(Long sessionId);

    void updateTitle(Long sessionId, String title);

    void updateSystemPrompt(Long sessionId, String systemPrompt);

    void deleteSession(Long sessionId);

    List<SessionVO> searchSessions(Long userId, String keyword);
}
