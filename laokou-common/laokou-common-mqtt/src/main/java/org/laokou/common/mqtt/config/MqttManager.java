/*
 * Copyright (c) 2022-2025 KCloud-Platform-IoT Author or Authors. All Rights Reserved.
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

package org.laokou.common.mqtt.config;

import lombok.RequiredArgsConstructor;
import org.laokou.common.core.utils.ThreadUtil;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ScheduledExecutorService;

/**
 * @author laokou
 */
@RequiredArgsConstructor
public class MqttManager {

	private static final Map<String, MqttClient> MQTT_SESSION_MAP = new ConcurrentHashMap<>(4096);

	private final SpringMqttBrokerProperties springMqttBrokerProperties;

	private final MqttLoadBalancer mqttLoadBalancer;

	private final ScheduledExecutorService executor = ThreadUtil.newScheduledThreadPool(32);

	public MqttClient getSession(String key) {
		return MQTT_SESSION_MAP.get(key);
	}

	public synchronized void open() {
		for (Map.Entry<String, MqttBrokerProperties> entry : springMqttBrokerProperties.getConfigs().entrySet()) {
			MqttClient client = new MqttClient(entry.getValue(), mqttLoadBalancer, executor);
			MQTT_SESSION_MAP.putIfAbsent(entry.getKey(), client);
		}
		MQTT_SESSION_MAP.values().forEach(MqttClient::open);
	}

	public synchronized void close() {
		MQTT_SESSION_MAP.values().forEach(MqttClient::close);
	}

}
