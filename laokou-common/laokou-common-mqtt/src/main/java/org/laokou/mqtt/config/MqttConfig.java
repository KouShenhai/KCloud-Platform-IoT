/*
 * Copyright (c) 2022 KCloud-Platform-Alibaba Author or Authors. All Rights Reserved.
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

import lombok.SneakyThrows;
import org.eclipse.paho.mqttv5.client.MqttClient;
import org.eclipse.paho.mqttv5.client.MqttConnectionOptions;
import org.eclipse.paho.mqttv5.client.persist.MemoryPersistence;
import org.eclipse.paho.mqttv5.common.MqttMessage;
import org.eclipse.paho.mqttv5.common.packet.MqttProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.nio.charset.StandardCharsets;

import static org.laokou.mqtt.constant.Constant.WILL_DATA;
import static org.laokou.mqtt.constant.Constant.WILL_TOPIC;

/**
 * @author laokou
 */
@Configuration
public class MqttConfig {

	@Bean
	@SneakyThrows
	public MqttClient mqttClient(SpringMqttProperties springMqttProperties) {
		MemoryPersistence persistence = new MemoryPersistence();
		MqttClient client = new MqttClient(springMqttProperties.getHost(), "123",persistence);
		// 手动ack接收确认
		client.setManualAcks(true);
		client.connect(mqttConnectionOptions(springMqttProperties));
		return client;
	}

	@Bean
	public MqttConnectionOptions mqttConnectionOptions(SpringMqttProperties springMqttProperties) {
		MqttConnectionOptions options = new MqttConnectionOptions();
		// 超时时间
		options.setConnectionTimeout(10);
		// 会话心跳
		options.setKeepAliveInterval(15);
		options.setUserName(springMqttProperties.getUsername());
		// 开启重连
		options.setAutomaticReconnect(true);
		options.setPassword(springMqttProperties.getPassword().getBytes(StandardCharsets.UTF_8));
		options.setServerURIs(new String[] { springMqttProperties.getHost() });
		// 客户端与服务器意外中断,服务器发送`遗嘱`消息(只有一次)
		options.setWill(WILL_TOPIC, new MqttMessage(WILL_DATA,2,false,new MqttProperties()));
		return options;
	}

}
