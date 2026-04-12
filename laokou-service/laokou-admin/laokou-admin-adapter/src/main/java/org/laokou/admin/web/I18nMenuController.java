/*
 * Copyright (c) 2022-2026 KCloud-Platform-IoT Author or Authors. All Rights Reserved.
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
import org.laokou.admin.i18nMenu.api.I18nMenusServiceI;
import org.laokou.admin.i18nMenu.dto.I18nMenuExportCmd;
import org.laokou.admin.i18nMenu.dto.I18nMenuGetQry;
import org.laokou.admin.i18nMenu.dto.I18nMenuImportCmd;
import org.laokou.admin.i18nMenu.dto.I18nMenuModifyCmd;
import org.laokou.admin.i18nMenu.dto.I18nMenuPageQry;
import org.laokou.admin.i18nMenu.dto.I18nMenuRemoveCmd;
import org.laokou.admin.i18nMenu.dto.I18nMenuSaveCmd;
import org.laokou.admin.i18nMenu.dto.clientobject.I18nMenuCO;
import org.laokou.common.i18n.dto.Page;
import org.laokou.common.i18n.dto.Result;
import org.laokou.common.idempotent.annotation.Idempotent;
import org.laokou.common.log.annotation.OperateLog;
import org.laokou.common.mybatisplus.annotation.DataFilter;
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

/**
 * 国际化菜单管理控制器.
 *
 * @author laokou
 */
@RestController
@RequiredArgsConstructor
@Tag(name = "国际化菜单管理", description = "国际化菜单管理")
public class I18nMenuController {

	private final I18nMenusServiceI i18nMenusServiceI;

	@Idempotent
	@PostMapping("/v1/i18n-menus")
	@PreAuthorize("hasAuthority('write') and hasAuthority('sys:i18n-menu:save')")
	@OperateLog(module = "国际化菜单管理", operation = "保存国际化菜单")
	@Operation(summary = "保存国际化菜单", description = "保存国际化菜单")
	public void saveI18nMenu(@RequestBody I18nMenuSaveCmd cmd) {
		i18nMenusServiceI.saveI18nMenu(cmd);
	}

	@PutMapping("/v1/i18n-menus")
	@PreAuthorize("hasAuthority('write') and hasAuthority('sys:i18n-menu:modify')")
	@OperateLog(module = "国际化菜单管理", operation = "修改国际化菜单")
	@Operation(summary = "修改国际化菜单", description = "修改国际化菜单")
	public void modifyI18nMenu(@RequestBody I18nMenuModifyCmd cmd) {
		i18nMenusServiceI.modifyI18nMenu(cmd);
	}

	@DeleteMapping("/v1/i18n-menus")
	@PreAuthorize("hasAuthority('write') and hasAuthority('sys:i18n-menu:remove')")
	@OperateLog(module = "国际化菜单管理", operation = "删除国际化菜单")
	@Operation(summary = "删除国际化菜单", description = "删除国际化菜单")
	public void removeI18nMenu(@RequestBody Long[] ids) {
		i18nMenusServiceI.removeI18nMenu(new I18nMenuRemoveCmd(ids));
	}

	@PostMapping(value = "/v1/i18n-menus/import", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	@PreAuthorize("hasAuthority('write') and hasAuthority('sys:i18n-menu:import')")
	@OperateLog(module = "国际化菜单管理", operation = "导入国际化菜单")
	@Operation(summary = "导入国际化菜单", description = "导入国际化菜单")
	public void importI18nMenu(@RequestPart("files") MultipartFile[] files) {
		i18nMenusServiceI.importI18nMenu(new I18nMenuImportCmd(files));
	}

	@PostMapping("/v1/i18n-menus/export")
	@PreAuthorize("hasAuthority('write') and hasAuthority('sys:i18n-menu:export')")
	@OperateLog(module = "国际化菜单管理", operation = "导出国际化菜单")
	@Operation(summary = "导出国际化菜单", description = "导出国际化菜单")
	public void exportI18nMenu(@RequestBody I18nMenuExportCmd cmd) {
		i18nMenusServiceI.exportI18nMenu(cmd);
	}

	@TraceLog
	@DataFilter
	@PostMapping("/v1/i18n-menus/page")
	@PreAuthorize("hasAuthority('read') and hasAuthority('sys:i18n-menu:page')")
	@Operation(summary = "分页查询国际化菜单列表", description = "分页查询国际化菜单列表")
	public Result<Page<I18nMenuCO>> pageI18nMenu(@Validated @RequestBody I18nMenuPageQry qry) {
		return i18nMenusServiceI.pageI18nMenu(qry);
	}

	@TraceLog
	@GetMapping("/v1/i18n-menus/{id}")
	@PreAuthorize("hasAuthority('read') and hasAuthority('sys:i18n-menu:detail')")
	@Operation(summary = "查看国际化菜单详情", description = "查看国际化菜单详情")
	public Result<I18nMenuCO> getI18nMenuById(@PathVariable("id") Long id) {
		return i18nMenusServiceI.getI18nMenuById(new I18nMenuGetQry(id));
	}

}
