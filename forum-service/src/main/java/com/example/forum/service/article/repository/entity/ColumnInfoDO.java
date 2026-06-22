package com.example.forum.service.article.repository.entity;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class ColumnInfoDO {
    private Long id;
    private String columnName;
    private Long userId;
    private String introduction;
    private String cover;
    private Integer state;
    private LocalDateTime publishTime;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
    private Integer section;
    private Integer nums;
    private Integer type;
    private LocalDateTime freeStartTime;
    private LocalDateTime freeEndTime;
}
