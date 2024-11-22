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
import org.laokou.auth.command.query.TenantGetIDQryExe;
import org.laokou.auth.command.query.TenantPageQryExe;
import org.laokou.auth.dto.TenantIDGetQry;
import org.laokou.auth.dto.TenantPageQry;
import org.laokou.auth.dto.clientobject.TenantCO;
import org.laokou.common.i18n.dto.Page;
import org.laokou.common.i18n.dto.Result;
import org.springframework.stereotype.Service;

/**
 * 租户管理.
 *
 * @author laokou
 */
@Service
@RequiredArgsConstructor
public class TenantsServiceImpl implements TenantsServiceI {

	private final TenantPageQryExe tenantPageQryExe;

	private final TenantGetIDQryExe tenantGetIDQryExe;

	/**
	 * 分页查询租户列表.
	 */
	 @Override
	 public Result<Page<TenantCO>> page(TenantPageQry qry) {
	 return tenantPageQryExe.execute(qry);
	 }

	/**
	 * 根据域名查看租户ID.
	 * @param qry 根据域名查看租户ID
	 * @return 租户ID
	 */
	@Override
	public Result<Long> getIdByDomainName(TenantIDGetQry qry) {
		return tenantGetIDQryExe.execute(qry);
	}

}
