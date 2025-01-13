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

package org.laokou.admin.tenant.api;

import org.laokou.admin.tenant.dto.*;
import org.laokou.admin.tenant.dto.clientobject.TenantCO;
import org.laokou.common.i18n.dto.Page;
import org.laokou.common.i18n.dto.Result;

/**
 * 租户接口.
 *
 * @author laokou
 */
public interface TenantsServiceI {

	/**
	 * 保存租户.
	 * @param cmd 保存命令
	 */
	void save(TenantSaveCmd cmd);

	/**
	 * 修改租户.
	 * @param cmd 修改命令
	 */
	void modify(TenantModifyCmd cmd);

	/**
	 * 删除租户.
	 * @param cmd 删除命令
	 */
	void remove(TenantRemoveCmd cmd);

	/**
	 * 导入租户.
	 * @param cmd 导入命令
	 */
	void importI(TenantImportCmd cmd);

	/**
	 * 导出租户.
	 * @param cmd 导出命令
	 */
	void export(TenantExportCmd cmd);

	/**
	 * 分页查询租户.
	 * @param qry 分页查询请求
	 */
	Result<Page<TenantCO>> page(TenantPageQry qry);

	/**
	 * 查看租户.
	 * @param qry 查看请求
	 */
	Result<TenantCO> getById(TenantGetQry qry);

}
