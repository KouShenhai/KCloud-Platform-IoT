/*
 Navicat Premium Data Transfer

 Source Server         : 192.168.1.100（老寇）
 Source Server Type    : MySQL
 Source Server Version : 80033 (8.0.33)
 Source Host           : 192.168.1.100:3306
 Source Schema         : kcloud_platform_alibaba_tenant

 Target Server Type    : MySQL
 Target Server Version : 80033 (8.0.33)
 File Encoding         : 65001

 Date: 24/06/2023 22:41:49
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for boot_sys_dict
-- ----------------------------
DROP TABLE IF EXISTS `boot_sys_dict`;
CREATE TABLE `boot_sys_dict`  (
                                  `id` bigint NOT NULL AUTO_INCREMENT COMMENT 'id',
                                  `creator` bigint NULL DEFAULT NULL COMMENT '创建者',
                                  `create_date` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                                  `update_date` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '修改时间',
                                  `del_flag` tinyint(1) NOT NULL DEFAULT 0 COMMENT '1已删除 0未删除',
                                  `editor` bigint NULL DEFAULT NULL COMMENT '编辑人',
                                  `dict_value` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '值',
                                  `dict_label` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '标签',
                                  `type` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '类型',
                                  `remark` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '备注',
                                  `sort` int NULL DEFAULT 1 COMMENT '排序',
                                  `version` int NOT NULL DEFAULT 0 COMMENT '版本号',
                                  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1578676566256455695 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '字典' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of boot_sys_dict
-- ----------------------------
INSERT INTO `boot_sys_dict` VALUES (1578676566256455694, 1537114827246292998, '2023-02-15 13:36:31', '2023-02-15 14:37:43', 0, 1537114827246292998, '123', '多租户字典名称', 'TENANT_TYPE', '多租户字典测试', 10, 1);

-- ----------------------------
-- Table structure for boot_sys_message
-- ----------------------------
DROP TABLE IF EXISTS `boot_sys_message`;
CREATE TABLE `boot_sys_message`  (
                                     `id` bigint NOT NULL AUTO_INCREMENT COMMENT 'id',
                                     `title` varchar(400) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '标题',
                                     `content` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '内容',
                                     `send_channel` tinyint NULL DEFAULT NULL COMMENT '发送渠道：0平台 1微信公众号 2邮箱',
                                     `creator` bigint NULL DEFAULT NULL COMMENT '创建人',
                                     `editor` bigint NULL DEFAULT NULL COMMENT '编辑人',
                                     `create_date` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                                     `update_date` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '修改时间',
                                     `del_flag` tinyint(1) NOT NULL DEFAULT 0 COMMENT '1已删除 0未删除',
                                     `type` tinyint NULL DEFAULT 0 COMMENT '0通知 1提醒',
                                     `version` int NOT NULL DEFAULT 0 COMMENT '版本号',
                                     PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1587737320788005183 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '消息' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of boot_sys_message
-- ----------------------------
INSERT INTO `boot_sys_message` VALUES (1587737320788005179, '测试', '## 二级标题测试', 0, 1537114827246292998, NULL, '2023-02-15 14:50:31', '2023-02-15 14:50:31', 0, 0, 0);
INSERT INTO `boot_sys_message` VALUES (1587737320788005182, '测试', '测试', 0, 1537114827246293001, NULL, '2023-02-15 15:58:47', '2023-02-15 15:58:47', 0, 0, 0);

-- ----------------------------
-- Table structure for boot_sys_message_detail
-- ----------------------------
DROP TABLE IF EXISTS `boot_sys_message_detail`;
CREATE TABLE `boot_sys_message_detail`  (
                                            `id` bigint NOT NULL AUTO_INCREMENT COMMENT 'id',
                                            `message_id` bigint NULL DEFAULT NULL COMMENT '消息id',
                                            `user_id` bigint NULL DEFAULT NULL COMMENT '用户id',
                                            `creator` bigint NULL DEFAULT NULL COMMENT '创建人',
                                            `editor` bigint NULL DEFAULT NULL COMMENT '编辑人',
                                            `create_date` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                                            `update_date` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '修改时间',
                                            `del_flag` tinyint(1) NOT NULL DEFAULT 0 COMMENT '1已删除 0未删除',
                                            `read_flag` tinyint(1) NOT NULL DEFAULT 0 COMMENT '是否已读 0未读 1已读',
                                            `version` int NOT NULL DEFAULT 0 COMMENT '版本号',
                                            PRIMARY KEY (`id`) USING BTREE,
                                            INDEX `idx_read_flag_user_id`(`read_flag` ASC, `user_id` ASC) USING BTREE COMMENT '已读_用户编号_索引'
) ENGINE = InnoDB AUTO_INCREMENT = 1587737321048052083 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '消息详情' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of boot_sys_message_detail
-- ----------------------------
INSERT INTO `boot_sys_message_detail` VALUES (1587737321048052078, 1587737320788005179, 1537114827246292998, 1537114827246292998, NULL, '2023-02-15 14:50:32', '2023-02-15 14:50:32', 0, 1, 1);
INSERT INTO `boot_sys_message_detail` VALUES (1587737321048052082, 1587737320788005182, 1537114827246293001, 1537114827246293001, NULL, '2023-02-15 15:58:47', '2023-02-15 15:58:47', 0, 1, 1);

-- ----------------------------
-- Table structure for boot_sys_oss
-- ----------------------------
DROP TABLE IF EXISTS `boot_sys_oss`;
CREATE TABLE `boot_sys_oss`  (
                                 `id` bigint NOT NULL AUTO_INCREMENT COMMENT 'id',
                                 `creator` bigint NULL DEFAULT NULL COMMENT '创建人',
                                 `editor` bigint NULL DEFAULT NULL COMMENT '编辑人',
                                 `create_date` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                                 `update_date` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '修改时间',
                                 `del_flag` tinyint(1) NOT NULL DEFAULT 0 COMMENT '1已删除 0未删除',
                                 `name` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '名称',
                                 `endpoint` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '终端地址',
                                 `region` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '区域',
                                 `access_key` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '访问密钥',
                                 `secret_key` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '用户密钥',
                                 `bucket_name` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '桶名',
                                 `path_style_access_enabled` tinyint(1) NULL DEFAULT NULL COMMENT '路径样式访问 1已开启 0未启用',
                                 `status` tinyint(1) NULL DEFAULT NULL COMMENT '状态 1已启用 0未启用',
                                 `version` int NOT NULL DEFAULT 0 COMMENT '版本号',
                                 PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1537444981390794757 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '对象存储' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of boot_sys_oss
-- ----------------------------
INSERT INTO `boot_sys_oss` VALUES (1537444981390794754, 1341620898007281665, 1341620898007281665, '2022-11-02 14:35:46', '2023-02-13 18:11:15', 0, 'Aliyun OSS', 'https://oss-cn-shenzhen.aliyuncs.com', '', 'LTAIPIOiuCK4ZK3o', 'dtYg8khtfEUM3FlV2FvVH6bHyezdqi', 'koushenhai', 0, 1, 22);

-- ----------------------------
-- Table structure for boot_sys_oss_log
-- ----------------------------
DROP TABLE IF EXISTS `boot_sys_oss_log`;
CREATE TABLE `boot_sys_oss_log`  (
                                     `id` bigint NOT NULL AUTO_INCREMENT COMMENT 'id',
                                     `creator` bigint NULL DEFAULT NULL COMMENT '创建人',
                                     `editor` bigint NULL DEFAULT NULL COMMENT '编辑人',
                                     `create_date` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                                     `update_date` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '修改时间',
                                     `del_flag` tinyint(1) NULL DEFAULT 0 COMMENT '1已删除 0未删除',
                                     `md5` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT 'md5标识',
                                     `url` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '路径',
                                     `file_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '文件名称',
                                     `file_size` bigint NULL DEFAULT NULL COMMENT '文件大小',
                                     `version` int NOT NULL DEFAULT 0 COMMENT '版本号',
                                     PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 3 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '存储日志' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of boot_sys_oss_log
-- ----------------------------

SET FOREIGN_KEY_CHECKS = 1;
