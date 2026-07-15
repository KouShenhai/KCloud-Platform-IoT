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

package org.laokou.iot.gateway.command.query;

import lombok.RequiredArgsConstructor;
import org.laokou.common.i18n.dto.Page;
import org.laokou.common.i18n.dto.Result;
import org.laokou.iot.gateway.convertor.GatewayCommandConvertor;
import org.laokou.iot.gateway.dto.GatewayCommandLogPageQry;
import org.laokou.iot.gateway.dto.clientobject.GatewayCommandLogCO;
import org.laokou.iot.gateway.gatewayimpl.database.GatewayCommandLogMapper;
import org.laokou.iot.gateway.gatewayimpl.database.dataobject.GatewayCommandLogDO;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 分页查询网关指令日志请求执行器.
 *
 * @author laokou
 */
@Component
@RequiredArgsConstructor
public class GatewayCommandLogPageQryExe {

	private final GatewayCommandLogMapper gatewayCommandLogMapper;

	public Result<Page<GatewayCommandLogCO>> execute(GatewayCommandLogPageQry qry) {
		List<GatewayCommandLogDO> list = gatewayCommandLogMapper.selectObjectPage(qry);
		long total = gatewayCommandLogMapper.selectObjectCount(qry);
		return Result.ok(Page.create(list.stream().map(GatewayCommandConvertor::toClientObject).toList(), total));
	}

}
