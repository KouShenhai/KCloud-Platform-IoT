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

package org.laokou.admin.role.api;

import org.laokou.admin.role.dto.*;
import org.laokou.admin.role.dto.clientobject.RoleCO;
import org.laokou.common.i18n.dto.Page;
import org.laokou.common.i18n.dto.Result;

/**
 * 角色接口.
 *
 * @author laokou
 */
public interface RolesServiceI {

	/**
	 * 保存角色.
	 * @param cmd 保存命令
	 */
	void save(RoleSaveCmd cmd);

	/**
	 * 修改角色.
	 * @param cmd 修改命令
	 */
	void modify(RoleModifyCmd cmd);

	/**
	 * 删除角色.
	 * @param cmd 删除命令
	 */
	void remove(RoleRemoveCmd cmd);

	/**
	 * 导入角色.
	 * @param cmd 导入命令
	 */
	void importI(RoleImportCmd cmd);

	/**
	 * 导出角色.
	 * @param cmd 导出命令
	 */
	void export(RoleExportCmd cmd);

	/**
	 * 分页查询角色.
	 * @param qry 分页查询请求
	 */
	Result<Page<RoleCO>> page(RolePageQry qry);

	/**
	 * 查看角色.
	 * @param qry 查看请求
	 */
	Result<RoleCO> getById(RoleGetQry qry);

}
