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
import org.laokou.admin.oss.api.OssServiceI;
import org.laokou.admin.oss.dto.*;
import org.laokou.admin.oss.dto.clientobject.OssCO;
import org.laokou.common.i18n.dto.Page;
import org.laokou.common.i18n.dto.Result;
import org.laokou.common.idempotent.annotation.Idempotent;
import org.laokou.common.log.annotation.OperateLog;
import org.laokou.common.trace.annotation.TraceLog;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

/**
 * OSS管理控制器.
 *
 * @author laokou
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("v3/oss")
@Tag(name = "OSS管理", description = "OSS管理")
public class OssControllerV3 {

	private final OssServiceI ossServiceI;

	@Idempotent
	@PostMapping
	@PreAuthorize("hasAuthority('sys:oss:save')")
	@OperateLog(module = "OSS管理", operation = "保存OSS")
	@Operation(summary = "保存OSS", description = "保存OSS")
	public void saveV3(@RequestBody OssSaveCmd cmd) {
		ossServiceI.save(cmd);
	}

	@PutMapping
	@PreAuthorize("hasAuthority('sys:oss:modify')")
	@OperateLog(module = "OSS管理", operation = "修改OSS")
	@Operation(summary = "修改OSS", description = "修改OSS")
	public void modifyV3(@RequestBody OssModifyCmd cmd) {
		ossServiceI.modify(cmd);
	}

	@DeleteMapping
	@PreAuthorize("hasAuthority('sys:oss:remove')")
	@OperateLog(module = "OSS管理", operation = "删除OSS")
	@Operation(summary = "删除OSS", description = "删除OSS")
	public void removeV3(@RequestBody Long[] ids) {
		ossServiceI.remove(new OssRemoveCmd(ids));
	}

	@PostMapping(value = "import", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	@PreAuthorize("hasAuthority('sys:oss:import')")
	@OperateLog(module = "OSS管理", operation = "导入OSS")
	@Operation(summary = "导入OSS", description = "导入OSS")
	public void importV3(@RequestPart("file") MultipartFile[] files) {
		ossServiceI.importI(new OssImportCmd(files));
	}

	@PostMapping("export")
	@PreAuthorize("hasAuthority('sys:oss:export')")
	@OperateLog(module = "OSS管理", operation = "导出OSS")
	@Operation(summary = "导出OSS", description = "导出OSS")
	public void exportV3(@RequestBody OssExportCmd cmd) {
		ossServiceI.export(cmd);
	}

	@TraceLog
	@PostMapping("page")
	@PreAuthorize("hasAuthority('sys:oss:page')")
	@Operation(summary = "分页查询OSS列表", description = "分页查询OSS列表")
	public Result<Page<OssCO>> pageV3(@Validated @RequestBody OssPageQry qry) {
		return ossServiceI.page(qry);
	}

	@TraceLog
	@GetMapping("{id}")
	@PreAuthorize("hasAuthority('sys:oss:detail')")
	@Operation(summary = "查看OSS详情", description = "查看OSS详情")
	public Result<OssCO> getByIdV3(@PathVariable("id") Long id) {
		return ossServiceI.getById(new OssGetQry(id));
	}

}
