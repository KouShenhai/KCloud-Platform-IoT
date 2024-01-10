/*
 * Copyright (c) 2022 KCloud-Platform-Alibaba Author or Authors. All Rights Reserved.
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

/**
 * @author laokou
 */
public final class DatasourceConstants {

	private DatasourceConstants() {
	}

	/**
	 * 模糊
	 */
	public static final String LIKE = "like";

	/**
	 * 或
	 */
	public static final String OR = "or";

	/**
	 * 流程
	 */
	public static final String FLOWABLE = "flowable";

	public static final String BOOT_SYS_DICT = "boot_sys_dict";

	public static final String BOOT_SYS_MESSAGE = "boot_sys_message";

	public static final String BOOT_SYS_OSS = "boot_sys_oss";

	public static final String BOOT_SYS_USER = "boot_sys_user";

	public static final String BOOT_SYS_DEPT = "boot_sys_dept";

	public static final String BOOT_SYS_MENU = "boot_sys_menu";

	public static final String BOOT_SYS_TENANT = "boot_sys_tenant";

	public static final String BOOT_SYS_ROLE = "boot_sys_role";

	public static final String BOOT_SYS_SOURCE = "boot_sys_source";

	public static final String BOOT_SYS_PACKAGE = "boot_sys_package";

	public static final String BOOT_SYS_OPERATE_LOG = "boot_sys_operate_log";

	public static final String BOOT_SYS_LOGIN_LOG = "boot_sys_login_log";

	public static final String BOOT_SYS_RESOURCE = "boot_sys_resource";

	public static final String BOOT_SYS_SQL_LOG = "boot_sys_sql_log";

	public static final String BOOT_SYS_IP = "boot_sys_ip";

	/**
	 * 租户
	 */
	public static final String TENANT = "#tenant";

	public static final String INSERT_SQL_TEMPLATE = "INSERT INTO `%s`(%s) VALUES(%s);\n";

	public static final String UPDATE_USERNAME_BY_ID_SQL_TEMPLATE = "UPDATE %s SET username = AES_ENCRYPT('%s','%s') WHERE ID = %s;\n";

	public static final String SHOW_TABLES = "show tables";

}
