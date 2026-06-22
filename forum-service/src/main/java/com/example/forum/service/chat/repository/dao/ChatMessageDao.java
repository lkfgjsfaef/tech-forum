package com.example.forum.service.chat.repository.dao;

import com.example.forum.service.chat.entity.ChatMessageDO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ChatMessageDao {
    int insert(ChatMessageDO message);

    int markAsRead(@Param("sessionId") Long sessionId, @Param("toUserId") Long toUserId);

    int countUnread(Long toUserId);

    List<ChatMessageDO> listBySession(@Param("sessionId") Long sessionId, @Param("offset") int offset, @Param("pageSize") int pageSize);
}
