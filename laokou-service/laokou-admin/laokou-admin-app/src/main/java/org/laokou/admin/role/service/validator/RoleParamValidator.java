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

package org.laokou.admin.role.service.validator;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import org.laokou.admin.role.gatewayimpl.database.RoleMapper;
import org.laokou.admin.role.gatewayimpl.database.dataobject.RoleDO;
import org.laokou.admin.role.model.RoleE;
import org.laokou.common.core.util.CollectionExtUtils;
import org.laokou.common.i18n.util.ObjectUtils;
import org.laokou.common.i18n.util.ParamValidator;
import org.laokou.common.i18n.util.StringExtUtils;
import org.laokou.common.mybatisplus.annotation.DataScope;

import java.util.List;

/**
 * @author laokou
 */
final class RoleParamValidator {

	private RoleParamValidator() {
	}

	public static ParamValidator.Validate validateSort(RoleE roleE) {
		Integer sort = roleE.getSort();
		if (ObjectUtils.isNull(sort)) {
			return ParamValidator.invalidate("角色排序不能为空");
		}
		if (sort < 1 || sort > 99999) {
			return ParamValidator.invalidate("角色排序范围1-99999");
		}
		return ParamValidator.validate();
	}

	public static ParamValidator.Validate validateId(RoleE roleE) {
		Long id = roleE.getId();
		if (ObjectUtils.isNull(id)) {
			return ParamValidator.invalidate("角色ID不能为空");
		}
		return ParamValidator.validate();
	}

	public static ParamValidator.Validate validateDataScope(RoleE roleE) {
		String dataScope = roleE.getDataScope();
		if (StringExtUtils.isEmpty(dataScope)) {
			return ParamValidator.invalidate("角色数据范围不能为空");
		}
		return ParamValidator.validate();
	}

	public static ParamValidator.Validate validateMenuIds(RoleE roleE) {
		List<String> menuIds = roleE.getMenuIds();
		if (CollectionExtUtils.isEmpty(menuIds)) {
			return ParamValidator.invalidate("角色菜单IDS不能为空");
		}
		return ParamValidator.validate();
	}

	public static ParamValidator.Validate validateDeptIds(RoleE roleE) {
		List<String> deptIds = roleE.getDeptIds();
		if (ObjectUtils.equals(roleE.getDataScope(), DataScope.CUSTOM.getCode())
				&& CollectionExtUtils.isEmpty(deptIds)) {
			return ParamValidator.invalidate("角色部门IDS不能为空");
		}
		return ParamValidator.validate();
	}

	public static ParamValidator.Validate validateName(RoleE roleE, RoleMapper roleMapper, boolean isSave) {
		Long id = roleE.getId();
		String name = roleE.getName();
		if (StringExtUtils.isEmpty(name)) {
			return ParamValidator.invalidate("角色名称不能为空");
		}
		if (isSave && roleMapper.selectCount(Wrappers.lambdaQuery(RoleDO.class).eq(RoleDO::getName, name)) > 0) {
			return ParamValidator.invalidate("角色名称已存在");
		}
		if (!isSave && roleMapper
			.selectCount(Wrappers.lambdaQuery(RoleDO.class).eq(RoleDO::getName, name).ne(RoleDO::getId, id)) > 0) {
			return ParamValidator.invalidate("角色名称已存在");
		}
		return ParamValidator.validate();
	}

}
