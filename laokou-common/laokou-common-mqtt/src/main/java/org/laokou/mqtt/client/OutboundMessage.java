package org.laokou.mqtt.client;

import io.netty.handler.codec.mqtt.MqttQoS;
import io.netty.util.Timeout;
import lombok.Data;

@Data
final class OutboundMessage {

	private final int packetId;

	private final String topic;

	private final byte[] payload;

	private final MqttQoS qos;

	private final boolean isRetain;

	private volatile Timeout timeoutTask;

	private volatile State state;

	public OutboundMessage(int packetId, String topic, byte[] payload, MqttQoS qos, boolean isRetain) {
		this.packetId = packetId;
		this.topic = topic;
		this.payload = payload;
		this.qos = qos;
		this.isRetain = isRetain;
		this.state = qos == MqttQoS.EXACTLY_ONCE ? State.WAIT_PUBREC : State.WAIT_PUBACK;
	}

}
