/*
 Navicat Premium Dump SQL

 Source Server         : 127.0.0.1
 Source Server Type    : PostgreSQL
 Source Server Version : 170003 (170003)
 Source Host           : 127.0.0.1:5432
 Source Catalog        : kcloud_platform
 Source Schema         : public

 Target Server Type    : PostgreSQL
 Target Server Version : 170003 (170003)
 File Encoding         : 65001

 Date: 15/05/2025 11:26:07
*/

DROP TABLE IF EXISTS "public"."users";
CREATE TABLE "public"."users" (
"username" varchar(50) NOT NULL,
"password" varchar(500) NOT NULL,
"enabled" bool NOT NULL DEFAULT true
)
;
ALTER TABLE "public"."users" ADD CONSTRAINT "users_pkey" PRIMARY KEY ("username");
INSERT INTO "public"."users" VALUES ('laokou', '$2a$10$75WIn2J5FoX9F5wEBdFsL.0cKdv5h8QqBMKMWBABhWAxKB4TO8WZq', 'f');
INSERT INTO "public"."users" VALUES ('nacos', '$2a$10$oVX1zRtaql9Jbsyzaaovx.TU2M6Bw0ZpCbPYWOIED58d1ougzaFRm', 'f');

DROP TABLE IF EXISTS "public"."tenant_info";
CREATE TABLE "public"."tenant_info" (
"id" bigserial NOT NULL,
"kp" varchar(128)  NOT NULL,
"tenant_id" varchar(128)  DEFAULT '',
"tenant_name" varchar(128)  DEFAULT '',
"tenant_desc" varchar(256) ,
"create_source" varchar(32) ,
"gmt_create" int8 NOT NULL,
"gmt_modified" int8 NOT NULL
)
;
COMMENT ON COLUMN "public"."tenant_info"."id" IS 'id';
COMMENT ON COLUMN "public"."tenant_info"."kp" IS 'kp';
COMMENT ON COLUMN "public"."tenant_info"."tenant_id" IS 'tenant_id';
COMMENT ON COLUMN "public"."tenant_info"."tenant_name" IS 'tenant_name';
COMMENT ON COLUMN "public"."tenant_info"."tenant_desc" IS 'tenant_desc';
COMMENT ON COLUMN "public"."tenant_info"."create_source" IS 'create_source';
COMMENT ON COLUMN "public"."tenant_info"."gmt_create" IS '创建时间';
COMMENT ON COLUMN "public"."tenant_info"."gmt_modified" IS '修改时间';
COMMENT ON TABLE "public"."tenant_info" IS 'tenant_info';
CREATE UNIQUE INDEX "uk_tenant_info_kptenantid" ON "public"."tenant_info" USING btree (
"kp" ,
"tenant_id"
	);
ALTER TABLE "public"."tenant_info" ADD CONSTRAINT "tenant_info_pkey" PRIMARY KEY ("id");
INSERT INTO "public"."tenant_info" VALUES (1, '2', 'nacos-default-mcp', 'nacos-default-mcp', 'Nacos default AI MCP module.', 'nacos', 1747555499268, 1747555499268);
INSERT INTO "public"."tenant_info" VALUES (2, '1', '0dac1a68-2f01-40df-bd26-bf0cb199057a', 'dev', 'dev', 'nacos', 1716631648356, 1716631648356);
INSERT INTO "public"."tenant_info" VALUES (3, '1', 'a61abd4c-ef96-42a5-99a1-616adee531f3', 'test', 'test', 'nacos', 1673556960289, 1716628319164);
INSERT INTO "public"."tenant_info" VALUES (4, '1', '8140e92b-fb43-48f5-b63b-7506185206a5', 'prod', 'prod', 'nacos', 1716631657328, 1716631657328);

DROP TABLE IF EXISTS "public"."tenant_capacity";
CREATE TABLE "public"."tenant_capacity" (
"id" bigserial NOT NULL,
"tenant_id" varchar(128) NOT NULL DEFAULT '',
"quota" int8 NOT NULL DEFAULT 0,
"usage" int8 NOT NULL DEFAULT 0,
"max_size" int8 NOT NULL DEFAULT 0,
"max_aggr_count" int8 NOT NULL DEFAULT 0,
"max_aggr_size" int8 NOT NULL DEFAULT 0,
"max_history_count" int8 NOT NULL DEFAULT 0,
"gmt_create" timestamp(6) NOT NULL DEFAULT CURRENT_TIMESTAMP,
"gmt_modified" timestamp(6) NOT NULL DEFAULT CURRENT_TIMESTAMP
)
;
COMMENT ON COLUMN "public"."tenant_capacity"."id" IS '主键ID';
COMMENT ON COLUMN "public"."tenant_capacity"."tenant_id" IS 'Tenant ID';
COMMENT ON COLUMN "public"."tenant_capacity"."quota" IS '配额，0表示使用默认值';
COMMENT ON COLUMN "public"."tenant_capacity"."usage" IS '使用量';
COMMENT ON COLUMN "public"."tenant_capacity"."max_size" IS '单个配置大小上限，单位为字节，0表示使用默认值';
COMMENT ON COLUMN "public"."tenant_capacity"."max_aggr_count" IS '聚合子配置最大个数';
COMMENT ON COLUMN "public"."tenant_capacity"."max_aggr_size" IS '单个聚合数据的子配置大小上限，单位为字节，0表示使用默认值';
COMMENT ON COLUMN "public"."tenant_capacity"."max_history_count" IS '最大变更历史数量';
COMMENT ON COLUMN "public"."tenant_capacity"."gmt_create" IS '创建时间';
COMMENT ON COLUMN "public"."tenant_capacity"."gmt_modified" IS '修改时间';
COMMENT ON TABLE "public"."tenant_capacity" IS '租户容量信息表';
ALTER TABLE "public"."tenant_capacity" ADD CONSTRAINT "tenant_capacity_pkey" PRIMARY KEY ("id");
CREATE UNIQUE INDEX "uk_tenant_id" ON "public"."tenant_capacity" USING btree (
"tenant_id"
);

DROP TABLE IF EXISTS "public"."roles";
CREATE TABLE "public"."roles" (
"username" varchar(50)  NOT NULL,
"role" varchar(50)  NOT NULL
);
CREATE UNIQUE INDEX "uk_username_role" ON "public"."roles" USING btree (
"username",
"role"
	);
INSERT INTO "public"."roles" VALUES ('laokou', 'ADMIN');
INSERT INTO "public"."roles" VALUES ('nacos', 'ROLE_ADMIN');

DROP TABLE IF EXISTS "public"."permissions";
CREATE TABLE "public"."permissions" (
"role" varchar(50)  NOT NULL,
"resource" varchar(255)  NOT NULL,
"action" varchar(8)  NOT NULL
)
;
CREATE UNIQUE INDEX "uk_role_permission" ON "public"."permissions" USING btree (
"role",
"resource"  ,
"action"
	);
INSERT INTO "public"."permissions" VALUES ('ADMIN', ':*:*', 'rw');
INSERT INTO "public"."permissions" VALUES ('ADMIN', 'a61abd4c-ef96-42a5-99a1-616adee531f3:*:*', 'rw');
INSERT INTO "public"."permissions" VALUES ('ROLE_ADMIN', ':*:*', 'rw');
INSERT INTO "public"."permissions" VALUES ('ROLE_ADMIN', 'a61abd4c-ef96-42a5-99a1-616adee531f3:*:*', 'rw');

DROP TABLE IF EXISTS "public"."his_config_info";
CREATE TABLE "public"."his_config_info" (
"id" bigserial NOT NULL,
"nid" int8 NOT NULL GENERATED BY DEFAULT AS IDENTITY,
"data_id" varchar(255)  NOT NULL,
"group_id" varchar(128)  NOT NULL,
"app_name" varchar(128) ,
"content" text  NOT NULL,
"md5" varchar(32) ,
"gmt_create" timestamp(6) NOT NULL DEFAULT CURRENT_TIMESTAMP,
"gmt_modified" timestamp(6) NOT NULL DEFAULT CURRENT_TIMESTAMP,
"src_user" text ,
"src_ip" varchar(50) ,
"op_type" char(10) ,
"tenant_id" varchar(128)  DEFAULT '',
"encrypted_data_key" text  NOT NULL,
"publish_type" varchar(50)  NOT NULL default 'formal',
"gray_name" varchar(128) ,
"ext_info" text
)
;
COMMENT ON COLUMN "public"."his_config_info"."id" IS 'id';
COMMENT ON COLUMN "public"."his_config_info"."nid" IS 'nid';
COMMENT ON COLUMN "public"."his_config_info"."data_id" IS 'data_id';
COMMENT ON COLUMN "public"."his_config_info"."group_id" IS 'group_id';
COMMENT ON COLUMN "public"."his_config_info"."app_name" IS 'app_name';
COMMENT ON COLUMN "public"."his_config_info"."content" IS 'content';
COMMENT ON COLUMN "public"."his_config_info"."md5" IS 'md5';
COMMENT ON COLUMN "public"."his_config_info"."gmt_create" IS '创建时间';
COMMENT ON COLUMN "public"."his_config_info"."gmt_modified" IS '修改时间';
COMMENT ON COLUMN "public"."his_config_info"."src_user" IS 'source user';
COMMENT ON COLUMN "public"."his_config_info"."src_ip" IS 'source ip';
COMMENT ON COLUMN "public"."his_config_info"."op_type" IS 'operation type';
COMMENT ON COLUMN "public"."his_config_info"."tenant_id" IS '租户字段';
COMMENT ON COLUMN "public"."his_config_info"."encrypted_data_key" IS '秘钥';
COMMENT ON COLUMN "public"."his_config_info"."publish_type" IS 'publish type gray or formal';
COMMENT ON COLUMN "public"."his_config_info"."gray_name" IS 'gray name';
COMMENT ON COLUMN "public"."his_config_info"."ext_info" IS 'ext info';
COMMENT ON TABLE "public"."his_config_info" IS '多租户改造';
ALTER TABLE "public"."his_config_info" ADD CONSTRAINT "his_config_info_pkey" PRIMARY KEY ("nid");
CREATE INDEX "idx_did" ON "public"."his_config_info" USING btree (
"data_id"
	);
CREATE INDEX "idx_gmt_create" ON "public"."his_config_info" USING btree (
"gmt_create"
	);
CREATE INDEX "idx_gmt_modified" ON "public"."his_config_info" USING btree (
"gmt_modified"
	);

DROP TABLE IF EXISTS "public"."group_capacity";
CREATE TABLE "public"."group_capacity" (
"id" bigserial NOT NULL,
"group_id" varchar(128)  NOT NULL DEFAULT '',
"quota" int8 NOT NULL DEFAULT 0,
"usage" int8 NOT NULL DEFAULT 0,
"max_size" int8 NOT NULL DEFAULT 0,
"max_aggr_count" int8 NOT NULL DEFAULT 0,
"max_aggr_size" int8 NOT NULL DEFAULT 0,
"max_history_count" int8 NOT NULL DEFAULT 0,
"gmt_create" timestamp(6) NOT NULL DEFAULT CURRENT_TIMESTAMP,
"gmt_modified" timestamp(6) NOT NULL DEFAULT CURRENT_TIMESTAMP
)
;
COMMENT ON COLUMN "public"."group_capacity"."id" IS '主键ID';
COMMENT ON COLUMN "public"."group_capacity"."group_id" IS 'Group ID，空字符表示整个集群';
COMMENT ON COLUMN "public"."group_capacity"."quota" IS '配额，0表示使用默认值';
COMMENT ON COLUMN "public"."group_capacity"."usage" IS '使用量';
COMMENT ON COLUMN "public"."group_capacity"."max_size" IS '单个配置大小上限，单位为字节，0表示使用默认值';
COMMENT ON COLUMN "public"."group_capacity"."max_aggr_count" IS '聚合子配置最大个数，，0表示使用默认值';
COMMENT ON COLUMN "public"."group_capacity"."max_aggr_size" IS '单个聚合数据的子配置大小上限，单位为字节，0表示使用默认值';
COMMENT ON COLUMN "public"."group_capacity"."max_history_count" IS '最大变更历史数量';
COMMENT ON COLUMN "public"."group_capacity"."gmt_create" IS '创建时间';
COMMENT ON COLUMN "public"."group_capacity"."gmt_modified" IS '修改时间';
COMMENT ON TABLE "public"."group_capacity" IS '集群、各Group容量信息表';
ALTER TABLE "public"."group_capacity" ADD CONSTRAINT "group_capacity_pkey" PRIMARY KEY ("id");
CREATE UNIQUE INDEX "uk_group_id" ON "public"."group_capacity" USING btree (
"group_id"
);

