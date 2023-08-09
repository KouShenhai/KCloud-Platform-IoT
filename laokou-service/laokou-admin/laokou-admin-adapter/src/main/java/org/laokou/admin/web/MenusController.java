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
import java.util.List;

/**
 * 系统菜单控制器
 *
 * @author laokou
 */
@RestController
@Tag(name = "MenusController", description = "菜单")
@RequiredArgsConstructor
public class MenusController {

	@TraceLog
	@GetMapping("v1/menus/tree-list")
	@Operation(summary = "树菜单列表", description = "树菜单列表")
	public Result<?> treeList() {
		return Result.of(null);
	}

	@TraceLog
	@PostMapping("v1/menus/list")
	@Operation(summary = "查询", description = "查询")
	// @PreAuthorize("hasAuthority('menus:list')")
	public Result<List<?>> list() {
		return Result.of(null);
	}

	@TraceLog
	@GetMapping("v1/menus/{id}")
	@Operation(summary = "查看", description = "查看")
	// @DataCache(name = "menus", key = "#id")
	public Result<?> get(@PathVariable("id") Long id) {
		return Result.of(null);
	}

	@TraceLog
	@PutMapping("v1/menus")
	@Operation(summary = "修改", description = "修改")
	// @OperateLog(module = "菜单管理", name = "修改")
	// @PreAuthorize("hasAuthority('menus:update')")
	// @DataCache(name = "menus", key = "#dto.id", type = CacheEnum.DEL)
	public Result<Boolean> update() {
		return Result.of(null);
	}

	@TraceLog
	@PostMapping("v1/menus")
	@Operation(summary = "新增", description = "新增")
	// @OperateLog(module = "菜单管理", name = "新增")
	// @PreAuthorize("hasAuthority('menus:insert')")
	public Result<Boolean> insert() {
		return Result.of(null);
	}

	@TraceLog
	@DeleteMapping("v1/menus/{id}")
	@Operation(summary = "删除", description = "删除")
	// @OperateLog(module = "菜单管理", name = "删除")
	// @PreAuthorize("hasAuthority('menus:delete')")
	// @DataCache(name = "menus", key = "#id", type = CacheEnum.DEL)
	public Result<Boolean> delete(@PathVariable("id") Long id) {
		return Result.of(null);
	}

	@TraceLog
	@GetMapping("v1/menus/tree")
	@Operation(summary = "树菜单", description = "树菜单")
	public Result<?> tree() {
		return Result.of(null);
	}

	@TraceLog
	@GetMapping("v1/menus/ids/{roleId}")
	@Operation(summary = "菜单树IDS", description = "菜单树IDS")
	public Result<List<Long>> ids(@PathVariable(value = "roleId") Long roleId) {
		return Result.of(null);
	}

	@TraceLog
	@GetMapping("v1/menus/tenant-tree")
	@Operation(summary = "租户树菜单", description = "租户树菜单")
	public Result<?> tenantTree() {
		return Result.of(null);
	}

}
