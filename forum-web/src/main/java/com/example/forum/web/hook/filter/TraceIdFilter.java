package com.example.forum.web.hook.filter;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Slf4j
@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class TraceIdFilter extends OncePerRequestFilter {

    private static final String TRACE_ID = "traceId";
    private static final Set<String> STATIC_EXTENSIONS = new HashSet<>(Arrays.asList(
            ".png", ".jpg", ".jpeg", ".gif", ".svg", ".ico",
            ".css", ".js", ".woff", ".woff2", ".ttf", ".eot",
            ".map"
    ));

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            String uri = request.getRequestURI();

            String traceId = request.getHeader("X-Trace-Id");
            if (traceId == null || traceId.isEmpty()) {
                traceId = generateTraceId();
            }
            MDC.put(TRACE_ID, traceId);
            response.setHeader("X-Trace-Id", traceId);

            boolean isStatic = isStaticResource(uri);

            if (!isStatic) {
                log.info("[请求开始] {} {} | IP={}",
                        request.getMethod(), uri, getClientIp(request));
            }

            long startTime = System.currentTimeMillis();
            request.setAttribute("_startTime", startTime);

            filterChain.doFilter(request, response);

            if (!isStatic) {
                long cost = System.currentTimeMillis() - startTime;
                int status = response.getStatus();
                log.info("[请求结束] {} {} | 状册{} | 耗时={}ms | traceId={}",
                        request.getMethod(), uri, status, cost, traceId);
            }
        } finally {
            MDC.clear();
        }
    }

    private boolean isStaticResource(String uri) {
        int dot = uri.lastIndexOf('.');
        if (dot > 0 && dot < uri.length() - 1) {
            String ext = uri.substring(dot).toLowerCase();
            return STATIC_EXTENSIONS.contains(ext);
        }
        return false;
    }

    private String generateTraceId() {
        return UUID.randomUUID().toString().replace("-", "").substring(0, 16);
    }

    private String getClientIp(HttpServletRequest request) {
        String ip = request.getHeader("X-Forwarded-For");
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("X-Real-IP");
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        return ip;
    }
}

