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
import lombok.extern.slf4j.Slf4j;
import org.laokou.common.mqtt.client.config.MqttClientProperties;
import org.laokou.common.mqtt.client.handler.MessageHandler;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author laokou
 */
@Slf4j
@RequiredArgsConstructor
public final class HivemqMqttClientManager {

	private static final Map<String, HivemqMqttClient> HIVE_MQTT_CLIENT_MAP = new ConcurrentHashMap<>(4096);

	public static HivemqMqttClient get(String clientId) {
		if (HIVE_MQTT_CLIENT_MAP.containsKey(clientId)) {
			return HIVE_MQTT_CLIENT_MAP.get(clientId);
		}
		throw new IllegalArgumentException("【HiveMQ】 => MQTT客户端不存在");
	}

	public static void remove(String clientId) {
		HIVE_MQTT_CLIENT_MAP.remove(clientId);
	}

	public static void add(String clientId, MqttClientProperties properties, List<MessageHandler> messageHandlers) {
		HIVE_MQTT_CLIENT_MAP.putIfAbsent(clientId, new HivemqMqttClient(properties, messageHandlers));
	}

	public static void open() {
		HIVE_MQTT_CLIENT_MAP.values().forEach(HivemqMqttClient::open);
	}

	public static void publish(String clientId, String topic, int qos, byte[] payload) {
		HIVE_MQTT_CLIENT_MAP.get(clientId).publish(topic, qos, payload);
	}

	public static void destroy() {
		log.info("【Hivemq】 => MQTT客户端销毁开始执行");
		HIVE_MQTT_CLIENT_MAP.values().forEach(HivemqMqttClient::close);
		HIVE_MQTT_CLIENT_MAP.clear();
		log.info("【Hivemq】 => MQTT客户端销毁执行完毕");
	}

}
