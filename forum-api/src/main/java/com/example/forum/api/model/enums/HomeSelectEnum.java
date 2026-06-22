package com.example.forum.api.model.enums;

import lombok.Getter;

@Getter
public enum HomeSelectEnum {
    ARTICLE("article", "文章"),
    READ("read", "阅读"),
    COLLECT("collect", "收藏"),
    LIKE("like", "点赞"),
    COMMENT("comment", "评论"),
    FOLLOW("follow", "关注");

    private final String code;
    private final String desc;

    HomeSelectEnum(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public static HomeSelectEnum fromCode(String code) {
        if (code == null) {
            return null;
        }
        for (HomeSelectEnum type : values()) {
            if (type.code.equalsIgnoreCase(code)) {
                return type;
            }
        }
        return null;
    }
}
