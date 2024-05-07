/*
 * Copyright (c) 2022-2024 KCloud-Platform-IoT Author or Authors. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package org.laokou.common.i18n.common;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * @author laokou
 */
@Schema(name = "DatasourceConstants", description = "数据源常量")
public final class DSConstant {

	private DSConstant() {
	}

	@Schema(name = "LIKE", description = "模糊匹配")
	public static final String LIKE = "like";

	@Schema(name = "OR", description = "或")
	public static final String OR = "or";

	@Schema(name = "AND", description = "和")
	public static final String AND = "and";

	@Schema(name = "BOOT_SYS_DICT", description = "字典表")
	public static final String BOOT_SYS_DICT = "boot_sys_dict";

	@Schema(name = "BOOT_SYS_MESSAGE", description = "消息表")
	public static final String BOOT_SYS_MESSAGE = "boot_sys_message";

	@Schema(name = "BOOT_SYS_OSS", description = "OSS表")
	public static final String BOOT_SYS_OSS = "boot_sys_oss";

	@Schema(name = "BOOT_SYS_USER", description = "用户表")
	public static final String BOOT_SYS_USER = "boot_sys_user";

	@Schema(name = "BOOT_SYS_DEPT", description = "部门表")
	public static final String BOOT_SYS_DEPT = "boot_sys_dept";

	@Schema(name = "BOOT_SYS_MENU", description = "菜单表")
	public static final String BOOT_SYS_MENU = "boot_sys_menu";

	@Schema(name = "BOOT_SYS_TENANT", description = "租户表")
	public static final String BOOT_SYS_TENANT = "boot_sys_tenant";

	@Schema(name = "BOOT_SYS_ROLE", description = "角色表")
	public static final String BOOT_SYS_ROLE = "boot_sys_role";

	@Schema(name = "BOOT_SYS_SOURCE", description = "数据源表")
	public static final String BOOT_SYS_SOURCE = "boot_sys_source";

	@Schema(name = "BOOT_SYS_PACKAGE", description = "套餐表")
	public static final String BOOT_SYS_PACKAGE = "boot_sys_package";

	@Schema(name = "BOOT_SYS_OPERATE_LOG", description = "操作日志表")
	public static final String BOOT_SYS_OPERATE_LOG = "boot_sys_operate_log";

	@Schema(name = "BOOT_SYS_LOGIN_LOG", description = "登录日志表")
	public static final String BOOT_SYS_LOGIN_LOG = "boot_sys_login_log";

	@Schema(name = "BOOT_SYS_RESOURCE", description = "资源表")
	public static final String BOOT_SYS_RESOURCE = "boot_sys_resource";

	@Schema(name = "BOOT_SYS_SQL_LOG", description = "SQL日志表")
	public static final String BOOT_SYS_SQL_LOG = "boot_sys_sql_log";

	public static final String BOOT_SYS_I18N_MESSAGE = "boot_sys_i18n_message";

	@Schema(name = "BOOT_SYS_IP", description = "IP表")
	public static final String BOOT_SYS_IP = "boot_sys_ip";

	@Schema(name = "BOOT_SYS_DOMAIN_EVENT", description = "领域事件表")
	public static final String BOOT_SYS_DOMAIN_EVENT = "boot_sys_domain_event";

	@Schema(name = "TENANT", description = "租户数据源标识")
	public static final String TENANT = "#tenant";

	@Schema(name = "INSERT_SQL_TEMPLATE", description = "插入SQL模板")
	public static final String INSERT_SQL_TEMPLATE = "INSERT INTO `%s`(%s) VALUES(%s);\n";

	@Schema(name = "UPDATE_USERNAME_BY_ID_SQL_TEMPLATE", description = "根据ID修改用户名SQL模板")
	public static final String UPDATE_USERNAME_BY_ID_SQL_TEMPLATE = "UPDATE %s SET username = AES_ENCRYPT('%s','%s') WHERE ID = %s;\n";

}
