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
package org.laokou.admin.web;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.laokou.admin.api.ResourceServiceI;
import org.laokou.admin.dto.oss.clientobject.FileCO;
import org.laokou.admin.dto.resource.*;
import org.laokou.admin.dto.resource.clientobject.ResourceCO;
import org.laokou.admin.dto.resource.clientobject.TaskCO;
import org.laokou.admin.domain.annotation.OperateLog;
import org.laokou.common.i18n.dto.Datas;
import org.laokou.common.i18n.dto.Result;
import org.laokou.common.lock.annotation.Lock4j;
import org.laokou.common.trace.annotation.TraceLog;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

/**
 * @author laokou
 */
@RestController
@Tag(name = "ResourceController", description = "资源管理")
@RequiredArgsConstructor
@RequestMapping("v1/resource")
public class ResourceController {

	private final ResourceServiceI resourceServiceI;

	@GetMapping("{id}/audit-log")
	@TraceLog
	@Operation(summary = "资源管理", description = "查询审批日志列表")
	@PreAuthorize("hasAuthority('resource:audit-log')")
	public Result<Datas<?>> auditLog(@PathVariable("id") Long id) {
		return resourceServiceI.auditLog(new ResourceAuditLogListQry(id));
	}

	@PostMapping("sync")
	@TraceLog
	@Operation(summary = "资源管理", description = "同步资源")
	@OperateLog(module = "资源管理", operation = "同步资源")
	@Lock4j(key = "resource_sync_lock_")
	@PreAuthorize("hasAuthority('resource:sync')")
	public Result<Boolean> sync() {
		return resourceServiceI.sync(new ResourceSyncCmd());
	}

	@PostMapping(value = "upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	@TraceLog
	@Operation(summary = "资源管理", description = "上传资源")
	@OperateLog(module = "资源管理", operation = "上传资源")
	public Result<FileCO> upload(@RequestPart("file") MultipartFile file) {
		return resourceServiceI.upload(new ResourceUploadCmd(file));
	}

	@PostMapping("list")
	@Operation(summary = "资源管理", description = "查询资源列表")
	@TraceLog
	@PreAuthorize("hasAuthority('resource:list')")
	public Result<Datas<ResourceCO>> list(@RequestBody ResourceListQry qry) {
		return resourceServiceI.list(qry);
	}

	@GetMapping(value = "{id}")
	@Operation(summary = "资源管理", description = "查看资源")
	@TraceLog
	@PreAuthorize("hasAuthority('resource:detail')")
	public Result<ResourceCO> getById(@PathVariable("id") Long id) {
		return resourceServiceI.getById(new ResourceGetQry(id));
	}

	@GetMapping(value = "{id}/download")
	@TraceLog
	@Operation(summary = "资源管理", description = "下载资源")
	@PreAuthorize("hasAuthority('resource:download')")
	public Result<Boolean> download(@PathVariable("id") Long id, HttpServletResponse response) {
		return resourceServiceI.download(new ResourceDownloadCmd(id, response));
	}

	@PostMapping
	@TraceLog
	@Operation(summary = "资源管理", description = "新增资源")
	@OperateLog(module = "资源管理", operation = "新增资源")
	@PreAuthorize("hasAuthority('resource:insert')")
	public Result<Boolean> insert(@RequestBody ResourceInsertCmd cmd) throws IOException {
		return resourceServiceI.insert(cmd);
	}

	@PutMapping
	@TraceLog
	@Operation(summary = "资源管理", description = "修改资源")
	@OperateLog(module = "资源管理", operation = "修改资源")
	@PreAuthorize("hasAuthority('resource:update')")
	public Result<Boolean> update(@RequestBody ResourceUpdateCmd cmd) throws IOException {
		return resourceServiceI.update(cmd);
	}

	@DeleteMapping(value = "{id}")
	@TraceLog
	@Operation(summary = "资源管理", description = "删除资源")
	@OperateLog(module = "资源管理", operation = "删除资源")
	@PreAuthorize("hasAuthority('resource:delete')")
	public Result<Boolean> deleteById(@PathVariable("id") Long id) {
		return resourceServiceI.deleteById(new ResourceDeleteCmd(id));
	}

	@GetMapping(value = "{instanceId}/diagram")
	@TraceLog
	@Operation(summary = "资源管理", description = "流程图")
	@PreAuthorize("hasAuthority('resource:diagram')")
	public Result<String> diagram(@PathVariable("instanceId") String instanceId) {
		return resourceServiceI.diagram(new ResourceDiagramGetQry(instanceId));
	}

	@TraceLog
	@PostMapping(value = "task-list")
	@Operation(summary = "资源管理", description = "查询任务列表")
	@PreAuthorize("hasAuthority('resource:task-list')")
	public Result<Datas<TaskCO>> taskList(@RequestBody ResourceTaskListQry qry) {
		return resourceServiceI.taskList(qry);
	}

	@TraceLog
	@PostMapping(value = "audit-task")
	@Operation(summary = "资源管理", description = "审批任务")
	@OperateLog(module = "资源管理", operation = "审批任务")
	@PreAuthorize("hasAuthority('resource:audit-task')")
	public Result<Boolean> auditTask(@RequestBody ResourceAuditTaskCmd cmd) {
		return resourceServiceI.auditTask(cmd);
	}

	@TraceLog
	@GetMapping(value = "{id}/detail-task")
	@Operation(summary = "资源管理", description = "查看任务")
	public Result<TaskCO> detailTask(@PathVariable("id") Long id) {
		return resourceServiceI.detailTask(new ResourceDetailTaskGetQry(id));
	}

	@TraceLog
	@PostMapping(value = "resolve-task")
	@Operation(summary = "资源管理", description = "处理任务")
	@OperateLog(module = "资源管理", operation = "处理任务")
	@PreAuthorize("hasAuthority('resource:resolve-task')")
	public Result<Boolean> resolveTask(@RequestBody ResourceResolveTaskCmd cmd) {
		return resourceServiceI.resolveTask(cmd);
	}

	@TraceLog
	@PostMapping(value = "transfer-task")
	@Operation(summary = "资源管理", description = "转办任务")
	@OperateLog(module = "资源管理", operation = "转办任务")
	@PreAuthorize("hasAuthority('resource:transfer-task')")
	public Result<Boolean> transferTask(@RequestBody ResourceTransferTaskCmd cmd) {
		return resourceServiceI.transferTask(cmd);
	}

	@TraceLog
	@PostMapping(value = "delegate-task")
	@Operation(summary = "资源管理", description = "委派任务")
	@OperateLog(module = "资源管理", operation = "委派任务")
	@PreAuthorize("hasAuthority('resource:delegate-task')")
	public Result<Boolean> delegateTask(@RequestBody ResourceDelegateTaskCmd cmd) {
		return resourceServiceI.delegateTask(cmd);
	}

	@TraceLog
	@PostMapping("search")
	@Operation(summary = "资源管理", description = "搜索资源")
	@PreAuthorize("hasAuthority('resource:search')")
	public Result<Datas<Map<String, Object>>> search(@RequestBody ResourceSearchGetQry qry) {
		return resourceServiceI.search(qry);
	}

}
