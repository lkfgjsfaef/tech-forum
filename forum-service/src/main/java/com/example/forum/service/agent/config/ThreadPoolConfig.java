package com.example.forum.service.agent.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

@Configuration
public class ThreadPoolConfig {

    @Bean
    public ExecutorService chatExecutor() {
        return new ThreadPoolExecutor(
                4,
                8,
                60L,
                TimeUnit.SECONDS,
                new LinkedBlockingQueue<>(256),
                new ThreadPoolExecutor.CallerRunsPolicy()
        );
    }
}
