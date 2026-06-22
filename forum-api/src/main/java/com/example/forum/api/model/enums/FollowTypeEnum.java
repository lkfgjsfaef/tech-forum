package com.example.forum.api.model.enums;

import lombok.Getter;

@Getter
public enum FollowTypeEnum {
    ARTICLE("article", "文章"),
    COLUMN("column", "专栏"),
    USER("user", "用户"),
    FOLLOW("follow", "关注"),
    FANS("fans", "粉丝");

    private final String code;
    private final String desc;

    FollowTypeEnum(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public static FollowTypeEnum fromCode(String code) {
        if (code == null) {
            return null;
        }
        for (FollowTypeEnum type : values()) {
            if (type.code.equalsIgnoreCase(code)) {
                return type;
            }
        }
        return null;
    }
}
