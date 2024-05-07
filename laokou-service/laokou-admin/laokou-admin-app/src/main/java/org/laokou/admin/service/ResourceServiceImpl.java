/*
 * Copyright (c) 2022-2024 KCloud-Platform-IoT Author or Authors. All Rights Reserved.
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

package org.laokou.admin.service;

import lombok.RequiredArgsConstructor;
import org.laokou.admin.api.ResourceServiceI;
import org.laokou.admin.command.resource.*;
import org.laokou.admin.command.resource.query.*;
import org.laokou.admin.dto.resource.*;
import org.laokou.admin.dto.resource.clientobject.AuditLogCO;
import org.laokou.admin.dto.resource.clientobject.ResourceCO;
import org.laokou.admin.dto.resource.clientobject.TaskCO;
import org.laokou.common.i18n.dto.Datas;
import org.laokou.common.i18n.dto.Result;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * 资源管理.
 *
 * @author laokou
 */
@Service
@RequiredArgsConstructor
public class ResourceServiceImpl implements ResourceServiceI {

	private final ResourceAuditLogListQryExe resourceAuditLogListQryExe;

	private final ResourceSyncCmdExe resourceSyncCmdExe;

	private final ResourceListQryExe resourceListQryExe;

	private final ResourceGetQryExe resourceGetQryExe;

	private final ResourceDownloadCmdExe resourceDownloadCmdExe;

	private final ResourceCreateCmdExe resourceCreateCmdExe;

	private final ResourceModifyCmdExe resourceModifyCmdExe;

	private final ResourceRemoveCmdExe resourceRemoveCmdExe;

	private final ResourceDiagramGetQryExe resourceDiagramGetQryExe;

	private final ResourceTaskListQryExe resourceTaskListQryExe;

	private final ResourceAuditTaskCmdExe resourceAuditTaskCmdExe;

	private final ResourceDetailTaskGetQryExe resourceDetailTaskGetQryExe;

	private final ResourceResolveTaskCmdExe resourceResolveTaskCmdExe;

	private final ResourceTransferTaskCmdExe resourceTransferTaskCmdExe;

	private final ResourceDelegateTaskCmdExe resourceDelegateTaskCmdExe;

	private final ResourceSearchGetQryExe resourceSearchGetQryExe;

	/**
	 * 查询审批日志列表.
	 * @param qry 查询审批日志列表参数
	 * @return 审批日志列表
	 */
	@Override
	public Result<List<AuditLogCO>> auditLog(ResourceAuditLogListQry qry) {
		return resourceAuditLogListQryExe.execute(qry);
	}

	/**
	 * 同步资源.
	 * @param cmd 同步资源参数
	 * @return 同步结果
	 */
	@Override
	public Result<Boolean> sync(ResourceSyncCmd cmd) {
		return resourceSyncCmdExe.execute(cmd);
	}

	/**
	 * 查询资源列表.
	 * @param qry 查询资源列表参数
	 * @return 资源列表
	 */
	@Override
	public Result<Datas<ResourceCO>> findList(ResourceListQry qry) {
		return resourceListQryExe.execute(qry);
	}

	/**
	 * 根据ID查看资源.
	 * @param qry 根据ID查看资源参数
	 * @return 资源
	 */
	@Override
	public Result<ResourceCO> findById(ResourceGetQry qry) {
		return resourceGetQryExe.execute(qry);
	}

	/**
	 * 下载资源.
	 * @param cmd 下载资源参数
	 */
	@Override
	public void download(ResourceDownloadCmd cmd) {
		resourceDownloadCmdExe.executeVoid(cmd);
	}

	/**
	 * 新增资源.
	 * @param cmd 新增资源
	 * @return 新增结果
	 */
	@Override
	public Result<Boolean> insert(ResourceCreateCmd cmd) {
		return resourceCreateCmdExe.execute(cmd);
	}

	/**
	 * 修改资源.
	 * @param cmd 修改资源
	 */
	@Override
	public void modify(ResourceModifyCmd cmd) {
		resourceModifyCmdExe.executeVoid(cmd);
	}

	/**
	 * 根据ID删除资源.
	 * @param cmd 根据ID删除资源参数
	 * @return 删除结果
	 */
	@Override
	public Result<Boolean> deleteById(ResourceRemoveCmd cmd) {
		return resourceRemoveCmdExe.execute(cmd);
	}

	/**
	 * 查看资源任务流程图.
	 * @param qry 查看资源任务流程图参数
	 * @return 流程图
	 */
	@Override
	public Result<String> diagram(ResourceDiagramGetQry qry) {
		return resourceDiagramGetQryExe.execute(qry);
	}

	/**
	 * 查询资源审批任务流程列表.
	 * @param qry 查询资源审批任务流程列表参数
	 * @return 资源审批任务流程列表
	 */
	@Override
	public Result<Datas<TaskCO>> taskList(ResourceTaskListQry qry) {
		return resourceTaskListQryExe.execute(qry);
	}

	/**
	 * 审批资源任务流程.
	 * @param cmd 审批资源任务流程参数
	 * @return 审批结果
	 */
	@Override
	public Result<Boolean> auditTask(ResourceAuditTaskCmd cmd) {
		return resourceAuditTaskCmdExe.execute(cmd);
	}

	/**
	 * 查看资源任务流程详情.
	 * @param qry 查看资源任务流程详情参数
	 * @return 资源任务流程详情
	 */
	@Override
	public Result<ResourceCO> detailTask(ResourceDetailTaskGetQry qry) {
		return resourceDetailTaskGetQryExe.execute(qry);
	}

	/**
	 * 处理资源任务流程.
	 * @param cmd 处理资源任务流程参数
	 * @return 处理结果
	 */
	@Override
	public Result<Boolean> resolveTask(ResourceResolveTaskCmd cmd) {
		return resourceResolveTaskCmdExe.execute(cmd);
	}

	/**
	 * 转办资源任务流程.
	 * @param cmd 转办资源任务流程参数
	 * @return 转办结果
	 */
	@Override
	public Result<Boolean> transferTask(ResourceTransferTaskCmd cmd) {
		return resourceTransferTaskCmdExe.execute(cmd);
	}

	/**
	 * 委派资源任务流程.
	 * @param cmd 委派资源任务流程参数
	 * @return 委派结果
	 */
	@Override
	public Result<Boolean> delegateTask(ResourceDelegateTaskCmd cmd) {
		return resourceDelegateTaskCmdExe.execute(cmd);
	}

	/**
	 * 搜索资源.
	 * @param qry 搜索资源参数
	 * @return 搜索结果
	 */
	@Override
	public Result<Datas<Map<String, Object>>> search(ResourceSearchGetQry qry) {
		return resourceSearchGetQryExe.execute(qry);
	}

}
