/*
 * Copyright (c) 2022 KCloud-Platform-Alibaba Authors. All Rights Reserved.
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

import io.seata.core.context.RootContext;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.flowable.engine.TaskService;
import org.flowable.task.api.Task;
import org.laokou.common.i18n.common.GlobalException;
import org.laokou.common.i18n.dto.Result;
import org.laokou.flowable.dto.task.TaskDelegateCmd;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author laokou
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class TaskDelegateCmdExe {

	private final TaskService taskService;

	public Result<Boolean> execute(TaskDelegateCmd cmd) {
		log.info("委派流程分布式事务 XID:{}", RootContext.getXID());
		String taskId = cmd.getTaskId();
		String owner = cmd.getUserId().toString();
		String deleteReason = cmd.getToUserId().toString();
		Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
		if (task == null) {
			throw new GlobalException("任务不存在");
		}
		if (!owner.equals(task.getAssignee())) {
			throw new GlobalException("用户无权操作任务");
		}
		return Result.of(delegate(taskId, owner, deleteReason));
	}

	@Transactional(rollbackFor = Exception.class)
	public Boolean delegate(String taskId, String owner, String deleteReason) {
		taskService.setOwner(taskId, owner);
		taskService.deleteTask(taskId, deleteReason);
		return true;
	}

}
