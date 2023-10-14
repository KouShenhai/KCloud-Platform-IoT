DROP TABLE kcloud_platform_alibaba.boot_sys_login_log_0;
DROP TABLE kcloud_platform_alibaba.boot_sys_login_log_1;

truncate table kcloud_platform_alibaba.boot_sys_resource_audit;

UPDATE `kcloud_platform_alibaba`.`boot_sys_menu` SET `creator` = 1537114827246292994, `editor` = 1707428076142559234, `create_date` = '2022-08-27 09:16:34', `update_date` = '2023-10-14 14:10:22', `del_flag` = 0, `version` = 1, `dept_id` = 0, `dept_path` = '0', `tenant_id` = 0, `pid` = 1545036486288732162, `permission` = 'definitions:diagram', `type` = 1, `name` = '查看流程图', `url` = '/v1/definitions/diagram', `icon` = 'eyeOpen', `sort` = 10, `visible` = 0 WHERE `id` = 1545444197799067650;
UPDATE `kcloud_platform_alibaba`.`boot_sys_menu` SET `creator` = 1537114827246292994, `editor` = 1707428076142559234, `create_date` = '2022-08-26 09:18:45', `update_date` = '2023-10-14 14:10:12', `del_flag` = 0, `version` = 2, `dept_id` = 0, `dept_path` = '0', `tenant_id` = 0, `pid` = 1560530418819862529, `permission` = 'resource:diagram', `type` = 1, `name` = '查看流程', `url` = '/v1/resource/diagram', `icon` = 'gold', `sort` = 10, `visible` = 0 WHERE `id` = 1562972785472630785;