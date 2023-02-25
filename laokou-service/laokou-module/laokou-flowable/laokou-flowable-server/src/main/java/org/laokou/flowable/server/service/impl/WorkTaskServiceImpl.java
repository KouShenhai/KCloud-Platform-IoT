/**
 * Copyright (c) 2022 KCloud-Platform-Alibaba Authors. All Rights Reserved.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.laokou.flowable.server.service.impl;
import io.seata.core.context.RootContext;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.MapUtils;
import org.flowable.bpmn.model.BpmnModel;
import org.flowable.engine.*;
import org.flowable.engine.history.HistoricActivityInstance;
import org.flowable.engine.history.HistoricProcessInstance;
import org.flowable.engine.repository.ProcessDefinition;
import org.flowable.engine.runtime.ProcessInstance;
import org.flowable.image.ProcessDiagramGenerator;
import org.flowable.task.api.Task;
import org.flowable.task.api.TaskQuery;
import org.laokou.common.i18n.core.CustomException;
import org.laokou.common.core.utils.FileUtil;
import org.laokou.common.core.utils.StringUtil;
import org.laokou.common.i18n.utils.ValidatorUtil;
import org.laokou.flowable.client.dto.AuditDTO;
import org.laokou.flowable.client.dto.ProcessDTO;
import org.laokou.flowable.client.dto.TaskDTO;
import org.laokou.flowable.client.vo.AssigneeVO;
import org.laokou.flowable.client.vo.PageVO;
import org.laokou.flowable.client.vo.TaskVO;
import org.laokou.flowable.server.config.CustomProcessDiagramGenerator;
import org.laokou.flowable.server.enums.FlowCommentEnum;
import org.laokou.flowable.server.service.WorkTaskService;
import org.laokou.flowable.server.utils.TaskUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
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

    private final TaskService taskService;

    private final TaskUtil taskUtil;

    private final RuntimeService runtimeService;

    private final HistoryService historyService;

    private final RepositoryService repositoryService;

    private final ProcessEngine processEngine;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public AssigneeVO auditTask(AuditDTO dto) {
        ValidatorUtil.validateEntity(dto);
        log.info("分布式事务 XID:{}", RootContext.getXID());
        String taskId = dto.getTaskId();
        String instanceId = dto.getInstanceId();
        String type = FlowCommentEnum.NORMAL.getType();
        String comment = dto.getComment();
        Map<String, Object> values = dto.getValues();
        String definitionId = dto.getDefinitionId();
        Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
        if (null == task) {
            throw new CustomException("任务不存在");
        }
        taskService.addComment(taskId,instanceId,type,comment);
        if (MapUtils.isNotEmpty(values)) {
            taskService.complete(taskId,values);
        } else {
            taskService.complete(taskId);
        }
        String assignee = taskUtil.getAssignee(definitionId, instanceId);
        log.info("当前审核人：{}",assignee.isEmpty() ? "无" : assignee);
        return new AssigneeVO(assignee,instanceId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public AssigneeVO startTask(ProcessDTO dto) {
        ValidatorUtil.validateEntity(dto);
        log.info("分布式事务 XID:{}", RootContext.getXID());
        String processKey = dto.getProcessKey();
        String businessKey = dto.getBusinessKey();
        String businessName = dto.getBusinessName();
        ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery()
                .processDefinitionKey(processKey)
                .latestVersion()
                .singleResult();
        if (processDefinition == null) {
            throw new CustomException("流程未定义");
        }
        if (processDefinition.isSuspended()) {
            throw new CustomException("流程已被挂起，请先激活流程");
        }
        final ProcessInstance processInstance = runtimeService.startProcessInstanceByKey(processKey,businessKey);
        if (processInstance == null) {
            throw new CustomException("流程不存在");
        }
        String definitionId = processDefinition.getId();
        String instanceId = processInstance.getId();
        runtimeService.setProcessInstanceName(instanceId,businessName);
        String assignee = taskUtil.getAssignee(definitionId, instanceId);
        log.info("当前审核人：{}",assignee.isEmpty() ? "无" : assignee);
        return new AssigneeVO(assignee,instanceId);
    }

    @Override
    public PageVO<TaskVO> queryTaskPage(TaskDTO dto) {
        ValidatorUtil.validateEntity(dto);
        final Integer pageNum = dto.getPageNum();
        final Integer pageSize = dto.getPageSize();
        String processName = dto.getProcessName();
        String processKey = dto.getProcessKey();
        String username = dto.getUsername();
        Long userId = dto.getUserId();
        TaskQuery taskQuery = taskService.createTaskQuery()
                .active()
                .includeProcessVariables()
                .processDefinitionKey(processKey)
                .taskCandidateOrAssigned(userId.toString())
                .orderByTaskCreateTime().desc();
        if (StringUtil.isNotEmpty(processName)) {
            taskQuery = taskQuery.processDefinitionNameLike("%" + processName + "%");
        }
        final long total = taskQuery.count();
        int  pageIndex = pageSize * (pageNum - 1);
        final List<Task> taskList = taskQuery.listPage(pageIndex, pageSize);
        List<TaskVO> voList = new ArrayList<>(taskList.size());
        for (Task task : taskList) {
            TaskVO vo = new TaskVO();
            vo.setTaskId(task.getId());
            vo.setTaskDefinitionKey(task.getTaskDefinitionKey());
            vo.setProcessInstanceId(task.getProcessInstanceId());
            ProcessInstance processInstance = runtimeService.createProcessInstanceQuery().processInstanceId(task.getProcessInstanceId()).singleResult();
            ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery()
                    .latestVersion()
                    .processDefinitionId(task.getProcessDefinitionId())
                    .singleResult();
            vo.setTaskName(task.getName());
            vo.setDefinitionId(processDefinition.getId());
            vo.setProcessName(processDefinition.getName());
            vo.setAssigneeName(username);
            vo.setCreateTime(task.getCreateTime());
            vo.setProcessInstanceName(processInstance.getName());
            vo.setBusinessKey(processInstance.getBusinessKey());
            voList.add(vo);
        }
        return new PageVO<>(voList,total);
    }

    @Override
    public void diagramTask(String processInstanceId, HttpServletResponse response) throws IOException {
        final InputStream inputStream = getInputStream(processInstanceId);
        final BufferedImage image = ImageIO.read(inputStream);
        response.setContentType("image/png");
        final ServletOutputStream outputStream = response.getOutputStream();
        if (null != image) {
            ImageIO.write(image,"png",outputStream);
        }
        outputStream.flush();
        inputStream.close();
        outputStream.close();
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
                    .processInstanceId(processInstanceId).singleResult();
            processDefinitionId = hpi.getProcessDefinitionId();
        } else {
            // 没有结束，获取当前活动节点
            // 根据流程实例id获取当前处于ActivityId集合
            final ProcessInstance pi = runtimeService.createProcessInstanceQuery()
                    .processInstanceId(processInstanceId).singleResult();
            processDefinitionId = pi.getProcessDefinitionId();
        }
        // 获取活动节点
        final List<HistoricActivityInstance> highLightedFlowList = historyService.createHistoricActivityInstanceQuery()
                .processInstanceId(processInstanceId).orderByHistoricActivityInstanceStartTime().asc().list();
        List<String> highLightedFlows = new ArrayList<>(5);
        List<String> highLightedNodes = new ArrayList<>(5);
        // 高亮线
        for (HistoricActivityInstance temActivityInstance : highLightedFlowList) {
            if ("sequenceFlow".equals(temActivityInstance.getActivityType())) {
                // 高亮线
                highLightedFlows.add(temActivityInstance.getActivityId());
            } else {
                // 高亮节点
                highLightedNodes.add(temActivityInstance.getActivityId());
            }
        }
        // 获取流程图
        final BpmnModel bpmnModel = repositoryService.getBpmnModel(processDefinitionId);
        final ProcessEngineConfiguration configuration = processEngine.getProcessEngineConfiguration();
        // 获取自定义图片生成器
        ProcessDiagramGenerator diagramGenerator = new CustomProcessDiagramGenerator();
        return diagramGenerator.generateDiagram(bpmnModel, "png", highLightedNodes, highLightedFlows, configuration.getActivityFontName(),
                configuration.getLabelFontName(), configuration.getAnnotationFontName(), configuration.getClassLoader(), 1.0, true);
    }
}
