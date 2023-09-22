/*
 * Copyright (c) 2022 KCloud-Platform-Alibaba Authors. All Rights Reserved.
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
package org.laokou.admin.web;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.laokou.admin.api.DefinitionsServiceI;
import org.laokou.admin.dto.definition.*;
import org.laokou.admin.dto.definition.clientobject.DefinitionCO;
import org.laokou.admin.domain.annotation.OperateLog;
import org.laokou.common.i18n.dto.Datas;
import org.laokou.common.i18n.dto.Result;
import org.laokou.common.trace.annotation.TraceLog;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

/**
 * @author laokou
 */
@RestController
@Tag(name = "DefinitionsController", description = "流程定义")
@RequiredArgsConstructor
@RequestMapping("v1/definitions")
public class DefinitionsController {

	private final DefinitionsServiceI definitionsServiceI;

	@TraceLog
	@PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	@Operation(summary = "流程定义", description = "新增流程")
	@OperateLog(module = "流程定义", operation = "新增流程")
	@PreAuthorize("hasAuthority('definitions:insert')")
	public Result<Boolean> insert(@RequestPart("file") MultipartFile file) throws IOException {
		return definitionsServiceI.insert(new DefinitionInsertCmd(file));
	}

	@TraceLog
	@PostMapping("list")
	@Operation(summary = "流程定义", description = "查询流程列表")
	@PreAuthorize("hasAuthority('definitions:list')")
	public Result<Datas<DefinitionCO>> list(@RequestBody DefinitionListQry qry) {
		return definitionsServiceI.list(qry);
	}

	@TraceLog
	@GetMapping("{definitionId}/diagram")
	@Operation(summary = "流程定义", description = "流程图")
	@PreAuthorize("hasAuthority('definitions:diagram')")
	public Result<String> diagram(@PathVariable("definitionId") String definitionId) {
		return definitionsServiceI.diagram(new DefinitionDiagramGetQry(definitionId));
	}

	@TraceLog
	@DeleteMapping("{deploymentId}")
	@Operation(summary = "流程定义", description = "删除流程")
	@OperateLog(module = "流程定义", operation = "删除流程")
	@PreAuthorize("hasAuthority('definitions:delete')")
	public Result<Boolean> delete(@PathVariable("deploymentId") String deploymentId) {
		return definitionsServiceI.delete(new DefinitionDeleteCmd(deploymentId));
	}

	@TraceLog
	@PutMapping("{definitionId}/suspend")
	@Operation(summary = "流程定义", description = "挂起流程")
	@OperateLog(module = "流程定义", operation = "挂起流程")
	@PreAuthorize("hasAuthority('definitions:suspend')")
	public Result<Boolean> suspend(@PathVariable("definitionId") String definitionId) {
		return definitionsServiceI.suspend(new DefinitionSuspendCmd(definitionId));
	}

	@TraceLog
	@PutMapping("{definitionId}/activate")
	@Operation(summary = "流程定义", description = "激活流程")
	@OperateLog(module = "流程定义", operation = "激活流程")
	@PreAuthorize("hasAuthority('definitions:activate')")
	public Result<Boolean> activate(@PathVariable("definitionId") String definitionId) {
		return definitionsServiceI.activate(new DefinitionActiveCmd(definitionId));
	}

	@GetMapping("template")
	@Operation(summary = "流程定义", description = "流程模板")
	@PreAuthorize("hasAuthority('definitions:template')")
	public void template(HttpServletResponse response) {
		definitionsServiceI.template(new DefinitionTemplateCmd(response));
	}

}
