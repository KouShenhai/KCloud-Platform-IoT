package org.laokou.mqtt.client;

import io.netty.handler.codec.mqtt.MqttQoS;

import java.util.concurrent.CompletableFuture;

/**
 * @author laokou
 */
final class NettyMqttClient implements MqttClient {

	@Override
	public CompletableFuture<Void> connect() {
		return null;
	}

	@Override
	public CompletableFuture<Void> disconnect() {
		return null;
	}

	@Override
	public CompletableFuture<Void> publish(String topic, byte[] payload, MqttQoS qos, boolean retained) {
		return null;
	}

	@Override
	public CompletableFuture<Integer> subscribe(String topicFilter, MqttQoS qos, MqttMessageListener listener) {
		return null;
	}

}
