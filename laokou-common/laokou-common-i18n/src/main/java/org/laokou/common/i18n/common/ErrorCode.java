/*
 * Copyright (c) 2022 KCloud-Platform-Alibaba Authors. All Rights Reserved.
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
@Schema(name = "ErrorCode", description = "错误码")
public interface ErrorCode {

	@Schema(name = "ACCOUNT_PASSWORD_ERROR", description = "账号或密码错误")
	int ACCOUNT_PASSWORD_ERROR = 200999;

	@Schema(name = "CAPTCHA_ERROR", description = "验证码错误")
	int CAPTCHA_ERROR = 200998;

	@Schema(name = "CAPTCHA_EXPIRED", description = "验证码已过期")
	int CAPTCHA_EXPIRED = 200997;

	@Schema(name = "ACCOUNT_DISABLE", description = "账号已锁定")
	int ACCOUNT_DISABLE = 200996;

	@Schema(name = "MOBILE_ERROR", description = "手机号错误")
	int MOBILE_ERROR = 200995;

	@Schema(name = "MAIL_ERROR", description = "邮箱错误")
	int MAIL_ERROR = 200994;

	@Schema(name = "INVALID_SCOPE", description = "无效作用域")
	int INVALID_SCOPE = 200993;

	@Schema(name = "INVALID_CLIENT", description = "无效客户端")
	int INVALID_CLIENT = 200992;

	@Schema(name = "REGISTERED_CLIENT_NOT_EXIST", description = "注册客户端不存在")
	int REGISTERED_CLIENT_NOT_EXIST = 200991;

	@Schema(name = "GENERATE_ACCESS_TOKEN_FAIL", description = "令牌生成器无法生成访问令牌")
	int GENERATE_ACCESS_TOKEN_FAIL = 200990;

	@Schema(name = "GENERATE_REFRESH_TOKEN_FAIL", description = "令牌生成器无法生成刷新令牌")
	int GENERATE_REFRESH_TOKEN_FAIL = 200989;

	@Schema(name = "GENERATE_ID_TOKEN_FAIL", description = "令牌生成器无法生成标识令牌")
	int GENERATE_ID_TOKEN_FAIL = 200988;

	@Schema(name = "REQUEST_FLOW", description = "请求已限流")
	int REQUEST_FLOW = 998999;

	@Schema(name = "DEGRADE", description = "已降级")
	int DEGRADE = 998998;

	@Schema(name = "PARAM_FLOW", description = "热点参数已限流")
	int PARAM_FLOW = 998997;

	@Schema(name = "SYSTEM_BLOCK", description = "系统规则错误")
	int SYSTEM_BLOCK = 998996;

	@Schema(name = "AUTHORITY", description = "授权规则错误")
	int AUTHORITY = 998995;

	@Schema(name = "TRANSACTION_TIMEOUT", description = "事务已超时")
	int TRANSACTION_TIMEOUT = 999999;

}
