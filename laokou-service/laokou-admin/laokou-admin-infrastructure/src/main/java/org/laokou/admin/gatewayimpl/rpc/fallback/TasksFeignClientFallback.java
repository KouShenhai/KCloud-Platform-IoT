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

import org.laokou.admin.dto.resource.TaskAuditCmd;
import org.laokou.admin.dto.resource.TaskStartCmd;
import org.laokou.admin.gatewayimpl.rpc.TasksFeignClient;
import org.laokou.common.i18n.common.exception.FeignException;
import org.laokou.common.i18n.dto.Result;

/**
 * 任务流程.
 *
 * @author laokou
 */
public class TasksFeignClientFallback implements TasksFeignClient {

	/**
	 * 审批任务流程.
	 * @param cmd 审批任务流程参数
	 * @return 审批结果
	 */
	@Override
	public Result<Boolean> audit(TaskAuditCmd cmd) {
		throw new FeignException("审批流程失败");
	}

	/**
	 * 开始任务流程.
	 * @param cmd 开始任务流程参数
	 * @return 开始结果
	 */
	@Override
	public Result<Boolean> start(TaskStartCmd cmd) {
		throw new FeignException("启动流程失败");
	}

}
