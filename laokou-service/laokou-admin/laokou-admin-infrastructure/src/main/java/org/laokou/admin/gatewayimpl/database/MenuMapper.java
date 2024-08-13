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

package org.laokou.admin.gatewayimpl.database;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.laokou.admin.gatewayimpl.database.dataobject.MenuDO;
import org.laokou.common.mybatisplus.mapper.CrudMapper;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 菜单.
 *
 * @author laokou
 */
@Mapper
@Repository
public interface MenuMapper extends CrudMapper<Long, Integer, MenuDO> {

	/**
	 * 根据用户ID查询菜单列表.
	 *
	 * @param userId 用户ID
	 * @return 菜单列表
	 */
	List<MenuDO> selectListByUserId(@Param("userId") Long userId);

	List<MenuDO> selectObjects();

	/**
	 * 根据角色ID查看菜单IDS.
	 *
	 * @param roleId 角色ID
	 * @return 菜单IDS
	 */
	List<Long> selectIdsByRoleId(@Param("roleId") Long roleId);

	/**
	 * 查询租户菜单列表.
	 *
	 * @return 租户菜单列表
	 */
	List<MenuDO> selectTenantMenuList();

	/**
	 * 根据套餐ID查询租户菜单列表.
	 *
	 * @param packageId 套餐ID
	 * @return 租户菜单列表
	 */
	List<MenuDO> selectTenantMenuListByPackageId(@Param("packageId") Long packageId);

}
