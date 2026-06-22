package com.example.forum.service.agent.service.impl;

import com.example.forum.service.agent.service.CacheService;
import com.example.forum.service.agent.vo.MessageVO;
import com.example.forum.service.agent.vo.SessionVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
@RequiredArgsConstructor
public class CacheServiceImpl implements CacheService {

    private final RedisTemplate<String, Object> agentRedisTemplate;

    private static final String SESSION_KEY = "chat:session:";
    private static final String MESSAGE_KEY = "chat:messages:";
    private static final long CACHE_TTL = 30;

    @Override
    public void cacheSessionList(Long userId, List<SessionVO> sessions) {
        String key = SESSION_KEY + userId;
        agentRedisTemplate.opsForValue().set(key, sessions, CACHE_TTL, TimeUnit.MINUTES);
        log.debug("缓存会话列表: userId={}", userId);
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<SessionVO> getCachedSessionList(Long userId) {
        String key = SESSION_KEY + userId;
        Object obj = agentRedisTemplate.opsForValue().get(key);
        if (obj instanceof List) {
            return (List<SessionVO>) obj;
        }
        return null;
    }

    @Override
    public void cacheMessages(Long sessionId, List<MessageVO> messages) {
        String key = MESSAGE_KEY + sessionId;
        agentRedisTemplate.opsForValue().set(key, messages, CACHE_TTL, TimeUnit.MINUTES);
        log.debug("缓存消息列表: sessionId={}", sessionId);
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<MessageVO> getCachedMessages(Long sessionId) {
        String key = MESSAGE_KEY + sessionId;
        Object obj = agentRedisTemplate.opsForValue().get(key);
        if (obj instanceof List) {
            return (List<MessageVO>) obj;
        }
        return null;
    }

    @Override
    public void evictSession(Long userId) {
        agentRedisTemplate.delete(SESSION_KEY + userId);
    }

    @Override
    public void evictMessages(Long sessionId) {
        agentRedisTemplate.delete(MESSAGE_KEY + sessionId);
    }
}
