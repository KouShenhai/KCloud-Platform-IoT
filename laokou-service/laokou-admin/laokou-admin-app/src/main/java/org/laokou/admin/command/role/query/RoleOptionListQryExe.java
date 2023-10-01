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

package org.laokou.admin.command.role.query;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.RequiredArgsConstructor;
import org.laokou.admin.dto.common.clientobject.OptionCO;
import org.laokou.admin.dto.role.RoleOptionListQry;
import org.laokou.admin.gatewayimpl.database.RoleMapper;
import org.laokou.admin.gatewayimpl.database.dataobject.RoleDO;
import org.laokou.common.core.utils.CollectionUtil;
import org.laokou.common.i18n.dto.Result;
import org.laokou.common.security.utils.UserUtil;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * @author laokou
 */
@Component
@RequiredArgsConstructor
public class RoleOptionListQryExe {

	private final RoleMapper roleMapper;

	public Result<List<OptionCO>> execute(RoleOptionListQry qry) {
		List<RoleDO> list = roleMapper.selectList(Wrappers.query(RoleDO.class)
			.eq("tenant_id", UserUtil.getTenantId())
			.select("id", "name")
			.orderByDesc("sort"));
		if (CollectionUtil.isEmpty(list)) {
			return Result.of(new ArrayList<>(0));
		}
		List<OptionCO> options = new ArrayList<>(list.size());
		for (RoleDO roleDO : list) {
			OptionCO oc = new OptionCO();
			oc.setLabel(roleDO.getName());
			oc.setValue(String.valueOf(roleDO.getId()));
			options.add(oc);
		}
		return Result.of(options);
	}

}
