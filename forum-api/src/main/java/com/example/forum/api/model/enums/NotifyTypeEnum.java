package com.example.forum.api.model.enums;

import lombok.Getter;

@Getter
public enum NotifyTypeEnum {
    COMMENT("comment", "评论"),
    REPLY("reply", "回复"),
    LIKE("like", "点赞"),
    COLLECT("collect", "收藏"),
    FOLLOW("follow", "关注"),
    SYSTEM("system", "系统通知");
    
    private final String code;
    private final String desc;
    
    NotifyTypeEnum(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }
    
    public static NotifyTypeEnum typeOf(String code) {
        if (code == null) {
            return null;
        }
        for (NotifyTypeEnum type : values()) {
            if (type.code.equalsIgnoreCase(code)) {
                return type;
            }
        }
        return null;
    }
}
