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
import org.flowable.engine.RepositoryService;
import org.flowable.engine.RuntimeService;
import org.flowable.engine.repository.ProcessDefinition;
import org.flowable.engine.runtime.ProcessInstance;
import org.laokou.common.i18n.utils.LogUtil;
import org.laokou.common.i18n.utils.ObjectUtil;
import org.laokou.common.i18n.common.exception.FlowException;
import org.laokou.common.i18n.common.exception.SystemException;
import org.laokou.common.i18n.dto.Result;
import org.laokou.common.mybatisplus.utils.TransactionalUtil;
import org.laokou.flowable.dto.task.TaskStartCmd;
import org.laokou.flowable.dto.task.clientobject.StartCO;
import org.springframework.stereotype.Component;

import static org.laokou.flowable.common.Constant.FLOWABLE;

/**
 * @author laokou
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class TaskStartCmdExe {

	private final RuntimeService runtimeService;

	private final RepositoryService repositoryService;

	private final TransactionalUtil transactionalUtil;

	public Result<StartCO> execute(TaskStartCmd cmd) {
		try {
			log.info("开始流程分布式事务 XID：{}", RootContext.getXID());
			String definitionKey = cmd.getDefinitionKey();
			String instanceName = cmd.getInstanceName();
			String businessKey = cmd.getBusinessKey();
			DynamicDataSourceContextHolder.push(FLOWABLE);
			ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery()
				.processDefinitionKey(definitionKey)
				.latestVersion()
				.singleResult();
			if (ObjectUtil.isNull(processDefinition)) {
				throw new FlowException("流程未定义");
			}
			if (processDefinition.isSuspended()) {
				throw new FlowException("流程已被挂起");
			}
			return Result.of(start(definitionKey, businessKey, instanceName));
		}
		finally {
			DynamicDataSourceContextHolder.clear();
		}
	}

	private StartCO start(String definitionKey, String businessKey, String instanceName) {
		return transactionalUtil.defaultExecute(r -> {
			try {
				ProcessInstance processInstance = runtimeService.startProcessInstanceByKey(definitionKey, businessKey);
				if (ObjectUtil.isNull(processInstance)) {
					throw new FlowException("流程不存在");
				}
				String instanceId = processInstance.getId();
				runtimeService.setProcessInstanceName(instanceId, instanceName);
				return new StartCO(instanceId);
			}
			catch (Exception e) {
				log.error("错误信息：{}，详情见日志", LogUtil.error(e.getMessage()), e);
				r.setRollbackOnly();
				throw new SystemException(e.getMessage());
			}
		});
	}

}
