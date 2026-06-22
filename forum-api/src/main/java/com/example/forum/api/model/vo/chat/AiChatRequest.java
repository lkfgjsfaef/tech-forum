package com.example.forum.api.model.vo.chat;

import lombok.Data;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * AI 聊天请求 DTO
 * 支持多种 AI 模型的统一请求格式
 */
@Data
public class AiChatRequest implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * AI 模型类型，对册"AISourceEnum
     */
    private String model;

    /**
     * 用户消息
     */
    private String message;

    /**
     * 会话 ID，用于上下文管理
     */
    private String sessionId;

    /**
     * 对话历史（可选，用于多轮对话册"
     */
    private List<MessageItem> history;

    /**
     * 系统提示词（可选）
     */
    private String systemPrompt;

    /**
     * 温度参数，控制回复的随机册"(0.0 - 1.0)
     */
    private Double temperature;

    /**
     * 最册"Token 数:
     */
    private Integer maxTokens;

    /**
     * 图片数据（Base64编码，用于视觉模型如GLM-4.6V册"
     * 格式: data:image/jpeg;base64,/9j/4AAQ...
     */
    private String image;

    /**
     * 图片原始文件名（用于日志和显示）
     */
    private String imageFileName;

    /**
     * 额外参数
     */
    private Map<String, Object> extra;

    @Data
    public static class MessageItem implements Serializable {
        private static final long serialVersionUID = 1L;

        /**
         * 角色：user / assistant / system
         */
        private String role;

        /**
         * 消息内容
         */
        private String content;

        public static MessageItem user(String content) {
            MessageItem item = new MessageItem();
            item.setRole("user");
            item.setContent(content);
            return item;
        }

        public static MessageItem assistant(String content) {
            MessageItem item = new MessageItem();
            item.setRole("assistant");
            item.setContent(content);
            return item;
        }

        public static MessageItem system(String content) {
            MessageItem item = new MessageItem();
            item.setRole("system");
            item.setContent(content);
            return item;
        }
    }
}


