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

package org.laokou.admin.source.gatewayimpl;

import lombok.RequiredArgsConstructor;
import org.laokou.admin.source.convertor.SourceConvertor;
import org.laokou.admin.source.gateway.SourceGateway;
import org.laokou.admin.source.model.SourceE;
import org.laokou.common.openfeign.rpc.DistributedIdentifierFeignClientWrapper;
import org.laokou.common.tenant.mapper.SourceDO;
import org.laokou.common.tenant.mapper.SourceMapper;
import org.springframework.stereotype.Component;

import java.util.Arrays;

/**
 * 数据源网关实现.
 *
 * @author laokou
 */
@Component
@RequiredArgsConstructor
public class SourceGatewayImpl implements SourceGateway {

	private final SourceMapper sourceMapper;

	private final DistributedIdentifierFeignClientWrapper distributedIdentifierFeignClientWrapper;

	@Override
	public void createSource(SourceE sourceE) {
		sourceMapper.insert(SourceConvertor.toDataObject(distributedIdentifierFeignClientWrapper.getId(), sourceE));
	}

	@Override
	public void updateSource(SourceE sourceE) {
		SourceDO sourceDO = SourceConvertor.toDataObject(null, sourceE);
		sourceDO.setVersion(sourceMapper.selectVersion(sourceE.getId()));
		sourceMapper.updateById(sourceDO);
	}

	@Override
	public void deleteSource(Long[] ids) {
		sourceMapper.deleteByIds(Arrays.asList(ids));
	}

}
