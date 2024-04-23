/*
 * Copyright (c) 2022-2024 KCloud-Platform-Alibaba Author or Authors. All Rights Reserved.
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

import com.baomidou.dynamic.datasource.toolkit.DynamicDataSourceContextHolder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.laokou.common.i18n.dto.Result;
import org.laokou.common.security.utils.UserUtil;
import org.laokou.flowable.dto.task.TaskAssigneeGetQry;
import org.laokou.flowable.dto.task.clientobject.AssigneeCO;
import org.laokou.flowable.gatewayimpl.database.TaskMapper;
import org.springframework.stereotype.Component;

import static org.laokou.common.i18n.common.DatasourceConstant.FLOWABLE;

/**
 * 查看任务流程执行人员执行器.
 *
 * @author laokou
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class TaskAssigneeGetQryExe {

	private final TaskMapper taskMapper;

	/**
	 * 执行查看任务流程执行人员.
	 * @param qry 查看任务流程执行人员参数
	 * @return 执行人员
	 */
	public Result<AssigneeCO> execute(TaskAssigneeGetQry qry) {
		try {
			DynamicDataSourceContextHolder.push(FLOWABLE);
			return Result
				.ok(new AssigneeCO(taskMapper.getAssigneeByInstanceId(qry.getInstanceId(), UserUtil.getTenantId())));
		}
		finally {
			DynamicDataSourceContextHolder.clear();
		}
	}

}
