/*
 Navicat Premium Data Transfer

 Source Server         : 192.168.1.100（老寇）
 Source Server Type    : MySQL
 Source Server Version : 80033 (8.0.33)
 Source Host           : 192.168.1.100:3306
 Source Schema         : kcloud_platform_alibaba_user_0

 Target Server Type    : MySQL
 Target Server Version : 80033 (8.0.33)
 File Encoding         : 65001

 Date: 24/06/2023 22:44:09
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for boot_sys_user_0
-- ----------------------------
DROP TABLE IF EXISTS `boot_sys_user_0`;
CREATE TABLE `boot_sys_user_0`  (
                                    `id` bigint NOT NULL AUTO_INCREMENT COMMENT 'id',
                                    `username` varchar(30) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL COMMENT '用户名',
                                    `password` varchar(100) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL COMMENT '密码',
                                    `super_admin` tinyint(1) NOT NULL DEFAULT 0 COMMENT '超级管理员：0否 1是',
                                    `creator` bigint NULL DEFAULT NULL COMMENT '创建人',
                                    `editor` bigint NULL DEFAULT NULL COMMENT '编辑人',
                                    `create_date` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                                    `update_date` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '修改时间',
                                    `del_flag` tinyint(1) NOT NULL DEFAULT 0 COMMENT '1已删除 0未删除',
                                    `mail` varchar(50) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '邮箱',
                                    `status` tinyint(1) NOT NULL DEFAULT 0 COMMENT '状态 0正常 1锁定',
                                    `avatar` varchar(400) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL DEFAULT 'https://i.postimg.cc/FsHgVKzX/1.gif' COMMENT '头像',
                                    `mobile` varchar(30) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '手机号',
                                    `dept_id` bigint NOT NULL DEFAULT 0 COMMENT '部门id',
                                    `version` int NOT NULL DEFAULT 0 COMMENT '版本号',
                                    `tenant_id` bigint NOT NULL DEFAULT 0 COMMENT '租户id',
                                    PRIMARY KEY (`id`) USING BTREE,
                                    UNIQUE INDEX `idx_tenant_id_username`(`tenant_id` ASC, `username` ASC) USING BTREE COMMENT '租户_用户名_唯一索引',
                                    UNIQUE INDEX `idx_tenant_id_mail`(`tenant_id` ASC, `mail` ASC) USING BTREE COMMENT '租户_邮箱_唯一索引',
                                    UNIQUE INDEX `idx_tenant_id_mobile`(`tenant_id` ASC, `mobile` ASC) USING BTREE COMMENT '租户_手机号_唯一索引'
) ENGINE = InnoDB AUTO_INCREMENT = 1537114827246292995 CHARACTER SET = utf8mb3 COLLATE = utf8mb3_general_ci COMMENT = '用户' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of boot_sys_user_0
-- ----------------------------
INSERT INTO `boot_sys_user_0` VALUES (1421312053736804354, 'UZdko/elN7be8o8HlsNYDw==', '{bcrypt}$2a$10$ysAmruc249SiAUpIqQzrpeM8wcdpgIJ6nEdtsXQnDrBgvLZkt7tJ6', 0, 1341620898007281665, 1341620898007281665, '2021-07-31 11:29:35', '2022-11-04 00:13:05', 0, '', 0, 'https://img2.baidu.com/it/u=2432885784,4104422384&fm=253&fmt=auto&app=138&f=JPEG?w=400&h=400', '', 1535881356595175426, 8, 0);
INSERT INTO `boot_sys_user_0` VALUES (1537114827246292994, 'JSB4EWKd5aI/aISsDw0ODA==', '{bcrypt}$2a$10$b.40TGb7W19z5Jryo3FBuOEDaX2c0OAqZHnRnCkXCPI67ru5G7Nha', 0, 1341620898007281665, 1341620898007281665, '2022-06-16 00:48:28', '2022-10-26 19:40:13', 0, NULL, 0, 'https://img2.baidu.com/it/u=2432885784,4104422384&fm=253&fmt=auto&app=138&f=JPEG?w=400&h=400', NULL, 1535881356595175426, 25, 0);

-- ----------------------------
-- Table structure for boot_sys_user_1
-- ----------------------------
DROP TABLE IF EXISTS `boot_sys_user_1`;
CREATE TABLE `boot_sys_user_1`  (
                                    `id` bigint NOT NULL AUTO_INCREMENT COMMENT 'id',
                                    `username` varchar(30) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL COMMENT '用户名',
                                    `password` varchar(100) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL COMMENT '密码',
                                    `super_admin` tinyint(1) NOT NULL DEFAULT 0 COMMENT '超级管理员：0否 1是',
                                    `creator` bigint NULL DEFAULT NULL COMMENT '创建人',
                                    `editor` bigint NULL DEFAULT NULL COMMENT '编辑人',
                                    `create_date` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                                    `update_date` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '修改时间',
                                    `del_flag` tinyint(1) NOT NULL DEFAULT 0 COMMENT '1已删除 0未删除',
                                    `mail` varchar(50) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '邮箱',
                                    `status` tinyint(1) NOT NULL DEFAULT 0 COMMENT '状态 0正常 1锁定',
                                    `avatar` varchar(400) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL DEFAULT 'https://i.postimg.cc/FsHgVKzX/1.gif' COMMENT '头像',
                                    `mobile` varchar(30) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '手机号',
                                    `dept_id` bigint NOT NULL DEFAULT 0 COMMENT '部门id',
                                    `version` int NOT NULL DEFAULT 0 COMMENT '版本号',
                                    `tenant_id` bigint NOT NULL DEFAULT 0 COMMENT '租户id',
                                    PRIMARY KEY (`id`) USING BTREE,
                                    UNIQUE INDEX `idx_tenant_id_username`(`tenant_id` ASC, `username` ASC) USING BTREE COMMENT '租户_用户名_唯一索引',
                                    UNIQUE INDEX `idx_tenant_id_mail`(`tenant_id` ASC, `mail` ASC) USING BTREE COMMENT '租户_邮箱_唯一索引',
                                    UNIQUE INDEX `idx_tenant_id_mobile`(`tenant_id` ASC, `mobile` ASC) USING BTREE COMMENT '租户_手机号_唯一索引'
) ENGINE = InnoDB AUTO_INCREMENT = 1537114827246292995 CHARACTER SET = utf8mb3 COLLATE = utf8mb3_general_ci COMMENT = '用户' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of boot_sys_user_1
-- ----------------------------
INSERT INTO `boot_sys_user_1` VALUES (1341620898007281665, '1ftkBJEIvmOFuxJygs2jnQ==', '{bcrypt}$2a$10$vFo4D9UwvFd6yIG3F3cZEe/RCScCooJqismrRV/FWh6F.qmDCPgxu', 1, 1341620898007281665, 1341620898007281665, '2021-11-29 20:13:11', '2022-10-26 19:24:55', 0, 'LkZpbZYqPmJRj54OG9P8xdgn8lmGt/9oxQT94xbsZyE=', 0, 'http://127.0.0.1/upload/node3/7904fff1c08a4883b40f1ee0336017dc.webp', 'i7KGWPr+sZqGBXO4JViAGg==', 0, 18, 0);
INSERT INTO `boot_sys_user_1` VALUES (1341623527018004481, 'xa1Nz1ENwGc9qWoUseZn6A==', '{bcrypt}$2a$10$jaQ.syONdH/N/UWTX6lwx.Hme2CElmCGhu0OiairxBkneSEvBD8Fe', 0, 1341620898007281665, 1341620898007281665, '2023-03-09 13:52:04', '2023-03-09 13:52:04', 0, NULL, 0, 'http://127.0.0.1/upload/node2/b4e5bb3944a046a6bb54f8bfd2c830c1.webp', NULL, 1535881356595175426, 20, 0);
INSERT INTO `boot_sys_user_1` VALUES (1537111101311844353, 'WPvtQBxvQwzVj5SJsKCifQ==', '{bcrypt}$2a$10$Wac.3sTE4A4pi/Zy6B/HWOstwLFjOH9g8Qrf4gHiBLa/avKAVcwpG', 0, 1341620898007281665, 1341620898007281665, '2022-06-16 00:33:39', '2022-10-24 18:40:20', 0, NULL, 0, 'https://img2.baidu.com/it/u=2432885784,4104422384&fm=253&fmt=auto&app=138&f=JPEG?w=400&h=400', NULL, 1535858679453085698, 19, 0);

SET FOREIGN_KEY_CHECKS = 1;
