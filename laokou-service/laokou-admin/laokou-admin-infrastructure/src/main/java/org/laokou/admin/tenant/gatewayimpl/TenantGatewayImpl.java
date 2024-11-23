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

package org.laokou.admin.tenant.gatewayimpl;

import lombok.RequiredArgsConstructor;
import org.laokou.admin.tenant.model.TenantE;
import org.springframework.stereotype.Component;
import org.laokou.admin.tenant.gateway.TenantGateway;
import org.laokou.common.tenant.mapper.TenantMapper;
import java.util.Arrays;
import org.laokou.admin.tenant.convertor.TenantConvertor;
import org.laokou.common.tenant.mapper.TenantDO;

/**
 * 租户网关实现.
 *
 * @author laokou
 */
@Component
@RequiredArgsConstructor
public class TenantGatewayImpl implements TenantGateway {

	private final TenantMapper tenantMapper;

	@Override
	public void create(TenantE tenantE) {
		tenantMapper.insert(TenantConvertor.toDataObject(tenantE));
	}

	@Override
	public void update(TenantE tenantE) {
		TenantDO tenantDO = TenantConvertor.toDataObject(tenantE);
		tenantDO.setVersion(tenantMapper.selectVersion(tenantE.getId()));
		tenantMapper.updateById(tenantDO);
	}

	@Override
	public void delete(Long[] ids) {
		tenantMapper.deleteByIds(Arrays.asList(ids));
	}

}
