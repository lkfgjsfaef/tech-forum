package com.example.forum.api.model.vo.banner.dto;

import lombok.Data;

@Data
public class ConfigDTO {
    private Long configId;
    private String name;
    private String bannerUrl;
    private String jumpUrl;
    private Integer type;
    private Integer status;
    private Integer rank;
}
