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

package org.laokou.iot.gateway.convertor;

import org.laokou.common.context.util.UserUtils;
import org.laokou.common.core.util.ForyJsonUtils;
import org.laokou.common.core.util.MapUtils;
import org.laokou.common.log.model.enums.Status;
import org.laokou.common.mqtt.client.SpringMqttClientProperties;
import org.laokou.common.mqtt.client.message.DownCommandGatewayMessage;
import org.laokou.common.mqtt.client.message.UpCommandReplyGatewayMessage;
import org.laokou.iot.device.gatewayimpl.database.dataobject.DeviceDO;
import org.laokou.iot.gateway.dto.clientobject.GatewayCO;
import org.laokou.iot.gateway.dto.clientobject.GatewayCommandCO;
import org.laokou.iot.gateway.dto.clientobject.GatewayPropertiesCO;
import org.laokou.iot.gateway.factory.GatewayDomainFactory;
import org.laokou.iot.gateway.gatewayimpl.database.dataobject.GatewayCommandDO;
import org.laokou.iot.gateway.gatewayimpl.database.dataobject.GatewayDO;
import org.laokou.iot.gateway.model.GatewayA;
import org.laokou.iot.gateway.model.GatewayCommandA;
import org.laokou.iot.gateway.model.entity.GatewayCommandE;
import org.laokou.iot.gateway.model.entity.GatewayE;

import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 *
 * 网关转换器.
 *
 * @author laokou
 */
public final class GatewayConvertor {

	private GatewayConvertor() {
	}

	public static GatewayDO toDataObject(GatewayA gatewayA) {
		GatewayDO gatewayDO = new GatewayDO();
		GatewayE gatewayE = gatewayA.getGatewayE();
		gatewayDO.setId(gatewayA.getId());
		gatewayDO.setName(gatewayE.getName());
		gatewayDO.setSn(gatewayE.getSn());
		gatewayDO.setSessionId(gatewayE.getSessionId());
		gatewayDO.setRemark(gatewayE.getRemark());
		return gatewayDO;
	}

	public static UpCommandReplyGatewayMessage toMessage() {
		UpCommandReplyGatewayMessage upCommandReplyGatewayMessage = new UpCommandReplyGatewayMessage();
		upCommandReplyGatewayMessage.setMessage("指令下发超时");
		upCommandReplyGatewayMessage.setPayload(Collections.emptyMap());
		return upCommandReplyGatewayMessage;
	}

	public static GatewayCommandE toEntity(GatewayDO gatewayDO, DeviceDO deviceDO, String command,
			Map<String, Object> param, SpringMqttClientProperties springMqttClientProperties) {
		return GatewayDomainFactory.createGatewayCommandE()
			.toBuilder()
			.gatewaySn(gatewayDO.getSn())
			.gatewayId(gatewayDO.getId())
			.command(command)
			.param(MapUtils.isEmpty(param) ? null : ForyJsonUtils.toJsonStr(param))
			.deviceId(deviceDO != null ? deviceDO.getId() : null)
			.deviceName(deviceDO != null ? deviceDO.getName() : null)
			.deviceSn(deviceDO != null ? deviceDO.getSn() : null)
			.replyTopic(String.format("up/%s/command/reply", springMqttClientProperties.getClientId()))
			.topic(String.format("down/%s/%s/command", UserUtils.getTenantCode(), gatewayDO.getSn()))
			.creator(UserUtils.getUserId())
			.tenantId(UserUtils.getTenantId())
			.deptId(UserUtils.getDeptId())
			.build();
	}

	public static DownCommandGatewayMessage toMessage(GatewayCommandA gatewayCommandA, SpringMqttClientProperties springMqttClientProperties, Map<String, Object> param) {
		DownCommandGatewayMessage downCommandGatewayMessage = new DownCommandGatewayMessage();
		GatewayCommandE gatewayCommandE = gatewayCommandA.getGatewayCommandE();
		downCommandGatewayMessage.setCommand(gatewayCommandE.getCommand());
		downCommandGatewayMessage.setParam(param);
		downCommandGatewayMessage.setDeviceName(gatewayCommandE.getDeviceName());
		downCommandGatewayMessage.setGatewaySn(gatewayCommandE.getGatewaySn());
		downCommandGatewayMessage.setDeviceSn(gatewayCommandE.getDeviceSn());
		downCommandGatewayMessage.setDeviceName(gatewayCommandE.getDeviceName());
		downCommandGatewayMessage.setTimestamp(System.currentTimeMillis());
		downCommandGatewayMessage.setRequestId(gatewayCommandA.getId());
		downCommandGatewayMessage.setTenantCode(UserUtils.getTenantCode());
		downCommandGatewayMessage.setMetadata(Map.of("clientId", springMqttClientProperties.getClientId()));
		return downCommandGatewayMessage;
	}

