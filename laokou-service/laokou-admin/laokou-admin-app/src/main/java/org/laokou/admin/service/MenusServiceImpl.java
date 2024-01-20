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

package org.laokou.admin.service;

import lombok.RequiredArgsConstructor;
import org.laokou.admin.api.MenusServiceI;
import org.laokou.admin.dto.menu.*;
import org.laokou.admin.dto.menu.clientobject.MenuCO;
import org.laokou.admin.command.menu.MenuDeleteCmdExe;
import org.laokou.admin.command.menu.MenuInsertCmdExe;
import org.laokou.admin.command.menu.MenuUpdateCmdExe;
import org.laokou.admin.command.menu.query.*;
import org.laokou.common.i18n.dto.Result;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 菜单管理.
 * @author laokou
 */
@Service
@RequiredArgsConstructor
public class MenusServiceImpl implements MenusServiceI {

	private final MenuTreeListQryExe menuTreeListQryExe;

	private final MenuGetQryExe menuGetQryExe;

	private final MenuListQryExe menuListQryExe;

	private final MenuUpdateCmdExe menuUpdateCmdExe;

	private final MenuInsertCmdExe menuInsertCmdExe;

	private final MenuDeleteCmdExe menuDeleteCmdExe;

	private final MenuTreeGetQryExe menuTreeGetQryExe;

	private final MenuIDSGetQryExe menuIDSGetQryExe;

	private final MenuTenantTreeGetQryExe menuTenantTreeGetQryExe;

	/**
	 * 查看树形菜单列表（用户）
	 * @param qry 查看树形菜单列表（用户）参数
	 * @return 树形菜单列表（用户）
	 */
	@Override
	public Result<MenuCO> treeList(MenuTreeListQry qry) {
		return menuTreeListQryExe.execute(qry);
	}

	/**
	 * 查询菜单列表
	 * @param qry 查询菜单列表参数
	 * @return 菜单列表
	 */
	@Override
	public Result<List<MenuCO>> list(MenuListQry qry) {
		return menuListQryExe.execute(qry);
	}

	/**
	 * 根据ID查看菜单
	 * @param qry 根据ID查看菜单参数
	 * @return 菜单
	 */
	@Override
	public Result<MenuCO> getById(MenuGetQry qry) {
		return menuGetQryExe.execute(qry);
	}

	/**
	 * 修改菜单
	 * @param cmd 修改菜单参数
	 * @return 修改结果
	 */
	@Override
	public Result<Boolean> update(MenuUpdateCmd cmd) {
		return menuUpdateCmdExe.execute(cmd);
	}

	/**
	 * 新增菜单
	 * @param cmd 新增菜单参数
	 * @return 新增结果
	 */
	@Override
	public Result<Boolean> insert(MenuInsertCmd cmd) {
		return menuInsertCmdExe.execute(cmd);
	}

	/**
	 * 根据ID删除菜单
	 * @param cmd 根据ID删除菜单参数
	 * @return 删除菜单
	 */
	@Override
	public Result<Boolean> deleteById(MenuDeleteCmd cmd) {
		return menuDeleteCmdExe.execute(cmd);
	}

	/**
	 * 查看树菜单
	 * @param qry 查看树菜单参数
	 * @return 树菜单
	 */
	@Override
	public Result<MenuCO> tree(MenuTreeGetQry qry) {
		return menuTreeGetQryExe.execute(qry);
	}

	/**
	 * 根据角色ID查看菜单IDS
	 * @param qry 根据角色ID查看菜单IDS参数
	 * @return 菜单IDS
	 */
	@Override
	public Result<List<Long>> ids(MenuIDSGetQry qry) {
		return menuIDSGetQryExe.execute(qry);
	}

	/**
	 * 查看租户菜单树
	 * @param qry 查看租户菜单树参数
	 * @return 租户菜单树
	 */
	@Override
	public Result<MenuCO> tenantTree(MenuTenantTreeGetQry qry) {
		return menuTenantTreeGetQryExe.execute(qry);
	}

}
