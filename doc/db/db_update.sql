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