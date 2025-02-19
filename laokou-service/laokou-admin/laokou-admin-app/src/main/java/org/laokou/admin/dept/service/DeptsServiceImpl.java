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

package org.laokou.admin.dept.service;

import lombok.RequiredArgsConstructor;
import org.laokou.admin.dept.api.DeptsServiceI;
import org.laokou.admin.dept.command.*;
import org.laokou.admin.dept.command.query.DeptGetQryExe;
import org.laokou.admin.dept.command.query.DeptPageQryExe;
import org.laokou.admin.dept.command.query.DeptTreeListQryExe;
import org.laokou.admin.dept.dto.*;
import org.laokou.admin.dept.dto.clientobject.DeptCO;
import org.laokou.admin.dept.dto.clientobject.DeptTreeCO;
import org.laokou.common.i18n.dto.Page;
import org.laokou.common.i18n.dto.Result;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 部门接口实现类.
 *
 * @author laokou
 */
@Service
@RequiredArgsConstructor
public class DeptsServiceImpl implements DeptsServiceI {

	private final DeptSaveCmdExe deptSaveCmdExe;

	private final DeptModifyCmdExe deptModifyCmdExe;

	private final DeptRemoveCmdExe deptRemoveCmdExe;

	private final DeptImportCmdExe deptImportCmdExe;

	private final DeptExportCmdExe deptExportCmdExe;

	private final DeptPageQryExe deptPageQryExe;

	private final DeptGetQryExe deptGetQryExe;

	private final DeptTreeListQryExe deptTreeListQryExe;

	@Override
	public void save(DeptSaveCmd cmd) {
		deptSaveCmdExe.executeVoid(cmd);
	}

	@Override
	public void modify(DeptModifyCmd cmd) {
		deptModifyCmdExe.executeVoid(cmd);
	}

	@Override
	public void remove(DeptRemoveCmd cmd) {
		deptRemoveCmdExe.executeVoid(cmd);
	}

	@Override
	public void importI(DeptImportCmd cmd) {
		deptImportCmdExe.executeVoid(cmd);
	}

	@Override
	public void export(DeptExportCmd cmd) {
		deptExportCmdExe.executeVoid(cmd);
	}

	@Override
	public Result<Page<DeptCO>> page(DeptPageQry qry) {
		return deptPageQryExe.execute(qry);
	}

	@Override
	public Result<List<DeptTreeCO>> treeList(DeptTreeListQry qry) {
		return deptTreeListQryExe.execute(qry);
	}

	@Override
	public Result<DeptCO> getById(DeptGetQry qry) {
		return deptGetQryExe.execute(qry);
	}

}
