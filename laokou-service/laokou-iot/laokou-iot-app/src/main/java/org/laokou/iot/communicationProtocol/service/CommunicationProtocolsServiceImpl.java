/*
 * Copyright (c) 2022-2024 KCloud-Platform-IoT Author or Authors. All Rights Reserved.
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

package org.laokou.iot.communicationProtocol.service;

import lombok.RequiredArgsConstructor;
import org.laokou.iot.communicationProtocol.api.CommunicationProtocolsServiceI;
import org.laokou.iot.communicationProtocol.command.*;
import org.laokou.iot.communicationProtocol.dto.*;
import org.laokou.iot.communicationProtocol.command.query.CommunicationProtocolGetQryExe;
import org.laokou.iot.communicationProtocol.command.query.CommunicationProtocolPageQryExe;
import org.laokou.iot.communicationProtocol.dto.clientobject.CommunicationProtocolCO;
import org.laokou.common.i18n.dto.Page;
import org.laokou.common.i18n.dto.Result;
import org.springframework.stereotype.Service;

/**
 *
 * 通讯协议接口实现类.
 *
 * @author laokou
 */
@Service
@RequiredArgsConstructor
public class CommunicationProtocolsServiceImpl implements CommunicationProtocolsServiceI {

	private final CommunicationProtocolSaveCmdExe communicationProtocolSaveCmdExe;

	private final CommunicationProtocolModifyCmdExe communicationProtocolModifyCmdExe;

	private final CommunicationProtocolRemoveCmdExe communicationProtocolRemoveCmdExe;

	private final CommunicationProtocolImportCmdExe communicationProtocolImportCmdExe;

	private final CommunicationProtocolExportCmdExe communicationProtocolExportCmdExe;

	private final CommunicationProtocolPageQryExe communicationProtocolPageQryExe;

	private final CommunicationProtocolGetQryExe communicationProtocolGetQryExe;

	@Override
	public void save(CommunicationProtocolSaveCmd cmd) {
		communicationProtocolSaveCmdExe.executeVoid(cmd);
	}

	@Override
	public void modify(CommunicationProtocolModifyCmd cmd) {
		communicationProtocolModifyCmdExe.executeVoid(cmd);
	}

	@Override
	public void remove(CommunicationProtocolRemoveCmd cmd) {
		communicationProtocolRemoveCmdExe.executeVoid(cmd);
	}

	@Override
	public void importI(CommunicationProtocolImportCmd cmd) {
		communicationProtocolImportCmdExe.executeVoid(cmd);
	}

	@Override
	public void export(CommunicationProtocolExportCmd cmd) {
		communicationProtocolExportCmdExe.executeVoid(cmd);
	}

	@Override
	public Result<Page<CommunicationProtocolCO>> page(CommunicationProtocolPageQry qry) {
		return communicationProtocolPageQryExe.execute(qry);
	}

	@Override
	public Result<CommunicationProtocolCO> getById(CommunicationProtocolGetQry qry) {
		return communicationProtocolGetQryExe.execute(qry);
	}

}
