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

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * 错误码.
 * @author laokou
 */
@Schema(name = "ErrorCode", description = "错误码")
public final class ErrorCodes {

	private ErrorCodes() {
	}

	/**
	 * 路由不存在.
	 */
	@Schema(name = "ROUTE_NOT_EXIST", description = "路由不存在")
	public static final int ROUTE_NOT_EXIST = 100999;

	/**
	 * 无效请求.
	 */
	@Schema(name = "INVALID_REQUEST", description = "无效请求")
	public static final int INVALID_REQUEST = 200987;

	/**
	 * 令牌生成器无法生成标识令牌.
	 */
	@Schema(name = "GENERATE_ID_TOKEN_FAIL", description = "令牌生成器无法生成标识令牌")
	public static final int GENERATE_ID_TOKEN_FAIL = 200988;

	/**
	 * 令牌生成器无法生成刷新令牌.
	 */
	@Schema(name = "GENERATE_REFRESH_TOKEN_FAIL", description = "令牌生成器无法生成刷新令牌")
	public static final int GENERATE_REFRESH_TOKEN_FAIL = 200989;

	/**
	 * 令牌生成器无法生成访问令牌.
	 */
	@Schema(name = "GENERATE_ACCESS_TOKEN_FAIL", description = "令牌生成器无法生成访问令牌")
	public static final int GENERATE_ACCESS_TOKEN_FAIL = 200990;

	/**
	 * 注册客户端不存在.
	 */
	@Schema(name = "REGISTERED_CLIENT_NOT_EXIST", description = "注册客户端不存在")
	public static final int REGISTERED_CLIENT_NOT_EXIST = 200991;

	/**
	 * 无效客户端.
	 */
	@Schema(name = "INVALID_CLIENT", description = "无效客户端")
	public static final int INVALID_CLIENT = 200992;

	/**
	 * 无效作用域.
	 */
	@Schema(name = "INVALID_SCOPE", description = "无效作用域")
	public static final int INVALID_SCOPE = 200993;

	/**
	 * 邮箱错误.
	 */
	@Schema(name = "MAIL_ERROR", description = "邮箱错误")
	public static final int MAIL_ERROR = 200994;

	/**
	 * 手机号错误.
	 */
	@Schema(name = "MOBILE_ERROR", description = "手机号错误")
	public static final int MOBILE_ERROR = 200995;

	/**
	 * 账号已锁定.
	 */
	@Schema(name = "ACCOUNT_DISABLE", description = "账号已锁定")
	public static final int ACCOUNT_DISABLE = 200996;

	/**
	 * 验证码已过期.
	 */
	@Schema(name = "CAPTCHA_EXPIRED", description = "验证码已过期")
	public static final int CAPTCHA_EXPIRED = 200997;

	/**
	 * 验证码错误.
	 */
	@Schema(name = "CAPTCHA_ERROR", description = "验证码错误")
	public static final int CAPTCHA_ERROR = 200998;

	/**
	 * 账号或密码错误.
	 */
	@Schema(name = "ACCOUNT_PASSWORD_ERROR", description = "账号或密码错误")
	public static final int ACCOUNT_PASSWORD_ERROR = 200999;

	/**
	 * 授权规则错误.
	 */
	@Schema(name = "AUTHORITY", description = "授权规则错误")
	public static final int AUTHORITY = 998995;

	/**
	 * 系统规则错误.
	 */
	@Schema(name = "SYSTEM_BLOCK", description = "系统规则错误")
	public static final int SYSTEM_BLOCK = 998996;

	/**
	 * 热点参数已限流.
	 */
	@Schema(name = "PARAM_FLOW", description = "热点参数已限流")
	public static final int PARAM_FLOW = 998997;

	/**
	 * 已降级.
	 */
	@Schema(name = "DEGRADE", description = "已降级")
	public static final int DEGRADE = 998998;

	/**
	 * 请求已限流.
	 */
	@Schema(name = "REQUEST_FLOW", description = "请求已限流")
	public static final int REQUEST_FLOW = 998999;

	/**
	 * 分布式事务未启动.
	 */
	@Schema(name = "DISTRIBUTED_TRANSACTION_DOWNTIME", description = "分布式事务未启动")
	public static final int DISTRIBUTED_TRANSACTION_DOWNTIME = 999998;

	/**
	 * 分布式事务已超时.
	 */
	@Schema(name = "DISTRIBUTED_TRANSACTION_TIMEOUT", description = "分布式事务已超时")
	public static final int DISTRIBUTED_TRANSACTION_TIMEOUT = 999999;

}