	public static GatewayCommandDO toDataObject(GatewayCommandA gatewayCommandA) {
		GatewayCommandDO gatewayCommandDO = new GatewayCommandDO();
		GatewayCommandE gatewayCommandE = gatewayCommandA.getGatewayCommandE();
		gatewayCommandDO.setId(gatewayCommandA.getId());
		gatewayCommandDO.setGatewaySn(gatewayCommandE.getGatewaySn());
		gatewayCommandDO.setGatewayId(gatewayCommandE.getGatewayId());
		gatewayCommandDO.setDeviceId(gatewayCommandE.getDeviceId());
		gatewayCommandDO.setDeviceName(gatewayCommandE.getDeviceName());
		gatewayCommandDO.setDeviceSn(gatewayCommandE.getDeviceSn());
		gatewayCommandDO.setTopic(gatewayCommandE.getTopic());
		gatewayCommandDO.setStatus(gatewayCommandE.getStatus());
		gatewayCommandDO.setParam(gatewayCommandE.getParam());
		gatewayCommandDO.setCommand(gatewayCommandE.getCommand());
		gatewayCommandDO.setMessage(gatewayCommandE.getMessage());
		gatewayCommandDO.setCreateTime(gatewayCommandA.getCreateTime());
		gatewayCommandDO.setReplyMessage(gatewayCommandE.getReplyMessage());
		gatewayCommandDO.setReplyPayload(gatewayCommandE.getReplyPayload());
		gatewayCommandDO.setReplyStatus(
				gatewayCommandE.getStatus() == Status.FAIL.getCode() ? null : gatewayCommandE.getReplyStatus());
		gatewayCommandDO.setReplyTopic(gatewayCommandE.getReplyTopic());
		gatewayCommandDO.setReplyTime(gatewayCommandE.getReplyTime());
		gatewayCommandDO.setCreator(gatewayCommandE.getCreator());
		gatewayCommandDO.setEditor(gatewayCommandE.getCreator());
		gatewayCommandDO.setTenantId(gatewayCommandE.getTenantId());
		gatewayCommandDO.setDeptId(gatewayCommandE.getDeptId());
		return gatewayCommandDO;
	}

	public static GatewayPropertiesCO toClientObject(GatewayDO gatewayDO, Object obj1, Object obj2) {
		GatewayPropertiesCO gatewayPropertiesCO = new GatewayPropertiesCO();
		gatewayPropertiesCO.setSn(gatewayDO.getSn());
		gatewayPropertiesCO.setName(gatewayDO.getName());
		if (obj1 instanceof String str) {
			gatewayPropertiesCO.setTimestamp(Long.parseLong(str));
		}
		if (obj2 instanceof String str) {
			gatewayPropertiesCO.setProperties(ForyJsonUtils.toBean(str, Map.class));
		}
		return gatewayPropertiesCO;
	}

	public static List<GatewayCO> toClientObjects(List<GatewayDO> list) {
		return list.stream().map(GatewayConvertor::toClientObject).toList();
	}

	public static GatewayCO toClientObject(GatewayDO gatewayDO) {
		GatewayCO gatewayCO = new GatewayCO();
		gatewayCO.setId(gatewayDO.getId());
		gatewayCO.setName(gatewayDO.getName());
		gatewayCO.setSessionId(gatewayDO.getSessionId());
		gatewayCO.setSn(gatewayDO.getSn());
		gatewayCO.setRemark(gatewayDO.getRemark());
		gatewayCO.setCreateTime(gatewayDO.getCreateTime());
		return gatewayCO;
	}

	public static GatewayE toEntity(GatewayCO gatewayCO) {
		return GatewayDomainFactory.createGatewayE()
			.toBuilder()
			.id(gatewayCO.getId())
			.name(gatewayCO.getName())
			.sn(gatewayCO.getSn())
			.sessionId(gatewayCO.getSessionId())
			.remark(gatewayCO.getRemark())
			.build();
	}

	public static List<GatewayCommandCO> toClientObjectList(List<GatewayCommandDO> list) {
		return list.stream().map(GatewayConvertor::toClientObject).toList();
	}

	public static GatewayCommandCO toClientObject(GatewayCommandDO gatewayCommandDO) {
		GatewayCommandCO gatewayCommandCO = new GatewayCommandCO();
		gatewayCommandCO.setId(gatewayCommandDO.getId());
		gatewayCommandCO.setCreateTime(gatewayCommandDO.getCreateTime());
		gatewayCommandCO.setGatewaySn(gatewayCommandDO.getGatewaySn());
		gatewayCommandCO.setCommand(gatewayCommandDO.getCommand());
		gatewayCommandCO.setTopic(gatewayCommandDO.getTopic());
		gatewayCommandCO.setReplyTopic(gatewayCommandDO.getReplyTopic());
		gatewayCommandCO.setDeviceSn(gatewayCommandDO.getDeviceSn());
		gatewayCommandCO.setDeviceName(gatewayCommandDO.getDeviceName());
		gatewayCommandCO.setParam(gatewayCommandDO.getParam());
		gatewayCommandCO.setMessage(gatewayCommandDO.getMessage());
		gatewayCommandCO.setStatus(gatewayCommandDO.getStatus());
		gatewayCommandCO.setReplyPayload(gatewayCommandDO.getReplyPayload());
		gatewayCommandCO.setReplyStatus(gatewayCommandDO.getReplyStatus());
		gatewayCommandCO.setReplyTime(gatewayCommandDO.getReplyTime());
		gatewayCommandCO.setReplyMessage(gatewayCommandDO.getReplyMessage());
		return gatewayCommandCO;
	}

}
