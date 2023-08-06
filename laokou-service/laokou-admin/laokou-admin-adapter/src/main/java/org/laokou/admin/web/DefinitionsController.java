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
import org.laokou.common.i18n.dto.Result;
import org.laokou.common.trace.annotation.TraceLog;
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

	@TraceLog
	@PostMapping("v1/definitions")
	@Operation(summary = "新增", description = "新增")
	//@OperateLog(module = "流程定义", name = "新增")
	//@PreAuthorize("hasAuthority('definitions:insert')")
	public Result<Boolean> insert(@RequestPart("file") MultipartFile file) throws IOException {
		return Result.of(null);
	}

	@TraceLog
	@PostMapping("v1/definitions/list")
	@Operation(summary = "查询", description = "查询")
	//@PreAuthorize("hasAuthority('definitions:list')")
	public Result<?> list() {
		return Result.of(null);
	}

	@TraceLog
	@GetMapping("v1/definitions/diagram/{definitionId}")
	@Operation(summary = "流程图", description = "流程图")
	//@PreAuthorize("hasAuthority('definitions:diagram')")
	public Result<String> image(@PathVariable("definitionId") String definitionId) {
		return Result.of(null);
	}

	@TraceLog
	@DeleteMapping("v1/definitions/{deploymentId}")
	@Operation(summary = "删除", description = "删除")
	//@OperateLog(module = "流程定义", name = "删除")
	//@PreAuthorize("hasAuthority('definitions:delete')")
	public Result<Boolean> delete(@PathVariable("deploymentId") String deploymentId) {
		return Result.of(null);
	}

	@TraceLog
	@PutMapping("v1/definitions/suspend/{definitionId}")
	@Operation(summary = "挂起", description = "挂起")
	//@OperateLog(module = "流程定义", name = "挂起")
	//@PreAuthorize("hasAuthority('definitions:suspend')")
	public Result<Boolean> suspend(@PathVariable("definitionId") String definitionId) {
		return Result.of(null);
	}

	@TraceLog
	@PutMapping("v1/definitions/activate/{definitionId}")
	@Operation(summary = "激活", description = "激活")
	//@OperateLog(module = "流程定义", name = "激活")
	//@PreAuthorize("hasAuthority('definitions:activate')")
	public Result<Boolean> activate(@PathVariable("definitionId") String definitionId) {
		return Result.of(null);
	}

	@TraceLog
	@GetMapping("v1/definitions/template")
	@Operation(summary = "模板", description = "模板")
	//@PreAuthorize("hasAuthority('definitions:template')")
	public void template(HttpServletResponse response) {
	}

}
