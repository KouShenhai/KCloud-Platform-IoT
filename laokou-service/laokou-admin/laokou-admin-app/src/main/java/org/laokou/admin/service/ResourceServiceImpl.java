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

package org.laokou.admin.service;

import lombok.RequiredArgsConstructor;
import org.laokou.admin.api.ResourceServiceI;
import org.laokou.admin.command.oss.OssUploadCmdExe;
import org.laokou.admin.dto.oss.OssUploadCmd;
import org.laokou.admin.dto.oss.clientobject.FileCO;
import org.laokou.admin.dto.resource.*;
import org.laokou.admin.dto.resource.clientobject.AuditLogCO;
import org.laokou.admin.dto.resource.clientobject.ResourceCO;
import org.laokou.admin.dto.resource.clientobject.TaskCO;
import org.laokou.admin.command.resource.*;
import org.laokou.admin.command.resource.query.*;
import org.laokou.common.i18n.dto.Datas;
import org.laokou.common.i18n.dto.Result;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * @author laokou
 */
@Service
@RequiredArgsConstructor
public class ResourceServiceImpl implements ResourceServiceI {

	private final ResourceAuditLogListQryExe resourceAuditLogListQryExe;

	private final ResourceSyncCmdExe resourceSyncCmdExe;

	private final ResourceListQryExe resourceListQryExe;

	private final ResourceGetQryExe resourceGetQryExe;

	private final OssUploadCmdExe ossUploadCmdExe;

	private final ResourceDownloadCmdExe resourceDownloadCmdExe;

	private final ResourceInsertCmdExe resourceInsertCmdExe;

	private final ResourceUpdateCmdExe resourceUpdateCmdExe;

	private final ResourceDeleteCmdExe resourceDeleteCmdExe;

	private final ResourceDiagramGetQryExe resourceDiagramGetQryExe;

	private final ResourceTaskListQryExe resourceTaskListQryExe;

	private final ResourceAuditTaskCmdExe resourceAuditTaskCmdExe;

	private final ResourceDetailTaskGetQryExe resourceDetailTaskGetQryExe;

	private final ResourceResolveTaskCmdExe resourceResolveTaskCmdExe;

	private final ResourceTransferTaskCmdExe resourceTransferTaskCmdExe;

	private final ResourceDelegateTaskCmdExe resourceDelegateTaskCmdExe;

	private final ResourceSearchGetQryExe resourceSearchGetQryExe;

	@Override
	public Result<List<AuditLogCO>> auditLog(ResourceAuditLogListQry qry) {
		return resourceAuditLogListQryExe.execute(qry);
	}

	@Override
	public Result<Boolean> sync(ResourceSyncCmd cmd) {
		return resourceSyncCmdExe.execute(cmd);
	}

	@Override
	public Result<FileCO> upload(OssUploadCmd cmd) {
		return ossUploadCmdExe.execute(cmd);
	}

	@Override
	public Result<Datas<ResourceCO>> list(ResourceListQry qry) {
		return resourceListQryExe.execute(qry);
	}

	@Override
	public Result<ResourceCO> getById(ResourceGetQry qry) {
		return resourceGetQryExe.execute(qry);
	}

	@Override
	public void download(ResourceDownloadCmd cmd) {
		resourceDownloadCmdExe.executeVoid(cmd);
	}

	@Override
	public Result<Boolean> insert(ResourceInsertCmd cmd) {
		return resourceInsertCmdExe.execute(cmd);
	}

	@Override
	public Result<Boolean> update(ResourceUpdateCmd cmd) {
		return resourceUpdateCmdExe.execute(cmd);
	}

	@Override
	public Result<Boolean> deleteById(ResourceDeleteCmd cmd) {
		return resourceDeleteCmdExe.execute(cmd);
	}

	@Override
	public Result<String> diagram(ResourceDiagramGetQry qry) {
		return resourceDiagramGetQryExe.execute(qry);
	}

	@Override
	public Result<Datas<TaskCO>> taskList(ResourceTaskListQry qry) {
		return resourceTaskListQryExe.execute(qry);
	}

	@Override
	public Result<Boolean> auditTask(ResourceAuditTaskCmd cmd) {
		return resourceAuditTaskCmdExe.execute(cmd);
	}

	@Override
	public Result<ResourceCO> detailTask(ResourceDetailTaskGetQry qry) {
		return resourceDetailTaskGetQryExe.execute(qry);
	}

	@Override
	public Result<Boolean> resolveTask(ResourceResolveTaskCmd cmd) {
		return resourceResolveTaskCmdExe.execute(cmd);
	}

	@Override
	public Result<Boolean> transferTask(ResourceTransferTaskCmd cmd) {
		return resourceTransferTaskCmdExe.execute(cmd);
	}

	@Override
	public Result<Boolean> delegateTask(ResourceDelegateTaskCmd cmd) {
		return resourceDelegateTaskCmdExe.execute(cmd);
	}

	@Override
	public Result<Datas<Map<String, Object>>> search(ResourceSearchGetQry qry) {
		return resourceSearchGetQryExe.execute(qry);
	}

}
