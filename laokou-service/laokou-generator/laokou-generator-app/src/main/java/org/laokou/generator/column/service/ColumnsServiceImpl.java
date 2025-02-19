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

package org.laokou.generator.column.service;

import lombok.RequiredArgsConstructor;
import org.laokou.generator.column.api.ColumnsServiceI;
import org.laokou.generator.column.command.*;
import org.laokou.generator.column.command.query.ColumnGetQryExe;
import org.laokou.generator.column.command.query.ColumnPageQryExe;
import org.laokou.generator.column.dto.*;
import org.laokou.generator.column.dto.clientobject.ColumnCO;
import org.laokou.common.i18n.dto.Page;
import org.laokou.common.i18n.dto.Result;
import org.springframework.stereotype.Service;

/**
 *
 * 代码生成器字段接口实现类.
 *
 * @author laokou
 */
@Service
@RequiredArgsConstructor
public class ColumnsServiceImpl implements ColumnsServiceI {

	private final ColumnSaveCmdExe columnSaveCmdExe;

	private final ColumnModifyCmdExe columnModifyCmdExe;

	private final ColumnRemoveCmdExe columnRemoveCmdExe;

	private final ColumnImportCmdExe columnImportCmdExe;

	private final ColumnExportCmdExe columnExportCmdExe;

	private final ColumnPageQryExe columnPageQryExe;

	private final ColumnGetQryExe columnGetQryExe;

	@Override
	public void save(ColumnSaveCmd cmd) {
		columnSaveCmdExe.executeVoid(cmd);
	}

	@Override
	public void modify(ColumnModifyCmd cmd) {
		columnModifyCmdExe.executeVoid(cmd);
	}

	@Override
	public void remove(ColumnRemoveCmd cmd) {
		columnRemoveCmdExe.executeVoid(cmd);
	}

	@Override
	public void importI(ColumnImportCmd cmd) {
		columnImportCmdExe.executeVoid(cmd);
	}

	@Override
	public void export(ColumnExportCmd cmd) {
		columnExportCmdExe.executeVoid(cmd);
	}

	@Override
	public Result<Page<ColumnCO>> page(ColumnPageQry qry) {
		return columnPageQryExe.execute(qry);
	}

	@Override
	public Result<ColumnCO> getById(ColumnGetQry qry) {
		return columnGetQryExe.execute(qry);
	}

}
