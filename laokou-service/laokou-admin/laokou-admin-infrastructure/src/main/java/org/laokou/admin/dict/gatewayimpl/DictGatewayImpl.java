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

package org.laokou.admin.dict.gatewayimpl;

import lombok.RequiredArgsConstructor;
import org.laokou.admin.dict.convertor.DictConvertor;
import org.laokou.admin.dict.gateway.DictGateway;
import org.laokou.admin.dict.gatewayimpl.database.DictMapper;
import org.laokou.admin.dict.gatewayimpl.database.dataobject.DictDO;
import org.laokou.admin.dict.model.DictE;
import org.springframework.stereotype.Component;

import java.util.Arrays;

/**
 * 字典网关实现.
 *
 * @author laokou
 */
@Component
@RequiredArgsConstructor
public class DictGatewayImpl implements DictGateway {

	private final DictMapper dictMapper;

	@Override
	public void create(DictE dictE) {
		dictMapper.insert(DictConvertor.toDataObject(dictE));
	}

	@Override
	public void update(DictE dictE) {
		DictDO dictDO = DictConvertor.toDataObject(dictE);
		dictDO.setVersion(dictMapper.selectVersion(dictE.getId()));
		dictMapper.updateById(dictDO);
	}

	@Override
	public void delete(Long[] ids) {
		dictMapper.deleteByIds(Arrays.asList(ids));
	}

}
