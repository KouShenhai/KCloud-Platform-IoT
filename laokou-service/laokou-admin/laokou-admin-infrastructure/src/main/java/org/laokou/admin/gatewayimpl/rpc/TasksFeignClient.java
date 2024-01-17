/*
 * Copyright (c) 2022 KCloud-Platform-Alibaba Author or Authors. All Rights Reserved.
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

package org.laokou.admin.gatewayimpl.rpc;

import org.laokou.admin.dto.resource.*;
import org.laokou.admin.dto.resource.clientobject.AssigneeCO;
import org.laokou.admin.dto.resource.clientobject.AuditCO;
import org.laokou.admin.dto.resource.clientobject.StartCO;
import org.laokou.admin.dto.resource.clientobject.TaskCO;
import org.laokou.admin.gatewayimpl.rpc.factory.TasksFeignClientFallbackFactory;
import org.laokou.common.i18n.dto.Datas;
import org.laokou.common.i18n.dto.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import static org.laokou.common.i18n.common.OpenFeignConstants.LAOKOU_FLOWABLE_SERVICE;

/**
 * @author laokou
 */
@FeignClient(contextId = "tasks", name = LAOKOU_FLOWABLE_SERVICE, path = "v1/tasks",
		fallbackFactory = TasksFeignClientFallbackFactory.class)
public interface TasksFeignClient {

	/**
	 * 查询任务列表.
	 * @param qry 查询参数
	 * @return 任务列表
	 */
	@PostMapping("list")
	Result<Datas<TaskCO>> list(@RequestBody TaskListQry qry);

	/**
	 * 审批任务.
	 * @param cmd 审批参数
	 * @return 审批任务结果
	 */
	@PostMapping("audit")
	Result<AuditCO> audit(@RequestBody TaskAuditCmd cmd);

	/**
	 * 处理任务.
	 * @param cmd 处理参数
	 * @return 处理任务结果
	 */
	@PostMapping("resolve")
	Result<Boolean> resolve(@RequestBody TaskResolveCmd cmd);

	/**
	 * 开始任务.
	 * @param cmd 参数
	 * @return 开始任务结果
	 */
	@PostMapping("start")
	Result<StartCO> start(@RequestBody TaskStartCmd cmd);

	/**
	 * 流程图.
	 * @param instanceId 实例ID
	 * @return 流程图
	 */
	@GetMapping("{instanceId}/diagram")
	Result<String> diagram(@PathVariable("instanceId") String instanceId);

	/**
	 * 转办任务.
	 * @param cmd 转办参数
	 * @return 转办任务结果
	 */
	@PostMapping("transfer")
	Result<Boolean> transfer(@RequestBody TaskTransferCmd cmd);

	/**
	 * 委派任务.
	 * @param cmd 委派参数
	 * @return 委派任务结果
	 */
	@PostMapping("delegate")
	Result<Boolean> delegate(@RequestBody TaskDelegateCmd cmd);

	/**
	 * 流程人员.
	 * @param instanceId 流程ID
	 * @return 操作流程人员
	 */
	@GetMapping("{instanceId}/assignee")
	Result<AssigneeCO> assignee(@PathVariable("instanceId") String instanceId);

}
