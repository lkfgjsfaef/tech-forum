package com.example.forum.api.model.vo.rank.dto;

import com.example.forum.api.model.enums.rank.ActivityRankTimeEnum;
import lombok.Data;

import java.util.List;

@Data
public class RankInfoDTO {
    private String title;
    private List<RankItemDTO> items;
    private ActivityRankTimeEnum time;
}
