package com.example.forum.service.tag.entity;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class Tag {
    private Long id;
    private String tagName;
    private Integer tagType;
    private Long categoryId;
    private Integer status;
    private Integer deleted;
    private Integer count;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
