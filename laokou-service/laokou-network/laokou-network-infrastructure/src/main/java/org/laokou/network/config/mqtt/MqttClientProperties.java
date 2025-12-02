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

package org.laokou.network.config.mqtt;

import lombok.Data;
import org.laokou.common.core.util.MapUtils;
import org.laokou.common.core.util.UUIDGenerator;

import java.util.Map;

/**
 * @author laokou
 */
@Data
public class MqttClientProperties {

	private boolean auth = true;

	private boolean subscribe = true;

	private String username = "emqx";

	private String password = "laokou123";

	private String host = "127.0.0.1";

	private int port = 1883;

	private String clientId = UUIDGenerator.generateUUID();

	// @formatter:off
	/**
	 * 控制是否创建新会话（true=新建，false=复用历史会话）. clearStart=true => Broker 会在连接断开后立即清除所有会话信息.
	 * clearStart=false => Broker 会在连接断开后保存会话信息，并在重新连接后复用会话信息.
	 */
	// @formatter:on
	private boolean clearSession = false;

	private int receiveBufferSize = Integer.MAX_VALUE;

	private int maxMessageSize = -1;

	/**
	 * 心跳包每隔60秒发一次.
	 */
	private int keepAliveInterval = 60;

	private boolean autoKeepAlive = true;

	private long reconnectInterval = 1000;

	private int reconnectAttempts = Integer.MAX_VALUE;

	private Map<String, Integer> topics = MapUtils.newHashMap(0);

	private int willQos = 1;

	private boolean willRetain = false;

	private int ackTimeout = -1;

	private boolean autoAck = true;

	/**
	 * 服务下线主题.
	 */
	private String willTopic = "/will";

	/**
	 * 服务下线数据.
	 */
	private String willPayload = "offline";

}
