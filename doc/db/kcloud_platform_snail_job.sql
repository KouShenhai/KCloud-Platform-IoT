/*
 Navicat Premium Dump SQL

 Source Server         : 127.0.0.1
 Source Server Type    : PostgreSQL
 Source Server Version : 160003 (160003)
 Source Host           : 127.0.0.1:5432
 Source Catalog        : kcloud_platform_snail_job
 Source Schema         : public

 Target Server Type    : PostgreSQL
 Target Server Version : 160003 (160003)
 File Encoding         : 65001

 Date: 16/10/2024 23:07:15
*/


-- ----------------------------
-- Sequence structure for sj_group_config_id_seq
-- ----------------------------
DROP SEQUENCE IF EXISTS "public"."sj_group_config_id_seq";
CREATE SEQUENCE "public"."sj_group_config_id_seq" 
INCREMENT 1
MINVALUE  1
MAXVALUE 9223372036854775807
START 1
CACHE 1;

-- ----------------------------
-- Sequence structure for sj_job_id_seq
-- ----------------------------
DROP SEQUENCE IF EXISTS "public"."sj_job_id_seq";
CREATE SEQUENCE "public"."sj_job_id_seq" 
INCREMENT 1
MINVALUE  1
MAXVALUE 9223372036854775807
START 1
CACHE 1;

-- ----------------------------
-- Sequence structure for sj_job_log_message_id_seq
-- ----------------------------
DROP SEQUENCE IF EXISTS "public"."sj_job_log_message_id_seq";
CREATE SEQUENCE "public"."sj_job_log_message_id_seq" 
INCREMENT 1
MINVALUE  1
MAXVALUE 9223372036854775807
START 1
CACHE 1;

-- ----------------------------
-- Sequence structure for sj_job_summary_id_seq
-- ----------------------------
DROP SEQUENCE IF EXISTS "public"."sj_job_summary_id_seq";
CREATE SEQUENCE "public"."sj_job_summary_id_seq" 
INCREMENT 1
MINVALUE  1
MAXVALUE 9223372036854775807
START 1
CACHE 1;

-- ----------------------------
-- Sequence structure for sj_job_task_batch_id_seq
-- ----------------------------
DROP SEQUENCE IF EXISTS "public"."sj_job_task_batch_id_seq";
CREATE SEQUENCE "public"."sj_job_task_batch_id_seq" 
INCREMENT 1
MINVALUE  1
MAXVALUE 9223372036854775807
START 1
CACHE 1;

-- ----------------------------
-- Sequence structure for sj_job_task_id_seq
-- ----------------------------
DROP SEQUENCE IF EXISTS "public"."sj_job_task_id_seq";
CREATE SEQUENCE "public"."sj_job_task_id_seq" 
INCREMENT 1
MINVALUE  1
MAXVALUE 9223372036854775807
START 1
CACHE 1;

-- ----------------------------
-- Sequence structure for sj_namespace_id_seq
-- ----------------------------
DROP SEQUENCE IF EXISTS "public"."sj_namespace_id_seq";
CREATE SEQUENCE "public"."sj_namespace_id_seq" 
INCREMENT 1
MINVALUE  1
MAXVALUE 9223372036854775807
START 1
CACHE 1;

-- ----------------------------
-- Sequence structure for sj_notify_config_id_seq
-- ----------------------------
DROP SEQUENCE IF EXISTS "public"."sj_notify_config_id_seq";
CREATE SEQUENCE "public"."sj_notify_config_id_seq" 
INCREMENT 1
MINVALUE  1
MAXVALUE 9223372036854775807
START 1
CACHE 1;

-- ----------------------------
-- Sequence structure for sj_notify_recipient_id_seq
-- ----------------------------
DROP SEQUENCE IF EXISTS "public"."sj_notify_recipient_id_seq";
CREATE SEQUENCE "public"."sj_notify_recipient_id_seq" 
INCREMENT 1
MINVALUE  1
MAXVALUE 9223372036854775807
START 1
CACHE 1;

-- ----------------------------
-- Sequence structure for sj_retry_dead_letter_0_id_seq
-- ----------------------------
DROP SEQUENCE IF EXISTS "public"."sj_retry_dead_letter_0_id_seq";
CREATE SEQUENCE "public"."sj_retry_dead_letter_0_id_seq" 
INCREMENT 1
MINVALUE  1
MAXVALUE 9223372036854775807
START 1
CACHE 1;

-- ----------------------------
-- Sequence structure for sj_retry_scene_config_id_seq
-- ----------------------------
DROP SEQUENCE IF EXISTS "public"."sj_retry_scene_config_id_seq";
CREATE SEQUENCE "public"."sj_retry_scene_config_id_seq" 
INCREMENT 1
MINVALUE  1
MAXVALUE 9223372036854775807
START 1
CACHE 1;

-- ----------------------------
-- Sequence structure for sj_retry_summary_id_seq
-- ----------------------------
DROP SEQUENCE IF EXISTS "public"."sj_retry_summary_id_seq";
CREATE SEQUENCE "public"."sj_retry_summary_id_seq" 
INCREMENT 1
MINVALUE  1
MAXVALUE 9223372036854775807
START 1
CACHE 1;

-- ----------------------------
-- Sequence structure for sj_retry_task_0_id_seq
-- ----------------------------
DROP SEQUENCE IF EXISTS "public"."sj_retry_task_0_id_seq";
CREATE SEQUENCE "public"."sj_retry_task_0_id_seq" 
INCREMENT 1
MINVALUE  1
MAXVALUE 9223372036854775807
START 1
CACHE 1;

-- ----------------------------
-- Sequence structure for sj_retry_task_log_id_seq
-- ----------------------------
DROP SEQUENCE IF EXISTS "public"."sj_retry_task_log_id_seq";
CREATE SEQUENCE "public"."sj_retry_task_log_id_seq" 
INCREMENT 1
MINVALUE  1
MAXVALUE 9223372036854775807
START 1
CACHE 1;

-- ----------------------------
-- Sequence structure for sj_retry_task_log_message_id_seq
-- ----------------------------
DROP SEQUENCE IF EXISTS "public"."sj_retry_task_log_message_id_seq";
CREATE SEQUENCE "public"."sj_retry_task_log_message_id_seq" 
INCREMENT 1
MINVALUE  1
MAXVALUE 9223372036854775807
START 1
CACHE 1;

-- ----------------------------
-- Sequence structure for sj_sequence_alloc_id_seq
-- ----------------------------
DROP SEQUENCE IF EXISTS "public"."sj_sequence_alloc_id_seq";
CREATE SEQUENCE "public"."sj_sequence_alloc_id_seq" 
INCREMENT 1
MINVALUE  1
MAXVALUE 9223372036854775807
START 1
CACHE 1;

-- ----------------------------
-- Sequence structure for sj_server_node_id_seq
-- ----------------------------
DROP SEQUENCE IF EXISTS "public"."sj_server_node_id_seq";
CREATE SEQUENCE "public"."sj_server_node_id_seq" 
INCREMENT 1
MINVALUE  1
MAXVALUE 9223372036854775807
START 1
CACHE 1;

-- ----------------------------
-- Sequence structure for sj_system_user_id_seq
-- ----------------------------
DROP SEQUENCE IF EXISTS "public"."sj_system_user_id_seq";
CREATE SEQUENCE "public"."sj_system_user_id_seq" 
INCREMENT 1
MINVALUE  1
MAXVALUE 9223372036854775807
START 1
CACHE 1;

-- ----------------------------
-- Sequence structure for sj_system_user_permission_id_seq
-- ----------------------------
DROP SEQUENCE IF EXISTS "public"."sj_system_user_permission_id_seq";
CREATE SEQUENCE "public"."sj_system_user_permission_id_seq" 
INCREMENT 1
MINVALUE  1
MAXVALUE 9223372036854775807
START 1
CACHE 1;

-- ----------------------------
-- Sequence structure for sj_workflow_id_seq
-- ----------------------------
DROP SEQUENCE IF EXISTS "public"."sj_workflow_id_seq";
CREATE SEQUENCE "public"."sj_workflow_id_seq" 
INCREMENT 1
MINVALUE  1
MAXVALUE 9223372036854775807
START 1
CACHE 1;

-- ----------------------------
-- Sequence structure for sj_workflow_node_id_seq
-- ----------------------------
DROP SEQUENCE IF EXISTS "public"."sj_workflow_node_id_seq";
CREATE SEQUENCE "public"."sj_workflow_node_id_seq" 
INCREMENT 1
MINVALUE  1
MAXVALUE 9223372036854775807
START 1
CACHE 1;

-- ----------------------------
-- Sequence structure for sj_workflow_task_batch_id_seq
-- ----------------------------
DROP SEQUENCE IF EXISTS "public"."sj_workflow_task_batch_id_seq";
CREATE SEQUENCE "public"."sj_workflow_task_batch_id_seq" 
INCREMENT 1
MINVALUE  1
MAXVALUE 9223372036854775807
START 1
CACHE 1;

-- ----------------------------
-- Table structure for databasechangelog
-- ----------------------------
DROP TABLE IF EXISTS "public"."databasechangelog";
CREATE TABLE "public"."databasechangelog" (
  "id" varchar(255) COLLATE "pg_catalog"."default" NOT NULL,
  "author" varchar(255) COLLATE "pg_catalog"."default" NOT NULL,
  "filename" varchar(255) COLLATE "pg_catalog"."default" NOT NULL,
  "dateexecuted" timestamp(6) NOT NULL,
  "orderexecuted" int4 NOT NULL,
  "exectype" varchar(10) COLLATE "pg_catalog"."default" NOT NULL,
  "md5sum" varchar(35) COLLATE "pg_catalog"."default",
  "description" varchar(255) COLLATE "pg_catalog"."default",
  "comments" varchar(255) COLLATE "pg_catalog"."default",
  "tag" varchar(255) COLLATE "pg_catalog"."default",
  "liquibase" varchar(20) COLLATE "pg_catalog"."default",
  "contexts" varchar(255) COLLATE "pg_catalog"."default",
  "labels" varchar(255) COLLATE "pg_catalog"."default",
  "deployment_id" varchar(10) COLLATE "pg_catalog"."default"
)
;

-- ----------------------------
-- Records of databasechangelog
-- ----------------------------

-- ----------------------------
-- Table structure for databasechangeloglock
-- ----------------------------
DROP TABLE IF EXISTS "public"."databasechangeloglock";
CREATE TABLE "public"."databasechangeloglock" (
  "id" int4 NOT NULL,
  "locked" bool NOT NULL,
  "lockgranted" timestamp(6),
  "lockedby" varchar(255) COLLATE "pg_catalog"."default"
)
;

-- ----------------------------
-- Records of databasechangeloglock
-- ----------------------------

-- ----------------------------
-- Table structure for sj_distributed_lock
-- ----------------------------
DROP TABLE IF EXISTS "public"."sj_distributed_lock";
CREATE TABLE "public"."sj_distributed_lock" (
  "name" varchar(64) COLLATE "pg_catalog"."default" NOT NULL,
  "lock_until" timestamp(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3),
  "locked_at" timestamp(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3),
  "locked_by" varchar(255) COLLATE "pg_catalog"."default" NOT NULL,
  "create_dt" timestamp(6) NOT NULL DEFAULT CURRENT_TIMESTAMP,
  "update_dt" timestamp(6) NOT NULL DEFAULT CURRENT_TIMESTAMP
)
;
COMMENT ON COLUMN "public"."sj_distributed_lock"."name" IS '锁名称';
COMMENT ON COLUMN "public"."sj_distributed_lock"."lock_until" IS '锁定时长';
COMMENT ON COLUMN "public"."sj_distributed_lock"."locked_at" IS '锁定时间';
COMMENT ON COLUMN "public"."sj_distributed_lock"."locked_by" IS '锁定者';
COMMENT ON COLUMN "public"."sj_distributed_lock"."create_dt" IS '创建时间';
COMMENT ON COLUMN "public"."sj_distributed_lock"."update_dt" IS '修改时间';
COMMENT ON TABLE "public"."sj_distributed_lock" IS '锁定表';

-- ----------------------------
-- Records of sj_distributed_lock
-- ----------------------------
INSERT INTO "public"."sj_distributed_lock" VALUES ('retryLogMerge', '2024-10-16 23:05:04.149', '2024-10-16 23:04:04.149', '1846567782544818176', '2024-10-16 23:04:04.148614', '2024-10-16 23:04:04.292893');
INSERT INTO "public"."sj_distributed_lock" VALUES ('clearLog', '2024-10-16 23:05:04.221', '2024-10-16 23:04:04.221', '1846567782544818176', '2024-10-16 23:04:04.22125', '2024-10-16 23:04:04.292893');
INSERT INTO "public"."sj_distributed_lock" VALUES ('jobClearLog', '2024-10-16 23:05:04.11', '2024-10-16 23:04:04.11', '1846567782544818176', '2024-10-16 23:04:04.11045', '2024-10-16 23:04:04.292893');
INSERT INTO "public"."sj_distributed_lock" VALUES ('jobLogMerge', '2024-10-16 23:05:04.08', '2024-10-16 23:04:04.08', '1846567782544818176', '2024-10-16 23:04:04.079853', '2024-10-16 23:04:04.292893');
INSERT INTO "public"."sj_distributed_lock" VALUES ('clearFinishAndMoveDeadLetterRetryTask', '2024-10-16 23:05:04.299', '2024-10-16 23:04:04.299', '1846567782544818176', '2024-10-16 23:04:04.298988', '2024-10-16 23:04:04.312888');
INSERT INTO "public"."sj_distributed_lock" VALUES ('retryTaskMoreThreshold', '2024-10-16 23:05:04.298', '2024-10-16 23:04:04.298', '1846567782544818176', '2024-10-16 23:04:04.297817', '2024-10-16 23:04:04.342963');
INSERT INTO "public"."sj_distributed_lock" VALUES ('retryErrorMoreThreshold', '2024-10-16 23:05:04.299', '2024-10-16 23:04:04.299', '1846567782544818176', '2024-10-16 23:04:04.298988', '2024-10-16 23:04:04.342963');
INSERT INTO "public"."sj_distributed_lock" VALUES ('workflowJobSummarySchedule', '2024-10-16 23:07:24.082', '2024-10-16 23:07:04.082', '1846567782544818176', '2024-10-16 23:04:04.080397', '2024-10-16 23:07:04.106003');
INSERT INTO "public"."sj_distributed_lock" VALUES ('jobSummaryDashboard', '2024-10-16 23:07:24.082', '2024-10-16 23:07:04.082', '1846567782544818176', '2024-10-16 23:04:04.081152', '2024-10-16 23:07:04.111868');
INSERT INTO "public"."sj_distributed_lock" VALUES ('retrySummaryDashboard', '2024-10-16 23:07:24.082', '2024-10-16 23:07:04.082', '1846567782544818176', '2024-10-16 23:04:04.154024', '2024-10-16 23:07:04.121735');
INSERT INTO "public"."sj_distributed_lock" VALUES ('clearOfflineNode', '2024-10-16 23:07:20.676', '2024-10-16 23:07:15.676', '1846567782544818176', '2024-10-16 23:04:03.969373', '2024-10-16 23:07:15.686064');

