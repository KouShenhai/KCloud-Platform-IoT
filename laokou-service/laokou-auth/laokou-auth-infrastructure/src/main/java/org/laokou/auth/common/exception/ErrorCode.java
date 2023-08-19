package org.laokou.auth.common.exception;

/**
 * 编码由5位数字构成 前2位为应用编号 后3位为业务编号
 * @author laokou
 */
public interface ErrorCode {

	/**
	 * 验证码不正确，请重新输入
	 */
	int CAPTCHA_ERROR = 2003;

	/**
	 * 验证码已过期
	 */
	int CAPTCHA_EXPIRED = 2004;

	/**
	 * 账号已被锁定，请联系管理员
	 */
	int USERNAME_DISABLE = 2006;

	/**
	 * 帐户或密码错误，请重新输入
	 */
	int USERNAME_PASSWORD_ERROR = 2007;

	/**
	 * 用户没有权限访问，请联系管理员
	 */
	int USERNAME_NOT_PERMISSION = 2008;

	/**
	 * 手机号错误，请重新输入
	 */
	int MOBILE_ERROR = 2011;

	/**
	 * 邮箱错误，请重新输入
	 */
	int MAIL_ERROR = 2013;

	/**
	 * 无效作用域
	 */
	int INVALID_SCOPE = 2015;

	/**
	 * 无效客户端
	 */
	int INVALID_CLIENT = 2016;

	/**
	 * 注册客户端不存在
	 */
	int REGISTERED_CLIENT_NOT_EXIST = 2019;

	/**
	 * 令牌生成器无法生成访问令牌
	 */
	int GENERATE_ACCESS_TOKEN_FAIL = 2020;

	/**
	 * 令牌生成器无法生成刷新令牌
	 */
	int GENERATE_REFRESH_TOKEN_FAIL = 2021;

}
