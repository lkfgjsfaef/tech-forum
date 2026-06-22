package com.example.forum.service.agent.service;

import com.example.forum.service.agent.entity.ChatMessage;
import com.example.forum.service.agent.vo.MessageVO;

import java.util.List;

public interface ChatMessageService {

    ChatMessage saveMessage(Long sessionId, String role, String content);

    List<MessageVO> listMessages(Long sessionId);

    List<ChatMessage> getRecentMessages(Long sessionId, int limit);

    int countBySessionId(Long sessionId);

    long getTotalContentLength(Long sessionId);

    void deleteBySessionId(Long sessionId);

    void updateMessage(Long messageId, String content);

    void deleteAfterMessage(Long sessionId, Long messageId);

    void updateFeedback(Long messageId, String feedback);

    List<MessageVO> searchMessages(Long userId, String keyword);
}
