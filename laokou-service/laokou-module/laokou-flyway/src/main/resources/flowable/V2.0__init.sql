/*
 Navicat Premium Data Transfer

 Source Server         : 192.168.30.133(老寇）
 Source Server Type    : MySQL
 Source Server Version : 80033 (8.0.33)
 Source Host           : 192.168.30.133:3306
 Source Schema         : kcloud_platform_alibaba_flowable

 Target Server Type    : MySQL
 Target Server Version : 80033 (8.0.33)
 File Encoding         : 65001

 Date: 20/09/2023 14:56:22
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for ACT_EVT_LOG
-- ----------------------------
DROP TABLE IF EXISTS `ACT_EVT_LOG`;
CREATE TABLE `ACT_EVT_LOG`  (
                                `LOG_NR_` bigint NOT NULL AUTO_INCREMENT,
                                `TYPE_` varchar(64) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NULL DEFAULT NULL,
                                `PROC_DEF_ID_` varchar(64) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NULL DEFAULT NULL,
                                `PROC_INST_ID_` varchar(64) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NULL DEFAULT NULL,
                                `EXECUTION_ID_` varchar(64) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NULL DEFAULT NULL,
                                `TASK_ID_` varchar(64) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NULL DEFAULT NULL,
                                `TIME_STAMP_` timestamp(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3),
                                `USER_ID_` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NULL DEFAULT NULL,
                                `DATA_` longblob NULL,
                                `LOCK_OWNER_` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NULL DEFAULT NULL,
                                `LOCK_TIME_` timestamp(3) NULL DEFAULT NULL,
                                `IS_PROCESSED_` tinyint NULL DEFAULT 0,
                                PRIMARY KEY (`LOG_NR_`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb3 COLLATE = utf8mb3_bin ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for ACT_GE_BYTEARRAY
-- ----------------------------
DROP TABLE IF EXISTS `ACT_GE_BYTEARRAY`;
CREATE TABLE `ACT_GE_BYTEARRAY`  (
                                     `ID_` varchar(64) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NOT NULL,
                                     `REV_` int NULL DEFAULT NULL,
                                     `NAME_` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NULL DEFAULT NULL,
                                     `DEPLOYMENT_ID_` varchar(64) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NULL DEFAULT NULL,
                                     `BYTES_` longblob NULL,
                                     `GENERATED_` tinyint NULL DEFAULT NULL,
                                     PRIMARY KEY (`ID_`) USING BTREE,
                                     INDEX `ACT_FK_BYTEARR_DEPL`(`DEPLOYMENT_ID_` ASC) USING BTREE,
                                     CONSTRAINT `ACT_FK_BYTEARR_DEPL` FOREIGN KEY (`DEPLOYMENT_ID_`) REFERENCES `ACT_RE_DEPLOYMENT` (`ID_`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8mb3 COLLATE = utf8mb3_bin ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for ACT_GE_PROPERTY
-- ----------------------------
DROP TABLE IF EXISTS `ACT_GE_PROPERTY`;
CREATE TABLE `ACT_GE_PROPERTY`  (
                                    `NAME_` varchar(64) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NOT NULL,
                                    `VALUE_` varchar(300) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NULL DEFAULT NULL,
                                    `REV_` int NULL DEFAULT NULL,
                                    PRIMARY KEY (`NAME_`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb3 COLLATE = utf8mb3_bin ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for ACT_HI_ACTINST
-- ----------------------------
DROP TABLE IF EXISTS `ACT_HI_ACTINST`;
CREATE TABLE `ACT_HI_ACTINST`  (
                                   `ID_` varchar(64) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NOT NULL,
                                   `REV_` int NULL DEFAULT 1,
                                   `PROC_DEF_ID_` varchar(64) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NOT NULL,
                                   `PROC_INST_ID_` varchar(64) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NOT NULL,
                                   `EXECUTION_ID_` varchar(64) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NOT NULL,
                                   `ACT_ID_` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NOT NULL,
                                   `TASK_ID_` varchar(64) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NULL DEFAULT NULL,
                                   `CALL_PROC_INST_ID_` varchar(64) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NULL DEFAULT NULL,
                                   `ACT_NAME_` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NULL DEFAULT NULL,
                                   `ACT_TYPE_` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NOT NULL,
                                   `ASSIGNEE_` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NULL DEFAULT NULL,
                                   `START_TIME_` datetime(3) NOT NULL,
                                   `END_TIME_` datetime(3) NULL DEFAULT NULL,
                                   `TRANSACTION_ORDER_` int NULL DEFAULT NULL,
                                   `DURATION_` bigint NULL DEFAULT NULL,
                                   `DELETE_REASON_` varchar(4000) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NULL DEFAULT NULL,
                                   `TENANT_ID_` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NULL DEFAULT '',
                                   PRIMARY KEY (`ID_`) USING BTREE,
                                   INDEX `ACT_IDX_HI_ACT_INST_START`(`START_TIME_` ASC) USING BTREE,
                                   INDEX `ACT_IDX_HI_ACT_INST_END`(`END_TIME_` ASC) USING BTREE,
                                   INDEX `ACT_IDX_HI_ACT_INST_PROCINST`(`PROC_INST_ID_` ASC, `ACT_ID_` ASC) USING BTREE,
                                   INDEX `ACT_IDX_HI_ACT_INST_EXEC`(`EXECUTION_ID_` ASC, `ACT_ID_` ASC) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb3 COLLATE = utf8mb3_bin ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for ACT_HI_ATTACHMENT
-- ----------------------------
DROP TABLE IF EXISTS `ACT_HI_ATTACHMENT`;
CREATE TABLE `ACT_HI_ATTACHMENT`  (
                                      `ID_` varchar(64) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NOT NULL,
                                      `REV_` int NULL DEFAULT NULL,
                                      `USER_ID_` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NULL DEFAULT NULL,
                                      `NAME_` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NULL DEFAULT NULL,
                                      `DESCRIPTION_` varchar(4000) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NULL DEFAULT NULL,
                                      `TYPE_` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NULL DEFAULT NULL,
                                      `TASK_ID_` varchar(64) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NULL DEFAULT NULL,
                                      `PROC_INST_ID_` varchar(64) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NULL DEFAULT NULL,
                                      `URL_` varchar(4000) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NULL DEFAULT NULL,
                                      `CONTENT_ID_` varchar(64) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NULL DEFAULT NULL,
                                      `TIME_` datetime(3) NULL DEFAULT NULL,
                                      PRIMARY KEY (`ID_`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb3 COLLATE = utf8mb3_bin ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for ACT_HI_COMMENT
-- ----------------------------
DROP TABLE IF EXISTS `ACT_HI_COMMENT`;
CREATE TABLE `ACT_HI_COMMENT`  (
                                   `ID_` varchar(64) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NOT NULL,
                                   `TYPE_` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NULL DEFAULT NULL,
                                   `TIME_` datetime(3) NOT NULL,
                                   `USER_ID_` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NULL DEFAULT NULL,
                                   `TASK_ID_` varchar(64) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NULL DEFAULT NULL,
                                   `PROC_INST_ID_` varchar(64) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NULL DEFAULT NULL,
                                   `ACTION_` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NULL DEFAULT NULL,
                                   `MESSAGE_` varchar(4000) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NULL DEFAULT NULL,
                                   `FULL_MSG_` longblob NULL,
                                   PRIMARY KEY (`ID_`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb3 COLLATE = utf8mb3_bin ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for ACT_HI_DETAIL
-- ----------------------------
DROP TABLE IF EXISTS `ACT_HI_DETAIL`;
CREATE TABLE `ACT_HI_DETAIL`  (
                                  `ID_` varchar(64) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NOT NULL,
                                  `TYPE_` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NOT NULL,
                                  `PROC_INST_ID_` varchar(64) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NULL DEFAULT NULL,
                                  `EXECUTION_ID_` varchar(64) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NULL DEFAULT NULL,
                                  `TASK_ID_` varchar(64) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NULL DEFAULT NULL,
                                  `ACT_INST_ID_` varchar(64) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NULL DEFAULT NULL,
                                  `NAME_` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NOT NULL,
                                  `VAR_TYPE_` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NULL DEFAULT NULL,
                                  `REV_` int NULL DEFAULT NULL,
                                  `TIME_` datetime(3) NOT NULL,
                                  `BYTEARRAY_ID_` varchar(64) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NULL DEFAULT NULL,
                                  `DOUBLE_` double NULL DEFAULT NULL,
                                  `LONG_` bigint NULL DEFAULT NULL,
                                  `TEXT_` varchar(4000) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NULL DEFAULT NULL,
                                  `TEXT2_` varchar(4000) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NULL DEFAULT NULL,
                                  PRIMARY KEY (`ID_`) USING BTREE,
                                  INDEX `ACT_IDX_HI_DETAIL_PROC_INST`(`PROC_INST_ID_` ASC) USING BTREE,
                                  INDEX `ACT_IDX_HI_DETAIL_ACT_INST`(`ACT_INST_ID_` ASC) USING BTREE,
                                  INDEX `ACT_IDX_HI_DETAIL_TIME`(`TIME_` ASC) USING BTREE,
                                  INDEX `ACT_IDX_HI_DETAIL_NAME`(`NAME_` ASC) USING BTREE,
                                  INDEX `ACT_IDX_HI_DETAIL_TASK_ID`(`TASK_ID_` ASC) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb3 COLLATE = utf8mb3_bin ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for ACT_HI_ENTITYLINK
-- ----------------------------
DROP TABLE IF EXISTS `ACT_HI_ENTITYLINK`;
CREATE TABLE `ACT_HI_ENTITYLINK`  (
                                      `ID_` varchar(64) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NOT NULL,
                                      `LINK_TYPE_` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NULL DEFAULT NULL,
                                      `CREATE_TIME_` datetime(3) NULL DEFAULT NULL,
                                      `SCOPE_ID_` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NULL DEFAULT NULL,
                                      `SUB_SCOPE_ID_` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NULL DEFAULT NULL,
                                      `SCOPE_TYPE_` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NULL DEFAULT NULL,
                                      `SCOPE_DEFINITION_ID_` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NULL DEFAULT NULL,
                                      `PARENT_ELEMENT_ID_` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NULL DEFAULT NULL,
                                      `REF_SCOPE_ID_` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NULL DEFAULT NULL,
                                      `REF_SCOPE_TYPE_` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NULL DEFAULT NULL,
                                      `REF_SCOPE_DEFINITION_ID_` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NULL DEFAULT NULL,
                                      `ROOT_SCOPE_ID_` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NULL DEFAULT NULL,
                                      `ROOT_SCOPE_TYPE_` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NULL DEFAULT NULL,
                                      `HIERARCHY_TYPE_` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NULL DEFAULT NULL,
                                      PRIMARY KEY (`ID_`) USING BTREE,
                                      INDEX `ACT_IDX_HI_ENT_LNK_SCOPE`(`SCOPE_ID_` ASC, `SCOPE_TYPE_` ASC, `LINK_TYPE_` ASC) USING BTREE,
                                      INDEX `ACT_IDX_HI_ENT_LNK_REF_SCOPE`(`REF_SCOPE_ID_` ASC, `REF_SCOPE_TYPE_` ASC, `LINK_TYPE_` ASC) USING BTREE,
                                      INDEX `ACT_IDX_HI_ENT_LNK_ROOT_SCOPE`(`ROOT_SCOPE_ID_` ASC, `ROOT_SCOPE_TYPE_` ASC, `LINK_TYPE_` ASC) USING BTREE,
                                      INDEX `ACT_IDX_HI_ENT_LNK_SCOPE_DEF`(`SCOPE_DEFINITION_ID_` ASC, `SCOPE_TYPE_` ASC, `LINK_TYPE_` ASC) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb3 COLLATE = utf8mb3_bin ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for ACT_HI_IDENTITYLINK
-- ----------------------------
DROP TABLE IF EXISTS `ACT_HI_IDENTITYLINK`;
CREATE TABLE `ACT_HI_IDENTITYLINK`  (
                                        `ID_` varchar(64) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NOT NULL,
                                        `GROUP_ID_` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NULL DEFAULT NULL,
                                        `TYPE_` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NULL DEFAULT NULL,
                                        `USER_ID_` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NULL DEFAULT NULL,
                                        `TASK_ID_` varchar(64) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NULL DEFAULT NULL,
                                        `CREATE_TIME_` datetime(3) NULL DEFAULT NULL,
                                        `PROC_INST_ID_` varchar(64) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NULL DEFAULT NULL,
                                        `SCOPE_ID_` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NULL DEFAULT NULL,
                                        `SUB_SCOPE_ID_` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NULL DEFAULT NULL,
                                        `SCOPE_TYPE_` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NULL DEFAULT NULL,
                                        `SCOPE_DEFINITION_ID_` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NULL DEFAULT NULL,
                                        PRIMARY KEY (`ID_`) USING BTREE,
                                        INDEX `ACT_IDX_HI_IDENT_LNK_USER`(`USER_ID_` ASC) USING BTREE,
                                        INDEX `ACT_IDX_HI_IDENT_LNK_SCOPE`(`SCOPE_ID_` ASC, `SCOPE_TYPE_` ASC) USING BTREE,
                                        INDEX `ACT_IDX_HI_IDENT_LNK_SUB_SCOPE`(`SUB_SCOPE_ID_` ASC, `SCOPE_TYPE_` ASC) USING BTREE,
                                        INDEX `ACT_IDX_HI_IDENT_LNK_SCOPE_DEF`(`SCOPE_DEFINITION_ID_` ASC, `SCOPE_TYPE_` ASC) USING BTREE,
                                        INDEX `ACT_IDX_HI_IDENT_LNK_TASK`(`TASK_ID_` ASC) USING BTREE,
                                        INDEX `ACT_IDX_HI_IDENT_LNK_PROCINST`(`PROC_INST_ID_` ASC) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb3 COLLATE = utf8mb3_bin ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for ACT_HI_PROCINST
-- ----------------------------
DROP TABLE IF EXISTS `ACT_HI_PROCINST`;
CREATE TABLE `ACT_HI_PROCINST`  (
                                    `ID_` varchar(64) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NOT NULL,
                                    `REV_` int NULL DEFAULT 1,
                                    `PROC_INST_ID_` varchar(64) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NOT NULL,
                                    `BUSINESS_KEY_` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NULL DEFAULT NULL,
                                    `PROC_DEF_ID_` varchar(64) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NOT NULL,
                                    `START_TIME_` datetime(3) NOT NULL,
                                    `END_TIME_` datetime(3) NULL DEFAULT NULL,
                                    `DURATION_` bigint NULL DEFAULT NULL,
                                    `START_USER_ID_` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NULL DEFAULT NULL,
                                    `START_ACT_ID_` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NULL DEFAULT NULL,
                                    `END_ACT_ID_` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NULL DEFAULT NULL,
                                    `SUPER_PROCESS_INSTANCE_ID_` varchar(64) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NULL DEFAULT NULL,
                                    `DELETE_REASON_` varchar(4000) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NULL DEFAULT NULL,
                                    `TENANT_ID_` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NULL DEFAULT '',
                                    `NAME_` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NULL DEFAULT NULL,
                                    `CALLBACK_ID_` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NULL DEFAULT NULL,
                                    `CALLBACK_TYPE_` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NULL DEFAULT NULL,
                                    `REFERENCE_ID_` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NULL DEFAULT NULL,
                                    `REFERENCE_TYPE_` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NULL DEFAULT NULL,
                                    `PROPAGATED_STAGE_INST_ID_` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NULL DEFAULT NULL,
                                    `BUSINESS_STATUS_` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NULL DEFAULT NULL,
                                    PRIMARY KEY (`ID_`) USING BTREE,
                                    UNIQUE INDEX `PROC_INST_ID_`(`PROC_INST_ID_` ASC) USING BTREE,
                                    INDEX `ACT_IDX_HI_PRO_INST_END`(`END_TIME_` ASC) USING BTREE,
                                    INDEX `ACT_IDX_HI_PRO_I_BUSKEY`(`BUSINESS_KEY_` ASC) USING BTREE,
                                    INDEX `ACT_IDX_HI_PRO_SUPER_PROCINST`(`SUPER_PROCESS_INSTANCE_ID_` ASC) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb3 COLLATE = utf8mb3_bin ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for ACT_HI_TASKINST
-- ----------------------------
DROP TABLE IF EXISTS `ACT_HI_TASKINST`;
CREATE TABLE `ACT_HI_TASKINST`  (
                                    `ID_` varchar(64) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NOT NULL,
                                    `REV_` int NULL DEFAULT 1,
                                    `PROC_DEF_ID_` varchar(64) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NULL DEFAULT NULL,
                                    `TASK_DEF_ID_` varchar(64) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NULL DEFAULT NULL,
                                    `TASK_DEF_KEY_` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NULL DEFAULT NULL,
                                    `PROC_INST_ID_` varchar(64) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NULL DEFAULT NULL,
                                    `EXECUTION_ID_` varchar(64) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NULL DEFAULT NULL,
                                    `SCOPE_ID_` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NULL DEFAULT NULL,
                                    `SUB_SCOPE_ID_` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NULL DEFAULT NULL,
                                    `SCOPE_TYPE_` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NULL DEFAULT NULL,
                                    `SCOPE_DEFINITION_ID_` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NULL DEFAULT NULL,
                                    `PROPAGATED_STAGE_INST_ID_` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NULL DEFAULT NULL,
                                    `NAME_` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NULL DEFAULT NULL,
                                    `PARENT_TASK_ID_` varchar(64) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NULL DEFAULT NULL,
                                    `DESCRIPTION_` varchar(4000) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NULL DEFAULT NULL,
                                    `OWNER_` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NULL DEFAULT NULL,
                                    `ASSIGNEE_` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NULL DEFAULT NULL,
                                    `START_TIME_` datetime(3) NOT NULL,
                                    `CLAIM_TIME_` datetime(3) NULL DEFAULT NULL,
                                    `END_TIME_` datetime(3) NULL DEFAULT NULL,
                                    `DURATION_` bigint NULL DEFAULT NULL,
                                    `DELETE_REASON_` varchar(4000) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NULL DEFAULT NULL,
                                    `PRIORITY_` int NULL DEFAULT NULL,
                                    `DUE_DATE_` datetime(3) NULL DEFAULT NULL,
                                    `FORM_KEY_` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NULL DEFAULT NULL,
                                    `CATEGORY_` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NULL DEFAULT NULL,
                                    `TENANT_ID_` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NULL DEFAULT '',
                                    `LAST_UPDATED_TIME_` datetime(3) NULL DEFAULT NULL,
                                    PRIMARY KEY (`ID_`) USING BTREE,
                                    INDEX `ACT_IDX_HI_TASK_SCOPE`(`SCOPE_ID_` ASC, `SCOPE_TYPE_` ASC) USING BTREE,
                                    INDEX `ACT_IDX_HI_TASK_SUB_SCOPE`(`SUB_SCOPE_ID_` ASC, `SCOPE_TYPE_` ASC) USING BTREE,
                                    INDEX `ACT_IDX_HI_TASK_SCOPE_DEF`(`SCOPE_DEFINITION_ID_` ASC, `SCOPE_TYPE_` ASC) USING BTREE,
                                    INDEX `ACT_IDX_HI_TASK_INST_PROCINST`(`PROC_INST_ID_` ASC) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb3 COLLATE = utf8mb3_bin ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for ACT_HI_TSK_LOG
-- ----------------------------
DROP TABLE IF EXISTS `ACT_HI_TSK_LOG`;
CREATE TABLE `ACT_HI_TSK_LOG`  (
                                   `ID_` bigint NOT NULL AUTO_INCREMENT,
                                   `TYPE_` varchar(64) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NULL DEFAULT NULL,
                                   `TASK_ID_` varchar(64) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NOT NULL,
                                   `TIME_STAMP_` timestamp(3) NOT NULL,
                                   `USER_ID_` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NULL DEFAULT NULL,
                                   `DATA_` varchar(4000) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NULL DEFAULT NULL,
                                   `EXECUTION_ID_` varchar(64) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NULL DEFAULT NULL,
                                   `PROC_INST_ID_` varchar(64) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NULL DEFAULT NULL,
                                   `PROC_DEF_ID_` varchar(64) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NULL DEFAULT NULL,
                                   `SCOPE_ID_` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NULL DEFAULT NULL,
                                   `SCOPE_DEFINITION_ID_` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NULL DEFAULT NULL,
                                   `SUB_SCOPE_ID_` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NULL DEFAULT NULL,
                                   `SCOPE_TYPE_` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NULL DEFAULT NULL,
                                   `TENANT_ID_` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NULL DEFAULT '',
                                   PRIMARY KEY (`ID_`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb3 COLLATE = utf8mb3_bin ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for ACT_HI_VARINST
-- ----------------------------
DROP TABLE IF EXISTS `ACT_HI_VARINST`;
CREATE TABLE `ACT_HI_VARINST`  (
                                   `ID_` varchar(64) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NOT NULL,
                                   `REV_` int NULL DEFAULT 1,
                                   `PROC_INST_ID_` varchar(64) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NULL DEFAULT NULL,
                                   `EXECUTION_ID_` varchar(64) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NULL DEFAULT NULL,
                                   `TASK_ID_` varchar(64) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NULL DEFAULT NULL,
                                   `NAME_` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NOT NULL,
                                   `VAR_TYPE_` varchar(100) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NULL DEFAULT NULL,
                                   `SCOPE_ID_` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NULL DEFAULT NULL,
                                   `SUB_SCOPE_ID_` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NULL DEFAULT NULL,
                                   `SCOPE_TYPE_` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NULL DEFAULT NULL,
                                   `BYTEARRAY_ID_` varchar(64) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NULL DEFAULT NULL,
                                   `DOUBLE_` double NULL DEFAULT NULL,
                                   `LONG_` bigint NULL DEFAULT NULL,
                                   `TEXT_` varchar(4000) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NULL DEFAULT NULL,
                                   `TEXT2_` varchar(4000) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NULL DEFAULT NULL,
                                   `CREATE_TIME_` datetime(3) NULL DEFAULT NULL,
                                   `LAST_UPDATED_TIME_` datetime(3) NULL DEFAULT NULL,
                                   PRIMARY KEY (`ID_`) USING BTREE,
                                   INDEX `ACT_IDX_HI_PROCVAR_NAME_TYPE`(`NAME_` ASC, `VAR_TYPE_` ASC) USING BTREE,
                                   INDEX `ACT_IDX_HI_VAR_SCOPE_ID_TYPE`(`SCOPE_ID_` ASC, `SCOPE_TYPE_` ASC) USING BTREE,
                                   INDEX `ACT_IDX_HI_VAR_SUB_ID_TYPE`(`SUB_SCOPE_ID_` ASC, `SCOPE_TYPE_` ASC) USING BTREE,
                                   INDEX `ACT_IDX_HI_PROCVAR_PROC_INST`(`PROC_INST_ID_` ASC) USING BTREE,
                                   INDEX `ACT_IDX_HI_PROCVAR_TASK_ID`(`TASK_ID_` ASC) USING BTREE,
                                   INDEX `ACT_IDX_HI_PROCVAR_EXE`(`EXECUTION_ID_` ASC) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb3 COLLATE = utf8mb3_bin ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for ACT_PROCDEF_INFO
-- ----------------------------
DROP TABLE IF EXISTS `ACT_PROCDEF_INFO`;
CREATE TABLE `ACT_PROCDEF_INFO`  (
                                     `ID_` varchar(64) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NOT NULL,
                                     `PROC_DEF_ID_` varchar(64) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NOT NULL,
                                     `REV_` int NULL DEFAULT NULL,
                                     `INFO_JSON_ID_` varchar(64) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NULL DEFAULT NULL,
                                     PRIMARY KEY (`ID_`) USING BTREE,
                                     UNIQUE INDEX `ACT_UNIQ_INFO_PROCDEF`(`PROC_DEF_ID_` ASC) USING BTREE,
                                     INDEX `ACT_IDX_INFO_PROCDEF`(`PROC_DEF_ID_` ASC) USING BTREE,
                                     INDEX `ACT_FK_INFO_JSON_BA`(`INFO_JSON_ID_` ASC) USING BTREE,
                                     CONSTRAINT `ACT_FK_INFO_JSON_BA` FOREIGN KEY (`INFO_JSON_ID_`) REFERENCES `ACT_GE_BYTEARRAY` (`ID_`) ON DELETE RESTRICT ON UPDATE RESTRICT,
                                     CONSTRAINT `ACT_FK_INFO_PROCDEF` FOREIGN KEY (`PROC_DEF_ID_`) REFERENCES `ACT_RE_PROCDEF` (`ID_`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8mb3 COLLATE = utf8mb3_bin ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for ACT_RE_DEPLOYMENT
-- ----------------------------
DROP TABLE IF EXISTS `ACT_RE_DEPLOYMENT`;
CREATE TABLE `ACT_RE_DEPLOYMENT`  (
                                      `ID_` varchar(64) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NOT NULL,
                                      `NAME_` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NULL DEFAULT NULL,
                                      `CATEGORY_` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NULL DEFAULT NULL,
                                      `KEY_` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NULL DEFAULT NULL,
                                      `TENANT_ID_` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NULL DEFAULT '',
                                      `DEPLOY_TIME_` timestamp(3) NULL DEFAULT NULL,
                                      `DERIVED_FROM_` varchar(64) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NULL DEFAULT NULL,
                                      `DERIVED_FROM_ROOT_` varchar(64) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NULL DEFAULT NULL,
                                      `PARENT_DEPLOYMENT_ID_` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NULL DEFAULT NULL,
                                      `ENGINE_VERSION_` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NULL DEFAULT NULL,
                                      PRIMARY KEY (`ID_`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb3 COLLATE = utf8mb3_bin ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for ACT_RE_MODEL
-- ----------------------------
DROP TABLE IF EXISTS `ACT_RE_MODEL`;
CREATE TABLE `ACT_RE_MODEL`  (
                                 `ID_` varchar(64) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NOT NULL,
                                 `REV_` int NULL DEFAULT NULL,
                                 `NAME_` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NULL DEFAULT NULL,
                                 `KEY_` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NULL DEFAULT NULL,
                                 `CATEGORY_` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NULL DEFAULT NULL,
                                 `CREATE_TIME_` timestamp(3) NULL DEFAULT NULL,
                                 `LAST_UPDATE_TIME_` timestamp(3) NULL DEFAULT NULL,
                                 `VERSION_` int NULL DEFAULT NULL,
                                 `META_INFO_` varchar(4000) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NULL DEFAULT NULL,
                                 `DEPLOYMENT_ID_` varchar(64) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NULL DEFAULT NULL,
                                 `EDITOR_SOURCE_VALUE_ID_` varchar(64) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NULL DEFAULT NULL,
                                 `EDITOR_SOURCE_EXTRA_VALUE_ID_` varchar(64) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NULL DEFAULT NULL,
                                 `TENANT_ID_` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NULL DEFAULT '',
                                 PRIMARY KEY (`ID_`) USING BTREE,
                                 INDEX `ACT_FK_MODEL_SOURCE`(`EDITOR_SOURCE_VALUE_ID_` ASC) USING BTREE,
                                 INDEX `ACT_FK_MODEL_SOURCE_EXTRA`(`EDITOR_SOURCE_EXTRA_VALUE_ID_` ASC) USING BTREE,
                                 INDEX `ACT_FK_MODEL_DEPLOYMENT`(`DEPLOYMENT_ID_` ASC) USING BTREE,
                                 CONSTRAINT `ACT_FK_MODEL_DEPLOYMENT` FOREIGN KEY (`DEPLOYMENT_ID_`) REFERENCES `ACT_RE_DEPLOYMENT` (`ID_`) ON DELETE RESTRICT ON UPDATE RESTRICT,
                                 CONSTRAINT `ACT_FK_MODEL_SOURCE` FOREIGN KEY (`EDITOR_SOURCE_VALUE_ID_`) REFERENCES `ACT_GE_BYTEARRAY` (`ID_`) ON DELETE RESTRICT ON UPDATE RESTRICT,
                                 CONSTRAINT `ACT_FK_MODEL_SOURCE_EXTRA` FOREIGN KEY (`EDITOR_SOURCE_EXTRA_VALUE_ID_`) REFERENCES `ACT_GE_BYTEARRAY` (`ID_`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8mb3 COLLATE = utf8mb3_bin ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for ACT_RE_PROCDEF
-- ----------------------------
DROP TABLE IF EXISTS `ACT_RE_PROCDEF`;
CREATE TABLE `ACT_RE_PROCDEF`  (
                                   `ID_` varchar(64) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NOT NULL,
                                   `REV_` int NULL DEFAULT NULL,
                                   `CATEGORY_` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NULL DEFAULT NULL,
                                   `NAME_` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NULL DEFAULT NULL,
                                   `KEY_` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NOT NULL,
                                   `VERSION_` int NOT NULL,
                                   `DEPLOYMENT_ID_` varchar(64) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NULL DEFAULT NULL,
                                   `RESOURCE_NAME_` varchar(4000) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NULL DEFAULT NULL,
                                   `DGRM_RESOURCE_NAME_` varchar(4000) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NULL DEFAULT NULL,
                                   `DESCRIPTION_` varchar(4000) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NULL DEFAULT NULL,
                                   `HAS_START_FORM_KEY_` tinyint NULL DEFAULT NULL,
                                   `HAS_GRAPHICAL_NOTATION_` tinyint NULL DEFAULT NULL,
                                   `SUSPENSION_STATE_` int NULL DEFAULT NULL,
                                   `TENANT_ID_` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NULL DEFAULT '',
                                   `ENGINE_VERSION_` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NULL DEFAULT NULL,
                                   `DERIVED_FROM_` varchar(64) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NULL DEFAULT NULL,
                                   `DERIVED_FROM_ROOT_` varchar(64) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NULL DEFAULT NULL,
                                   `DERIVED_VERSION_` int NOT NULL DEFAULT 0,
                                   PRIMARY KEY (`ID_`) USING BTREE,
                                   UNIQUE INDEX `ACT_UNIQ_PROCDEF`(`KEY_` ASC, `VERSION_` ASC, `DERIVED_VERSION_` ASC, `TENANT_ID_` ASC) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb3 COLLATE = utf8mb3_bin ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for ACT_RU_ACTINST
-- ----------------------------
DROP TABLE IF EXISTS `ACT_RU_ACTINST`;
CREATE TABLE `ACT_RU_ACTINST`  (
                                   `ID_` varchar(64) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NOT NULL,
                                   `REV_` int NULL DEFAULT 1,
                                   `PROC_DEF_ID_` varchar(64) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NOT NULL,
                                   `PROC_INST_ID_` varchar(64) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NOT NULL,
                                   `EXECUTION_ID_` varchar(64) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NOT NULL,
                                   `ACT_ID_` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NOT NULL,
                                   `TASK_ID_` varchar(64) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NULL DEFAULT NULL,
                                   `CALL_PROC_INST_ID_` varchar(64) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NULL DEFAULT NULL,
                                   `ACT_NAME_` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NULL DEFAULT NULL,
                                   `ACT_TYPE_` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NOT NULL,
                                   `ASSIGNEE_` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NULL DEFAULT NULL,
                                   `START_TIME_` datetime(3) NOT NULL,
                                   `END_TIME_` datetime(3) NULL DEFAULT NULL,
                                   `DURATION_` bigint NULL DEFAULT NULL,
                                   `TRANSACTION_ORDER_` int NULL DEFAULT NULL,
                                   `DELETE_REASON_` varchar(4000) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NULL DEFAULT NULL,
                                   `TENANT_ID_` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NULL DEFAULT '',
                                   PRIMARY KEY (`ID_`) USING BTREE,
                                   INDEX `ACT_IDX_RU_ACTI_START`(`START_TIME_` ASC) USING BTREE,
                                   INDEX `ACT_IDX_RU_ACTI_END`(`END_TIME_` ASC) USING BTREE,
                                   INDEX `ACT_IDX_RU_ACTI_PROC`(`PROC_INST_ID_` ASC) USING BTREE,
                                   INDEX `ACT_IDX_RU_ACTI_PROC_ACT`(`PROC_INST_ID_` ASC, `ACT_ID_` ASC) USING BTREE,
                                   INDEX `ACT_IDX_RU_ACTI_EXEC`(`EXECUTION_ID_` ASC) USING BTREE,
                                   INDEX `ACT_IDX_RU_ACTI_EXEC_ACT`(`EXECUTION_ID_` ASC, `ACT_ID_` ASC) USING BTREE,
                                   INDEX `ACT_IDX_RU_ACTI_TASK`(`TASK_ID_` ASC) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb3 COLLATE = utf8mb3_bin ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for ACT_RU_DEADLETTER_JOB
-- ----------------------------
DROP TABLE IF EXISTS `ACT_RU_DEADLETTER_JOB`;
CREATE TABLE `ACT_RU_DEADLETTER_JOB`  (
                                          `ID_` varchar(64) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NOT NULL,
                                          `REV_` int NULL DEFAULT NULL,
                                          `CATEGORY_` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NULL DEFAULT NULL,
                                          `TYPE_` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NOT NULL,
                                          `EXCLUSIVE_` tinyint(1) NULL DEFAULT NULL,
                                          `EXECUTION_ID_` varchar(64) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NULL DEFAULT NULL,
                                          `PROCESS_INSTANCE_ID_` varchar(64) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NULL DEFAULT NULL,
                                          `PROC_DEF_ID_` varchar(64) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NULL DEFAULT NULL,
                                          `ELEMENT_ID_` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NULL DEFAULT NULL,
                                          `ELEMENT_NAME_` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NULL DEFAULT NULL,
                                          `SCOPE_ID_` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NULL DEFAULT NULL,
                                          `SUB_SCOPE_ID_` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NULL DEFAULT NULL,
                                          `SCOPE_TYPE_` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NULL DEFAULT NULL,
                                          `SCOPE_DEFINITION_ID_` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NULL DEFAULT NULL,
                                          `CORRELATION_ID_` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NULL DEFAULT NULL,
                                          `EXCEPTION_STACK_ID_` varchar(64) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NULL DEFAULT NULL,
                                          `EXCEPTION_MSG_` varchar(4000) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NULL DEFAULT NULL,
                                          `DUEDATE_` timestamp(3) NULL DEFAULT NULL,
                                          `REPEAT_` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NULL DEFAULT NULL,
                                          `HANDLER_TYPE_` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NULL DEFAULT NULL,
                                          `HANDLER_CFG_` varchar(4000) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NULL DEFAULT NULL,
                                          `CUSTOM_VALUES_ID_` varchar(64) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NULL DEFAULT NULL,
                                          `CREATE_TIME_` timestamp(3) NULL DEFAULT NULL,
                                          `TENANT_ID_` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NULL DEFAULT '',
                                          PRIMARY KEY (`ID_`) USING BTREE,
                                          INDEX `ACT_IDX_DEADLETTER_JOB_EXCEPTION_STACK_ID`(`EXCEPTION_STACK_ID_` ASC) USING BTREE,
                                          INDEX `ACT_IDX_DEADLETTER_JOB_CUSTOM_VALUES_ID`(`CUSTOM_VALUES_ID_` ASC) USING BTREE,
                                          INDEX `ACT_IDX_DEADLETTER_JOB_CORRELATION_ID`(`CORRELATION_ID_` ASC) USING BTREE,
                                          INDEX `ACT_IDX_DJOB_SCOPE`(`SCOPE_ID_` ASC, `SCOPE_TYPE_` ASC) USING BTREE,
                                          INDEX `ACT_IDX_DJOB_SUB_SCOPE`(`SUB_SCOPE_ID_` ASC, `SCOPE_TYPE_` ASC) USING BTREE,
                                          INDEX `ACT_IDX_DJOB_SCOPE_DEF`(`SCOPE_DEFINITION_ID_` ASC, `SCOPE_TYPE_` ASC) USING BTREE,
                                          INDEX `ACT_FK_DEADLETTER_JOB_EXECUTION`(`EXECUTION_ID_` ASC) USING BTREE,
                                          INDEX `ACT_FK_DEADLETTER_JOB_PROCESS_INSTANCE`(`PROCESS_INSTANCE_ID_` ASC) USING BTREE,
                                          INDEX `ACT_FK_DEADLETTER_JOB_PROC_DEF`(`PROC_DEF_ID_` ASC) USING BTREE,
                                          CONSTRAINT `ACT_FK_DEADLETTER_JOB_CUSTOM_VALUES` FOREIGN KEY (`CUSTOM_VALUES_ID_`) REFERENCES `ACT_GE_BYTEARRAY` (`ID_`) ON DELETE RESTRICT ON UPDATE RESTRICT,
                                          CONSTRAINT `ACT_FK_DEADLETTER_JOB_EXCEPTION` FOREIGN KEY (`EXCEPTION_STACK_ID_`) REFERENCES `ACT_GE_BYTEARRAY` (`ID_`) ON DELETE RESTRICT ON UPDATE RESTRICT,
                                          CONSTRAINT `ACT_FK_DEADLETTER_JOB_EXECUTION` FOREIGN KEY (`EXECUTION_ID_`) REFERENCES `ACT_RU_EXECUTION` (`ID_`) ON DELETE RESTRICT ON UPDATE RESTRICT,
                                          CONSTRAINT `ACT_FK_DEADLETTER_JOB_PROC_DEF` FOREIGN KEY (`PROC_DEF_ID_`) REFERENCES `ACT_RE_PROCDEF` (`ID_`) ON DELETE RESTRICT ON UPDATE RESTRICT,
                                          CONSTRAINT `ACT_FK_DEADLETTER_JOB_PROCESS_INSTANCE` FOREIGN KEY (`PROCESS_INSTANCE_ID_`) REFERENCES `ACT_RU_EXECUTION` (`ID_`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8mb3 COLLATE = utf8mb3_bin ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for ACT_RU_ENTITYLINK
-- ----------------------------
DROP TABLE IF EXISTS `ACT_RU_ENTITYLINK`;
CREATE TABLE `ACT_RU_ENTITYLINK`  (
                                      `ID_` varchar(64) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NOT NULL,
                                      `REV_` int NULL DEFAULT NULL,
                                      `CREATE_TIME_` datetime(3) NULL DEFAULT NULL,
                                      `LINK_TYPE_` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NULL DEFAULT NULL,
                                      `SCOPE_ID_` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NULL DEFAULT NULL,
                                      `SUB_SCOPE_ID_` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NULL DEFAULT NULL,
                                      `SCOPE_TYPE_` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NULL DEFAULT NULL,
                                      `SCOPE_DEFINITION_ID_` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NULL DEFAULT NULL,
                                      `PARENT_ELEMENT_ID_` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NULL DEFAULT NULL,
                                      `REF_SCOPE_ID_` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NULL DEFAULT NULL,
                                      `REF_SCOPE_TYPE_` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NULL DEFAULT NULL,
                                      `REF_SCOPE_DEFINITION_ID_` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NULL DEFAULT NULL,
                                      `ROOT_SCOPE_ID_` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NULL DEFAULT NULL,
                                      `ROOT_SCOPE_TYPE_` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NULL DEFAULT NULL,
                                      `HIERARCHY_TYPE_` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NULL DEFAULT NULL,
                                      PRIMARY KEY (`ID_`) USING BTREE,
                                      INDEX `ACT_IDX_ENT_LNK_SCOPE`(`SCOPE_ID_` ASC, `SCOPE_TYPE_` ASC, `LINK_TYPE_` ASC) USING BTREE,
                                      INDEX `ACT_IDX_ENT_LNK_REF_SCOPE`(`REF_SCOPE_ID_` ASC, `REF_SCOPE_TYPE_` ASC, `LINK_TYPE_` ASC) USING BTREE,
                                      INDEX `ACT_IDX_ENT_LNK_ROOT_SCOPE`(`ROOT_SCOPE_ID_` ASC, `ROOT_SCOPE_TYPE_` ASC, `LINK_TYPE_` ASC) USING BTREE,
                                      INDEX `ACT_IDX_ENT_LNK_SCOPE_DEF`(`SCOPE_DEFINITION_ID_` ASC, `SCOPE_TYPE_` ASC, `LINK_TYPE_` ASC) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb3 COLLATE = utf8mb3_bin ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for ACT_RU_EVENT_SUBSCR
-- ----------------------------
DROP TABLE IF EXISTS `ACT_RU_EVENT_SUBSCR`;
CREATE TABLE `ACT_RU_EVENT_SUBSCR`  (
                                        `ID_` varchar(64) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NOT NULL,
                                        `REV_` int NULL DEFAULT NULL,
                                        `EVENT_TYPE_` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NOT NULL,
                                        `EVENT_NAME_` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NULL DEFAULT NULL,
                                        `EXECUTION_ID_` varchar(64) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NULL DEFAULT NULL,
                                        `PROC_INST_ID_` varchar(64) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NULL DEFAULT NULL,
                                        `ACTIVITY_ID_` varchar(64) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NULL DEFAULT NULL,
                                        `CONFIGURATION_` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NULL DEFAULT NULL,
                                        `CREATED_` timestamp(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3),
                                        `PROC_DEF_ID_` varchar(64) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NULL DEFAULT NULL,
                                        `SUB_SCOPE_ID_` varchar(64) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NULL DEFAULT NULL,
                                        `SCOPE_ID_` varchar(64) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NULL DEFAULT NULL,
                                        `SCOPE_DEFINITION_ID_` varchar(64) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NULL DEFAULT NULL,
                                        `SCOPE_TYPE_` varchar(64) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NULL DEFAULT NULL,
                                        `LOCK_TIME_` timestamp(3) NULL DEFAULT NULL,
                                        `LOCK_OWNER_` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NULL DEFAULT NULL,
                                        `TENANT_ID_` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NULL DEFAULT '',
                                        PRIMARY KEY (`ID_`) USING BTREE,
                                        INDEX `ACT_IDX_EVENT_SUBSCR_CONFIG_`(`CONFIGURATION_` ASC) USING BTREE,
                                        INDEX `ACT_IDX_EVENT_SUBSCR_SCOPEREF_`(`SCOPE_ID_` ASC, `SCOPE_TYPE_` ASC) USING BTREE,
                                        INDEX `ACT_FK_EVENT_EXEC`(`EXECUTION_ID_` ASC) USING BTREE,
                                        CONSTRAINT `ACT_FK_EVENT_EXEC` FOREIGN KEY (`EXECUTION_ID_`) REFERENCES `ACT_RU_EXECUTION` (`ID_`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8mb3 COLLATE = utf8mb3_bin ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for ACT_RU_EXECUTION
-- ----------------------------
DROP TABLE IF EXISTS `ACT_RU_EXECUTION`;
CREATE TABLE `ACT_RU_EXECUTION`  (
                                     `ID_` varchar(64) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NOT NULL,
                                     `REV_` int NULL DEFAULT NULL,
                                     `PROC_INST_ID_` varchar(64) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NULL DEFAULT NULL,
                                     `BUSINESS_KEY_` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NULL DEFAULT NULL,
                                     `PARENT_ID_` varchar(64) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NULL DEFAULT NULL,
                                     `PROC_DEF_ID_` varchar(64) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NULL DEFAULT NULL,
                                     `SUPER_EXEC_` varchar(64) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NULL DEFAULT NULL,
                                     `ROOT_PROC_INST_ID_` varchar(64) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NULL DEFAULT NULL,
                                     `ACT_ID_` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NULL DEFAULT NULL,
                                     `IS_ACTIVE_` tinyint NULL DEFAULT NULL,
                                     `IS_CONCURRENT_` tinyint NULL DEFAULT NULL,
                                     `IS_SCOPE_` tinyint NULL DEFAULT NULL,
                                     `IS_EVENT_SCOPE_` tinyint NULL DEFAULT NULL,
                                     `IS_MI_ROOT_` tinyint NULL DEFAULT NULL,
                                     `SUSPENSION_STATE_` int NULL DEFAULT NULL,
                                     `CACHED_ENT_STATE_` int NULL DEFAULT NULL,
                                     `TENANT_ID_` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NULL DEFAULT '',
                                     `NAME_` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NULL DEFAULT NULL,
                                     `START_ACT_ID_` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NULL DEFAULT NULL,
                                     `START_TIME_` datetime(3) NULL DEFAULT NULL,
                                     `START_USER_ID_` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NULL DEFAULT NULL,
                                     `LOCK_TIME_` timestamp(3) NULL DEFAULT NULL,
                                     `LOCK_OWNER_` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NULL DEFAULT NULL,
                                     `IS_COUNT_ENABLED_` tinyint NULL DEFAULT NULL,
                                     `EVT_SUBSCR_COUNT_` int NULL DEFAULT NULL,
                                     `TASK_COUNT_` int NULL DEFAULT NULL,
                                     `JOB_COUNT_` int NULL DEFAULT NULL,
                                     `TIMER_JOB_COUNT_` int NULL DEFAULT NULL,
                                     `SUSP_JOB_COUNT_` int NULL DEFAULT NULL,
                                     `DEADLETTER_JOB_COUNT_` int NULL DEFAULT NULL,
                                     `EXTERNAL_WORKER_JOB_COUNT_` int NULL DEFAULT NULL,
                                     `VAR_COUNT_` int NULL DEFAULT NULL,
                                     `ID_LINK_COUNT_` int NULL DEFAULT NULL,
                                     `CALLBACK_ID_` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NULL DEFAULT NULL,
                                     `CALLBACK_TYPE_` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NULL DEFAULT NULL,
                                     `REFERENCE_ID_` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NULL DEFAULT NULL,
                                     `REFERENCE_TYPE_` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NULL DEFAULT NULL,
                                     `PROPAGATED_STAGE_INST_ID_` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NULL DEFAULT NULL,
                                     `BUSINESS_STATUS_` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NULL DEFAULT NULL,
                                     PRIMARY KEY (`ID_`) USING BTREE,
                                     INDEX `ACT_IDX_EXEC_BUSKEY`(`BUSINESS_KEY_` ASC) USING BTREE,
                                     INDEX `ACT_IDC_EXEC_ROOT`(`ROOT_PROC_INST_ID_` ASC) USING BTREE,
                                     INDEX `ACT_IDX_EXEC_REF_ID_`(`REFERENCE_ID_` ASC) USING BTREE,
                                     INDEX `ACT_FK_EXE_PROCINST`(`PROC_INST_ID_` ASC) USING BTREE,
                                     INDEX `ACT_FK_EXE_PARENT`(`PARENT_ID_` ASC) USING BTREE,
                                     INDEX `ACT_FK_EXE_SUPER`(`SUPER_EXEC_` ASC) USING BTREE,
                                     INDEX `ACT_FK_EXE_PROCDEF`(`PROC_DEF_ID_` ASC) USING BTREE,
                                     CONSTRAINT `ACT_FK_EXE_PARENT` FOREIGN KEY (`PARENT_ID_`) REFERENCES `ACT_RU_EXECUTION` (`ID_`) ON DELETE CASCADE ON UPDATE RESTRICT,
                                     CONSTRAINT `ACT_FK_EXE_PROCDEF` FOREIGN KEY (`PROC_DEF_ID_`) REFERENCES `ACT_RE_PROCDEF` (`ID_`) ON DELETE RESTRICT ON UPDATE RESTRICT,
                                     CONSTRAINT `ACT_FK_EXE_PROCINST` FOREIGN KEY (`PROC_INST_ID_`) REFERENCES `ACT_RU_EXECUTION` (`ID_`) ON DELETE CASCADE ON UPDATE CASCADE,
                                     CONSTRAINT `ACT_FK_EXE_SUPER` FOREIGN KEY (`SUPER_EXEC_`) REFERENCES `ACT_RU_EXECUTION` (`ID_`) ON DELETE CASCADE ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8mb3 COLLATE = utf8mb3_bin ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for ACT_RU_EXTERNAL_JOB
-- ----------------------------
DROP TABLE IF EXISTS `ACT_RU_EXTERNAL_JOB`;
CREATE TABLE `ACT_RU_EXTERNAL_JOB`  (
                                        `ID_` varchar(64) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NOT NULL,
                                        `REV_` int NULL DEFAULT NULL,
                                        `CATEGORY_` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NULL DEFAULT NULL,
                                        `TYPE_` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NOT NULL,
                                        `LOCK_EXP_TIME_` timestamp(3) NULL DEFAULT NULL,
                                        `LOCK_OWNER_` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NULL DEFAULT NULL,
                                        `EXCLUSIVE_` tinyint(1) NULL DEFAULT NULL,
                                        `EXECUTION_ID_` varchar(64) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NULL DEFAULT NULL,
                                        `PROCESS_INSTANCE_ID_` varchar(64) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NULL DEFAULT NULL,
                                        `PROC_DEF_ID_` varchar(64) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NULL DEFAULT NULL,
                                        `ELEMENT_ID_` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NULL DEFAULT NULL,
                                        `ELEMENT_NAME_` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NULL DEFAULT NULL,
                                        `SCOPE_ID_` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NULL DEFAULT NULL,
                                        `SUB_SCOPE_ID_` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NULL DEFAULT NULL,
                                        `SCOPE_TYPE_` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NULL DEFAULT NULL,
                                        `SCOPE_DEFINITION_ID_` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NULL DEFAULT NULL,
                                        `CORRELATION_ID_` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NULL DEFAULT NULL,
                                        `RETRIES_` int NULL DEFAULT NULL,
                                        `EXCEPTION_STACK_ID_` varchar(64) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NULL DEFAULT NULL,
                                        `EXCEPTION_MSG_` varchar(4000) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NULL DEFAULT NULL,
                                        `DUEDATE_` timestamp(3) NULL DEFAULT NULL,
                                        `REPEAT_` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NULL DEFAULT NULL,
                                        `HANDLER_TYPE_` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NULL DEFAULT NULL,
                                        `HANDLER_CFG_` varchar(4000) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NULL DEFAULT NULL,
                                        `CUSTOM_VALUES_ID_` varchar(64) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NULL DEFAULT NULL,
                                        `CREATE_TIME_` timestamp(3) NULL DEFAULT NULL,
                                        `TENANT_ID_` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NULL DEFAULT '',
                                        PRIMARY KEY (`ID_`) USING BTREE,
                                        INDEX `ACT_IDX_EXTERNAL_JOB_EXCEPTION_STACK_ID`(`EXCEPTION_STACK_ID_` ASC) USING BTREE,
                                        INDEX `ACT_IDX_EXTERNAL_JOB_CUSTOM_VALUES_ID`(`CUSTOM_VALUES_ID_` ASC) USING BTREE,
                                        INDEX `ACT_IDX_EXTERNAL_JOB_CORRELATION_ID`(`CORRELATION_ID_` ASC) USING BTREE,
                                        INDEX `ACT_IDX_EJOB_SCOPE`(`SCOPE_ID_` ASC, `SCOPE_TYPE_` ASC) USING BTREE,
                                        INDEX `ACT_IDX_EJOB_SUB_SCOPE`(`SUB_SCOPE_ID_` ASC, `SCOPE_TYPE_` ASC) USING BTREE,
                                        INDEX `ACT_IDX_EJOB_SCOPE_DEF`(`SCOPE_DEFINITION_ID_` ASC, `SCOPE_TYPE_` ASC) USING BTREE,
                                        CONSTRAINT `ACT_FK_EXTERNAL_JOB_CUSTOM_VALUES` FOREIGN KEY (`CUSTOM_VALUES_ID_`) REFERENCES `ACT_GE_BYTEARRAY` (`ID_`) ON DELETE RESTRICT ON UPDATE RESTRICT,
                                        CONSTRAINT `ACT_FK_EXTERNAL_JOB_EXCEPTION` FOREIGN KEY (`EXCEPTION_STACK_ID_`) REFERENCES `ACT_GE_BYTEARRAY` (`ID_`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8mb3 COLLATE = utf8mb3_bin ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for ACT_RU_HISTORY_JOB
-- ----------------------------
DROP TABLE IF EXISTS `ACT_RU_HISTORY_JOB`;
CREATE TABLE `ACT_RU_HISTORY_JOB`  (
                                       `ID_` varchar(64) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NOT NULL,
                                       `REV_` int NULL DEFAULT NULL,
                                       `LOCK_EXP_TIME_` timestamp(3) NULL DEFAULT NULL,
                                       `LOCK_OWNER_` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NULL DEFAULT NULL,
                                       `RETRIES_` int NULL DEFAULT NULL,
                                       `EXCEPTION_STACK_ID_` varchar(64) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NULL DEFAULT NULL,
                                       `EXCEPTION_MSG_` varchar(4000) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NULL DEFAULT NULL,
                                       `HANDLER_TYPE_` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NULL DEFAULT NULL,
                                       `HANDLER_CFG_` varchar(4000) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NULL DEFAULT NULL,
                                       `CUSTOM_VALUES_ID_` varchar(64) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NULL DEFAULT NULL,
                                       `ADV_HANDLER_CFG_ID_` varchar(64) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NULL DEFAULT NULL,
                                       `CREATE_TIME_` timestamp(3) NULL DEFAULT NULL,
                                       `SCOPE_TYPE_` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NULL DEFAULT NULL,
                                       `TENANT_ID_` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NULL DEFAULT '',
                                       PRIMARY KEY (`ID_`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb3 COLLATE = utf8mb3_bin ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for ACT_RU_IDENTITYLINK
-- ----------------------------
DROP TABLE IF EXISTS `ACT_RU_IDENTITYLINK`;
CREATE TABLE `ACT_RU_IDENTITYLINK`  (
                                        `ID_` varchar(64) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NOT NULL,
                                        `REV_` int NULL DEFAULT NULL,
                                        `GROUP_ID_` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NULL DEFAULT NULL,
                                        `TYPE_` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NULL DEFAULT NULL,
                                        `USER_ID_` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NULL DEFAULT NULL,
                                        `TASK_ID_` varchar(64) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NULL DEFAULT NULL,
                                        `PROC_INST_ID_` varchar(64) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NULL DEFAULT NULL,
                                        `PROC_DEF_ID_` varchar(64) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NULL DEFAULT NULL,
                                        `SCOPE_ID_` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NULL DEFAULT NULL,
                                        `SUB_SCOPE_ID_` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NULL DEFAULT NULL,
                                        `SCOPE_TYPE_` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NULL DEFAULT NULL,
                                        `SCOPE_DEFINITION_ID_` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NULL DEFAULT NULL,
                                        PRIMARY KEY (`ID_`) USING BTREE,
                                        INDEX `ACT_IDX_IDENT_LNK_USER`(`USER_ID_` ASC) USING BTREE,
                                        INDEX `ACT_IDX_IDENT_LNK_GROUP`(`GROUP_ID_` ASC) USING BTREE,
                                        INDEX `ACT_IDX_IDENT_LNK_SCOPE`(`SCOPE_ID_` ASC, `SCOPE_TYPE_` ASC) USING BTREE,
                                        INDEX `ACT_IDX_IDENT_LNK_SUB_SCOPE`(`SUB_SCOPE_ID_` ASC, `SCOPE_TYPE_` ASC) USING BTREE,
                                        INDEX `ACT_IDX_IDENT_LNK_SCOPE_DEF`(`SCOPE_DEFINITION_ID_` ASC, `SCOPE_TYPE_` ASC) USING BTREE,
                                        INDEX `ACT_IDX_ATHRZ_PROCEDEF`(`PROC_DEF_ID_` ASC) USING BTREE,
                                        INDEX `ACT_FK_TSKASS_TASK`(`TASK_ID_` ASC) USING BTREE,
                                        INDEX `ACT_FK_IDL_PROCINST`(`PROC_INST_ID_` ASC) USING BTREE,
                                        CONSTRAINT `ACT_FK_ATHRZ_PROCEDEF` FOREIGN KEY (`PROC_DEF_ID_`) REFERENCES `ACT_RE_PROCDEF` (`ID_`) ON DELETE RESTRICT ON UPDATE RESTRICT,
                                        CONSTRAINT `ACT_FK_IDL_PROCINST` FOREIGN KEY (`PROC_INST_ID_`) REFERENCES `ACT_RU_EXECUTION` (`ID_`) ON DELETE RESTRICT ON UPDATE RESTRICT,
                                        CONSTRAINT `ACT_FK_TSKASS_TASK` FOREIGN KEY (`TASK_ID_`) REFERENCES `ACT_RU_TASK` (`ID_`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8mb3 COLLATE = utf8mb3_bin ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for ACT_RU_JOB
-- ----------------------------
DROP TABLE IF EXISTS `ACT_RU_JOB`;
CREATE TABLE `ACT_RU_JOB`  (
                               `ID_` varchar(64) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NOT NULL,
                               `REV_` int NULL DEFAULT NULL,
                               `CATEGORY_` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NULL DEFAULT NULL,
                               `TYPE_` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NOT NULL,
                               `LOCK_EXP_TIME_` timestamp(3) NULL DEFAULT NULL,
                               `LOCK_OWNER_` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NULL DEFAULT NULL,
                               `EXCLUSIVE_` tinyint(1) NULL DEFAULT NULL,
                               `EXECUTION_ID_` varchar(64) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NULL DEFAULT NULL,
                               `PROCESS_INSTANCE_ID_` varchar(64) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NULL DEFAULT NULL,
                               `PROC_DEF_ID_` varchar(64) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NULL DEFAULT NULL,
                               `ELEMENT_ID_` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NULL DEFAULT NULL,
                               `ELEMENT_NAME_` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NULL DEFAULT NULL,
                               `SCOPE_ID_` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NULL DEFAULT NULL,
                               `SUB_SCOPE_ID_` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NULL DEFAULT NULL,
                               `SCOPE_TYPE_` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NULL DEFAULT NULL,
                               `SCOPE_DEFINITION_ID_` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NULL DEFAULT NULL,
                               `CORRELATION_ID_` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NULL DEFAULT NULL,
                               `RETRIES_` int NULL DEFAULT NULL,
                               `EXCEPTION_STACK_ID_` varchar(64) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NULL DEFAULT NULL,
                               `EXCEPTION_MSG_` varchar(4000) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NULL DEFAULT NULL,
                               `DUEDATE_` timestamp(3) NULL DEFAULT NULL,
                               `REPEAT_` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NULL DEFAULT NULL,
                               `HANDLER_TYPE_` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NULL DEFAULT NULL,
                               `HANDLER_CFG_` varchar(4000) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NULL DEFAULT NULL,
                               `CUSTOM_VALUES_ID_` varchar(64) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NULL DEFAULT NULL,
                               `CREATE_TIME_` timestamp(3) NULL DEFAULT NULL,
                               `TENANT_ID_` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NULL DEFAULT '',
                               PRIMARY KEY (`ID_`) USING BTREE,
                               INDEX `ACT_IDX_JOB_EXCEPTION_STACK_ID`(`EXCEPTION_STACK_ID_` ASC) USING BTREE,
                               INDEX `ACT_IDX_JOB_CUSTOM_VALUES_ID`(`CUSTOM_VALUES_ID_` ASC) USING BTREE,
                               INDEX `ACT_IDX_JOB_CORRELATION_ID`(`CORRELATION_ID_` ASC) USING BTREE,
                               INDEX `ACT_IDX_JOB_SCOPE`(`SCOPE_ID_` ASC, `SCOPE_TYPE_` ASC) USING BTREE,
                               INDEX `ACT_IDX_JOB_SUB_SCOPE`(`SUB_SCOPE_ID_` ASC, `SCOPE_TYPE_` ASC) USING BTREE,
                               INDEX `ACT_IDX_JOB_SCOPE_DEF`(`SCOPE_DEFINITION_ID_` ASC, `SCOPE_TYPE_` ASC) USING BTREE,
                               INDEX `ACT_FK_JOB_EXECUTION`(`EXECUTION_ID_` ASC) USING BTREE,
                               INDEX `ACT_FK_JOB_PROCESS_INSTANCE`(`PROCESS_INSTANCE_ID_` ASC) USING BTREE,
                               INDEX `ACT_FK_JOB_PROC_DEF`(`PROC_DEF_ID_` ASC) USING BTREE,
                               CONSTRAINT `ACT_FK_JOB_CUSTOM_VALUES` FOREIGN KEY (`CUSTOM_VALUES_ID_`) REFERENCES `ACT_GE_BYTEARRAY` (`ID_`) ON DELETE RESTRICT ON UPDATE RESTRICT,
                               CONSTRAINT `ACT_FK_JOB_EXCEPTION` FOREIGN KEY (`EXCEPTION_STACK_ID_`) REFERENCES `ACT_GE_BYTEARRAY` (`ID_`) ON DELETE RESTRICT ON UPDATE RESTRICT,
                               CONSTRAINT `ACT_FK_JOB_EXECUTION` FOREIGN KEY (`EXECUTION_ID_`) REFERENCES `ACT_RU_EXECUTION` (`ID_`) ON DELETE RESTRICT ON UPDATE RESTRICT,
                               CONSTRAINT `ACT_FK_JOB_PROC_DEF` FOREIGN KEY (`PROC_DEF_ID_`) REFERENCES `ACT_RE_PROCDEF` (`ID_`) ON DELETE RESTRICT ON UPDATE RESTRICT,
                               CONSTRAINT `ACT_FK_JOB_PROCESS_INSTANCE` FOREIGN KEY (`PROCESS_INSTANCE_ID_`) REFERENCES `ACT_RU_EXECUTION` (`ID_`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8mb3 COLLATE = utf8mb3_bin ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for ACT_RU_SUSPENDED_JOB
-- ----------------------------
DROP TABLE IF EXISTS `ACT_RU_SUSPENDED_JOB`;
CREATE TABLE `ACT_RU_SUSPENDED_JOB`  (
                                         `ID_` varchar(64) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NOT NULL,
                                         `REV_` int NULL DEFAULT NULL,
                                         `CATEGORY_` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NULL DEFAULT NULL,
                                         `TYPE_` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NOT NULL,
                                         `EXCLUSIVE_` tinyint(1) NULL DEFAULT NULL,
                                         `EXECUTION_ID_` varchar(64) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NULL DEFAULT NULL,
                                         `PROCESS_INSTANCE_ID_` varchar(64) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NULL DEFAULT NULL,
                                         `PROC_DEF_ID_` varchar(64) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NULL DEFAULT NULL,
                                         `ELEMENT_ID_` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NULL DEFAULT NULL,
                                         `ELEMENT_NAME_` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NULL DEFAULT NULL,
                                         `SCOPE_ID_` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NULL DEFAULT NULL,
                                         `SUB_SCOPE_ID_` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NULL DEFAULT NULL,
                                         `SCOPE_TYPE_` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NULL DEFAULT NULL,
                                         `SCOPE_DEFINITION_ID_` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NULL DEFAULT NULL,
                                         `CORRELATION_ID_` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NULL DEFAULT NULL,
                                         `RETRIES_` int NULL DEFAULT NULL,
                                         `EXCEPTION_STACK_ID_` varchar(64) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NULL DEFAULT NULL,
                                         `EXCEPTION_MSG_` varchar(4000) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NULL DEFAULT NULL,
                                         `DUEDATE_` timestamp(3) NULL DEFAULT NULL,
                                         `REPEAT_` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NULL DEFAULT NULL,
                                         `HANDLER_TYPE_` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NULL DEFAULT NULL,
                                         `HANDLER_CFG_` varchar(4000) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NULL DEFAULT NULL,
                                         `CUSTOM_VALUES_ID_` varchar(64) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NULL DEFAULT NULL,
                                         `CREATE_TIME_` timestamp(3) NULL DEFAULT NULL,
                                         `TENANT_ID_` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NULL DEFAULT '',
                                         PRIMARY KEY (`ID_`) USING BTREE,
                                         INDEX `ACT_IDX_SUSPENDED_JOB_EXCEPTION_STACK_ID`(`EXCEPTION_STACK_ID_` ASC) USING BTREE,
                                         INDEX `ACT_IDX_SUSPENDED_JOB_CUSTOM_VALUES_ID`(`CUSTOM_VALUES_ID_` ASC) USING BTREE,
                                         INDEX `ACT_IDX_SUSPENDED_JOB_CORRELATION_ID`(`CORRELATION_ID_` ASC) USING BTREE,
                                         INDEX `ACT_IDX_SJOB_SCOPE`(`SCOPE_ID_` ASC, `SCOPE_TYPE_` ASC) USING BTREE,
                                         INDEX `ACT_IDX_SJOB_SUB_SCOPE`(`SUB_SCOPE_ID_` ASC, `SCOPE_TYPE_` ASC) USING BTREE,
                                         INDEX `ACT_IDX_SJOB_SCOPE_DEF`(`SCOPE_DEFINITION_ID_` ASC, `SCOPE_TYPE_` ASC) USING BTREE,
                                         INDEX `ACT_FK_SUSPENDED_JOB_EXECUTION`(`EXECUTION_ID_` ASC) USING BTREE,
                                         INDEX `ACT_FK_SUSPENDED_JOB_PROCESS_INSTANCE`(`PROCESS_INSTANCE_ID_` ASC) USING BTREE,
                                         INDEX `ACT_FK_SUSPENDED_JOB_PROC_DEF`(`PROC_DEF_ID_` ASC) USING BTREE,
                                         CONSTRAINT `ACT_FK_SUSPENDED_JOB_CUSTOM_VALUES` FOREIGN KEY (`CUSTOM_VALUES_ID_`) REFERENCES `ACT_GE_BYTEARRAY` (`ID_`) ON DELETE RESTRICT ON UPDATE RESTRICT,
                                         CONSTRAINT `ACT_FK_SUSPENDED_JOB_EXCEPTION` FOREIGN KEY (`EXCEPTION_STACK_ID_`) REFERENCES `ACT_GE_BYTEARRAY` (`ID_`) ON DELETE RESTRICT ON UPDATE RESTRICT,
                                         CONSTRAINT `ACT_FK_SUSPENDED_JOB_EXECUTION` FOREIGN KEY (`EXECUTION_ID_`) REFERENCES `ACT_RU_EXECUTION` (`ID_`) ON DELETE RESTRICT ON UPDATE RESTRICT,
                                         CONSTRAINT `ACT_FK_SUSPENDED_JOB_PROC_DEF` FOREIGN KEY (`PROC_DEF_ID_`) REFERENCES `ACT_RE_PROCDEF` (`ID_`) ON DELETE RESTRICT ON UPDATE RESTRICT,
                                         CONSTRAINT `ACT_FK_SUSPENDED_JOB_PROCESS_INSTANCE` FOREIGN KEY (`PROCESS_INSTANCE_ID_`) REFERENCES `ACT_RU_EXECUTION` (`ID_`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8mb3 COLLATE = utf8mb3_bin ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for ACT_RU_TASK
-- ----------------------------
DROP TABLE IF EXISTS `ACT_RU_TASK`;
CREATE TABLE `ACT_RU_TASK`  (
                                `ID_` varchar(64) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NOT NULL,
                                `REV_` int NULL DEFAULT NULL,
                                `EXECUTION_ID_` varchar(64) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NULL DEFAULT NULL,
                                `PROC_INST_ID_` varchar(64) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NULL DEFAULT NULL,
                                `PROC_DEF_ID_` varchar(64) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NULL DEFAULT NULL,
                                `TASK_DEF_ID_` varchar(64) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NULL DEFAULT NULL,
                                `SCOPE_ID_` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NULL DEFAULT NULL,
                                `SUB_SCOPE_ID_` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NULL DEFAULT NULL,
                                `SCOPE_TYPE_` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NULL DEFAULT NULL,
                                `SCOPE_DEFINITION_ID_` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NULL DEFAULT NULL,
                                `PROPAGATED_STAGE_INST_ID_` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NULL DEFAULT NULL,
                                `NAME_` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NULL DEFAULT NULL,
                                `PARENT_TASK_ID_` varchar(64) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NULL DEFAULT NULL,
                                `DESCRIPTION_` varchar(4000) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NULL DEFAULT NULL,
                                `TASK_DEF_KEY_` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NULL DEFAULT NULL,
                                `OWNER_` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NULL DEFAULT NULL,
                                `ASSIGNEE_` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NULL DEFAULT NULL,
                                `DELEGATION_` varchar(64) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NULL DEFAULT NULL,
                                `PRIORITY_` int NULL DEFAULT NULL,
                                `CREATE_TIME_` timestamp(3) NULL DEFAULT NULL,
                                `DUE_DATE_` datetime(3) NULL DEFAULT NULL,
                                `CATEGORY_` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NULL DEFAULT NULL,
                                `SUSPENSION_STATE_` int NULL DEFAULT NULL,
                                `TENANT_ID_` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NULL DEFAULT '',
                                `FORM_KEY_` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NULL DEFAULT NULL,
                                `CLAIM_TIME_` datetime(3) NULL DEFAULT NULL,
                                `IS_COUNT_ENABLED_` tinyint NULL DEFAULT NULL,
                                `VAR_COUNT_` int NULL DEFAULT NULL,
                                `ID_LINK_COUNT_` int NULL DEFAULT NULL,
                                `SUB_TASK_COUNT_` int NULL DEFAULT NULL,
                                PRIMARY KEY (`ID_`) USING BTREE,
                                INDEX `ACT_IDX_TASK_CREATE`(`CREATE_TIME_` ASC) USING BTREE,
                                INDEX `ACT_IDX_TASK_SCOPE`(`SCOPE_ID_` ASC, `SCOPE_TYPE_` ASC) USING BTREE,
                                INDEX `ACT_IDX_TASK_SUB_SCOPE`(`SUB_SCOPE_ID_` ASC, `SCOPE_TYPE_` ASC) USING BTREE,
                                INDEX `ACT_IDX_TASK_SCOPE_DEF`(`SCOPE_DEFINITION_ID_` ASC, `SCOPE_TYPE_` ASC) USING BTREE,
                                INDEX `ACT_FK_TASK_EXE`(`EXECUTION_ID_` ASC) USING BTREE,
                                INDEX `ACT_FK_TASK_PROCINST`(`PROC_INST_ID_` ASC) USING BTREE,
                                INDEX `ACT_FK_TASK_PROCDEF`(`PROC_DEF_ID_` ASC) USING BTREE,
                                CONSTRAINT `ACT_FK_TASK_EXE` FOREIGN KEY (`EXECUTION_ID_`) REFERENCES `ACT_RU_EXECUTION` (`ID_`) ON DELETE RESTRICT ON UPDATE RESTRICT,
                                CONSTRAINT `ACT_FK_TASK_PROCDEF` FOREIGN KEY (`PROC_DEF_ID_`) REFERENCES `ACT_RE_PROCDEF` (`ID_`) ON DELETE RESTRICT ON UPDATE RESTRICT,
                                CONSTRAINT `ACT_FK_TASK_PROCINST` FOREIGN KEY (`PROC_INST_ID_`) REFERENCES `ACT_RU_EXECUTION` (`ID_`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8mb3 COLLATE = utf8mb3_bin ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for ACT_RU_TIMER_JOB
-- ----------------------------
DROP TABLE IF EXISTS `ACT_RU_TIMER_JOB`;
CREATE TABLE `ACT_RU_TIMER_JOB`  (
                                     `ID_` varchar(64) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NOT NULL,
                                     `REV_` int NULL DEFAULT NULL,
                                     `CATEGORY_` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NULL DEFAULT NULL,
                                     `TYPE_` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NOT NULL,
                                     `LOCK_EXP_TIME_` timestamp(3) NULL DEFAULT NULL,
                                     `LOCK_OWNER_` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NULL DEFAULT NULL,
                                     `EXCLUSIVE_` tinyint(1) NULL DEFAULT NULL,
                                     `EXECUTION_ID_` varchar(64) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NULL DEFAULT NULL,
                                     `PROCESS_INSTANCE_ID_` varchar(64) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NULL DEFAULT NULL,
                                     `PROC_DEF_ID_` varchar(64) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NULL DEFAULT NULL,
                                     `ELEMENT_ID_` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NULL DEFAULT NULL,
                                     `ELEMENT_NAME_` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NULL DEFAULT NULL,
                                     `SCOPE_ID_` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NULL DEFAULT NULL,
                                     `SUB_SCOPE_ID_` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NULL DEFAULT NULL,
                                     `SCOPE_TYPE_` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NULL DEFAULT NULL,
                                     `SCOPE_DEFINITION_ID_` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NULL DEFAULT NULL,
                                     `CORRELATION_ID_` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NULL DEFAULT NULL,
                                     `RETRIES_` int NULL DEFAULT NULL,
                                     `EXCEPTION_STACK_ID_` varchar(64) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NULL DEFAULT NULL,
                                     `EXCEPTION_MSG_` varchar(4000) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NULL DEFAULT NULL,
                                     `DUEDATE_` timestamp(3) NULL DEFAULT NULL,
                                     `REPEAT_` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NULL DEFAULT NULL,
                                     `HANDLER_TYPE_` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NULL DEFAULT NULL,
                                     `HANDLER_CFG_` varchar(4000) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NULL DEFAULT NULL,
                                     `CUSTOM_VALUES_ID_` varchar(64) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NULL DEFAULT NULL,
                                     `CREATE_TIME_` timestamp(3) NULL DEFAULT NULL,
                                     `TENANT_ID_` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NULL DEFAULT '',
                                     PRIMARY KEY (`ID_`) USING BTREE,
                                     INDEX `ACT_IDX_TIMER_JOB_EXCEPTION_STACK_ID`(`EXCEPTION_STACK_ID_` ASC) USING BTREE,
                                     INDEX `ACT_IDX_TIMER_JOB_CUSTOM_VALUES_ID`(`CUSTOM_VALUES_ID_` ASC) USING BTREE,
                                     INDEX `ACT_IDX_TIMER_JOB_CORRELATION_ID`(`CORRELATION_ID_` ASC) USING BTREE,
                                     INDEX `ACT_IDX_TIMER_JOB_DUEDATE`(`DUEDATE_` ASC) USING BTREE,
                                     INDEX `ACT_IDX_TJOB_SCOPE`(`SCOPE_ID_` ASC, `SCOPE_TYPE_` ASC) USING BTREE,
                                     INDEX `ACT_IDX_TJOB_SUB_SCOPE`(`SUB_SCOPE_ID_` ASC, `SCOPE_TYPE_` ASC) USING BTREE,
                                     INDEX `ACT_IDX_TJOB_SCOPE_DEF`(`SCOPE_DEFINITION_ID_` ASC, `SCOPE_TYPE_` ASC) USING BTREE,
                                     INDEX `ACT_FK_TIMER_JOB_EXECUTION`(`EXECUTION_ID_` ASC) USING BTREE,
                                     INDEX `ACT_FK_TIMER_JOB_PROCESS_INSTANCE`(`PROCESS_INSTANCE_ID_` ASC) USING BTREE,
                                     INDEX `ACT_FK_TIMER_JOB_PROC_DEF`(`PROC_DEF_ID_` ASC) USING BTREE,
                                     CONSTRAINT `ACT_FK_TIMER_JOB_CUSTOM_VALUES` FOREIGN KEY (`CUSTOM_VALUES_ID_`) REFERENCES `ACT_GE_BYTEARRAY` (`ID_`) ON DELETE RESTRICT ON UPDATE RESTRICT,
                                     CONSTRAINT `ACT_FK_TIMER_JOB_EXCEPTION` FOREIGN KEY (`EXCEPTION_STACK_ID_`) REFERENCES `ACT_GE_BYTEARRAY` (`ID_`) ON DELETE RESTRICT ON UPDATE RESTRICT,
                                     CONSTRAINT `ACT_FK_TIMER_JOB_EXECUTION` FOREIGN KEY (`EXECUTION_ID_`) REFERENCES `ACT_RU_EXECUTION` (`ID_`) ON DELETE RESTRICT ON UPDATE RESTRICT,
                                     CONSTRAINT `ACT_FK_TIMER_JOB_PROC_DEF` FOREIGN KEY (`PROC_DEF_ID_`) REFERENCES `ACT_RE_PROCDEF` (`ID_`) ON DELETE RESTRICT ON UPDATE RESTRICT,
                                     CONSTRAINT `ACT_FK_TIMER_JOB_PROCESS_INSTANCE` FOREIGN KEY (`PROCESS_INSTANCE_ID_`) REFERENCES `ACT_RU_EXECUTION` (`ID_`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8mb3 COLLATE = utf8mb3_bin ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for ACT_RU_VARIABLE
-- ----------------------------
DROP TABLE IF EXISTS `ACT_RU_VARIABLE`;
CREATE TABLE `ACT_RU_VARIABLE`  (
                                    `ID_` varchar(64) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NOT NULL,
                                    `REV_` int NULL DEFAULT NULL,
                                    `TYPE_` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NOT NULL,
                                    `NAME_` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NOT NULL,
                                    `EXECUTION_ID_` varchar(64) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NULL DEFAULT NULL,
                                    `PROC_INST_ID_` varchar(64) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NULL DEFAULT NULL,
                                    `TASK_ID_` varchar(64) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NULL DEFAULT NULL,
                                    `SCOPE_ID_` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NULL DEFAULT NULL,
                                    `SUB_SCOPE_ID_` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NULL DEFAULT NULL,
                                    `SCOPE_TYPE_` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NULL DEFAULT NULL,
                                    `BYTEARRAY_ID_` varchar(64) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NULL DEFAULT NULL,
                                    `DOUBLE_` double NULL DEFAULT NULL,
                                    `LONG_` bigint NULL DEFAULT NULL,
                                    `TEXT_` varchar(4000) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NULL DEFAULT NULL,
                                    `TEXT2_` varchar(4000) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NULL DEFAULT NULL,
                                    PRIMARY KEY (`ID_`) USING BTREE,
                                    INDEX `ACT_IDX_RU_VAR_SCOPE_ID_TYPE`(`SCOPE_ID_` ASC, `SCOPE_TYPE_` ASC) USING BTREE,
                                    INDEX `ACT_IDX_RU_VAR_SUB_ID_TYPE`(`SUB_SCOPE_ID_` ASC, `SCOPE_TYPE_` ASC) USING BTREE,
                                    INDEX `ACT_FK_VAR_BYTEARRAY`(`BYTEARRAY_ID_` ASC) USING BTREE,
                                    INDEX `ACT_IDX_VARIABLE_TASK_ID`(`TASK_ID_` ASC) USING BTREE,
                                    INDEX `ACT_FK_VAR_EXE`(`EXECUTION_ID_` ASC) USING BTREE,
                                    INDEX `ACT_FK_VAR_PROCINST`(`PROC_INST_ID_` ASC) USING BTREE,
                                    CONSTRAINT `ACT_FK_VAR_BYTEARRAY` FOREIGN KEY (`BYTEARRAY_ID_`) REFERENCES `ACT_GE_BYTEARRAY` (`ID_`) ON DELETE RESTRICT ON UPDATE RESTRICT,
                                    CONSTRAINT `ACT_FK_VAR_EXE` FOREIGN KEY (`EXECUTION_ID_`) REFERENCES `ACT_RU_EXECUTION` (`ID_`) ON DELETE RESTRICT ON UPDATE RESTRICT,
                                    CONSTRAINT `ACT_FK_VAR_PROCINST` FOREIGN KEY (`PROC_INST_ID_`) REFERENCES `ACT_RU_EXECUTION` (`ID_`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8mb3 COLLATE = utf8mb3_bin ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for FLW_CHANNEL_DEFINITION
-- ----------------------------
DROP TABLE IF EXISTS `FLW_CHANNEL_DEFINITION`;
CREATE TABLE `FLW_CHANNEL_DEFINITION`  (
                                           `ID_` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
                                           `NAME_` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
                                           `VERSION_` int NULL DEFAULT NULL,
                                           `KEY_` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
                                           `CATEGORY_` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
                                           `DEPLOYMENT_ID_` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
                                           `CREATE_TIME_` datetime(3) NULL DEFAULT NULL,
                                           `TENANT_ID_` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
                                           `RESOURCE_NAME_` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
                                           `DESCRIPTION_` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
                                           `TYPE_` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
                                           `IMPLEMENTATION_` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
                                           PRIMARY KEY (`ID_`) USING BTREE,
                                           UNIQUE INDEX `ACT_IDX_CHANNEL_DEF_UNIQ`(`KEY_` ASC, `VERSION_` ASC, `TENANT_ID_` ASC) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for FLW_EVENT_DEFINITION
-- ----------------------------
DROP TABLE IF EXISTS `FLW_EVENT_DEFINITION`;
CREATE TABLE `FLW_EVENT_DEFINITION`  (
                                         `ID_` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
                                         `NAME_` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
                                         `VERSION_` int NULL DEFAULT NULL,
                                         `KEY_` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
                                         `CATEGORY_` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
                                         `DEPLOYMENT_ID_` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
                                         `TENANT_ID_` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
                                         `RESOURCE_NAME_` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
                                         `DESCRIPTION_` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
                                         PRIMARY KEY (`ID_`) USING BTREE,
                                         UNIQUE INDEX `ACT_IDX_EVENT_DEF_UNIQ`(`KEY_` ASC, `VERSION_` ASC, `TENANT_ID_` ASC) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for FLW_EVENT_DEPLOYMENT
-- ----------------------------
DROP TABLE IF EXISTS `FLW_EVENT_DEPLOYMENT`;
CREATE TABLE `FLW_EVENT_DEPLOYMENT`  (
                                         `ID_` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
                                         `NAME_` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
                                         `CATEGORY_` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
                                         `DEPLOY_TIME_` datetime(3) NULL DEFAULT NULL,
                                         `TENANT_ID_` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
                                         `PARENT_DEPLOYMENT_ID_` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
                                         PRIMARY KEY (`ID_`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for FLW_EVENT_RESOURCE
-- ----------------------------
DROP TABLE IF EXISTS `FLW_EVENT_RESOURCE`;
CREATE TABLE `FLW_EVENT_RESOURCE`  (
                                       `ID_` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
                                       `NAME_` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
                                       `DEPLOYMENT_ID_` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
                                       `RESOURCE_BYTES_` longblob NULL,
                                       PRIMARY KEY (`ID_`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for FLW_EV_DATABASECHANGELOG
-- ----------------------------
DROP TABLE IF EXISTS `FLW_EV_DATABASECHANGELOG`;
CREATE TABLE `FLW_EV_DATABASECHANGELOG`  (
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
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for FLW_EV_DATABASECHANGELOGLOCK
-- ----------------------------
DROP TABLE IF EXISTS `FLW_EV_DATABASECHANGELOGLOCK`;
CREATE TABLE `FLW_EV_DATABASECHANGELOGLOCK`  (
                                                 `ID` int NOT NULL,
                                                 `LOCKED` bit(1) NOT NULL,
                                                 `LOCKGRANTED` datetime NULL DEFAULT NULL,
                                                 `LOCKEDBY` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
                                                 PRIMARY KEY (`ID`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for FLW_RU_BATCH
-- ----------------------------
DROP TABLE IF EXISTS `FLW_RU_BATCH`;
CREATE TABLE `FLW_RU_BATCH`  (
                                 `ID_` varchar(64) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NOT NULL,
                                 `REV_` int NULL DEFAULT NULL,
                                 `TYPE_` varchar(64) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NOT NULL,
                                 `SEARCH_KEY_` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NULL DEFAULT NULL,
                                 `SEARCH_KEY2_` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NULL DEFAULT NULL,
                                 `CREATE_TIME_` datetime(3) NOT NULL,
                                 `COMPLETE_TIME_` datetime(3) NULL DEFAULT NULL,
                                 `STATUS_` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NULL DEFAULT NULL,
                                 `BATCH_DOC_ID_` varchar(64) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NULL DEFAULT NULL,
                                 `TENANT_ID_` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NULL DEFAULT '',
                                 PRIMARY KEY (`ID_`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb3 COLLATE = utf8mb3_bin ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for FLW_RU_BATCH_PART
-- ----------------------------
DROP TABLE IF EXISTS `FLW_RU_BATCH_PART`;
CREATE TABLE `FLW_RU_BATCH_PART`  (
                                      `ID_` varchar(64) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NOT NULL,
                                      `REV_` int NULL DEFAULT NULL,
                                      `BATCH_ID_` varchar(64) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NULL DEFAULT NULL,
                                      `TYPE_` varchar(64) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NOT NULL,
                                      `SCOPE_ID_` varchar(64) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NULL DEFAULT NULL,
                                      `SUB_SCOPE_ID_` varchar(64) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NULL DEFAULT NULL,
                                      `SCOPE_TYPE_` varchar(64) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NULL DEFAULT NULL,
                                      `SEARCH_KEY_` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NULL DEFAULT NULL,
                                      `SEARCH_KEY2_` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NULL DEFAULT NULL,
                                      `CREATE_TIME_` datetime(3) NOT NULL,
                                      `COMPLETE_TIME_` datetime(3) NULL DEFAULT NULL,
                                      `STATUS_` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NULL DEFAULT NULL,
                                      `RESULT_DOC_ID_` varchar(64) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NULL DEFAULT NULL,
                                      `TENANT_ID_` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NULL DEFAULT '',
                                      PRIMARY KEY (`ID_`) USING BTREE,
                                      INDEX `FLW_IDX_BATCH_PART`(`BATCH_ID_` ASC) USING BTREE,
                                      CONSTRAINT `FLW_FK_BATCH_PART_PARENT` FOREIGN KEY (`BATCH_ID_`) REFERENCES `FLW_RU_BATCH` (`ID_`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8mb3 COLLATE = utf8mb3_bin ROW_FORMAT = DYNAMIC;

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
-- Table structure for undo_log
-- ----------------------------
DROP TABLE IF EXISTS `undo_log`;
CREATE TABLE `undo_log`  (
                             `id` bigint NOT NULL AUTO_INCREMENT,
                             `branch_id` bigint NOT NULL,
                             `xid` varchar(100) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL,
                             `context` varchar(128) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL,
                             `rollback_info` longblob NOT NULL,
                             `log_status` int NOT NULL,
                             `log_created` datetime NOT NULL,
                             `log_modified` datetime NOT NULL,
                             `ext` varchar(100) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL,
                             PRIMARY KEY (`id`) USING BTREE,
                             UNIQUE INDEX `ux_undo_log`(`xid` ASC, `branch_id` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 48 CHARACTER SET = utf8mb3 COLLATE = utf8mb3_general_ci ROW_FORMAT = DYNAMIC;

SET FOREIGN_KEY_CHECKS = 1;
