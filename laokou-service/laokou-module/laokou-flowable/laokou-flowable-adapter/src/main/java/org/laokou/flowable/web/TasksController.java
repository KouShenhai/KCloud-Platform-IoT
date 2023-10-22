/*
 * Copyright (c) 2022 KCloud-Platform-Alibaba Author or Authors. All Rights Reserved.
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
import org.laokou.common.idempotent.annotation.Idempotent;
import org.laokou.flowable.api.TasksServiceI;
import org.laokou.flowable.dto.task.*;
import org.laokou.flowable.dto.task.clientobject.AssigneeCO;
import org.laokou.flowable.dto.task.clientobject.AuditCO;
import org.laokou.flowable.dto.task.clientobject.StartCO;
import org.laokou.flowable.dto.task.clientobject.TaskCO;
import org.springframework.web.bind.annotation.*;

/**
 * @author laokou
 */
@RestController
@Tag(name = "TasksController", description = "流程任务")
@RequiredArgsConstructor
@RequestMapping("v1/tasks")
public class TasksController {

	private final TasksServiceI tasksServiceI;

	@PostMapping(value = "list")
	@Operation(summary = "流程任务", description = "查询任务列表")
	public Result<Datas<TaskCO>> list(@RequestBody TaskListQry qry) {
		return tasksServiceI.list(qry);
	}

	@Idempotent
	@PostMapping(value = "audit")
	@Operation(summary = "流程任务", description = "审批任务")
	public Result<AuditCO> audit(@RequestBody TaskAuditCmd cmd) {
		return tasksServiceI.audit(cmd);
	}

	@PostMapping(value = "resolve")
	@Operation(summary = "流程任务", description = "处理任务")
	public Result<Boolean> resolve(@RequestBody TaskResolveCmd cmd) {
		return tasksServiceI.resolve(cmd);
	}

	@PostMapping(value = "start")
	@Operation(summary = "流程任务", description = "开始任务")
	public Result<StartCO> start(@RequestBody TaskStartCmd cmd) {
		return tasksServiceI.start(cmd);
	}

	@GetMapping(value = "{instanceId}/diagram")
	@Operation(summary = "流程任务", description = "流程图")
	public Result<String> diagram(@PathVariable("instanceId") String instanceId) {
		return null;
	}

	@PostMapping("transfer")
	@Operation(summary = "流程任务", description = "转办任务")
	public Result<Boolean> transfer(@RequestBody TaskTransferCmd cmd) {
		return tasksServiceI.transfer(cmd);
	}

	@PostMapping("delegate")
	@Operation(summary = "流程任务", description = "委派任务")
	public Result<Boolean> delegate(@RequestBody TaskDelegateCmd cmd) {
		return tasksServiceI.delegate(cmd);
	}

	@GetMapping("{instanceId}/assignee")
	@Operation(summary = "流程任务", description = "流程人员")
	public Result<AssigneeCO> assignee(@PathVariable("instanceId") String instanceId) {
		return tasksServiceI.assignee(new TaskAssigneeGetQry(instanceId));
	}

}