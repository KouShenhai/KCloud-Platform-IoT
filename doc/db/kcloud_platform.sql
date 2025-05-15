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

-- ----------------------------
-- -------------集群------------
-- ----------------------------
DROP TABLE IF EXISTS "public"."boot_sys_cluster";
CREATE TABLE "public"."boot_sys_cluster" (
 "id" int8 NOT NULL,
 "creator" int8 NOT NULL DEFAULT 0,
 "editor" int8 NOT NULL DEFAULT 0,
 "create_time" timestamp(6) NOT NULL DEFAULT CURRENT_TIMESTAMP,
 "update_time" timestamp(6) NOT NULL DEFAULT CURRENT_TIMESTAMP,
 "del_flag" int2 NOT NULL DEFAULT 0,
 "version" int4 NOT NULL DEFAULT 0,
 "tenant_id" int8 NOT NULL DEFAULT 0,
 "name" varchar(50) NOT NULL,
 "code" varchar(50) NOT NULL,
 "remark" varchar(200) NOT NULL
);
COMMENT ON COLUMN "public"."boot_sys_cluster"."id" IS 'ID';
COMMENT ON COLUMN "public"."boot_sys_cluster"."creator" IS '创建人';
COMMENT ON COLUMN "public"."boot_sys_cluster"."editor" IS '编辑人';
COMMENT ON COLUMN "public"."boot_sys_cluster"."create_time" IS '创建时间';
COMMENT ON COLUMN "public"."boot_sys_cluster"."update_time" IS '修改时间';
COMMENT ON COLUMN "public"."boot_sys_cluster"."del_flag" IS '删除标识 0未删除 1已删除';
COMMENT ON COLUMN "public"."boot_sys_cluster"."version" IS '版本号';
COMMENT ON COLUMN "public"."boot_sys_cluster"."tenant_id" IS '租户ID';
COMMENT ON COLUMN "public"."boot_sys_cluster"."name" IS '集群名称';
COMMENT ON COLUMN "public"."boot_sys_cluster"."code" IS '集群编码';
COMMENT ON COLUMN "public"."boot_sys_cluster"."remark" IS '集群备注';
COMMENT ON TABLE "public"."boot_sys_cluster" IS '集群';

ALTER TABLE "public"."boot_sys_cluster" ADD CONSTRAINT "boot_sys_cluster_pkey" PRIMARY KEY ("id");

-- ----------------------------
-- -------------部门------------
-- ----------------------------
DROP TABLE IF EXISTS "public"."boot_sys_dept";
CREATE TABLE "public"."boot_sys_dept" (
  "id" int8 NOT NULL,
  "creator" int8 NOT NULL DEFAULT 0,
  "editor" int8 NOT NULL DEFAULT 0,
  "create_time" timestamp(6) NOT NULL DEFAULT CURRENT_TIMESTAMP,
  "update_time" timestamp(6) NOT NULL DEFAULT CURRENT_TIMESTAMP,
  "del_flag" int2 NOT NULL DEFAULT 0,
  "version" int4 NOT NULL DEFAULT 0,
  "tenant_id" int8 NOT NULL DEFAULT 0,
  "pid" int8 NOT NULL,
  "name" varchar(100) NOT NULL,
  "path" text NOT NULL DEFAULT '0',
  "sort" int4 NOT NULL DEFAULT 1
);
COMMENT ON COLUMN "public"."boot_sys_dept"."id" IS 'ID';
COMMENT ON COLUMN "public"."boot_sys_dept"."creator" IS '创建人';
COMMENT ON COLUMN "public"."boot_sys_dept"."editor" IS '编辑人';
COMMENT ON COLUMN "public"."boot_sys_dept"."create_time" IS '创建时间';
COMMENT ON COLUMN "public"."boot_sys_dept"."update_time" IS '修改时间';
COMMENT ON COLUMN "public"."boot_sys_dept"."del_flag" IS '删除标识 0未删除 1已删除';
COMMENT ON COLUMN "public"."boot_sys_dept"."version" IS '版本号';
COMMENT ON COLUMN "public"."boot_sys_dept"."tenant_id" IS '租户ID';
COMMENT ON COLUMN "public"."boot_sys_dept"."pid" IS '部门父节点ID';
COMMENT ON COLUMN "public"."boot_sys_dept"."name" IS '部门名称';
COMMENT ON COLUMN "public"."boot_sys_dept"."path" IS '部门节点';
COMMENT ON COLUMN "public"."boot_sys_dept"."sort" IS '部门排序';
COMMENT ON TABLE "public"."boot_sys_dept" IS '部门';

ALTER TABLE "public"."boot_sys_dept" ADD CONSTRAINT "boot_sys_dept_pkey" PRIMARY KEY ("id");

INSERT INTO "public"."boot_sys_dept" VALUES (1535858679453085698, 1341620898007281665, 1341620898007281665, '2022-11-02 22:35:30', '2023-09-22 11:31:42', 0, 4, 0, 1535887940687765505, '广州分公司', '0,1535887940687765505,1535858679453085698', 666);
INSERT INTO "public"."boot_sys_dept" VALUES (1535881356595175426, 1341620898007281665, 1341620898007281665, '2022-11-02 22:35:30', '2023-09-22 11:31:42', 0, 18, 0, 1535887940687765505, '长沙分公司', '0,1535887940687765505,1535881356595175426', 111);
INSERT INTO "public"."boot_sys_dept" VALUES (1535887129341599746, 1341620898007281665, 1341620898007281665, '2022-11-02 22:35:30', '2023-09-22 11:31:42', 0, 8, 0, 1535887940687765505, '深圳分公司', '0,1535887940687765505,1535887129341599746', 888);
INSERT INTO "public"."boot_sys_dept" VALUES (1535887940687765505, 1341620898007281665, 1341620898007281665, '2022-11-16 12:12:55', '2023-09-22 11:31:42', 0, 38, 0, 0, '老寇云集团', '0,1535887940687765505', 1000);
INSERT INTO "public"."boot_sys_dept" VALUES (1584488175088373761, 1341620898007281665, 1341620898007281665, '2022-11-02 22:35:30', '2023-09-22 11:31:42', 0, 43, 0, 1535881356595175426, '研发部', '0,1535887940687765505,1535881356595175426,1584488175088373761', 20);
INSERT INTO "public"."boot_sys_dept" VALUES (1584488411756171265, 1341620898007281665, 1341620898007281665, '2022-11-02 22:35:30', '2023-09-22 11:31:42', 0, 9, 0, 1535881356595175426, '市场部', '0,1535887940687765505,1535881356595175426,1584488411756171265', 10);
INSERT INTO "public"."boot_sys_dept" VALUES (1584488411756171266, 1341620898007281665, 1341620898007281665, '2022-11-02 22:35:30', '2023-09-22 11:31:42', 0, 19, 0, 1584488175088373761, '开发组', '0,1535887940687765505,1535881356595175426,1584488175088373761,1584488411756171266', 15);
INSERT INTO "public"."boot_sys_dept" VALUES (1584488411756171268, 1341620898007281665, 1341620898007281665, '2023-02-10 22:01:36', '2023-09-22 11:31:42', 0, 9, 0, 1584488175088373761, '运维组', '0,1535887940687765505,1535881356595175426,1584488175088373761,1584488411756171268', 5);
INSERT INTO "public"."boot_sys_dept" VALUES (1584488411756171269, 1341620898007281665, 1341620898007281665, '2023-02-10 22:06:44', '2023-09-22 11:31:42', 0, 8, 0, 1584488175088373761, '测试组', '0,1535887940687765505,1535881356595175426,1584488175088373761,1584488411756171269', 10);

