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

package org.laokou.flowable.api;

import org.laokou.common.i18n.dto.Datas;
import org.laokou.common.i18n.dto.Result;
import org.laokou.flowable.dto.task.*;
import org.laokou.flowable.dto.task.clientobject.AssigneeCO;
import org.laokou.flowable.dto.task.clientobject.TaskCO;

/**
 * 任务流程.
 *
 * @author laokou
 */
public interface TasksServiceI {

	/**
	 * 查询任务流程列表.
	 * @param qry 查询任务流程列表参数
	 * @return 任务流程列表
	 */
	Result<Datas<TaskCO>> list(TaskListQry qry);

	/**
	 * 审批任务流程.
	 * @param cmd 审批任务流程参数
	 */
	Result<Boolean> audit(TaskAuditCmd cmd);

	/**
	 * 处理任务流程.
	 * @param cmd 处理任务流程参数
	 * @return 处理结果
	 */
	Result<Boolean> resolve(TaskResolveCmd cmd);

	/**
	 * 开始任务流程.
	 * @param cmd 开始任务流程
	 */
	Result<Boolean> start(TaskStartCmd cmd);

	/**
	 * 查看任务流程图.
	 * @param qry 查看任务流程图参数
	 * @return 流程图
	 */
	Result<String> diagram(TaskDiagramGetQry qry);

	/**
	 * 转办任务流程.
	 * @param cmd 转办任务流程参数
	 * @return 转办结果
	 */
	Result<Boolean> transfer(TaskTransferCmd cmd);

	/**
	 * 委派任务流程.
	 * @param cmd 委派任务流程
	 * @return 委派结果
	 */
	Result<Boolean> delegate(TaskDelegateCmd cmd);

	/**
	 * 查看执行人员.
	 * @param qry 查看执行人员参数
	 * @return 执行人员
	 */
	Result<AssigneeCO> assignee(TaskAssigneeGetQry qry);

}
