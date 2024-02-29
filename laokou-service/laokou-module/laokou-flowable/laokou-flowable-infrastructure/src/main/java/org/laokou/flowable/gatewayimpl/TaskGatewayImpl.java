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

package org.laokou.flowable.gatewayimpl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.flowable.engine.RepositoryService;
import org.flowable.engine.RuntimeService;
import org.flowable.engine.TaskService;
import org.flowable.engine.repository.ProcessDefinition;
import org.flowable.engine.runtime.ProcessInstance;
import org.flowable.task.api.DelegationState;
import org.flowable.task.api.Task;
import org.laokou.common.core.utils.MapUtil;
import org.laokou.common.i18n.common.exception.SystemException;
import org.laokou.common.i18n.utils.LogUtil;
import org.laokou.common.mybatisplus.utils.TransactionalUtil;
import org.laokou.common.security.utils.UserUtil;
import org.laokou.flowable.domain.gateway.TaskGateway;
import org.laokou.flowable.domain.task.Audit;
import org.laokou.flowable.domain.task.Start;
import org.springframework.stereotype.Component;

/**
 * @author laokou
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class TaskGatewayImpl implements TaskGateway {

	private final TransactionalUtil transactionalUtil;

	private final RuntimeService runtimeService;

	private final RepositoryService repositoryService;

	private final TaskService taskService;

	@Override
	public void start(Start start) {
		ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery()
			.processDefinitionTenantId(UserUtil.getTenantId().toString())
			.processDefinitionKey(start.getDefinitionKey())
			.latestVersion()
			.singleResult();
		start.checkDefinition(processDefinition);
		start.checkSuspended(processDefinition.isSuspended());
		startInstance(start);
	}

	@Override
	public void audit(Audit audit) {
		Task task = taskService.createTaskQuery()
			.taskTenantId(UserUtil.getTenantId().toString())
			.taskId(audit.getTaskId())
			.singleResult();
		audit.checkTask(task);
		audit.checkPending(DelegationState.PENDING.equals(task.getDelegationState()));
		complete(audit);
	}

	private void complete(Audit audit) {
		transactionalUtil.defaultExecuteWithoutResult(r -> {
			try {
				if (MapUtil.isNotEmpty(audit.getValues())) {
					taskService.complete(audit.getTaskId(), audit.getValues());
				}
				else {
					taskService.complete(audit.getTaskId());
				}
			}
			catch (Exception e) {
				String msg = e.getMessage();
				log.error("错误信息：{}，详情见日志", LogUtil.result(msg), e);
				r.setRollbackOnly();
				throw new SystemException(LogUtil.fail(msg));
			}
		});
	}

	private void startInstance(Start start) {
		transactionalUtil.defaultExecuteWithoutResult(r -> {
			try {
				ProcessInstance processInstance = runtimeService.startProcessInstanceByKeyAndTenantId(
						start.getDefinitionKey(), start.getBusinessKey(), UserUtil.getTenantId().toString());
				start.checkInstance(processInstance);
				runtimeService.setProcessInstanceName(processInstance.getId(), start.getInstanceName());
			}
			catch (Exception e) {
				String msg = e.getMessage();
				log.error("错误信息：{}，详情见日志", LogUtil.result(msg), e);
				r.setRollbackOnly();
				throw new SystemException(LogUtil.fail(msg));
			}
		});
	}

}
