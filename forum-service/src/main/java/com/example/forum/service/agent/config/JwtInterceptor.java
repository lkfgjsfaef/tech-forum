package com.example.forum.service.agent.config;

import com.example.forum.api.model.context.ReqInfoContext;
import com.example.forum.service.agent.common.util.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtInterceptor implements HandlerInterceptor {

    private final JwtUtil jwtUtil;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if ("OPTIONS".equalsIgnoreCase(request.getMethod())) {
            return true;
        }

        String uri = request.getRequestURI();
        if (uri.endsWith("/user/login") || uri.endsWith("/user/register") || uri.endsWith("/model/list") || uri.startsWith("/agent/chat/stream")) {
            return true;
        }

        String authHeader = request.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            // 如果是从前端页面发起的请求（没有携带Authorization），放行它或者使用匿名用户身份
            // 为了保证前后端合并后的 /chat 页面能正常操作 Agent 接口
            if (ReqInfoContext.getReqInfo() != null && ReqInfoContext.getReqInfo().getUserId() != null) {
                Long userId = ReqInfoContext.getReqInfo().getUserId();
                request.setAttribute("userId", userId);
                return true;
            }
            
            // 允许页面上的无Token访问，为其分配匿名ID
            // 这里我们暂时允许匿名访问以修复按钮没反应的问题，分配一个虚拟访客 ID
            request.setAttribute("userId", 1L); 
            return true;
        }

        String token = authHeader.substring(7);
        try {
            Long userId = jwtUtil.getUserId(token);
            String role = jwtUtil.getRole(token);
            request.setAttribute("userId", userId);
            request.setAttribute("role", role);
            return true;
        } catch (Exception e) {
            log.warn("JWT楠岃瘉澶辫触: {}", e.getMessage());
            response.setStatus(401);
            response.setContentType("application/json;charset=UTF-8");
            response.getWriter().write("{\"code\":401,\"msg\":\"token鏃犳晥\"}");
            return false;
        }
    }
}
