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

import lombok.Data;
import org.laokou.common.i18n.utils.IdGenerator;

import java.util.HashSet;
import java.util.Set;

import static org.laokou.common.i18n.common.constant.StringConstant.EMPTY;

/**
 * @author laokou
 */
@Data
public class MqttBrokerProperties {

	private String username = EMPTY;

	private String password = EMPTY;

	private String uri = "tcp://127.0.0.1:1883";

	private String clientId = String.valueOf(IdGenerator.defaultSnowflakeId());

	private int subscribeQos = 0;

	private int sendQos = 1;

	private boolean clearStart = false;

	private int receiveMaximum = 5;

	private long maximumPacketSize = 1024;

	private int connectionTimeout = 10;

	private int keepAliveInterval = 15;

	private boolean automaticReconnect = true;

	private boolean manualAcks = true;

	private Set<String> topics = new HashSet<>(0);

}
