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
  `create_date` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_date` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  `del_flag` tinyint(1) DEFAULT '0' COMMENT '1已删除 0未删除',
  `sort` int(11) DEFAULT '1' COMMENT '排序',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC COMMENT='菜单';

CREATE TABLE `boot_sys_role_menu` (
  `role_id` bigint(20) NOT NULL COMMENT '角色id',
  `menu_id` bigint(20) NOT NULL COMMENT '菜单id',
  KEY `role_id` (`role_id`) USING BTREE,
  KEY `menu_id` (`menu_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC COMMENT='角色-菜单';

CREATE TABLE `boot_sys_role` (
  `id` bigint(20) NOT NULL COMMENT 'id',
  `name` varchar(50) NOT NULL COMMENT '角色名称',
  `creator` bigint(20) NOT NULL COMMENT '创建人',
  `editor` bigint(20) DEFAULT NULL COMMENT '编辑人',
  `create_date` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_date` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  `del_flag` tinyint(1) NOT NULL DEFAULT '0' COMMENT '1已删除 0未删除',
  `sort` int(11) DEFAULT '1' COMMENT '排序',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC COMMENT='角色';

CREATE TABLE `boot_sys_user_role` (
  `user_id` bigint(20) NOT NULL COMMENT '用户id',
  `role_id` bigint(20) NOT NULL COMMENT '角色id',
  KEY `user_id` (`user_id`) USING BTREE,
  KEY `role_id` (`role_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC COMMENT='用户-角色';

CREATE TABLE `boot_sys_user` (
  `id` bigint(20) NOT NULL COMMENT 'id',
  `username` varchar(20) NOT NULL COMMENT '用户名',
  `password` varchar(100) NOT NULL COMMENT '密码',
  `super_admin` tinyint(1) NOT NULL DEFAULT '0' COMMENT '超级管理员：0否 1是',
  `creator` bigint(20) DEFAULT NULL COMMENT '创建人',
  `editor` bigint(20) DEFAULT NULL COMMENT '编辑人',
  `create_date` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_date` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  `del_flag` tinyint(1) NOT NULL COMMENT '1已删除 0未删除',
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

CREATE TABLE `boot_zfb_user` (
  `id` varchar(32) NOT NULL COMMENT '支付宝用户唯一标识',
  `gender` varchar(1) DEFAULT NULL COMMENT '性别',
  `province` varchar(10) DEFAULT NULL COMMENT '省份',
  `city` varchar(10) DEFAULT NULL COMMENT '城市',
  `avatar` varchar(400) DEFAULT NULL COMMENT '图像',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='支付宝用户';

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
  `create_date` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_date` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  `del_flag` tinyint(1) NOT NULL DEFAULT '0' COMMENT '1已删除 0未删除',
  `editor` bigint(20) DEFAULT NULL COMMENT '编辑人',
  PRIMARY KEY (`id`),
  KEY `idx_module` (`module`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='操作日志';

CREATE TABLE `boot_sys_login_log` (
  `id` bigint(20) NOT NULL COMMENT 'id',
  `creator` bigint(20) DEFAULT NULL COMMENT '创建者',
  `create_date` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_date` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  `del_flag` tinyint(1) NOT NULL DEFAULT '0' COMMENT '1已删除 0未删除',
  `editor` bigint(20) DEFAULT NULL COMMENT '编辑人',
  `login_name` varchar(20) DEFAULT NULL COMMENT '登录用户',
  `request_ip` varchar(50) DEFAULT NULL COMMENT 'IP地址',
  `request_address` varchar(200) DEFAULT NULL COMMENT '归属地',
  `browser` varchar(50) DEFAULT NULL COMMENT '浏览器',
  `os` varchar(50) DEFAULT NULL COMMENT '操作系统',
  `request_status` tinyint(1) unsigned NOT NULL COMMENT '状态  0：成功   1：失败',
  `msg` varchar(500) DEFAULT NULL COMMENT '提示信息',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='登录日志';

CREATE TABLE `boot_sys_dict` (
  `id` bigint(20) NOT NULL COMMENT 'id',
  `creator` bigint(20) DEFAULT NULL COMMENT '创建者',
  `create_date` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_date` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  `del_flag` tinyint(1) NOT NULL DEFAULT '0' COMMENT '1已删除 0未删除',
  `editor` bigint(20) DEFAULT NULL COMMENT '编辑人',
  `dict_value` text COMMENT '值',
  `dict_label` varchar(100) DEFAULT NULL COMMENT '标签',
  `type` varchar(100) DEFAULT NULL COMMENT '类型',
  `status` tinyint(1) DEFAULT NULL COMMENT '状态 0正常 1停用',
  `remark` varchar(500) DEFAULT NULL COMMENT '备注',
  `sort` int(11) DEFAULT '1' COMMENT '排序',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='系统字典';

INSERT INTO `boot_sys_menu` (`id`, `pid`, `permissions`, `type`, `name`, `url`, `auth_level`, `method`, `icon`, `creator`, `editor`, `create_date`, `update_date`, `del_flag`, `sort`) VALUES ('1391677542887788567', '1535878154046939137', 'sys:menu:view', '0', '菜单管理', '/sys/menu/view', '0', 'GET', 'treeTable', '1341620898007281665', '1341620898007281665', '2022-06-12 23:36:44', '2022-06-12 23:36:44', '0', '3000');
INSERT INTO `boot_sys_menu` (`id`, `pid`, `permissions`, `type`, `name`, `url`, `auth_level`, `method`, `icon`, `creator`, `editor`, `create_date`, `update_date`, `del_flag`, `sort`) VALUES ('1535858679453085698', '1391677542887788567', 'sys:menu:query', '1', '菜单查询', '/sys/menu/api/query', '0', 'POST', 'search', '1341620898007281665', '1341620898007281665', '2022-06-22 07:09:59', '2022-06-21 23:11:00', '0', '10');
INSERT INTO `boot_sys_menu` (`id`, `pid`, `permissions`, `type`, `name`, `url`, `auth_level`, `method`, `icon`, `creator`, `editor`, `create_date`, `update_date`, `del_flag`, `sort`) VALUES ('1535859148908949506', '1391677542887788567', 'sys:menu:insert', '1', '菜单新增', '/sys/menu/api/insert', '0', 'POST', 'plus', '1341620898007281665', NULL, '2022-06-12 21:59:41', '2022-06-12 21:59:41', '0', '10');
INSERT INTO `boot_sys_menu` (`id`, `pid`, `permissions`, `type`, `name`, `url`, `auth_level`, `method`, `icon`, `creator`, `editor`, `create_date`, `update_date`, `del_flag`, `sort`) VALUES ('1535859326311231489', '1391677542887788567', 'sys:menu:update', '1', '菜单修改', '/sys/menu/api/update', '0', 'PUT', 'edit', '1341620898007281665', '1341620898007281665', '2022-06-20 01:45:22', '2022-06-19 17:46:23', '0', '10');
INSERT INTO `boot_sys_menu` (`id`, `pid`, `permissions`, `type`, `name`, `url`, `auth_level`, `method`, `icon`, `creator`, `editor`, `create_date`, `update_date`, `del_flag`, `sort`) VALUES ('1535859588534923265', '1391677542887788567', 'sys:menu:delete', '1', '菜单删除', '/sys/menu/api/delete', '0', 'DELETE', 'delete', '1341620898007281665', NULL, '2022-06-12 13:40:35', '2022-06-12 13:40:35', '0', '10');
INSERT INTO `boot_sys_menu` (`id`, `pid`, `permissions`, `type`, `name`, `url`, `auth_level`, `method`, `icon`, `creator`, `editor`, `create_date`, `update_date`, `del_flag`, `sort`) VALUES ('1535878154046939137', '0', 'sys:view', '0', '系统管理', '/sys', '0', 'GET', 'system', '1341620898007281665', '1341620898007281665', '2022-06-21 21:20:28', '2022-06-21 13:21:28', '0', '9000');
INSERT INTO `boot_sys_menu` (`id`, `pid`, `permissions`, `type`, `name`, `url`, `auth_level`, `method`, `icon`, `creator`, `editor`, `create_date`, `update_date`, `del_flag`, `sort`) VALUES ('1535881096963563522', '1535878154046939137', 'sys:user:view', '0', '用户管理', '/sys/user/view', '0', 'GET', 'user', '1341620898007281665', '1341620898007281665', '2022-06-16 04:04:56', '2022-06-15 20:05:57', '0', '1000');
INSERT INTO `boot_sys_menu` (`id`, `pid`, `permissions`, `type`, `name`, `url`, `auth_level`, `method`, `icon`, `creator`, `editor`, `create_date`, `update_date`, `del_flag`, `sort`) VALUES ('1535881356595175426', '1535878154046939137', 'sys:role:view', '0', '角色管理', '/sys/role/view', '0', 'GET', 'peoples', '1341620898007281665', NULL, '2022-06-12 15:07:05', '2022-06-12 15:07:05', '0', '2000');
INSERT INTO `boot_sys_menu` (`id`, `pid`, `permissions`, `type`, `name`, `url`, `auth_level`, `method`, `icon`, `creator`, `editor`, `create_date`, `update_date`, `del_flag`, `sort`) VALUES ('1535886982654205953', '1535881356595175426', 'sys:role:query', '1', '角色查询', '/sys/role/api/query', '0', 'POST', 'search', '1341620898007281665', '1341620898007281665', '2022-06-22 07:10:10', '2022-06-21 23:11:10', '0', '10');
INSERT INTO `boot_sys_menu` (`id`, `pid`, `permissions`, `type`, `name`, `url`, `auth_level`, `method`, `icon`, `creator`, `editor`, `create_date`, `update_date`, `del_flag`, `sort`) VALUES ('1535887129341599746', '1535881356595175426', 'sys:role:insert', '1', '角色新增', '/sys/role/api/insert', '0', 'POST', 'plus', '1341620898007281665', NULL, '2022-06-12 15:30:02', '2022-06-12 15:30:02', '0', '10');
INSERT INTO `boot_sys_menu` (`id`, `pid`, `permissions`, `type`, `name`, `url`, `auth_level`, `method`, `icon`, `creator`, `editor`, `create_date`, `update_date`, `del_flag`, `sort`) VALUES ('1535887276649750530', '1535881356595175426', 'sys:role:update', '1', '角色修改', '/sys/role/api/update', '0', 'PUT', 'edit', '1341620898007281665', '1341620898007281665', '2022-06-20 01:45:31', '2022-06-19 17:46:31', '0', '10');
INSERT INTO `boot_sys_menu` (`id`, `pid`, `permissions`, `type`, `name`, `url`, `auth_level`, `method`, `icon`, `creator`, `editor`, `create_date`, `update_date`, `del_flag`, `sort`) VALUES ('1535887450809835521', '1535881356595175426', 'sys:role:delete', '1', '角色删除', '/sys/role/api/delete', '0', 'DELETE', 'delete', '1341620898007281665', NULL, '2022-06-12 15:31:18', '2022-06-12 15:31:18', '0', '10');
INSERT INTO `boot_sys_menu` (`id`, `pid`, `permissions`, `type`, `name`, `url`, `auth_level`, `method`, `icon`, `creator`, `editor`, `create_date`, `update_date`, `del_flag`, `sort`) VALUES ('1535887779873955841', '1535881096963563522', 'sys:user:query', '1', '用户查询', '/sys/user/api/query', '0', 'POST', 'search', '1341620898007281665', '1341620898007281665', '2022-06-22 07:10:19', '2022-06-21 23:11:20', '0', '10');
INSERT INTO `boot_sys_menu` (`id`, `pid`, `permissions`, `type`, `name`, `url`, `auth_level`, `method`, `icon`, `creator`, `editor`, `create_date`, `update_date`, `del_flag`, `sort`) VALUES ('1535887940687765505', '1535881096963563522', 'sys:user:insert', '1', '用户新增', '/sys/user/api/insert', '0', 'POST', 'plus', '1341620898007281665', NULL, '2022-06-12 15:33:15', '2022-06-12 15:33:15', '0', '10');
INSERT INTO `boot_sys_menu` (`id`, `pid`, `permissions`, `type`, `name`, `url`, `auth_level`, `method`, `icon`, `creator`, `editor`, `create_date`, `update_date`, `del_flag`, `sort`) VALUES ('1535888146045083649', '1535881096963563522', 'sys:user:update', '1', '用户修改', '/sys/user/api/update', '0', 'PUT', 'edit', '1341620898007281665', '1341620898007281665', '2022-06-20 01:45:37', '2022-06-19 17:46:37', '0', '10');
INSERT INTO `boot_sys_menu` (`id`, `pid`, `permissions`, `type`, `name`, `url`, `auth_level`, `method`, `icon`, `creator`, `editor`, `create_date`, `update_date`, `del_flag`, `sort`) VALUES ('1535888341252186114', '1535881096963563522', 'sys:user:delete', '1', '用户删除', '/sys/user/api/delete', '0', 'DELETE', 'delete', '1341620898007281665', NULL, '2022-06-12 23:37:47', '2022-06-12 23:37:47', '0', '10');
INSERT INTO `boot_sys_menu` (`id`, `pid`, `permissions`, `type`, `name`, `url`, `auth_level`, `method`, `icon`, `creator`, `editor`, `create_date`, `update_date`, `del_flag`, `sort`) VALUES ('1537444981390794754', '0', 'monitor:view', '0', '系统监控', '/monitor', '0', 'GET', 'monitor', '1341620898007281665', '1341620898007281665', '2022-06-21 21:26:48', '2022-06-21 13:27:48', '0', '8000');
INSERT INTO `boot_sys_menu` (`id`, `pid`, `permissions`, `type`, `name`, `url`, `auth_level`, `method`, `icon`, `creator`, `editor`, `create_date`, `update_date`, `del_flag`, `sort`) VALUES ('1537447512825225218', '1537444981390794754', 'monitor:druid:view', '0', '数据监控', 'https://1.com/laokou/admin/druid/login.html', '0', 'GET', 'druid', '1341620898007281665', '1341620898007281665', '2022-06-22 06:42:18', '2022-06-21 22:43:19', '0', '4000');
INSERT INTO `boot_sys_menu` (`id`, `pid`, `permissions`, `type`, `name`, `url`, `auth_level`, `method`, `icon`, `creator`, `editor`, `create_date`, `update_date`, `del_flag`, `sort`) VALUES ('1538469199368712193', '1537444981390794754', 'monitor:admin:view', '0', '服务监控', 'http://192.168.62.1:5000/monitor', '0', 'GET', 'server', '1341620898007281665', '1341620898007281665', '2022-06-22 07:02:02', '2022-06-22 07:02:02', '0', '3000');
INSERT INTO `boot_sys_menu` (`id`, `pid`, `permissions`, `type`, `name`, `url`, `auth_level`, `method`, `icon`, `creator`, `editor`, `create_date`, `update_date`, `del_flag`, `sort`) VALUES ('1539250224424394753', '1535878154046939137', 'sys:log:view', '0', '日志管理', '/sys/log', '0', 'GET', 'log', '1341620898007281665', '1341620898007281665', '2022-06-24 06:44:58', '2022-06-23 23:12:25', '0', '500');
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
INSERT INTO `boot_sys_menu` (`id`, `pid`, `permissions`, `type`, `name`, `url`, `auth_level`, `method`, `icon`, `creator`, `editor`, `create_date`, `update_date`, `del_flag`, `sort`) VALUES ('1545035717690912769', '0', 'workflow:view', '0', '工作流程', '/workflow', '0', 'GET', 'tree', '1341620898007281665', NULL, '2022-07-07 21:23:15', '2022-07-07 21:23:15', '0', '7000');
INSERT INTO `boot_sys_menu` (`id`, `pid`, `permissions`, `type`, `name`, `url`, `auth_level`, `method`, `icon`, `creator`, `editor`, `create_date`, `update_date`, `del_flag`, `sort`) VALUES ('1545036486288732162', '1545035717690912769', 'workflow:definition:view', '0', '流程定义', '/workflow/definition/view', '0', 'GET', 'guide', '1341620898007281665', '1341620898007281665', '2022-07-07 21:27:18', '2022-07-07 21:27:18', '0', '5000');
INSERT INTO `boot_sys_menu` (`id`, `pid`, `permissions`, `type`, `name`, `url`, `auth_level`, `method`, `icon`, `creator`, `editor`, `create_date`, `update_date`, `del_flag`, `sort`) VALUES ('1545037580289044482', '1545035717690912769', 'workflow:process:view', '0', '流程任务', '/workflow/process/view', '0', 'GET', 'timeRange', '1341620898007281665', '1341620898007281665', '2022-07-07 21:34:30', '2022-07-07 21:34:31', '0', '4000');
INSERT INTO `boot_sys_menu` (`id`, `pid`, `permissions`, `type`, `name`, `url`, `auth_level`, `method`, `icon`, `creator`, `editor`, `create_date`, `update_date`, `del_flag`, `sort`) VALUES ('1545442687073681410', '1545036486288732162', 'workflow:definition:insert', '1', '流程新增', '/workflow/definition/api/insert', '0', 'POST', 'plus', '1537114827246292994', NULL, '2022-07-09 00:20:24', '2022-07-09 00:20:24', '0', '10');
INSERT INTO `boot_sys_menu` (`id`, `pid`, `permissions`, `type`, `name`, `url`, `auth_level`, `method`, `icon`, `creator`, `editor`, `create_date`, `update_date`, `del_flag`, `sort`) VALUES ('1545442932918616065', '1545036486288732162', 'workflow:definition:query', '1', '流程查询', '/workflow/definition/api/query', '0', 'GET', 'search', '1537114827246292994', NULL, '2022-07-09 00:21:23', '2022-07-09 00:21:23', '0', '10');
INSERT INTO `boot_sys_menu` (`id`, `pid`, `permissions`, `type`, `name`, `url`, `auth_level`, `method`, `icon`, `creator`, `editor`, `create_date`, `update_date`, `del_flag`, `sort`) VALUES ('1545443357407346689', '1545036486288732162', 'workflow:definition:activate', '1', '流程激活', '/workflow/definition/api/activate', '0', 'PUT', 'play-circle', '1537114827246292994', NULL, '2022-07-09 00:23:04', '2022-07-09 00:23:04', '0', '10');
INSERT INTO `boot_sys_menu` (`id`, `pid`, `permissions`, `type`, `name`, `url`, `auth_level`, `method`, `icon`, `creator`, `editor`, `create_date`, `update_date`, `del_flag`, `sort`) VALUES ('1545443597204094977', '1545036486288732162', 'workflow:definition:suspend', '1', '流程挂起', '/workflow/definition/api/suspend', '0', 'PUT', 'pause-circle', '1537114827246292994', NULL, '2022-07-09 00:24:01', '2022-07-09 00:24:01', '0', '10');
INSERT INTO `boot_sys_menu` (`id`, `pid`, `permissions`, `type`, `name`, `url`, `auth_level`, `method`, `icon`, `creator`, `editor`, `create_date`, `update_date`, `del_flag`, `sort`) VALUES ('1545443828113113090', '1545036486288732162', 'workflow:definition:delete', '1', '流程删除', '/workflow/definition/api/delete', '0', 'DELETE', 'delete', '1537114827246292994', NULL, '2022-07-09 00:24:56', '2022-07-09 00:24:56', '0', '10');
INSERT INTO `boot_sys_menu` (`id`, `pid`, `permissions`, `type`, `name`, `url`, `auth_level`, `method`, `icon`, `creator`, `editor`, `create_date`, `update_date`, `del_flag`, `sort`) VALUES ('1545444197799067650', '1545036486288732162', 'workflow:definition:image', '1', '流程查看', '/workflow/definition/api/image', '0', 'GET', 'eye', '1537114827246292994', NULL, '2022-07-09 00:26:24', '2022-07-09 00:26:24', '0', '10');
INSERT INTO `boot_sys_menu` (`id`, `pid`, `permissions`, `type`, `name`, `url`, `auth_level`, `method`, `icon`, `creator`, `editor`, `create_date`, `update_date`, `del_flag`, `sort`) VALUES ('1545444494848065538', '1545036486288732162', 'workflow:process:start', '1', '流程发起', '/workflow/process/api/start', '0', 'POST', 'cluster', '1537114827246292994', NULL, '2022-07-09 00:27:35', '2022-07-09 00:27:35', '0', '10');
INSERT INTO `boot_sys_menu` (`id`, `pid`, `permissions`, `type`, `name`, `url`, `auth_level`, `method`, `icon`, `creator`, `editor`, `create_date`, `update_date`, `del_flag`, `sort`) VALUES ('1545444853809184770', '1545037580289044482', 'workflow:process:query', '1', '任务查询', '/workflow/process/api/query', '0', 'GET', 'search', '1537114827246292994', NULL, '2022-07-09 00:29:01', '2022-07-09 00:29:01', '0', '10');
INSERT INTO `boot_sys_menu` (`id`, `pid`, `permissions`, `type`, `name`, `url`, `auth_level`, `method`, `icon`, `creator`, `editor`, `create_date`, `update_date`, `del_flag`, `sort`) VALUES ('1545445208781520897', '1545037580289044482', 'workflow:task:audit', '1', '任务审核', '/workflow/task/api/audit', '0', 'POST', 'audit', '1537114827246292994', '1537114827246292994', '2022-07-09 00:40:59', '2022-07-09 00:40:59', '0', '10');
INSERT INTO `boot_sys_menu` (`id`, `pid`, `permissions`, `type`, `name`, `url`, `auth_level`, `method`, `icon`, `creator`, `editor`, `create_date`, `update_date`, `del_flag`, `sort`) VALUES ('1545445677574684673', '1545037580289044482', 'workflow:task:diagram', '1', '任务进度', '/workflow/task/api/diagram', '0', 'GET', 'eye', '1537114827246292994', NULL, '2022-07-09 00:32:17', '2022-07-09 00:32:17', '0', '10');

INSERT INTO `boot_sys_role` (`id`, `name`, `creator`, `editor`, `create_date`, `update_date`, `del_flag`, `sort`) VALUES ('139167754288778856', '游客', '0', '1341620898007281665', '2021-11-27 17:11:15', '2022-06-13 00:59:04', '0', '10');
INSERT INTO `boot_sys_role` (`id`, `name`, `creator`, `editor`, `create_date`, `update_date`, `del_flag`, `sort`) VALUES ('139167754288778857', '管理员', '0', '1341620898007281665', '2021-11-27 17:11:19', '2022-07-01 07:49:02', '0', '100');
INSERT INTO `boot_sys_role` (`id`, `name`, `creator`, `editor`, `create_date`, `update_date`, `del_flag`, `sort`) VALUES ('1535949666183573505', '测试', '1341620898007281665', '1341620898007281665', '2022-06-12 19:38:32', '2022-06-13 00:55:32', '0', '50');

INSERT INTO `boot_sys_user` (`id`, `username`, `password`, `super_admin`, `creator`, `editor`, `create_date`, `update_date`, `del_flag`, `email`, `status`, `img_url`, `mobile`, `zfb_openid`) VALUES ('1341620898007281665', 'admin', '$2a$10$JH4rLaMdK9KBCa8oBL9Tz.zMSYKn/8OEheFUMKLdTC1SFIVwjgQXO', '1', '1341620898007281665', '1341623527018004481', '2021-11-29 20:13:11', '2022-06-20 01:36:58', '0', '2413176044@qq.com', '0', 'https://1.com//upload/node3/7904fff1c08a4883b40f1ee0336017dc.webp', '18974432576', '2088722720196501');
INSERT INTO `boot_sys_user` (`id`, `username`, `password`, `super_admin`, `creator`, `editor`, `create_date`, `update_date`, `del_flag`, `email`, `status`, `img_url`, `mobile`, `zfb_openid`) VALUES ('1341623527018004481', 'test', '$2a$10$Nrnb0lwgMMvuq5lC.nO/pO5S2dCT2ZoStcf8Iho3NqyTedTgIcu9W', '0', '1341620898007281665', '1341620898007281665', '2020-12-23 13:55:50', '2022-06-20 02:52:18', '0', 'XX@qq.com', '0', 'https://1.com/upload/node2/b4e5bb3944a046a6bb54f8bfd2c830c1.webp', 'xxx', NULL);
INSERT INTO `boot_sys_user` (`id`, `username`, `password`, `super_admin`, `creator`, `editor`, `create_date`, `update_date`, `del_flag`, `email`, `status`, `img_url`, `mobile`, `zfb_openid`) VALUES ('1421312053736804354', 'koush5', '$2a$10$sy6boUKxyUGApAE0aJUJHuSVm5a21Yb6XQ.Y/bGueiZQ41JSxibZu', '0', '1341620898007281665', '1421312053736804354', '2021-07-31 11:29:35', '2022-06-14 20:28:43', '0', 'YY@qq.com', '0', 'https://ruoyi.setworld.net/img/profile.473f5971.jpg', 'xxxxx', NULL);
INSERT INTO `boot_sys_user` (`id`, `username`, `password`, `super_admin`, `creator`, `editor`, `create_date`, `update_date`, `del_flag`, `email`, `status`, `img_url`, `mobile`, `zfb_openid`) VALUES ('1537111101311844353', 'wumh5', '$2a$10$A/iY2ZSmiQ1SqEZkAj8vzucGZwCsRvfe2zP7DeLGDdH4KW4OvEw92', '0', '1341620898007281665', '1341620898007281665', '2022-06-16 00:33:39', '2022-06-20 02:46:43', '0', NULL, '0', 'https://gimg2.baidu.com/image_search/src=http%3A%2F%2Fi.qqkou.com%2Fi%2F1a3626475345x3078425090b26.jpg&amp;refer=http%3A%2F%2Fi.qqkou.com&amp;app=2002&amp;size=f9999,10000&amp;q=a80&amp;n=0&amp;g=0n&amp;fmt=auto?sec=1657902941&amp;t=73f98a243f12f3eabe1dce87d2b6401b', NULL, NULL);
INSERT INTO `boot_sys_user` (`id`, `username`, `password`, `super_admin`, `creator`, `editor`, `create_date`, `update_date`, `del_flag`, `email`, `status`, `img_url`, `mobile`, `zfb_openid`) VALUES ('1537114827246292994', 'laok5', '$2a$10$0YO/xdMa0Gcs7CQGengZy.BKTFLRZyRSjhOIawdEIfeIOgepH/gem', '0', '1341620898007281665', '1537114827246292994', '2022-06-16 00:48:28', '2022-06-16 09:14:26', '0', NULL, '0', 'https://gimg2.baidu.com/image_search/src=http%3A%2F%2Fi.qqkou.com%2Fi%2F1a3626475345x3078425090b26.jpg&amp;refer=http%3A%2F%2Fi.qqkou.com&amp;app=2002&amp;size=f9999,10000&amp;q=a80&amp;n=0&amp;g=0n&amp;fmt=auto?sec=1657902941&amp;t=73f98a243f12f3eabe1dce87d2b6401b', NULL, NULL);

INSERT INTO `boot_sys_dict` (`id`, `creator`, `create_date`, `update_date`, `del_flag`, `editor`, `dict_value`, `dict_label`, `type`, `status`, `remark`, `sort`) VALUES ('1537114827246292322', '1341620898007281665', '2022-06-23 20:33:26', '2022-06-24 22:28:48', '0', NULL, '{\"fastdfsDomain\":\"https://120.79.93.23\",\"fastdfsGroup\":\"group1\",\"localDomain\":\"https://1.com\",\"localPath\":\"D:/koushenhai/project/temp\",\"localPrefix\":\"upload\",\"type\":0,\"aliyunDomain\":\"https://koushenhai.oss-cn-shenzhen.aliyuncs.com\",\"aliyunPrefix\":\"laokou\",\"aliyunEndPoint\":\"https://oss-cn-shenzhen.aliyuncs.com\",\"aliyunAccessKeyId\":\"LTAIPIOiuCK4ZK3o\",\"aliyunAccessKeySecret\":\"dtYg8khtfEUM3FlV2FvVH6bHyezdqi\",\"aliyunBucketName\":\"koushenhai\"}', '对象存储', 'oss', '0', '对象存储', '1000');