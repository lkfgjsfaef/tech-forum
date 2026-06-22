package com.example.forum.api.model.vo.chat;

import lombok.Data;

import java.io.Serializable;

@Data
public class ChatSessionItemVo implements Serializable {
    private static final long serialVersionUID = 1L;

    private String chatId;
    private String title;
    private Long createTime;
    private Long updateTime;
}
