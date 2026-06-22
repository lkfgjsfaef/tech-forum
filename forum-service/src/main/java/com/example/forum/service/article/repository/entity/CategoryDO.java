package com.example.forum.service.article.repository.entity;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class CategoryDO {
    private Long id;
    private String categoryName;
    String category;
    private Integer status;
    private Integer rank;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
