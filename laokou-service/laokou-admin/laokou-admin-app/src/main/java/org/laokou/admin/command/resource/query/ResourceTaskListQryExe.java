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

package org.laokou.admin.command.resource.query;

import lombok.RequiredArgsConstructor;
import org.laokou.admin.dto.resource.ResourceTaskListQry;
import org.laokou.admin.dto.resource.TaskListQry;
import org.laokou.admin.dto.resource.clientobject.TaskCO;
import org.laokou.admin.gatewayimpl.feign.TasksFeignClient;
import org.laokou.common.core.utils.CollectionUtil;
import org.laokou.common.i18n.dto.Datas;
import org.laokou.common.i18n.dto.Result;
import org.laokou.common.security.utils.UserUtil;
import org.springframework.stereotype.Component;

import java.util.List;

import static org.laokou.admin.common.Constant.KEY;

/**
 * @author laokou
 */
@Component
@RequiredArgsConstructor
public class ResourceTaskListQryExe {

	private final TasksFeignClient tasksFeignClient;

	public Result<Datas<TaskCO>> execute(ResourceTaskListQry qry) {
		Result<Datas<TaskCO>> result = tasksFeignClient.list(toQry(qry));
		List<TaskCO> records = result.getData().getRecords();
		String userName = UserUtil.getUserName();
		if (CollectionUtil.isNotEmpty(records)) {
			records.parallelStream().forEach(item -> item.setUsername(userName));
		}
		return result;
	}

	private TaskListQry toQry(ResourceTaskListQry qry) {
		TaskListQry taskListQry = new TaskListQry();
		taskListQry.setName(qry.getName());
		taskListQry.setPageNum(qry.getPageNum());
		taskListQry.setPageSize(qry.getPageSize());
		taskListQry.setUserId(UserUtil.getUserId());
		taskListQry.setKey(KEY);
		return taskListQry;
	}

}
