package com.example.forum.api.model.enums;

import lombok.Getter;

@Getter
public enum ArticleReadTypeEnum {
    NORMAL(0, "正常阅读"),
    PREVIEW(1, "预览模式"),
    CONFIRM(2, "确认阅读"),
    STAR_READ(3, "星球阅读"),
    PAY_READ(4, "付费阅读"),
    LOGIN(5, "登录阅读");
    
    private final Integer type;
    private final String desc;
    
    ArticleReadTypeEnum(Integer type, String desc) {
        this.type = type;
        this.desc = desc;
    }
    
    public static ArticleReadTypeEnum fromType(Integer type) {
        if (type == null) {
            return null;
        }
        for (ArticleReadTypeEnum e : values()) {
            if (e.type.equals(type)) {
                return e;
            }
        }
        return null;
    }
    
    public static ArticleReadTypeEnum typeOf(Integer type) {
        return fromType(type);
    }
}
