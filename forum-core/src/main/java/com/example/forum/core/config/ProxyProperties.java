package com.example.forum.core.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

@Data
@ConfigurationProperties(prefix = "forum.proxy")
public class ProxyProperties {
    private boolean enable = false;
    private List<ProxyItem> proxy;
    private String host;
    private Integer port;
    private String username;
    private String password;
    
    @Data
    public static class ProxyItem {
        private String host;
        private Integer port;
        private String username;
        private String password;
    }
}
