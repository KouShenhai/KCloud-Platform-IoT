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
import org.laokou.admin.domain.gateway.MenuGateway;
import org.laokou.admin.domain.menu.Menu;
import org.laokou.admin.gatewayimpl.database.MenuMapper;
import org.laokou.admin.gatewayimpl.database.dataobject.MenuDO;
import org.laokou.auth.domain.user.SuperAdmin;
import org.laokou.auth.domain.user.User;
import org.laokou.common.core.utils.ConvertUtil;
import org.springframework.stereotype.Component;
import java.util.List;
import static org.laokou.admin.common.Constant.DEFAULT_TENANT;
/**
 * @author laokou
 */
@Component
@RequiredArgsConstructor
public class MenuGatewayImpl implements MenuGateway {

	private final MenuMapper menuMapper;

	@Override
	public List<Menu> list(Integer type, User user) {
		List<MenuDO> menuList = getMenuList(type, user);
		return ConvertUtil.sourceToTarget(menuList, Menu.class);
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

}
