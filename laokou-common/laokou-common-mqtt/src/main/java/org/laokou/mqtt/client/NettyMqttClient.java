package org.laokou.mqtt.client;

import io.netty.handler.codec.mqtt.*;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;

import java.nio.charset.StandardCharsets;
import java.util.concurrent.CompletableFuture;

/**
 * @author laokou
 */
record NettyMqttClient(MqttClientProperties mqttClientProperties) implements MqttClient {

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

	private MqttConnectMessage buildConnectMessage() {
		MqttFixedHeader fixedHeader = new MqttFixedHeader(MqttMessageType.CONNECT, false, MqttQoS.AT_MOST_ONCE, false, 0);
		MqttConnectVariableHeader variableHeader = new MqttConnectVariableHeader(MqttVersion.MQTT_5.protocolName(), MqttVersion.MQTT_5.protocolLevel(), true, true, false, 0, false,
			mqttClientProperties.isCleanSession(),
			mqttClientProperties.getKeepAliveTimeSeconds(),
			MqttProperties.NO_PROPERTIES);
		MqttConnectPayload payload = new MqttConnectPayload(mqttClientProperties.getClientId(), null, null, mqttClientProperties.getUsername(), mqttClientProperties.getPassword().getBytes(StandardCharsets.UTF_8));
		return new MqttConnectMessage(fixedHeader, variableHeader, payload);
	}

	private SslContext buildSslContext() throws Exception {
		SslContextBuilder builder = SslContextBuilder.forClient();
//		if (props.getSsl().isInsecureSkipVerify()) {
//			builder.trustManager(InsecureTrustManagerFactory.INSTANCE);
//		}
		return builder.build();
	}

}
