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

package org.laokou.common.mqtt.client;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author laokou
 */
public class MqttClientManager {

	private static final Map<String, MqttClient> MQTT_CLIENT_MAP = new ConcurrentHashMap<>(4096);

	public static MqttClient get(String clientId) {
		if (MQTT_CLIENT_MAP.containsKey(clientId)) {
			return MQTT_CLIENT_MAP.get(clientId);
		}
		return MQTT_CLIENT_MAP.computeIfAbsent(clientId, c -> create());
	}

	public static void remove(String clientId) {
		MQTT_CLIENT_MAP.remove(clientId);
	}

	public static void add(String clientId) {

	}

	public static void open(String clientId) {
	}

	public static void close(String clientId) {

	}

	public static void subscribe(String clientId, String[] topics, int[] qos) {
	}

	public static void unsubscribe(String clientId, String[] topics) {

	}

	public static void publishSubscribeEvent(String clientId, Set<String> topics, int qos) {
	}

	public static void publishUnsubscribeEvent(String clientId, Set<String> topics) {

	}

	public static void publishOpenEvent(String clientId) {
	}

	public static void publishCloseEvent(String clientId) {
	}

	public static void preDestroy() {

	}

	protected static MqttClient create() {
		return null;
	}

}
