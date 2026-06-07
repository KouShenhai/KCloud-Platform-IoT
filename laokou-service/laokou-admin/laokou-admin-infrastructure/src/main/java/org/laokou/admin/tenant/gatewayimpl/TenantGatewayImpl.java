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

package org.laokou.admin.tenant.gatewayimpl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.RequiredArgsConstructor;
import org.laokou.admin.tenant.convertor.TenantConvertor;
import org.laokou.admin.tenant.gateway.TenantGateway;
import org.laokou.admin.tenant.model.TenantE;
import org.laokou.common.i18n.util.ObjectUtils;
import org.laokou.common.tenant.mapper.TenantDO;
import org.laokou.common.tenant.mapper.TenantMapper;
import org.springframework.stereotype.Component;

import java.util.Arrays;

/**
 * 租户网关实现.
 *
 * @author laokou
 */
@Component("adminTenantGateway")
@RequiredArgsConstructor
public class TenantGatewayImpl implements TenantGateway {

	private static final long DEFAULT_TENANT_ID = 1L;

	private static final String DEFAULT_TENANT_CODE = "laokouyun";

	private final TenantMapper tenantMapper;

	@Override
	public void createTenant(TenantE tenantE) {
		// tenantMapper.insert(TenantConvertor.toDataObject(idGenerator.getId(), tenantE));
	}

	@Override
	public void updateTenant(TenantE tenantE) {
		TenantDO tenantDO = TenantConvertor.toDataObject(null, tenantE);
		tenantDO.setVersion(tenantMapper.selectVersion(tenantE.getId()));
		tenantMapper.updateById(tenantDO);
	}

	@Override
	public void deleteTenant(Long[] ids) {
		tenantMapper.deleteByIds(Arrays.asList(ids));
	}

	@Override
	public boolean existsCode(Long id, String code) {
		return tenantMapper.selectCount(Wrappers.lambdaQuery(TenantDO.class)
			.eq(TenantDO::getCode, code)
			.ne(ObjectUtils.isNotNull(id), TenantDO::getId, id)) > 0;
	}

	@Override
	public boolean existsTenant(Long id) {
		return tenantMapper.selectCount(Wrappers.lambdaQuery(TenantDO.class).eq(TenantDO::getId, id)) > 0;
	}

	@Override
	public boolean existsTenant(Long[] ids) {
		return tenantMapper
			.selectCount(Wrappers.lambdaQuery(TenantDO.class).in(TenantDO::getId, Arrays.asList(ids))) == ids.length;
	}

	@Override
	public boolean existsDefaultTenant(Long[] ids) {
		return tenantMapper.selectCount(Wrappers.lambdaQuery(TenantDO.class)
			.in(TenantDO::getId, Arrays.asList(ids))
			.and(wrapper -> wrapper.eq(TenantDO::getId, DEFAULT_TENANT_ID)
				.or()
				.eq(TenantDO::getCode, DEFAULT_TENANT_CODE))) > 0;
	}

}
