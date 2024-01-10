/*
 * Copyright (c) 2022 KCloud-Platform-Alibaba Author or Authors. All Rights Reserved.
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

package org.laokou.flowable.command.definition;

import com.baomidou.dynamic.datasource.toolkit.DynamicDataSourceContextHolder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.flowable.engine.RepositoryService;
import org.flowable.engine.repository.ProcessDefinition;
import org.laokou.common.i18n.common.exception.FlowException;
import org.laokou.common.i18n.common.exception.SystemException;
import org.laokou.common.i18n.dto.Result;
import org.laokou.common.i18n.utils.LogUtil;
import org.laokou.common.mybatisplus.utils.TransactionalUtil;
import org.laokou.common.security.utils.UserUtil;
import org.laokou.flowable.dto.definition.DefinitionSuspendCmd;
import org.springframework.stereotype.Component;

import static org.laokou.common.i18n.common.DatasourceConstants.FLOWABLE;

/**
 * @author laokou
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class DefinitionSuspendCmdExe {

	private final RepositoryService repositoryService;

	private final TransactionalUtil transactionalUtil;

	public Result<Boolean> execute(DefinitionSuspendCmd cmd) {
		try {
			DynamicDataSourceContextHolder.push(FLOWABLE);
			String definitionId = cmd.getDefinitionId();
			final ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery()
				.processDefinitionTenantId(UserUtil.getTenantId().toString())
				.processDefinitionId(definitionId)
				.singleResult();
			if (!processDefinition.isSuspended()) {
				return Result.of(suspend(definitionId));
			}
			else {
				throw new FlowException("挂起失败，流程已挂起");
			}
		}
		finally {
			DynamicDataSourceContextHolder.clear();
		}
	}

	private Boolean suspend(String definitionId) {
		return transactionalUtil.defaultExecute(r -> {
			try {
				// 挂起
				repositoryService.suspendProcessDefinitionById(definitionId, true, null);
				return true;
			}
			catch (Exception e) {
				log.error("错误信息：{}，详情见日志", LogUtil.result(e.getMessage()), e);
				r.setRollbackOnly();
				throw new SystemException(LogUtil.fail(e.getMessage()));
			}
		});
	}

}
