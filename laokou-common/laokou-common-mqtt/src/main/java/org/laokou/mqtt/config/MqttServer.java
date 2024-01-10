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
import lombok.extern.slf4j.Slf4j;
import org.eclipse.paho.mqttv5.client.MqttClient;
import org.eclipse.paho.mqttv5.client.MqttConnectionOptions;
import org.eclipse.paho.mqttv5.client.persist.MemoryPersistence;
import org.eclipse.paho.mqttv5.common.MqttMessage;
import org.eclipse.paho.mqttv5.common.packet.MqttProperties;
import org.laokou.common.core.utils.IdGenerator;
import org.laokou.common.i18n.utils.ObjectUtil;

import java.nio.charset.StandardCharsets;

import static org.laokou.common.i18n.common.MqttConstants.WILL_DATA;
import static org.laokou.common.i18n.common.MqttConstants.WILL_TOPIC;

/**
 * @author laokou
 */
@Slf4j
public class MqttServer implements Server {

	private volatile boolean running;

	private volatile MqttClient client;

	public static final Long CLIENT_ID = IdGenerator.defaultSnowflakeId();

	private final SpringMqttProperties springMqttProperties;

	private final MqttStrategy mqttStrategy;

	public MqttServer(SpringMqttProperties springMqttProperties, MqttStrategy mqttStrategy) {
		this.springMqttProperties = springMqttProperties;
		this.mqttStrategy = mqttStrategy;
	}

	@Override
	@SneakyThrows
	public synchronized void start() {
		if (running) {
			log.error("MQTT已启动");
			return;
		}
		client = new MqttClient(springMqttProperties.getHost(), CLIENT_ID.toString(), new MemoryPersistence());
		// 手动ack接收确认
		client.setManualAcks(springMqttProperties.isManualAcks());
		client.setCallback(new MqttMessageCallback(client, mqttStrategy));
		client.connect(options());
		client.subscribe(springMqttProperties.getTopics().toArray(String[]::new), new int[] { 2 });
		running = true;
		log.info("MQTT启动成功");
	}

	@Override
	@SneakyThrows
	public synchronized void stop() {
		running = false;
		if (ObjectUtil.isNotNull(client)) {
			client.disconnectForcibly();
		}
		log.info("关闭MQTT");
	}

	@Override
	@SneakyThrows
	public void send(String topic, String payload) {
		client.publish(topic, payload.getBytes(StandardCharsets.UTF_8), 2, false);
	}

	private MqttConnectionOptions options() {
		MqttConnectionOptions options = new MqttConnectionOptions();
		options.setCleanStart(springMqttProperties.isClearStart());
		options.setUserName(springMqttProperties.getUsername());
		options.setPassword(springMqttProperties.getPassword().getBytes(StandardCharsets.UTF_8));
		options.setReceiveMaximum(springMqttProperties.getReceiveMaximum());
		options.setMaximumPacketSize(springMqttProperties.getMaximumPacketSize());
		options.setWill(WILL_TOPIC, new MqttMessage(WILL_DATA, 2, false, new MqttProperties()));
		// 超时时间
		options.setConnectionTimeout(springMqttProperties.getConnectionTimeout());
		// 会话心跳
		options.setKeepAliveInterval(springMqttProperties.getKeepAliveInterval());
		// 开启重连
		options.setAutomaticReconnect(springMqttProperties.isAutomaticReconnect());
		return options;
	}

}
