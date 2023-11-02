/*
 * Copyright (c) 2022 KCloud-Platform-Alibaba Authors. All Rights Reserved.
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

package org.laokou.mqtt.config;

import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.mqtt.core.DefaultMqttPahoClientFactory;
import org.springframework.integration.mqtt.core.MqttPahoClientFactory;

import static org.laokou.mqtt.constant.Constant.WILL_DATA;
import static org.laokou.mqtt.constant.Constant.WILL_TOPIC;

/**
 * @author laokou
 */
@Configuration
public class MqttConfig {

	@Bean
	public MqttPahoClientFactory mqttPahoClientFactory(MqttProperties mqttProperties) {
		DefaultMqttPahoClientFactory clientFactory = new DefaultMqttPahoClientFactory();
		clientFactory.setConnectionOptions(mqttConnectOptions(mqttProperties));
		return clientFactory;
	}

	private MqttConnectOptions mqttConnectOptions(MqttProperties mqttProperties) {
		MqttConnectOptions options = new MqttConnectOptions();
		// 超时时间
		options.setConnectionTimeout(10);
		// 会话心跳
		options.setKeepAliveInterval(15);
		options.setUserName(mqttProperties.getUsername());
		// 开启重连
		options.setAutomaticReconnect(true);
		options.setPassword(mqttProperties.getPassword().toCharArray());
		options.setServerURIs(new String[] { mqttProperties.getHost() });
		// 客户端与服务器意外中断,服务器发送`遗嘱`消息(只有一次)
		options.setWill(WILL_TOPIC, WILL_DATA, 2, false);
		return options;
	}

}
