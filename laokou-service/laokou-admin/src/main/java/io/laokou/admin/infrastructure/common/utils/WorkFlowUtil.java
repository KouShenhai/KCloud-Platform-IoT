package io.laokou.admin.infrastructure.common.utils;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import io.laokou.admin.application.service.SysMessageApplicationService;
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
import org.springframework.stereotype.Component;
import java.util.List;
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

    public Long getAuditUser(String definitionId,String executionId,String processInstanceId) {
        if (StringUtils.isBlank(executionId)) {
            final Task task = taskService.createTaskQuery().processInstanceId(processInstanceId).active().singleResult();
            executionId = task.getExecutionId();
        }
        Execution execution = runtimeService.createExecutionQuery().executionId(executionId).singleResult();
        String activityId = execution.getActivityId();
        BpmnModel bpmnModel = repositoryService.getBpmnModel(definitionId);
        FlowNode flowNode = (FlowNode) bpmnModel.getFlowElement(activityId);
        List<SequenceFlow> outFlows = flowNode.getOutgoingFlows();
        for (SequenceFlow sequenceFlow : outFlows) {
            FlowElement sourceFlowElement = sequenceFlow.getSourceFlowElement();
            final String json = JSON.toJSONString(sourceFlowElement);
            return JSONObject.parseObject(json).getLong("assignee");
        }
        return null;
    }

    public void sendAuditMsg(Long userId) {

    }

}
