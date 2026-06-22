package com.example.forum.api.model.vo.chat;

import lombok.Data;

import java.io.Serializable;

/**
 * AI 聊天响应 DTO
 * 支持流式和非流式两种模式
 */
@Data
public class AiChatResponse implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 会话 ID
     */
    private String sessionId;

    /**
     * AI 模型类型
     */
    private String model;

    /**
     * 回复内容（非流式模式下的完整回复册"
     */
    private String content;

    /**
     * 是否为流式响应的最后一册"
     */
    private Boolean finished;

    /**
     * 输入 Token 数:
     */
    private Integer promptTokens;

    /**
     * 输出 Token 数:
     */
    private Integer completionTokens;

    /**
     * 册"Token 数:
     */
    private Integer totalTokens;

    /**
     * 响应耗时（毫秒）
     */
    private Long costTime;

    /**
     * 错误信息（非 null 表示调用失败册"
     */
    private String errorMessage;

    public static AiChatResponse success(String content) {
        AiChatResponse response = new AiChatResponse();
        response.setContent(content);
        response.setFinished(true);
        return response;
    }

    public static AiChatResponse error(String errorMessage) {
        AiChatResponse response = new AiChatResponse();
        response.setErrorMessage(errorMessage);
        response.setFinished(true);
        return response;
    }

    public static AiChatResponse streamChunk(String content) {
        AiChatResponse response = new AiChatResponse();
        response.setContent(content);
        response.setFinished(false);
        return response;
    }
}


