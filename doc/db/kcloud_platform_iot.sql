/*
 Navicat Premium Dump SQL

 Source Server         : 127.0.0.1
 Source Server Type    : PostgreSQL
 Source Server Version : 160002 (160002)
 Source Host           : 127.0.0.1:5432
 Source Catalog        : kcloud_platform_iot
 Source Schema         : public

 Target Server Type    : PostgreSQL
 Target Server Version : 160002 (160002)
 File Encoding         : 65001

 Date: 05/11/2024 17:01:00
*/


-- ----------------------------
-- Sequence structure for boot_iot_cp_id_seq
-- ----------------------------
DROP SEQUENCE IF EXISTS "public"."boot_iot_cp_id_seq";
CREATE SEQUENCE "public"."boot_iot_cp_id_seq" 
INCREMENT 1
MINVALUE  1
MAXVALUE 9223372036854775807
START 1
CACHE 1;

-- ----------------------------
-- Sequence structure for boot_iot_device_id_seq
-- ----------------------------
DROP SEQUENCE IF EXISTS "public"."boot_iot_device_id_seq";
CREATE SEQUENCE "public"."boot_iot_device_id_seq" 
INCREMENT 1
MINVALUE  1
MAXVALUE 9223372036854775807
START 1
CACHE 1;

-- ----------------------------
-- Sequence structure for boot_iot_device_id_seq1
-- ----------------------------
DROP SEQUENCE IF EXISTS "public"."boot_iot_device_id_seq1";
CREATE SEQUENCE "public"."boot_iot_device_id_seq1" 
INCREMENT 1
MINVALUE  1
MAXVALUE 9223372036854775807
START 1
CACHE 1;

-- ----------------------------
-- Sequence structure for boot_iot_device_id_seq2
-- ----------------------------
DROP SEQUENCE IF EXISTS "public"."boot_iot_device_id_seq2";
CREATE SEQUENCE "public"."boot_iot_device_id_seq2" 
INCREMENT 1
MINVALUE  1
MAXVALUE 9223372036854775807
START 1
CACHE 1;

-- ----------------------------
-- Sequence structure for boot_iot_device_id_seq3
-- ----------------------------
DROP SEQUENCE IF EXISTS "public"."boot_iot_device_id_seq3";
CREATE SEQUENCE "public"."boot_iot_device_id_seq3" 
INCREMENT 1
MINVALUE  1
MAXVALUE 9223372036854775807
START 1
CACHE 1;

-- ----------------------------
-- Sequence structure for boot_iot_model_id_seq
-- ----------------------------
DROP SEQUENCE IF EXISTS "public"."boot_iot_model_id_seq";
CREATE SEQUENCE "public"."boot_iot_model_id_seq" 
INCREMENT 1
MINVALUE  1
MAXVALUE 9223372036854775807
START 1
CACHE 1;

-- ----------------------------
-- Sequence structure for boot_iot_model_id_seq1
-- ----------------------------
DROP SEQUENCE IF EXISTS "public"."boot_iot_model_id_seq1";
CREATE SEQUENCE "public"."boot_iot_model_id_seq1" 
INCREMENT 1
MINVALUE  1
MAXVALUE 9223372036854775807
START 1
CACHE 1;

-- ----------------------------
-- Sequence structure for boot_iot_model_id_seq2
-- ----------------------------
DROP SEQUENCE IF EXISTS "public"."boot_iot_model_id_seq2";
CREATE SEQUENCE "public"."boot_iot_model_id_seq2" 
INCREMENT 1
MINVALUE  1
MAXVALUE 9223372036854775807
START 1
CACHE 1;

-- ----------------------------
-- Sequence structure for boot_iot_network_id_seq
-- ----------------------------
DROP SEQUENCE IF EXISTS "public"."boot_iot_network_id_seq";
CREATE SEQUENCE "public"."boot_iot_network_id_seq" 
INCREMENT 1
MINVALUE  1
MAXVALUE 9223372036854775807
START 1
CACHE 1;

-- ----------------------------
-- Sequence structure for boot_iot_network_id_seq1
-- ----------------------------
DROP SEQUENCE IF EXISTS "public"."boot_iot_network_id_seq1";
CREATE SEQUENCE "public"."boot_iot_network_id_seq1" 
INCREMENT 1
MINVALUE  1
MAXVALUE 9223372036854775807
START 1
CACHE 1;

-- ----------------------------
-- Sequence structure for boot_iot_product_category_id_seq
-- ----------------------------
DROP SEQUENCE IF EXISTS "public"."boot_iot_product_category_id_seq";
CREATE SEQUENCE "public"."boot_iot_product_category_id_seq" 
INCREMENT 1
MINVALUE  1
MAXVALUE 9223372036854775807
START 1
CACHE 1;

