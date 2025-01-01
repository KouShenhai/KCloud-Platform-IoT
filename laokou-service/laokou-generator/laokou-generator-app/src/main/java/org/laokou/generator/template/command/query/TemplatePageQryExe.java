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

package org.laokou.generator.template.command.query;

import lombok.RequiredArgsConstructor;
import org.laokou.common.i18n.dto.Page;
import org.laokou.common.i18n.dto.Result;
import org.laokou.generator.template.convertor.TemplateConvertor;
import org.laokou.generator.template.dto.TemplatePageQry;
import org.laokou.generator.template.dto.clientobject.TemplateCO;
import org.laokou.generator.template.gatewayimpl.database.TemplateMapper;
import org.laokou.generator.template.gatewayimpl.database.dataobject.TemplateDO;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 分页查询代码生成器模板请求执行器.
 *
 * @author laokou
 */
@Component
@RequiredArgsConstructor
public class TemplatePageQryExe {

	private final TemplateMapper templateMapper;

	public Result<Page<TemplateCO>> execute(TemplatePageQry qry) {
		List<TemplateDO> list = templateMapper.selectObjectPage(qry);
		long total = templateMapper.selectObjectCount(qry);
		return Result.ok(Page.create(list.stream().map(TemplateConvertor::toClientObject).toList(), total));
	}

}
