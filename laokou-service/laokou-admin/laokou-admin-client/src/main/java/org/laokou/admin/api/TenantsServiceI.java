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

package org.laokou.admin.api;

import org.laokou.admin.dto.tenant.*;
import org.laokou.admin.dto.tenant.clientobject.TenantCO;
import org.laokou.common.i18n.dto.Datas;
import org.laokou.common.i18n.dto.Result;

/**
 * 租户管理.
 *
 * @author laokou
 */
public interface TenantsServiceI {

	/**
	 * 新增租户.
	 * @param cmd 新增租户参数
	 */
	void create(TenantCreateCmd cmd);

	/**
	 * 修改租户.
	 * @param cmd 修改租户参数
	 */
	void modify(TenantModifyCmd cmd);

	/**
	 * 根据ID删除租户.
	 * @param cmd 根据IDS删除租户
	 */
	void remove(TenantRemoveCmd cmd);

	/**
	 * 查询租户列表.
	 * @param qry 查询租户列表参数
	 * @return 租户列表
	 */
	Result<Datas<TenantCO>> findList(TenantListQry qry);

	/**
	 * 根据ID查看租户.
	 * @param qry 根据ID查看租户
	 * @return 租户
	 */
	Result<TenantCO> findById(TenantGetQry qry);

	/**
	 * 下载租户数据源压缩包.
	 * @param cmd 下载租户数据源压缩包参数
	 */
	void downloadDatasource(TenantDownloadDatasourceCmd cmd);

}
