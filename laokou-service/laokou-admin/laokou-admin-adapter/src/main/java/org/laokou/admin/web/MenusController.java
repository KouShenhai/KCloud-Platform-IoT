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
import org.laokou.admin.client.api.MenusServiceI;
import org.laokou.admin.client.dto.menu.*;
import org.laokou.admin.client.dto.menu.clientobject.MenuCO;
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
@Tag(name = "MenusController", description = "菜单管理")
@RequiredArgsConstructor
public class MenusController {

	private final MenusServiceI menusServiceI;

	@TraceLog
	@GetMapping("v1/menus/tree-list")
	@Operation(summary = "菜单管理", description = "树形菜单列表（用户）")
	public Result<MenuCO> treeList() {
		return menusServiceI.treeList(new MenuTreeListQry());
	}

	@TraceLog
	@PostMapping("v1/menus/list")
	@Operation(summary = "菜单管理", description = "查询菜单列表")
	@PreAuthorize("hasAuthority('menus:list')")
	public Result<List<MenuCO>> list(@RequestBody MenuListQry qry) {
		return menusServiceI.list(qry);
	}

	@TraceLog
	@GetMapping("v1/menus/{id}")
	@Operation(summary = "菜单管理", description = "查看菜单")
	@DataCache(name = "menus", key = "#id")
	public Result<MenuCO> get(@PathVariable("id") Long id) {
		return menusServiceI.get(new MenuGetQry(id));
	}

	@TraceLog
	@PutMapping("v1/menus")
	@Operation(summary = "菜单管理", description = "修改菜单")
	@OperateLog(module = "菜单管理", operation = "修改菜单")
	@PreAuthorize("hasAuthority('menus:update')")
	@DataCache(name = "menus", key = "#cmd.menuCO.id", type = Cache.DEL)
	public Result<Boolean> update(@RequestBody MenuUpdateCmd cmd) {
		return menusServiceI.update(cmd);
	}

	@TraceLog
	@PostMapping("v1/menus")
	@Operation(summary = "菜单管理", description = "新增菜单")
	@OperateLog(module = "菜单管理", operation = "新增菜单")
	@PreAuthorize("hasAuthority('menus:insert')")
	public Result<Boolean> insert(@RequestBody MenuInsertCmd cmd) {
		return menusServiceI.insert(cmd);
	}

	@TraceLog
	@DeleteMapping("v1/menus/{id}")
	@Operation(summary = "菜单管理", description = "删除菜单")
	@OperateLog(module = "菜单管理", operation = "删除菜单")
	@PreAuthorize("hasAuthority('menus:delete')")
	@DataCache(name = "menus", key = "#id", type = Cache.DEL)
	public Result<Boolean> delete(@PathVariable("id") Long id) {
		return menusServiceI.delete(new MenuDeleteCmd(id));
	}

	@TraceLog
	@GetMapping("v1/menus/tree")
	@Operation(summary = "菜单管理", description = "树形菜单列表")
	public Result<MenuCO> tree() {
		return menusServiceI.tree(new MenuTreeGetQry());
	}

	@TraceLog
	@GetMapping("v1/menus/{roleId}/ids")
	@Operation(summary = "菜单管理", description = "菜单树IDS")
	public Result<List<Long>> ids(@PathVariable(value = "roleId") Long roleId) {
		return menusServiceI.ids(new MenuIDSGetQry(roleId));
	}

	@TraceLog
	@GetMapping("v1/menus/tenant-tree")
	@Operation(summary = "菜单管理", description = "树形租户菜单列表")
	public Result<?> tenantTree() {
		return menusServiceI.tenantTree(new MenuTenantTreeGetQry());
	}

}
