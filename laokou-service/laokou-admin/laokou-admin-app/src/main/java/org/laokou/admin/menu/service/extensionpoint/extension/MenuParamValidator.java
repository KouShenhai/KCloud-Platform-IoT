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

package org.laokou.admin.menu.service.extensionpoint.extension;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import org.laokou.admin.menu.gatewayimpl.database.MenuMapper;
import org.laokou.admin.menu.gatewayimpl.database.dataobject.MenuDO;
import org.laokou.admin.menu.model.MenuE;
import org.laokou.admin.menu.model.MenuTypeEnum;
import org.laokou.common.i18n.util.ObjectUtils;
import org.laokou.common.i18n.util.ParamValidator;
import org.laokou.common.i18n.util.StringUtils;

import static org.laokou.common.i18n.util.ParamValidator.invalidate;
import static org.laokou.common.i18n.util.ParamValidator.validate;

/**
 * @author laokou
 */
public final class MenuParamValidator {

	private MenuParamValidator() {
	}

	public static ParamValidator.Validate validatePermission(MenuE menuE, MenuMapper menuMapper, boolean isSave) {
		Integer type = menuE.getType();
		String permission = menuE.getPermission();
		if (MenuTypeEnum.BUTTON.getCode() == type) {
			if (StringUtils.isEmpty(permission)) {
				return invalidate("菜单权限标识不能为空");
			}
			if (isSave && menuMapper
				.selectCount(Wrappers.lambdaQuery(MenuDO.class).eq(MenuDO::getPermission, permission)) > 0) {
				return invalidate("菜单权限标识已存在");
			}
			if (!isSave && menuMapper.selectCount(Wrappers.lambdaQuery(MenuDO.class)
				.eq(MenuDO::getPermission, permission)
				.ne(MenuDO::getId, menuE.getId())) > 0) {
				return invalidate("菜单权限标识已存在");
			}
		}
		return validate();
	}

	public static ParamValidator.Validate validateParentId(MenuE menuE) {
		Long pid = menuE.getPid();
		if (ObjectUtils.isNull(pid)) {
			return invalidate("菜单父级ID不能为空");
		}
		return validate();
	}

	public static ParamValidator.Validate validateType(MenuE menuE) {
		Integer type = menuE.getType();
		if (ObjectUtils.isNull(type)) {
			return invalidate("菜单类型不能为空");
		}
		return validate();
	}

	public static ParamValidator.Validate validatePath(MenuE menuE, MenuMapper menuMapper, boolean isSave) {
		Integer type = menuE.getType();
		String path = menuE.getPath();
		if (MenuTypeEnum.MENU.getCode() == type) {
			if (StringUtils.isEmpty(path)) {
				return invalidate("菜单路径不能为空");
			}
			if (isSave && menuMapper.selectCount(Wrappers.lambdaQuery(MenuDO.class).eq(MenuDO::getPath, path)) > 0) {
				return invalidate("菜单路径已存在");
			}
			if (!isSave && menuMapper.selectCount(Wrappers.lambdaQuery(MenuDO.class)
				.eq(MenuDO::getPath, path)
				.ne(MenuDO::getId, menuE.getId())) > 0) {
				return invalidate("菜单路径已存在");
			}
		}
		return validate();
	}

	public static ParamValidator.Validate validateSort(MenuE menuE) {
		Integer sort = menuE.getSort();
		if (ObjectUtils.isNull(sort)) {
			return invalidate("菜单排序不能为空");
		}
		if (sort < 1 || sort > 99999) {
			return invalidate("菜单排序范围1-99999");
		}
		return validate();
	}

	public static ParamValidator.Validate validateStatus(MenuE menuE) {
		Integer type = menuE.getType();
		Integer status = menuE.getStatus();
		if (MenuTypeEnum.MENU.getCode() == type && ObjectUtils.isNull(status)) {
			return invalidate("菜单状态不能为空");
		}
		return validate();
	}

	public static ParamValidator.Validate validateId(MenuE menuE) {
		Long id = menuE.getId();
		if (ObjectUtils.isNull(id)) {
			return invalidate("菜单ID不能为空");
		}
		return validate();
	}

	public static ParamValidator.Validate validateName(MenuE menuE, MenuMapper menuMapper, boolean isSave) {
		String name = menuE.getName();
		Integer type = menuE.getType();
		if (StringUtils.isEmpty(name)) {
			return invalidate("菜单名称不能为空");
		}
		if (MenuTypeEnum.MENU.getCode() == type) {
			if (isSave && menuMapper.selectCount(Wrappers.lambdaQuery(MenuDO.class).eq(MenuDO::getName, name)) > 0) {
				return invalidate("菜单名称已存在");
			}
			if (!isSave && menuMapper.selectCount(Wrappers.lambdaQuery(MenuDO.class)
				.eq(MenuDO::getName, name)
				.ne(MenuDO::getId, menuE.getId())) > 0) {
				return invalidate("菜单名称已存在");
			}
		}
		return validate();
	}

}
