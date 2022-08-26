------------------------------------菜单------------------------------------
CREATE TABLE `boot_sys_menu` (
  `id` bigint(20) NOT NULL COMMENT 'id',
  `pid` bigint(20) NOT NULL COMMENT '父节点',
  `permissions` text NOT NULL COMMENT '授权(多个用逗号分隔，如：sys:user:list,sys:user:save)',
  `type` tinyint(1) unsigned DEFAULT NULL COMMENT '类型   0：菜单   1：按钮',
  `name` varchar(100) NOT NULL COMMENT '菜单名称',
  `url` varchar(200) NOT NULL COMMENT '路径',
  `auth_level` tinyint(3) NOT NULL COMMENT '认证等级   0：权限认证   1：登录认证    2：无需认证',
  `method` varchar(100) NOT NULL COMMENT '请求方式（如：GET、POST、PUT、DELETE）',
  `icon` varchar(50) DEFAULT NULL COMMENT '图标',
  `creator` bigint(20) DEFAULT NULL COMMENT '创建人',
  `editor` bigint(20) DEFAULT NULL COMMENT '编辑人',
  `create_date` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_date` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  `del_flag` tinyint(1) DEFAULT '0' COMMENT '1已删除 0未删除',
  `sort` int(11) DEFAULT '1' COMMENT '排序',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC COMMENT='菜单';
INSERT INTO `boot_sys_menu` (`id`, `pid`, `permissions`, `type`, `name`, `url`, `auth_level`, `method`, `icon`, `creator`, `editor`, `create_date`, `update_date`, `del_flag`, `sort`) VALUES ('1391677542887788567', '1535878154046939137', 'sys:menu:view', '0', '菜单管理', '/sys/menu/view', '0', 'GET', 'treeTable', '1341620898007281665', '1341620898007281665', '2022-06-12 23:36:44', '2022-06-12 23:36:44', '0', '3000');
INSERT INTO `boot_sys_menu` (`id`, `pid`, `permissions`, `type`, `name`, `url`, `auth_level`, `method`, `icon`, `creator`, `editor`, `create_date`, `update_date`, `del_flag`, `sort`) VALUES ('1535858679453085698', '1391677542887788567', 'sys:menu:query', '1', '菜单查询', '/sys/menu/api/query', '0', 'POST', 'search', '1341620898007281665', '1341620898007281665', '2022-06-22 07:09:59', '2022-06-21 23:11:00', '0', '10');
INSERT INTO `boot_sys_menu` (`id`, `pid`, `permissions`, `type`, `name`, `url`, `auth_level`, `method`, `icon`, `creator`, `editor`, `create_date`, `update_date`, `del_flag`, `sort`) VALUES ('1535859148908949506', '1391677542887788567', 'sys:menu:insert', '1', '菜单新增', '/sys/menu/api/insert', '0', 'POST', 'plus', '1341620898007281665', NULL, '2022-06-12 21:59:41', '2022-06-12 21:59:41', '0', '10');
INSERT INTO `boot_sys_menu` (`id`, `pid`, `permissions`, `type`, `name`, `url`, `auth_level`, `method`, `icon`, `creator`, `editor`, `create_date`, `update_date`, `del_flag`, `sort`) VALUES ('1535859326311231489', '1391677542887788567', 'sys:menu:update', '1', '菜单修改', '/sys/menu/api/update', '0', 'PUT', 'edit', '1341620898007281665', '1341620898007281665', '2022-06-20 01:45:22', '2022-06-19 17:46:23', '0', '10');
INSERT INTO `boot_sys_menu` (`id`, `pid`, `permissions`, `type`, `name`, `url`, `auth_level`, `method`, `icon`, `creator`, `editor`, `create_date`, `update_date`, `del_flag`, `sort`) VALUES ('1535859588534923265', '1391677542887788567', 'sys:menu:delete', '1', '菜单删除', '/sys/menu/api/delete', '0', 'DELETE', 'delete', '1341620898007281665', NULL, '2022-06-12 13:40:35', '2022-06-12 13:40:35', '0', '10');
INSERT INTO `boot_sys_menu` (`id`, `pid`, `permissions`, `type`, `name`, `url`, `auth_level`, `method`, `icon`, `creator`, `editor`, `create_date`, `update_date`, `del_flag`, `sort`) VALUES ('1535878154046939137', '0', 'sys:view', '0', '系统管理', '/sys/view', '0', 'GET', 'system', '1341620898007281665', '1341620898007281665', '2022-08-23 12:03:36', '2022-08-23 12:03:36', '0', '9000');
INSERT INTO `boot_sys_menu` (`id`, `pid`, `permissions`, `type`, `name`, `url`, `auth_level`, `method`, `icon`, `creator`, `editor`, `create_date`, `update_date`, `del_flag`, `sort`) VALUES ('1535881096963563522', '1535878154046939137', 'sys:user:view', '0', '用户管理', '/sys/user/view', '0', 'GET', 'user', '1341620898007281665', '1341620898007281665', '2022-06-16 04:04:56', '2022-06-15 20:05:57', '0', '1000');
INSERT INTO `boot_sys_menu` (`id`, `pid`, `permissions`, `type`, `name`, `url`, `auth_level`, `method`, `icon`, `creator`, `editor`, `create_date`, `update_date`, `del_flag`, `sort`) VALUES ('1535881356595175426', '1535878154046939137', 'sys:role:view', '0', '角色管理', '/sys/role/view', '0', 'GET', 'peoples', '1341620898007281665', NULL, '2022-06-12 15:07:05', '2022-06-12 15:07:05', '0', '2000');
INSERT INTO `boot_sys_menu` (`id`, `pid`, `permissions`, `type`, `name`, `url`, `auth_level`, `method`, `icon`, `creator`, `editor`, `create_date`, `update_date`, `del_flag`, `sort`) VALUES ('1535886982654205953', '1535881356595175426', 'sys:role:query', '1', '角色查询', '/sys/role/api/query', '0', 'POST', 'search', '1341620898007281665', '1341620898007281665', '2022-06-22 07:10:10', '2022-06-21 23:11:10', '0', '10');
INSERT INTO `boot_sys_menu` (`id`, `pid`, `permissions`, `type`, `name`, `url`, `auth_level`, `method`, `icon`, `creator`, `editor`, `create_date`, `update_date`, `del_flag`, `sort`) VALUES ('1535887129341599746', '1535881356595175426', 'sys:role:insert', '1', '角色新增', '/sys/role/api/insert', '0', 'POST', 'plus', '1341620898007281665', NULL, '2022-06-12 15:30:02', '2022-06-12 15:30:02', '0', '10');
INSERT INTO `boot_sys_menu` (`id`, `pid`, `permissions`, `type`, `name`, `url`, `auth_level`, `method`, `icon`, `creator`, `editor`, `create_date`, `update_date`, `del_flag`, `sort`) VALUES ('1535887276649750530', '1535881356595175426', 'sys:role:update', '1', '角色修改', '/sys/role/api/update', '0', 'PUT', 'edit', '1341620898007281665', '1341620898007281665', '2022-06-20 01:45:31', '2022-06-19 17:46:31', '0', '10');
INSERT INTO `boot_sys_menu` (`id`, `pid`, `permissions`, `type`, `name`, `url`, `auth_level`, `method`, `icon`, `creator`, `editor`, `create_date`, `update_date`, `del_flag`, `sort`) VALUES ('1535887450809835521', '1535881356595175426', 'sys:role:delete', '1', '角色删除', '/sys/role/api/delete', '0', 'DELETE', 'delete', '1341620898007281665', NULL, '2022-06-12 15:31:18', '2022-06-12 15:31:18', '0', '10');
INSERT INTO `boot_sys_menu` (`id`, `pid`, `permissions`, `type`, `name`, `url`, `auth_level`, `method`, `icon`, `creator`, `editor`, `create_date`, `update_date`, `del_flag`, `sort`) VALUES ('1535887779873955841', '1535881096963563522', 'sys:user:query', '1', '用户查询', '/sys/user/api/query', '0', 'POST', 'search', '1341620898007281665', '1341620898007281665', '2022-06-22 07:10:19', '2022-06-21 23:11:20', '0', '10');
INSERT INTO `boot_sys_menu` (`id`, `pid`, `permissions`, `type`, `name`, `url`, `auth_level`, `method`, `icon`, `creator`, `editor`, `create_date`, `update_date`, `del_flag`, `sort`) VALUES ('1535887940687765505', '1535881096963563522', 'sys:user:insert', '1', '用户新增', '/sys/user/api/insert', '0', 'POST', 'plus', '1341620898007281665', '1341620898007281665', '2022-07-23 15:34:06', '2022-07-23 15:34:06', '0', '10');
INSERT INTO `boot_sys_menu` (`id`, `pid`, `permissions`, `type`, `name`, `url`, `auth_level`, `method`, `icon`, `creator`, `editor`, `create_date`, `update_date`, `del_flag`, `sort`) VALUES ('1535888146045083649', '1535881096963563522', 'sys:user:update', '1', '用户修改', '/sys/user/api/update', '0', 'PUT', 'edit', '1341620898007281665', '1341620898007281665', '2022-06-20 01:45:37', '2022-06-19 17:46:37', '0', '10');
INSERT INTO `boot_sys_menu` (`id`, `pid`, `permissions`, `type`, `name`, `url`, `auth_level`, `method`, `icon`, `creator`, `editor`, `create_date`, `update_date`, `del_flag`, `sort`) VALUES ('1535888341252186114', '1535881096963563522', 'sys:user:delete', '1', '用户删除', '/sys/user/api/delete', '0', 'DELETE', 'delete', '1341620898007281665', NULL, '2022-06-12 23:37:47', '2022-06-12 23:37:47', '0', '10');
INSERT INTO `boot_sys_menu` (`id`, `pid`, `permissions`, `type`, `name`, `url`, `auth_level`, `method`, `icon`, `creator`, `editor`, `create_date`, `update_date`, `del_flag`, `sort`) VALUES ('1537444981390794754', '0', 'monitor:view', '0', '系统监控', '/monitor/view', '0', 'GET', 'monitor', '1341620898007281665', '1341620898007281665', '2022-08-23 12:03:57', '2022-08-23 12:03:57', '0', '8000');
INSERT INTO `boot_sys_menu` (`id`, `pid`, `permissions`, `type`, `name`, `url`, `auth_level`, `method`, `icon`, `creator`, `editor`, `create_date`, `update_date`, `del_flag`, `sort`) VALUES ('1537447512825225218', '1537444981390794754', 'monitor:druid:view', '0', '数据监控', '/druid/view', '0', 'GET', 'druid', '1341620898007281665', '1341620898007281665', '2022-08-25 16:33:52', '2022-08-25 16:33:52', '0', '4000');
INSERT INTO `boot_sys_menu` (`id`, `pid`, `permissions`, `type`, `name`, `url`, `auth_level`, `method`, `icon`, `creator`, `editor`, `create_date`, `update_date`, `del_flag`, `sort`) VALUES ('1538469199368712193', '1537444981390794754', 'monitor:admin:view', '0', '服务监控', 'http://124.222.196.51:5000/monitor', '0', 'GET', 'server', '1341620898007281665', '1341620898007281665', '2022-07-17 17:39:45', '2022-07-17 17:39:45', '0', '3000');
INSERT INTO `boot_sys_menu` (`id`, `pid`, `permissions`, `type`, `name`, `url`, `auth_level`, `method`, `icon`, `creator`, `editor`, `create_date`, `update_date`, `del_flag`, `sort`) VALUES ('1539250224424394753', '1535878154046939137', 'sys:log:view', '0', '日志管理', '/sys/log/view', '0', 'GET', 'log', '1341620898007281665', '1341620898007281665', '2022-08-23 12:04:04', '2022-08-23 12:04:04', '0', '500');
INSERT INTO `boot_sys_menu` (`id`, `pid`, `permissions`, `type`, `name`, `url`, `auth_level`, `method`, `icon`, `creator`, `editor`, `create_date`, `update_date`, `del_flag`, `sort`) VALUES ('1539251440843857922', '1539250224424394753', 'sys:log:operate:view', '0', '操作日志', '/sys/log/operate/view', '0', 'GET', 'form', '1341620898007281665', '1341620898007281665', '2022-06-24 06:45:42', '2022-06-23 23:13:09', '0', '200');
INSERT INTO `boot_sys_menu` (`id`, `pid`, `permissions`, `type`, `name`, `url`, `auth_level`, `method`, `icon`, `creator`, `editor`, `create_date`, `update_date`, `del_flag`, `sort`) VALUES ('1539265093588545538', '1539251440843857922', 'sys:log:operate:query', '1', '查询日志', '/sys/log/api/operate/query', '0', 'POST', 'search', '1341620898007281665', '1341620898007281665', '2022-06-24 06:46:11', '2022-06-23 23:13:38', '0', '10');
INSERT INTO `boot_sys_menu` (`id`, `pid`, `permissions`, `type`, `name`, `url`, `auth_level`, `method`, `icon`, `creator`, `editor`, `create_date`, `update_date`, `del_flag`, `sort`) VALUES ('1539396453854629890', '1539250224424394753', 'sys:log:login:view', '0', '登录日志', '/sys/log/login/view', '0', 'GET', 'logininfor', '1341620898007281665', '1341620898007281665', '2022-06-24 06:46:01', '2022-06-23 23:13:28', '0', '100');
INSERT INTO `boot_sys_menu` (`id`, `pid`, `permissions`, `type`, `name`, `url`, `auth_level`, `method`, `icon`, `creator`, `editor`, `create_date`, `update_date`, `del_flag`, `sort`) VALUES ('1539402478934646785', '1539396453854629890', 'sys:log:login:query', '1', '查询日志', '/sys/log/api/login/query', '0', 'POST', 'search', '1341620898007281665', '1341620898007281665', '2022-06-24 06:45:53', '2022-06-23 23:13:20', '0', '10');
INSERT INTO `boot_sys_menu` (`id`, `pid`, `permissions`, `type`, `name`, `url`, `auth_level`, `method`, `icon`, `creator`, `editor`, `create_date`, `update_date`, `del_flag`, `sort`) VALUES ('1539615028590673921', '1535881096963563522', 'sys:user:password', '1', '重置密码', '/sys/user/api/password', '0', 'PUT', 'key', '1341620898007281665', NULL, '2022-06-23 06:22:41', '2022-06-23 06:22:41', '0', '10');
INSERT INTO `boot_sys_menu` (`id`, `pid`, `permissions`, `type`, `name`, `url`, `auth_level`, `method`, `icon`, `creator`, `editor`, `create_date`, `update_date`, `del_flag`, `sort`) VALUES ('1539989085181972481', '1535878154046939137', 'sys:dict:view', '0', '字典管理', '/sys/dict/view', '0', 'GET', 'dict', '1341620898007281665', '1341620898007281665', '2022-06-24 07:01:55', '2022-06-23 23:29:23', '0', '900');
INSERT INTO `boot_sys_menu` (`id`, `pid`, `permissions`, `type`, `name`, `url`, `auth_level`, `method`, `icon`, `creator`, `editor`, `create_date`, `update_date`, `del_flag`, `sort`) VALUES ('1540000886082768897', '1539989085181972481', 'sys:dict:query', '1', '字典查询', '/sys/dict/api/query', '0', 'POST', 'search', '1341620898007281665', '1341620898007281665', '2022-06-24 07:30:24', '2022-06-23 23:57:51', '0', '10');
INSERT INTO `boot_sys_menu` (`id`, `pid`, `permissions`, `type`, `name`, `url`, `auth_level`, `method`, `icon`, `creator`, `editor`, `create_date`, `update_date`, `del_flag`, `sort`) VALUES ('1540001496240754689', '1539989085181972481', 'sys:dict:insert', '1', '字典新增', '/sys/dict/api/insert', '0', 'POST', 'plus', '1341620898007281665', '1341620898007281665', '2022-06-24 07:32:06', '2022-06-23 23:59:33', '0', '10');
INSERT INTO `boot_sys_menu` (`id`, `pid`, `permissions`, `type`, `name`, `url`, `auth_level`, `method`, `icon`, `creator`, `editor`, `create_date`, `update_date`, `del_flag`, `sort`) VALUES ('1540001809446211586', '1539989085181972481', 'sys:dict:update', '1', '字典修改', '/sys/dict/api/update', '0', 'PUT', 'edit', '1341620898007281665', NULL, '2022-06-24 00:00:18', '2022-06-24 00:00:18', '0', '10');
INSERT INTO `boot_sys_menu` (`id`, `pid`, `permissions`, `type`, `name`, `url`, `auth_level`, `method`, `icon`, `creator`, `editor`, `create_date`, `update_date`, `del_flag`, `sort`) VALUES ('1540001992288505857', '1539989085181972481', 'sys:dict:delete', '1', '字典删除', '/sys/dict/api/delete', '0', 'DELETE', 'delete', '1341620898007281665', '1341620898007281665', '2022-06-24 07:33:59', '2022-06-24 00:01:26', '0', '10');
INSERT INTO `boot_sys_menu` (`id`, `pid`, `permissions`, `type`, `name`, `url`, `auth_level`, `method`, `icon`, `creator`, `editor`, `create_date`, `update_date`, `del_flag`, `sort`) VALUES ('1545035717690912769', '0', 'workflow:view', '0', '工作流程', '/workflow/view', '0', 'GET', 'tree', '1341620898007281665', '1341620898007281665', '2022-08-23 12:03:49', '2022-08-23 12:03:49', '0', '7000');
INSERT INTO `boot_sys_menu` (`id`, `pid`, `permissions`, `type`, `name`, `url`, `auth_level`, `method`, `icon`, `creator`, `editor`, `create_date`, `update_date`, `del_flag`, `sort`) VALUES ('1545036486288732162', '1545035717690912769', 'workflow:definition:view', '0', '流程定义', '/workflow/definition/view', '0', 'GET', 'guide', '1341620898007281665', '1341620898007281665', '2022-07-07 21:27:18', '2022-07-07 21:27:18', '0', '5000');
INSERT INTO `boot_sys_menu` (`id`, `pid`, `permissions`, `type`, `name`, `url`, `auth_level`, `method`, `icon`, `creator`, `editor`, `create_date`, `update_date`, `del_flag`, `sort`) VALUES ('1545037580289044482', '1545035717690912769', 'workflow:process:view', '0', '流程任务', '/workflow/process/view', '0', 'GET', 'timeRange', '1341620898007281665', '1341620898007281665', '2022-07-07 21:34:30', '2022-07-07 21:34:31', '0', '4000');
INSERT INTO `boot_sys_menu` (`id`, `pid`, `permissions`, `type`, `name`, `url`, `auth_level`, `method`, `icon`, `creator`, `editor`, `create_date`, `update_date`, `del_flag`, `sort`) VALUES ('1545442687073681410', '1545036486288732162', 'workflow:definition:insert', '1', '流程新增', '/workflow/definition/api/insert', '0', 'POST', 'plus', '1537114827246292994', NULL, '2022-07-09 00:20:24', '2022-07-09 00:20:24', '0', '10');
INSERT INTO `boot_sys_menu` (`id`, `pid`, `permissions`, `type`, `name`, `url`, `auth_level`, `method`, `icon`, `creator`, `editor`, `create_date`, `update_date`, `del_flag`, `sort`) VALUES ('1545442932918616065', '1545036486288732162', 'workflow:definition:query', '1', '流程查询', '/workflow/definition/api/query', '0', 'POST', 'search', '1537114827246292994', NULL, '2022-08-25 16:31:34', '2022-08-25 16:31:34', '0', '10');
INSERT INTO `boot_sys_menu` (`id`, `pid`, `permissions`, `type`, `name`, `url`, `auth_level`, `method`, `icon`, `creator`, `editor`, `create_date`, `update_date`, `del_flag`, `sort`) VALUES ('1545443357407346689', '1545036486288732162', 'workflow:definition:activate', '1', '流程激活', '/workflow/definition/api/activate', '0', 'PUT', 'play-circle', '1537114827246292994', NULL, '2022-07-09 00:23:04', '2022-07-09 00:23:04', '0', '10');
INSERT INTO `boot_sys_menu` (`id`, `pid`, `permissions`, `type`, `name`, `url`, `auth_level`, `method`, `icon`, `creator`, `editor`, `create_date`, `update_date`, `del_flag`, `sort`) VALUES ('1545443597204094977', '1545036486288732162', 'workflow:definition:suspend', '1', '流程挂起', '/workflow/definition/api/suspend', '0', 'PUT', 'pause-circle', '1537114827246292994', NULL, '2022-07-09 00:24:01', '2022-07-09 00:24:01', '0', '10');
INSERT INTO `boot_sys_menu` (`id`, `pid`, `permissions`, `type`, `name`, `url`, `auth_level`, `method`, `icon`, `creator`, `editor`, `create_date`, `update_date`, `del_flag`, `sort`) VALUES ('1545443828113113090', '1545036486288732162', 'workflow:definition:delete', '1', '流程删除', '/workflow/definition/api/delete', '0', 'DELETE', 'delete', '1537114827246292994', NULL, '2022-07-09 00:24:56', '2022-07-09 00:24:56', '0', '10');
INSERT INTO `boot_sys_menu` (`id`, `pid`, `permissions`, `type`, `name`, `url`, `auth_level`, `method`, `icon`, `creator`, `editor`, `create_date`, `update_date`, `del_flag`, `sort`) VALUES ('1545444197799067650', '1545036486288732162', 'workflow:definition:image', '1', '流程查看', '/workflow/definition/api/image', '0', 'GET', 'eye', '1537114827246292994', NULL, '2022-07-09 00:26:24', '2022-07-09 00:26:24', '0', '10');
INSERT INTO `boot_sys_menu` (`id`, `pid`, `permissions`, `type`, `name`, `url`, `auth_level`, `method`, `icon`, `creator`, `editor`, `create_date`, `update_date`, `del_flag`, `sort`) VALUES ('1545444853809184770', '1545037580289044482', 'workflow:process:resource:query', '1', '资源查询', '/workflow/process/api/resource/query', '0', 'POST', 'search', '1537114827246292994', '1341620898007281665', '2022-08-25 13:27:40', '2022-08-25 13:27:40', '0', '10');
INSERT INTO `boot_sys_menu` (`id`, `pid`, `permissions`, `type`, `name`, `url`, `auth_level`, `method`, `icon`, `creator`, `editor`, `create_date`, `update_date`, `del_flag`, `sort`) VALUES ('1545445208781520897', '1545037580289044482', 'workflow:process:resource:audit', '1', '资源审核', '/workflow/process/api/resource/audit', '0', 'POST', 'audit', '1537114827246292994', '1341620898007281665', '2022-08-25 13:27:25', '2022-08-25 13:27:26', '0', '10');
INSERT INTO `boot_sys_menu` (`id`, `pid`, `permissions`, `type`, `name`, `url`, `auth_level`, `method`, `icon`, `creator`, `editor`, `create_date`, `update_date`, `del_flag`, `sort`) VALUES ('1547109188256260097', '1535878154046939137', 'sys:api:view', '0', '接口文档', 'http://175.178.69.253/laokou/doc.html', '0', 'GET', 'row', '1341620898007281665', '1341620898007281665', '2022-07-17 17:04:18', '2022-07-17 17:04:19', '0', '400');
INSERT INTO `boot_sys_menu` (`id`, `pid`, `permissions`, `type`, `name`, `url`, `auth_level`, `method`, `icon`, `creator`, `editor`, `create_date`, `update_date`, `del_flag`, `sort`) VALUES ('1549758203624480770', '1535878154046939137', 'sys:message:view', '0', '消息管理', '/sys/message/view', '0', 'GET', 'message', '1341620898007281665', NULL, '2022-07-20 22:08:44', '2022-07-20 22:08:44', '0', '800');
INSERT INTO `boot_sys_menu` (`id`, `pid`, `permissions`, `type`, `name`, `url`, `auth_level`, `method`, `icon`, `creator`, `editor`, `create_date`, `update_date`, `del_flag`, `sort`) VALUES ('1549963210746126337', '1537447512825225218', 'monitor:druid:auth:view', '0', '认证模块监控', 'http://175.178.69.253/laokou/auth/druid/login.html', '0', 'GET', 'password', '1341620898007281665', '1341620898007281665', '2022-07-28 08:19:44', '2022-07-28 08:19:44', '0', '900');
INSERT INTO `boot_sys_menu` (`id`, `pid`, `permissions`, `type`, `name`, `url`, `auth_level`, `method`, `icon`, `creator`, `editor`, `create_date`, `update_date`, `del_flag`, `sort`) VALUES ('1549963748090994690', '1537447512825225218', 'monitor:druid:admin:view', '0', '后台模块监控', 'http://175.178.69.253/laokou/admin/druid/login.html', '0', 'GET', 'desktop', '1341620898007281665', '1341620898007281665', '2022-07-28 08:19:48', '2022-07-28 08:19:48', '0', '800');
INSERT INTO `boot_sys_menu` (`id`, `pid`, `permissions`, `type`, `name`, `url`, `auth_level`, `method`, `icon`, `creator`, `editor`, `create_date`, `update_date`, `del_flag`, `sort`) VALUES ('1550116610713944065', '1549758203624480770', 'sys:message:query', '1', '消息查询', '/sys/message/api/query', '0', 'POST', 'search', '1341620898007281665', NULL, '2022-08-25 16:32:35', '2022-08-25 16:32:35', '0', '10');
INSERT INTO `boot_sys_menu` (`id`, `pid`, `permissions`, `type`, `name`, `url`, `auth_level`, `method`, `icon`, `creator`, `editor`, `create_date`, `update_date`, `del_flag`, `sort`) VALUES ('1550117239402364930', '1549758203624480770', 'sys:message:insert', '1', '消息新增', '/sys/message/api/insert', '0', 'POST', 'plus', '1341620898007281665', '1341620898007281665', '2022-07-21 21:55:54', '2022-07-21 21:55:54', '0', '10');
INSERT INTO `boot_sys_menu` (`id`, `pid`, `permissions`, `type`, `name`, `url`, `auth_level`, `method`, `icon`, `creator`, `editor`, `create_date`, `update_date`, `del_flag`, `sort`) VALUES ('1550117550498086913', '1549758203624480770', 'sys:message:detail', '1', '消息查看', '/sys/message/api/detail', '0', 'GET', 'eyeOpen', '1341620898007281665', NULL, '2022-07-21 21:56:38', '2022-07-21 21:56:38', '0', '10');
INSERT INTO `boot_sys_menu` (`id`, `pid`, `permissions`, `type`, `name`, `url`, `auth_level`, `method`, `icon`, `creator`, `editor`, `create_date`, `update_date`, `del_flag`, `sort`) VALUES ('1551957039155638274', '1535878154046939137', 'sys:dept:view', '0', '部门管理', '/sys/dept/view', '0', 'GET', 'team', '1341620898007281665', NULL, '2022-07-26 23:46:07', '2022-07-26 23:46:07', '0', '999');
INSERT INTO `boot_sys_menu` (`id`, `pid`, `permissions`, `type`, `name`, `url`, `auth_level`, `method`, `icon`, `creator`, `editor`, `create_date`, `update_date`, `del_flag`, `sort`) VALUES ('1552304553893392386', '1551957039155638274', 'sys:dept:query', '1', '部门查询', '/sys/dept/api/query', '0', 'POST', 'search', '1341620898007281665', NULL, '2022-08-25 16:32:48', '2022-08-25 16:32:48', '0', '10');
INSERT INTO `boot_sys_menu` (`id`, `pid`, `permissions`, `type`, `name`, `url`, `auth_level`, `method`, `icon`, `creator`, `editor`, `create_date`, `update_date`, `del_flag`, `sort`) VALUES ('1552304713121755138', '1551957039155638274', 'sys:dept:insert', '1', '部门新增', '/sys/dept/api/insert', '0', 'POST', 'plus', '1341620898007281665', NULL, '2022-07-27 22:47:39', '2022-07-27 22:47:39', '0', '10');
INSERT INTO `boot_sys_menu` (`id`, `pid`, `permissions`, `type`, `name`, `url`, `auth_level`, `method`, `icon`, `creator`, `editor`, `create_date`, `update_date`, `del_flag`, `sort`) VALUES ('1552304865207218177', '1551957039155638274', 'sys:dept:update', '1', '部门修改', '/sys/dept/api/update', '0', 'PUT', 'edit', '1341620898007281665', NULL, '2022-07-27 22:48:15', '2022-07-27 22:48:15', '0', '10');
INSERT INTO `boot_sys_menu` (`id`, `pid`, `permissions`, `type`, `name`, `url`, `auth_level`, `method`, `icon`, `creator`, `editor`, `create_date`, `update_date`, `del_flag`, `sort`) VALUES ('1552305016701284353', '1551957039155638274', 'sys:dept:delete', '1', '部门删除', '/sys/dept/api/delete', '0', 'DELETE', 'delete', '1341620898007281665', NULL, '2022-07-27 22:48:51', '2022-07-27 22:48:51', '0', '10');
INSERT INTO `boot_sys_menu` (`id`, `pid`, `permissions`, `type`, `name`, `url`, `auth_level`, `method`, `icon`, `creator`, `editor`, `create_date`, `update_date`, `del_flag`, `sort`) VALUES ('1552525480564416514', '1537444981390794754', 'sys:monitor:cache', '0', '缓存监控', '/monitor/cache/view', '0', 'GET', 'redis', '1341620898007281665', '1341620898007281665', '2022-07-28 23:30:24', '2022-07-28 23:30:25', '0', '2000');
INSERT INTO `boot_sys_menu` (`id`, `pid`, `permissions`, `type`, `name`, `url`, `auth_level`, `method`, `icon`, `creator`, `editor`, `create_date`, `update_date`, `del_flag`, `sort`) VALUES ('1552526314178142209', '1537444981390794754', 'sys:monitor:server', '0', '主机监控', '/monitor/server/view', '0', 'GET', 'hdd', '1341620898007281665', '1341620898007281665', '2022-07-28 23:30:04', '2022-07-28 23:30:05', '0', '1000');
INSERT INTO `boot_sys_menu` (`id`, `pid`, `permissions`, `type`, `name`, `url`, `auth_level`, `method`, `icon`, `creator`, `editor`, `create_date`, `update_date`, `del_flag`, `sort`) VALUES ('1557599617712517122', '1535878154046939137', 'sys:oauth:view', '0', '认证管理', '/sys/oauth/view', '0', 'GET', 'phone', '1341620898007281665', NULL, '2022-08-11 13:27:42', '2022-08-11 13:27:42', '0', '555');
INSERT INTO `boot_sys_menu` (`id`, `pid`, `permissions`, `type`, `name`, `url`, `auth_level`, `method`, `icon`, `creator`, `editor`, `create_date`, `update_date`, `del_flag`, `sort`) VALUES ('1557710997866184706', '1557599617712517122', 'sys:oauth:query', '1', '认证查询', '/sys/oauth/api/query', '0', 'POST', 'search', '1341620898007281665', '1341620898007281665', '2022-08-25 16:32:50', '2022-08-25 16:32:50', '0', '10');
INSERT INTO `boot_sys_menu` (`id`, `pid`, `permissions`, `type`, `name`, `url`, `auth_level`, `method`, `icon`, `creator`, `editor`, `create_date`, `update_date`, `del_flag`, `sort`) VALUES ('1557711193186533378', '1557599617712517122', 'sys:oauth:insert', '1', '认证新增', '/sys/oauth/api/insert', '0', 'POST', 'plus', '1341620898007281665', NULL, '2022-08-11 20:51:04', '2022-08-11 20:51:04', '0', '10');
INSERT INTO `boot_sys_menu` (`id`, `pid`, `permissions`, `type`, `name`, `url`, `auth_level`, `method`, `icon`, `creator`, `editor`, `create_date`, `update_date`, `del_flag`, `sort`) VALUES ('1557711375076720642', '1557599617712517122', 'sys:oauth:update', '1', '认证修改', '/sys/oauth/api/update', '0', 'PUT', 'edit', '1341620898007281665', NULL, '2022-08-11 20:51:47', '2022-08-11 20:51:47', '0', '10');
INSERT INTO `boot_sys_menu` (`id`, `pid`, `permissions`, `type`, `name`, `url`, `auth_level`, `method`, `icon`, `creator`, `editor`, `create_date`, `update_date`, `del_flag`, `sort`) VALUES ('1557711509764210689', '1557599617712517122', 'sys:oauth:delete', '1', '认证删除', '/sys/oauth/api/delete', '0', 'DELETE', 'delete', '1341620898007281665', NULL, '2022-08-11 20:52:19', '2022-08-11 20:52:19', '0', '10');
INSERT INTO `boot_sys_menu` (`id`, `pid`, `permissions`, `type`, `name`, `url`, `auth_level`, `method`, `icon`, `creator`, `editor`, `create_date`, `update_date`, `del_flag`, `sort`) VALUES ('1560528267620061186', '1535878154046939137', 'sys:resource:view', '0', '资源管理', '/sys/resource/view', '0', 'GET', 'folder', '1341620898007281665', '1341620898007281665', '2022-08-23 12:03:22', '2022-08-23 12:03:22', '0', '510');
INSERT INTO `boot_sys_menu` (`id`, `pid`, `permissions`, `type`, `name`, `url`, `auth_level`, `method`, `icon`, `creator`, `editor`, `create_date`, `update_date`, `del_flag`, `sort`) VALUES ('1560529603300364290', '1560528267620061186', 'sys:resource:audio:view', '0', '音频管理', '/sys/resource/audio/view', '0', 'GET', 'customer-service', '1341620898007281665', '1341620898007281665', '2022-08-19 15:31:27', '2022-08-19 15:31:28', '0', '100');
INSERT INTO `boot_sys_menu` (`id`, `pid`, `permissions`, `type`, `name`, `url`, `auth_level`, `method`, `icon`, `creator`, `editor`, `create_date`, `update_date`, `del_flag`, `sort`) VALUES ('1560530192751071234', '1560528267620061186', 'sys:resource:video:view', '0', '视频管理', '/sys/resource/video/view', '0', 'GET', 'video-camera', '1341620898007281665', NULL, '2022-08-19 15:32:46', '2022-08-19 15:32:46', '0', '200');
INSERT INTO `boot_sys_menu` (`id`, `pid`, `permissions`, `type`, `name`, `url`, `auth_level`, `method`, `icon`, `creator`, `editor`, `create_date`, `update_date`, `del_flag`, `sort`) VALUES ('1560530418819862529', '1560528267620061186', 'sys:resource:image:view', '0', '图片管理', '/sys/resource/image/view', '0', 'GET', 'picture', '1341620898007281665', '1341620898007281665', '2022-08-19 15:34:18', '2022-08-19 15:34:18', '0', '300');
INSERT INTO `boot_sys_menu` (`id`, `pid`, `permissions`, `type`, `name`, `url`, `auth_level`, `method`, `icon`, `creator`, `editor`, `create_date`, `update_date`, `del_flag`, `sort`) VALUES ('1562969136453316610', '1560529603300364290', 'sys:resource:audio:query', '1', '音频查询', '/sys/resource/audio/api/query', '0', 'POST', 'search', '1537114827246292994', NULL, '2022-08-26 09:04:15', '2022-08-26 09:04:15', '0', '10');
INSERT INTO `boot_sys_menu` (`id`, `pid`, `permissions`, `type`, `name`, `url`, `auth_level`, `method`, `icon`, `creator`, `editor`, `create_date`, `update_date`, `del_flag`, `sort`) VALUES ('1562969406365167618', '1560529603300364290', 'sys:resource:audio:insert', '1', '音频新增', '/sys/resource/audio/api/insert', '0', 'POST', 'plus', '1537114827246292994', NULL, '2022-08-26 09:05:20', '2022-08-26 09:05:20', '0', '10');
INSERT INTO `boot_sys_menu` (`id`, `pid`, `permissions`, `type`, `name`, `url`, `auth_level`, `method`, `icon`, `creator`, `editor`, `create_date`, `update_date`, `del_flag`, `sort`) VALUES ('1562969780283174914', '1560529603300364290', 'sys:resource:audio:update', '1', '音频修改', '/sys/resource/audio/api/update', '0', 'PUT', 'edit', '1537114827246292994', NULL, '2022-08-26 09:06:49', '2022-08-26 09:06:49', '0', '10');
INSERT INTO `boot_sys_menu` (`id`, `pid`, `permissions`, `type`, `name`, `url`, `auth_level`, `method`, `icon`, `creator`, `editor`, `create_date`, `update_date`, `del_flag`, `sort`) VALUES ('1562970001914392577', '1560529603300364290', 'sys:resource:audio:delete', '1', '音频删除', '/sys/resource/audio/api/delete', '0', 'DELETE', 'delete', '1537114827246292994', NULL, '2022-08-26 09:07:42', '2022-08-26 09:07:42', '0', '10');
INSERT INTO `boot_sys_menu` (`id`, `pid`, `permissions`, `type`, `name`, `url`, `auth_level`, `method`, `icon`, `creator`, `editor`, `create_date`, `update_date`, `del_flag`, `sort`) VALUES ('1562970341317472258', '1560529603300364290', 'sys:resource:audio:diagram', '1', '音频流程图', '/sys/resource/audio/api/diagram', '0', 'GET', 'gold', '1537114827246292994', '1537114827246292994', '2022-08-26 09:18:57', '2022-08-26 09:18:58', '0', '10');
INSERT INTO `boot_sys_menu` (`id`, `pid`, `permissions`, `type`, `name`, `url`, `auth_level`, `method`, `icon`, `creator`, `editor`, `create_date`, `update_date`, `del_flag`, `sort`) VALUES ('1562971590226014209', '1560530418819862529', 'sys:resource:image:query', '1', '图片查询', '/sys/resource/image/api/query', '0', 'POST', 'search', '1537114827246292994', NULL, '2022-08-26 09:14:00', '2022-08-26 09:14:00', '0', '10');
INSERT INTO `boot_sys_menu` (`id`, `pid`, `permissions`, `type`, `name`, `url`, `auth_level`, `method`, `icon`, `creator`, `editor`, `create_date`, `update_date`, `del_flag`, `sort`) VALUES ('1562971829607526401', '1560530418819862529', 'sys:resource:image:insert', '1', '图片新增', '/sys/resource/image/api/insert', '0', 'POST', 'plus', '1537114827246292994', NULL, '2022-08-26 09:14:57', '2022-08-26 09:14:57', '0', '10');
INSERT INTO `boot_sys_menu` (`id`, `pid`, `permissions`, `type`, `name`, `url`, `auth_level`, `method`, `icon`, `creator`, `editor`, `create_date`, `update_date`, `del_flag`, `sort`) VALUES ('1562972243434336257', '1560530418819862529', 'sys:resource:image:update', '1', '图片修改', '/sys/resource/image/api/update', '0', 'PUT', 'edit', '1537114827246292994', NULL, '2022-08-26 09:16:36', '2022-08-26 09:16:36', '0', '10');
INSERT INTO `boot_sys_menu` (`id`, `pid`, `permissions`, `type`, `name`, `url`, `auth_level`, `method`, `icon`, `creator`, `editor`, `create_date`, `update_date`, `del_flag`, `sort`) VALUES ('1562972411969859586', '1560530418819862529', 'sys:resource:image:delete', '1', '图片删除', '/sys/resource/image/api/delete', '0', 'DELETE', 'delete', '1537114827246292994', NULL, '2022-08-26 09:17:16', '2022-08-26 09:17:16', '0', '10');
INSERT INTO `boot_sys_menu` (`id`, `pid`, `permissions`, `type`, `name`, `url`, `auth_level`, `method`, `icon`, `creator`, `editor`, `create_date`, `update_date`, `del_flag`, `sort`) VALUES ('1562972785472630785', '1560530418819862529', 'sys:resource:image:diagram', '1', '图片流程图', '/sys/resource/image/api/diagram', '0', 'GET', 'gold', '1537114827246292994', NULL, '2022-08-26 09:18:45', '2022-08-26 09:18:45', '0', '10');
INSERT INTO `boot_sys_menu` (`id`, `pid`, `permissions`, `type`, `name`, `url`, `auth_level`, `method`, `icon`, `creator`, `editor`, `create_date`, `update_date`, `del_flag`, `sort`) VALUES ('1562973157121519617', '1560530192751071234', 'sys:resource:video:query', '1', '视频查询', '/sys/resource/video/api/query', '0', 'POST', 'search', '1537114827246292994', NULL, '2022-08-26 09:20:14', '2022-08-26 09:20:14', '0', '10');
INSERT INTO `boot_sys_menu` (`id`, `pid`, `permissions`, `type`, `name`, `url`, `auth_level`, `method`, `icon`, `creator`, `editor`, `create_date`, `update_date`, `del_flag`, `sort`) VALUES ('1562973326504292353', '1560530192751071234', 'sys:resource:video:insert', '1', '视频新增', '/sys/resource/video/api/insert', '0', 'POST', 'plus', '1537114827246292994', NULL, '2022-08-26 09:20:54', '2022-08-26 09:20:54', '0', '10');
INSERT INTO `boot_sys_menu` (`id`, `pid`, `permissions`, `type`, `name`, `url`, `auth_level`, `method`, `icon`, `creator`, `editor`, `create_date`, `update_date`, `del_flag`, `sort`) VALUES ('1562973502765723650', '1560530192751071234', 'sys:resource:video:update', '1', '视频修改', '/sys/resource/video/api/update', '0', 'PUT', 'edit', '1537114827246292994', NULL, '2022-08-26 09:21:36', '2022-08-26 09:21:36', '0', '10');
INSERT INTO `boot_sys_menu` (`id`, `pid`, `permissions`, `type`, `name`, `url`, `auth_level`, `method`, `icon`, `creator`, `editor`, `create_date`, `update_date`, `del_flag`, `sort`) VALUES ('1562973726783500290', '1560530192751071234', 'sys:resource:video:delete', '1', '视频删除', '/sys/resource/video/api/delete', '0', 'DELETE', 'delete', '1537114827246292994', NULL, '2022-08-26 09:22:30', '2022-08-26 09:22:30', '0', '10');
INSERT INTO `boot_sys_menu` (`id`, `pid`, `permissions`, `type`, `name`, `url`, `auth_level`, `method`, `icon`, `creator`, `editor`, `create_date`, `update_date`, `del_flag`, `sort`) VALUES ('1562973963560349698', '1560530192751071234', 'sys:resource:video:diagram', '1', '视频流程图', '/sys/resource/video/api/diagram', '0', 'GET', 'gold', '1537114827246292994', NULL, '2022-08-26 09:23:26', '2022-08-26 09:23:26', '0', '10');
------------------------------------菜单------------------------------------

------------------------------------角色菜单------------------------------------
CREATE TABLE `boot_sys_role_menu` (
  `role_id` bigint(20) NOT NULL COMMENT '角色id',
  `menu_id` bigint(20) NOT NULL COMMENT '菜单id',
  KEY `role_id` (`role_id`) USING BTREE,
  KEY `menu_id` (`menu_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC COMMENT='角色-菜单';
------------------------------------角色菜单------------------------------------

------------------------------------角色------------------------------------
CREATE TABLE `boot_sys_role` (
  `id` bigint(20) NOT NULL COMMENT 'id',
  `name` varchar(50) NOT NULL COMMENT '角色名称',
  `creator` bigint(20) NOT NULL COMMENT '创建人',
  `editor` bigint(20) DEFAULT NULL COMMENT '编辑人',
  `create_date` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_date` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  `del_flag` tinyint(1) NOT NULL DEFAULT '0' COMMENT '1已删除 0未删除',
  `sort` int(11) DEFAULT '1' COMMENT '排序',
  `dept_id` bigint(20) DEFAULT NULL COMMENT '部门id',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC COMMENT='角色';
INSERT INTO `boot_sys_role` (`id`, `name`, `creator`, `editor`, `create_date`, `update_date`, `del_flag`, `sort`, `dept_id`) VALUES ('139167754288778856', '游客', '0', '1341620898007281665', '2021-11-27 17:11:15', '2022-07-27 22:55:25', '0', '10', '1535887940687765505');
INSERT INTO `boot_sys_role` (`id`, `name`, `creator`, `editor`, `create_date`, `update_date`, `del_flag`, `sort`, `dept_id`) VALUES ('139167754288778857', '管理员', '0', '1341620898007281665', '2021-11-27 17:11:19', '2022-07-27 22:55:26', '0', '100', '1535887940687765505');
INSERT INTO `boot_sys_role` (`id`, `name`, `creator`, `editor`, `create_date`, `update_date`, `del_flag`, `sort`, `dept_id`) VALUES ('1535949666183573505', '测试', '1341620898007281665', '1341620898007281665', '2022-06-12 19:38:32', '2022-07-27 22:55:26', '0', '50', '1535887940687765505');
------------------------------------角色------------------------------------

------------------------------------用户角色------------------------------------
CREATE TABLE `boot_sys_user_role` (
  `user_id` bigint(20) NOT NULL COMMENT '用户id',
  `role_id` bigint(20) NOT NULL COMMENT '角色id',
  KEY `user_id` (`user_id`) USING BTREE,
  KEY `role_id` (`role_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC COMMENT='用户-角色';
------------------------------------用户角色------------------------------------

------------------------------------用户------------------------------------
CREATE TABLE `boot_sys_user` (
  `id` bigint(20) NOT NULL COMMENT 'id',
  `username` varchar(20) NOT NULL COMMENT '用户名',
  `password` varchar(100) NOT NULL COMMENT '密码',
  `super_admin` tinyint(1) NOT NULL DEFAULT '0' COMMENT '超级管理员：0否 1是',
  `creator` bigint(20) DEFAULT NULL COMMENT '创建人',
  `editor` bigint(20) DEFAULT NULL COMMENT '编辑人',
  `create_date` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_date` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  `del_flag` tinyint(1) NOT NULL DEFAULT '0' COMMENT '1已删除 0未删除',
  `email` varchar(50) DEFAULT NULL COMMENT '电子邮箱',
  `status` tinyint(1) NOT NULL DEFAULT '1' COMMENT '状态 0正常 1停用',
  `img_url` varchar(400) NOT NULL DEFAULT 'https://gimg2.baidu.com/image_search/src=http%3A%2F%2Fi.qqkou.com%2Fi%2F1a3626475345x3078425090b26.jpg&refer=http%3A%2F%2Fi.qqkou.com&app=2002&size=f9999,10000&q=a80&n=0&g=0n&fmt=auto?sec=1657902941&t=73f98a243f12f3eabe1dce87d2b6401b' COMMENT '头像url',
  `mobile` varchar(11) DEFAULT NULL COMMENT '手机号',
  `zfb_openid` varchar(32) DEFAULT NULL COMMENT '支付宝用户唯一标识',
  PRIMARY KEY (`id`,`username`) USING BTREE,
  UNIQUE KEY `idx_email` (`email`) USING BTREE,
  UNIQUE KEY `idx_mobile` (`mobile`) USING BTREE,
  UNIQUE KEY `idx_zfb` (`zfb_openid`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC COMMENT='用户';
INSERT INTO `boot_sys_user` (`id`, `username`, `password`, `super_admin`, `creator`, `editor`, `create_date`, `update_date`, `del_flag`, `email`, `status`, `img_url`, `mobile`, `zfb_openid`, `dept_id`) VALUES ('1341620898007281665', 'admin', '$2a$10$n94v8BWjCb5zcvUwxxi9COKDS7Fn2n3WXbVs0Qeg5B1AkkagK1qO6', '1', '1341620898007281665', '1341620898007281665', '2021-11-29 20:13:11', '2022-07-27 22:10:26', '0', '2413176044@qq.com', '0', 'http://175.178.69.253/upload/node3/7904fff1c08a4883b40f1ee0336017dc.webp', '18974432576', '2088722720196501', '1535887940687765505');
INSERT INTO `boot_sys_user` (`id`, `username`, `password`, `super_admin`, `creator`, `editor`, `create_date`, `update_date`, `del_flag`, `email`, `status`, `img_url`, `mobile`, `zfb_openid`, `dept_id`) VALUES ('1341623527018004481', 'test', '$2a$10$cdEXE9uaSqkqoKa.73.XBuhHFPiOLomStfzUlmkuVqfA2wldqEfIG', '0', '1341620898007281665', '1341620898007281665', '2020-12-23 13:55:50', '2022-07-27 22:20:39', '0', '2314176044@qq.com', '0', 'http://175.178.69.253/upload/node2/b4e5bb3944a046a6bb54f8bfd2c830c1.webp', '18888888888', NULL, '1535881356595175426');
INSERT INTO `boot_sys_user` (`id`, `username`, `password`, `super_admin`, `creator`, `editor`, `create_date`, `update_date`, `del_flag`, `email`, `status`, `img_url`, `mobile`, `zfb_openid`, `dept_id`) VALUES ('1421312053736804354', 'koush5', '$2a$10$ysAmruc249SiAUpIqQzrpeM8wcdpgIJ6nEdtsXQnDrBgvLZkt7tJ6', '0', '1341620898007281665', '1341620898007281665', '2021-07-31 11:29:35', '2022-07-27 22:20:12', '0', 'YY@qq.com', '0', 'https://ruoyi.setworld.net/img/profile.473f5971.jpg', 'xxxxx', NULL, '1535881356595175426');
INSERT INTO `boot_sys_user` (`id`, `username`, `password`, `super_admin`, `creator`, `editor`, `create_date`, `update_date`, `del_flag`, `email`, `status`, `img_url`, `mobile`, `zfb_openid`, `dept_id`) VALUES ('1537111101311844353', 'wumh5', '$2a$10$KZHkKBF2jUGaOJDp4YcgeuVt1gBhteJC990OQWuOoRmyYQILBzQlG', '0', '1341620898007281665', '1341620898007281665', '2022-06-16 00:33:39', '2022-07-27 22:20:30', '0', NULL, '0', 'https://gimg2.baidu.com/image_search/src=http%3A%2F%2Fi.qqkou.com%2Fi%2F1a3626475345x3078425090b26.jpg&amp;refer=http%3A%2F%2Fi.qqkou.com&amp;app=2002&amp;size=f9999,10000&amp;q=a80&amp;n=0&amp;g=0n&amp;fmt=auto?sec=1657902941&amp;t=73f98a243f12f3eabe1dce87d2b6401b', NULL, NULL, '1535858679453085698');
INSERT INTO `boot_sys_user` (`id`, `username`, `password`, `super_admin`, `creator`, `editor`, `create_date`, `update_date`, `del_flag`, `email`, `status`, `img_url`, `mobile`, `zfb_openid`, `dept_id`) VALUES ('1537114827246292994', 'laok5', '$2a$10$b.40TGb7W19z5Jryo3FBuOEDaX2c0OAqZHnRnCkXCPI67ru5G7Nha', '0', '1341620898007281665', '1341620898007281665', '2022-06-16 00:48:28', '2022-07-27 22:20:23', '0', NULL, '0', 'https://gimg2.baidu.com/image_search/src=http%3A%2F%2Fi.qqkou.com%2Fi%2F1a3626475345x3078425090b26.jpg&amp;refer=http%3A%2F%2Fi.qqkou.com&amp;app=2002&amp;size=f9999,10000&amp;q=a80&amp;n=0&amp;g=0n&amp;fmt=auto?sec=1657902941&amp;t=73f98a243f12f3eabe1dce87d2b6401b', NULL, NULL, '1535881356595175426');
------------------------------------用户------------------------------------

------------------------------------日志------------------------------------
CREATE TABLE `boot_sys_operate_log` (
  `id` bigint(20) NOT NULL COMMENT 'id',
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
  `update_date` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  `del_flag` tinyint(1) NOT NULL DEFAULT '0' COMMENT '1已删除 0未删除',
  `editor` bigint(20) DEFAULT NULL COMMENT '编辑人',
  `dept_id` bigint(20) DEFAULT NULL COMMENT '部门id',
  PRIMARY KEY (`id`),
  KEY `idx_module` (`module`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='操作日志';
CREATE TABLE `boot_sys_login_log` (
  `id` bigint(20) NOT NULL COMMENT 'id',
  `creator` bigint(20) DEFAULT NULL COMMENT '创建者',
  `create_date` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_date` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  `del_flag` tinyint(1) NOT NULL DEFAULT '0' COMMENT '1已删除 0未删除',
  `editor` bigint(20) DEFAULT NULL COMMENT '编辑人',
  `login_name` varchar(200) DEFAULT NULL COMMENT '登录用户',
  `request_ip` varchar(50) DEFAULT NULL COMMENT 'IP地址',
  `request_address` varchar(200) DEFAULT NULL COMMENT '归属地',
  `browser` varchar(50) DEFAULT NULL COMMENT '浏览器',
  `os` varchar(50) DEFAULT NULL COMMENT '操作系统',
  `request_status` tinyint(1) unsigned NOT NULL COMMENT '状态  0：成功   1：失败',
  `msg` varchar(500) DEFAULT NULL COMMENT '提示信息',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='登录日志';
------------------------------------日志------------------------------------

------------------------------------字典------------------------------------
CREATE TABLE `boot_sys_dict` (
  `id` bigint(20) NOT NULL COMMENT 'id',
  `creator` bigint(20) DEFAULT NULL COMMENT '创建者',
  `create_date` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_date` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  `del_flag` tinyint(1) NOT NULL DEFAULT '0' COMMENT '1已删除 0未删除',
  `editor` bigint(20) DEFAULT NULL COMMENT '编辑人',
  `dict_value` text COMMENT '值',
  `dict_label` varchar(100) DEFAULT NULL COMMENT '标签',
  `type` varchar(100) DEFAULT NULL COMMENT '类型',
  `status` tinyint(1) DEFAULT NULL COMMENT '状态 0正常 1停用',
  `remark` varchar(500) DEFAULT NULL COMMENT '备注',
  `sort` int(11) DEFAULT '1' COMMENT '排序',
  `dept_id` bigint(20) DEFAULT NULL COMMENT '部门id',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='字典';
------------------------------------字典------------------------------------

------------------------------------支付宝用户------------------------------------
CREATE TABLE `boot_zfb_user` (
  `id` bigint(20) NOT NULL COMMENT 'id',
  `creator` bigint(20) DEFAULT NULL COMMENT '创建人',
  `editor` bigint(20) DEFAULT NULL COMMENT '编辑人',
  `create_date` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_date` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  `del_flag` tinyint(1) NOT NULL DEFAULT '0' COMMENT '1已删除 0未删除',
  `openid` varchar(32) NOT NULL COMMENT '支付宝用户唯一标识',
  `gender` varchar(1) DEFAULT NULL COMMENT '性别',
  `province` varchar(10) DEFAULT NULL COMMENT '省份',
  `city` varchar(10) DEFAULT NULL COMMENT '城市',
  `avatar` varchar(400) DEFAULT NULL COMMENT '头像',
  PRIMARY KEY (`id`,`openid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='支付宝用户';
------------------------------------支付宝用户------------------------------------

------------------------------------微信公众号------------------------------------
CREATE TABLE `boot_wx_mp_account` (
  `id` bigint(20) NOT NULL COMMENT 'id',
  `name` varchar(100) DEFAULT NULL COMMENT '名称',
  `app_id` varchar(100) DEFAULT NULL COMMENT 'AppID',
  `app_secret` varchar(100) DEFAULT NULL COMMENT 'AppSecret',
  `token` varchar(100) DEFAULT NULL COMMENT 'Token',
  `aes_key` varchar(100) DEFAULT NULL COMMENT 'EncodingAESKey',
  `creator` bigint(20) DEFAULT NULL COMMENT '创建人',
  `editor` bigint(20) DEFAULT NULL COMMENT '编辑人',
  `create_date` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_date` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  `del_flag` tinyint(1) NOT NULL DEFAULT '0' COMMENT '1已删除 0未删除',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC COMMENT='微信公众号账号';
CREATE TABLE `boot_wx_mp_menu` (
  `id` bigint(20) NOT NULL COMMENT 'id',
  `menu` varchar(2000) DEFAULT NULL COMMENT '菜单json数据',
  `app_id` varchar(100) DEFAULT NULL COMMENT 'AppID',
  `creator` bigint(20) DEFAULT NULL COMMENT '创建人',
  `editor` bigint(20) DEFAULT NULL COMMENT '编辑人',
  `create_date` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_date` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  `del_flag` tinyint(1) NOT NULL DEFAULT '0' COMMENT '1已删除 0未删除',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `uk_app_id` (`app_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC COMMENT='微信公众号自定义菜单';
------------------------------------微信公众号------------------------------------

------------------------------------消息------------------------------------
CREATE TABLE `boot_sys_message` (
  `id` bigint(20) NOT NULL COMMENT 'id',
  `username` varchar(20) CHARACTER SET utf8 DEFAULT NULL COMMENT '发送用户',
  `title` varchar(400) DEFAULT NULL COMMENT '标题',
  `content` longtext COMMENT '内容',
  `send_channel` tinyint(1) DEFAULT NULL COMMENT '发送渠道：0平台 1微信公众号 2邮箱',
  `creator` bigint(20) DEFAULT NULL COMMENT '创建人',
  `editor` bigint(20) DEFAULT NULL COMMENT '编辑人',
  `create_date` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_date` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  `del_flag` tinyint(1) NOT NULL DEFAULT '0' COMMENT '1已删除 0未删除',
  `dept_id` bigint(20) DEFAULT NULL COMMENT '部门id',
  `type` tinyint(2) DEFAULT '0' COMMENT '0通知 1提醒',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='消息';
CREATE TABLE `boot_sys_message_detail` (
  `id` bigint(20) NOT NULL COMMENT 'id',
  `message_id` bigint(20) DEFAULT NULL COMMENT '消息id',
  `user_id` bigint(20) DEFAULT NULL COMMENT '用户id',
  `creator` bigint(20) DEFAULT NULL COMMENT '创建人',
  `editor` bigint(20) DEFAULT NULL COMMENT '编辑人',
  `create_date` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_date` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  `del_flag` tinyint(1) NOT NULL DEFAULT '0' COMMENT '1已删除 0未删除',
  `read_flag` tinyint(1) DEFAULT '0' COMMENT '是否已读 0未读 1已读',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='消息详情';
------------------------------------消息------------------------------------

------------------------------------部门------------------------------------
CREATE TABLE `boot_sys_dept` (
  `id` bigint(20) NOT NULL COMMENT 'id',
  `pid` bigint(20) NOT NULL COMMENT '父节点',
  `name` varchar(100) DEFAULT NULL COMMENT '名称',
  `sort` int(11) DEFAULT '1' COMMENT '排序',
  `creator` bigint(20) DEFAULT NULL COMMENT '创建人',
  `editor` bigint(20) DEFAULT NULL COMMENT '编辑人',
  `create_date` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_date` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  `del_flag` tinyint(1) DEFAULT '0' COMMENT '1已删除 0未删除',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='部门';
INSERT INTO `boot_sys_dept` (`id`, `pid`, `name`, `sort`, `creator`, `editor`, `create_date`, `update_date`, `del_flag`) VALUES ('1535858679453085698', '1535887940687765505', '广州分公司', '666', '1341620898007281665', '1341620898007281665', '2022-07-27 20:50:20', '2022-07-27 20:50:18', '0');
INSERT INTO `boot_sys_dept` (`id`, `pid`, `name`, `sort`, `creator`, `editor`, `create_date`, `update_date`, `del_flag`) VALUES ('1535881356595175426', '1535887940687765505', '长沙分公司', '111', '1341620898007281665', '1341620898007281665', '2022-07-27 20:50:26', '2022-07-27 20:50:25', '0');
INSERT INTO `boot_sys_dept` (`id`, `pid`, `name`, `sort`, `creator`, `editor`, `create_date`, `update_date`, `del_flag`) VALUES ('1535887129341599746', '1535887940687765505', '深圳分公司', '888', '1341620898007281665', '1341620898007281665', '2022-07-27 20:50:12', '2022-07-27 20:50:10', '0');
INSERT INTO `boot_sys_dept` (`id`, `pid`, `name`, `sort`, `creator`, `editor`, `create_date`, `update_date`, `del_flag`) VALUES ('1535887940687765505', '0', '老寇云集团', '1000', '1341620898007281665', '1341620898007281665', '2022-07-27 20:48:56', '2022-07-27 20:48:55', '0');
------------------------------------部门------------------------------------

------------------------------------角色部门------------------------------------
CREATE TABLE `boot_sys_role_dept` (
  `role_id` bigint(20) NOT NULL COMMENT '角色id',
  `dept_id` bigint(20) NOT NULL COMMENT '部门id',
  KEY `role_id` (`role_id`) USING BTREE,
  KEY `menu_id` (`dept_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC COMMENT='角色-部门';
------------------------------------角色部门------------------------------------

------------------------------------认证------------------------------------
CREATE TABLE `boot_sys_oauth_client_details` (
  `id` bigint(20) NOT NULL COMMENT 'id',
  `client_id` varchar(128) DEFAULT NULL COMMENT '客户端id',
  `resource_ids` varchar(128) DEFAULT NULL COMMENT '资源ids',
  `client_secret` varchar(128) DEFAULT NULL COMMENT '客户端密钥',
  `scope` varchar(128) DEFAULT NULL COMMENT '授权范围',
  `authorized_grant_types` varchar(128) DEFAULT NULL COMMENT '授权类型',
  `web_server_redirect_uri` varchar(128) DEFAULT NULL COMMENT '回调地址',
  `authorities` varchar(128) DEFAULT NULL COMMENT '权限标识',
  `access_token_validity` int(11) DEFAULT NULL COMMENT '访问令牌有效期',
  `refresh_token_validity` int(11) DEFAULT NULL COMMENT '刷新令牌有效期',
  `additional_information` varchar(4096) DEFAULT NULL COMMENT '附加信息',
  `autoapprove` varchar(128) DEFAULT NULL COMMENT '自动授权',
  `creator` bigint(20) DEFAULT NULL COMMENT '创建人',
  `editor` bigint(20) DEFAULT NULL COMMENT '编辑人',
  `create_date` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_date` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  `del_flag` tinyint(1) DEFAULT '0' COMMENT '1已删除 0未删除',
  `dept_id` bigint(20) DEFAULT NULL COMMENT '部门id',
  `sort` int(11) DEFAULT NULL COMMENT '排序',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='oauth客户端令牌';
CREATE TABLE `oauth_code` (
  `code` varchar(128) DEFAULT NULL,
  `authentication` blob
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='oauth授权码';
INSERT INTO `boot_sys_oauth_client_details` (`id`, `client_id`, `resource_ids`, `client_secret`, `scope`, `authorized_grant_types`, `web_server_redirect_uri`, `authorities`, `access_token_validity`, `refresh_token_validity`, `additional_information`, `autoapprove`, `creator`, `editor`, `create_date`, `update_date`, `del_flag`, `dept_id`, `sort`) VALUES ('1421312053736804354', 'client_auth', NULL, 'secret', 'all', 'authorization_code,client_credentials,implicit,refresh_token,password', 'https://localhost:8080', NULL, '0', '86400', NULL, 'false', '1341620898007281665', '1341620898007281665', '2022-08-11 20:47:51', '2022-08-11 20:47:51', '0', '1535887940687765505', '999');
------------------------------------认证------------------------------------

------------------------------------资源------------------------------------
CREATE TABLE `boot_sys_resource` (
  `id` bigint(20) NOT NULL COMMENT 'id',
  `title` varchar(200) NOT NULL COMMENT '名称',
  `author` varchar(100) NOT NULL DEFAULT 'admin' COMMENT '作者',
  `uri` varchar(500) NOT NULL COMMENT '地址',
  `status` tinyint(1) NOT NULL DEFAULT '0' COMMENT '审核状态(0待审核 1审核中 2审批结束)',
  `code` varchar(10) NOT NULL COMMENT '类型 audio音频 video视频  image图片 text文本 other其他',
  `create_date` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_date` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  `remark` longtext COMMENT '备注',
  `creator` bigint(20) DEFAULT NULL COMMENT '创建人',
  `editor` bigint(20) DEFAULT NULL COMMENT '编辑人',
  `del_flag` tinyint(1) NOT NULL DEFAULT '0' COMMENT '1已删除 0未删除',
  `tags` longtext COMMENT '标签',
  `md5` varchar(50) DEFAULT NULL COMMENT '唯一标识',
  `process_instance_id` varchar(50) DEFAULT NULL COMMENT '实例id',
  PRIMARY KEY (`id`,`create_date`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC COMMENT='资源'
PARTITION BY RANGE ( UNIX_TIMESTAMP(`create_date`))
(PARTITION lk202110 VALUES LESS THAN (1633017600) ENGINE = InnoDB,
 PARTITION lk202111 VALUES LESS THAN (1635696000) ENGINE = InnoDB,
 PARTITION lk202112 VALUES LESS THAN (1638288000) ENGINE = InnoDB,
 PARTITION lk202203 VALUES LESS THAN (1646064000) ENGINE = InnoDB,
 PARTITION lk202204 VALUES LESS THAN (1648742400) ENGINE = InnoDB,
 PARTITION lk202205 VALUES LESS THAN (1651334400) ENGINE = InnoDB,
 PARTITION lk202206 VALUES LESS THAN (1654012800) ENGINE = InnoDB,
 PARTITION lk202207 VALUES LESS THAN (1656604800) ENGINE = InnoDB,
 PARTITION lk202208 VALUES LESS THAN (1659283200) ENGINE = InnoDB,
 PARTITION lk202209 VALUES LESS THAN (1661961600) ENGINE = InnoDB,
 PARTITION lk202210 VALUES LESS THAN (1664553600) ENGINE = InnoDB,
 PARTITION lk202211 VALUES LESS THAN (1667232000) ENGINE = InnoDB,
 PARTITION lk202212 VALUES LESS THAN (1669824000) ENGINE = InnoDB);
INSERT INTO `boot_sys_resource` (`id`, `title`, `author`, `uri`, `status`, `code`, `create_date`, `update_date`, `remark`, `creator`, `editor`, `del_flag`, `tags`, `md5`, `process_instance_id`) VALUES ('1429355654328815617', '大籽 - 白月光与朱砂痣.mp3', 'admin', 'http://175.178.69.253:81/upload/node4/f906b6a282564c559632a1beeb449f5f.mp3', '3', 'audio', '2021-10-21 13:05:09', '2022-08-26 10:43:30', '《白月光与朱砂痣》是由大籽、嘿人李逵演唱的歌曲，收录于2021年1月1日发行的《白月光与朱砂痣》专辑。', '1341620898007281665', '1341620898007281665', '0', '大籽', 'b683aa12313835fb780d10866c00942b', '1296a870-24e4-11ed-b537-525400cf57fe');
INSERT INTO `boot_sys_resource` (`id`, `title`, `author`, `uri`, `status`, `code`, `create_date`, `update_date`, `remark`, `creator`, `editor`, `del_flag`, `tags`, `md5`, `process_instance_id`) VALUES ('1429355954762616834', '王胜男_花粥 - 出山.mp3', 'admin', 'http://175.178.69.253:81/upload/node1/ebd577c32a8d448c8349af779d36110a.mp3', '3', 'audio', '2021-10-21 13:05:09', '2022-08-26 10:39:43', '《出山》是由花粥作词、作曲，王胜男和花粥演唱的歌曲，正式发行于2018年9月28日。背景伴奏原曲是Bachbeats创作的《Super Love》。', '1341620898007281665', '1341620898007281665', '0', '王胜男,花粥', 'e2ecdf590fd0fcb3dc18fddca2efa84f', 'a4d10ab8-24e4-11ed-b537-525400cf57fe');
INSERT INTO `boot_sys_resource` (`id`, `title`, `author`, `uri`, `status`, `code`, `create_date`, `update_date`, `remark`, `creator`, `editor`, `del_flag`, `tags`, `md5`, `process_instance_id`) VALUES ('1429355987293638657', '艾辰 - 错位时空.mp3', 'admin', 'http://175.178.69.253:81/upload/node2/a673b6697e4142e5b24e5347b2b32fe8.mp3', '3', 'audio', '2021-10-21 13:05:09', '2022-08-26 10:39:50', '《错位时空》是周仁作词，张博文作曲，艾辰演唱的歌曲，由网易云音乐飓风工作室出品，于2021年1月1日发行。', '1341620898007281665', '1341620898007281665', '0', '艾辰', '7d7cdcbd02f9f3a260166b96bd2589a7', '8a148558-24e3-11ed-b537-525400cf57fe');
INSERT INTO `boot_sys_resource` (`id`, `title`, `author`, `uri`, `status`, `code`, `create_date`, `update_date`, `remark`, `creator`, `editor`, `del_flag`, `tags`, `md5`, `process_instance_id`) VALUES ('1429356225781764097', '周杰伦 - 告白气球.mp3', 'admin', 'http://175.178.69.253:81/upload/node4/33e764e16a3f43dfa720967f8a95b9d8.mp3', '3', 'audio', '2021-10-21 13:05:09', '2022-08-26 10:40:04', '《告白气球》是周杰伦演唱的一首歌曲，由方文山作词，周杰伦作曲，林迈可编曲，收录于周杰伦2016年6月24日发行的录音室专辑《周杰伦的床边故事》中。\n2017年1月，这首歌曲获得Billboard Radio China 2016年度十大金曲奖；同年，该歌曲入围了第28届金曲奖最佳年度歌曲奖。', '1341620898007281665', '1341620898007281665', '0', '周杰伦', '5ba8f31a24deac3b8aeb0c501e455d21', '3f6457a8-24e3-11ed-b537-525400cf57fe');
INSERT INTO `boot_sys_resource` (`id`, `title`, `author`, `uri`, `status`, `code`, `create_date`, `update_date`, `remark`, `creator`, `editor`, `del_flag`, `tags`, `md5`, `process_instance_id`) VALUES ('1429356501397868545', '丸子呦 - 广寒宫.mp3', 'admin', 'http://175.178.69.253:81/upload/node4/c62266fb46004a96954f1c88e57999ea.mp3', '3', 'audio', '2021-10-21 13:05:09', '2022-08-26 10:39:56', '《广寒宫》是2019年4月30日丸子哟发布演唱的古风歌曲。作曲 : 刘凤瑶作词 : 蒋柽，编曲：侯贻强 / EZ-Mitchell。', '1341620898007281665', '1341620898007281665', '0', '丸子呦', '4a7d4a6b11a2dde997033e51b135b41f', '5a8a1c50-24e3-11ed-b537-525400cf57fe');
INSERT INTO `boot_sys_resource` (`id`, `title`, `author`, `uri`, `status`, `code`, `create_date`, `update_date`, `remark`, `creator`, `editor`, `del_flag`, `tags`, `md5`, `process_instance_id`) VALUES ('1429356560529166337', '胡歌 - 逍遥叹.mp3', 'admin', 'http://175.178.69.253:81/upload/node4/9e18868a6555416aa6b8c50eb24f25d4.mp3', '3', 'audio', '2021-10-21 13:05:09', '2022-08-26 10:43:24', '《逍遥叹》是电视剧《仙剑奇侠传》的插曲，由陈宇任（Funck）作词、作曲，屠颖编曲，郭文宗制作，胡歌演唱，收录在华研国际音乐2005年1月25日发行的电视剧《仙剑奇侠传》原声带中 。', '1341620898007281665', '1341620898007281665', '0', '胡歌', '1415e6eab8e8b0538f5fb66526c9eb65', 'fee32b98-24e3-11ed-b537-525400cf57fe');
INSERT INTO `boot_sys_resource` (`id`, `title`, `author`, `uri`, `status`, `code`, `create_date`, `update_date`, `remark`, `creator`, `editor`, `del_flag`, `tags`, `md5`, `process_instance_id`) VALUES ('1429356593961963521', '要不要买菜 - 下山.mp3', 'admin', 'http://175.178.69.253:81/upload/node3/b66ce02154434655b43cd82eb20354f5.mp3', '3', 'audio', '2021-10-21 13:05:09', '2022-08-26 10:38:08', '《下山》是由朱斌语作词作曲，要不要买菜演唱的歌曲，发行于2019年11月22日。\n2021年1月23日，该曲获得第二届腾讯音乐娱乐盛典年度十大热歌奖。', '1341620898007281665', '1341620898007281665', '0', '要不要买菜', '2820dd7a1d40b299d3e532b1305732aa', 'cae93370-24e3-11ed-b537-525400cf57fe');
INSERT INTO `boot_sys_resource` (`id`, `title`, `author`, `uri`, `status`, `code`, `create_date`, `update_date`, `remark`, `creator`, `editor`, `del_flag`, `tags`, `md5`, `process_instance_id`) VALUES ('1429356687457193986', '陈一发儿 - 童话镇.mp3', 'admin', 'http://175.178.69.253:81/upload/node5/99e4a13f475144c7b0a984dc50f67a84.mp3', '3', 'audio', '2021-10-21 13:05:09', '2022-08-26 10:36:01', '《童话镇》是由竹君作词，暗杠作曲，陈一发儿翻唱的流行歌曲。该曲女声版本在云音乐热歌榜连续两周榜首，并最终入选2016年度云音乐最热歌曲第五名，成为陈一发儿的代表作之一。', '1341620898007281665', '1341620898007281665', '0', '陈一发儿', 'ed35367bd830d24073cce802c075c6ad', '92650200-24e4-11ed-b537-525400cf57fe');
INSERT INTO `boot_sys_resource` (`id`, `title`, `author`, `uri`, `status`, `code`, `create_date`, `update_date`, `remark`, `creator`, `editor`, `del_flag`, `tags`, `md5`, `process_instance_id`) VALUES ('1429356715475144706', '汪睿 - 桃花笑.mp3', 'admin', 'http://175.178.69.253:81/upload/node4/424ca662e7d84e8486eb5bac29f858f0.mp3', '3', 'audio', '2021-10-21 13:05:09', '2022-08-26 10:36:08', '《桃花笑》为林依晨、张彬彬领衔主演的电视剧《小女花不弃》中的插曲，由汪睿演唱，由青萝子作词，西门振作曲。该歌曲温馨喜庆，旋律轻快欢乐，歌词郎朗上口，配上汪睿独特可爱的嗓音，好听又为剧情发展烘托气氛。', '1341620898007281665', '1341620898007281665', '0', '汪睿', '908f7591b72113ebede5c4dd0ba89dcc', '846e54c8-24e4-11ed-b537-525400cf57fe');
INSERT INTO `boot_sys_resource` (`id`, `title`, `author`, `uri`, `status`, `code`, `create_date`, `update_date`, `remark`, `creator`, `editor`, `del_flag`, `tags`, `md5`, `process_instance_id`) VALUES ('1429356956618264577', '银临 - 棠梨煎雪.mp3', 'admin', 'http://175.178.69.253:81/upload/node5/3db5f8ab95cc45debbac3238b2178336.mp3', '3', 'audio', '2021-10-21 13:05:09', '2022-08-26 10:39:12', '《棠梨煎雪》是一首银临演唱的古风歌曲，收录于2013年12月15日发行的专辑《腐草为萤》', '1341620898007281665', '1341620898007281665', '0', '银临', 'e68e545e1d6b34cecdbd4edaac1e99a0', '9b3856a0-24e3-11ed-b537-525400cf57fe');
INSERT INTO `boot_sys_resource` (`id`, `title`, `author`, `uri`, `status`, `code`, `create_date`, `update_date`, `remark`, `creator`, `editor`, `del_flag`, `tags`, `md5`, `process_instance_id`) VALUES ('1429358833586081793', '蔡依林 - 日不落.mp3', 'admin', 'http://175.178.69.253:81/upload/node3/bea227b2276e4ff08ba80c78bb135c28.mp3', '3', 'audio', '2021-10-21 13:05:09', '2022-08-26 10:38:17', '《日不落》是中国台湾流行音乐女歌手蔡依林演唱的一首歌曲，翻唱自BWO的《Sunshine in the Rain》，由崔惟楷重新填词，Bodies Without Organs作曲，收录于蔡依林2007年09月21日发行的专辑《特务J》中，亦是音乐电影《记忆的裂痕》中的背景音乐。\n该歌曲发行后获得第六届HITO流行音乐奖年度听众最爱歌曲、第十五届北京流行音乐典礼年度金曲和第八届全球华语歌曲排行榜最受欢迎二十大金曲等奖项。2008年1月《日不落》获得中国台湾Hit Fm百首单曲冠军。', '1341620898007281665', '1341620898007281665', '0', '蔡依林', '5dfa21b06e27e3a4f14002dbe3f9ec08', 'b5ca3fb8-24e3-11ed-b537-525400cf57fe');
INSERT INTO `boot_sys_resource` (`id`, `title`, `author`, `uri`, `status`, `code`, `create_date`, `update_date`, `remark`, `creator`, `editor`, `del_flag`, `tags`, `md5`, `process_instance_id`) VALUES ('1429358893971476482', '深海鱼子酱 - 千千万万.mp3', 'admin', 'http://175.178.69.253:81/upload/node1/e74d47b36a3b47f99da3bc1ca23320ff.mp3', '3', 'audio', '2021-10-21 13:05:09', '2022-08-26 10:39:05', '《千千万万》是深海鱼子酱演唱的歌曲，发行于2022年1月13日，收录于专辑《千千万万(粤语版)》中。', '1341620898007281665', '1341620898007281665', '0', '深海鱼子酱', 'e49ccc37c42661f1d38dab8b11f554d7', 'c723f7e8-24e4-11ed-b537-525400cf57fe');
INSERT INTO `boot_sys_resource` (`id`, `title`, `author`, `uri`, `status`, `code`, `create_date`, `update_date`, `remark`, `creator`, `editor`, `del_flag`, `tags`, `md5`, `process_instance_id`) VALUES ('1429358962762256386', '流芒菌 - 红昭愿.mp3', 'admin', 'http://175.178.69.253:81/upload/node4/fa9aa526d024481b9c1c5cbdc5ae2ab0.mp3', '3', 'audio', '2021-10-21 13:05:09', '2022-08-26 10:38:24', '《红昭愿》是来自音阙诗听音乐社的一首原创作品，由荒唐客作词，殇小瑾作曲，流茫菌演唱的一首歌。正式发行于2017年1月11日。大量的不重复处理让歌曲呈现更高的耐听感，时时刻刻都是新鲜的元素！据社长也亦是《红昭愿》这首歌的制作人殇小谨介绍：“魔性的开场loop人声采样，是我们的第七版创意。和声的配唱模式，是我们的第三版创意。编曲的整体搭配，包括中国风古筝元素的加入，是我们的第三版创意。”', '1341620898007281665', '1341620898007281665', '0', '流芒菌', '76792d34cd28decff96bef5f93e8c08b', '0730c210-24e5-11ed-b537-525400cf57fe');
INSERT INTO `boot_sys_resource` (`id`, `title`, `author`, `uri`, `status`, `code`, `create_date`, `update_date`, `remark`, `creator`, `editor`, `del_flag`, `tags`, `md5`, `process_instance_id`) VALUES ('1429359285337788418', '张韶涵 - 淋雨一直走.mp3', 'admin', 'http://175.178.69.253:81/upload/node2/fc4f4b3713174e57a9e88cc55e9d009f.mp3', '3', 'audio', '2021-10-21 13:05:09', '2022-08-26 10:42:56', '《淋雨一直走》是张韶涵演唱的一首歌曲，收录在张韶涵发行的专辑《有形的翅膀》中。\n2012年12月这首歌曲获得第20届中国歌曲排行榜年度金曲奖和最佳编曲奖，2013年4月获得Music Radio中国TOP排行榜年度金曲奖。', '1341620898007281665', '1341620898007281665', '0', '张韶涵', 'c3d162ca21a65f91ceaa4784107d4ab4', '1b788fa8-24e5-11ed-b537-525400cf57fe');
INSERT INTO `boot_sys_resource` (`id`, `title`, `author`, `uri`, `status`, `code`, `create_date`, `update_date`, `remark`, `creator`, `editor`, `del_flag`, `tags`, `md5`, `process_instance_id`) VALUES ('1429360419737636866', '花僮 - 笑纳.mp3', 'admin', 'http://175.178.69.253:81/upload/node4/1ea804cfdda948ae8901b7d7cd670cb8.mp3', '3', 'audio', '2021-10-21 13:05:09', '2022-08-26 10:39:35', '《笑纳》是周仁作词，古月作曲，韩珂编曲，花僮演唱的歌曲，发行于2020年8月26日。收录于同名专辑《笑纳》中。', '1341620898007281665', '1341620898007281665', '0', '花僮', 'f44502ce78a2c453e5a0cab7cd8eb3ce', 'b6f58320-24e4-11ed-b537-525400cf57fe');
INSERT INTO `boot_sys_resource` (`id`, `title`, `author`, `uri`, `status`, `code`, `create_date`, `update_date`, `remark`, `creator`, `editor`, `del_flag`, `tags`, `md5`, `process_instance_id`) VALUES ('1431054811305213953', '许嵩 - 如果当时.mp3', 'admin', 'http://175.178.69.253:81/upload/node5/40ca5064ce1e4df594ab7bbfe53b7b8d.mp3', '3', 'audio', '2021-10-21 13:05:09', '2022-08-26 10:40:11', '《如果当时》是许嵩首张个人专辑《自定义》的一首主打歌曲，创作工作均由许嵩独自完成。发行于2009年1月10日。\n这首歌是一首构思独特的养耳之作，具有浓厚的中国风味道。', '1341620898007281665', '1341620898007281665', '0', '许嵩', '9ebc503d394632b45441e2a48df855a8', '253dda90-24e3-11ed-b537-525400cf57fe');
INSERT INTO `boot_sys_resource` (`id`, `title`, `author`, `uri`, `status`, `code`, `create_date`, `update_date`, `remark`, `creator`, `editor`, `del_flag`, `tags`, `md5`, `process_instance_id`) VALUES ('1441610165923516417', '黎林添娇 - 星月糖.mp3', 'admin', 'http://175.178.69.253:81/upload/node4/bed7739d71414a95a43694a20ec69e9a.mp3', '3', 'audio', '2021-10-21 13:05:09', '2022-08-26 10:43:57', '《星月糖》是漠飞作词，王鹏作曲，黎林添娇演唱的一首歌。', '1341620898007281665', '1341620898007281665', '0', '黎林添娇', 'd26ed58ff7884a115a28af3b20197346', '22349d08-24e4-11ed-b537-525400cf57fe');
INSERT INTO `boot_sys_resource` (`id`, `title`, `author`, `uri`, `status`, `code`, `create_date`, `update_date`, `remark`, `creator`, `editor`, `del_flag`, `tags`, `md5`, `process_instance_id`) VALUES ('1441610450502848513', 'yihuik苡慧 - 银河与星斗.mp3', 'admin', 'http://175.178.69.253:81/upload/node5/f96ff9b14ce94f8e8746ef8738614fcd.mp3', '3', 'audio', '2021-10-21 13:05:09', '2022-08-26 10:37:41', '《银河与星斗》是由李昀格和刘思情作词，轮子作曲，yihuik苡慧演唱的歌曲，发行于2021年5月30日。收录于同名专辑《银河与星斗》中。', '1341620898007281665', '1341620898007281665', '0', 'yihuik苡慧', '72d34631c6c38eff907af3a137a87f98', '35e1fe60-24e4-11ed-b537-525400cf57fe');
INSERT INTO `boot_sys_resource` (`id`, `title`, `author`, `uri`, `status`, `code`, `create_date`, `update_date`, `remark`, `creator`, `editor`, `del_flag`, `tags`, `md5`, `process_instance_id`) VALUES ('1441610450502848514', '黄霑_罗大佑_徐克 - 沧海一声笑.mp4', 'admin', 'http://175.178.69.253:81/upload/node4/108f3fe556a54faea00c0af69ff7a832.mp4', '3', 'video', '2021-10-24 14:55:43', '2022-08-26 10:43:43', '《沧海一声笑》是1990年上映的电影《笑傲江湖》的主题曲，由黄霑作词、作曲，顾嘉辉编曲。\n歌曲分粤语、普通话两个版本，但歌词几乎相同；粤语版由许冠杰演唱，收录于许冠杰1990年4月1日由宝丽金唱片发行的专辑《90电影金曲精选》中；普通话版由罗大佑、黄霑、徐克共同演唱，收录于黄沾1990年由滚石唱片发行的专辑《笑傲江湖-百无禁忌黄沾作品集》中。\n1991年，该曲获第10届香港电影金像奖最佳原创电影歌曲奖。', '1341620898007281665', '1341620898007281665', '0', '黄霑,罗大佑,徐克', '6fab31bd30385769f306b417f190318c', '79dd2488-24e5-11ed-b537-525400cf57fe');
INSERT INTO `boot_sys_resource` (`id`, `title`, `author`, `uri`, `status`, `code`, `create_date`, `update_date`, `remark`, `creator`, `editor`, `del_flag`, `tags`, `md5`, `process_instance_id`) VALUES ('1441610450502848515', '令狐冲 - 一曲肝肠断.jpg', 'admin', 'http://175.178.69.253:81/upload/node3/bacb44ff9a5f4ba9bb7eefd7adc45a47.jpg', '3', 'image', '2021-10-24 15:07:54', '2022-08-26 10:38:57', '老寇的QQ个人头像', '1341620898007281665', '1341620898007281665', '0', '令狐冲', 'e0c88f125b536e826ff749a791b1ddac', 'a9c9c983-24ce-11ed-9e12-005056c00001');
INSERT INTO `boot_sys_resource` (`id`, `title`, `author`, `uri`, `status`, `code`, `create_date`, `update_date`, `remark`, `creator`, `editor`, `del_flag`, `tags`, `md5`, `process_instance_id`) VALUES ('1441610450502848516', '郑中基 - 无赖.mp3', 'admin', 'http://175.178.69.253:81/upload/node2/0cacaed1d0e24dd8bf783d6126981cd3.mp3', '3', 'audio', '2021-10-24 15:33:48', '2022-08-26 10:43:50', '《无赖》是郑中基演唱的一首粤语歌曲，由李峻一填词、作曲，收录在郑中基2005年6月由金牌大风唱片发行的粤语专辑《Before After》中。该歌曲是电影《龙咁威2之皇母娘娘呢》的主题曲。\n2005年12月该歌曲获得新城劲爆歌曲、劲爆卡拉OK歌曲两个奖项；2006年获得第28届香港十大中文金曲、2005年度十大劲歌金曲金曲奖。', '1341620898007281665', '1341620898007281665', '0', '郑中基', 'b54ae499c5f2bea0f7a21adde160f9f3', '127dbe68-24e3-11ed-b537-525400cf57fe');
INSERT INTO `boot_sys_resource` (`id`, `title`, `author`, `uri`, `status`, `code`, `create_date`, `update_date`, `remark`, `creator`, `editor`, `del_flag`, `tags`, `md5`, `process_instance_id`) VALUES ('1441610450502848520', '李昕融_樊桐舟_李凯稠 - 你笑起来真好看.mp3', 'admin', 'http://175.178.69.253:81/upload/node1/6f159e4ade6340cfa877508f28457289.mp3', '3', 'audio', '2021-10-26 08:39:20', '2022-08-26 10:43:17', '《你笑起来真好看》是由周兵作词，李凯稠作曲，李昕融、樊桐舟、李凯稠共同演唱的歌曲，于2020年10月26日以单曲形式发行。\n2020年，该曲获得中国当代歌曲创作精品工程“听见中国听见你”年度优秀歌曲奖。', '1341620898007281665', '1341620898007281665', '0', '李昕融,樊桐舟,李凯稠', 'f8538982306946651aac48f29c9d647d', 'f7cd9ef0-24e2-11ed-b537-525400cf57fe');
INSERT INTO `boot_sys_resource` (`id`, `title`, `author`, `uri`, `status`, `code`, `create_date`, `update_date`, `remark`, `creator`, `editor`, `del_flag`, `tags`, `md5`, `process_instance_id`) VALUES ('1441610450502848524', '胡歌 - 指纹.mp4', 'admin', 'http://175.178.69.253:81/upload/node5/69142f1e30d04175bbe2825688efbe25.mp4', '3', 'video', '2021-11-04 20:45:23', '2022-08-26 10:42:50', '《指纹》是电视剧《轩辕剑之天之痕》的插曲，由周剑光作词，郑宇界作曲，胡歌演唱，谢嘉荣制作MV。', '1341620898007281665', '1341620898007281665', '0', '胡歌', 'a161872d69e26adc44ac60e6dfcf13e3', '46f4c370-24e5-11ed-b537-525400cf57fe');
INSERT INTO `boot_sys_resource` (`id`, `title`, `author`, `uri`, `status`, `code`, `create_date`, `update_date`, `remark`, `creator`, `editor`, `del_flag`, `tags`, `md5`, `process_instance_id`) VALUES ('1441610450502848525', '刘德华 - 17岁.mp4', 'admin', 'http://175.178.69.253:81/upload/node3/fecc0b2a83dd4c94b0398f6187ebc04c.mp4', '3', 'video', '2021-11-07 09:53:00', '2022-08-26 10:38:49', '《17岁》是刘德华演唱的一首歌曲，由刘德华和徐继宗作词，徐继宗作曲的一首歌，编曲为Billy Chan，收录在专辑《如果有一天》中。', '1341620898007281665', '1341620898007281665', '0', '刘德华', '5256ce59614ebf73e1cb7376e24c956c', '69a4f8ab-24d0-11ed-9e12-005056c00001');
INSERT INTO `boot_sys_resource` (`id`, `title`, `author`, `uri`, `status`, `code`, `create_date`, `update_date`, `remark`, `creator`, `editor`, `del_flag`, `tags`, `md5`, `process_instance_id`) VALUES ('1441610450502848527', '黄安 - 新鸳鸯蝴蝶梦.mp3', 'admin', 'http://175.178.69.253:81/upload/node3/73c11e1aac3c4b3db893ca711bb78ab5.mp3', '3', 'audio', '2021-11-28 17:43:38', '2022-08-26 10:36:15', '《新鸳鸯蝴蝶梦》是1993年台湾电视剧《包青天》的主题曲，由黄安演唱、填词并作曲，詹宏达编曲，最早收录于1993年2月1日发行的同名专辑《新鸳鸯蝴蝶梦》中。', '1341620898007281665', '1341620898007281665', '0', '黄安', '86fcfca80a39b9c5c1e095cf83497db7', 'b8410140-24e2-11ed-b537-525400cf57fe');
INSERT INTO `boot_sys_resource` (`id`, `title`, `author`, `uri`, `status`, `code`, `create_date`, `update_date`, `remark`, `creator`, `editor`, `del_flag`, `tags`, `md5`, `process_instance_id`) VALUES ('1441610450502848538', '周杰伦 - 兰亭序.mp3', 'admin', 'http://175.178.69.253:81/upload/node1/d8ed3d9659b843ceb776d603cc068bef.mp3', '3', 'audio', '2022-02-24 11:29:06', '2022-08-26 10:36:48', '《兰亭序》是周杰伦演唱的一首歌曲，由周杰伦作曲，方文山填词，钟兴民编曲，收录在周杰伦2008年10月15日发行的专辑《魔杰座》中。\n2011年，周杰伦在央视春晚上演唱了这首歌曲，并获得了2011年CCTV春晚我最喜爱的春节联欢晚会评选歌舞类节目三等奖。\n', '1341620898007281665', '1341620898007281665', '0', '周杰伦', '6bbd6faf1a600ea5d198e92502de3297', '913baf98-24e2-11ed-b537-525400cf57fe');
INSERT INTO `boot_sys_resource` (`id`, `title`, `author`, `uri`, `status`, `code`, `create_date`, `update_date`, `remark`, `creator`, `editor`, `del_flag`, `tags`, `md5`, `process_instance_id`) VALUES ('1441610450502848530', '蔡依林_周杰伦 - 布拉格广场.flac', 'admin', 'http://175.178.69.253:81/upload/node5/a607608ec20b46e2abbeaf3e704db6d6.flac', '3', 'audio', '2022-03-14 11:07:44', '2022-08-26 10:37:49', '《布拉格广场》是蔡依林、周杰伦演唱的歌曲，由方文山作词、周杰伦作曲、钟兴民编曲，收录于蔡依林2003年3月7日发行的专辑《看我72变》中。\n2004年5月，钟兴民在第15届台湾金曲奖颁奖典礼上凭借该曲获得“最佳编曲人奖” 。', '1341620898007281665', '1341620898007281665', '0', '蔡依林,周杰伦', 'ec1d65892e59a046b22be2c99de68f4a', '3eb76af8-24e2-11ed-b537-525400cf57fe');
INSERT INTO `boot_sys_resource` (`id`, `title`, `author`, `uri`, `status`, `code`, `create_date`, `update_date`, `remark`, `creator`, `editor`, `del_flag`, `tags`, `md5`, `process_instance_id`) VALUES ('1441610450502848531', '周杰伦 - 本草纲目.flac', 'admin', 'http://175.178.69.253:81/upload/node5/1cabe4eabef547688090a4c58ad2c82e.flac', '3', 'audio', '2022-03-01 11:17:50', '2022-08-26 10:36:57', '《本草纲目》是周杰伦演唱的一首歌曲，由方文山作词，周杰伦作曲，林迈可编曲，收录在周杰伦2006年9月5日发行的专辑《依然范特西》中。 \n周杰伦在2009年春节联欢晚会上与宋祖英以“英伦组合”合唱该歌曲。', '1341620898007281665', '1341620898007281665', '0', '周杰伦', '3682d061c1f28236def78a7ab3a567a5', '7d2f8b70-24e2-11ed-b537-525400cf57fe');
INSERT INTO `boot_sys_resource` (`id`, `title`, `author`, `uri`, `status`, `code`, `create_date`, `update_date`, `remark`, `creator`, `editor`, `del_flag`, `tags`, `md5`, `process_instance_id`) VALUES ('1441610450502848532', '周杰伦 - 超人不会飞.flac', 'admin', 'http://175.178.69.253:81/upload/node3/98f52b4365df44efb76e4c8e046f006d.flac', '3', 'audio', '2022-03-07 11:19:14', '2022-08-26 10:37:24', '《超人不会飞》是周杰伦演唱的一首歌曲，由周杰伦填词、谱曲，林迈可编曲，收录在周杰伦2010年5月18日发行的专辑《跨时代》中。', '1341620898007281665', '1341620898007281665', '0', '周杰伦', '512a437738066e9d0aa0532920073e60', '69493ec8-24e2-11ed-b537-525400cf57fe');
INSERT INTO `boot_sys_resource` (`id`, `title`, `author`, `uri`, `status`, `code`, `create_date`, `update_date`, `remark`, `creator`, `editor`, `del_flag`, `tags`, `md5`, `process_instance_id`) VALUES ('1441610450502848533', '周杰伦 - 稻香.flac', 'admin', 'http://175.178.69.253:81/upload/node4/76340fdb43be499ea3a540ea6f934267.flac', '3', 'audio', '2022-03-16 11:20:50', '2022-08-26 10:43:10', '《稻香》是周杰伦演唱的一首歌曲，由周杰伦作词、作曲，黄雨勋编曲，收录在周杰伦2008年10月15日发行的专辑《魔杰座》中；周杰伦在接受媒体采访时表示，《稻香》是因5·12汶川地震创作的。\n2009年这首歌曲获得第20届台湾金曲奖最佳年度歌曲奖、雪碧榜中国原创音乐流行榜港台地区歌曲奖等奖项。', '1341620898007281665', '1341620898007281665', '0', '周杰伦', 'f9c90d62803ea395d7cb9566e06fb54d', '94f885a0-24e1-11ed-b537-525400cf57fe');
INSERT INTO `boot_sys_resource` (`id`, `title`, `author`, `uri`, `status`, `code`, `create_date`, `update_date`, `remark`, `creator`, `editor`, `del_flag`, `tags`, `md5`, `process_instance_id`) VALUES ('1441610450502848534', '周杰伦 - 东风破.flac', 'admin', 'http://175.178.69.253:81/upload/node3/a56c134452cc466e8596c39d9c4fb0bc.flac', '3', 'audio', '2022-03-14 11:22:10', '2022-08-26 10:33:54', '《东风破》是周杰伦演唱的一首歌曲，由周杰伦谱曲，方文山填词，林迈可编曲，收录在周杰伦2003年7月31日发行的个人第四张专辑《叶惠美》中。\n2004年，该歌曲获得第四届华语流行乐传媒大奖十大单曲奖、第11届中国歌曲排行榜港台地区最受欢迎歌曲奖、MusicRadio中国TOP排行榜台港地区年度歌曲和第七届中央电视台音乐电视大赛港台及海外华语最佳MV作品奖等奖项，方文山凭借该歌曲获得第四届百事音乐风云榜年度港台及海外华人最佳作词奖。', '1341620898007281665', '1341620898007281665', '0', '周杰伦', '12a81c97cf300b24bf57857b97931ec5', '288336b0-24e2-11ed-b537-525400cf57fe');
INSERT INTO `boot_sys_resource` (`id`, `title`, `author`, `uri`, `status`, `code`, `create_date`, `update_date`, `remark`, `creator`, `editor`, `del_flag`, `tags`, `md5`, `process_instance_id`) VALUES ('1441610450502848535', '周杰伦 - 断了的弦.flac', 'admin', 'http://175.178.69.253:81/upload/node5/b5c8a23a9aeb4fb7963a93b5a46d034c.flac', '3', 'audio', '2022-03-14 11:23:59', '2022-08-26 10:35:54', '《断了的弦》是周杰伦演唱的一首歌曲，由方文山作词，周杰伦作曲，钟兴民编曲，收录于周杰伦于2003年11月13日发行的EP《寻找周杰伦》中 。该曲是电影《寻找周杰伦》的插曲。', '1341620898007281665', '1341620898007281665', '0', '周杰伦', 'fb27cd144628b3c3f440c9b0dc6158a4', '0c574ca8-24e2-11ed-b537-525400cf57fe');
INSERT INTO `boot_sys_resource` (`id`, `title`, `author`, `uri`, `status`, `code`, `create_date`, `update_date`, `remark`, `creator`, `editor`, `del_flag`, `tags`, `md5`, `process_instance_id`) VALUES ('1441610450502848536', '周杰伦 - 发如雪.flac', 'admin', 'http://175.178.69.253:81/upload/node1/b83abed50bf64d5490afed2762ccea05.flac', '3', 'audio', '2022-03-27 11:25:11', '2022-08-26 07:12:36', '《发如雪》是由周杰伦作曲，方文山填词，周杰伦演唱。《发如雪》是首古筝伴奏的中国古典风格的作品，相比《夜曲》的欧洲古典风格，这首《十一月的萧邦》专辑中的第二主打歌曲毫不逊色。另外歌词“本来无一物，何处惹尘埃”道出了这生生世世，缘起缘落，一切只因心尘未脱的世间痴男怨女的心境。《发如雪》是一首在唱腔上颇具难度技巧的歌曲，周杰伦飙到高音的地方令人印象深刻。', '1341620898007281665', '1341620898007281665', '0', '周杰伦', 'e7a39a7c883cb3305e1527f87658ffbe', '889e1ac5-24ca-11ed-9e12-005056c00001');
INSERT INTO `boot_sys_resource` (`id`, `title`, `author`, `uri`, `status`, `code`, `create_date`, `update_date`, `remark`, `creator`, `editor`, `del_flag`, `tags`, `md5`, `process_instance_id`) VALUES ('1441610450502848537', '周杰伦 - 霍元甲.flac', 'admin', 'http://175.178.69.253:81/upload/node3/e632ad8073b24f76a8ca17e6d9a812ae.flac', '3', 'audio', '2022-03-14 11:26:15', '2022-08-26 10:40:18', '《霍元甲》是周杰伦演唱的一首歌曲，由方文山作词，周杰伦作曲，洪敬尧编曲，收录于2006年1月20日发行的同名EP《霍元甲》中。该曲是电影《霍元甲》的同名主题曲。\n2007年，该曲获得第26届香港电影金像奖最佳原创电影歌曲提名，周杰伦凭借该曲获得第18届台湾金曲奖最佳单曲制作人奖。', '1341620898007281665', '1341620898007281665', '0', '周杰伦', 'e2f4fcc868e56f1cc7c84bd6bcf0cfb7', 'f6ae8f80-24e1-11ed-b537-525400cf57fe');
INSERT INTO `boot_sys_resource` (`id`, `title`, `author`, `uri`, `status`, `code`, `create_date`, `update_date`, `remark`, `creator`, `editor`, `del_flag`, `tags`, `md5`, `process_instance_id`) VALUES ('1441610450502848539', '周杰伦 - 龙卷风.flac', 'admin', 'http://175.178.69.253:81/upload/node4/f74da90133ed48638193cc072773a217.flac', '3', 'audio', '2022-03-14 11:31:07', '2022-08-26 10:42:43', '《龙卷风》是周杰伦演唱的一首歌曲，由徐若瑄作词，周杰伦作曲，钟兴民编曲，收录于周杰伦2000年11月7日发行的首张个人专辑《Jay》中。\n2001年，该歌曲获得新城劲爆卡拉OK歌曲奖。', '1341620898007281665', '1341620898007281665', '0', '周杰伦', '133617be05cfbddd351d576b9a6de674', 'e319e548-24e1-11ed-b537-525400cf57fe');
INSERT INTO `boot_sys_resource` (`id`, `title`, `author`, `uri`, `status`, `code`, `create_date`, `update_date`, `remark`, `creator`, `editor`, `del_flag`, `tags`, `md5`, `process_instance_id`) VALUES ('1441610450502848540', '周杰伦 - 牛仔很忙.flac', 'admin', 'http://175.178.69.253:81/upload/node1/d97ae6efe4a841f3a119d96e32f3994b.flac', '3', 'audio', '2022-03-29 11:33:34', '2022-08-25 14:47:38', '《牛仔很忙》是周杰伦演唱的一首歌曲，由周杰伦作曲，黄俊郎填词，钟兴民编曲，收录在周杰伦2007年11月2日发行的专辑《我很忙》中。', '1341620898007281665', '1341620898007281665', '0', '周杰伦', '628895955dc5b857db72376354dc96d1', '49eac432-2441-11ed-ac33-005056c00001');
INSERT INTO `boot_sys_resource` (`id`, `title`, `author`, `uri`, `status`, `code`, `create_date`, `update_date`, `remark`, `creator`, `editor`, `del_flag`, `tags`, `md5`, `process_instance_id`) VALUES ('1441610450502848541', '周杰伦 - 七里香.flac', 'admin', 'http://175.178.69.253:81/upload/node1/31dbe74b6a184f94b3e690ddb23b173d.flac', '3', 'audio', '2022-03-29 11:35:07', '2022-08-26 07:14:11', '七里香歌曲《七里香》，由周杰伦演唱，方文山作词，周杰伦谱曲，钟兴民编曲，收录在周杰伦2004年8月3日发行的同名专辑《七里香》中。\n2004年，该曲获得香港TVB8十大金曲最佳作曲、监制、编曲3项大奖。2005年，该曲获得第27届十大中文金曲十大金曲奖、优秀流行华语歌曲奖以及第11届全球华语音乐榜中榜年度最佳歌曲等多个奖项。', '1341620898007281665', '1341620898007281665', '0', '周杰伦', '1f766acd83ac6a2df378495c87533fc8', 'd2483f7d-24c9-11ed-9e12-005056c00001');
INSERT INTO `boot_sys_resource` (`id`, `title`, `author`, `uri`, `status`, `code`, `create_date`, `update_date`, `remark`, `creator`, `editor`, `del_flag`, `tags`, `md5`, `process_instance_id`) VALUES ('1441610450502848542', '周杰伦_费玉清 - 千里之外.flac', 'admin', 'http://175.178.69.253:81/upload/node4/7c51289e75fd427484871b63aeee0545.flac', '3', 'audio', '2022-03-07 11:36:12', '2022-08-26 10:38:42', '《千里之外》是周杰伦、费玉清演唱的一首歌曲，由方文山作词，周杰伦作曲，林迈可编曲，收录在周杰伦2006年9月5日发行的专辑《依然范特西》中，是周杰伦的代表作品之一。该歌曲另有费玉清的独唱版本，收录于费玉清2007年4月20日发行的专辑《回想曲青青校树》中。\n2007年，该歌曲获得了第十三届全球华语音乐榜中榜年度最佳歌曲奖、第29届十大中文金曲全国最受欢迎中文歌曲奖、第7届蒙牛酸酸乳音乐风云榜年度最佳歌曲奖。', '1341620898007281665', '1341620898007281665', '0', '费玉清,周杰伦', 'e96cf23291d51b38655392788d102fd6', '579a6b90-24e2-11ed-b537-525400cf57fe');
INSERT INTO `boot_sys_resource` (`id`, `title`, `author`, `uri`, `status`, `code`, `create_date`, `update_date`, `remark`, `creator`, `editor`, `del_flag`, `tags`, `md5`, `process_instance_id`) VALUES ('1441610450502848543', '周杰伦 - 烟花易冷.flac', 'admin', 'http://175.178.69.253:81/upload/node4/4c6655d7d5dc42b1a491f8a0c4138413.flac', '3', 'audio', '2022-03-15 11:37:57', '2022-08-26 10:43:03', '《烟花易冷》是方文山作词，黄雨勋编曲，周杰伦作曲并演唱的一首歌曲，收录在周杰伦2010年5月18日发行的专辑《跨时代》中。\n2011年，该曲获得2010年度北京流行音乐典礼“年度金曲奖”。', '1341620898007281665', '1341620898007281665', '0', '周杰伦', 'e678890eb6fc2abe0c524a7cb9b775b9', 'b27e8168-24e1-11ed-b537-525400cf57fe');
INSERT INTO `boot_sys_resource` (`id`, `title`, `author`, `uri`, `status`, `code`, `create_date`, `update_date`, `remark`, `creator`, `editor`, `del_flag`, `tags`, `md5`, `process_instance_id`) VALUES ('1441610450502848544', '周杰伦 - 夜的第七章.mp3', 'admin', 'http://175.178.69.253:81/upload/node2/23d90162fde44dd58b7e7147e196adb9.mp3', '3', 'audio', '2022-03-16 11:39:14', '2022-08-26 10:38:35', '《夜的第七章》讲的是伦敦近郊的公园里的发生了一起凶杀案，现场唯一留下的线索是死者口里的一朵蓝色玫瑰的故事。\n《夜的第七章》是周杰伦、潘儿演唱的一首歌曲，由黄俊郎作词，周杰伦作曲，钟兴民、林迈可编曲。收录在周杰伦于2006年9月5日发行的专辑《依然范特西》中。', '1341620898007281665', '1341620898007281665', '0', '周杰伦', 'f03f3a0cb53139bd4da8886265ea5943', '5fcf5c48-24e1-11ed-b537-525400cf57fe');
INSERT INTO `boot_sys_resource` (`id`, `title`, `author`, `uri`, `status`, `code`, `create_date`, `update_date`, `remark`, `creator`, `editor`, `del_flag`, `tags`, `md5`, `process_instance_id`) VALUES ('1441610450502848545', '周杰伦 - 夜曲.flac', 'admin', 'http://175.178.69.253:81/upload/node1/eefa051169454bf9ab1ccaa313febf8e.flac', '3', 'audio', '2022-03-14 11:40:57', '2022-08-26 10:42:36', '《夜曲》是周杰伦演唱的一首歌曲，由方文山作词，周杰伦作曲，林迈可编曲，收录在周杰伦2005年11月1日发行的专辑《11月的萧邦》中。\n2005年，该曲获得雪碧榜港台金曲、9+2音乐先锋榜年度先锋金曲。2006年，该曲获得全球华语音乐榜中榜年度最佳歌曲等多个奖项。', '1341620898007281665', '1341620898007281665', '0', '周杰伦', '98eb0d625611a9bb95a265ac99ec57c8', 'cbcd6330-24e1-11ed-b537-525400cf57fe');
INSERT INTO `boot_sys_resource` (`id`, `title`, `author`, `uri`, `status`, `code`, `create_date`, `update_date`, `remark`, `creator`, `editor`, `del_flag`, `tags`, `md5`, `process_instance_id`) VALUES ('1562946299216121858', 'k↑.jpg', 'admin', 'http://124.222.196.51/group1/M00/00/00/CgAQEGMIBreAE9WPAACcoeFrkzk407.jpg', '3', 'image', '2022-08-26 07:33:31', '2022-08-26 10:43:37', '老寇的CSDN个人头像', '1341620898007281665', '1341620898007281665', '0', 'CSDN', '15d0c51477b30d6818cf93c62698a4d3', '85639510-24e5-11ed-b537-525400cf57fe');
------------------------------------资源------------------------------------