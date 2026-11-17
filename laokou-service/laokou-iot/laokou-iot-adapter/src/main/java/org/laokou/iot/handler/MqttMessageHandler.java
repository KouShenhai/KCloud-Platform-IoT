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

import com.baomidou.dynamic.datasource.toolkit.DynamicDataSourceContextHolder;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import io.netty.handler.codec.mqtt.MqttQoS;
import lombok.extern.slf4j.Slf4j;
import org.apache.pulsar.client.api.SubscriptionType;
import org.apache.pulsar.common.schema.SchemaType;
import org.jspecify.annotations.NonNull;
import org.laokou.common.core.util.MapUtils;
import org.laokou.common.fory.config.ForyFactory;
import org.laokou.common.fory.util.ForyJsonUtils;
import org.laokou.common.i18n.common.exception.StatusCode;
import org.laokou.common.i18n.util.InstantUtils;
import org.laokou.common.i18n.util.ObjectUtils;
import org.laokou.common.i18n.util.RedisKeyUtils;
import org.laokou.common.i18n.util.ResourceExtUtils;
import org.laokou.iot.gateway.dto.event.GatewayCommandEvent;
import org.laokou.common.mqtt.client.MqttClientManager;
import org.laokou.common.mqtt.client.MqttForyFactory;
import org.laokou.common.mqtt.client.message.MqttMessageType;
import org.laokou.common.mqtt.client.message.UpCommandReplyGatewayMessage;
import org.laokou.common.mqtt.client.message.UpReportPropertiesDeviceMessage;
import org.laokou.common.mqtt.client.message.UpReportPropertiesGatewayMessage;
import org.laokou.common.mybatisplus.util.TransactionalUtils;
import org.laokou.common.redis.util.RedisUtils;
import org.laokou.common.tenant.constant.DSConstants;
import org.laokou.iot.gateway.convertor.GatewayConvertor;
import org.laokou.iot.gateway.gatewayimpl.database.GatewayCommandMapper;
import org.laokou.iot.gateway.gatewayimpl.database.dataobject.GatewayCommandDO;
import org.laokou.iot.gateway.model.enums.MqTopic;
import org.laokou.iot.gateway.model.enums.ReplyStatus;
import org.laokou.iot.gateway.model.enums.Status;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.pulsar.annotation.PulsarListener;
import org.springframework.pulsar.annotation.PulsarListeners;
import org.springframework.pulsar.listener.AckMode;
import org.springframework.pulsar.listener.Acknowledgement;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

/**
 * @author laokou
 */
@Slf4j
@Component
public final class MqttMessageHandler {

	private final RedisUtils redisUtils;

	private final DefaultRedisScript<@NonNull Long> script;

	private final GatewayCommandMapper gatewayCommandMapper;

	private final TransactionalUtils transactionalUtils;

	private final MqttClientManager mqttClientManager;

	public MqttMessageHandler(RedisUtils redisUtils, GatewayCommandMapper gatewayCommandMapper,
			TransactionalUtils transactionalUtils, MqttClientManager mqttClientManager) throws IOException {
		this.redisUtils = redisUtils;
		this.gatewayCommandMapper = gatewayCommandMapper;
		this.transactionalUtils = transactionalUtils;
		this.mqttClientManager = mqttClientManager;
		this.script = new DefaultRedisScript<>(
				ResourceExtUtils.getResource("lua/set_properties.lua").getContentAsString(StandardCharsets.UTF_8),
				Long.class);
	}

