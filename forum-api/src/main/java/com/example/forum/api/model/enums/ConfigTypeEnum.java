package com.example.forum.api.model.enums;

import lombok.Getter;

@Getter
public enum ConfigTypeEnum {
    HOME_PAGE("首页配置"),
    SIDEBAR("侧边栏配册");
    
    private final String desc;
    
    ConfigTypeEnum(String desc) {
        this.desc = desc;
    }
}

