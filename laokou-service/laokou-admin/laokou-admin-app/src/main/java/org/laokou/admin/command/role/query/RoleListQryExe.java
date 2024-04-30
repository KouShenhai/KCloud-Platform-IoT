/*
 * Copyright (c) 2022-2024 KCloud-Platform-IOT Author or Authors. All Rights Reserved.
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

package org.laokou.admin.command.role.query;

import com.baomidou.dynamic.datasource.annotation.DS;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.laokou.admin.convertor.RoleConvertor;
import org.laokou.admin.domain.annotation.DataFilter;
import org.laokou.admin.dto.role.RoleListQry;
import org.laokou.admin.dto.role.clientobject.RoleCO;
import org.laokou.admin.gatewayimpl.database.RoleMapper;
import org.laokou.admin.gatewayimpl.database.dataobject.RoleDO;
import org.laokou.common.i18n.dto.Datas;
import org.laokou.common.i18n.dto.PageQuery;
import org.laokou.common.i18n.dto.Result;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

import static org.laokou.common.i18n.common.DatasourceConstant.BOOT_SYS_ROLE;
import static org.laokou.common.i18n.common.DatasourceConstant.TENANT;

/**
 * 查询角色列表执行器.
 *
 * @author laokou
 */
@Component
@RequiredArgsConstructor
public class RoleListQryExe {

	private final RoleMapper roleMapper;

	private final Executor executor;

	private final RoleConvertor roleConvertor;

	/**
	 * 执行查询角色列表.
	 * @param qry 查询角色列表参数
	 * @return 角色列表
	 */
	@SneakyThrows
	@DS(TENANT)
	@DataFilter(tableAlias = BOOT_SYS_ROLE)
	public Result<Datas<RoleCO>> execute(RoleListQry qry) {
		RoleDO roleDO = new RoleDO(qry.getName());
		PageQuery page = qry.page();
		CompletableFuture<List<RoleDO>> c1 = CompletableFuture
			.supplyAsync(() -> roleMapper.selectListByCondition(roleDO, page), executor);
		CompletableFuture<Long> c2 = CompletableFuture
			.supplyAsync(() -> roleMapper.selectCountByCondition(roleDO, page), executor);
		CompletableFuture.allOf(List.of(c1, c2).toArray(CompletableFuture[]::new)).join();
		return Result.ok(Datas.to(c1.get().stream().map(roleConvertor::convertClientObj).toList(), c2.get()));
	}

}
