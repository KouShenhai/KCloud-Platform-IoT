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

package org.laokou.admin.dict.service;

import lombok.RequiredArgsConstructor;
import org.laokou.admin.dict.api.DictsServiceI;
import org.laokou.admin.dict.command.DictExportCmdExe;
import org.laokou.admin.dict.command.DictImportCmdExe;
import org.laokou.admin.dict.command.DictModifyCmdExe;
import org.laokou.admin.dict.command.DictRemoveCmdExe;
import org.laokou.admin.dict.command.DictSaveCmdExe;
import org.laokou.admin.dict.command.query.DictGetQryExe;
import org.laokou.admin.dict.command.query.DictPageQryExe;
import org.laokou.admin.dict.dto.DictExportCmd;
import org.laokou.admin.dict.dto.DictGetQry;
import org.laokou.admin.dict.dto.DictImportCmd;
import org.laokou.admin.dict.dto.DictModifyCmd;
import org.laokou.admin.dict.dto.DictPageQry;
import org.laokou.admin.dict.dto.DictRemoveCmd;
import org.laokou.admin.dict.dto.DictSaveCmd;
import org.laokou.admin.dict.dto.clientobject.DictCO;
import org.laokou.common.i18n.dto.Page;
import org.laokou.common.i18n.dto.Result;
import org.springframework.stereotype.Service;

/**
 * 字典接口实现类.
 *
 * @author laokou
 */
@Service
@RequiredArgsConstructor
public class DictsServiceImpl implements DictsServiceI {

	private final DictSaveCmdExe dictSaveCmdExe;

	private final DictModifyCmdExe dictModifyCmdExe;

	private final DictRemoveCmdExe dictRemoveCmdExe;

	private final DictImportCmdExe dictImportCmdExe;

	private final DictExportCmdExe dictExportCmdExe;

	private final DictPageQryExe dictPageQryExe;

	private final DictGetQryExe dictGetQryExe;

	@Override
	public void saveDict(DictSaveCmd cmd) {
		dictSaveCmdExe.executeVoid(cmd);
	}

	@Override
	public void modifyDict(DictModifyCmd cmd) {
		dictModifyCmdExe.executeVoid(cmd);
	}

	@Override
	public void removeDict(DictRemoveCmd cmd) {
		dictRemoveCmdExe.executeVoid(cmd);
	}

	@Override
	public void importDict(DictImportCmd cmd) {
		dictImportCmdExe.executeVoid(cmd);
	}

	@Override
	public void exportDict(DictExportCmd cmd) {
		dictExportCmdExe.executeVoid(cmd);
	}

	@Override
	public Result<Page<DictCO>> pageDict(DictPageQry qry) {
		return dictPageQryExe.execute(qry);
	}

	@Override
	public Result<DictCO> getDictById(DictGetQry qry) {
		return dictGetQryExe.execute(qry);
	}

}
