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

package org.laokou.admin.command.tenant.query;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.laokou.admin.convertor.TenantConvertor;
import org.laokou.admin.dto.tenant.TenantListQry;
import org.laokou.admin.dto.tenant.clientobject.TenantCO;
import org.laokou.admin.gatewayimpl.database.TenantMapper;
import org.laokou.admin.gatewayimpl.database.dataobject.TenantDO;
import org.laokou.common.i18n.dto.Datas;
import org.laokou.common.i18n.dto.PageQuery;
import org.laokou.common.i18n.dto.Result;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

/**
 * 查询租户列表执行器.
 *
 * @author laokou
 */
@Component
@RequiredArgsConstructor
public class TenantListQryExe {

	private final TenantMapper tenantMapper;

	private final Executor executor;

	private final TenantConvertor tenantConvertor;

	/**
	 * 执行查询租户列表.
	 * @param qry 查询租户列表参数
	 * @return 租户列表
	 */
	@SneakyThrows
	// @DataFilter(tableAlias = BOOT_SYS_TENANT)
	public Result<Datas<TenantCO>> execute(TenantListQry qry) {
		TenantDO tenantDO = new TenantDO(qry.getName());
		PageQuery page = qry;
		CompletableFuture<List<TenantDO>> c1 = CompletableFuture
			.supplyAsync(() -> tenantMapper.selectListByCondition(tenantDO, page), executor);
		CompletableFuture<Long> c2 = CompletableFuture
			.supplyAsync(() -> tenantMapper.selectCountByCondition(tenantDO, page), executor);
		CompletableFuture.allOf(List.of(c1, c2).toArray(CompletableFuture[]::new)).join();
		return Result.ok(Datas.create(c1.get().stream().map(tenantConvertor::convertClientObj).toList(), c2.get()));
	}

}
