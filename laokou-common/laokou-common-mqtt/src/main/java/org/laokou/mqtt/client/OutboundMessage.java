package org.laokou.mqtt.client;

import io.netty.handler.codec.mqtt.MqttQoS;
import io.netty.util.Timeout;
import lombok.Data;

import java.util.concurrent.atomic.AtomicInteger;

@Data
public final class OutboundMessage {

	public enum State {

		/** QoS1：等待 PUBACK */
		WAIT_PUBACK,
		/** QoS2：已发 PUBLISH，等待 PUBREC */
		WAIT_PUBREC,
		/** QoS2：已发 PUBREL，等待 PUBCOMP */
		WAIT_PUBCOMP
	}

	private final int packetId;

	private final String topic;

	private final byte[] payload;

	private final MqttQoS qos;

	private final boolean isRetain;

	private volatile boolean isDup;

	private volatile Timeout timeoutTask;

	private volatile AtomicInteger retries;

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
