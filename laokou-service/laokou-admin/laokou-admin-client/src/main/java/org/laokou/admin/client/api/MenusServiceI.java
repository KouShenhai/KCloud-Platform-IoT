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
package org.laokou.admin.client.api;

import org.laokou.admin.client.dto.menu.*;
import org.laokou.admin.client.dto.menu.clientobject.MenuCO;
import org.laokou.common.i18n.dto.Result;

import java.util.List;

/**
 * @author laokou
 */
public interface MenusServiceI {

	Result<MenuCO> treeList(MenuTreeListQry qry);

	Result<List<MenuCO>> list(MenuListQry qry);

	Result<MenuCO> get(MenuGetQry qry);

	Result<Boolean> update(MenuUpdateCmd cmd);

	Result<Boolean> insert(MenuInsertCmd cmd);

	Result<Boolean> delete(MenuDeleteCmd cmd);

	//
	// /**
	// * 获取菜单列表（树形）
	// * @return
	// */
	// SysMenuVO getMenuList();
	//
	// /**
	// * 获取菜单列表
	// * @param dto
	// * @return
	// */
	// List<SysMenuVO> queryMenuList(SysMenuQo dto);
	//
	// /**
	// * 根据id查询菜单
	// * @param id
	// * @return
	// */
	// SysMenuVO getMenuById(Long id);
	//
	// /**
	// * 修改菜单
	// * @param dto
	// * @return
	// */
	// Boolean updateMenu(SysMenuDTO dto);
	//
	// /**
	// * 新增菜单
	// * @param dto
	// * @return
	// */
	// Boolean insertMenu(SysMenuDTO dto);
	//
	// /**
	// * 根据id删除菜单
	// * @param id
	// * @return
	// */
	// Boolean deleteMenu(Long id);
	//
	// /**
	// * 根据角色id构建菜单列表（树形）
	// * @return
	// */
	// SysMenuVO treeMenu();
	//
	// /**
	// * 根据角色id查询菜单ids
	// * @param roleId
	// * @return
	// */
	// List<Long> getMenuIdsByRoleId(Long roleId);
	//
	// /**
	// * 租户树菜单
	// * @return
	// */
	// SysMenuVO treeTenantMenu();

}
