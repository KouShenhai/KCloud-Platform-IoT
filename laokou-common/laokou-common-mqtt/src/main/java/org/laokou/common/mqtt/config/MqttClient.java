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

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.paho.mqttv5.client.MqttConnectionOptions;
import org.eclipse.paho.mqttv5.client.persist.MemoryPersistence;
import org.eclipse.paho.mqttv5.common.MqttMessage;
import org.eclipse.paho.mqttv5.common.packet.MqttProperties;
import org.laokou.common.i18n.utils.ObjectUtil;

import java.nio.charset.StandardCharsets;

import static java.nio.charset.StandardCharsets.UTF_8;

/**
 * MQTT客户端.
 *
 * @author laokou
 */
@Slf4j
public class MqttClient {

	/**
	 * 服务下线主题.
	 */
	private static final String WILL_TOPIC = "will_topic";

	/**
	 * 服务下线数据.
	 */
	private static final byte[] WILL_DATA = "offline".getBytes(UTF_8);

	private final MqttBrokerProperties mqttBrokerProperties;

	private org.eclipse.paho.mqttv5.client.MqttClient client;

	private final MqttLoadBalancer mqttLoadBalancer;

	public MqttClient(MqttBrokerProperties mqttBrokerProperties, MqttLoadBalancer mqttLoadBalancer) {
		this.mqttBrokerProperties = mqttBrokerProperties;
		this.mqttLoadBalancer = mqttLoadBalancer;
	}

	@SneakyThrows
	public void open() {
		try {
			client = new org.eclipse.paho.mqttv5.client.MqttClient(mqttBrokerProperties.getUri(),
					mqttBrokerProperties.getClientId(), new MemoryPersistence());
			client.setManualAcks(mqttBrokerProperties.isManualAcks());
			client.setCallback(new MqttMessageCallback(mqttLoadBalancer, mqttBrokerProperties, client));
			client.connect(options());
			client.subscribe(mqttBrokerProperties.getTopics().toArray(String[]::new),
					mqttBrokerProperties.getTopics()
						.stream()
						.mapToInt(item -> mqttBrokerProperties.getQos())
						.toArray());
			log.info("MQTT连接成功");
		}
		catch (Exception e) {
			log.error("MQTT连接失败，错误信息：{}", e.getMessage(), e);
		}
	}

	@SneakyThrows
	public void close() {
		if (ObjectUtil.isNotNull(client)) {
			// 等待10秒
			client.disconnectForcibly(10);
			client.close();
			log.info("关闭MQTT连接");
		}
	}

	@SneakyThrows
	public void send(String topic, String payload, int qos) {
		client.publish(topic, payload.getBytes(StandardCharsets.UTF_8), qos, false);
	}

	private MqttConnectionOptions options() {
		MqttConnectionOptions options = new MqttConnectionOptions();
		options.setCleanStart(mqttBrokerProperties.isClearStart());
		options.setUserName(mqttBrokerProperties.getUsername());
		options.setPassword(mqttBrokerProperties.getPassword().getBytes(StandardCharsets.UTF_8));
		options.setReceiveMaximum(mqttBrokerProperties.getReceiveMaximum());
		options.setMaximumPacketSize(mqttBrokerProperties.getMaximumPacketSize());
		options.setWill(WILL_TOPIC,
				new MqttMessage(WILL_DATA, mqttBrokerProperties.getQos(), false, new MqttProperties()));
		// 超时时间
		options.setConnectionTimeout(mqttBrokerProperties.getConnectionTimeout());
		// 会话心跳
		options.setKeepAliveInterval(mqttBrokerProperties.getKeepAliveInterval());
		// 开启重连
		options.setAutomaticReconnect(mqttBrokerProperties.isAutomaticReconnect());
		return options;
	}

}
