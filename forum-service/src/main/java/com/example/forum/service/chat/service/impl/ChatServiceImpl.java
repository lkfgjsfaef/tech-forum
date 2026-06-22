package com.example.forum.service.chat.service.impl;

import com.example.forum.api.model.vo.PageListVo;
import com.example.forum.service.chat.entity.ChatMessageDO;
import com.example.forum.service.chat.entity.ChatSessionDO;
import com.example.forum.service.chat.repository.dao.ChatMessageDao;
import com.example.forum.service.chat.repository.dao.ChatSessionDao;
import com.example.forum.service.chat.service.ChatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ChatServiceImpl implements ChatService {

    @Autowired
    private ChatSessionDao chatSessionDao;

    @Autowired
    private ChatMessageDao chatMessageDao;

    @Override
    public List<ChatSessionDO> getUserSessions(Long userId) {
        return chatSessionDao.listByUserId(userId);
    }

    @Override
    public ChatSessionDO getSessionOrCreate(Long user1Id, Long user2Id) {
        Long smaller = Math.min(user1Id, user2Id);
        Long larger = Math.max(user1Id, user2Id);
        ChatSessionDO session = chatSessionDao.findByUserPair(smaller, larger);
        if (session != null) {
            return session;
        }
        session = new ChatSessionDO();
        session.setUser1Id(smaller);
        session.setUser2Id(larger);
        session.setLastMessage("");
        session.setLastMessageTime(LocalDateTime.now());
        session.setUser1Unread(0);
        session.setUser2Unread(0);
        chatSessionDao.insert(session);
        return session;
    }

    @Override
    public PageListVo<ChatMessageDO> getSessionMessages(Long sessionId, int pageNum, int pageSize) {
        List<ChatMessageDO> messages = chatMessageDao.listBySession(sessionId, (pageNum - 1) * pageSize, pageSize);
        long total = messages.size();
        boolean hasMore = messages.size() >= pageSize;
        return PageListVo.of(messages, hasMore ? -1 : total, pageNum, pageSize);
    }

    @Override
    public ChatMessageDO sendMessage(Long fromUserId, Long toUserId, String content) {
        if (content == null || content.trim().isEmpty()) {
            return null;
        }
        content = content.trim();
        if (content.length() > 500) {
            content = content.substring(0, 500);
        }

        ChatSessionDO session = getSessionOrCreate(fromUserId, toUserId);

        ChatMessageDO message = new ChatMessageDO();
        message.setSessionId(session.getId());
        message.setFromUserId(fromUserId);
        message.setToUserId(toUserId);
        message.setContent(content);
        message.setMessageType(0);
        chatMessageDao.insert(message);

        session.setLastMessage(content.length() > 30 ? content.substring(0, 30) + "..." : content);
        session.setLastMessageTime(LocalDateTime.now());
        if (session.getUser1Id().equals(toUserId)) {
            session.setUser1Unread(session.getUser1Unread() + 1);
        } else {
            session.setUser2Unread(session.getUser2Unread() + 1);
        }
        chatSessionDao.updateById(session);

        return message;
    }

    @Override
    public void markSessionAsRead(Long sessionId, Long userId) {
        chatMessageDao.markAsRead(sessionId, userId);
        ChatSessionDO session = chatSessionDao.findById(sessionId);
        if (session != null) {
            if (session.getUser1Id().equals(userId)) {
                session.setUser1Unread(0);
            } else {
                session.setUser2Unread(0);
            }
            chatSessionDao.updateById(session);
        }
    }

    @Override
    public int getUnreadCount(Long userId) {
        return chatMessageDao.countUnread(userId);
    }
}
