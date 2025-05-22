/*
 * Copyright (c) 2022-2025 KCloud-Platform-IoT Author or Authors. All Rights Reserved.
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
import lombok.RequiredArgsConstructor;
import org.laokou.admin.operateLog.api.OperateLogsServiceI;
import org.laokou.admin.operateLog.dto.*;
import org.laokou.admin.operateLog.dto.clientobject.OperateLogCO;
import org.laokou.common.i18n.dto.Page;
import org.laokou.common.i18n.dto.Result;
import org.laokou.common.idempotent.annotation.Idempotent;
import org.laokou.common.log.annotation.OperateLog;
import org.laokou.common.secret.annotation.ApiSecret;
import org.laokou.common.trace.annotation.TraceLog;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

/**
 * 操作日志管理控制器.
 *
 * @author laokou
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("v3/operate-logs")
@Tag(name = "操作日志管理", description = "操作日志管理")
public class OperateLogsControllerV3 {

	private final OperateLogsServiceI operateLogsServiceI;

	@ApiSecret
	@Idempotent
	@PostMapping
	@PreAuthorize("hasAuthority('sys:operate-log:save')")
	@OperateLog(module = "操作日志管理", operation = "保存操作日志")
	@Operation(summary = "保存操作日志", description = "保存操作日志")
	public void saveOperateLog(@RequestBody OperateLogSaveCmd cmd) {
		operateLogsServiceI.save(cmd);
	}

	@ApiSecret
	@PutMapping
	@PreAuthorize("hasAuthority('sys:operate-log:modify')")
	@OperateLog(module = "操作日志管理", operation = "修改操作日志")
	@Operation(summary = "修改操作日志", description = "修改操作日志")
	public void modifyOperateLog(@RequestBody OperateLogModifyCmd cmd) {
		operateLogsServiceI.modify(cmd);
	}

	@ApiSecret
	@DeleteMapping
	@PreAuthorize("hasAuthority('sys:operate-log:remove')")
	@OperateLog(module = "操作日志管理", operation = "删除操作日志")
	@Operation(summary = "删除操作日志", description = "删除操作日志")
	public void removeOperateLog(@RequestBody Long[] ids) {
		operateLogsServiceI.remove(new OperateLogRemoveCmd(ids));
	}

	@ApiSecret
	@PostMapping(value = "import", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	@PreAuthorize("hasAuthority('sys:operate-log:import')")
	@OperateLog(module = "操作日志管理", operation = "导入操作日志")
	@Operation(summary = "导入操作日志", description = "导入操作日志")
	public void importOperateLog(@RequestPart("files") MultipartFile[] files) {
		operateLogsServiceI.importI(new OperateLogImportCmd(files));
	}

	@PostMapping("export")
	@PreAuthorize("hasAuthority('sys:operate-log:export')")
	@OperateLog(module = "操作日志管理", operation = "导出操作日志")
	@Operation(summary = "导出操作日志", description = "导出操作日志")
	public void exportOperateLog(@RequestBody OperateLogExportCmd cmd) {
		operateLogsServiceI.export(cmd);
	}

	@TraceLog
	@PostMapping("page")
	@PreAuthorize("hasAuthority('sys:operate-log:page')")
	@Operation(summary = "分页查询操作日志列表", description = "分页查询操作日志列表")
	public Result<Page<OperateLogCO>> pageOperateLog(@Validated @RequestBody OperateLogPageQry qry) {
		return operateLogsServiceI.page(qry);
	}

	@TraceLog
	@GetMapping("{id}")
	@PreAuthorize("hasAuthority('sys:operate-log:detail')")
	@Operation(summary = "查看操作日志详情", description = "查看操作日志详情")
	public Result<OperateLogCO> getByIdOperateLog(@PathVariable("id") Long id) {
		return operateLogsServiceI.getById(new OperateLogGetQry(id));
	}

}
