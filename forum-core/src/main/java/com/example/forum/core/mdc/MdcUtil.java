package com.example.forum.core.mdc;

import org.slf4j.MDC;

import java.util.Map;
import java.util.UUID;

public class MdcUtil {
    public static final String TRACE_ID = "traceId";
    public static final String TRACE_ID_KEY = "traceId";
    public static final String USER_ID = "userId";

    public static void addTraceId() {
        MDC.put(TRACE_ID, UUID.randomUUID().toString().replace("-", "").substring(0, 16));
    }

    public static void removeTraceId() {
        MDC.remove(TRACE_ID);
    }

    public static String getTraceId() {
        return MDC.get(TRACE_ID);
    }

    public static void putUserId(String userId) {
        MDC.put(USER_ID, userId);
    }

    public static void removeUserId() {
        MDC.remove(USER_ID);
    }

    public static Map<String, String> getCopyOfContextMap() {
        return MDC.getCopyOfContextMap();
    }

    public static void setContextMap(Map<String, String> contextMap) {
        if (contextMap != null) {
            MDC.setContextMap(contextMap);
        }
    }

    public static void clear() {
        MDC.clear();
    }

    public static void add(String key, String value) {
        MDC.put(key, value);
    }

    public static void remove(String key) {
        MDC.remove(key);
    }

    public static String get(String key) {
        return MDC.get(key);
    }
}
