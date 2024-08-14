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
import org.laokou.admin.dto.role.*;
import org.laokou.admin.dto.role.clientobject.RoleCO;
import org.laokou.common.i18n.dto.Page;
import org.laokou.common.i18n.dto.Option;
import org.laokou.common.i18n.dto.Result;
import org.laokou.common.idempotent.annotation.Idempotent;
import org.laokou.common.log.annotation.OperateLog;
import org.laokou.common.trace.annotation.TraceLog;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author laokou
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("v3/roles")
@Tag(name = "RolesV3Controller", description = "角色管理")
public class RolesV3Controller {

	@TraceLog
	@PostMapping("page")
	@PreAuthorize("hasAuthority('role:page')")
	@Operation(summary = "角色管理", description = "分页查询角色列表")
	public Result<Page<RoleCO>> pageV3() {
		return null;
	}

	@TraceLog
	@GetMapping("options")
	@Operation(summary = "角色管理", description = "查询角色下拉选择项列表")
	public Result<List<Option>> listOptionV3() {
		return null;
	}

	@TraceLog
	@GetMapping("{id}")
	@Operation(summary = "角色管理", description = "查看角色")
	public Result<RoleCO> getByIdV3(@PathVariable("id") Long id) {
		return null;
	}

	@Idempotent
	@PostMapping
	@PreAuthorize("hasAuthority('role:save')")
	@OperateLog(module = "角色管理", operation = "新增角色")
	@Operation(summary = "角色管理", description = "新增角色")
	public void saveV3() {

	}

	@PutMapping
	@PreAuthorize("hasAuthority('role:modify')")
	@OperateLog(module = "角色管理", operation = "修改角色")
	@Operation(summary = "角色管理", description = "修改角色")
	public void modifyV3(@RequestBody RoleModifyCmd cmd) {

	}

	@DeleteMapping
	@PreAuthorize("hasAuthority('role:remove')")
	@OperateLog(module = "角色管理", operation = "删除角色")
	@Operation(summary = "角色管理", description = "删除角色")
	public void removeV3(@RequestBody Long[] ids) {

	}

}
