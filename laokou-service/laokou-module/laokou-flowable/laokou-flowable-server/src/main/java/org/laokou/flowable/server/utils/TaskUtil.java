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

package org.laokou.flowable.server.utils;

import lombok.RequiredArgsConstructor;
import org.flowable.bpmn.model.BpmnModel;
import org.flowable.bpmn.model.FlowElement;
import org.flowable.bpmn.model.FlowNode;
import org.flowable.bpmn.model.SequenceFlow;
import org.flowable.engine.RepositoryService;
import org.flowable.engine.RuntimeService;
import org.flowable.engine.TaskService;
import org.flowable.engine.runtime.Execution;
import org.flowable.task.api.Task;
import org.laokou.common.core.utils.JacksonUtil;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author laokou
 */
@Component
@RequiredArgsConstructor
public class TaskUtil {

    private final TaskService taskService;

    private final RuntimeService runtimeService;

    private final RepositoryService repositoryService;

    private static final String ASSIGNEE = "assignee";

    public String getAssignee (String definitionId,String processInstanceId) {
        final Task task = taskService.createTaskQuery().processInstanceId(processInstanceId).active().singleResult();
        if (null == task) {
            return "";
        }
        String executionId = task.getExecutionId();
        Execution execution = runtimeService.createExecutionQuery().executionId(executionId).singleResult();
        String activityId = execution.getActivityId();
        BpmnModel bpmnModel = repositoryService.getBpmnModel(definitionId);
        FlowNode flowNode = (FlowNode) bpmnModel.getFlowElement(activityId);
        List<SequenceFlow> outFlows = flowNode.getOutgoingFlows();
        for (SequenceFlow sequenceFlow : outFlows) {
            FlowElement sourceFlowElement = sequenceFlow.getSourceFlowElement();
            final String json = JacksonUtil.toJsonStr(sourceFlowElement);
            return JacksonUtil.readTree(json).get(ASSIGNEE).asText();
        }
        return "";
    }
}
