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
import org.laokou.admin.client.api.DeptsServiceI;
import org.laokou.admin.client.dto.dept.*;
import org.laokou.admin.client.dto.dept.clientobject.DeptCO;
import org.laokou.admin.domain.annotation.OperateLog;
import org.laokou.common.data.cache.annotation.DataCache;
import org.laokou.common.data.cache.enums.Cache;
import org.laokou.common.i18n.dto.Result;
import org.laokou.common.trace.annotation.TraceLog;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author laokou
 */
@RestController
@Tag(name = "DeptsController", description = "部门")
@RequiredArgsConstructor
public class DeptsController {

	private final DeptsServiceI deptsServiceI;

	@GetMapping("v1/depts/tree")
	@TraceLog
	@Operation(summary = "树菜单", description = "树菜单")
	public Result<DeptCO> tree() {
		return deptsServiceI.tree(new DeptTreeGetQry());
	}

	@PostMapping("v1/depts/list")
	@Operation(summary = "查询", description = "查询")
	@PreAuthorize("hasAuthority('depts:list')")
	@TraceLog
	public Result<List<DeptCO>> list(@RequestBody DeptListQry qry) {
		return deptsServiceI.list(qry);
	}

	@PostMapping("v1/depts")
	@Operation(summary = "新增", description = "新增")
	@OperateLog(module = "部门管理", operation = "新增")
	@PreAuthorize("hasAuthority('depts:insert')")
	@TraceLog
	public Result<Boolean> insert(@RequestBody DeptInsertCmd cmd) {
		return deptsServiceI.insert(cmd);
	}

	@PutMapping("v1/depts")
	@Operation(summary = "修改", description = "修改")
	@OperateLog(module = "部门管理", operation = "修改")
	@PreAuthorize("hasAuthority('depts:update')")
	@TraceLog
	@DataCache(name = "depts", key = "#dto.id", type = Cache.DEL)
	public Result<Boolean> update(@RequestBody DeptUpdateCmd cmd) {
		return deptsServiceI.update(cmd);
	}

	@GetMapping("v1/depts/{id}")
	@TraceLog
	@Operation(summary = "查看", description = "查看")
	@DataCache(name = "depts", key = "#id")
	public Result<?> get(@PathVariable("id") Long id) {
		return deptsServiceI.get(new DeptGetQry(id));
	}

	@DeleteMapping("v1/depts/{id}")
	@TraceLog
	@Operation(summary = "删除", description = "删除")
	@OperateLog(module = "部门管理", operation = "删除")
	@PreAuthorize("hasAuthority('depts:delete')")
	@DataCache(name = "depts", key = "#id", type = Cache.DEL)
	public Result<Boolean> delete(@PathVariable("id") Long id) {
		return deptsServiceI.delete(new DeptDeleteCmd(id));
	}

	@GetMapping("v1/depts/ids/{roleId}")
	@TraceLog
	@Operation(summary = "部门IDS", description = "部门IDS")
	public Result<List<Long>> ids(@PathVariable(value = "roleId") Long roleId) {
		return deptsServiceI.ids(new DeptIDSGetQry(roleId));
	}

}
