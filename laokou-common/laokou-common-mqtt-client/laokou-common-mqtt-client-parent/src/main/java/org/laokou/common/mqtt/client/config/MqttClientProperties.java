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
import org.laokou.common.core.util.UUIDGenerator;

import java.util.HashSet;
import java.util.Set;

/**
 * @author laokou
 */
@Data
public class MqttClientProperties {

	private boolean auth = true;

	private String username = "emqx";

	private String password = "laokou123";

	private String host = "127.0.0.1";

	private int port = 1883;

	private String clientId = UUIDGenerator.generateUUID();

	private int subscribeQos = 1;

	private int publishQos = 0;

	private int willQos = 1;

	// @formatter:off
	/**
	 * 控制是否创建新会话（true=新建，false=复用历史会话）. clearStart=true => Broker 会在连接断开后立即清除所有会话信息.
	 * clearStart=false => Broker 会在连接断开后保存会话信息，并在重新连接后复用会话信息.
	 * <a href="https://github.com/hivemq/hivemq-mqtt-client/issues/627">...</a>
	 */
	// @formatter:on
	private boolean clearStart = false;

	private int receiveMaximum = 10000;

	private int sendMaximum = 10000;

	private int maximumPacketSize = 10000;

	private int sendMaximumPacketSize = 10000;

	private int topicAliasMaximum = 1024;

	private int sendTopicAliasMaximum = 2048;

	private long messageExpiryInterval = 86400L;

	private boolean requestProblemInformation = true;

	private boolean requestResponseInformation = true;

	/**
	 * 秒.
	 */
	private int connectionTimeout = 30;

	// @formatter:off
	/**
	 * 默认会话保留一天.
	 * 最大值，4294967295L，会话过期时间【永不过期，单位秒】.
	 * 定义客户端断开后会话保留的时间（仅在 Clean Session = false 时生效）.
	 */
	private long sessionExpiryInterval = 86400L;
	// @formatter:on

	/**
	 * 心跳包每隔60秒发一次.
	 */
	private int keepAliveInterval = 60;

	private boolean automaticReconnect = true;

	private long automaticReconnectMaxDelay = 5;

	private long automaticReconnectInitialDelay = 1;

	private boolean manualAcks = false;

	private Set<String> topics = new HashSet<>(0);

	private int nettyThreads = 32;

	private boolean retain = false;

	private boolean noLocal = false;

}
