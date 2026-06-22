package com.example.forum.core.async;

import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Slf4j
public class ConcurrentExecutor {
    private final String name;
    private final List<CompletableFuture<Void>> futures = new ArrayList<>();
    private final ExecutorService executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());

    public ConcurrentExecutor(String name) {
        this.name = name;
    }

    public ConcurrentExecutor async(Runnable task, String taskName) {
        CompletableFuture<Void> future = CompletableFuture.runAsync(() -> {
            try {
                long start = System.currentTimeMillis();
                task.run();
                log.debug("{} - {} 执行耗时: {}ms", name, taskName, System.currentTimeMillis() - start);
            } catch (Exception e) {
                log.error("{} - {} 执行异常", name, taskName, e);
            }
        }, executor);
        futures.add(future);
        return this;
    }

    public ConcurrentExecutor allExecuted() {
        CompletableFuture.allOf(futures.toArray(new CompletableFuture[0])).join();
        executor.shutdown();
        return this;
    }

    public void prettyPrint() {
        log.info("{} - 所有任务执行完成", name);
    }
}


