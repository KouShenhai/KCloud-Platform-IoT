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

package org.laokou.admin.command.resource.query;

import lombok.RequiredArgsConstructor;
import org.laokou.admin.config.DefaultConfigProperties;
import org.laokou.admin.dto.resource.ResourceTaskListQry;
import org.laokou.admin.dto.resource.TaskListQry;
import org.laokou.admin.dto.resource.clientobject.TaskCO;
import org.laokou.common.i18n.dto.Datas;
import org.laokou.common.i18n.dto.Result;
import org.laokou.common.security.utils.UserUtil;
import org.springframework.stereotype.Component;

/**
 * 查询资源任务列表执行器.
 *
 * @author laokou
 */
@Component
@RequiredArgsConstructor
public class ResourceTaskListQryExe {

	// private final TasksFeignClient tasksFeignClient;

	private final DefaultConfigProperties defaultConfigProperties;

	/**
	 * 执行查询资源任务列表.
	 * @param qry 查询资源任务列表参数
	 * @return 资源任务列表
	 */
	public Result<Datas<TaskCO>> execute(ResourceTaskListQry qry) {
		return null;
		// Datas<TaskCO> result = FeignUtil.result(tasksFeignClient.list(toQry(qry)));
		// List<TaskCO> records = result.getRecords();
		// String userName = UserUtil.getUserName();
		// if (CollectionUtil.isNotEmpty(records)) {
		// records.parallelStream().forEach(item -> item.setUsername(userName));
		// }
		// return Result.ok(result);
	}

	/**
	 * 转换成任务列表查询参数.
	 * @param qry 查询资源任务列表参数
	 * @return 任务列表命令请求
	 */
	private TaskListQry toQry(ResourceTaskListQry qry) {
		TaskListQry taskListQry = new TaskListQry();
		taskListQry.setName(qry.getName());
		taskListQry.setPageNum(qry.getPageNum());
		taskListQry.setPageSize(qry.getPageSize());
		taskListQry.setUserId(UserUtil.getUserId());
		taskListQry.setKey(defaultConfigProperties.getDefinitionKey());
		return taskListQry;
	}

}
