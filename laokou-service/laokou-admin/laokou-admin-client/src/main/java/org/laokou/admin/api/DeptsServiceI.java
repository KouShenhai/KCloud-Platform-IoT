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

package org.laokou.admin.api;

import org.laokou.admin.dto.dept.*;
import org.laokou.admin.dto.dept.clientobject.DeptCO;
import org.laokou.common.i18n.dto.Result;

import java.util.List;

/**
 * 部门管理.
 *
 * @author laokou
 */
public interface DeptsServiceI {

	/**
	 * 查看部门树.
	 * @param qry 查看部门树参数
	 * @return 部门树
	 */
	Result<DeptCO> tree(DeptTreeGetQry qry);

	/**
	 * 查询部门列表.
	 * @param qry 查询部门列表参数
	 * @return 部门列表
	 */
	Result<List<DeptCO>> list(DeptListQry qry);

	/**
	 * 新增部门.
	 * @param cmd 新增部门参数
	 * @return 新增结果
	 */
	Result<Boolean> insert(DeptInsertCmd cmd);

	/**
	 * 修改部门.
	 * @param cmd 修改部门参数
	 * @return 修改参数
	 */
	Result<Boolean> update(DeptUpdateCmd cmd);

	/**
	 * 根据ID删除部门.
	 * @param cmd 根据ID删除部门参数
	 * @return 删除结果
	 */
	Result<Boolean> deleteById(DeptDeleteCmd cmd);

	/**
	 * 根据ID查看部门.
	 * @param qry 根据ID查看部门参数
	 * @return 部门
	 */
	Result<DeptCO> getById(DeptGetQry qry);

	/**
	 * 根据角色ID查看部门IDS.
	 * @param qry 根据角色ID查看部门IDS参数
	 * @return 部门IDS
	 */
	Result<List<Long>> ids(DeptIDSGetQry qry);

}
