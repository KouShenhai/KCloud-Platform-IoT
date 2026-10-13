package org.laokou.mqtt.client;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author laokou
 */
final class PacketIdGenerator {

	private final AtomicInteger next = new AtomicInteger(0);

	public int nextId() {
		return next.updateAndGet(v -> (v & 0xFFFF) + 1);
	}

}
