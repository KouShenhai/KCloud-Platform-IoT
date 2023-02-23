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

