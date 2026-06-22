package com.example.forum.api.model.enums;

import lombok.Getter;

@Getter
public enum ChatSocketStateEnum {
    CONNECTED(0, "已连册"),
    DISCONNECTED(1, "已断开"),
    ERROR(2, "错误"),
    WAITING(3, "等待册"),
    Established(4, "已建册"),
    Payload(5, "消息负载"),
    Closed(6, "已关册");
    
    private final Integer code;
    private final String desc;
    
    ChatSocketStateEnum(Integer code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public static ChatSocketStateEnum fromCode(Integer code) {
        if (code == null) return null;
        for (ChatSocketStateEnum e : values()) {
            if (e.code.equals(code)) return e;
        }
        return null;
    }
}

