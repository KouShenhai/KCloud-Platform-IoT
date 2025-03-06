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

package org.laokou.test.mqtt.config;

import io.micrometer.common.lang.NonNullApi;
import jakarta.annotation.PreDestroy;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.paho.mqttv5.common.MqttException;
import org.laokou.common.i18n.common.exception.SystemException;
import org.laokou.common.mqtt.config.MessageHandler;
import org.laokou.common.mqtt.config.MqttBrokerProperties;
import org.laokou.common.mqtt.config.MqttClientManager;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author laokou
 */
@Slf4j
@Component
@NonNullApi
@RequiredArgsConstructor
public class MqttConfig implements ApplicationListener<ApplicationReadyEvent> {

	private final List<MessageHandler> messageHandlers;

	@Override
	public void onApplicationEvent(ApplicationReadyEvent event) {
		MqttBrokerProperties properties = new MqttBrokerProperties();
		properties.setPublishQos(2);
		properties.setSubscribeQos(2);
		MqttClientManager.add(properties.getClientId(), properties, messageHandlers);
		// 启动MQTT客户端
		MqttClientManager.open(properties.getClientId());
		try {
			Thread.sleep(5000);
		}
		catch (InterruptedException e) {
			throw new RuntimeException(e);
		}
		// 订阅MQTT主题
		try {
			MqttClientManager.subscribe(properties.getClientId(), new String[] { "test" }, new int[] { 2 });
		}
		catch (MqttException e) {
			log.error("订阅MQTT主题失败，错误信息：{}", e.getMessage());
			throw new SystemException("S_UnKnow_Error", e.getMessage(), e);
		}
		try {
			Thread.sleep(5000);
		}
		catch (InterruptedException e) {
			throw new RuntimeException(e);
		}
		// 发送MQTT消息
		for (int i = 0; i < 1000; i++) {
			try {
				Thread.sleep(100);
				MqttClientManager.get(properties.getClientId()).publish("test", "hello".getBytes(), 2);
			}
			catch (MqttException | InterruptedException e) {
				log.error("发送MQTT消息失败，错误信息：{}", e.getMessage());
				throw new SystemException("S_UnKnow_Error", e.getMessage(), e);
			}
		}
	}

	@PreDestroy
	public void destroy() {
		MqttClientManager.preDestroy();
	}

}
