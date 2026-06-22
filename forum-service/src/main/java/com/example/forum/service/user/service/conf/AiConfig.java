package com.example.forum.service.user.service.conf;

import com.example.forum.api.model.enums.ai.AISourceEnum;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
@Component
@ConfigurationProperties(prefix = "ai")
public class AiConfig {
    private List<AISourceEnum> source;
    private String defaultSource;

    /**
     * 各AI源的API Key配置
     * 例如: ai.keys.glm-4-5=sk-xxx, ai.keys.gpt-4-1=github_pat_xxx
     */
    private Map<String, String> keys = new HashMap<>();

    /**
     * 各AI源的API端点配置 (可选，有默认值"
     */
    private Map<String, String> endpoints = new HashMap<>();

    /**
     * 获取指定AI源对应的API Key
     */
    public String getApiKey(AISourceEnum source) {
        if (source == null) return null;
        return keys.get(source.name().toLowerCase());
    }

    /**
     * 获取指定AI源对应的API端点
     */
    public String getEndpoint(AISourceEnum source) {
        if (source == null) return null;
        return endpoints.get(source.name().toLowerCase());
    }
}


