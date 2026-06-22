package com.example.forum.core.cache;

import org.springframework.data.redis.core.RedisTemplate;

import java.time.Duration;
import java.util.Arrays;
import java.util.Set;
import java.util.concurrent.TimeUnit;

public class RedisClient {

    private static RedisTemplate<String, String> redisTemplate;

    public RedisClient(RedisTemplate<String, String> redisTemplate) {
        RedisClient.redisTemplate = redisTemplate;
    }

    public static void register(RedisTemplate<String, String> template) {
        redisTemplate = template;
    }

    public static RedisTemplate<String, String> getRedisTemplate() {
        return redisTemplate;
    }

    public static void set(String key, String value) {
        if (redisTemplate != null) {
            redisTemplate.opsForValue().set(key, value);
        }
    }

    public static Boolean setIfAbsent(String key, String value, long timeout, TimeUnit unit) {
        if (redisTemplate != null) {
            return redisTemplate.opsForValue().setIfAbsent(key, value, timeout, unit);
        }
        return false;
    }

    public static String get(String key) {
        if (redisTemplate != null) {
            return redisTemplate.opsForValue().get(key);
        }
        return null;
    }

    public static void delete(String key) {
        if (redisTemplate != null) {
            redisTemplate.delete(key);
        }
    }

    public static boolean hasKey(String key) {
        if (redisTemplate != null) {
            return Boolean.TRUE.equals(redisTemplate.hasKey(key));
        }
        return false;
    }

    public static Long getExpire(String key) {
        if (redisTemplate != null) {
            return redisTemplate.getExpire(key);
        }
        return null;
    }

    public static boolean expire(String key, Duration timeout) {
        if (redisTemplate != null) {
            return redisTemplate.expire(key, timeout);
        }
        return false;
    }

    public static boolean expire(String key, long timeoutSeconds) {
        return expire(key, Duration.ofSeconds(timeoutSeconds));
    }

    public static boolean expire(String key, long timeout, TimeUnit unit) {
        if (redisTemplate != null) {
            return redisTemplate.expire(key, timeout, unit);
        }
        return false;
    }

    public static Long incr(String key) {
        if (redisTemplate != null) {
            return redisTemplate.opsForValue().increment(key);
        }
        return 0L;
    }

    public static void del(String... keys) {
        if (redisTemplate != null && keys.length > 0) {
            redisTemplate.delete(Arrays.asList(keys));
        }
    }

    public static Long sadd(String key, String... members) {
        if (redisTemplate != null && members.length > 0) {
            return redisTemplate.opsForSet().add(key, members);
        }
        return 0L;
    }

    public static Long srem(String key, String... members) {
        if (redisTemplate != null && members.length > 0) {
            return redisTemplate.opsForSet().remove(key, (Object[]) members);
        }
        return 0L;
    }

    public static Boolean sismember(String key, String member) {
        if (redisTemplate != null) {
            return redisTemplate.opsForSet().isMember(key, member);
        }
        return false;
    }

    public static Set<String> keys(String pattern) {
        if (redisTemplate != null) {
            return redisTemplate.keys(pattern);
        }
        return java.util.Collections.emptySet();
    }
}
