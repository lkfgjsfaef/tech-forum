package com.example.forum.api.model.enums.column;

import lombok.Getter;

@Getter
public enum ColumnTypeEnum {
    FREE(0, "免费专栏"),
    PAY(1, "付费专栏"),
    LOGIN(2, "登录阅读"),
    STAR_READ(3, "星球阅读"),
    TIME_FREE(4, "限时免费");

    private final Integer type;
    private final String desc;

    ColumnTypeEnum(Integer type, String desc) {
        this.type = type;
        this.desc = desc;
    }

    public Integer getType() {
        return type;
    }
}
