package com.example.forum.service.agent.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.example.forum.service.agent.common.constant.Constants;
import com.example.forum.service.agent.entity.ChatMessage;
import com.example.forum.service.agent.entity.ChatSession;
import com.example.forum.service.agent.entity.ChatSummary;
import com.example.forum.service.agent.mapper.ChatSummaryMapper;
import com.example.forum.service.agent.service.AiModelRouterService;
import com.example.forum.service.agent.service.ChatMessageService;
import com.example.forum.service.agent.service.ChatSessionService;
import com.example.forum.service.agent.service.MemoryService;
import com.example.forum.service.agent.service.RerankService;
import com.example.forum.service.agent.service.TokenService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import redis.clients.jedis.UnifiedJedis;
import redis.clients.jedis.search.SearchResult;
import redis.clients.jedis.search.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class MemoryServiceImpl implements MemoryService {

    private final ChatMessageService chatMessageService;
    private final ChatSummaryMapper chatSummaryMapper;
    private final AiModelRouterService aiModelRouterService;
    private final ChatSessionService chatSessionService;
    private final VectorStore vectorStore;
    private final TokenService tokenService;
    
    @Autowired(required = false)
    private RerankService rerankService;
    
    @Autowired(required = false)
    private UnifiedJedis unifiedJedis;
    
    @Value("${spring.ai.vectorstore.redis.index:vector_index}")
    private String redisIndexName;

    @Value("${ai.context.max-rounds:8}")
    private int maxContextRounds;

    @Value("${ai.context.max-length:2000}")
    private int maxContextLength;

    @Value("${ai.summary.enabled:true}")
    private boolean summaryEnabled;

    @Override
    public List<Map<String, Object>> buildContext(Long sessionId) {
        List<Map<String, Object>> context = new ArrayList<>();

        ChatSession session = chatSessionService.getById(sessionId);
        String systemPrompt = "你是一个智能AI助手，请根据上下文和用户的问题给出准确、有用的回答。";
        if (session != null && session.getSystemPrompt() != null && !session.getSystemPrompt().trim().isEmpty()) {
            systemPrompt = session.getSystemPrompt().trim();
        }

        // Use HashMap instead of Map.of to allow mutable map if needed, though immutable is fine for GLM
        // Map.of creates immutable map which is strict about nulls
        Map<String, Object> systemMsg = new HashMap<>();
        systemMsg.put("role", "system");
        systemMsg.put("content", systemPrompt);
        context.add(systemMsg);

        ChatSummary summary = getSummary(sessionId);
        if (summary != null && summary.getSummary() != null && !summary.getSummary().isEmpty()) {
            Map<String, Object> summaryMsg = new HashMap<>();
            summaryMsg.put("role", "system");
            summaryMsg.put("content", "以下是之前对话的摘要，请参考：\n" + summary.getSummary());
            context.add(summaryMsg);
        }

        List<ChatMessage> recentMessages = chatMessageService.getRecentMessages(sessionId, maxContextRounds);
        
        // RAG Retrieval: Extract the latest user question to search VectorStore
        String latestUserQuestion = "";
        for (int i = recentMessages.size() - 1; i >= 0; i--) {
            ChatMessage msg = recentMessages.get(i);
            if (Constants.ROLE_USER.equals(msg.getRole()) && msg.getContent() != null) {
                latestUserQuestion = msg.getContent();
                break;
            }
        }
        
        if (!latestUserQuestion.isEmpty()) {
            try {
                // Perform Hybrid Search: Vector Search + BM25 Keyword Search
                log.info("执行RAG混合检索: query: {}", latestUserQuestion);
                
                Set<String> uniqueContents = new HashSet<>();
                List<String> combinedResults = new ArrayList<>();
                
                // 1. Vector Search (Semantic) - 召回更多数据用于重排
                List<Document> similarDocs = null;
                try {
                    similarDocs = vectorStore.similaritySearch(
                        SearchRequest.query(latestUserQuestion).withTopK(10)
                    );
                } catch (Exception ve) {
                    log.warn("向量检索失败: {}", ve.getMessage());
                }
                
                if (similarDocs != null) {
                    for (Document doc : similarDocs) {
                        if (doc.getContent() != null && uniqueContents.add(doc.getContent())) {
                            combinedResults.add(doc.getContent());
                        }
                    }
                }
                
                // 2. BM25 Keyword Search (Literal) using RediSearch FT.SEARCH
                if (unifiedJedis != null) {
                    try {
                        // Escape punctuation for RediSearch query syntax
                        String escapedQuery = latestUserQuestion.replaceAll("([^a-zA-Z0-9\\u4e00-\\u9fa5])", "\\\\$1");
                        Query q = new Query(escapedQuery).limit(0, 10);
                        SearchResult searchResult = unifiedJedis.ftSearch(redisIndexName, q);
                        
                        if (searchResult != null && searchResult.getDocuments() != null) {
                            for (redis.clients.jedis.search.Document doc : searchResult.getDocuments()) {
                                String content = doc.getString("content");
                                if (content != null && uniqueContents.add(content)) {
                                    combinedResults.add(content);
                                }
                            }
                        }
                    } catch (Exception e) {
                        log.warn("BM25 关键字检索失败(可能索引不存在或语法错误): {}", e.getMessage());
                    }
                }
                
                if (!combinedResults.isEmpty()) {
                    // 3. Rerank (重排)
                    List<String> finalResults = combinedResults;
                    if (rerankService != null) {
                        finalResults = rerankService.rerank(latestUserQuestion, combinedResults, 3);
                    } else {
                        // 降级：截取前 3 条
                        finalResults = combinedResults.stream().limit(3).collect(Collectors.toList());
                    }
                    
                    StringBuilder ragContext = new StringBuilder("你是一个拥有外部知识库的AI助手。如果以下检索到的知识片段与用户问题相关，请结合这些知识回答问题。如果不相关，请忽略它们。\n\n[知识库检索结果开始]\n");
                    for (int i = 0; i < finalResults.size(); i++) {
                        ragContext.append(String.format("--- 片段 %d ---\n%s\n\n", i + 1, finalResults.get(i)));
                    }
                    ragContext.append("[知识库检索结果结束]\n");
                    
                    Map<String, Object> ragSysMsg = new HashMap<>();
                    ragSysMsg.put("role", "system");
                    ragSysMsg.put("content", ragContext.toString());
                    context.add(ragSysMsg);
                    log.info("RAG混合检索命中 {} 个相关片段, 重排后保留 {} 个", combinedResults.size(), finalResults.size());
                }
            } catch (Exception e) {
                log.warn("RAG向量检索失败: {}", e.getMessage());
            }
        }

        for (ChatMessage msg : recentMessages) {
            Map<String, Object> userMsg = new HashMap<>();
            userMsg.put("role", msg.getRole());
            userMsg.put("content", msg.getContent());
            context.add(userMsg);
        }
        
        // 5. 应用 Token 精算与滑动窗口截断机制
        // 假设大模型的最大上下文 Token 限制为 8000（预留 1000 给生成的回答，所以输入限制 7000）
        context = tokenService.truncateContext(context, 7000);

        return context;
    }

    @Override
    public void saveUserMessage(Long sessionId, String content) {
        chatMessageService.saveMessage(sessionId, Constants.ROLE_USER, content);
    }

    @Override
    public void saveAssistantMessage(Long sessionId, String content) {
        chatMessageService.saveMessage(sessionId, Constants.ROLE_ASSISTANT, content);
    }

    @Override
    public void triggerSummaryIfNeeded(Long sessionId) {
        if (!summaryEnabled) {
            return;
        }

        int messageCount = chatMessageService.countBySessionId(sessionId);
        long totalLength = chatMessageService.getTotalContentLength(sessionId);

        if (messageCount > maxContextRounds * 2 || totalLength > maxContextLength) {
            log.info("触发自动摘要: sessionId={}, 消息数={}, 总长度={}", sessionId, messageCount, totalLength);
            generateSummary(sessionId);
        }
    }

    private void generateSummary(Long sessionId) {
        try {
            List<ChatMessage> allMessages = chatMessageService.getRecentMessages(sessionId, 50);
            StringBuilder sb = new StringBuilder();
            for (ChatMessage msg : allMessages) {
                sb.append(msg.getRole()).append(": ").append(msg.getContent()).append("\n");
            }

            String summaryPrompt = "请对以下对话进行精简摘要，保留用户需求、关键信息和结论，不要遗漏重要细节：\n\n" + sb;

            Map<String, Object> sysMap = new HashMap<>();
            sysMap.put("role", "system");
            sysMap.put("content", "你是一个对话摘要助手，请精简准确地总结对话内容。");
            
            Map<String, Object> userMap = new HashMap<>();
            userMap.put("role", "user");
            userMap.put("content", summaryPrompt);

            List<Map<String, Object>> messages = new ArrayList<>();
            messages.add(sysMap);
            messages.add(userMap);

            StringBuilder summaryBuilder = new StringBuilder();
            aiModelRouterService.streamChat(null, messages)
                    .doOnNext(summaryBuilder::append)
                    .doOnComplete(() -> {
                        String summaryText = summaryBuilder.toString();
                        log.info("摘要生成完成，长度: {}", summaryText.length());
                        updateSummary(sessionId, summaryText);
                    })
                    .doOnError(e -> log.error("摘要生成失败: {}", e.getMessage()))
                    .subscribe();
        } catch (Exception e) {
            log.error("触发摘要异常: {}", e.getMessage());
        }
    }

    @Override
    public ChatSummary getSummary(Long sessionId) {
        LambdaQueryWrapper<ChatSummary> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ChatSummary::getSessionId, sessionId);
        return chatSummaryMapper.selectOne(wrapper);
    }

    @Override
    public void updateSummary(Long sessionId, String summary) {
        ChatSummary existing = getSummary(sessionId);
        if (existing != null) {
            existing.setSummary(summary);
            chatSummaryMapper.updateById(existing);
        } else {
            ChatSummary newSummary = new ChatSummary();
            newSummary.setSessionId(sessionId);
            newSummary.setSummary(summary);
            chatSummaryMapper.insert(newSummary);
        }
    }
}



