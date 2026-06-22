package com.example.forum.service.chat.entity;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class ChatSessionDO {
    private Long id;
    private Long user1Id;
    private Long user2Id;
    private String lastMessage;
    private LocalDateTime lastMessageTime;
    private Integer user1Unread;
    private Integer user2Unread;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
