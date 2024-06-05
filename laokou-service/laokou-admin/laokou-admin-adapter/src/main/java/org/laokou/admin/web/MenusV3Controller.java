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
import org.laokou.admin.dto.menu.clientobject.RouterCO;
import org.laokou.common.data.cache.annotation.DataCache;
import org.laokou.common.i18n.dto.Result;
import org.laokou.common.idempotent.annotation.Idempotent;
import org.laokou.common.log.annotation.OperateLog;
import org.laokou.common.trace.annotation.TraceLog;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.laokou.common.data.cache.constant.NameConstant.MENUS;
import static org.laokou.common.data.cache.constant.TypeEnum.DEL;

/**
 * @author laokou
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("v3/menus")
@Tag(name = "菜单管理", description = "菜单管理")
public class MenusV3Controller {

	private final MenusServiceI menusServiceI;

	@TraceLog
	@GetMapping("routers")
	@Operation(summary = "菜单管理-查询用户菜单路由列表", description = "菜单管理-查询用户菜单路由列表")
	public Result<List<RouterCO>> getRoutersV3() {
		return menusServiceI.getRouters();
	}

	@TraceLog
	@PostMapping("list")
	@PreAuthorize("hasAuthority('menu:list')")
	@Operation(summary = "菜单管理", description = "查询菜单列表")
	public Result<?> listV3() {
		return null;
	}

	@TraceLog
	@GetMapping("{id}")
	@DataCache(name = MENUS, key = "#id")
	@Operation(summary = "菜单管理", description = "查看菜单详情")
	public Result<?> getByIdV3(@PathVariable("id") Long id) {
		return null;
	}

	@PutMapping
	@PreAuthorize("hasAuthority('menu:modify')")
	@OperateLog(module = "菜单管理", operation = "修改菜单")
	@Operation(summary = "菜单管理", description = "修改菜单")
	@DataCache(name = MENUS, key = "#cmd.co.id", type = DEL)
	public void modifyV3() {

	}

	@Idempotent
	@PostMapping
	@PreAuthorize("hasAuthority('menu:save')")
	@OperateLog(module = "菜单管理", operation = "新增菜单")
	@Operation(summary = "菜单管理", description = "新增菜单")
	public void saveV3() {

	}

	@DeleteMapping
	@PreAuthorize("hasAuthority('menu:remove')")
	@OperateLog(module = "菜单管理", operation = "删除菜单")
	@Operation(summary = "菜单管理", description = "删除菜单")
	public void removeV3(@RequestBody Long[] ids) {

	}

	@TraceLog
	@GetMapping("ids/{roleId}")
	@Operation(summary = "菜单管理", description = "查看菜单IDS")
	public Result<List<Long>> getIdsByRoleIdV3(@PathVariable("roleId") Long roleId) {
		return null;
	}

	@TraceLog
	@PostMapping("tenant-menu-list")
	@Operation(summary = "菜单管理", description = "查询租户菜单列表")
	public Result<List<?>> listTenantMenuV3() {
		return null;
	}

}
