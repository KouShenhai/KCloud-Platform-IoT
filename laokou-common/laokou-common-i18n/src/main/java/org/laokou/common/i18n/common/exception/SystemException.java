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

package org.laokou.common.i18n.common.exception;

/**
 * 系统异常.
 *
 * @author laokou
 */
public final class SystemException extends GlobalException {

	/**
	 * IP已列入黑名单.
	 */
	public static final String IP_BLACKED = "S_Ip_Blacked";

	/**
	 * IP被限制.
	 */
	public static final String IP_RESTRICTED = "S_Ip_Restricted";

	/**
	 * 路由不存在.
	 */
	public static final String ROUTER_NOT_EXIST = "S_Gateway_RouterNotExist";

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

	/**
	 * 表不存在.
	 */
	public static final String TABLE_NOT_EXIST = "S_DS_TableNotExist";

	/**
	 * 用户名解密失败.
	 */
	public static final String USERNAME_AES_DECRYPT_FAIL = "S_User_UsernameAESDecryptFail";

	/**
	 * 手机号解密失败.
	 */
	public static final String MOBILE_AES_DECRYPT_FAIL = "S_User_MobileAESDecryptFail";

	/**
	 * 邮箱解密失败.
	 */
	public static final String MAIL_AES_DECRYPT_FAIL = "S_User_MailAESDecryptFail";

	public SystemException(String code) {
		super(code);
	}

	public SystemException(String code, String msg) {
		super(code, msg);
	}

}
