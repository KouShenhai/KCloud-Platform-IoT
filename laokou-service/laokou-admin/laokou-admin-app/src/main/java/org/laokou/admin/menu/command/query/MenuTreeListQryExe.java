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

package org.laokou.admin.menu.command.query;

import org.laokou.admin.menu.dto.MenuTreeListQry;
import org.laokou.admin.menu.dto.clientobject.MenuTreeCO;
import org.laokou.admin.menu.model.MenuTreeBuilder;
import org.laokou.admin.menu.model.MenuTypeTreeEnum;
import org.laokou.common.core.context.UserContextHolder;
import org.laokou.common.i18n.dto.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import java.util.List;

/**
 * 查询菜单请求执行器.
 *
 * @author laokou
 */
@Component
public class MenuTreeListQryExe {

	@Autowired
	@Qualifier("userMenuTreeBuilder")
	private MenuTreeBuilder userMenuTreeBuilder;

	@Autowired
	@Qualifier("systemMenuTreeBuilder")
	private MenuTreeBuilder systemMenuTreeBuilder;

	public Result<List<MenuTreeCO>> execute(MenuTreeListQry qry) {
		MenuTypeTreeEnum menuTypeTreeEnum = MenuTypeTreeEnum.getByCode(qry.getCode());
		Assert.notNull(menuTypeTreeEnum, "菜单类型不存在");
		return Result.ok(((MenuTreeCO) menuTypeTreeEnum.buildMenuTree(qry, UserContextHolder.get().getId(),
				userMenuTreeBuilder, systemMenuTreeBuilder))
			.getChildren());
	}

}
