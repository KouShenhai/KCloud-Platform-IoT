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
package org.laokou.admin.command.menu.query;

import lombok.RequiredArgsConstructor;
import org.laokou.admin.domain.gateway.MenuGateway;
import org.laokou.admin.domain.menu.Menu;
import org.laokou.admin.domain.user.User;
import org.laokou.admin.dto.menu.MenuTreeListQry;
import org.laokou.admin.dto.menu.clientobject.MenuCO;
import org.laokou.common.core.utils.ConvertUtil;
import org.laokou.common.core.utils.TreeUtil;
import org.laokou.common.i18n.dto.Result;
import org.laokou.common.redis.utils.RedisKeyUtil;
import org.laokou.common.redis.utils.RedisUtil;
import org.laokou.common.security.utils.UserUtil;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author laokou
 */
@Component
@RequiredArgsConstructor
public class MenuTreeListQryExe {

	private final MenuGateway menuGateway;

	private final RedisUtil redisUtil;

	public Result<MenuCO> execute(MenuTreeListQry qry) {
		String menuTreeKey = RedisKeyUtil.getMenuTreeKey(UserUtil.getUserId());
		Object obj = redisUtil.get(menuTreeKey);
		if (obj != null) {
			return Result.of((MenuCO) obj);
		}
		User user = ConvertUtil.sourceToTarget(UserUtil.user(), User.class);
		List<Menu> menuList = menuGateway.list(user, 0);
		List<MenuCO> menus = ConvertUtil.sourceToTarget(menuList, MenuCO.class);
		MenuCO menuCO = TreeUtil.buildTreeNode(menus, MenuCO.class);
		redisUtil.set(menuTreeKey, menuCO, RedisUtil.HOUR_ONE_EXPIRE);
		return Result.of(menuCO);
	}

}
