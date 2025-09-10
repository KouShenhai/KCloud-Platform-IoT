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


import org.laokou.admin.role.dto.RoleExportCmd;
import org.laokou.admin.role.dto.RoleGetQry;
import org.laokou.admin.role.dto.RoleImportCmd;
import org.laokou.admin.role.dto.RoleModifyAuthorityCmd;
import org.laokou.admin.role.dto.RoleModifyCmd;
import org.laokou.admin.role.dto.RolePageQry;
import org.laokou.admin.role.dto.RoleRemoveCmd;
import org.laokou.admin.role.dto.RoleSaveCmd;
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
	void saveRole(RoleSaveCmd cmd);

	/**
	 * 修改角色.
	 * @param cmd 修改命令
	 */
	void modifyRole(RoleModifyCmd cmd);

	/**
	 * 删除角色.
	 * @param cmd 删除命令
	 */
	void removeRole(RoleRemoveCmd cmd) throws InterruptedException;

	/**
	 * 导入角色.
	 * @param cmd 导入命令
	 */
	void importRole(RoleImportCmd cmd);

	/**
	 * 导出角色.
	 * @param cmd 导出命令
	 */
	void exportRole(RoleExportCmd cmd);

	/**
	 * 修改权限.
	 * @param cmd 修改权限命令
	 */
	void modifyRoleAuthority(RoleModifyAuthorityCmd cmd) throws InterruptedException;

	/**
	 * 分页查询角色.
	 * @param qry 分页查询请求
	 */
	Result<Page<RoleCO>> pageRole(RolePageQry qry);

	/**
	 * 查看角色.
	 * @param qry 查看请求
	 */
	Result<RoleCO> getRoleById(RoleGetQry qry);

}
