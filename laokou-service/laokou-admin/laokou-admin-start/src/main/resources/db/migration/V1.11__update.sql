alter table boot_sys_oss_log add column `status` tinyint NOT NULL DEFAULT 0 comment '上传状态 0成功 1失败';
alter table boot_sys_oss_log add column `error_message` text comment '错误信息';
