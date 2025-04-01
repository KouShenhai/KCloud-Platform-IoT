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

package org.laokou.common.mqtt.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.paho.mqttv5.client.IMqttToken;
import org.eclipse.paho.mqttv5.client.MqttCallback;
import org.eclipse.paho.mqttv5.client.MqttDisconnectResponse;
import org.eclipse.paho.mqttv5.common.MqttException;
import org.eclipse.paho.mqttv5.common.MqttMessage;
import org.eclipse.paho.mqttv5.common.packet.MqttProperties;
import org.laokou.common.mqtt.client.config.MqttClientProperties;
import org.laokou.common.mqtt.client.handler.MessageHandler;

import java.util.List;

/**
 * @author laokou
 */
@Slf4j
@RequiredArgsConstructor
public class PahoMqttClientMessageCallback implements MqttCallback {

	private final List<MessageHandler> messageHandlers;

	private final MqttClientProperties mqttClientProperties;

	@Override
	public void disconnected(MqttDisconnectResponse disconnectResponse) {
		log.error("【Paho】 => MQTT关闭连接");
	}

	@Override
	public void mqttErrorOccurred(MqttException ex) {
		log.error("【Paho】 => MQTT报错，错误信息：{}", ex.getMessage());
	}

	@Override
	public void messageArrived(String topic, MqttMessage message) {
		for (MessageHandler messageHandler : messageHandlers) {
			if (messageHandler.isSubscribe(topic)) {
				messageHandler.handle(new org.laokou.common.mqtt.client.MqttMessage(message.getPayload(), topic));
				break;
			}
		}
	}

	@Override
	public void deliveryComplete(IMqttToken token) {
		log.info("【Paho】 => MQTT消息发送成功，消息ID：{}", token.getMessageId());
	}

	@Override
	public void connectComplete(boolean reconnect, String uri) {
		if (reconnect) {
			log.info("【Paho】 => MQTT重连成功，URI：{}", uri);
		}
		else {
			log.info("【Paho】 => MQTT建立连接，URI：{}", uri);
		}
	}

	@Override
	public void authPacketArrived(int reasonCode, MqttProperties properties) {
		log.info("【Paho】 => 接收到身份验证数据包：{}", reasonCode);
	}

}
