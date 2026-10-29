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

import lombok.extern.slf4j.Slf4j;
import org.apache.pulsar.client.api.SubscriptionType;
import org.apache.pulsar.common.schema.SchemaType;
import org.jspecify.annotations.NonNull;
import org.laokou.common.fory.util.ForyJsonUtils;
import org.laokou.common.i18n.util.RedisKeyUtils;
import org.laokou.common.i18n.util.ResourceExtUtils;
import org.laokou.common.mqtt.client.MqttForyFactory;
import org.laokou.common.mqtt.client.message.MqttMessageType;
import org.laokou.common.mqtt.client.message.UpReportPropertiesDeviceMessage;
import org.laokou.common.mqtt.client.message.UpReportPropertiesGatewayMessage;
import org.laokou.common.redis.util.RedisUtils;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.pulsar.annotation.PulsarListener;
import org.springframework.pulsar.annotation.PulsarListeners;
import org.springframework.pulsar.listener.AckMode;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

/**
 * @author laokou
 */
@Slf4j
@Component
public final class MqttMessageHandler {

	private final RedisUtils redisUtils;

	private final DefaultRedisScript<@NonNull Long> script;

	public MqttMessageHandler(RedisUtils redisUtils) throws IOException {
		this.redisUtils = redisUtils;
		this.script = new DefaultRedisScript<>(
				ResourceExtUtils.getResource("lua/set_properties.lua").getContentAsString(StandardCharsets.UTF_8),
				Long.class);
	}

	// @formatter:off
	@PulsarListeners(value = {
		@PulsarListener(
			topics = "persistent://${system-settings.tenant-code}/gateway/" + MqttMessageType.UP_REPORT_PROPERTIES_DEVICE_MESSAGE_TOPIC,
			subscriptionName = "${system-settings.tenant-code}",
			schemaType = SchemaType.BYTES,
			batch = true,
			ackMode = AckMode.BATCH,
			subscriptionType = SubscriptionType.Shared,
			consumerCustomizer = "mqttMessageConsumerCustomizer"
		)
	})
	public void handleIotUpReportPropertiesDeviceMessage(List<byte[]> messages) {
		List<Object> list = messages.stream().map(MqttForyFactory.INSTANCE::deserialize).toList();
		Thread.startVirtualThread(() -> setPropertiesCache(list));
	}
	// @formatter:on

	// @formatter:off
	@PulsarListeners(value = {
		@PulsarListener(
			topics = "persistent://${system-settings.tenant-code}/gateway/" + MqttMessageType.UP_REPORT_PROPERTIES_GATEWAY_MESSAGE_TOPIC,
			subscriptionName = "${system-settings.tenant-code}",
			schemaType = SchemaType.BYTES,
			batch = true,
			ackMode = AckMode.BATCH,
			subscriptionType = SubscriptionType.Shared,
			consumerCustomizer = "mqttMessageConsumerCustomizer"
		)
	})
	public void handleIotUpReportPropertiesGatewayMessage(List<byte[]> messages) {
		Thread.startVirtualThread(() -> setPropertiesCache(messages.stream().map(MqttForyFactory.INSTANCE::deserialize).toList()));
	}
	// @formatter:on

	private void setPropertiesCache(List<Object> list) {
		List<String> keys = new ArrayList<>(list.size());
		Object[] args = new Object[list.size() * 2];
		for (int i = 0; i < list.size(); i++) {
			Object obj = list.get(i);
			if (obj instanceof UpReportPropertiesGatewayMessage msg) {
				keys.add(RedisKeyUtils.getGatewayPropertiesKey(msg.getTenantCode(), msg.getGatewaySn()));
				args[2 * i] = msg.getTimestamp();
				args[2 * i + 1] = ForyJsonUtils.toJsonStr(msg.getProperties());
			}
			else if (obj instanceof UpReportPropertiesDeviceMessage msg) {
				keys.add(RedisKeyUtils.getDevicePropertiesKey(msg.getTenantCode(), msg.getGatewaySn()));
				args[2 * i] = msg.getTimestamp();
				args[2 * i + 1] = ForyJsonUtils.toJsonStr(msg.getProperties());
			}
		}
		Long changed = redisUtils.execute(script, keys, args);
		log.info("set properties cache changed:{}", changed);
	}

}