DROP TABLE IF EXISTS "public"."config_tags_relation";
CREATE TABLE "public"."config_tags_relation" (
"id" bigserial NOT NULL,
"tag_name" varchar(128)  NOT NULL,
"tag_type" varchar(64) ,
"data_id" varchar(255)  NOT NULL,
"group_id" varchar(128)  NOT NULL,
"tenant_id" varchar(128)  DEFAULT '',
"nid" int8 NOT NULL GENERATED BY DEFAULT AS IDENTITY
)
;
COMMENT ON COLUMN "public"."config_tags_relation"."id" IS 'id';
COMMENT ON COLUMN "public"."config_tags_relation"."tag_name" IS 'tag_name';
COMMENT ON COLUMN "public"."config_tags_relation"."tag_type" IS 'tag_type';
COMMENT ON COLUMN "public"."config_tags_relation"."data_id" IS 'data_id';
COMMENT ON COLUMN "public"."config_tags_relation"."group_id" IS 'group_id';
COMMENT ON COLUMN "public"."config_tags_relation"."tenant_id" IS 'tenant_id';
COMMENT ON TABLE "public"."config_tags_relation" IS 'config_tag_relation';

CREATE UNIQUE INDEX "uk_configtagrelation_configidtag" ON "public"."config_tags_relation" USING btree (
	"id"  ,
	"tag_name"  ,
	"tag_type"
	);

ALTER TABLE "public"."config_tags_relation" ADD CONSTRAINT "config_tags_relation_pkey" PRIMARY KEY ("nid");

CREATE INDEX "idx_tenant_id" ON "public"."config_tags_relation" ("tenant_id");

DROP TABLE IF EXISTS "public"."config_info_aggr";
CREATE TABLE "public"."config_info_aggr" (
"id" bigserial NOT NULL,
"data_id" varchar(255)  NOT NULL,
"group_id" varchar(128)  NOT NULL,
"datum_id" varchar(255)  NOT NULL,
"content" text  NOT NULL,
"gmt_modified" timestamp(6) NOT NULL,
"app_name" varchar(128) ,
"tenant_id" varchar(128)  DEFAULT ''
)
;
COMMENT ON COLUMN "public"."config_info_aggr"."id" IS 'id';
COMMENT ON COLUMN "public"."config_info_aggr"."data_id" IS 'data_id';
COMMENT ON COLUMN "public"."config_info_aggr"."group_id" IS 'group_id';
COMMENT ON COLUMN "public"."config_info_aggr"."datum_id" IS 'datum_id';
COMMENT ON COLUMN "public"."config_info_aggr"."content" IS '内容';
COMMENT ON COLUMN "public"."config_info_aggr"."gmt_modified" IS '修改时间';
COMMENT ON COLUMN "public"."config_info_aggr"."tenant_id" IS '租户字段';
COMMENT ON TABLE "public"."config_info_aggr" IS '增加租户字段';
ALTER TABLE "public"."config_info_aggr" ADD CONSTRAINT "config_info_aggr_pkey" PRIMARY KEY ("id");
CREATE UNIQUE INDEX "config_info_aggr_data_id_group_id_tenant_id_datum_id_idx" ON "public"."config_info_aggr" USING btree (
 "data_id"  ,
 "group_id"  ,
 "tenant_id"  ,
 "datum_id"
	);

DROP TABLE IF EXISTS "public"."config_info";
CREATE TABLE "public"."config_info" (
"id" bigserial NOT NULL,
"data_id" varchar(255)  NOT NULL,
"group_id" varchar(128) ,
"content" text  NOT NULL,
"md5" varchar(32) ,
"gmt_create" timestamp(6) NOT NULL DEFAULT CURRENT_TIMESTAMP,
"gmt_modified" timestamp(6) NOT NULL DEFAULT CURRENT_TIMESTAMP,
"src_user" text ,
"src_ip" varchar(50) ,
"app_name" varchar(128) ,
"tenant_id" varchar(128)  DEFAULT '',
"c_desc" varchar(256) ,
"c_use" varchar(64) ,
"effect" varchar(64) ,
"type" varchar(64) ,
"c_schema" text ,
"encrypted_data_key" text  NOT NULL
)
;
COMMENT ON COLUMN "public"."config_info"."id" IS 'id';
COMMENT ON COLUMN "public"."config_info"."data_id" IS 'data_id';
COMMENT ON COLUMN "public"."config_info"."content" IS 'content';
COMMENT ON COLUMN "public"."config_info"."md5" IS 'md5';
COMMENT ON COLUMN "public"."config_info"."gmt_create" IS '创建时间';
COMMENT ON COLUMN "public"."config_info"."gmt_modified" IS '修改时间';
COMMENT ON COLUMN "public"."config_info"."src_user" IS 'source user';
COMMENT ON COLUMN "public"."config_info"."src_ip" IS 'source ip';
COMMENT ON COLUMN "public"."config_info"."app_name" IS 'app_name';
COMMENT ON COLUMN "public"."config_info"."tenant_id" IS '租户字段';
COMMENT ON COLUMN "public"."config_info"."c_desc" IS 'configuration description';
COMMENT ON COLUMN "public"."config_info"."c_use" IS 'configuration usage';
COMMENT ON COLUMN "public"."config_info"."effect" IS '配置生效的描述';
COMMENT ON COLUMN "public"."config_info"."type" IS '配置的类型';
COMMENT ON COLUMN "public"."config_info"."c_schema" IS '配置的模式';
COMMENT ON COLUMN "public"."config_info"."encrypted_data_key" IS '秘钥';
COMMENT ON TABLE "public"."config_info" IS 'config_info';

ALTER TABLE "public"."config_info" ADD CONSTRAINT "config_info_pkey" PRIMARY KEY ("id");
CREATE UNIQUE INDEX "uk_configinfo_datagrouptenant" ON "public"."config_info" USING btree (
"data_id"  ,
"group_id"  ,
"tenant_id"
	);