-- ----------------------------
-- Table structure for sj_group_config
-- ----------------------------
DROP TABLE IF EXISTS "public"."sj_group_config";
CREATE TABLE "public"."sj_group_config" (
  "id" int8 NOT NULL DEFAULT nextval('sj_group_config_id_seq'::regclass),
  "namespace_id" varchar(64) COLLATE "pg_catalog"."default" NOT NULL DEFAULT '764d604ec6fc45f68cd92514c40e9e1a'::character varying,
  "group_name" varchar(64) COLLATE "pg_catalog"."default" NOT NULL DEFAULT ''::character varying,
  "description" varchar(256) COLLATE "pg_catalog"."default" NOT NULL DEFAULT ''::character varying,
  "token" varchar(64) COLLATE "pg_catalog"."default" NOT NULL DEFAULT 'SJ_cKqBTPzCsWA3VyuCfFoccmuIEGXjr5KT'::character varying,
  "group_status" int2 NOT NULL DEFAULT 0,
  "version" int4 NOT NULL,
  "group_partition" int4 NOT NULL,
  "id_generator_mode" int2 NOT NULL DEFAULT 1,
  "init_scene" int2 NOT NULL DEFAULT 0,
  "bucket_index" int4 NOT NULL DEFAULT 0,
  "create_dt" timestamp(6) NOT NULL DEFAULT CURRENT_TIMESTAMP,
  "update_dt" timestamp(6) NOT NULL DEFAULT CURRENT_TIMESTAMP
)
;
COMMENT ON COLUMN "public"."sj_group_config"."id" IS '主键';
COMMENT ON COLUMN "public"."sj_group_config"."namespace_id" IS '命名空间id';
COMMENT ON COLUMN "public"."sj_group_config"."group_name" IS '组名称';
COMMENT ON COLUMN "public"."sj_group_config"."description" IS '组描述';
COMMENT ON COLUMN "public"."sj_group_config"."token" IS 'token';
COMMENT ON COLUMN "public"."sj_group_config"."group_status" IS '组状态 0、未启用 1、启用';
COMMENT ON COLUMN "public"."sj_group_config"."version" IS '版本号';
COMMENT ON COLUMN "public"."sj_group_config"."group_partition" IS '分区';
COMMENT ON COLUMN "public"."sj_group_config"."id_generator_mode" IS '唯一id生成模式 默认号段模式';
COMMENT ON COLUMN "public"."sj_group_config"."init_scene" IS '是否初始化场景 0:否 1:是';
COMMENT ON COLUMN "public"."sj_group_config"."bucket_index" IS 'bucket';
COMMENT ON COLUMN "public"."sj_group_config"."create_dt" IS '创建时间';
COMMENT ON COLUMN "public"."sj_group_config"."update_dt" IS '修改时间';
COMMENT ON TABLE "public"."sj_group_config" IS '组配置';

-- ----------------------------
-- Records of sj_group_config
-- ----------------------------

-- ----------------------------
-- Table structure for sj_job
-- ----------------------------
DROP TABLE IF EXISTS "public"."sj_job";
CREATE TABLE "public"."sj_job" (
  "id" int8 NOT NULL DEFAULT nextval('sj_job_id_seq'::regclass),
  "namespace_id" varchar(64) COLLATE "pg_catalog"."default" NOT NULL DEFAULT '764d604ec6fc45f68cd92514c40e9e1a'::character varying,
  "group_name" varchar(64) COLLATE "pg_catalog"."default" NOT NULL,
  "job_name" varchar(64) COLLATE "pg_catalog"."default" NOT NULL,
  "args_str" text COLLATE "pg_catalog"."default",
  "args_type" int2 NOT NULL DEFAULT 1,
  "next_trigger_at" int8 NOT NULL,
  "job_status" int2 NOT NULL DEFAULT 1,
  "task_type" int2 NOT NULL DEFAULT 1,
  "route_key" int2 NOT NULL DEFAULT 4,
  "executor_type" int2 NOT NULL DEFAULT 1,
  "executor_info" varchar(255) COLLATE "pg_catalog"."default" DEFAULT NULL::character varying,
  "trigger_type" int2 NOT NULL,
  "trigger_interval" varchar(255) COLLATE "pg_catalog"."default" NOT NULL,
  "block_strategy" int2 NOT NULL DEFAULT 1,
  "executor_timeout" int4 NOT NULL DEFAULT 0,
  "max_retry_times" int4 NOT NULL DEFAULT 0,
  "parallel_num" int4 NOT NULL DEFAULT 1,
  "retry_interval" int4 NOT NULL DEFAULT 0,
  "bucket_index" int4 NOT NULL DEFAULT 0,
  "resident" int2 NOT NULL DEFAULT 0,
  "description" varchar(256) COLLATE "pg_catalog"."default" NOT NULL DEFAULT ''::character varying,
  "ext_attrs" varchar(256) COLLATE "pg_catalog"."default" DEFAULT ''::character varying,
  "deleted" int2 NOT NULL DEFAULT 0,
  "create_dt" timestamp(6) NOT NULL DEFAULT CURRENT_TIMESTAMP,
  "update_dt" timestamp(6) NOT NULL DEFAULT CURRENT_TIMESTAMP
)
;
COMMENT ON COLUMN "public"."sj_job"."id" IS '主键';
COMMENT ON COLUMN "public"."sj_job"."namespace_id" IS '命名空间id';
COMMENT ON COLUMN "public"."sj_job"."group_name" IS '组名称';
COMMENT ON COLUMN "public"."sj_job"."job_name" IS '名称';
COMMENT ON COLUMN "public"."sj_job"."args_str" IS '执行方法参数';
COMMENT ON COLUMN "public"."sj_job"."args_type" IS '参数类型 ';
COMMENT ON COLUMN "public"."sj_job"."next_trigger_at" IS '下次触发时间';
COMMENT ON COLUMN "public"."sj_job"."job_status" IS '任务状态 0、关闭、1、开启';
COMMENT ON COLUMN "public"."sj_job"."task_type" IS '任务类型 1、集群 2、广播 3、切片';
COMMENT ON COLUMN "public"."sj_job"."route_key" IS '路由策略';
COMMENT ON COLUMN "public"."sj_job"."executor_type" IS '执行器类型';
COMMENT ON COLUMN "public"."sj_job"."executor_info" IS '执行器名称';
COMMENT ON COLUMN "public"."sj_job"."trigger_type" IS '触发类型 1.CRON 表达式 2. 固定时间';
COMMENT ON COLUMN "public"."sj_job"."trigger_interval" IS '间隔时长';
COMMENT ON COLUMN "public"."sj_job"."block_strategy" IS '阻塞策略 1、丢弃 2、覆盖 3、并行';
COMMENT ON COLUMN "public"."sj_job"."executor_timeout" IS '任务执行超时时间，单位秒';
COMMENT ON COLUMN "public"."sj_job"."max_retry_times" IS '最大重试次数';
COMMENT ON COLUMN "public"."sj_job"."parallel_num" IS '并行数';
COMMENT ON COLUMN "public"."sj_job"."retry_interval" IS '重试间隔 ( s ) ';
COMMENT ON COLUMN "public"."sj_job"."bucket_index" IS 'bucket';
COMMENT ON COLUMN "public"."sj_job"."resident" IS '是否是常驻任务';
COMMENT ON COLUMN "public"."sj_job"."description" IS '描述';
COMMENT ON COLUMN "public"."sj_job"."ext_attrs" IS '扩展字段';
COMMENT ON COLUMN "public"."sj_job"."deleted" IS '逻辑删除 1、删除';
COMMENT ON COLUMN "public"."sj_job"."create_dt" IS '创建时间';
COMMENT ON COLUMN "public"."sj_job"."update_dt" IS '修改时间';
COMMENT ON TABLE "public"."sj_job" IS '任务信息';

-- ----------------------------
-- Records of sj_job
-- ----------------------------

-- ----------------------------
-- Table structure for sj_job_log_message
-- ----------------------------
DROP TABLE IF EXISTS "public"."sj_job_log_message";
CREATE TABLE "public"."sj_job_log_message" (
  "id" int8 NOT NULL DEFAULT nextval('sj_job_log_message_id_seq'::regclass),
  "namespace_id" varchar(64) COLLATE "pg_catalog"."default" NOT NULL DEFAULT '764d604ec6fc45f68cd92514c40e9e1a'::character varying,
  "group_name" varchar(64) COLLATE "pg_catalog"."default" NOT NULL,
  "job_id" int8 NOT NULL,
  "task_batch_id" int8 NOT NULL,
  "task_id" int8 NOT NULL,
  "message" text COLLATE "pg_catalog"."default" NOT NULL,
  "log_num" int4 NOT NULL DEFAULT 1,
  "real_time" int8 NOT NULL DEFAULT 0,
  "ext_attrs" varchar(256) COLLATE "pg_catalog"."default" DEFAULT ''::character varying,
  "create_dt" timestamp(6) NOT NULL DEFAULT CURRENT_TIMESTAMP
)
;
COMMENT ON COLUMN "public"."sj_job_log_message"."id" IS '主键';
COMMENT ON COLUMN "public"."sj_job_log_message"."namespace_id" IS '命名空间id';
COMMENT ON COLUMN "public"."sj_job_log_message"."group_name" IS '组名称';
COMMENT ON COLUMN "public"."sj_job_log_message"."job_id" IS '任务信息id';
COMMENT ON COLUMN "public"."sj_job_log_message"."task_batch_id" IS '任务批次id';
COMMENT ON COLUMN "public"."sj_job_log_message"."task_id" IS '调度任务id';
COMMENT ON COLUMN "public"."sj_job_log_message"."message" IS '调度信息';
COMMENT ON COLUMN "public"."sj_job_log_message"."log_num" IS '日志数量';
COMMENT ON COLUMN "public"."sj_job_log_message"."real_time" IS '上报时间';
COMMENT ON COLUMN "public"."sj_job_log_message"."ext_attrs" IS '扩展字段';
COMMENT ON COLUMN "public"."sj_job_log_message"."create_dt" IS '创建时间';
COMMENT ON TABLE "public"."sj_job_log_message" IS '调度日志';

-- ----------------------------
-- Records of sj_job_log_message
-- ----------------------------

-- ----------------------------
-- Table structure for sj_job_summary
-- ----------------------------
DROP TABLE IF EXISTS "public"."sj_job_summary";
CREATE TABLE "public"."sj_job_summary" (
  "id" int8 NOT NULL DEFAULT nextval('sj_job_summary_id_seq'::regclass),
  "namespace_id" varchar(64) COLLATE "pg_catalog"."default" NOT NULL DEFAULT '764d604ec6fc45f68cd92514c40e9e1a'::character varying,
  "group_name" varchar(64) COLLATE "pg_catalog"."default" NOT NULL DEFAULT ''::character varying,
  "business_id" int8 NOT NULL,
  "system_task_type" int2 NOT NULL DEFAULT 3,
  "trigger_at" timestamp(6) NOT NULL DEFAULT CURRENT_TIMESTAMP,
  "success_num" int4 NOT NULL DEFAULT 0,
  "fail_num" int4 NOT NULL DEFAULT 0,
  "fail_reason" varchar(512) COLLATE "pg_catalog"."default" NOT NULL DEFAULT ''::character varying,
  "stop_num" int4 NOT NULL DEFAULT 0,
  "stop_reason" varchar(512) COLLATE "pg_catalog"."default" NOT NULL DEFAULT ''::character varying,
  "cancel_num" int4 NOT NULL DEFAULT 0,
  "cancel_reason" varchar(512) COLLATE "pg_catalog"."default" NOT NULL DEFAULT ''::character varying,
  "create_dt" timestamp(6) NOT NULL DEFAULT CURRENT_TIMESTAMP,
  "update_dt" timestamp(6) NOT NULL DEFAULT CURRENT_TIMESTAMP
)
;
COMMENT ON COLUMN "public"."sj_job_summary"."id" IS '主键';
COMMENT ON COLUMN "public"."sj_job_summary"."namespace_id" IS '命名空间id';
COMMENT ON COLUMN "public"."sj_job_summary"."group_name" IS '组名称';
COMMENT ON COLUMN "public"."sj_job_summary"."business_id" IS '业务id  ( job_id或workflow_id ) ';
COMMENT ON COLUMN "public"."sj_job_summary"."system_task_type" IS '任务类型 3、JOB任务 4、WORKFLOW任务';
COMMENT ON COLUMN "public"."sj_job_summary"."trigger_at" IS '统计时间';
COMMENT ON COLUMN "public"."sj_job_summary"."success_num" IS '执行成功-日志数量';
COMMENT ON COLUMN "public"."sj_job_summary"."fail_num" IS '执行失败-日志数量';
COMMENT ON COLUMN "public"."sj_job_summary"."fail_reason" IS '失败原因';
COMMENT ON COLUMN "public"."sj_job_summary"."stop_num" IS '执行失败-日志数量';
COMMENT ON COLUMN "public"."sj_job_summary"."stop_reason" IS '失败原因';
COMMENT ON COLUMN "public"."sj_job_summary"."cancel_num" IS '执行失败-日志数量';
COMMENT ON COLUMN "public"."sj_job_summary"."cancel_reason" IS '失败原因';
COMMENT ON COLUMN "public"."sj_job_summary"."create_dt" IS '创建时间';
COMMENT ON COLUMN "public"."sj_job_summary"."update_dt" IS '修改时间';
COMMENT ON TABLE "public"."sj_job_summary" IS 'DashBoard_Job';

