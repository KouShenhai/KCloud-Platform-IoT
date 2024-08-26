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

package org.laokou.admin.dictType.service;

import lombok.RequiredArgsConstructor;
import org.laokou.admin.dictType.api.DictTypesServiceI;
import org.laokou.admin.dictType.command.*;
import org.laokou.admin.dictType.command.query.DictTypeGetQryExe;
import org.laokou.admin.dictType.command.query.DictTypePageQryExe;
import org.laokou.admin.dictType.dto.*;
import org.laokou.admin.dictType.dto.clientobject.DictTypeCO;
import org.laokou.common.i18n.dto.Page;
import org.laokou.common.i18n.dto.Result;
import org.springframework.stereotype.Service;

/**
 * 字典类型接口实现类.
 *
 * @author laokou
 */
@Service
@RequiredArgsConstructor
public class DictTypesServiceImpl implements DictTypesServiceI {

	private final DictTypeSaveCmdExe dictTypeSaveCmdExe;

	private final DictTypeModifyCmdExe dictTypeModifyCmdExe;

	private final DictTypeRemoveCmdExe dictTypeRemoveCmdExe;

	private final DictTypeImportCmdExe dictTypeImportCmdExe;

	private final DictTypeExportCmdExe dictTypeExportCmdExe;

	private final DictTypePageQryExe dictTypePageQryExe;

	private final DictTypeGetQryExe dictTypeGetQryExe;

	@Override
	public void save(DictTypeSaveCmd cmd) {
		dictTypeSaveCmdExe.executeVoid(cmd);
	}

	@Override
	public void modify(DictTypeModifyCmd cmd) {
		dictTypeModifyCmdExe.executeVoid(cmd);
	}

	@Override
	public void remove(DictTypeRemoveCmd cmd) {
		dictTypeRemoveCmdExe.executeVoid(cmd);
	}

	@Override
	public void importI(DictTypeImportCmd cmd) {
		dictTypeImportCmdExe.executeVoid(cmd);
	}

	@Override
	public void export(DictTypeExportCmd cmd) {
		dictTypeExportCmdExe.executeVoid(cmd);
	}

	@Override
	public Result<Page<DictTypeCO>> page(DictTypePageQry qry) {
		return dictTypePageQryExe.execute(qry);
	}

	@Override
	public Result<DictTypeCO> getById(DictTypeGetQry qry) {
		return dictTypeGetQryExe.execute(qry);
	}

}