INSERT INTO "public"."config_info"  OVERRIDING SYSTEM VALUE VALUES  (20, 'admin-flow.json', 'IOT_GROUP', '[
  {
    "resource": "/api/v1/users",
    "limitApp": "default",
    "count": 500000,
    "grade": 1,
    "strategy": 0,
    "controlBehavior": 0
  },
  {
    "resource": "/api/v1/users/import",
    "limitApp": "default",
    "count": 100000,
    "grade": 1,
    "strategy": 0,
    "controlBehavior": 0
  },
  {
    "resource": "/api/v1/users/export",
    "limitApp": "default",
    "count": 100000,
    "grade": 1,
    "strategy": 0,
    "controlBehavior": 0
  },
  {
    "resource": "/api/v1/users/page",
    "limitApp": "default",
    "count": 100000,
    "grade": 1,
    "strategy": 0,
    "controlBehavior": 0
  },
  {
    "resource": "/api/v1/users/{id}",
    "limitApp": "default",
    "count": 100000,
    "grade": 1,
    "strategy": 0,
    "controlBehavior": 0
  },
  {
    "resource": "/api/v1/users/profile",
    "limitApp": "default",
    "count": 100000,
    "grade": 1,
    "strategy": 0,
    "controlBehavior": 0
  },
  {
    "resource": "/api/v1/tenants",
    "limitApp": "default",
    "count": 500000,
    "grade": 1,
    "strategy": 0,
    "controlBehavior": 0
  },
  {
    "resource": "/api/v1/tenants/import",
    "limitApp": "default",
    "count": 100000,
    "grade": 1,
    "strategy": 0,
    "controlBehavior": 0
  },
  {
    "resource": "/api/v1/tenants/export",
    "limitApp": "default",
    "count": 100000,
    "grade": 1,
    "strategy": 0,
    "controlBehavior": 0
  },
  {
    "resource": "/api/v1/tenants/page",
    "limitApp": "default",
    "count": 100000,
    "grade": 1,
    "strategy": 0,
    "controlBehavior": 0
  },
  {
    "resource": "/api/v1/tenants/{id}",
    "limitApp": "default",
    "count": 100000,
    "grade": 1,
    "strategy": 0,
    "controlBehavior": 0
  },
  {
    "resource": "/api/v1/sources",
    "limitApp": "default",
    "count": 500000,
    "grade": 1,
    "strategy": 0,
    "controlBehavior": 0
  },
  {
    "resource": "/api/v1/sources/import",
    "limitApp": "default",
    "count": 100000,
    "grade": 1,
    "strategy": 0,
    "controlBehavior": 0
  },
  {
    "resource": "/api/v1/sources/export",
    "limitApp": "default",
    "count": 100000,
    "grade": 1,
    "strategy": 0,
    "controlBehavior": 0
  },
  {
    "resource": "/api/v1/sources/page",
    "limitApp": "default",
    "count": 100000,
    "grade": 1,
    "strategy": 0,
    "controlBehavior": 0
  },
  {
    "resource": "/api/v1/sources/{id}",
    "limitApp": "default",
    "count": 100000,
    "grade": 1,
    "strategy": 0,
    "controlBehavior": 0
  },
  {
    "resource": "/api/v1/roles",
    "limitApp": "default",
    "count": 500000,
    "grade": 1,
    "strategy": 0,
    "controlBehavior": 0
  },
  {
    "resource": "/api/v1/roles/import",
    "limitApp": "default",
    "count": 100000,
    "grade": 1,
    "strategy": 0,
    "controlBehavior": 0
  },
  {
    "resource": "/api/v1/roles/export",
    "limitApp": "default",
    "count": 100000,
    "grade": 1,
    "strategy": 0,
    "controlBehavior": 0
  },
  {
    "resource": "/api/v1/roles/page",
    "limitApp": "default",
    "count": 100000,
    "grade": 1,
    "strategy": 0,
    "controlBehavior": 0
  },
  {
    "resource": "/api/v1/roles/{id}",
    "limitApp": "default",
    "count": 100000,
    "grade": 1,
    "strategy": 0,
    "controlBehavior": 0
  },
  {
    "resource": "/api/v1/oss-logs",
    "limitApp": "default",
    "count": 500000,
    "grade": 1,
    "strategy": 0,
    "controlBehavior": 0
  },
  {
    "resource": "/api/v1/oss-logs/import",
    "limitApp": "default",
    "count": 100000,
    "grade": 1,
    "strategy": 0,
    "controlBehavior": 0
  },
  {
    "resource": "/api/v1/oss-logs/export",
    "limitApp": "default",
    "count": 100000,
    "grade": 1,
    "strategy": 0,
    "controlBehavior": 0
  },
  {
    "resource": "/api/v1/oss-logs/page",
    "limitApp": "default",
    "count": 100000,
    "grade": 1,
    "strategy": 0,
    "controlBehavior": 0
  },
  {
    "resource": "/api/v1/oss-logs/{id}",
    "limitApp": "default",
    "count": 100000,
    "grade": 1,
    "strategy": 0,
    "controlBehavior": 0
  },
  {
    "resource": "/api/v1/oss",
    "limitApp": "default",
    "count": 500000,
    "grade": 1,
    "strategy": 0,
    "controlBehavior": 0
  },
  {
    "resource": "/api/v1/oss/import",
    "limitApp": "default",
    "count": 100000,
    "grade": 1,
    "strategy": 0,
    "controlBehavior": 0
  },
  {
    "resource": "/api/v1/oss/export",
    "limitApp": "default",
    "count": 100000,
    "grade": 1,
    "strategy": 0,
    "controlBehavior": 0
  },
  {
    "resource": "/api/v1/oss/page",
    "limitApp": "default",
    "count": 100000,
    "grade": 1,
    "strategy": 0,
    "controlBehavior": 0
  },
  {
    "resource": "/api/v1/oss/{id}",
    "limitApp": "default",
    "count": 100000,
    "grade": 1,
    "strategy": 0,
    "controlBehavior": 0
  },
  {
    "resource": "/api/v1/operate-logs",
    "limitApp": "default",
    "count": 500000,
    "grade": 1,
    "strategy": 0,
    "controlBehavior": 0
  },
  {
    "resource": "/api/v1/operate-logs/import",
    "limitApp": "default",
    "count": 100000,
    "grade": 1,
    "strategy": 0,
    "controlBehavior": 0
  },
  {
    "resource": "/api/v1/operate-logs/export",
    "limitApp": "default",
    "count": 100000,
    "grade": 1,
    "strategy": 0,
    "controlBehavior": 0
  },
  {
    "resource": "/api/v1/operate-logs/page",
    "limitApp": "default",
    "count": 100000,
    "grade": 1,
    "strategy": 0,
    "controlBehavior": 0
  },
  {
    "resource": "/api/v1/operate-logs/{id}",
    "limitApp": "default",
    "count": 100000,
    "grade": 1,
    "strategy": 0,
    "controlBehavior": 0
  },
  {
    "resource": "/api/v1/notice-logs",
    "limitApp": "default",
    "count": 500000,
    "grade": 1,
    "strategy": 0,
    "controlBehavior": 0
  },
  {
    "resource": "/api/v1/notice-logs/import",
    "limitApp": "default",
    "count": 100000,
    "grade": 1,
    "strategy": 0,
    "controlBehavior": 0
  },
  {
    "resource": "/api/v1/notice-logs/export",
    "limitApp": "default",
    "count": 100000,
    "grade": 1,
    "strategy": 0,
    "controlBehavior": 0
  },
  {
    "resource": "/api/v1/notice-logs/page",
    "limitApp": "default",
    "count": 100000,
    "grade": 1,
    "strategy": 0,
    "controlBehavior": 0
  },
  {
    "resource": "/api/v1/notice-logs/{id}",
    "limitApp": "default",
    "count": 100000,
    "grade": 1,
    "strategy": 0,
    "controlBehavior": 0
  },
  {
    "resource": "/api/v1/menus",
    "limitApp": "default",
    "count": 500000,
    "grade": 1,
    "strategy": 0,
    "controlBehavior": 0
  },
  {
    "resource": "/api/v1/menus/import",
    "limitApp": "default",
    "count": 100000,
    "grade": 1,
    "strategy": 0,
    "controlBehavior": 0
  },
  {
    "resource": "/api/v1/menus/export",
    "limitApp": "default",
    "count": 100000,
    "grade": 1,
    "strategy": 0,
    "controlBehavior": 0
  },
  {
    "resource": "/api/v1/menus/page",
    "limitApp": "default",
    "count": 100000,
    "grade": 1,
    "strategy": 0,
    "controlBehavior": 0
  },
  {
    "resource": "/api/v1/menus/{id}",
    "limitApp": "default",
    "count": 100000,
    "grade": 1,
    "strategy": 0,
    "controlBehavior": 0
  },
  {
    "resource": "/api/v1/login-log",
    "limitApp": "default",
    "count": 500000,
    "grade": 1,
    "strategy": 0,
    "controlBehavior": 0
  },
  {
    "resource": "/api/v1/login-log/import",
    "limitApp": "default",
    "count": 100000,
    "grade": 1,
    "strategy": 0,
    "controlBehavior": 0
  },
  {
    "resource": "/api/v1/login-log/export",
    "limitApp": "default",
    "count": 100000,
    "grade": 1,
    "strategy": 0,
    "controlBehavior": 0
  },
  {
    "resource": "/api/v1/login-log/page",
    "limitApp": "default",
    "count": 100000,
    "grade": 1,
    "strategy": 0,
    "controlBehavior": 0
  },
  {
    "resource": "/api/v1/login-log/{id}",
    "limitApp": "default",
    "count": 100000,
    "grade": 1,
    "strategy": 0,
    "controlBehavior": 0
  },
  {
    "resource": "/api/v1/login-log/all",
    "limitApp": "default",
    "count": 100000,
    "grade": 1,
    "strategy": 0,
    "controlBehavior": 0
  },
  {
    "resource": "/api/v1/i18n-menus",
    "limitApp": "default",
    "count": 500000,
    "grade": 1,
    "strategy": 0,
    "controlBehavior": 0
  },
  {
    "resource": "/api/v1/i18n-menus/import",
    "limitApp": "default",
    "count": 100000,
    "grade": 1,
    "strategy": 0,
    "controlBehavior": 0
  },
  {
    "resource": "/api/v1/i18n-menus/export",
    "limitApp": "default",
    "count": 100000,
    "grade": 1,
    "strategy": 0,
    "controlBehavior": 0
  },
  {
    "resource": "/api/v1/i18n-menus/page",
    "limitApp": "default",
    "count": 100000,
    "grade": 1,
    "strategy": 0,
    "controlBehavior": 0
  },
  {
    "resource": "/api/v1/i18n-menus/{id}",
    "limitApp": "default",
    "count": 100000,
    "grade": 1,
    "strategy": 0,
    "controlBehavior": 0
  },
  {
    "resource": "/api/v1/domain-events",
    "limitApp": "default",
    "count": 500000,
    "grade": 1,
    "strategy": 0,
    "controlBehavior": 0
  },
  {
    "resource": "/api/v1/domain-events/import",
    "limitApp": "default",
    "count": 100000,
    "grade": 1,
    "strategy": 0,
    "controlBehavior": 0
  },
  {
    "resource": "/api/v1/domain-events/export",
    "limitApp": "default",
    "count": 100000,
    "grade": 1,
    "strategy": 0,
    "controlBehavior": 0
  },
  {
    "resource": "/api/v1/domain-events/page",
    "limitApp": "default",
    "count": 100000,
    "grade": 1,
    "strategy": 0,
    "controlBehavior": 0
  },
  {
    "resource": "/api/v1/domain-events/{id}",
    "limitApp": "default",
    "count": 100000,
    "grade": 1,
    "strategy": 0,
    "controlBehavior": 0
  },
  {
    "resource": "/api/v1/dicts",
    "limitApp": "default",
    "count": 500000,
    "grade": 1,
    "strategy": 0,
    "controlBehavior": 0
  },
  {
    "resource": "/api/v1/dicts/import",
    "limitApp": "default",
    "count": 100000,
    "grade": 1,
    "strategy": 0,
    "controlBehavior": 0
  },
  {
    "resource": "/api/v1/dicts/export",
    "limitApp": "default",
    "count": 100000,
    "grade": 1,
    "strategy": 0,
    "controlBehavior": 0
  },
  {
    "resource": "/api/v1/dicts/page",
    "limitApp": "default",
    "count": 100000,
    "grade": 1,
    "strategy": 0,
    "controlBehavior": 0
  },
  {
    "resource": "/api/v1/dicts/{id}",
    "limitApp": "default",
    "count": 100000,
    "grade": 1,
    "strategy": 0,
    "controlBehavior": 0
  },
  {
    "resource": "/api/v1/dict-items",
    "limitApp": "default",
    "count": 500000,
    "grade": 1,
    "strategy": 0,
    "controlBehavior": 0
  },
  {
    "resource": "/api/v1/dict-items/import",
    "limitApp": "default",
    "count": 100000,
    "grade": 1,
    "strategy": 0,
    "controlBehavior": 0
  },
  {
    "resource": "/api/v1/dict-items/export",
    "limitApp": "default",
    "count": 100000,
    "grade": 1,
    "strategy": 0,
    "controlBehavior": 0
  },
  {
    "resource": "/api/v1/dict-items/page",
    "limitApp": "default",
    "count": 100000,
    "grade": 1,
    "strategy": 0,
    "controlBehavior": 0
  },
  {
    "resource": "/api/v1/dict-items/{id}",
    "limitApp": "default",
    "count": 100000,
    "grade": 1,
    "strategy": 0,
    "controlBehavior": 0
  },
  {
    "resource": "/api/v1/depts",
    "limitApp": "default",
    "count": 500000,
    "grade": 1,
    "strategy": 0,
    "controlBehavior": 0
  },
  {
    "resource": "/api/v1/depts/import",
    "limitApp": "default",
    "count": 100000,
    "grade": 1,
    "strategy": 0,
    "controlBehavior": 0
  },
  {
    "resource": "/api/v1/depts/export",
    "limitApp": "default",
    "count": 100000,
    "grade": 1,
    "strategy": 0,
    "controlBehavior": 0
  },
  {
    "resource": "/api/v1/depts/page",
    "limitApp": "default",
    "count": 100000,
    "grade": 1,
    "strategy": 0,
    "controlBehavior": 0
  },
  {
    "resource": "/api/v1/depts/{id}",
    "limitApp": "default",
    "count": 100000,
    "grade": 1,
    "strategy": 0,
    "controlBehavior": 0
  },
  {
    "resource": "/api/v1/clusters",
    "limitApp": "default",
    "count": 500000,
    "grade": 1,
    "strategy": 0,
    "controlBehavior": 0
  },
  {
    "resource": "/api/v1/clusters/import",
    "limitApp": "default",
    "count": 100000,
    "grade": 1,
    "strategy": 0,
    "controlBehavior": 0
  },
  {
    "resource": "/api/v1/clusters/export",
    "limitApp": "default",
    "count": 100000,
    "grade": 1,
    "strategy": 0,
    "controlBehavior": 0
  },
  {
    "resource": "/api/v1/clusters/page",
    "limitApp": "default",
    "count": 100000,
    "grade": 1,
    "strategy": 0,
    "controlBehavior": 0
  },
  {
    "resource": "/api/v1/clusters/{id}",
    "limitApp": "default",
    "count": 100000,
    "grade": 1,
    "strategy": 0,
    "controlBehavior": 0
  }
]
', '546e2d9817dd516db763f5ef3ef3a756', '2024-11-12 11:21:13.251', '2024-11-12 11:22:10.475', 'nacos', '0:0:0:0:0:0:0:1', 'laokou-admin', '8140e92b-fb43-48f5-b63b-7506185206a5', 'admin sentinel flow rule', '', '', 'json', '', '');
INSERT INTO "public"."config_info"  OVERRIDING SYSTEM VALUE VALUES  (11, 'gateway-flow.json', 'IOT_GROUP', '[
  {
    "resource": "laokou-auth",
    "grade": 1,
    "count": 100000,
    "intervalSec": 1,
    "burst": 500000,
    "controlBehavior": 0
  },
  {
    "resource": "laokou-admin",
    "grade": 1,
    "count": 100000,
    "intervalSec": 1,
    "burst": 500000,
    "controlBehavior": 0
  },
  {
    "resource": "laokou-iot",
    "grade": 1,
    "count": 100000,
    "intervalSec": 1,
    "burst": 500000,
    "controlBehavior": 0
  },
  {
    "resource": "laokou-generator",
    "grade": 1,
    "count": 100000,
    "intervalSec": 1,
    "burst": 500000,
    "controlBehavior": 0
  }
]', '5e013d830e3af21e5e321c0f4910fda0', '2024-05-25 18:12:47.358', '2024-11-11 20:51:44.543', 'nacos', '0:0:0:0:0:0:0:1', 'laokou-gateway', '0dac1a68-2f01-40df-bd26-bf0cb199057a', 'gateway sentinel flow rule', '', '', 'json', '', '');
INSERT INTO "public"."config_info"  OVERRIDING SYSTEM VALUE VALUES  (38, 'auth-flow.json', 'IOT_GROUP', '[
  {
    "resource": "/api/v1/username-password/captchas/{uuid}",
    "limitApp": "default",
    "count": 100000,
    "grade": 1,
    "strategy": 0,
    "controlBehavior": 0
  },
  {
    "resource": "/api/v1/captchas/send-mobile",
    "limitApp": "default",
    "count": 100000,
    "grade": 1,
    "strategy": 0,
    "controlBehavior": 0
  },
    {
    "resource": "/api/v1/captchas/send-mail",
    "limitApp": "default",
    "count": 100000,
    "grade": 1,
    "strategy": 0,
    "controlBehavior": 0
  },
  {
    "resource": "/api/v1/secrets",
    "limitApp": "default",
    "count": 100000,
    "grade": 1,
    "strategy": 0,
    "controlBehavior": 0
  },
  {
    "resource": "/api/v1/tokens",
    "limitApp": "default",
    "count": 100000,
    "grade": 1,
    "strategy": 0,
    "controlBehavior": 0
  }
]
', '2bb6ed92c98b4a1693426dadf1c96a30', '2024-11-11 21:17:37.578', '2024-11-11 21:18:17.553', 'nacos', '0:0:0:0:0:0:0:1', 'laokou-auth', '8140e92b-fb43-48f5-b63b-7506185206a5', 'auth sentinel flow rule', '', '', 'json', '', '');
INSERT INTO "public"."config_info"  OVERRIDING SYSTEM VALUE VALUES (39, 'auth-flow.json', 'IOT_GROUP', '[
  {
    "resource": "/api/v1/username-password/captchas/{uuid}",
    "limitApp": "default",
    "count": 100000,
    "grade": 1,
    "strategy": 0,
    "controlBehavior": 0
  },
  {
    "resource": "/api/v1/captchas/send-mobile",
    "limitApp": "default",
    "count": 100000,
    "grade": 1,
    "strategy": 0,
    "controlBehavior": 0
  },
    {
    "resource": "/api/v1/captchas/send-mail",
    "limitApp": "default",
    "count": 100000,
    "grade": 1,
    "strategy": 0,
    "controlBehavior": 0
  },
  {
    "resource": "/api/v1/secrets",
    "limitApp": "default",
    "count": 100000,
    "grade": 1,
    "strategy": 0,
    "controlBehavior": 0
  },
  {
    "resource": "/api/v1/tokens",
    "limitApp": "default",
    "count": 100000,
    "grade": 1,
    "strategy": 0,
    "controlBehavior": 0
  }
]
', '2bb6ed92c98b4a1693426dadf1c96a30', '2024-11-11 21:16:13.23', '2024-11-11 21:17:25.382', 'nacos', '0:0:0:0:0:0:0:1', 'laokou-auth', 'a61abd4c-ef96-42a5-99a1-616adee531f3', 'auth sentinel flow rule', '', '', 'json', '', '');
INSERT INTO "public"."config_info"  OVERRIDING SYSTEM VALUE VALUES  (40, 'admin-flow.json', 'IOT_GROUP', '[
  {
    "resource": "/api/v1/users",
    "limitApp": "default",
    "count": 500000,
    "grade": 1,
    "strategy": 0,
    "controlBehavior": 0
  },
  {
    "resource": "/api/v1/users/import",
    "limitApp": "default",
    "count": 100000,
    "grade": 1,
    "strategy": 0,
    "controlBehavior": 0
  },
  {
    "resource": "/api/v1/users/export",
    "limitApp": "default",
    "count": 100000,
    "grade": 1,
    "strategy": 0,
    "controlBehavior": 0
  },
  {
    "resource": "/api/v1/users/page",
    "limitApp": "default",
    "count": 100000,
    "grade": 1,
    "strategy": 0,
    "controlBehavior": 0
  },
  {
    "resource": "/api/v1/users/{id}",
    "limitApp": "default",
    "count": 100000,
    "grade": 1,
    "strategy": 0,
    "controlBehavior": 0
  },
  {
    "resource": "/api/v1/users/profile",
    "limitApp": "default",
    "count": 100000,
    "grade": 1,
    "strategy": 0,
    "controlBehavior": 0
  },
  {
    "resource": "/api/v1/tenants",
    "limitApp": "default",
    "count": 500000,
    "grade": 1,
    "strategy": 0,
    "controlBehavior": 0
  },
  {
    "resource": "/api/v1/tenants/import",
    "limitApp": "default",
    "count": 100000,
    "grade": 1,
    "strategy": 0,
    "controlBehavior": 0
  },
  {
    "resource": "/api/v1/tenants/export",
    "limitApp": "default",
    "count": 100000,
    "grade": 1,
    "strategy": 0,
    "controlBehavior": 0
  },
  {
    "resource": "/api/v1/sources",
    "limitApp": "default",
    "count": 500000,
    "grade": 1,
    "strategy": 0,
    "controlBehavior": 0
  },
  {
    "resource": "/api/v1/sources/import",
    "limitApp": "default",
    "count": 100000,
    "grade": 1,
    "strategy": 0,
    "controlBehavior": 0
  },
  {
    "resource": "/api/v1/sources/export",
    "limitApp": "default",
    "count": 100000,
    "grade": 1,
    "strategy": 0,
    "controlBehavior": 0
  },
  {
    "resource": "/api/v1/sources/page",
    "limitApp": "default",
    "count": 100000,
    "grade": 1,
    "strategy": 0,
    "controlBehavior": 0
  },
  {
    "resource": "/api/v1/sources/{id}",
    "limitApp": "default",
    "count": 100000,
    "grade": 1,
    "strategy": 0,
    "controlBehavior": 0
  },
  {
    "resource": "/api/v1/roles",
    "limitApp": "default",
    "count": 500000,
    "grade": 1,
    "strategy": 0,
    "controlBehavior": 0
  },
  {
    "resource": "/api/v1/roles/import",
    "limitApp": "default",
    "count": 100000,
    "grade": 1,
    "strategy": 0,
    "controlBehavior": 0
  },
  {
    "resource": "/api/v1/roles/export",
    "limitApp": "default",
    "count": 100000,
    "grade": 1,
    "strategy": 0,
    "controlBehavior": 0
  },
  {
    "resource": "/api/v1/roles/page",
    "limitApp": "default",
    "count": 100000,
    "grade": 1,
    "strategy": 0,
    "controlBehavior": 0
  },
  {
    "resource": "/api/v1/roles/{id}",
    "limitApp": "default",
    "count": 100000,
    "grade": 1,
    "strategy": 0,
    "controlBehavior": 0
  },
  {
    "resource": "/api/v1/oss-logs",
    "limitApp": "default",
    "count": 500000,
    "grade": 1,
    "strategy": 0,
    "controlBehavior": 0
  },
  {
    "resource": "/api/v1/oss-logs/import",
    "limitApp": "default",
    "count": 100000,
    "grade": 1,
    "strategy": 0,
    "controlBehavior": 0
  },
  {
    "resource": "/api/v1/oss-logs/export",
    "limitApp": "default",
    "count": 100000,
    "grade": 1,
    "strategy": 0,
    "controlBehavior": 0
  },
  {
    "resource": "/api/v1/oss-logs/page",
    "limitApp": "default",
    "count": 100000,
    "grade": 1,
    "strategy": 0,
    "controlBehavior": 0
  },
  {
    "resource": "/api/v1/oss-logs/{id}",
    "limitApp": "default",
    "count": 100000,
    "grade": 1,
    "strategy": 0,
    "controlBehavior": 0
  },
  {
    "resource": "/api/v1/oss",
    "limitApp": "default",
    "count": 500000,
    "grade": 1,
    "strategy": 0,
    "controlBehavior": 0
  },
  {
    "resource": "/api/v1/oss/import",
    "limitApp": "default",
    "count": 100000,
    "grade": 1,
    "strategy": 0,
    "controlBehavior": 0
  },
  {
    "resource": "/api/v1/oss/export",
    "limitApp": "default",
    "count": 100000,
    "grade": 1,
    "strategy": 0,
    "controlBehavior": 0
  },
  {
    "resource": "/api/v1/oss/page",
    "limitApp": "default",
    "count": 100000,
    "grade": 1,
    "strategy": 0,
    "controlBehavior": 0
  },
  {
    "resource": "/api/v1/oss/{id}",
    "limitApp": "default",
    "count": 100000,
    "grade": 1,
    "strategy": 0,
    "controlBehavior": 0
  },
  {
    "resource": "/api/v1/operate-logs",
    "limitApp": "default",
    "count": 500000,
    "grade": 1,
    "strategy": 0,
    "controlBehavior": 0
  },
  {
    "resource": "/api/v1/operate-logs/import",
    "limitApp": "default",
    "count": 100000,
    "grade": 1,
    "strategy": 0,
    "controlBehavior": 0
  },
  {
    "resource": "/api/v1/operate-logs/export",
    "limitApp": "default",
    "count": 100000,
    "grade": 1,
    "strategy": 0,
    "controlBehavior": 0
  },
  {
    "resource": "/api/v1/operate-logs/page",
    "limitApp": "default",
    "count": 100000,
    "grade": 1,
    "strategy": 0,
    "controlBehavior": 0
  },
  {
    "resource": "/api/v1/operate-logs/{id}",
    "limitApp": "default",
    "count": 100000,
    "grade": 1,
    "strategy": 0,
    "controlBehavior": 0
  },
  {
    "resource": "/api/v1/notice-logs",
    "limitApp": "default",
    "count": 500000,
    "grade": 1,
    "strategy": 0,
    "controlBehavior": 0
  },
  {
    "resource": "/api/v1/notice-logs/import",
    "limitApp": "default",
    "count": 100000,
    "grade": 1,
    "strategy": 0,
    "controlBehavior": 0
  },
  {
    "resource": "/api/v1/notice-logs/export",
    "limitApp": "default",
    "count": 100000,
    "grade": 1,
    "strategy": 0,
    "controlBehavior": 0
  },
  {
    "resource": "/api/v1/notice-logs/page",
    "limitApp": "default",
    "count": 100000,
    "grade": 1,
    "strategy": 0,
    "controlBehavior": 0
  },
  {
    "resource": "/api/v1/notice-logs/{id}",
    "limitApp": "default",
    "count": 100000,
    "grade": 1,
    "strategy": 0,
    "controlBehavior": 0
  },
  {
    "resource": "/api/v1/menus",
    "limitApp": "default",
    "count": 500000,
    "grade": 1,
    "strategy": 0,
    "controlBehavior": 0
  },
  {
    "resource": "/api/v1/menus/import",
    "limitApp": "default",
    "count": 100000,
    "grade": 1,
    "strategy": 0,
    "controlBehavior": 0
  },
  {
    "resource": "/api/v1/menus/export",
    "limitApp": "default",
    "count": 100000,
    "grade": 1,
    "strategy": 0,
    "controlBehavior": 0
  },
  {
    "resource": "/api/v1/menus/page",
    "limitApp": "default",
    "count": 100000,
    "grade": 1,
    "strategy": 0,
    "controlBehavior": 0
  },
  {
    "resource": "/api/v1/menus/{id}",
    "limitApp": "default",
    "count": 100000,
    "grade": 1,
    "strategy": 0,
    "controlBehavior": 0
  },
  {
    "resource": "/api/v1/login-log",
    "limitApp": "default",
    "count": 500000,
    "grade": 1,
    "strategy": 0,
    "controlBehavior": 0
  },
  {
    "resource": "/api/v1/login-log/import",
    "limitApp": "default",
    "count": 100000,
    "grade": 1,
    "strategy": 0,
    "controlBehavior": 0
  },
  {
    "resource": "/api/v1/login-log/export",
    "limitApp": "default",
    "count": 100000,
    "grade": 1,
    "strategy": 0,
    "controlBehavior": 0
  },
  {
    "resource": "/api/v1/login-log/page",
    "limitApp": "default",
    "count": 100000,
    "grade": 1,
    "strategy": 0,
    "controlBehavior": 0
  },
  {
    "resource": "/api/v1/login-log/{id}",
    "limitApp": "default",
    "count": 100000,
    "grade": 1,
    "strategy": 0,
    "controlBehavior": 0
  },
  {
    "resource": "/api/v1/login-log/all",
    "limitApp": "default",
    "count": 100000,
    "grade": 1,
    "strategy": 0,
    "controlBehavior": 0
  },
  {
    "resource": "/api/v1/i18n-menus",
    "limitApp": "default",
    "count": 500000,
    "grade": 1,
    "strategy": 0,
    "controlBehavior": 0
  },
  {
    "resource": "/api/v1/i18n-menus/import",
    "limitApp": "default",
    "count": 100000,
    "grade": 1,
    "strategy": 0,
    "controlBehavior": 0
  },
  {
    "resource": "/api/v1/i18n-menus/export",
    "limitApp": "default",
    "count": 100000,
    "grade": 1,
    "strategy": 0,
    "controlBehavior": 0
  },
  {
    "resource": "/api/v1/i18n-menus/page",
    "limitApp": "default",
    "count": 100000,
    "grade": 1,
    "strategy": 0,
    "controlBehavior": 0
  },
  {
    "resource": "/api/v1/i18n-menus/{id}",
    "limitApp": "default",
    "count": 100000,
    "grade": 1,
    "strategy": 0,
    "controlBehavior": 0
  },
  {
    "resource": "/api/v1/domain-events",
    "limitApp": "default",
    "count": 500000,
    "grade": 1,
    "strategy": 0,
    "controlBehavior": 0
  },
  {
    "resource": "/api/v1/domain-events/import",
    "limitApp": "default",
    "count": 100000,
    "grade": 1,
    "strategy": 0,
    "controlBehavior": 0
  },
  {
    "resource": "/api/v1/domain-events/export",
    "limitApp": "default",
    "count": 100000,
    "grade": 1,
    "strategy": 0,
    "controlBehavior": 0
  },
  {
    "resource": "/api/v1/domain-events/page",
    "limitApp": "default",
    "count": 100000,
    "grade": 1,
    "strategy": 0,
    "controlBehavior": 0
  },
  {
    "resource": "/api/v1/domain-events/{id}",
    "limitApp": "default",
    "count": 100000,
    "grade": 1,
    "strategy": 0,
    "controlBehavior": 0
  },
  {
    "resource": "/api/v1/dicts",
    "limitApp": "default",
    "count": 500000,
    "grade": 1,
    "strategy": 0,
    "controlBehavior": 0
  },
  {
    "resource": "/api/v1/dicts/import",
    "limitApp": "default",
    "count": 100000,
    "grade": 1,
    "strategy": 0,
    "controlBehavior": 0
  },
  {
    "resource": "/api/v1/dicts/export",
    "limitApp": "default",
    "count": 100000,
    "grade": 1,
    "strategy": 0,
    "controlBehavior": 0
  },
  {
    "resource": "/api/v1/dicts/page",
    "limitApp": "default",
    "count": 100000,
    "grade": 1,
    "strategy": 0,
    "controlBehavior": 0
  },
  {
    "resource": "/api/v1/dicts/{id}",
    "limitApp": "default",
    "count": 100000,
    "grade": 1,
    "strategy": 0,
    "controlBehavior": 0
  },
  {
    "resource": "/api/v1/dict-items",
    "limitApp": "default",
    "count": 500000,
    "grade": 1,
    "strategy": 0,
    "controlBehavior": 0
  },
  {
    "resource": "/api/v1/dict-items/import",
    "limitApp": "default",
    "count": 100000,
    "grade": 1,
    "strategy": 0,
    "controlBehavior": 0
  },
  {
    "resource": "/api/v1/dict-items/export",
    "limitApp": "default",
    "count": 100000,
    "grade": 1,
    "strategy": 0,
    "controlBehavior": 0
  },
  {
    "resource": "/api/v1/dict-items/page",
    "limitApp": "default",
    "count": 100000,
    "grade": 1,
    "strategy": 0,
    "controlBehavior": 0
  },
  {
    "resource": "/api/v1/dict-items/{id}",
    "limitApp": "default",
    "count": 100000,
    "grade": 1,
    "strategy": 0,
    "controlBehavior": 0
  },
  {
    "resource": "/api/v1/depts",
    "limitApp": "default",
    "count": 500000,
    "grade": 1,
    "strategy": 0,
    "controlBehavior": 0
  },
  {
    "resource": "/api/v1/depts/import",
    "limitApp": "default",
    "count": 100000,
    "grade": 1,
    "strategy": 0,
    "controlBehavior": 0
  },
  {
    "resource": "/api/v1/depts/export",
    "limitApp": "default",
    "count": 100000,
    "grade": 1,
    "strategy": 0,
    "controlBehavior": 0
  },
  {
    "resource": "/api/v1/depts/page",
    "limitApp": "default",
    "count": 100000,
    "grade": 1,
    "strategy": 0,
    "controlBehavior": 0
  },
  {
    "resource": "/api/v1/depts/{id}",
    "limitApp": "default",
    "count": 100000,
    "grade": 1,
    "strategy": 0,
    "controlBehavior": 0
  },
  {
    "resource": "/api/v1/clusters",
    "limitApp": "default",
    "count": 500000,
    "grade": 1,
    "strategy": 0,
    "controlBehavior": 0
  },
  {
    "resource": "/api/v1/clusters/import",
    "limitApp": "default",
    "count": 100000,
    "grade": 1,
    "strategy": 0,
    "controlBehavior": 0
  },
  {
    "resource": "/api/v1/clusters/export",
    "limitApp": "default",
    "count": 100000,
    "grade": 1,
    "strategy": 0,
    "controlBehavior": 0
  },
  {
    "resource": "/api/v1/clusters/page",
    "limitApp": "default",
    "count": 100000,
    "grade": 1,
    "strategy": 0,
    "controlBehavior": 0
  },
  {
    "resource": "/api/v1/clusters/{id}",
    "limitApp": "default",
    "count": 100000,
    "grade": 1,
    "strategy": 0,
    "controlBehavior": 0
  }
]
', '546e2d9817dd516db763f5ef3ef3a756', '2024-11-11 21:19:54.557', '2024-11-12 11:13:47.766', 'nacos', '0:0:0:0:0:0:0:1', 'laokou-admin', 'a61abd4c-ef96-42a5-99a1-616adee531f3', 'admin sentinel flow rule', '', '', 'json', '', '');
INSERT INTO "public"."config_info"  OVERRIDING SYSTEM VALUE VALUES  (41, 'admin-flow.json', 'IOT_GROUP', '[
  {
    "resource": "/api/v1/users",
    "limitApp": "default",
    "count": 500000,
    "grade": 1,
    "strategy": 0,
    "controlBehavior": 0
  },
  {
    "resource": "/api/v1/users/import",
    "limitApp": "default",
    "count": 100000,
    "grade": 1,
    "strategy": 0,
    "controlBehavior": 0
  },
  {
    "resource": "/api/v1/users/export",
    "limitApp": "default",
    "count": 100000,
    "grade": 1,
    "strategy": 0,
    "controlBehavior": 0
  },
  {
    "resource": "/api/v1/users/page",
    "limitApp": "default",
    "count": 100000,
    "grade": 1,
    "strategy": 0,
    "controlBehavior": 0
  },
  {
    "resource": "/api/v1/users/{id}",
    "limitApp": "default",
    "count": 100000,
    "grade": 1,
    "strategy": 0,
    "controlBehavior": 0
  },
  {
    "resource": "/api/v1/users/profile",
    "limitApp": "default",
    "count": 100000,
    "grade": 1,
    "strategy": 0,
    "controlBehavior": 0
  },
  {
    "resource": "/api/v1/tenants",
    "limitApp": "default",
    "count": 500000,
    "grade": 1,
    "strategy": 0,
    "controlBehavior": 0
  },
  {
    "resource": "/api/v1/tenants/import",
    "limitApp": "default",
    "count": 100000,
    "grade": 1,
    "strategy": 0,
    "controlBehavior": 0
  },
  {
    "resource": "/api/v1/tenants/export",
    "limitApp": "default",
    "count": 100000,
    "grade": 1,
    "strategy": 0,
    "controlBehavior": 0
  },
  {
    "resource": "/api/v1/tenants/page",
    "limitApp": "default",
    "count": 100000,
    "grade": 1,
    "strategy": 0,
    "controlBehavior": 0
  },
  {
    "resource": "/api/v1/tenants/{id}",
    "limitApp": "default",
    "count": 100000,
    "grade": 1,
    "strategy": 0,
    "controlBehavior": 0
  },
  {
    "resource": "/api/v1/sources",
    "limitApp": "default",
    "count": 500000,
    "grade": 1,
    "strategy": 0,
    "controlBehavior": 0
  },
  {
    "resource": "/api/v1/sources/import",
    "limitApp": "default",
    "count": 100000,
    "grade": 1,
    "strategy": 0,
    "controlBehavior": 0
  },
  {
    "resource": "/api/v1/sources/export",
    "limitApp": "default",
    "count": 100000,
    "grade": 1,
    "strategy": 0,
    "controlBehavior": 0
  },
  {
    "resource": "/api/v1/sources/page",
    "limitApp": "default",
    "count": 100000,
    "grade": 1,
    "strategy": 0,
    "controlBehavior": 0
  },
  {
    "resource": "/api/v1/sources/{id}",
    "limitApp": "default",
    "count": 100000,
    "grade": 1,
    "strategy": 0,
    "controlBehavior": 0
  },
  {
    "resource": "/api/v1/roles",
    "limitApp": "default",
    "count": 500000,
    "grade": 1,
    "strategy": 0,
    "controlBehavior": 0
  },
  {
    "resource": "/api/v1/roles/import",
    "limitApp": "default",
    "count": 100000,
    "grade": 1,
    "strategy": 0,
    "controlBehavior": 0
  },
  {
    "resource": "/api/v1/roles/export",
    "limitApp": "default",
    "count": 100000,
    "grade": 1,
    "strategy": 0,
    "controlBehavior": 0
  },
  {
    "resource": "/api/v1/roles/page",
    "limitApp": "default",
    "count": 100000,
    "grade": 1,
    "strategy": 0,
    "controlBehavior": 0
  },
  {
    "resource": "/api/v1/roles/{id}",
    "limitApp": "default",
    "count": 100000,
    "grade": 1,
    "strategy": 0,
    "controlBehavior": 0
  },
  {
    "resource": "/api/v1/oss-logs",
    "limitApp": "default",
    "count": 500000,
    "grade": 1,
    "strategy": 0,
    "controlBehavior": 0
  },
  {
    "resource": "/api/v1/oss-logs/import",
    "limitApp": "default",
    "count": 100000,
    "grade": 1,
    "strategy": 0,
    "controlBehavior": 0
  },
  {
    "resource": "/api/v1/oss-logs/export",
    "limitApp": "default",
    "count": 100000,
    "grade": 1,
    "strategy": 0,
    "controlBehavior": 0
  },
  {
    "resource": "/api/v1/oss-logs/page",
    "limitApp": "default",
    "count": 100000,
    "grade": 1,
    "strategy": 0,
    "controlBehavior": 0
  },
  {
    "resource": "/api/v1/oss-logs/{id}",
    "limitApp": "default",
    "count": 100000,
    "grade": 1,
    "strategy": 0,
    "controlBehavior": 0
  },
  {
    "resource": "/api/v1/oss",
    "limitApp": "default",
    "count": 500000,
    "grade": 1,
    "strategy": 0,
    "controlBehavior": 0
  },
  {
    "resource": "/api/v1/oss/import",
    "limitApp": "default",
    "count": 100000,
    "grade": 1,
    "strategy": 0,
    "controlBehavior": 0
  },
  {
    "resource": "/api/v1/oss/export",
    "limitApp": "default",
    "count": 100000,
    "grade": 1,
    "strategy": 0,
    "controlBehavior": 0
  },
  {
    "resource": "/api/v1/oss/page",
    "limitApp": "default",
    "count": 100000,
    "grade": 1,
    "strategy": 0,
    "controlBehavior": 0
  },
  {
    "resource": "/api/v1/oss/{id}",
    "limitApp": "default",
    "count": 100000,
    "grade": 1,
    "strategy": 0,
    "controlBehavior": 0
  },
  {
    "resource": "/api/v1/operate-logs",
    "limitApp": "default",
    "count": 500000,
    "grade": 1,
    "strategy": 0,
    "controlBehavior": 0
  },
  {
    "resource": "/api/v1/operate-logs/import",
    "limitApp": "default",
    "count": 100000,
    "grade": 1,
    "strategy": 0,
    "controlBehavior": 0
  },
  {
    "resource": "/api/v1/operate-logs/export",
    "limitApp": "default",
    "count": 100000,
    "grade": 1,
    "strategy": 0,
    "controlBehavior": 0
  },
  {
    "resource": "/api/v1/operate-logs/page",
    "limitApp": "default",
    "count": 100000,
    "grade": 1,
    "strategy": 0,
    "controlBehavior": 0
  },
  {
    "resource": "/api/v1/operate-logs/{id}",
    "limitApp": "default",
    "count": 100000,
    "grade": 1,
    "strategy": 0,
    "controlBehavior": 0
  },
  {
    "resource": "/api/v1/notice-logs",
    "limitApp": "default",
    "count": 500000,
    "grade": 1,
    "strategy": 0,
    "controlBehavior": 0
  },
  {
    "resource": "/api/v1/notice-logs/import",
    "limitApp": "default",
    "count": 100000,
    "grade": 1,
    "strategy": 0,
    "controlBehavior": 0
  },
  {
    "resource": "/api/v1/notice-logs/export",
    "limitApp": "default",
    "count": 100000,
    "grade": 1,
    "strategy": 0,
    "controlBehavior": 0
  },
  {
    "resource": "/api/v1/notice-logs/page",
    "limitApp": "default",
    "count": 100000,
    "grade": 1,
    "strategy": 0,
    "controlBehavior": 0
  },
  {
    "resource": "/api/v1/notice-logs/{id}",
    "limitApp": "default",
    "count": 100000,
    "grade": 1,
    "strategy": 0,
    "controlBehavior": 0
  },
  {
    "resource": "/api/v1/menus",
    "limitApp": "default",
    "count": 500000,
    "grade": 1,
    "strategy": 0,
    "controlBehavior": 0
  },
  {
    "resource": "/api/v1/menus/import",
    "limitApp": "default",
    "count": 100000,
    "grade": 1,
    "strategy": 0,
    "controlBehavior": 0
  },
  {
    "resource": "/api/v1/menus/export",
    "limitApp": "default",
    "count": 100000,
    "grade": 1,
    "strategy": 0,
    "controlBehavior": 0
  },
  {
    "resource": "/api/v1/menus/page",
    "limitApp": "default",
    "count": 100000,
    "grade": 1,
    "strategy": 0,
    "controlBehavior": 0
  },
  {
    "resource": "/api/v1/menus/{id}",
    "limitApp": "default",
    "count": 100000,
    "grade": 1,
    "strategy": 0,
    "controlBehavior": 0
  },
  {
    "resource": "/api/v1/login-log",
    "limitApp": "default",
    "count": 500000,
    "grade": 1,
    "strategy": 0,
    "controlBehavior": 0
  },
  {
    "resource": "/api/v1/login-log/import",
    "limitApp": "default",
    "count": 100000,
    "grade": 1,
    "strategy": 0,
    "controlBehavior": 0
  },
  {
    "resource": "/api/v1/login-log/export",
    "limitApp": "default",
    "count": 100000,
    "grade": 1,
    "strategy": 0,
    "controlBehavior": 0
  },
  {
    "resource": "/api/v1/login-log/page",
    "limitApp": "default",
    "count": 100000,
    "grade": 1,
    "strategy": 0,
    "controlBehavior": 0
  },
  {
    "resource": "/api/v1/login-log/{id}",
    "limitApp": "default",
    "count": 100000,
    "grade": 1,
    "strategy": 0,
    "controlBehavior": 0
  },
  {
    "resource": "/api/v1/login-log/all",
    "limitApp": "default",
    "count": 100000,
    "grade": 1,
    "strategy": 0,
    "controlBehavior": 0
  },
  {
    "resource": "/api/v1/i18n-menus",
    "limitApp": "default",
    "count": 500000,
    "grade": 1,
    "strategy": 0,
    "controlBehavior": 0
  },
  {
    "resource": "/api/v1/i18n-menus/import",
    "limitApp": "default",
    "count": 100000,
    "grade": 1,
    "strategy": 0,
    "controlBehavior": 0
  },
  {
    "resource": "/api/v1/i18n-menus/export",
    "limitApp": "default",
    "count": 100000,
    "grade": 1,
    "strategy": 0,
    "controlBehavior": 0
  },
  {
    "resource": "/api/v1/i18n-menus/page",
    "limitApp": "default",
    "count": 100000,
    "grade": 1,
    "strategy": 0,
    "controlBehavior": 0
  },
  {
    "resource": "/api/v1/i18n-menus/{id}",
    "limitApp": "default",
    "count": 100000,
    "grade": 1,
    "strategy": 0,
    "controlBehavior": 0
  },
  {
    "resource": "/api/v1/domain-events",
    "limitApp": "default",
    "count": 500000,
    "grade": 1,
    "strategy": 0,
    "controlBehavior": 0
  },
  {
    "resource": "/api/v1/domain-events/import",
    "limitApp": "default",
    "count": 100000,
    "grade": 1,
    "strategy": 0,
    "controlBehavior": 0
  },
  {
    "resource": "/api/v1/domain-events/export",
    "limitApp": "default",
    "count": 100000,
    "grade": 1,
    "strategy": 0,
    "controlBehavior": 0
  },
  {
    "resource": "/api/v1/domain-events/page",
    "limitApp": "default",
    "count": 100000,
    "grade": 1,
    "strategy": 0,
    "controlBehavior": 0
  },
  {
    "resource": "/api/v1/domain-events/{id}",
    "limitApp": "default",
    "count": 100000,
    "grade": 1,
    "strategy": 0,
    "controlBehavior": 0
  },
  {
    "resource": "/api/v1/dicts",
    "limitApp": "default",
    "count": 500000,
    "grade": 1,
    "strategy": 0,
    "controlBehavior": 0
  },
  {
    "resource": "/api/v1/dicts/import",
    "limitApp": "default",
    "count": 100000,
    "grade": 1,
    "strategy": 0,
    "controlBehavior": 0
  },
  {
    "resource": "/api/v1/dicts/export",
    "limitApp": "default",
    "count": 100000,
    "grade": 1,
    "strategy": 0,
    "controlBehavior": 0
  },
  {
    "resource": "/api/v1/dicts/page",
    "limitApp": "default",
    "count": 100000,
    "grade": 1,
    "strategy": 0,
    "controlBehavior": 0
  },
  {
    "resource": "/api/v1/dicts/{id}",
    "limitApp": "default",
    "count": 100000,
    "grade": 1,
    "strategy": 0,
    "controlBehavior": 0
  },
  {
    "resource": "/api/v1/dict-items",
    "limitApp": "default",
    "count": 500000,
    "grade": 1,
    "strategy": 0,
    "controlBehavior": 0
  },
  {
    "resource": "/api/v1/dict-items/import",
    "limitApp": "default",
    "count": 100000,
    "grade": 1,
    "strategy": 0,
    "controlBehavior": 0
  },
  {
    "resource": "/api/v1/dict-items/export",
    "limitApp": "default",
    "count": 100000,
    "grade": 1,
    "strategy": 0,
    "controlBehavior": 0
  },
  {
    "resource": "/api/v1/dict-items/page",
    "limitApp": "default",
    "count": 100000,
    "grade": 1,
    "strategy": 0,
    "controlBehavior": 0
  },
  {
    "resource": "/api/v1/dict-items/{id}",
    "limitApp": "default",
    "count": 100000,
    "grade": 1,
    "strategy": 0,
    "controlBehavior": 0
  },
  {
    "resource": "/api/v1/depts",
    "limitApp": "default",
    "count": 500000,
    "grade": 1,
    "strategy": 0,
    "controlBehavior": 0
  },
  {
    "resource": "/api/v1/depts/import",
    "limitApp": "default",
    "count": 100000,
    "grade": 1,
    "strategy": 0,
    "controlBehavior": 0
  },
  {
    "resource": "/api/v1/depts/export",
    "limitApp": "default",
    "count": 100000,
    "grade": 1,
    "strategy": 0,
    "controlBehavior": 0
  },
  {
    "resource": "/api/v1/depts/page",
    "limitApp": "default",
    "count": 100000,
    "grade": 1,
    "strategy": 0,
    "controlBehavior": 0
  },
  {
    "resource": "/api/v1/depts/{id}",
    "limitApp": "default",
    "count": 100000,
    "grade": 1,
    "strategy": 0,
    "controlBehavior": 0
  },
  {
    "resource": "/api/v1/clusters",
    "limitApp": "default",
    "count": 500000,
    "grade": 1,
    "strategy": 0,
    "controlBehavior": 0
  },
  {
    "resource": "/api/v1/clusters/import",
    "limitApp": "default",
    "count": 100000,
    "grade": 1,
    "strategy": 0,
    "controlBehavior": 0
  },
  {
    "resource": "/api/v1/clusters/export",
    "limitApp": "default",
    "count": 100000,
    "grade": 1,
    "strategy": 0,
    "controlBehavior": 0
  },
  {
    "resource": "/api/v1/clusters/page",
    "limitApp": "default",
    "count": 100000,
    "grade": 1,
    "strategy": 0,
    "controlBehavior": 0
  },
  {
    "resource": "/api/v1/clusters/{id}",
    "limitApp": "default",
    "count": 100000,
    "grade": 1,
    "strategy": 0,
    "controlBehavior": 0
  }
]
', '546e2d9817dd516db763f5ef3ef3a756', '2024-11-11 21:20:18.495', '2024-11-12 11:14:01.899', 'nacos', '0:0:0:0:0:0:0:1', 'laokou-admin', '0dac1a68-2f01-40df-bd26-bf0cb199057a', 'admin sentinel flow rule', '', '', 'json', '', '');
INSERT INTO "public"."config_info"  OVERRIDING SYSTEM VALUE VALUES  (43, 'auth-flow.json', 'IOT_GROUP', '[
  {
    "resource": "/api/v1/username-password/captchas/{uuid}",
    "limitApp": "default",
    "count": 100000,
    "grade": 1,
    "strategy": 0,
    "controlBehavior": 0
  },
  {
    "resource": "/api/v1/captchas/send-mobile",
    "limitApp": "default",
    "count": 100000,
    "grade": 1,
    "strategy": 0,
    "controlBehavior": 0
  },
    {
    "resource": "/api/v1/captchas/send-mail",
    "limitApp": "default",
    "count": 100000,
    "grade": 1,
    "strategy": 0,
    "controlBehavior": 0
  },
  {
    "resource": "/api/v1/secrets",
    "limitApp": "default",
    "count": 100000,
    "grade": 1,
    "strategy": 0,
    "controlBehavior": 0
  },
  {
    "resource": "/api/v1/tokens",
    "limitApp": "default",
    "count": 100000,
    "grade": 1,
    "strategy": 0,
    "controlBehavior": 0
  }
]
', '2bb6ed92c98b4a1693426dadf1c96a30', '2024-11-11 21:17:33.583', '2024-11-11 21:18:04.394', 'nacos', '0:0:0:0:0:0:0:1', 'laokou-auth', '0dac1a68-2f01-40df-bd26-bf0cb199057a', 'auth sentinel flow rule', '', '', 'json', '', '');
INSERT INTO "public"."config_info"  OVERRIDING SYSTEM VALUE VALUES  (13, 'gateway-flow.json', 'IOT_GROUP', '[
  {
    "resource": "laokou-auth",
    "grade": 1,
    "count": 100000,
    "intervalSec": 1,
    "burst": 500000,
    "controlBehavior": 0
  },
  {
    "resource": "laokou-admin",
    "grade": 1,
    "count": 100000,
    "intervalSec": 1,
    "burst": 500000,
    "controlBehavior": 0
  },
  {
    "resource": "laokou-iot",
    "grade": 1,
    "count": 100000,
    "intervalSec": 1,
    "burst": 500000,
    "controlBehavior": 0
  },
  {
    "resource": "laokou-generator",
    "grade": 1,
    "count": 100000,
    "intervalSec": 1,
    "burst": 500000,
    "controlBehavior": 0
  }
]', '5e013d830e3af21e5e321c0f4910fda0', '2024-05-25 18:13:10.6', '2024-11-11 20:52:03.56', 'nacos', '0:0:0:0:0:0:0:1', 'laokou-gateway', '8140e92b-fb43-48f5-b63b-7506185206a5', 'gateway sentinel flow rule', '', '', 'json', '', '');

