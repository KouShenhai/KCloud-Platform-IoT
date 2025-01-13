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

package org.laokou.admin.tenant.command.query;

import lombok.RequiredArgsConstructor;
import org.laokou.admin.tenant.convertor.TenantConvertor;
import org.laokou.admin.tenant.dto.TenantPageQry;
import org.laokou.admin.tenant.dto.clientobject.TenantCO;
import org.laokou.common.i18n.dto.Page;
import org.laokou.common.i18n.dto.Result;
import org.laokou.common.tenant.mapper.TenantDO;
import org.laokou.common.tenant.mapper.TenantMapper;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 分页查询租户请求执行器.
 *
 * @author laokou
 */
@Component
@RequiredArgsConstructor
public class TenantPageQryExe {

	private final TenantMapper tenantMapper;

	public Result<Page<TenantCO>> execute(TenantPageQry qry) {
		List<TenantDO> list = tenantMapper.selectObjectPage(qry);
		long total = tenantMapper.selectObjectCount(qry);
		return Result.ok(Page.create(list.stream().map(TenantConvertor::toClientObject).toList(), total));
	}

}
