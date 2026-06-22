package com.example.forum.api.model.enums;

import lombok.Getter;

@Getter
public enum DocumentTypeEnum {
    ARTICLE("文章"),
    COMMENT("评论"),
    COLUMN("专栏");
    
    private final String desc;
    
    DocumentTypeEnum(String desc) {
        this.desc = desc;
    }
}
