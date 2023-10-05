/*
 * Copyright (c) 2022 KCloud-Platform-Alibaba Authors. All Rights Reserved.
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
import org.laokou.admin.api.RolesServiceI;
import org.laokou.admin.domain.annotation.OperateLog;
import org.laokou.admin.dto.common.clientobject.OptionCO;
import org.laokou.admin.dto.role.*;
import org.laokou.admin.dto.role.clientobject.RoleCO;
import org.laokou.common.data.cache.annotation.DataCache;
import org.laokou.common.data.cache.aspect.Type;
import org.laokou.common.i18n.dto.Datas;
import org.laokou.common.i18n.dto.Result;
import org.laokou.common.idempotent.annotation.Idempotent;
import org.laokou.common.trace.annotation.TraceLog;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author laokou
 */
@RestController
@Tag(name = "RolesController", description = "角色管理")
@RequiredArgsConstructor
@RequestMapping("v1/roles")
public class RolesController {

	private final RolesServiceI rolesServiceI;

	@TraceLog
	@PostMapping("list")
	@Operation(summary = "角色管理", description = "查询角色列表")
	@PreAuthorize("hasAuthority('roles:list')")
	public Result<Datas<RoleCO>> list(@RequestBody RoleListQry qry) {
		return rolesServiceI.list(qry);
	}

	@TraceLog
	@GetMapping("option-list")
	@Operation(summary = "角色管理", description = "下拉列表")
	public Result<List<OptionCO>> optionList() {
		return rolesServiceI.optionList(new RoleOptionListQry());
	}

	@TraceLog
	@GetMapping("{id}")
	@Operation(summary = "角色管理", description = "查看角色")
	@DataCache(name = "roles", key = "#id")
	public Result<RoleCO> getById(@PathVariable("id") Long id) {
		return rolesServiceI.getById(new RoleGetQry(id));
	}

	@Idempotent
	@TraceLog
	@PostMapping
	@Operation(summary = "角色管理", description = "新增角色")
	@OperateLog(module = "角色管理", operation = "新增角色")
	@PreAuthorize("hasAuthority('roles:insert')")
	public Result<Boolean> insert(@RequestBody RoleInsertCmd cmd) {
		return rolesServiceI.insert(cmd);
	}

	@TraceLog
	@PutMapping
	@Operation(summary = "角色管理", description = "修改角色")
	@OperateLog(module = "角色管理", operation = "修改角色")
	@PreAuthorize("hasAuthority('roles:update')")
	@DataCache(name = "roles", key = "#cmd.roleCO.id", type = Type.DEL)
	public Result<Boolean> update(@RequestBody RoleUpdateCmd cmd) {
		return rolesServiceI.update(cmd);
	}

	@TraceLog
	@DeleteMapping("{id}")
	@Operation(summary = "角色管理", description = "删除角色")
	@OperateLog(module = "角色管理", operation = "删除角色")
	@PreAuthorize("hasAuthority('roles:delete')")
	@DataCache(name = "roles", key = "#id", type = Type.DEL)
	public Result<Boolean> deleteById(@PathVariable("id") Long id) {
		return rolesServiceI.deleteById(new RoleDeleteCmd(id));
	}

}
