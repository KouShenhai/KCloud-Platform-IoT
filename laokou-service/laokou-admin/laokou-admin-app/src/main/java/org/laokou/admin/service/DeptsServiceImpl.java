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
import org.laokou.admin.api.DeptsServiceI;
import org.laokou.admin.dto.dept.*;
import org.laokou.admin.dto.dept.clientobject.DeptCO;
import org.laokou.admin.command.dept.DeptRemoveCmdExe;
import org.laokou.admin.command.dept.DeptCreateCmdExe;
import org.laokou.admin.command.dept.DeptModifyCmdExe;
import org.laokou.admin.command.dept.query.DeptGetQryExe;
import org.laokou.admin.command.dept.query.DeptIdsGetQryExe;
import org.laokou.admin.command.dept.query.DeptListQryExe;
import org.laokou.common.i18n.dto.Result;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 部门管理.
 *
 * @author laokou
 */
@Service
@RequiredArgsConstructor
public class DeptsServiceImpl implements DeptsServiceI {

	private final DeptListQryExe deptListQryExe;

	private final DeptCreateCmdExe deptCreateCmdExe;

	private final DeptModifyCmdExe deptModifyCmdExe;

	private final DeptRemoveCmdExe deptRemoveCmdExe;

	private final DeptGetQryExe deptGetQryExe;

	private final DeptIdsGetQryExe deptIDSGetQryExe;

	/**
	 * 查询部门列表.
	 * @param qry 查询部门列表参数
	 * @return 部门列表
	 */
	@Override
	public Result<List<DeptCO>> findList(DeptListQry qry) {
		return deptListQryExe.execute(qry);
	}

	/**
	 * 新增部门.
	 * @param cmd 新增部门参数
	 */
	@Override
	public void create(DeptCreateCmd cmd) {
		deptCreateCmdExe.executeVoid(cmd);
	}

	/**
	 * 修改部门.
	 * @param cmd 修改部门参数
	 */
	@Override
	public void modify(DeptModifyCmd cmd) {
		deptModifyCmdExe.executeVoid(cmd);
	}

	/**
	 * 根据ID删除部门.
	 * @param cmd 根据ID删除部门参数
	 */
	@Override
	public void remove(DeptRemoveCmd cmd) {
		deptRemoveCmdExe.executeVoid(cmd);
	}

	/**
	 * 根据ID查看部门.
	 * @param qry 根据ID查看部门参数
	 * @return 部门
	 */
	@Override
	public Result<DeptCO> findById(DeptGetQry qry) {
		return deptGetQryExe.execute(qry);
	}

	/**
	 * 根据角色ID查看部门IDS.
	 * @param qry 根据角色ID查看部门IDS参数
	 * @return 部门IDS
	 */
	@Override
	public Result<List<Long>> findIds(DeptIdsGetQry qry) {
		return deptIDSGetQryExe.execute(qry);
	}

}
