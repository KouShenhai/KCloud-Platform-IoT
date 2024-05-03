/*
 * Copyright (c) 2022-2024 KCloud-Platform-IOT Author or Authors. All Rights Reserved.
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

package org.laokou.admin.service;

import lombok.RequiredArgsConstructor;
import org.laokou.admin.api.MenusServiceI;
import org.laokou.admin.command.menu.MenuCreateCmdExe;
import org.laokou.admin.command.menu.MenuModifyCmdExe;
import org.laokou.admin.command.menu.MenuRemoveCmdExe;
import org.laokou.admin.command.menu.query.*;
import org.laokou.admin.dto.menu.*;
import org.laokou.admin.dto.menu.clientobject.MenuCO;
import org.laokou.admin.dto.menu.clientobject.RouterCO;
import org.laokou.common.i18n.dto.Result;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 菜单管理.
 *
 * @author laokou
 */
@Service
@RequiredArgsConstructor
public class MenusServiceImpl implements MenusServiceI {

	private final MenuGetQryExe menuGetQryExe;

	private final MenuListQryExe menuListQryExe;

	private final MenuModifyCmdExe menuModifyCmdExe;

	private final MenuCreateCmdExe menuCreateCmdExe;

	private final MenuRemoveCmdExe menuRemoveCmdExe;

	private final MenuIdsGetQryExe menuIDSGetQryExe;

	private final MenuTenantListQryExe menuTenantListQryExe;

	private final MenuListRouterQryExe menuListRouterQryExe;

	@Override
	public Result<List<RouterCO>> getRouters() {
		return menuListRouterQryExe.execute();
	}

	/**
	 * 查询菜单列表.
	 * @param qry 查询菜单列表参数
	 * @return 菜单列表
	 */
	@Override
	public Result<List<MenuCO>> findList(MenuListQry qry) {
		return menuListQryExe.execute(qry);
	}

	/**
	 * 根据ID查看菜单.
	 * @param qry 根据ID查看菜单参数
	 * @return 菜单
	 */
	@Override
	public Result<MenuCO> findById(MenuGetQry qry) {
		return menuGetQryExe.execute(qry);
	}

	/**
	 * 修改菜单.
	 * @param cmd 修改菜单参数
	 */
	@Override
	public void modify(MenuModifyCmd cmd) {
		menuModifyCmdExe.executeVoid(cmd);
	}

	/**
	 * 新增菜单.
	 * @param cmd 新增菜单参数
	 */
	@Override
	public void create(MenuCreateCmd cmd) {
		menuCreateCmdExe.executeVoid(cmd);
	}

	/**
	 * 根据ID删除菜单.
	 * @param cmd 根据ID删除菜单参数
	 */
	@Override
	public void remove(MenuRemoveCmd cmd) {
		menuRemoveCmdExe.executeVoid(cmd);
	}

	/**
	 * 根据角色ID查看菜单IDS.
	 * @param qry 根据角色ID查看菜单IDS参数
	 * @return 菜单IDS
	 */
	@Override
	public Result<List<Long>> findIds(MenuIdsGetQry qry) {
		return menuIDSGetQryExe.execute(qry);
	}

	/**
	 * 查看租户菜单树.
	 * @param qry 查看租户菜单树参数
	 * @return 租户菜单树
	 */
	@Override
	public Result<List<MenuCO>> findTenantMenuList(MenuTenantListQry qry) {
		return menuTenantListQryExe.execute(qry);
	}

}