-- ----------------------------
-- Sequence structure for boot_iot_product_id_seq
-- ----------------------------
DROP SEQUENCE IF EXISTS "public"."boot_iot_product_id_seq";
CREATE SEQUENCE "public"."boot_iot_product_id_seq" 
INCREMENT 1
MINVALUE  1
MAXVALUE 9223372036854775807
START 1
CACHE 1;

-- ----------------------------
-- Sequence structure for boot_iot_product_id_seq1
-- ----------------------------
DROP SEQUENCE IF EXISTS "public"."boot_iot_product_id_seq1";
CREATE SEQUENCE "public"."boot_iot_product_id_seq1" 
INCREMENT 1
MINVALUE  1
MAXVALUE 9223372036854775807
START 1
CACHE 1;

-- ----------------------------
-- Sequence structure for boot_iot_product_id_seq2
-- ----------------------------
DROP SEQUENCE IF EXISTS "public"."boot_iot_product_id_seq2";
CREATE SEQUENCE "public"."boot_iot_product_id_seq2" 
INCREMENT 1
MINVALUE  1
MAXVALUE 9223372036854775807
START 1
CACHE 1;

-- ----------------------------
-- Sequence structure for boot_iot_product_model_id_seq
-- ----------------------------
DROP SEQUENCE IF EXISTS "public"."boot_iot_product_model_id_seq";
CREATE SEQUENCE "public"."boot_iot_product_model_id_seq" 
INCREMENT 1
MINVALUE  1
MAXVALUE 9223372036854775807
START 1
CACHE 1;

-- ----------------------------
-- Sequence structure for boot_iot_product_model_id_seq1
-- ----------------------------
DROP SEQUENCE IF EXISTS "public"."boot_iot_product_model_id_seq1";
CREATE SEQUENCE "public"."boot_iot_product_model_id_seq1" 
INCREMENT 1
MINVALUE  1
MAXVALUE 9223372036854775807
START 1
CACHE 1;

-- ----------------------------
-- Sequence structure for boot_iot_product_type_id_seq
-- ----------------------------
DROP SEQUENCE IF EXISTS "public"."boot_iot_product_type_id_seq";
CREATE SEQUENCE "public"."boot_iot_product_type_id_seq" 
INCREMENT 1
MINVALUE  1
MAXVALUE 9223372036854775807
START 1
CACHE 1;

-- ----------------------------
-- Sequence structure for boot_iot_product_type_id_seq1
-- ----------------------------
DROP SEQUENCE IF EXISTS "public"."boot_iot_product_type_id_seq1";
CREATE SEQUENCE "public"."boot_iot_product_type_id_seq1" 
INCREMENT 1
MINVALUE  1
MAXVALUE 9223372036854775807
START 1
CACHE 1;

-- ----------------------------
-- Sequence structure for boot_iot_protocol_id_seq
-- ----------------------------
DROP SEQUENCE IF EXISTS "public"."boot_iot_protocol_id_seq";
CREATE SEQUENCE "public"."boot_iot_protocol_id_seq" 
INCREMENT 1
MINVALUE  1
MAXVALUE 9223372036854775807
START 1
CACHE 1;

-- ----------------------------
-- Sequence structure for boot_iot_protocol_id_seq1
-- ----------------------------
DROP SEQUENCE IF EXISTS "public"."boot_iot_protocol_id_seq1";
CREATE SEQUENCE "public"."boot_iot_protocol_id_seq1" 
INCREMENT 1
MINVALUE  1
MAXVALUE 9223372036854775807
START 1
CACHE 1;

-- ----------------------------
-- Sequence structure for boot_iot_tp_id_seq
-- ----------------------------
DROP SEQUENCE IF EXISTS "public"."boot_iot_tp_id_seq";
CREATE SEQUENCE "public"."boot_iot_tp_id_seq" 
INCREMENT 1
MINVALUE  1
MAXVALUE 9223372036854775807
START 1
CACHE 1;

