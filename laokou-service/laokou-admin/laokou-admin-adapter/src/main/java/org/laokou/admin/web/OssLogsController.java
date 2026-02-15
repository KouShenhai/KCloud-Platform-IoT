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
import org.laokou.admin.ossLog.api.OssLogsServiceI;
import org.laokou.admin.ossLog.dto.OssLogExportCmd;
import org.laokou.admin.ossLog.dto.OssLogGetQry;
import org.laokou.admin.ossLog.dto.OssLogImportCmd;
import org.laokou.admin.ossLog.dto.OssLogModifyCmd;
import org.laokou.admin.ossLog.dto.OssLogPageQry;
import org.laokou.admin.ossLog.dto.OssLogRemoveCmd;
import org.laokou.admin.ossLog.dto.OssLogSaveCmd;
import org.laokou.admin.ossLog.dto.clientobject.OssLogCO;
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
 * OSS日志管理控制器.
 *
 * @author laokou
 */
@RestController
@RequiredArgsConstructor
@Tag(name = "OSS日志管理", description = "OSS日志管理")
public class OssLogsController {

	private final OssLogsServiceI ossLogsServiceI;

	@ApiSecret
	@Idempotent
	@PostMapping("/v1/oss-logs")
	@PreAuthorize("@permissionService.has('sys:oss-log:save')")
	@OperateLog(module = "OSS日志管理", operation = "保存OSS日志")
	@Operation(summary = "保存OSS日志", description = "保存OSS日志")
	public void saveOssLog(@RequestBody OssLogSaveCmd cmd) {
		ossLogsServiceI.saveOssLog(cmd);
	}

	@ApiSecret
	@PutMapping("/v1/oss-logs")
	@PreAuthorize("@permissionService.has('sys:oss-log:modify')")
	@OperateLog(module = "OSS日志管理", operation = "修改OSS日志")
	@Operation(summary = "修改OSS日志", description = "修改OSS日志")
	public void modifyOssLog(@RequestBody OssLogModifyCmd cmd) {
		ossLogsServiceI.modifyOssLog(cmd);
	}

	@ApiSecret
	@DeleteMapping("/v1/oss-logs")
	@PreAuthorize("@permissionService.has('sys:oss-log:remove')")
	@OperateLog(module = "OSS日志管理", operation = "删除OSS日志")
	@Operation(summary = "删除OSS日志", description = "删除OSS日志")
	public void removeOssLog(@RequestBody Long[] ids) {
		ossLogsServiceI.removeOssLog(new OssLogRemoveCmd(ids));
	}

	@ApiSecret
	@PostMapping(value = "/v1/oss-logs/import", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	@PreAuthorize("@permissionService.has('sys:oss-log:import')")
	@OperateLog(module = "OSS日志管理", operation = "导入OSS日志")
	@Operation(summary = "导入OSS日志", description = "导入OSS日志")
	public void importOssLog(@RequestPart("files") MultipartFile[] files) {
		ossLogsServiceI.importOssLog(new OssLogImportCmd(files));
	}

	@PostMapping("/v1/oss-logs/export")
	@PreAuthorize("@permissionService.has('sys:oss-log:export')")
	@OperateLog(module = "OSS日志管理", operation = "导出OSS日志")
	@Operation(summary = "导出OSS日志", description = "导出OSS日志")
	public void exportOssLog(@RequestBody OssLogExportCmd cmd) {
		ossLogsServiceI.exportOssLog(cmd);
	}

	@TraceLog
	@PostMapping("/v1/oss-logs/page")
	@PreAuthorize("@permissionService.has('sys:oss-log:page')")
	@Operation(summary = "分页查询OSS日志列表", description = "分页查询OSS日志列表")
	public Result<Page<OssLogCO>> pageOssLog(@Validated @RequestBody OssLogPageQry qry) {
		return ossLogsServiceI.pageOssLog(qry);
	}

	@TraceLog
	@GetMapping("/v1/oss-logs/{id}")
	@PreAuthorize("@permissionService.has('sys:oss-log:detail')")
	@Operation(summary = "查看OSS日志详情", description = "查看OSS日志详情")
	public Result<OssLogCO> getByIdOssLog(@PathVariable("id") Long id) {
		return ossLogsServiceI.getByIdOssLog(new OssLogGetQry(id));
	}

}
