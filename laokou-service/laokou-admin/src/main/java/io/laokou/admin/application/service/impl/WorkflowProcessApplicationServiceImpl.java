package io.laokou.admin.application.service.impl;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.common.collect.Lists;
import io.laokou.admin.application.service.WorkflowProcessApplicationService;
import io.laokou.admin.infrastructure.common.user.SecurityUser;
import io.laokou.admin.interfaces.qo.TaskQO;
import io.laokou.admin.interfaces.vo.TaskVO;
import io.laokou.common.exception.CustomException;
import org.flowable.engine.HistoryService;
import org.flowable.engine.RepositoryService;
import org.flowable.engine.RuntimeService;
import org.flowable.engine.TaskService;
import org.flowable.engine.repository.ProcessDefinition;
import org.flowable.task.api.Task;
import org.flowable.task.api.TaskQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
@Service
@Transactional(rollbackFor = Exception.class)
public class WorkflowProcessApplicationServiceImpl implements WorkflowProcessApplicationService {

    @Autowired
    private RepositoryService repositoryService;

    @Autowired
    private RuntimeService runtimeService;

    @Autowired
    private TaskService taskService;

    @Autowired
    private HistoryService historyService;

    @Override
    public Boolean startProcess(String definitionId) {
        ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery()
                .processDefinitionId(definitionId)
                .singleResult();
        if (null != processDefinition && processDefinition.isSuspended()) {
            throw new CustomException("流程已被挂起，请先激活流程");
        }
        runtimeService.startProcessInstanceById(definitionId);
        return true;
    }

    @Override
    public IPage<TaskVO> queryTaskPage(TaskQO qo, HttpServletRequest request) {
        final Integer pageNum = qo.getPageNum();
        final Integer pageSize = qo.getPageSize();
        final Long userId = SecurityUser.getUserId(request);
        final String username = SecurityUser.getUsername(request);
        final TaskQuery taskQuery = taskService.createTaskQuery()
                .active()
                .includeProcessVariables()
                .taskCandidateOrAssigned(userId.toString())
                .orderByTaskCreateTime().desc();
        final long pageTotal = taskQuery.count();
        IPage<TaskVO> page = new Page<>(pageNum,pageSize,pageTotal);
        int  pageIndex = pageSize * (pageNum - 1);
        final List<Task> taskList = taskQuery.listPage(pageIndex, pageSize);
        List<TaskVO> voList = Lists.newArrayList();
        for (Task task : taskList) {
            TaskVO vo = new TaskVO();
            vo.setTaskId(task.getId());
            vo.setTaskDefinitionKey(task.getTaskDefinitionKey());
            vo.setProcessInstanceId(task.getProcessInstanceId());
            vo.setTaskName(task.getName());
            vo.setAssigneeName(username);
            vo.setCreateTime(task.getCreateTime());
            voList.add(vo);
        }
        page.setRecords(voList);
        return page;
    }

}
