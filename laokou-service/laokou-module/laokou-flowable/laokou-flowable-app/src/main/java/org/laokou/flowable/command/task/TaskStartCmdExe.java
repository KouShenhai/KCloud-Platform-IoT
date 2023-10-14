/*
 * Copyright (c) 2022 KCloud-Platform-Alibaba Authors. All Rights Reserved.
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
 *
 */

package org.laokou.flowable.command.task;

import io.seata.core.context.RootContext;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.flowable.engine.RepositoryService;
import org.flowable.engine.RuntimeService;
import org.flowable.engine.repository.ProcessDefinition;
import org.flowable.engine.runtime.ProcessInstance;
import org.laokou.common.i18n.common.GlobalException;
import org.laokou.common.i18n.dto.Result;
import org.laokou.flowable.dto.task.TaskStartCmd;
import org.laokou.flowable.dto.task.clientobject.StartCO;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author laokou
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class TaskStartCmdExe {

    private final RuntimeService runtimeService;
    private final RepositoryService repositoryService;

    public Result<StartCO> execute(TaskStartCmd cmd) {
        log.info("分布式事务 XID:{}", RootContext.getXID());
        String definitionKey = cmd.getDefinitionKey();
        String instanceName = cmd.getInstanceName();
        String businessKey = cmd.getBusinessKey();
        ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery()
                .processDefinitionKey(definitionKey)
                .latestVersion()
                .singleResult();
        if (processDefinition == null) {
            throw new GlobalException("流程未定义");
        }
        if (processDefinition.isSuspended()) {
            throw new GlobalException("流程已被挂起，请激活流程");
        }
        return Result.of(start(definitionKey,businessKey,instanceName));
    }

    @Transactional(rollbackFor = Exception.class)
    public StartCO start(String definitionKey,String businessKey,String instanceName) {
        ProcessInstance processInstance = runtimeService.startProcessInstanceById(definitionKey, businessKey);
        if (processInstance == null) {
            throw new GlobalException("流程不存在");
        }
        String instanceId = processInstance.getId();
        runtimeService.setProcessInstanceName(instanceId, instanceName);
        return new StartCO(instanceId);
    }

}
