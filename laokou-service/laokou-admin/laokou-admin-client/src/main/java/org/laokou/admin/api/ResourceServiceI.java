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
package org.laokou.admin.api;

import org.laokou.admin.dto.oss.clientobject.FileCO;
import org.laokou.admin.dto.resource.clientobject.ResourceCO;
import org.laokou.admin.dto.resource.clientobject.TaskCO;
import org.laokou.admin.dto.resource.*;
import org.laokou.common.i18n.dto.Datas;
import org.laokou.common.i18n.dto.Result;

import java.util.Map;

/**
 * @author laokou
 */
public interface ResourceServiceI {

	Result<Datas<?>> auditLog(ResourceAuditLogListQry qry);

	Result<Boolean> sync(ResourceSyncCmd cmd);

	Result<FileCO> upload(ResourceUploadCmd cmd);

	Result<Datas<ResourceCO>> list(ResourceListQry qry);

	Result<ResourceCO> getById(ResourceGetQry qry);

	Result<Boolean> download(ResourceDownloadCmd cmd);

	Result<Boolean> insert(ResourceInsertCmd cmd);

	Result<Boolean> update(ResourceUpdateCmd cmd);

	Result<Boolean> deleteById(ResourceDeleteCmd cmd);

	Result<String> diagram(ResourceDiagramGetQry qry);

	Result<Datas<TaskCO>> taskList(ResourceTaskListQry qry);

	Result<Boolean> auditTask(ResourceAuditTaskCmd cmd);

	Result<TaskCO> detailTask(ResourceDetailTaskGetQry qry);

	Result<Boolean> resolveTask(ResourceResolveTaskCmd cmd);

	Result<Boolean> transferTask(ResourceTransferTaskCmd cmd);

	Result<Boolean> delegateTask(ResourceDelegateTaskCmd cmd);

	Result<Datas<Map<String, Object>>> search(ResourceSearchGetQry qry);

}
