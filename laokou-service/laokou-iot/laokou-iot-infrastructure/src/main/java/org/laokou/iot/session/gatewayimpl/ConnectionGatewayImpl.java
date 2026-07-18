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

package org.laokou.iot.session.gatewayimpl;

import lombok.RequiredArgsConstructor;
import org.laokou.common.i18n.common.IdGenerator;
import org.laokou.network.connection.convertor.ConnectionConvertor;
import org.laokou.network.connection.gateway.ConnectionGateway;
import org.laokou.network.connection.gatewayimpl.database.ConnectionMapper;
import org.laokou.network.connection.gatewayimpl.database.dataobject.ConnectionDO;
import org.laokou.network.connection.model.ConnectionE;
import org.springframework.stereotype.Component;

import java.util.Arrays;

/**
 * Network connection gateway implementation.
 *
 * @author laokou
 */
@Component
@RequiredArgsConstructor
public class ConnectionGatewayImpl implements ConnectionGateway {

	private final ConnectionMapper connectionMapper;

	private final IdGenerator idGenerator;

	@Override
	public void createConnection(ConnectionE connectionE) {
		connectionMapper.insert(ConnectionConvertor.toDataObject(idGenerator.getId(), connectionE, true));
	}

	@Override
	public void updateConnection(ConnectionE connectionE) {
		ConnectionDO connectionDO = ConnectionConvertor.toDataObject(null, connectionE, false);
		connectionDO.setVersion(connectionMapper.selectVersion(connectionE.getId()));
		connectionMapper.updateById(connectionDO);
	}

	@Override
	public void deleteConnection(Long[] ids) {
		connectionMapper.deleteByIds(Arrays.asList(ids));
	}

	@Override
	public boolean existsConnection(Long id) {
		return connectionMapper.selectById(id) != null;
	}

	@Override
	public boolean existsConnection(Long[] ids) {
		return Arrays.stream(ids).allMatch(this::existsConnection);
	}

	@Override
	public Integer getConnectionType(Long id) {
		ConnectionDO connectionDO = connectionMapper.selectById(id);
		return connectionDO == null ? null : connectionDO.getType();
	}

}
