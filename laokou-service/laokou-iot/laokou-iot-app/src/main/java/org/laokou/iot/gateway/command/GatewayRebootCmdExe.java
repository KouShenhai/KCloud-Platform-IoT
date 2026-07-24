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

package org.laokou.iot.gateway.command;

import lombok.RequiredArgsConstructor;
import org.laokou.common.domain.annotation.CommandLog;
import org.laokou.common.i18n.common.IdGenerator;
import org.laokou.common.i18n.util.InstantUtils;
import org.laokou.common.mybatisplus.util.TransactionalUtils;
import org.laokou.iot.gateway.ability.GatewayCommandDomainService;
import org.laokou.iot.gateway.dto.GatewayRebootCmd;
import org.laokou.iot.gateway.gateway.GatewayGateway;
import org.laokou.iot.gateway.model.GatewayCommandE;
import org.springframework.stereotype.Component;

/**
 *
 * 重启网关命令执行器.
 *
 * @author laokou
 */
@Component
@RequiredArgsConstructor
public class GatewayRebootCmdExe {

	private final GatewayCommandDomainService gatewayCommandDomainService;

	private final GatewayGateway gatewayGateway;

	private final TransactionalUtils transactionalUtils;

	private final IdGenerator idGenerator;

	@CommandLog
	public Long execute(GatewayRebootCmd cmd) {
		Long commandId = idGenerator.getId();
		GatewayCommandE gatewayCommandE = new GatewayCommandE();
		gatewayCommandE.setCommandId(commandId);
		gatewayCommandE.setGatewayId(cmd.getGatewayId());
		gatewayCommandE.setGatewayKey(gatewayGateway.findGatewayKeyById(cmd.getGatewayId()));
		gatewayCommandE.setType(GatewayCommandE.TYPE_REBOOT);
		gatewayCommandE.setStatus(GatewayCommandE.STATUS_PENDING);
		gatewayCommandE.setPayload("{}");
		gatewayCommandE.setTimestamp(InstantUtils.now().toEpochMilli());
		transactionalUtils.executeInTransaction(() -> gatewayCommandDomainService.dispatch(gatewayCommandE));
		return commandId;
	}

}
