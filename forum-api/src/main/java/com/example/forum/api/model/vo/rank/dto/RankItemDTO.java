package com.example.forum.api.model.vo.rank.dto;

import lombok.Data;

@Data
public class RankItemDTO {
    private Long userId;
    private String userName;
    private String avatar;
    private Integer count;
    private Integer rank;
}
