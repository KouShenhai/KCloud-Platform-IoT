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
import org.laokou.admin.tenant.api.TenantsServiceI;
import org.laokou.admin.tenant.dto.*;
import org.laokou.admin.tenant.dto.clientobject.TenantCO;
import org.laokou.common.data.cache.annotation.DataCache;
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

import static org.laokou.common.data.cache.constant.NameConstants.TENANTS;
import static org.laokou.common.data.cache.constant.TypeEnum.DEL;

/**
 * 租户管理控制器.
 *
 * @author laokou
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("v3/tenants")
@Tag(name = "租户管理", description = "租户管理")
public class TenantsControllerV3 {

	private final TenantsServiceI tenantsServiceI;

	@Idempotent
	@PostMapping
	@PreAuthorize("hasAuthority('sys:tenant:save')")
	@OperateLog(module = "租户管理", operation = "保存租户")
	@Operation(summary = "保存租户", description = "保存租户")
	public void saveV3(@RequestBody TenantSaveCmd cmd) {
		tenantsServiceI.save(cmd);
	}

	@PutMapping
	@PreAuthorize("hasAuthority('sys:tenant:modify')")
	@OperateLog(module = "租户管理", operation = "修改租户")
	@Operation(summary = "修改租户", description = "修改租户")
	@DataCache(name = TENANTS, key = "#cmd.co.id", type = DEL)
	public void modifyV3(@RequestBody TenantModifyCmd cmd) {
		tenantsServiceI.modify(cmd);
	}

	@DeleteMapping
	@PreAuthorize("hasAuthority('sys:tenant:remove')")
	@OperateLog(module = "租户管理", operation = "删除租户")
	@Operation(summary = "删除租户", description = "删除租户")
	public void removeV3(@RequestBody Long[] ids) {
		tenantsServiceI.remove(new TenantRemoveCmd(ids));
	}

	@PostMapping(value = "import", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	@PreAuthorize("hasAuthority('sys:tenant:import')")
	@OperateLog(module = "租户管理", operation = "导入租户")
	@Operation(summary = "导入租户", description = "导入租户")
	public void importV3(@RequestPart("files") MultipartFile[] files) {
		tenantsServiceI.importI(new TenantImportCmd(files));
	}

	@PostMapping("export")
	@PreAuthorize("hasAuthority('sys:tenant:export')")
	@OperateLog(module = "租户管理", operation = "导出租户")
	@Operation(summary = "导出租户", description = "导出租户")
	public void exportV3(@RequestBody TenantExportCmd cmd) {
		tenantsServiceI.export(cmd);
	}

	@TraceLog
	@PostMapping("page")
	@PreAuthorize("hasAuthority('sys:tenant:page')")
	@Operation(summary = "分页查询租户列表", description = "分页查询租户列表")
	public Result<Page<TenantCO>> pageV3(@Validated @RequestBody TenantPageQry qry) {
		return tenantsServiceI.page(qry);
	}

	@TraceLog
	@GetMapping("{id}")
	@DataCache(name = TENANTS, key = "#id")
	@PreAuthorize("hasAuthority('sys:tenant:detail')")
	@Operation(summary = "查看租户详情", description = "查看租户详情")
	public Result<TenantCO> getByIdV3(@PathVariable("id") Long id) {
		return tenantsServiceI.getById(new TenantGetQry(id));
	}

}
