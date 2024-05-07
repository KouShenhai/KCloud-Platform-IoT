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

package org.laokou.admin.api;

import org.laokou.admin.dto.menu.*;
import org.laokou.admin.dto.menu.clientobject.MenuCO;
import org.laokou.admin.dto.menu.clientobject.RouterCO;
import org.laokou.common.i18n.dto.Result;

import java.util.List;

/**
 * 菜单管理.
 *
 * @author laokou
 */
public interface MenusServiceI {

	Result<List<RouterCO>> getRouters();

	/**
	 * 查询菜单列表.
	 * @param qry 查询菜单列表参数
	 * @return 菜单列表
	 */
	Result<List<MenuCO>> findList(MenuListQry qry);

	/**
	 * 根据ID查看菜单.
	 * @param qry 根据ID查看菜单参数
	 * @return 菜单
	 */
	Result<MenuCO> findById(MenuGetQry qry);

	/**
	 * 修改菜单.
	 * @param cmd 修改菜单参数
	 */
	void modify(MenuModifyCmd cmd);

	/**
	 * 新增菜单.
	 * @param cmd 新增菜单参数
	 */
	void create(MenuCreateCmd cmd);

	/**
	 * 根据IDS删除菜单.
	 * @param cmd 根据IDS删除菜单参数
	 */
	void remove(MenuRemoveCmd cmd);

	/**
	 * 根据角色ID查看菜单IDS.
	 * @param qry 根据角色ID查看菜单IDS参数
	 * @return 菜单IDS
	 */
	Result<List<Long>> findIds(MenuIdsGetQry qry);

	/**
	 * 查看租户菜单树.
	 * @param qry 查看租户菜单树参数
	 * @return 租户菜单树
	 */
	Result<List<MenuCO>> findTenantMenuList(MenuTenantListQry qry);

}
