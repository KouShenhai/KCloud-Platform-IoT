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
import org.laokou.common.i18n.dto.Result;
import org.laokou.common.trace.annotation.TraceLog;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;

/**
 * @author laokou
 */
@RestController
@Tag(name = "ResourceController", description = "资源")
@RequiredArgsConstructor
public class ResourceController {

	@GetMapping("v1/resource/audit-log/{id}")
	@TraceLog
	@Operation(summary = "审批日志", description = "审批日志")
	// @PreAuthorize("hasAuthority('resource:audit:log')")
	public Result<?> auditLog(@PathVariable("id") Long id) {
		return Result.of(null);
	}

	@PostMapping("v1/resource/sync")
	@TraceLog
	@Operation(summary = "同步", description = "同步")
	// @OperateLog(module = "资源管理", name = "同步")
	// @Lock4j(key = "resource_sync_lock_")
	// @PreAuthorize("hasAuthority('resource:sync')")
	public Result<Boolean> sync() {
		return Result.of(null);
	}

	@PostMapping(value = "v1/resource/upload/{md5}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	@TraceLog
	@Operation(summary = "上传", description = "上传")
	public Result<?> upload(@RequestPart("file") MultipartFile file, @PathVariable("md5") String md5) throws Exception {
		return Result.of(null);
	}

	@PostMapping("v1/resource/list")
	@Operation(summary = "查询", description = "查询")
	@TraceLog
	// @PreAuthorize("hasAuthority('resource:list')")
	public Result<?> list() {
		return Result.of(null);
	}

	@GetMapping(value = "v1/resource/{id}")
	@Operation(summary = "查看", description = "查看")
	@TraceLog
	// @PreAuthorize("hasAuthority('resource:detail')")
	public Result<?> get(@PathVariable("id") Long id) {
		return Result.of(null);
	}

	@GetMapping(value = "v1/resource/download/{id}")
	@TraceLog
	@Operation(summary = "下载", description = "下载")
	// @PreAuthorize("hasAuthority('resource:download')")
	public void download(@PathVariable("id") Long id, HttpServletResponse response) {

	}

	@PostMapping(value = "v1/resource")
	@TraceLog
	@Operation(summary = "新增", description = "新增")
	// @OperateLog(module = "资源管理", name = "新增")
	// @PreAuthorize("hasAuthority('resource:insert')")
	public Result<Boolean> insert() throws IOException {
		return Result.of(null);
	}

	@PutMapping(value = "v1/resource")
	@TraceLog
	@Operation(summary = "修改", description = "修改")
	// @OperateLog(module = "资源管理", name = "修改")
	// @PreAuthorize("hasAuthority('resource:update')")
	public Result<Boolean> update() throws IOException {
		return Result.of(null);
	}

	@DeleteMapping(value = "v1/resource/{id}")
	@TraceLog
	@Operation(summary = "删除", description = "删除")
	// @OperateLog(module = "资源管理", name = "删除")
	// @PreAuthorize("hasAuthority('resource:delete')")
	public Result<Boolean> delete(@PathVariable("id") Long id) {
		return Result.of(null);
	}

	@GetMapping(value = "v1/resource/diagram/{instanceId}")
	@TraceLog
	@Operation(summary = "流程图", description = "流程图")
	// @PreAuthorize("hasAuthority('resource:diagram')")
	public Result<String> diagram(@PathVariable("instanceId") String instanceId) {
		return Result.of(null);
	}

	@TraceLog
	@PostMapping(value = "v1/resource/task-list")
	@Operation(summary = "查询任务", description = "查询任务")
	// @PreAuthorize("hasAuthority('resource:task:list')")
	public Result<?> taskList() {
		return Result.of(null);
	}

	@TraceLog
	@PostMapping(value = "v1/resource/audit-task")
	@Operation(summary = "审批任务", description = "审批任务")
	// @OperateLog(module = "资源管理", name = "审批任务")
	// @PreAuthorize("hasAuthority('resource:task:audit')")
	public Result<Boolean> auditTask() {
		return Result.of(null);
	}

	@TraceLog
	@GetMapping(value = "v1/resource/task-detail/{id}")
	@Operation(summary = "查看任务", description = "查看任务")
	public Result<?> detailResource(@PathVariable("id") Long id) {
		return Result.of(null);
	}

	@TraceLog
	@PostMapping(value = "v1/resource/resolve-task")
	@Operation(summary = "处理任务", description = "处理任务")
	// @OperateLog(module = "资源管理", name = "处理任务")
	// @PreAuthorize("hasAuthority('resource:task:resolve')")
	public Result<Boolean> resolveTask() {
		return Result.of(null);
	}

	@TraceLog
	@PostMapping(value = "v1/resource/transfer-task")
	@Operation(summary = "转办任务", description = "转办任务")
	// @OperateLog(module = "资源管理", name = "转办任务")
	// @PreAuthorize("hasAuthority('resource:task:transfer')")
	public Result<Boolean> transferTask() {
		return Result.of(null);
	}

	@TraceLog
	@PostMapping(value = "v1/resource/delegate-task")
	@Operation(summary = "委派任务", description = "委派任务")
	// @OperateLog(module = "资源管理", name = "委派任务")
	// @PreAuthorize("hasAuthority('resource:task:delegate')")
	public Result<Boolean> delegateTask() {
		return Result.of(null);
	}

}
