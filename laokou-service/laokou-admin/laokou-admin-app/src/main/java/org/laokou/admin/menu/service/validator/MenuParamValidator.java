/*
 * Copyright (c) 2022-2026 KCloud-Platform-IoT Author or Authors. All Rights Reserved.
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

package org.laokou.admin.menu.service.validator;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import org.laokou.admin.menu.gatewayimpl.database.MenuMapper;
import org.laokou.admin.menu.gatewayimpl.database.dataobject.MenuDO;
import org.laokou.admin.menu.model.MenuA;
import org.laokou.admin.menu.model.entity.MenuE;
import org.laokou.common.i18n.util.ObjectUtils;
import org.laokou.common.i18n.util.ParamValidator;
import org.laokou.common.i18n.util.StringExtUtils;

/**
 * @author laokou
 */
final class MenuParamValidator {

	private MenuParamValidator() {
	}

	static ParamValidator.Validate validatePermission(MenuA menuA, MenuMapper menuMapper) {
		String permission = menuA.getMenuE().getPermission();
		if (menuA.isMenu() && StringExtUtils.isEmpty(permission)) {
			return org.laokou.common.i18n.util.ParamValidator.invalidate("菜单权限标识不能为空");
		}
		if (menuA.isMenu() && menuA.isSave() && menuMapper
			.selectCount(Wrappers.lambdaQuery(MenuDO.class).eq(MenuDO::getPermission, permission)) > 0) {
			return org.laokou.common.i18n.util.ParamValidator.invalidate("菜单权限标识已存在");
		}
		if (menuA.isMenu() && menuA.isModify()
				&& menuMapper.selectCount(Wrappers.lambdaQuery(MenuDO.class)
					.eq(MenuDO::getPermission, permission)
					.ne(MenuDO::getId, menuA.getId())) > 0) {
			return ParamValidator.invalidate("菜单权限标识已存在");
		}
		return ParamValidator.validate();
	}

	static ParamValidator.Validate validateParentId(MenuA menuA) {
		Long pid = menuA.getMenuE().getPid();
		if (ObjectUtils.isNull(pid)) {
			return ParamValidator.invalidate("菜单父级ID不能为空");
		}
		return ParamValidator.validate();
	}

	static ParamValidator.Validate validateType(MenuA menuA) {
		if (ObjectUtils.isNull(menuA.getMenuE().getType())) {
			return ParamValidator.invalidate("菜单类型不能为空");
		}
		return ParamValidator.validate();
	}

	static ParamValidator.Validate validatePath(MenuA menuA, MenuMapper menuMapper) {
		String path = menuA.getMenuE().getPath();
		if (menuA.isMenu() && StringExtUtils.isEmpty(path)) {
			return ParamValidator.invalidate("菜单路径不能为空");
		}
		if (menuA.isMenu() && menuA.isSave()
				&& menuMapper.selectCount(Wrappers.lambdaQuery(MenuDO.class).eq(MenuDO::getPath, path)) > 0) {
			return ParamValidator.invalidate("菜单路径已存在");
		}
		if (menuA.isMenu() && menuA.isModify() && menuMapper.selectCount(
				Wrappers.lambdaQuery(MenuDO.class).eq(MenuDO::getPath, path).ne(MenuDO::getId, menuA.getId())) > 0) {
			return ParamValidator.invalidate("菜单路径已存在");
		}
		return ParamValidator.validate();
	}

	static ParamValidator.Validate validateSort(MenuA menuA) {
		Integer sort = menuA.getMenuE().getSort();
		if (ObjectUtils.isNull(sort)) {
			return ParamValidator.invalidate("菜单排序不能为空");
		}
		if (sort < 1 || sort > 99999) {
			return ParamValidator.invalidate("菜单排序范围1-99999");
		}
		return ParamValidator.validate();
	}

	static ParamValidator.Validate validateStatus(MenuA menuA) {
		Integer status = menuA.getMenuE().getStatus();
		if (menuA.isMenu() && ObjectUtils.isNull(status)) {
			return ParamValidator.invalidate("菜单状态不能为空");
		}
		if (menuA.statusNotExist()) {
			return ParamValidator.invalidate("菜单状态不存在");
		}
		return ParamValidator.validate();
	}

	static ParamValidator.Validate validateId(MenuA menuA) {
		Long id = menuA.getId();
		if (ObjectUtils.isNull(id)) {
			return ParamValidator.invalidate("菜单ID不能为空");
		}
		return ParamValidator.validate();
	}

	static ParamValidator.Validate validateName(MenuA menuA, MenuMapper menuMapper) {
		MenuE menuE = menuA.getMenuE();
		String name = menuE.getName();
		if (StringExtUtils.isEmpty(name)) {
			return ParamValidator.invalidate("菜单名称不能为空");
		}
		if (menuA.isMenu() && menuA.isSave()
				&& menuMapper.selectCount(Wrappers.lambdaQuery(MenuDO.class).eq(MenuDO::getName, name)) > 0) {
			return ParamValidator.invalidate("菜单名称已存在");
		}
		if (menuA.isMenu() && menuA.isModify() && menuMapper.selectCount(
				Wrappers.lambdaQuery(MenuDO.class).eq(MenuDO::getName, name).ne(MenuDO::getId, menuA.getId())) > 0) {
			return ParamValidator.invalidate("菜单名称已存在");
		}
		return ParamValidator.validate();
	}

}
