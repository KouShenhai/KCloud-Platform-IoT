-- 2023/2/22 增加boot_sys_user索引 老寇
alter table boot_sys_user drop idx_mobile;
alter table boot_sys_user drop idx_mail;
alter table boot_sys_user drop idx_username;
alter table boot_sys_user drop idx_tenant_id;
alter table boot_sys_user add unique index idx_tenant_id_username(tenant_id, username) comment '租户_用户名_唯一索引';
alter table boot_sys_user add unique index idx_tenant_id_mail(tenant_id, mail) comment '租户_邮箱_唯一索引';
alter table boot_sys_user add unique index idx_tenant_id_mobile(tenant_id, mobile) comment '租户_手机号_唯一索引';

-- 2023/2/23 增加oauth2_authorization索引 老寇
alter table oauth2_authorization add index idx_token_expires_issued_principal_name(access_token_value(700),access_token_expires_at,access_token_issued_at,principal_name) comment 'token_过期时间_开始时间_登录人_索引';

-- 2023/2/24 增加boot_sys_audit_log索引 老寇
alter table boot_sys_audit_log add index idx_business_id_type(business_id,`type`) comment '业务编号_索引';

-- 2023/2/24 增加boot_sys_login_log索引 老寇
alter table boot_sys_login_log add index idx_tenant_id_request_status(tenant_id,request_status) comment '租户编号_请求状态_索引';

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
INSERT INTO `boot_sys_menu` (`id`, `pid`, `permission`, `type`, `name`, `url`, `icon`, `creator`, `editor`, `create_date`, `update_date`, `del_flag`, `sort`, `version`, `visible`) VALUES ('1564996817056710701', '1535878154046939137', 'sys:config:view', '0', '配置中心', 'http://192.168.62.144:8848/nacos', 'appstore', '1341620898007281665', '1341620898007281665', '2023-02-26 21:09:04', '2023-02-26 21:32:40', '0', '404', '1', '0');

-- 2023/3/2 增加boot_sys_menu数据 老寇
INSERT INTO `boot_sys_menu` (`id`, `pid`, `permission`, `type`, `name`, `url`, `icon`, `creator`, `editor`, `create_date`, `update_date`, `del_flag`, `sort`, `version`, `visible`) VALUES ('1564996817056710705', '1545036486288732162', 'workflow:definition:template', '1', '流程模板', '/workflow/definition/api/template', 'documentation', '1341620898007281665', '1341620898007281665', '2023-03-02 07:52:36', '2023-03-02 07:53:10', '0', '10', '2', '0');

-- 2023/3/9 增加boot_sys_menu数据 老寇
INSERT INTO `boot_sys_menu` (`id`, `pid`, `permission`, `type`, `name`, `url`, `icon`, `creator`, `editor`, `create_date`, `update_date`, `del_flag`, `sort`, `version`, `visible`) VALUES (1564996817056710706, 1545037580289044482, 'workflow:task:resource:resolve', 1, '任务处理', '/workflow/task/api/resource/resolve', 'like', 1341623527018004481, NULL, '2023-03-08 07:45:38', '2023-03-08 07:45:38', 0, 10, 0, 0);
INSERT INTO `boot_sys_menu` (`id`, `pid`, `permission`, `type`, `name`, `url`, `icon`, `creator`, `editor`, `create_date`, `update_date`, `del_flag`, `sort`, `version`, `visible`) VALUES (1564996817056710707, 1545037580289044482, 'workflow:task:resource:transfer', 1, '任务转办', '/workflow/task/api/resource/transfer', 'solution', 1341623527018004481, 1537114827246292994, '2023-03-08 07:46:11', '2023-03-08 08:05:01', 0, 10, 6, 0);
INSERT INTO `boot_sys_menu` (`id`, `pid`, `permission`, `type`, `name`, `url`, `icon`, `creator`, `editor`, `create_date`, `update_date`, `del_flag`, `sort`, `version`, `visible`) VALUES (1564996817056710708, 1545037580289044482, 'workflow:task:resource:delegate', 1, '任务委派', '/workflow/task/api/resource/delegate', 'user-add', 1341623527018004481, NULL, '2023-03-08 07:51:53', '2023-03-08 07:51:53', 0, 10, 0, 0);