-- ----------------------------
-- -------------字典------------
-- ----------------------------
DROP TABLE IF EXISTS "public"."boot_sys_dict";
CREATE TABLE "public"."boot_sys_dict" (
  "id" int8 NOT NULL,
  "creator" int8 NOT NULL DEFAULT 0,
  "editor" int8 NOT NULL DEFAULT 0,
  "create_time" timestamp(6) NOT NULL DEFAULT CURRENT_TIMESTAMP,
  "update_time" timestamp(6) NOT NULL DEFAULT CURRENT_TIMESTAMP,
  "del_flag" int2 NOT NULL DEFAULT 0,
  "version" int4 NOT NULL DEFAULT 0,
  "tenant_id" int8 NOT NULL DEFAULT 0,
  "name" varchar(100) NOT NULL,
  "type" varchar(100) NOT NULL,
  "remark" varchar(500),
  "status" int2 NOT NULL DEFAULT 0
);
COMMENT ON COLUMN "public"."boot_sys_dict"."id" IS 'ID';
COMMENT ON COLUMN "public"."boot_sys_dict"."creator" IS '创建人';
COMMENT ON COLUMN "public"."boot_sys_dict"."editor" IS '编辑人';
COMMENT ON COLUMN "public"."boot_sys_dict"."create_time" IS '创建时间';
COMMENT ON COLUMN "public"."boot_sys_dict"."update_time" IS '修改时间';
COMMENT ON COLUMN "public"."boot_sys_dict"."del_flag" IS '删除标识 0未删除 1已删除';
COMMENT ON COLUMN "public"."boot_sys_dict"."version" IS '版本号';
COMMENT ON COLUMN "public"."boot_sys_dict"."tenant_id" IS '租户ID';
COMMENT ON COLUMN "public"."boot_sys_dict"."name" IS '字典名称';
COMMENT ON COLUMN "public"."boot_sys_dict"."type" IS '字典类型';
COMMENT ON COLUMN "public"."boot_sys_dict"."remark" IS '字典备注';
COMMENT ON COLUMN "public"."boot_sys_dict"."status" IS '字典状态 0启用 1停用';
COMMENT ON TABLE "public"."boot_sys_dict" IS '字典';

ALTER TABLE "public"."boot_sys_dict" ADD CONSTRAINT "boot_sys_dict_pkey" PRIMARY KEY ("id");

INSERT INTO "public"."boot_sys_dict" VALUES (1, 1341620898007281665, 1341620898007281665, '2024-05-09 00:15:30', '2024-05-09 00:15:40', 0, 0, 0, 'dict-type.manage.name.menu.show-hide', 'sys_menu_show_hide', '菜单状态列表', 0);

-- ----------------------------
-- -------------字典项------------
-- ----------------------------
DROP TABLE IF EXISTS "public"."boot_sys_dict_item";
CREATE TABLE "public"."boot_sys_dict_item" (
   "id" int8 NOT NULL,
   "creator" int8 NOT NULL DEFAULT 0,
   "editor" int8 NOT NULL DEFAULT 0,
   "create_time" timestamp(6) NOT NULL DEFAULT CURRENT_TIMESTAMP,
   "update_time" timestamp(6) NOT NULL DEFAULT CURRENT_TIMESTAMP,
   "del_flag" int2 NOT NULL DEFAULT 0,
   "version" int4 NOT NULL DEFAULT 0,
   "tenant_id" int8 NOT NULL DEFAULT 0,
   "label" varchar(100) NOT NULL,
   "value" varchar(100) NOT NULL,
   "sort" int4 NOT NULL DEFAULT 1,
   "remark" varchar(500),
   "status" int2 NOT NULL DEFAULT 0,
   "type_id" int8 NOT NULL
);
COMMENT ON COLUMN "public"."boot_sys_dict_item"."id" IS 'ID';
COMMENT ON COLUMN "public"."boot_sys_dict_item"."creator" IS '创建人';
COMMENT ON COLUMN "public"."boot_sys_dict_item"."editor" IS '编辑人';
COMMENT ON COLUMN "public"."boot_sys_dict_item"."create_time" IS '创建时间';
COMMENT ON COLUMN "public"."boot_sys_dict_item"."update_time" IS '修改时间';
COMMENT ON COLUMN "public"."boot_sys_dict_item"."del_flag" IS '删除标识 0未删除 1已删除';
COMMENT ON COLUMN "public"."boot_sys_dict_item"."version" IS '版本号';
COMMENT ON COLUMN "public"."boot_sys_dict_item"."tenant_id" IS '租户ID';
COMMENT ON COLUMN "public"."boot_sys_dict_item"."label" IS '字典标签';
COMMENT ON COLUMN "public"."boot_sys_dict_item"."value" IS '字典值';
COMMENT ON COLUMN "public"."boot_sys_dict_item"."sort" IS '字典排序';
COMMENT ON COLUMN "public"."boot_sys_dict_item"."remark" IS '字典备注';
COMMENT ON COLUMN "public"."boot_sys_dict_item"."status" IS '字典状态 0启用 1停用';
COMMENT ON COLUMN "public"."boot_sys_dict_item"."type_id" IS '类型ID';
COMMENT ON TABLE "public"."boot_sys_dict_item" IS '字典项';

