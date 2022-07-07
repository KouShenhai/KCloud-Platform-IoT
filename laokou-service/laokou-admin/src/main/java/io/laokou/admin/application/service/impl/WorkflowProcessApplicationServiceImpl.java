package io.laokou.admin.application.service.impl;

import io.laokou.admin.application.service.WorkflowProcessApplicationService;
import io.laokou.common.exception.CustomException;
import org.flowable.engine.RepositoryService;
import org.flowable.engine.RuntimeService;
import org.flowable.engine.repository.ProcessDefinition;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(rollbackFor = Exception.class)
public class WorkflowProcessApplicationServiceImpl implements WorkflowProcessApplicationService {

    @Autowired
    private RepositoryService repositoryService;

    @Autowired
    private RuntimeService runtimeService;

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

}
