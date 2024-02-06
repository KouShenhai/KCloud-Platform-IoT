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

package org.laokou.admin.command.menu.query;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.RequiredArgsConstructor;
import org.laokou.admin.dto.menu.clientobject.MenuCO;
import org.laokou.admin.gatewayimpl.database.MenuMapper;
import org.laokou.admin.gatewayimpl.database.dataobject.MenuDO;
import org.laokou.common.core.utils.TreeUtil;
import org.laokou.common.i18n.dto.Result;
import org.laokou.common.i18n.utils.ObjectUtil;
import org.laokou.common.redis.utils.RedisKeyUtil;
import org.laokou.common.redis.utils.RedisUtil;
import org.laokou.common.security.utils.UserDetail;
import org.laokou.common.security.utils.UserUtil;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

import static org.laokou.admin.domain.menu.MenuTypeEnums.MENU;
import static org.laokou.admin.domain.menu.VisibleEnums.YES;
import static org.laokou.common.i18n.common.DatasourceConstants.TENANT;

/**
 * 查询树菜单列表执行器.
 *
 * @author laokou
 */
@Component
@RequiredArgsConstructor
public class MenuTreeListQryExe {

	private final MenuMapper menuMapper;

	private final RedisUtil redisUtil;

	/**
	 * 执行查询树菜单列表.
	 * @return 树菜单列表
	 */
	@DS(TENANT)
	public Result<List<MenuCO>> execute() {
		String menuTreeKey = RedisKeyUtil.getMenuTreeKey(UserUtil.getUserId());
		Object obj = redisUtil.get(menuTreeKey);
		if (ObjectUtil.isNotNull(obj)) {
			return Result.of(((MenuCO) obj).getChildren());
		}
		MenuCO co = buildTreeNode(convert(getMenuList()));
		redisUtil.set(menuTreeKey, co, RedisUtil.HOUR_ONE_EXPIRE);
		return Result.of(co.getChildren());
	}

	private List<MenuCO> convert(List<MenuDO> list) {
		return list.stream().map(this::convert).toList();
	}

	private MenuCO buildTreeNode(List<MenuCO> list) {
		return TreeUtil.buildTreeNode(list, MenuCO.class);
	}

	private MenuCO convert(MenuDO menuDO) {
		return MenuCO.builder()
				.url(menuDO.getUrl())
				.icon(menuDO.getIcon())
				.name(menuDO.getName())
				.pid(menuDO.getPid())
				.sort(menuDO.getSort())
				.type(menuDO.getType())
				.id(menuDO.getId())
				.permission(menuDO.getPermission())
				.visible(menuDO.getVisible())
				.children(new ArrayList<>(16))
				.build();
	}

	private List<MenuDO> getMenuList() {
		UserDetail user = UserUtil.user();
		if (user.isSuperAdministrator()) {
			LambdaQueryWrapper<MenuDO> wrapper = Wrappers.lambdaQuery(MenuDO.class)
					.eq(MenuDO::getType, MENU.ordinal())
					.eq(MenuDO::getVisible, YES.ordinal()).orderByDesc(MenuDO::getSort);
			return menuMapper.selectList(wrapper);
		}
		return menuMapper.selectListByUserId(user.getId());
	}

}
