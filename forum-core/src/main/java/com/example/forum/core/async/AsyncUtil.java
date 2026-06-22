package com.example.forum.core.async;

import org.springframework.core.task.TaskExecutor;
import com.example.forum.core.util.SpringUtil;

import java.util.concurrent.CompletableFuture;

public class AsyncUtil {
    private static TaskExecutor taskExecutor;

    public static void execute(Runnable runnable) {
        if (taskExecutor == null) {
            taskExecutor = SpringUtil.getBean(TaskExecutor.class);
        }
        taskExecutor.execute(runnable);
    }

    public static CompletableFuture<Void> runAsync(Runnable runnable) {
        return CompletableFuture.runAsync(runnable);
    }

    public static ConcurrentExecutor concurrentExecutor(String name) {
        return new ConcurrentExecutor(name);
    }
}
