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
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.laokou.common.i18n.util.InstantUtils;
import org.laokou.common.mqtt.client.GatewayCommandEvent;
import org.laokou.common.mqtt.client.GatewayCommandEventHandler;
import org.laokou.common.mqtt.client.MqttClientManager;
import org.laokou.common.mqtt.client.MqttForyFactory;
import org.laokou.common.mybatisplus.util.TransactionalUtils;
import org.laokou.common.tenant.constant.DSConstants;
import org.laokou.iot.gateway.convertor.GatewayConvertor;
import org.laokou.iot.gateway.gatewayimpl.database.GatewayCommandMapper;
import org.laokou.iot.gateway.gatewayimpl.database.dataobject.GatewayCommandDO;
import org.laokou.iot.gateway.model.enums.Status;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.concurrent.atomic.AtomicReference;

/**
 * @author laokou
 */
@Slf4j
@Component
@RequiredArgsConstructor
public final class MqttGatewayCommandEventHandler implements GatewayCommandEventHandler {

	private final MqttClientManager mqttClientManager;

	private final GatewayCommandMapper gatewayCommandMapper;

	private final TransactionalUtils transactionalUtils;

	@Override
	public void onEvent(GatewayCommandEvent event, long sequence, boolean endOfBatch) {
		AtomicReference<String> errorMsg = new AtomicReference<>("");
		try {
			mqttClientManager.publish(event.getSessionId(), event.getTopic(),
					MqttForyFactory.INSTANCE.serialize(GatewayConvertor.toMessage(event)), MqttQoS.AT_LEAST_ONCE);
		}
		catch (Exception ex) {
			log.error("发布MQTT消息失败，错误信息：{}", ex.getMessage(), ex);
			errorMsg.set(ex.getMessage());
		}
		finally {
			try {
				DynamicDataSourceContextHolder.push(DSConstants.IOT);
				transactionalUtils.executeInTransaction(() -> updateGatewayCommand(errorMsg.get(), event.getCommandId(),
						gatewayCommandMapper.selectVersion(event.getSessionId())));
			}
			finally {
				DynamicDataSourceContextHolder.clear();
			}
		}
	}

	private void updateGatewayCommand(String errorMsg, Long commandId, Integer version) {
		gatewayCommandMapper.update(Wrappers.lambdaUpdate(GatewayCommandDO.class)
			.set(GatewayCommandDO::getErrorMessage, errorMsg)
			.set(GatewayCommandDO::getDispatchTime, InstantUtils.now())
			.set(GatewayCommandDO::getStatus,
					StringUtils.hasText(errorMsg) ? Status.FAILURE.getCode() : Status.SUCCESS.getCode())
			.set(GatewayCommandDO::getVersion, version + 1)
			.eq(GatewayCommandDO::getId, commandId)
			.eq(GatewayCommandDO::getVersion, version));
	}

}
