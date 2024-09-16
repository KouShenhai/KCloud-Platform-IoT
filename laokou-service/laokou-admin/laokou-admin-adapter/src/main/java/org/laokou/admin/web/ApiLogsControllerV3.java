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

package org.laokou.admin.web;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.laokou.admin.apiLog.api.ApiLogsServiceI;
import org.laokou.admin.apiLog.dto.*;
import org.laokou.admin.apiLog.dto.clientobject.ApiLogCO;
import org.laokou.common.i18n.dto.Page;
import org.laokou.common.i18n.dto.Result;
import org.laokou.common.idempotent.annotation.Idempotent;
import org.laokou.common.log.annotation.OperateLog;
import org.laokou.common.secret.annotation.ApiSecret;
import org.laokou.common.trace.annotation.TraceLog;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

/**
 * Api日志管理控制器.
 *
 * @author laokou
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("v3/api-logs")
@Tag(name = "Api日志管理", description = "Api日志管理")
public class ApiLogsControllerV3 {

	private final ApiLogsServiceI apiLogsServiceI;

	@ApiSecret
	@Idempotent
	@PostMapping
	@PreAuthorize("hasAuthority('sys:api-log:save')")
	@OperateLog(module = "Api日志管理", operation = "保存Api日志")
	@Operation(summary = "保存Api日志", description = "保存Api日志")
	public void saveV3(@RequestBody ApiLogSaveCmd cmd) {
		apiLogsServiceI.save(cmd);
	}

	@ApiSecret
	@PutMapping
	@PreAuthorize("hasAuthority('sys:api-log:modify')")
	@OperateLog(module = "Api日志管理", operation = "修改Api日志")
	@Operation(summary = "修改Api日志", description = "修改Api日志")
	public void modifyV3(@RequestBody ApiLogModifyCmd cmd) {
		apiLogsServiceI.modify(cmd);
	}

	@ApiSecret
	@DeleteMapping
	@PreAuthorize("hasAuthority('sys:api-log:remove')")
	@OperateLog(module = "Api日志管理", operation = "删除Api日志")
	@Operation(summary = "删除Api日志", description = "删除Api日志")
	public void removeV3(@RequestBody Long[] ids) {
		apiLogsServiceI.remove(new ApiLogRemoveCmd(ids));
	}

	@ApiSecret
	@PostMapping(value = "import", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	@PreAuthorize("hasAuthority('sys:api-log:import')")
	@OperateLog(module = "Api日志管理", operation = "导入Api日志")
	@Operation(summary = "导入Api日志", description = "导入Api日志")
	public void importV3(@RequestPart("file") MultipartFile[] files) {
		apiLogsServiceI.importI(new ApiLogImportCmd(files));
	}

	@PostMapping("export")
	@PreAuthorize("hasAuthority('sys:api-log:export')")
	@OperateLog(module = "Api日志管理", operation = "导出Api日志")
	@Operation(summary = "导出Api日志", description = "导出Api日志")
	public void exportV3(@RequestBody ApiLogExportCmd cmd) {
		apiLogsServiceI.export(cmd);
	}

	@TraceLog
	@PostMapping("page")
	@PreAuthorize("hasAuthority('sys:api-log:page')")
	@Operation(summary = "分页查询Api日志列表", description = "分页查询Api日志列表")
	public Result<Page<ApiLogCO>> pageV3(@RequestBody ApiLogPageQry qry) {
		return apiLogsServiceI.page(qry);
	}

	@TraceLog
	@GetMapping("{id}")
	@Operation(summary = "查看Api日志详情", description = "查看Api日志详情")
	public Result<ApiLogCO> getByIdV3(@PathVariable("id") Long id) {
		return apiLogsServiceI.getById(new ApiLogGetQry(id));
	}

}
