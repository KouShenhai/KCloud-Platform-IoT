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
package org.laokou.admin.application.service.impl;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.common.collect.Lists;
import org.laokou.admin.application.service.WorkflowProcessApplicationService;
import org.laokou.admin.application.service.WorkflowTaskApplicationService;
import org.laokou.admin.domain.sys.entity.SysResourceAuditLogDO;
import org.laokou.admin.domain.sys.entity.SysResourceDO;
import org.laokou.admin.domain.sys.repository.service.SysResourceAuditLogService;
import org.laokou.admin.domain.sys.repository.service.SysResourceService;
import org.laokou.admin.infrastructure.common.enums.ChannelTypeEnum;
import org.laokou.admin.infrastructure.common.enums.MessageTypeEnum;
import org.laokou.admin.infrastructure.common.utils.WorkFlowUtil;
import org.laokou.admin.interfaces.dto.AuditDTO;
import org.laokou.admin.interfaces.vo.StartProcessVO;
import org.laokou.admin.interfaces.qo.TaskQO;
import org.laokou.admin.interfaces.vo.TaskVO;
import org.laokou.common.user.SecurityUser;
import org.laokou.common.exception.CustomException;
import org.laokou.datasource.annotation.DataSource;
import io.seata.spring.annotation.GlobalTransactional;
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
import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;
import java.util.Map;
/**
 * @author Kou Shenhai
 */
@Service
@GlobalTransactional(rollbackFor = Exception.class)
public class WorkflowProcessApplicationServiceImpl implements WorkflowProcessApplicationService {

    @Autowired
    private RepositoryService repositoryService;

    @Autowired
    private RuntimeService runtimeService;

    @Autowired
    private TaskService taskService;

    private static final String PROCESS_KEY = "Process_88888888";

    @Autowired
    private WorkflowTaskApplicationService workflowTaskApplicationService;

    @Autowired
    private WorkFlowUtil workFlowUtil;

    @Autowired
    private SysResourceService sysResourceService;

    @Autowired
    private SysResourceAuditLogService sysResourceAuditLogService;

    @Override
    @DataSource("master")
    public StartProcessVO startResourceProcess(String processKey, String businessKey, String instanceName) {
        StartProcessVO vo = new StartProcessVO();
        ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery()
                .processDefinitionKey(processKey)
                .latestVersion()
                .singleResult();
        if (null != processDefinition && processDefinition.isSuspended()) {
            throw new CustomException("流程已被挂起，请先激活流程");
        }
        final ProcessInstance processInstance = runtimeService.startProcessInstanceByKey(processKey,businessKey);
        runtimeService.setProcessInstanceName(processInstance.getId(),instanceName);
        vo.setDefinitionId(processDefinition.getId());
        vo.setInstanceId(processInstance.getId());
        return vo;
    }

    @Override
    @DataSource("master")
    public IPage<TaskVO> queryResourceTaskPage(TaskQO qo, HttpServletRequest request) {
        final Integer pageNum = qo.getPageNum();
        final Integer pageSize = qo.getPageSize();
        String processName = qo.getProcessName();
        final Long userId = SecurityUser.getUserId(request);
        final String username = SecurityUser.getUsername(request);
        TaskQuery taskQuery = taskService.createTaskQuery()
                .active()
                .includeProcessVariables()
                .processDefinitionKey(PROCESS_KEY)
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
            vo.setBusinessKey(processInstance.getBusinessKey());
            voList.add(vo);
        }
        page.setRecords(voList);
        return page;
    }

    @Override
    public Boolean auditResourceTask(AuditDTO dto, HttpServletRequest request) {
        Map<String, Object> values = dto.getValues();
        workflowTaskApplicationService.auditTask(dto, request);
        String auditUser = workFlowUtil.getAuditUser(dto.getDefinitionId(), null, dto.getInstanceId());
        Integer status;
        Integer auditStatus = Integer.valueOf(values.get("auditStatus").toString());
        SysResourceDO sysResourceDO = sysResourceService.getById(dto.getBusinessKey());
        if (null != auditUser) {
            //审批中
            status = 1;
            workFlowUtil.sendAuditMsg(auditUser, MessageTypeEnum.REMIND.ordinal(), ChannelTypeEnum.PLATFORM.ordinal(),Long.valueOf(dto.getBusinessKey()),dto.getInstanceName(),request);
        } else {
            //0拒绝 1同意
            if (0 == auditStatus) {
                //审批拒绝
                status = 2;
            } else {
                //审批通过
                status = 3;
            }
        }
        sysResourceDO.setStatus(status);
        sysResourceService.updateById(sysResourceDO);
        //插入审批日志
        SysResourceAuditLogDO logDO = new SysResourceAuditLogDO();
        logDO.setAuditDate(new Date());
        logDO.setAuditName(SecurityUser.getUsername(request));
        logDO.setCreator(SecurityUser.getUserId(request));
        logDO.setComment(dto.getComment());
        logDO.setResourceId(Long.valueOf(dto.getBusinessKey()));
        logDO.setAuditStatus(auditStatus);
        return sysResourceAuditLogService.save(logDO);
    }

}