-- ----------------------------
-- Records of sj_job_summary
-- ----------------------------

-- ----------------------------
-- Table structure for sj_job_task
-- ----------------------------
DROP TABLE IF EXISTS "public"."sj_job_task";
CREATE TABLE "public"."sj_job_task" (
  "id" int8 NOT NULL DEFAULT nextval('sj_job_task_id_seq'::regclass),
  "namespace_id" varchar(64) COLLATE "pg_catalog"."default" NOT NULL DEFAULT '764d604ec6fc45f68cd92514c40e9e1a'::character varying,
  "group_name" varchar(64) COLLATE "pg_catalog"."default" NOT NULL,
  "job_id" int8 NOT NULL,
  "task_batch_id" int8 NOT NULL,
  "parent_id" int8 NOT NULL DEFAULT 0,
  "task_status" int2 NOT NULL DEFAULT 0,
  "retry_count" int4 NOT NULL DEFAULT 0,
  "mr_stage" int2,
  "leaf" int2 NOT NULL DEFAULT '1'::smallint,
  "task_name" varchar(255) COLLATE "pg_catalog"."default" NOT NULL DEFAULT ''::character varying,
  "client_info" varchar(128) COLLATE "pg_catalog"."default" DEFAULT NULL::character varying,
  "wf_context" text COLLATE "pg_catalog"."default",
  "result_message" text COLLATE "pg_catalog"."default" NOT NULL,
  "args_str" text COLLATE "pg_catalog"."default",
  "args_type" int2 NOT NULL DEFAULT 1,
  "ext_attrs" varchar(256) COLLATE "pg_catalog"."default" DEFAULT ''::character varying,
  "create_dt" timestamp(6) NOT NULL DEFAULT CURRENT_TIMESTAMP,
  "update_dt" timestamp(6) NOT NULL DEFAULT CURRENT_TIMESTAMP
)
;
COMMENT ON COLUMN "public"."sj_job_task"."id" IS '主键';
COMMENT ON COLUMN "public"."sj_job_task"."namespace_id" IS '命名空间id';
COMMENT ON COLUMN "public"."sj_job_task"."group_name" IS '组名称';
COMMENT ON COLUMN "public"."sj_job_task"."job_id" IS '任务信息id';
COMMENT ON COLUMN "public"."sj_job_task"."task_batch_id" IS '调度任务id';
COMMENT ON COLUMN "public"."sj_job_task"."parent_id" IS '父执行器id';
COMMENT ON COLUMN "public"."sj_job_task"."task_status" IS '执行的状态 0、失败 1、成功';
COMMENT ON COLUMN "public"."sj_job_task"."retry_count" IS '重试次数';
COMMENT ON COLUMN "public"."sj_job_task"."mr_stage" IS '动态分片所处阶段 1:map 2:reduce 3:mergeReduce';
COMMENT ON COLUMN "public"."sj_job_task"."leaf" IS '叶子节点';
COMMENT ON COLUMN "public"."sj_job_task"."task_name" IS '任务名称';
COMMENT ON COLUMN "public"."sj_job_task"."client_info" IS '客户端地址 clientId#ip:port';
COMMENT ON COLUMN "public"."sj_job_task"."wf_context" IS '工作流全局上下文';
COMMENT ON COLUMN "public"."sj_job_task"."result_message" IS '执行结果';
COMMENT ON COLUMN "public"."sj_job_task"."args_str" IS '执行方法参数';
COMMENT ON COLUMN "public"."sj_job_task"."args_type" IS '参数类型 ';
COMMENT ON COLUMN "public"."sj_job_task"."ext_attrs" IS '扩展字段';
COMMENT ON COLUMN "public"."sj_job_task"."create_dt" IS '创建时间';
COMMENT ON COLUMN "public"."sj_job_task"."update_dt" IS '修改时间';
COMMENT ON TABLE "public"."sj_job_task" IS '任务实例';

-- ----------------------------
-- Records of sj_job_task
-- ----------------------------

-- ----------------------------
-- Table structure for sj_job_task_batch
-- ----------------------------
DROP TABLE IF EXISTS "public"."sj_job_task_batch";
CREATE TABLE "public"."sj_job_task_batch" (
  "id" int8 NOT NULL DEFAULT nextval('sj_job_task_batch_id_seq'::regclass),
  "namespace_id" varchar(64) COLLATE "pg_catalog"."default" NOT NULL DEFAULT '764d604ec6fc45f68cd92514c40e9e1a'::character varying,
  "group_name" varchar(64) COLLATE "pg_catalog"."default" NOT NULL,
  "job_id" int8 NOT NULL,
  "workflow_node_id" int8 NOT NULL DEFAULT 0,
  "parent_workflow_node_id" int8 NOT NULL DEFAULT 0,
  "workflow_task_batch_id" int8 NOT NULL DEFAULT 0,
  "task_batch_status" int2 NOT NULL DEFAULT 0,
  "operation_reason" int2 NOT NULL DEFAULT 0,
  "execution_at" int8 NOT NULL DEFAULT 0,
  "system_task_type" int2 NOT NULL DEFAULT 3,
  "parent_id" varchar(64) COLLATE "pg_catalog"."default" NOT NULL DEFAULT ''::character varying,
  "ext_attrs" varchar(256) COLLATE "pg_catalog"."default" DEFAULT ''::character varying,
  "deleted" int2 NOT NULL DEFAULT 0,
  "create_dt" timestamp(6) NOT NULL DEFAULT CURRENT_TIMESTAMP,
  "update_dt" timestamp(6) NOT NULL DEFAULT CURRENT_TIMESTAMP
)
;
COMMENT ON COLUMN "public"."sj_job_task_batch"."id" IS '主键';
COMMENT ON COLUMN "public"."sj_job_task_batch"."namespace_id" IS '命名空间id';
COMMENT ON COLUMN "public"."sj_job_task_batch"."group_name" IS '组名称';
COMMENT ON COLUMN "public"."sj_job_task_batch"."job_id" IS '任务id';
COMMENT ON COLUMN "public"."sj_job_task_batch"."workflow_node_id" IS '工作流节点id';
COMMENT ON COLUMN "public"."sj_job_task_batch"."parent_workflow_node_id" IS '工作流任务父批次id';
COMMENT ON COLUMN "public"."sj_job_task_batch"."workflow_task_batch_id" IS '工作流任务批次id';
COMMENT ON COLUMN "public"."sj_job_task_batch"."task_batch_status" IS '任务批次状态 0、失败 1、成功';
COMMENT ON COLUMN "public"."sj_job_task_batch"."operation_reason" IS '操作原因';
COMMENT ON COLUMN "public"."sj_job_task_batch"."execution_at" IS '任务执行时间';
COMMENT ON COLUMN "public"."sj_job_task_batch"."system_task_type" IS '任务类型 3、JOB任务 4、WORKFLOW任务';
COMMENT ON COLUMN "public"."sj_job_task_batch"."parent_id" IS '父节点';
COMMENT ON COLUMN "public"."sj_job_task_batch"."ext_attrs" IS '扩展字段';
COMMENT ON COLUMN "public"."sj_job_task_batch"."deleted" IS '逻辑删除 1、删除';
COMMENT ON COLUMN "public"."sj_job_task_batch"."create_dt" IS '创建时间';
COMMENT ON COLUMN "public"."sj_job_task_batch"."update_dt" IS '修改时间';
COMMENT ON TABLE "public"."sj_job_task_batch" IS '任务批次';

-- ----------------------------
-- Records of sj_job_task_batch
-- ----------------------------

-- ----------------------------
-- Table structure for sj_namespace
-- ----------------------------
DROP TABLE IF EXISTS "public"."sj_namespace";
CREATE TABLE "public"."sj_namespace" (
  "id" int8 NOT NULL DEFAULT nextval('sj_namespace_id_seq'::regclass),
  "name" varchar(64) COLLATE "pg_catalog"."default" NOT NULL,
  "unique_id" varchar(64) COLLATE "pg_catalog"."default" NOT NULL,
  "description" varchar(256) COLLATE "pg_catalog"."default" NOT NULL DEFAULT ''::character varying,
  "deleted" int2 NOT NULL DEFAULT 0,
  "create_dt" timestamp(6) NOT NULL DEFAULT CURRENT_TIMESTAMP,
  "update_dt" timestamp(6) NOT NULL DEFAULT CURRENT_TIMESTAMP
)
;
COMMENT ON COLUMN "public"."sj_namespace"."id" IS '主键';
COMMENT ON COLUMN "public"."sj_namespace"."name" IS '名称';
COMMENT ON COLUMN "public"."sj_namespace"."unique_id" IS '唯一id';
COMMENT ON COLUMN "public"."sj_namespace"."description" IS '描述';
COMMENT ON COLUMN "public"."sj_namespace"."deleted" IS '逻辑删除 1、删除';
COMMENT ON COLUMN "public"."sj_namespace"."create_dt" IS '创建时间';
COMMENT ON COLUMN "public"."sj_namespace"."update_dt" IS '修改时间';
COMMENT ON TABLE "public"."sj_namespace" IS '命名空间';

-- ----------------------------
-- Records of sj_namespace
-- ----------------------------
INSERT INTO "public"."sj_namespace" VALUES (1, 'laokou', '764d604ec6fc45f68cd92514c40e9e1a', '', 0, '2024-10-16 23:05:54.73973', '2024-10-16 23:05:54.73973');

-- ----------------------------
-- Table structure for sj_notify_config
-- ----------------------------
DROP TABLE IF EXISTS "public"."sj_notify_config";
CREATE TABLE "public"."sj_notify_config" (
  "id" int8 NOT NULL DEFAULT nextval('sj_notify_config_id_seq'::regclass),
  "namespace_id" varchar(64) COLLATE "pg_catalog"."default" NOT NULL DEFAULT '764d604ec6fc45f68cd92514c40e9e1a'::character varying,
  "group_name" varchar(64) COLLATE "pg_catalog"."default" NOT NULL,
  "business_id" varchar(64) COLLATE "pg_catalog"."default" NOT NULL,
  "system_task_type" int2 NOT NULL DEFAULT 3,
  "notify_status" int2 NOT NULL DEFAULT 0,
  "recipient_ids" varchar(128) COLLATE "pg_catalog"."default" NOT NULL,
  "notify_threshold" int4 NOT NULL DEFAULT 0,
  "notify_scene" int2 NOT NULL DEFAULT 0,
  "rate_limiter_status" int2 NOT NULL DEFAULT 0,
  "rate_limiter_threshold" int4 NOT NULL DEFAULT 0,
  "description" varchar(256) COLLATE "pg_catalog"."default" NOT NULL DEFAULT ''::character varying,
  "create_dt" timestamp(6) NOT NULL DEFAULT CURRENT_TIMESTAMP,
  "update_dt" timestamp(6) NOT NULL DEFAULT CURRENT_TIMESTAMP
)
;
COMMENT ON COLUMN "public"."sj_notify_config"."id" IS '主键';
COMMENT ON COLUMN "public"."sj_notify_config"."namespace_id" IS '命名空间id';
COMMENT ON COLUMN "public"."sj_notify_config"."group_name" IS '组名称';
COMMENT ON COLUMN "public"."sj_notify_config"."business_id" IS '业务id  ( job_id或workflow_id或scene_name ) ';
COMMENT ON COLUMN "public"."sj_notify_config"."system_task_type" IS '任务类型 1. 重试任务 2. 重试回调 3、JOB任务 4、WORKFLOW任务';
COMMENT ON COLUMN "public"."sj_notify_config"."notify_status" IS '通知状态 0、未启用 1、启用';
COMMENT ON COLUMN "public"."sj_notify_config"."recipient_ids" IS '接收人id列表';
COMMENT ON COLUMN "public"."sj_notify_config"."notify_threshold" IS '通知阈值';
COMMENT ON COLUMN "public"."sj_notify_config"."notify_scene" IS '通知场景';
COMMENT ON COLUMN "public"."sj_notify_config"."rate_limiter_status" IS '限流状态 0、未启用 1、启用';
COMMENT ON COLUMN "public"."sj_notify_config"."rate_limiter_threshold" IS '每秒限流阈值';
COMMENT ON COLUMN "public"."sj_notify_config"."description" IS '描述';
COMMENT ON COLUMN "public"."sj_notify_config"."create_dt" IS '创建时间';
COMMENT ON COLUMN "public"."sj_notify_config"."update_dt" IS '修改时间';
COMMENT ON TABLE "public"."sj_notify_config" IS '通知配置';

-- ----------------------------
-- Records of sj_notify_config
-- ----------------------------

