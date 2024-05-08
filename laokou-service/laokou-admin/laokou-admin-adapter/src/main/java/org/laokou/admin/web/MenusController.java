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
import org.laokou.admin.api.MenusServiceI;
import org.laokou.admin.dto.menu.MenuListQry;
import org.laokou.admin.dto.menu.clientobject.MenuCO;
import org.laokou.admin.dto.menu.clientobject.RouterCO;
import org.laokou.common.i18n.dto.Result;
import org.laokou.common.trace.annotation.TraceLog;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

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
	@GetMapping("v1/menus/routers")
	@Operation(summary = "菜单管理", description = "查询用户菜单路由列表")
	public Result<List<RouterCO>> getRoutersV1() {
		return menusServiceI.getRouters();
	}

	@TraceLog
	@PostMapping("v1/menus/list")
	@Operation(summary = "菜单管理", description = "查询菜单列表")
	@PreAuthorize("hasAuthority('menu:list')")
	public Result<List<MenuCO>> listV1(@RequestBody MenuListQry qry) {
		return menusServiceI.list(qry);
	}

/*

	@TraceLog
	@GetMapping("v1/menus/{id}")
	@Operation(summary = "菜单管理", description = "查看菜单")
	@DataCache(name = MENUS, key = "#id")
	public Result<MenuCO> findById(@PathVariable("id") Long id) {
		return menusServiceI.findById(new MenuGetQry(id));
	}

	@PutMapping("v1/menus")
	@Operation(summary = "菜单管理", description = "修改菜单")
	@OperateLog(module = "菜单管理", operation = "修改菜单")
	@PreAuthorize("hasAuthority('menu:modify')")
	@DataCache(name = MENUS, key = "#cmd.menuCO.id", type = TypeEnum.DEL)
	public void modify(@RequestBody MenuModifyCmd cmd) {
		menusServiceI.modify(cmd);
	}

	@Idempotent
	@PostMapping("v1/menus")
	@Operation(summary = "菜单管理", description = "新增菜单")
	@OperateLog(module = "菜单管理", operation = "新增菜单")
	@PreAuthorize("hasAuthority('menu:create')")
	public void create(@RequestBody MenuCreateCmd cmd) {
		menusServiceI.create(cmd);
	}

	@DeleteMapping("v1/menus")
	@Operation(summary = "菜单管理", description = "删除菜单")
	@OperateLog(module = "菜单管理", operation = "删除菜单")
	@PreAuthorize("hasAuthority('menu:remove')")
	public void remove(@RequestBody Long[] ids) {
		menusServiceI.remove(new MenuRemoveCmd(ids));
	}

	@TraceLog
	@GetMapping("v1/menus/{roleId}/ids")
	@Operation(summary = "菜单管理", description = "菜单树IDS")
	public Result<List<Long>> findIds(@PathVariable("roleId") Long roleId) {
		return menusServiceI.findIds(new MenuIdsGetQry(roleId));
	}

	@TraceLog
	@PostMapping("v1/menus/tenant-menu-list")
	@Operation(summary = "菜单管理", description = "查询租户菜单列表")
	public Result<List<MenuCO>> findTenantMenuList(@RequestBody MenuTenantListQry qry) {
		return menusServiceI.findTenantMenuList(qry);
	}
*/

}
