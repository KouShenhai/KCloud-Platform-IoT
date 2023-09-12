/*
 Navicat Premium Data Transfer

 Source Server         : 192.168.30.133(老寇）
 Source Server Type    : MySQL
 Source Server Version : 80033 (8.0.33)
 Source Host           : 192.168.30.133:3306
 Source Schema         : kcloud_platform_alibaba_user_0

 Target Server Type    : MySQL
 Target Server Version : 80033 (8.0.33)
 File Encoding         : 65001

 Date: 12/09/2023 15:51:01
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for boot_sys_user_0
-- ----------------------------
DROP TABLE IF EXISTS `boot_sys_user_0`;
CREATE TABLE `boot_sys_user_0`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `creator` bigint NOT NULL COMMENT '创建人',
  `editor` bigint NULL DEFAULT NULL COMMENT '编辑人',
  `create_date` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_date` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '修改时间',
  `del_flag` tinyint(1) NOT NULL DEFAULT 0 COMMENT '删除标识 1已删除 0未删除',
  `version` int NOT NULL DEFAULT 0 COMMENT '版本号',
  `dept_id` bigint NOT NULL DEFAULT 0 COMMENT '部门ID',
  `tenant_id` bigint NOT NULL DEFAULT 0 COMMENT '租户ID',
  `username` varchar(30) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL COMMENT '用户名',
  `password` varchar(100) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL COMMENT '密码',
  `super_admin` tinyint(1) NOT NULL DEFAULT 0 COMMENT '超级管理员标识 0否 1是',
  `mail` varchar(50) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '邮箱',
  `status` tinyint(1) NOT NULL DEFAULT 0 COMMENT '状态 0正常 1锁定',
  `avatar` varchar(400) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL DEFAULT 'https://i.postimg.cc/FsHgVKzX/1.gif' COMMENT '头像',
  `mobile` varchar(30) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '手机号',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `idx_tenant_id_username`(`tenant_id` ASC, `username` ASC) USING BTREE COMMENT '租户_用户名_唯一索引',
  UNIQUE INDEX `idx_tenant_id_mail`(`tenant_id` ASC, `mail` ASC) USING BTREE COMMENT '租户_邮箱_唯一索引',
  UNIQUE INDEX `idx_tenant_id_mobile`(`tenant_id` ASC, `mobile` ASC) USING BTREE COMMENT '租户_手机号_唯一索引'
) ENGINE = InnoDB AUTO_INCREMENT = 1537114827246292995 CHARACTER SET = utf8mb3 COLLATE = utf8mb3_general_ci COMMENT = '用户' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of boot_sys_user_0
-- ----------------------------
INSERT INTO `boot_sys_user_0` VALUES (1421312053736804354, 1341620898007281665, 1341620898007281665, '2021-07-31 11:29:35', '2022-11-04 00:13:05', 0, 11, 1535881356595175426, 0, 'UZdko/elN7be8o8HlsNYDw==', '{bcrypt}$2a$10$ysAmruc249SiAUpIqQzrpeM8wcdpgIJ6nEdtsXQnDrBgvLZkt7tJ6', 0, '', 0, 'https://img2.baidu.com/it/u=2432885784,4104422384&fm=253&fmt=auto&app=138&f=JPEG?w=400&h=400', '');
INSERT INTO `boot_sys_user_0` VALUES (1537114827246292994, 1341620898007281665, 1341620898007281665, '2022-06-16 00:48:28', '2022-10-26 19:40:13', 0, 38, 1584488411756171266, 0, 'JSB4EWKd5aI/aISsDw0ODA==', '{bcrypt}$2a$10$cF89J1QesYz2sYghWbw6d./ly/zWK4yH8ehr2/l33UvtD3sCNHh.i', 0, NULL, 0, 'https://img2.baidu.com/it/u=2432885784,4104422384&fm=253&fmt=auto&app=138&f=JPEG?w=400&h=400', NULL);

-- ----------------------------
-- Table structure for boot_sys_user_1
-- ----------------------------
DROP TABLE IF EXISTS `boot_sys_user_1`;
CREATE TABLE `boot_sys_user_1`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `creator` bigint NOT NULL COMMENT '创建人',
  `editor` bigint NULL DEFAULT NULL COMMENT '编辑人',
  `create_date` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_date` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '修改时间',
  `del_flag` tinyint(1) NOT NULL DEFAULT 0 COMMENT '删除标识 1已删除 0未删除',
  `version` int NOT NULL DEFAULT 0 COMMENT '版本号',
  `dept_id` bigint NOT NULL DEFAULT 0 COMMENT '部门ID',
  `tenant_id` bigint NOT NULL DEFAULT 0 COMMENT '租户ID',
  `username` varchar(30) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL COMMENT '用户名',
  `password` varchar(100) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL COMMENT '密码',
  `super_admin` tinyint(1) NOT NULL DEFAULT 0 COMMENT '超级管理员标识 0否 1是',
  `mail` varchar(50) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '邮箱',
  `status` tinyint(1) NOT NULL DEFAULT 0 COMMENT '状态 0正常 1锁定',
  `avatar` varchar(400) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL DEFAULT 'https://i.postimg.cc/FsHgVKzX/1.gif' COMMENT '头像',
  `mobile` varchar(30) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '手机号',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `idx_tenant_id_username`(`tenant_id` ASC, `username` ASC) USING BTREE COMMENT '租户_用户名_唯一索引',
  UNIQUE INDEX `idx_tenant_id_mail`(`tenant_id` ASC, `mail` ASC) USING BTREE COMMENT '租户_邮箱_唯一索引',
  UNIQUE INDEX `idx_tenant_id_mobile`(`tenant_id` ASC, `mobile` ASC) USING BTREE COMMENT '租户_手机号_唯一索引'
) ENGINE = InnoDB AUTO_INCREMENT = 1537114827246292995 CHARACTER SET = utf8mb3 COLLATE = utf8mb3_general_ci COMMENT = '用户' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of boot_sys_user_1
-- ----------------------------
INSERT INTO `boot_sys_user_1` VALUES (1341620898007281665, 1341620898007281665, 1341620898007281665, '2021-11-29 20:13:11', '2022-10-26 19:24:55', 0, 33, 0, 0, '1ftkBJEIvmOFuxJygs2jnQ==', '{bcrypt}$2a$10$bGXM7u58FPMDanMyqvZ7Reb9sqJiUTCdAcb1wN5IIkFa8nYOMOioK', 1, 'LkZpbZYqPmJRj54OG9P8xdgn8lmGt/9oxQT94xbsZyE=', 0, 'http://127.0.0.1:81/upload/node3/7904fff1c08a4883b40f1ee0336017dc.webp', 'i7KGWPr+sZqGBXO4JViAGg==');
INSERT INTO `boot_sys_user_1` VALUES (1341623527018004481, 1341620898007281665, 1341620898007281665, '2023-03-09 13:52:04', '2023-03-09 13:52:04', 0, 72, 1584488411756171266, 0, 'xa1Nz1ENwGc9qWoUseZn6A==', '{bcrypt}$2a$10$.GMEgS3MXRzAZfni2pyrbO2jPpYPvwphk/yIiCNPQZBMWRUwg4Gt2', 0, NULL, 0, 'http://127.0.0.1:81/upload/node2/b4e5bb3944a046a6bb54f8bfd2c830c1.webp', NULL);
INSERT INTO `boot_sys_user_1` VALUES (1537111101311844353, 1341620898007281665, 1341620898007281665, '2022-06-16 00:33:39', '2022-10-24 18:40:20', 0, 19, 1535858679453085698, 0, 'WPvtQBxvQwzVj5SJsKCifQ==', '{bcrypt}$2a$10$Wac.3sTE4A4pi/Zy6B/HWOstwLFjOH9g8Qrf4gHiBLa/avKAVcwpG', 0, NULL, 0, 'https://img2.baidu.com/it/u=2432885784,4104422384&fm=253&fmt=auto&app=138&f=JPEG?w=400&h=400', NULL);

-- ----------------------------
-- Table structure for flyway_schema_history
-- ----------------------------
DROP TABLE IF EXISTS `flyway_schema_history`;
CREATE TABLE `flyway_schema_history`  (
  `installed_rank` int NOT NULL,
  `version` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `description` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `type` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `script` varchar(1000) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `checksum` int NULL DEFAULT NULL,
  `installed_by` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `installed_on` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `execution_time` int NOT NULL,
  `success` tinyint(1) NOT NULL,
  PRIMARY KEY (`installed_rank`) USING BTREE,
  INDEX `flyway_schema_history_s_idx`(`success` ASC) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of flyway_schema_history
-- ----------------------------
INSERT INTO `flyway_schema_history` VALUES (1, '1.0', 'init', 'SQL', 'V1.0__init.sql', 2066969004, 'root', '2023-06-30 22:59:43', 58, 1);

SET FOREIGN_KEY_CHECKS = 1;
