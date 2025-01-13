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

package org.laokou.admin.dictItem.service;

import lombok.RequiredArgsConstructor;
import org.laokou.admin.dictItem.api.DictItemsServiceI;
import org.laokou.admin.dictItem.command.*;
import org.laokou.admin.dictItem.command.query.DictItemGetQryExe;
import org.laokou.admin.dictItem.command.query.DictItemPageQryExe;
import org.laokou.admin.dictItem.dto.*;
import org.laokou.admin.dictItem.dto.clientobject.DictItemCO;
import org.laokou.common.i18n.dto.Page;
import org.laokou.common.i18n.dto.Result;
import org.springframework.stereotype.Service;

/**
 * 字典项接口实现类.
 *
 * @author laokou
 */
@Service
@RequiredArgsConstructor
public class DictItemsServiceImpl implements DictItemsServiceI {

	private final DictItemSaveCmdExe dictItemSaveCmdExe;

	private final DictItemModifyCmdExe dictItemModifyCmdExe;

	private final DictItemRemoveCmdExe dictItemRemoveCmdExe;

	private final DictItemImportCmdExe dictItemImportCmdExe;

	private final DictItemExportCmdExe dictItemExportCmdExe;

	private final DictItemPageQryExe dictItemPageQryExe;

	private final DictItemGetQryExe dictItemGetQryExe;

	@Override
	public void save(DictItemSaveCmd cmd) {
		dictItemSaveCmdExe.executeVoid(cmd);
	}

	@Override
	public void modify(DictItemModifyCmd cmd) {
		dictItemModifyCmdExe.executeVoid(cmd);
	}

	@Override
	public void remove(DictItemRemoveCmd cmd) {
		dictItemRemoveCmdExe.executeVoid(cmd);
	}

	@Override
	public void importI(DictItemImportCmd cmd) {
		dictItemImportCmdExe.executeVoid(cmd);
	}

	@Override
	public void export(DictItemExportCmd cmd) {
		dictItemExportCmdExe.executeVoid(cmd);
	}

	@Override
	public Result<Page<DictItemCO>> page(DictItemPageQry qry) {
		return dictItemPageQryExe.execute(qry);
	}

	@Override
	public Result<DictItemCO> getById(DictItemGetQry qry) {
		return dictItemGetQryExe.execute(qry);
	}

}
