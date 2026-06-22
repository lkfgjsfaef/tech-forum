package com.example.forum.api.model.enums;

import lombok.Getter;

@Getter
public enum FollowSelectEnum {
    FOLLOW("follow", "关注"),
    FANS("fans", "粉丝");

    private final String code;
    private final String desc;

    FollowSelectEnum(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public static FollowSelectEnum fromCode(String code) {
        if (code == null) return null;
        for (FollowSelectEnum e : values()) {
            if (e.code.equalsIgnoreCase(code)) return e;
        }
        return null;
    }
}
