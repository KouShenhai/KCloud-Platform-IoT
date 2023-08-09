package org.laokou.gateway.exception;

public interface ErrorCode {

	/**
	 * 请求过于频繁，请稍后再试
	 */
	int SERVICE_BLOCK_REQUEST = 1001;

	/**
	 * 请求已中断，请刷新页面
	 */
	int SERVICE_REQUEST_CLOSE = 1002;

	/**
	 * IP被列入黑名单，请联系管理员
	 */
	int IP_BLACK = 1003;

	/**
	 * 无效客户端
	 */
	int INVALID_CLIENT = 2016;

}