ALTER TABLE "public"."boot_sys_dict_item" ADD CONSTRAINT "boot_sys_dict_item_pkey" PRIMARY KEY ("id");

CREATE UNIQUE INDEX "boot_sys_dict_type_tenantId_idx" ON "public"."boot_sys_dict" USING btree (
   "type",
   "tenant_id"
);
COMMENT ON INDEX "public"."boot_sys_dict_type_tenantId_idx" IS '类型_租户ID_唯一索引';

-- ----------------------------
-- -------------国际化消息------------
-- ----------------------------
DROP TABLE IF EXISTS "public"."boot_sys_i18n_message";
CREATE TABLE "public"."boot_sys_i18n_message" (
  "id" int8 NOT NULL,
  "creator" int8 NOT NULL DEFAULT 0,
  "editor" int8 NOT NULL DEFAULT 0,
  "create_time" timestamp(6) NOT NULL DEFAULT CURRENT_TIMESTAMP,
  "update_time" timestamp(6) NOT NULL DEFAULT CURRENT_TIMESTAMP,
  "del_flag" int2 NOT NULL DEFAULT 0,
  "version" int4 NOT NULL DEFAULT 0,
  "tenant_id" int8 NOT NULL DEFAULT 0,
  "code" varchar(50) NOT NULL,
  "zh_message" varchar(50) NOT NULL,
  "en_message" varchar(50) NOT NULL
);
COMMENT ON COLUMN "public"."boot_sys_i18n_message"."id" IS 'ID';
COMMENT ON COLUMN "public"."boot_sys_i18n_message"."creator" IS '创建人';
COMMENT ON COLUMN "public"."boot_sys_i18n_message"."editor" IS '编辑人';
COMMENT ON COLUMN "public"."boot_sys_i18n_message"."create_time" IS '创建时间';
COMMENT ON COLUMN "public"."boot_sys_i18n_message"."update_time" IS '修改时间';
COMMENT ON COLUMN "public"."boot_sys_i18n_message"."del_flag" IS '删除标识 0未删除 1已删除';
COMMENT ON COLUMN "public"."boot_sys_i18n_message"."version" IS '版本号';
COMMENT ON COLUMN "public"."boot_sys_i18n_message"."tenant_id" IS '租户ID';
COMMENT ON COLUMN "public"."boot_sys_i18n_message"."code" IS '编码';
COMMENT ON COLUMN "public"."boot_sys_i18n_message"."zh_message" IS '中文';
COMMENT ON COLUMN "public"."boot_sys_i18n_message"."en_message" IS '英文';
COMMENT ON TABLE "public"."boot_sys_i18n_message" IS '国际化消息';

CREATE INDEX "boot_sys_i18n_message_code_tenantId_idx" ON "public"."boot_sys_i18n_message" USING btree (
  "code",
  "tenant_id"
);
COMMENT ON INDEX "public"."boot_sys_i18n_message_code_tenantId_idx" IS '编码_租户ID_唯一索引';

ALTER TABLE "public"."boot_sys_i18n_message" ADD CONSTRAINT "boot_sys_i18n_message_pkey" PRIMARY KEY ("id");

INSERT INTO "public"."boot_sys_i18n_message" VALUES (1, 1341620898007281665, 1341620898007281665, '2024-05-07 10:45:30', '2024-05-07 10:45:37', 0, 0, 0, 'sys.manage', '系统管理', 'System Management');
INSERT INTO "public"."boot_sys_i18n_message" VALUES (2, 1341620898007281665, 1341620898007281665, '2024-05-07 10:45:30', '2024-05-07 10:45:37', 0, 0, 0, 'menu.manage', '菜单管理', 'Menu Management');
INSERT INTO "public"."boot_sys_i18n_message" VALUES (4, 1341620898007281665, 1341620898007281665, '2024-05-07 10:45:30', '2024-05-07 10:45:37', 0, 0, 0, 'dict.manage', '字典管理', 'Dictionary Management');

-- ----------------------------
-- -------------IP------------
-- ----------------------------
DROP TABLE IF EXISTS "public"."boot_sys_ip";
CREATE TABLE "public"."boot_sys_ip" (
	"id" int8 NOT NULL,
	"creator" int8 NOT NULL DEFAULT 0,
	"editor" int8 NOT NULL DEFAULT 0,
	"create_time" timestamp(6) NOT NULL DEFAULT CURRENT_TIMESTAMP,
	"update_time" timestamp(6) NOT NULL DEFAULT CURRENT_TIMESTAMP,
	"del_flag" int2 NOT NULL DEFAULT 0,
	"version" int4 NOT NULL DEFAULT 0,
	"tenant_id" int8 NOT NULL DEFAULT 0,
	"label" int2  NOT NULL default 0,
	"value" varchar(20)  NOT NULL
);
COMMENT ON COLUMN "public"."boot_sys_ip"."id" IS 'ID';
COMMENT ON COLUMN "public"."boot_sys_ip"."creator" IS '创建人';
COMMENT ON COLUMN "public"."boot_sys_ip"."editor" IS '编辑人';
COMMENT ON COLUMN "public"."boot_sys_ip"."create_time" IS '创建时间';
COMMENT ON COLUMN "public"."boot_sys_ip"."update_time" IS '修改时间';
COMMENT ON COLUMN "public"."boot_sys_ip"."del_flag" IS '删除标识 0未删除 1已删除';
COMMENT ON COLUMN "public"."boot_sys_ip"."version" IS '版本号';
COMMENT ON COLUMN "public"."boot_sys_ip"."tenant_id" IS '租户ID';
COMMENT ON COLUMN "public"."boot_sys_ip"."label" IS '类型 0白名单 1黑名单';
COMMENT ON COLUMN "public"."boot_sys_ip"."value" IS '值';
COMMENT ON TABLE "public"."boot_sys_ip" IS 'IP';

ALTER TABLE "public"."boot_sys_ip" ADD CONSTRAINT "boot_sys_ip_pkey" PRIMARY KEY ("id");

CREATE INDEX "boot_sys_ip_label_idx" ON "public"."boot_sys_ip" USING btree (
	"label"
);

