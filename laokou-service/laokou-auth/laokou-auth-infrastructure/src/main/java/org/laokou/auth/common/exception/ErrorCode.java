package org.laokou.auth.common.exception;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * @author laokou
 */
@Schema(name = "ErrorCode", description = "错误码")
public interface ErrorCode {

	@Schema(name = "CAPTCHA_ERROR", description = "验证码错误，请重新输入")
	int CAPTCHA_ERROR = 2003;

	@Schema(name = "CAPTCHA_EXPIRED", description = "验证码已过期")
	int CAPTCHA_EXPIRED = 2004;

	@Schema(name = "USERNAME_DISABLE", description = "账号已被锁定，请联系管理员")
	int USERNAME_DISABLE = 2006;

	@Schema(name = "USERNAME_PASSWORD_ERROR", description = "账号或密码错误，请重新输入")
	int USERNAME_PASSWORD_ERROR = 2007;

	@Schema(name = "USERNAME_NOT_PERMISSION", description = "用户没有权限访问，请联系管理员")
	int USERNAME_NOT_PERMISSION = 2008;

	@Schema(name = "MOBILE_ERROR", description = "手机号错误，请重新输入")
	int MOBILE_ERROR = 2011;

	@Schema(name = "MAIL_ERROR", description = "邮箱错误，请重新输入")
	int MAIL_ERROR = 2013;

	@Schema(name = "INVALID_SCOPE", description = "无效作用域")
	int INVALID_SCOPE = 2015;

	@Schema(name = "INVALID_CLIENT", description = "无效客户端")
	int INVALID_CLIENT = 2016;

	@Schema(name = "REGISTERED_CLIENT_NOT_EXIST", description = "注册客户端不存在")
	int REGISTERED_CLIENT_NOT_EXIST = 2019;

	@Schema(name = "GENERATE_ACCESS_TOKEN_FAIL", description = "令牌生成器无法生成访问令牌")
	int GENERATE_ACCESS_TOKEN_FAIL = 2020;

	@Schema(name = "GENERATE_REFRESH_TOKEN_FAIL", description = "令牌生成器无法生成刷新令牌")
	int GENERATE_REFRESH_TOKEN_FAIL = 2021;

	@Schema(name = "GENERATE_ID_TOKEN_FAIL", description = "令牌生成器无法生成标识令牌")
	int GENERATE_ID_TOKEN_FAIL = 2022;

}
