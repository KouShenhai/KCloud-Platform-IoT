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

package org.laokou.iot.gateway.command;

import com.baomidou.dynamic.datasource.toolkit.DynamicDataSourceContextHolder;
import io.netty.handler.codec.mqtt.MqttQoS;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.laokou.common.domain.annotation.CommandLog;
import org.laokou.common.i18n.common.exception.GlobalException;
import org.laokou.common.i18n.util.ObjectUtils;
import org.laokou.common.mqtt.client.GatewayCommandFutureManager;
import org.laokou.common.mqtt.client.MqttClientManager;
import org.laokou.common.mqtt.client.MqttForyFactory;
import org.laokou.common.mqtt.client.SpringMqttClientProperties;
import org.laokou.common.mqtt.client.message.UpCommandReplyGatewayMessage;
import org.laokou.common.mybatisplus.util.TransactionalUtils;
import org.laokou.common.tenant.constant.DSConstants;
import org.laokou.iot.device.gatewayimpl.database.DeviceMapper;
import org.laokou.iot.device.gatewayimpl.database.dataobject.DeviceDO;
import org.laokou.iot.gateway.ability.GatewayDomainService;
import org.laokou.iot.gateway.convertor.GatewayConvertor;
import org.laokou.iot.gateway.dto.GatewayDownCommandCmd;
import org.laokou.iot.gateway.dto.clientobject.GatewayCommandReplyCO;
import org.laokou.iot.gateway.factory.GatewayDomainFactory;
import org.laokou.iot.gateway.gatewayimpl.database.GatewayMapper;
import org.laokou.iot.gateway.gatewayimpl.database.dataobject.GatewayDO;
import org.laokou.iot.gateway.model.GatewayCommandA;
import org.springframework.stereotype.Component;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * @author laokou
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class GatewayDownCommandCmdExe {

	private final GatewayMapper gatewayMapper;

	private final DeviceMapper deviceMapper;

	private final TransactionalUtils transactionalUtils;

	private final GatewayDomainService gatewayDomainService;

	private final MqttClientManager mqttClientManager;

	private final SpringMqttClientProperties springMqttClientProperties;

	private final GatewayCommandFutureManager gatewayCommandFutureManager;

	private final ExecutorService virtualThreadExecutor;

	@CommandLog
	public CompletableFuture<GatewayCommandReplyCO> execute(GatewayDownCommandCmd cmd)
			throws ExecutionException, InterruptedException, TimeoutException {
		try {
			DynamicDataSourceContextHolder.push(DSConstants.IOT);
			GatewayDO gatewayDO = gatewayMapper.selectById(cmd.getGatewayId());
			DeviceDO deviceDO = ObjectUtils.isNotNull(cmd.getDeviceId()) ? deviceMapper.selectById(cmd.getDeviceId())
					: null;
			GatewayCommandA gatewayCommandA = GatewayDomainFactory.createGatewayCommandA()
				.create(GatewayConvertor.toEntity(gatewayDO, deviceDO, cmd.getCommand(), cmd.getParam(),
						springMqttClientProperties));
			// 校验类型
			gatewayCommandA.checkCommand();
			Long commandId = gatewayCommandA.getId();
			CompletableFuture<UpCommandReplyGatewayMessage> future = gatewayCommandFutureManager
				.put(gatewayCommandA.getId(), new CompletableFuture<>())
				.completeOnTimeout(GatewayConvertor.toMessage(),
						springMqttClientProperties.getCommandReplyTimeoutSeconds(), TimeUnit.SECONDS)
				.whenComplete((_, _) -> gatewayCommandFutureManager.remove(commandId));
			// 发布 MQTT 消息
			return CompletableFuture.runAsync(() -> mqttClientManager.publish(gatewayDO.getSessionId(),
					gatewayCommandA.getGatewayCommandE().getTopic(),
					MqttForyFactory.INSTANCE.serialize(GatewayConvertor.toMessage(gatewayCommandA, cmd.getParam())),
					MqttQoS.AT_LEAST_ONCE), virtualThreadExecutor)
				.whenComplete((_, err) -> {
					if (err != null) {
						future.completeExceptionally(err);
					}
				})
				.whenCompleteAsync((_, err) -> {
					if (err != null) {
						createGatewayCommand(new UpCommandReplyGatewayMessage(), gatewayCommandA, err);
					}
				}, virtualThreadExecutor)
				.thenCompose(_ -> future)
				.whenCompleteAsync((reply, err) -> createGatewayCommand(reply, gatewayCommandA, err),
						virtualThreadExecutor)
				.thenApply(reply -> new GatewayCommandReplyCO(reply.getPayload(), reply.getMessage()));
		}
		catch (GlobalException gex) {
			throw gex;
		}
		catch (Exception ex) {
			log.error("下发重启网关指令失败，错误信息：{}", ex.getMessage(), ex);
			throw ex;
		}
		finally {
			DynamicDataSourceContextHolder.clear();
		}
	}

	private void createGatewayCommand(UpCommandReplyGatewayMessage reply, GatewayCommandA gatewayCommandA,
			Throwable err) {
		try {
			DynamicDataSourceContextHolder.push(DSConstants.IOT);
			gatewayCommandA.getReplyInfo(reply.getPayload(), reply.getMessage(), reply.getTimestamp(), err);
			transactionalUtils.executeInTransaction(() -> gatewayDomainService.createGatewayCommand(gatewayCommandA));
		}
		finally {
			DynamicDataSourceContextHolder.clear();
		}
	}

}
