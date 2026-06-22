package com.example.forum.service.article.repository.entity;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class ColumnArticleDO {
    private Long id;
    private Long columnId;
    private Long articleId;
    private Integer section;
    private Integer readType;
    private Integer status;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
