CREATE TABLE `boot_sys_ip` (
                               `id` bigint NOT NULL AUTO_INCREMENT COMMENT 'ID',
                               `creator` bigint NOT NULL DEFAULT '0' COMMENT '创建人',
                               `editor` bigint NOT NULL DEFAULT '0' COMMENT '编辑人',
                               `create_date` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                               `update_date` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
                               `del_flag` tinyint(1) NOT NULL DEFAULT '0' COMMENT '删除标识 0未删除 1已删除',
                               `version` int NOT NULL DEFAULT '0' COMMENT '版本号',
                               `dept_id` bigint NOT NULL DEFAULT '0' COMMENT '部门ID',
                               `dept_path` varchar(1000) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '0' COMMENT '部门PATH',
                               `tenant_id` bigint NOT NULL DEFAULT '0' COMMENT '租户ID',
                               `label` varchar(10) NOT NULL COMMENT '标签',
                               `value` varchar(20) NOT NULL COMMENT '值',
                               PRIMARY KEY (`id`) USING BTREE,
                               KEY `idx_label` (`label`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 ROW_FORMAT=DYNAMIC COMMENT='IP';