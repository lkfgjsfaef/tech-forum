package com.example.forum.core.common;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CommonConstants {
    public static final String CATEGORY_ALL = "all";
    
    public static final Map<String, List<String>> HOMEPAGE_TOP_PIC_MAP = new HashMap<>();
    
    static {
        HOMEPAGE_TOP_PIC_MAP.put(CATEGORY_ALL, Arrays.asList(
            "/img/default-cover-1.jpg",
            "/img/default-cover-2.jpg",
            "/img/default-cover-3.jpg"
        ));
    }
    
    private CommonConstants() {
    }
}
