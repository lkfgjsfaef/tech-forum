package com.example.forum.core.autoconf;

import org.springframework.stereotype.Component;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class DynamicConfigContainer {
    private final Map<String, Object> configMap = new ConcurrentHashMap<>();

    public void put(String key, Object value) {
        configMap.put(key, value);
    }

    public Object get(String key) {
        return configMap.get(key);
    }

    public <T> T get(String key, Class<T> clz) {
        Object value = configMap.get(key);
        return value != null ? clz.cast(value) : null;
    }

    public void remove(String key) {
        configMap.remove(key);
    }

    public boolean contains(String key) {
        return configMap.containsKey(key);
    }

    public void forceRefresh() {
    }

    public Map<String, Object> getCache() {
        return new ConcurrentHashMap<>(configMap);
    }
}