-- ----------------------------
-- Table structure for sj_notify_recipient
-- ----------------------------
DROP TABLE IF EXISTS "public"."sj_notify_recipient";
CREATE TABLE "public"."sj_notify_recipient" (
  "id" int8 NOT NULL DEFAULT nextval('sj_notify_recipient_id_seq'::regclass),
  "namespace_id" varchar(64) COLLATE "pg_catalog"."default" NOT NULL DEFAULT '764d604ec6fc45f68cd92514c40e9e1a'::character varying,
  "recipient_name" varchar(64) COLLATE "pg_catalog"."default" NOT NULL,
  "notify_type" int2 NOT NULL DEFAULT 0,
  "notify_attribute" varchar(512) COLLATE "pg_catalog"."default" NOT NULL,
  "description" varchar(256) COLLATE "pg_catalog"."default" NOT NULL DEFAULT ''::character varying,
  "create_dt" timestamp(6) NOT NULL DEFAULT CURRENT_TIMESTAMP,
  "update_dt" timestamp(6) NOT NULL DEFAULT CURRENT_TIMESTAMP
)
;
COMMENT ON COLUMN "public"."sj_notify_recipient"."id" IS '主键';
COMMENT ON COLUMN "public"."sj_notify_recipient"."namespace_id" IS '命名空间id';
COMMENT ON COLUMN "public"."sj_notify_recipient"."recipient_name" IS '接收人名称';
COMMENT ON COLUMN "public"."sj_notify_recipient"."notify_type" IS '通知类型 1、钉钉 2、邮件 3、企业微信 4 飞书 5 webhook';
COMMENT ON COLUMN "public"."sj_notify_recipient"."notify_attribute" IS '配置属性';
COMMENT ON COLUMN "public"."sj_notify_recipient"."description" IS '描述';
COMMENT ON COLUMN "public"."sj_notify_recipient"."create_dt" IS '创建时间';
COMMENT ON COLUMN "public"."sj_notify_recipient"."update_dt" IS '修改时间';
COMMENT ON TABLE "public"."sj_notify_recipient" IS '告警通知接收人';

-- ----------------------------
-- Records of sj_notify_recipient
-- ----------------------------

-- ----------------------------
-- Table structure for sj_retry_dead_letter_0
-- ----------------------------
DROP TABLE IF EXISTS "public"."sj_retry_dead_letter_0";
CREATE TABLE "public"."sj_retry_dead_letter_0" (
  "id" int8 NOT NULL DEFAULT nextval('sj_retry_dead_letter_0_id_seq'::regclass),
  "namespace_id" varchar(64) COLLATE "pg_catalog"."default" NOT NULL DEFAULT '764d604ec6fc45f68cd92514c40e9e1a'::character varying,
  "unique_id" varchar(64) COLLATE "pg_catalog"."default" NOT NULL,
  "group_name" varchar(64) COLLATE "pg_catalog"."default" NOT NULL,
  "scene_name" varchar(64) COLLATE "pg_catalog"."default" NOT NULL,
  "idempotent_id" varchar(64) COLLATE "pg_catalog"."default" NOT NULL,
  "biz_no" varchar(64) COLLATE "pg_catalog"."default" NOT NULL DEFAULT ''::character varying,
  "executor_name" varchar(512) COLLATE "pg_catalog"."default" NOT NULL DEFAULT ''::character varying,
  "args_str" text COLLATE "pg_catalog"."default" NOT NULL,
  "ext_attrs" text COLLATE "pg_catalog"."default" NOT NULL,
  "task_type" int2 NOT NULL DEFAULT 1,
  "create_dt" timestamp(6) NOT NULL DEFAULT CURRENT_TIMESTAMP
)
;
COMMENT ON COLUMN "public"."sj_retry_dead_letter_0"."id" IS '主键';
COMMENT ON COLUMN "public"."sj_retry_dead_letter_0"."namespace_id" IS '命名空间id';
COMMENT ON COLUMN "public"."sj_retry_dead_letter_0"."unique_id" IS '同组下id唯一';
COMMENT ON COLUMN "public"."sj_retry_dead_letter_0"."group_name" IS '组名称';
COMMENT ON COLUMN "public"."sj_retry_dead_letter_0"."scene_name" IS '场景名称';
COMMENT ON COLUMN "public"."sj_retry_dead_letter_0"."idempotent_id" IS '幂等id';
COMMENT ON COLUMN "public"."sj_retry_dead_letter_0"."biz_no" IS '业务编号';
COMMENT ON COLUMN "public"."sj_retry_dead_letter_0"."executor_name" IS '执行器名称';
COMMENT ON COLUMN "public"."sj_retry_dead_letter_0"."args_str" IS '执行方法参数';
COMMENT ON COLUMN "public"."sj_retry_dead_letter_0"."ext_attrs" IS '扩展字段';
COMMENT ON COLUMN "public"."sj_retry_dead_letter_0"."task_type" IS '任务类型 1、重试数据 2、回调数据';
COMMENT ON COLUMN "public"."sj_retry_dead_letter_0"."create_dt" IS '创建时间';
COMMENT ON TABLE "public"."sj_retry_dead_letter_0" IS '死信队列表';

-- ----------------------------
-- Records of sj_retry_dead_letter_0
-- ----------------------------

-- ----------------------------
-- Table structure for sj_retry_scene_config
-- ----------------------------
DROP TABLE IF EXISTS "public"."sj_retry_scene_config";
CREATE TABLE "public"."sj_retry_scene_config" (
  "id" int8 NOT NULL DEFAULT nextval('sj_retry_scene_config_id_seq'::regclass),
  "namespace_id" varchar(64) COLLATE "pg_catalog"."default" NOT NULL DEFAULT '764d604ec6fc45f68cd92514c40e9e1a'::character varying,
  "scene_name" varchar(64) COLLATE "pg_catalog"."default" NOT NULL,
  "group_name" varchar(64) COLLATE "pg_catalog"."default" NOT NULL,
  "scene_status" int2 NOT NULL DEFAULT 0,
  "max_retry_count" int4 NOT NULL DEFAULT 5,
  "back_off" int2 NOT NULL DEFAULT 1,
  "trigger_interval" varchar(16) COLLATE "pg_catalog"."default" NOT NULL DEFAULT ''::character varying,
  "deadline_request" int8 NOT NULL DEFAULT 60000,
  "executor_timeout" int4 NOT NULL DEFAULT 5,
  "route_key" int2 NOT NULL DEFAULT 4,
  "description" varchar(256) COLLATE "pg_catalog"."default" NOT NULL DEFAULT ''::character varying,
  "create_dt" timestamp(6) NOT NULL DEFAULT CURRENT_TIMESTAMP,
  "update_dt" timestamp(6) NOT NULL DEFAULT CURRENT_TIMESTAMP
)
;
COMMENT ON COLUMN "public"."sj_retry_scene_config"."id" IS '主键';
COMMENT ON COLUMN "public"."sj_retry_scene_config"."namespace_id" IS '命名空间id';
COMMENT ON COLUMN "public"."sj_retry_scene_config"."scene_name" IS '场景名称';
COMMENT ON COLUMN "public"."sj_retry_scene_config"."group_name" IS '组名称';
COMMENT ON COLUMN "public"."sj_retry_scene_config"."scene_status" IS '组状态 0、未启用 1、启用';
COMMENT ON COLUMN "public"."sj_retry_scene_config"."max_retry_count" IS '最大重试次数';
COMMENT ON COLUMN "public"."sj_retry_scene_config"."back_off" IS '1、默认等级 2、固定间隔时间 3、CRON 表达式';
COMMENT ON COLUMN "public"."sj_retry_scene_config"."trigger_interval" IS '间隔时长';
COMMENT ON COLUMN "public"."sj_retry_scene_config"."deadline_request" IS 'Deadline Request 调用链超时 单位毫秒';
COMMENT ON COLUMN "public"."sj_retry_scene_config"."executor_timeout" IS '任务执行超时时间，单位秒';
COMMENT ON COLUMN "public"."sj_retry_scene_config"."route_key" IS '路由策略';
COMMENT ON COLUMN "public"."sj_retry_scene_config"."description" IS '描述';
COMMENT ON COLUMN "public"."sj_retry_scene_config"."create_dt" IS '创建时间';
COMMENT ON COLUMN "public"."sj_retry_scene_config"."update_dt" IS '修改时间';
COMMENT ON TABLE "public"."sj_retry_scene_config" IS '场景配置';

-- ----------------------------
-- Records of sj_retry_scene_config
-- ----------------------------

-- ----------------------------
-- Table structure for sj_retry_summary
-- ----------------------------
DROP TABLE IF EXISTS "public"."sj_retry_summary";
CREATE TABLE "public"."sj_retry_summary" (
  "id" int8 NOT NULL DEFAULT nextval('sj_retry_summary_id_seq'::regclass),
  "namespace_id" varchar(64) COLLATE "pg_catalog"."default" NOT NULL DEFAULT '764d604ec6fc45f68cd92514c40e9e1a'::character varying,
  "group_name" varchar(64) COLLATE "pg_catalog"."default" NOT NULL DEFAULT ''::character varying,
  "scene_name" varchar(50) COLLATE "pg_catalog"."default" NOT NULL DEFAULT ''::character varying,
  "trigger_at" timestamp(6) NOT NULL DEFAULT CURRENT_TIMESTAMP,
  "running_num" int4 NOT NULL DEFAULT 0,
  "finish_num" int4 NOT NULL DEFAULT 0,
  "max_count_num" int4 NOT NULL DEFAULT 0,
  "suspend_num" int4 NOT NULL DEFAULT 0,
  "create_dt" timestamp(6) NOT NULL DEFAULT CURRENT_TIMESTAMP,
  "update_dt" timestamp(6) NOT NULL DEFAULT CURRENT_TIMESTAMP
)
;
COMMENT ON COLUMN "public"."sj_retry_summary"."id" IS '主键';
COMMENT ON COLUMN "public"."sj_retry_summary"."namespace_id" IS '命名空间id';
COMMENT ON COLUMN "public"."sj_retry_summary"."group_name" IS '组名称';
COMMENT ON COLUMN "public"."sj_retry_summary"."scene_name" IS '场景名称';
COMMENT ON COLUMN "public"."sj_retry_summary"."trigger_at" IS '统计时间';
COMMENT ON COLUMN "public"."sj_retry_summary"."running_num" IS '重试中-日志数量';
COMMENT ON COLUMN "public"."sj_retry_summary"."finish_num" IS '重试完成-日志数量';
COMMENT ON COLUMN "public"."sj_retry_summary"."max_count_num" IS '重试到达最大次数-日志数量';
COMMENT ON COLUMN "public"."sj_retry_summary"."suspend_num" IS '暂停重试-日志数量';
COMMENT ON COLUMN "public"."sj_retry_summary"."create_dt" IS '创建时间';
COMMENT ON COLUMN "public"."sj_retry_summary"."update_dt" IS '修改时间';
COMMENT ON TABLE "public"."sj_retry_summary" IS 'DashBoard_Retry';

-- ----------------------------
-- Records of sj_retry_summary
-- ----------------------------

-- ----------------------------
-- Table structure for sj_retry_task_0
-- ----------------------------
DROP TABLE IF EXISTS "public"."sj_retry_task_0";
CREATE TABLE "public"."sj_retry_task_0" (
  "id" int8 NOT NULL DEFAULT nextval('sj_retry_task_0_id_seq'::regclass),
  "namespace_id" varchar(64) COLLATE "pg_catalog"."default" NOT NULL DEFAULT '764d604ec6fc45f68cd92514c40e9e1a'::character varying,
  "unique_id" varchar(64) COLLATE "pg_catalog"."default" NOT NULL,
  "group_name" varchar(64) COLLATE "pg_catalog"."default" NOT NULL,
  "scene_name" varchar(64) COLLATE "pg_catalog"."default" NOT NULL,
  "idempotent_id" varchar(64) COLLATE "pg_catalog"."default" NOT NULL,
  "biz_no" varchar(64) COLLATE "pg_catalog"."default" NOT NULL DEFAULT ''::character varying,
  "executor_name" varchar(512) COLLATE "pg_catalog"."default" NOT NULL DEFAULT ''::character varying,
  "args_str" text COLLATE "pg_catalog"."default" NOT NULL,
  "ext_attrs" text COLLATE "pg_catalog"."default" NOT NULL,
  "next_trigger_at" timestamp(6) NOT NULL,
  "retry_count" int4 NOT NULL DEFAULT 0,
  "retry_status" int2 NOT NULL DEFAULT 0,
  "task_type" int2 NOT NULL DEFAULT 1,
  "create_dt" timestamp(6) NOT NULL DEFAULT CURRENT_TIMESTAMP,
  "update_dt" timestamp(6) NOT NULL DEFAULT CURRENT_TIMESTAMP
)
;
COMMENT ON COLUMN "public"."sj_retry_task_0"."id" IS '主键';
COMMENT ON COLUMN "public"."sj_retry_task_0"."namespace_id" IS '命名空间id';
COMMENT ON COLUMN "public"."sj_retry_task_0"."unique_id" IS '同组下id唯一';
COMMENT ON COLUMN "public"."sj_retry_task_0"."group_name" IS '组名称';
COMMENT ON COLUMN "public"."sj_retry_task_0"."scene_name" IS '场景名称';
COMMENT ON COLUMN "public"."sj_retry_task_0"."idempotent_id" IS '幂等id';
COMMENT ON COLUMN "public"."sj_retry_task_0"."biz_no" IS '业务编号';
COMMENT ON COLUMN "public"."sj_retry_task_0"."executor_name" IS '执行器名称';
COMMENT ON COLUMN "public"."sj_retry_task_0"."args_str" IS '执行方法参数';
COMMENT ON COLUMN "public"."sj_retry_task_0"."ext_attrs" IS '扩展字段';
COMMENT ON COLUMN "public"."sj_retry_task_0"."next_trigger_at" IS '下次触发时间';
COMMENT ON COLUMN "public"."sj_retry_task_0"."retry_count" IS '重试次数';
COMMENT ON COLUMN "public"."sj_retry_task_0"."retry_status" IS '重试状态 0、重试中 1、成功 2、最大重试次数';
COMMENT ON COLUMN "public"."sj_retry_task_0"."task_type" IS '任务类型 1、重试数据 2、回调数据';
COMMENT ON COLUMN "public"."sj_retry_task_0"."create_dt" IS '创建时间';
COMMENT ON COLUMN "public"."sj_retry_task_0"."update_dt" IS '修改时间';
COMMENT ON TABLE "public"."sj_retry_task_0" IS '任务表';

