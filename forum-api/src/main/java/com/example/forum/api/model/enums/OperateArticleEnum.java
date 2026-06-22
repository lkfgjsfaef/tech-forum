package com.example.forum.api.model.enums;

import lombok.Getter;

@Getter
public enum OperateArticleEnum {
    EMPTY(0, "空操册"),
    PUBLISH(1, "发布"),
    OFFLINE(2, "下线"),
    DELETE(3, "删除");
    
    private final Integer code;
    private final String desc;
    
    OperateArticleEnum(Integer code, String desc) {
        this.code = code;
        this.desc = desc;
    }
    
    public static OperateArticleEnum fromCode(Integer code) {
        if (code == null) {
            return EMPTY;
        }
        for (OperateArticleEnum e : values()) {
            if (e.code.equals(code)) {
                return e;
            }
        }
        return EMPTY;
    }
}

