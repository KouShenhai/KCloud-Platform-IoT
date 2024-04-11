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
import org.flowable.bpmn.converter.BpmnXMLConverter;
import org.flowable.bpmn.model.BpmnModel;
import org.flowable.bpmn.model.Process;
import org.flowable.common.engine.impl.util.io.InputStreamSource;
import org.flowable.engine.RepositoryService;
import org.flowable.engine.repository.ProcessDefinition;
import org.laokou.common.i18n.utils.LogUtil;
import org.laokou.common.mybatisplus.utils.TransactionalUtil;
import org.laokou.common.security.utils.UserUtil;
import org.laokou.flowable.domain.definition.Activate;
import org.laokou.flowable.domain.definition.Deployment;
import org.laokou.flowable.domain.definition.Suspend;
import org.laokou.flowable.domain.gateway.DefinitionGateway;
import org.springframework.stereotype.Component;

/**
 * @author laokou
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class DefinitionGatewayImpl implements DefinitionGateway {

	private final TransactionalUtil transactionalUtil;

	private final RepositoryService repositoryService;

	@Override
	public void create(Deployment deployment) {
		BpmnXMLConverter converter = new BpmnXMLConverter();
		InputStreamSource inputStreamSource = new InputStreamSource(deployment.getInputStream());
		BpmnModel bpmnModel = converter.convertToBpmnModel(inputStreamSource, true, true);
		Process process = bpmnModel.getProcesses().stream().findFirst().orElse(new Process());
		deployment.modify(process.getId(), process.getName());
		long count = repositoryService.createDeploymentQuery()
			.deploymentTenantId(UserUtil.getTenantId().toString())
			.deploymentKey(deployment.getKey())
			.count();
		deployment.checkKey(count);
		create(deployment, bpmnModel);
	}

	@Override
	public void remove(String deploymentId) {
		transactionalUtil.defaultExecuteWithoutResult(r -> {
			try {
				// true允许级联删除 不设置会导致数据库关联异常
				repositoryService.deleteDeployment(deploymentId, true);
			}
			catch (Exception e) {
				log.error("错误信息：{}，详情见日志", LogUtil.result(e.getMessage()), e);
				r.setRollbackOnly();
				throw new RuntimeException(LogUtil.fail(e.getMessage()));
			}
		});
	}

	@Override
	public void activate(Activate activate) {
		String definitionId = activate.getDefinitionId();
		ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery()
			.processDefinitionTenantId(UserUtil.getTenantId().toString())
			.processDefinitionId(definitionId)
			.singleResult();
		activate.checkActivated(!processDefinition.isSuspended());
		activate(definitionId);
	}

	@Override
	public void suspend(Suspend suspend) {
		String definitionId = suspend.getDefinitionId();
		ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery()
			.processDefinitionTenantId(UserUtil.getTenantId().toString())
			.processDefinitionId(definitionId)
			.singleResult();
		suspend.checkSuspended(processDefinition.isSuspended());
		suspend(definitionId);
	}

	/**
	 * 激活流程.
	 * @param definitionId 定义ID
	 */
	private void activate(String definitionId) {
		transactionalUtil.defaultExecuteWithoutResult(r -> {
			try {
				// 激活
				repositoryService.activateProcessDefinitionById(definitionId, true, null);
			}
			catch (Exception e) {
				log.error("错误信息：{}，详情见日志", LogUtil.result(e.getMessage()), e);
				r.setRollbackOnly();
				throw new RuntimeException(LogUtil.fail(e.getMessage()));
			}
		});
	}

	/**
	 * 挂起流程.
	 * @param definitionId 定义ID
	 */
	private void suspend(String definitionId) {
		transactionalUtil.defaultExecuteWithoutResult(r -> {
			try {
				// 挂起
				repositoryService.suspendProcessDefinitionById(definitionId, true, null);
			}
			catch (Exception e) {
				log.error("错误信息：{}，详情见日志", LogUtil.result(e.getMessage()), e);
				r.setRollbackOnly();
				throw new RuntimeException(LogUtil.fail(e.getMessage()));
			}
		});
	}

	/**
	 * 部署流程.
	 * @param deployment 部署
	 * @param bpmnModel 模型
	 */
	private void create(Deployment deployment, BpmnModel bpmnModel) {
		transactionalUtil.defaultExecuteWithoutResult(r -> {
			try {
				repositoryService.createDeployment()
					.tenantId(UserUtil.getTenantId().toString())
					.name(deployment.getName())
					.key(deployment.getKey())
					.addBpmnModel(deployment.getName(), bpmnModel)
					.deploy();
			}
			catch (Exception e) {
				log.error("错误信息：{}，详情见日志", LogUtil.result(e.getMessage()), e);
				r.setRollbackOnly();
				throw new RuntimeException(LogUtil.fail(e.getMessage()));
			}
		});
	}

}
