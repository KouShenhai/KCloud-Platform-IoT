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
package org.laokou.flowable.server.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.seata.core.context.RootContext;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.hc.client5.http.utils.Base64;
import org.flowable.bpmn.model.BpmnModel;
import org.flowable.engine.*;
import org.flowable.engine.history.HistoricActivityInstance;
import org.flowable.engine.history.HistoricProcessInstance;
import org.flowable.engine.repository.ProcessDefinition;
import org.flowable.engine.runtime.ProcessInstance;
import org.flowable.task.api.DelegationState;
import org.flowable.task.api.Task;
import org.laokou.common.core.utils.MapUtil;
import org.laokou.common.i18n.common.GlobalException;
import org.laokou.common.i18n.utils.ValidatorUtil;
import org.laokou.flowable.dto.*;
import org.laokou.flowable.server.mapper.TaskMapper;
import org.laokou.flowable.server.service.WorkTaskService;
import org.laokou.flowable.server.utils.TaskUtil;
import org.laokou.flowable.vo.AssigneeVO;
import org.laokou.flowable.vo.PageVO;
import org.laokou.flowable.vo.TaskVO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author laokou
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class WorkTaskServiceImpl implements WorkTaskService {

	private static final String PNG = "png";

	private final TaskService taskService;

	private final TaskUtil taskUtil;

	private final RuntimeService runtimeService;

	private final HistoryService historyService;

	private final RepositoryService repositoryService;

	private final ProcessEngine processEngine;

	private final TaskMapper taskMapper;

	@Override
	@Transactional(rollbackFor = Exception.class)
	public AssigneeVO auditTask(AuditDTO dto) {
		ValidatorUtil.validateEntity(dto);
		log.info("分布式事务 XID:{}", RootContext.getXID());
		String taskId = dto.getTaskId();
		String instanceId = dto.getInstanceId();
		Map<String, Object> values = dto.getValues();
		Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
		if (null == task) {
			throw new GlobalException("任务不存在");
		}
		if (DelegationState.PENDING.equals(task.getDelegationState())) {
			throw new GlobalException("非审批任务，请处理任务");
		}
		// 审批处理
		if (MapUtil.isNotEmpty(values)) {
			taskService.complete(taskId, values);
		}
		else {
			taskService.complete(taskId);
		}
		String assignee = taskUtil.getAssignee(instanceId);
		log.info("当前审核人：{}", assignee == null ? "无" : assignee);
		return new AssigneeVO(assignee, instanceId);
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public AssigneeVO resolveTask(ResolveDTO dto) {
		ValidatorUtil.validateEntity(dto);
		String taskId = dto.getTaskId();
		String instanceId = dto.getInstanceId();
		Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
		if (null == task) {
			throw new GlobalException("任务不存在");
		}
		if (DelegationState.PENDING.equals(task.getDelegationState())) {
			taskService.resolveTask(taskId);
		}
		else {
			throw new GlobalException("非处理任务，请审批任务");
		}
		String assignee = taskUtil.getAssignee(instanceId);
		log.info("当前审核人：{}", assignee == null ? "无" : assignee);
		return new AssigneeVO(assignee, instanceId);
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public AssigneeVO startTask(ProcessDTO dto) {
		ValidatorUtil.validateEntity(dto);
		log.info("分布式事务 XID:{}", RootContext.getXID());
		String processKey = dto.getDefinitionKey();
		String businessKey = dto.getBusinessKey();
		String businessName = dto.getBusinessName();
		ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery()
			.processDefinitionKey(processKey)
			.latestVersion()
			.singleResult();
		if (processDefinition == null) {
			throw new GlobalException("流程未定义");
		}
		if (processDefinition.isSuspended()) {
			throw new GlobalException("流程已被挂起，请先激活流程");
		}
		final ProcessInstance processInstance = runtimeService.startProcessInstanceByKey(processKey, businessKey);
		if (processInstance == null) {
			throw new GlobalException("流程不存在");
		}
		String instanceId = processInstance.getId();
		runtimeService.setProcessInstanceName(instanceId, businessName);
		String assignee = taskUtil.getAssignee(instanceId);
		log.info("当前审核人：{}", assignee == null ? "无" : assignee);
		return new AssigneeVO(assignee, instanceId);
	}

	@Override
	public PageVO<TaskVO> queryTaskPage(TaskDTO dto) {
		ValidatorUtil.validateEntity(dto);
		IPage<TaskVO> page = new Page<>(dto.getPageNum(), dto.getPageSize());
		IPage<TaskVO> takePage = taskMapper.getTakePage(page, dto);
		return new PageVO<>(takePage.getRecords(), takePage.getTotal());
	}

	@Override
	public String diagramTask(String processInstanceId) throws IOException {
		final InputStream inputStream = getInputStream(processInstanceId);
		final BufferedImage image = ImageIO.read(inputStream);
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		if (null != image) {
			ImageIO.write(image, PNG, outputStream);
		}
		return Base64.encodeBase64String(outputStream.toByteArray());
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public AssigneeVO transferTask(TransferDTO dto) {
		ValidatorUtil.validateEntity(dto);
		String owner = dto.getUserId().toString();
		String assignee = dto.getAssignee().toString();
		String instanceId = dto.getInstanceId();
		String taskId = dto.getTaskId();
		checkTask(taskId, owner);
		taskService.setOwner(taskId, owner);
		taskService.setAssignee(taskId, assignee);
		return new AssigneeVO(assignee, instanceId);
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public AssigneeVO delegateTask(DelegateDTO dto) {
		ValidatorUtil.validateEntity(dto);
		String owner = dto.getUserId().toString();
		String assignee = dto.getAssignee().toString();
		String instanceId = dto.getInstanceId();
		String taskId = dto.getTaskId();
		checkTask(taskId, owner);
		taskService.setOwner(taskId, owner);
		taskService.delegateTask(taskId, assignee);
		return new AssigneeVO(assignee, instanceId);
	}

	private void checkTask(String taskId, String owner) {
		Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
		if (task == null) {
			throw new GlobalException("任务不存在");
		}
		if (!owner.equals(task.getAssignee())) {
			throw new GlobalException("该用户无法操作任务");
		}
	}

	private InputStream getInputStream(String processInstanceId) {
		String processDefinitionId;
		// 获取当前的流程实例
		final ProcessInstance processInstance = runtimeService.createProcessInstanceQuery()
			.processInstanceId(processInstanceId)
			.singleResult();
		// 如果流程已结束，则得到结束节点
		if (null == processInstance) {
			final HistoricProcessInstance hpi = historyService.createHistoricProcessInstanceQuery()
				.processInstanceId(processInstanceId)
				.singleResult();
			processDefinitionId = hpi.getProcessDefinitionId();
		}
		else {
			// 没有结束，获取当前活动节点
			// 根据流程实例id获取当前处于ActivityId集合
			final ProcessInstance pi = runtimeService.createProcessInstanceQuery()
				.processInstanceId(processInstanceId)
				.singleResult();
			processDefinitionId = pi.getProcessDefinitionId();
		}
		// 获取活动节点
		final List<HistoricActivityInstance> highLightedFlowList = historyService.createHistoricActivityInstanceQuery()
			.processInstanceId(processInstanceId)
			.orderByHistoricActivityInstanceStartTime()
			.asc()
			.list();
		List<String> highLightedFlows = new ArrayList<>(5);
		List<String> highLightedNodes = new ArrayList<>(5);
		// 高亮线
		for (HistoricActivityInstance temActivityInstance : highLightedFlowList) {
			if ("sequenceFlow".equals(temActivityInstance.getActivityType())) {
				// 高亮线
				highLightedFlows.add(temActivityInstance.getActivityId());
			}
			else {
				// 高亮节点
				highLightedNodes.add(temActivityInstance.getActivityId());
			}
		}
		// 获取流程图
		final BpmnModel bpmnModel = repositoryService.getBpmnModel(processDefinitionId);
		final ProcessEngineConfiguration configuration = processEngine.getProcessEngineConfiguration();
		// 获取自定义图片生成器
		// ProcessDiagramGenerator diagramGenerator = new ProcessDiagramGeneratorConfig();
		// return diagramGenerator.generateDiagram(bpmnModel, "png", highLightedNodes,
		// highLightedFlows,
		// configuration.getActivityFontName(), configuration.getLabelFontName(),
		// configuration.getAnnotationFontName(), configuration.getClassLoader(), 1.0,
		// true);
		return null;
	}

}
