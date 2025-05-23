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
import org.laokou.admin.dictItem.api.DictItemsServiceI;
import org.laokou.admin.dictItem.dto.*;
import org.laokou.admin.dictItem.dto.clientobject.DictItemCO;
import org.laokou.common.i18n.dto.Page;
import org.laokou.common.i18n.dto.Result;
import org.laokou.common.idempotent.annotation.Idempotent;
import org.laokou.common.log.annotation.OperateLog;
import org.laokou.common.trace.annotation.TraceLog;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

/**
 * 字典项管理控制器.
 *
 * @author laokou
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("v3/dict-items")
@Tag(name = "字典项管理", description = "字典项管理")
public class DictItemsControllerV3 {

	private final DictItemsServiceI dictItemsServiceI;

	@Idempotent
	@PostMapping
	@PreAuthorize("hasAuthority('sys:dict-item:save')")
	@OperateLog(module = "字典项管理", operation = "保存字典项")
	@Operation(summary = "保存字典项", description = "保存字典项")
	public void saveDictItem(@RequestBody DictItemSaveCmd cmd) {
		dictItemsServiceI.saveDictItem(cmd);
	}

	@PutMapping
	@PreAuthorize("hasAuthority('sys:dict-item:modify')")
	@OperateLog(module = "字典项管理", operation = "修改字典项")
	@Operation(summary = "修改字典项", description = "修改字典项")
	public void modifyDictItem(@RequestBody DictItemModifyCmd cmd) {
		dictItemsServiceI.modifyDictItem(cmd);
	}

	@DeleteMapping
	@PreAuthorize("hasAuthority('sys:dict-item:remove')")
	@OperateLog(module = "字典项管理", operation = "删除字典项")
	@Operation(summary = "删除字典项", description = "删除字典项")
	public void removeDictItem(@RequestBody Long[] ids) {
		dictItemsServiceI.removeDictItem(new DictItemRemoveCmd(ids));
	}

	@PostMapping(value = "import", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	@PreAuthorize("hasAuthority('sys:dict-item:import')")
	@OperateLog(module = "字典项管理", operation = "导入字典项")
	@Operation(summary = "导入字典项", description = "导入字典项")
	public void importDictItem(@RequestPart("files") MultipartFile[] files) {
		dictItemsServiceI.importDictItem(new DictItemImportCmd(files));
	}

	@PostMapping("export")
	@PreAuthorize("hasAuthority('sys:dict-item:export')")
	@OperateLog(module = "字典项管理", operation = "导出字典项")
	@Operation(summary = "导出字典项", description = "导出字典项")
	public void exportDictItem(@RequestBody DictItemExportCmd cmd) {
		dictItemsServiceI.exportDictItem(cmd);
	}

	@TraceLog
	@PostMapping("page")
	@PreAuthorize("hasAuthority('sys:dict-item:page')")
	@Operation(summary = "分页查询字典项列表", description = "分页查询字典项列表")
	public Result<Page<DictItemCO>> pageDictItem(@Validated @RequestBody DictItemPageQry qry) {
		return dictItemsServiceI.pageDictItem(qry);
	}

	@TraceLog
	@GetMapping("{id}")
	@PreAuthorize("hasAuthority('sys:dict-item:detail')")
	@Operation(summary = "查看字典项详情", description = "查看字典项详情")
	public Result<DictItemCO> getByIdDictItem(@PathVariable("id") Long id) {
		return dictItemsServiceI.getByIdDictItem(new DictItemGetQry(id));
	}

}