-- ----------------------------
-- Records of sj_retry_task_0
-- ----------------------------

-- ----------------------------
-- Table structure for sj_retry_task_log
-- ----------------------------
DROP TABLE IF EXISTS "public"."sj_retry_task_log";
CREATE TABLE "public"."sj_retry_task_log" (
  "id" int8 NOT NULL DEFAULT nextval('sj_retry_task_log_id_seq'::regclass),
  "namespace_id" varchar(64) COLLATE "pg_catalog"."default" NOT NULL DEFAULT '764d604ec6fc45f68cd92514c40e9e1a'::character varying,
  "unique_id" varchar(64) COLLATE "pg_catalog"."default" NOT NULL,
  "group_name" varchar(64) COLLATE "pg_catalog"."default" NOT NULL,
  "scene_name" varchar(64) COLLATE "pg_catalog"."default" NOT NULL,
  "idempotent_id" varchar(64) COLLATE "pg_catalog"."default" NOT NULL,
  "biz_no" varchar(64) COLLATE "pg_catalog"."default" NOT NULL DEFAULT ''::character varying,
  "executor_name" varchar(512) COLLATE "pg_catalog"."default" NOT NULL DEFAULT ''::character varying,
  "args_str" text COLLATE "pg_catalog"."default" NOT NULL,
  "ext_attrs" text COLLATE "pg_catalog"."default" NOT NULL,
  "retry_status" int2 NOT NULL DEFAULT 0,
  "task_type" int2 NOT NULL DEFAULT 1,
  "create_dt" timestamp(6) NOT NULL DEFAULT CURRENT_TIMESTAMP,
  "update_dt" timestamp(6) NOT NULL DEFAULT CURRENT_TIMESTAMP
)
;
COMMENT ON COLUMN "public"."sj_retry_task_log"."id" IS '主键';
COMMENT ON COLUMN "public"."sj_retry_task_log"."namespace_id" IS '命名空间id';
COMMENT ON COLUMN "public"."sj_retry_task_log"."unique_id" IS '同组下id唯一';
COMMENT ON COLUMN "public"."sj_retry_task_log"."group_name" IS '组名称';
COMMENT ON COLUMN "public"."sj_retry_task_log"."scene_name" IS '场景名称';
COMMENT ON COLUMN "public"."sj_retry_task_log"."idempotent_id" IS '幂等id';
COMMENT ON COLUMN "public"."sj_retry_task_log"."biz_no" IS '业务编号';
COMMENT ON COLUMN "public"."sj_retry_task_log"."executor_name" IS '执行器名称';
COMMENT ON COLUMN "public"."sj_retry_task_log"."args_str" IS '执行方法参数';
COMMENT ON COLUMN "public"."sj_retry_task_log"."ext_attrs" IS '扩展字段';
COMMENT ON COLUMN "public"."sj_retry_task_log"."retry_status" IS '重试状态 0、重试中 1、成功 2、最大次数';
COMMENT ON COLUMN "public"."sj_retry_task_log"."task_type" IS '任务类型 1、重试数据 2、回调数据';
COMMENT ON COLUMN "public"."sj_retry_task_log"."create_dt" IS '创建时间';
COMMENT ON COLUMN "public"."sj_retry_task_log"."update_dt" IS '修改时间';
COMMENT ON TABLE "public"."sj_retry_task_log" IS '任务日志基础信息表';

-- ----------------------------
-- Records of sj_retry_task_log
-- ----------------------------

-- ----------------------------
-- Table structure for sj_retry_task_log_message
-- ----------------------------
DROP TABLE IF EXISTS "public"."sj_retry_task_log_message";
CREATE TABLE "public"."sj_retry_task_log_message" (
  "id" int8 NOT NULL DEFAULT nextval('sj_retry_task_log_message_id_seq'::regclass),
  "namespace_id" varchar(64) COLLATE "pg_catalog"."default" NOT NULL DEFAULT '764d604ec6fc45f68cd92514c40e9e1a'::character varying,
  "group_name" varchar(64) COLLATE "pg_catalog"."default" NOT NULL,
  "unique_id" varchar(64) COLLATE "pg_catalog"."default" NOT NULL,
  "message" text COLLATE "pg_catalog"."default" NOT NULL,
  "log_num" int4 NOT NULL DEFAULT 1,
  "real_time" int8 NOT NULL DEFAULT 0,
  "create_dt" timestamp(6) NOT NULL DEFAULT CURRENT_TIMESTAMP
)
;
COMMENT ON COLUMN "public"."sj_retry_task_log_message"."id" IS '主键';
COMMENT ON COLUMN "public"."sj_retry_task_log_message"."namespace_id" IS '命名空间id';
COMMENT ON COLUMN "public"."sj_retry_task_log_message"."group_name" IS '组名称';
COMMENT ON COLUMN "public"."sj_retry_task_log_message"."unique_id" IS '同组下id唯一';
COMMENT ON COLUMN "public"."sj_retry_task_log_message"."message" IS '异常信息';
COMMENT ON COLUMN "public"."sj_retry_task_log_message"."log_num" IS '日志数量';
COMMENT ON COLUMN "public"."sj_retry_task_log_message"."real_time" IS '上报时间';
COMMENT ON COLUMN "public"."sj_retry_task_log_message"."create_dt" IS '创建时间';
COMMENT ON TABLE "public"."sj_retry_task_log_message" IS '任务调度日志信息记录表';

-- ----------------------------
-- Records of sj_retry_task_log_message
-- ----------------------------

-- ----------------------------
-- Table structure for sj_sequence_alloc
-- ----------------------------
DROP TABLE IF EXISTS "public"."sj_sequence_alloc";
CREATE TABLE "public"."sj_sequence_alloc" (
  "id" int8 NOT NULL DEFAULT nextval('sj_sequence_alloc_id_seq'::regclass),
  "namespace_id" varchar(64) COLLATE "pg_catalog"."default" NOT NULL DEFAULT '764d604ec6fc45f68cd92514c40e9e1a'::character varying,
  "group_name" varchar(64) COLLATE "pg_catalog"."default" NOT NULL DEFAULT ''::character varying,
  "max_id" int8 NOT NULL DEFAULT 1,
  "step" int4 NOT NULL DEFAULT 100,
  "update_dt" timestamp(6) NOT NULL DEFAULT CURRENT_TIMESTAMP
)
;
COMMENT ON COLUMN "public"."sj_sequence_alloc"."id" IS '主键';
COMMENT ON COLUMN "public"."sj_sequence_alloc"."namespace_id" IS '命名空间id';
COMMENT ON COLUMN "public"."sj_sequence_alloc"."group_name" IS '组名称';
COMMENT ON COLUMN "public"."sj_sequence_alloc"."max_id" IS '最大id';
COMMENT ON COLUMN "public"."sj_sequence_alloc"."step" IS '步长';
COMMENT ON COLUMN "public"."sj_sequence_alloc"."update_dt" IS '更新时间';
COMMENT ON TABLE "public"."sj_sequence_alloc" IS '号段模式序号ID分配表';

-- ----------------------------
-- Records of sj_sequence_alloc
-- ----------------------------

-- ----------------------------
-- Table structure for sj_server_node
-- ----------------------------
DROP TABLE IF EXISTS "public"."sj_server_node";
CREATE TABLE "public"."sj_server_node" (
  "id" int8 NOT NULL DEFAULT nextval('sj_server_node_id_seq'::regclass),
  "namespace_id" varchar(64) COLLATE "pg_catalog"."default" NOT NULL DEFAULT '764d604ec6fc45f68cd92514c40e9e1a'::character varying,
  "group_name" varchar(64) COLLATE "pg_catalog"."default" NOT NULL,
  "host_id" varchar(64) COLLATE "pg_catalog"."default" NOT NULL,
  "host_ip" varchar(64) COLLATE "pg_catalog"."default" NOT NULL,
  "host_port" int4 NOT NULL,
  "expire_at" timestamp(6) NOT NULL,
  "node_type" int2 NOT NULL,
  "ext_attrs" varchar(256) COLLATE "pg_catalog"."default" DEFAULT ''::character varying,
  "create_dt" timestamp(6) NOT NULL DEFAULT CURRENT_TIMESTAMP,
  "update_dt" timestamp(6) NOT NULL DEFAULT CURRENT_TIMESTAMP
)
;
COMMENT ON COLUMN "public"."sj_server_node"."id" IS '主键';
COMMENT ON COLUMN "public"."sj_server_node"."namespace_id" IS '命名空间id';
COMMENT ON COLUMN "public"."sj_server_node"."group_name" IS '组名称';
COMMENT ON COLUMN "public"."sj_server_node"."host_id" IS '主机id';
COMMENT ON COLUMN "public"."sj_server_node"."host_ip" IS '机器ip';
COMMENT ON COLUMN "public"."sj_server_node"."host_port" IS '机器端口';
COMMENT ON COLUMN "public"."sj_server_node"."expire_at" IS '过期时间';
COMMENT ON COLUMN "public"."sj_server_node"."node_type" IS '节点类型 1、客户端 2、是服务端';
COMMENT ON COLUMN "public"."sj_server_node"."ext_attrs" IS '扩展字段';
COMMENT ON COLUMN "public"."sj_server_node"."create_dt" IS '创建时间';
COMMENT ON COLUMN "public"."sj_server_node"."update_dt" IS '修改时间';
COMMENT ON TABLE "public"."sj_server_node" IS '服务器节点';

-- ----------------------------
-- Records of sj_server_node
-- ----------------------------
INSERT INTO "public"."sj_server_node" VALUES (2, 'DEFAULT_SERVER_NAMESPACE_ID', 'DEFAULT_SERVER', '1846567782544818176', '198.18.0.1', 1788, '2024-10-16 23:07:33.973519', 2, '{"webPort":8088}', '2024-10-16 23:04:04.021211', '2024-10-16 23:07:03.980105');

-- ----------------------------
-- Table structure for sj_system_user
-- ----------------------------
DROP TABLE IF EXISTS "public"."sj_system_user";
CREATE TABLE "public"."sj_system_user" (
  "id" int8 NOT NULL DEFAULT nextval('sj_system_user_id_seq'::regclass),
  "username" varchar(64) COLLATE "pg_catalog"."default" NOT NULL,
  "password" varchar(128) COLLATE "pg_catalog"."default" NOT NULL,
  "role" int2 NOT NULL DEFAULT 0,
  "create_dt" timestamp(6) NOT NULL DEFAULT CURRENT_TIMESTAMP,
  "update_dt" timestamp(6) NOT NULL DEFAULT CURRENT_TIMESTAMP
)
;
COMMENT ON COLUMN "public"."sj_system_user"."id" IS '主键';
COMMENT ON COLUMN "public"."sj_system_user"."username" IS '账号';
COMMENT ON COLUMN "public"."sj_system_user"."password" IS '密码';
COMMENT ON COLUMN "public"."sj_system_user"."role" IS '角色：1-普通用户、2-管理员';
COMMENT ON COLUMN "public"."sj_system_user"."create_dt" IS '创建时间';
COMMENT ON COLUMN "public"."sj_system_user"."update_dt" IS '修改时间';
COMMENT ON TABLE "public"."sj_system_user" IS '系统用户表';

-- ----------------------------
-- Records of sj_system_user
-- ----------------------------
INSERT INTO "public"."sj_system_user" VALUES (1, 'admin', '95097e227f2acc2a6e97945024aaf54b5d54f3eca5665898b4f7d08591020cb0', 2, '2024-10-16 22:58:04.182485', '2024-10-16 22:58:04.182485');

-- ----------------------------
-- Table structure for sj_system_user_permission
-- ----------------------------
DROP TABLE IF EXISTS "public"."sj_system_user_permission";
CREATE TABLE "public"."sj_system_user_permission" (
  "id" int8 NOT NULL DEFAULT nextval('sj_system_user_permission_id_seq'::regclass),
  "group_name" varchar(64) COLLATE "pg_catalog"."default" NOT NULL,
  "namespace_id" varchar(64) COLLATE "pg_catalog"."default" NOT NULL DEFAULT '764d604ec6fc45f68cd92514c40e9e1a'::character varying,
  "system_user_id" int8 NOT NULL,
  "create_dt" timestamp(6) NOT NULL DEFAULT CURRENT_TIMESTAMP,
  "update_dt" timestamp(6) NOT NULL DEFAULT CURRENT_TIMESTAMP
)
;
COMMENT ON COLUMN "public"."sj_system_user_permission"."id" IS '主键';
COMMENT ON COLUMN "public"."sj_system_user_permission"."group_name" IS '组名称';
COMMENT ON COLUMN "public"."sj_system_user_permission"."namespace_id" IS '命名空间id';
COMMENT ON COLUMN "public"."sj_system_user_permission"."system_user_id" IS '系统用户id';
COMMENT ON COLUMN "public"."sj_system_user_permission"."create_dt" IS '创建时间';
COMMENT ON COLUMN "public"."sj_system_user_permission"."update_dt" IS '修改时间';
COMMENT ON TABLE "public"."sj_system_user_permission" IS '系统用户权限表';

-- ----------------------------
-- Records of sj_system_user_permission
-- ----------------------------