INSERT INTO "public"."config_info"  OVERRIDING SYSTEM VALUE VALUES  (51, 'gateway-flow.json', 'IOT_GROUP', '[
  {
    "resource": "laokou-auth",
    "grade": 1,
    "count": 100000,
    "intervalSec": 1,
    "burst": 500000,
    "controlBehavior": 0
  },
  {
    "resource": "laokou-admin",
    "grade": 1,
    "count": 100000,
    "intervalSec": 1,
    "burst": 500000,
    "controlBehavior": 0
  },
  {
    "resource": "laokou-iot",
    "grade": 1,
    "count": 100000,
    "intervalSec": 1,
    "burst": 500000,
    "controlBehavior": 0
  },
  {
    "resource": "laokou-generator",
    "grade": 1,
    "count": 100000,
    "intervalSec": 1,
    "burst": 500000,
    "controlBehavior": 0
  }
]', '5e013d830e3af21e5e321c0f4910fda0', '2023-02-26 14:59:20', '2024-11-11 20:51:31.708', 'nacos', '0:0:0:0:0:0:0:1', 'laokou-gateway', 'a61abd4c-ef96-42a5-99a1-616adee531f3', 'gateway sentinel flow rule', '', '', 'json', '', '');
INSERT INTO "public"."config_info"  OVERRIDING SYSTEM VALUE VALUES  (31, 'router.json', 'IOT_GROUP', '[
  {
    "id": "laokou-auth",
    "uri": "lb://laokou-auth",
    "predicates": [
      {
        "name": "Path",
        "args": {
          "pattern": "/api-gateway/auth/**"
        }
      },
      {
        "name": "Weight",
        "args": {
          "_genkey_0": "auth",
          "_genkey_1": "100"
        }
      }
    ],
    "filters": [
      {
        "name": "StripPrefix",
        "args": {
          "parts": "2"
        }
      },
      {
        "name": "RewritePath",
        "args": {
          "_genkey_0": "/api-gateway/auth/(?<path>.*)",
          "_genkey_1": "/$\\{path}"
        }
      }
    ],
    "metadata": {
      "version": "v1"
    },
    "order": 1
  },
  {
    "id": "laokou-admin",
    "uri": "lb://laokou-admin",
    "predicates": [
      {
        "name": "Path",
        "args": {
          "pattern": "/api-gateway/admin/**"
        }
      },
      {
        "name": "Weight",
        "args": {
          "_genkey_0": "admin",
          "_genkey_1": "100"
        }
      }
    ],
    "filters": [
      {
        "name": "StripPrefix",
        "args": {
          "parts": "2"
        }
      },
      {
        "name": "RewritePath",
        "args": {
          "_genkey_0": "/api-gateway/admin/(?<path>.*)",
          "_genkey_1": "/$\\{path}"
        }
      }
    ],
    "metadata": {
      "version": "v1"
    },
    "order": 1
  },
  {
    "id": "laokou-iot",
    "uri": "lb://laokou-iot",
    "predicates": [
      {
        "name": "Path",
        "args": {
          "pattern": "/api-gateway/iot/**"
        }
      },
      {
        "name": "Weight",
        "args": {
          "_genkey_0": "iot",
          "_genkey_1": "100"
        }
      }
    ],
    "filters": [
      {
        "name": "StripPrefix",
        "args": {
          "parts": "2"
        }
      },
      {
        "name": "RewritePath",
        "args": {
          "_genkey_0": "/api-gateway/iot/(?<path>.*)",
          "_genkey_1": "/$\\{path}"
        }
      }
    ],
    "metadata": {
      "version": "v1"
    },
    "order": 1
  },
    {
    "id": "laokou-generator",
    "uri": "lb://laokou-generator",
    "predicates": [
      {
        "name": "Path",
        "args": {
          "pattern": "/api-gateway/generator/**"
        }
      },
      {
        "name": "Weight",
        "args": {
          "_genkey_0": "generator",
          "_genkey_1": "100"
        }
      }
    ],
    "filters": [
      {
        "name": "StripPrefix",
        "args": {
          "parts": "2"
        }
      },
      {
        "name": "RewritePath",
        "args": {
          "_genkey_0": "/api-gateway/generator/(?<path>.*)",
          "_genkey_1": "/$\\{path}"
        }
      }
    ],
    "metadata": {
      "version": "v1"
    },
    "order": 1
  },
  {
    "id": "laokou-iot-websocket",
    "uri": "lb:wss://laokou-iot-websocket",
    "predicates": [
      {
        "name": "Path",
        "args": {
          "pattern": "/api-gateway/iot-websocket/**"
        }
      },
      {
        "name": "Weight",
        "args": {
          "_genkey_0": "iot-websocket",
          "_genkey_1": "100"
        }
      }
    ],
    "filters": [
      {
        "name": "StripPrefix",
        "args": {
          "parts": "2"
        }
      },
      {
        "name": "RewritePath",
        "args": {
          "_genkey_0": "/api-gateway/iot-websocket/(?<path>.*)",
          "_genkey_1": "/$\\{path}"
        }
      }
    ],
    "metadata": {
      "version": "v1"
    },
    "order": 1
  }
]
', '0e03959c7b36b5d0c13fc33b2463e0d3', '2024-05-25 18:13:10.616', '2024-11-06 22:47:22.13', 'nacos', '0:0:0:0:0:0:0:1', 'laokou-gateway', '8140e92b-fb43-48f5-b63b-7506185206a5', '动态路由配置', '', '', 'json', '', '');
INSERT INTO "public"."config_info"  OVERRIDING SYSTEM VALUE VALUES  (26, 'router.json', 'IOT_GROUP', '[
  {
    "id": "laokou-auth",
    "uri": "lb://laokou-auth",
    "predicates": [
      {
        "name": "Path",
        "args": {
          "pattern": "/api-gateway/auth/**"
        }
      },
      {
        "name": "Weight",
        "args": {
          "_genkey_0": "auth",
          "_genkey_1": "100"
        }
      }
    ],
    "filters": [
      {
        "name": "StripPrefix",
        "args": {
          "parts": "2"
        }
      },
      {
        "name": "RewritePath",
        "args": {
          "_genkey_0": "/api-gateway/auth/(?<path>.*)",
          "_genkey_1": "/$\\{path}"
        }
      }
    ],
    "metadata": {
      "version": "v1"
    },
    "order": 1
  },
  {
    "id": "laokou-admin",
    "uri": "lb://laokou-admin",
    "predicates": [
      {
        "name": "Path",
        "args": {
          "pattern": "/api-gateway/admin/**"
        }
      },
      {
        "name": "Weight",
        "args": {
          "_genkey_0": "admin",
          "_genkey_1": "100"
        }
      }
    ],
    "filters": [
      {
        "name": "StripPrefix",
        "args": {
          "parts": "2"
        }
      },
      {
        "name": "RewritePath",
        "args": {
          "_genkey_0": "/api-gateway/admin/(?<path>.*)",
          "_genkey_1": "/$\\{path}"
        }
      }
    ],
    "metadata": {
      "version": "v1"
    },
    "order": 1
  },
  {
    "id": "laokou-iot",
    "uri": "lb://laokou-iot",
    "predicates": [
      {
        "name": "Path",
        "args": {
          "pattern": "/api-gateway/iot/**"
        }
      },
      {
        "name": "Weight",
        "args": {
          "_genkey_0": "iot",
          "_genkey_1": "100"
        }
      }
    ],
    "filters": [
      {
        "name": "StripPrefix",
        "args": {
          "parts": "2"
        }
      },
      {
        "name": "RewritePath",
        "args": {
          "_genkey_0": "/api-gateway/iot/(?<path>.*)",
          "_genkey_1": "/$\\{path}"
        }
      }
    ],
    "metadata": {
      "version": "v1"
    },
    "order": 1
  },
    {
    "id": "laokou-generator",
    "uri": "lb://laokou-generator",
    "predicates": [
      {
        "name": "Path",
        "args": {
          "pattern": "/api-gateway/generator/**"
        }
      },
      {
        "name": "Weight",
        "args": {
          "_genkey_0": "generator",
          "_genkey_1": "100"
        }
      }
    ],
    "filters": [
      {
        "name": "StripPrefix",
        "args": {
          "parts": "2"
        }
      },
      {
        "name": "RewritePath",
        "args": {
          "_genkey_0": "/api-gateway/generator/(?<path>.*)",
          "_genkey_1": "/$\\{path}"
        }
      }
    ],
    "metadata": {
      "version": "v1"
    },
    "order": 1
  },
  {
    "id": "laokou-iot-websocket",
    "uri": "lb:ws://laokou-iot-websocket",
    "predicates": [
      {
        "name": "Path",
        "args": {
          "pattern": "/api-gateway/iot-websocket/**"
        }
      },
      {
        "name": "Weight",
        "args": {
          "_genkey_0": "iot-websocket",
          "_genkey_1": "100"
        }
      }
    ],
    "filters": [
      {
        "name": "StripPrefix",
        "args": {
          "parts": "2"
        }
      },
      {
        "name": "RewritePath",
        "args": {
          "_genkey_0": "/api-gateway/iot-websocket/(?<path>.*)",
          "_genkey_1": "/$\\{path}"
        }
      }
    ],
    "metadata": {
      "version": "v1"
    },
    "order": 1
  }
]
', '0e03959c7b36b5d0c13fc33b2463e0d3', '2024-05-25 18:13:33.387', '2024-11-06 22:46:53.026', 'nacos', '0:0:0:0:0:0:0:1', 'laokou-gateway', '0dac1a68-2f01-40df-bd26-bf0cb199057a', '动态路由配置', '', '', 'json', '', '');
INSERT INTO "public"."config_info"  OVERRIDING SYSTEM VALUE VALUES  (30, 'router.json', 'IOT_GROUP', '[
  {
    "id": "laokou-auth",
    "uri": "lb://laokou-auth",
    "predicates": [
      {
        "name": "Path",
        "args": {
          "pattern": "/api-gateway/auth/**"
        }
      },
      {
        "name": "Weight",
        "args": {
          "_genkey_0": "auth",
          "_genkey_1": "100"
        }
      }
    ],
    "filters": [
      {
        "name": "StripPrefix",
        "args": {
          "parts": "2"
        }
      },
      {
        "name": "RewritePath",
        "args": {
          "_genkey_0": "/api-gateway/auth/(?<path>.*)",
          "_genkey_1": "/$\\{path}"
        }
      }
    ],
    "metadata": {
      "version": "v1"
    },
    "order": 1
  },
  {
    "id": "laokou-admin",
    "uri": "lb://laokou-admin",
    "predicates": [
      {
        "name": "Path",
        "args": {
          "pattern": "/api-gateway/admin/**"
        }
      },
      {
        "name": "Weight",
        "args": {
          "_genkey_0": "admin",
          "_genkey_1": "100"
        }
      }
    ],
    "filters": [
      {
        "name": "StripPrefix",
        "args": {
          "parts": "2"
        }
      },
      {
        "name": "RewritePath",
        "args": {
          "_genkey_0": "/api-gateway/admin/(?<path>.*)",
          "_genkey_1": "/$\\{path}"
        }
      }
    ],
    "metadata": {
      "version": "v1"
    },
    "order": 1
  },
  {
    "id": "laokou-iot",
    "uri": "lb://laokou-iot",
    "predicates": [
      {
        "name": "Path",
        "args": {
          "pattern": "/api-gateway/iot/**"
        }
      },
      {
        "name": "Weight",
        "args": {
          "_genkey_0": "iot",
          "_genkey_1": "100"
        }
      }
    ],
    "filters": [
      {
        "name": "StripPrefix",
        "args": {
          "parts": "2"
        }
      },
      {
        "name": "RewritePath",
        "args": {
          "_genkey_0": "/api-gateway/iot/(?<path>.*)",
          "_genkey_1": "/$\\{path}"
        }
      }
    ],
    "metadata": {
      "version": "v1"
    },
    "order": 1
  },
    {
    "id": "laokou-generator",
    "uri": "lb://laokou-generator",
    "predicates": [
      {
        "name": "Path",
        "args": {
          "pattern": "/api-gateway/generator/**"
        }
      },
      {
        "name": "Weight",
        "args": {
          "_genkey_0": "generator",
          "_genkey_1": "100"
        }
      }
    ],
    "filters": [
      {
        "name": "StripPrefix",
        "args": {
          "parts": "2"
        }
      },
      {
        "name": "RewritePath",
        "args": {
          "_genkey_0": "/api-gateway/generator/(?<path>.*)",
          "_genkey_1": "/$\\{path}"
        }
      }
    ],
    "metadata": {
      "version": "v1"
    },
    "order": 1
  },
  {
    "id": "laokou-iot-websocket",
    "uri": "lb:wss://laokou-iot-websocket",
    "predicates": [
      {
        "name": "Path",
        "args": {
          "pattern": "/api-gateway/iot-websocket/**"
        }
      },
      {
        "name": "Weight",
        "args": {
          "_genkey_0": "iot-websocket",
          "_genkey_1": "100"
        }
      }
    ],
    "filters": [
      {
        "name": "StripPrefix",
        "args": {
          "parts": "2"
        }
      },
      {
        "name": "RewritePath",
        "args": {
          "_genkey_0": "/api-gateway/iot-websocket/(?<path>.*)",
          "_genkey_1": "/$\\{path}"
        }
      }
    ],
    "metadata": {
      "version": "v1"
    },
    "order": 1
  }
]
', '0e03959c7b36b5d0c13fc33b2463e0d3', '2023-01-13 15:44:25', '2024-11-06 22:47:10.779', 'nacos', '0:0:0:0:0:0:0:1', 'laokou-gateway', 'a61abd4c-ef96-42a5-99a1-616adee531f3', '', '', '', 'json', '', '');

