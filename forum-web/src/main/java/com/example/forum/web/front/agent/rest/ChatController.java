package com.example.forum.web.front.agent.rest;

import com.example.forum.service.agent.service.CacheService;
import com.example.forum.service.agent.service.ChatSessionService;
import com.example.forum.service.agent.service.MemoryService;
import com.example.forum.service.agent.service.TokenService;

import com.example.forum.service.agent.service.AiModelRouterService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.example.forum.service.agent.common.result.Result;
import jakarta.servlet.http.HttpServletRequest;
import com.example.forum.api.model.context.ReqInfoContext;
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
@RequestMapping("/agent/chat")
@RequiredArgsConstructor
public class ChatController {

    private final AiModelRouterService aiModelRouterService;
    private final MemoryService memoryService;
    private final ChatSessionService chatSessionService;
    private final CacheService cacheService;
    
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

        ScheduledFuture<?> heartbeat = heartbeatExecutor.scheduleAtFixedRate(() -> {
            try {
                emitter.send(SseEmitter.event().comment("heartbeat"));
            } catch (Exception e) {
                log.debug("心跳发送失败，连接可能已关闭");
            }
        }, 15, 15, TimeUnit.SECONDS);

        // 将主线程的请求上下文取出
        final ReqInfoContext.ReqInfo reqInfo = ReqInfoContext.getReqInfo();
        final Long currentUserId = (reqInfo != null) ? reqInfo.getUserId() : null;
        
        CompletableFuture.runAsync(() -> {
            // 在子线程中恢复上下文，防止 ReqInfoContext.getReqInfo() 返回 null
            if (reqInfo != null) {
                ReqInfoContext.addReqInfo(reqInfo);
            }
            try {
                long startTime = System.currentTimeMillis();
                StringBuilder fullResponse = new StringBuilder();
                var disposable = aiModelRouterService.streamChat(modelName, context)
                        .doOnNext(chunk -> {
                            fullResponse.append(chunk);
                            try {
                                emitter.send(SseEmitter.event().data(chunk));
                            } catch (IllegalStateException | IOException e) {
                                // 前端断开连接，忽略异常，不要打满屏堆栈
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
                                
                                // update token
                                if (currentUserId != null) {
                                    int promptTokens = tokenService.countContextTokens(context);
                                    int completionTokens = tokenService.countTokens(fullResponse.toString());
                                    int totalTokens = promptTokens + completionTokens;
                                    log.debug("Tokens 消耗: Prompt={}, Completion={}, Total={}", promptTokens, completionTokens, totalTokens);
                                }
                                
                                emitter.send(SseEmitter.event().data("[DONE]"));
                                emitter.complete();
                                long costTime = System.currentTimeMillis() - startTime;
                                log.info("会话[{}]回答完成，长度: {}，耗时: {}ms", sessionId, fullResponse.length(), costTime);
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
                        
                // 当 SseEmitter 异常或完成时，主动取消底层大模型 API 的请求，释放连接
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
            } finally {
                // 清理子线程上下文，防止内存泄漏
                ReqInfoContext.clear();
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

    @PostMapping("/regenerate")
    public SseEmitter regenerate(@RequestBody Map<String, Object> body, HttpServletRequest request) {
        Long sessionId = Long.valueOf(body.get("sessionId").toString());
        String modelName = body.get("modelName") != null ? body.get("modelName").toString() : null;

        SseEmitter emitter = new SseEmitter(180000L);

        List<Map<String, Object>> context = memoryService.buildContext(sessionId);
        cacheService.evictMessages(sessionId);

        // 将主线程的请求上下文取出
        final ReqInfoContext.ReqInfo reqInfo = ReqInfoContext.getReqInfo();
        final Long currentUserId = (reqInfo != null) ? reqInfo.getUserId() : null;
        
        CompletableFuture.runAsync(() -> {
            // 在子线程中恢复上下文，防止 ReqInfoContext.getReqInfo() 返回 null
            if (reqInfo != null) {
                ReqInfoContext.addReqInfo(reqInfo);
            }
            try {
                long startTime = System.currentTimeMillis();
            StringBuilder fullResponse = new StringBuilder();
            var disposable = aiModelRouterService.streamChat(modelName, context)
                    .doOnNext(chunk -> {
                        fullResponse.append(chunk);
                        try {
                            emitter.send(SseEmitter.event().data(chunk));
                        } catch (IllegalStateException | IOException e) {
                            log.debug("重生成SSE发送被中断: {}", e.getMessage());
                            throw new RuntimeException("CLIENT_DISCONNECTED", e);
                        }
                    })
                    .doOnComplete(() -> {
                        try {
                            memoryService.saveAssistantMessage(sessionId, fullResponse.toString());
                            memoryService.triggerSummaryIfNeeded(sessionId);
                            cacheService.evictMessages(sessionId);
                            
                            Object userIdObj = null;
                            try {
                                if (ReqInfoContext.getReqInfo() != null) {
                                    userIdObj = ReqInfoContext.getReqInfo().getUserId();
                                }
                            } catch (Exception ignored) {}
                            if (userIdObj == null) {
                                userIdObj = currentUserId;
                            }
                            
                            if (userIdObj != null) {
                                int promptTokens = tokenService.countContextTokens(context);
                                int completionTokens = tokenService.countTokens(fullResponse.toString());
                                int totalTokens = promptTokens + completionTokens;
                                }
                            
                            emitter.send(SseEmitter.event().data("[DONE]"));
                            emitter.complete();
                            long costTime = System.currentTimeMillis() - startTime;
                            log.info("会话[{}]重新生成回答完成，长度: {}，耗时: {}ms", sessionId, fullResponse.length(), costTime);
                        } catch (Exception e) {
                                log.error("重新生成完成处理异常: {}", e.getMessage());
                                sendError(emitter, "重新生成保存时发生错误");
                                emitter.completeWithError(e);
                            }
                    })
                    .doOnError(e -> {
                        if ("CLIENT_DISCONNECTED".equals(e.getMessage())) return;
                        log.error("重新生成流式调用异常: {}", e.getMessage());
                        sendError(emitter, "AI重新生成失败: " + e.getMessage());
                        emitter.completeWithError(e);
                    })
                    .subscribe();
                    
                    Runnable cancelTask = () -> {
                        if (!disposable.isDisposed()) disposable.dispose();
                    };
                    emitter.onTimeout(cancelTask);
                    emitter.onError(ex -> cancelTask.run());
                    emitter.onCompletion(cancelTask);
            } catch (Exception e) {
                log.error("重新生成流式调用外层异常", e);
                emitter.completeWithError(e);
            } finally {
                // 清理子线程上下文，防止内存泄漏
                ReqInfoContext.clear();
            }
        }, chatExecutor);

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