-- ----------------------------
-- Table structure for sj_workflow
-- ----------------------------
DROP TABLE IF EXISTS "public"."sj_workflow";
CREATE TABLE "public"."sj_workflow" (
  "id" int8 NOT NULL DEFAULT nextval('sj_workflow_id_seq'::regclass),
  "workflow_name" varchar(64) COLLATE "pg_catalog"."default" NOT NULL,
  "namespace_id" varchar(64) COLLATE "pg_catalog"."default" NOT NULL DEFAULT '764d604ec6fc45f68cd92514c40e9e1a'::character varying,
  "group_name" varchar(64) COLLATE "pg_catalog"."default" NOT NULL,
  "workflow_status" int2 NOT NULL DEFAULT 1,
  "trigger_type" int2 NOT NULL,
  "trigger_interval" varchar(255) COLLATE "pg_catalog"."default" NOT NULL,
  "next_trigger_at" int8 NOT NULL,
  "block_strategy" int2 NOT NULL DEFAULT 1,
  "executor_timeout" int4 NOT NULL DEFAULT 0,
  "description" varchar(256) COLLATE "pg_catalog"."default" NOT NULL DEFAULT ''::character varying,
  "flow_info" text COLLATE "pg_catalog"."default",
  "wf_context" text COLLATE "pg_catalog"."default",
  "bucket_index" int4 NOT NULL DEFAULT 0,
  "version" int4 NOT NULL,
  "ext_attrs" varchar(256) COLLATE "pg_catalog"."default" DEFAULT ''::character varying,
  "deleted" int2 NOT NULL DEFAULT 0,
  "create_dt" timestamp(6) NOT NULL DEFAULT CURRENT_TIMESTAMP,
  "update_dt" timestamp(6) NOT NULL DEFAULT CURRENT_TIMESTAMP
)
;
COMMENT ON COLUMN "public"."sj_workflow"."id" IS '主键';
COMMENT ON COLUMN "public"."sj_workflow"."workflow_name" IS '工作流名称';
COMMENT ON COLUMN "public"."sj_workflow"."namespace_id" IS '命名空间id';
COMMENT ON COLUMN "public"."sj_workflow"."group_name" IS '组名称';
COMMENT ON COLUMN "public"."sj_workflow"."workflow_status" IS '工作流状态 0、关闭、1、开启';
COMMENT ON COLUMN "public"."sj_workflow"."trigger_type" IS '触发类型 1.CRON 表达式 2. 固定时间';
COMMENT ON COLUMN "public"."sj_workflow"."trigger_interval" IS '间隔时长';
COMMENT ON COLUMN "public"."sj_workflow"."next_trigger_at" IS '下次触发时间';
COMMENT ON COLUMN "public"."sj_workflow"."block_strategy" IS '阻塞策略 1、丢弃 2、覆盖 3、并行';
COMMENT ON COLUMN "public"."sj_workflow"."executor_timeout" IS '任务执行超时时间，单位秒';
COMMENT ON COLUMN "public"."sj_workflow"."description" IS '描述';
COMMENT ON COLUMN "public"."sj_workflow"."flow_info" IS '流程信息';
COMMENT ON COLUMN "public"."sj_workflow"."wf_context" IS '上下文';
COMMENT ON COLUMN "public"."sj_workflow"."bucket_index" IS 'bucket';
COMMENT ON COLUMN "public"."sj_workflow"."version" IS '版本号';
COMMENT ON COLUMN "public"."sj_workflow"."ext_attrs" IS '扩展字段';
COMMENT ON COLUMN "public"."sj_workflow"."deleted" IS '逻辑删除 1、删除';
COMMENT ON COLUMN "public"."sj_workflow"."create_dt" IS '创建时间';
COMMENT ON COLUMN "public"."sj_workflow"."update_dt" IS '修改时间';
COMMENT ON TABLE "public"."sj_workflow" IS '工作流';

-- ----------------------------
-- Records of sj_workflow
-- ----------------------------

-- ----------------------------
-- Table structure for sj_workflow_node
-- ----------------------------
DROP TABLE IF EXISTS "public"."sj_workflow_node";
CREATE TABLE "public"."sj_workflow_node" (
  "id" int8 NOT NULL DEFAULT nextval('sj_workflow_node_id_seq'::regclass),
  "namespace_id" varchar(64) COLLATE "pg_catalog"."default" NOT NULL DEFAULT '764d604ec6fc45f68cd92514c40e9e1a'::character varying,
  "node_name" varchar(64) COLLATE "pg_catalog"."default" NOT NULL,
  "group_name" varchar(64) COLLATE "pg_catalog"."default" NOT NULL,
  "job_id" int8 NOT NULL,
  "workflow_id" int8 NOT NULL,
  "node_type" int2 NOT NULL DEFAULT 1,
  "expression_type" int2 NOT NULL DEFAULT 0,
  "fail_strategy" int2 NOT NULL DEFAULT 1,
  "workflow_node_status" int2 NOT NULL DEFAULT 1,
  "priority_level" int4 NOT NULL DEFAULT 1,
  "node_info" text COLLATE "pg_catalog"."default",
  "version" int4 NOT NULL,
  "ext_attrs" varchar(256) COLLATE "pg_catalog"."default" DEFAULT ''::character varying,
  "deleted" int2 NOT NULL DEFAULT 0,
  "create_dt" timestamp(6) NOT NULL DEFAULT CURRENT_TIMESTAMP,
  "update_dt" timestamp(6) NOT NULL DEFAULT CURRENT_TIMESTAMP
)
;
COMMENT ON COLUMN "public"."sj_workflow_node"."id" IS '主键';
COMMENT ON COLUMN "public"."sj_workflow_node"."namespace_id" IS '命名空间id';
COMMENT ON COLUMN "public"."sj_workflow_node"."node_name" IS '节点名称';
COMMENT ON COLUMN "public"."sj_workflow_node"."group_name" IS '组名称';
COMMENT ON COLUMN "public"."sj_workflow_node"."job_id" IS '任务信息id';
COMMENT ON COLUMN "public"."sj_workflow_node"."workflow_id" IS '工作流ID';
COMMENT ON COLUMN "public"."sj_workflow_node"."node_type" IS '1、任务节点 2、条件节点';
COMMENT ON COLUMN "public"."sj_workflow_node"."expression_type" IS '1、SpEl、2、Aviator 3、QL';
COMMENT ON COLUMN "public"."sj_workflow_node"."fail_strategy" IS '失败策略 1、跳过 2、阻塞';
COMMENT ON COLUMN "public"."sj_workflow_node"."workflow_node_status" IS '工作流节点状态 0、关闭、1、开启';
COMMENT ON COLUMN "public"."sj_workflow_node"."priority_level" IS '优先级';
COMMENT ON COLUMN "public"."sj_workflow_node"."node_info" IS '节点信息 ';
COMMENT ON COLUMN "public"."sj_workflow_node"."version" IS '版本号';
COMMENT ON COLUMN "public"."sj_workflow_node"."ext_attrs" IS '扩展字段';
COMMENT ON COLUMN "public"."sj_workflow_node"."deleted" IS '逻辑删除 1、删除';
COMMENT ON COLUMN "public"."sj_workflow_node"."create_dt" IS '创建时间';
COMMENT ON COLUMN "public"."sj_workflow_node"."update_dt" IS '修改时间';
COMMENT ON TABLE "public"."sj_workflow_node" IS '工作流节点';

-- ----------------------------
-- Records of sj_workflow_node
-- ----------------------------

-- ----------------------------
-- Table structure for sj_workflow_task_batch
-- ----------------------------
DROP TABLE IF EXISTS "public"."sj_workflow_task_batch";
CREATE TABLE "public"."sj_workflow_task_batch" (
  "id" int8 NOT NULL DEFAULT nextval('sj_workflow_task_batch_id_seq'::regclass),
  "namespace_id" varchar(64) COLLATE "pg_catalog"."default" NOT NULL DEFAULT '764d604ec6fc45f68cd92514c40e9e1a'::character varying,
  "group_name" varchar(64) COLLATE "pg_catalog"."default" NOT NULL,
  "workflow_id" int8 NOT NULL,
  "task_batch_status" int2 NOT NULL DEFAULT 0,
  "operation_reason" int2 NOT NULL DEFAULT 0,
  "flow_info" text COLLATE "pg_catalog"."default",
  "wf_context" text COLLATE "pg_catalog"."default",
  "execution_at" int8 NOT NULL DEFAULT 0,
  "ext_attrs" varchar(256) COLLATE "pg_catalog"."default" DEFAULT ''::character varying,
  "version" int4 NOT NULL DEFAULT 1,
  "deleted" int2 NOT NULL DEFAULT 0,
  "create_dt" timestamp(6) NOT NULL DEFAULT CURRENT_TIMESTAMP,
  "update_dt" timestamp(6) NOT NULL DEFAULT CURRENT_TIMESTAMP
)
;
COMMENT ON COLUMN "public"."sj_workflow_task_batch"."id" IS '主键';
COMMENT ON COLUMN "public"."sj_workflow_task_batch"."namespace_id" IS '命名空间id';
COMMENT ON COLUMN "public"."sj_workflow_task_batch"."group_name" IS '组名称';
COMMENT ON COLUMN "public"."sj_workflow_task_batch"."workflow_id" IS '工作流任务id';
COMMENT ON COLUMN "public"."sj_workflow_task_batch"."task_batch_status" IS '任务批次状态 0、失败 1、成功';
COMMENT ON COLUMN "public"."sj_workflow_task_batch"."operation_reason" IS '操作原因';
COMMENT ON COLUMN "public"."sj_workflow_task_batch"."flow_info" IS '流程信息';
COMMENT ON COLUMN "public"."sj_workflow_task_batch"."wf_context" IS '全局上下文';
COMMENT ON COLUMN "public"."sj_workflow_task_batch"."execution_at" IS '任务执行时间';
COMMENT ON COLUMN "public"."sj_workflow_task_batch"."ext_attrs" IS '扩展字段';
COMMENT ON COLUMN "public"."sj_workflow_task_batch"."version" IS '版本号';
COMMENT ON COLUMN "public"."sj_workflow_task_batch"."deleted" IS '逻辑删除 1、删除';
COMMENT ON COLUMN "public"."sj_workflow_task_batch"."create_dt" IS '创建时间';
COMMENT ON COLUMN "public"."sj_workflow_task_batch"."update_dt" IS '修改时间';
COMMENT ON TABLE "public"."sj_workflow_task_batch" IS '工作流批次';

-- ----------------------------
-- Records of sj_workflow_task_batch
-- ----------------------------

-- ----------------------------
-- Alter sequences owned by
-- ----------------------------
ALTER SEQUENCE "public"."sj_group_config_id_seq"
OWNED BY "public"."sj_group_config"."id";
SELECT setval('"public"."sj_group_config_id_seq"', 1, false);

-- ----------------------------
-- Alter sequences owned by
-- ----------------------------
ALTER SEQUENCE "public"."sj_job_id_seq"
OWNED BY "public"."sj_job"."id";
SELECT setval('"public"."sj_job_id_seq"', 1, false);

-- ----------------------------
-- Alter sequences owned by
-- ----------------------------
ALTER SEQUENCE "public"."sj_job_log_message_id_seq"
OWNED BY "public"."sj_job_log_message"."id";
SELECT setval('"public"."sj_job_log_message_id_seq"', 1, false);

-- ----------------------------
-- Alter sequences owned by
-- ----------------------------
ALTER SEQUENCE "public"."sj_job_summary_id_seq"
OWNED BY "public"."sj_job_summary"."id";
SELECT setval('"public"."sj_job_summary_id_seq"', 2, true);

-- ----------------------------
-- Alter sequences owned by
-- ----------------------------
ALTER SEQUENCE "public"."sj_job_task_batch_id_seq"
OWNED BY "public"."sj_job_task_batch"."id";
SELECT setval('"public"."sj_job_task_batch_id_seq"', 2, true);

-- ----------------------------
-- Alter sequences owned by
-- ----------------------------
ALTER SEQUENCE "public"."sj_job_task_id_seq"
OWNED BY "public"."sj_job_task"."id";
SELECT setval('"public"."sj_job_task_id_seq"', 1, false);

-- ----------------------------
-- Alter sequences owned by
-- ----------------------------
ALTER SEQUENCE "public"."sj_namespace_id_seq"
OWNED BY "public"."sj_namespace"."id";
SELECT setval('"public"."sj_namespace_id_seq"', 1, false);

-- ----------------------------
-- Alter sequences owned by
-- ----------------------------
ALTER SEQUENCE "public"."sj_notify_config_id_seq"
OWNED BY "public"."sj_notify_config"."id";
SELECT setval('"public"."sj_notify_config_id_seq"', 1, false);

-- ----------------------------
-- Alter sequences owned by
-- ----------------------------
ALTER SEQUENCE "public"."sj_notify_recipient_id_seq"
OWNED BY "public"."sj_notify_recipient"."id";
SELECT setval('"public"."sj_notify_recipient_id_seq"', 1, false);

-- ----------------------------
-- Alter sequences owned by
-- ----------------------------
ALTER SEQUENCE "public"."sj_retry_dead_letter_0_id_seq"
OWNED BY "public"."sj_retry_dead_letter_0"."id";
SELECT setval('"public"."sj_retry_dead_letter_0_id_seq"', 1, false);

-- ----------------------------
-- Alter sequences owned by
-- ----------------------------
ALTER SEQUENCE "public"."sj_retry_scene_config_id_seq"
OWNED BY "public"."sj_retry_scene_config"."id";
SELECT setval('"public"."sj_retry_scene_config_id_seq"', 1, false);

-- ----------------------------
-- Alter sequences owned by
-- ----------------------------
ALTER SEQUENCE "public"."sj_retry_summary_id_seq"
OWNED BY "public"."sj_retry_summary"."id";
SELECT setval('"public"."sj_retry_summary_id_seq"', 1, false);

-- ----------------------------
-- Alter sequences owned by
-- ----------------------------
ALTER SEQUENCE "public"."sj_retry_task_0_id_seq"
OWNED BY "public"."sj_retry_task_0"."id";
SELECT setval('"public"."sj_retry_task_0_id_seq"', 1, false);

