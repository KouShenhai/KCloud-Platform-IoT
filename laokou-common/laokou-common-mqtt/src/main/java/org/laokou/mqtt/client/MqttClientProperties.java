/*
 * Copyright (c) 2022-2026 KCloud-Platform-IoT Author or Authors. All Rights Reserved.
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

package org.laokou.mqtt.client;

import lombok.Data;

/**
 * MQTT 客户端配置.
 *
 * @author laokou
 */
@Data
final class MqttClientProperties {

	private String tenantCode;

	/**
	 * 主机.
	 */
	private String host = "127.0.0.1";

	/**
	 * 端口.
	 */
	private int port = 1883;

	/**
	 * 用户名。
	 */
	private String username = "root";

	/**
	 * 密码。
	 */
	private String password = "laokou123";

	/**
	 * 清除会话.
	 */
	private boolean cleanSession = true;

	/**
	 * 心跳包每隔60秒发一次.
	 */
	private int keepAliveTimeSeconds = 60;

	/**
	 * 连接超时。
	 */
	private int connectTimeoutMs = 15000;

	/**
	 * 最大消息字节数。
	 */
	private int maxBytesInMessage = 64 * 1024 * 1024;

	/**
	 * 读写核心线程数.
	 */
	private int ioThreads = 32;

	/**
	 * QoS1/2 单条消息 ACK 超时（毫秒）
	 */
	private long ackTimeoutMs = 5000;

}
