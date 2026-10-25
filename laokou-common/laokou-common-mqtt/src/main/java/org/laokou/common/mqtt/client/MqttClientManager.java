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

import org.laokou.common.mqtt.client.listener.MessageListener;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author laokou
 */
public class MqttClientManager implements AutoCloseable {

	private final Map<Long, MqttClient> clients = new ConcurrentHashMap<>();

	public void open(Long id, MqttClientProperties properties, MqttEventLoop mqttEventLoop,
			List<MessageListener> listeners) {
		clients.computeIfAbsent(id, _ -> new NettyMqttClient(properties, mqttEventLoop, listeners)).connect();
	}

	public void close(Long id) {
		clients.remove(id).disconnect();
	}

	@Override
	public void close() {
		clients.keySet().forEach(this::close);
	}

}
