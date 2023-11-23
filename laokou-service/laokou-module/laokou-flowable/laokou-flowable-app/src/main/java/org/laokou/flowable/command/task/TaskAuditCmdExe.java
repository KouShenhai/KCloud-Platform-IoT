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

package org.laokou.flowable.command.task;

import com.baomidou.dynamic.datasource.toolkit.DynamicDataSourceContextHolder;
import io.seata.core.context.RootContext;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.flowable.engine.TaskService;
import org.flowable.task.api.DelegationState;
import org.flowable.task.api.Task;
import org.laokou.common.core.utils.MapUtil;
import org.laokou.common.i18n.common.exception.FlowException;
import org.laokou.common.i18n.common.exception.SystemException;
import org.laokou.common.i18n.dto.Result;
import org.laokou.common.mybatisplus.utils.TransactionalUtil;
import org.laokou.flowable.dto.task.TaskAuditCmd;
import org.laokou.flowable.dto.task.clientobject.AuditCO;
import org.laokou.flowable.gatewayimpl.database.TaskMapper;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Objects;

import static org.laokou.flowable.common.Constant.FLOWABLE;

/**
 * @author laokou
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class TaskAuditCmdExe {

	private final TaskService taskService;

	private final TaskMapper taskMapper;

	private final TransactionalUtil transactionalUtil;

	public Result<AuditCO> execute(TaskAuditCmd cmd) {
		try {
			log.info("审批流程分布式事务 XID:{}", RootContext.getXID());
			String taskId = cmd.getTaskId();
			Map<String, Object> values = cmd.getValues();
			String instanceId = cmd.getInstanceId();
			DynamicDataSourceContextHolder.push(FLOWABLE);
			Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
			if (Objects.isNull(task)) {
				throw new FlowException("任务不存在");
			}
			if (DelegationState.PENDING.equals(task.getDelegationState())) {
				throw new FlowException("非审批任务，请处理任务");
			}
			// 审批
			audit(taskId, values);
			return Result.of(new AuditCO(taskMapper.getAssigneeByInstanceId(instanceId)));
		}
		finally {
			DynamicDataSourceContextHolder.clear();
		}
	}

	private void audit(String taskId, Map<String, Object> values) {
		transactionalUtil.defaultExecuteWithoutResult(r -> {
			try {
				if (MapUtil.isNotEmpty(values)) {
					taskService.complete(taskId, values);
				}
				else {
					taskService.complete(taskId);
				}
			}
			catch (Exception e) {
				log.error("错误信息：{}", e.getMessage());
				r.setRollbackOnly();
				throw new SystemException(e.getMessage());
			}
		});
	}

}
