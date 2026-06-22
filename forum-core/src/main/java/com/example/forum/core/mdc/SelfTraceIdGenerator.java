package com.example.forum.core.mdc;

import java.util.UUID;

public class SelfTraceIdGenerator {
    public static String generate() {
        return UUID.randomUUID().toString().replace("-", "").substring(0, 16);
    }
}
