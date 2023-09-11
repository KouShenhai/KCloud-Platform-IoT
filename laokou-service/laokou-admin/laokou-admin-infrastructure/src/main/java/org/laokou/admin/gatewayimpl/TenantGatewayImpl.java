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
import org.laokou.admin.convertor.TenantConvertor;
import org.laokou.admin.domain.gateway.TenantGateway;
import org.laokou.admin.domain.tenant.Tenant;
import org.laokou.admin.gatewayimpl.database.TenantMapper;
import org.laokou.admin.gatewayimpl.database.UserMapper;
import org.laokou.admin.gatewayimpl.database.dataobject.TenantDO;
import org.laokou.admin.gatewayimpl.database.dataobject.UserDO;
import org.laokou.auth.domain.user.SuperAdmin;
import org.laokou.common.jasypt.utils.AesUtil;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author laokou
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class TenantGatewayImpl implements TenantGateway {

	private final TenantMapper tenantMapper;

	private final PasswordEncoder passwordEncoder;

	private final UserMapper userMapper;

	private static final String TENANT_USERNAME = AesUtil.encrypt("tenant");

	private static final String TENANT_PASSWORD = "tenant123";

	@Override
	public Boolean insert(Tenant tenant) {
		TenantDO tenantDO = TenantConvertor.toDataObject(tenant);
		return insertTenant(tenantDO, 1L);
	}

	@Transactional(rollbackFor = Exception.class)
	public Boolean insertTenant(TenantDO tenantDO, Long tenantCount) {
		boolean flag = tenantMapper.insert(tenantDO) > 0;
		return flag && insertUser(tenantCount);
	}

	private Boolean insertUser(Long tenantCount) {
		if (tenantCount > 0) {
			return false;
		}
		// 初始化超级管理员
		UserDO userDO = new UserDO();
		userDO.setUsername(TENANT_USERNAME);
		userDO.setPassword(passwordEncoder.encode(TENANT_PASSWORD));
		userDO.setSuperAdmin(SuperAdmin.YES.ordinal());
		return userMapper.insert(userDO) > 0;
	}

}
