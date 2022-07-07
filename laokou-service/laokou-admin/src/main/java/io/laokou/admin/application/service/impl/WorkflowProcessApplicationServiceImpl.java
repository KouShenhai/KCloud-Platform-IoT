package io.laokou.admin.application.service.impl;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.common.collect.Lists;
import io.laokou.admin.application.service.WorkflowProcessApplicationService;
import io.laokou.admin.infrastructure.common.user.SecurityUser;
import io.laokou.admin.interfaces.qo.TodoQO;
import io.laokou.admin.interfaces.vo.TodoVO;
import io.laokou.common.exception.CustomException;
import org.flowable.engine.HistoryService;
import org.flowable.engine.RepositoryService;
import org.flowable.engine.RuntimeService;
import org.flowable.engine.TaskService;
import org.flowable.engine.repository.ProcessDefinition;
import org.flowable.task.api.Task;
import org.flowable.task.api.TaskQuery;
import org.flowable.task.api.history.HistoricTaskInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;
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
    public IPage<TodoVO> todoTaskPage(TodoQO qo, HttpServletRequest request) {
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
        IPage<TodoVO> page = new Page<>(pageNum,pageSize,pageTotal);
        int  pageIndex = pageSize * (pageNum - 1);
        final List<Task> taskList = taskQuery.listPage(pageIndex, pageSize);
        List<TodoVO> voList = Lists.newArrayList();
        for (Task task : taskList) {
            TodoVO vo = new TodoVO();
            vo.setTaskId(task.getId());
            vo.setTaskDefinitionKey(task.getTaskDefinitionKey());
            vo.setProcessInstanceId(task.getProcessInstanceId());
            vo.setTaskName(task.getName());
            vo.setAssigneeName(username);
            vo.setCreateTime(task.getCreateTime());
            vo.setProcessVariables(this.getProcessVariables(task.getId()));
            voList.add(vo);
        }
        page.setRecords(voList);
        return page;
    }

    private Map<String,Object> getProcessVariables(String taskId) {
        final HistoricTaskInstance historicTaskInstance = historyService.createHistoricTaskInstanceQuery()
                .includeTaskLocalVariables()
                .finished()
                .taskId(taskId)
                .singleResult();
        if (null != historicTaskInstance) {
            return historicTaskInstance.getProcessVariables();
        }
        return taskService.getVariables(taskId);
    }

}
