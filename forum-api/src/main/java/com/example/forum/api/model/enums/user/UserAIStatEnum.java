package com.example.forum.api.model.enums.user;

import lombok.Getter;

@Getter
public enum UserAIStatEnum {
    NORMAL(0, "正常"),
    BANNED(1, "禁用"),
    VIP(2, "VIP用户"),
    LOGIN(3, "已登册"),
    FORMAL(4, "正式用户"),
    EMPTY(5, "空状册");
    
    private final Integer code;
    private final String desc;
    
    UserAIStatEnum(Integer code, String desc) {
        this.code = code;
        this.desc = desc;
    }
    
    public static UserAIStatEnum fromCode(Integer code) {
        if (code == null) {
            return null;
        }
        for (UserAIStatEnum e : values()) {
            if (e.code.equals(code)) {
                return e;
            }
        }
        return null;
    }
}

