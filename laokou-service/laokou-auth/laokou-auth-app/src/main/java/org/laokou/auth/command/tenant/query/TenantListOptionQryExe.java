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

package org.laokou.auth.command.tenant.query;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.RequiredArgsConstructor;
import org.laokou.auth.gatewayimpl.database.TenantMapper;
import org.laokou.auth.gatewayimpl.database.dataobject.TenantDO;
import org.laokou.common.i18n.dto.Option;
import org.laokou.common.i18n.dto.Result;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 查询租户下拉框选择项列表执行器.
 *
 * @author laokou
 */
@Component
@RequiredArgsConstructor
public class TenantListOptionQryExe {

	private final TenantMapper tenantMapper;

	/**
	 * 执行查询租户下拉框选择项列表.
	 * @return 租户下拉框选择项列表
	 */
	public Result<List<Option>> execute() {
		List<TenantDO> list = tenantMapper.selectList(Wrappers.lambdaQuery(TenantDO.class)
			.select(TenantDO::getId, TenantDO::getName)
			.orderByDesc(TenantDO::getId));
		return Result
			.ok(list.stream().map(item -> new Option(item.getName(), String.valueOf(item.getId()))).toList());
	}

}
