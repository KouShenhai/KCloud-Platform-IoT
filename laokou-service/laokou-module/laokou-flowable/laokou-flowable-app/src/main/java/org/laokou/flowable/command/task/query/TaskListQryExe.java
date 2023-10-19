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

package org.laokou.flowable.command.task.query;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import org.laokou.common.core.utils.ConvertUtil;
import org.laokou.common.i18n.dto.Datas;
import org.laokou.common.i18n.dto.Result;
import org.laokou.flowable.dto.task.TaskListQry;
import org.laokou.flowable.dto.task.clientobject.TaskCO;
import org.laokou.flowable.gatewayimpl.database.TaskMapper;
import org.laokou.flowable.gatewayimpl.database.dataobject.TaskDO;
import org.springframework.stereotype.Component;

import static org.laokou.flowable.common.Constant.FLOWABLE;

/**
 * @author laokou
 */
@Component
@RequiredArgsConstructor
public class TaskListQryExe {

	private final TaskMapper taskMapper;

	@DS(FLOWABLE)
	public Result<Datas<TaskCO>> execute(TaskListQry qry) {
		String key = qry.getKey();
		String name = qry.getName();
		Long userId = qry.getUserId();
		IPage<TaskDO> page = new Page<>(qry.getPageNum(), qry.getPageSize());
		IPage<TaskDO> newPage = taskMapper.getTaskList(page, key, userId, name);
		Datas<TaskCO> datas = new Datas<>();
		datas.setRecords(ConvertUtil.sourceToTarget(newPage.getRecords(), TaskCO.class));
		datas.setTotal(newPage.getTotal());
		return Result.of(datas);
	}

}
