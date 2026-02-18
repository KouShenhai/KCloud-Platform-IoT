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
import org.laokou.admin.oss.api.OssServiceI;
import org.laokou.admin.oss.dto.OssExportCmd;
import org.laokou.admin.oss.dto.OssGetQry;
import org.laokou.admin.oss.dto.OssImportCmd;
import org.laokou.admin.oss.dto.OssModifyCmd;
import org.laokou.admin.oss.dto.OssPageQry;
import org.laokou.admin.oss.dto.OssRemoveCmd;
import org.laokou.admin.oss.dto.OssSaveCmd;
import org.laokou.admin.oss.dto.clientobject.OssCO;
import org.laokou.common.i18n.dto.Page;
import org.laokou.common.i18n.dto.Result;
import org.laokou.common.idempotent.annotation.Idempotent;
import org.laokou.common.log.annotation.OperateLog;
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
 * OSS管理控制器.
 *
 * @author laokou
 */
@RestController
@RequiredArgsConstructor
@Tag(name = "OSS管理", description = "OSS管理")
public class OssController {

	private final OssServiceI ossServiceI;

	@Idempotent
	@PostMapping("/v1/oss")
	@PreAuthorize("@permissionService.has('sys:oss:save')")
	@OperateLog(module = "OSS管理", operation = "保存OSS")
	@Operation(summary = "保存OSS", description = "保存OSS")
	public void saveOss(@RequestBody OssSaveCmd cmd) {
		ossServiceI.saveOss(cmd);
	}

	@PutMapping("/v1/oss")
	@PreAuthorize("@permissionService.has('sys:oss:modify')")
	@OperateLog(module = "OSS管理", operation = "修改OSS")
	@Operation(summary = "修改OSS", description = "修改OSS")
	public void modifyOss(@RequestBody OssModifyCmd cmd) {
		ossServiceI.modifyOss(cmd);
	}

	@DeleteMapping
	@PreAuthorize("@permissionService.has('sys:oss:remove')")
	@OperateLog(module = "OSS管理", operation = "删除OSS")
	@Operation(summary = "删除OSS", description = "删除OSS")
	public void removeOss(@RequestBody Long[] ids) {
		ossServiceI.removeOss(new OssRemoveCmd(ids));
	}

	@PostMapping(value = "/v1/oss/import", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	@PreAuthorize("@permissionService.has('sys:oss:import')")
	@OperateLog(module = "OSS管理", operation = "导入OSS")
	@Operation(summary = "导入OSS", description = "导入OSS")
	public void importOss(@RequestPart("files") MultipartFile[] files) {
		ossServiceI.importOss(new OssImportCmd(files));
	}

	@PostMapping("/v1/oss/export")
	@PreAuthorize("@permissionService.has('sys:oss:export')")
	@OperateLog(module = "OSS管理", operation = "导出OSS")
	@Operation(summary = "导出OSS", description = "导出OSS")
	public void exportOss(@RequestBody OssExportCmd cmd) {
		ossServiceI.exportOss(cmd);
	}

	@TraceLog
	@PostMapping("/v1/oss/page")
	@PreAuthorize("@permissionService.has('sys:oss:page')")
	@Operation(summary = "分页查询OSS列表", description = "分页查询OSS列表")
	public Result<Page<OssCO>> pageOss(@Validated @RequestBody OssPageQry qry) {
		return ossServiceI.pageOss(qry);
	}

	@TraceLog
	@GetMapping("/v1/oss/{id}")
	@PreAuthorize("@permissionService.has('sys:oss:detail')")
	@Operation(summary = "查看OSS详情", description = "查看OSS详情")
	public Result<OssCO> getOssById(@PathVariable("id") Long id) {
		return ossServiceI.getOssById(new OssGetQry(id));
	}

}