-- ----------------------------
-- Table structure for boot_iot_cp
-- ----------------------------
DROP TABLE IF EXISTS "public"."boot_iot_cp";
CREATE TABLE "public"."boot_iot_cp" (
  "id" int8 NOT NULL GENERATED BY DEFAULT AS IDENTITY (
INCREMENT 1
MINVALUE  1
MAXVALUE 9223372036854775807
START 1
CACHE 1
),
  "creator" int8 NOT NULL DEFAULT 0,
  "editor" int8 NOT NULL DEFAULT 0,
  "create_time" timestamp(6) NOT NULL DEFAULT CURRENT_TIMESTAMP,
  "update_time" timestamp(6) NOT NULL DEFAULT CURRENT_TIMESTAMP,
  "del_flag" int2 NOT NULL DEFAULT 0,
  "version" int4 NOT NULL DEFAULT 0,
  "tenant_id" int8 NOT NULL DEFAULT 0
)
;
COMMENT ON COLUMN "public"."boot_iot_cp"."id" IS 'ID';
COMMENT ON COLUMN "public"."boot_iot_cp"."creator" IS '创建人';
COMMENT ON COLUMN "public"."boot_iot_cp"."editor" IS '编辑人';
COMMENT ON COLUMN "public"."boot_iot_cp"."create_time" IS '创建时间';
COMMENT ON COLUMN "public"."boot_iot_cp"."update_time" IS '修改时间';
COMMENT ON COLUMN "public"."boot_iot_cp"."del_flag" IS '删除标识 0未删除 1已删除';
COMMENT ON COLUMN "public"."boot_iot_cp"."version" IS '版本号';
COMMENT ON COLUMN "public"."boot_iot_cp"."tenant_id" IS '租户ID';
COMMENT ON TABLE "public"."boot_iot_cp" IS '通讯协议';

-- ----------------------------
-- Records of boot_iot_cp
-- ----------------------------

-- ----------------------------
-- Table structure for boot_iot_device
-- ----------------------------
DROP TABLE IF EXISTS "public"."boot_iot_device";
CREATE TABLE "public"."boot_iot_device" (
  "id" int8 NOT NULL GENERATED BY DEFAULT AS IDENTITY (
INCREMENT 1
MINVALUE  1
MAXVALUE 9223372036854775807
START 1
CACHE 1
),
  "creator" int8 NOT NULL DEFAULT 0,
  "editor" int8 NOT NULL DEFAULT 0,
  "create_time" timestamp(6) NOT NULL DEFAULT CURRENT_TIMESTAMP,
  "update_time" timestamp(6) NOT NULL DEFAULT CURRENT_TIMESTAMP,
  "del_flag" int2 NOT NULL DEFAULT 0,
  "version" int4 NOT NULL DEFAULT 0,
  "tenant_id" int8 NOT NULL DEFAULT 0,
  "sn" varchar(64) COLLATE "pg_catalog"."default" NOT NULL,
  "name" varchar(50) COLLATE "pg_catalog"."default" NOT NULL,
  "status" int2 NOT NULL DEFAULT 0,
  "longitude" float8,
  "latitude" float8,
  "img_url" varchar(400) COLLATE "pg_catalog"."default",
  "address" varchar(200) COLLATE "pg_catalog"."default",
  "remark" varchar(400) COLLATE "pg_catalog"."default",
  "product_id" int8 NOT NULL
)
;
COMMENT ON COLUMN "public"."boot_iot_device"."id" IS 'ID';
COMMENT ON COLUMN "public"."boot_iot_device"."creator" IS '创建人';
COMMENT ON COLUMN "public"."boot_iot_device"."editor" IS '编辑人';
COMMENT ON COLUMN "public"."boot_iot_device"."create_time" IS '创建时间';
COMMENT ON COLUMN "public"."boot_iot_device"."update_time" IS '修改时间';
COMMENT ON COLUMN "public"."boot_iot_device"."del_flag" IS '删除标识 0未删除 1已删除';
COMMENT ON COLUMN "public"."boot_iot_device"."version" IS '版本号';
COMMENT ON COLUMN "public"."boot_iot_device"."tenant_id" IS '租户ID';
COMMENT ON COLUMN "public"."boot_iot_device"."sn" IS '设备序列号';
COMMENT ON COLUMN "public"."boot_iot_device"."name" IS '设备名称';
COMMENT ON COLUMN "public"."boot_iot_device"."status" IS '设备状态 0在线 1离线';
COMMENT ON COLUMN "public"."boot_iot_device"."longitude" IS '设备经度';
COMMENT ON COLUMN "public"."boot_iot_device"."latitude" IS '设备纬度';
COMMENT ON COLUMN "public"."boot_iot_device"."img_url" IS '设备图片URL';
COMMENT ON COLUMN "public"."boot_iot_device"."address" IS '设备地址';
COMMENT ON COLUMN "public"."boot_iot_device"."remark" IS '设备备注';
COMMENT ON COLUMN "public"."boot_iot_device"."product_id" IS '产品ID';
COMMENT ON TABLE "public"."boot_iot_device" IS '设备';

