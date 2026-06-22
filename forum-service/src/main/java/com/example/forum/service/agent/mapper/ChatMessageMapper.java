package com.example.forum.service.agent.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.forum.service.agent.entity.ChatMessage;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import java.util.List;


public interface ChatMessageMapper extends BaseMapper<ChatMessage> {

    @Select("SELECT m.* FROM chat_message m JOIN chat_session s ON m.session_id = s.id WHERE s.user_id = #{userId} AND m.content LIKE CONCAT('%', #{keyword}, '%') ORDER BY m.create_time DESC")
    List<ChatMessage> searchMessages(@Param("userId") Long userId, @Param("keyword") String keyword);
}
