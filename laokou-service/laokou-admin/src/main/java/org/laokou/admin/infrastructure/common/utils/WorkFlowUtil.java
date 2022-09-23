/**
 * Copyright (c) 2022 KCloud-Platform Authors. All Rights Reserved.
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
 */
package org.laokou.admin.infrastructure.common.utils;
import com.google.common.collect.Sets;
import org.laokou.admin.application.service.SysMessageApplicationService;
import org.laokou.admin.interfaces.dto.MessageDTO;
import org.laokou.common.utils.JacksonUtil;
import org.apache.commons.lang3.StringUtils;
import org.flowable.bpmn.model.BpmnModel;
import org.flowable.bpmn.model.FlowElement;
import org.flowable.bpmn.model.FlowNode;
import org.flowable.bpmn.model.SequenceFlow;
import org.flowable.engine.RepositoryService;
import org.flowable.engine.RuntimeService;
import org.flowable.engine.TaskService;
import org.flowable.engine.runtime.Execution;
import org.flowable.task.api.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Set;
/**
 * @author Kou Shenhai
 * @version 1.0
 * @date 2022/8/19 0019 上午 9:23
 */
@Component
public class WorkFlowUtil {

    @Autowired
    private RuntimeService runtimeService;

    @Autowired
    private TaskService taskService;

    @Autowired
    private RepositoryService repositoryService;

    @Autowired
    private SysMessageApplicationService sysMessageApplicationService;

    public String getAuditUser(String definitionId,String executionId,String processInstanceId) {
        if (StringUtils.isBlank(executionId)) {
            final Task task = taskService.createTaskQuery().processInstanceId(processInstanceId).active().singleResult();
            if (null == task) {
                return null;
            }
            executionId = task.getExecutionId();
        }
        Execution execution = runtimeService.createExecutionQuery().executionId(executionId).singleResult();
        String activityId = execution.getActivityId();
        BpmnModel bpmnModel = repositoryService.getBpmnModel(definitionId);
        FlowNode flowNode = (FlowNode) bpmnModel.getFlowElement(activityId);
        List<SequenceFlow> outFlows = flowNode.getOutgoingFlows();
        for (SequenceFlow sequenceFlow : outFlows) {
            FlowElement sourceFlowElement = sequenceFlow.getSourceFlowElement();
            final String json = JacksonUtil.toJsonStr(sourceFlowElement);
            return JacksonUtil.toMap(json,String.class,Object.class).get("assignee").toString();
        }
        return null;
    }

    /**
     *
     * @param assignee
     * @param type
     * @param sendChannel
     */
    @Async
    public void sendAuditMsg(String assignee, Integer type, Integer sendChannel,Long id,String name,HttpServletRequest request) {
        String title = "资源审批提醒";
        String content = String.format("编号为%s，名称为%s的资源需要审批，请及时查看并处理",id,name);
        Set set = Sets.newHashSet();
        set.add(assignee);
        MessageDTO dto = new MessageDTO();
        dto.setContent(content);
        dto.setTitle(title);
        dto.setSendChannel(sendChannel);
        dto.setReceiver(set);
        dto.setType(type);
        sysMessageApplicationService.sendMessage(dto,request);
    }

}
