package com.example.forum.core.dal;

import java.util.function.Supplier;

public class DsSelectExecutor {
    public static <T> T execute(MasterSlaveDsEnum ds, Supplier<T> supplier) {
        return supplier.get();
    }

    public static void execute(MasterSlaveDsEnum ds, Runnable runnable) {
        runnable.run();
    }
}
