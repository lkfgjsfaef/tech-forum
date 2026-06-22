/*
 Navicat Premium Dump SQL

 Source Server         : 2023mysql
 Source Server Type    : MySQL
 Source Server Version : 80042 (8.0.42)
 Source Host           : localhost:3306
 Source Schema         : tech_forum

 Target Server Type    : MySQL
 Target Server Version : 80042 (8.0.42)
 File Encoding         : 65001

 Date: 13/04/2026 12:24:16
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for article
-- ----------------------------
DROP TABLE IF EXISTS `article`;
CREATE TABLE `article`  (
  `id` bigint UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `user_id` bigint UNSIGNED NOT NULL DEFAULT 0 COMMENT '用户id',
  `article_type` tinyint NOT NULL DEFAULT 1 COMMENT '文章类型：1-博文，2-问答',
  `title` varchar(120) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '' COMMENT '文章标题',
  `short_title` varchar(120) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '' COMMENT '短标题',
  `url_slug` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '' COMMENT 'URL友好的文章标识，用于SEO优化',
  `picture` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '' COMMENT '文章头图',
  `summary` varchar(300) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '' COMMENT '文章摘要',
  `category_id` int UNSIGNED NOT NULL DEFAULT 0 COMMENT '类目ID',
  `source` tinyint NOT NULL DEFAULT 1 COMMENT '来源：1-转载，2-原创，3-翻译',
  `source_url` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '1' COMMENT '原文链接',
  `offical_stat` int UNSIGNED NOT NULL DEFAULT 0 COMMENT '官方状态：0-非官方，1-官方',
  `topping_stat` int UNSIGNED NOT NULL DEFAULT 0 COMMENT '置顶状态：0-不置顶，1-置顶',
  `cream_stat` int UNSIGNED NOT NULL DEFAULT 0 COMMENT '加精状态：0-不加精，1-加精',
  `status` tinyint NOT NULL DEFAULT 0 COMMENT '状态：0-未发布，1-已发布',
  `deleted` tinyint NOT NULL DEFAULT 0 COMMENT '是否删除',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '最后更新时间',
  `read_type` tinyint NOT NULL DEFAULT 0 COMMENT '文章阅读类型 0-直接阅读 1-登录阅读 2-付费阅读 3-星球',
  `pay_amount` int NOT NULL DEFAULT 0 COMMENT '付费阅读金额，单位为分',
  `pay_way` varchar(12) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '支付方式: email-个人收款码, wx_h5-微信h5支付, wx_jsapi-微信jsapi, wx_native-微信native',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_category_id`(`category_id` ASC) USING BTREE,
  INDEX `idx_title`(`title` ASC) USING BTREE,
  INDEX `idx_short_title`(`short_title` ASC) USING BTREE,
  INDEX `idx_url_slug`(`url_slug` ASC) USING BTREE,
  INDEX `idx_user_id`(`user_id` ASC) USING BTREE,
  INDEX `idx_status_create_time`(`status` ASC, `create_time` DESC) USING BTREE,
  INDEX `idx_category_status`(`category_id` ASC, `status` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 217 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '文章表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of article
-- ----------------------------

-- ----------------------------
-- Table structure for article_collect
-- ----------------------------
DROP TABLE IF EXISTS `article_collect`;
CREATE TABLE `article_collect`  (
  `id` bigint UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `user_id` bigint UNSIGNED NOT NULL DEFAULT 0 COMMENT '用户ID',
  `article_id` bigint UNSIGNED NOT NULL DEFAULT 0 COMMENT '文章ID',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '收藏时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_user_article`(`user_id` ASC, `article_id` ASC) USING BTREE,
  INDEX `idx_collect_user`(`user_id` ASC) USING BTREE,
  INDEX `idx_collect_article`(`article_id` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 5 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '文章收藏表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of article_collect
-- ----------------------------

-- ----------------------------
-- Table structure for article_detail
-- ----------------------------
DROP TABLE IF EXISTS `article_detail`;
CREATE TABLE `article_detail`  (
  `id` bigint UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `article_id` bigint UNSIGNED NOT NULL DEFAULT 0 COMMENT '文章ID',
  `version` int UNSIGNED NOT NULL DEFAULT 0 COMMENT '版本号',
  `content` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL COMMENT '文章内容',
  `deleted` tinyint NOT NULL DEFAULT 0 COMMENT '是否删除',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '最后更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `idx_article_version`(`article_id` ASC, `version` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 217 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '文章详情表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of article_detail
-- ----------------------------

-- ----------------------------
-- Table structure for article_like
-- ----------------------------
DROP TABLE IF EXISTS `article_like`;
CREATE TABLE `article_like`  (
  `id` bigint UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `user_id` bigint UNSIGNED NOT NULL DEFAULT 0 COMMENT '用户ID',
  `article_id` bigint UNSIGNED NOT NULL DEFAULT 0 COMMENT '文章ID',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '点赞时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_user_article`(`user_id` ASC, `article_id` ASC) USING BTREE,
  INDEX `idx_like_user`(`user_id` ASC) USING BTREE,
  INDEX `idx_like_article`(`article_id` ASC) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '文章点赞表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of article_like
-- ----------------------------

-- ----------------------------
-- Table structure for article_pay_record
-- ----------------------------
DROP TABLE IF EXISTS `article_pay_record`;
CREATE TABLE `article_pay_record`  (
  `id` bigint UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `article_id` bigint UNSIGNED NOT NULL DEFAULT 0 COMMENT '文章ID',
  `pay_user_id` bigint UNSIGNED NOT NULL DEFAULT 0 COMMENT '支付用户id',
  `receive_user_id` bigint UNSIGNED NOT NULL DEFAULT 0 COMMENT '收款用户id',
  `pay_status` tinyint NULL DEFAULT 0 COMMENT '支付状态 0-未支付 1-支付中 2-支付成功 3-支付失败',
  `notes` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '备注信息',
  `verify_code` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '' COMMENT '唯一code，会传递给外部支付系统',
  `notify_cnt` int NOT NULL DEFAULT 0 COMMENT '邮件通知次数',
  `notify_time` timestamp NULL DEFAULT NULL COMMENT '邮件通知收款用户时间',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '最后更新时间',
  `pre_pay_id` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '微信支付创建订单回传的关键信息',
  `pre_pay_expire_time` timestamp NULL DEFAULT NULL COMMENT 'prePayId失效时间',
  `pay_callback_time` timestamp NULL DEFAULT NULL COMMENT '支付成功时间',
  `third_trans_code` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '第三方交易流水号',
  `pay_way` varchar(12) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT 'email' COMMENT '支付方式: email-个人收款码, wx_h5-微信h5支付, wx_jsapi-微信jsapi, wx_native-微信native',
  `pay_amount` int NULL DEFAULT NULL COMMENT '支付金额，单位为分',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `i_article_id`(`article_id` ASC) USING BTREE,
  INDEX `i_pay_user_id`(`pay_user_id` ASC) USING BTREE,
  INDEX `i_receive_user_id`(`receive_user_id` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '文章支付记录表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of article_pay_record
-- ----------------------------

-- ----------------------------
-- Table structure for article_tag
-- ----------------------------
DROP TABLE IF EXISTS `article_tag`;
CREATE TABLE `article_tag`  (
  `id` bigint UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `article_id` bigint UNSIGNED NOT NULL DEFAULT 0 COMMENT '文章ID',
  `tag_id` int NOT NULL DEFAULT 0 COMMENT '标签',
  `deleted` tinyint NOT NULL DEFAULT 0 COMMENT '是否删除',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '最后更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_tag_id`(`tag_id` ASC) USING BTREE,
  INDEX `idx_article_id`(`article_id` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 134 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '文章标签映射' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of article_tag
-- ----------------------------

-- ----------------------------
-- Table structure for banner
-- ----------------------------
DROP TABLE IF EXISTS `banner`;
CREATE TABLE `banner`  (
  `id` int UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `banner_name` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '' COMMENT '名称',
  `banner_url` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '' COMMENT '图片url',
  `banner_type` tinyint NOT NULL DEFAULT 0 COMMENT '类型：1-首页，2-侧边栏，3-广告位',
  `rank` tinyint NOT NULL DEFAULT 0 COMMENT '排序',
  `status` tinyint NOT NULL DEFAULT 0 COMMENT '状态：0-未发布，1-已发布',
  `deleted` tinyint NOT NULL DEFAULT 0 COMMENT '是否删除',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '最后更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = 'banner表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of banner
-- ----------------------------

-- ----------------------------
-- Table structure for category
-- ----------------------------
DROP TABLE IF EXISTS `category`;
CREATE TABLE `category`  (
  `id` int UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `category_name` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '' COMMENT '类目名称',
  `status` tinyint NOT NULL DEFAULT 0 COMMENT '状态：0-未发布，1-已发布',
  `rank` tinyint NOT NULL DEFAULT 0 COMMENT '排序',
  `deleted` tinyint NOT NULL DEFAULT 0 COMMENT '是否删除',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '最后更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 58 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '类目管理表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of category
-- ----------------------------

-- ----------------------------
-- Table structure for chat_message
-- ----------------------------
DROP TABLE IF EXISTS `chat_message`;
CREATE TABLE `chat_message`  (
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
) ENGINE = InnoDB AUTO_INCREMENT = 26 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '私信消息表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of chat_message
-- ----------------------------

-- ----------------------------
-- Table structure for chat_session
-- ----------------------------
DROP TABLE IF EXISTS `chat_session`;
CREATE TABLE `chat_session`  (
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
) ENGINE = InnoDB AUTO_INCREMENT = 3 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '私信会话表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of chat_session
-- ----------------------------

-- ----------------------------
-- Table structure for column_article
-- ----------------------------
DROP TABLE IF EXISTS `column_article`;
CREATE TABLE `column_article`  (
  `id` bigint UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `column_id` int UNSIGNED NOT NULL DEFAULT 0 COMMENT '专栏ID',
  `article_id` bigint UNSIGNED NOT NULL DEFAULT 0 COMMENT '文章ID',
  `section` int UNSIGNED NOT NULL DEFAULT 0 COMMENT '章节顺序，越小越靠前',
  `read_type` tinyint NULL DEFAULT 0 COMMENT '文章阅读类型 0-沿用专栏规则 1-登录阅读 2-限时免费 3-星球',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '最后更新时间',
  `group_id` bigint UNSIGNED NOT NULL DEFAULT 0 COMMENT '文章对应的分组id，为1时归属在未分组的集合中',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `column_id`(`column_id` ASC) USING BTREE,
  INDEX `idx_article_id`(`article_id` ASC) USING BTREE,
  INDEX `idx_column_section`(`column_id` ASC, `section` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 24 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '专栏文章列表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of column_article
-- ----------------------------

-- ----------------------------
-- Table structure for column_article_group
-- ----------------------------
DROP TABLE IF EXISTS `column_article_group`;
CREATE TABLE `column_article_group`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `column_id` bigint UNSIGNED NOT NULL DEFAULT 0 COMMENT '专栏ID',
  `parent_group_id` bigint UNSIGNED NOT NULL DEFAULT 0 COMMENT '父分组id',
  `title` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '' COMMENT '分组名',
  `section` bigint UNSIGNED NOT NULL DEFAULT 0 COMMENT '分组顺序，越小越靠前；第一层小于1000，第二层为上一层 * 1000 + 计数',
  `deleted` tinyint NOT NULL DEFAULT 0 COMMENT '是否删除',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '最后更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `column_id`(`column_id` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 4 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '专栏文章分组' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of column_article_group
-- ----------------------------

-- ----------------------------
-- Table structure for column_info
-- ----------------------------
DROP TABLE IF EXISTS `column_info`;
CREATE TABLE `column_info`  (
  `id` bigint UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `column_name` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '' COMMENT '专栏名',
  `user_id` bigint UNSIGNED NOT NULL DEFAULT 0 COMMENT '用户id',
  `introduction` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '' COMMENT '专栏简述',
  `cover` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '' COMMENT '专栏封面',
  `state` tinyint UNSIGNED NOT NULL DEFAULT 0 COMMENT '状态: 0-审核中，1-连载，2-完结',
  `publish_time` timestamp NOT NULL DEFAULT '1970-01-02 00:00:00' COMMENT '上线时间',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '最后更新时间',
  `section` int UNSIGNED NOT NULL DEFAULT 0 COMMENT '排序',
  `nums` int UNSIGNED NOT NULL DEFAULT 0 COMMENT '专栏预计的更新的文章数',
  `type` int UNSIGNED NOT NULL DEFAULT 0 COMMENT '专栏类型 0-免费 1-登录阅读 2-限时免费 3-星球',
  `free_start_time` timestamp NOT NULL DEFAULT '1970-01-02 00:00:00' COMMENT '限时免费开始时间',
  `free_end_time` timestamp NOT NULL DEFAULT '1970-01-02 00:00:00' COMMENT '限时免费结束时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `user_id`(`user_id` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 19 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '专栏' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of column_info
-- ----------------------------

-- ----------------------------
-- Table structure for comment
-- ----------------------------
DROP TABLE IF EXISTS `comment`;
CREATE TABLE `comment`  (
  `id` bigint UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `article_id` bigint UNSIGNED NOT NULL DEFAULT 0 COMMENT '文章ID',
  `user_id` bigint UNSIGNED NOT NULL DEFAULT 0 COMMENT '用户id',
  `content` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL COMMENT '评论内容',
  `top_comment_id` int NOT NULL DEFAULT 0 COMMENT '顶级评论ID',
  `parent_comment_id` int UNSIGNED NOT NULL DEFAULT 0 COMMENT '父评论ID',
  `highlight_info` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL COMMENT '划线高亮的评论内容，包含划线内容偏移量，json格式',
  `deleted` tinyint NOT NULL DEFAULT 0 COMMENT '是否删除',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '最后更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_article_id`(`article_id` ASC) USING BTREE,
  INDEX `idx_user_id`(`user_id` ASC) USING BTREE,
  INDEX `idx_parent_comment_id`(`parent_comment_id` ASC) USING BTREE,
  INDEX `idx_article_time`(`article_id` ASC, `create_time` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 6 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '评论表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of comment
-- ----------------------------

-- ----------------------------
-- Table structure for comment_like
-- ----------------------------
DROP TABLE IF EXISTS `comment_like`;
CREATE TABLE `comment_like`  (
  `id` bigint UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `user_id` bigint UNSIGNED NOT NULL DEFAULT 0 COMMENT '用户ID',
  `comment_id` bigint UNSIGNED NOT NULL DEFAULT 0 COMMENT '评论ID',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '点赞时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_user_comment`(`user_id` ASC, `comment_id` ASC) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '评论点赞表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of comment_like
-- ----------------------------

-- ----------------------------
-- Table structure for config
-- ----------------------------
DROP TABLE IF EXISTS `config`;
CREATE TABLE `config`  (
  `id` int UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `type` tinyint NOT NULL DEFAULT 0 COMMENT '配置类型：1-首页，2-侧边栏，3-广告位，4-公告',
  `name` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '' COMMENT '名称',
  `banner_url` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '' COMMENT '图片链接',
  `jump_url` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '' COMMENT '跳转链接',
  `content` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '' COMMENT '内容',
  `rank` tinyint NOT NULL DEFAULT 0 COMMENT '排序',
  `status` tinyint NOT NULL DEFAULT 0 COMMENT '状态：0-未发布，1-已发布',
  `tags` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '' COMMENT '配置关联标签，英文逗号分隔 1 火 2 官方 3 推荐',
  `extra` varchar(1024) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '{}' COMMENT '扩展信息',
  `deleted` tinyint NOT NULL DEFAULT 0 COMMENT '是否删除',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '最后更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 7 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '配置表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of config
-- ----------------------------

-- ----------------------------
-- Table structure for databasechangelog
-- ----------------------------
DROP TABLE IF EXISTS `databasechangelog`;
CREATE TABLE `databasechangelog`  (
  `ID` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `AUTHOR` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `FILENAME` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `DATEEXECUTED` datetime NOT NULL,
  `ORDEREXECUTED` int NOT NULL,
  `EXECTYPE` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `MD5SUM` varchar(35) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `DESCRIPTION` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `COMMENTS` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `TAG` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `LIQUIBASE` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `CONTEXTS` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `LABELS` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `DEPLOYMENT_ID` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of databasechangelog
-- ----------------------------

-- ----------------------------
-- Table structure for databasechangeloglock
-- ----------------------------
DROP TABLE IF EXISTS `databasechangeloglock`;
CREATE TABLE `databasechangeloglock`  (
  `ID` int NOT NULL,
  `LOCKED` bit(1) NOT NULL,
  `LOCKGRANTED` datetime NULL DEFAULT NULL,
  `LOCKEDBY` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  PRIMARY KEY (`ID`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of databasechangeloglock
-- ----------------------------

-- ----------------------------
-- Table structure for dict_common
-- ----------------------------
DROP TABLE IF EXISTS `dict_common`;
CREATE TABLE `dict_common`  (
  `id` int UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `type_code` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '' COMMENT '字典类型，sex, status 等',
  `dict_code` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '' COMMENT '字典类型的值编码',
  `dict_desc` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '' COMMENT '字典类型的值描述',
  `sort_no` int UNSIGNED NOT NULL DEFAULT 0 COMMENT '排序编号',
  `remark` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '备注',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '最后更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_type_code_dict_code`(`type_code` ASC, `dict_code` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 70 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '通用数据字典' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of dict_common
-- ----------------------------

-- ----------------------------
-- Table structure for global_conf
-- ----------------------------
DROP TABLE IF EXISTS `global_conf`;
CREATE TABLE `global_conf`  (
  `id` int UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `key` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '' COMMENT '配置key',
  `value` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '配置value',
  `comment` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '' COMMENT '注释',
  `deleted` tinyint NOT NULL DEFAULT 0 COMMENT '是否删除 0 未删除 1 已删除',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '最后更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_key`(`key` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '全局配置表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of global_conf
-- ----------------------------

-- ----------------------------
-- Table structure for notify_msg
-- ----------------------------
DROP TABLE IF EXISTS `notify_msg`;
CREATE TABLE `notify_msg`  (
  `id` bigint UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `related_id` bigint UNSIGNED NOT NULL DEFAULT 0 COMMENT '关联的主键',
  `comment_id` bigint NULL DEFAULT NULL COMMENT '关联的评论ID',
  `notify_user_id` bigint UNSIGNED NOT NULL DEFAULT 0 COMMENT '通知的用户id',
  `operate_user_id` bigint UNSIGNED NOT NULL DEFAULT 0 COMMENT '触发这个通知的用户id',
  `msg` varchar(1024) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '' COMMENT '消息内容',
  `type` tinyint UNSIGNED NOT NULL DEFAULT 0 COMMENT '类型: 0-默认，1-评论，2-回复 3-点赞 4-收藏 5-关注 6-系统',
  `state` tinyint UNSIGNED NOT NULL DEFAULT 0 COMMENT '阅读状态: 0-未读，1-已读',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '最后更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `key_notify_user_id_type_state`(`notify_user_id` ASC, `type` ASC, `state` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '消息通知列表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of notify_msg
-- ----------------------------

-- ----------------------------
-- Table structure for read_count
-- ----------------------------
DROP TABLE IF EXISTS `read_count`;
CREATE TABLE `read_count`  (
  `id` bigint UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `document_id` bigint UNSIGNED NOT NULL DEFAULT 0 COMMENT '文档ID（文章/评论）',
  `document_type` tinyint NOT NULL DEFAULT 1 COMMENT '文档类型：1-文章，2-评论',
  `cnt` int UNSIGNED NOT NULL DEFAULT 0 COMMENT '访问计数',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '最后更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `idx_document_id_type`(`document_id` ASC, `document_type` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 21 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '计数表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of read_count
-- ----------------------------

-- ----------------------------
-- Table structure for request_count
-- ----------------------------
DROP TABLE IF EXISTS `request_count`;
CREATE TABLE `request_count`  (
  `id` bigint UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `host` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '' COMMENT '机器IP',
  `cnt` int UNSIGNED NOT NULL DEFAULT 0 COMMENT '访问计数',
  `date` date NOT NULL COMMENT '当前日期',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '最后更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_unique_id_date`(`date` ASC, `host` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 24 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '请求计数表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of request_count
-- ----------------------------

-- ----------------------------
-- Table structure for short_link
-- ----------------------------
DROP TABLE IF EXISTS `short_link`;
CREATE TABLE `short_link`  (
  `id` bigint UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `original_url` varchar(2048) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '原始URL',
  `short_code` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '短链接代码',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '最后更新时间',
  `deleted` tinyint NOT NULL DEFAULT 0 COMMENT '是否删除',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `short_code`(`short_code` ASC) USING BTREE,
  INDEX `idx_short_code`(`short_code` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '短链接表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of short_link
-- ----------------------------

-- ----------------------------
-- Table structure for short_link_record
-- ----------------------------
DROP TABLE IF EXISTS `short_link_record`;
CREATE TABLE `short_link_record`  (
  `id` bigint UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `short_code` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '短链接代码',
  `user_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '用户ID',
  `access_time` bigint NOT NULL COMMENT '访问时间',
  `ip_address` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT 'IP地址',
  `login_method` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '登录方式',
  `access_source` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '访问来源',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '最后更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_short_code`(`short_code` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '短链接记录表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of short_link_record
-- ----------------------------

-- ----------------------------
-- Table structure for tag
-- ----------------------------
DROP TABLE IF EXISTS `tag`;
CREATE TABLE `tag`  (
  `id` int UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `tag_name` varchar(120) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '标签名称',
  `tag_type` tinyint NOT NULL DEFAULT 1 COMMENT '标签类型：1-系统标签，2-自定义标签',
  `category_id` int UNSIGNED NOT NULL DEFAULT 0 COMMENT '类目ID',
  `status` tinyint NOT NULL DEFAULT 0 COMMENT '状态：0-未发布，1-已发布',
  `deleted` tinyint NOT NULL DEFAULT 0 COMMENT '是否删除',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '最后更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_category_id`(`category_id` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 213 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '标签管理表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of tag
-- ----------------------------

-- ----------------------------
-- Table structure for user
-- ----------------------------
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user`  (
  `id` bigint UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `third_account_id` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '' COMMENT '第三方用户ID',
  `user_name` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '' COMMENT '用户名',
  `password` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '' COMMENT '密码',
  `login_type` tinyint NOT NULL DEFAULT 0 COMMENT '登录方式: 0-微信登录，1-账号密码登录',
  `session` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '登录会话标识',
  `deleted` tinyint NOT NULL DEFAULT 0 COMMENT '是否删除',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '最后更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `key_third_account_id`(`third_account_id` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 15 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '用户登录表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of user
-- ----------------------------

-- ----------------------------
-- Table structure for user_ai
-- ----------------------------
DROP TABLE IF EXISTS `user_ai`;
CREATE TABLE `user_ai`  (
  `id` bigint UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `user_id` bigint UNSIGNED NOT NULL DEFAULT 0 COMMENT '用户id',
  `star_number` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '0' COMMENT '知识星球编号',
  `star_type` tinyint NOT NULL DEFAULT 0 COMMENT '星球来源 1=java进阶之路 2=Tech Forum',
  `inviter_user_id` bigint NOT NULL DEFAULT 0 COMMENT '当前用户绑定的邀请者',
  `invite_code` varchar(8) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '' COMMENT '邀请码',
  `invite_num` int NOT NULL DEFAULT 0 COMMENT '当前用户邀请的人数',
  `state` int NOT NULL DEFAULT 0 COMMENT '0 审核中 1 试用中 2 审核通过 3 审核拒绝',
  `strategy` int NOT NULL DEFAULT 0 COMMENT '二进制表示法：0位绑定微信公众号 1位 绑定邀请 2位绑定java进阶星球 3位绑定Tech Forum星球',
  `deleted` tinyint NOT NULL DEFAULT 0 COMMENT '是否删除',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '最后更新时间',
  `star_expire_time` datetime NULL DEFAULT NULL COMMENT '星球账号过期时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_user_id`(`user_id` ASC) USING BTREE,
  INDEX `idx_inviter_user_id`(`inviter_user_id` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = 'AI聊天运营策略信息表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of user_ai
-- ----------------------------

-- ----------------------------
-- Table structure for user_ai_history
-- ----------------------------
DROP TABLE IF EXISTS `user_ai_history`;
CREATE TABLE `user_ai_history`  (
  `id` bigint UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `user_id` bigint UNSIGNED NOT NULL DEFAULT 0 COMMENT '用户id',
  `ai_type` tinyint NOT NULL DEFAULT 0 COMMENT '使用的AI类型 0=Tech Forum 1=chatgpt3.5  2=chatgpt4 3=讯飞',
  `question` varchar(512) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '' COMMENT '问题',
  `answer` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL COMMENT '回答',
  `deleted` tinyint NOT NULL DEFAULT 0 COMMENT '是否删除',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '最后更新时间',
  `chat_id` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '聊天id',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_user_id_ai_type`(`user_id` ASC, `ai_type` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '用户与AI的聊天历史' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of user_ai_history
-- ----------------------------

-- ----------------------------
-- Table structure for user_follow
-- ----------------------------
DROP TABLE IF EXISTS `user_follow`;
CREATE TABLE `user_follow`  (
  `id` bigint UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `follower_id` bigint UNSIGNED NOT NULL DEFAULT 0 COMMENT '粉丝用户ID',
  `following_id` bigint UNSIGNED NOT NULL DEFAULT 0 COMMENT '被关注的用户ID',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '关注时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_follower_following`(`follower_id` ASC, `following_id` ASC) USING BTREE,
  INDEX `idx_follower`(`follower_id` ASC) USING BTREE,
  INDEX `idx_following`(`following_id` ASC) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '用户关注表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of user_follow
-- ----------------------------

-- ----------------------------
-- Table structure for user_foot
-- ----------------------------
DROP TABLE IF EXISTS `user_foot`;
CREATE TABLE `user_foot`  (
  `id` bigint UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `user_id` bigint UNSIGNED NOT NULL DEFAULT 0 COMMENT '用户id',
  `document_id` bigint UNSIGNED NOT NULL DEFAULT 0 COMMENT '文档ID（文章/评论）',
  `document_type` tinyint NOT NULL DEFAULT 1 COMMENT '文档类型：1-文章，2-评论',
  `document_user_id` bigint UNSIGNED NOT NULL DEFAULT 0 COMMENT '发布该文档的用户ID',
  `collection_stat` tinyint UNSIGNED NOT NULL DEFAULT 0 COMMENT '收藏状态: 0-未收藏，1-已收藏，2-取消收藏',
  `read_stat` tinyint UNSIGNED NOT NULL DEFAULT 0 COMMENT '阅读状态: 0-未读，1-已读',
  `comment_stat` tinyint UNSIGNED NOT NULL DEFAULT 0 COMMENT '评论状态: 0-未评论，1-已评论，2-删除评论',
  `praise_stat` tinyint UNSIGNED NOT NULL DEFAULT 0 COMMENT '点赞状态: 0-未点赞，1-已点赞，2-取消点赞',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '最后更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `idx_user_doucument`(`user_id` ASC, `document_id` ASC, `document_type` ASC) USING BTREE,
  INDEX `idx_doucument_id`(`document_id` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 11 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '用户足迹表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of user_foot
-- ----------------------------

-- ----------------------------
-- Table structure for user_info
-- ----------------------------
DROP TABLE IF EXISTS `user_info`;
CREATE TABLE `user_info`  (
  `id` bigint UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `user_id` bigint UNSIGNED NOT NULL DEFAULT 0 COMMENT '用户id',
  `user_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '' COMMENT '用户名',
  `photo` varchar(512) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '' COMMENT '用户图像',
  `position` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '' COMMENT '职位',
  `company` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '' COMMENT '公司',
  `profile` varchar(225) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '' COMMENT '个人简介',
  `user_role` int NOT NULL DEFAULT 0 COMMENT '0 普通用户 1 超管',
  `extend` varchar(1024) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '' COMMENT '扩展字段',
  `ip` json NOT NULL COMMENT '用户的ip信息',
  `deleted` tinyint NOT NULL DEFAULT 0 COMMENT '是否删除',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '最后更新时间',
  `email` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '' COMMENT '用户邮箱',
  `pay_code` varchar(1024) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '' COMMENT '聚合收款码',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `key_user_id`(`user_id` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 12 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '用户个人信息表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of user_info
-- ----------------------------

-- ----------------------------
-- Table structure for user_relation
-- ----------------------------
DROP TABLE IF EXISTS `user_relation`;
CREATE TABLE `user_relation`  (
  `id` bigint UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `user_id` bigint UNSIGNED NOT NULL DEFAULT 0 COMMENT '用户id',
  `follow_user_id` bigint UNSIGNED NOT NULL DEFAULT 0 COMMENT '用户id',
  `follow_state` tinyint UNSIGNED NOT NULL DEFAULT 0 COMMENT '阅读状态: 0-未关注，1-已关注，2-取消关注',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '最后更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_user_follow`(`user_id` ASC, `follow_user_id` ASC) USING BTREE,
  INDEX `key_follow_user_id`(`follow_user_id` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 5 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '用户关系表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of user_relation
-- ----------------------------

SET FOREIGN_KEY_CHECKS = 1;
