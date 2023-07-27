package org.laokou.common.netty.config;

public interface Server {

	/**
	 * 发送消息
	 */
	void send(String clientId, Object obj);

	/**
	 * 启动
	 */
	void start();

	/**
	 * 停止
	 */
	void stop();

}
