package com.example.forum.web.front.login.wx.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "wx.login")
public class WxLoginProperties {
    private String loginQrType = "none";
    private String appId;
    private String appSecret;
    private String redirectUri;
}
