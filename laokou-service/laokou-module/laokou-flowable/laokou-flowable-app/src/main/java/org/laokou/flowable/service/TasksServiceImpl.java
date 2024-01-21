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
 * 任务流程.
 *
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

	/**
	 * 查询任务流程列表.
	 * @param qry 查询任务流程列表参数
	 * @return 任务流程列表
	 */
	@Override
	public Result<Datas<TaskCO>> list(TaskListQry qry) {
		return taskListQryExe.execute(qry);
	}

	/**
	 * 审批任务流程.
	 * @param cmd 审批任务流程参数
	 * @return 审批结果
	 */
	@Override
	public Result<AuditCO> audit(TaskAuditCmd cmd) {
		return taskAuditCmdExe.execute(cmd);
	}

	/**
	 * 处理任务流程.
	 * @param cmd 处理任务流程参数
	 * @return 处理结果
	 */
	@Override
	public Result<Boolean> resolve(TaskResolveCmd cmd) {
		return taskResolveCmdExe.execute(cmd);
	}

	/**
	 * 开始任务流程.
	 * @param cmd 开始任务流程
	 * @return 开始结果
	 */
	@Override
	public Result<StartCO> start(TaskStartCmd cmd) {
		return taskStartCmdExe.execute(cmd);
	}

	/**
	 * 查看任务流程图.
	 * @param qry 查看任务流程图参数
	 * @return 流程图
	 */
	@Override
	public Result<String> diagram(TaskDiagramGetQry qry) {
		return taskDiagramGetQryExe.execute(qry);
	}

	/**
	 * 转办任务流程.
	 * @param cmd 转办任务流程参数
	 * @return 转办结果
	 */
	@Override
	public Result<Boolean> transfer(TaskTransferCmd cmd) {
		return taskTransferCmdExe.execute(cmd);
	}

	/**
	 * 委派任务流程.
	 * @param cmd 委派任务流程
	 * @return 委派结果
	 */
	@Override
	public Result<Boolean> delegate(TaskDelegateCmd cmd) {
		return taskDelegateCmdExe.execute(cmd);
	}

	/**
	 * 查看执行人员.
	 * @param qry 查看执行人员参数
	 * @return 执行人员
	 */
	@Override
	public Result<AssigneeCO> assignee(TaskAssigneeGetQry qry) {
		return taskAssigneeGetQryExe.execute(qry);
	}

}
