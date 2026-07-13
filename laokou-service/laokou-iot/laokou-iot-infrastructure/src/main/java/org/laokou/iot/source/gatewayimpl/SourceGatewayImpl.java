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

package org.laokou.iot.source.gatewayimpl;

import lombok.RequiredArgsConstructor;
import org.laokou.iot.source.convertor.SourceConvertor;
import org.laokou.iot.source.gateway.SourceGateway;
import org.laokou.iot.source.gatewayimpl.database.SourceMapper;
import org.laokou.iot.source.gatewayimpl.database.dataobject.SourceDO;
import org.laokou.iot.source.model.SourceA;
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

	@Override
	public void createSource(SourceA sourceA) {
		sourceMapper.insert(SourceConvertor.toDataObject(sourceA));
	}

	@Override
	public void updateSource(SourceA sourceA) {
		SourceDO sourceDO = SourceConvertor.toDataObject(sourceA);
		sourceDO.setVersion(sourceMapper.selectVersion(sourceA.getId()));
		sourceMapper.updateById(sourceDO);
	}

	@Override
	public void deleteSource(Long[] ids) {
		sourceMapper.deleteByIds(Arrays.asList(ids));
	}

}
