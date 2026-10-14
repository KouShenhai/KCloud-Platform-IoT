package org.laokou.mqtt.client;

import io.netty.handler.codec.mqtt.MqttQoS;
import lombok.Data;

@Data
public final class OutboundMessage {

	private final int packetId;
	private final String topic;
	private final byte[] payload;
	private final MqttQoS qos;
	private final boolean retained;
	private volatile boolean isDup;

	public OutboundMessage(int packetId, String topic, byte[] payload, MqttQoS qos, boolean retained) {
		this.packetId = packetId;
		this.topic = topic;
		this.payload = payload;
		this.qos = qos;
		this.retained = retained;
	}
}
