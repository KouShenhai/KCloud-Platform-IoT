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

package org.laokou.flowable.server.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.laokou.common.i18n.dto.Result;
import org.laokou.flowable.dto.*;
import org.laokou.flowable.vo.AssigneeVO;
import org.laokou.flowable.vo.PageVO;
import org.laokou.flowable.vo.TaskVO;
import org.laokou.flowable.server.service.WorkTaskService;
import org.springframework.web.bind.annotation.*;
import java.io.IOException;

/**
 * @author laokou
 */
@RestController
@Tag(name = "Work Task API", description = "流程任务API")
@RequestMapping("/work/task/api")
@RequiredArgsConstructor
public class WorkTaskApiController {

	private final WorkTaskService workTaskService;

	@PostMapping(value = "/query")
	@Operation(summary = "流程任务>任务查询", description = "流程任务>任务查询")
	public Result<PageVO<TaskVO>> query(@RequestBody TaskDTO dto) {
		return Result.of(workTaskService.queryTaskPage(dto));
	}

	@PostMapping(value = "/audit")
	@Operation(summary = "流程任务>任务审批", description = "流程任务>任务审批")
	public Result<AssigneeVO> audit(@RequestBody AuditDTO dto) {
		return Result.of(workTaskService.auditTask(dto));
	}

	@PostMapping(value = "/resolve")
	@Operation(summary = "流程任务>任务处理", description = "流程任务>任务处理")
	public Result<AssigneeVO> resolve(@RequestBody ResolveDTO dto) {
		return Result.of(workTaskService.resolveTask(dto));
	}

	@PostMapping(value = "/start")
	@Operation(summary = "流程任务>任务开始", description = "流程任务>任务开始")
	public Result<AssigneeVO> start(@RequestBody ProcessDTO dto) {
		return Result.of(workTaskService.startTask(dto));
	}

	@GetMapping(value = "/diagram")
	@Operation(summary = "流程任务>任务流程", description = "流程任务>任务流程")
	public Result<String> diagram(@RequestParam("processInstanceId") String processInstanceId) throws IOException {
		return Result.of(workTaskService.diagramTask(processInstanceId));
	}

	@PostMapping("/transfer")
	@Operation(summary = "流程任务>任务转办", description = "流程任务>任务转办")
	public Result<AssigneeVO> transfer(@RequestBody TransferDTO dto) {
		return Result.of(workTaskService.transferTask(dto));
	}

	@PostMapping("/delegate")
	@Operation(summary = "流程任务>任务委派", description = "流程任务>任务委派")
	public Result<AssigneeVO> delegate(@RequestBody DelegateDTO dto) {
		return Result.of(workTaskService.delegateTask(dto));
	}

}