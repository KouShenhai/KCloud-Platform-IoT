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

import com.baomidou.dynamic.datasource.toolkit.DynamicDataSourceContextHolder;
import io.seata.core.context.RootContext;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.flowable.engine.TaskService;
import org.flowable.task.api.DelegationState;
import org.flowable.task.api.Task;
import org.laokou.common.i18n.common.StatusCode;
import org.laokou.common.i18n.utils.LogUtil;
import org.laokou.common.i18n.utils.ObjectUtil;
import org.laokou.common.i18n.common.exception.SystemException;
import org.laokou.common.i18n.dto.Result;
import org.laokou.common.mybatisplus.utils.TransactionalUtil;
import org.laokou.common.security.utils.UserUtil;
import org.laokou.flowable.dto.task.TaskResolveCmd;
import org.springframework.stereotype.Component;

import static org.laokou.common.i18n.common.DatasourceConstant.FLOWABLE;

/**
 * 处理任务流程执行器.
 *
 * @author laokou
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class TaskResolveCmdExe {

	private final TaskService taskService;

	private final TransactionalUtil transactionalUtil;

	/**
	 * 执行处理任务流程.
	 * @param cmd 处理任务流程参数
	 * @return 执行结果
	 */
	public Result<Boolean> execute(TaskResolveCmd cmd) {
		try {
			log.info("处理流程分布式事务 XID：{}", RootContext.getXID());
			String taskId = cmd.getTaskId();
			DynamicDataSourceContextHolder.push(FLOWABLE);
			Task task = taskService.createTaskQuery()
				.taskTenantId(UserUtil.getTenantId().toString())
				.taskId(taskId)
				.singleResult();
			if (ObjectUtil.isNull(task)) {
				throw new RuntimeException("任务不存在");
			}
			if (DelegationState.RESOLVED.equals(task.getDelegationState())) {
				throw new RuntimeException("非处理任务，请审批任务");
			}
			return Result.ok(resolve(taskId));
		}
		finally {
			DynamicDataSourceContextHolder.clear();
		}
	}

	/**
	 * 处理任务流程.
	 * @param taskId 任务ID
	 * @return 处理结果
	 */
	private Boolean resolve(String taskId) {
		return transactionalUtil.defaultExecute(r -> {
			try {
				taskService.resolveTask(taskId);
				return true;
			}
			catch (Exception e) {
				log.error("错误信息：{}，详情见日志", LogUtil.record(e.getMessage()), e);
				r.setRollbackOnly();
				throw new SystemException(StatusCode.INTERNAL_SERVER_ERROR, LogUtil.except(e.getMessage()));
			}
		});
	}

}
