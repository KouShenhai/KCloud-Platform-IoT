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
import org.laokou.admin.api.TenantsServiceI;
import org.laokou.admin.command.tenant.TenantDownloadDatasourceCmdExe;
import org.laokou.admin.command.tenant.query.TenantGetIDQryExe;
import org.laokou.common.i18n.dto.Option;
import org.laokou.admin.dto.tenant.*;
import org.laokou.admin.dto.tenant.clientobject.TenantCO;
import org.laokou.admin.command.tenant.TenantRemoveCmdExe;
import org.laokou.admin.command.tenant.TenantCreateCmdExe;
import org.laokou.admin.command.tenant.TenantModifyCmdExe;
import org.laokou.admin.command.tenant.query.TenantGetQryExe;
import org.laokou.admin.command.tenant.query.TenantListQryExe;
import org.laokou.admin.command.tenant.query.TenantOptionListQryExe;
import org.laokou.common.i18n.dto.Datas;
import org.laokou.common.i18n.dto.Result;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 租户管理.
 *
 * @author laokou
 */
@Service
@RequiredArgsConstructor
public class TenantsServiceImpl implements TenantsServiceI {

	private final TenantCreateCmdExe tenantCreateCmdExe;

	private final TenantOptionListQryExe tenantOptionListQryExe;

	private final TenantModifyCmdExe tenantModifyCmdExe;

	private final TenantRemoveCmdExe tenantRemoveCmdExe;

	private final TenantListQryExe tenantListQryExe;

	private final TenantGetQryExe tenantGetQryExe;

	private final TenantGetIDQryExe tenantGetIDQryExe;

	private final TenantDownloadDatasourceCmdExe tenantDownloadDatasourceCmdExe;

	/**
	 * 查询租户下拉框选择项列表.
	 * @return 租户下拉框选择项列表
	 */
	@Override
	public Result<List<Option>> findOptionList() {
		return tenantOptionListQryExe.execute();
	}

	/**
	 * 新增租户.
	 * @param cmd 新增租户参数
	 */
	@Override
	public void create(TenantCreateCmd cmd) {
		tenantCreateCmdExe.executeVoid(cmd);
	}

	/**
	 * 修改租户.
	 * @param cmd 修改租户参数
	 */
	@Override
	public void modify(TenantModifyCmd cmd) {
		tenantModifyCmdExe.executeVoid(cmd);
	}

	/**
	 * 根据ID删除租户.
	 * @param cmd 根据ID删除租户
	 */
	@Override
	public void remove(TenantRemoveCmd cmd) {
		tenantRemoveCmdExe.executeVoid(cmd);
	}

	/**
	 * 查询租户列表.
	 * @param qry 查询租户列表参数
	 * @return 租户列表
	 */
	@Override
	public Result<Datas<TenantCO>> findList(TenantListQry qry) {
		return tenantListQryExe.execute(qry);
	}

	/**
	 * 根据ID查看租户.
	 * @param qry 根据ID查看租户
	 * @return 租户
	 */
	@Override
	public Result<TenantCO> findById(TenantGetQry qry) {
		return tenantGetQryExe.execute(qry);
	}

	/**
	 * 根据域名查看租户ID.
	 * @param qry 根据域名查看租户ID
	 * @return 租户ID
	 */
	@Override
	public Result<Long> findIdByDomainName(TenantGetIDQry qry) {
		return tenantGetIDQryExe.execute(qry);
	}

	/**
	 * 下载租户数据源压缩包.
	 * @param cmd 下载租户数据源压缩包参数
	 */
	@Override
	public void downloadDatasource(TenantDownloadDatasourceCmd cmd) {
		tenantDownloadDatasourceCmdExe.executeVoid(cmd);
	}

}
