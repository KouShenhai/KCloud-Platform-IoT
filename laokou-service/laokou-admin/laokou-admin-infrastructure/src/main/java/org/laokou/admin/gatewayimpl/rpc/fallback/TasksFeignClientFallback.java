/*
 * Copyright (c) 2022-2024 KCloud-Platform-Alibaba Author or Authors. All Rights Reserved.
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

package org.laokou.admin.gatewayimpl.rpc.fallback;

import org.laokou.admin.dto.resource.*;
import org.laokou.admin.dto.resource.clientobject.AssigneeCO;
import org.laokou.admin.dto.resource.clientobject.AuditCO;
import org.laokou.admin.dto.resource.clientobject.StartCO;
import org.laokou.admin.dto.resource.clientobject.TaskCO;
import org.laokou.admin.gatewayimpl.rpc.TasksFeignClient;
import org.laokou.common.i18n.common.exception.FeignException;
import org.laokou.common.i18n.dto.Datas;
import org.laokou.common.i18n.dto.Result;

import static org.laokou.common.i18n.common.StringConstants.EMPTY;

/**
 * 任务流程.
 *
 * @author laokou
 */
public class TasksFeignClientFallback implements TasksFeignClient {

	/**
	 * 查询任务流程列表.
	 * @param qry 查询任务流程列表参数
	 * @return 任务流程列表
	 */
	@Override
	public Result<Datas<TaskCO>> list(TaskListQry qry) {
		return Result.of(Datas.of());
	}

	/**
	 * 审批任务流程.
	 * @param cmd 审批任务流程参数
	 * @return 审批结果
	 */
	@Override
	public Result<AuditCO> audit(TaskAuditCmd cmd) {
		throw new FeignException("审批流程失败");
	}

	/**
	 * 处理任务流程.
	 * @param cmd 处理任务流程参数
	 * @return 处理结果
	 */
	@Override
	public Result<Boolean> resolve(TaskResolveCmd cmd) {
		throw new FeignException("处理流程失败");
	}

	/**
	 * 开始任务流程.
	 * @param cmd 开始任务流程参数
	 * @return 开始结果
	 */
	@Override
	public Result<StartCO> start(TaskStartCmd cmd) {
		throw new FeignException("启动流程失败");
	}

	/**
	 * 查看流程图.
	 * @param instanceId 实例ID
	 * @return 流程图
	 */
	@Override
	public Result<String> diagram(String instanceId) {
		return Result.of(EMPTY);
	}

	/**
	 * 转办任务流程.
	 * @param cmd 转办任务流程参数
	 * @return 转办结果
	 */
	@Override
	public Result<Boolean> transfer(TaskTransferCmd cmd) {
		throw new FeignException("转办流程失败");
	}

	/**
	 * 委派任务流程.
	 * @param cmd 委派任务流程参数
	 * @return 委派结果
	 */
	@Override
	public Result<Boolean> delegate(TaskDelegateCmd cmd) {
		throw new FeignException("委派流程失败");
	}

	/**
	 * 查看流程人员ID.
	 * @param instanceId 流程ID
	 * @return 流程人员ID
	 */
	@Override
	public Result<AssigneeCO> assignee(String instanceId) {
		throw new FeignException("获取流程人员失败");
	}

}
