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

package org.laokou.admin.command.role.query;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.RequiredArgsConstructor;
import org.laokou.admin.dto.common.clientobject.OptionCO;
import org.laokou.admin.gatewayimpl.database.RoleRepository;
import org.laokou.admin.gatewayimpl.database.dataobject.RoleDO;
import org.laokou.common.i18n.dto.Result;
import org.springframework.stereotype.Component;

import java.util.List;

import static org.laokou.common.i18n.common.DatasourceConstant.TENANT;

/**
 * 查询角色下拉框选择项列表执行器.
 *
 * @author laokou
 */
@Component
@RequiredArgsConstructor
public class RoleOptionListQryExe {

	private final RoleRepository roleMapper;

	/**
	 * 执行查询角色下拉框选择项列表.
	 * @return 角色下拉框选择项列表
	 */
	@DS(TENANT)
	public Result<List<OptionCO>> execute() {
		List<RoleDO> list = roleMapper.selectList(
				Wrappers.lambdaQuery(RoleDO.class).select(RoleDO::getId, RoleDO::getName).orderByDesc(RoleDO::getSort));
		return Result.of(list.stream().map(this::convert).toList());
	}

	private OptionCO convert(RoleDO roleDO) {
		return OptionCO.builder().label(roleDO.getName()).value(String.valueOf(roleDO.getId())).build();
	}

}
