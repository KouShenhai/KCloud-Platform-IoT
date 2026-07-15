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

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.laokou.common.i18n.util.JacksonUtils;
import org.laokou.common.i18n.util.ObjectUtils;
import org.laokou.iot.gateway.api.GatewayCommandServiceI;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * @author laokou
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class MqttMessageHandler {

	private final GatewayCommandServiceI gatewayCommandServiceI;

	/**
	 * 处理网关指令回执消息.
	 * <p>
	 * 上行主题：persistent://laokouyun/mqtt/up-*-command-reply，回执报文格式：
	 * {"commandId":123,"code":1,"result":"..."}，其中 code 1成功 2失败。
	 * </p>
	 * TODO: 待MQTT/Pulsar消费者接入后，通过 @PulsarListener 监听 up-*-command-reply 主题并调用本方法。
	 * @param message 回执报文
	 */
	public void handleCommandReplyMessage(byte[] message) {
		if (ObjectUtils.isNull(message)) {
			return;
		}
		Map<String, Object> reply = JacksonUtils.toMap(new String(message), String.class, Object.class);
		Object commandId = reply.get("commandId");
		Object code = reply.get("code");
		Object result = reply.get("result");
		if (ObjectUtils.isNull(commandId) || ObjectUtils.isNull(code)) {
			log.warn("网关指令回执报文缺少必要字段：{}", reply);
			return;
		}
		gatewayCommandServiceI.handleReply(Long.valueOf(commandId.toString()), Integer.valueOf(code.toString()),
				ObjectUtils.isNull(result) ? null : result.toString());
	}

	// @PulsarListeners(value = { @PulsarListener(topicPattern =
	// "persistent://laokouyun/mqtt/up-property-report",
	// subscriptionName = "laokouyun-mqtt-up-property-report", schemaType =
	// SchemaType.BYTES, batch = true,
	// ackMode = AckMode.BATCH, subscriptionType = SubscriptionType.Shared), })
	// public void handlePropertyReportMessage(List<byte[]> messages) {
	// log.info("接收到MQTT消息：{}", messages);
	// }
	//
	// @PulsarListeners(value = { @PulsarListener(topicPattern =
	// "persistent://laokouyun/mqtt/up-property-read-reply",
	// subscriptionName = "laokouyun-mqtt-up-property-read-reply", schemaType =
	// SchemaType.BYTES, batch = true,
	// ackMode = AckMode.BATCH, subscriptionType = SubscriptionType.Shared), })
	// public void handlePropertyReadReplyMessage(List<byte[]> messages) {
	// log.info("接收到MQTT消息：{}", messages);
	// }
	//
	// @PulsarListeners(value = { @PulsarListener(topicPattern =
	// "persistent://laokouyun/mqtt/up-property-write-reply",
	// subscriptionName = "laokouyun-mqtt-up-property-write-reply", schemaType =
	// SchemaType.BYTES, batch = true,
	// ackMode = AckMode.BATCH, subscriptionType = SubscriptionType.Shared), })
	// public void handlePropertyWriteReplyMessage(List<byte[]> messages) {
	// log.info("接收到MQTT消息：{}", messages);
	// }
	//
	// @PulsarListeners(value = { @PulsarListener(topicPattern =
	// "persistent://laokouyun/mqtt/up-ota-upgrade-report",
	// subscriptionName = "laokouyun-mqtt-up-ota-upgrade-report", schemaType =
	// SchemaType.BYTES, batch = true,
	// ackMode = AckMode.BATCH, subscriptionType = SubscriptionType.Shared), })
	// public void handleOtaUpgradeReportMessage(List<byte[]> messages) {
	// log.info("接收到MQTT消息：{}", messages);
	// }
	//
	// @PulsarListeners(value = { @PulsarListener(topicPattern =
	// "persistent://laokouyun/mqtt/up-ota-upgrade-set",
	// subscriptionName = "laokouyun-mqtt-up-ota-upgrade-set", schemaType =
	// SchemaType.BYTES, batch = true,
	// ackMode = AckMode.BATCH, subscriptionType = SubscriptionType.Shared), })
	// public void handleOtaUpgradeSetMessage(List<byte[]> messages) {
	// log.info("接收到MQTT消息：{}", messages);
	// }

}
