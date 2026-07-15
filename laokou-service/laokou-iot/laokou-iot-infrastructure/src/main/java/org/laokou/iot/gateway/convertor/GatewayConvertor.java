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
import org.laokou.iot.gateway.gatewayimpl.database.dataobject.GatewayDO;
import org.laokou.iot.gateway.model.GatewayE;

/**
 *
 * 网关转换器.
 *
 * @author laokou
 */
public class GatewayConvertor {

	public static GatewayDO toDataObject(Long id, GatewayE gatewayE, boolean isInsert) {
		GatewayDO gatewayDO = new GatewayDO();
		if (isInsert) {
			gatewayDO.setId(id);
		}
		else {
			gatewayDO.setId(gatewayE.getId());
		}
		gatewayDO.setGatewayKey(gatewayE.getGatewayKey());
		gatewayDO.setName(gatewayE.getName());
		gatewayDO.setStatus(gatewayE.getStatus());
		gatewayDO.setProductId(gatewayE.getProductId());
		gatewayDO.setAddress(gatewayE.getAddress());
		gatewayDO.setLongitude(gatewayE.getLongitude());
		gatewayDO.setLatitude(gatewayE.getLatitude());
		gatewayDO.setRemark(gatewayE.getRemark());
		return gatewayDO;
	}

	public static GatewayCO toClientObject(GatewayDO gatewayDO) {
		GatewayCO gatewayCO = new GatewayCO();
		gatewayCO.setId(gatewayDO.getId());
		gatewayCO.setGatewayKey(gatewayDO.getGatewayKey());
		gatewayCO.setName(gatewayDO.getName());
		gatewayCO.setStatus(gatewayDO.getStatus());
		gatewayCO.setProductId(gatewayDO.getProductId());
		gatewayCO.setAddress(gatewayDO.getAddress());
		gatewayCO.setLongitude(gatewayDO.getLongitude());
		gatewayCO.setLatitude(gatewayDO.getLatitude());
		gatewayCO.setRemark(gatewayDO.getRemark());
		gatewayCO.setCreateTime(gatewayDO.getCreateTime());
		return gatewayCO;
	}

	public static GatewayE toEntity(GatewayCO gatewayCO) {
		GatewayE gatewayE = new GatewayE();
		gatewayE.setId(gatewayCO.getId());
		gatewayE.setGatewayKey(gatewayCO.getGatewayKey());
		gatewayE.setName(gatewayCO.getName());
		gatewayE.setStatus(gatewayCO.getStatus());
		gatewayE.setProductId(gatewayCO.getProductId());
		gatewayE.setAddress(gatewayCO.getAddress());
		gatewayE.setLongitude(gatewayCO.getLongitude());
		gatewayE.setLatitude(gatewayCO.getLatitude());
		gatewayE.setRemark(gatewayCO.getRemark());
		return gatewayE;
	}

}
