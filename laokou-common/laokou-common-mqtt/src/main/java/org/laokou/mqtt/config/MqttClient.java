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

package org.laokou.mqtt.config;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.paho.mqttv5.client.MqttConnectionOptions;
import org.eclipse.paho.mqttv5.client.persist.MemoryPersistence;
import org.eclipse.paho.mqttv5.common.MqttMessage;
import org.eclipse.paho.mqttv5.common.packet.MqttProperties;
import org.laokou.common.core.utils.CollectionUtil;
import org.laokou.common.i18n.utils.ObjectUtil;
import org.laokou.common.netty.config.Client;

import java.nio.charset.StandardCharsets;
import java.util.concurrent.atomic.AtomicInteger;

import static java.nio.charset.StandardCharsets.UTF_8;

/**
 * MQTT客户端.
 *
 * @author laokou
 */
@Slf4j
public class MqttClient implements Client {

	/**
	 * 服务停止前的消息主题。
	 */
	private static final String WILL_TOPIC = "will_topic";

	/**
	 * 服务下线。
	 */
	private static final byte[] WILL_DATA = "offline".getBytes(UTF_8);

	private volatile boolean running;

	private volatile org.eclipse.paho.mqttv5.client.MqttClient client;

	private final SpringMqttProperties springMqttProperties;

	private final MqttStrategy mqttStrategy;

	private final AtomicInteger ATOMIC = new AtomicInteger(0);

	public MqttClient(SpringMqttProperties springMqttProperties, MqttStrategy mqttStrategy) {
		this.springMqttProperties = springMqttProperties;
		this.mqttStrategy = mqttStrategy;
	}

	@Override
	@SneakyThrows
	public synchronized void open() {
		if (running) {
			log.error("MQTT已连接");
			return;
		}
		try {
			client = new org.eclipse.paho.mqttv5.client.MqttClient(springMqttProperties.getHost(),
					springMqttProperties.getClientId(), new MemoryPersistence());
			// 手动ack接收确认
			client.setManualAcks(springMqttProperties.isManualAcks());
			client.setCallback(new MqttMessageCallback(mqttStrategy));
			client.connect(options());
			if (CollectionUtil.isNotEmpty(springMqttProperties.getTopics())) {
				client.subscribe(springMqttProperties.getTopics().toArray(String[]::new),
						springMqttProperties.getTopics().stream().mapToInt(item -> 2).toArray());
			}
			log.info("MQTT连接成功");
			running = true;
			ATOMIC.set(0);
		}
		catch (Exception e) {
			// 最大重试10次
			if (ATOMIC.incrementAndGet() <= 10) {
				// 5秒后重连
				Thread.sleep(5000);
				open();
			}
		}
	}

	@Override
	@SneakyThrows
	public synchronized void close() {
		running = false;
		if (ObjectUtil.isNotNull(client)) {
			// 等待10秒
			client.disconnectForcibly(10);
			client.close();
		}
		log.info("关闭MQTT连接");
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
