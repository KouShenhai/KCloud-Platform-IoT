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

package org.laokou.iot.model.gatewayimpl;

import lombok.RequiredArgsConstructor;
import org.laokou.iot.model.model.ModelE;
import org.springframework.stereotype.Component;
import org.laokou.iot.model.gateway.ModelGateway;
import org.laokou.iot.model.gatewayimpl.database.ModelMapper;
import java.util.Arrays;
import org.laokou.common.mybatisplus.utils.TransactionalUtil;
import org.laokou.iot.model.convertor.ModelConvertor;
import org.laokou.iot.model.gatewayimpl.database.dataobject.ModelDO;

/**
 *
 * 模型网关实现.
 *
 * @author laokou
 */
@Component
@RequiredArgsConstructor
public class ModelGatewayImpl implements ModelGateway {

	private final ModelMapper modelMapper;

	private final TransactionalUtil transactionalUtil;

	@Override
	public void create(ModelE modelE) {
		modelMapper.insert(ModelConvertor.toDataObject(modelE, true));
	}

	@Override
	public void update(ModelE modelE) {
		ModelDO modelDO = ModelConvertor.toDataObject(modelE, false);
		modelDO.setVersion(modelMapper.selectVersion(modelE.getId()));
		modelMapper.updateById(modelDO);
	}

	@Override
	public void delete(Long[] ids) {
		modelMapper.deleteByIds(Arrays.asList(ids));
	}

}
