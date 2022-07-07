package io.laokou.admin.application.service.impl;
import io.laokou.admin.application.service.WorkflowTaskApplicationService;
import io.laokou.admin.infrastructure.common.enums.FlowCommentEnum;
import io.laokou.admin.infrastructure.common.user.SecurityUser;
import io.laokou.admin.interfaces.dto.AuditDTO;
import io.laokou.admin.interfaces.dto.ClaimDTO;
import io.laokou.admin.interfaces.dto.UnClaimDTO;
import io.laokou.common.exception.CustomException;
import org.apache.commons.collections.MapUtils;
import org.flowable.engine.TaskService;
import org.flowable.task.api.DelegationState;
import org.flowable.task.api.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import javax.servlet.http.HttpServletRequest;
/**
 * @author Kou Shenhai
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class WorkflowTaskApplicationServiceImpl implements WorkflowTaskApplicationService {

    @Autowired
    private TaskService taskService;

    @Override
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
    public Boolean unClaimTask(UnClaimDTO dto) {
        taskService.unclaim(dto.getTaskId());
        return true;
    }

    @Override
    public Boolean deleteTask(String taskId) {
        taskService.deleteTask(taskId);
        return true;
    }

}