-- ----------------------------
-- Alter sequences owned by
-- ----------------------------
ALTER SEQUENCE "public"."sj_retry_task_log_id_seq"
OWNED BY "public"."sj_retry_task_log"."id";
SELECT setval('"public"."sj_retry_task_log_id_seq"', 1, false);

-- ----------------------------
-- Alter sequences owned by
-- ----------------------------
ALTER SEQUENCE "public"."sj_retry_task_log_message_id_seq"
OWNED BY "public"."sj_retry_task_log_message"."id";
SELECT setval('"public"."sj_retry_task_log_message_id_seq"', 1, false);

-- ----------------------------
-- Alter sequences owned by
-- ----------------------------
ALTER SEQUENCE "public"."sj_sequence_alloc_id_seq"
OWNED BY "public"."sj_sequence_alloc"."id";
SELECT setval('"public"."sj_sequence_alloc_id_seq"', 1, false);

-- ----------------------------
-- Alter sequences owned by
-- ----------------------------
ALTER SEQUENCE "public"."sj_server_node_id_seq"
OWNED BY "public"."sj_server_node"."id";
SELECT setval('"public"."sj_server_node_id_seq"', 2, true);

-- ----------------------------
-- Alter sequences owned by
-- ----------------------------
ALTER SEQUENCE "public"."sj_system_user_id_seq"
OWNED BY "public"."sj_system_user"."id";
SELECT setval('"public"."sj_system_user_id_seq"', 1, true);

-- ----------------------------
-- Alter sequences owned by
-- ----------------------------
ALTER SEQUENCE "public"."sj_system_user_permission_id_seq"
OWNED BY "public"."sj_system_user_permission"."id";
SELECT setval('"public"."sj_system_user_permission_id_seq"', 1, false);

-- ----------------------------
-- Alter sequences owned by
-- ----------------------------
ALTER SEQUENCE "public"."sj_workflow_id_seq"
OWNED BY "public"."sj_workflow"."id";
SELECT setval('"public"."sj_workflow_id_seq"', 1, false);

-- ----------------------------
-- Alter sequences owned by
-- ----------------------------
ALTER SEQUENCE "public"."sj_workflow_node_id_seq"
OWNED BY "public"."sj_workflow_node"."id";
SELECT setval('"public"."sj_workflow_node_id_seq"', 1, false);

-- ----------------------------
-- Alter sequences owned by
-- ----------------------------
ALTER SEQUENCE "public"."sj_workflow_task_batch_id_seq"
OWNED BY "public"."sj_workflow_task_batch"."id";
SELECT setval('"public"."sj_workflow_task_batch_id_seq"', 1, false);

-- ----------------------------
-- Primary Key structure for table databasechangeloglock
-- ----------------------------
ALTER TABLE "public"."databasechangeloglock" ADD CONSTRAINT "databasechangeloglock_pkey" PRIMARY KEY ("id");

-- ----------------------------
-- Primary Key structure for table sj_distributed_lock
-- ----------------------------
ALTER TABLE "public"."sj_distributed_lock" ADD CONSTRAINT "sj_distributed_lock_pkey" PRIMARY KEY ("name");

-- ----------------------------
-- Indexes structure for table sj_group_config
-- ----------------------------
CREATE UNIQUE INDEX "uk_sj_group_config_01" ON "public"."sj_group_config" USING btree (
  "namespace_id" COLLATE "pg_catalog"."default" "pg_catalog"."text_ops" ASC NULLS LAST,
  "group_name" COLLATE "pg_catalog"."default" "pg_catalog"."text_ops" ASC NULLS LAST
);

-- ----------------------------
-- Primary Key structure for table sj_group_config
-- ----------------------------
ALTER TABLE "public"."sj_group_config" ADD CONSTRAINT "sj_group_config_pkey" PRIMARY KEY ("id");

-- ----------------------------
-- Indexes structure for table sj_job
-- ----------------------------
CREATE INDEX "idx_sj_job_01" ON "public"."sj_job" USING btree (
  "namespace_id" COLLATE "pg_catalog"."default" "pg_catalog"."text_ops" ASC NULLS LAST,
  "group_name" COLLATE "pg_catalog"."default" "pg_catalog"."text_ops" ASC NULLS LAST
);
CREATE INDEX "idx_sj_job_02" ON "public"."sj_job" USING btree (
  "job_status" "pg_catalog"."int2_ops" ASC NULLS LAST,
  "bucket_index" "pg_catalog"."int4_ops" ASC NULLS LAST
);
CREATE INDEX "idx_sj_job_03" ON "public"."sj_job" USING btree (
  "create_dt" "pg_catalog"."timestamp_ops" ASC NULLS LAST
);

-- ----------------------------
-- Primary Key structure for table sj_job
-- ----------------------------
ALTER TABLE "public"."sj_job" ADD CONSTRAINT "sj_job_pkey" PRIMARY KEY ("id");

-- ----------------------------
-- Indexes structure for table sj_job_log_message
-- ----------------------------
CREATE INDEX "idx_sj_job_log_message_01" ON "public"."sj_job_log_message" USING btree (
  "task_batch_id" "pg_catalog"."int8_ops" ASC NULLS LAST,
  "task_id" "pg_catalog"."int8_ops" ASC NULLS LAST
);
CREATE INDEX "idx_sj_job_log_message_02" ON "public"."sj_job_log_message" USING btree (
  "create_dt" "pg_catalog"."timestamp_ops" ASC NULLS LAST
);
CREATE INDEX "idx_sj_job_log_message_03" ON "public"."sj_job_log_message" USING btree (
  "namespace_id" COLLATE "pg_catalog"."default" "pg_catalog"."text_ops" ASC NULLS LAST,
  "group_name" COLLATE "pg_catalog"."default" "pg_catalog"."text_ops" ASC NULLS LAST
);

-- ----------------------------
-- Primary Key structure for table sj_job_log_message
-- ----------------------------
ALTER TABLE "public"."sj_job_log_message" ADD CONSTRAINT "sj_job_log_message_pkey" PRIMARY KEY ("id");

-- ----------------------------
-- Indexes structure for table sj_job_summary
-- ----------------------------
CREATE INDEX "idx_sj_job_summary_01" ON "public"."sj_job_summary" USING btree (
  "namespace_id" COLLATE "pg_catalog"."default" "pg_catalog"."text_ops" ASC NULLS LAST,
  "group_name" COLLATE "pg_catalog"."default" "pg_catalog"."text_ops" ASC NULLS LAST,
  "business_id" "pg_catalog"."int8_ops" ASC NULLS LAST
);
CREATE UNIQUE INDEX "uk_sj_job_summary_01" ON "public"."sj_job_summary" USING btree (
  "trigger_at" "pg_catalog"."timestamp_ops" ASC NULLS LAST,
  "system_task_type" "pg_catalog"."int2_ops" ASC NULLS LAST,
  "business_id" "pg_catalog"."int8_ops" ASC NULLS LAST
);

-- ----------------------------
-- Primary Key structure for table sj_job_summary
-- ----------------------------
ALTER TABLE "public"."sj_job_summary" ADD CONSTRAINT "sj_job_summary_pkey" PRIMARY KEY ("id");

-- ----------------------------
-- Indexes structure for table sj_job_task
-- ----------------------------
CREATE INDEX "idx_sj_job_task_01" ON "public"."sj_job_task" USING btree (
  "task_batch_id" "pg_catalog"."int8_ops" ASC NULLS LAST,
  "task_status" "pg_catalog"."int2_ops" ASC NULLS LAST
);
CREATE INDEX "idx_sj_job_task_02" ON "public"."sj_job_task" USING btree (
  "create_dt" "pg_catalog"."timestamp_ops" ASC NULLS LAST
);
CREATE INDEX "idx_sj_job_task_03" ON "public"."sj_job_task" USING btree (
  "namespace_id" COLLATE "pg_catalog"."default" "pg_catalog"."text_ops" ASC NULLS LAST,
  "group_name" COLLATE "pg_catalog"."default" "pg_catalog"."text_ops" ASC NULLS LAST
);

-- ----------------------------
-- Primary Key structure for table sj_job_task
-- ----------------------------
ALTER TABLE "public"."sj_job_task" ADD CONSTRAINT "sj_job_task_pkey" PRIMARY KEY ("id");

-- ----------------------------
-- Indexes structure for table sj_job_task_batch
-- ----------------------------
CREATE INDEX "idx_sj_job_task_batch_01" ON "public"."sj_job_task_batch" USING btree (
  "job_id" "pg_catalog"."int8_ops" ASC NULLS LAST,
  "task_batch_status" "pg_catalog"."int2_ops" ASC NULLS LAST
);
CREATE INDEX "idx_sj_job_task_batch_02" ON "public"."sj_job_task_batch" USING btree (
  "create_dt" "pg_catalog"."timestamp_ops" ASC NULLS LAST
);
CREATE INDEX "idx_sj_job_task_batch_03" ON "public"."sj_job_task_batch" USING btree (
  "namespace_id" COLLATE "pg_catalog"."default" "pg_catalog"."text_ops" ASC NULLS LAST,
  "group_name" COLLATE "pg_catalog"."default" "pg_catalog"."text_ops" ASC NULLS LAST
);
CREATE INDEX "idx_sj_job_task_batch_04" ON "public"."sj_job_task_batch" USING btree (
  "workflow_task_batch_id" "pg_catalog"."int8_ops" ASC NULLS LAST,
  "workflow_node_id" "pg_catalog"."int8_ops" ASC NULLS LAST
);

-- ----------------------------
-- Primary Key structure for table sj_job_task_batch
-- ----------------------------
ALTER TABLE "public"."sj_job_task_batch" ADD CONSTRAINT "sj_job_task_batch_pkey" PRIMARY KEY ("id");

-- ----------------------------
-- Indexes structure for table sj_namespace
-- ----------------------------
CREATE INDEX "idx_sj_namespace_01" ON "public"."sj_namespace" USING btree (
  "name" COLLATE "pg_catalog"."default" "pg_catalog"."text_ops" ASC NULLS LAST
);

-- ----------------------------
-- Primary Key structure for table sj_namespace
-- ----------------------------
ALTER TABLE "public"."sj_namespace" ADD CONSTRAINT "sj_namespace_pkey" PRIMARY KEY ("id");

-- ----------------------------
-- Indexes structure for table sj_notify_config
-- ----------------------------
CREATE INDEX "idx_sj_notify_config_01" ON "public"."sj_notify_config" USING btree (
  "namespace_id" COLLATE "pg_catalog"."default" "pg_catalog"."text_ops" ASC NULLS LAST,
  "group_name" COLLATE "pg_catalog"."default" "pg_catalog"."text_ops" ASC NULLS LAST,
  "business_id" COLLATE "pg_catalog"."default" "pg_catalog"."text_ops" ASC NULLS LAST
);

-- ----------------------------
-- Primary Key structure for table sj_notify_config
-- ----------------------------
ALTER TABLE "public"."sj_notify_config" ADD CONSTRAINT "sj_notify_config_pkey" PRIMARY KEY ("id");

-- ----------------------------
-- Indexes structure for table sj_notify_recipient
-- ----------------------------
CREATE INDEX "idx_sj_notify_recipient_01" ON "public"."sj_notify_recipient" USING btree (
  "namespace_id" COLLATE "pg_catalog"."default" "pg_catalog"."text_ops" ASC NULLS LAST
);

-- ----------------------------
-- Primary Key structure for table sj_notify_recipient
-- ----------------------------
ALTER TABLE "public"."sj_notify_recipient" ADD CONSTRAINT "sj_notify_recipient_pkey" PRIMARY KEY ("id");

-- ----------------------------
-- Indexes structure for table sj_retry_dead_letter_0
-- ----------------------------
CREATE INDEX "idx_sj_retry_dead_letter_0_01" ON "public"."sj_retry_dead_letter_0" USING btree (
  "namespace_id" COLLATE "pg_catalog"."default" "pg_catalog"."text_ops" ASC NULLS LAST,
  "group_name" COLLATE "pg_catalog"."default" "pg_catalog"."text_ops" ASC NULLS LAST,
  "scene_name" COLLATE "pg_catalog"."default" "pg_catalog"."text_ops" ASC NULLS LAST
);
CREATE INDEX "idx_sj_retry_dead_letter_0_02" ON "public"."sj_retry_dead_letter_0" USING btree (
  "idempotent_id" COLLATE "pg_catalog"."default" "pg_catalog"."text_ops" ASC NULLS LAST
);
CREATE INDEX "idx_sj_retry_dead_letter_0_03" ON "public"."sj_retry_dead_letter_0" USING btree (
  "biz_no" COLLATE "pg_catalog"."default" "pg_catalog"."text_ops" ASC NULLS LAST
);
CREATE INDEX "idx_sj_retry_dead_letter_0_04" ON "public"."sj_retry_dead_letter_0" USING btree (
  "create_dt" "pg_catalog"."timestamp_ops" ASC NULLS LAST
);
CREATE UNIQUE INDEX "uk_sj_retry_dead_letter_0_01" ON "public"."sj_retry_dead_letter_0" USING btree (
  "namespace_id" COLLATE "pg_catalog"."default" "pg_catalog"."text_ops" ASC NULLS LAST,
  "group_name" COLLATE "pg_catalog"."default" "pg_catalog"."text_ops" ASC NULLS LAST,
  "unique_id" COLLATE "pg_catalog"."default" "pg_catalog"."text_ops" ASC NULLS LAST
);

-- ----------------------------
-- Primary Key structure for table sj_retry_dead_letter_0
-- ----------------------------
ALTER TABLE "public"."sj_retry_dead_letter_0" ADD CONSTRAINT "sj_retry_dead_letter_0_pkey" PRIMARY KEY ("id");

