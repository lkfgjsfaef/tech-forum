package com.niit.agent.service.impl;

import com.knuddels.jtokkit.Encodings;
import com.knuddels.jtokkit.api.Encoding;
import com.knuddels.jtokkit.api.EncodingRegistry;
import com.knuddels.jtokkit.api.EncodingType;
import com.niit.agent.service.TokenService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class TokenServiceImpl implements TokenService {

    private final EncodingRegistry registry = Encodings.newDefaultEncodingRegistry();
    // 使用 CL100K_BASE 编码，适用于 GPT-4, GPT-3.5, GLM-4 等主流大模型
    private final Encoding encoding = registry.getEncoding(EncodingType.CL100K_BASE);

    @Override
    public int countTokens(String text) {
        if (text == null || text.isEmpty()) {
            return 0;
        }
        try {
            return encoding.countTokens(text);
        } catch (Exception e) {
            log.warn("Token 计算失败, 降级使用估算: {}", e.getMessage());
            return (int) Math.ceil(text.length() * 1.5);
        }
    }

    @Override
    public int countContextTokens(List<Map<String, Object>> messages) {
        if (messages == null || messages.isEmpty()) {
            return 0;
        }
        // 计算总Token数，由于 List<Map> 中有的可能是字符串，有的是 Object，我们需要详细累加
        int totalTokens = 0;
        for (Map<String, Object> msg : messages) {
            totalTokens += 4; // role 和换行等基础开销
            
            Object contentObj = msg.get("content");
            if (contentObj instanceof String) {
                totalTokens += countTokens((String) contentObj);
            }
            
            Object toolCallsObj = msg.get("tool_calls");
            if (toolCallsObj instanceof List) {
                totalTokens += ((List<?>) toolCallsObj).size() * 20; 
            }
        }
        totalTokens += 3; // 助手回复的基础开销
        return totalTokens;
    }

    @Override
    public List<Map<String, Object>> truncateContext(List<Map<String, Object>> messages, int maxTokens) {
        int currentTokens = countContextTokens(messages);
        if (currentTokens <= maxTokens) {
            return messages;
        }

        log.info("当前上下文 Token 数 [{}] 超过最大限制 [{}], 执行滑动窗口截断", currentTokens, maxTokens);
        
        List<Map<String, Object>> systemMessages = new ArrayList<>();
        List<Map<String, Object>> historyMessages = new ArrayList<>();
        List<Map<String, Object>> toolMessages = new ArrayList<>(); // 尽量保留最近的工具交互
        
        // 分离系统提示词和历史消息
        for (Map<String, Object> msg : messages) {
            String role = (String) msg.get("role");
            if ("system".equals(role)) {
                systemMessages.add(msg);
            } else if ("tool".equals(role)) {
                toolMessages.add(msg);
            } else {
                historyMessages.add(msg);
            }
        }
        
        // 计算必须保留的部分 (system + tool) 的 token
        int requiredTokens = countContextTokens(systemMessages) + countContextTokens(toolMessages);
        
        // 如果连 system prompt 都超了，那就只能硬截断了（极端情况）
        if (requiredTokens >= maxTokens) {
            log.warn("System/Tool 消息已经超过最大限制, 无法保留历史对话!");
            List<Map<String, Object>> result = new ArrayList<>(systemMessages);
            result.addAll(toolMessages);
            return result;
        }
        
        int availableTokens = maxTokens - requiredTokens;
        List<Map<String, Object>> keptHistory = new ArrayList<>();
        int historyTokens = 0;
        
        // 从最新的历史消息往前推，直到达到可用 token 上限
        for (int i = historyMessages.size() - 1; i >= 0; i--) {
            Map<String, Object> msg = historyMessages.get(i);
            int msgTokens = countContextTokens(List.of(msg));
            if (historyTokens + msgTokens <= availableTokens) {
                keptHistory.add(0, msg); // 插入到头部保持顺序
                historyTokens += msgTokens;
            } else {
                // 遇到第一条超长的，可以根据业务需求选择截断该条消息本身，或者直接丢弃更早的历史
                // 这里为了保证语义完整性，选择直接丢弃更早的消息
                log.debug("从第 {} 条消息开始被截断", i);
                break;
            }
        }
        
        List<Map<String, Object>> finalContext = new ArrayList<>(systemMessages);
        finalContext.addAll(keptHistory);
        finalContext.addAll(toolMessages);
        
        log.info("截断完成, 保留了 {} 条消息, 预计 Token 数: {}", finalContext.size(), countContextTokens(finalContext));
        return finalContext;
    }
}


