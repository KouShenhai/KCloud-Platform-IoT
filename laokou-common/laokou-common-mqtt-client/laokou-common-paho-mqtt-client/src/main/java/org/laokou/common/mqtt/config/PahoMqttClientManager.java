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
import org.eclipse.paho.mqttv5.common.MqttException;
import org.laokou.common.core.util.ThreadUtils;
import org.laokou.common.mqtt.client.config.MqttClientProperties;
import org.laokou.common.mqtt.client.handler.MessageHandler;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ScheduledExecutorService;

/**
 * @author laokou
 */
@Slf4j
@RequiredArgsConstructor
public final class PahoMqttClientManager {

	private static final Map<String, PahoMqttClient> PAHO_MQTT_CLIENT_MAP = new ConcurrentHashMap<>(4096);

	private static final ScheduledExecutorService EXECUTOR = ThreadUtils.newScheduledThreadPool(32);

	public static PahoMqttClient get(String clientId) {
		if (PAHO_MQTT_CLIENT_MAP.containsKey(clientId)) {
			return PAHO_MQTT_CLIENT_MAP.get(clientId);
		}
		throw new IllegalArgumentException("【Paho】 => MQTT客户端不存在");
	}

	public static void remove(String clientId) {
		close(clientId);
		PAHO_MQTT_CLIENT_MAP.remove(clientId);
	}

	public static void add(String clientId, MqttClientProperties properties, List<MessageHandler> messageHandlers) {
		PAHO_MQTT_CLIENT_MAP.putIfAbsent(clientId, new PahoMqttClient(properties, messageHandlers, EXECUTOR));
	}

	public static void open(String clientId) {
		get(clientId).open();
	}

	public static void close(String clientId) {
		get(clientId).close();
	}

	public static void subscribe(String clientId, String[] topics, int[] qos) throws MqttException {
		get(clientId).subscribe(topics, qos);
	}

	public static void unsubscribe(String clientId, String[] topics) throws MqttException {
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

	public static void destroy() {
		log.info("【Paho】 => MQTT客户端销毁开始执行");
		PAHO_MQTT_CLIENT_MAP.values().forEach(PahoMqttClient::close);
		PAHO_MQTT_CLIENT_MAP.clear();
		log.info("【Paho】 => MQTT客户端销毁执行完毕");
	}

}
