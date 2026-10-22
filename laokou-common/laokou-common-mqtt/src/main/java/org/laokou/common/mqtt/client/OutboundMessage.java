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

import io.netty.handler.codec.mqtt.MqttQoS;
import io.netty.util.Timeout;
import lombok.Data;

/**
 * @author laokou
 */
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