DROP TABLE IF EXISTS "public"."config_info_gray";
CREATE TABLE "public"."config_info_gray" (
"id" bigserial NOT NULL,
"data_id" varchar(255)  NOT NULL,
"group_id" varchar(128)  NOT NULL,
"content" text  NOT NULL,
"md5" varchar(32) ,
"src_user" text ,
"src_ip" varchar(100) ,
"gmt_create" timestamp(6) NOT NULL,
"gmt_modified" timestamp(6) NOT NULL,
"app_name" varchar(128) ,
"tenant_id" varchar(128) ,
"gray_name" varchar(128)  NOT NULL,
"gray_rule" text  NOT NULL,
"encrypted_data_key" varchar(256)  NOT NULL,
CONSTRAINT "config_info_gray_pkey" PRIMARY KEY ("id")
);
CREATE INDEX "idx_dataid_gmt_modified" ON "public"."config_info_gray" USING btree ("data_id"  ,"gmt_modified" );

CREATE INDEX "idx_gmt_modified_gray" ON "public"."config_info_gray" USING btree ("gmt_modified" );

CREATE UNIQUE INDEX "uk_configinfogray_datagrouptenantgray" ON "public"."config_info_gray" USING btree ("data_id"  ,"group_id"  ,"tenant_id"  ,"gray_name"  );

