-- 初始化菜单
INSERT INTO sys_menu (pid, name, url, authority, type, open_style, icon, sort, version, deleted, creator, create_time, updater, update_time) VALUES (1, '${tableComment!}', '<#if moduleName??>${moduleName}/</#if>${classname}/index', NULL, 0, 0, 'icon-menu', 0, 0, 0, 10000, now(), 10000, now());

-- 菜单ID
set @menuId = @@identity;

INSERT INTO sys_menu (pid, name, url, authority, type, open_style, icon, sort, version, deleted, creator, create_time, updater, update_time) VALUES ((SELECT @menuId), '查看', '', '<#if moduleName??>${moduleName}:</#if>${classname}:page', 1, 0, '', 0, 0, 0, 10000, now(), 10000, now());
INSERT INTO sys_menu (pid, name, url, authority, type, open_style, icon, sort, version, deleted, creator, create_time, updater, update_time) VALUES ((SELECT @menuId), '新增', '', '<#if moduleName??>${moduleName}:</#if>${classname}:save', 1, 0, '', 1, 0, 0, 10000, now(), 10000, now());
INSERT INTO sys_menu (pid, name, url, authority, type, open_style, icon, sort, version, deleted, creator, create_time, updater, update_time) VALUES ((SELECT @menuId), '修改', '', '<#if moduleName??>${moduleName}:</#if>${classname}:update,<#if moduleName??>${moduleName}:</#if>${classname}:info', 1, 0, '', 2, 0, 0, 10000, now(), 10000, now());
INSERT INTO sys_menu (pid, name, url, authority, type, open_style, icon, sort, version, deleted, creator, create_time, updater, update_time) VALUES ((SELECT @menuId), '删除', '', '<#if moduleName??>${moduleName}:</#if>${classname}:delete', 1, 0, '', 3, 0, 0, 10000, now(), 10000, now());
