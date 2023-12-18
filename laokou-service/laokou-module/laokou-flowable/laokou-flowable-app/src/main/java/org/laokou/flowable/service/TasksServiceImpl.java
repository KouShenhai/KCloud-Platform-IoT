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

package org.laokou.flowable.service;

import lombok.RequiredArgsConstructor;
import org.laokou.common.i18n.dto.Datas;
import org.laokou.common.i18n.dto.Result;
import org.laokou.flowable.api.TasksServiceI;
import org.laokou.flowable.command.task.*;
import org.laokou.flowable.command.task.query.TaskDiagramGetQryExe;
import org.laokou.flowable.command.task.query.TaskListQryExe;
import org.laokou.flowable.dto.task.*;
import org.laokou.flowable.dto.task.clientobject.AssigneeCO;
import org.laokou.flowable.dto.task.clientobject.AuditCO;
import org.laokou.flowable.dto.task.clientobject.StartCO;
import org.laokou.flowable.dto.task.clientobject.TaskCO;
import org.springframework.stereotype.Service;

/**
 * @author laokou
 */
@Service
@RequiredArgsConstructor
public class TasksServiceImpl implements TasksServiceI {

	private final TaskListQryExe taskListQryExe;

	private final TaskAuditCmdExe taskAuditCmdExe;

	private final TaskDelegateCmdExe taskDelegateCmdExe;

	private final TaskResolveCmdExe taskResolveCmdExe;

	private final TaskStartCmdExe taskStartCmdExe;

	private final TaskTransferCmdExe taskTransferCmdExe;

	private final TaskAssigneeGetQryExe taskAssigneeGetQryExe;

	private final TaskDiagramGetQryExe taskDiagramGetQryExe;

	@Override
	public Result<Datas<TaskCO>> list(TaskListQry qry) {
		return taskListQryExe.execute(qry);
	}

	@Override
	public Result<AuditCO> audit(TaskAuditCmd cmd) {
		return taskAuditCmdExe.execute(cmd);
	}

	@Override
	public Result<Boolean> resolve(TaskResolveCmd cmd) {
		return taskResolveCmdExe.execute(cmd);
	}

	@Override
	public Result<StartCO> start(TaskStartCmd cmd) {
		return taskStartCmdExe.execute(cmd);
	}

	@Override
	public Result<String> diagram(TaskDiagramGetQry qry) {
		return taskDiagramGetQryExe.execute(qry);
	}

	@Override
	public Result<Boolean> transfer(TaskTransferCmd cmd) {
		return taskTransferCmdExe.execute(cmd);
	}

	@Override
	public Result<Boolean> delegate(TaskDelegateCmd cmd) {
		return taskDelegateCmdExe.execute(cmd);
	}

	@Override
	public Result<AssigneeCO> assignee(TaskAssigneeGetQry qry) {
		return taskAssigneeGetQryExe.execute(qry);
	}

}