COMMENT ON COLUMN "public"."config_info_gray"."id" IS 'id';
COMMENT ON COLUMN "public"."config_info_gray"."data_id" IS 'data_id';
COMMENT ON COLUMN "public"."config_info_gray"."group_id" IS 'group_id';
COMMENT ON COLUMN "public"."config_info_gray"."content" IS 'content';
COMMENT ON COLUMN "public"."config_info_gray"."md5" IS 'md5';
COMMENT ON COLUMN "public"."config_info_gray"."src_user" IS 'src_user';
COMMENT ON COLUMN "public"."config_info_gray"."src_ip" IS 'src_ip';
COMMENT ON COLUMN "public"."config_info_gray"."gmt_create" IS 'gmt_create';
COMMENT ON COLUMN "public"."config_info_gray"."gmt_modified" IS 'gmt_modified';
COMMENT ON COLUMN "public"."config_info_gray"."app_name" IS 'app_name';
COMMENT ON COLUMN "public"."config_info_gray"."tenant_id" IS 'tenant_id';
COMMENT ON COLUMN "public"."config_info_gray"."gray_name" IS 'gray_name';
COMMENT ON COLUMN "public"."config_info_gray"."gray_rule" IS 'gray_rule';
COMMENT ON COLUMN "public"."config_info_gray"."encrypted_data_key" IS 'encrypted_data_key';
COMMENT ON TABLE "public"."config_info_gray" IS 'config_info_gray';

