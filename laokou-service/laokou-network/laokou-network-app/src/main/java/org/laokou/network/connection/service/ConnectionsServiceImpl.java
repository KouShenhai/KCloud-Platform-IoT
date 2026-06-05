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

package org.laokou.network.connection.service;

import lombok.RequiredArgsConstructor;
import org.laokou.common.i18n.dto.Page;
import org.laokou.common.i18n.dto.Result;
import org.laokou.network.connection.api.ConnectionsServiceI;
import org.laokou.network.connection.command.ConnectionModifyCmdExe;
import org.laokou.network.connection.command.ConnectionRemoveCmdExe;
import org.laokou.network.connection.command.ConnectionSaveCmdExe;
import org.laokou.network.connection.command.query.ConnectionGetQryExe;
import org.laokou.network.connection.command.query.ConnectionPageQryExe;
import org.laokou.network.connection.dto.ConnectionGetQry;
import org.laokou.network.connection.dto.ConnectionModifyCmd;
import org.laokou.network.connection.dto.ConnectionPageQry;
import org.laokou.network.connection.dto.ConnectionRemoveCmd;
import org.laokou.network.connection.dto.ConnectionSaveCmd;
import org.laokou.network.connection.dto.clientobject.ConnectionCO;
import org.springframework.stereotype.Service;

/**
 * Network connection service implementation.
 *
 * @author laokou
 */
@Service
@RequiredArgsConstructor
public class ConnectionsServiceImpl implements ConnectionsServiceI {

	private final ConnectionSaveCmdExe connectionSaveCmdExe;

	private final ConnectionModifyCmdExe connectionModifyCmdExe;

	private final ConnectionRemoveCmdExe connectionRemoveCmdExe;

	private final ConnectionPageQryExe connectionPageQryExe;

	private final ConnectionGetQryExe connectionGetQryExe;

	@Override
	public void saveConnection(ConnectionSaveCmd cmd) {
		connectionSaveCmdExe.executeVoid(cmd);
	}

	@Override
	public void modifyConnection(ConnectionModifyCmd cmd) {
		connectionModifyCmdExe.executeVoid(cmd);
	}

	@Override
	public void removeConnection(ConnectionRemoveCmd cmd) {
		connectionRemoveCmdExe.executeVoid(cmd);
	}

	@Override
	public Result<Page<ConnectionCO>> pageConnection(ConnectionPageQry qry) {
		return connectionPageQryExe.execute(qry);
	}

	@Override
	public Result<ConnectionCO> getConnectionById(ConnectionGetQry qry) {
		return connectionGetQryExe.execute(qry);
	}

}
