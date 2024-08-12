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

package org.laokou.generator.ability;

import lombok.RequiredArgsConstructor;
import org.laokou.generator.gateway.TableGateway;
import org.laokou.generator.model.GeneratorA;
import org.laokou.generator.model.TableV;
import org.laokou.generator.model.Template;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * @author laokou
 */
@Component
@RequiredArgsConstructor
public class GeneratorDomainService {

	private final TableGateway tableGateway;

	public void generateCode(GeneratorA generatorA) {
		// 表字段信息
		List<TableV> list = tableGateway.list(generatorA.getTableE());
		// 模板
		generatorA.updateTemplates(getTemplates());
	}

	private List<Template> getTemplates() {
		List<Template> templates = new ArrayList<>(26);
		templates.add(Template.SAVE_CMD);
		templates.add(Template.MODIFY_CMD);
		templates.add(Template.REMOVE_CMD);
		templates.add(Template.PAGE_QRY);
		templates.add(Template.GET_QRY);
		templates.add(Template.IMPORT_CMD);
		templates.add(Template.EXPORT_CMD);
		templates.add(Template.CONVERTOR);
		templates.add(Template.SAVE_CMD_EXE);
		templates.add(Template.MODIFY_CMD_EXE);
		templates.add(Template.REMOVE_CMD_EXE);
		templates.add(Template.PAGE_QRY_EXE);
		templates.add(Template.GET_QRY_EXE);
		templates.add(Template.IMPORT_CMD_EXE);
		templates.add(Template.EXPORT_CMD_EXE);
		templates.add(Template.ENTITY);
		templates.add(Template.SERVICE_I);
		templates.add(Template.SERVICE_IMPL);
		templates.add(Template.DOMAIN_SERVICE);
		templates.add(Template.DO);
		templates.add(Template.CO);
		templates.add(Template.GATEWAY);
		templates.add(Template.GATEWAY_IMPL);
		templates.add(Template.CONTROLLER);
		templates.add(Template.MAPPER);
		templates.add(Template.MAPPER_XML);
		return templates;
	}

}
