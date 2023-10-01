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

package org.laokou.admin.gatewayimpl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.laokou.admin.convertor.MenuConvertor;
import org.laokou.admin.domain.gateway.MenuGateway;
import org.laokou.admin.domain.menu.Menu;
import org.laokou.admin.domain.user.SuperAdmin;
import org.laokou.admin.domain.user.User;
import org.laokou.admin.gatewayimpl.database.MenuMapper;
import org.laokou.admin.gatewayimpl.database.dataobject.MenuDO;
import org.laokou.common.core.utils.ConvertUtil;
import org.laokou.common.mybatisplus.utils.TransactionalUtil;
import org.springframework.stereotype.Component;

import java.util.List;

import static org.laokou.admin.common.Constant.DEFAULT_TENANT;

/**
 * @author laokou
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class MenuGatewayImpl implements MenuGateway {

	private final MenuMapper menuMapper;

	private final TransactionalUtil transactionalUtil;

	@Override
	public List<Menu> list(User user, Integer type) {
		List<MenuDO> menuList = getMenuList(type, user);
		return ConvertUtil.sourceToTarget(menuList, Menu.class);
	}

	@Override
	public Boolean update(Menu menu) {
		MenuDO menuDO = MenuConvertor.toDataObject(menu);
		menuDO.setVersion(menuMapper.getVersion(menuDO.getId(), MenuDO.class));
		return updateMenu(menuDO);
	}

	@Override
	public Boolean insert(Menu menu) {
		MenuDO menuDO = MenuConvertor.toDataObject(menu);
		return insertMenu(menuDO);
	}

	@Override
	public Boolean deleteById(Long id) {
		return transactionalUtil.execute(rollback -> {
			try {
				return menuMapper.deleteById(id) > 0;
			}
			catch (Exception e) {
				log.error("错误信息：{}", e.getMessage());
				rollback.setRollbackOnly();
				return false;
			}
		});
	}

	@Override
	public Menu getById(Long id) {
		MenuDO menuDO = menuMapper.selectById(id);
		return ConvertUtil.sourceToTarget(menuDO, Menu.class);
	}

	@Override
	public List<Long> getIdsByRoleId(Long roleId) {
		return menuMapper.getMenuIdsByRoleId(roleId);
	}

	@Override
	public List<Menu> list(Menu menu, Long tenantId) {
		List<MenuDO> list;
		if (tenantId == DEFAULT_TENANT) {
			list = menuMapper.getMenuListLikeName(null, menu.getName());
		}
		else {
			list = menuMapper.getMenuListByTenantIdAndLikeName(null, tenantId, menu.getName());
		}
		return ConvertUtil.sourceToTarget(list, Menu.class);
	}

	@Override
	public List<Menu> getTenantMenuList() {
		List<MenuDO> list = menuMapper.getTenantMenuList();
		return ConvertUtil.sourceToTarget(list, Menu.class);
	}

	private List<MenuDO> getMenuList(Integer type, User user) {
		Long userId = user.getId();
		Long tenantId = user.getTenantId();
		Integer superAdmin = user.getSuperAdmin();
		if (tenantId == DEFAULT_TENANT) {
			if (superAdmin == SuperAdmin.YES.ordinal()) {
				return menuMapper.getMenuListLikeName(type, null);
			}
			return menuMapper.getMenuListByUserId(type, userId);
		}
		else {
			return menuMapper.getMenuListByTenantIdAndLikeName(type, tenantId, null);
		}
	}

	private Boolean updateMenu(MenuDO menuDO) {
		return transactionalUtil.execute(r -> {
			try {
				return menuMapper.updateById(menuDO) > 0;
			}
			catch (Exception e) {
				log.error("错误信息：{}", e.getMessage());
				r.setRollbackOnly();
				return false;
			}
		});
	}

	private Boolean insertMenu(MenuDO menuDO) {
		return transactionalUtil.execute(r -> {
			try {
				return menuMapper.insertTable(menuDO);
			}
			catch (Exception e) {
				log.error("错误信息：{}", e.getMessage());
				r.setRollbackOnly();
				return false;
			}
		});
	}

}
