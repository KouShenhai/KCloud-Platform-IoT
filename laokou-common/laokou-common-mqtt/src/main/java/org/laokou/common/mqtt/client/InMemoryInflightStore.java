/*
 * Copyright (c) 2022-2026 KCloud-Platform-IoT Author or Authors. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package org.laokou.common.mqtt.client;

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
