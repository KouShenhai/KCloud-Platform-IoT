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

package org.laokou.generator.web;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.laokou.common.i18n.dto.Page;
import org.laokou.common.i18n.dto.Result;
import org.laokou.common.idempotent.annotation.Idempotent;
import org.laokou.common.log.annotation.OperateLog;
import org.laokou.common.trace.annotation.TraceLog;
import org.laokou.generator.template.api.TemplatesServiceI;
import org.laokou.generator.template.dto.TemplateExportCmd;
import org.laokou.generator.template.dto.TemplateGetQry;
import org.laokou.generator.template.dto.TemplateImportCmd;
import org.laokou.generator.template.dto.TemplateModifyCmd;
import org.laokou.generator.template.dto.TemplatePageQry;
import org.laokou.generator.template.dto.TemplateRemoveCmd;
import org.laokou.generator.template.dto.TemplateSaveCmd;
import org.laokou.generator.template.dto.clientobject.TemplateCO;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/**
 *
 * 代码生成器模板管理控制器.
 *
 * @author laokou
 */
@RestController
@RequiredArgsConstructor
@Tag(name = "代码生成器模板管理", description = "代码生成器模板管理")
public class TemplatesController {

	private final TemplatesServiceI templatesServiceI;

	@Idempotent
	@PostMapping("/v1/templates")
	@PreAuthorize("hasAuthority('generator:template:save')")
	@OperateLog(module = "代码生成器模板管理", operation = "保存代码生成器模板")
	@Operation(summary = "保存代码生成器模板", description = "保存代码生成器模板")
	public void saveTemplate(@RequestBody TemplateSaveCmd cmd) {
		templatesServiceI.saveTemplate(cmd);
	}

	@PutMapping("/v1/templates")
	@PreAuthorize("hasAuthority('generator:template:modify')")
	@OperateLog(module = "代码生成器模板管理", operation = "修改代码生成器模板")
	@Operation(summary = "修改代码生成器模板", description = "修改代码生成器模板")
	public void modifyTemplate(@RequestBody TemplateModifyCmd cmd) {
		templatesServiceI.modifyTemplate(cmd);
	}

	@DeleteMapping("/v1/templates")
	@PreAuthorize("hasAuthority('generator:template:remove')")
	@OperateLog(module = "代码生成器模板管理", operation = "删除代码生成器模板")
	@Operation(summary = "删除代码生成器模板", description = "删除代码生成器模板")
	public void removeTemplate(@RequestBody Long[] ids) {
		templatesServiceI.removeTemplate(new TemplateRemoveCmd(ids));
	}

	@PostMapping(value = "/v1/templates/import", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	@PreAuthorize("hasAuthority('generator:template:import')")
	@OperateLog(module = "代码生成器模板管理", operation = "导入代码生成器模板")
	@Operation(summary = "导入代码生成器模板", description = "导入代码生成器模板")
	public void importTemplate(@RequestPart("files") MultipartFile[] files) {
		templatesServiceI.importTemplate(new TemplateImportCmd(files));
	}

	@PostMapping("/v1/templates/export")
	@PreAuthorize("hasAuthority('generator:template:export')")
	@OperateLog(module = "代码生成器模板管理", operation = "导出代码生成器模板")
	@Operation(summary = "导出代码生成器模板", description = "导出代码生成器模板")
	public void exportTemplate(@RequestBody TemplateExportCmd cmd) {
		templatesServiceI.exportTemplate(cmd);
	}

	@TraceLog
	@PostMapping("/v1/templates/page")
	@PreAuthorize("hasAuthority('generator:template:page')")
	@Operation(summary = "分页查询代码生成器模板列表", description = "分页查询代码生成器模板列表")
	public Result<Page<TemplateCO>> pageTemplate(@Validated @RequestBody TemplatePageQry qry) {
		return templatesServiceI.pageTemplate(qry);
	}

	@TraceLog
	@GetMapping("/v1/templates/{id}")
	@Operation(summary = "查看代码生成器模板详情", description = "查看代码生成器模板详情")
	public Result<TemplateCO> getTemplateById(@PathVariable("id") Long id) {
		return templatesServiceI.getTemplateById(new TemplateGetQry(id));
	}

}
