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

import static java.nio.charset.StandardCharsets.UTF_8;

/**
 * @author laokou
 */
public abstract class AbstractMqttClient implements MqttClient {

	/**
	 * 服务下线主题.
	 */
	protected String WILL_TOPIC = "will_topic";

	/**
	 * 服务下线数据.
	 */
	protected byte[] WILL_DATA = "offline".getBytes(UTF_8);

	protected void checkTopicAndQos(String[] topics, int[] qos, String name) {
		if (topics == null || qos == null) {
			throw new IllegalArgumentException("【" + name + "】 => Topics and QoS arrays cannot be null");
		}
		if (topics.length != qos.length) {
			throw new IllegalArgumentException("【" + name + "】 => Topics and QoS arrays must have the same length");
		}
		if (topics.length == 0) {
			throw new IllegalArgumentException("【" + name + "】 => Topics array cannot be empty");
		}
	}

	protected void checkTopic(String[] topics, String name) {
		if (topics.length == 0) {
			throw new IllegalArgumentException("【" + name + "】 => Topics array cannot be empty");
		}
	}

}
