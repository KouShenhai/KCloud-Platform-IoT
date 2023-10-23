package org.laokou.gateway.exception;

/**
 * @author laokou
 */
public interface BizCode {

	/**
	 * IP被列入黑名单，请联系管理员
	 */
	int IP_BLACK = 1003;

	/**
	 * IP被限制，请联系管理员
	 */
	int IP_WHITE = 1004;

	/**
	 * 无效客户端
	 */
	int INVALID_CLIENT = 2016;

}
