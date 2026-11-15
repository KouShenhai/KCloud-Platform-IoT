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

package org.laokou.iot.handler;

import io.netty.handler.codec.mqtt.MqttQoS;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.laokou.common.mqtt.client.GatewayCommandEvent;
import org.laokou.common.mqtt.client.GatewayCommandEventHandler;
import org.laokou.common.mqtt.client.MqttClientManager;
import org.laokou.common.mqtt.client.MqttForyFactory;
import org.laokou.common.mqtt.client.message.DownCommandGatewayMessage;
import org.springframework.stereotype.Component;

/**
 * @author laokou
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class MqttGatewayCommandEventHandler implements GatewayCommandEventHandler {

	private final MqttClientManager mqttClientManager;

	@Override
	public void onEvent(GatewayCommandEvent event, long sequence, boolean endOfBatch) {
		mqttClientManager.publish(event.getSessionId(), event.getTopic(),
				MqttForyFactory.INSTANCE.serialize(buildMessage(event)), MqttQoS.AT_LEAST_ONCE);
	}

	private DownCommandGatewayMessage buildMessage(GatewayCommandEvent event) {
		DownCommandGatewayMessage downCommandGatewayMessage = new DownCommandGatewayMessage();
		downCommandGatewayMessage.setCommand(event.getCommand());
		downCommandGatewayMessage.setParam(event.getParam());
		downCommandGatewayMessage.setDeviceName(event.getDeviceName());
		downCommandGatewayMessage.setGatewaySn(event.getGatewaySn());
		downCommandGatewayMessage.setDeviceSn(event.getDeviceSn());
		downCommandGatewayMessage.setTimestamp(System.currentTimeMillis());
		downCommandGatewayMessage.setRequestId(event.getCommandId());
		downCommandGatewayMessage.setTenantCode(event.getTenantCode());
		return downCommandGatewayMessage;
	}

}
