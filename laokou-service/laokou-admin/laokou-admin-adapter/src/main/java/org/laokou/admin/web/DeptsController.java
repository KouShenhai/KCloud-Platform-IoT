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
import org.laokou.common.i18n.dto.Result;
import org.laokou.common.trace.annotation.TraceLog;
import org.springframework.web.bind.annotation.*;
import java.util.*;

/**
 * @author laokou
 */
@RestController
@Tag(name = "DeptsController", description = "部门")
@RequiredArgsConstructor
public class DeptsController {

	@GetMapping("v1/depts/tree")
	@TraceLog
	@Operation(summary = "树菜单", description = "树菜单")
	public Result<?> tree() {
		return Result.of(null);
	}

	@PostMapping("v1/depts/list")
	@Operation(summary = "查询", description = "查询")
	// @PreAuthorize("hasAuthority('depts:list')")
	@TraceLog
	public Result<List<?>> list() {
		return Result.of(null);
	}

	@PostMapping("v1/depts")
	@Operation(summary = "新增", description = "新增")
	// @OperateLog(module = "部门管理", name = "新增")
	// @PreAuthorize("hasAuthority('depts:insert')")
	@TraceLog
	public Result<Boolean> insert() {
		return Result.of(null);
	}

	@PutMapping("v1/depts")
	@Operation(summary = "修改", description = "修改")
	// @OperateLog(module = "部门管理", name = "修改")
	// @PreAuthorize("hasAuthority('depts:update')")
	@TraceLog
	// @DataCache(name = "depts", key = "#dto.id", type = CacheEnum.DEL)
	public Result<Boolean> update() {
		return Result.of(null);
	}

	@GetMapping("v1/depts/{id}")
	@TraceLog
	@Operation(summary = "查看", description = "查看")
	// @DataCache(name = "depts", key = "#id")
	public Result<?> get(@PathVariable("id") Long id) {
		return Result.of(null);
	}

	@DeleteMapping("v1/depts/{id}")
	@TraceLog
	@Operation(summary = "删除", description = "删除")
	// @OperateLog(module = "部门管理", name = "删除")
	// @PreAuthorize("hasAuthority('depts:delete')")
	// @DataCache(name = "depts", key = "#id", type = CacheEnum.DEL)
	public Result<Boolean> delete(@PathVariable("id") Long id) {
		return Result.of(null);
	}

	@GetMapping("v1/depts/ids/{roleId}")
	@TraceLog
	@Operation(summary = "部门IDS", description = "部门IDS")
	public Result<List<Long>> ids(@PathVariable(value = "roleId") Long roleId) {
		return Result.of(null);
	}

}
