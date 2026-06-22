package com.example.forum.core.util;

import org.springframework.core.env.Environment;
import com.example.forum.core.util.SpringUtil;

public class EnvUtil {
    private static Environment environment;

    public static String getEnv() {
        if (environment == null) {
            environment = SpringUtil.getBean(Environment.class);
        }
        return environment.getProperty("env.name", "dev");
    }

    public static boolean isDev() {
        return "dev".equals(getEnv());
    }

    public static boolean isProd() {
        return "prod".equals(getEnv());
    }

    public static boolean isPro() {
        return isProd();
    }

    public static boolean isTest() {
        return "test".equals(getEnv());
    }

    public static String getProperty(String key) {
        if (environment == null) {
            environment = SpringUtil.getBean(Environment.class);
        }
        return environment.getProperty(key);
    }

    public static String getProperty(String key, String defaultValue) {
        if (environment == null) {
            environment = SpringUtil.getBean(Environment.class);
        }
        return environment.getProperty(key, defaultValue);
    }
}
