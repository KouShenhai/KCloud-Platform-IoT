/*
 * Copyright (c) 2022 KCloud-Platform-Alibaba Author or Authors. All Rights Reserved.
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
import org.laokou.auth.domain.gateway.SourceGateway;
import org.laokou.auth.domain.source.Source;
import org.laokou.auth.gatewayimpl.database.SourceMapper;
import org.laokou.auth.gatewayimpl.database.dataobject.SourceDO;
import org.laokou.common.i18n.utils.ObjectUtil;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import java.util.Objects;

/**
 * @author laokou
 */
@Component
@RequiredArgsConstructor
public class SourceGatewayImpl implements SourceGateway {

	private final SourceMapper sourceMapper;

	@Override
	public Source getSourceName(Long tenantId) {
		return toSource(sourceMapper.getSourceByTenantId(tenantId));
	}

	private Source toSource(SourceDO sourceDO) {
		Assert.isTrue(ObjectUtil.isNotNull(sourceDO), "sourceDO is null");
		Source source = new Source();
		source.setName(sourceDO.getName());
		source.setPassword(sourceDO.getPassword());
		source.setUsername(sourceDO.getUsername());
		source.setUrl(sourceDO.getUrl());
		source.setDriverClassName(sourceDO.getDriverClassName());
		return source;
	}

}
