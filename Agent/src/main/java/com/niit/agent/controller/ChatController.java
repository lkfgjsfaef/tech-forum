package com.niit.agent.controller;

import com.niit.agent.service.CacheService;
import com.niit.agent.service.ChatSessionService;
import com.niit.agent.service.MemoryService;
import com.niit.agent.service.TokenService;
import com.niit.agent.service.UserService;
import com.niit.agent.service.AiModelRouterService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.niit.agent.common.result.Result;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;

@Slf4j
@RestController
@RequestMapping("/chat")
@RequiredArgsConstructor
public class ChatController {

    private final AiModelRouterService aiModelRouterService;
    private final MemoryService memoryService;
    private final ChatSessionService chatSessionService;
    private final CacheService cacheService;
    private final UserService userService;
    private final TokenService tokenService;
    private final ObjectMapper objectMapper;
    private final ExecutorService chatExecutor;
    private final ScheduledExecutorService heartbeatExecutor = Executors.newScheduledThreadPool(2);

    @GetMapping(value = "/stream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public SseEmitter streamChat(
            @RequestParam(required = false) Long sessionId,
            @RequestParam String content,
            @RequestParam(required = false) String modelName,
            HttpServletRequest request) {

        SseEmitter emitter = new SseEmitter(180000L);

        if (sessionId == null) {
            emitter.completeWithError(new IllegalArgumentException("sessionId is required"));
            return emitter;
        }

        memoryService.saveUserMessage(sessionId, content);
        cacheService.evictMessages(sessionId);

        try {
            var session = chatSessionService.getById(sessionId);
            if (session != null && "新对话".equals(session.getTitle())) {
                String title = content.length() > 20 ? content.substring(0, 20) + "..." : content;
                chatSessionService.updateTitle(sessionId, title);
                cacheService.evictSession(session.getUserId());
            }
        } catch (Exception ignored) {}

        List<Map<String, Object>> context = memoryService.buildContext(sessionId);
        log.info("会话[{}]上下文消息数: {}", sessionId, context.size());

        return handleChatStream(sessionId, modelName, context, request, emitter);
    }

    @PostMapping("/regenerate")
    public SseEmitter regenerate(@RequestBody Map<String, Object> body, HttpServletRequest request) {
        Long sessionId = Long.valueOf(body.get("sessionId").toString());
        String modelName = body.get("modelName") != null ? body.get("modelName").toString() : null;

        SseEmitter emitter = new SseEmitter(180000L);

        List<Map<String, Object>> context = memoryService.buildContext(sessionId);
        cacheService.evictMessages(sessionId);

        return handleChatStream(sessionId, modelName, context, request, emitter);
    }

    private SseEmitter handleChatStream(Long sessionId, String modelName, List<Map<String, Object>> context, HttpServletRequest request, SseEmitter emitter) {
        ScheduledFuture<?> heartbeat = heartbeatExecutor.scheduleAtFixedRate(() -> {
            try {
                emitter.send(SseEmitter.event().comment("heartbeat"));
            } catch (Exception e) {
                log.debug("心跳发送失败，连接可能已关闭");
            }
        }, 15, 15, TimeUnit.SECONDS);

        CompletableFuture.runAsync(() -> {
            long startTime = System.currentTimeMillis();
            StringBuilder fullResponse = new StringBuilder();
            try {
                var disposable = aiModelRouterService.streamChat(modelName, context)
                        .doOnNext(chunk -> {
                            fullResponse.append(chunk);
                            try {
                                emitter.send(SseEmitter.event().data(chunk));
                            } catch (IllegalStateException | IOException e) {
                                log.debug("SSE发送被中断 (可能前端已断开): {}", e.getMessage());
                                throw new RuntimeException("CLIENT_DISCONNECTED", e);
                            }
                        })
                        .doOnComplete(() -> {
                            heartbeat.cancel(true);
                            try {
                                memoryService.saveAssistantMessage(sessionId, fullResponse.toString());
                                memoryService.triggerSummaryIfNeeded(sessionId);
                                cacheService.evictMessages(sessionId);
                                
                                Object userIdObj = request.getAttribute("userId");
                                if (userIdObj != null) {
                                    int promptTokens = tokenService.countContextTokens(context);
                                    int completionTokens = tokenService.countTokens(fullResponse.toString());
                                    int totalTokens = promptTokens + completionTokens;
                                    userService.incrementTokenUsage(Long.parseLong(userIdObj.toString()), totalTokens);
                                    log.debug("Tokens 消耗: Prompt={}, Completion={}, Total={}", promptTokens, completionTokens, totalTokens);
                                }
                                
                                emitter.send(SseEmitter.event().data("[DONE]"));
                                emitter.complete();
                                log.info("会话[{}]回答完成，长度: {}，耗时: {}ms", sessionId, fullResponse.length(), System.currentTimeMillis() - startTime);
                            } catch (Exception e) {
                                log.error("完成处理异常: {}", e.getMessage());
                                sendError(emitter, "对话保存或统计时发生错误");
                                emitter.completeWithError(e);
                            }
                        })
                        .doOnError(e -> {
                            heartbeat.cancel(true);
                            if ("CLIENT_DISCONNECTED".equals(e.getMessage())) {
                                log.info("会话[{}]已中断，提前终止生成", sessionId);
                                return;
                            }
                            log.error("流式调用异常: {}", e.getMessage());
                            sendError(emitter, "AI回复生成失败: " + e.getMessage());
                            emitter.completeWithError(e);
                        })
                        .subscribe();
                        
                Runnable cancelTask = () -> {
                    heartbeat.cancel(true);
                    if (!disposable.isDisposed()) {
                        disposable.dispose();
                        log.debug("主动释放底层的流请求资源: sessionId={}", sessionId);
                    }
                };
                emitter.onTimeout(cancelTask);
                emitter.onError(ex -> cancelTask.run());
                emitter.onCompletion(cancelTask);
                
            } catch (Exception e) {
                heartbeat.cancel(true);
                log.error("创建流式调用异常", e);
                emitter.completeWithError(e);
            }
        }, chatExecutor);

        emitter.onTimeout(() -> {
            heartbeat.cancel(true);
            log.warn("SSE连接超时: sessionId={}", sessionId);
            emitter.complete();
        });

        emitter.onError(ex -> {
            heartbeat.cancel(true);
            log.warn("SSE连接异常: {}", ex.getMessage());
        });

        emitter.onCompletion(() -> heartbeat.cancel(true));

        return emitter;
    }

    private void sendError(SseEmitter emitter, String message) {
        try {
            Result<Void> result = Result.fail(message);
            // 封装为 JSON 格式返回，方便前端统一解析
            emitter.send(SseEmitter.event().data(objectMapper.writeValueAsString(result)));
        } catch (Exception e) {
            log.error("发送 SSE 错误消息失败", e);
        }
    }
}


