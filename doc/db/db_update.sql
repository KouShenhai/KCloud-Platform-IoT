-- 2023/2/23 增加oauth2_authorization索引 老寇
alter table oauth2_authorization add index idx_token_expires_issued_principal_name(access_token_value(700),access_token_expires_at,access_token_issued_at,principal_name) comment 'token_过期时间_开始时间_登录人_索引';

-- 2023/2/24 增加boot_sys_audit_log索引 老寇
alter table boot_sys_audit_log add index idx_business_id_type(business_id,`type`) comment '业务编号_索引';

-- 2023/2/24 增加boot_sys_menu索引 老寇
ALTER TABLE boot_sys_menu ADD index idx_type_visible(`type`,`visible`) COMMENT '类型_可见_索引';

-- 2023/2/24 增加boot_sys_message_detail索引 老寇
ALTER table boot_sys_message_detail add INDEX idx_read_flag_user_id(read_flag,user_id) COMMENT '已读_用户编号_索引';

-- 2023/2/24 删除索引boot_sys_operate_log索引 老寇
alter alter boot_sys_operate_log drop idx_module;

-- 2023/2/24 增加boot_sys_resource索引 老寇
ALTER table boot_sys_resource ADD INDEX idx_code(`code`) comment '编码_索引';

-- 2023/2/24 增加boot_sys_resource_audit索引 老寇
ALTER table boot_sys_resource_audit add index idx_resource_id(`resource_id`) comment '资源编号_索引';

-- 2023/2/26 增加boot_sys_menu数据 老寇
INSERT INTO `boot_sys_menu` (`id`, `pid`, `permission`, `type`, `name`, `url`, `icon`, `creator`, `editor`, `create_date`, `update_date`, `del_flag`, `sort`, `version`, `visible`) VALUES ('1564996817056710700', '1537444981390794754', 'monitor:flow:view', '0', '流量监控', 'http://127.0.0.1:8081', 'dashboard', '1341620898007281665', NULL, '2023-02-26 21:05:30', '2023-02-26 21:05:30', '0', '4000', '0', '0');
INSERT INTO `boot_sys_menu` (`id`, `pid`, `permission`, `type`, `name`, `url`, `icon`, `creator`, `editor`, `create_date`, `update_date`, `del_flag`, `sort`, `version`, `visible`) VALUES ('1564996817056710701', '1535878154046939137', 'sys:config:view', '0', '配置中心', 'http://127.0.0.1:8848/nacos', 'appstore', '1341620898007281665', '1341620898007281665', '2023-02-26 21:09:04', '2023-02-26 21:32:40', '0', '404', '1', '0');

-- 2023/3/2 增加boot_sys_menu数据 老寇
INSERT INTO `boot_sys_menu` (`id`, `pid`, `permission`, `type`, `name`, `url`, `icon`, `creator`, `editor`, `create_date`, `update_date`, `del_flag`, `sort`, `version`, `visible`) VALUES ('1564996817056710705', '1545036486288732162', 'workflow:definition:template', '1', '流程模板', '/workflow/definition/api/template', 'documentation', '1341620898007281665', '1341620898007281665', '2023-03-02 07:52:36', '2023-03-02 07:53:10', '0', '10', '2', '0');

-- 2023/3/9 增加boot_sys_menu数据 老寇
INSERT INTO `boot_sys_menu` (`id`, `pid`, `permission`, `type`, `name`, `url`, `icon`, `creator`, `editor`, `create_date`, `update_date`, `del_flag`, `sort`, `version`, `visible`) VALUES (1564996817056710706, 1545037580289044482, 'workflow:task:resource:resolve', 1, '任务处理', '/workflow/task/api/resource/resolve', 'like', 1341623527018004481, NULL, '2023-03-08 07:45:38', '2023-03-08 07:45:38', 0, 10, 0, 0);
INSERT INTO `boot_sys_menu` (`id`, `pid`, `permission`, `type`, `name`, `url`, `icon`, `creator`, `editor`, `create_date`, `update_date`, `del_flag`, `sort`, `version`, `visible`) VALUES (1564996817056710707, 1545037580289044482, 'workflow:task:resource:transfer', 1, '任务转办', '/workflow/task/api/resource/transfer', 'solution', 1341623527018004481, 1537114827246292994, '2023-03-08 07:46:11', '2023-03-08 08:05:01', 0, 10, 6, 0);
INSERT INTO `boot_sys_menu` (`id`, `pid`, `permission`, `type`, `name`, `url`, `icon`, `creator`, `editor`, `create_date`, `update_date`, `del_flag`, `sort`, `version`, `visible`) VALUES (1564996817056710708, 1545037580289044482, 'workflow:task:resource:delegate', 1, '任务委派', '/workflow/task/api/resource/delegate', 'user-add', 1341623527018004481, NULL, '2023-03-08 07:51:53', '2023-03-08 07:51:53', 0, 10, 0, 0);