-- ----------------------------
-- Records of boot_iot_device
-- ----------------------------
INSERT INTO "public"."boot_iot_device" VALUES (1, 1341620898007281665, 1341620898007281665, '2024-05-11 03:56:15.821857', '2024-05-11 03:56:15.821857', 0, 0, 0, '139c5556-8494-5753-ac97-de09f2a6a929', 'HFCL设备', 0, NULL, NULL, NULL, NULL, NULL, 1);

-- ----------------------------
-- Table structure for boot_iot_model
-- ----------------------------
DROP TABLE IF EXISTS "public"."boot_iot_model";
CREATE TABLE "public"."boot_iot_model" (
  "id" int8 NOT NULL GENERATED BY DEFAULT AS IDENTITY (
INCREMENT 1
MINVALUE  1
MAXVALUE 9223372036854775807
START 1
CACHE 1
),
  "creator" int8 NOT NULL DEFAULT 0,
  "editor" int8 NOT NULL DEFAULT 0,
  "create_time" timestamp(6) NOT NULL DEFAULT CURRENT_TIMESTAMP,
  "update_time" timestamp(6) NOT NULL DEFAULT CURRENT_TIMESTAMP,
  "del_flag" int2 NOT NULL DEFAULT 0,
  "version" int4 NOT NULL DEFAULT 0,
  "tenant_id" int8 NOT NULL DEFAULT 0,
  "name" varchar(50) COLLATE "pg_catalog"."default" NOT NULL,
  "code" varchar(50) COLLATE "pg_catalog"."default" NOT NULL,
  "data_type" varchar(20) COLLATE "pg_catalog"."default" NOT NULL,
  "category" int2 NOT NULL,
  "rw_type" varchar(30) COLLATE "pg_catalog"."default" NOT NULL,
  "expression" varchar(200) COLLATE "pg_catalog"."default",
  "sort" int4 NOT NULL DEFAULT 1,
  "specs" json,
  "remark" varchar(400) COLLATE "pg_catalog"."default"
)
;
COMMENT ON COLUMN "public"."boot_iot_model"."id" IS 'ID';
COMMENT ON COLUMN "public"."boot_iot_model"."creator" IS '创建人';
COMMENT ON COLUMN "public"."boot_iot_model"."editor" IS '编辑人';
COMMENT ON COLUMN "public"."boot_iot_model"."create_time" IS '创建时间';
COMMENT ON COLUMN "public"."boot_iot_model"."update_time" IS '修改时间';
COMMENT ON COLUMN "public"."boot_iot_model"."del_flag" IS '删除标识 0未删除 1已删除';
COMMENT ON COLUMN "public"."boot_iot_model"."version" IS '版本号';
COMMENT ON COLUMN "public"."boot_iot_model"."tenant_id" IS '租户ID';
COMMENT ON COLUMN "public"."boot_iot_model"."name" IS '模型名称';
COMMENT ON COLUMN "public"."boot_iot_model"."code" IS '模型编码';
COMMENT ON COLUMN "public"."boot_iot_model"."data_type" IS '数据类型 integer string decimal boolean double';
COMMENT ON COLUMN "public"."boot_iot_model"."category" IS '模型类别 1属性 2事件';
COMMENT ON COLUMN "public"."boot_iot_model"."rw_type" IS '读写类型 read读 write写';
COMMENT ON COLUMN "public"."boot_iot_model"."expression" IS '表达式';
COMMENT ON COLUMN "public"."boot_iot_model"."sort" IS '排序';
COMMENT ON COLUMN "public"."boot_iot_model"."specs" IS '规则说明';
COMMENT ON COLUMN "public"."boot_iot_model"."remark" IS '备注';
COMMENT ON TABLE "public"."boot_iot_model" IS '模型';

-- ----------------------------
-- Records of boot_iot_model
-- ----------------------------

