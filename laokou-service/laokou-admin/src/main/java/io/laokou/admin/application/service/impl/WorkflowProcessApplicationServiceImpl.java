package io.laokou.admin.application.service.impl;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.common.collect.Lists;
import io.laokou.admin.application.service.WorkflowProcessApplicationService;
import io.laokou.admin.interfaces.vo.StartProcessVO;
import io.laokou.common.user.SecurityUser;
import io.laokou.admin.interfaces.qo.TaskQO;
import io.laokou.admin.interfaces.vo.TaskVO;
import io.laokou.common.exception.CustomException;
import io.laokou.datasource.annotation.DataSource;
import org.apache.commons.lang3.StringUtils;
import org.flowable.engine.RepositoryService;
import org.flowable.engine.RuntimeService;
import org.flowable.engine.TaskService;
import org.flowable.engine.repository.ProcessDefinition;
import org.flowable.engine.runtime.ProcessInstance;
import org.flowable.task.api.Task;
import org.flowable.task.api.TaskQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
/**
 * @author Kou Shenhai
 */
@Service
@Transactional(rollbackFor = Exception.class,propagation = Propagation.REQUIRES_NEW)
public class WorkflowProcessApplicationServiceImpl implements WorkflowProcessApplicationService {

    @Autowired
    private RepositoryService repositoryService;

    @Autowired
    private RuntimeService runtimeService;

    @Autowired
    private TaskService taskService;

    @Override
    @DataSource("master")
    public StartProcessVO startProcess(String processKey,String instanceName) {
        StartProcessVO vo = new StartProcessVO();
        ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery()
                .processDefinitionKey(processKey)
                .latestVersion()
                .singleResult();
        if (null != processDefinition && processDefinition.isSuspended()) {
            throw new CustomException("流程已被挂起，请先激活流程");
        }
        final ProcessInstance processInstance = runtimeService.startProcessInstanceByKey(processKey);
        runtimeService.setProcessInstanceName(processInstance.getId(),instanceName);
        vo.setDefinitionId(processDefinition.getId());
        vo.setInstanceId(processInstance.getId());
        return vo;
    }

    @Override
    @DataSource("master")
    public IPage<TaskVO> queryTaskPage(TaskQO qo, HttpServletRequest request) {
        final Integer pageNum = qo.getPageNum();
        final Integer pageSize = qo.getPageSize();
        String processName = qo.getProcessName();
        final Long userId = SecurityUser.getUserId(request);
        final String username = SecurityUser.getUsername(request);
        TaskQuery taskQuery = taskService.createTaskQuery()
                .active()
                .includeProcessVariables()
                .taskCandidateOrAssigned(userId.toString())
                .orderByTaskCreateTime().desc();
        if (StringUtils.isNotBlank(processName)) {
            taskQuery = taskQuery.processDefinitionNameLike("%" + processName + "%");
        }
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
            voList.add(vo);
        }
        page.setRecords(voList);
        return page;
    }

}
