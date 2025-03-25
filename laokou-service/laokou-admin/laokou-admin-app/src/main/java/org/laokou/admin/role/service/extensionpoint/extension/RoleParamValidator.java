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

package org.laokou.admin.role.service.extensionpoint.extension;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import org.laokou.admin.role.gatewayimpl.database.RoleMapper;
import org.laokou.admin.role.gatewayimpl.database.dataobject.RoleDO;
import org.laokou.admin.role.model.RoleE;
import org.laokou.common.core.util.CollectionUtils;
import org.laokou.common.i18n.util.ObjectUtils;
import org.laokou.common.i18n.util.ParamValidator;
import org.laokou.common.i18n.util.StringUtils;
import org.laokou.common.mybatisplus.annotation.DataScope;

import java.util.List;

import static org.laokou.common.i18n.util.ParamValidator.invalidate;
import static org.laokou.common.i18n.util.ParamValidator.validate;

/**
 * @author laokou
 */
public final class RoleParamValidator {

	private RoleParamValidator() {
	}

	public static ParamValidator.Validate validateSort(RoleE roleE) {
		Integer sort = roleE.getSort();
		if (ObjectUtils.isNull(sort)) {
			return invalidate("排序不能为空");
		}
		if (sort < 1 || sort > 99999) {
			return invalidate("排序范围1-99999");
		}
		return validate();
	}

	public static ParamValidator.Validate validateId(RoleE roleE) {
		Long id = roleE.getId();
		if (ObjectUtils.isNull(id)) {
			return invalidate("ID不能为空");
		}
		return validate();
	}

	public static ParamValidator.Validate validateDataScope(RoleE roleE) {
		String dataScope = roleE.getDataScope();
		if (StringUtils.isEmpty(dataScope)) {
			return invalidate("数据范围不能为空");
		}
		return validate();
	}

	public static ParamValidator.Validate validateMenuIds(RoleE roleE) {
		List<String> menuIds = roleE.getMenuIds();
		if (CollectionUtils.isEmpty(menuIds)) {
			return invalidate("菜单IDS不能为空");
		}
		return validate();
	}

	public static ParamValidator.Validate validateDeptIds(RoleE roleE) {
		List<String> deptIds = roleE.getDeptIds();
		if (ObjectUtils.equals(roleE.getDataScope(), DataScope.CUSTOM.getCode()) && CollectionUtils.isEmpty(deptIds)) {
			return invalidate("部门IDS不能为空");
		}
		return validate();
	}

	public static ParamValidator.Validate validateName(RoleE roleE, RoleMapper roleMapper, boolean isSave) {
		Long id = roleE.getId();
		String name = roleE.getName();
		if (StringUtils.isEmpty(name)) {
			return invalidate("名称不能为空");
		}
		if (isSave && roleMapper.selectCount(Wrappers.lambdaQuery(RoleDO.class).eq(RoleDO::getName, name)) > 0) {
			return invalidate("名称已存在");
		}
		if (!isSave && roleMapper
			.selectCount(Wrappers.lambdaQuery(RoleDO.class).eq(RoleDO::getName, name).ne(RoleDO::getId, id)) > 0) {
			return invalidate("名称已存在");
		}
		return validate();
	}

}
