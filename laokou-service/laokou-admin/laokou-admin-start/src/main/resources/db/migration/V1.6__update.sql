CREATE TABLE if not exists `boot_sys_domain_event` (
                                         `id` bigint NOT NULL AUTO_INCREMENT COMMENT 'ID',
                                         `creator` bigint DEFAULT '0' COMMENT '创建人',
                                         `editor` bigint DEFAULT '0' COMMENT '编辑人',
                                         `create_date` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                                         `update_date` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
                                         `del_flag` tinyint(1) NOT NULL DEFAULT '0' COMMENT '删除标识 0未删除 1已删除',
                                         `version` int NOT NULL DEFAULT '0' COMMENT '版本号',
                                         `dept_id` bigint DEFAULT '0' COMMENT '部门ID',
                                         `dept_path` varchar(1000) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT '0' COMMENT '部门PATH',
                                         `tenant_id` bigint DEFAULT '0' COMMENT '租户ID',
                                         `aggregate_id` bigint DEFAULT NULL COMMENT '聚合根ID',
                                         `event_type` varchar(100) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL COMMENT '事件类型',
                                         `event_status` varchar(50) NOT NULL COMMENT '事件状态  CREATED创建  PUBLISH_SUCCEED发布成功  PUBLISH_FAILED发布失败  CONSUME_SUCCEED消费成功  CONSUME_FAILED消费失败',
                                         `topic` varchar(100) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL COMMENT 'MQ主题',
                                         `source_name` varchar(20) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL COMMENT '数据源名称',
                                         `attribute` json DEFAULT NULL COMMENT '扩展属性',
                                         PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 ROW_FORMAT=DYNAMIC COMMENT='领域事件';

drop table if exists `boot_sys_sql_log`;
CREATE TABLE if not exists `boot_sys_sql_log` (
                                    `id` bigint NOT NULL AUTO_INCREMENT COMMENT 'ID',
                                    `creator` bigint DEFAULT '0' COMMENT '创建人',
                                    `editor` bigint DEFAULT '0' COMMENT '编辑人',
                                    `create_date` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                                    `update_date` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
                                    `del_flag` tinyint(1) NOT NULL DEFAULT '0' COMMENT '删除标识 0未删除 1已删除',
                                    `version` int NOT NULL DEFAULT '0' COMMENT '版本号',
                                    `dept_id` bigint DEFAULT '0' COMMENT '部门ID',
                                    `dept_path` varchar(1000) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT '0' COMMENT '部门PATH',
                                    `tenant_id` bigint DEFAULT '0' COMMENT '租户ID',
                                    `app_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '应用名称',
                                    `dsl` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '查询语句',
                                    `cost_time` bigint NOT NULL DEFAULT '0' COMMENT '消耗时间',
                                    PRIMARY KEY (`id`) USING BTREE,
                                    KEY `idx_app_name` (`app_name`) USING BTREE COMMENT '应用名称_索引'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci ROW_FORMAT=DYNAMIC COMMENT='SQL日志';