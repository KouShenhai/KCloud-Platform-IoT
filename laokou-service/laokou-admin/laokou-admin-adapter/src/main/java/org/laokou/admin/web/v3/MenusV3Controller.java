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

package org.laokou.admin.web.v3;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
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
@Tag(name = "MenusV3Controller", description = "菜单管理")
@RequiredArgsConstructor
@RequestMapping("v3/menus")
public class MenusV3Controller {

	@TraceLog
	@GetMapping("routers")
	@Operation(summary = "菜单管理", description = "查询用户菜单路由列表")
	public Result<List<RouterCO>> getRoutersV3() {
		return null;
	}

	@TraceLog
	@PostMapping("list")
	@Operation(summary = "菜单管理", description = "查询菜单列表")
	@PreAuthorize("hasAuthority('menu:list')")
	public Result<?> listV3() {
		return null;
	}

	@TraceLog
	@GetMapping("{id}")
	@Operation(summary = "菜单管理", description = "查看菜单详情")
	@DataCache(name = MENUS, key = "#id")
	public Result<?> getByIdV3(@PathVariable("id") Long id) {
		return null;
	}

	@PutMapping
	@Operation(summary = "菜单管理", description = "修改菜单")
	@OperateLog(module = "菜单管理", operation = "修改菜单")
	@PreAuthorize("hasAuthority('menu:modify')")
	@DataCache(name = MENUS, key = "#cmd.co.id", type = DEL)
	public void modifyV3() {

	}

	@Idempotent
	@PostMapping
	@Operation(summary = "菜单管理", description = "新增菜单")
	@OperateLog(module = "菜单管理", operation = "新增菜单")
	@PreAuthorize("hasAuthority('menu:save')")
	public void saveV3() {

	}

	@DeleteMapping
	@Operation(summary = "菜单管理", description = "删除菜单")
	@OperateLog(module = "菜单管理", operation = "删除菜单")
	@PreAuthorize("hasAuthority('menu:remove')")
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
