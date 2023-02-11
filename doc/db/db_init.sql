-- /**
--  * Copyright (c) 2022 KCloud-Platform-Alibaba Authors. All Rights Reserved.
--  * <p>
--  * Licensed under the Apache License, Version 2.0 (the "License");
--  * you may not use this file except in compliance with the License.
--  * You may obtain a copy of the License at
--  * <p>
--  *   http://www.apache.org/licenses/LICENSE-2.0
--  * <p>
--  * Unless required by applicable law or agreed to in writing, software
--  * distributed under the License is distributed on an "AS IS" BASIS,
--  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
--  * See the License for the specific language governing permissions and
--  * limitations under the License.
--  */
-- ------------------------------------菜单------------------------------------
CREATE TABLE `boot_sys_menu` (
                                 `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'id',
                                 `pid` bigint(20) NOT NULL COMMENT '父节点',
                                 `permission` varchar(200) NOT NULL COMMENT '授权(多个用逗号分隔，如：sys:user:list,sys:user:save)',
                                 `type` tinyint(1) unsigned DEFAULT NULL COMMENT '类型   0：菜单   1：按钮',
                                 `name` varchar(100) NOT NULL COMMENT '菜单名称',
                                 `url` varchar(200) NOT NULL COMMENT '路径',
                                 `icon` varchar(50) DEFAULT NULL COMMENT '图标',
                                 `creator` bigint(20) DEFAULT NULL COMMENT '创建人',
                                 `editor` bigint(20) DEFAULT NULL COMMENT '编辑人',
                                 `create_date` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                                 `update_date` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '修改时间',
                                 `del_flag` tinyint(1) DEFAULT '0' COMMENT '1已删除 0未删除',
                                 `sort` int(11) DEFAULT '1' COMMENT '排序',
                                 `version` int(11) NOT NULL DEFAULT '0' COMMENT '版本号',
                                 PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='菜单';
INSERT INTO `boot_sys_menu` (`id`, `pid`, `permission`, `type`, `name`, `url`, `icon`, `creator`, `editor`, `create_date`, `update_date`, `del_flag`, `sort`, `version`) VALUES ('1391677542887788567', '1535878154046939137', 'sys:menu:view', '0', '菜单管理', '/sys/menu/view', 'treeTable', '1341620898007281665', '1341620898007281665', '2022-06-12 23:36:44', '2022-06-12 23:36:44', '0', '3000', '0');
INSERT INTO `boot_sys_menu` (`id`, `pid`, `permission`, `type`, `name`, `url`, `icon`, `creator`, `editor`, `create_date`, `update_date`, `del_flag`, `sort`, `version`) VALUES ('1535858679453085698', '1391677542887788567', 'sys:menu:query', '1', '菜单查询', '/sys/menu/api/query', 'search', '1341620898007281665', '1341620898007281665', '2022-06-22 07:09:59', '2022-06-21 23:11:00', '0', '10', '0');
INSERT INTO `boot_sys_menu` (`id`, `pid`, `permission`, `type`, `name`, `url`, `icon`, `creator`, `editor`, `create_date`, `update_date`, `del_flag`, `sort`, `version`) VALUES ('1535859148908949506', '1391677542887788567', 'sys:menu:insert', '1', '菜单新增', '/sys/menu/api/insert', 'plus', '1341620898007281665', NULL, '2022-06-12 21:59:41', '2022-06-12 21:59:41', '0', '10', '0');
INSERT INTO `boot_sys_menu` (`id`, `pid`, `permission`, `type`, `name`, `url`, `icon`, `creator`, `editor`, `create_date`, `update_date`, `del_flag`, `sort`, `version`) VALUES ('1535859326311231489', '1391677542887788567', 'sys:menu:update', '1', '菜单修改', '/sys/menu/api/update', 'edit', '1341620898007281665', '1341620898007281665', '2022-06-20 01:45:22', '2022-06-19 17:46:23', '0', '10', '0');
INSERT INTO `boot_sys_menu` (`id`, `pid`, `permission`, `type`, `name`, `url`, `icon`, `creator`, `editor`, `create_date`, `update_date`, `del_flag`, `sort`, `version`) VALUES ('1535859588534923265', '1391677542887788567', 'sys:menu:delete', '1', '菜单删除', '/sys/menu/api/delete', 'delete', '1341620898007281665', NULL, '2022-06-12 13:40:35', '2022-06-12 13:40:35', '0', '10', '0');
INSERT INTO `boot_sys_menu` (`id`, `pid`, `permission`, `type`, `name`, `url`, `icon`, `creator`, `editor`, `create_date`, `update_date`, `del_flag`, `sort`, `version`) VALUES ('1535878154046939137', '0', 'sys:view', '0', '系统管理', '/sys/view', 'system', '1341620898007281665', '1341620898007281665', '2022-10-26 19:26:21', '2023-01-30 17:34:41', '0', '9000', '1');
INSERT INTO `boot_sys_menu` (`id`, `pid`, `permission`, `type`, `name`, `url`, `icon`, `creator`, `editor`, `create_date`, `update_date`, `del_flag`, `sort`, `version`) VALUES ('1535881096963563522', '1535878154046939137', 'sys:user:view', '0', '用户管理', '/sys/user/view', 'user', '1341620898007281665', '1341620898007281665', '2022-06-16 04:04:56', '2022-06-15 20:05:57', '0', '1000', '0');
INSERT INTO `boot_sys_menu` (`id`, `pid`, `permission`, `type`, `name`, `url`, `icon`, `creator`, `editor`, `create_date`, `update_date`, `del_flag`, `sort`, `version`) VALUES ('1535881356595175426', '1535878154046939137', 'sys:role:view', '0', '角色管理', '/sys/role/view', 'peoples', '1341620898007281665', NULL, '2022-06-12 15:07:05', '2022-06-12 15:07:05', '0', '2000', '0');
INSERT INTO `boot_sys_menu` (`id`, `pid`, `permission`, `type`, `name`, `url`, `icon`, `creator`, `editor`, `create_date`, `update_date`, `del_flag`, `sort`, `version`) VALUES ('1535886982654205953', '1535881356595175426', 'sys:role:query', '1', '角色查询', '/sys/role/api/query', 'search', '1341620898007281665', '1341620898007281665', '2022-06-22 07:10:10', '2022-06-21 23:11:10', '0', '10', '0');
INSERT INTO `boot_sys_menu` (`id`, `pid`, `permission`, `type`, `name`, `url`, `icon`, `creator`, `editor`, `create_date`, `update_date`, `del_flag`, `sort`, `version`) VALUES ('1535887129341599746', '1535881356595175426', 'sys:role:insert', '1', '角色新增', '/sys/role/api/insert', 'plus', '1341620898007281665', NULL, '2022-06-12 15:30:02', '2022-06-12 15:30:02', '0', '10', '0');
INSERT INTO `boot_sys_menu` (`id`, `pid`, `permission`, `type`, `name`, `url`, `icon`, `creator`, `editor`, `create_date`, `update_date`, `del_flag`, `sort`, `version`) VALUES ('1535887276649750530', '1535881356595175426', 'sys:role:update', '1', '角色修改', '/sys/role/api/update', 'edit', '1341620898007281665', '1341620898007281665', '2022-06-20 01:45:31', '2022-06-19 17:46:31', '0', '10', '0');
INSERT INTO `boot_sys_menu` (`id`, `pid`, `permission`, `type`, `name`, `url`, `icon`, `creator`, `editor`, `create_date`, `update_date`, `del_flag`, `sort`, `version`) VALUES ('1535887450809835521', '1535881356595175426', 'sys:role:delete', '1', '角色删除', '/sys/role/api/delete', 'delete', '1341620898007281665', NULL, '2022-06-12 15:31:18', '2022-06-12 15:31:18', '0', '10', '0');
INSERT INTO `boot_sys_menu` (`id`, `pid`, `permission`, `type`, `name`, `url`, `icon`, `creator`, `editor`, `create_date`, `update_date`, `del_flag`, `sort`, `version`) VALUES ('1535887779873955841', '1535881096963563522', 'sys:user:query', '1', '用户查询', '/sys/user/api/query', 'search', '1341620898007281665', '1341620898007281665', '2022-06-22 07:10:19', '2022-06-21 23:11:20', '0', '10', '0');
INSERT INTO `boot_sys_menu` (`id`, `pid`, `permission`, `type`, `name`, `url`, `icon`, `creator`, `editor`, `create_date`, `update_date`, `del_flag`, `sort`, `version`) VALUES ('1535887940687765505', '1535881096963563522', 'sys:user:insert', '1', '用户新增', '/sys/user/api/insert', 'plus', '1341620898007281665', '1341620898007281665', '2022-07-23 15:34:06', '2022-07-23 15:34:06', '0', '10', '0');
INSERT INTO `boot_sys_menu` (`id`, `pid`, `permission`, `type`, `name`, `url`, `icon`, `creator`, `editor`, `create_date`, `update_date`, `del_flag`, `sort`, `version`) VALUES ('1535888146045083649', '1535881096963563522', 'sys:user:update', '1', '用户修改', '/sys/user/api/update', 'edit', '1341620898007281665', '1341620898007281665', '2022-06-20 01:45:37', '2022-06-19 17:46:37', '0', '10', '0');
INSERT INTO `boot_sys_menu` (`id`, `pid`, `permission`, `type`, `name`, `url`, `icon`, `creator`, `editor`, `create_date`, `update_date`, `del_flag`, `sort`, `version`) VALUES ('1535888341252186114', '1535881096963563522', 'sys:user:delete', '1', '用户删除', '/sys/user/api/delete', 'delete', '1341620898007281665', NULL, '2022-06-12 23:37:47', '2022-06-12 23:37:47', '0', '10', '0');
INSERT INTO `boot_sys_menu` (`id`, `pid`, `permission`, `type`, `name`, `url`, `icon`, `creator`, `editor`, `create_date`, `update_date`, `del_flag`, `sort`, `version`) VALUES ('1537444981390794754', '0', 'monitor:view', '0', '系统监控', '/monitor/view', 'monitor', '1341620898007281665', '1341620898007281665', '2022-10-26 19:26:30', '2022-10-26 19:26:30', '0', '8000', '0');
INSERT INTO `boot_sys_menu` (`id`, `pid`, `permission`, `type`, `name`, `url`, `icon`, `creator`, `editor`, `create_date`, `update_date`, `del_flag`, `sort`, `version`) VALUES ('1538469199368712193', '1537444981390794754', 'monitor:admin:view', '0', '服务监控', 'http://192.168.62.1:5000/monitor', 'server', '1341620898007281665', '1341620898007281665', '2022-07-17 17:39:45', '2023-01-12 07:36:25', '0', '3000', '0');
INSERT INTO `boot_sys_menu` (`id`, `pid`, `permission`, `type`, `name`, `url`, `icon`, `creator`, `editor`, `create_date`, `update_date`, `del_flag`, `sort`, `version`) VALUES ('1539250224424394753', '1535878154046939137', 'sys:log:view', '0', '日志管理', '/sys/log/view', 'log', '1341620898007281665', '1341620898007281665', '2022-08-23 12:04:04', '2022-08-23 12:04:04', '0', '500', '0');
INSERT INTO `boot_sys_menu` (`id`, `pid`, `permission`, `type`, `name`, `url`, `icon`, `creator`, `editor`, `create_date`, `update_date`, `del_flag`, `sort`, `version`) VALUES ('1539251440843857922', '1539250224424394753', 'sys:log:operate:view', '0', '操作日志', '/sys/log/operate/view', 'form', '1341620898007281665', '1341620898007281665', '2022-06-24 06:45:42', '2022-06-23 23:13:09', '0', '200', '0');
INSERT INTO `boot_sys_menu` (`id`, `pid`, `permission`, `type`, `name`, `url`, `icon`, `creator`, `editor`, `create_date`, `update_date`, `del_flag`, `sort`, `version`) VALUES ('1539265093588545538', '1539251440843857922', 'sys:log:operate:query', '1', '查询日志', '/sys/log/api/operate/query', 'search', '1341620898007281665', '1341620898007281665', '2022-06-24 06:46:11', '2022-06-23 23:13:38', '0', '10', '0');
INSERT INTO `boot_sys_menu` (`id`, `pid`, `permission`, `type`, `name`, `url`, `icon`, `creator`, `editor`, `create_date`, `update_date`, `del_flag`, `sort`, `version`) VALUES ('1539396453854629890', '1539250224424394753', 'sys:log:login:view', '0', '登录日志', '/sys/log/login/view', 'logininfor', '1341620898007281665', '1341620898007281665', '2022-06-24 06:46:01', '2022-06-23 23:13:28', '0', '100', '0');
INSERT INTO `boot_sys_menu` (`id`, `pid`, `permission`, `type`, `name`, `url`, `icon`, `creator`, `editor`, `create_date`, `update_date`, `del_flag`, `sort`, `version`) VALUES ('1539402478934646785', '1539396453854629890', 'sys:log:login:query', '1', '查询日志', '/sys/log/api/login/query', 'search', '1341620898007281665', '1341620898007281665', '2022-06-24 06:45:53', '2022-06-23 23:13:20', '0', '10', '0');
INSERT INTO `boot_sys_menu` (`id`, `pid`, `permission`, `type`, `name`, `url`, `icon`, `creator`, `editor`, `create_date`, `update_date`, `del_flag`, `sort`, `version`) VALUES ('1539615028590673921', '1535881096963563522', 'sys:user:password', '1', '重置密码', '/sys/user/api/password', 'key', '1341620898007281665', NULL, '2022-06-23 06:22:41', '2022-06-23 06:22:41', '0', '10', '0');
INSERT INTO `boot_sys_menu` (`id`, `pid`, `permission`, `type`, `name`, `url`, `icon`, `creator`, `editor`, `create_date`, `update_date`, `del_flag`, `sort`, `version`) VALUES ('1539989085181972481', '1535878154046939137', 'sys:dict:view', '0', '字典管理', '/sys/dict/view', 'dict', '1341620898007281665', '1341620898007281665', '2022-06-24 07:01:55', '2022-06-23 23:29:23', '0', '900', '0');
INSERT INTO `boot_sys_menu` (`id`, `pid`, `permission`, `type`, `name`, `url`, `icon`, `creator`, `editor`, `create_date`, `update_date`, `del_flag`, `sort`, `version`) VALUES ('1540000886082768897', '1539989085181972481', 'sys:dict:query', '1', '字典查询', '/sys/dict/api/query', 'search', '1341620898007281665', '1341620898007281665', '2022-06-24 07:30:24', '2022-06-23 23:57:51', '0', '10', '0');
INSERT INTO `boot_sys_menu` (`id`, `pid`, `permission`, `type`, `name`, `url`, `icon`, `creator`, `editor`, `create_date`, `update_date`, `del_flag`, `sort`, `version`) VALUES ('1540001496240754689', '1539989085181972481', 'sys:dict:insert', '1', '字典新增', '/sys/dict/api/insert', 'plus', '1341620898007281665', '1341620898007281665', '2022-06-24 07:32:06', '2022-06-23 23:59:33', '0', '10', '0');
INSERT INTO `boot_sys_menu` (`id`, `pid`, `permission`, `type`, `name`, `url`, `icon`, `creator`, `editor`, `create_date`, `update_date`, `del_flag`, `sort`, `version`) VALUES ('1540001809446211586', '1539989085181972481', 'sys:dict:update', '1', '字典修改', '/sys/dict/api/update', 'edit', '1341620898007281665', NULL, '2022-06-24 00:00:18', '2022-06-24 00:00:18', '0', '10', '0');
INSERT INTO `boot_sys_menu` (`id`, `pid`, `permission`, `type`, `name`, `url`, `icon`, `creator`, `editor`, `create_date`, `update_date`, `del_flag`, `sort`, `version`) VALUES ('1540001992288505857', '1539989085181972481', 'sys:dict:delete', '1', '字典删除', '/sys/dict/api/delete', 'delete', '1341620898007281665', '1341620898007281665', '2022-06-24 07:33:59', '2022-06-24 00:01:26', '0', '10', '0');
INSERT INTO `boot_sys_menu` (`id`, `pid`, `permission`, `type`, `name`, `url`, `icon`, `creator`, `editor`, `create_date`, `update_date`, `del_flag`, `sort`, `version`) VALUES ('1545035717690912769', '0', 'workflow:view', '0', '工作流程', '/workflow/view', 'tree', '1341620898007281665', '1341620898007281665', '2022-10-26 19:26:25', '2022-10-26 19:26:25', '0', '7000', '0');
INSERT INTO `boot_sys_menu` (`id`, `pid`, `permission`, `type`, `name`, `url`, `icon`, `creator`, `editor`, `create_date`, `update_date`, `del_flag`, `sort`, `version`) VALUES ('1545036486288732162', '1545035717690912769', 'workflow:definition:view', '0', '流程定义', '/workflow/definition/view', 'guide', '1341620898007281665', '1341620898007281665', '2022-07-07 21:27:18', '2022-07-07 21:27:18', '0', '5000', '0');
INSERT INTO `boot_sys_menu` (`id`, `pid`, `permission`, `type`, `name`, `url`, `icon`, `creator`, `editor`, `create_date`, `update_date`, `del_flag`, `sort`, `version`) VALUES ('1545037580289044482', '1560528267620061186', 'sys:resource:audit:view', '0', '资源审批', '/sys/resource/audit/view', 'timeRange', '1341620898007281665', '1341620898007281665', '2022-07-07 21:34:30', '2022-12-09 20:29:45', '0', '99', '0');
INSERT INTO `boot_sys_menu` (`id`, `pid`, `permission`, `type`, `name`, `url`, `icon`, `creator`, `editor`, `create_date`, `update_date`, `del_flag`, `sort`, `version`) VALUES ('1545442687073681410', '1545036486288732162', 'workflow:definition:insert', '1', '流程新增', '/workflow/definition/api/insert', 'plus', '1537114827246292994', NULL, '2022-07-09 00:20:24', '2022-07-09 00:20:24', '0', '10', '0');
INSERT INTO `boot_sys_menu` (`id`, `pid`, `permission`, `type`, `name`, `url`, `icon`, `creator`, `editor`, `create_date`, `update_date`, `del_flag`, `sort`, `version`) VALUES ('1545442932918616065', '1545036486288732162', 'workflow:definition:query', '1', '流程查询', '/workflow/definition/api/query', 'search', '1537114827246292994', NULL, '2022-08-25 16:31:34', '2022-08-25 16:31:34', '0', '10', '0');
INSERT INTO `boot_sys_menu` (`id`, `pid`, `permission`, `type`, `name`, `url`, `icon`, `creator`, `editor`, `create_date`, `update_date`, `del_flag`, `sort`, `version`) VALUES ('1545443357407346689', '1545036486288732162', 'workflow:definition:activate', '1', '流程激活', '/workflow/definition/api/activate', 'play-circle', '1537114827246292994', NULL, '2022-07-09 00:23:04', '2022-07-09 00:23:04', '0', '10', '0');
INSERT INTO `boot_sys_menu` (`id`, `pid`, `permission`, `type`, `name`, `url`, `icon`, `creator`, `editor`, `create_date`, `update_date`, `del_flag`, `sort`, `version`) VALUES ('1545443597204094977', '1545036486288732162', 'workflow:definition:suspend', '1', '流程挂起', '/workflow/definition/api/suspend', 'pause-circle', '1537114827246292994', NULL, '2022-07-09 00:24:01', '2022-07-09 00:24:01', '0', '10', '0');
INSERT INTO `boot_sys_menu` (`id`, `pid`, `permission`, `type`, `name`, `url`, `icon`, `creator`, `editor`, `create_date`, `update_date`, `del_flag`, `sort`, `version`) VALUES ('1545443828113113090', '1545036486288732162', 'workflow:definition:delete', '1', '流程删除', '/workflow/definition/api/delete', 'delete', '1537114827246292994', NULL, '2022-07-09 00:24:56', '2022-07-09 00:24:56', '0', '10', '0');
INSERT INTO `boot_sys_menu` (`id`, `pid`, `permission`, `type`, `name`, `url`, `icon`, `creator`, `editor`, `create_date`, `update_date`, `del_flag`, `sort`, `version`) VALUES ('1545444197799067650', '1545036486288732162', 'workflow:definition:diagram', '1', '流程查看', '/workflow/definition/api/diagram', 'eyeOpen', '1537114827246292994', '1341620898007281665', '2022-08-27 09:16:34', '2022-12-09 21:05:54', '0', '10', '0');
INSERT INTO `boot_sys_menu` (`id`, `pid`, `permission`, `type`, `name`, `url`, `icon`, `creator`, `editor`, `create_date`, `update_date`, `del_flag`, `sort`, `version`) VALUES ('1545444853809184770', '1545037580289044482', 'workflow:task:resource:query', '1', '任务查询', '/workflow/task/api/resource/query', 'search', '1537114827246292994', '1341620898007281665', '2022-08-25 13:27:40', '2022-12-09 20:32:50', '0', '10', '0');
INSERT INTO `boot_sys_menu` (`id`, `pid`, `permission`, `type`, `name`, `url`, `icon`, `creator`, `editor`, `create_date`, `update_date`, `del_flag`, `sort`, `version`) VALUES ('1545445208781520897', '1545037580289044482', 'workflow:task:resource:audit', '1', '任务审批', '/workflow/task/api/resource/audit', 'audit', '1537114827246292994', '1341620898007281665', '2022-08-25 13:27:25', '2022-12-09 20:36:38', '0', '10', '0');
INSERT INTO `boot_sys_menu` (`id`, `pid`, `permission`, `type`, `name`, `url`, `icon`, `creator`, `editor`, `create_date`, `update_date`, `del_flag`, `sort`, `version`) VALUES ('1547109188256260097', '1535878154046939137', 'sys:api:view', '0', '接口文档', 'http://192.168.62.1:5555/swagger-ui.html', 'row', '1341620898007281665', '1341620898007281665', '2022-07-17 17:04:18', '2023-01-12 07:37:02', '0', '400', '0');
INSERT INTO `boot_sys_menu` (`id`, `pid`, `permission`, `type`, `name`, `url`, `icon`, `creator`, `editor`, `create_date`, `update_date`, `del_flag`, `sort`, `version`) VALUES ('1549758203624480770', '1535878154046939137', 'sys:message:view', '0', '消息管理', '/sys/message/view', 'message', '1341620898007281665', NULL, '2022-07-20 22:08:44', '2022-07-20 22:08:44', '0', '800', '0');
INSERT INTO `boot_sys_menu` (`id`, `pid`, `permission`, `type`, `name`, `url`, `icon`, `creator`, `editor`, `create_date`, `update_date`, `del_flag`, `sort`, `version`) VALUES ('1550116610713944065', '1549758203624480770', 'sys:message:query', '1', '消息查询', '/sys/message/api/query', 'search', '1341620898007281665', NULL, '2022-08-25 16:32:35', '2022-08-25 16:32:35', '0', '10', '0');
INSERT INTO `boot_sys_menu` (`id`, `pid`, `permission`, `type`, `name`, `url`, `icon`, `creator`, `editor`, `create_date`, `update_date`, `del_flag`, `sort`, `version`) VALUES ('1550117239402364930', '1549758203624480770', 'sys:message:insert', '1', '消息新增', '/sys/message/api/insert', 'plus', '1341620898007281665', '1341620898007281665', '2022-08-29 19:19:22', '2022-08-29 19:19:22', '0', '10', '0');
INSERT INTO `boot_sys_menu` (`id`, `pid`, `permission`, `type`, `name`, `url`, `icon`, `creator`, `editor`, `create_date`, `update_date`, `del_flag`, `sort`, `version`) VALUES ('1550117550498086913', '1549758203624480770', 'sys:message:detail', '1', '消息查看', '/sys/message/api/detail', 'eyeOpen', '1341620898007281665', NULL, '2022-07-21 21:56:38', '2022-07-21 21:56:38', '0', '10', '0');
INSERT INTO `boot_sys_menu` (`id`, `pid`, `permission`, `type`, `name`, `url`, `icon`, `creator`, `editor`, `create_date`, `update_date`, `del_flag`, `sort`, `version`) VALUES ('1551957039155638274', '1535878154046939137', 'sys:dept:view', '0', '部门管理', '/sys/dept/view', 'team', '1341620898007281665', NULL, '2022-07-26 23:46:07', '2022-07-26 23:46:07', '0', '999', '0');
INSERT INTO `boot_sys_menu` (`id`, `pid`, `permission`, `type`, `name`, `url`, `icon`, `creator`, `editor`, `create_date`, `update_date`, `del_flag`, `sort`, `version`) VALUES ('1552304553893392386', '1551957039155638274', 'sys:dept:query', '1', '部门查询', '/sys/dept/api/query', 'search', '1341620898007281665', NULL, '2022-08-25 16:32:48', '2022-08-25 16:32:48', '0', '10', '0');
INSERT INTO `boot_sys_menu` (`id`, `pid`, `permission`, `type`, `name`, `url`, `icon`, `creator`, `editor`, `create_date`, `update_date`, `del_flag`, `sort`, `version`) VALUES ('1552304713121755138', '1551957039155638274', 'sys:dept:insert', '1', '部门新增', '/sys/dept/api/insert', 'plus', '1341620898007281665', NULL, '2022-07-27 22:47:39', '2022-07-27 22:47:39', '0', '10', '0');
INSERT INTO `boot_sys_menu` (`id`, `pid`, `permission`, `type`, `name`, `url`, `icon`, `creator`, `editor`, `create_date`, `update_date`, `del_flag`, `sort`, `version`) VALUES ('1552304865207218177', '1551957039155638274', 'sys:dept:update', '1', '部门修改', '/sys/dept/api/update', 'edit', '1341620898007281665', NULL, '2022-07-27 22:48:15', '2022-07-27 22:48:15', '0', '10', '0');
INSERT INTO `boot_sys_menu` (`id`, `pid`, `permission`, `type`, `name`, `url`, `icon`, `creator`, `editor`, `create_date`, `update_date`, `del_flag`, `sort`, `version`) VALUES ('1552305016701284353', '1551957039155638274', 'sys:dept:delete', '1', '部门删除', '/sys/dept/api/delete', 'delete', '1341620898007281665', NULL, '2022-07-27 22:48:51', '2022-07-27 22:48:51', '0', '10', '0');
INSERT INTO `boot_sys_menu` (`id`, `pid`, `permission`, `type`, `name`, `url`, `icon`, `creator`, `editor`, `create_date`, `update_date`, `del_flag`, `sort`, `version`) VALUES ('1552525480564416514', '1537444981390794754', 'sys:monitor:cache', '0', '缓存监控', '/monitor/cache/view', 'redis', '1341620898007281665', '1341620898007281665', '2022-07-28 23:30:24', '2022-07-28 23:30:25', '0', '2000', '0');
INSERT INTO `boot_sys_menu` (`id`, `pid`, `permission`, `type`, `name`, `url`, `icon`, `creator`, `editor`, `create_date`, `update_date`, `del_flag`, `sort`, `version`) VALUES ('1552526314178142209', '1537444981390794754', 'sys:monitor:server', '0', '主机监控', '/monitor/server/view', 'hdd', '1341620898007281665', '1341620898007281665', '2022-07-28 23:30:04', '2022-07-28 23:30:05', '0', '1000', '0');
INSERT INTO `boot_sys_menu` (`id`, `pid`, `permission`, `type`, `name`, `url`, `icon`, `creator`, `editor`, `create_date`, `update_date`, `del_flag`, `sort`, `version`) VALUES ('1560528267620061186', '1535878154046939137', 'sys:resource:view', '0', '资源管理', '/sys/resource/view', 'folder', '1341620898007281665', '1341620898007281665', '2022-08-23 12:03:22', '2022-08-23 12:03:22', '0', '510', '0');
INSERT INTO `boot_sys_menu` (`id`, `pid`, `permission`, `type`, `name`, `url`, `icon`, `creator`, `editor`, `create_date`, `update_date`, `del_flag`, `sort`, `version`) VALUES ('1560529603300364290', '1560528267620061186', 'sys:resource:audio:view', '0', '音频管理', '/sys/resource/audio/view', 'customer-service', '1341620898007281665', '1341620898007281665', '2022-08-19 15:31:27', '2022-08-19 15:31:28', '0', '100', '0');
INSERT INTO `boot_sys_menu` (`id`, `pid`, `permission`, `type`, `name`, `url`, `icon`, `creator`, `editor`, `create_date`, `update_date`, `del_flag`, `sort`, `version`) VALUES ('1560530192751071234', '1560528267620061186', 'sys:resource:video:view', '0', '视频管理', '/sys/resource/video/view', 'video-camera', '1341620898007281665', NULL, '2022-08-19 15:32:46', '2022-08-19 15:32:46', '0', '200', '0');
INSERT INTO `boot_sys_menu` (`id`, `pid`, `permission`, `type`, `name`, `url`, `icon`, `creator`, `editor`, `create_date`, `update_date`, `del_flag`, `sort`, `version`) VALUES ('1560530418819862529', '1560528267620061186', 'sys:resource:image:view', '0', '图片管理', '/sys/resource/image/view', 'picture', '1341620898007281665', '1341620898007281665', '2022-08-19 15:34:18', '2022-08-19 15:34:18', '0', '300', '0');
INSERT INTO `boot_sys_menu` (`id`, `pid`, `permission`, `type`, `name`, `url`, `icon`, `creator`, `editor`, `create_date`, `update_date`, `del_flag`, `sort`, `version`) VALUES ('1562969136453316610', '1560529603300364290', 'sys:resource:audio:query', '1', '音频查询', '/sys/resource/audio/api/query', 'search', '1537114827246292994', NULL, '2022-08-26 09:04:15', '2022-08-26 09:04:15', '0', '10', '0');
INSERT INTO `boot_sys_menu` (`id`, `pid`, `permission`, `type`, `name`, `url`, `icon`, `creator`, `editor`, `create_date`, `update_date`, `del_flag`, `sort`, `version`) VALUES ('1562969406365167618', '1560529603300364290', 'sys:resource:audio:insert', '1', '音频新增', '/sys/resource/audio/api/insert', 'plus', '1537114827246292994', NULL, '2022-08-26 09:05:20', '2022-08-26 09:05:20', '0', '10', '0');
INSERT INTO `boot_sys_menu` (`id`, `pid`, `permission`, `type`, `name`, `url`, `icon`, `creator`, `editor`, `create_date`, `update_date`, `del_flag`, `sort`, `version`) VALUES ('1562969780283174914', '1560529603300364290', 'sys:resource:audio:update', '1', '音频修改', '/sys/resource/audio/api/update', 'edit', '1537114827246292994', NULL, '2022-08-26 09:06:49', '2022-08-26 09:06:49', '0', '10', '0');
INSERT INTO `boot_sys_menu` (`id`, `pid`, `permission`, `type`, `name`, `url`, `icon`, `creator`, `editor`, `create_date`, `update_date`, `del_flag`, `sort`, `version`) VALUES ('1562970001914392577', '1560529603300364290', 'sys:resource:audio:delete', '1', '音频删除', '/sys/resource/audio/api/delete', 'delete', '1537114827246292994', NULL, '2022-08-26 09:07:42', '2022-08-26 09:07:42', '0', '10', '0');
INSERT INTO `boot_sys_menu` (`id`, `pid`, `permission`, `type`, `name`, `url`, `icon`, `creator`, `editor`, `create_date`, `update_date`, `del_flag`, `sort`, `version`) VALUES ('1562970341317472258', '1560529603300364290', 'sys:resource:audio:diagram', '1', '音频流程图', '/sys/resource/audio/api/diagram', 'gold', '1537114827246292994', '1537114827246292994', '2022-08-26 09:18:57', '2022-08-26 09:18:58', '0', '10', '0');
INSERT INTO `boot_sys_menu` (`id`, `pid`, `permission`, `type`, `name`, `url`, `icon`, `creator`, `editor`, `create_date`, `update_date`, `del_flag`, `sort`, `version`) VALUES ('1562971590226014209', '1560530418819862529', 'sys:resource:image:query', '1', '图片查询', '/sys/resource/image/api/query', 'search', '1537114827246292994', NULL, '2022-08-26 09:14:00', '2022-08-26 09:14:00', '0', '10', '0');
INSERT INTO `boot_sys_menu` (`id`, `pid`, `permission`, `type`, `name`, `url`, `icon`, `creator`, `editor`, `create_date`, `update_date`, `del_flag`, `sort`, `version`) VALUES ('1562971829607526401', '1560530418819862529', 'sys:resource:image:insert', '1', '图片新增', '/sys/resource/image/api/insert', 'plus', '1537114827246292994', NULL, '2022-08-26 09:14:57', '2022-08-26 09:14:57', '0', '10', '0');
INSERT INTO `boot_sys_menu` (`id`, `pid`, `permission`, `type`, `name`, `url`, `icon`, `creator`, `editor`, `create_date`, `update_date`, `del_flag`, `sort`, `version`) VALUES ('1562972243434336257', '1560530418819862529', 'sys:resource:image:update', '1', '图片修改', '/sys/resource/image/api/update', 'edit', '1537114827246292994', NULL, '2022-08-26 09:16:36', '2022-08-26 09:16:36', '0', '10', '0');
INSERT INTO `boot_sys_menu` (`id`, `pid`, `permission`, `type`, `name`, `url`, `icon`, `creator`, `editor`, `create_date`, `update_date`, `del_flag`, `sort`, `version`) VALUES ('1562972411969859586', '1560530418819862529', 'sys:resource:image:delete', '1', '图片删除', '/sys/resource/image/api/delete', 'delete', '1537114827246292994', NULL, '2022-08-26 09:17:16', '2022-08-26 09:17:16', '0', '10', '0');
INSERT INTO `boot_sys_menu` (`id`, `pid`, `permission`, `type`, `name`, `url`, `icon`, `creator`, `editor`, `create_date`, `update_date`, `del_flag`, `sort`, `version`) VALUES ('1562972785472630785', '1560530418819862529', 'sys:resource:image:diagram', '1', '图片流程图', '/sys/resource/image/api/diagram', 'gold', '1537114827246292994', NULL, '2022-08-26 09:18:45', '2022-08-26 09:18:45', '0', '10', '0');
INSERT INTO `boot_sys_menu` (`id`, `pid`, `permission`, `type`, `name`, `url`, `icon`, `creator`, `editor`, `create_date`, `update_date`, `del_flag`, `sort`, `version`) VALUES ('1562973157121519617', '1560530192751071234', 'sys:resource:video:query', '1', '视频查询', '/sys/resource/video/api/query', 'search', '1537114827246292994', NULL, '2022-08-26 09:20:14', '2022-08-26 09:20:14', '0', '10', '0');
INSERT INTO `boot_sys_menu` (`id`, `pid`, `permission`, `type`, `name`, `url`, `icon`, `creator`, `editor`, `create_date`, `update_date`, `del_flag`, `sort`, `version`) VALUES ('1562973326504292353', '1560530192751071234', 'sys:resource:video:insert', '1', '视频新增', '/sys/resource/video/api/insert', 'plus', '1537114827246292994', NULL, '2022-08-26 09:20:54', '2022-08-26 09:20:54', '0', '10', '0');
INSERT INTO `boot_sys_menu` (`id`, `pid`, `permission`, `type`, `name`, `url`, `icon`, `creator`, `editor`, `create_date`, `update_date`, `del_flag`, `sort`, `version`) VALUES ('1562973502765723650', '1560530192751071234', 'sys:resource:video:update', '1', '视频修改', '/sys/resource/video/api/update', 'edit', '1537114827246292994', NULL, '2022-08-26 09:21:36', '2022-08-26 09:21:36', '0', '10', '0');
INSERT INTO `boot_sys_menu` (`id`, `pid`, `permission`, `type`, `name`, `url`, `icon`, `creator`, `editor`, `create_date`, `update_date`, `del_flag`, `sort`, `version`) VALUES ('1562973726783500290', '1560530192751071234', 'sys:resource:video:delete', '1', '视频删除', '/sys/resource/video/api/delete', 'delete', '1537114827246292994', NULL, '2022-08-26 09:22:30', '2022-08-26 09:22:30', '0', '10', '0');
INSERT INTO `boot_sys_menu` (`id`, `pid`, `permission`, `type`, `name`, `url`, `icon`, `creator`, `editor`, `create_date`, `update_date`, `del_flag`, `sort`, `version`) VALUES ('1562973963560349698', '1560530192751071234', 'sys:resource:video:diagram', '1', '视频流程图', '/sys/resource/video/api/diagram', 'gold', '1537114827246292994', NULL, '2022-08-26 09:23:26', '2022-08-26 09:23:26', '0', '10', '0');
INSERT INTO `boot_sys_menu` (`id`, `pid`, `permission`, `type`, `name`, `url`, `icon`, `creator`, `editor`, `create_date`, `update_date`, `del_flag`, `sort`, `version`) VALUES ('1563103435471122433', '1560529603300364290', 'sys:resource:audio:auditLog', '1', '音频-审批日志', '/sys/resource/audio/api/auditLog', 'file', '1341620898007281665', '1341620898007281665', '2022-08-27 09:08:26', '2022-12-10 16:33:19', '0', '10', '0');
INSERT INTO `boot_sys_menu` (`id`, `pid`, `permission`, `type`, `name`, `url`, `icon`, `creator`, `editor`, `create_date`, `update_date`, `del_flag`, `sort`, `version`) VALUES ('1563104611147116545', '1560530192751071234', 'sys:resource:video:auditLog', '1', '视频-审批日志', '/sys/resource/video/api/auditLog', 'file', '1341620898007281665', '1341620898007281665', '2022-08-27 09:08:37', '2022-12-10 16:32:11', '0', '10', '0');
INSERT INTO `boot_sys_menu` (`id`, `pid`, `permission`, `type`, `name`, `url`, `icon`, `creator`, `editor`, `create_date`, `update_date`, `del_flag`, `sort`, `version`) VALUES ('1563104888692600833', '1560530418819862529', 'sys:resource:image:auditLog', '1', '图片-审批日志', '/sys/resource/image/api/auditLog', 'file', '1341620898007281665', '1341620898007281665', '2022-08-27 09:08:46', '2022-12-10 16:32:30', '0', '10', '0');
INSERT INTO `boot_sys_menu` (`id`, `pid`, `permission`, `type`, `name`, `url`, `icon`, `creator`, `editor`, `create_date`, `update_date`, `del_flag`, `sort`, `version`) VALUES ('1563333871266885634', '1560530418819862529', 'sys:resource:image:detail', '1', '图片查看', '/sys/resource/image/api/detail', 'eyeOpen', '1341620898007281665', NULL, '2022-08-27 09:16:33', '2022-08-27 09:16:33', '0', '10', '0');
INSERT INTO `boot_sys_menu` (`id`, `pid`, `permission`, `type`, `name`, `url`, `icon`, `creator`, `editor`, `create_date`, `update_date`, `del_flag`, `sort`, `version`) VALUES ('1563334139421323265', '1560530192751071234', 'sys:resource:video:detail', '1', '视频查看', '/sys/resource/video/api/detail', 'eyeOpen', '1341620898007281665', NULL, '2022-08-27 09:14:39', '2022-08-27 09:14:39', '0', '10', '0');
INSERT INTO `boot_sys_menu` (`id`, `pid`, `permission`, `type`, `name`, `url`, `icon`, `creator`, `editor`, `create_date`, `update_date`, `del_flag`, `sort`, `version`) VALUES ('1563334404471975938', '1560529603300364290', 'sys:resource:audio:detail', '1', '音频查看', '/sys/resource/audio/api/detail', 'eyeOpen', '1341620898007281665', NULL, '2022-08-27 09:16:31', '2022-08-27 09:16:31', '0', '10', '0');
INSERT INTO `boot_sys_menu` (`id`, `pid`, `permission`, `type`, `name`, `url`, `icon`, `creator`, `editor`, `create_date`, `update_date`, `del_flag`, `sort`, `version`) VALUES ('1564211460488826881', '1560529603300364290', 'sys:resource:audio:complete:syncIndex', '1', '音频-全量同步', '/sys/resource/audio/api/complete/syncIndex', 'undo', '1341620898007281665', '1341620898007281665', '2022-08-29 19:20:48', '2023-01-17 15:56:33', '0', '10', '0');
INSERT INTO `boot_sys_menu` (`id`, `pid`, `permission`, `type`, `name`, `url`, `icon`, `creator`, `editor`, `create_date`, `update_date`, `del_flag`, `sort`, `version`) VALUES ('1564214128678539265', '1560530418819862529', 'sys:resource:image:complete:syncIndex', '1', '图片-全量同步', '/sys/resource/image/api/complete/syncIndex', 'undo', '1341620898007281665', '1341620898007281665', '2022-08-29 19:31:25', '2023-01-17 15:52:55', '0', '10', '0');
INSERT INTO `boot_sys_menu` (`id`, `pid`, `permission`, `type`, `name`, `url`, `icon`, `creator`, `editor`, `create_date`, `update_date`, `del_flag`, `sort`, `version`) VALUES ('1564214393074880513', '1560530192751071234', 'sys:resource:video:complete:syncIndex', '1', '视频-全量同步', '/sys/resource/video/api/complete/syncIndex', 'undo', '1341620898007281665', '1341620898007281665', '2022-08-29 19:32:28', '2023-01-17 15:54:19', '0', '10', '0');
INSERT INTO `boot_sys_menu` (`id`, `pid`, `permission`, `type`, `name`, `url`, `icon`, `creator`, `editor`, `create_date`, `update_date`, `del_flag`, `sort`, `version`) VALUES ('1564227439767920641', '1535878154046939137', 'sys:search:view', '0', '搜索管理', '/sys/search/view', 'ie', '1341620898007281665', '1341620898007281665', '2022-08-29 20:26:22', '2022-08-29 20:26:22', '0', '700', '0');
INSERT INTO `boot_sys_menu` (`id`, `pid`, `permission`, `type`, `name`, `url`, `icon`, `creator`, `editor`, `create_date`, `update_date`, `del_flag`, `sort`, `version`) VALUES ('1564228322215927810', '1564227439767920641', 'sys:search:resource:view', '0', '资源搜索', '/sys/search/resource/view', 'loading', '1341620898007281665', NULL, '2022-08-29 20:27:49', '2022-08-29 20:27:49', '0', '100', '0');
INSERT INTO `boot_sys_menu` (`id`, `pid`, `permission`, `type`, `name`, `url`, `icon`, `creator`, `editor`, `create_date`, `update_date`, `del_flag`, `sort`, `version`) VALUES ('1564996817056710657', '1564228322215927810', 'sys:search:resource:query', '1', '搜索资源', '/sys/search/api/resource', 'search', '1341620898007281665', NULL, '2022-08-31 23:21:32', '2022-08-31 23:21:32', '0', '10', '0');
INSERT INTO `boot_sys_menu` (`id`, `pid`, `permission`, `type`, `name`, `url`, `icon`, `creator`, `editor`, `create_date`, `update_date`, `del_flag`, `sort`, `version`) VALUES ('1564996817056710661', '1545037580289044482', 'workflow:task:resource:detail', '1', '任务详情', '/workflow/task/api/resource/detail', 'eye', '1341620898007281665', '1341620898007281665', '2022-12-10 12:44:49', '2022-12-10 12:45:27', '0', '10', '0');
INSERT INTO `boot_sys_menu` (`id`, `pid`, `permission`, `type`, `name`, `url`, `icon`, `creator`, `editor`, `create_date`, `update_date`, `del_flag`, `sort`, `version`) VALUES ('1564996817056710662', '1560530418819862529', 'sys:resource:image:increment:syncIndex', '1', '图片-增量同步', '/sys/resource/image/api/increment/syncIndex', 'redo', '1341620898007281665', '1341620898007281665', '2022-12-10 16:28:20', '2023-01-17 16:04:16', '0', '10', '0');
INSERT INTO `boot_sys_menu` (`id`, `pid`, `permission`, `type`, `name`, `url`, `icon`, `creator`, `editor`, `create_date`, `update_date`, `del_flag`, `sort`, `version`) VALUES ('1564996817056710664', '1560529603300364290', 'sys:resource:audio:increment:syncIndex', '1', '音频-增量同步', '/sys/resource/audio/api/increment/syncIndex', 'redo', '1341620898007281665', '1341620898007281665', '2022-12-10 16:33:50', '2023-01-17 16:03:32', '0', '10', '0');
INSERT INTO `boot_sys_menu` (`id`, `pid`, `permission`, `type`, `name`, `url`, `icon`, `creator`, `editor`, `create_date`, `update_date`, `del_flag`, `sort`, `version`) VALUES ('1564996817056710665', '1539251440843857922', 'sys:log:operate:export', '1', '操作-导出日志', '/sys/log/api/operate/export', 'export', '1341620898007281665', '1341620898007281665', '2022-12-11 11:32:53', '2022-12-11 11:50:57', '0', '10', '0');
INSERT INTO `boot_sys_menu` (`id`, `pid`, `permission`, `type`, `name`, `url`, `icon`, `creator`, `editor`, `create_date`, `update_date`, `del_flag`, `sort`, `version`) VALUES ('1564996817056710666', '1539396453854629890', 'sys:log:login:export', '1', '登录-导出日志', '/sys/log/api/login/export', 'export', '1341620898007281665', '1341620898007281665', '2022-12-11 11:50:45', '2022-12-11 11:52:03', '0', '10', '0');
INSERT INTO `boot_sys_menu` (`id`, `pid`, `permission`, `type`, `name`, `url`, `icon`, `creator`, `editor`, `create_date`, `update_date`, `del_flag`, `sort`, `version`) VALUES ('1564996817056710668', '1560530192751071234', 'sys:resource:video:increment:syncIndex', '1', '视频-增量同步', '/sys/resource/video/api/increment/syncIndex', 'redo', '1341620898007281665', '1341620898007281665', '2022-12-12 21:05:31', '2023-01-17 16:03:49', '0', '10', '0');
-- ------------------------------------菜单------------------------------------

-- ------------------------------------中间表------------------------------------
CREATE TABLE `boot_sys_role_dept` (
                                      `role_id` bigint(20) NOT NULL COMMENT '角色id',
                                      `dept_id` bigint(20) NOT NULL COMMENT '部门id',
                                      PRIMARY KEY (`role_id`,`dept_id`),
                                      KEY `role_id` (`role_id`) USING BTREE,
                                      KEY `menu_id` (`dept_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='角色-部门';
CREATE TABLE `boot_sys_role_menu` (
                                      `role_id` bigint(20) NOT NULL COMMENT '角色id',
                                      `menu_id` bigint(20) NOT NULL COMMENT '菜单id',
                                      PRIMARY KEY (`role_id`,`menu_id`),
                                      KEY `role_id` (`role_id`) USING BTREE,
                                      KEY `menu_id` (`menu_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='角色-菜单';
CREATE TABLE `boot_sys_user_role` (
                                      `user_id` bigint(20) NOT NULL COMMENT '用户id',
                                      `role_id` bigint(20) NOT NULL COMMENT '角色id',
                                      PRIMARY KEY (`user_id`,`role_id`),
                                      KEY `user_id` (`user_id`) USING BTREE,
                                      KEY `role_id` (`role_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='用户-角色';
-- ------------------------------------中间表------------------------------------

-- ------------------------------------日志------------------------------------
CREATE TABLE `boot_sys_audit_log` (
                                      `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'id',
                                      `business_id` bigint(20) DEFAULT NULL COMMENT '资源id',
                                      `audit_name` varchar(50) DEFAULT NULL COMMENT '审批人',
                                      `audit_date` datetime DEFAULT NULL COMMENT '审批时间',
                                      `create_date` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                                      `update_date` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
                                      `creator` bigint(20) DEFAULT NULL COMMENT '创建人',
                                      `editor` bigint(20) DEFAULT NULL COMMENT '编辑人',
                                      `del_flag` tinyint(1) NOT NULL DEFAULT '0' COMMENT '1已删除 0未删除',
                                      `audit_status` tinyint(1) DEFAULT NULL COMMENT '0审批驳回 1审批通过',
                                      `comment` varchar(200) DEFAULT NULL COMMENT '审批意见',
                                      `type` tinyint(1) DEFAULT '0' COMMENT '0 资源',
                                      `version` int(11) NOT NULL DEFAULT '0' COMMENT '版本号',
                                      PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='审批日志';
CREATE TABLE `boot_sys_login_log` (
                                      `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'id',
                                      `creator` bigint(20) DEFAULT NULL COMMENT '创建者',
                                      `create_date` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                                      `update_date` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '修改时间',
                                      `del_flag` tinyint(1) NOT NULL DEFAULT '0' COMMENT '1已删除 0未删除',
                                      `editor` bigint(20) DEFAULT NULL COMMENT '编辑人',
                                      `login_name` varchar(200) DEFAULT NULL COMMENT '登录用户',
                                      `request_ip` varchar(50) DEFAULT NULL COMMENT 'IP地址',
                                      `request_address` varchar(200) DEFAULT NULL COMMENT '归属地',
                                      `browser` varchar(50) DEFAULT NULL COMMENT '浏览器版本',
                                      `os` varchar(50) DEFAULT NULL COMMENT '操作系统',
                                      `request_status` tinyint(1) unsigned NOT NULL COMMENT '状态  0：成功   1：失败',
                                      `msg` varchar(500) DEFAULT NULL COMMENT '提示信息',
                                      `login_type` varchar(50) DEFAULT NULL COMMENT '登录类型',
                                      `version` int(11) NOT NULL DEFAULT '0' COMMENT '版本号',
                                      PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='登录日志';
CREATE TABLE `boot_sys_operate_log` (
                                        `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'id',
                                        `module` varchar(32) DEFAULT NULL COMMENT '模块名称，如：系统菜单',
                                        `operation` varchar(50) DEFAULT NULL COMMENT '操作名称',
                                        `request_uri` varchar(200) DEFAULT NULL COMMENT '请求URI',
                                        `method_name` varchar(1000) DEFAULT NULL COMMENT '方法名称',
                                        `request_method` varchar(20) DEFAULT NULL COMMENT '请求方式',
                                        `request_params` text COMMENT '请求参数',
                                        `user_agent` varchar(500) DEFAULT NULL COMMENT '浏览器版本',
                                        `request_ip` varchar(50) DEFAULT NULL COMMENT 'IP地址',
                                        `request_address` varchar(200) DEFAULT NULL COMMENT '归属地',
                                        `request_status` tinyint(1) unsigned NOT NULL COMMENT '状态  0：成功   1：失败',
                                        `operator` varchar(50) DEFAULT NULL COMMENT '操作人',
                                        `error_msg` text COMMENT '错误信息',
                                        `creator` bigint(20) DEFAULT NULL COMMENT '创建者',
                                        `create_date` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                                        `update_date` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '修改时间',
                                        `del_flag` tinyint(1) NOT NULL DEFAULT '0' COMMENT '1已删除 0未删除',
                                        `editor` bigint(20) DEFAULT NULL COMMENT '编辑人',
                                        `dept_id` bigint(20) DEFAULT NULL COMMENT '部门id',
                                        `version` int(11) NOT NULL DEFAULT '0' COMMENT '版本号',
                                        PRIMARY KEY (`id`),
                                        KEY `idx_module` (`module`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='操作日志';
-- ------------------------------------日志------------------------------------

-- ------------------------------------字典------------------------------------
CREATE TABLE `boot_sys_dict` (
                                 `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'id',
                                 `creator` bigint(20) DEFAULT NULL COMMENT '创建者',
                                 `create_date` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                                 `update_date` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '修改时间',
                                 `del_flag` tinyint(1) NOT NULL DEFAULT '0' COMMENT '1已删除 0未删除',
                                 `editor` bigint(20) DEFAULT NULL COMMENT '编辑人',
                                 `dict_value` text COMMENT '值',
                                 `dict_label` varchar(100) DEFAULT NULL COMMENT '标签',
                                 `type` varchar(100) DEFAULT NULL COMMENT '类型',
                                 `remark` varchar(500) DEFAULT NULL COMMENT '备注',
                                 `sort` int(11) DEFAULT '1' COMMENT '排序',
                                 `dept_id` bigint(20) DEFAULT NULL COMMENT '部门id',
                                 `version` int(11) NOT NULL DEFAULT '0' COMMENT '版本号',
                                 PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='字典';
-- ------------------------------------字典------------------------------------

-- ------------------------------------部门------------------------------------
CREATE TABLE `boot_sys_dept` (
                                 `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'id',
                                 `pid` bigint(20) NOT NULL COMMENT '父节点',
                                 `name` varchar(100) DEFAULT NULL COMMENT '名称',
                                 `sort` int(11) DEFAULT '1' COMMENT '排序',
                                 `creator` bigint(20) DEFAULT NULL COMMENT '创建人',
                                 `editor` bigint(20) DEFAULT NULL COMMENT '编辑人',
                                 `create_date` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                                 `update_date` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '修改时间',
                                 `del_flag` tinyint(1) DEFAULT '0' COMMENT '1已删除 0未删除',
                                 `path` varchar(2000) DEFAULT NULL COMMENT '路径',
                                 `version` int(11) NOT NULL DEFAULT '0' COMMENT '版本号',
                                 PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='部门';
INSERT INTO `boot_sys_dept` (`id`, `pid`, `name`, `sort`, `creator`, `editor`, `create_date`, `update_date`, `del_flag`, `path`, `version`) VALUES ('1535858679453085698', '1535887940687765505', '广州分公司', '666', '1341620898007281665', '1341620898007281665', '2022-11-02 22:35:30', '2022-11-02 22:35:30', '0', '0/1535858679453085698', '0');
INSERT INTO `boot_sys_dept` (`id`, `pid`, `name`, `sort`, `creator`, `editor`, `create_date`, `update_date`, `del_flag`, `path`, `version`) VALUES ('1535881356595175426', '1535887940687765505', '长沙分公司', '111', '1341620898007281665', '1341620898007281665', '2022-11-02 22:35:30', '2022-11-02 22:35:30', '0', '0/1535881356595175426', '0');
INSERT INTO `boot_sys_dept` (`id`, `pid`, `name`, `sort`, `creator`, `editor`, `create_date`, `update_date`, `del_flag`, `path`, `version`) VALUES ('1535887129341599746', '1535887940687765505', '深圳分公司', '888', '1341620898007281665', '1341620898007281665', '2022-11-02 22:35:30', '2023-01-30 22:01:13', '0', '0/1535887940687765505/1535887129341599746', '1');
INSERT INTO `boot_sys_dept` (`id`, `pid`, `name`, `sort`, `creator`, `editor`, `create_date`, `update_date`, `del_flag`, `path`, `version`) VALUES ('1535887940687765505', '0', '老寇云集团', '1000', '1341620898007281665', '1341620898007281665', '2022-11-16 12:12:55', '2022-11-16 12:12:56', '0', '0/1535887940687765505', '0');
INSERT INTO `boot_sys_dept` (`id`, `pid`, `name`, `sort`, `creator`, `editor`, `create_date`, `update_date`, `del_flag`, `path`, `version`) VALUES ('1584488175088373761', '1535881356595175426', '研发部', '20', '1341620898007281665', '1341620898007281665', '2022-11-02 22:35:30', '2022-11-02 22:35:30', '0', '0/1584488175088373761', '0');
INSERT INTO `boot_sys_dept` (`id`, `pid`, `name`, `sort`, `creator`, `editor`, `create_date`, `update_date`, `del_flag`, `path`, `version`) VALUES ('1584488411756171265', '1535881356595175426', '市场部', '10', '1341620898007281665', '1341620898007281665', '2022-11-02 22:35:30', '2022-11-02 22:35:30', '0', '0/1584488411756171265', '0');
INSERT INTO `boot_sys_dept` (`id`, `pid`, `name`, `sort`, `creator`, `editor`, `create_date`, `update_date`, `del_flag`, `path`, `version`) VALUES ('1584488411756171266', '1584488175088373761', '开发组', '5', '1341620898007281665', '1341620898007281665', '2022-11-02 22:35:30', '2022-12-28 17:20:25', '0', '0/1584488175088373761/1584488411756171266', '0');
-- ------------------------------------部门------------------------------------

-- ------------------------------------消息------------------------------------
CREATE TABLE `boot_sys_message` (
                                    `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'id',
                                    `title` varchar(400) DEFAULT NULL COMMENT '标题',
                                    `content` longtext COMMENT '内容',
                                    `send_channel` tinyint(10) DEFAULT NULL COMMENT '发送渠道：0平台 1微信公众号 2邮箱',
                                    `creator` bigint(20) DEFAULT NULL COMMENT '创建人',
                                    `editor` bigint(20) DEFAULT NULL COMMENT '编辑人',
                                    `create_date` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                                    `update_date` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '修改时间',
                                    `del_flag` tinyint(1) NOT NULL DEFAULT '0' COMMENT '1已删除 0未删除',
                                    `dept_id` bigint(20) DEFAULT NULL COMMENT '部门id',
                                    `type` tinyint(2) DEFAULT '0' COMMENT '0通知 1提醒',
                                    `version` int(11) NOT NULL DEFAULT '0' COMMENT '版本号',
                                    PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='消息';
CREATE TABLE `boot_sys_message_detail` (
                                           `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'id',
                                           `message_id` bigint(20) DEFAULT NULL COMMENT '消息id',
                                           `user_id` bigint(20) DEFAULT NULL COMMENT '用户id',
                                           `creator` bigint(20) DEFAULT NULL COMMENT '创建人',
                                           `editor` bigint(20) DEFAULT NULL COMMENT '编辑人',
                                           `create_date` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                                           `update_date` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
                                           `del_flag` tinyint(1) NOT NULL DEFAULT '0' COMMENT '1已删除 0未删除',
                                           `read_flag` tinyint(1) DEFAULT '0' COMMENT '是否已读 0未读 1已读',
                                           `version` int(11) NOT NULL DEFAULT '0' COMMENT '版本号',
                                           PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='消息详情';
-- ------------------------------------消息------------------------------------

-- ------------------------------------对象存储------------------------------------
CREATE TABLE `boot_sys_oss` (
                                `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'id',
                                `creator` bigint(20) DEFAULT NULL COMMENT '创建人',
                                `editor` bigint(20) DEFAULT NULL COMMENT '编辑人',
                                `create_date` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                                `update_date` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '修改时间',
                                `del_flag` tinyint(1) DEFAULT '0' COMMENT '1已删除 0未删除',
                                `name` varchar(20) DEFAULT NULL COMMENT '名称',
                                `endpoint` varchar(200) DEFAULT NULL COMMENT '终端',
                                `region` varchar(10) DEFAULT NULL COMMENT '区域',
                                `access_key` varchar(50) DEFAULT NULL COMMENT '访问密钥',
                                `secret_key` varchar(100) DEFAULT NULL COMMENT '用户密钥',
                                `bucket_name` varchar(20) DEFAULT NULL COMMENT '桶名',
                                `path_style_access_enabled` tinyint(1) DEFAULT NULL COMMENT '路径样式访问 1启动 0不启用',
                                `status` tinyint(1) DEFAULT NULL COMMENT '状态 1已启用 0未启用',
                                `version` int(11) NOT NULL DEFAULT '0' COMMENT '版本号',
                                PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='对象存储';
INSERT INTO `boot_sys_oss` (`id`, `creator`, `editor`, `create_date`, `update_date`, `del_flag`, `name`, `endpoint`, `region`, `access_key`, `secret_key`, `bucket_name`, `path_style_access_enabled`, `status`, `version`) VALUES ('1', '1341620898007281665', '1341620898007281665', '2023-01-05 16:36:37', '2023-01-05 16:36:37', '0', 'Minio OSS', 'http://localhost:9000', 'Shenzhen', 'minioadmin', 'minioadmin', 'laokou-minio-bucket', '1', '0', '0');
CREATE TABLE `boot_sys_oss_log` (
                                    `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'id',
                                    `creator` bigint(20) DEFAULT NULL COMMENT '创建人',
                                    `editor` bigint(20) DEFAULT NULL COMMENT '编辑人',
                                    `create_date` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                                    `update_date` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '修改时间',
                                    `del_flag` tinyint(1) DEFAULT '0' COMMENT '1已删除 0未删除',
                                    `md5` varchar(100) DEFAULT NULL COMMENT 'md5标识',
                                    `url` varchar(200) DEFAULT NULL COMMENT '路径',
                                    `file_name` varchar(100) DEFAULT NULL COMMENT '文件名称',
                                    `file_size` bigint(20) DEFAULT NULL COMMENT '文件大小',
                                    `version` int(11) NOT NULL DEFAULT '0' COMMENT '版本号',
                                    PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='存储日志';
-- ------------------------------------对象存储------------------------------------

-- ------------------------------------资源------------------------------------

-- ------------------------------------资源------------------------------------

-- ------------------------------------角色------------------------------------
CREATE TABLE `boot_sys_role` (
                                 `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'id',
                                 `name` varchar(50) NOT NULL COMMENT '角色名称',
                                 `creator` bigint(20) NOT NULL COMMENT '创建人',
                                 `editor` bigint(20) DEFAULT NULL COMMENT '编辑人',
                                 `create_date` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                                 `update_date` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '修改时间',
                                 `del_flag` tinyint(1) NOT NULL DEFAULT '0' COMMENT '1已删除 0未删除',
                                 `sort` int(11) DEFAULT '1' COMMENT '排序',
                                 `dept_id` bigint(20) DEFAULT NULL COMMENT '部门id',
                                 `version` int(11) NOT NULL DEFAULT '0' COMMENT '版本号',
                                 PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='角色';
INSERT INTO `boot_sys_role` (`id`, `name`, `creator`, `editor`, `create_date`, `update_date`, `del_flag`, `sort`, `dept_id`, `version`) VALUES ('139167754288778856', '游客', '1341620898007281665', '1341620898007281665', '2021-11-27 17:11:15', '2022-10-09 14:18:30', '0', '10', '1535887940687765505', '0');
INSERT INTO `boot_sys_role` (`id`, `name`, `creator`, `editor`, `create_date`, `update_date`, `del_flag`, `sort`, `dept_id`, `version`) VALUES ('139167754288778857', '管理员', '1341620898007281665', '1341620898007281665', '2021-11-27 17:11:19', '2023-01-19 17:58:11', '0', '100', '1535887940687765505', '0');
INSERT INTO `boot_sys_role` (`id`, `name`, `creator`, `editor`, `create_date`, `update_date`, `del_flag`, `sort`, `dept_id`, `version`) VALUES ('1535949666183573505', '测试', '1341620898007281665', '1341620898007281665', '2022-06-12 19:38:32', '2022-11-23 17:05:39', '0', '50', '1535887940687765505', '0');
INSERT INTO `boot_sys_role` (`id`, `name`, `creator`, `editor`, `create_date`, `update_date`, `del_flag`, `sort`, `dept_id`, `version`) VALUES ('1578676133601415170', '备胎', '1341623527018004481', '1341620898007281665', '2022-10-08 17:18:15', '2022-12-11 12:07:37', '0', '1', '1535881356595175426', '0');
-- ------------------------------------角色------------------------------------

-- ------------------------------------用户------------------------------------
CREATE TABLE `boot_sys_user` (
                                 `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'id',
                                 `username` varchar(20) NOT NULL COMMENT '用户名',
                                 `password` varchar(100) NOT NULL COMMENT '密码',
                                 `super_admin` tinyint(1) NOT NULL DEFAULT '0' COMMENT '超级管理员：0否 1是',
                                 `creator` bigint(20) DEFAULT NULL COMMENT '创建人',
                                 `editor` bigint(20) DEFAULT NULL COMMENT '编辑人',
                                 `create_date` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                                 `update_date` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '修改时间',
                                 `del_flag` tinyint(1) NOT NULL COMMENT '1已删除 0未删除',
                                 `email` varchar(50) DEFAULT NULL COMMENT '电子邮箱',
                                 `status` tinyint(1) NOT NULL DEFAULT '1' COMMENT '状态 0正常 1停用',
                                 `img_url` varchar(400) NOT NULL DEFAULT 'https://gimg2.baidu.com/image_search/src=http%3A%2F%2Fi.qqkou.com%2Fi%2F1a3626475345x3078425090b26.jpg&refer=http%3A%2F%2Fi.qqkou.com&app=2002&size=f9999,10000&q=a80&n=0&g=0n&fmt=auto?sec=1657902941&t=73f98a243f12f3eabe1dce87d2b6401b' COMMENT '头像url',
                                 `mobile` varchar(11) DEFAULT NULL COMMENT '手机号',
                                 `dept_id` bigint(20) DEFAULT NULL COMMENT '部门id',
                                 `version` int(11) NOT NULL DEFAULT '0' COMMENT '版本号',
                                 `tenant_id` bigint(20) NOT NULL DEFAULT '0' COMMENT '租户id',
                                 PRIMARY KEY (`id`),
                                 UNIQUE KEY `idx_username` (`username`) USING BTREE,
                                 UNIQUE KEY `idx_email` (`email`) USING BTREE,
                                 UNIQUE KEY `idx_mobile` (`mobile`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='用户';
INSERT INTO `boot_sys_user` (`id`, `username`, `password`, `super_admin`, `creator`, `editor`, `create_date`, `update_date`, `del_flag`, `email`, `status`, `img_url`, `mobile`, `dept_id`, `version`, `tenant_id`) VALUES ('1341620898007281665', 'admin', '$2a$10$xuvqs7rNKn9KeRwdRdgq3.v.p/AFhFDDo97H9dSEaTpLM5DLtLGZS', '1', '1341620898007281665', '1341620898007281665', '2021-11-29 20:13:11', '2022-10-26 19:24:55', '0', '2413176044@qq.com', '0', 'http://175.178.69.253:81/upload/node3/7904fff1c08a4883b40f1ee0336017dc.webp', '18974432576', '1535887940687765505', '0', '0');
INSERT INTO `boot_sys_user` (`id`, `username`, `password`, `super_admin`, `creator`, `editor`, `create_date`, `update_date`, `del_flag`, `email`, `status`, `img_url`, `mobile`, `dept_id`, `version`, `tenant_id`) VALUES ('1341623527018004481', 'test', '$2a$10$cdEXE9uaSqkqoKa.73.XBuhHFPiOLomStfzUlmkuVqfA2wldqEfIG', '0', '1341620898007281665', '1341620898007281665', '2020-12-23 13:55:50', '2022-11-01 13:09:55', '0', '2314176044@qq.com', '0', 'http://175.178.69.253:81/upload/node2/b4e5bb3944a046a6bb54f8bfd2c830c1.webp', '18888888888', '1535881356595175426', '0', '0');
INSERT INTO `boot_sys_user` (`id`, `username`, `password`, `super_admin`, `creator`, `editor`, `create_date`, `update_date`, `del_flag`, `email`, `status`, `img_url`, `mobile`, `dept_id`, `version`, `tenant_id`) VALUES ('1421312053736804354', 'koush5', '$2a$10$ysAmruc249SiAUpIqQzrpeM8wcdpgIJ6nEdtsXQnDrBgvLZkt7tJ6', '0', '1341620898007281665', '1341620898007281665', '2021-07-31 11:29:35', '2022-11-04 00:13:05', '0', 'YY@qq.com', '0', 'https://ruoyi.setworld.net/img/profile.473f5971.jpg', 'xxxxx', '1535881356595175426', '0', '0');
INSERT INTO `boot_sys_user` (`id`, `username`, `password`, `super_admin`, `creator`, `editor`, `create_date`, `update_date`, `del_flag`, `email`, `status`, `img_url`, `mobile`, `dept_id`, `version`, `tenant_id`) VALUES ('1537111101311844353', 'wumh5', '$2a$10$KZHkKBF2jUGaOJDp4YcgeuVt1gBhteJC990OQWuOoRmyYQILBzQlG', '0', '1341620898007281665', '1341620898007281665', '2022-06-16 00:33:39', '2022-10-24 18:40:20', '0', NULL, '1', 'https://gimg2.baidu.com/image_search/src=http%3A%2F%2Fi.qqkou.com%2Fi%2F1a3626475345x3078425090b26.jpg&amp;refer=http%3A%2F%2Fi.qqkou.com&amp;app=2002&amp;size=f9999,10000&amp;q=a80&amp;n=0&amp;g=0n&amp;fmt=auto?sec=1657902941&amp;t=73f98a243f12f3eabe1dce87d2b6401b', NULL, '1535858679453085698', '0', '0');
INSERT INTO `boot_sys_user` (`id`, `username`, `password`, `super_admin`, `creator`, `editor`, `create_date`, `update_date`, `del_flag`, `email`, `status`, `img_url`, `mobile`, `dept_id`, `version`, `tenant_id`) VALUES ('1537114827246292994', 'laok5', '$2a$10$b.40TGb7W19z5Jryo3FBuOEDaX2c0OAqZHnRnCkXCPI67ru5G7Nha', '0', '1341620898007281665', '1341620898007281665', '2022-06-16 00:48:28', '2022-10-26 19:40:13', '0', NULL, '0', 'https://gimg2.baidu.com/image_search/src=http%3A%2F%2Fi.qqkou.com%2Fi%2F1a3626475345x3078425090b26.jpg&amp;refer=http%3A%2F%2Fi.qqkou.com&amp;app=2002&amp;size=f9999,10000&amp;q=a80&amp;n=0&amp;g=0n&amp;fmt=auto?sec=1657902941&amp;t=73f98a243f12f3eabe1dce87d2b6401b', NULL, '1535881356595175426', '0', '0');
-- ------------------------------------用户------------------------------------

-- ------------------------------------低代码------------------------------------
CREATE TABLE `gen_field_type` (
                                  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'id',
                                  `column_type` varchar(200) DEFAULT NULL COMMENT '字段类型',
                                  `attr_type` varchar(200) DEFAULT NULL COMMENT '属性类型',
                                  `package_name` varchar(200) DEFAULT NULL COMMENT '属性包名',
                                  `creator` bigint(20) DEFAULT NULL COMMENT '创建人',
                                  `editor` bigint(20) DEFAULT NULL COMMENT '编辑人',
                                  `create_date` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                                  `update_date` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '修改时间',
                                  `del_flag` tinyint(1) NOT NULL COMMENT '1已删除 0未删除',
                                  PRIMARY KEY (`id`),
                                  UNIQUE KEY `column_type` (`column_type`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='字段类型管理';
INSERT INTO `gen_field_type` (`id`, `column_type`, `attr_type`, `package_name`, `creator`, `editor`, `create_date`, `update_date`, `del_flag`) VALUES ('1391677542887788567', 'datetime', 'Date', 'java.util.Date', '1341620898007281665', '1341620898007281665', '2022-07-25 10:16:54', '2022-07-25 10:21:45', '0');
INSERT INTO `gen_field_type` (`id`, `column_type`, `attr_type`, `package_name`, `creator`, `editor`, `create_date`, `update_date`, `del_flag`) VALUES ('1535858679453085698', 'date', 'Date', 'java.util.Date', '1341620898007281665', '1341620898007281665', '2022-07-25 10:16:54', '2022-07-25 10:21:45', '0');
INSERT INTO `gen_field_type` (`id`, `column_type`, `attr_type`, `package_name`, `creator`, `editor`, `create_date`, `update_date`, `del_flag`) VALUES ('1535859148908949506', 'tinyint', 'Integer', NULL, '1341620898007281665', '1341620898007281665', '2022-07-25 10:16:54', '2022-07-25 10:21:45', '0');
INSERT INTO `gen_field_type` (`id`, `column_type`, `attr_type`, `package_name`, `creator`, `editor`, `create_date`, `update_date`, `del_flag`) VALUES ('1535859326311231489', 'smallint', 'Integer', NULL, '1341620898007281665', '1341620898007281665', '2022-07-25 10:16:54', '2022-07-25 10:21:46', '0');
INSERT INTO `gen_field_type` (`id`, `column_type`, `attr_type`, `package_name`, `creator`, `editor`, `create_date`, `update_date`, `del_flag`) VALUES ('1535859588534923265', 'mediumint', 'Integer', NULL, '1341620898007281665', '1341620898007281665', '2022-07-25 10:16:54', '2022-07-25 10:21:47', '0');
INSERT INTO `gen_field_type` (`id`, `column_type`, `attr_type`, `package_name`, `creator`, `editor`, `create_date`, `update_date`, `del_flag`) VALUES ('1535878154046939137', 'int', 'Integer', NULL, '1341620898007281665', '1341620898007281665', '2022-07-25 10:16:54', '2022-07-25 10:21:47', '0');
INSERT INTO `gen_field_type` (`id`, `column_type`, `attr_type`, `package_name`, `creator`, `editor`, `create_date`, `update_date`, `del_flag`) VALUES ('1535881096963563522', 'integer', 'Integer', NULL, '1341620898007281665', '1341620898007281665', '2022-07-25 10:16:54', '2022-07-25 10:21:47', '0');
INSERT INTO `gen_field_type` (`id`, `column_type`, `attr_type`, `package_name`, `creator`, `editor`, `create_date`, `update_date`, `del_flag`) VALUES ('1535881356595175426', 'bigint', 'Long', NULL, '1341620898007281665', '1341620898007281665', '2022-07-25 10:16:54', '2022-07-25 10:21:47', '0');
INSERT INTO `gen_field_type` (`id`, `column_type`, `attr_type`, `package_name`, `creator`, `editor`, `create_date`, `update_date`, `del_flag`) VALUES ('1535886982654205953', 'float', 'Float', NULL, '1341620898007281665', '1341620898007281665', '2022-07-25 10:16:54', '2022-07-25 10:21:47', '0');
INSERT INTO `gen_field_type` (`id`, `column_type`, `attr_type`, `package_name`, `creator`, `editor`, `create_date`, `update_date`, `del_flag`) VALUES ('1535887129341599746', 'double', 'Double', NULL, '1341620898007281665', '1341620898007281665', '2022-07-25 10:16:54', '2022-07-25 10:21:47', '0');
INSERT INTO `gen_field_type` (`id`, `column_type`, `attr_type`, `package_name`, `creator`, `editor`, `create_date`, `update_date`, `del_flag`) VALUES ('1535887276649750530', 'decimal', 'BigDecimal', 'java.math.BigDecimal', '1341620898007281665', '1341620898007281665', '2022-07-25 10:16:54', '2022-07-25 10:21:47', '0');
INSERT INTO `gen_field_type` (`id`, `column_type`, `attr_type`, `package_name`, `creator`, `editor`, `create_date`, `update_date`, `del_flag`) VALUES ('1535887450809835521', 'bit', 'Boolean', NULL, '1341620898007281665', '1341620898007281665', '2022-07-25 10:16:54', '2022-07-25 10:21:47', '0');
INSERT INTO `gen_field_type` (`id`, `column_type`, `attr_type`, `package_name`, `creator`, `editor`, `create_date`, `update_date`, `del_flag`) VALUES ('1535887779873955841', 'char', 'String', NULL, '1341620898007281665', '1341620898007281665', '2022-07-25 10:16:54', '2022-07-25 10:21:47', '0');
INSERT INTO `gen_field_type` (`id`, `column_type`, `attr_type`, `package_name`, `creator`, `editor`, `create_date`, `update_date`, `del_flag`) VALUES ('1535887940687765505', 'varchar', 'String', NULL, '1341620898007281665', '1341620898007281665', '2022-07-25 10:16:54', '2022-07-25 10:21:47', '0');
INSERT INTO `gen_field_type` (`id`, `column_type`, `attr_type`, `package_name`, `creator`, `editor`, `create_date`, `update_date`, `del_flag`) VALUES ('1535888146045083649', 'tinytext', 'String', NULL, '1341620898007281665', '1341620898007281665', '2022-07-25 10:16:54', '2022-07-25 10:21:47', '0');
INSERT INTO `gen_field_type` (`id`, `column_type`, `attr_type`, `package_name`, `creator`, `editor`, `create_date`, `update_date`, `del_flag`) VALUES ('1535888341252186114', 'text', 'String', NULL, '1341620898007281665', '1341620898007281665', '2022-07-25 10:16:54', '2022-07-25 10:21:47', '0');
INSERT INTO `gen_field_type` (`id`, `column_type`, `attr_type`, `package_name`, `creator`, `editor`, `create_date`, `update_date`, `del_flag`) VALUES ('1537444981390794754', 'mediumtext', 'String', NULL, '1341620898007281665', '1341620898007281665', '2022-07-25 10:16:54', '2022-07-25 10:21:48', '0');
INSERT INTO `gen_field_type` (`id`, `column_type`, `attr_type`, `package_name`, `creator`, `editor`, `create_date`, `update_date`, `del_flag`) VALUES ('1537447512825225218', 'longtext', 'String', NULL, '1341620898007281665', '1341620898007281665', '2022-07-25 10:16:54', '2022-07-25 10:21:48', '0');
INSERT INTO `gen_field_type` (`id`, `column_type`, `attr_type`, `package_name`, `creator`, `editor`, `create_date`, `update_date`, `del_flag`) VALUES ('1538469199368712193', 'timestamp', 'Date', 'java.util.Date', '1341620898007281665', '1341620898007281665', '2022-07-25 10:16:54', '2022-07-25 10:21:48', '0');
INSERT INTO `gen_field_type` (`id`, `column_type`, `attr_type`, `package_name`, `creator`, `editor`, `create_date`, `update_date`, `del_flag`) VALUES ('1539250224424394753', 'NUMBER', 'Integer', NULL, '1341620898007281665', '1341620898007281665', '2022-07-25 10:16:54', '2022-07-25 10:21:48', '0');
INSERT INTO `gen_field_type` (`id`, `column_type`, `attr_type`, `package_name`, `creator`, `editor`, `create_date`, `update_date`, `del_flag`) VALUES ('1539251440843857922', 'BINARY_INTEGER', 'Integer', NULL, '1341620898007281665', '1341620898007281665', '2022-07-25 10:16:54', '2022-07-25 10:21:48', '0');
INSERT INTO `gen_field_type` (`id`, `column_type`, `attr_type`, `package_name`, `creator`, `editor`, `create_date`, `update_date`, `del_flag`) VALUES ('1539265093588545538', 'BINARY_FLOAT', 'Float', NULL, '1341620898007281665', '1341620898007281665', '2022-07-25 10:16:54', '2022-07-25 10:21:48', '0');
INSERT INTO `gen_field_type` (`id`, `column_type`, `attr_type`, `package_name`, `creator`, `editor`, `create_date`, `update_date`, `del_flag`) VALUES ('1539396453854629890', 'BINARY_DOUBLE', 'Double', NULL, '1341620898007281665', '1341620898007281665', '2022-07-25 10:16:54', '2022-07-25 10:21:48', '0');
INSERT INTO `gen_field_type` (`id`, `column_type`, `attr_type`, `package_name`, `creator`, `editor`, `create_date`, `update_date`, `del_flag`) VALUES ('1539402478934646785', 'VARCHAR2', 'String', NULL, '1341620898007281665', '1341620898007281665', '2022-07-25 10:16:54', '2022-07-25 10:21:48', '0');
INSERT INTO `gen_field_type` (`id`, `column_type`, `attr_type`, `package_name`, `creator`, `editor`, `create_date`, `update_date`, `del_flag`) VALUES ('1539615028590673921', 'NVARCHAR', 'String', NULL, '1341620898007281665', '1341620898007281665', '2022-07-25 10:16:54', '2022-07-25 10:21:48', '0');
INSERT INTO `gen_field_type` (`id`, `column_type`, `attr_type`, `package_name`, `creator`, `editor`, `create_date`, `update_date`, `del_flag`) VALUES ('1539989085181972481', 'NVARCHAR2', 'String', NULL, '1341620898007281665', '1341620898007281665', '2022-07-25 10:16:54', '2022-07-25 10:21:48', '0');
INSERT INTO `gen_field_type` (`id`, `column_type`, `attr_type`, `package_name`, `creator`, `editor`, `create_date`, `update_date`, `del_flag`) VALUES ('1540000886082768897', 'CLOB', 'String', NULL, '1341620898007281665', '1341620898007281665', '2022-07-25 10:16:54', '2022-07-25 10:21:48', '0');
INSERT INTO `gen_field_type` (`id`, `column_type`, `attr_type`, `package_name`, `creator`, `editor`, `create_date`, `update_date`, `del_flag`) VALUES ('1540001496240754689', 'int8', 'Long', NULL, '1341620898007281665', '1341620898007281665', '2022-07-25 10:16:54', '2022-07-25 10:21:48', '0');
INSERT INTO `gen_field_type` (`id`, `column_type`, `attr_type`, `package_name`, `creator`, `editor`, `create_date`, `update_date`, `del_flag`) VALUES ('1540001809446211586', 'int4', 'Integer', NULL, '1341620898007281665', '1341620898007281665', '2022-07-25 10:16:54', '2022-07-25 10:21:48', '0');
INSERT INTO `gen_field_type` (`id`, `column_type`, `attr_type`, `package_name`, `creator`, `editor`, `create_date`, `update_date`, `del_flag`) VALUES ('1540001992288505857', 'int2', 'Integer', NULL, '1341620898007281665', '1341620898007281665', '2022-07-25 10:16:54', '2022-07-25 10:21:48', '0');
INSERT INTO `gen_field_type` (`id`, `column_type`, `attr_type`, `package_name`, `creator`, `editor`, `create_date`, `update_date`, `del_flag`) VALUES ('1545035717690912769', 'numeric', 'BigDecimal', 'java.math.BigDecimal', '1341620898007281665', '1341620898007281665', '2022-07-25 10:16:54', '2022-07-25 10:21:48', '0');
-- ------------------------------------低代码------------------------------------

-- ------------------------------------认证------------------------------------
CREATE TABLE `oauth2_authorization` (
                                        `id` varchar(100) NOT NULL,
                                        `registered_client_id` varchar(100) NOT NULL,
                                        `principal_name` varchar(200) NOT NULL,
                                        `authorization_grant_type` varchar(100) NOT NULL,
                                        `authorized_scopes` varchar(1000) DEFAULT NULL,
                                        `attributes` blob,
                                        `state` varchar(500) DEFAULT NULL,
                                        `authorization_code_value` blob,
                                        `authorization_code_issued_at` timestamp NULL DEFAULT NULL,
                                        `authorization_code_expires_at` timestamp NULL DEFAULT NULL,
                                        `authorization_code_metadata` blob,
                                        `access_token_value` blob,
                                        `access_token_issued_at` timestamp NULL DEFAULT NULL,
                                        `access_token_expires_at` timestamp NULL DEFAULT NULL,
                                        `access_token_metadata` blob,
                                        `access_token_type` varchar(100) DEFAULT NULL,
                                        `access_token_scopes` varchar(1000) DEFAULT NULL,
                                        `oidc_id_token_value` blob,
                                        `oidc_id_token_issued_at` timestamp NULL DEFAULT NULL,
                                        `oidc_id_token_expires_at` timestamp NULL DEFAULT NULL,
                                        `oidc_id_token_metadata` blob,
                                        `refresh_token_value` blob,
                                        `refresh_token_issued_at` timestamp NULL DEFAULT NULL,
                                        `refresh_token_expires_at` timestamp NULL DEFAULT NULL,
                                        `refresh_token_metadata` blob,
                                        PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
CREATE TABLE `oauth2_authorization_consent` (
                                                `registered_client_id` varchar(100) NOT NULL,
                                                `principal_name` varchar(200) NOT NULL,
                                                `authorities` varchar(1000) NOT NULL,
                                                PRIMARY KEY (`registered_client_id`,`principal_name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
CREATE TABLE `oauth2_registered_client` (
                                            `id` varchar(100) NOT NULL,
                                            `client_id` varchar(100) NOT NULL,
                                            `client_id_issued_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
                                            `client_secret` varchar(200) DEFAULT NULL,
                                            `client_secret_expires_at` timestamp NULL DEFAULT NULL,
                                            `client_name` varchar(200) NOT NULL,
                                            `client_authentication_methods` varchar(1000) NOT NULL,
                                            `authorization_grant_types` varchar(1000) NOT NULL,
                                            `redirect_uris` text,
                                            `scopes` varchar(1000) NOT NULL,
                                            `client_settings` varchar(2000) NOT NULL,
                                            `token_settings` varchar(2000) NOT NULL,
                                            PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
-- ------------------------------------认证------------------------------------

-- ------------------------------------存储过程------------------------------------
-- 存储过程1 开始
# 删除存储过程
DROP PROCEDURE
    IF EXISTS deptIds;

# 创建存储过程
CREATE PROCEDURE deptIds (IN userId BIGINT(20))
BEGIN
# 定义变量值
DECLARE dept_id BIGINT (20);

# 定义变量值
DECLARE s INT DEFAULT 0;

# 接收数据集
DECLARE consume CURSOR FOR SELECT
                                                     boot_sys_dept.id
                           FROM
                               boot_sys_dept,
                               boot_sys_role,
                               boot_sys_role_dept,
                               boot_sys_user,
                               boot_sys_user_role
                           WHERE
                                                         boot_sys_dept.id = boot_sys_role_dept.dept_id
                             AND boot_sys_role_dept.role_id = boot_sys_role.id
                             AND boot_sys_user.id = boot_sys_user_role.user_id
                             AND boot_sys_role.id = boot_sys_user_role.role_id
                             AND boot_sys_user.id = userId
                             AND boot_sys_dept.del_flag = 0
                           GROUP BY
                                                     boot_sys_dept.id;

# 没有数据返回,将变量设置为1
DECLARE CONTINUE HANDLER FOR NOT FOUND
SET s = 1;

# 创建表
CREATE TABLE
    IF NOT EXISTS `temp_boot_sys_dept` (
    `id` BIGINT (20) DEFAULT NULL,
    `uid` BIGINT (20) DEFAULT NULL,
    KEY `idx_id_uid` (`id`, `uid`)
    ) ENGINE = INNODB DEFAULT CHARSET = utf8mb4;

# 删除数据
DELETE
FROM
    temp_boot_sys_dept
WHERE
        uid = userId;

# 打开cosume游标进行程序调用
OPEN consume;

# 将consume赋值给dept_id
FETCH consume INTO dept_id;

WHILE s <> 1 DO
INSERT temp_boot_sys_dept SELECT
                                        id,
                                        userId
                          FROM
                                        boot_sys_dept
                          WHERE
                                            del_flag = 0
                            AND path LIKE concat('%', dept_id, '%');

# 将consume赋值给dept_id
FETCH consume INTO dept_id;


END
WHILE;


# 关闭游标
CLOSE consume;

# 查询
SELECT
    id
FROM
    temp_boot_sys_dept
WHERE
        uid = userId
GROUP BY
    id;

# 提交事务
COMMIT;

END;

# 调用
CALL deptIds ('1');
-- 存储过程1 结束

-- 存储过程2 开始
# 删除存储过程
DROP PROCEDURE
    IF EXISTS updatePath2;

# 创建存储过程
CREATE PROCEDURE updatePath2 (
    IN xid BIGINT (20),
    IN pid BIGINT (20)
        )
BEGIN
	# 定义变量
DECLARE s INT DEFAULT 0;

# 定义变量
DECLARE parpath VARCHAR (2000);

# 定义变量
DECLARE nid BIGINT (20);

# 定义变量
DECLARE npath VARCHAR (2000);

# 接收结果集
DECLARE con1 CURSOR FOR SELECT

                                                                          IF (count(path) > 0, path, '0') AS path
                        FROM
                                                                          boot_sys_dept
                        WHERE
                                                                              id = pid;

# 接收结果集
DECLARE con2 CURSOR FOR SELECT
                              id,
                              path
                        FROM
                              boot_sys_dept
                        WHERE
                                  path LIKE concat('%', xid, '%')
                          AND del_flag = 0
                          AND id <> xid;

# 没有数据后返回,将s=1
DECLARE CONTINUE HANDLER FOR NOT FOUND
SET s = 1;

# 开启游标
OPEN con1;

# 开启游标
OPEN con2;

# 赋值
FETCH con1 INTO parpath;

# 赋值
FETCH con2 INTO nid,
 npath;


WHILE s <> 1 DO
UPDATE boot_sys_dept
SET path = concat(parpath, '/', nid)
WHERE
        id = nid;

# 赋值
FETCH con2 INTO nid,
 npath;


END
WHILE;

# 关闭游标
CLOSE con1;

# 关闭游标
CLOSE con2;

# 修改
UPDATE boot_sys_dept
SET path = concat(parpath, '/', xid)
WHERE
        id = xid;
# 提交事务
COMMIT;

END;

# 调用
CALL updatePath2 ('1', '0');
-- 存储过程2 结束

-- 存储过程3 开始
# 创建存储过程
DROP PROCEDURE
    IF EXISTS updatePath1;

CREATE PROCEDURE updatePath1 (
    IN did BIGINT (20),
    IN pid BIGINT (20)
        )
BEGIN
	# 定义变量
DECLARE s INT DEFAULT 0;


DECLARE nid BIGINT (20);


DECLARE npath VARCHAR (2000);

# 接收数据集
DECLARE consume CURSOR FOR SELECT
                                                               id,
                                                               path
                           FROM
                                                               boot_sys_dept
                           WHERE
                                                                   id = pid;

# 打开游标
OPEN consume;

# 赋值
FETCH consume INTO nid,
 npath;


IF pid = '0' THEN
UPDATE boot_sys_dept
SET path = CONCAT('0/', did)
WHERE
        id = did;


ELSE
UPDATE boot_sys_dept
SET path = CONCAT(npath, '/', did)
WHERE
        id = did;


END
IF;

# 关闭游标
CLOSE consume;

# 提交事务
COMMIT;

END;

# 调用
CALL updatePath1 ('1', '0');
-- 存储过程3 结束
-- ------------------------------------存储过程------------------------------------

-- ------------------------------------分布式事务------------------------------------
CREATE TABLE `undo_log` (
                            `id` bigint(20) NOT NULL AUTO_INCREMENT,
                            `branch_id` bigint(20) NOT NULL,
                            `xid` varchar(100) NOT NULL,
                            `context` varchar(128) NOT NULL,
                            `rollback_info` longblob NOT NULL,
                            `log_status` int(11) NOT NULL,
                            `log_created` datetime NOT NULL,
                            `log_modified` datetime NOT NULL,
                            `ext` varchar(100) DEFAULT NULL,
                            PRIMARY KEY (`id`),
                            UNIQUE KEY `ux_undo_log` (`xid`,`branch_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
-- ------------------------------------分布式事务------------------------------------

--
CREATE TABLE `boot_sys_mail` (
                                 `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'id',
                                 `creator` bigint(20) DEFAULT NULL COMMENT '创建人',
                                 `editor` bigint(20) DEFAULT NULL COMMENT '编辑人',
                                 `create_date` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                                 `update_date` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '修改时间',
                                 `del_flag` tinyint(1) NOT NULL COMMENT '1已删除 0未删除',
                                 `username` varchar(50) DEFAULT NULL,
                                 `host` varchar(20) DEFAULT NULL,
                                 `password` varchar(20) DEFAULT NULL,
                                 `version` int(11) NOT NULL DEFAULT '0' COMMENT '版本号',
                                 PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
--