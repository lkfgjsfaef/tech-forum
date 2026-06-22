package com.example.forum.service.agent.vo;

import lombok.Data;

@Data
public class ChatRequestVO {

    private Long sessionId;
    private String content;
    private String modelName;
}
