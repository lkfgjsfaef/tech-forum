package com.example.forum.api.model.enums.column;

import lombok.Getter;

@Getter
public enum ColumnArticleReadEnum {
    ALL(0, "全部文章"),
    FREE(1, "免费文章"),
    PAY(2, "付费文章"),
    COLUMN_TYPE(3, "跟随专栏类型"),
    LOGIN(4, "登录阅读"),
    STAR_READ(5, "星球阅读");
    
    private final Integer read;
    private final String desc;
    
    ColumnArticleReadEnum(Integer read, String desc) {
        this.read = read;
        this.desc = desc;
    }
    
    public Integer getRead() {
        return read;
    }
    
    public static ColumnArticleReadEnum valueOf(Integer read) {
        if (read == null) {
            return null;
        }
        for (ColumnArticleReadEnum e : values()) {
            if (e.read.equals(read)) {
                return e;
            }
        }
        return null;
    }
}
