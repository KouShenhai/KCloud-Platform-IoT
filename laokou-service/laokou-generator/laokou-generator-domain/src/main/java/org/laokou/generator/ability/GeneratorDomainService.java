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

package org.laokou.generator.ability;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.laokou.common.core.utils.FileUtil;
import org.laokou.common.core.utils.TemplateUtil;
import org.laokou.common.i18n.common.exception.SystemException;
import org.laokou.common.i18n.utils.ResourceUtil;
import org.laokou.generator.gateway.TableGateway;
import org.laokou.generator.model.GeneratorA;
import org.laokou.generator.model.TableV;
import org.laokou.generator.model.Template;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;

import static org.laokou.common.i18n.common.constant.StringConstant.SLASH;

/**
 * @author laokou
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class GeneratorDomainService {

	/**
	 * 代码生成路径.
	 */
	private static final String SOURCE_PATH = "D:\\koushenhai\\project\\KCloud-Platform-IoT\\laokou-service\\";

	/**
	 * ZIP压缩路径.
	 */
	private static final String TARGET_PATH = "D:\\cola\\laokou.zip";

	/**
	 * 模板路径.
	 */
	private static final String TEMPLATE_PATH = "templates";

	private final TableGateway tableGateway;

	private final ExecutorService virtualThreadExecutor;

	public void generateCode(GeneratorA generatorA) {
		// 表信息
		List<TableV> tables = tableGateway.list(generatorA.getTableE());
		// 模板
		List<Template> templates = getTemplates();
		// 生成代码
		generateCode(generatorA, tables, templates);
	}

	private void generateCode(GeneratorA generatorA, List<TableV> tables, List<Template> templates) {
		// 生成到本地指定目录【临时】
		tables.forEach(item -> generateCode(generatorA, item, templates));
		// ZIP压缩到指定目录
		// FileUtil.zip(SOURCE_PATH + generatorA.getModuleName(), TARGET_PATH);
		// 删除【临时文件夹及目录】
		// FileUtil.delete(SOURCE_PATH + generatorA.getModuleName());
	}

	private void generateCode(GeneratorA generatorA, TableV tableV, List<Template> templates) {
		try {
			// 更新表信息
			generatorA.updateTable(tableV);
			// 根据模板批量生成代码
			List<Callable<Boolean>> list = templates.stream().map(item -> (Callable<Boolean>) () -> {
				String content = getContent(generatorA.toMap(), item.getTemplatePath(TEMPLATE_PATH));
				// 写入文件
				String directory = SOURCE_PATH + generatorA.getModuleName() + SLASH + item.getFileDirectory(generatorA);
				Path path = FileUtil.create(directory, item.getFileName(generatorA));
				FileUtil.write(path, content.getBytes(StandardCharsets.UTF_8));
				return true;
			}).toList();
			virtualThreadExecutor.invokeAll(list);
		}
		catch (InterruptedException e) {
			Thread.currentThread().interrupt();
			log.error("未知错误，错误信息：{}", e.getMessage(), e);
			throw new SystemException("S_UnKnow_Error", e.getMessage(), e);
		}
	}

	private String getContent(Map<String, Object> map, String templatePath) throws IOException {
		String template = ResourceUtil.getResource(templatePath).getContentAsString(StandardCharsets.UTF_8).trim();
		return TemplateUtil.getContent(template, map);
	}

	private List<Template> getTemplates() {
		List<Template> templates = new ArrayList<>(29);
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
		templates.add(Template.DATA_OBJECT);
		templates.add(Template.CLIENT_OBJECT);
		templates.add(Template.GATEWAY);
		templates.add(Template.GATEWAY_IMPL);
		templates.add(Template.CONTROLLER);
		templates.add(Template.MAPPER);
		templates.add(Template.MAPPER_XML);
		// templates.add(Template.API);
		// templates.add(Template.VIEW);
		// templates.add(Template.FORM_VIEW);
		return templates;
	}

}