-- ----------------------------
-- -------------菜单------------
-- ----------------------------
DROP TABLE IF EXISTS "public"."boot_sys_menu";
CREATE TABLE "public"."boot_sys_menu" (
  "id" int8 NOT NULL,
  "creator" int8 NOT NULL DEFAULT 0,
  "editor" int8 NOT NULL DEFAULT 0,
  "create_time" timestamp(6) NOT NULL DEFAULT CURRENT_TIMESTAMP,
  "update_time" timestamp(6) NOT NULL DEFAULT CURRENT_TIMESTAMP,
  "del_flag" int2 NOT NULL DEFAULT 0,
  "version" int4 NOT NULL DEFAULT 0,
  "tenant_id" int8 NOT NULL DEFAULT 0,
  "pid" int8 NOT NULL,
  "permission" varchar(200),
  "type" int2 NOT NULL DEFAULT 0,
  "name" varchar(100) NOT NULL,
  "path" varchar(100),
  "icon" varchar(50),
  "sort" int4 NOT NULL DEFAULT 1,
  "hidden" int2 NOT NULL DEFAULT 0,
  "status" int2 NOT NULL DEFAULT 0,
  "url" varchar(400)
);
COMMENT ON COLUMN "public"."boot_sys_menu"."id" IS 'ID';
COMMENT ON COLUMN "public"."boot_sys_menu"."creator" IS '创建人';
COMMENT ON COLUMN "public"."boot_sys_menu"."editor" IS '编辑人';
COMMENT ON COLUMN "public"."boot_sys_menu"."create_time" IS '创建时间';
COMMENT ON COLUMN "public"."boot_sys_menu"."update_time" IS '修改时间';
COMMENT ON COLUMN "public"."boot_sys_menu"."del_flag" IS '删除标识 0未删除 1已删除';
COMMENT ON COLUMN "public"."boot_sys_menu"."version" IS '版本号';
COMMENT ON COLUMN "public"."boot_sys_menu"."tenant_id" IS '租户ID';
COMMENT ON COLUMN "public"."boot_sys_menu"."pid" IS '菜单父节点ID';
COMMENT ON COLUMN "public"."boot_sys_menu"."permission" IS '菜单权限标识';
COMMENT ON COLUMN "public"."boot_sys_menu"."type" IS '菜单类型 0菜单 1按钮';
COMMENT ON COLUMN "public"."boot_sys_menu"."name" IS '菜单名称';
COMMENT ON COLUMN "public"."boot_sys_menu"."path" IS '菜单路径';
COMMENT ON COLUMN "public"."boot_sys_menu"."icon" IS '菜单图标';
COMMENT ON COLUMN "public"."boot_sys_menu"."sort" IS '菜单排序';
COMMENT ON COLUMN "public"."boot_sys_menu"."status" IS '菜单状态 0启用 1停用';
COMMENT ON TABLE "public"."boot_sys_menu" IS '菜单';

ALTER TABLE "public"."boot_sys_menu" ADD CONSTRAINT "boot_sys_menu_pkey" PRIMARY KEY ("id");

