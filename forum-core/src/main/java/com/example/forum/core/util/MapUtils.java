package com.example.forum.core.util;

import java.util.HashMap;
import java.util.Map;

public class MapUtils {
    public static <K, V> Map<K, V> newHashMap() {
        return new HashMap<>();
    }

    public static <K, V> Map<K, V> newHashMap(int initialCapacity) {
        return new HashMap<>(initialCapacity);
    }

    public static <K, V> Map<K, V> of(K key, V value) {
        Map<K, V> map = new HashMap<>();
        map.put(key, value);
        return map;
    }

    public static <K, V> Map<K, V> of(K k1, V v1, K k2, V v2) {
        Map<K, V> map = new HashMap<>();
        map.put(k1, v1);
        map.put(k2, v2);
        return map;
    }

    public static <K, V> Map<K, V> of(K k1, V v1, K k2, V v2, K k3, V v3) {
        Map<K, V> map = new HashMap<>();
        map.put(k1, v1);
        map.put(k2, v2);
        map.put(k3, v3);
        return map;
    }

    public static boolean isEmpty(Map<?, ?> map) {
        return map == null || map.isEmpty();
    }

    public static boolean isNotEmpty(Map<?, ?> map) {
        return !isEmpty(map);
    }

    public static <K, V> Map<K, V> create(K key, V value) {
        return of(key, value);
    }
}
