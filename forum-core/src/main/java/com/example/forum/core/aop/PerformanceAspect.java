package com.example.forum.core.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Slf4j
@Aspect
@Component
@Order(2)
public class PerformanceAspect {

    @Pointcut("execution(* com.example.forum.web.front.*.rest.*.*(..))")
    public void apiPointcut() {}

    @Around("apiPointcut()")
    public Object around(ProceedingJoinPoint point) throws Throwable {
        long startTime = System.currentTimeMillis();
        String className = point.getTarget().getClass().getSimpleName();
        String methodName = point.getSignature().getName();

        Object result = null;
        try {
            result = point.proceed();
            return result;
        } finally {
            long cost = System.currentTimeMillis() - startTime;
            if (cost > 1000) {
                log.warn("[慢接口] {}.{} 耗时: {}ms", className, methodName, cost);
            } else if (cost > 300) {
                log.info("[接口耗时] {}.{} 耗时: {}ms", className, methodName, cost);
            }
        }
    }
}
