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

package org.laokou.common.mqtt.client.config;

import lombok.Data;
import org.laokou.common.core.utils.IdGenerator;

import java.util.HashSet;
import java.util.Set;

/**
 * @author laokou
 */
@Data
public class MqttBrokerProperties {

	private boolean auth = true;

	private String username = "emqx";

	private String password = "laokou123";

	private String host = "127.0.0.1";

	private int port = 1883;

	private String clientId = String.valueOf(IdGenerator.defaultSnowflakeId());

	private int subscribeQos = 0;

	private int publishQos = 0;

	private int willQos = 0;

	private boolean clearStart = false;

	private int receiveMaximum = 65_535;

	private int sendMaximum = 65_535;

	private int maximumPacketSize = 268_435_460;

	private int sendMaximumPacketSize = 268_435_460;

	private int topicAliasMaximum = 32;

	private int sendTopicAliasMaximum = 64;

	private long messageExpiryInterval = Long.MAX_VALUE;

	private boolean requestProblemInformation = true;

	private boolean requestResponseInformation = true;

	/**
	 * 秒.
	 */
	private int connectionTimeout = 15;

	/**
	 * 毫秒.
	 */
	private int automaticReconnectMaxDelay = 100;

	private long sessionExpiryInterval = 30;

	private int keepAliveInterval = 60;

	private boolean automaticReconnect = true;

	private boolean manualAcks = true;

	private Set<String> topics = new HashSet<>(0);

}
