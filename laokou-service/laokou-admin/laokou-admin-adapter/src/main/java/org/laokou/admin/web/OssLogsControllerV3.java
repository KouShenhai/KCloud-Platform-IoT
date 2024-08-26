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
import org.laokou.admin.ossLog.api.OssLogsServiceI;
import org.laokou.admin.ossLog.dto.*;
import org.laokou.admin.ossLog.dto.clientobject.OssLogCO;
import org.laokou.common.i18n.dto.Page;
import org.laokou.common.i18n.dto.Result;
import org.laokou.common.idempotent.annotation.Idempotent;
import org.laokou.common.log.annotation.OperateLog;
import org.laokou.common.trace.annotation.TraceLog;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

/**
 * OSS日志管理控制器.
 *
 * @author laokou
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("v3/oss-logs")
@Tag(name = "OSS日志管理", description = "OSS日志管理")
public class OssLogsControllerV3 {

	private final OssLogsServiceI ossLogsServiceI;

	@Idempotent
	@PostMapping
	@PreAuthorize("hasAuthority('oss-log:save')")
	@OperateLog(module = "保存OSS日志", operation = "保存OSS日志")
	@Operation(summary = "保存OSS日志", description = "保存OSS日志")
	public void saveV3(@RequestBody OssLogSaveCmd cmd) {
		ossLogsServiceI.save(cmd);
	}

	@PutMapping
	@PreAuthorize("hasAuthority('oss-log:modify')")
	@OperateLog(module = "修改OSS日志", operation = "修改OSS日志")
	@Operation(summary = "修改OSS日志", description = "修改OSS日志")
	public void modifyV3(@RequestBody OssLogModifyCmd cmd) {
		ossLogsServiceI.modify(cmd);
	}

	@DeleteMapping
	@PreAuthorize("hasAuthority('oss-log:remove')")
	@OperateLog(module = "删除OSS日志", operation = "删除OSS日志")
	@Operation(summary = "删除OSS日志", description = "删除OSS日志")
	public void removeV3(@RequestBody Long[] ids) {
		ossLogsServiceI.remove(new OssLogRemoveCmd(ids));
	}

	@PostMapping(value = "import", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	@PreAuthorize("hasAuthority('oss-log:import')")
	@Operation(summary = "导入OSS日志", description = "导入OSS日志")
	@OperateLog(module = "导入OSS日志", operation = "导入OSS日志")
	public void importV3(@RequestPart("file") MultipartFile[] files) {
		ossLogsServiceI.importI(new OssLogImportCmd(files));
	}

	@PostMapping("export")
	@PreAuthorize("hasAuthority('oss-log:export')")
	@Operation(summary = "导出OSS日志", description = "导出OSS日志")
	@OperateLog(module = "导出OSS日志", operation = "导出OSS日志")
	public void exportV3(@RequestBody OssLogExportCmd cmd) {
		ossLogsServiceI.export(cmd);
	}

	@TraceLog
	@PostMapping("page")
	@PreAuthorize("hasAuthority('oss-log:page')")
	@Operation(summary = "分页查询OSS日志列表", description = "分页查询OSS日志列表")
	public Result<Page<OssLogCO>> pageV3(@RequestBody OssLogPageQry qry) {
		return ossLogsServiceI.page(qry);
	}

	@TraceLog
	@GetMapping("{id}")
	@Operation(summary = "查看OSS日志详情", description = "查看OSS日志详情")
	public Result<OssLogCO> getByIdV3(@PathVariable("id") Long id) {
		return ossLogsServiceI.getById(new OssLogGetQry(id));
	}

}
