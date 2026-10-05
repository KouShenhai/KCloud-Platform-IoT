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

package org.laokou.iot.gateway.gatewayimpl;

import lombok.RequiredArgsConstructor;
import org.laokou.iot.gateway.convertor.GatewayConvertor;
import org.laokou.iot.gateway.gateway.GatewayGateway;
import org.laokou.iot.gateway.gatewayimpl.database.GatewayMapper;
import org.laokou.iot.gateway.gatewayimpl.database.dataobject.GatewayDO;
import org.laokou.iot.gateway.model.GatewayA;
import org.springframework.stereotype.Component;

import java.util.Arrays;

/**
 *
 * 网关网关实现.
 *
 * @author laokou
 */
@Component
@RequiredArgsConstructor
public class GatewayGatewayImpl implements GatewayGateway {

	private final GatewayMapper gatewayMapper;

	@Override
	public void createGateway(GatewayA gatewayA) {
		gatewayMapper.insert(GatewayConvertor.toDataObject(gatewayA));
	}

	@Override
	public void updateGateway(GatewayA gatewayA) {
		GatewayDO gatewayDO = GatewayConvertor.toDataObject(gatewayA);
		gatewayDO.setVersion(gatewayMapper.selectVersion(gatewayA.getId()));
		gatewayMapper.updateById(gatewayDO);
	}

	@Override
	public void deleteGateway(Long[] ids) {
		gatewayMapper.deleteByIds(Arrays.asList(ids));
	}

}
