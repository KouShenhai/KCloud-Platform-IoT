/*
 * Copyright (c) 2022-2024 KCloud-Platform-IOT Author or Authors. All Rights Reserved.
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

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.paho.mqttv5.client.IMqttToken;
import org.eclipse.paho.mqttv5.client.MqttCallback;
import org.eclipse.paho.mqttv5.client.MqttClient;
import org.eclipse.paho.mqttv5.client.MqttDisconnectResponse;
import org.eclipse.paho.mqttv5.common.MqttException;
import org.eclipse.paho.mqttv5.common.MqttMessage;
import org.eclipse.paho.mqttv5.common.packet.MqttProperties;

/**
 * @author laokou
 */
@Slf4j
@RequiredArgsConstructor
public class MqttMessageCallback implements MqttCallback {

	private final MqttClient client;

	private final MqttStrategy mqttStrategy;

	@Override
	public void disconnected(MqttDisconnectResponse disconnectResponse) {
		log.info("MQTT关闭连接");
	}

	@Override
	public void mqttErrorOccurred(MqttException ex) {
		log.error("MQTT报错", ex);
	}

	@Override
	public void messageArrived(String topic, MqttMessage message) {
		mqttStrategy.get(topic).onMessage(message);
	}

	@Override
	public void deliveryComplete(IMqttToken token) {
		log.info("MQTT消息发送成功");
	}

	@Override
	public void connectComplete(boolean reconnect, String uri) {
		log.info("MQTT建立连接");
	}

	@Override
	public void authPacketArrived(int reasonCode, MqttProperties properties) {
		log.info("接收到身份验证数据包");
	}

}