-- 2023/3/10 移除boot_sys_dept字段 老寇
alter table boot_sys_dept drop `path`;

-- 2023/3/10 增加boot_sys_dept索引 老寇
ALTER table boot_sys_dept ADD INDEX idx_tenant_id(`tenant_id`) comment '租户编号_索引';

-- 2023/3/11 为oauth2_authorization增加事件 老寇
-- 每天零点清理过期token
drop event if exists delete_oauth2_authorization_expire_token_every_day;
create event delete_oauth2_authorization_expire_token_every_day
on schedule every 1 day starts '2023-03-11 00:00:00'
on completion preserve
do delete from oauth2_authorization where access_token_expires_at <= now();

-- 2023/3/12 增加boot_sys_menu数据 老寇
INSERT INTO `boot_sys_menu` (`id`, `pid`, `permission`, `type`, `name`, `url`, `icon`, `creator`, `editor`, `create_date`, `update_date`, `del_flag`, `sort`, `version`, `visible`) VALUES ('1564996817056710709', '1535878154046939137', 'sys:db:view', '0', '数据库文档', '/sys/db/view', 'book', '1341620898007281665', '1341620898007281665', '2023-03-12 13:05:20', '2023-03-12 13:06:54', '0', '401', '2', '0');

-- 2023/3/15 增加boot_sys_menu数据 老寇
INSERT INTO `boot_sys_menu` (`id`, `pid`, `permission`, `type`, `name`, `url`, `icon`, `creator`, `editor`, `create_date`, `update_date`, `del_flag`, `sort`, `version`, `visible`) VALUES ('1564996817056710710', '1535878154046939137', 'sys:tx:view', '0', '事务管理', 'http://127.0.0.1:7091', 'cascader', '1341620898007281665', '1341620898007281665', '2023-03-15 22:11:04', '2023-03-15 22:13:06', '0', '403', '2', '0');

-- 2023/4/20 分表boot_sys_login_log数据 老寇
CREATE TABLE `boot_sys_login_log_0` (
                                        `id` bigint NOT NULL AUTO_INCREMENT COMMENT 'id',
                                        `creator` bigint DEFAULT NULL COMMENT '创建者',
                                        `create_date` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                                        `update_date` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '修改时间',
                                        `del_flag` tinyint(1) NOT NULL DEFAULT '0' COMMENT '1已删除 0未删除',
                                        `editor` bigint DEFAULT NULL COMMENT '编辑人',
                                        `login_name` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '登录用户',
                                        `request_ip` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT 'IP地址',
                                        `request_address` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '归属地',
                                        `browser` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '浏览器版本',
                                        `os` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '操作系统',
                                        `request_status` tinyint unsigned NOT NULL COMMENT '状态  0：成功   1：失败',
                                        `msg` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '提示信息',
                                        `login_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '登录类型',
                                        `version` int NOT NULL DEFAULT '0' COMMENT '版本号',
                                        `tenant_id` bigint NOT NULL DEFAULT '0' COMMENT '租户id',
                                        PRIMARY KEY (`id`),
                                        KEY `idx_tenant_id_request_status` (`tenant_id`,`request_status`) USING BTREE COMMENT '租户编号_请求状态_索引'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='登录日志';

CREATE TABLE `boot_sys_login_log_1` (
                                        `id` bigint NOT NULL AUTO_INCREMENT COMMENT 'id',
                                        `creator` bigint DEFAULT NULL COMMENT '创建者',
                                        `create_date` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                                        `update_date` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '修改时间',
                                        `del_flag` tinyint(1) NOT NULL DEFAULT '0' COMMENT '1已删除 0未删除',
                                        `editor` bigint DEFAULT NULL COMMENT '编辑人',
                                        `login_name` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '登录用户',
                                        `request_ip` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT 'IP地址',
                                        `request_address` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '归属地',
                                        `browser` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '浏览器版本',
                                        `os` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '操作系统',
                                        `request_status` tinyint unsigned NOT NULL COMMENT '状态  0：成功   1：失败',
                                        `msg` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '提示信息',
                                        `login_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '登录类型',
                                        `version` int NOT NULL DEFAULT '0' COMMENT '版本号',
                                        `tenant_id` bigint NOT NULL DEFAULT '0' COMMENT '租户id',
                                        PRIMARY KEY (`id`),
                                        KEY `idx_tenant_id_request_status` (`tenant_id`,`request_status`) USING BTREE COMMENT '租户编号_请求状态_索引'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='登录日志';
