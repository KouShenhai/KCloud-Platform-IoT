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

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.RequiredArgsConstructor;
import org.laokou.admin.menu.dto.MenuListQry;
import org.laokou.admin.menu.dto.clientobject.MenuCO;
import org.laokou.admin.gatewayimpl.database.MenuMapper;
import org.laokou.admin.gatewayimpl.database.dataobject.MenuDO;
import org.laokou.common.i18n.dto.Result;
import org.laokou.common.i18n.utils.StringUtil;
import org.springframework.stereotype.Component;

import java.util.List;

import static org.laokou.admin.config.DsTenantProcessor.TENANT;

/**
 * 查询菜单列表执行器.
 *
 * @author laokou
 */
@Component
@RequiredArgsConstructor
public class MenuListQryExe {

	private final MenuMapper menuMapper;

	/**
	 * 执行查询菜单列表.
	 * @param qry 查询菜单列表参数
	 * @return 菜单列表
	 */
	@DS(TENANT)
	public Result<List<MenuCO>> execute(MenuListQry qry) {
		return null;
		// return
		// Result.ok(getMenuList(qry).stream().map(menuConvertor::convertClientObj).toList());
	}

	private List<MenuDO> getMenuList(MenuListQry qry) {
		LambdaQueryWrapper<MenuDO> wrapper = Wrappers.lambdaQuery(MenuDO.class)
			.like(StringUtil.isNotEmpty(qry.getName()), MenuDO::getName, qry.getName())
			.orderByDesc(MenuDO::getSort);
		return menuMapper.selectList(wrapper);
	}

}
