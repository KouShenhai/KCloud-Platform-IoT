ALTER TABLE boot_sys_role_menu ADD `creator` bigint DEFAULT NULL COMMENT '创建人';
ALTER TABLE boot_sys_role_menu ADD `editor` bigint DEFAULT NULL COMMENT '编辑人';
ALTER TABLE boot_sys_role_menu ADD `create_date` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间';
ALTER TABLE boot_sys_role_menu ADD `update_date` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '修改时间';
ALTER TABLE boot_sys_role_menu ADD `del_flag` tinyint(1) NOT NULL DEFAULT '0' COMMENT '1已删除 0未删除';
ALTER TABLE boot_sys_role_menu ADD `version` int NOT NULL DEFAULT '0' COMMENT '版本号';

ALTER TABLE boot_sys_role_dept ADD `creator` bigint DEFAULT NULL COMMENT '创建人';
ALTER TABLE boot_sys_role_dept ADD `editor` bigint DEFAULT NULL COMMENT '编辑人';
ALTER TABLE boot_sys_role_dept ADD `create_date` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间';
ALTER TABLE boot_sys_role_dept ADD `update_date` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '修改时间';
ALTER TABLE boot_sys_role_dept ADD `del_flag` tinyint(1) NOT NULL DEFAULT '0' COMMENT '1已删除 0未删除';
ALTER TABLE boot_sys_role_dept ADD `version` int NOT NULL DEFAULT '0' COMMENT '版本号';

ALTER TABLE boot_sys_user_role ADD `creator` bigint DEFAULT NULL COMMENT '创建人';
ALTER TABLE boot_sys_user_role ADD `editor` bigint DEFAULT NULL COMMENT '编辑人';
ALTER TABLE boot_sys_user_role ADD `create_date` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间';
ALTER TABLE boot_sys_user_role ADD `update_date` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '修改时间';
ALTER TABLE boot_sys_user_role ADD `del_flag` tinyint(1) NOT NULL DEFAULT '0' COMMENT '1已删除 0未删除';
ALTER TABLE boot_sys_user_role ADD `version` int NOT NULL DEFAULT '0' COMMENT '版本号';

ALTER TABLE boot_sys_package_menu ADD `creator` bigint DEFAULT NULL COMMENT '创建人';
ALTER TABLE boot_sys_package_menu ADD `editor` bigint DEFAULT NULL COMMENT '编辑人';
ALTER TABLE boot_sys_package_menu ADD `create_date` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间';
ALTER TABLE boot_sys_package_menu ADD `update_date` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '修改时间';
ALTER TABLE boot_sys_package_menu ADD `del_flag` tinyint(1) NOT NULL DEFAULT '0' COMMENT '1已删除 0未删除';
ALTER TABLE boot_sys_package_menu ADD `version` int NOT NULL DEFAULT '0' COMMENT '版本号';