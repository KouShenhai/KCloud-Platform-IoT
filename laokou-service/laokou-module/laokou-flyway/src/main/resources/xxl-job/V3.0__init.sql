/*
 Navicat Premium Data Transfer

 Source Server         : 192.168.30.133(老寇）
 Source Server Type    : MySQL
 Source Server Version : 80033 (8.0.33)
 Source Host           : 192.168.30.133:3306
 Source Schema         : kcloud_platform_alibaba_xxl_job

 Target Server Type    : MySQL
 Target Server Version : 80033 (8.0.33)
 File Encoding         : 65001

 Date: 08/11/2023 14:19:33
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

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
-- Table structure for xxl_job_group
-- ----------------------------
DROP TABLE IF EXISTS `xxl_job_group`;
CREATE TABLE `xxl_job_group`  (
                                  `id` int NOT NULL AUTO_INCREMENT,
                                  `app_name` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '执行器AppName',
                                  `title` varchar(12) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '执行器名称',
                                  `address_type` tinyint NOT NULL DEFAULT 0 COMMENT '执行器地址类型：0=自动注册、1=手动录入',
                                  `address_list` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL COMMENT '执行器地址列表，多地址逗号分隔',
                                  `update_time` datetime NULL DEFAULT NULL,
                                  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 5 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of xxl_job_group
-- ----------------------------
INSERT INTO `xxl_job_group` VALUES (4, 'laokou-logstash', '分布式链路', 0, 'http://192.168.30.1:9999/', '2023-11-07 11:44:45');

-- ----------------------------
-- Table structure for xxl_job_info
-- ----------------------------
DROP TABLE IF EXISTS `xxl_job_info`;
CREATE TABLE `xxl_job_info`  (
                                 `id` int NOT NULL AUTO_INCREMENT,
                                 `job_group` int NOT NULL COMMENT '执行器主键ID',
                                 `job_desc` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
                                 `add_time` datetime NULL DEFAULT NULL,
                                 `update_time` datetime NULL DEFAULT NULL,
                                 `author` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '作者',
                                 `alarm_email` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '报警邮件',
                                 `schedule_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT 'NONE' COMMENT '调度类型',
                                 `schedule_conf` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '调度配置，值含义取决于调度类型',
                                 `misfire_strategy` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT 'DO_NOTHING' COMMENT '调度过期策略',
                                 `executor_route_strategy` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '执行器路由策略',
                                 `executor_handler` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '执行器任务handler',
                                 `executor_param` varchar(512) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '执行器任务参数',
                                 `executor_block_strategy` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '阻塞处理策略',
                                 `executor_timeout` int NOT NULL DEFAULT 0 COMMENT '任务执行超时时间，单位秒',
                                 `executor_fail_retry_count` int NOT NULL DEFAULT 0 COMMENT '失败重试次数',
                                 `glue_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT 'GLUE类型',
                                 `glue_source` mediumtext CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL COMMENT 'GLUE源代码',
                                 `glue_remark` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT 'GLUE备注',
                                 `glue_updatetime` datetime NULL DEFAULT NULL COMMENT 'GLUE更新时间',
                                 `child_jobid` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '子任务ID，多个逗号分隔',
                                 `trigger_status` tinyint NOT NULL DEFAULT 0 COMMENT '调度状态：0-停止，1-运行',
                                 `trigger_last_time` bigint NOT NULL DEFAULT 0 COMMENT '上次调度时间',
                                 `trigger_next_time` bigint NOT NULL DEFAULT 0 COMMENT '下次调度时间',
                                 PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 4 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of xxl_job_info
-- ----------------------------
INSERT INTO `xxl_job_info` VALUES (3, 4, '分布式链路', '2023-11-03 15:45:55', '2023-11-03 15:45:55', 'laokou', '2413176044@qq.com', 'CRON', '0 50 23 L * ?', 'DO_NOTHING', 'FIRST', 'traceJobHandler', '', 'SERIAL_EXECUTION', 0, 0, 'BEAN', '', 'GLUE代码初始化', '2023-11-03 15:45:55', '', 0, 0, 0);

-- ----------------------------
-- Table structure for xxl_job_lock
-- ----------------------------
DROP TABLE IF EXISTS `xxl_job_lock`;
CREATE TABLE `xxl_job_lock`  (
                                 `lock_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '锁名称',
                                 PRIMARY KEY (`lock_name`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of xxl_job_lock
-- ----------------------------
INSERT INTO `xxl_job_lock` VALUES ('schedule_lock');

-- ----------------------------
-- Table structure for xxl_job_log
-- ----------------------------
DROP TABLE IF EXISTS `xxl_job_log`;
CREATE TABLE `xxl_job_log`  (
                                `id` bigint NOT NULL AUTO_INCREMENT,
                                `job_group` int NOT NULL COMMENT '执行器主键ID',
                                `job_id` int NOT NULL COMMENT '任务，主键ID',
                                `executor_address` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '执行器地址，本次执行的地址',
                                `executor_handler` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '执行器任务handler',
                                `executor_param` varchar(512) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '执行器任务参数',
                                `executor_sharding_param` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '执行器任务分片参数，格式如 1/2',
                                `executor_fail_retry_count` int NOT NULL DEFAULT 0 COMMENT '失败重试次数',
                                `trigger_time` datetime NULL DEFAULT NULL COMMENT '调度-时间',
                                `trigger_code` int NOT NULL COMMENT '调度-结果',
                                `trigger_msg` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL COMMENT '调度-日志',
                                `handle_time` datetime NULL DEFAULT NULL COMMENT '执行-时间',
                                `handle_code` int NOT NULL COMMENT '执行-状态',
                                `handle_msg` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL COMMENT '执行-日志',
                                `alarm_status` tinyint NOT NULL DEFAULT 0 COMMENT '告警状态：0-默认、1-无需告警、2-告警成功、3-告警失败',
                                PRIMARY KEY (`id`) USING BTREE,
                                INDEX `I_trigger_time`(`trigger_time` ASC) USING BTREE,
                                INDEX `I_handle_code`(`handle_code` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 168 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of xxl_job_log
-- ----------------------------
INSERT INTO `xxl_job_log` VALUES (162, 4, 3, 'http://192.168.30.1:9999/', 'traceJobHandler', '', NULL, 0, '2023-11-03 15:56:26', 200, '任务触发类型：手动触发<br>调度机器：192.168.30.1<br>执行器-注册方式：自动注册<br>执行器-地址列表：[http://192.168.30.1:9999/]<br>路由策略：第一个<br>阻塞处理策略：单机串行<br>任务超时时间：0<br>失败重试次数：0<br><br><span style=\"color:#00c0ef;\" > >>>>>>>>>>>触发调度<<<<<<<<<<< </span><br>触发调度：<br>address：http://192.168.30.1:9999/<br>code：200<br>msg：null', '2023-11-03 15:56:26', 200, '执行成功', 0);
INSERT INTO `xxl_job_log` VALUES (163, 4, 3, 'http://192.168.30.1:9999/', 'traceJobHandler', '', NULL, 0, '2023-11-03 16:07:59', 200, '任务触发类型：手动触发<br>调度机器：192.168.30.1<br>执行器-注册方式：自动注册<br>执行器-地址列表：[http://192.168.30.1:9999/]<br>路由策略：第一个<br>阻塞处理策略：单机串行<br>任务超时时间：0<br>失败重试次数：0<br><br><span style=\"color:#00c0ef;\" > >>>>>>>>>>>触发调度<<<<<<<<<<< </span><br>触发调度：<br>address：http://192.168.30.1:9999/<br>code：200<br>msg：null', '2023-11-03 16:07:59', 200, '创建索引【{}】执行成功laokou_trace_202311', 0);
INSERT INTO `xxl_job_log` VALUES (164, 4, 3, 'http://192.168.30.1:9999/', 'traceJobHandler', '2022-10-01', NULL, 0, '2023-11-03 16:10:34', 200, '任务触发类型：手动触发<br>调度机器：192.168.30.1<br>执行器-注册方式：自动注册<br>执行器-地址列表：[http://192.168.30.1:9999/]<br>路由策略：第一个<br>阻塞处理策略：单机串行<br>任务超时时间：0<br>失败重试次数：0<br><br><span style=\"color:#00c0ef;\" > >>>>>>>>>>>触发调度<<<<<<<<<<< </span><br>触发调度：<br>address：http://192.168.30.1:9999/<br>code：200<br>msg：null', '2023-11-03 16:10:34', 200, '创建索引【{laokou_trace_202210}】执行成功', 0);
INSERT INTO `xxl_job_log` VALUES (165, 4, 3, 'http://192.168.30.1:9999/', 'traceJobHandler', '2022-01-02', NULL, 0, '2023-11-03 16:20:23', 200, '任务触发类型：手动触发<br>调度机器：192.168.30.1<br>执行器-注册方式：自动注册<br>执行器-地址列表：[http://192.168.30.1:9999/]<br>路由策略：第一个<br>阻塞处理策略：单机串行<br>任务超时时间：0<br>失败重试次数：0<br><br><span style=\"color:#00c0ef;\" > >>>>>>>>>>>触发调度<<<<<<<<<<< </span><br>触发调度：<br>address：http://192.168.30.1:9999/<br>code：200<br>msg：null', '2023-11-03 16:20:24', 200, '创建索引【{laokou_trace_202201}】执行成功', 0);
INSERT INTO `xxl_job_log` VALUES (166, 4, 3, 'http://192.168.30.1:9999/', 'traceJobHandler', '2022-01222', NULL, 0, '2023-11-03 16:21:07', 200, '任务触发类型：手动触发<br>调度机器：192.168.30.1<br>执行器-注册方式：自动注册<br>执行器-地址列表：[http://192.168.30.1:9999/]<br>路由策略：第一个<br>阻塞处理策略：单机串行<br>任务超时时间：0<br>失败重试次数：0<br><br><span style=\"color:#00c0ef;\" > >>>>>>>>>>>触发调度<<<<<<<<<<< </span><br>触发调度：<br>address：http://192.168.30.1:9999/<br>code：200<br>msg：null', '2023-11-03 16:21:07', 500, 'java.lang.reflect.InvocationTargetException\r\n	at java.base/jdk.internal.reflect.NativeMethodAccessorImpl.invoke0(Native Method)\r\n	at java.base/jdk.internal.reflect.NativeMethodAccessorImpl.invoke(NativeMethodAccessorImpl.java:77)\r\n	at java.base/jdk.internal.reflect.DelegatingMethodAccessorImpl.invoke(DelegatingMethodAccessorImpl.java:43)\r\n	at java.base/java.lang.reflect.Method.invoke(Method.java:568)\r\n	at com.xxl.job.core.handler.impl.MethodJobHandler.execute(MethodJobHandler.java:31)\r\n	at com.xxl.job.core.thread.JobThread.run(JobThread.java:166)\r\nCaused by: java.time.format.DateTimeParseException: Text \'2022-01222\' could not be parsed at index 7\r\n	at java.base/java.time.format.DateTimeFormatter.parseResolved0(DateTimeFormatter.java:2052)\r\n	at java.base/java.time.format.DateTimeFormatter.parse(DateTimeFormatter.java:1954)\r\n	at java.base/java.time.LocalDate.parse(LocalDate.java:430)\r\n	at org.laokou.common.i18n.utils.DateUtil.parseDate(DateUtil.java:143)\r\n	at org.laokou.logstash.consumer.TraceConsumer.scheduleCreateIndex(TraceConsumer.java:69)\r\n	... 6 more\r\n', 2);
INSERT INTO `xxl_job_log` VALUES (167, 4, 3, 'http://192.168.30.1:9999/', 'traceJobHandler', '2222222222222', NULL, 0, '2023-11-03 16:22:15', 200, '任务触发类型：手动触发<br>调度机器：192.168.30.1<br>执行器-注册方式：自动注册<br>执行器-地址列表：[http://192.168.30.1:9999/]<br>路由策略：第一个<br>阻塞处理策略：单机串行<br>任务超时时间：0<br>失败重试次数：0<br><br><span style=\"color:#00c0ef;\" > >>>>>>>>>>>触发调度<<<<<<<<<<< </span><br>触发调度：<br>address：http://192.168.30.1:9999/<br>code：200<br>msg：null', '2023-11-03 16:22:15', 500, 'java.lang.reflect.InvocationTargetException\r\n	at java.base/jdk.internal.reflect.NativeMethodAccessorImpl.invoke0(Native Method)\r\n	at java.base/jdk.internal.reflect.NativeMethodAccessorImpl.invoke(NativeMethodAccessorImpl.java:77)\r\n	at java.base/jdk.internal.reflect.DelegatingMethodAccessorImpl.invoke(DelegatingMethodAccessorImpl.java:43)\r\n	at java.base/java.lang.reflect.Method.invoke(Method.java:568)\r\n	at com.xxl.job.core.handler.impl.MethodJobHandler.execute(MethodJobHandler.java:31)\r\n	at com.xxl.job.core.thread.JobThread.run(JobThread.java:166)\r\nCaused by: java.time.format.DateTimeParseException: Text \'2222222222222\' could not be parsed at index 0\r\n	at java.base/java.time.format.DateTimeFormatter.parseResolved0(DateTimeFormatter.java:2052)\r\n	at java.base/java.time.format.DateTimeFormatter.parse(DateTimeFormatter.java:1954)\r\n	at java.base/java.time.LocalDate.parse(LocalDate.java:430)\r\n	at org.laokou.common.i18n.utils.DateUtil.parseDate(DateUtil.java:143)\r\n	at org.laokou.logstash.consumer.TraceConsumer.scheduleCreateIndex(TraceConsumer.java:69)\r\n	... 6 more\r\n', 2);

-- ----------------------------
-- Table structure for xxl_job_log_report
-- ----------------------------
DROP TABLE IF EXISTS `xxl_job_log_report`;
CREATE TABLE `xxl_job_log_report`  (
                                       `id` int NOT NULL AUTO_INCREMENT,
                                       `trigger_day` datetime NULL DEFAULT NULL COMMENT '调度-时间',
                                       `running_count` int NOT NULL DEFAULT 0 COMMENT '运行中-日志数量',
                                       `suc_count` int NOT NULL DEFAULT 0 COMMENT '执行成功-日志数量',
                                       `fail_count` int NOT NULL DEFAULT 0 COMMENT '执行失败-日志数量',
                                       `update_time` datetime NULL DEFAULT NULL,
                                       PRIMARY KEY (`id`) USING BTREE,
                                       UNIQUE INDEX `i_trigger_day`(`trigger_day` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 20 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of xxl_job_log_report
-- ----------------------------
INSERT INTO `xxl_job_log_report` VALUES (1, '2023-04-03 00:00:00', 4, 3, 91, NULL);
INSERT INTO `xxl_job_log_report` VALUES (2, '2023-04-02 00:00:00', 0, 0, 0, NULL);
INSERT INTO `xxl_job_log_report` VALUES (3, '2023-04-01 00:00:00', 0, 0, 0, NULL);
INSERT INTO `xxl_job_log_report` VALUES (4, '2023-04-04 00:00:00', 0, 0, 0, NULL);
INSERT INTO `xxl_job_log_report` VALUES (5, '2023-04-05 00:00:00', 0, 0, 0, NULL);
INSERT INTO `xxl_job_log_report` VALUES (6, '2023-09-17 00:00:00', 0, 0, 0, NULL);
INSERT INTO `xxl_job_log_report` VALUES (7, '2023-09-16 00:00:00', 0, 0, 0, NULL);
INSERT INTO `xxl_job_log_report` VALUES (8, '2023-09-15 00:00:00', 0, 0, 0, NULL);
INSERT INTO `xxl_job_log_report` VALUES (9, '2023-10-21 00:00:00', 0, 0, 0, NULL);
INSERT INTO `xxl_job_log_report` VALUES (10, '2023-10-20 00:00:00', 0, 0, 0, NULL);
INSERT INTO `xxl_job_log_report` VALUES (11, '2023-10-19 00:00:00', 0, 0, 0, NULL);
INSERT INTO `xxl_job_log_report` VALUES (12, '2023-11-02 00:00:00', 0, 0, 0, NULL);
INSERT INTO `xxl_job_log_report` VALUES (13, '2023-11-01 00:00:00', 0, 0, 0, NULL);
INSERT INTO `xxl_job_log_report` VALUES (14, '2023-10-31 00:00:00', 0, 0, 0, NULL);
INSERT INTO `xxl_job_log_report` VALUES (15, '2023-11-03 00:00:00', 0, 4, 2, NULL);
INSERT INTO `xxl_job_log_report` VALUES (16, '2023-11-06 00:00:00', 0, 0, 0, NULL);
INSERT INTO `xxl_job_log_report` VALUES (17, '2023-11-05 00:00:00', 0, 0, 0, NULL);
INSERT INTO `xxl_job_log_report` VALUES (18, '2023-11-04 00:00:00', 0, 0, 0, NULL);
INSERT INTO `xxl_job_log_report` VALUES (19, '2023-11-07 00:00:00', 0, 0, 0, NULL);

-- ----------------------------
-- Table structure for xxl_job_logglue
-- ----------------------------
DROP TABLE IF EXISTS `xxl_job_logglue`;
CREATE TABLE `xxl_job_logglue`  (
                                    `id` int NOT NULL AUTO_INCREMENT,
                                    `job_id` int NOT NULL COMMENT '任务，主键ID',
                                    `glue_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT 'GLUE类型',
                                    `glue_source` mediumtext CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL COMMENT 'GLUE源代码',
                                    `glue_remark` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT 'GLUE备注',
                                    `add_time` datetime NULL DEFAULT NULL,
                                    `update_time` datetime NULL DEFAULT NULL,
                                    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of xxl_job_logglue
-- ----------------------------

-- ----------------------------
-- Table structure for xxl_job_registry
-- ----------------------------
DROP TABLE IF EXISTS `xxl_job_registry`;
CREATE TABLE `xxl_job_registry`  (
                                     `id` int NOT NULL AUTO_INCREMENT,
                                     `registry_group` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
                                     `registry_key` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
                                     `registry_value` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
                                     `update_time` datetime NULL DEFAULT NULL,
                                     PRIMARY KEY (`id`) USING BTREE,
                                     INDEX `i_g_k_v`(`registry_group` ASC, `registry_key` ASC, `registry_value` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 142 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of xxl_job_registry
-- ----------------------------

-- ----------------------------
-- Table structure for xxl_job_user
-- ----------------------------
DROP TABLE IF EXISTS `xxl_job_user`;
CREATE TABLE `xxl_job_user`  (
                                 `id` int NOT NULL AUTO_INCREMENT,
                                 `username` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '账号',
                                 `password` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '密码',
                                 `role` tinyint NOT NULL COMMENT '角色：0-普通用户、1-管理员',
                                 `permission` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '权限：执行器ID列表，多个逗号分割',
                                 PRIMARY KEY (`id`) USING BTREE,
                                 UNIQUE INDEX `i_username`(`username` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of xxl_job_user
-- ----------------------------
INSERT INTO `xxl_job_user` VALUES (1, 'admin', '653ef328fde361f0f2977b7ce8bc08f6', 1, NULL);

SET FOREIGN_KEY_CHECKS = 1;
