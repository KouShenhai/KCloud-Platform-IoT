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

package org.laokou.auth.service;

import lombok.RequiredArgsConstructor;
import org.laokou.auth.api.TenantsServiceI;
import org.laokou.auth.command.tenant.query.TenantGetIDQryExe;
import org.laokou.auth.command.tenant.query.TenantListOptionQryExe;
import org.laokou.auth.dto.tenant.TenantGetIDQry;
import org.laokou.common.i18n.dto.Option;
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

	private final TenantListOptionQryExe tenantListOptionQryExe;

	private final TenantGetIDQryExe tenantGetIDQryExe;

	/**
	 * 查询租户下拉框选择项列表.
	 * @return 租户下拉框选择项列表
	 */
	@Override
	public Result<List<Option>> listOption() {
		return tenantListOptionQryExe.execute();
	}

	/**
	 * 根据域名查看租户ID.
	 * @param qry 根据域名查看租户ID
	 * @return 租户ID
	 */
	@Override
	public Result<Long> getIdByDomainName(TenantGetIDQry qry) {
		return tenantGetIDQryExe.execute(qry);
	}

}
