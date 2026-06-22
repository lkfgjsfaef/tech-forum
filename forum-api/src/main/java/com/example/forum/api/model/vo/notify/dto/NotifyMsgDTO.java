package com.example.forum.api.model.vo.notify.dto;

import lombok.Data;

@Data
public class NotifyMsgDTO {
    private Long id;
    private Long userId;
    private String title;
    private String content;
    private Integer type;
    private Long createTime;
    private Boolean read;
}
