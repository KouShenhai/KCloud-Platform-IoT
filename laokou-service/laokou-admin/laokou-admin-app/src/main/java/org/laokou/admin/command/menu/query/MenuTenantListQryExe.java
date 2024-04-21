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
import lombok.RequiredArgsConstructor;
import org.laokou.admin.dto.menu.MenuTenantListQry;
import org.laokou.admin.dto.menu.clientobject.MenuCO;
import org.laokou.admin.gatewayimpl.database.MenuMapper;
import org.laokou.admin.gatewayimpl.database.dataobject.MenuDO;
import org.laokou.common.core.utils.TreeUtil;
import org.laokou.common.i18n.common.FindTypeEnum;
import org.laokou.common.i18n.dto.Result;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

import static org.laokou.common.i18n.common.DatasourceConstant.TENANT;

/**
 * 查看租户树菜单执行器.
 *
 * @author laokou
 */
@Component
@RequiredArgsConstructor
public class MenuTenantListQryExe {

	private final MenuMapper menuMapper;

	/**
	 * 执行查看租户树菜单.
	 * @param qry 查看租户树菜单参数
	 * @return 租户树菜单
	 */
	@DS(TENANT)
	public Result<List<MenuCO>> execute(MenuTenantListQry qry) {
		return switch (FindTypeEnum.valueOf(qry.getType())) {
			case LIST, USER_TREE_LIST -> null;
			case TREE_LIST ->
				Result.ok(buildTreeNode(menuMapper.selectTenantMenuList().stream().map(this::convert).toList())
					.getChildren());
		};
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

	private MenuCO buildTreeNode(List<MenuCO> list) {
		return TreeUtil.buildTreeNode(list, MenuCO.class);
	}

}
