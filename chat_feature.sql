-- ============================================================
-- Tech Forum - 私信聊天功能 SQL
-- 执行方式: 直接复制到 MySQL 中执行
-- ============================================================

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- 会话表：记录两个用户之间的对话关系
-- ----------------------------
DROP TABLE IF EXISTS `chat_session`;
CREATE TABLE `chat_session` (
  `id` bigint UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `user1_id` bigint UNSIGNED NOT NULL DEFAULT 0 COMMENT '用户1 ID（较小的那个）',
  `user2_id` bigint UNSIGNED NOT NULL DEFAULT 0 COMMENT '用户2 ID（较大的那个）',
  `last_message` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '' COMMENT '最后一条消息内容',
  `last_message_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '最后消息时间',
  `user1_unread` int UNSIGNED NOT NULL DEFAULT 0 COMMENT '用户1未读数',
  `user2_unread` int UNSIGNED NOT NULL DEFAULT 0 COMMENT '用户2未读数',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '会话创建时间',
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_user_pair`(`user1_id` ASC, `user2_id` ASC) USING BTREE,
  INDEX `idx_user1`(`user1_id` ASC) USING BTREE,
  INDEX `idx_user2`(`user2_id` ASC) USING BTREE,
  INDEX `idx_last_time`(`last_message_time` DESC) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '私信会话表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- 消息表：存储具体的聊天消息
-- ----------------------------
DROP TABLE IF EXISTS `chat_message`;
CREATE TABLE `chat_message` (
  `id` bigint UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `session_id` bigint UNSIGNED NOT NULL DEFAULT 0 COMMENT '所属会话ID',
  `from_user_id` bigint UNSIGNED NOT NULL DEFAULT 0 COMMENT '发送者用户ID',
  `to_user_id` bigint UNSIGNED NOT NULL DEFAULT 0 COMMENT '接收者用户ID',
  `content` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '消息内容',
  `message_type` tinyint NOT NULL DEFAULT 0 COMMENT '消息类型: 0-文本, 1-图片, 2-系统消息',
  `is_read` tinyint NOT NULL DEFAULT 0 COMMENT '是否已读: 0-未读, 1-已读',
  `deleted` tinyint NOT NULL DEFAULT 0 COMMENT '是否删除(撤回): 0-正常, 1-已删除',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '发送时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_session`(`session_id` ASC, `create_time` ASC) USING BTREE,
  INDEX `idx_from_user`(`from_user_id` ASC) USING BTREE,
  INDEX `idx_to_user`(`to_user_id` ASC) USING BTREE,
  INDEX `idx_to_read`(`to_user_id` ASC, `is_read` ASC) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '私信消息表' ROW_FORMAT = Dynamic;

SET FOREIGN_KEY_CHECKS = 1;
