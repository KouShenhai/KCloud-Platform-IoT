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

import com.baomidou.dynamic.datasource.toolkit.DynamicDataSourceContextHolder;
import lombok.RequiredArgsConstructor;
import org.laokou.auth.gateway.OssLogGateway;
import org.laokou.auth.gatewayimpl.database.OssLogMapper;
import org.laokou.auth.gatewayimpl.database.dataobject.OssLogDO;
import org.laokou.common.i18n.util.ObjectUtils;
import org.springframework.stereotype.Component;

import static org.laokou.common.tenant.constant.DSConstants.DOMAIN;

/**
 * @author laokou
 */
@Component
@RequiredArgsConstructor
public class OssLogGatewayImpl implements OssLogGateway {

	private final OssLogMapper ossLogMapper;

	@Override
	public String getOssUrl(Long id) {
		try {
			DynamicDataSourceContextHolder.push(DOMAIN);
			OssLogDO ossLogDO = ossLogMapper.selectById(id);
			return ObjectUtils.isNotNull(ossLogDO) ? ossLogDO.getUrl() : "";
		}
		finally {
			DynamicDataSourceContextHolder.clear();
		}
	}

}
