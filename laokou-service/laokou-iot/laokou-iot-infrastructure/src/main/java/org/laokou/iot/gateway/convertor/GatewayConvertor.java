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

import org.laokou.common.core.config.SystemSettingsProperties;
import org.laokou.common.fory.util.ForyJsonUtils;
import org.laokou.common.mqtt.client.GatewayCommandEvent;
import org.laokou.iot.gateway.dto.clientobject.GatewayCO;
import org.laokou.iot.gateway.dto.clientobject.GatewayPropertiesCO;
import org.laokou.iot.gateway.factory.GatewayDomainFactory;
import org.laokou.iot.gateway.gatewayimpl.database.dataobject.GatewayCommandDO;
import org.laokou.iot.gateway.gatewayimpl.database.dataobject.GatewayDO;
import org.laokou.iot.gateway.model.GatewayA;
import org.laokou.iot.gateway.model.GatewayCommandA;
import org.laokou.iot.gateway.model.entity.GatewayCommandE;
import org.laokou.iot.gateway.model.entity.GatewayE;
import org.laokou.iot.gateway.model.enums.Command;
import org.laokou.iot.gateway.model.enums.Status;
import org.springframework.util.StringUtils;

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

	public static GatewayCommandEvent toEvent(GatewayCommandDO gatewayCommandDO,
			SystemSettingsProperties systemSettingsProperties) {
		GatewayCommandEvent gatewayCommandEvent = new GatewayCommandEvent();
		gatewayCommandEvent.setCommandId(gatewayCommandDO.getId());
		gatewayCommandEvent.setGatewaySn(gatewayCommandDO.getGatewaySn());
		gatewayCommandEvent.setDeviceName(gatewayCommandDO.getDeviceName());
		gatewayCommandEvent.setDeviceSn(gatewayCommandDO.getDeviceSn());
		gatewayCommandEvent.setParam(StringUtils.hasText(gatewayCommandDO.getParam())
				? ForyJsonUtils.toBean(gatewayCommandDO.getParam(), Map.class) : Collections.EMPTY_MAP);
		gatewayCommandEvent.setCommand(gatewayCommandDO.getCommand());
		gatewayCommandEvent.setTopic(gatewayCommandDO.getTopic());
		gatewayCommandEvent.setTenantCode(systemSettingsProperties.getTenantCode());
		return gatewayCommandEvent;
	}

	public static GatewayCommandE toEntity(GatewayDO gatewayDO, SystemSettingsProperties systemSettingsProperties) {
		return GatewayDomainFactory.createGatewayCommandE()
			.toBuilder()
			.gatewaySn(gatewayDO.getSn())
			.gatewayId(gatewayDO.getId())
			.command(Command.REBOOT.getCode())
			.topic(String.format("up/%s/%s/command", systemSettingsProperties.getTenantCode(), gatewayDO.getSn()))
			.status(Status.PENDING.getCode())
			.build();
	}

	public static GatewayCommandDO toDataObject(GatewayCommandA gatewayCommandA) {
		GatewayCommandDO gatewayCommandDO = new GatewayCommandDO();
		GatewayCommandE gatewayCommandE = gatewayCommandA.getGatewayCommandE();
		gatewayCommandDO.setId(gatewayCommandA.getId());
		gatewayCommandDO.setGatewaySn(gatewayCommandE.getGatewaySn());
		gatewayCommandDO.setCommand(gatewayCommandE.getCommand());
		gatewayCommandDO.setTopic(gatewayCommandE.getTopic());
		gatewayCommandDO.setStatus(gatewayCommandE.getStatus());
		gatewayCommandDO.setParam(gatewayCommandE.getParam());
		gatewayCommandDO.setCommand(gatewayCommandE.getCommand());
		gatewayCommandDO.setCreateTime(gatewayCommandA.getCreateTime());
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

}
