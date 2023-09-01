package org.laokou.auth.common;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * @author laokou
 */
@Schema(name = "BizCode", description = "业务码")
public interface BizCode {

	@Schema(name = "UUID_NOT_NULL", description = "UUID不能为空")
	int UUID_NOT_NULL = 2001;

	@Schema(name = "CAPTCHA_NOT_NULL", description = "验证码不能为空")
	int CAPTCHA_NOT_NULL = 2002;

	@Schema(name = "USERNAME_NOT_NULL", description = "账号不能为空")
	int USERNAME_NOT_NULL = 2005;

	@Schema(name = "PASSWORD_NOT_NULL", description = "密码不能为空")
	int PASSWORD_NOT_NULL = 2009;

	@Schema(name = "MOBILE_NOT_NULL", description = "手机号不能为空")
	int MOBILE_NOT_NULL = 2010;

	@Schema(name = "MAIL_NOT_NULL", description = "邮箱不能为空")
	int MAIL_NOT_NULL = 2012;

	@Schema(name = "TENANT_ID_NOT_NULL", description = "租户ID不能为空")
	int TENANT_ID_NOT_NULL = 2014;

	@Schema(name = "LOGIN_SUCCEEDED", description = "登录成功")
	int LOGIN_SUCCEEDED = 2018;

}
