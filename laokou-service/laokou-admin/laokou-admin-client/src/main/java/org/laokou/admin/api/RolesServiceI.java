/*
 * Copyright (c) 2022-2024 KCloud-Platform-IOT Author or Authors. All Rights Reserved.
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

package org.laokou.admin.api;

import org.laokou.common.i18n.dto.Option;
import org.laokou.admin.dto.role.*;
import org.laokou.admin.dto.role.clientobject.RoleCO;
import org.laokou.common.i18n.dto.Datas;
import org.laokou.common.i18n.dto.Result;

import java.util.List;

/**
 * 角色管理.
 *
 * @author laokou
 */
public interface RolesServiceI {

	/**
	 * 查询角色列表.
	 * @param qry 查询角色列表
	 * @return 角色列表
	 */
	Result<Datas<RoleCO>> findList(RoleListQry qry);

	/**
	 * 查询角色下拉框选择项列表.
	 * @return 角色下拉框选择项列表
	 */
	Result<List<Option>> findOptionList();

	/**
	 * 根据ID查看角色.
	 * @param qry 根据ID查看角色
	 * @return 角色
	 */
	Result<RoleCO> findById(RoleGetQry qry);

	/**
	 * 新增角色.
	 * @param cmd 新增角色参数
	 */
	void create(RoleCreateCmd cmd);

	/**
	 * 修改角色.
	 * @param cmd 修改角色参数
	 */
	void modify(RoleModifyCmd cmd);

	/**
	 * 根据IDS删除角色.
	 * @param cmd 根据IDS删除角色参数
	 */
	void remove(RoleRemoveCmd cmd);

}
