package com.example.forum.api.model.vo.banner;

import lombok.Data;

@Data
public class ConfigReq {
    private Long id;
    private String configKey;
    private String configValue;
    private Integer type;
}
