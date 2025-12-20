/*
 * Copyright (c) 2022-2025 KCloud-Platform-IoT Author or Authors. All Rights Reserved.
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
import org.laokou.admin.menu.dto.MenuExportCmd;
import org.laokou.admin.menu.dto.MenuGetQry;
import org.laokou.admin.menu.dto.MenuImportCmd;
import org.laokou.admin.menu.dto.MenuModifyCmd;
import org.laokou.admin.menu.dto.MenuPageQry;
import org.laokou.admin.menu.dto.MenuRemoveCmd;
import org.laokou.admin.menu.dto.MenuSaveCmd;
import org.laokou.admin.menu.dto.MenuTreeListQry;
import org.laokou.admin.menu.dto.clientobject.MenuCO;
import org.laokou.admin.menu.dto.clientobject.MenuTreeCO;
import org.laokou.common.data.cache.annotation.DistributedCache;
import org.laokou.common.data.cache.constant.NameConstants;
import org.laokou.common.data.cache.model.OperateTypeEnum;
import org.laokou.common.i18n.dto.Page;
import org.laokou.common.i18n.dto.Result;
import org.laokou.common.idempotent.annotation.Idempotent;
import org.laokou.common.log.annotation.OperateLog;
import org.laokou.common.trace.annotation.TraceLog;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * 菜单管理控制器.
 *
 * @author laokou
 */
@RestController
@RequiredArgsConstructor
@Tag(name = "菜单管理", description = "菜单管理")
public class MenusController {

	private final MenusServiceI menusServiceI;

	@Idempotent
	@PostMapping("/v1/menus")
	@PreAuthorize("hasAuthority('sys:menu:save')")
	@OperateLog(module = "菜单管理", operation = "保存菜单")
	@Operation(summary = "保存菜单", description = "保存菜单")
	public void saveMenu(@RequestBody MenuSaveCmd cmd) {
		menusServiceI.saveMenu(cmd);
	}

	@PutMapping("/v1/menus")
	@PreAuthorize("hasAuthority('sys:menu:modify')")
	@OperateLog(module = "菜单管理", operation = "修改菜单")
	@Operation(summary = "修改菜单", description = "修改菜单")
	@DistributedCache(name = NameConstants.MENUS, key = "#cmd.co.id", operateType = OperateTypeEnum.DEL)
	public void modifyMenu(@RequestBody MenuModifyCmd cmd) {
		menusServiceI.modifyMenu(cmd);
	}

	@DeleteMapping("/v1/menus")
	@PreAuthorize("hasAuthority('sys:menu:remove')")
	@OperateLog(module = "菜单管理", operation = "删除菜单")
	@Operation(summary = "删除菜单", description = "删除菜单")
	public void removeMenu(@RequestBody Long[] ids) {
		menusServiceI.removeMenu(new MenuRemoveCmd(ids));
	}

	@PostMapping(value = "/v1/menus/import", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	@PreAuthorize("hasAuthority('sys:menu:import')")
	@OperateLog(module = "菜单管理", operation = "导入菜单")
	@Operation(summary = "导入菜单", description = "导入菜单")
	public void importMenu(@RequestPart("files") MultipartFile[] files) {
		menusServiceI.importMenu(new MenuImportCmd(files));
	}

	@PostMapping("/v1/menus/export")
	@PreAuthorize("hasAuthority('sys:menu:export')")
	@OperateLog(module = "菜单管理", operation = "导出菜单")
	@Operation(summary = "导出菜单", description = "导出菜单")
	public void exportMenu(@RequestBody MenuExportCmd cmd) {
		menusServiceI.exportMenu(cmd);
	}

	@TraceLog
	@PostMapping("/v1/menus/page")
	@PreAuthorize("hasAuthority('sys:menu:page')")
	@Operation(summary = "分页查询菜单列表", description = "分页查询菜单列表")
	public Result<Page<MenuCO>> pageMenu(@Validated @RequestBody MenuPageQry qry) {
		return menusServiceI.pageMenu(qry);
	}

	@TraceLog
	@PostMapping("/v1/menus/list-tree")
	@PreAuthorize("hasAuthority('sys:menu:list-tree')")
	@Operation(summary = "查询菜单树列表", description = "查询菜单树列表")
	public Result<List<MenuTreeCO>> listTreeMenu(@RequestBody MenuTreeListQry qry) {
		return menusServiceI.listTreeMenu(qry);
	}

	@TraceLog
	@PostMapping("/v1/menus/list-user-tree")
	@Operation(summary = "查询用户菜单树列表", description = "查询用户菜单树列表")
	public Result<List<MenuTreeCO>> listUserTreeMenu(@RequestBody MenuTreeListQry qry) {
		return menusServiceI.listTreeMenu(qry);
	}

	@TraceLog
	@GetMapping("/v1/menus/{id}")
	@DistributedCache(name = NameConstants.MENUS, key = "#id")
	@PreAuthorize("hasAuthority('sys:menu:detail')")
	@Operation(summary = "查看菜单详情", description = "查看菜单详情")
	public Result<MenuCO> getMenuById(@PathVariable("id") Long id) {
		return menusServiceI.getMenuById(new MenuGetQry(id));
	}

}
