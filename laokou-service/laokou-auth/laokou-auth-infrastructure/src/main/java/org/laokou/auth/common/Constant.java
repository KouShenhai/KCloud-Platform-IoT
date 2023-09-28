package org.laokou.auth.common;

/**
 * @author laokou
 */
public interface Constant {

	/**
	 * 默认
	 */
	long DEFAULT_TENANT = 0L;

	/**
	 * 默认数据库
	 */
	String DEFAULT_SOURCE = "master";

	/**
	 * 密码登录
	 */
	String AUTH_PASSWORD = "password";

	/**
	 * 邮箱登录
	 */
	String AUTH_MAIL = "mail";

	/**
	 * 手机号登录
	 */
	String AUTH_MOBILE = "mobile";

	/**
	 * 唯一标识
	 */
	String UUID = "uuid";

	/**
	 * 验证码
	 */
	String CAPTCHA = "captcha";

	/**
	 * 邮箱
	 */
	String MAIL = "mail";

	/**
	 * 手机
	 */
	String MOBILE = "mobile";

	/**
	 * 登录
	 */
	String LOGIN_PATTERN = "/login";

	/**
	 * 租户ID
	 */
	String TENANT_ID = "tenantId";

	/**
	 * 用户
	 */
	String USER = "user";

	/**
	 * 登录日志
	 */
	String LOGIN_LOG = "login_log";

}
