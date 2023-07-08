/*
 Navicat Premium Data Transfer

 Source Server         : 192.168.1.100（老寇）
 Source Server Type    : MySQL
 Source Server Version : 80033 (8.0.33)
 Source Host           : 192.168.1.100:3306
 Source Schema         : kcloud_platform_alibaba_user_1

 Target Server Type    : MySQL
 Target Server Version : 80033 (8.0.33)
 File Encoding         : 65001

 Date: 24/06/2023 22:45:59
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
) ENGINE = InnoDB AUTO_INCREMENT = 1537114827246293002 CHARACTER SET = utf8mb3 COLLATE = utf8mb3_general_ci COMMENT = '用户' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of boot_sys_user_0
-- ----------------------------
INSERT INTO `boot_sys_user_0` VALUES (1537114827246292998, '5/Pqo/yVzE72YyPDE5RKAw==', '{bcrypt}$2a$10$ToBq5JB191IUkAfnqfeV5OFLOFDvhr9wWaRm1LhTn5sbL8uyJ0Gre', 1, 1341620898007281665, 1341620898007281665, '2023-02-09 22:59:04', '2023-02-09 22:59:04', 0, NULL, 0, 'https://img2.baidu.com/it/u=2432885784,4104422384&fm=253&fmt=auto&app=138&f=JPEG?w=400&h=400', NULL, 0, 0, 1);

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
) ENGINE = InnoDB AUTO_INCREMENT = 1537114827246293002 CHARACTER SET = utf8mb3 COLLATE = utf8mb3_general_ci COMMENT = '用户' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of boot_sys_user_1
-- ----------------------------
INSERT INTO `boot_sys_user_1` VALUES (1537114827246293001, 'cmV6CFYc1NUWgni0E8xpdg==', '{bcrypt}$2a$10$nbLXUQeCfuiw.7wZwuOT.e0r1mr.ZQcLIlFbil28PCrPBNAnPLRT.', 0, 1537114827246292998, NULL, '2023-02-15 13:18:39', '2023-02-15 13:18:39', 0, NULL, 0, 'https://img2.baidu.com/it/u=2432885784,4104422384&fm=253&fmt=auto&app=138&f=JPEG?w=400&h=400', NULL, 1584488411756171278, 0, 1);

SET FOREIGN_KEY_CHECKS = 1;
