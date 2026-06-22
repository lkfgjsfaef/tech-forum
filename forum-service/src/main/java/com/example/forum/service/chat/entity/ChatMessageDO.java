package com.example.forum.service.chat.entity;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class ChatMessageDO {
    private Long id;
    private Long sessionId;
    private Long fromUserId;
    private Long toUserId;
    private String content;
    private Integer messageType;
    private Integer isRead;
    private Integer deleted;
    private LocalDateTime createTime;
}
