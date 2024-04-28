/*
 * Copyright (c) 2022-2024 KCloud-Platform-Alibaba Author or Authors. All Rights Reserved.
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
import org.laokou.admin.api.RolesServiceI;
import org.laokou.common.i18n.dto.Option;
import org.laokou.admin.dto.role.*;
import org.laokou.admin.dto.role.clientobject.RoleCO;
import org.laokou.admin.command.role.RoleRemoveCmdExe;
import org.laokou.admin.command.role.RoleCreateCmdExe;
import org.laokou.admin.command.role.RoleModifyCmdExe;
import org.laokou.admin.command.role.query.RoleGetQryExe;
import org.laokou.admin.command.role.query.RoleListQryExe;
import org.laokou.admin.command.role.query.RoleOptionListQryExe;
import org.laokou.common.i18n.dto.Datas;
import org.laokou.common.i18n.dto.Result;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 角色管理.
 *
 * @author laokou
 */
@Service
@RequiredArgsConstructor
public class RolesServiceImpl implements RolesServiceI {

	private final RoleListQryExe roleListQryExe;

	private final RoleOptionListQryExe roleOptionListQryExe;

	private final RoleGetQryExe roleGetQryExe;

	private final RoleCreateCmdExe roleCreateCmdExe;

	private final RoleModifyCmdExe roleModifyCmdExe;

	private final RoleRemoveCmdExe roleRemoveCmdExe;

	/**
	 * 查询角色列表.
	 * @param qry 查询角色列表
	 * @return 查询角色列表
	 */
	@Override
	public Result<Datas<RoleCO>> findList(RoleListQry qry) {
		return roleListQryExe.execute(qry);
	}

	/**
	 * 查询角色下拉框选择项列表.
	 * @return 角色下拉框选择项列表
	 */
	@Override
	public Result<List<Option>> findOptionList() {
		return roleOptionListQryExe.execute();
	}

	/**
	 * 根据ID查看角色.
	 * @param qry 根据ID查看角色
	 * @return 角色
	 */
	@Override
	public Result<RoleCO> findById(RoleGetQry qry) {
		return roleGetQryExe.execute(qry);
	}

	/**
	 * 新增角色.
	 * @param cmd 新增角色参数
	 */
	@Override
	public void create(RoleCreateCmd cmd) {
		roleCreateCmdExe.executeVoid(cmd);
	}

	/**
	 * 修改角色.
	 * @param cmd 修改角色参数
	 */
	@Override
	public void modify(RoleModifyCmd cmd) {
		roleModifyCmdExe.executeVoid(cmd);
	}

	/**
	 * 根据IDS删除角色.
	 * @param cmd 根据IDS删除角色参数
	 */
	@Override
	public void remove(RoleRemoveCmd cmd) {
		roleRemoveCmdExe.executeVoid(cmd);
	}

}