INSERT INTO "public"."boot_sys_menu" VALUES (1, 1, 1, '2024-06-04 17:20:42', '2024-06-04 17:20:46', 0, 0, 0, 0, NULL, 0, '系统管理', '/sys', 'SettingOutlined', 90000, 0);
INSERT INTO "public"."boot_sys_menu" VALUES (2, 1, 1, '2024-06-04 17:27:14', '2024-06-04 17:27:12', 0, 0, 0, 1, NULL, 0, '日志管理', '/sys/log', '', 1000, 0);
INSERT INTO "public"."boot_sys_menu" VALUES (3, 1, 1, '2024-09-15 12:56:07', '2024-09-15 12:56:10', 0, 0, 0, 2, NULL, 0, '登录日志', '/sys/log/login', '', 800, 0);
INSERT INTO "public"."boot_sys_menu" VALUES (4, 1, 1, '2024-06-04 17:27:14', '2024-06-04 17:27:12', 0, 0, 0, 3, 'sys:login-log:page', 1, '分页查询登录日志', NULL, '', 50, 0);
INSERT INTO "public"."boot_sys_menu" VALUES (5, 1, 1, '2024-06-04 17:27:14', '2024-06-04 17:27:12', 0, 0, 0, 3, 'sys:login-log:export', 1, '导出全部登录日志', NULL, '', 40, 0);
INSERT INTO "public"."boot_sys_menu" VALUES (8, 1, 1, '2024-09-17 18:36:56', '2024-09-17 18:36:59', 0, 0, 0, 2, NULL, 0, '通知日志', '/sys/log/notice', '', 700, 0);
INSERT INTO "public"."boot_sys_menu" VALUES (9, 1, 1, '2024-09-17 18:38:24', '2024-09-17 18:38:26', 0, 0, 0, 8, 'sys:notice-log:page', 1, '分页查询通知日志', NULL, '', 50, 0);
INSERT INTO "public"."boot_sys_menu" VALUES (10, 1, 1, '2024-09-17 18:39:34', '2024-09-17 18:39:37', 0, 0, 0, 8, 'sys:notice-log:export', 1, '导出全部通知日志', NULL, '', 40, 0);
INSERT INTO "public"."boot_sys_menu" VALUES (11, 1, 1, '2025-01-11 14:10:54', '2025-01-11 14:10:57', 0, 0, 0, 3, 'sys:login-log:detail', 1, '查看登录日志', '', '', 30, 0);
INSERT INTO "public"."boot_sys_menu" VALUES (12, 1, 1, '2025-01-11 14:13:13', '2025-01-11 14:13:16', 0, 0, 0, 8, 'sys:notice-log:detail', 1, '查看通知日志', NULL, NULL, 30, 0);
INSERT INTO "public"."boot_sys_menu" VALUES (13, 1, 1, '2025-01-18 09:26:17', '2025-01-18 09:26:20', 0, 0, 0, 0, NULL, 0, '物联管理', '/iot', 'RobotOutlined', 80000, 0);
INSERT INTO "public"."boot_sys_menu" VALUES (14, 1, 1, '2025-01-18 09:28:42', '2025-01-18 09:28:44', 0, 0, 0, 13, NULL, 0, '设备管理', '/iot/device', NULL, 5000, 0);
INSERT INTO "public"."boot_sys_menu" VALUES (15, 1, 1, '2025-01-18 09:30:03', '2025-01-18 09:30:05', 0, 0, 0, 14, NULL, 0, '设备', '/iot/device/index', NULL, 100, 0);
INSERT INTO "public"."boot_sys_menu" VALUES (16, 1, 1, '2025-01-18 09:37:53', '2025-01-18 09:37:55', 0, 0, 0, 14, NULL, 0, '物模型', '/iot/device/thingModel', NULL, 400, 0);
INSERT INTO "public"."boot_sys_menu" VALUES (17, 1, 1, '2025-01-18 09:38:58', '2025-01-18 09:39:00', 0, 0, 0, 14, NULL, 0, '产品', '/iot/device/product', NULL, 200, 0);
INSERT INTO "public"."boot_sys_menu" VALUES (18, 1, 1, '2025-01-18 09:39:31', '2025-01-18 09:39:34', 0, 0, 0, 14, NULL, 0, '产品类别', '/iot/device/productCategory', NULL, 300, 0);
INSERT INTO "public"."boot_sys_menu" VALUES (19, 1, 1, '2025-01-21 05:15:10', '2025-01-21 05:15:13', 0, 0, 0, 13, NULL, 0, '协议管理', '/iot/protocol', NULL, 4000, 0);
INSERT INTO "public"."boot_sys_menu" VALUES (20, 1, 1, '2025-01-21 05:19:17', '2025-01-21 05:19:20', 0, 0, 0, 19, NULL, 0, '通讯协议', '/iot/protocol/communication', NULL, 100, 0);
INSERT INTO "public"."boot_sys_menu" VALUES (21, 1, 1, '2025-01-21 05:19:17', '2025-01-21 05:19:20', 0, 0, 0, 19, NULL, 0, '传输协议', '/iot/protocol/transport', NULL, 200, 0);
INSERT INTO "public"."boot_sys_menu" VALUES (22, 1, 1, '2025-01-21 05:19:17', '2025-01-21 05:19:20', 0, 0, 0, 1, NULL, 0, '权限管理', '/sys/permission', NULL, 5000, 0);
INSERT INTO "public"."boot_sys_menu" VALUES (23, 1, 1, '2025-01-21 05:19:17', '2025-01-21 05:19:20', 0, 0, 0, 22, NULL, 0, '菜单', '/sys/permission/menu', NULL, 900, 0);
INSERT INTO "public"."boot_sys_menu" VALUES (24, 1, 1, '2025-01-21 05:19:17', '2025-01-21 05:19:20', 0, 0, 0, 22, NULL, 0, '部门', '/sys/permission/dept', NULL, 800, 0);
INSERT INTO "public"."boot_sys_menu" VALUES (25, 1, 1, '2025-01-21 05:19:17', '2025-01-21 05:19:20', 0, 0, 0, 22, NULL, 0, '角色', '/sys/permission/role', NULL, 700, 0);
INSERT INTO "public"."boot_sys_menu" VALUES (26, 1, 1, '2025-01-21 05:19:17', '2025-01-21 05:19:20', 0, 0, 0, 22, NULL, 0, '用户', '/sys/permission/user', NULL, 600, 0);
INSERT INTO "public"."boot_sys_menu" VALUES (27, 1, 1, '2025-01-21 05:19:17', '2025-01-21 05:19:20', 0, 0, 0, 1, NULL, 0, '租户管理', '/sys/tenant', NULL, 4000, 0);
INSERT INTO "public"."boot_sys_menu" VALUES (28, 1, 1, '2025-01-21 05:19:17', '2025-01-21 05:19:20', 0, 0, 0, 27, NULL, 0, '数据源', '/sys/tenant/source', NULL, 400, 0);
INSERT INTO "public"."boot_sys_menu" VALUES (29, 1, 1, '2025-01-21 05:19:17', '2025-01-21 05:19:20', 0, 0, 0, 27, NULL, 0, '套餐', '/sys/tenant/package', NULL, 300, 0);
INSERT INTO "public"."boot_sys_menu" VALUES (30, 1, 1, '2025-01-21 05:19:17', '2025-01-21 05:19:20', 0, 0, 0, 27, NULL, 0, '租户', '/sys/tenant/index', NULL, 200, 0);
INSERT INTO "public"."boot_sys_menu" VALUES (31, 1, 1, '2025-01-21 05:19:17', '2025-01-21 05:19:20', 0, 0, 0, 1, NULL, 0, '基础数据', '/sys/base', NULL, 2000, 0);
INSERT INTO "public"."boot_sys_menu" VALUES (32, 1, 1, '2025-01-21 05:19:17', '2025-01-21 05:19:20', 0, 0, 0, 31, NULL, 0, '数据字典', '/sys/base/dict', NULL, 900, 0);
INSERT INTO "public"."boot_sys_menu" VALUES (33, 1, 1, '2024-09-15 12:56:07', '2024-09-15 12:56:10', 0, 0, 0, 2, NULL, 0, '操作日志', '/sys/log/operate', '', 900, 0);
INSERT INTO "public"."boot_sys_menu" VALUES (34, 1, 1, '2025-01-21 05:19:17', '2025-01-21 05:19:20', 0, 0, 0, 31, NULL, 0, '国际化', '/sys/base/i18n', NULL, 800, 0);
INSERT INTO "public"."boot_sys_menu" VALUES (35, 1, 1, '2025-01-21 05:19:17', '2025-01-21 05:19:20', 0, 0, 0, 31, NULL, 0, 'IP', '/sys/base/ip', NULL, 700, 0);
INSERT INTO "public"."boot_sys_menu" VALUES (36, 1, 1, '2025-01-21 05:19:17', '2025-01-21 05:19:20', 0, 0, 0, 1, NULL, 0, '对象存储', '/sys/oss', NULL, 1000, 0);
INSERT INTO "public"."boot_sys_menu" VALUES (37, 1, 1, '2025-01-21 05:19:17', '2025-01-21 05:19:20', 0, 0, 0, 36, NULL, 0, '对象存储配置', '/sys/oss/config', NULL, 200, 0);
INSERT INTO "public"."boot_sys_menu" VALUES (38, 1, 1, '2025-01-21 05:19:17', '2025-01-21 05:19:20', 0, 0, 0, 36, NULL, 0, '对象存储日志', '/sys/oss/log', NULL, 100, 0);
INSERT INTO "public"."boot_sys_menu" VALUES (39, 1, 1, '2025-01-21 05:19:17', '2025-01-21 05:19:20', 0, 0, 0, 1, NULL, 0, '集群管理', '/sys/cluster', NULL, 3000, 0);
INSERT INTO "public"."boot_sys_menu" VALUES (40, 1, 1, '2025-01-21 05:19:17', '2025-01-29 11:20:15.092772', 0, 0, 0, 23, 'sys:menu:list-tree', 1, '查询菜单树列表', '', NULL, 50, 0);
INSERT INTO "public"."boot_sys_menu" VALUES (41, 1, 1, '2025-01-21 05:19:17', '2025-01-29 11:20:15.092772', 0, 0, 0, 23, 'sys:menu:remove', 1, '删除菜单', '', NULL, 20, 0);
INSERT INTO "public"."boot_sys_menu" VALUES (42, 1, 1, '2025-01-21 05:19:17', '2025-01-29 14:32:03.026995', 0, 0, 0, 23, 'sys:menu:save', 1, '新增菜单', '', NULL, 40, 0);
INSERT INTO "public"."boot_sys_menu" VALUES (43, 1, 1, '2025-01-31 13:08:53.459704', '2025-01-31 13:08:53.459779', 0, 0, 0, 23, 'sys:menu:detail', 1, '查看菜单', NULL, NULL, 10, 0);
INSERT INTO "public"."boot_sys_menu" VALUES (44, 1, 1, '2025-01-31 13:50:50.855744', '2025-01-31 13:50:50.856767', 0, 0, 0, 23, 'sys:menu:modify', 1, '修改菜单', NULL, NULL, 30, 0);
INSERT INTO "public"."boot_sys_menu" VALUES (45, 1, 1, '2025-02-01 09:57:06.903239', '2025-02-01 09:57:16.643637', 0, 1, 0, 24, 'sys:dept:list-tree', 1, '查询部门树列表', NULL, NULL, 50, 0);
INSERT INTO "public"."boot_sys_menu" VALUES (46, 1, 1, '2025-02-02 13:24:26.26515', '2025-02-02 13:24:26.26515', 0, 0, 0, 24, 'sys:dept:detail', 1, '查看部门', NULL, NULL, 10, 0);
INSERT INTO "public"."boot_sys_menu" VALUES (47, 1, 1, '2025-02-02 13:36:39.981857', '2025-02-02 13:36:39.981857', 0, 0, 0, 24, 'sys:dept:save', 1, '新增部门', NULL, NULL, 40, 0);
INSERT INTO "public"."boot_sys_menu" VALUES (48, 1, 1, '2025-02-02 13:37:18.191332', '2025-02-02 13:37:26.503011', 0, 1, 0, 24, 'sys:dept:modify', 1, '修改部门', NULL, NULL, 30, 0);
INSERT INTO "public"."boot_sys_menu" VALUES (49, 1, 1, '2025-02-02 13:37:56.611868', '2025-02-02 13:38:04.173925', 0, 1, 0, 24, 'sys:dept:remove', 1, '删除部门', NULL, NULL, 20, 0);
INSERT INTO "public"."boot_sys_menu" VALUES (50, 1, 1, '2025-02-03 10:18:36.114539', '2025-02-03 10:18:36.115538', 0, 0, 0, 25, 'sys:role:page', 1, '分页查询角色', NULL, NULL, 50, 0);
INSERT INTO "public"."boot_sys_menu" VALUES (51, 1, 1, '2025-02-03 10:19:23.231544', '2025-02-03 10:19:23.232089', 0, 0, 0, 25, 'sys:role:save', 1, '新增角色', NULL, NULL, 40, 0);
INSERT INTO "public"."boot_sys_menu" VALUES (52, 1, 1, '2025-02-03 10:20:30.753445', '2025-02-03 10:20:30.753445', 0, 0, 0, 25, 'sys:role:modify', 1, '修改角色', NULL, NULL, 30, 0);
INSERT INTO "public"."boot_sys_menu" VALUES (53, 1, 1, '2025-02-03 10:20:55.204108', '2025-02-03 10:20:55.204108', 0, 0, 0, 25, 'sys:role:remove', 1, '删除角色', NULL, NULL, 20, 0);
INSERT INTO "public"."boot_sys_menu" VALUES (54, 1, 1, '2025-02-03 10:21:33.499697', '2025-02-03 10:21:33.499697', 0, 0, 0, 25, 'sys:role:detail', 1, '查看角色', NULL, NULL, 10, 0);
INSERT INTO "public"."boot_sys_menu" VALUES (55, 1, 1, '2025-02-04 13:45:32.958243', '2025-02-04 13:45:32.959244', 0, 0, 0, 26, 'sys:user:page', 1, '分页查询用户', NULL, NULL, 50, 0);
INSERT INTO "public"."boot_sys_menu" VALUES (56, 1, 1, '2025-02-04 13:46:48.225873', '2025-02-04 13:46:48.226411', 0, 0, 0, 26, 'sys:user:save', 1, '新增用户', NULL, NULL, 40, 0);
INSERT INTO "public"."boot_sys_menu" VALUES (57, 1, 1, '2025-02-04 13:47:19.981199', '2025-02-04 13:47:19.981199', 0, 0, 0, 26, 'sys:user:modify', 1, '修改用户', NULL, NULL, 30, 0);
INSERT INTO "public"."boot_sys_menu" VALUES (58, 1, 1, '2025-02-04 13:47:47.639939', '2025-02-04 13:47:47.639939', 0, 0, 0, 26, 'sys:user:remove', 1, '删除用户', NULL, NULL, 20, 0);
INSERT INTO "public"."boot_sys_menu" VALUES (59, 1, 1, '2025-02-04 13:48:34.215596', '2025-02-04 13:48:34.215596', 0, 0, 0, 26, 'sys:user:detail', 1, '查看用户', NULL, NULL, 10, 0);
INSERT INTO "public"."boot_sys_menu" VALUES (60, 1, 1, '2025-03-02 17:52:49.303296', '2025-03-02 17:54:03.34208', 0, 2, 0, 33, 'sys:operate-log:page', 1, '分页查询操作日志', NULL, NULL, 50, 0);
INSERT INTO "public"."boot_sys_menu" VALUES (61, 1, 1, '2025-03-02 18:06:30.782409', '2025-03-02 18:07:31.180306', 0, 1, 0, 33, 'sys:operate-log:export', 1, '导出全部操作日志', NULL, NULL, 40, 0);
INSERT INTO "public"."boot_sys_menu" VALUES (62, 1, 1, '2025-03-02 18:09:02.472121', '2025-03-02 18:09:02.472121', 0, 0, 0, 33, 'sys:operate-log:detail', 1, '查看操作日志', NULL, NULL, 30, 0);
INSERT INTO "public"."boot_sys_menu" VALUES (63, 1, 1, '2025-03-15 12:15:37.277552', '2025-03-15 12:15:37.278549', 0, 0, 0, 37, 'sys:oss:upload', 1, '上传文件', NULL, NULL, 5, 0);
INSERT INTO "public"."boot_sys_menu" VALUES (64, 1, 1, '2025-03-16 11:29:37.482459', '2025-03-16 11:29:37.482459', 0, 0, 0, 20, 'iot:communication-protocol:detail', 1, '查看通讯协议', NULL, NULL, 10, 0);
INSERT INTO "public"."boot_sys_menu" VALUES (65, 1, 1, '2025-03-16 11:29:16.557205', '2025-03-16 11:29:16.557272', 0, 0, 0, 20, 'iot:communication-protocol:remove', 1, '删除通讯协议', NULL, NULL, 20, 0);
INSERT INTO "public"."boot_sys_menu" VALUES (66, 1, 1, '2025-03-16 11:28:50.27337', '2025-03-16 11:28:50.27337', 0, 0, 0, 20, 'iot:communication-protocol:modify', 1, '修改通讯协议', NULL, NULL, 30, 0);
INSERT INTO "public"."boot_sys_menu" VALUES (67, 1, 1, '2025-03-16 11:28:30.822693', '2025-03-16 11:28:30.822693', 0, 0, 0, 20, 'iot:communication-protocol:save', 1, '新增通讯协议', NULL, NULL, 40, 0);
INSERT INTO "public"."boot_sys_menu" VALUES (68, 1, 1, '2025-03-16 11:27:37.626071', '2025-03-16 11:27:37.626071', 0, 0, 0, 20, 'iot:communication-protocol:page', 1, '分页查询通讯协议', NULL, NULL, 50, 0);
INSERT INTO "public"."boot_sys_menu" VALUES (69, 1, 1, '2025-03-16 11:26:56.775423', '2025-03-16 11:26:56.775423', 0, 0, 0, 21, 'iot:transport-protocol:detail', 1, '查看传输协议', NULL, NULL, 10, 0);
INSERT INTO "public"."boot_sys_menu" VALUES (70, 1, 1, '2025-03-16 11:26:35.151986', '2025-03-16 11:26:35.151986', 0, 0, 0, 21, 'iot:transport-protocol:remove', 1, '删除传输协议', NULL, NULL, 20, 0);
INSERT INTO "public"."boot_sys_menu" VALUES (71, 1, 1, '2025-03-16 11:25:49.171885', '2025-03-16 11:25:49.171885', 0, 0, 0, 21, 'iot:transport-protocol:modify', 1, '修改传输协议', NULL, NULL, 30, 0);
INSERT INTO "public"."boot_sys_menu" VALUES (72, 1, 1, '2025-03-16 11:25:28.95389', '2025-03-16 11:25:28.95389', 0, 0, 0, 21, 'iot:transport-protocol:save', 1, '新增传输协议', NULL, NULL, 40, 0);
INSERT INTO "public"."boot_sys_menu" VALUES (73, 1, 1, '2025-03-16 11:25:05.526693', '2025-03-16 11:25:05.526693', 0, 0, 0, 21, 'iot:transport-protocol:page', 1, '分页查询传输协议', NULL, NULL, 50, 0);
INSERT INTO "public"."boot_sys_menu" VALUES (74, 1, 1, '2025-03-16 11:24:21.93876', '2025-03-16 11:24:21.93876', 0, 0, 0, 15, 'iot:device:detail', 1, '查看设备', NULL, NULL, 10, 0);
INSERT INTO "public"."boot_sys_menu" VALUES (75, 1, 1, '2025-03-16 11:24:02.28912', '2025-03-16 11:24:02.28912', 0, 0, 0, 15, 'iot:device:remove', 1, '删除设备', NULL, NULL, 20, 0);
INSERT INTO "public"."boot_sys_menu" VALUES (76, 1, 1, '2025-03-16 11:23:41.504246', '2025-03-16 11:23:41.504246', 0, 0, 0, 15, 'iot:device:modify', 1, '修改设备', NULL, NULL, 30, 0);
INSERT INTO "public"."boot_sys_menu" VALUES (77, 1, 1, '2025-03-16 11:23:23.056863', '2025-03-16 11:23:23.056863', 0, 0, 0, 15, 'iot:device:save', 1, '新增设备', NULL, NULL, 40, 0);
INSERT INTO "public"."boot_sys_menu" VALUES (78, 1, 1, '2025-03-16 11:22:56.958172', '2025-03-16 11:22:56.958172', 0, 0, 0, 15, 'iot:device:page', 1, '分页查询设备', NULL, NULL, 50, 0);
INSERT INTO "public"."boot_sys_menu" VALUES (79, 1, 1, '2025-03-16 11:22:20.518897', '2025-03-16 11:22:20.518897', 0, 0, 0, 18, 'iot:product-category:detail', 1, '查看产品类别', NULL, NULL, 10, 0);
INSERT INTO "public"."boot_sys_menu" VALUES (80, 1, 1, '2025-03-16 11:22:00.290849', '2025-03-16 11:22:00.290849', 0, 0, 0, 18, 'iot:product-category:remove', 1, '删除产品类别', NULL, NULL, 20, 0);
INSERT INTO "public"."boot_sys_menu" VALUES (81, 1, 1, '2025-03-16 11:21:32.205209', '2025-03-16 11:21:32.205209', 0, 0, 0, 18, 'iot:product-category:modify', 1, '修改产品类别', NULL, NULL, 30, 0);
INSERT INTO "public"."boot_sys_menu" VALUES (82, 1, 1, '2025-03-16 11:21:04.431009', '2025-03-16 11:21:04.431009', 0, 0, 0, 18, 'iot:product-category:save', 1, '新增产品类别', NULL, NULL, 40, 0);
INSERT INTO "public"."boot_sys_menu" VALUES (83, 1, 1, '2025-03-16 11:20:40.603807', '2025-03-16 11:20:40.603807', 0, 0, 0, 18, 'iot:product-category:list-tree', 1, '查询产品类别树列表', NULL, NULL, 50, 0);
INSERT INTO "public"."boot_sys_menu" VALUES (84, 1, 1, '2025-03-16 11:19:25.506634', '2025-03-16 11:19:25.506634', 0, 0, 0, 17, 'iot:product:detail', 1, '查看产品', NULL, NULL, 10, 0);
INSERT INTO "public"."boot_sys_menu" VALUES (85, 1, 1, '2025-03-16 11:18:43.37544', '2025-03-16 11:18:43.37544', 0, 0, 0, 17, 'iot:product:remove', 1, '删除产品', NULL, NULL, 20, 0);
INSERT INTO "public"."boot_sys_menu" VALUES (86, 1, 1, '2025-03-16 11:17:16.858243', '2025-03-16 11:18:07.952616', 0, 1, 0, 17, 'iot:product:modify', 1, '修改产品', NULL, NULL, 30, 0);
INSERT INTO "public"."boot_sys_menu" VALUES (87, 1, 1, '2025-03-16 11:16:56.540728', '2025-03-16 11:18:02.393751', 0, 1, 0, 17, 'iot:product:save', 1, '新增产品', NULL, NULL, 40, 0);
INSERT INTO "public"."boot_sys_menu" VALUES (88, 1, 1, '2025-03-16 11:04:36.477875', '2025-03-16 11:04:36.477875', 0, 0, 0, 17, 'iot:product:page', 1, '分页查询产品', NULL, NULL, 50, 0);
INSERT INTO "public"."boot_sys_menu" VALUES (89, 1, 1, '2025-03-16 11:02:48.298633', '2025-03-16 11:02:48.298633', 0, 0, 0, 16, 'iot:thing-model:detail', 1, '查看物模型', NULL, NULL, 10, 0);
INSERT INTO "public"."boot_sys_menu" VALUES (90, 1, 1, '2025-03-16 11:02:24.689321', '2025-03-16 11:02:24.689321', 0, 0, 0, 16, 'iot:thing-model:remove', 1, '删除物模型', NULL, NULL, 20, 0);
INSERT INTO "public"."boot_sys_menu" VALUES (91, 1, 1, '2025-03-16 11:01:52.176027', '2025-03-16 11:18:56.079399', 0, 2, 0, 16, 'iot:thing-model:modify', 1, '修改物模型', NULL, NULL, 30, 0);
INSERT INTO "public"."boot_sys_menu" VALUES (92, 1, 1, '2025-03-16 11:01:31.000074', '2025-03-16 11:01:31.000074', 0, 0, 0, 16, 'iot:thing-model:save', 1, '新增物模型', NULL, NULL, 40, 0);
INSERT INTO "public"."boot_sys_menu" VALUES (93, 1, 1, '2025-03-16 11:00:58.755018', '2025-03-16 11:00:58.755018', 0, 0, 0, 16, 'iot:thing-model:page', 1, '分页查询物模型', NULL, NULL, 50, 0);

