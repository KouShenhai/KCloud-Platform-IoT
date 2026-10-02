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

import org.laokou.iot.gateway.dto.clientobject.GatewayCO;
import org.laokou.iot.gateway.factory.GatewayDomainFactory;
import org.laokou.iot.gateway.gatewayimpl.database.dataobject.GatewayDO;
import org.laokou.iot.gateway.model.GatewayA;
import org.laokou.iot.gateway.model.entity.GatewayE;

import java.util.List;

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
