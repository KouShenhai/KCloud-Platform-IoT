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

package org.laokou.generator.info.gatewayimpl;

import lombok.RequiredArgsConstructor;
import org.laokou.generator.info.model.InfoE;
import org.springframework.stereotype.Component;
import org.laokou.generator.info.gateway.InfoGateway;
import org.laokou.generator.info.gatewayimpl.database.InfoMapper;
import java.util.Arrays;
import org.laokou.generator.info.convertor.InfoConvertor;
import org.laokou.generator.info.gatewayimpl.database.dataobject.InfoDO;

/**
 *
 * 代码生成器信息网关实现.
 *
 * @author laokou
 */
@Component
@RequiredArgsConstructor
public class InfoGatewayImpl implements InfoGateway {

	private final InfoMapper infoMapper;

	@Override
	public void create(InfoE infoE) {
		infoMapper.insert(InfoConvertor.toDataObject(infoE, true));
	}

	@Override
	public void update(InfoE infoE) {
		InfoDO infoDO = InfoConvertor.toDataObject(infoE, false);
		infoDO.setVersion(infoMapper.selectVersion(infoE.getId()));
		infoMapper.updateById(infoDO);
	}

	@Override
	public void delete(Long[] ids) {
		infoMapper.deleteByIds(Arrays.asList(ids));
	}

}
