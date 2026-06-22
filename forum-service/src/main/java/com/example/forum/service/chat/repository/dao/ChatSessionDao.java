package com.example.forum.service.chat.repository.dao;

import com.example.forum.service.chat.entity.ChatSessionDO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ChatSessionDao {
    int insert(ChatSessionDO session);

    int updateById(ChatSessionDO session);

    ChatSessionDO findByUserPair(@Param("user1Id") Long user1Id, @Param("user2Id") Long user2Id);

    List<ChatSessionDO> listByUserId(@Param("userId") Long userId);

    ChatSessionDO findById(Long id);
}