-- 2023/3/9 修改boot_sys_user数据 老寇
UPDATE `boot_sys_user` SET `username` = '1ftkBJEIvmOFuxJygs2jnQ==', `password` = '$2a$10$vFo4D9UwvFd6yIG3F3cZEe/RCScCooJqismrRV/FWh6F.qmDCPgxu', `super_admin` = 1, `creator` = 1341620898007281665, `editor` = 1341620898007281665, `create_date` = '2021-11-29 20:13:11', `update_date` = '2022-10-26 19:24:55', `del_flag` = 0, `mail` = 'LkZpbZYqPmJRj54OG9P8xdgn8lmGt/9oxQT94xbsZyE=', `status` = 0, `img_url` = 'http://175.178.69.253:81/upload/node3/7904fff1c08a4883b40f1ee0336017dc.webp', `mobile` = 'fX158CgrUru6HxUVFeaDdQ==', `dept_id` = 0, `version` = 8, `tenant_id` = 0 WHERE `id` = 1341620898007281665;
UPDATE `boot_sys_user` SET `username` = 'xa1Nz1ENwGc9qWoUseZn6A==', `password` = '$2a$10$jaQ.syONdH/N/UWTX6lwx.Hme2CElmCGhu0OiairxBkneSEvBD8Fe', `super_admin` = 0, `creator` = 1341620898007281665, `editor` = 1341620898007281665, `create_date` = '2023-03-09 13:52:04', `update_date` = '2023-03-09 13:52:04', `del_flag` = 0, `mail` = NULL, `status` = 0, `img_url` = 'http://175.178.69.253:81/upload/node2/b4e5bb3944a046a6bb54f8bfd2c830c1.webp', `mobile` = NULL, `dept_id` = 1535881356595175426, `version` = 0, `tenant_id` = 0 WHERE `id` = 1341623527018004481;
UPDATE `boot_sys_user` SET `username` = 'UZdko/elN7be8o8HlsNYDw==', `password` = '$2a$10$ysAmruc249SiAUpIqQzrpeM8wcdpgIJ6nEdtsXQnDrBgvLZkt7tJ6', `super_admin` = 0, `creator` = 1341620898007281665, `editor` = 1341620898007281665, `create_date` = '2021-07-31 11:29:35', `update_date` = '2022-11-04 00:13:05', `del_flag` = 0, `mail` = '', `status` = 0, `img_url` = 'https://img2.baidu.com/it/u=2432885784,4104422384&fm=253&fmt=auto&app=138&f=JPEG?w=400&h=400', `mobile` = '', `dept_id` = 1535881356595175426, `version` = 6, `tenant_id` = 0 WHERE `id` = 1421312053736804354;
UPDATE `boot_sys_user` SET `username` = 'WPvtQBxvQwzVj5SJsKCifQ==', `password` = '$2a$10$Wac.3sTE4A4pi/Zy6B/HWOstwLFjOH9g8Qrf4gHiBLa/avKAVcwpG', `super_admin` = 0, `creator` = 1341620898007281665, `editor` = 1341620898007281665, `create_date` = '2022-06-16 00:33:39', `update_date` = '2022-10-24 18:40:20', `del_flag` = 0, `mail` = NULL, `status` = 0, `img_url` = 'https://img2.baidu.com/it/u=2432885784,4104422384&fm=253&fmt=auto&app=138&f=JPEG?w=400&h=400', `mobile` = NULL, `dept_id` = 1535858679453085698, `version` = 17, `tenant_id` = 0 WHERE `id` = 1537111101311844353;
UPDATE `boot_sys_user` SET `username` = 'JSB4EWKd5aI/aISsDw0ODA==', `password` = '$2a$10$b.40TGb7W19z5Jryo3FBuOEDaX2c0OAqZHnRnCkXCPI67ru5G7Nha', `super_admin` = 0, `creator` = 1341620898007281665, `editor` = 1341620898007281665, `create_date` = '2022-06-16 00:48:28', `update_date` = '2022-10-26 19:40:13', `del_flag` = 0, `mail` = NULL, `status` = 0, `img_url` = 'https://img2.baidu.com/it/u=2432885784,4104422384&fm=253&fmt=auto&app=138&f=JPEG?w=400&h=400', `mobile` = NULL, `dept_id` = 1535881356595175426, `version` = 23, `tenant_id` = 0 WHERE `id` = 1537114827246292994;
UPDATE `boot_sys_user` SET `username` = '5/Pqo/yVzE72YyPDE5RKAw==', `password` = '$2a$10$ToBq5JB191IUkAfnqfeV5OFLOFDvhr9wWaRm1LhTn5sbL8uyJ0Gre', `super_admin` = 1, `creator` = 1341620898007281665, `editor` = 1341620898007281665, `create_date` = '2023-02-09 22:59:04', `update_date` = '2023-02-09 22:59:04', `del_flag` = 0, `mail` = NULL, `status` = 0, `img_url` = 'https://img2.baidu.com/it/u=2432885784,4104422384&fm=253&fmt=auto&app=138&f=JPEG?w=400&h=400', `mobile` = NULL, `dept_id` = 0, `version` = 0, `tenant_id` = 1 WHERE `id` = 1537114827246292998;
UPDATE `boot_sys_user` SET `username` = 'cmV6CFYc1NUWgni0E8xpdg==', `password` = '$2a$10$nbLXUQeCfuiw.7wZwuOT.e0r1mr.ZQcLIlFbil28PCrPBNAnPLRT.', `super_admin` = 0, `creator` = 1537114827246292998, `editor` = NULL, `create_date` = '2023-02-15 13:18:39', `update_date` = '2023-02-15 13:18:39', `del_flag` = 0, `mail` = NULL, `status` = 0, `img_url` = 'https://img2.baidu.com/it/u=2432885784,4104422384&fm=253&fmt=auto&app=138&f=JPEG?w=400&h=400', `mobile` = NULL, `dept_id` = 1584488411756171278, `version` = 0, `tenant_id` = 1 WHERE `id` = 1537114827246293001;

-- 2023/3/10 移除boot_sys_dept字段 老寇
alter table boot_sys_dept drop path;

-- 2023/3/10 增加boot_sys_dept索引 老寇
ALTER table boot_sys_dept ADD INDEX idx_tenant_id(`tenant_id`) comment '租户编号_索引';

-- 2023/3/11 为oauth2_authorization增加事件 老寇
-- 每天零点清理过期token
drop event if exists delete_oauth2_authorization_expire_token_every_day;
create event delete_oauth2_authorization_expire_token_every_day
on schedule every 1 day starts '2023-03-11 00:00:00'
on completion preserve
do delete from oauth2_authorization where access_token_expires_at <= now();