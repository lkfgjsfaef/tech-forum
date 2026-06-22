package com.example.forum.api.model.enums;

import lombok.Getter;

@Getter
public enum PushStatusEnum {
    DRAFT(0, "草稿"),
    PUBLISH(1, "发布"),
    ONLINE(1, "上线"),
    OFFLINE(2, "下线");
    
    private final Integer code;
    private final String desc;
    
    PushStatusEnum(Integer code, String desc) {
        this.code = code;
        this.desc = desc;
    }
}
