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

package org.laokou.admin.dept.api;

import org.laokou.admin.dept.dto.*;
import org.laokou.admin.dept.dto.clientobject.DeptCO;
import org.laokou.admin.dept.dto.clientobject.DeptTreeCO;
import org.laokou.common.i18n.dto.Page;
import org.laokou.common.i18n.dto.Result;

import java.util.List;

/**
 * 部门接口.
 *
 * @author laokou
 */
public interface DeptsServiceI {

	/**
	 * 保存部门.
	 * @param cmd 保存命令
	 */
	void saveDept(DeptSaveCmd cmd);

	/**
	 * 修改部门.
	 * @param cmd 修改命令
	 */
	void modifyDept(DeptModifyCmd cmd);

	/**
	 * 删除部门.
	 * @param cmd 删除命令
	 */
	void removeDept(DeptRemoveCmd cmd);

	/**
	 * 导入部门.
	 * @param cmd 导入命令
	 */
	void importDept(DeptImportCmd cmd);

	/**
	 * 导出部门.
	 * @param cmd 导出命令
	 */
	void exportDept(DeptExportCmd cmd);

	/**
	 * 分页查询部门列表.
	 * @param qry 分页查询请求
	 */
	Result<Page<DeptCO>> pageDept(DeptPageQry qry);

	/**
	 * 查询部门树列表.
	 * @param qry 查询请求
	 */
	Result<List<DeptTreeCO>> listTreeDept(DeptTreeListQry qry);

	/**
	 * 查看部门.
	 * @param qry 查看请求
	 */
	Result<DeptCO> getDeptById(DeptGetQry qry);

}
