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

package org.laokou.flowable.command.task.query;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.apache.hc.client5.http.utils.Base64;
import org.flowable.bpmn.model.BpmnModel;
import org.flowable.engine.*;
import org.flowable.engine.history.HistoricActivityInstance;
import org.flowable.engine.history.HistoricProcessInstance;
import org.flowable.engine.runtime.ProcessInstance;
import org.flowable.image.ProcessDiagramGenerator;
import org.laokou.common.i18n.dto.Result;
import org.laokou.common.security.utils.UserUtil;
import org.laokou.flowable.config.ProcessDiagramGeneratorConfig;
import org.laokou.flowable.dto.task.TaskDiagramGetQry;
import org.springframework.stereotype.Component;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * 查看任务流程图执行器.
 *
 * @author laokou
 */
@Component
@RequiredArgsConstructor
public class TaskDiagramGetQryExe {

	private static final String PNG = "png";

	private static final String SEQUENCE_FLOW = "sequenceFlow";

	private final RuntimeService runtimeService;

	private final HistoryService historyService;

	private final RepositoryService repositoryService;

	private final ProcessEngine processEngine;

	/**
	 * 执行查看任务流程图.
	 * @param qry 查看任务流程图参数
	 * @return 流程图
	 */
	@SneakyThrows
	public Result<String> execute(TaskDiagramGetQry qry) {
		try (InputStream inputStream = getInputStream(qry.getInstanceId());
				ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
			BufferedImage image = ImageIO.read(inputStream);
			if (null != image) {
				ImageIO.write(image, PNG, outputStream);
			}
			return Result.ok(Base64.encodeBase64String(outputStream.toByteArray()));
		}
	}

	/**
	 * 获取流程图输入流.
	 * @param instanceId 实例ID
	 * @return 输入流
	 */
	private InputStream getInputStream(String instanceId) {
		String processDefinitionId;
		// 获取当前的流程实例
		final ProcessInstance processInstance = runtimeService.createProcessInstanceQuery()
			.processInstanceId(UserUtil.getTenantId().toString())
			.processInstanceId(instanceId)
			.singleResult();
		// 如果流程已结束，则得到结束节点
		if (null == processInstance) {
			final HistoricProcessInstance hpi = historyService.createHistoricProcessInstanceQuery()
				.processInstanceTenantId(UserUtil.getTenantId().toString())
				.processInstanceId(instanceId)
				.singleResult();
			processDefinitionId = hpi.getProcessDefinitionId();
		}
		else {
			// 没有结束，获取当前活动节点
			// 根据流程实例id获取当前处于ActivityId集合
			final ProcessInstance pi = runtimeService.createProcessInstanceQuery()
				.processInstanceTenantId(UserUtil.getTenantId().toString())
				.processInstanceId(instanceId)
				.singleResult();
			processDefinitionId = pi.getProcessDefinitionId();
		}
		// 获取活动节点
		final List<HistoricActivityInstance> highLightedFlowList = historyService.createHistoricActivityInstanceQuery()
			.activityTenantId(UserUtil.getTenantId().toString())
			.processInstanceId(instanceId)
			.orderByHistoricActivityInstanceStartTime()
			.asc()
			.list();
		List<String> highLightedFlows = new ArrayList<>(5);
		List<String> highLightedNodes = new ArrayList<>(5);
		// 高亮线
		for (HistoricActivityInstance temActivityInstance : highLightedFlowList) {
			if (SEQUENCE_FLOW.equals(temActivityInstance.getActivityType())) {
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
		ProcessDiagramGenerator diagramGenerator = new ProcessDiagramGeneratorConfig();
		return diagramGenerator.generateDiagram(bpmnModel, PNG, highLightedNodes, highLightedFlows,
				configuration.getActivityFontName(), configuration.getLabelFontName(),
				configuration.getAnnotationFontName(), configuration.getClassLoader(), 1.0, true);
	}

}
