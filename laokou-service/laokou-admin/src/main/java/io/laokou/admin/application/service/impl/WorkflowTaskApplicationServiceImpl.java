package io.laokou.admin.application.service.impl;
import com.google.common.collect.Lists;
import io.laokou.admin.application.service.WorkflowTaskApplicationService;
import io.laokou.admin.infrastructure.common.enums.FlowCommentEnum;
import io.laokou.common.user.SecurityUser;
import io.laokou.admin.infrastructure.component.CustomProcessDiagramGenerator;
import io.laokou.admin.interfaces.dto.AuditDTO;
import io.laokou.admin.interfaces.dto.ClaimDTO;
import io.laokou.admin.interfaces.dto.UnClaimDTO;
import io.laokou.common.exception.CustomException;
import io.laokou.common.utils.FileUtil;
import io.laokou.datasource.annotation.DataSource;
import lombok.AllArgsConstructor;
import org.apache.commons.collections.MapUtils;
import org.flowable.bpmn.model.BpmnModel;
import org.flowable.engine.*;
import org.flowable.engine.history.HistoricActivityInstance;
import org.flowable.engine.history.HistoricProcessInstance;
import org.flowable.engine.runtime.ProcessInstance;
import org.flowable.image.ProcessDiagramGenerator;
import org.flowable.task.api.DelegationState;
import org.flowable.task.api.Task;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
/**
 * @author Kou Shenhai
 */
@Service
@AllArgsConstructor
@Transactional(rollbackFor = Exception.class,propagation = Propagation.REQUIRES_NEW)
public class WorkflowTaskApplicationServiceImpl implements WorkflowTaskApplicationService {

    private final TaskService taskService;

    private final RuntimeService runtimeService;

    private final HistoryService historyService;

    private final RepositoryService repositoryService;

    @Qualifier("processEngine")
    private final ProcessEngine processEngine;

    @Override
    @DataSource("master")
    public Boolean auditTask(AuditDTO dto, HttpServletRequest request) {
        Task task = taskService.createTaskQuery().taskId(dto.getTaskId()).singleResult();
        if (null == task) {
            throw new CustomException("任务不存在");
        }
        if (DelegationState.PENDING.equals(task.getDelegationState())) {
            taskService.addComment(dto.getTaskId(),dto.getInstanceId(),FlowCommentEnum.DELEGATE.getType(),dto.getComment());
            //委派
            taskService.resolveTask(dto.getTaskId(),dto.getValues());
        } else {
            taskService.addComment(dto.getTaskId(),dto.getInstanceId(), FlowCommentEnum.NORMAL.getType(),dto.getComment());
            taskService.setAssignee(dto.getTaskId(), SecurityUser.getUserId(request).toString());
            if (MapUtils.isNotEmpty(dto.getValues())) {
                taskService.complete(dto.getTaskId(),dto.getValues());
            } else {
                taskService.complete(dto.getTaskId());
            }
        }
        return true;
    }

    @Override
    @DataSource("master")
    public Boolean claimTask(ClaimDTO dto,HttpServletRequest request) {
        final Task task = taskService.createTaskQuery()
                .taskId(dto.getTaskId())
                .singleResult();
        if (null == task) {
            throw new CustomException("任务不存在");
        }
        taskService.claim(dto.getTaskId(),SecurityUser.getUserId(request).toString());
        return true;
    }

    @Override
    @DataSource("master")
    public Boolean unClaimTask(UnClaimDTO dto) {
        taskService.unclaim(dto.getTaskId());
        return true;
    }

    @Override
    @DataSource("master")
    public Boolean deleteTask(String taskId) {
        taskService.deleteTask(taskId);
        return true;
    }

    @Override
    @DataSource("master")
    public void diagramProcess(String processInstanceId, HttpServletResponse response) throws IOException {
        final InputStream inputStream = getInputStream(processInstanceId);
        final BufferedImage image = ImageIO.read(inputStream);
        response.setContentType("image/png");
        final ServletOutputStream outputStream = response.getOutputStream();
        if (null != image) {
            ImageIO.write(image,"png",outputStream);
        }
        outputStream.flush();
        FileUtil.closeStream(inputStream,outputStream);
    }

    private InputStream getInputStream(String processInstanceId) {
        String processDefinitionId;
        //获取当前的流程实例
        final ProcessInstance processInstance = runtimeService.createProcessInstanceQuery()
                .processInstanceId(processInstanceId)
                .singleResult();
        //如果流程已结束，则得到结束节点
        if (null == processInstance) {
            final HistoricProcessInstance hpi = historyService.createHistoricProcessInstanceQuery()
                    .processInstanceId(processInstanceId).singleResult();
            processDefinitionId = hpi.getProcessDefinitionId();
        } else {
            //没有结束，获取当前活动节点
            //根据流程实例id获取当前处于ActivityId集合
            final ProcessInstance pi = runtimeService.createProcessInstanceQuery()
                    .processInstanceId(processInstanceId).singleResult();
            processDefinitionId = pi.getProcessDefinitionId();
        }
        //获取活动节点
        final List<HistoricActivityInstance> highLightedFlowList = historyService.createHistoricActivityInstanceQuery()
                .processInstanceId(processInstanceId).orderByHistoricActivityInstanceStartTime().asc().list();
        List<String> highLightedFlows = Lists.newArrayList();
        List<String> highLightedNodes = Lists.newArrayList();
        //高亮线
        for (HistoricActivityInstance temActivityInstance : highLightedFlowList) {
            if ("sequenceFlow".equals(temActivityInstance.getActivityType())) {
                //高亮线
                highLightedFlows.add(temActivityInstance.getActivityId());
            } else {
                //高亮节点
                highLightedNodes.add(temActivityInstance.getActivityId());
            }
        }
        //获取流程图
        final BpmnModel bpmnModel = repositoryService.getBpmnModel(processDefinitionId);
        final ProcessEngineConfiguration configuration = processEngine.getProcessEngineConfiguration();
        //获取自定义图片生成器
        ProcessDiagramGenerator diagramGenerator = new CustomProcessDiagramGenerator();
        return diagramGenerator.generateDiagram(bpmnModel, "png", highLightedNodes, highLightedFlows, configuration.getActivityFontName(),
                configuration.getLabelFontName(), configuration.getAnnotationFontName(), configuration.getClassLoader(), 1.0, true);
    }

}
