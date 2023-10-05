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
import org.laokou.admin.api.DeptsServiceI;
import org.laokou.admin.dto.dept.*;
import org.laokou.admin.dto.dept.clientobject.DeptCO;
import org.laokou.admin.domain.annotation.OperateLog;
import org.laokou.common.data.cache.annotation.DataCache;
import org.laokou.common.data.cache.aspect.Type;
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
@Tag(name = "DeptsController", description = "部门管理")
@RequiredArgsConstructor
@RequestMapping("v1/depts")
public class DeptsController {

	private final DeptsServiceI deptsServiceI;

	@GetMapping("tree")
	@TraceLog
	@Operation(summary = "部门管理", description = "树形部门列表")
	public Result<DeptCO> tree() {
		return deptsServiceI.tree(new DeptTreeGetQry());
	}

	@PostMapping("list")
	@Operation(summary = "部门管理", description = "查询菜单列表")
	@PreAuthorize("hasAuthority('depts:list')")
	@TraceLog
	public Result<List<DeptCO>> list(@RequestBody DeptListQry qry) {
		return deptsServiceI.list(qry);
	}

	@Idempotent
	@PostMapping
	@Operation(summary = "部门管理", description = "新增菜单")
	@OperateLog(module = "部门管理", operation = "新增菜单")
	@PreAuthorize("hasAuthority('depts:insert')")
	@TraceLog
	public Result<Boolean> insert(@RequestBody DeptInsertCmd cmd) {
		return deptsServiceI.insert(cmd);
	}

	@PutMapping
	@Operation(summary = "部门管理", description = "修改菜单")
	@OperateLog(module = "部门管理", operation = "修改菜单")
	@PreAuthorize("hasAuthority('depts:update')")
	@TraceLog
	@DataCache(name = "depts", key = "#cmd.deptCO.id", type = Type.DEL)
	public Result<Boolean> update(@RequestBody DeptUpdateCmd cmd) {
		return deptsServiceI.update(cmd);
	}

	@GetMapping("{id}")
	@TraceLog
	@Operation(summary = "部门管理", description = "查看菜单")
	@DataCache(name = "depts", key = "#id")
	public Result<DeptCO> getById(@PathVariable("id") Long id) {
		return deptsServiceI.getById(new DeptGetQry(id));
	}

	@DeleteMapping("{id}")
	@TraceLog
	@Operation(summary = "部门管理", description = "删除菜单")
	@OperateLog(module = "部门管理", operation = "删除菜单")
	@PreAuthorize("hasAuthority('depts:delete')")
	@DataCache(name = "depts", key = "#id", type = Type.DEL)
	public Result<Boolean> deleteById(@PathVariable("id") Long id) {
		return deptsServiceI.deleteById(new DeptDeleteCmd(id));
	}

	@GetMapping("{roleId}/ids")
	@TraceLog
	@Operation(summary = "部门管理", description = "部门IDS")
	public Result<List<Long>> ids(@PathVariable(value = "roleId") Long roleId) {
		return deptsServiceI.ids(new DeptIDSGetQry(roleId));
	}

}
