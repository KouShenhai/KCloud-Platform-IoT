/*
 * Copyright (c) 2022-2025 KCloud-Platform-IoT Author or Authors. All Rights Reserved.
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
import org.laokou.auth.gateway.SourceGateway;
import org.laokou.common.i18n.util.ObjectUtils;
import org.laokou.common.tenant.mapper.SourceDO;
import org.laokou.common.tenant.mapper.SourceMapper;
import org.springframework.stereotype.Component;

/**
 * 数据源.
 *
 * @author laokou
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class SourceGatewayImpl implements SourceGateway {

	private final SourceMapper sourceMapper;

	/**
	 * 查看数据源.
	 * @param tenantCode 租户编码
	 * @return 数据源
	 */
	@Override
	public String getPrefix(String tenantCode) {
		SourceDO sourceDO = sourceMapper.selectOneByTenantCode(tenantCode);
		return ObjectUtils.isNull(sourceDO) ? null : sourceDO.getName();
	}

}
