package com.example.forum.api.model.enums.column;

import lombok.Getter;

@Getter
public enum ColumnStatusEnum {
    DRAFT(0, "草稿"),
    PUBLISH(1, "发布"),
    OFFLINE(2, "下线");
    
    private final Integer code;
    private final String desc;
    
    ColumnStatusEnum(Integer code, String desc) {
        this.code = code;
        this.desc = desc;
    }
    
    public static ColumnStatusEnum fromCode(Integer code) {
        if (code == null) {
            return null;
        }
        for (ColumnStatusEnum e : values()) {
            if (e.code.equals(code)) {
                return e;
            }
        }
        return null;
    }
}
