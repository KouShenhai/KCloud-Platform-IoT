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

package org.laokou.auth.command.query;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.RequiredArgsConstructor;
import org.laokou.auth.convertor.TenantConvertor;
import org.laokou.auth.dto.TenantPageQry;
import org.laokou.auth.dto.clientobject.TenantCO;
import org.laokou.auth.gatewayimpl.database.TenantMapper;
import org.laokou.auth.gatewayimpl.database.dataobject.TenantDO;
import org.laokou.common.i18n.dto.Page;
import org.laokou.common.i18n.dto.Result;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author laokou
 */
@Component
@RequiredArgsConstructor
public class TenantPageQryExe {

	private final TenantMapper tenantMapper;

	 public Result<Page<TenantCO>> execute(TenantPageQry qry) {
		 com.baomidou.mybatisplus.extension.plugins.pagination.Page<TenantDO> page = tenantMapper.selectPage(new com.baomidou.mybatisplus.extension.plugins.pagination.Page<>(qry.getPageNum(), qry.getPageSize()), Wrappers.lambdaQuery(TenantDO.class)
			 .select(TenantDO::getId, TenantDO::getName)
			 .orderByDesc(TenantDO::getId));
		 List<TenantCO> list = page.getRecords().stream().map(TenantConvertor::toClientObject).toList();
		 return Result.ok(Page.create(list, page.getTotal()));
	 }

}
