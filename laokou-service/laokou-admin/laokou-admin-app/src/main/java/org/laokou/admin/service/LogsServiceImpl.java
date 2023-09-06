/*
 * Copyright (c) 2022 KCloud-Platform-Alibaba Authors. All Rights Reserved.
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

package org.laokou.admin.service;

import lombok.RequiredArgsConstructor;
import org.laokou.admin.client.api.LogsServiceI;
import org.laokou.admin.client.dto.log.LogLoginExportCmd;
import org.laokou.admin.client.dto.log.LogLoginListQry;
import org.laokou.admin.client.dto.log.LogOperateExportCmd;
import org.laokou.admin.client.dto.log.LogOperateListQry;
import org.laokou.admin.client.dto.log.clientobject.LoginLogCO;
import org.laokou.admin.client.dto.log.clientobject.OperateLogCO;
import org.laokou.admin.command.log.LogLoginExportCmdExe;
import org.laokou.admin.command.log.LogOperateExportCmdExe;
import org.laokou.admin.command.log.query.LogLoginListQryExe;
import org.laokou.admin.command.log.query.LogOperateListQryExe;
import org.laokou.common.i18n.dto.Datas;
import org.laokou.common.i18n.dto.Result;
import org.springframework.stereotype.Service;

/**
 * @author laokou
 */
@Service
@RequiredArgsConstructor
public class LogsServiceImpl implements LogsServiceI {

	private final LogOperateListQryExe logOperateListQryExe;

	private final LogOperateExportCmdExe logOperateExportCmdExe;

	private final LogLoginListQryExe logLoginListQryExe;

	private final LogLoginExportCmdExe logLoginExportCmdExe;

	@Override
	public Result<Datas<OperateLogCO>> operateList(LogOperateListQry qry) {
		return null;
	}

	@Override
	public Result<Boolean> operateExport(LogOperateExportCmd cmd) {
		return null;
	}

	@Override
	public Result<Datas<LoginLogCO>> loginList(LogLoginListQry qry) {
		return null;
	}

	@Override
	public Result<Boolean> loginExport(LogLoginExportCmd cmd) {
		return null;
	}

}
