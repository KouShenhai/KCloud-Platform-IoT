/*
 * Copyright (c) 2022-2026 KCloud-Platform-IoT Author or Authors. All Rights Reserved.
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

package org.laokou.auth.gatewayimpl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.laokou.auth.gateway.MenuGateway;
import org.laokou.auth.gatewayimpl.database.MenuMapper;
import org.laokou.auth.model.entity.UserE;
import org.springframework.stereotype.Component;

import java.util.Set;

/**
 * 菜单.
 *
 * @author laokou
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class MenuGatewayImpl implements MenuGateway {

	private final MenuMapper menuMapper;

	/**
	 * 查看菜单权限标识集合.
	 * @param user 用户对象
	 * @return 菜单权限标识集合
	 */
	@Override
	public Set<String> getMenuPermissions(UserE user) {
		if (user.isSuperAdministrator()) {
			return Set.copyOf(menuMapper.selectPermissions());
		}
		return Set.copyOf(menuMapper.selectPermissionsByUserId(user.getId()));
	}

}
