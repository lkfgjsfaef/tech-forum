package com.example.forum.web.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.StringUtils;

import jakarta.annotation.PostConstruct;
import java.security.SecureRandom;
import java.util.Base64;

@Slf4j
@Configuration
public class SecurityConfig {

    @Value("${paicoding.jwt.secret:}")
    private String jwtSecret;

    @PostConstruct
    public void validateJwtSecret() {
        if (!StringUtils.hasText(jwtSecret)) {
            log.warn("⚠️ JWT密钥为空！已自动生成随机密钥（重启后会变化，生产环境请固定配置）");
            byte[] bytes = new byte[32];
            new SecureRandom().nextBytes(bytes);
            log.info("🔐 自动生成的JWT密钥: {}", Base64.getEncoder().encodeToString(bytes));
        } else if (jwtSecret.length() < 32) {
            log.warn("⚠️ JWT密钥长度不足32位（当前{}位），建议使用更长的随机字符串", jwtSecret.length());
        } else {
            log.info("JWT密钥配置正常，长度: {}", jwtSecret.length());
        }
    }
}



