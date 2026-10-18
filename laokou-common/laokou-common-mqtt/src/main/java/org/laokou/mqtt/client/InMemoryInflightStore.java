package org.laokou.mqtt.client;

import java.util.concurrent.atomic.AtomicReferenceArray;

/**
 * @author laokou
 */
class InMemoryInflightStore implements InflightStore {

	private final AtomicReferenceArray<OutboundMessage> slots = new AtomicReferenceArray<>(0xFFFF);

	@Override
	public boolean putIfAbsent(OutboundMessage msg) {
		return slots.compareAndSet(msg.getPacketId(), null, msg);
	}

	@Override
	public OutboundMessage get(int packetId) {
		return slots.get(packetId);
	}

	@Override
	public OutboundMessage remove(int packetId) {
		return slots.getAndSet(packetId, null);
	}

}
