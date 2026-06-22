package com.example.forum.service.rank.service.model;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class ActivityScoreBo {
    private Long userId;
    private String activityType;
    private String path;
    private int score;
}
