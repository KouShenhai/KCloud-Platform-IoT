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
import lombok.extern.slf4j.Slf4j;
import org.laokou.admin.role.api.RolesServiceI;
import org.laokou.admin.role.dto.*;
import org.laokou.admin.role.dto.clientobject.RoleCO;
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
 * 角色管理控制器.
 *
 * @author laokou
 */
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("v3/roles")
@Tag(name = "角色管理", description = "角色管理")
public class RolesControllerV3 {

	private final RolesServiceI rolesServiceI;

	@Idempotent
	@PostMapping
	@PreAuthorize("hasAuthority('sys:role:save')")
	@OperateLog(module = "角色管理", operation = "保存角色")
	@Operation(summary = "保存角色", description = "保存角色")
	public void saveRole(@RequestBody RoleSaveCmd cmd) {
		rolesServiceI.saveRole(cmd);
	}

	@PutMapping
	@PreAuthorize("hasAuthority('sys:role:modify')")
	@OperateLog(module = "角色管理", operation = "修改角色")
	@Operation(summary = "修改角色", description = "修改角色")
	public void modifyRole(@RequestBody RoleModifyCmd cmd) {
		rolesServiceI.modifyRole(cmd);
	}

	@DeleteMapping
	@PreAuthorize("hasAuthority('sys:role:remove')")
	@OperateLog(module = "角色管理", operation = "删除角色")
	@Operation(summary = "删除角色", description = "删除角色")
	public void removeRole(@RequestBody Long[] ids) throws InterruptedException {
		rolesServiceI.removeRole(new RoleRemoveCmd(ids));
	}

	@PostMapping(value = "import", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	@PreAuthorize("hasAuthority('sys:role:import')")
	@OperateLog(module = "角色管理", operation = "导入角色")
	@Operation(summary = "导入角色", description = "导入角色")
	public void importRole(@RequestPart("files") MultipartFile[] files) {
		rolesServiceI.importRole(new RoleImportCmd(files));
	}

	@PostMapping("export")
	@PreAuthorize("hasAuthority('sys:role:export')")
	@OperateLog(module = "角色管理", operation = "导出角色")
	@Operation(summary = "导出角色", description = "导出角色")
	public void exportRole(@RequestBody RoleExportCmd cmd) {
		rolesServiceI.exportRole(cmd);
	}

	@PutMapping("authority")
	@PreAuthorize("hasAuthority('sys:role:modify')")
	@OperateLog(module = "用户管理", operation = "修改角色权限")
	@Operation(summary = "修改角色权限", description = "修改角色权限")
	public void modifyRoleAuthority(@RequestBody RoleModifyAuthorityCmd cmd) throws InterruptedException {
		rolesServiceI.modifyRoleAuthority(cmd);
	}

	@TraceLog
	@PostMapping("page")
	@PreAuthorize("hasAuthority('sys:role:page')")
	@Operation(summary = "分页查询角色列表", description = "分页查询角色列表")
	public Result<Page<RoleCO>> pageRole(@Validated @RequestBody RolePageQry qry) {
		return rolesServiceI.pageRole(qry);
	}

	@TraceLog
	@GetMapping("{id}")
	@PreAuthorize("hasAuthority('sys:role:detail')")
	@Operation(summary = "查看角色详情", description = "查看角色详情")
	public Result<RoleCO> getRoleById(@PathVariable("id") Long id) {
		return rolesServiceI.getRoleById(new RoleGetQry(id));
	}

}
