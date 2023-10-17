/*
 * Copyright (c) 2022 KCloud-Platform-Alibaba Authors. All Rights Reserved.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.laokou.admin.gatewayimpl.feign;

import io.swagger.v3.oas.annotations.Operation;
import org.laokou.admin.dto.resource.*;
import org.laokou.admin.dto.resource.clientobject.AssigneeCO;
import org.laokou.admin.dto.resource.clientobject.AuditCO;
import org.laokou.admin.dto.resource.clientobject.StartCO;
import org.laokou.admin.dto.resource.clientobject.TaskCO;
import org.laokou.admin.gatewayimpl.feign.factory.TasksFeignClientFallbackFactory;
import org.laokou.common.i18n.dto.Datas;
import org.laokou.common.i18n.dto.Result;
import org.laokou.common.openfeign.constant.ServiceConstant;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * @author laokou
 */
@FeignClient(contextId = "tasks", value = ServiceConstant.LAOKOU_FLOWABLE, path = "v1/tasks",
		fallbackFactory = TasksFeignClientFallbackFactory.class)
public interface TasksFeignClient {

	@PostMapping(value = "list")
	@Operation(summary = "流程任务", description = "查询任务列表")
	Result<Datas<TaskCO>> list(@RequestBody TaskListQry qry);

	@PostMapping(value = "audit")
	@Operation(summary = "流程任务", description = "审批任务")
	Result<AuditCO> audit(@RequestBody TaskAuditCmd cmd);

	@PostMapping(value = "resolve")
	@Operation(summary = "流程任务", description = "处理任务")
	Result<Boolean> resolve(@RequestBody TaskResolveCmd cmd);

	@PostMapping(value = "start")
	@Operation(summary = "流程任务", description = "开始任务")
	Result<StartCO> start(@RequestBody TaskStartCmd cmd);

	@GetMapping(value = "{instanceId}/diagram")
	@Operation(summary = "流程任务", description = "流程图")
	Result<String> diagram(@PathVariable("instanceId") String instanceId);

	@PostMapping("transfer")
	@Operation(summary = "流程任务", description = "转办任务")
	Result<Boolean> transfer(@RequestBody TaskTransferCmd cmd);

	@PostMapping("delegate")
	@Operation(summary = "流程任务", description = "委派任务")
	Result<Boolean> delegate(@RequestBody TaskDelegateCmd cmd);

	@GetMapping("{instanceId}/assignee")
	@Operation(summary = "流程任务", description = "流程人员")
	Result<AssigneeCO> assignee(@PathVariable("instanceId") String instanceId);

}
