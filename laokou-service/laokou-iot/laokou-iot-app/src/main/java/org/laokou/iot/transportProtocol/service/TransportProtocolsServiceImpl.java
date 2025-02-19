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

package org.laokou.iot.transportProtocol.service;

import lombok.RequiredArgsConstructor;
import org.laokou.iot.transportProtocol.api.TransportProtocolsServiceI;
import org.laokou.iot.transportProtocol.command.*;
import org.laokou.iot.transportProtocol.command.query.TransportProtocolGetQryExe;
import org.laokou.iot.transportProtocol.command.query.TransportProtocolPageQryExe;
import org.laokou.iot.transportProtocol.dto.*;
import org.laokou.iot.transportProtocol.dto.clientobject.TransportProtocolCO;
import org.laokou.common.i18n.dto.Page;
import org.laokou.common.i18n.dto.Result;
import org.springframework.stereotype.Service;

/**
 *
 * 传输协议接口实现类.
 *
 * @author laokou
 */
@Service
@RequiredArgsConstructor
public class TransportProtocolsServiceImpl implements TransportProtocolsServiceI {

	private final TransportProtocolSaveCmdExe transportProtocolSaveCmdExe;

	private final TransportProtocolModifyCmdExe transportProtocolModifyCmdExe;

	private final TransportProtocolRemoveCmdExe transportProtocolRemoveCmdExe;

	private final TransportProtocolImportCmdExe transportProtocolImportCmdExe;

	private final TransportProtocolExportCmdExe transportProtocolExportCmdExe;

	private final TransportProtocolPageQryExe transportProtocolPageQryExe;

	private final TransportProtocolGetQryExe transportProtocolGetQryExe;

	@Override
	public void save(TransportProtocolSaveCmd cmd) {
		transportProtocolSaveCmdExe.executeVoid(cmd);
	}

	@Override
	public void modify(TransportProtocolModifyCmd cmd) {
		transportProtocolModifyCmdExe.executeVoid(cmd);
	}

	@Override
	public void remove(TransportProtocolRemoveCmd cmd) {
		transportProtocolRemoveCmdExe.executeVoid(cmd);
	}

	@Override
	public void importI(TransportProtocolImportCmd cmd) {
		transportProtocolImportCmdExe.executeVoid(cmd);
	}

	@Override
	public void export(TransportProtocolExportCmd cmd) {
		transportProtocolExportCmdExe.executeVoid(cmd);
	}

	@Override
	public Result<Page<TransportProtocolCO>> page(TransportProtocolPageQry qry) {
		return transportProtocolPageQryExe.execute(qry);
	}

	@Override
	public Result<TransportProtocolCO> getById(TransportProtocolGetQry qry) {
		return transportProtocolGetQryExe.execute(qry);
	}

}
