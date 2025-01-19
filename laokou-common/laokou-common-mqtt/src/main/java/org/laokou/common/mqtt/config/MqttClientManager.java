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

import jakarta.annotation.PreDestroy;
import lombok.RequiredArgsConstructor;
import org.eclipse.paho.mqttv5.common.MqttException;
import org.laokou.common.core.utils.ThreadUtil;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ScheduledExecutorService;

/**
 * @author laokou
 */
@RequiredArgsConstructor
public class MqttClientManager {

	private static final Map<String, MqttClient> MQTT_CLIENT_MAP = new ConcurrentHashMap<>(4096);

	private static final ScheduledExecutorService EXECUTOR = ThreadUtil.newScheduledThreadPool(32);

	public static MqttClient get(String clientId) {
		return MQTT_CLIENT_MAP.get(clientId);
	}

	public static void remove(String clientId) {
		MQTT_CLIENT_MAP.remove(clientId);
	}

	public static void add(String clientId, MqttBrokerProperties properties,
			MqttClientLoadBalancer mqttClientLoadBalancer) {
		MQTT_CLIENT_MAP.putIfAbsent(clientId, new MqttClient(properties, mqttClientLoadBalancer, EXECUTOR));
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

	@PreDestroy
	public void shutdown() {
		ThreadUtil.shutdown(EXECUTOR, 60);
		MQTT_CLIENT_MAP.values().forEach(MqttClient::close);
		MQTT_CLIENT_MAP.clear();
	}

}
