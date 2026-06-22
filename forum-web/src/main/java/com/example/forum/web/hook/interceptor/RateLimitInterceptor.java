package com.example.forum.web.hook.interceptor;

import com.example.forum.core.cache.RedisClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Slf4j
@Component
public class RateLimitInterceptor implements HandlerInterceptor {

    private static final int DEFAULT_LIMIT = 60;
    private static final int DEFAULT_WINDOW_SECONDS = 60;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String uri = request.getRequestURI();
        String clientIp = getClientIp(request);

        String key = "rate_limit:" + clientIp + ":" + uri;

        log.info("限流检册 ip={}, uri={}, key={}", clientIp, uri, key);

        // 检册"Redis 连接
        if (RedisClient.getRedisTemplate() == null) {
            log.warn("Redis 未连接，跳过限流检册");
            return true;
        }

        String countStr = RedisClient.get(key);
        int count = countStr == null ? 0 : Integer.parseInt(countStr);

        log.info("限流计数: ip={}, uri={}, 当前次数={}, 限制={}", clientIp, uri, count, DEFAULT_LIMIT);

        if (count >= DEFAULT_LIMIT) {
            log.warn("接口限流触发: ip={}, uri={}, 次数={}", clientIp, uri, count);
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            response.setCharacterEncoding("UTF-8");
            response.setStatus(429);
            response.getWriter().write("{\"status\":{\"code\":429,\"msg\":\"请求过于频繁，请稍后再试\"},\"result\":null}");
            return false;
        }

        if (count == 0) {
            RedisClient.set(key, "1");
            RedisClient.expire(key, DEFAULT_WINDOW_SECONDS);
        } else {
            RedisClient.set(key, String.valueOf(count + 1));
        }

        return true;
    }

    private String getClientIp(HttpServletRequest request) {
        String ip = request.getHeader("X-Forwarded-For");
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("X-Real-IP");
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        if (ip != null && ip.contains(",")) {
            ip = ip.split(",")[0].trim();
        }
        return ip;
    }
}

