package com.example.forum.service.agent.service;

import com.example.forum.service.agent.vo.MessageVO;
import com.example.forum.service.agent.vo.SessionVO;

import java.util.List;

public interface CacheService {

    void cacheSessionList(Long userId, List<SessionVO> sessions);

    List<SessionVO> getCachedSessionList(Long userId);

    void cacheMessages(Long sessionId, List<MessageVO> messages);

    List<MessageVO> getCachedMessages(Long sessionId);

    void evictSession(Long userId);

    void evictMessages(Long sessionId);
}