-- ----------------------------
-- Table structure for boot_iot_product
-- ----------------------------
DROP TABLE IF EXISTS "public"."boot_iot_product";
CREATE TABLE "public"."boot_iot_product" (
  "id" int8 NOT NULL GENERATED BY DEFAULT AS IDENTITY (
INCREMENT 1
MINVALUE  1
MAXVALUE 9223372036854775807
START 1
CACHE 1
),
  "creator" int8 NOT NULL DEFAULT 0,
  "editor" int8 NOT NULL DEFAULT 0,
  "create_time" timestamp(6) NOT NULL DEFAULT CURRENT_TIMESTAMP,
  "update_time" timestamp(6) NOT NULL DEFAULT CURRENT_TIMESTAMP,
  "del_flag" int2 NOT NULL DEFAULT 0,
  "version" int4 NOT NULL DEFAULT 0,
  "tenant_id" int8 NOT NULL DEFAULT 0,
  "name" varchar(50) COLLATE "pg_catalog"."default" NOT NULL,
  "category_id" int8 NOT NULL,
  "device_type" int2 NOT NULL,
  "img_url" varchar(400) COLLATE "pg_catalog"."default",
  "cp_id" int8 NOT NULL,
  "tp_id" int8 NOT NULL,
  "remark" varchar(400) COLLATE "pg_catalog"."default"
)
;
COMMENT ON COLUMN "public"."boot_iot_product"."id" IS 'ID';
COMMENT ON COLUMN "public"."boot_iot_product"."creator" IS '创建人';
COMMENT ON COLUMN "public"."boot_iot_product"."editor" IS '编辑人';
COMMENT ON COLUMN "public"."boot_iot_product"."create_time" IS '创建时间';
COMMENT ON COLUMN "public"."boot_iot_product"."update_time" IS '修改时间';
COMMENT ON COLUMN "public"."boot_iot_product"."del_flag" IS '删除标识 0未删除 1已删除';
COMMENT ON COLUMN "public"."boot_iot_product"."version" IS '版本号';
COMMENT ON COLUMN "public"."boot_iot_product"."tenant_id" IS '租户ID';
COMMENT ON COLUMN "public"."boot_iot_product"."name" IS '产品名称';
COMMENT ON COLUMN "public"."boot_iot_product"."category_id" IS '产品类别';
COMMENT ON COLUMN "public"."boot_iot_product"."device_type" IS '设备类型 1直连设备 2网关设备 3监控设备';
COMMENT ON COLUMN "public"."boot_iot_product"."img_url" IS '产品图片URL';
COMMENT ON COLUMN "public"."boot_iot_product"."cp_id" IS '通讯协议ID';
COMMENT ON COLUMN "public"."boot_iot_product"."tp_id" IS '传输协议ID';
COMMENT ON COLUMN "public"."boot_iot_product"."remark" IS '备注';
COMMENT ON TABLE "public"."boot_iot_product" IS '产品';

-- ----------------------------
-- Records of boot_iot_product
-- ----------------------------

-- ----------------------------
-- Table structure for boot_iot_product_category
-- ----------------------------
DROP TABLE IF EXISTS "public"."boot_iot_product_category";
CREATE TABLE "public"."boot_iot_product_category" (
  "id" int8 NOT NULL GENERATED BY DEFAULT AS IDENTITY (
INCREMENT 1
MINVALUE  1
MAXVALUE 9223372036854775807
START 1
CACHE 1
),
  "creator" int8 NOT NULL DEFAULT 0,
  "editor" int8 NOT NULL DEFAULT 0,
  "create_time" timestamp(6) NOT NULL DEFAULT CURRENT_TIMESTAMP,
  "update_time" timestamp(6) NOT NULL DEFAULT CURRENT_TIMESTAMP,
  "del_flag" int2 NOT NULL DEFAULT 0,
  "version" int4 NOT NULL DEFAULT 0,
  "tenant_id" int8 NOT NULL DEFAULT 0,
  "name" varchar(50) COLLATE "pg_catalog"."default" NOT NULL,
  "sort" int4 NOT NULL,
  "pid" int8 NOT NULL,
  "remark" varchar(400) COLLATE "pg_catalog"."default"
)
;
COMMENT ON COLUMN "public"."boot_iot_product_category"."id" IS 'ID';
COMMENT ON COLUMN "public"."boot_iot_product_category"."creator" IS '创建人';
COMMENT ON COLUMN "public"."boot_iot_product_category"."editor" IS '编辑人';
COMMENT ON COLUMN "public"."boot_iot_product_category"."create_time" IS '创建时间';
COMMENT ON COLUMN "public"."boot_iot_product_category"."update_time" IS '修改时间';
COMMENT ON COLUMN "public"."boot_iot_product_category"."del_flag" IS '删除标识 0未删除 1已删除';
COMMENT ON COLUMN "public"."boot_iot_product_category"."version" IS '版本号';
COMMENT ON COLUMN "public"."boot_iot_product_category"."tenant_id" IS '租户ID';
COMMENT ON COLUMN "public"."boot_iot_product_category"."name" IS '产品类别名称';
COMMENT ON COLUMN "public"."boot_iot_product_category"."sort" IS '排序';
COMMENT ON COLUMN "public"."boot_iot_product_category"."pid" IS '产品类别父节点ID';
COMMENT ON COLUMN "public"."boot_iot_product_category"."remark" IS '备注';
COMMENT ON TABLE "public"."boot_iot_product_category" IS '产品类别';

