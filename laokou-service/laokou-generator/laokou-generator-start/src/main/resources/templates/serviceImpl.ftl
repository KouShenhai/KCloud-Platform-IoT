// @formatter:off
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

package org.laokou.admin.service;

import lombok.RequiredArgsConstructor;
import ${packageName}.${instanceName}.api.${className}sServiceI;
import ${packageName}.${instanceName}.command.*;
import ${packageName}.${instanceName}.command.query.${className}GetQryExe;
import ${packageName}.${instanceName}.command.query.${className}PageQryExe;
import ${packageName}.${instanceName}.dto.*;
import ${packageName}.${instanceName}.dto.clientobject.${className}CO;
import org.laokou.common.i18n.dto.Page;
import org.laokou.common.i18n.dto.Result;
import org.springframework.stereotype.Service;

/**
 * @author ${author}
 */
@Service
@RequiredArgsConstructor
public class ${className}sServiceImpl implements ${className}sServiceI {

	private final ${className}SaveCmdExe ${instanceName}SaveCmdExe;

	private final ${className}ModifyCmdExe ${instanceName}ModifyCmdExe;

	private final ${className}RemoveCmdExe ${instanceName}RemoveCmdExe;

	private final ${className}ImportCmdExe ${instanceName}ImportCmdExe;

	private final ${className}ExortCmdExe ${instanceName}ExportCmdExe;

	private final ${className}PageQryExe ${instanceName}PageQryExe;

	private final ${className}GetQryExe ${instanceName}GetQryExe;

	@Override
	public void save(${className}SaveCmd cmd) {
		${instanceName}SaveCmdExe.executeVoid(cmd);
	}

	@Override
	public void modify(${className}ModifyCmd cmd) {
		${instanceName}ModifyCmdExe.executeVoid(cmd);
	}

	@Override
	public void remove(${className}RemoveCmd cmd) {
		${instanceName}RemoveCmdExe.executeVoid(cmd);
	}

	@Override
	public void importI(${className}ImportCmd cmd) {
		${instanceName}ImportCmdExe.executeVoid(cmd);
	}

	@Override
	public void export(${className}ExportCmd cmd) {
		${instanceName}ExportCmdExe.executeVoid(cmd);
	}

	@Override
	public Result<Page<${className}CO>> page(${className}PageQry qry) {
		return ${instanceName}SaveCmdExe.execute(qry);
	}

	@Override
	public Result<${className}CO> getById(${className}GetQry qry) {
		return ${instanceName}SaveCmdExe.execute(qry);
	}

}
// @formatter:on
