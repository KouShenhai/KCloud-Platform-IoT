/*
 * Copyright (c) 2022-2025 KCloud-Platform-IoT Author or Authors. All Rights Reserved.
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

package org.laokou.iot.transportProtocol.convertor;

import org.laokou.common.core.util.IdGenerator;
import org.laokou.iot.transportProtocol.dto.clientobject.TransportProtocolCO;
import org.laokou.iot.transportProtocol.gatewayimpl.database.dataobject.TransportProtocolDO;
import org.laokou.iot.transportProtocol.model.TransportProtocolE;

/**
 *
 * 传输协议转换器.
 *
 * @author laokou
 */
public class TransportProtocolConvertor {

	public static TransportProtocolDO toDataObject(TransportProtocolE transportProtocolE, boolean isInsert) {
		TransportProtocolDO transportProtocolDO = new TransportProtocolDO();
		if (isInsert) {
			transportProtocolDO.setId(IdGenerator.defaultSnowflakeId());
		}
		else {
			transportProtocolDO.setId(transportProtocolE.getId());
		}
		transportProtocolDO.setName(transportProtocolE.getName());
		transportProtocolDO.setType(transportProtocolE.getType());
		transportProtocolDO.setHost(transportProtocolE.getHost());
		transportProtocolDO.setPort(transportProtocolE.getPort());
		transportProtocolDO.setClientId(transportProtocolE.getClientId());
		transportProtocolDO.setUsername(transportProtocolE.getUsername());
		transportProtocolDO.setPassword(transportProtocolE.getPassword());
		transportProtocolDO.setRemark(transportProtocolE.getRemark());
		return transportProtocolDO;
	}

	public static TransportProtocolCO toClientObject(TransportProtocolDO transportProtocolDO) {
		TransportProtocolCO transportProtocolCO = new TransportProtocolCO();
		transportProtocolCO.setName(transportProtocolDO.getName());
		transportProtocolCO.setType(transportProtocolDO.getType());
		transportProtocolCO.setHost(transportProtocolDO.getHost());
		transportProtocolCO.setPort(transportProtocolDO.getPort());
		transportProtocolCO.setClientId(transportProtocolDO.getClientId());
		transportProtocolCO.setUsername(transportProtocolDO.getUsername());
		transportProtocolCO.setPassword(transportProtocolDO.getPassword());
		transportProtocolCO.setRemark(transportProtocolDO.getRemark());
		return transportProtocolCO;
	}

	public static TransportProtocolE toEntity(TransportProtocolCO transportProtocolCO) {
		TransportProtocolE transportProtocolE = new TransportProtocolE();
		transportProtocolE.setName(transportProtocolCO.getName());
		transportProtocolE.setType(transportProtocolCO.getType());
		transportProtocolE.setHost(transportProtocolCO.getHost());
		transportProtocolE.setPort(transportProtocolCO.getPort());
		transportProtocolE.setClientId(transportProtocolCO.getClientId());
		transportProtocolE.setUsername(transportProtocolCO.getUsername());
		transportProtocolE.setPassword(transportProtocolCO.getPassword());
		transportProtocolE.setRemark(transportProtocolCO.getRemark());
		return transportProtocolE;
	}

}