	// @formatter:off
	@PulsarListeners(value = {
		@PulsarListener(
			topics = "persistent://${system-settings.tenant-code}/gateway/" + MqTopic.DOWN_COMMAND_GATEWAY_MESSAGE_TOPIC,
			subscriptionName = "${system-settings.tenant-code}",
			schemaType = SchemaType.BYTES,
			ackMode = AckMode.MANUAL,
			subscriptionType = SubscriptionType.Shared
		)
	})
	// @formatter:on
	public void handleDownCommandGatewayMessage(byte[] msg, Acknowledgement ack) {
		try {
			DynamicDataSourceContextHolder.push(DSConstants.IOT);
			if (ForyFactory.INSTANCE.deserialize(msg) instanceof GatewayCommandEvent event) {
				AtomicReference<String> errorMsg = new AtomicReference<>("");
				try {
					mqttClientManager.publish(event.getSessionId(), event.getTopic(),
							MqttForyFactory.INSTANCE.serialize(GatewayConvertor.toMessage(event)),
							MqttQoS.AT_LEAST_ONCE);
				}
				catch (Exception ex) {
					log.error("发布MQTT消息失败，错误信息：{}", ex.getMessage(), ex);
					errorMsg.set(ex.getMessage());
				}
				finally {
					Integer version = gatewayCommandMapper.selectVersion(event.getCommandId());
					transactionalUtils.executeInTransaction(() -> updateGatewayCommand(errorMsg.get(),
							event.getCommandId(), version == null ? 0 : version));
				}
			}
			ack.acknowledge();
		}
		catch (Exception ex) {
			log.error("handleDownCommandGatewayMessage error", ex);
			ack.nack();
		}
		finally {
			DynamicDataSourceContextHolder.clear();
		}
	}

	// @formatter:off
	@PulsarListeners(value = {
		@PulsarListener(
			topics = "persistent://${system-settings.tenant-code}/gateway/" + MqttMessageType.UP_COMMAND_REPLY_GATEWAY_MESSAGE_TOPIC,
			subscriptionName = "${system-settings.tenant-code}",
			schemaType = SchemaType.BYTES,
			ackMode = AckMode.MANUAL,
			subscriptionType = SubscriptionType.Shared
		)
	})
	// @formatter:on
	public void handleUpCommandReplyGatewayMessage(byte[] msg, Acknowledgement ack) {
		try {
			DynamicDataSourceContextHolder.push(DSConstants.IOT);
			if (MqttForyFactory.INSTANCE
				.deserialize(msg) instanceof UpCommandReplyGatewayMessage upCommandReplyGatewayMessage) {
				Integer version = gatewayCommandMapper.selectVersion(upCommandReplyGatewayMessage.getRequestId());
				transactionalUtils.executeInTransaction(
						() -> updateGatewayCommand(upCommandReplyGatewayMessage, version == null ? 0 : version));
			}
			ack.acknowledge();
		}
		catch (Exception ex) {
			log.error("handleUpCommandReplyGatewayMessage error", ex);
			ack.nack();
		}
		finally {
			DynamicDataSourceContextHolder.clear();
		}
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

	private void updateGatewayCommand(UpCommandReplyGatewayMessage upCommandReplyGatewayMessage, Integer version) {
		gatewayCommandMapper.update(Wrappers.lambdaUpdate(GatewayCommandDO.class)
			.set(MapUtils.isNotEmpty(upCommandReplyGatewayMessage.getPayload()), GatewayCommandDO::getReplyPayload,
					ForyJsonUtils.toJsonStr(upCommandReplyGatewayMessage.getPayload()))
			.set(GatewayCommandDO::getReplyTime, InstantUtils.now())
			.set(GatewayCommandDO::getReplyStatus,
					ObjectUtils.equals(upCommandReplyGatewayMessage.getMessage(), StatusCode.OK)
							? ReplyStatus.SUCCESS.getCode() : ReplyStatus.FAILURE.getCode())
			.set(GatewayCommandDO::getReplyMessage, upCommandReplyGatewayMessage.getMessage())
			.set(GatewayCommandDO::getVersion, version + 1)
			.eq(GatewayCommandDO::getId, upCommandReplyGatewayMessage.getRequestId())
			.eq(GatewayCommandDO::getVersion, version));
	}

	private void updateGatewayCommand(String errorMsg, Long commandId, Integer version) {
		gatewayCommandMapper.update(Wrappers.lambdaUpdate(GatewayCommandDO.class)
			.set(GatewayCommandDO::getErrorMessage, errorMsg)
			.set(GatewayCommandDO::getDownTime, InstantUtils.now())
			.set(GatewayCommandDO::getStatus,
					StringUtils.hasText(errorMsg) ? Status.FAILURE.getCode() : Status.SUCCESS.getCode())
			.set(GatewayCommandDO::getVersion, version + 1)
			.eq(GatewayCommandDO::getId, commandId)
			.eq(GatewayCommandDO::getVersion, version));
	}

}
