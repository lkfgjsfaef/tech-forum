package com.niit.agent.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.niit.agent.entity.ChatMessage;
import com.niit.agent.mapper.ChatMessageMapper;
import com.niit.agent.service.ChatMessageService;
import com.niit.agent.vo.MessageVO;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ChatMessageServiceImpl implements ChatMessageService {

    private final ChatMessageMapper chatMessageMapper;

    @Override
    public ChatMessage saveMessage(Long sessionId, String role, String content) {
        ChatMessage message = new ChatMessage();
        message.setSessionId(sessionId);
        message.setRole(role);
        message.setContent(content);
        chatMessageMapper.insert(message);
        return message;
    }

    @Override
    public List<MessageVO> listMessages(Long sessionId) {
        LambdaQueryWrapper<ChatMessage> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ChatMessage::getSessionId, sessionId)
                .orderByAsc(ChatMessage::getCreateTime);
        List<ChatMessage> messages = chatMessageMapper.selectList(wrapper);
        return messages.stream().map(m -> {
            MessageVO vo = new MessageVO();
            BeanUtils.copyProperties(m, vo);
            return vo;
        }).collect(Collectors.toList());
    }

    @Override
    public List<ChatMessage> getRecentMessages(Long sessionId, int limit) {
        LambdaQueryWrapper<ChatMessage> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ChatMessage::getSessionId, sessionId)
                .orderByDesc(ChatMessage::getCreateTime)
                .last("LIMIT " + limit);
        List<ChatMessage> messages = chatMessageMapper.selectList(wrapper);
        java.util.Collections.reverse(messages);
        return messages;
    }

    @Override
    public int countBySessionId(Long sessionId) {
        LambdaQueryWrapper<ChatMessage> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ChatMessage::getSessionId, sessionId);
        return Math.toIntExact(chatMessageMapper.selectCount(wrapper));
    }

    @Override
    public long getTotalContentLength(Long sessionId) {
        LambdaQueryWrapper<ChatMessage> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ChatMessage::getSessionId, sessionId);
        List<ChatMessage> messages = chatMessageMapper.selectList(wrapper);
        return messages.stream().mapToLong(m -> m.getContent() != null ? m.getContent().length() : 0).sum();
    }

    @Override
    public void deleteBySessionId(Long sessionId) {
        LambdaQueryWrapper<ChatMessage> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ChatMessage::getSessionId, sessionId);
        chatMessageMapper.delete(wrapper);
    }

    @Override
    public void updateMessage(Long messageId, String content) {
        ChatMessage message = chatMessageMapper.selectById(messageId);
        if (message != null) {
            message.setContent(content);
            chatMessageMapper.updateById(message);
        }
    }

    @Override
    public void deleteAfterMessage(Long sessionId, Long messageId) {
        LambdaQueryWrapper<ChatMessage> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ChatMessage::getSessionId, sessionId)
               .gt(ChatMessage::getId, messageId);
        chatMessageMapper.delete(wrapper);
    }

    @Override
    public void updateFeedback(Long messageId, String feedback) {
        ChatMessage message = chatMessageMapper.selectById(messageId);
        if (message != null) {
            message.setFeedback(feedback);
            chatMessageMapper.updateById(message);
        }
    }

    @Override
    public List<MessageVO> searchMessages(Long userId, String keyword) {
        List<ChatMessage> messages = chatMessageMapper.searchMessages(userId, keyword);
        return messages.stream().map(m -> {
            MessageVO vo = new MessageVO();
            BeanUtils.copyProperties(m, vo);
            return vo;
        }).collect(Collectors.toList());
    }
}
