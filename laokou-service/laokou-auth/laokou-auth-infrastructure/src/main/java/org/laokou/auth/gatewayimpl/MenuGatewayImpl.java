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
package org.laokou.auth.gatewayimpl;

import lombok.RequiredArgsConstructor;
import org.laokou.auth.domain.gateway.MenuGateway;
import org.laokou.auth.domain.user.SuperAdmin;
import org.laokou.auth.domain.user.User;
import org.laokou.auth.gatewayimpl.database.MenuMapper;
import org.laokou.auth.gatewayimpl.database.TenantMapper;
import org.springframework.stereotype.Component;

import java.util.List;

import static org.laokou.auth.common.Constant.*;

/**
 * @author laokou
 */
@Component
@RequiredArgsConstructor
public class MenuGatewayImpl implements MenuGateway {

	private final MenuMapper menuMapper;

	private final TenantMapper tenantMapper;

	@Override
	public List<String> getPermissions(User user) {
		Long userId = user.getId();
		Long tenantId = user.getTenantId();
		Integer superAdmin = user.getSuperAdmin();
		if (superAdmin == SuperAdmin.YES.ordinal()) {
			if (tenantId == DEFAULT_TENANT) {
				return menuMapper.getPermissions();
			}
			return tenantMapper.getPermissionsByTenantId(tenantId);
		}
		return menuMapper.getPermissionsByUserId(userId);
	}

}
