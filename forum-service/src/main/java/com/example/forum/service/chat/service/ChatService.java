package com.example.forum.service.chat.service;

import com.example.forum.api.model.vo.PageListVo;
import com.example.forum.service.chat.entity.ChatMessageDO;
import com.example.forum.service.chat.entity.ChatSessionDO;
import java.util.List;

public interface ChatService {
    List<ChatSessionDO> getUserSessions(Long userId);

    ChatSessionDO getSessionOrCreate(Long user1Id, Long user2Id);

    PageListVo<ChatMessageDO> getSessionMessages(Long sessionId, int pageNum, int pageSize);

    ChatMessageDO sendMessage(Long fromUserId, Long toUserId, String content);

    void markSessionAsRead(Long sessionId, Long userId);

    int getUnreadCount(Long userId);
}