-- ----------------------------
-- Table structure for pipeline_execution
-- ----------------------------
DROP TABLE IF EXISTS "pipeline_execution";
CREATE TABLE "pipeline_execution" (
  "execution_id"  varchar(64)  NOT NULL,
  "resource_type" varchar(32)  NOT NULL,
  "resource_name" varchar(256) NOT NULL,
  "namespace_id"  varchar(128) DEFAULT NULL,
  "version"       varchar(64)  DEFAULT NULL,
  "status"        varchar(32)  NOT NULL,
  "pipeline"      text         NOT NULL,
  "create_time"   bigint       NOT NULL,
  "update_time"   bigint       NOT NULL
);
COMMENT ON COLUMN "pipeline_execution"."execution_id" IS '执行ID';
COMMENT ON COLUMN "pipeline_execution"."resource_type" IS '资源类型';
COMMENT ON COLUMN "pipeline_execution"."resource_name" IS '资源名称';
COMMENT ON COLUMN "pipeline_execution"."namespace_id" IS '命名空间ID';
COMMENT ON COLUMN "pipeline_execution"."version" IS '版本';
COMMENT ON COLUMN "pipeline_execution"."status" IS '执行状态';
COMMENT ON COLUMN "pipeline_execution"."pipeline" IS 'pipeline节点结果JSON';
COMMENT ON COLUMN "pipeline_execution"."create_time" IS '创建时间';
COMMENT ON COLUMN "pipeline_execution"."update_time" IS '修改时间';
COMMENT ON TABLE "pipeline_execution" IS 'AI资源发布审核Pipeline执行记录';

