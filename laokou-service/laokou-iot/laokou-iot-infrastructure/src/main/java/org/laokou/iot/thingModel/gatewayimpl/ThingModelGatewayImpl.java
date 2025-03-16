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

package org.laokou.iot.thingModel.gatewayimpl;

import lombok.RequiredArgsConstructor;
import org.laokou.iot.thingModel.convertor.ThingModelConvertor;
import org.laokou.iot.thingModel.gatewayimpl.database.ThingModelMapper;
import org.laokou.iot.thingModel.gatewayimpl.database.dataobject.ThingModelDO;
import org.laokou.iot.thingModel.gateway.ThingModelGateway;
import org.laokou.iot.thingModel.model.ThingModelE;
import org.springframework.stereotype.Component;

import java.util.Arrays;

/**
 *
 * 物模型网关实现.
 *
 * @author laokou
 */
@Component
@RequiredArgsConstructor
public class ThingModelGatewayImpl implements ThingModelGateway {

	private final ThingModelMapper thingModelMapper;

	@Override
	public void create(ThingModelE thingModelE) {
		thingModelMapper.insert(ThingModelConvertor.toDataObject(thingModelE, true));
	}

	@Override
	public void update(ThingModelE thingModelE) {
		ThingModelDO thingModelDO = ThingModelConvertor.toDataObject(thingModelE, false);
		thingModelDO.setVersion(thingModelMapper.selectVersion(thingModelE.getId()));
		thingModelMapper.updateById(thingModelDO);
	}

	@Override
	public void delete(Long[] ids) {
		thingModelMapper.deleteByIds(Arrays.asList(ids));
	}

}
