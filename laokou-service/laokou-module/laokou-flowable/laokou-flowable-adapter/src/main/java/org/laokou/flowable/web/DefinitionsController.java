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

package org.laokou.flowable.web;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.laokou.common.i18n.dto.Datas;
import org.laokou.common.i18n.dto.Result;
import org.laokou.flowable.api.DefinitionsServiceI;
import org.laokou.flowable.dto.definition.*;
import org.laokou.flowable.dto.definition.clientobject.DefinitionCO;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author laokou
 */
@RestController
@RequiredArgsConstructor
@Tag(name = "DefinitionsController", description = "流程定义")
@RequestMapping("v1/definitions")
public class DefinitionsController {

	private final DefinitionsServiceI definitionsServiceI;

	@PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	@Operation(summary = "流程定义", description = "新增流程")
	public Result<Boolean> insert(@RequestPart("file") MultipartFile file) {
		return definitionsServiceI.insert(new DefinitionInsertCmd(file));
	}

	@PostMapping(value = "list")
	@Operation(summary = "流程定义", description = "查询流程列表")
	public Result<Datas<DefinitionCO>> list(@RequestBody DefinitionListQry qry) {
		return definitionsServiceI.list(qry);
	}

	@GetMapping(value = "{definitionId}/diagram")
	@Operation(summary = "流程定义", description = "流程图")
	public Result<String> diagram(@PathVariable("definitionId") String definitionId) {
		return definitionsServiceI.diagram(new DefinitionDiagramGetQry(definitionId));
	}

	@DeleteMapping(value = "{deploymentId}")
	@Operation(summary = "流程定义", description = "删除流程")
	public Result<Boolean> delete(@PathVariable("deploymentId") String deploymentId) {
		return definitionsServiceI.delete(new DefinitionDeleteCmd(deploymentId));
	}

	@PutMapping(value = "{definitionId}/suspend")
	@Operation(summary = "流程定义", description = "挂起流程")
	public Result<Boolean> suspend(@PathVariable("definitionId") String definitionId) {
		return definitionsServiceI.suspend(new DefinitionSuspendCmd(definitionId));
	}

	@PutMapping(value = "{definitionId}/activate")
	@Operation(summary = "流程定义", description = "激活流程")
	public Result<Boolean> activate(@PathVariable("definitionId") String definitionId) {
		return definitionsServiceI.activate(new DefinitionActivateCmd(definitionId));
	}

}