-- ----------------------------
-- Primary Key structure for table pipeline_execution
-- ----------------------------
ALTER TABLE "pipeline_execution" ADD CONSTRAINT "pipeline_execution_pkey" PRIMARY KEY ("execution_id");

-- ----------------------------
-- Table structure for ai_resource
-- ----------------------------
DROP TABLE IF EXISTS "ai_resource";
CREATE TABLE "ai_resource" (
   "id" bigserial NOT NULL,
   "gmt_create" timestamp(6) NOT NULL DEFAULT CURRENT_TIMESTAMP,
   "gmt_modified" timestamp(6) NOT NULL DEFAULT CURRENT_TIMESTAMP,
   "name" varchar(256) NOT NULL,
   "type" varchar(32) NOT NULL,
   "c_desc" varchar(1024),
   "status" varchar(32),
   "namespace_id" varchar(128) NOT NULL DEFAULT '',
   "biz_tags" varchar(1024),
   "ext" text,
   "c_from" varchar(256) NOT NULL DEFAULT 'local',
   "version_info" text,
   "meta_version" bigint NOT NULL DEFAULT 1,
   "scope" varchar(16) NOT NULL DEFAULT 'PRIVATE',
   "owner" varchar(128) NOT NULL DEFAULT '',
   "download_count" bigint NOT NULL DEFAULT 0
)
;

COMMENT ON COLUMN "ai_resource"."id" IS 'id';
COMMENT ON COLUMN "ai_resource"."gmt_create" IS '创建时间';
COMMENT ON COLUMN "ai_resource"."gmt_modified" IS '修改时间';
COMMENT ON COLUMN "ai_resource"."name" IS '资源名称';
COMMENT ON COLUMN "ai_resource"."type" IS '资源类型';
COMMENT ON COLUMN "ai_resource"."c_desc" IS '资源描述';
COMMENT ON COLUMN "ai_resource"."status" IS '资源状态';
COMMENT ON COLUMN "ai_resource"."namespace_id" IS '命名空间ID';
COMMENT ON COLUMN "ai_resource"."biz_tags" IS '业务标签';
COMMENT ON COLUMN "ai_resource"."ext" IS '扩展信息(JSON)';
COMMENT ON COLUMN "ai_resource"."c_from" IS '来源标识(导入/同步来源)';
COMMENT ON COLUMN "ai_resource"."version_info" IS '版本信息(JSON)';
COMMENT ON COLUMN "ai_resource"."meta_version" IS '元数据版本(乐观锁)';
COMMENT ON COLUMN "ai_resource"."scope" IS '可见性: PUBLIC/PRIVATE';
COMMENT ON COLUMN "ai_resource"."owner" IS '创建者用户名';
COMMENT ON COLUMN "ai_resource"."download_count" IS '下载次数';
COMMENT ON TABLE "ai_resource" IS 'AI资源元数据表';

-- ----------------------------
-- Primary Key structure for table ai_resource
-- ----------------------------
ALTER TABLE "ai_resource" ADD CONSTRAINT "ai_resource_pkey" PRIMARY KEY ("id");

-- ----------------------------
-- Indexes structure for table ai_resource
-- ----------------------------
CREATE UNIQUE INDEX "uk_ai_resource_ns_name_type" ON "ai_resource" USING btree (
	"namespace_id",
	"name",
	"type",
	"c_from"
	);
CREATE INDEX "idx_ai_resource_name" ON "ai_resource" USING btree ("name");
CREATE INDEX "idx_ai_resource_type" ON "ai_resource" USING btree ("type");
CREATE INDEX "idx_ai_resource_gmt_modified" ON "ai_resource" USING btree ("gmt_modified");

-- ----------------------------
-- Table structure for ai_resource_version
-- ----------------------------
DROP TABLE IF EXISTS "ai_resource_version";
CREATE TABLE "ai_resource_version" (
   "id" bigserial NOT NULL,
   "gmt_create" timestamp(6) NOT NULL DEFAULT CURRENT_TIMESTAMP,
   "gmt_modified" timestamp(6) NOT NULL DEFAULT CURRENT_TIMESTAMP,
   "type" varchar(32) NOT NULL,
   "author" varchar(128),
   "name" varchar(256) NOT NULL,
   "c_desc" varchar(1024),
   "status" varchar(32) NOT NULL,
   "version" varchar(64) NOT NULL,
   "namespace_id" varchar(128) NOT NULL DEFAULT '',
   "storage" text,
   "publish_pipeline_info" text,
   "download_count" bigint NOT NULL DEFAULT 0
)
;

COMMENT ON COLUMN "ai_resource_version"."id" IS 'id';
COMMENT ON COLUMN "ai_resource_version"."gmt_create" IS '创建时间';
COMMENT ON COLUMN "ai_resource_version"."gmt_modified" IS '修改时间';
COMMENT ON COLUMN "ai_resource_version"."type" IS '资源类型';
COMMENT ON COLUMN "ai_resource_version"."author" IS '作者';
COMMENT ON COLUMN "ai_resource_version"."name" IS '资源名称';
COMMENT ON COLUMN "ai_resource_version"."c_desc" IS '版本描述';
COMMENT ON COLUMN "ai_resource_version"."status" IS '版本状态';
COMMENT ON COLUMN "ai_resource_version"."version" IS '版本号';
COMMENT ON COLUMN "ai_resource_version"."namespace_id" IS '命名空间ID';
COMMENT ON COLUMN "ai_resource_version"."storage" IS '存储信息(JSON)';
COMMENT ON COLUMN "ai_resource_version"."publish_pipeline_info" IS '发布流水线信息(JSON)';
COMMENT ON COLUMN "ai_resource_version"."download_count" IS '下载次数';
COMMENT ON TABLE "ai_resource_version" IS 'AI资源版本表';

-- ----------------------------
-- Primary Key structure for table ai_resource_version
-- ----------------------------
ALTER TABLE "ai_resource_version" ADD CONSTRAINT "ai_resource_version_pkey" PRIMARY KEY ("id");

-- ----------------------------
-- Indexes structure for table ai_resource_version
-- ----------------------------
CREATE UNIQUE INDEX "uk_ai_resource_ver_ns_name_type_ver" ON "ai_resource_version" USING btree (
	"namespace_id",
	"name",
	"type",
	"version"
	);
CREATE INDEX "idx_ai_resource_ver_name" ON "ai_resource_version" USING btree ("name");
CREATE INDEX "idx_ai_resource_ver_status" ON "ai_resource_version" USING btree ("status");
CREATE INDEX "idx_ai_resource_ver_gmt_modified" ON "ai_resource_version" USING btree ("gmt_modified");
