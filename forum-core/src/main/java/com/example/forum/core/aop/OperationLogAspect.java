package com.example.forum.core.aop;

import com.alibaba.fastjson2.JSON;
import com.example.forum.api.model.context.ReqInfoContext;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Slf4j
@Aspect
@Component
@Order(1)
public class OperationLogAspect {

    @Pointcut("@annotation(com.example.forum.core.aop.OperationLog)")
    public void logPointcut() {}

    @Around("logPointcut()")
    public Object around(ProceedingJoinPoint point) throws Throwable {
        MethodSignature signature = (MethodSignature) point.getSignature();
        OperationLog operationLog = signature.getMethod().getAnnotation(OperationLog.class);

        long startTime = System.currentTimeMillis();
        String className = point.getTarget().getClass().getSimpleName();
        String methodName = signature.getName();

        Object result = null;
        Exception exception = null;

        try {
            result = point.proceed();
            return result;
        } catch (Exception e) {
            exception = e;
            throw e;
        } finally {
            long cost = System.currentTimeMillis() - startTime;
            Long userId = -1L;
            try {
                if (ReqInfoContext.getReqInfo() != null) {
                    userId = ReqInfoContext.getReqInfo().getUserId();
                }
            } catch (Exception ignored) {}

            String params = "";
            try {
                Object[] args = point.getArgs();
                if (args != null && args.length > 0) {
                    StringBuilder sb = new StringBuilder("[");
                    for (Object arg : args) {
                        if (arg instanceof String || arg instanceof Number || arg instanceof Boolean) {
                            sb.append(arg).append(",");
                        } else {
                            sb.append(JSON.toJSONString(arg)).append(",");
                        }
                    }
                    params = sb.append("]").toString();
                }
            } catch (Exception ignored) {}

            if (exception != null) {
                log.warn("[操作日志] 失败 | 用户={} | 模块={} | 操作={} | 方法={}.{} | 参数={} | 耗时={}ms | 异常={}",
                        userId, operationLog.module(), operationLog.value(),
                        className, methodName, params, cost,
                        exception.getClass().getSimpleName());
            } else {
                log.info("[操作日志] 成功 | 用户={} | 模块={} | 操作={} | 方法={}.{} | 参数={} | 耗时={}ms",
                        userId, operationLog.module(), operationLog.value(),
                        className, methodName, params, cost);
            }
        }
    }
}
