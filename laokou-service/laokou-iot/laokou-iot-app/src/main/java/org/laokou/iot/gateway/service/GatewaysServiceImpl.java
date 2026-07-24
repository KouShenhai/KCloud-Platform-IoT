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
import org.laokou.iot.gateway.api.GatewaysServiceI;
import org.laokou.iot.gateway.command.GatewayExportCmdExe;
import org.laokou.iot.gateway.command.GatewayImportCmdExe;
import org.laokou.iot.gateway.command.GatewayModifyCmdExe;
import org.laokou.iot.gateway.command.GatewayRemoveCmdExe;
import org.laokou.iot.gateway.command.GatewaySaveCmdExe;
import org.laokou.iot.gateway.command.query.GatewayGetQryExe;
import org.laokou.iot.gateway.command.query.GatewayPageQryExe;
import org.laokou.iot.gateway.dto.GatewayExportCmd;
import org.laokou.iot.gateway.dto.GatewayGetQry;
import org.laokou.iot.gateway.dto.GatewayImportCmd;
import org.laokou.iot.gateway.dto.GatewayModifyCmd;
import org.laokou.iot.gateway.dto.GatewayPageQry;
import org.laokou.iot.gateway.dto.GatewayRemoveCmd;
import org.laokou.iot.gateway.dto.GatewaySaveCmd;
import org.laokou.iot.gateway.dto.clientobject.GatewayCO;
import org.springframework.stereotype.Service;

/**
 *
 * 网关接口实现类.
 *
 * @author laokou
 */
@Service
@RequiredArgsConstructor
public class GatewaysServiceImpl implements GatewaysServiceI {

	private final GatewaySaveCmdExe gatewaySaveCmdExe;

	private final GatewayModifyCmdExe gatewayModifyCmdExe;

	private final GatewayRemoveCmdExe gatewayRemoveCmdExe;

	private final GatewayImportCmdExe gatewayImportCmdExe;

	private final GatewayExportCmdExe gatewayExportCmdExe;

	private final GatewayPageQryExe gatewayPageQryExe;

	private final GatewayGetQryExe gatewayGetQryExe;

	@Override
	public void saveGateway(GatewaySaveCmd cmd) {
		gatewaySaveCmdExe.executeVoid(cmd);
	}

	@Override
	public void modifyGateway(GatewayModifyCmd cmd) {
		gatewayModifyCmdExe.executeVoid(cmd);
	}

	@Override
	public void removeGateway(GatewayRemoveCmd cmd) {
		gatewayRemoveCmdExe.executeVoid(cmd);
	}

	@Override
	public void importGateway(GatewayImportCmd cmd) {
		gatewayImportCmdExe.executeVoid(cmd);
	}

	@Override
	public void exportGateway(GatewayExportCmd cmd) {
		gatewayExportCmdExe.executeVoid(cmd);
	}

	@Override
	public Result<Page<GatewayCO>> pageGateway(GatewayPageQry qry) {
		return gatewayPageQryExe.execute(qry);
	}

	@Override
	public Result<GatewayCO> getGatewayById(GatewayGetQry qry) {
		return gatewayGetQryExe.execute(qry);
	}

}
