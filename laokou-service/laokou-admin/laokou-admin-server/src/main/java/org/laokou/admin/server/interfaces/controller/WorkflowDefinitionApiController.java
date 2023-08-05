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
package org.laokou.admin.server.interfaces.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.laokou.admin.server.application.service.WorkflowDefinitionApplicationService;
import org.laokou.common.i18n.dto.Result;
import org.laokou.common.log.annotation.OperateLog;
import org.laokou.admin.server.interfaces.qo.DefinitionQo;
import org.laokou.common.trace.annotation.TraceLog;
import org.laokou.flowable.client.vo.DefinitionVO;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;

/**
 * @author laokou
 */
@RestController
@Tag(name = "Workflow Definition API", description = "流程定义API")
@RequestMapping("/workflow/definition/api")
@RequiredArgsConstructor
public class WorkflowDefinitionApiController {

	private final WorkflowDefinitionApplicationService workflowDefinitionApplicationService;

	@TraceLog
	@PostMapping("/insert")
	@Operation(summary = "流程定义>新增", description = "流程定义>新增")
	@OperateLog(module = "流程定义", name = "流程新增")
	@PreAuthorize("hasAuthority('workflow:definition:insert')")
	public Result<Boolean> insert(@RequestPart("file") MultipartFile file) throws IOException {
		return Result.of(workflowDefinitionApplicationService.insertDefinition(file));
	}

	@TraceLog
	@PostMapping("/query")
	@Operation(summary = "流程定义>查询", description = "流程定义>查询")
	@PreAuthorize("hasAuthority('workflow:definition:query')")
	public Result<IPage<DefinitionVO>> query(@RequestBody DefinitionQo qo) {
		return Result.of(workflowDefinitionApplicationService.queryDefinitionPage(qo));
	}

	@TraceLog
	@GetMapping("/image")
	@Operation(summary = "流程定义>图片", description = "流程定义>图片")
	@PreAuthorize("hasAuthority('workflow:definition:diagram')")
	public Result<String> image(@RequestParam("definitionId") String definitionId) {
		return Result.of(workflowDefinitionApplicationService.diagramDefinition(definitionId));
	}

	@TraceLog
	@DeleteMapping("/delete")
	@Operation(summary = "流程定义>删除", description = "流程定义>删除")
	@OperateLog(module = "流程定义", name = "流程删除")
	@PreAuthorize("hasAuthority('workflow:definition:delete')")
	public Result<Boolean> delete(@RequestParam("deploymentId") String deploymentId) {
		return Result.of(workflowDefinitionApplicationService.deleteDefinition(deploymentId));
	}

	@TraceLog
	@PutMapping("/suspend")
	@Operation(summary = "流程定义>挂起", description = "流程定义>挂起")
	@OperateLog(module = "流程定义", name = "流程挂起")
	@PreAuthorize("hasAuthority('workflow:definition:suspend')")
	public Result<Boolean> suspend(@RequestParam("definitionId") String definitionId) {
		return Result.of(workflowDefinitionApplicationService.suspendDefinition(definitionId));
	}

	@TraceLog
	@PutMapping("/activate")
	@Operation(summary = "流程定义>激活", description = "流程定义>激活")
	@OperateLog(module = "流程定义", name = "流程激活")
	@PreAuthorize("hasAuthority('workflow:definition:activate')")
	public Result<Boolean> activate(@RequestParam("definitionId") String definitionId) {
		return Result.of(workflowDefinitionApplicationService.activateDefinition(definitionId));
	}

	@TraceLog
	@GetMapping("/template")
	@Operation(summary = "流程定义>模板", description = "流程定义>模板")
	@PreAuthorize("hasAuthority('workflow:definition:template')")
	public void template(HttpServletResponse response) throws IOException {
		workflowDefinitionApplicationService.downloadTemplate(response);
	}

}
