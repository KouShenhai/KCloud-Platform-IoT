/*
 Navicat Premium Data Transfer

 Source Server         : 192.168.30.133(老寇）
 Source Server Type    : MySQL
 Source Server Version : 80033 (8.0.33)
 Source Host           : 192.168.30.133:3306
 Source Schema         : kcloud_platform_alibaba_seata

 Target Server Type    : MySQL
 Target Server Version : 80033 (8.0.33)
 File Encoding         : 65001

 Date: 20/09/2023 14:59:28
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for branch_table
-- ----------------------------
DROP TABLE IF EXISTS `branch_table`;
CREATE TABLE `branch_table`  (
                                 `branch_id` bigint NOT NULL,
                                 `xid` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
                                 `transaction_id` bigint NULL DEFAULT NULL,
                                 `resource_group_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
                                 `resource_id` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
                                 `branch_type` varchar(8) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
                                 `status` tinyint NULL DEFAULT NULL,
                                 `client_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
                                 `application_data` varchar(2000) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
                                 `gmt_create` datetime(6) NULL DEFAULT NULL,
                                 `gmt_modified` datetime(6) NULL DEFAULT NULL,
                                 PRIMARY KEY (`branch_id`) USING BTREE,
                                 INDEX `idx_xid`(`xid` ASC) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for distributed_lock
-- ----------------------------
DROP TABLE IF EXISTS `distributed_lock`;
CREATE TABLE `distributed_lock`  (
                                     `lock_key` char(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
                                     `lock_value` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
                                     `expire` bigint NULL DEFAULT NULL,
                                     PRIMARY KEY (`lock_key`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = DYNAMIC;

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
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for global_table
-- ----------------------------
DROP TABLE IF EXISTS `global_table`;
CREATE TABLE `global_table`  (
                                 `xid` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
                                 `transaction_id` bigint NULL DEFAULT NULL,
                                 `status` tinyint NOT NULL,
                                 `application_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
                                 `transaction_service_group` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
                                 `transaction_name` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
                                 `timeout` int NULL DEFAULT NULL,
                                 `begin_time` bigint NULL DEFAULT NULL,
                                 `application_data` varchar(2000) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
                                 `gmt_create` datetime NULL DEFAULT NULL,
                                 `gmt_modified` datetime NULL DEFAULT NULL,
                                 PRIMARY KEY (`xid`) USING BTREE,
                                 INDEX `idx_status_gmt_modified`(`status` ASC, `gmt_modified` ASC) USING BTREE,
                                 INDEX `idx_transaction_id`(`transaction_id` ASC) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for lock_table
-- ----------------------------
DROP TABLE IF EXISTS `lock_table`;
CREATE TABLE `lock_table`  (
                               `row_key` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
                               `xid` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
                               `transaction_id` bigint NULL DEFAULT NULL,
                               `branch_id` bigint NOT NULL,
                               `resource_id` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
                               `table_name` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
                               `pk` varchar(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
                               `status` tinyint NOT NULL DEFAULT 0 COMMENT '0:locked ,1:rollbacking',
                               `gmt_create` datetime NULL DEFAULT NULL,
                               `gmt_modified` datetime NULL DEFAULT NULL,
                               PRIMARY KEY (`row_key`) USING BTREE,
                               INDEX `idx_status`(`status` ASC) USING BTREE,
                               INDEX `idx_branch_id`(`branch_id` ASC) USING BTREE,
                               INDEX `idx_xid`(`xid` ASC) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = DYNAMIC;

SET FOREIGN_KEY_CHECKS = 1;
