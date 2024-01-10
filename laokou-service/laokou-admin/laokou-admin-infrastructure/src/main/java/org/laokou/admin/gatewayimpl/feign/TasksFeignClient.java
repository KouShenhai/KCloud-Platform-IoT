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
package org.laokou.admin.gatewayimpl.feign;

import org.laokou.admin.dto.resource.*;
import org.laokou.admin.dto.resource.clientobject.AssigneeCO;
import org.laokou.admin.dto.resource.clientobject.AuditCO;
import org.laokou.admin.dto.resource.clientobject.StartCO;
import org.laokou.admin.dto.resource.clientobject.TaskCO;
import org.laokou.admin.gatewayimpl.feign.factory.TasksFeignClientFallbackFactory;
import org.laokou.common.i18n.dto.Datas;
import org.laokou.common.i18n.dto.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import static org.laokou.common.i18n.common.OpenFeignConstants.SERVICE_LAOKOU_FLOWABLE;

/**
 * @author laokou
 */
@FeignClient(contextId = "tasks", name = SERVICE_LAOKOU_FLOWABLE, path = "v1/tasks",
		fallbackFactory = TasksFeignClientFallbackFactory.class)
public interface TasksFeignClient {

	/**
	 * 查询任务列表
	 * @param qry
	 * @return
	 */
	@PostMapping("list")
	Result<Datas<TaskCO>> list(@RequestBody TaskListQry qry);

	/**
	 * 审批任务
	 * @param cmd
	 * @return
	 */
	@PostMapping("audit")
	Result<AuditCO> audit(@RequestBody TaskAuditCmd cmd);

	/**
	 * 处理任务
	 * @param cmd
	 * @return
	 */
	@PostMapping("resolve")
	Result<Boolean> resolve(@RequestBody TaskResolveCmd cmd);

	/**
	 * 开始任务
	 * @param cmd
	 * @return
	 */
	@PostMapping("start")
	Result<StartCO> start(@RequestBody TaskStartCmd cmd);

	/**
	 * 流程图
	 * @param instanceId
	 * @return
	 */
	@GetMapping("{instanceId}/diagram")
	Result<String> diagram(@PathVariable("instanceId") String instanceId);

	/**
	 * 转办任务
	 * @param cmd
	 * @return
	 */
	@PostMapping("transfer")
	Result<Boolean> transfer(@RequestBody TaskTransferCmd cmd);

	/**
	 * 委派任务
	 * @param cmd
	 * @return
	 */
	@PostMapping("delegate")
	Result<Boolean> delegate(@RequestBody TaskDelegateCmd cmd);

	/**
	 * 流程人员
	 * @param instanceId
	 * @return
	 */
	@GetMapping("{instanceId}/assignee")
	Result<AssigneeCO> assignee(@PathVariable("instanceId") String instanceId);

}
