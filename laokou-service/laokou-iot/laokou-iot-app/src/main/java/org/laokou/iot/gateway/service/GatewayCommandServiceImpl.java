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

package org.laokou.iot.gateway.service;

import lombok.RequiredArgsConstructor;
import org.laokou.common.i18n.dto.Page;
import org.laokou.common.i18n.dto.Result;
import org.laokou.iot.gateway.ability.GatewayCommandDomainService;
import org.laokou.iot.gateway.api.GatewayCommandServiceI;
import org.laokou.iot.gateway.command.GatewayReadPropertyCmdExe;
import org.laokou.iot.gateway.command.GatewayRebootCmdExe;
import org.laokou.iot.gateway.command.GatewayWritePropertyCmdExe;
import org.laokou.iot.gateway.command.query.GatewayCommandLogPageQryExe;
import org.laokou.iot.gateway.dto.GatewayCommandLogPageQry;
import org.laokou.iot.gateway.dto.GatewayReadPropertyCmd;
import org.laokou.iot.gateway.dto.GatewayRebootCmd;
import org.laokou.iot.gateway.dto.GatewayWritePropertyCmd;
import org.laokou.iot.gateway.dto.clientobject.GatewayCommandLogCO;
import org.springframework.stereotype.Service;

/**
 *
 * 网关指令接口实现类.
 *
 * @author laokou
 */
@Service
@RequiredArgsConstructor
public class GatewayCommandServiceImpl implements GatewayCommandServiceI {

	private final GatewayRebootCmdExe gatewayRebootCmdExe;

	private final GatewayReadPropertyCmdExe gatewayReadPropertyCmdExe;

	private final GatewayWritePropertyCmdExe gatewayWritePropertyCmdExe;

	private final GatewayCommandLogPageQryExe gatewayCommandLogPageQryExe;

	private final GatewayCommandDomainService gatewayCommandDomainService;

	@Override
	public Result<Long> reboot(GatewayRebootCmd cmd) {
		return Result.ok(gatewayRebootCmdExe.execute(cmd));
	}

	@Override
	public Result<Long> readProperty(GatewayReadPropertyCmd cmd) {
		return Result.ok(gatewayReadPropertyCmdExe.execute(cmd));
	}

	@Override
	public Result<Long> writeProperty(GatewayWritePropertyCmd cmd) {
		return Result.ok(gatewayWritePropertyCmdExe.execute(cmd));
	}

	@Override
	public void handleReply(Long commandId, Integer status, String result) {
		gatewayCommandDomainService.handleReply(commandId, status, result);
	}

	@Override
	public Result<Page<GatewayCommandLogCO>> pageCommandLog(GatewayCommandLogPageQry qry) {
		return gatewayCommandLogPageQryExe.execute(qry);
	}

}
