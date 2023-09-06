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
import org.laokou.admin.client.api.DefinitionsServiceI;
import org.laokou.admin.domain.annotation.OperateLog;
import org.laokou.common.i18n.dto.Result;
import org.laokou.common.trace.annotation.TraceLog;
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
public class DefinitionsController {

	private final DefinitionsServiceI definitionsServiceI;

	@TraceLog
	@PostMapping("v1/definitions")
	@Operation(summary = "流程定义", description = "流程新增")
	@OperateLog(module = "流程定义", operation = "流程新增")
	@PreAuthorize("hasAuthority('definitions:insert')")
	public Result<Boolean> insert(@RequestPart("file") MultipartFile file) throws IOException {
		return Result.of(null);
	}

	@TraceLog
	@PostMapping("v1/definitions/list")
	@Operation(summary = "流程定义", description = "流程查询列表")
	@PreAuthorize("hasAuthority('definitions:list')")
	public Result<?> list() {
		return Result.of(null);
	}

	@TraceLog
	@GetMapping("v1/definitions/diagram/{definitionId}")
	@Operation(summary = "流程定义", description = "流程图")
	@PreAuthorize("hasAuthority('definitions:diagram')")
	public Result<String> diagram(@PathVariable("definitionId") String definitionId) {
		return Result.of(null);
	}

	@TraceLog
	@DeleteMapping("v1/definitions/{deploymentId}")
	@Operation(summary = "流程定义", description = "流程删除")
	@OperateLog(module = "流程定义", operation = "流程删除")
	@PreAuthorize("hasAuthority('definitions:delete')")
	public Result<Boolean> delete(@PathVariable("deploymentId") String deploymentId) {
		return Result.of(null);
	}

	@TraceLog
	@PutMapping("v1/definitions/suspend/{definitionId}")
	@Operation(summary = "流程定义", description = "流程挂起")
	@OperateLog(module = "流程定义", operation = "流程挂起")
	@PreAuthorize("hasAuthority('definitions:suspend')")
	public Result<Boolean> suspend(@PathVariable("definitionId") String definitionId) {
		return Result.of(null);
	}

	@TraceLog
	@PutMapping("v1/definitions/activate/{definitionId}")
	@Operation(summary = "流程定义", description = "流程激活")
	@OperateLog(module = "流程定义", operation = "流程激活")
	@PreAuthorize("hasAuthority('definitions:activate')")
	public Result<Boolean> activate(@PathVariable("definitionId") String definitionId) {
		return Result.of(null);
	}

	@TraceLog
	@GetMapping("v1/definitions/template")
	@Operation(summary = "流程定义", description = "流程模板")
	@PreAuthorize("hasAuthority('definitions:template')")
	public void template(HttpServletResponse response) {

	}

}
