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

package org.laokou.flowable.command.task;

import com.baomidou.dynamic.datasource.annotation.DS;
import io.seata.core.context.RootContext;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.laokou.common.i18n.dto.Result;
import org.laokou.flowable.domain.gateway.TaskGateway;
import org.laokou.flowable.domain.task.Audit;
import org.laokou.flowable.dto.task.TaskAuditCmd;
import org.springframework.stereotype.Component;

import static org.laokou.common.i18n.common.DatasourceConstant.TENANT;

/**
 * 审批任务流程执行器.
 *
 * @author laokou
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class TaskAuditCmdExe {

	private final TaskGateway taskGateway;

	/**
	 * 执行审批任务流程.
	 * @param cmd 审批任务流程参数
	 */
	@DS(TENANT)
	public Result<Boolean> execute(TaskAuditCmd cmd) {
		log.info("审批流程分布式事务 XID：{}", RootContext.getXID());
		taskGateway.audit(convert(cmd));
		return Result.ok(Boolean.TRUE);
	}

	private Audit convert(TaskAuditCmd cmd) {
		return Audit.builder().values(cmd.getValues()).taskId(cmd.getTaskId()).build();
	}

}
