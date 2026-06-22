package com.example.forum.api.model.vo.statistics.dto;

import lombok.Data;

@Data
public class StatisticsCountDTO {
    private Long pv;
    private Long uv;
    private Long articleCount;
    private Long userCount;
    private Long commentCount;
    private Long viewCount;
}
