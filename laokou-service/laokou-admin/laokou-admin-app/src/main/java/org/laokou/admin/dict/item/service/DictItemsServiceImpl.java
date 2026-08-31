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

package org.laokou.admin.dict.item.service;

import lombok.RequiredArgsConstructor;
import org.laokou.admin.dict.item.api.DictItemsServiceI;
import org.laokou.admin.dict.item.command.DictItemExportCmdExe;
import org.laokou.admin.dict.item.command.DictItemImportCmdExe;
import org.laokou.admin.dict.item.command.DictItemModifyCmdExe;
import org.laokou.admin.dict.item.command.DictItemRemoveCmdExe;
import org.laokou.admin.dict.item.command.DictItemSaveCmdExe;
import org.laokou.admin.dict.item.command.query.DictItemGetQryExe;
import org.laokou.admin.dict.item.command.query.DictItemListQryExe;
import org.laokou.admin.dict.item.command.query.DictItemPageQryExe;
import org.laokou.admin.dict.item.dto.DictItemExportCmd;
import org.laokou.admin.dict.item.dto.DictItemGetQry;
import org.laokou.admin.dict.item.dto.DictItemImportCmd;
import org.laokou.admin.dict.item.dto.DictItemListQry;
import org.laokou.admin.dict.item.dto.DictItemModifyCmd;
import org.laokou.admin.dict.item.dto.DictItemPageQry;
import org.laokou.admin.dict.item.dto.DictItemRemoveCmd;
import org.laokou.admin.dict.item.dto.DictItemSaveCmd;
import org.laokou.admin.dict.item.dto.clientobject.DictItemCO;
import org.laokou.common.i18n.dto.Page;
import org.laokou.common.i18n.dto.Result;
import org.springframework.stereotype.Service;

import java.util.List;

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

	private final DictItemListQryExe dictItemListQryExe;

	@Override
	public void saveDictItem(DictItemSaveCmd cmd) {
		dictItemSaveCmdExe.executeVoid(cmd);
	}

	@Override
	public void modifyDictItem(DictItemModifyCmd cmd) {
		dictItemModifyCmdExe.executeVoid(cmd);
	}

	@Override
	public void removeDictItem(DictItemRemoveCmd cmd) {
		dictItemRemoveCmdExe.executeVoid(cmd);
	}

	@Override
	public void importDictItem(DictItemImportCmd cmd) {
		dictItemImportCmdExe.executeVoid(cmd);
	}

	@Override
	public void exportDictItem(DictItemExportCmd cmd) {
		dictItemExportCmdExe.executeVoid(cmd);
	}

	@Override
	public Result<Page<DictItemCO>> pageDictItem(DictItemPageQry qry) {
		return dictItemPageQryExe.execute(qry);
	}

	@Override
	public Result<List<DictItemCO>> listDictItem(DictItemListQry qry) {
		return dictItemListQryExe.execute(qry);
	}

	@Override
	public Result<DictItemCO> getDictItemById(DictItemGetQry qry) {
		return dictItemGetQryExe.execute(qry);
	}

}
