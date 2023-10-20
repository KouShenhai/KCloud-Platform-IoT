DROP TABLE IF EXISTS `boot_sys_sql_log`;
CREATE TABLE `boot_sys_sql_log`  (
                                     `id` bigint NOT NULL AUTO_INCREMENT COMMENT 'ID',
                                     `creator` bigint NOT NULL DEFAULT 0 COMMENT '创建人',
                                     `editor` bigint NOT NULL DEFAULT 0 COMMENT '编辑人',
                                     `create_date` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                                     `update_date` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
                                     `del_flag` tinyint(1) NOT NULL DEFAULT 0 COMMENT '删除标识 0未删除 1已删除',
                                     `version` int NOT NULL DEFAULT 0 COMMENT '版本号',
                                     `dept_id` bigint NOT NULL DEFAULT 0 COMMENT '部门ID',
                                     `dept_path` varchar(1000) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '0' COMMENT '部门PATH',
                                     `tenant_id` bigint NOT NULL DEFAULT 0 COMMENT '租户ID',
                                     `app_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '应用名称',
                                     `dsl` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '查询语句',
                                     `cost_time` bigint NOT NULL DEFAULT 0 COMMENT '消耗时间',
                                     PRIMARY KEY (`id`) USING BTREE,
                                     INDEX `idx_app_name`(`app_name` ASC) USING BTREE COMMENT '应用名称_索引'
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = 'SQL日志' ROW_FORMAT = DYNAMIC;