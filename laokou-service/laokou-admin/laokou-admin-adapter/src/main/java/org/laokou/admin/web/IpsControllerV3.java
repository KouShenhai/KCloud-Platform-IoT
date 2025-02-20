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
import org.laokou.admin.ip.api.IpsServiceI;
import org.laokou.admin.ip.dto.*;
import org.laokou.admin.ip.dto.clientobject.IpCO;
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
 * IP管理控制器.
 *
 * @author laokou
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("v3/ips")
@Tag(name = "IP管理", description = "IP管理")
public class IpsControllerV3 {

	private final IpsServiceI ipsServiceI;

	@Idempotent
	@PostMapping
	@PreAuthorize("hasAuthority('sys:ip:save')")
	@OperateLog(module = "IP管理", operation = "保存IP")
	@Operation(summary = "保存IP", description = "保存IP")
	public void saveV3(@RequestBody IpSaveCmd cmd) {
		ipsServiceI.save(cmd);
	}

	@PutMapping
	@PreAuthorize("hasAuthority('sys:ip:modify')")
	@OperateLog(module = "IP管理", operation = "修改IP")
	@Operation(summary = "修改IP", description = "修改IP")
	public void modifyV3(@RequestBody IpModifyCmd cmd) {
		ipsServiceI.modify(cmd);
	}

	@DeleteMapping
	@PreAuthorize("hasAuthority('sys:ip:remove')")
	@OperateLog(module = "IP管理", operation = "删除IP")
	@Operation(summary = "删除IP", description = "删除IP")
	public void removeV3(@RequestBody Long[] ids) {
		ipsServiceI.remove(new IpRemoveCmd(ids));
	}

	@PostMapping(value = "import", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	@PreAuthorize("hasAuthority('sys:ip:import')")
	@OperateLog(module = "IP管理", operation = "导入IP")
	@Operation(summary = "导入IP", description = "导入IP")
	public void importV3(@RequestPart("file") MultipartFile[] files) {
		ipsServiceI.importI(new IpImportCmd(files));
	}

	@PostMapping("export")
	@PreAuthorize("hasAuthority('sys:ip:export')")
	@OperateLog(module = "IP管理", operation = "导出IP")
	@Operation(summary = "导出IP", description = "导出IP")
	public void exportV3(@RequestBody IpExportCmd cmd) {
		ipsServiceI.export(cmd);
	}

	@TraceLog
	@PostMapping("page")
	@PreAuthorize("hasAuthority('sys:ip:page')")
	@Operation(summary = "分页查询IP列表", description = "分页查询IP列表")
	public Result<Page<IpCO>> pageV3(@Validated @RequestBody IpPageQry qry) {
		return ipsServiceI.page(qry);
	}

	@TraceLog
	@GetMapping("{id}")
	@PreAuthorize("hasAuthority('sys:ip:detail')")
	@Operation(summary = "查看IP详情", description = "查看IP详情")
	public Result<IpCO> getByIdV3(@PathVariable("id") Long id) {
		return ipsServiceI.getById(new IpGetQry(id));
	}

}
