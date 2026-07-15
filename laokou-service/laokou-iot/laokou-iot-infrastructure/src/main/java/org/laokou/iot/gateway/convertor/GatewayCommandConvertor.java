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

import org.laokou.iot.gateway.dto.clientobject.GatewayCommandLogCO;
import org.laokou.iot.gateway.gatewayimpl.database.dataobject.GatewayCommandLogDO;
import org.laokou.iot.gateway.model.GatewayCommandE;

/**
 *
 * 网关指令转换器.
 *
 * @author laokou
 */
public class GatewayCommandConvertor {

	public static GatewayCommandLogDO toDataObject(Long id, GatewayCommandE gatewayCommandE) {
		GatewayCommandLogDO gatewayCommandLogDO = new GatewayCommandLogDO();
		gatewayCommandLogDO.setId(id);
		gatewayCommandLogDO.setCommandId(gatewayCommandE.getCommandId());
		gatewayCommandLogDO.setGatewayId(gatewayCommandE.getGatewayId());
		gatewayCommandLogDO.setGatewayKey(gatewayCommandE.getGatewayKey());
		gatewayCommandLogDO.setType(gatewayCommandE.getType());
		gatewayCommandLogDO.setDeviceKey(gatewayCommandE.getDeviceKey());
		gatewayCommandLogDO.setPayload(gatewayCommandE.getPayload());
		gatewayCommandLogDO.setStatus(gatewayCommandE.getStatus());
		gatewayCommandLogDO.setResult(gatewayCommandE.getResult());
		return gatewayCommandLogDO;
	}

	public static GatewayCommandLogCO toClientObject(GatewayCommandLogDO gatewayCommandLogDO) {
		GatewayCommandLogCO gatewayCommandLogCO = new GatewayCommandLogCO();
		gatewayCommandLogCO.setId(gatewayCommandLogDO.getId());
		gatewayCommandLogCO.setCommandId(gatewayCommandLogDO.getCommandId());
		gatewayCommandLogCO.setGatewayId(gatewayCommandLogDO.getGatewayId());
		gatewayCommandLogCO.setGatewayKey(gatewayCommandLogDO.getGatewayKey());
		gatewayCommandLogCO.setType(gatewayCommandLogDO.getType());
		gatewayCommandLogCO.setDeviceKey(gatewayCommandLogDO.getDeviceKey());
		gatewayCommandLogCO.setPayload(gatewayCommandLogDO.getPayload());
		gatewayCommandLogCO.setStatus(gatewayCommandLogDO.getStatus());
		gatewayCommandLogCO.setResult(gatewayCommandLogDO.getResult());
		gatewayCommandLogCO.setCreateTime(gatewayCommandLogDO.getCreateTime());
		return gatewayCommandLogCO;
	}

}
