package com.niit.agent.config;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class DbInitializer {

    private final JdbcTemplate jdbcTemplate;

    @PostConstruct
    public void init() {
        try {
            jdbcTemplate.execute("CREATE TABLE IF NOT EXISTS chat_attachment (" +
                    "id BIGINT AUTO_INCREMENT PRIMARY KEY," +
                    "session_id BIGINT," +
                    "message_id BIGINT," +
                    "file_name VARCHAR(255)," +
                    "file_path VARCHAR(500)," +
                    "file_size BIGINT," +
                    "file_type VARCHAR(100)," +
                    "extracted_text LONGTEXT," +
                    "create_time DATETIME" +
                    ") ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;");
            log.info("数据库表 chat_attachment 初始化完成");
            
            // 兼容低版本 MySQL: 捕获由于不支持 IF NOT EXISTS 或字段已存在抛出的异常
            try {
                // 检查字段是否存在
                String checkSql = "SELECT COUNT(*) FROM information_schema.COLUMNS WHERE table_schema = DATABASE() AND table_name = 'user' AND column_name = 'role'";
                Integer count = jdbcTemplate.queryForObject(checkSql, Integer.class);
                if (count == null || count == 0) {
                    jdbcTemplate.execute("ALTER TABLE `user` ADD COLUMN `role` VARCHAR(20) DEFAULT 'user' AFTER `theme`;");
                    log.info("成功为 user 表添加 role 字段");
                } else {
                    log.info("user 表已存在 role 字段");
                }
            } catch (Exception alterEx) {
                log.warn("检查或更新 user 的 role 字段失败 (可能已存在): {}", alterEx.getMessage());
            }
            
        } catch (Exception e) {
            log.error("初始化数据表异常", e);
        }
    }
}



