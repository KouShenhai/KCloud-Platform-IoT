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

package org.laokou.generator.template.gatewayimpl;

import lombok.RequiredArgsConstructor;
import org.laokou.generator.template.model.TemplateE;
import org.springframework.stereotype.Component;
import org.laokou.generator.template.gateway.TemplateGateway;
import org.laokou.generator.template.gatewayimpl.database.TemplateMapper;
import java.util.Arrays;
import org.laokou.generator.template.convertor.TemplateConvertor;
import org.laokou.generator.template.gatewayimpl.database.dataobject.TemplateDO;

/**
 *
 * 代码生成器模板网关实现.
 *
 * @author laokou
 */
@Component
@RequiredArgsConstructor
public class TemplateGatewayImpl implements TemplateGateway {

	private final TemplateMapper templateMapper;

	@Override
	public void create(TemplateE templateE) {
		templateMapper.insert(TemplateConvertor.toDataObject(templateE, true));
	}

	@Override
	public void update(TemplateE templateE) {
		TemplateDO templateDO = TemplateConvertor.toDataObject(templateE, false);
		templateDO.setVersion(templateMapper.selectVersion(templateE.getId()));
		templateMapper.updateById(templateDO);
	}

	@Override
	public void delete(Long[] ids) {
		templateMapper.deleteByIds(Arrays.asList(ids));
	}

}
