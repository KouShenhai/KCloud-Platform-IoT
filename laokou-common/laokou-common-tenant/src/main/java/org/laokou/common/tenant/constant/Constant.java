/*
 * Copyright (c) 2022-2025 KCloud-Platform-IoT Author or Authors. All Rights Reserved.
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

package org.laokou.common.tenant.constant;

/**
 * @author laokou
 */
public final class Constant {

	public static final String IOT = "iot";

	public static final String MASTER = "master";

	public static final String DOMAIN = "domain";

	public static final String GENERATOR = "generator";

	public static final class IoT {

		private IoT() {
		}

	}

	public static final class Master {

		private Master() {
		}

		/**
		 * 菜单表.
		 */
		public static final String MENU_TABLE = "boot_sys_menu";

		/**
		 * 用户表.
		 */
		public static final String USER_TABLE = "boot_sys_user";

		/**
		 * 部门表.
		 */
		public static final String DEPT_TABLE = "boot_sys_dept";

		/**
		 * 角色表.
		 */
		public static final String ROLE_TABLE = "boot_sys_role";

		/**
		 * 用户角色表.
		 */
		public static final String USER_ROLE_TABLE = "boot_sys_user_role";

		/**
		 * 用户部门表.
		 */
		public static final String USER_DEPT_TABLE = "boot_sys_user_dept";

		/**
		 * 角色菜单表.
		 */
		public static final String ROLE_MENU_TABLE = "boot_sys_role_menu";

	}

	public static final class Domain {

		private Domain() {
		}

		/**
		 * 通知日志表.
		 */
		public static final String NOTICE_LOG_TABLE = "boot_sys_notice_log";

		/**
		 * 登录日志表.
		 */
		public static final String LOGIN_LOG_TABLE = "boot_sys_login_log";

		/**
		 * 操作日志表.
		 */
		public static final String OPERATE_LOG_TABLE = "boot_sys_operate_log";

	}

	private Constant() {
	}

}
