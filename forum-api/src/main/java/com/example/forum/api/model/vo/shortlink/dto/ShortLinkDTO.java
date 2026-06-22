package com.example.forum.api.model.vo.shortlink.dto;

import lombok.Data;

@Data
public class ShortLinkDTO {
    private Long id;
    private String shortCode;
    private String originalUrl;
    private Long createTime;
    private String userId;
    private String extra;

    public ShortLinkDTO() {
    }

    public ShortLinkDTO(String originalUrl, String userId, String extra) {
        this.originalUrl = originalUrl;
        this.userId = userId;
        this.extra = extra;
    }
}
