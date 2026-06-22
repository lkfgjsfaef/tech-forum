package com.example.forum.core.cache;

import lombok.extern.slf4j.Slf4j;

import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

@Slf4j
public class RedisLock {

    private static final String LOCK_PREFIX = "lock:";
    private static final long DEFAULT_EXPIRE_SECONDS = 10L;
    private static final long RETRY_INTERVAL_MS = 50L;
    private static final long MAX_RETRY_MS = 3000L;

    private String lockKey;
    private String lockValue;
    private long expireSeconds;

    public RedisLock(String key) {
        this(key, DEFAULT_EXPIRE_SECONDS);
    }

    public RedisLock(String key, long expireSeconds) {
        this.lockKey = LOCK_PREFIX + key;
        this.expireSeconds = expireSeconds;
        this.lockValue = UUID.randomUUID().toString();
    }

    public boolean tryLock() {
        Boolean success = RedisClient.setIfAbsent(lockKey, lockValue, expireSeconds, TimeUnit.SECONDS);
        if (Boolean.TRUE.equals(success)) {
            log.debug("获取锁成功, key={}", lockKey);
            return true;
        }
        return false;
    }

    public boolean tryLock(long timeoutMs) {
        long start = System.currentTimeMillis();
        while (System.currentTimeMillis() - start < timeoutMs) {
            if (tryLock()) {
                return true;
            }
            try {
                Thread.sleep(RETRY_INTERVAL_MS);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }
        log.warn("获取锁超时, key={}, timeout={}ms", lockKey, timeoutMs);
        return false;
    }

    public void unlock() {
        try {
            String currentValue = RedisClient.get(lockKey);
            if (lockValue.equals(currentValue)) {
                RedisClient.del(lockKey);
                log.debug("释放锁成功, key={}", lockKey);
            }
        } catch (Exception e) {
            log.error("释放锁异常, key={}", lockKey, e);
        }
    }

    public <T> T execute(Supplier<T> supplier) {
        return execute(supplier, MAX_RETRY_MS);
    }

    public <T> T execute(Supplier<T> supplier, long waitTimeoutMs) {
        if (tryLock(waitTimeoutMs)) {
            try {
                return supplier.get();
            } finally {
                unlock();
            }
        }
        throw new RuntimeException("获取分布式锁失败: " + lockKey);
    }

    public void execute(Runnable runnable) {
        execute(runnable, MAX_RETRY_MS);
    }

    public void execute(Runnable runnable, long waitTimeoutMs) {
        if (tryLock(waitTimeoutMs)) {
            try {
                runnable.run();
            } finally {
                unlock();
            }
        } else {
            throw new RuntimeException("获取分布式锁失败: " + lockKey);
        }
    }

    public static boolean isLocked(String key) {
        return RedisClient.hasKey(LOCK_PREFIX + key);
    }
}


