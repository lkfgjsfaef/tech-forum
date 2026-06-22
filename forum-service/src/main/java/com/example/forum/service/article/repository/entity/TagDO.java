package com.example.forum.service.article.repository.entity;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class TagDO {
    private Long id;
    private String tagName;
    private String tag;
    private Integer tagType;
    private Long categoryId;
    private Integer status;
    private Integer articleCount;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
