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

package org.laokou.admin.api;

import org.laokou.admin.dto.resource.*;
import org.laokou.admin.dto.resource.clientobject.AuditLogCO;
import org.laokou.admin.dto.resource.clientobject.ResourceCO;
import org.laokou.admin.dto.resource.clientobject.TaskCO;
import org.laokou.common.i18n.dto.Datas;
import org.laokou.common.i18n.dto.Result;

import java.util.List;
import java.util.Map;

/**
 * 资源管理.
 *
 * @author laokou
 */
public interface ResourceServiceI {

	/**
	 * 查询审批日志列表.
	 * @param qry 查询审批日志列表参数
	 * @return 审批日志列表
	 */
	Result<List<AuditLogCO>> auditLog(ResourceAuditLogListQry qry);

	/**
	 * 同步资源.
	 * @param cmd 同步资源参数
	 * @return 同步结果
	 */
	Result<Boolean> sync(ResourceSyncCmd cmd);

	/**
	 * 查询资源列表.
	 * @param qry 查询资源列表参数
	 * @return 资源列表
	 */
	Result<Datas<ResourceCO>> findList(ResourceListQry qry);

	/**
	 * 根据ID查看资源.
	 * @param qry 根据ID查看资源参数
	 * @return 资源
	 */
	Result<ResourceCO> findById(ResourceGetQry qry);

	/**
	 * 下载资源.
	 * @param cmd 下载资源参数
	 */
	void download(ResourceDownloadCmd cmd);

	/**
	 * 新增资源.
	 * @param cmd 新增资源
	 * @return 新增结果
	 */
	Result<Boolean> insert(ResourceCreateCmd cmd);

	/**
	 * 修改资源.
	 * @param cmd 修改资源
	 */
	void modify(ResourceModifyCmd cmd);

	/**
	 * 根据ID删除资源.
	 * @param cmd 根据ID删除资源参数
	 * @return 删除结果
	 */
	Result<Boolean> deleteById(ResourceRemoveCmd cmd);

	/**
	 * 查看资源任务流程图.
	 * @param qry 查看资源任务流程图参数
	 * @return 流程图
	 */
	Result<String> diagram(ResourceDiagramGetQry qry);

	/**
	 * 查询资源审批任务流程列表.
	 * @param qry 查询资源审批任务流程列表参数
	 * @return 资源审批任务流程列表
	 */
	Result<Datas<TaskCO>> taskList(ResourceTaskListQry qry);

	/**
	 * 审批资源任务流程.
	 * @param cmd 审批资源任务流程参数
	 * @return 审批结果
	 */
	Result<Boolean> auditTask(ResourceAuditTaskCmd cmd);

	/**
	 * 查看资源任务流程详情.
	 * @param qry 查看资源任务流程详情参数
	 * @return 资源任务流程详情
	 */
	Result<ResourceCO> detailTask(ResourceDetailTaskGetQry qry);

	/**
	 * 处理资源任务流程.
	 * @param cmd 处理资源任务流程参数
	 * @return 处理结果
	 */
	Result<Boolean> resolveTask(ResourceResolveTaskCmd cmd);

	/**
	 * 转办资源任务流程.
	 * @param cmd 转办资源任务流程参数
	 * @return 转办结果
	 */
	Result<Boolean> transferTask(ResourceTransferTaskCmd cmd);

	/**
	 * 委派资源任务流程.
	 * @param cmd 委派资源任务流程参数
	 * @return 委派结果
	 */
	Result<Boolean> delegateTask(ResourceDelegateTaskCmd cmd);

	/**
	 * 搜索资源.
	 * @param qry 搜索资源参数
	 * @return 搜索结果
	 */
	Result<Datas<Map<String, Object>>> search(ResourceSearchGetQry qry);

}