-- ----------------------------
-- Records of boot_iot_product_category
-- ----------------------------

-- ----------------------------
-- Table structure for boot_iot_product_model
-- ----------------------------
DROP TABLE IF EXISTS "public"."boot_iot_product_model";
CREATE TABLE "public"."boot_iot_product_model" (
  "id" int8 NOT NULL GENERATED BY DEFAULT AS IDENTITY (
INCREMENT 1
MINVALUE  1
MAXVALUE 9223372036854775807
START 1
CACHE 1
),
  "creator" int8 NOT NULL DEFAULT 0,
  "editor" int8 NOT NULL DEFAULT 0,
  "create_time" timestamp(6) NOT NULL DEFAULT CURRENT_TIMESTAMP,
  "update_time" timestamp(6) NOT NULL DEFAULT CURRENT_TIMESTAMP,
  "del_flag" int2 NOT NULL DEFAULT 0,
  "version" int4 NOT NULL DEFAULT 0,
  "tenant_id" int8 NOT NULL DEFAULT 0,
  "product_id" int8 NOT NULL,
  "model_id" int8 NOT NULL
)
;
COMMENT ON COLUMN "public"."boot_iot_product_model"."id" IS 'ID';
COMMENT ON COLUMN "public"."boot_iot_product_model"."creator" IS '创建人';
COMMENT ON COLUMN "public"."boot_iot_product_model"."editor" IS '编辑人';
COMMENT ON COLUMN "public"."boot_iot_product_model"."create_time" IS '创建时间';
COMMENT ON COLUMN "public"."boot_iot_product_model"."update_time" IS '修改时间';
COMMENT ON COLUMN "public"."boot_iot_product_model"."del_flag" IS '删除标识 0未删除 1已删除';
COMMENT ON COLUMN "public"."boot_iot_product_model"."version" IS '版本号';
COMMENT ON COLUMN "public"."boot_iot_product_model"."tenant_id" IS '租户ID';
COMMENT ON COLUMN "public"."boot_iot_product_model"."product_id" IS '产品ID';
COMMENT ON COLUMN "public"."boot_iot_product_model"."model_id" IS '模型ID';
COMMENT ON TABLE "public"."boot_iot_product_model" IS '产品模型';

-- ----------------------------
-- Records of boot_iot_product_model
-- ----------------------------

-- ----------------------------
-- Table structure for boot_iot_tp
-- ----------------------------
DROP TABLE IF EXISTS "public"."boot_iot_tp";
CREATE TABLE "public"."boot_iot_tp" (
  "id" int8 NOT NULL GENERATED BY DEFAULT AS IDENTITY (
INCREMENT 1
MINVALUE  1
MAXVALUE 9223372036854775807
START 1
CACHE 1
),
  "creator" int8 NOT NULL DEFAULT 0,
  "editor" int8 NOT NULL DEFAULT 0,
  "create_time" timestamp(6) NOT NULL DEFAULT CURRENT_TIMESTAMP,
  "update_time" timestamp(6) NOT NULL DEFAULT CURRENT_TIMESTAMP,
  "del_flag" int2 NOT NULL DEFAULT 0,
  "version" int4 NOT NULL DEFAULT 0,
  "tenant_id" int8 NOT NULL DEFAULT 0
)
;
COMMENT ON COLUMN "public"."boot_iot_tp"."id" IS 'ID';
COMMENT ON COLUMN "public"."boot_iot_tp"."creator" IS '创建人';
COMMENT ON COLUMN "public"."boot_iot_tp"."editor" IS '编辑人';
COMMENT ON COLUMN "public"."boot_iot_tp"."create_time" IS '创建时间';
COMMENT ON COLUMN "public"."boot_iot_tp"."update_time" IS '修改时间';
COMMENT ON COLUMN "public"."boot_iot_tp"."del_flag" IS '删除标识 0未删除 1已删除';
COMMENT ON COLUMN "public"."boot_iot_tp"."version" IS '版本号';
COMMENT ON COLUMN "public"."boot_iot_tp"."tenant_id" IS '租户ID';
COMMENT ON TABLE "public"."boot_iot_tp" IS '传输协议';

-- ----------------------------
-- Records of boot_iot_tp
-- ----------------------------

-- ----------------------------
-- Alter sequences owned by
-- ----------------------------
ALTER SEQUENCE "public"."boot_iot_cp_id_seq"
OWNED BY "public"."boot_iot_cp"."id";
SELECT setval('"public"."boot_iot_cp_id_seq"', 1, false);

-- ----------------------------
-- Alter sequences owned by
-- ----------------------------
ALTER SEQUENCE "public"."boot_iot_device_id_seq"
OWNED BY "public"."boot_iot_device"."id";
SELECT setval('"public"."boot_iot_device_id_seq"', 1, false);

