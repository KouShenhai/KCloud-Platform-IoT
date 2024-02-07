/*
 * Copyright (c) 2022-2024 KCloud-Platform-Alibaba Author or Authors. All Rights Reserved.
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
import org.laokou.admin.api.MenusServiceI;
import org.laokou.admin.dto.menu.*;
import org.laokou.admin.dto.menu.clientobject.MenuCO;
import org.laokou.admin.domain.annotation.OperateLog;
import org.laokou.common.data.cache.annotation.DataCache;
import org.laokou.common.i18n.common.CacheOperatorTypeEnums;
import org.laokou.common.i18n.dto.Result;
import org.laokou.common.idempotent.annotation.Idempotent;
import org.laokou.common.trace.annotation.TraceLog;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.laokou.common.i18n.common.CacheNameConstants.MENUS;

/**
 * @author laokou
 */
@RestController
@Tag(name = "MenusController", description = "菜单管理")
@RequiredArgsConstructor
@RequestMapping("v1/menus")
public class MenusController {

	private final MenusServiceI menusServiceI;

	@TraceLog
	@PostMapping("list")
	@Operation(summary = "菜单管理", description = "查询菜单列表")
	@PreAuthorize("hasAuthority('menus:list')")
	public Result<List<MenuCO>> findList(@RequestBody MenuListQry qry) {
		return menusServiceI.findList(qry);
	}

	@TraceLog
	@GetMapping("{id}")
	@Operation(summary = "菜单管理", description = "查看菜单")
	@DataCache(name = MENUS, key = "#id")
	public Result<MenuCO> findById(@PathVariable("id") Long id) {
		return menusServiceI.findById(new MenuGetQry(id));
	}

	@TraceLog
	@PutMapping
	@Operation(summary = "菜单管理", description = "修改菜单")
	@OperateLog(module = "菜单管理", operation = "修改菜单")
	@PreAuthorize("hasAuthority('menus:modify')")
	@DataCache(name = MENUS, key = "#cmd.menuCO.id", type = CacheOperatorTypeEnums.DEL)
	public void modify(@RequestBody MenuModifyCmd cmd) {
		menusServiceI.modify(cmd);
	}

	@Idempotent
	@TraceLog
	@PostMapping
	@Operation(summary = "菜单管理", description = "新增菜单")
	@OperateLog(module = "菜单管理", operation = "新增菜单")
	@PreAuthorize("hasAuthority('menus:create')")
	public void create(@RequestBody MenuCreateCmd cmd) {
		menusServiceI.create(cmd);
	}

	@TraceLog
	@DeleteMapping
	@Operation(summary = "菜单管理", description = "删除菜单")
	@OperateLog(module = "菜单管理", operation = "删除菜单")
	@PreAuthorize("hasAuthority('menus:remove')")
	public void remove(@RequestBody Long[] ids) {
		menusServiceI.remove(new MenuRemoveCmd(ids));
	}

	@TraceLog
	@GetMapping("{roleId}/ids")
	@Operation(summary = "菜单管理", description = "菜单树IDS")
	public Result<List<Long>> findIds(@PathVariable("roleId") Long roleId) {
		return menusServiceI.ids(new MenuIDSGetQry(roleId));
	}

	@TraceLog
	@GetMapping("tenant-list")
	@Operation(summary = "菜单管理", description = "树形租户菜单列表")
	public Result<MenuCO> findTenantTreeList() {
		return menusServiceI.tenantTree(new MenuTenantTreeGetQry());
	}

}
