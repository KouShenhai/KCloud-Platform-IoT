package org.laokou.mqtt.client;

import io.netty.handler.codec.mqtt.MqttQoS;

import java.util.concurrent.CompletableFuture;

/**
 * @author laokou
 * <p>所有 API 均为异步非阻塞，返回 {@link CompletableFuture}：
 * <ul>
 *   <li>QoS0：发送成功后完成</li>
 *   <li>QoS1：收到 PUBACK 后完成</li>
 *   <li>QoS2：走完 PUBREC/PUBREL/PUBCOMP 完整握手后完成</li>
 * </ul>
 */
interface MqttClient {

	/**
	 * 建立连接。
	 * @return CompletableFuture
	 */
	CompletableFuture<Void> connect();

	/**
	 * 主动关闭，不自动重连。
	 * @return CompletableFuture
	 */
	CompletableFuture<Void> disconnect();

	/**
	 * 发布消息
	 * @param topic 主题
	 * @param payload 消息
	 * @param qos qos
	 * @param retained retained
	 * @return CompletableFuture
	 */
	CompletableFuture<Void> publish(String topic, byte[] payload, MqttQoS qos, boolean retained);

	/**
	 * 订阅主题.
	 * @param topicFilter 主题
	 * @param qos qos
	 * @param listener 监听器
	 * @return CompletableFuture
	 */
	CompletableFuture<Integer> subscribe(String topicFilter, MqttQoS qos, MqttMessageListener listener);

	/**
	 * 取消订阅.
	 * @param topicFilter 主题
	 * @return CompletableFuture
	 */
	CompletableFuture<Void> unsubscribe(String topicFilter);

	ClientState state();

	default boolean isConnected() {
		return state() == ClientState.CONNECTED;
	}

}
