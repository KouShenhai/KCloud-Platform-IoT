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

package org.laokou.network.connection.command.query;

import lombok.RequiredArgsConstructor;
import org.laokou.common.i18n.dto.Page;
import org.laokou.common.i18n.dto.Result;
import org.laokou.network.connection.convertor.ConnectionConvertor;
import org.laokou.network.connection.dto.ConnectionPageQry;
import org.laokou.network.connection.dto.clientobject.ConnectionCO;
import org.laokou.network.connection.gatewayimpl.database.ConnectionMapper;
import org.laokou.network.connection.gatewayimpl.database.dataobject.ConnectionDO;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Network connection page query executor.
 *
 * @author laokou
 */
@Component
@RequiredArgsConstructor
public class ConnectionPageQryExe {

	private final ConnectionMapper connectionMapper;

	public Result<Page<ConnectionCO>> execute(ConnectionPageQry qry) {
		List<ConnectionDO> list = connectionMapper.selectObjectPage(qry);
		long total = connectionMapper.selectObjectCount(qry);
		return Result.ok(Page.create(list.stream().map(ConnectionConvertor::toClientObject).toList(), total));
	}

}
