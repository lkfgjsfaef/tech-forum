package com.example.forum.core.ws;

import jakarta.websocket.Session;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;

import java.io.IOException;
import java.util.Map;
import java.util.function.Consumer;

@Slf4j
public class WebSocketResponseUtil {
    public static void sendResponse(Session session, Object response) {
        if (session == null || !session.isOpen()) {
            return;
        }
        try {
            session.getBasicRemote().sendText(response.toString());
        } catch (IOException e) {
            log.error("WebSocket发送消息失败", e);
        }
    }

    public static void sendText(Session session, String message) {
        if (session == null || !session.isOpen()) {
            return;
        }
        try {
            session.getBasicRemote().sendText(message);
        } catch (IOException e) {
            log.error("WebSocket发送消息失败", e);
        }
    }

    public static void sendMsgToUser(String session, String destination, Object response) {
        log.info("Sending message to user {} at {}: {}", session, destination, response);
    }

    public static void broadcastMsg(String topic, String message) {
        log.info("Broadcasting message to topic {}: {}", topic, message);
    }

    public static void execute(SimpMessageHeaderAccessor accessor, Runnable func) {
        try {
            Map<String, Object> attrs = accessor.getSessionAttributes();
            if (attrs != null) {
                func.run();
            }
        } catch (Exception e) {
            log.error("WebSocket execute error", e);
        }
    }
}


