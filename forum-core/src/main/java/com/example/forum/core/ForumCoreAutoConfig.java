package com.example.forum.core;

import com.github.benmanes.caffeine.cache.Caffeine;
import com.example.forum.core.cache.RedisClient;
import com.example.forum.core.config.ProxyProperties;
import com.example.forum.core.net.ProxyCenter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cache.CacheManager;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;

import jakarta.annotation.PostConstruct;
import java.util.concurrent.TimeUnit;

@Configuration
@EnableConfigurationProperties(ProxyProperties.class)
@ComponentScan(basePackages = "com.example.forum.core")
public class ForumCoreAutoConfig {
    @Autowired
    private ProxyProperties proxyProperties;

    @Autowired(required = false)
    private RedisTemplate<String, String> redisTemplate;

    @PostConstruct
    public void init() {
        ProxyCenter.initProxyPool(proxyProperties.getProxy());
        
        if (redisTemplate != null) {
            RedisClient.register(redisTemplate);
            System.out.println("[RedisClient] RedisTemplate 已注册");
        } else {
            System.err.println("[RedisClient] 警告: RedisTemplate 未注入，Redis 相关功能将不可用");
        }
    }

    @Bean
    public CacheManager cacheManager() {
        CaffeineCacheManager cacheManager = new CaffeineCacheManager();
        cacheManager.setCaffeine(Caffeine.newBuilder().
                recordStats()
                .expireAfterWrite(5, TimeUnit.MINUTES)
                .initialCapacity(200)
                .maximumSize(1000)
        );
        return cacheManager;
    }
}