-- ----------------------------
-- Alter sequences owned by
-- ----------------------------
ALTER SEQUENCE "public"."boot_iot_device_id_seq1"
OWNED BY "public"."boot_iot_device"."id";
SELECT setval('"public"."boot_iot_device_id_seq1"', 1, false);

-- ----------------------------
-- Alter sequences owned by
-- ----------------------------
ALTER SEQUENCE "public"."boot_iot_device_id_seq2"
OWNED BY "public"."boot_iot_device"."id";
SELECT setval('"public"."boot_iot_device_id_seq2"', 1, false);

-- ----------------------------
-- Alter sequences owned by
-- ----------------------------
ALTER SEQUENCE "public"."boot_iot_device_id_seq3"
OWNED BY "public"."boot_iot_device"."id";
SELECT setval('"public"."boot_iot_device_id_seq3"', 1, false);

-- ----------------------------
-- Alter sequences owned by
-- ----------------------------
ALTER SEQUENCE "public"."boot_iot_model_id_seq"
OWNED BY "public"."boot_iot_model"."id";
SELECT setval('"public"."boot_iot_model_id_seq"', 1, false);

-- ----------------------------
-- Alter sequences owned by
-- ----------------------------
ALTER SEQUENCE "public"."boot_iot_model_id_seq1"
OWNED BY "public"."boot_iot_model"."id";
SELECT setval('"public"."boot_iot_model_id_seq1"', 1, false);

-- ----------------------------
-- Alter sequences owned by
-- ----------------------------
ALTER SEQUENCE "public"."boot_iot_model_id_seq2"
OWNED BY "public"."boot_iot_model"."id";
SELECT setval('"public"."boot_iot_model_id_seq2"', 1, false);

-- ----------------------------
-- Alter sequences owned by
-- ----------------------------
ALTER SEQUENCE "public"."boot_iot_network_id_seq"
OWNED BY "public"."boot_iot_tp"."id";
SELECT setval('"public"."boot_iot_network_id_seq"', 1, false);

-- ----------------------------
-- Alter sequences owned by
-- ----------------------------
ALTER SEQUENCE "public"."boot_iot_network_id_seq1"
OWNED BY "public"."boot_iot_tp"."id";
SELECT setval('"public"."boot_iot_network_id_seq1"', 1, false);

-- ----------------------------
-- Alter sequences owned by
-- ----------------------------
ALTER SEQUENCE "public"."boot_iot_product_category_id_seq"
OWNED BY "public"."boot_iot_product_category"."id";
SELECT setval('"public"."boot_iot_product_category_id_seq"', 1, false);

-- ----------------------------
-- Alter sequences owned by
-- ----------------------------
ALTER SEQUENCE "public"."boot_iot_product_id_seq"
OWNED BY "public"."boot_iot_product"."id";
SELECT setval('"public"."boot_iot_product_id_seq"', 1, false);

-- ----------------------------
-- Alter sequences owned by
-- ----------------------------
ALTER SEQUENCE "public"."boot_iot_product_id_seq1"
OWNED BY "public"."boot_iot_product"."id";
SELECT setval('"public"."boot_iot_product_id_seq1"', 1, false);

-- ----------------------------
-- Alter sequences owned by
-- ----------------------------
ALTER SEQUENCE "public"."boot_iot_product_id_seq2"
OWNED BY "public"."boot_iot_product"."id";
SELECT setval('"public"."boot_iot_product_id_seq2"', 1, false);

-- ----------------------------
-- Alter sequences owned by
-- ----------------------------
ALTER SEQUENCE "public"."boot_iot_product_model_id_seq"
OWNED BY "public"."boot_iot_product_model"."id";
SELECT setval('"public"."boot_iot_product_model_id_seq"', 1, false);

-- ----------------------------
-- Alter sequences owned by
-- ----------------------------
ALTER SEQUENCE "public"."boot_iot_product_model_id_seq1"
OWNED BY "public"."boot_iot_product_model"."id";
SELECT setval('"public"."boot_iot_product_model_id_seq1"', 1, false);

-- ----------------------------
-- Alter sequences owned by
-- ----------------------------
ALTER SEQUENCE "public"."boot_iot_product_type_id_seq"
OWNED BY "public"."boot_iot_product_category"."id";
SELECT setval('"public"."boot_iot_product_type_id_seq"', 1, false);

-- ----------------------------
-- Alter sequences owned by
-- ----------------------------
ALTER SEQUENCE "public"."boot_iot_product_type_id_seq1"
OWNED BY "public"."boot_iot_product_category"."id";
SELECT setval('"public"."boot_iot_product_type_id_seq1"', 1, false);

