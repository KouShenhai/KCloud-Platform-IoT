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
import org.laokou.common.i18n.common.exception.FlowException;
import org.laokou.common.i18n.common.exception.SystemException;
import org.laokou.common.i18n.dto.Result;
import org.laokou.common.mybatisplus.utils.TransactionalUtil;
import org.laokou.flowable.dto.task.TaskResolveCmd;
import org.springframework.stereotype.Component;

import java.util.Objects;

import static org.laokou.flowable.common.Constant.FLOWABLE;

/**
 * @author laokou
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class TaskResolveCmdExe {

	private final TaskService taskService;

	private final TransactionalUtil transactionalUtil;

	public Result<Boolean> execute(TaskResolveCmd cmd) {
		try {
			log.info("处理流程分布式事务 XID:{}", RootContext.getXID());
			String taskId = cmd.getTaskId();
			DynamicDataSourceContextHolder.push(FLOWABLE);
			Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
			if (Objects.isNull(task)) {
				throw new FlowException("任务不存在");
			}
			if (DelegationState.RESOLVED.equals(task.getDelegationState())) {
				throw new FlowException("非处理任务，请审批任务");
			}
			return Result.of(resolve(taskId));
		}
		finally {
			DynamicDataSourceContextHolder.clear();
		}
	}

	private Boolean resolve(String taskId) {
		return transactionalUtil.defaultExecute(r -> {
			try {
				taskService.resolveTask(taskId);
				return true;
			}
			catch (Exception e) {
				log.error("错误信息：{}", e.getMessage());
				r.setRollbackOnly();
				throw new SystemException(e.getMessage());
			}
		});
	}

}
