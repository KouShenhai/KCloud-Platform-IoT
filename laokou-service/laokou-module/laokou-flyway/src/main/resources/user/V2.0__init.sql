/*
 Navicat Premium Data Transfer

 Source Server         : 192.168.30.133(老寇）
 Source Server Type    : MySQL
 Source Server Version : 80033 (8.0.33)
 Source Host           : 192.168.30.133:3306
 Source Schema         : kcloud_platform_alibaba_user

 Target Server Type    : MySQL
 Target Server Version : 80033 (8.0.33)
 File Encoding         : 65001

 Date: 29/09/2023 00:33:49
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for boot_sys_user_202201
-- ----------------------------
DROP TABLE IF EXISTS `boot_sys_user_202201`;
CREATE TABLE `boot_sys_user_202201`  (
                                         `id` bigint NOT NULL AUTO_INCREMENT COMMENT 'ID',
                                         `creator` bigint NOT NULL DEFAULT 0 COMMENT '创建人',
                                         `editor` bigint NOT NULL DEFAULT 0 COMMENT '编辑人',
                                         `create_date` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                                         `update_date` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
                                         `del_flag` tinyint(1) NOT NULL DEFAULT 0 COMMENT '删除标识 0未删除 1已删除',
                                         `version` int NOT NULL DEFAULT 0 COMMENT '版本号',
                                         `dept_id` bigint NOT NULL DEFAULT 0 COMMENT '部门ID',
                                         `dept_path` varchar(1000) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '0' COMMENT '部门PATH',
                                         `tenant_id` bigint NOT NULL DEFAULT 0 COMMENT '租户ID',
                                         `username` varchar(30) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL COMMENT '用户名',
                                         `password` varchar(100) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL COMMENT '密码',
                                         `super_admin` tinyint(1) NOT NULL DEFAULT 0 COMMENT '超级管理员标识 0否 1是',
                                         `mail` varchar(50) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '邮箱',
                                         `status` tinyint(1) NOT NULL DEFAULT 0 COMMENT '状态 0正常 1锁定',
                                         `avatar` varchar(400) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL DEFAULT 'https://i.postimg.cc/FsHgVKzX/1.gif' COMMENT '头像',
                                         `mobile` varchar(30) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '手机号',
                                         PRIMARY KEY (`id`) USING BTREE,
                                         UNIQUE INDEX `idx_tenant_id_username_boot_sys_user_202201`(`tenant_id` ASC, `username` ASC) USING BTREE COMMENT '租户_用户名_唯一索引',
                                         INDEX `idx_tenant_id_mail_boot_sys_user_202201`(`tenant_id` ASC, `mail` ASC) USING BTREE COMMENT '租户_邮箱_唯一索引',
                                         INDEX `idx_tenant_id_mobile_boot_sys_user_202201`(`tenant_id` ASC, `mobile` ASC) USING BTREE COMMENT '租户_手机号_唯一索引'
) ENGINE = InnoDB AUTO_INCREMENT = 1707428076784287746 CHARACTER SET = utf8mb3 COLLATE = utf8mb3_general_ci COMMENT = '用户' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for boot_sys_user_202206
-- ----------------------------
DROP TABLE IF EXISTS `boot_sys_user_202206`;
CREATE TABLE `boot_sys_user_202206`  (
                                         `id` bigint NOT NULL AUTO_INCREMENT COMMENT 'ID',
                                         `creator` bigint NOT NULL DEFAULT 0 COMMENT '创建人',
                                         `editor` bigint NOT NULL DEFAULT 0 COMMENT '编辑人',
                                         `create_date` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                                         `update_date` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
                                         `del_flag` tinyint(1) NOT NULL DEFAULT 0 COMMENT '删除标识 0未删除 1已删除',
                                         `version` int NOT NULL DEFAULT 0 COMMENT '版本号',
                                         `dept_id` bigint NOT NULL DEFAULT 0 COMMENT '部门ID',
                                         `dept_path` varchar(1000) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '0' COMMENT '部门PATH',
                                         `tenant_id` bigint NOT NULL DEFAULT 0 COMMENT '租户ID',
                                         `username` varchar(30) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL COMMENT '用户名',
                                         `password` varchar(100) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL COMMENT '密码',
                                         `super_admin` tinyint(1) NOT NULL DEFAULT 0 COMMENT '超级管理员标识 0否 1是',
                                         `mail` varchar(50) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '邮箱',
                                         `status` tinyint(1) NOT NULL DEFAULT 0 COMMENT '状态 0正常 1锁定',
                                         `avatar` varchar(400) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL DEFAULT 'https://i.postimg.cc/FsHgVKzX/1.gif' COMMENT '头像',
                                         `mobile` varchar(30) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '手机号',
                                         PRIMARY KEY (`id`) USING BTREE,
                                         UNIQUE INDEX `idx_tenant_id_username_boot_sys_user_202206`(`tenant_id` ASC, `username` ASC) USING BTREE COMMENT '租户_用户名_唯一索引',
                                         INDEX `idx_tenant_id_mail_boot_sys_user_202206`(`tenant_id` ASC, `mail` ASC) USING BTREE COMMENT '租户_邮箱_唯一索引',
                                         INDEX `idx_tenant_id_mobile_boot_sys_user_202206`(`tenant_id` ASC, `mobile` ASC) USING BTREE COMMENT '租户_手机号_唯一索引'
) ENGINE = InnoDB AUTO_INCREMENT = 1707428077144997890 CHARACTER SET = utf8mb3 COLLATE = utf8mb3_general_ci COMMENT = '用户' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for boot_sys_user_202302
-- ----------------------------
DROP TABLE IF EXISTS `boot_sys_user_202302`;
CREATE TABLE `boot_sys_user_202302`  (
                                         `id` bigint NOT NULL AUTO_INCREMENT COMMENT 'ID',
                                         `creator` bigint NOT NULL DEFAULT 0 COMMENT '创建人',
                                         `editor` bigint NOT NULL DEFAULT 0 COMMENT '编辑人',
                                         `create_date` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                                         `update_date` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
                                         `del_flag` tinyint(1) NOT NULL DEFAULT 0 COMMENT '删除标识 0未删除 1已删除',
                                         `version` int NOT NULL DEFAULT 0 COMMENT '版本号',
                                         `dept_id` bigint NOT NULL DEFAULT 0 COMMENT '部门ID',
                                         `dept_path` varchar(1000) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '0' COMMENT '部门PATH',
                                         `tenant_id` bigint NOT NULL DEFAULT 0 COMMENT '租户ID',
                                         `username` varchar(30) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL COMMENT '用户名',
                                         `password` varchar(100) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL COMMENT '密码',
                                         `super_admin` tinyint(1) NOT NULL DEFAULT 0 COMMENT '超级管理员标识 0否 1是',
                                         `mail` varchar(50) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '邮箱',
                                         `status` tinyint(1) NOT NULL DEFAULT 0 COMMENT '状态 0正常 1锁定',
                                         `avatar` varchar(400) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL DEFAULT 'https://i.postimg.cc/FsHgVKzX/1.gif' COMMENT '头像',
                                         `mobile` varchar(30) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '手机号',
                                         PRIMARY KEY (`id`) USING BTREE,
                                         UNIQUE INDEX `idx_tenant_id_username_boot_sys_user_202302`(`tenant_id` ASC, `username` ASC) USING BTREE COMMENT '租户_用户名_唯一索引',
                                         INDEX `idx_tenant_id_mail_boot_sys_user_202302`(`tenant_id` ASC, `mail` ASC) USING BTREE COMMENT '租户_邮箱_唯一索引',
                                         INDEX `idx_tenant_id_mobile_boot_sys_user_202302`(`tenant_id` ASC, `mobile` ASC) USING BTREE COMMENT '租户_手机号_唯一索引'
) ENGINE = InnoDB AUTO_INCREMENT = 1707428077488930818 CHARACTER SET = utf8mb3 COLLATE = utf8mb3_general_ci COMMENT = '用户' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for boot_sys_user_202303
-- ----------------------------
DROP TABLE IF EXISTS `boot_sys_user_202303`;
CREATE TABLE `boot_sys_user_202303`  (
                                         `id` bigint NOT NULL AUTO_INCREMENT COMMENT 'ID',
                                         `creator` bigint NOT NULL DEFAULT 0 COMMENT '创建人',
                                         `editor` bigint NOT NULL DEFAULT 0 COMMENT '编辑人',
                                         `create_date` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                                         `update_date` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
                                         `del_flag` tinyint(1) NOT NULL DEFAULT 0 COMMENT '删除标识 0未删除 1已删除',
                                         `version` int NOT NULL DEFAULT 0 COMMENT '版本号',
                                         `dept_id` bigint NOT NULL DEFAULT 0 COMMENT '部门ID',
                                         `dept_path` varchar(1000) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '0' COMMENT '部门PATH',
                                         `tenant_id` bigint NOT NULL DEFAULT 0 COMMENT '租户ID',
                                         `username` varchar(30) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL COMMENT '用户名',
                                         `password` varchar(100) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL COMMENT '密码',
                                         `super_admin` tinyint(1) NOT NULL DEFAULT 0 COMMENT '超级管理员标识 0否 1是',
                                         `mail` varchar(50) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '邮箱',
                                         `status` tinyint(1) NOT NULL DEFAULT 0 COMMENT '状态 0正常 1锁定',
                                         `avatar` varchar(400) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL DEFAULT 'https://i.postimg.cc/FsHgVKzX/1.gif' COMMENT '头像',
                                         `mobile` varchar(30) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '手机号',
                                         PRIMARY KEY (`id`) USING BTREE,
                                         UNIQUE INDEX `idx_tenant_id_username_boot_sys_user_202303`(`tenant_id` ASC, `username` ASC) USING BTREE COMMENT '租户_用户名_唯一索引',
                                         INDEX `idx_tenant_id_mail_boot_sys_user_202303`(`tenant_id` ASC, `mail` ASC) USING BTREE COMMENT '租户_邮箱_唯一索引',
                                         INDEX `idx_tenant_id_mobile_boot_sys_user_202303`(`tenant_id` ASC, `mobile` ASC) USING BTREE COMMENT '租户_手机号_唯一索引'
) ENGINE = InnoDB AUTO_INCREMENT = 1707428076452937731 CHARACTER SET = utf8mb3 COLLATE = utf8mb3_general_ci COMMENT = '用户' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for boot_sys_user_202309
-- ----------------------------
DROP TABLE IF EXISTS `boot_sys_user_202309`;
CREATE TABLE `boot_sys_user_202309`  (
                                         `id` bigint NOT NULL AUTO_INCREMENT COMMENT 'ID',
                                         `creator` bigint NOT NULL DEFAULT 0 COMMENT '创建人',
                                         `editor` bigint NOT NULL DEFAULT 0 COMMENT '编辑人',
                                         `create_date` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                                         `update_date` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
                                         `del_flag` tinyint(1) NOT NULL DEFAULT 0 COMMENT '删除标识 0未删除 1已删除',
                                         `version` int NOT NULL DEFAULT 0 COMMENT '版本号',
                                         `dept_id` bigint NOT NULL DEFAULT 0 COMMENT '部门ID',
                                         `dept_path` varchar(1000) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '0' COMMENT '部门PATH',
                                         `tenant_id` bigint NOT NULL DEFAULT 0 COMMENT '租户ID',
                                         `username` varchar(30) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL COMMENT '用户名',
                                         `password` varchar(100) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL COMMENT '密码',
                                         `super_admin` tinyint(1) NOT NULL DEFAULT 0 COMMENT '超级管理员标识 0否 1是',
                                         `mail` varchar(50) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '邮箱',
                                         `status` tinyint(1) NOT NULL DEFAULT 0 COMMENT '状态 0正常 1锁定',
                                         `avatar` varchar(400) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL DEFAULT 'https://i.postimg.cc/FsHgVKzX/1.gif' COMMENT '头像',
                                         `mobile` varchar(30) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '手机号',
                                         PRIMARY KEY (`id`) USING BTREE,
                                         UNIQUE INDEX `idx_tenant_id_username_boot_sys_user_202309`(`tenant_id` ASC, `username` ASC) USING BTREE COMMENT '租户_用户名_唯一索引',
                                         INDEX `idx_tenant_id_mail_boot_sys_user_202309`(`tenant_id` ASC, `mail` ASC) USING BTREE COMMENT '租户_邮箱_唯一索引',
                                         INDEX `idx_tenant_id_mobile_boot_sys_user_202309`(`tenant_id` ASC, `mobile` ASC) USING BTREE COMMENT '租户_手机号_唯一索引'
) ENGINE = InnoDB AUTO_INCREMENT = 1707428076079644674 CHARACTER SET = utf8mb3 COLLATE = utf8mb3_general_ci COMMENT = '用户' ROW_FORMAT = DYNAMIC;

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

SET FOREIGN_KEY_CHECKS = 1;

-- ----------------------------
-- Records of boot_sys_user_202201
-- ----------------------------
INSERT INTO `boot_sys_user_202201` VALUES (1707428076142559234, 1341620898007281665, 1341620898007281665, '2022-01-01 20:13:11', '2023-09-28 09:32:15', 0, 50, 1535887940687765505, '0,1535887940687765505', 0, '1ftkBJEIvmOFuxJygs2jnQ==', '{bcrypt}$2a$10$bGXM7u58FPMDanMyqvZ7Reb9sqJiUTCdAcb1wN5IIkFa8nYOMOioK', 1, 'LkZpbZYqPmJRj54OG9P8xdgn8lmGt/9oxQT94xbsZyE=', 0, 'http://localhost:9000/laokou-minio-bucket/c10bbd207e3c47329ba02a0ef8162d50.jpg', 'i7KGWPr+sZqGBXO4JViAGg==');
INSERT INTO `boot_sys_user_202201` VALUES (1707428076784287745, 1341620898007281665, 1341620898007281665, '2022-01-31 11:29:35', '2023-09-28 09:32:23', 0, 13, 1535881356595175426, '0,1535887940687765505,1535881356595175426', 0, 'UZdko/elN7be8o8HlsNYDw==', '{bcrypt}$2a$10$ysAmruc249SiAUpIqQzrpeM8wcdpgIJ6nEdtsXQnDrBgvLZkt7tJ6', 0, '', 0, 'https://img2.baidu.com/it/u=2432885784,4104422384&fm=253&fmt=auto&app=138&f=JPEG?w=400&h=400', '');

-- ----------------------------
-- Records of boot_sys_user_202206
-- ----------------------------
INSERT INTO `boot_sys_user_202206` VALUES (1707428076855590914, 1341620898007281665, 1341620898007281665, '2022-06-16 00:33:39', '2023-09-25 04:55:45', 0, 23, 1535887940687765505, '0,1535887940687765505', 0, 'WPvtQBxvQwzVj5SJsKCifQ==', '{bcrypt}$2a$10$Wac.3sTE4A4pi/Zy6B/HWOstwLFjOH9g8Qrf4gHiBLa/avKAVcwpG', 0, '', 0, 'https://img2.baidu.com/it/u=2432885784,4104422384&fm=253&fmt=auto&app=138&f=JPEG?w=400&h=400', '');
INSERT INTO `boot_sys_user_202206` VALUES (1707428077144997889, 1341620898007281665, 1341620898007281665, '2022-06-16 00:48:28', '2023-09-17 13:39:06', 0, 71, 1535881356595175426, '0,1535887940687765505,1535881356595175426', 0, 'JSB4EWKd5aI/aISsDw0ODA==', '{bcrypt}$2a$10$cF89J1QesYz2sYghWbw6d./ly/zWK4yH8ehr2/l33UvtD3sCNHh.i', 0, '', 0, 'https://img2.baidu.com/it/u=2432885784,4104422384&fm=253&fmt=auto&app=138&f=JPEG?w=400&h=400', '');

-- ----------------------------
-- Records of boot_sys_user_202302
-- ----------------------------
INSERT INTO `boot_sys_user_202302` VALUES (1707428077199523842, 1341620898007281665, 1341620898007281665, '2023-02-09 22:59:04', '2023-02-09 22:59:04', 0, 0, 0, '0', 1, '5/Pqo/yVzE72YyPDE5RKAw==', '{bcrypt}$2a$10$ToBq5JB191IUkAfnqfeV5OFLOFDvhr9wWaRm1LhTn5sbL8uyJ0Gre', 1, '', 0, 'https://img2.baidu.com/it/u=2432885784,4104422384&fm=253&fmt=auto&app=138&f=JPEG?w=400&h=400', '');
INSERT INTO `boot_sys_user_202302` VALUES (1707428077488930817, 1537114827246292998, 1537114827246292998, '2023-02-15 13:18:39', '2023-02-15 13:18:39', 0, 0, 1584488411756171278, '0', 1, 'cmV6CFYc1NUWgni0E8xpdg==', '{bcrypt}$2a$10$nbLXUQeCfuiw.7wZwuOT.e0r1mr.ZQcLIlFbil28PCrPBNAnPLRT.', 0, '', 0, 'https://img2.baidu.com/it/u=2432885784,4104422384&fm=253&fmt=auto&app=138&f=JPEG?w=400&h=400', '');

-- ----------------------------
-- Records of boot_sys_user_202303
-- ----------------------------
INSERT INTO `boot_sys_user_202303` VALUES (1707428076452937730, 1341620898007281665, 1341620898007281665, '2023-03-09 13:52:04', '2023-09-22 03:20:30', 0, 75, 1535887940687765505, '0,1535887940687765505', 0, 'xa1Nz1ENwGc9qWoUseZn6A==', '{bcrypt}$2a$10$.GMEgS3MXRzAZfni2pyrbO2jPpYPvwphk/yIiCNPQZBMWRUwg4Gt2', 0, '', 0, 'http://127.0.0.1:81/upload/node2/b4e5bb3944a046a6bb54f8bfd2c830c1.webp', '');

-- ----------------------------
-- Records of boot_sys_user_202309
-- ----------------------------
INSERT INTO `boot_sys_user_202309` VALUES (1707428069997903873, 1341620898007281665, 910193112402362368, '2023-09-17 15:42:27', '2023-09-25 05:05:57', 0, 1, 1535887940687765505, '0,1535887940687765505', 1703312526740615171, '5/Pqo/yVzE72YyPDE5RKAw==', '{bcrypt}$2a$10$tfrR0lGv5RChP27iEKrxoO9/g7vtaUdSXqb3vnzfe.GiDmJVBO1Fq', 1, '', 0, 'https://i.postimg.cc/FsHgVKzX/1.gif', '');
INSERT INTO `boot_sys_user_202309` VALUES (1707428076079644673, 910193112402362368, 910193112402362368, '2023-09-23 20:13:09', '2023-09-23 20:13:09', 0, 0, 1535887940687765505, '0,1535887940687765505', 1703312526740615171, 'cSjZ+Jz+mwGui+vPZ1BxGw==', '{bcrypt}$2a$10$iTfk2uTCO6EOsqtBtaj4p.e.c3g9t5r/QT9P4MWSm7datxLoPFyuC', 0, '', 0, 'https://i.postimg.cc/FsHgVKzX/1.gif', '');