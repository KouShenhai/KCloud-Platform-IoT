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
import org.laokou.admin.dict.api.DictsServiceI;
import org.laokou.admin.dict.dto.*;
import org.laokou.admin.dict.dto.clientobject.DictCO;
import org.laokou.common.i18n.dto.Page;
import org.laokou.common.i18n.dto.Result;
import org.laokou.common.idempotent.annotation.Idempotent;
import org.laokou.common.log.annotation.OperateLog;
import org.laokou.common.trace.annotation.TraceLog;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

/**
 * 字典管理控制器.
 *
 * @author laokou
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("v3/dicts")
@Tag(name = "字典管理", description = "字典管理")
public class DictsControllerV3 {

	private final DictsServiceI dictsServiceI;

	@Idempotent
	@PostMapping
	@PreAuthorize("hasAuthority('sys:dict:save')")
	@OperateLog(module = "字典管理", operation = "保存字典")
	@Operation(summary = "保存字典", description = "保存字典")
	public void saveV3(@RequestBody DictSaveCmd cmd) {
		dictsServiceI.save(cmd);
	}

	@PutMapping
	@PreAuthorize("hasAuthority('sys:dict:modify')")
	@OperateLog(module = "字典管理", operation = "修改字典")
	@Operation(summary = "修改字典", description = "修改字典")
	public void modifyV3(@RequestBody DictModifyCmd cmd) {
		dictsServiceI.modify(cmd);
	}

	@DeleteMapping
	@PreAuthorize("hasAuthority('sys:dict:remove')")
	@OperateLog(module = "字典管理", operation = "删除字典")
	@Operation(summary = "删除字典", description = "删除字典")
	public void removeV3(@RequestBody Long[] ids) {
		dictsServiceI.remove(new DictRemoveCmd(ids));
	}

	@PostMapping(value = "import", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	@PreAuthorize("hasAuthority('sys:dict:import')")
	@OperateLog(module = "字典管理", operation = "导入字典")
	@Operation(summary = "导入字典", description = "导入字典")
	public void importV3(@RequestPart("file") MultipartFile[] files) {
		dictsServiceI.importI(new DictImportCmd(files));
	}

	@PostMapping("export")
	@PreAuthorize("hasAuthority('sys:dict:export')")
	@OperateLog(module = "字典管理", operation = "导出字典")
	@Operation(summary = "导出字典", description = "导出字典")
	public void exportV3(@RequestBody DictExportCmd cmd) {
		dictsServiceI.export(cmd);
	}

	@TraceLog
	@PostMapping("page")
	@PreAuthorize("hasAuthority('sys:dict:page')")
	@Operation(summary = "分页查询字典列表", description = "分页查询字典列表")
	public Result<Page<DictCO>> pageV3(@RequestBody DictPageQry qry) {
		return dictsServiceI.page(qry);
	}

	@TraceLog
	@GetMapping("{id}")
	@Operation(summary = "查看字典详情", description = "查看字典详情")
	public Result<DictCO> getByIdV3(@PathVariable("id") Long id) {
		return dictsServiceI.getById(new DictGetQry(id));
	}

}
