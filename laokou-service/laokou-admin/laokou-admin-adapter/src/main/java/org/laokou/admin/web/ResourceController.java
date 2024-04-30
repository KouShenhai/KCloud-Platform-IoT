/*
 * Copyright (c) 2022-2024 KCloud-Platform-IOT Author or Authors. All Rights Reserved.
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
import org.laokou.admin.dto.resource.*;
import org.laokou.admin.dto.resource.clientobject.AuditLogCO;
import org.laokou.admin.dto.resource.clientobject.ResourceCO;
import org.laokou.admin.dto.resource.clientobject.TaskCO;
import org.laokou.common.i18n.dto.Datas;
import org.laokou.common.i18n.dto.Result;
import org.laokou.common.idempotent.annotation.Idempotent;
import org.laokou.common.log.annotation.OperateLog;
import org.laokou.common.trace.annotation.TraceLog;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
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
	public Result<List<AuditLogCO>> findAuditLog(@PathVariable("id") Long id) {
		return resourceServiceI.auditLog(new ResourceAuditLogListQry(id));
	}

	// @PostMapping("sync")
	// @RateLimiter(id = "RESOURCE_SYNC", unit = RateIntervalUnit.MINUTES)
	// @TraceLog
	// @Operation(summary = "资源管理", description = "同步资源")
	// @OperateLog(module = "资源管理", operation = "同步资源")
	// @Lock4j(key = "resource_sync_lock", expire = 60000)
	// @PreAuthorize("hasAuthority('resource:sync')")
	// public Result<Boolean> sync() {
	// return resourceServiceI.sync(new ResourceSyncCmd());
	// }

	@PostMapping("list")
	@Operation(summary = "资源管理", description = "查询资源列表")
	@TraceLog
	@PreAuthorize("hasAuthority('resource:list')")
	public Result<Datas<ResourceCO>> findList(@RequestBody ResourceListQry qry) {
		return resourceServiceI.findList(qry);
	}

	@GetMapping("{id}")
	@Operation(summary = "资源管理", description = "查看资源")
	@TraceLog
	@PreAuthorize("hasAuthority('resource:detail')")
	public Result<ResourceCO> findById(@PathVariable("id") Long id) {
		return resourceServiceI.findById(new ResourceGetQry(id));
	}

	@GetMapping("{id}/download")
	@Operation(summary = "资源管理", description = "下载资源")
	@PreAuthorize("hasAuthority('resource:download')")
	public void download(@PathVariable("id") Long id, HttpServletResponse response) {
		resourceServiceI.download(new ResourceDownloadCmd(id, response));
	}

	@Idempotent
	@PostMapping
	@Operation(summary = "资源管理", description = "新增资源")
	@OperateLog(module = "资源管理", operation = "新增资源")
	@PreAuthorize("hasAuthority('resource:create')")
	public void create(@Validated @RequestBody ResourceCreateCmd cmd) throws IOException {
		resourceServiceI.insert(cmd);
	}

	@Idempotent
	@PutMapping
	@Operation(summary = "资源管理", description = "修改资源")
	@OperateLog(module = "资源管理", operation = "修改资源")
	@PreAuthorize("hasAuthority('resource:modify')")
	public void modify(@Validated @RequestBody ResourceModifyCmd cmd) throws IOException {
		resourceServiceI.modify(cmd);
	}

	@DeleteMapping("{id}")
	@Operation(summary = "资源管理", description = "删除资源")
	@OperateLog(module = "资源管理", operation = "删除资源")
	@PreAuthorize("hasAuthority('resource:remove')")
	public void remove(@PathVariable("id") Long id) {
		resourceServiceI.deleteById(new ResourceRemoveCmd(id));
	}

	@GetMapping("{instanceId}/diagram")
	@TraceLog
	@Operation(summary = "资源管理", description = "查看流程")
	@PreAuthorize("hasAuthority('resource:diagram')")
	public Result<String> findDiagram(@PathVariable("instanceId") String instanceId) {
		return resourceServiceI.diagram(new ResourceDiagramGetQry(instanceId));
	}

	@TraceLog
	@PostMapping("task-list")
	@Operation(summary = "资源管理", description = "查询任务列表")
	@PreAuthorize("hasAuthority('resource:task-list')")
	public Result<Datas<TaskCO>> findTaskList(@RequestBody ResourceTaskListQry qry) {
		return resourceServiceI.taskList(qry);
	}

	@Idempotent
	@TraceLog
	@PostMapping("audit-task")
	@Operation(summary = "资源管理", description = "审批任务")
	@OperateLog(module = "资源管理", operation = "审批任务")
	@PreAuthorize("hasAuthority('resource:audit-task')")
	public Result<Boolean> auditTask(@RequestBody ResourceAuditTaskCmd cmd) {
		return resourceServiceI.auditTask(cmd);
	}

	@TraceLog
	@GetMapping("{id}/detail-task")
	@Operation(summary = "资源管理", description = "查看任务")
	public Result<ResourceCO> findTaskInfo(@PathVariable("id") Long id) {
		return resourceServiceI.detailTask(new ResourceDetailTaskGetQry(id));
	}

	@Idempotent
	@TraceLog
	@PostMapping("resolve-task")
	@Operation(summary = "资源管理", description = "处理任务")
	@OperateLog(module = "资源管理", operation = "处理任务")
	@PreAuthorize("hasAuthority('resource:resolve-task')")
	public Result<Boolean> resolveTask(@RequestBody ResourceResolveTaskCmd cmd) {
		return resourceServiceI.resolveTask(cmd);
	}

	@Idempotent
	@TraceLog
	@PostMapping("transfer-task")
	@Operation(summary = "资源管理", description = "转办任务")
	@OperateLog(module = "资源管理", operation = "转办任务")
	@PreAuthorize("hasAuthority('resource:transfer-task')")
	public Result<Boolean> transferTask(@RequestBody ResourceTransferTaskCmd cmd) {
		return resourceServiceI.transferTask(cmd);
	}

	@Idempotent
	@TraceLog
	@PostMapping("delegate-task")
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
	public Result<Datas<Map<String, Object>>> search(@Validated @RequestBody ResourceSearchGetQry qry) {
		return resourceServiceI.search(qry);
	}

}
