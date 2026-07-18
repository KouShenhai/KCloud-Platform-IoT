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

package org.laokou.iot.session.convertor;

import org.laokou.network.connection.dto.clientobject.ConnectionCO;
import org.laokou.network.connection.gatewayimpl.database.dataobject.ConnectionDO;
import org.laokou.network.connection.model.ConnectionE;

/**
 * Network connection converter.
 *
 * @author laokou
 */
public class ConnectionConvertor {

	public static ConnectionDO toDataObject(Long id, ConnectionE connectionE, boolean isInsert) {
		ConnectionDO connectionDO = new ConnectionDO();
		connectionDO.setId(isInsert ? id : connectionE.getId());
		connectionDO.setName(connectionE.getName());
		connectionDO.setType(connectionE.getType());
		connectionDO.setHost(connectionE.getHost());
		connectionDO.setPort(connectionE.getPort());
		connectionDO.setEnabled(connectionE.getEnabled());
		connectionDO.setConfig(connectionE.getConfig());
		connectionDO.setRemark(connectionE.getRemark());
		return connectionDO;
	}

	public static ConnectionCO toClientObject(ConnectionDO connectionDO) {
		if (connectionDO == null) {
			return null;
		}
		ConnectionCO connectionCO = new ConnectionCO();
		connectionCO.setId(connectionDO.getId());
		connectionCO.setName(connectionDO.getName());
		connectionCO.setType(connectionDO.getType());
		connectionCO.setHost(connectionDO.getHost());
		connectionCO.setPort(connectionDO.getPort());
		connectionCO.setEnabled(connectionDO.getEnabled());
		connectionCO.setConfig(connectionDO.getConfig());
		connectionCO.setRemark(connectionDO.getRemark());
		connectionCO.setCreateTime(connectionDO.getCreateTime());
		return connectionCO;
	}

	public static ConnectionE toEntity(ConnectionCO connectionCO) {
		if (connectionCO == null) {
			return null;
		}
		ConnectionE connectionE = new ConnectionE();
		connectionE.setId(connectionCO.getId());
		connectionE.setName(connectionCO.getName());
		connectionE.setType(connectionCO.getType());
		connectionE.setHost(connectionCO.getHost());
		connectionE.setPort(connectionCO.getPort());
		connectionE.setEnabled(connectionCO.getEnabled());
		connectionE.setConfig(connectionCO.getConfig());
		connectionE.setRemark(connectionCO.getRemark());
		connectionE.setCreateTime(connectionCO.getCreateTime());
		return connectionE;
	}

}
