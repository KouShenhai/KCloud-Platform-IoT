/*
 * Copyright (c) 2022-2024 KCloud-Platform-IoT Author or Authors. All Rights Reserved.
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

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author laokou
 */
@RequiredArgsConstructor
public class MqttManager {

	private final SpringMqttBrokerProperties springMqttBrokerProperties;

	private final MqttLoadBalancer mqttLoadBalancer;

	private static final Map<String, MqttClient> MQTT_SESSION_MAP = new ConcurrentHashMap<>();

	public MqttClient getSession(String key) {
		return MQTT_SESSION_MAP.get(key);
	}

	public synchronized void open() {
		springMqttBrokerProperties.getConfigs().forEach((k, v) -> {
			MqttClient client = new MqttClient(v, mqttLoadBalancer);
			client.open();
			MQTT_SESSION_MAP.put(k, client);
		});
	}

	public synchronized void close() {
		springMqttBrokerProperties.getConfigs().forEach((k, v) -> {
			MqttClient client = new MqttClient(v, mqttLoadBalancer);
			client.close();
			MQTT_SESSION_MAP.remove(k);
		});
	}

}
