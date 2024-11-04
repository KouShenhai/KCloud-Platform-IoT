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

package org.laokou.iot.device.service;

import lombok.RequiredArgsConstructor;
import org.laokou.iot.device.api.DevicesServiceI;
import org.laokou.iot.device.command.*;
import org.laokou.iot.device.command.query.DeviceGetQryExe;
import org.laokou.iot.device.command.query.DevicePageQryExe;
import org.laokou.iot.device.dto.*;
import org.laokou.iot.device.dto.clientobject.DeviceCO;
import org.laokou.common.i18n.dto.Page;
import org.laokou.common.i18n.dto.Result;
import org.springframework.stereotype.Service;

/**
 *
 * 设备接口实现类.
 *
 * @author laokou
 */
@Service
@RequiredArgsConstructor
public class DevicesServiceImpl implements DevicesServiceI {

	private final DeviceSaveCmdExe deviceSaveCmdExe;

	private final DeviceModifyCmdExe deviceModifyCmdExe;

	private final DeviceRemoveCmdExe deviceRemoveCmdExe;

	private final DeviceImportCmdExe deviceImportCmdExe;

	private final DeviceExportCmdExe deviceExportCmdExe;

	private final DevicePageQryExe devicePageQryExe;

	private final DeviceGetQryExe deviceGetQryExe;

	@Override
	public void save(DeviceSaveCmd cmd) {
		deviceSaveCmdExe.executeVoid(cmd);
	}

	@Override
	public void modify(DeviceModifyCmd cmd) {
		deviceModifyCmdExe.executeVoid(cmd);
	}

	@Override
	public void remove(DeviceRemoveCmd cmd) {
		deviceRemoveCmdExe.executeVoid(cmd);
	}

	@Override
	public void importI(DeviceImportCmd cmd) {
		deviceImportCmdExe.executeVoid(cmd);
	}

	@Override
	public void export(DeviceExportCmd cmd) {
		deviceExportCmdExe.executeVoid(cmd);
	}

	@Override
	public Result<Page<DeviceCO>> page(DevicePageQry qry) {
		return devicePageQryExe.execute(qry);
	}

	@Override
	public Result<DeviceCO> getById(DeviceGetQry qry) {
		return deviceGetQryExe.execute(qry);
	}

}
