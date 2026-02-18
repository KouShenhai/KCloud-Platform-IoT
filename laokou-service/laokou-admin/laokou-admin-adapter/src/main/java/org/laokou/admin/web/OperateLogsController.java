/*
 * Copyright (c) 2022-2026 KCloud-Platform-IoT Author or Authors. All Rights Reserved.
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
import org.laokou.admin.operateLog.dto.OperateLogExportCmd;
import org.laokou.admin.operateLog.dto.OperateLogGetQry;
import org.laokou.admin.operateLog.dto.OperateLogImportCmd;
import org.laokou.admin.operateLog.dto.OperateLogModifyCmd;
import org.laokou.admin.operateLog.dto.OperateLogPageQry;
import org.laokou.admin.operateLog.dto.OperateLogRemoveCmd;
import org.laokou.admin.operateLog.dto.OperateLogSaveCmd;
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
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/**
 * 操作日志管理控制器.
 *
 * @author laokou
 */
@RestController
@RequiredArgsConstructor
@Tag(name = "操作日志管理", description = "操作日志管理")
public class OperateLogsController {

	private final OperateLogsServiceI operateLogsServiceI;

	@ApiSecret
	@Idempotent
	@PostMapping("/v1/operate-logs")
	@PreAuthorize("@permissionService.has('sys:operate-log:save')")
	@OperateLog(module = "操作日志管理", operation = "保存操作日志")
	@Operation(summary = "保存操作日志", description = "保存操作日志")
	public void saveOperateLog(@RequestBody OperateLogSaveCmd cmd) {
		operateLogsServiceI.saveOperateLog(cmd);
	}

	@ApiSecret
	@PutMapping("/v1/operate-logs")
	@PreAuthorize("@permissionService.has('sys:operate-log:modify')")
	@OperateLog(module = "操作日志管理", operation = "修改操作日志")
	@Operation(summary = "修改操作日志", description = "修改操作日志")
	public void modifyOperateLog(@RequestBody OperateLogModifyCmd cmd) {
		operateLogsServiceI.modifyOperateLog(cmd);
	}

	@ApiSecret
	@DeleteMapping("/v1/operate-logs")
	@PreAuthorize("@permissionService.has('sys:operate-log:remove')")
	@OperateLog(module = "操作日志管理", operation = "删除操作日志")
	@Operation(summary = "删除操作日志", description = "删除操作日志")
	public void removeOperateLog(@RequestBody Long[] ids) {
		operateLogsServiceI.removeOperateLog(new OperateLogRemoveCmd(ids));
	}

	@ApiSecret
	@PostMapping(value = "/v1/operate-logs/import", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	@PreAuthorize("@permissionService.has('sys:operate-log:import')")
	@OperateLog(module = "操作日志管理", operation = "导入操作日志")
	@Operation(summary = "导入操作日志", description = "导入操作日志")
	public void importOperateLog(@RequestPart("files") MultipartFile[] files) {
		operateLogsServiceI.importOperateLog(new OperateLogImportCmd(files));
	}

	@PostMapping("/v1/operate-logs/export")
	@PreAuthorize("@permissionService.has('sys:operate-log:export')")
	@OperateLog(module = "操作日志管理", operation = "导出操作日志")
	@Operation(summary = "导出操作日志", description = "导出操作日志")
	public void exportOperateLog(@RequestBody OperateLogExportCmd cmd) {
		operateLogsServiceI.exportOperateLog(cmd);
	}

	@TraceLog
	@PostMapping("/v1/operate-logs/page")
	@PreAuthorize("@permissionService.has('sys:operate-log:page')")
	@Operation(summary = "分页查询操作日志列表", description = "分页查询操作日志列表")
	public Result<Page<OperateLogCO>> pageOperateLog(@Validated @RequestBody OperateLogPageQry qry) {
		return operateLogsServiceI.pageOperateLog(qry);
	}

	@TraceLog
	@GetMapping("/v1/operate-logs/{id}")
	@PreAuthorize("@permissionService.has('sys:operate-log:detail')")
	@Operation(summary = "查看操作日志详情", description = "查看操作日志详情")
	public Result<OperateLogCO> getOperateLogById(@PathVariable("id") Long id) {
		return operateLogsServiceI.getOperateLogById(new OperateLogGetQry(id));
	}

}
