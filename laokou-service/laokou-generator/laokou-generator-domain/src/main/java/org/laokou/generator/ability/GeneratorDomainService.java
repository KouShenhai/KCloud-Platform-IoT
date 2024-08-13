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
import lombok.SneakyThrows;
import org.laokou.common.core.utils.FileUtil;
import org.laokou.common.core.utils.TemplateUtil;
import org.laokou.common.i18n.utils.ResourceUtil;
import org.laokou.generator.gateway.TableGateway;
import org.laokou.generator.model.GeneratorA;
import org.laokou.generator.model.TableV;
import org.laokou.generator.model.Template;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

import static org.laokou.common.i18n.common.constant.StringConstant.SLASH;

/**
 * @author laokou
 */
@Component
@RequiredArgsConstructor
public class GeneratorDomainService {

	/**
	 * 代码生成路径.
	 */
	private static final String SOURCE_PATH = "D:\\cola\\laokou";

	/**
	 * ZIP压缩路径.
	 */
	private static final String TARGET_PATH = "D:\\cola\\laokou.zip";

	/**
	 * 模板路径.
	 */
	private static final String TEMPLATE_PATH = "templates";

	private final TableGateway tableGateway;

	private final Executor executor;

	public void generateCode(GeneratorA generatorA) {
		// 表字段
		List<TableV> tables = tableGateway.list(generatorA.getTableE());
		// 模板
		List<Template> templates = getTemplate();
		// 生成代码
		generateCode(generatorA, tables, templates);
	}

	private void generateCode(GeneratorA generatorA, List<TableV> tables, List<Template> templates) {
		// 生成到本地指定目录【临时】
		tables.forEach(item -> generateCode(generatorA, item, templates));
		// ZIP压缩到指定目录
		FileUtil.zip(SOURCE_PATH, TARGET_PATH);
		// 删除临时文件
		FileUtil.deleteFile(SOURCE_PATH);
	}

	private void generateCode(GeneratorA generatorA, TableV tableV, List<Template> templates) {
		templates.parallelStream().map(item -> CompletableFuture.runAsync(() -> {
			generatorA.updateTable(tableV);
			String content = getContent(generatorA.toMap(), item.getTemplatePath(TEMPLATE_PATH));
			// 写入文件
			File file = FileUtil.createFile(
					SOURCE_PATH + SLASH
							+ item.getFileDirectory(generatorA.getPackageName(), generatorA.getModuleName()),
					item.getFileName(tableV.className()));
			FileUtil.write(file, content.getBytes(StandardCharsets.UTF_8));
		}, executor)).toList().forEach(CompletableFuture::join);
	}

	@SneakyThrows
	private String getContent(Map<String, Object> map, String templatePath) {
		try (InputStream inputStream = ResourceUtil.getResource(templatePath).getInputStream()) {
			String template = new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
			return TemplateUtil.getContent(template, map);
		}
	}

	private List<Template> getTemplate() {
		return List.of(Template.DO);
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
		templates.add(Template.DO);
		templates.add(Template.CO);
		templates.add(Template.GATEWAY);
		templates.add(Template.GATEWAY_IMPL);
		templates.add(Template.CONTROLLER);
		templates.add(Template.MAPPER);
		templates.add(Template.MAPPER_XML);
		templates.add(Template.API);
		templates.add(Template.VIEW);
		templates.add(Template.FORM_VIEW);
		return templates;
	}

}
