-- ============================================================
-- Tech Forum 数据库优化脚本
-- 日期: 2026-04-11
-- 内容: 添加缺失索引 + 新增功能表（收藏/点赞/关注）
-- ============================================================

SET NAMES utf8mb4;

-- ==================== 1. 缺失索引优化 ====================

-- 1.1 article 表：user_id 是高频查询条件，缺少索引
-- 场景：查询用户文章列表、个人中心文章、专栏作者文章等
ALTER TABLE `article` ADD INDEX `idx_user_id` (`user_id`) USING BTREE;

-- 1.2 article 表：status + create_time 复合索引
-- 场景：首页文章列表（WHERE status=1 ORDER BY create_time DESC）
ALTER TABLE `article` ADD INDEX `idx_status_create_time` (`status`, `create_time` DESC) USING BTREE;

-- 1.3 article 表：category_id + status 复合索引
-- 场景：分类文章列表（WHERE category_id=? AND status=1）
ALTER TABLE `article` ADD INDEX `idx_category_status` (`category_id`, `status`) USING BTREE;

-- 1.4 article_tag 表：article_id 索引缺失（只有tag_id有索引）
-- 场景：根据文章ID查标签
ALTER TABLE `article_tag` ADD INDEX `idx_article_id` (`article_id`) USING BTREE;

-- 1.5 column_article 表：article_id 索引缺失
-- 场景：根据文章查所属专栏
ALTER TABLE `column_article` ADD INDEX `idx_article_id` (`article_id`) USING BTREE;

-- 1.6 column_article 表：column_id + section 排序索引
-- 场景：查询专栏文章按章节排序
ALTER TABLE `column_article` ADD INDEX `idx_column_section` (`column_id`, `section` ASC) USING BTREE;

-- 1.7 comment 表：parent_comment_id 索引（用于回复评论查询）
-- 场景：查询某条评论的所有回复
ALTER TABLE `comment` ADD INDEX `idx_parent_comment_id` (`parent_comment_id`) USING BTREE;

-- 1.8 comment 表：article_id + create_time 复合索引（用于文章评论排序）
-- 场景：查询某文章的评论按时间排序
ALTER TABLE `comment` ADD INDEX `idx_article_time` (`article_id`, `create_time` ASC) USING BTREE;


-- ==================== 2. 新增功能表 ====================

-- 2.1 文章收藏表
DROP TABLE IF EXISTS `article_collect`;
CREATE TABLE `article_collect` (
    `id` bigint UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `user_id` bigint UNSIGNED NOT NULL DEFAULT 0 COMMENT '用户ID',
    `article_id` bigint UNSIGNED NOT NULL DEFAULT 0 COMMENT '文章ID',
    `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '收藏时间',
    PRIMARY KEY (`id`) USING BTREE,
    UNIQUE KEY `uk_user_article` (`user_id`, `article_id`),
    INDEX `idx_collect_user` (`user_id`) USING BTREE,
    INDEX `idx_collect_article` (`article_id`) USING BTREE
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '文章收藏表';

-- 2.2 文章点赞表
DROP TABLE IF EXISTS `article_like`;
CREATE TABLE `article_like` (
    `id` bigint UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `user_id` bigint UNSIGNED NOT NULL DEFAULT 0 COMMENT '用户ID',
    `article_id` bigint UNSIGNED NOT NULL DEFAULT 0 COMMENT '文章ID',
    `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '点赞时间',
    PRIMARY KEY (`id`) USING BTREE,
    UNIQUE KEY `uk_user_article` (`user_id`, `article_id`),
    INDEX `idx_like_user` (`user_id`) USING BTREE,
    INDEX `idx_like_article` (`article_id`) USING BTREE
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '文章点赞表';

-- 2.3 评论点赞表
DROP TABLE IF EXISTS `comment_like`;
CREATE TABLE `comment_like` (
    `id` bigint UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `user_id` bigint UNSIGNED NOT NULL DEFAULT 0 COMMENT '用户ID',
    `comment_id` bigint UNSIGNED NOT NULL DEFAULT 0 COMMENT '评论ID',
    `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '点赞时间',
    PRIMARY KEY (`id`) USING BTREE,
    UNIQUE KEY `uk_user_comment` (`user_id`, `comment_id`)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '评论点赞表';

-- 2.4 用户关注表
DROP TABLE IF EXISTS `user_follow`;
CREATE TABLE `user_follow` (
    `id` bigint UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `follower_id` bigint UNSIGNED NOT NULL DEFAULT 0 COMMENT '粉丝用户ID',
    `following_id` bigint UNSIGNED NOT NULL DEFAULT 0 COMMENT '被关注的用户ID',
    `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '关注时间',
    PRIMARY KEY (`id`) USING BTREE,
    UNIQUE KEY `uk_follower_following` (`follower_id`, `following_id`),
    INDEX `idx_follower` (`follower_id`) USING BTREE,
    INDEX `idx_following` (`following_id`) USING BTREE
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '用户关注表';


-- ==================== 3. 验证索引 ====================
SELECT 
    TABLE_NAME,
    INDEX_NAME,
    COLUMN_NAME,
    SEQ_IN_INDEX
FROM information_schema.STATISTICS 
WHERE TABLE_SCHEMA = DATABASE()
  AND TABLE_NAME IN ('article', 'column_article', 'comment', 'article_tag')
ORDER BY TABLE_NAME, INDEX_NAME, SEQ_IN_INDEX;
