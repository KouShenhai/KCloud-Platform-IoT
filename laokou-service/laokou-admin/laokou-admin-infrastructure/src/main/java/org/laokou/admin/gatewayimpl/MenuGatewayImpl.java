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

package org.laokou.admin.gatewayimpl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.laokou.admin.convertor.MenuConvertor;
import org.laokou.admin.domain.gateway.MenuGateway;
import org.laokou.admin.domain.menu.Menu;
import org.laokou.common.i18n.common.SuperAdminEnums;
import org.laokou.admin.domain.user.User;
import org.laokou.admin.gatewayimpl.database.MenuMapper;
import org.laokou.admin.gatewayimpl.database.dataobject.MenuDO;
import org.laokou.common.i18n.common.exception.SystemException;
import org.laokou.common.i18n.utils.LogUtil;
import org.laokou.common.mybatisplus.utils.TransactionalUtil;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 菜单管理.
 * @author laokou
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class MenuGatewayImpl implements MenuGateway {

	private final MenuMapper menuMapper;

	private final TransactionalUtil transactionalUtil;

	private final MenuConvertor menuConvertor;

	/**
	 * 查询菜单列表
	 * @param user 用户对象
	 * @param type 类型
	 * @return 菜单列表
	 */
	@Override
	public List<Menu> list(User user, Integer type) {
		return menuConvertor.convertEntityList(getMenuList(type, user));
	}

	/**
	 * 修改菜单
	 * @param menu 菜单对象
	 * @return 修改结果
	 */
	@Override
	public Boolean update(Menu menu) {
		MenuDO menuDO = menuConvertor.toDataObject(menu);
		menuDO.setVersion(menuMapper.getVersion(menuDO.getId(), MenuDO.class));
		return updateMenu(menuDO);
	}

	/**
	 * 新增菜单
	 * @param menu 菜单对象
	 * @return 新增结果
	 */
	@Override
	public Boolean insert(Menu menu) {
		MenuDO menuDO = menuConvertor.toDataObject(menu);
		return insertMenu(menuDO);
	}

	/**
	 * 根据ID删除菜单
	 * @param id ID
	 * @return 删除结果
	 */
	@Override
	public Boolean deleteById(Long id) {
		return transactionalUtil.defaultExecute(r -> {
			try {
				return menuMapper.deleteById(id) > 0;
			}
			catch (Exception e) {
				log.error("错误信息：{}，详情见日志", LogUtil.result(e.getMessage()), e);
				r.setRollbackOnly();
				throw new SystemException(e.getMessage());
			}
		});
	}

	/**
	 * 根据ID查看菜单
	 * @param id ID
	 * @return 菜单
	 */
	@Override
	public Menu getById(Long id) {
		return menuConvertor.convertEntity(menuMapper.selectById(id));
	}

	/**
	 * 根据角色ID查看菜单IDS
	 * @param roleId 角色ID
	 * @return 菜单IDS
	 */
	@Override
	public List<Long> getIdsByRoleId(Long roleId) {
		return menuMapper.getMenuIdsByRoleId(roleId);
	}

	/**
	 * 根据租户ID查询菜单列表
	 * @param menu 菜单对象
	 * @param tenantId 租户ID
	 * @return 菜单列表
	 */
	@Override
	public List<Menu> list(Menu menu, Long tenantId) {
		List<MenuDO> list = menuMapper.getMenuListLikeName(null, menu.getName());
		return menuConvertor.convertEntityList(list);
	}

	/**
	 * 查询租户菜单列表
	 * @return 租户菜单列表
	 */
	@Override
	public List<Menu> getTenantMenuList() {
		return menuConvertor.convertEntityList(menuMapper.getTenantMenuList());
	}

	/**
	 * 查询菜单列表
	 * @param type 菜单类型 0菜单 1按钮
	 * @param user 用户对象
	 * @return 菜单列表
	 */
	private List<MenuDO> getMenuList(Integer type, User user) {
		Long userId = user.getId();
		Integer superAdmin = user.getSuperAdmin();
		if (superAdmin == SuperAdminEnums.YES.ordinal()) {
			return menuMapper.getMenuListLikeName(type, null);
		}
		return menuMapper.getMenuListByUserId(type, userId);
	}

	/**
	 * 修改菜单
	 * @param menuDO 菜单数据模型
	 * @return 修改结果
	 */
	private Boolean updateMenu(MenuDO menuDO) {
		return transactionalUtil.defaultExecute(r -> {
			try {
				return menuMapper.updateById(menuDO) > 0;
			}
			catch (Exception e) {
				log.error("错误信息：{}，详情见日志", LogUtil.result(e.getMessage()), e);
				r.setRollbackOnly();
				throw new SystemException(e.getMessage());
			}
		});
	}

	/**
	 * 新增菜单
	 * @param menuDO 菜单数据模型
	 * @return 新增结果
	 */
	private Boolean insertMenu(MenuDO menuDO) {
		return transactionalUtil.defaultExecute(r -> {
			try {
				return menuMapper.insertTable(menuDO);
			}
			catch (Exception e) {
				log.error("错误信息：{}，详情见日志", LogUtil.result(e.getMessage()), e);
				r.setRollbackOnly();
				throw new SystemException(e.getMessage());
			}
		});
	}

}
