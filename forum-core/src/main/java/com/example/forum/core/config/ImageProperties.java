package com.example.forum.core.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "image")
public class ImageProperties {
    private String cdnHost;
    private String ossType;
    private String basePath;
    private Long maxSize;
    private String allowTypes;
}
