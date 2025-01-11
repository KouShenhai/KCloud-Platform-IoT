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
import org.laokou.admin.menu.api.MenusServiceI;
import org.laokou.admin.menu.dto.*;
import org.laokou.admin.menu.dto.clientobject.MenuCO;
import org.laokou.admin.menu.dto.clientobject.MenuTreeCO;
import org.laokou.common.data.cache.annotation.DataCache;
import org.laokou.common.i18n.dto.Page;
import org.laokou.common.i18n.dto.Result;
import org.laokou.common.idempotent.annotation.Idempotent;
import org.laokou.common.log.annotation.OperateLog;
import org.laokou.common.trace.annotation.TraceLog;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

import static org.laokou.common.data.cache.constant.NameConstant.MENUS;
import static org.laokou.common.data.cache.constant.Type.DEL;

/**
 * 菜单管理控制器.
 *
 * @author laokou
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("v3/menus")
@Tag(name = "菜单管理", description = "菜单管理")
public class MenusControllerV3 {

	private final MenusServiceI menusServiceI;

	@Idempotent
	@PostMapping
	@PreAuthorize("hasAuthority('sys:menu:save')")
	@OperateLog(module = "菜单管理", operation = "保存菜单")
	@Operation(summary = "保存菜单", description = "保存菜单")
	public void saveV3(@RequestBody MenuSaveCmd cmd) {
		menusServiceI.save(cmd);
	}

	@PutMapping
	@PreAuthorize("hasAuthority('sys:menu:modify')")
	@OperateLog(module = "菜单管理", operation = "修改菜单")
	@Operation(summary = "修改菜单", description = "修改菜单")
	@DataCache(name = MENUS, key = "#cmd.co.id", type = DEL)
	public void modifyV3(@RequestBody MenuModifyCmd cmd) {
		menusServiceI.modify(cmd);
	}

	@DeleteMapping
	@PreAuthorize("hasAuthority('sys:menu:remove')")
	@OperateLog(module = "菜单管理", operation = "删除菜单")
	@Operation(summary = "删除菜单", description = "删除菜单")
	public void removeV3(@RequestBody Long[] ids) {
		menusServiceI.remove(new MenuRemoveCmd(ids));
	}

	@PostMapping(value = "import", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	@PreAuthorize("hasAuthority('sys:menu:import')")
	@OperateLog(module = "菜单管理", operation = "导入菜单")
	@Operation(summary = "导入菜单", description = "导入菜单")
	public void importV3(@RequestPart("file") MultipartFile[] files) {
		menusServiceI.importI(new MenuImportCmd(files));
	}

	@PostMapping("export")
	@PreAuthorize("hasAuthority('sys:menu:export')")
	@OperateLog(module = "菜单管理", operation = "导出菜单")
	@Operation(summary = "导出菜单", description = "导出菜单")
	public void exportV3(@RequestBody MenuExportCmd cmd) {
		menusServiceI.export(cmd);
	}

	@TraceLog
	@PostMapping("page")
	@PreAuthorize("hasAuthority('sys:menu:page')")
	@Operation(summary = "分页查询菜单列表", description = "分页查询菜单列表")
	public Result<Page<MenuCO>> pageV3(@RequestBody MenuPageQry qry) {
		return menusServiceI.page(qry);
	}

	@TraceLog
	@PostMapping("tree-list")
	@Operation(summary = "查询菜单树列表", description = "查询菜单树列表")
	public Result<List<MenuTreeCO>> treeListV3(@RequestBody MenuTreeListQry qry) {
		return menusServiceI.treeList(qry);
	}

	@TraceLog
	@GetMapping("{id}")
	@DataCache(name = MENUS, key = "#id")
	@PreAuthorize("hasAuthority('sys:menu:detail')")
	@Operation(summary = "查看菜单详情", description = "查看菜单详情")
	public Result<MenuCO> getByIdV3(@PathVariable("id") Long id) {
		return menusServiceI.getById(new MenuGetQry(id));
	}

}
