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

package org.laokou.common.network.mqtt.client.util;

import io.netty.handler.codec.mqtt.MqttQoS;

import java.util.regex.Pattern;

/**
 * @author laokou
 */
public final class VertxMqttUtils {

	private VertxMqttUtils() {
	}

	public static boolean matchTopic(String subscribeTopic, String publishTopic) {
		if (subscribeTopic.equals(publishTopic)) {
			return true;
		}
		String regex = subscribeTopic.replace("+", "[^/]+").replace("#", ".+");
		return Pattern.matches(regex, publishTopic);
	}

	public static MqttQoS convertQos(int qos) {
		return MqttQoS.valueOf(qos);
	}

}