-- ----------------------------
-- Alter sequences owned by
-- ----------------------------
ALTER SEQUENCE "public"."boot_iot_protocol_id_seq"
OWNED BY "public"."boot_iot_cp"."id";
SELECT setval('"public"."boot_iot_protocol_id_seq"', 1, false);

-- ----------------------------
-- Alter sequences owned by
-- ----------------------------
ALTER SEQUENCE "public"."boot_iot_protocol_id_seq1"
OWNED BY "public"."boot_iot_cp"."id";
SELECT setval('"public"."boot_iot_protocol_id_seq1"', 1, false);

-- ----------------------------
-- Alter sequences owned by
-- ----------------------------
ALTER SEQUENCE "public"."boot_iot_tp_id_seq"
OWNED BY "public"."boot_iot_tp"."id";
SELECT setval('"public"."boot_iot_tp_id_seq"', 1, false);

-- ----------------------------
-- Auto increment value for boot_iot_cp
-- ----------------------------
SELECT setval('"public"."boot_iot_cp_id_seq"', 1, false);

-- ----------------------------
-- Primary Key structure for table boot_iot_cp
-- ----------------------------
ALTER TABLE "public"."boot_iot_cp" ADD CONSTRAINT "boot_iot_protocol_pkey" PRIMARY KEY ("id");

-- ----------------------------
-- Auto increment value for boot_iot_device
-- ----------------------------
SELECT setval('"public"."boot_iot_device_id_seq3"', 1, false);

-- ----------------------------
-- Primary Key structure for table boot_iot_device
-- ----------------------------
ALTER TABLE "public"."boot_iot_device" ADD CONSTRAINT "boot_iot_device_pkey" PRIMARY KEY ("id");

-- ----------------------------
-- Auto increment value for boot_iot_model
-- ----------------------------
SELECT setval('"public"."boot_iot_model_id_seq2"', 1, false);

-- ----------------------------
-- Indexes structure for table boot_iot_model
-- ----------------------------
CREATE UNIQUE INDEX "boot_iot_model_name_code_tenant_id_idx" ON "public"."boot_iot_model" USING btree (
  "name" COLLATE "pg_catalog"."default" "pg_catalog"."text_ops" ASC NULLS LAST,
  "code" COLLATE "pg_catalog"."default" "pg_catalog"."text_ops" ASC NULLS LAST,
  "tenant_id" "pg_catalog"."int8_ops" ASC NULLS LAST
);
COMMENT ON INDEX "public"."boot_iot_model_name_code_tenant_id_idx" IS '名称_编码_租户ID_唯一索引';

-- ----------------------------
-- Primary Key structure for table boot_iot_model
-- ----------------------------
ALTER TABLE "public"."boot_iot_model" ADD CONSTRAINT "boot_iot_model_pkey" PRIMARY KEY ("id");

-- ----------------------------
-- Auto increment value for boot_iot_product
-- ----------------------------
SELECT setval('"public"."boot_iot_product_id_seq2"', 1, false);

-- ----------------------------
-- Primary Key structure for table boot_iot_product
-- ----------------------------
ALTER TABLE "public"."boot_iot_product" ADD CONSTRAINT "boot_iot_product_pkey" PRIMARY KEY ("id");

-- ----------------------------
-- Auto increment value for boot_iot_product_category
-- ----------------------------
SELECT setval('"public"."boot_iot_product_category_id_seq"', 1, false);

-- ----------------------------
-- Primary Key structure for table boot_iot_product_category
-- ----------------------------
ALTER TABLE "public"."boot_iot_product_category" ADD CONSTRAINT "boot_iot_product_type_pkey" PRIMARY KEY ("id");

-- ----------------------------
-- Auto increment value for boot_iot_product_model
-- ----------------------------
SELECT setval('"public"."boot_iot_product_model_id_seq1"', 1, false);

-- ----------------------------
-- Primary Key structure for table boot_iot_product_model
-- ----------------------------
ALTER TABLE "public"."boot_iot_product_model" ADD CONSTRAINT "boot_iot_product_model_pkey" PRIMARY KEY ("id");

-- ----------------------------
-- Auto increment value for boot_iot_tp
-- ----------------------------
SELECT setval('"public"."boot_iot_tp_id_seq"', 1, false);

-- ----------------------------
-- Primary Key structure for table boot_iot_tp
-- ----------------------------
ALTER TABLE "public"."boot_iot_tp" ADD CONSTRAINT "boot_iot_network_pkey" PRIMARY KEY ("id");
