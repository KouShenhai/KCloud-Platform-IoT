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

package org.laokou.common.i18n.common.exception;

/**
 * 系统异常.
 *
 * @author laokou
 */
public final class SystemException extends GlobalException {

	public SystemException(String code) {
		super(code);
	}

	public SystemException(String code, String msg) {
		super(code, msg);
	}

	public SystemException(String code, String msg, Throwable throwable) {
		super(code, msg, throwable);
	}

	public final static class Gateway {

		/**
		 * IP已列入黑名单.
		 */
		public static final String IP_BLACKED = "S_Gateway_IpBlacked";

		/**
		 * IP被限制.
		 */
		public static final String IP_RESTRICTED = "S_Gateway_IpRestricted";

		/**
		 * 路由不存在.
		 */
		public static final String ROUTER_NOT_EXIST = "S_Gateway_RouterNotExist";

	}

	public final static class Sentinel {

		/**
		 * 授权规则错误.
		 */
		public static final String AUTHORITY = "S_Sentinel_Authority";

		/**
		 * 系统规则错误.
		 */
		public static final String SYSTEM_BLOCKED = "S_Sentinel_SystemBlocked";

		/**
		 * 热点参数已限流.
		 */
		public static final String PARAM_FLOWED = "S_Sentinel_ParamFlowed";

		/**
		 * 已降级.
		 */
		public static final String DEGRADED = "S_Sentinel_Degraded";

		/**
		 * 已限流.
		 */
		public static final String FLOWED = "S_Sentinel_Flowed";

	}

}
