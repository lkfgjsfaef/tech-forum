package com.niit.agent.service.impl;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.niit.agent.common.util.ZhipuAuthUtil;
import com.niit.agent.service.RerankService;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Slf4j
@Service
public class ZhipuRerankServiceImpl implements RerankService {

    @Value("${spring.ai.zhipuai.api-key:}")
    private String apiKey;

    // 智谱官方 BGE-Reranker 模型接口
    private static final String RERANK_URL = "https://open.bigmodel.cn/api/paas/v4/tools";
    
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final OkHttpClient client = new OkHttpClient.Builder()
            .connectTimeout(10, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .build();

    @Override
    public List<String> rerank(String query, List<String> documents, int topK) {
        if (documents == null || documents.isEmpty() || apiKey == null || apiKey.isEmpty()) {
            // 如果没有配置 ApiKey 或文档为空，直接返回原有的截断列表
            return documents.stream().limit(topK).collect(Collectors.toList());
        }

        try {
            // 智谱官方 Rerank V4 接口标准
            String actualUrl = "https://open.bigmodel.cn/api/paas/v4/rerank";
            Map<String, Object> bodyMap = new HashMap<>();
            bodyMap.put("model", "rerank-1"); // 智谱官方唯一的重排模型标识
            bodyMap.put("query", query);
            bodyMap.put("documents", documents);
            bodyMap.put("top_n", Math.min(topK, documents.size())); // 防止 top_n 大于实际文档数导致 400 错误

            String jsonBody = objectMapper.writeValueAsString(bodyMap);
            
            // 智谱原生接口必须使用 JWT 鉴权
            String jwtToken = ZhipuAuthUtil.generateToken(apiKey);
            
            Request request = new Request.Builder()
                    .url(actualUrl)
                    .addHeader("Authorization", "Bearer " + jwtToken)
                    .addHeader("Content-Type", "application/json")
                    .post(RequestBody.create(jsonBody, MediaType.parse("application/json")))
                    .build();

            try (Response response = client.newCall(request).execute()) {
                if (response.isSuccessful() && response.body() != null) {
                    String responseBody = response.body().string();
                    JsonNode rootNode = objectMapper.readTree(responseBody);
                    JsonNode resultsNode = rootNode.get("results");
                    
                    if (resultsNode != null && resultsNode.isArray()) {
                        List<String> rerankedDocs = new ArrayList<>();
                        for (JsonNode node : resultsNode) {
                            int index = node.get("index").asInt();
                            if (index >= 0 && index < documents.size()) {
                                rerankedDocs.add(documents.get(index));
                            }
                        }
                        log.info("Rerank 完成, 从 {} 篇文档中重排提取了 {} 篇", documents.size(), rerankedDocs.size());
                        return rerankedDocs;
                    }
                } else {
                    String errorBody = response.body() != null ? response.body().string() : "empty";
                    log.error("Rerank API 调用失败: HTTP {} - {}", response.code(), errorBody);
                }
            }
        } catch (Exception e) {
            log.error("Rerank 重排处理异常: {}", e.getMessage());
        }
        
        // 降级：如果 Rerank 失败，直接返回原始列表的前 Top K
        return documents.stream().limit(topK).collect(Collectors.toList());
    }
}


