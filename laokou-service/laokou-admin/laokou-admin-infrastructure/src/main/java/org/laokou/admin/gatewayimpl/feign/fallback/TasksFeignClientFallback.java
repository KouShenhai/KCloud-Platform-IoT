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

package org.laokou.admin.gatewayimpl.feign.fallback;

import org.laokou.admin.dto.resource.*;
import org.laokou.admin.dto.resource.clientobject.AssigneeCO;
import org.laokou.admin.dto.resource.clientobject.AuditCO;
import org.laokou.admin.dto.resource.clientobject.StartCO;
import org.laokou.admin.dto.resource.clientobject.TaskCO;
import org.laokou.admin.gatewayimpl.feign.TasksFeignClient;
import org.laokou.common.i18n.common.exception.FeignException;
import org.laokou.common.i18n.dto.Datas;
import org.laokou.common.i18n.dto.Result;

import static org.laokou.common.i18n.common.Constant.EMPTY;

/**
 * @author laokou
 */
public class TasksFeignClientFallback implements TasksFeignClient {

	@Override
	public Result<Datas<TaskCO>> list(TaskListQry qry) {
		return Result.of(Datas.of());
	}

	@Override
	public Result<AuditCO> audit(TaskAuditCmd cmd) {
		throw new FeignException("审批流程失败");
	}

	@Override
	public Result<Boolean> resolve(TaskResolveCmd cmd) {
		throw new FeignException("处理流程失败");
	}

	@Override
	public Result<StartCO> start(TaskStartCmd cmd) {
		throw new FeignException("启动流程失败");
	}

	@Override
	public Result<String> diagram(String instanceId) {
		return Result.of(EMPTY);
	}

	@Override
	public Result<Boolean> transfer(TaskTransferCmd cmd) {
		throw new FeignException("转办流程失败");
	}

	@Override
	public Result<Boolean> delegate(TaskDelegateCmd cmd) {
		throw new FeignException("委派流程失败");
	}

	@Override
	public Result<AssigneeCO> assignee(String instanceId) {
		throw new FeignException("获取流程人员失败");
	}

}
