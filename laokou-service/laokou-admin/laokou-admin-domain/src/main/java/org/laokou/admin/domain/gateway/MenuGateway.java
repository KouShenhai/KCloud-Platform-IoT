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

package org.laokou.admin.domain.gateway;

import io.swagger.v3.oas.annotations.media.Schema;
import org.laokou.admin.domain.menu.Menu;

import java.util.List;

/**
 * @author laokou
 */
@Schema(name = "MenuGateway", description = "菜单网关")
public interface MenuGateway {

	/**
	 * 修改菜单.
	 * @param menu 菜单对象
	 */
	void modify(Menu menu);

	/**
	 * 新增菜单.
	 * @param menu 菜单对象
	 */
	void create(Menu menu);

	/**
	 * 根据ID删除菜单.
	 * @param ids IDS
	 */
	void remove(Long[] ids);

	/**
	 * 根据ID查看菜单.
	 * @param id ID
	 * @return 菜单
	 */
	Menu getById(Long id);

	/**
	 * 根据角色ID查询菜单IDS.
	 * @param roleId 角色ID
	 * @return 菜单IDS
	 */
	List<Long> getIdsByRoleId(Long roleId);

	/**
	 * 查询租户菜单列表.
	 * @return 租户菜单列表
	 */
	List<Menu> getTenantMenuList();

}
