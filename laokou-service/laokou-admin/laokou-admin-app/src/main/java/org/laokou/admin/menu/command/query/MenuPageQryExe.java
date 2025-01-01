/*
 * Copyright (c) 2022-2024 KCloud-Platform-IoT Author or Authors. All Rights Reserved.
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

import lombok.RequiredArgsConstructor;
import org.laokou.admin.menu.convertor.MenuConvertor;
import org.laokou.admin.menu.dto.MenuPageQry;
import org.laokou.admin.menu.dto.clientobject.MenuCO;
import org.laokou.admin.menu.gatewayimpl.database.MenuMapper;
import org.laokou.admin.menu.gatewayimpl.database.dataobject.MenuDO;
import org.laokou.common.i18n.dto.Page;
import org.laokou.common.i18n.dto.Result;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 分页查询菜单请求执行器.
 *
 * @author laokou
 */
@Component
@RequiredArgsConstructor
public class MenuPageQryExe {

	private final MenuMapper menuMapper;

	public Result<Page<MenuCO>> execute(MenuPageQry qry) {
		List<MenuDO> list = menuMapper.selectObjectPage(qry);
		long total = menuMapper.selectObjectCount(qry);
		return Result.ok(Page.create(list.stream().map(MenuConvertor::toClientObject).toList(), total));
	}

}
