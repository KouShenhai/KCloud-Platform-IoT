/*
 * Copyright (c) 2022-2024 KCloud-Platform-IOT Author or Authors. All Rights Reserved.
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
import org.laokou.common.data.cache.annotation.DataCache;
import org.laokou.common.i18n.common.CacheOperatorTypeEnum;
import org.laokou.common.i18n.dto.Result;
import org.laokou.common.idempotent.annotation.Idempotent;
import org.laokou.common.log.annotation.OperateLog;
import org.laokou.common.trace.annotation.TraceLog;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.laokou.common.i18n.common.CacheNameConstant.DEPTS;

/**
 * @author laokou
 */
@RestController
@Tag(name = "DeptsController", description = "部门管理")
@RequiredArgsConstructor
@RequestMapping("v1/depts")
public class DeptsController {

	private final DeptsServiceI deptsServiceI;

	@PostMapping("list")
	@Operation(summary = "部门管理", description = "查询菜单列表")
	@PreAuthorize("hasAuthority('depts:list')")
	@TraceLog
	public Result<List<DeptCO>> findList(@RequestBody DeptListQry qry) {
		return deptsServiceI.findList(qry);
	}

	@Idempotent
	@PostMapping
	@Operation(summary = "部门管理", description = "新增菜单")
	@OperateLog(module = "部门管理", operation = "新增菜单")
	@PreAuthorize("hasAuthority('depts:create')")
	public void create(@RequestBody DeptCreateCmd cmd) {
		deptsServiceI.create(cmd);
	}

	@PutMapping
	@Operation(summary = "部门管理", description = "修改菜单")
	@OperateLog(module = "部门管理", operation = "修改菜单")
	@PreAuthorize("hasAuthority('depts:modify')")
	@DataCache(name = DEPTS, key = "#cmd.deptCO.id", type = CacheOperatorTypeEnum.DEL)
	public void modify(@RequestBody DeptModifyCmd cmd) {
		deptsServiceI.modify(cmd);
	}

	@GetMapping("{id}")
	@TraceLog
	@Operation(summary = "部门管理", description = "查看菜单")
	@DataCache(name = DEPTS, key = "#id")
	public Result<DeptCO> findById(@PathVariable("id") Long id) {
		return deptsServiceI.findById(new DeptGetQry(id));
	}

	@DeleteMapping
	@Operation(summary = "部门管理", description = "删除菜单")
	@OperateLog(module = "部门管理", operation = "删除菜单")
	@PreAuthorize("hasAuthority('depts:remove')")
	public void remove(@RequestBody Long[] ids) {
		deptsServiceI.remove(new DeptRemoveCmd(ids));
	}

	@GetMapping("{roleId}/ids")
	@TraceLog
	@Operation(summary = "部门管理", description = "部门IDS")
	public Result<List<Long>> findIds(@PathVariable("roleId") Long roleId) {
		return deptsServiceI.findIds(new DeptIdsGetQry(roleId));
	}

}