-- ----------------------------
-- -------------菜单套餐------------
-- ----------------------------
DROP TABLE IF EXISTS "public"."boot_sys_menu_package";
CREATE TABLE "public"."boot_sys_menu_package" (
  "id" int8 NOT NULL,
  "creator" int8 NOT NULL DEFAULT 0,
  "editor" int8 NOT NULL DEFAULT 0,
  "create_time" timestamp(6) NOT NULL DEFAULT CURRENT_TIMESTAMP,
  "update_time" timestamp(6) NOT NULL DEFAULT CURRENT_TIMESTAMP,
  "del_flag" int2 NOT NULL DEFAULT 0,
  "version" int4 NOT NULL DEFAULT 0,
  "tenant_id" int8 NOT NULL DEFAULT 0,
  "menu_id" int8 NOT NULL,
  "package_id" int8 NOT NULL
);
COMMENT ON COLUMN "public"."boot_sys_menu_package"."id" IS 'ID';
COMMENT ON COLUMN "public"."boot_sys_menu_package"."creator" IS '创建人';
COMMENT ON COLUMN "public"."boot_sys_menu_package"."editor" IS '编辑人';
COMMENT ON COLUMN "public"."boot_sys_menu_package"."create_time" IS '创建时间';
COMMENT ON COLUMN "public"."boot_sys_menu_package"."update_time" IS '修改时间';
COMMENT ON COLUMN "public"."boot_sys_menu_package"."del_flag" IS '删除标识 0未删除 1已删除';
COMMENT ON COLUMN "public"."boot_sys_menu_package"."version" IS '版本号';
COMMENT ON COLUMN "public"."boot_sys_menu_package"."tenant_id" IS '租户ID';
COMMENT ON COLUMN "public"."boot_sys_menu_package"."menu_id" IS '菜单ID';
COMMENT ON COLUMN "public"."boot_sys_menu_package"."package_id" IS '套餐ID';
COMMENT ON TABLE "public"."boot_sys_menu_package" IS '菜单套餐';