-- ----------------------------
-- Indexes structure for table sj_retry_scene_config
-- ----------------------------
CREATE UNIQUE INDEX "uk_sj_retry_scene_config_01" ON "public"."sj_retry_scene_config" USING btree (
  "namespace_id" COLLATE "pg_catalog"."default" "pg_catalog"."text_ops" ASC NULLS LAST,
  "group_name" COLLATE "pg_catalog"."default" "pg_catalog"."text_ops" ASC NULLS LAST,
  "scene_name" COLLATE "pg_catalog"."default" "pg_catalog"."text_ops" ASC NULLS LAST
);

-- ----------------------------
-- Primary Key structure for table sj_retry_scene_config
-- ----------------------------
ALTER TABLE "public"."sj_retry_scene_config" ADD CONSTRAINT "sj_retry_scene_config_pkey" PRIMARY KEY ("id");

-- ----------------------------
-- Indexes structure for table sj_retry_summary
-- ----------------------------
CREATE INDEX "idx_sj_retry_summary_01" ON "public"."sj_retry_summary" USING btree (
  "trigger_at" "pg_catalog"."timestamp_ops" ASC NULLS LAST
);
CREATE UNIQUE INDEX "uk_sj_retry_summary_01" ON "public"."sj_retry_summary" USING btree (
  "namespace_id" COLLATE "pg_catalog"."default" "pg_catalog"."text_ops" ASC NULLS LAST,
  "group_name" COLLATE "pg_catalog"."default" "pg_catalog"."text_ops" ASC NULLS LAST,
  "scene_name" COLLATE "pg_catalog"."default" "pg_catalog"."text_ops" ASC NULLS LAST,
  "trigger_at" "pg_catalog"."timestamp_ops" ASC NULLS LAST
);

-- ----------------------------
-- Primary Key structure for table sj_retry_summary
-- ----------------------------
ALTER TABLE "public"."sj_retry_summary" ADD CONSTRAINT "sj_retry_summary_pkey" PRIMARY KEY ("id");

-- ----------------------------
-- Indexes structure for table sj_retry_task_0
-- ----------------------------
CREATE INDEX "idx_sj_retry_task_0_01" ON "public"."sj_retry_task_0" USING btree (
  "namespace_id" COLLATE "pg_catalog"."default" "pg_catalog"."text_ops" ASC NULLS LAST,
  "group_name" COLLATE "pg_catalog"."default" "pg_catalog"."text_ops" ASC NULLS LAST,
  "scene_name" COLLATE "pg_catalog"."default" "pg_catalog"."text_ops" ASC NULLS LAST
);
CREATE INDEX "idx_sj_retry_task_0_02" ON "public"."sj_retry_task_0" USING btree (
  "namespace_id" COLLATE "pg_catalog"."default" "pg_catalog"."text_ops" ASC NULLS LAST,
  "group_name" COLLATE "pg_catalog"."default" "pg_catalog"."text_ops" ASC NULLS LAST,
  "task_type" "pg_catalog"."int2_ops" ASC NULLS LAST
);
CREATE INDEX "idx_sj_retry_task_0_03" ON "public"."sj_retry_task_0" USING btree (
  "namespace_id" COLLATE "pg_catalog"."default" "pg_catalog"."text_ops" ASC NULLS LAST,
  "group_name" COLLATE "pg_catalog"."default" "pg_catalog"."text_ops" ASC NULLS LAST,
  "retry_status" "pg_catalog"."int2_ops" ASC NULLS LAST
);
CREATE INDEX "idx_sj_retry_task_0_04" ON "public"."sj_retry_task_0" USING btree (
  "idempotent_id" COLLATE "pg_catalog"."default" "pg_catalog"."text_ops" ASC NULLS LAST
);
CREATE INDEX "idx_sj_retry_task_0_05" ON "public"."sj_retry_task_0" USING btree (
  "biz_no" COLLATE "pg_catalog"."default" "pg_catalog"."text_ops" ASC NULLS LAST
);
CREATE INDEX "idx_sj_retry_task_0_06" ON "public"."sj_retry_task_0" USING btree (
  "create_dt" "pg_catalog"."timestamp_ops" ASC NULLS LAST
);
CREATE UNIQUE INDEX "uk_sj_retry_task_0_01" ON "public"."sj_retry_task_0" USING btree (
  "namespace_id" COLLATE "pg_catalog"."default" "pg_catalog"."text_ops" ASC NULLS LAST,
  "group_name" COLLATE "pg_catalog"."default" "pg_catalog"."text_ops" ASC NULLS LAST,
  "unique_id" COLLATE "pg_catalog"."default" "pg_catalog"."text_ops" ASC NULLS LAST
);

-- ----------------------------
-- Primary Key structure for table sj_retry_task_0
-- ----------------------------
ALTER TABLE "public"."sj_retry_task_0" ADD CONSTRAINT "sj_retry_task_0_pkey" PRIMARY KEY ("id");

-- ----------------------------
-- Indexes structure for table sj_retry_task_log
-- ----------------------------
CREATE INDEX "idx_sj_retry_task_log_01" ON "public"."sj_retry_task_log" USING btree (
  "namespace_id" COLLATE "pg_catalog"."default" "pg_catalog"."text_ops" ASC NULLS LAST,
  "group_name" COLLATE "pg_catalog"."default" "pg_catalog"."text_ops" ASC NULLS LAST,
  "scene_name" COLLATE "pg_catalog"."default" "pg_catalog"."text_ops" ASC NULLS LAST
);
CREATE INDEX "idx_sj_retry_task_log_02" ON "public"."sj_retry_task_log" USING btree (
  "retry_status" "pg_catalog"."int2_ops" ASC NULLS LAST
);
CREATE INDEX "idx_sj_retry_task_log_03" ON "public"."sj_retry_task_log" USING btree (
  "idempotent_id" COLLATE "pg_catalog"."default" "pg_catalog"."text_ops" ASC NULLS LAST
);
CREATE INDEX "idx_sj_retry_task_log_04" ON "public"."sj_retry_task_log" USING btree (
  "unique_id" COLLATE "pg_catalog"."default" "pg_catalog"."text_ops" ASC NULLS LAST
);
CREATE INDEX "idx_sj_retry_task_log_05" ON "public"."sj_retry_task_log" USING btree (
  "biz_no" COLLATE "pg_catalog"."default" "pg_catalog"."text_ops" ASC NULLS LAST
);
CREATE INDEX "idx_sj_retry_task_log_06" ON "public"."sj_retry_task_log" USING btree (
  "create_dt" "pg_catalog"."timestamp_ops" ASC NULLS LAST
);

-- ----------------------------
-- Primary Key structure for table sj_retry_task_log
-- ----------------------------
ALTER TABLE "public"."sj_retry_task_log" ADD CONSTRAINT "sj_retry_task_log_pkey" PRIMARY KEY ("id");

-- ----------------------------
-- Indexes structure for table sj_retry_task_log_message
-- ----------------------------
CREATE INDEX "idx_sj_retry_task_log_message_01" ON "public"."sj_retry_task_log_message" USING btree (
  "namespace_id" COLLATE "pg_catalog"."default" "pg_catalog"."text_ops" ASC NULLS LAST,
  "group_name" COLLATE "pg_catalog"."default" "pg_catalog"."text_ops" ASC NULLS LAST,
  "unique_id" COLLATE "pg_catalog"."default" "pg_catalog"."text_ops" ASC NULLS LAST
);
CREATE INDEX "idx_sj_retry_task_log_message_02" ON "public"."sj_retry_task_log_message" USING btree (
  "create_dt" "pg_catalog"."timestamp_ops" ASC NULLS LAST
);

-- ----------------------------
-- Primary Key structure for table sj_retry_task_log_message
-- ----------------------------
ALTER TABLE "public"."sj_retry_task_log_message" ADD CONSTRAINT "sj_retry_task_log_message_pkey" PRIMARY KEY ("id");

-- ----------------------------
-- Indexes structure for table sj_sequence_alloc
-- ----------------------------
CREATE UNIQUE INDEX "uk_sj_sequence_alloc_01" ON "public"."sj_sequence_alloc" USING btree (
  "namespace_id" COLLATE "pg_catalog"."default" "pg_catalog"."text_ops" ASC NULLS LAST,
  "group_name" COLLATE "pg_catalog"."default" "pg_catalog"."text_ops" ASC NULLS LAST
);

-- ----------------------------
-- Primary Key structure for table sj_sequence_alloc
-- ----------------------------
ALTER TABLE "public"."sj_sequence_alloc" ADD CONSTRAINT "sj_sequence_alloc_pkey" PRIMARY KEY ("id");

-- ----------------------------
-- Indexes structure for table sj_server_node
-- ----------------------------
CREATE INDEX "idx_sj_server_node_01" ON "public"."sj_server_node" USING btree (
  "namespace_id" COLLATE "pg_catalog"."default" "pg_catalog"."text_ops" ASC NULLS LAST,
  "group_name" COLLATE "pg_catalog"."default" "pg_catalog"."text_ops" ASC NULLS LAST
);
CREATE INDEX "idx_sj_server_node_02" ON "public"."sj_server_node" USING btree (
  "expire_at" "pg_catalog"."timestamp_ops" ASC NULLS LAST,
  "node_type" "pg_catalog"."int2_ops" ASC NULLS LAST
);
CREATE UNIQUE INDEX "uk_sj_server_node_01" ON "public"."sj_server_node" USING btree (
  "host_id" COLLATE "pg_catalog"."default" "pg_catalog"."text_ops" ASC NULLS LAST,
  "host_ip" COLLATE "pg_catalog"."default" "pg_catalog"."text_ops" ASC NULLS LAST
);

-- ----------------------------
-- Primary Key structure for table sj_server_node
-- ----------------------------
ALTER TABLE "public"."sj_server_node" ADD CONSTRAINT "sj_server_node_pkey" PRIMARY KEY ("id");

-- ----------------------------
-- Primary Key structure for table sj_system_user
-- ----------------------------
ALTER TABLE "public"."sj_system_user" ADD CONSTRAINT "sj_system_user_pkey" PRIMARY KEY ("id");

-- ----------------------------
-- Indexes structure for table sj_system_user_permission
-- ----------------------------
CREATE UNIQUE INDEX "uk_sj_system_user_permission_01" ON "public"."sj_system_user_permission" USING btree (
  "namespace_id" COLLATE "pg_catalog"."default" "pg_catalog"."text_ops" ASC NULLS LAST,
  "group_name" COLLATE "pg_catalog"."default" "pg_catalog"."text_ops" ASC NULLS LAST,
  "system_user_id" "pg_catalog"."int8_ops" ASC NULLS LAST
);

-- ----------------------------
-- Primary Key structure for table sj_system_user_permission
-- ----------------------------
ALTER TABLE "public"."sj_system_user_permission" ADD CONSTRAINT "sj_system_user_permission_pkey" PRIMARY KEY ("id");

-- ----------------------------
-- Indexes structure for table sj_workflow
-- ----------------------------
CREATE INDEX "idx_sj_workflow_01" ON "public"."sj_workflow" USING btree (
  "create_dt" "pg_catalog"."timestamp_ops" ASC NULLS LAST
);
CREATE INDEX "idx_sj_workflow_02" ON "public"."sj_workflow" USING btree (
  "namespace_id" COLLATE "pg_catalog"."default" "pg_catalog"."text_ops" ASC NULLS LAST,
  "group_name" COLLATE "pg_catalog"."default" "pg_catalog"."text_ops" ASC NULLS LAST
);

-- ----------------------------
-- Primary Key structure for table sj_workflow
-- ----------------------------
ALTER TABLE "public"."sj_workflow" ADD CONSTRAINT "sj_workflow_pkey" PRIMARY KEY ("id");

-- ----------------------------
-- Indexes structure for table sj_workflow_node
-- ----------------------------
CREATE INDEX "idx_sj_workflow_node_01" ON "public"."sj_workflow_node" USING btree (
  "create_dt" "pg_catalog"."timestamp_ops" ASC NULLS LAST
);
CREATE INDEX "idx_sj_workflow_node_02" ON "public"."sj_workflow_node" USING btree (
  "namespace_id" COLLATE "pg_catalog"."default" "pg_catalog"."text_ops" ASC NULLS LAST,
  "group_name" COLLATE "pg_catalog"."default" "pg_catalog"."text_ops" ASC NULLS LAST
);

-- ----------------------------
-- Primary Key structure for table sj_workflow_node
-- ----------------------------
ALTER TABLE "public"."sj_workflow_node" ADD CONSTRAINT "sj_workflow_node_pkey" PRIMARY KEY ("id");

-- ----------------------------
-- Indexes structure for table sj_workflow_task_batch
-- ----------------------------
CREATE INDEX "idx_sj_workflow_task_batch_01" ON "public"."sj_workflow_task_batch" USING btree (
  "workflow_id" "pg_catalog"."int8_ops" ASC NULLS LAST,
  "task_batch_status" "pg_catalog"."int2_ops" ASC NULLS LAST
);
CREATE INDEX "idx_sj_workflow_task_batch_02" ON "public"."sj_workflow_task_batch" USING btree (
  "create_dt" "pg_catalog"."timestamp_ops" ASC NULLS LAST
);
CREATE INDEX "idx_sj_workflow_task_batch_03" ON "public"."sj_workflow_task_batch" USING btree (
  "namespace_id" COLLATE "pg_catalog"."default" "pg_catalog"."text_ops" ASC NULLS LAST,
  "group_name" COLLATE "pg_catalog"."default" "pg_catalog"."text_ops" ASC NULLS LAST
);

-- ----------------------------
-- Primary Key structure for table sj_workflow_task_batch
-- ----------------------------
ALTER TABLE "public"."sj_workflow_task_batch" ADD CONSTRAINT "sj_workflow_task_batch_pkey" PRIMARY KEY ("id");
