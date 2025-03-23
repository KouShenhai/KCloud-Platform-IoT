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
import org.laokou.common.i18n.common.exception.SystemException;
import org.laokou.common.mqtt.client.config.MqttBrokerProperties;
import org.laokou.common.mqtt.client.handler.MessageHandler;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;

/**
 * @author laokou
 */
@RequiredArgsConstructor
public class HivemqMqttClientManager {

	private static final Map<String, HivemqMqttClient> PAHO_MQTT_CLIENT_MAP = new ConcurrentHashMap<>(4096);

	public static HivemqMqttClient get(String clientId) {
		if (PAHO_MQTT_CLIENT_MAP.containsKey(clientId)) {
			return PAHO_MQTT_CLIENT_MAP.get(clientId);
		}
		throw new SystemException("S_Mqtt_NotExist", "MQTT客户端不存在");
	}

	public static void remove(String clientId) {
		close(clientId);
		PAHO_MQTT_CLIENT_MAP.remove(clientId);
	}

	public static void add(String clientId, MqttBrokerProperties properties, List<MessageHandler> messageHandlers,
			ExecutorService virtualThreadExecutor) {
		PAHO_MQTT_CLIENT_MAP.putIfAbsent(clientId,
				new HivemqMqttClient(properties, messageHandlers, virtualThreadExecutor));
	}

	public static void open(String clientId) {
		get(clientId).open();
	}

	public static void close(String clientId) {
		get(clientId).close();
	}

	public static void subscribe(String clientId, String[] topics, int[] qos) {
		get(clientId).subscribe(topics, qos);
	}

	public static void consume(String clientId) {
		get(clientId).consume();
	}

	public static void unsubscribe(String clientId, String[] topics) {
		get(clientId).unsubscribe(topics);
	}

	public static void publishSubscribeEvent(String clientId, Set<String> topics, int qos) {
		get(clientId).publishSubscribeEvent(topics, qos);
	}

	public static void publishUnsubscribeEvent(String clientId, Set<String> topics) {
		get(clientId).publishUnsubscribeEvent(topics);
	}

	public static void publishOpenEvent(String clientId) {
		get(clientId).publishOpenEvent(clientId);
	}

	public static void publishCloseEvent(String clientId) {
		get(clientId).publishCloseEvent(clientId);
	}

	public static void preDestroy(ExecutorService virtualThreadExecutor) {
		PAHO_MQTT_CLIENT_MAP.values().forEach(HivemqMqttClient::close);
		PAHO_MQTT_CLIENT_MAP.clear();
		ThreadUtil.shutdown(virtualThreadExecutor, 60);
	}

}
