/*
 * Copyright (c) 2022 KCloud-Platform-Alibaba Authors. All Rights Reserved.
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

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.RequiredArgsConstructor;
import org.laokou.admin.dto.common.clientobject.OptionCO;
import org.laokou.admin.dto.tenant.TenantOptionListQry;
import org.laokou.admin.gatewayimpl.database.TenantMapper;
import org.laokou.admin.gatewayimpl.database.dataobject.TenantDO;
import org.laokou.common.core.utils.CollectionUtil;
import org.laokou.common.i18n.dto.Result;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * @author laokou
 */
@Component
@RequiredArgsConstructor
public class TenantOptionListQryExe {

	private final TenantMapper tenantMapper;

	public Result<List<OptionCO>> execute(TenantOptionListQry qry) {
		List<TenantDO> list = tenantMapper
			.selectList(Wrappers.query(TenantDO.class).select("id", "name").orderByDesc("create_date"));
		if (CollectionUtil.isEmpty(list)) {
			return Result.of(new ArrayList<>(0));
		}
		List<OptionCO> options = new ArrayList<>(list.size());
		for (TenantDO tenantDO : list) {
			OptionCO oc = new OptionCO();
			oc.setLabel(tenantDO.getName());
			oc.setValue(String.valueOf(tenantDO.getId()));
			options.add(oc);
		}
		return Result.of(options);
	}

}