ALTER TABLE "public"."boot_sys_menu_package" ADD CONSTRAINT "boot_sys_menu_package_pkey" PRIMARY KEY ("id");

-- ----------------------------
-- -------------消息------------
-- ----------------------------
DROP TABLE IF EXISTS "public"."boot_sys_message";
CREATE TABLE "public"."boot_sys_message" (
	 "id" int8 NOT NULL,
	 "creator" int8 NOT NULL DEFAULT 0,
	 "editor" int8 NOT NULL DEFAULT 0,
	 "create_time" timestamp(6) NOT NULL DEFAULT CURRENT_TIMESTAMP,
	 "update_time" timestamp(6) NOT NULL DEFAULT CURRENT_TIMESTAMP,
	 "del_flag" int2 NOT NULL DEFAULT 0,
	 "version" int4 NOT NULL DEFAULT 0,
	 "tenant_id" int8 NOT NULL DEFAULT 0,
	 "title" varchar(400) NOT NULL,
	 "content" text NOT NULL,
	 "type" int2 NOT NULL DEFAULT 0
)
;
COMMENT ON COLUMN "public"."boot_sys_message"."id" IS 'ID';
COMMENT ON COLUMN "public"."boot_sys_message"."creator" IS '创建人';
COMMENT ON COLUMN "public"."boot_sys_message"."editor" IS '编辑人';
COMMENT ON COLUMN "public"."boot_sys_message"."create_time" IS '创建时间';
COMMENT ON COLUMN "public"."boot_sys_message"."update_time" IS '修改时间';
COMMENT ON COLUMN "public"."boot_sys_message"."del_flag" IS '删除标识 0未删除 1已删除';
COMMENT ON COLUMN "public"."boot_sys_message"."version" IS '版本号';
COMMENT ON COLUMN "public"."boot_sys_message"."tenant_id" IS '租户ID';
COMMENT ON COLUMN "public"."boot_sys_message"."title" IS '消息标题';
COMMENT ON COLUMN "public"."boot_sys_message"."content" IS '消息内容';
COMMENT ON COLUMN "public"."boot_sys_message"."type" IS '消息类型 0通知 1提醒';
COMMENT ON TABLE "public"."boot_sys_message" IS '消息';

ALTER TABLE "public"."boot_sys_message" ADD CONSTRAINT "boot_sys_message_pkey" PRIMARY KEY ("id");
