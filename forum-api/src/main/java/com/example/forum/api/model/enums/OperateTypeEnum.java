package com.example.forum.api.model.enums;

import lombok.Getter;

@Getter
public enum OperateTypeEnum {
    EMPTY(-1, "空操册"),
    LIKE(0, "点赞"),
    COLLECT(1, "收藏"),
    CANCEL_LIKE(2, "取消点赞"),
    CANCEL_COLLECT(3, "取消收藏");
    
    private final Integer code;
    private final String desc;
    
    OperateTypeEnum(Integer code, String desc) {
        this.code = code;
        this.desc = desc;
    }
    
    public static OperateTypeEnum fromCode(Integer code) {
        if (code == null) {
            return EMPTY;
        }
        for (OperateTypeEnum e : values()) {
            if (e.code.equals(code)) {
                return e;
            }
        }
        return EMPTY;
    }
}

