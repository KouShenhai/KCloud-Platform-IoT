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
import org.laokou.admin.dictType.api.DictTypesServiceI;
import org.laokou.admin.dictType.dto.*;
import org.laokou.admin.dictType.dto.clientobject.DictTypeCO;
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
 * 字典类型管理控制器.
 *
 * @author laokou
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("v3/dict-types")
@Tag(name = "字典类型管理", description = "字典类型管理")
public class DictTypesControllerV3 {

	private final DictTypesServiceI dictTypesServiceI;

	@Idempotent
	@PostMapping
	@PreAuthorize("hasAuthority('dict-type:save')")
	@OperateLog(module = "保存字典类型", operation = "保存字典类型")
	@Operation(summary = "保存字典类型", description = "保存字典类型")
	public void saveV3(@RequestBody DictTypeSaveCmd cmd) {
		dictTypesServiceI.save(cmd);
	}

	@PutMapping
	@PreAuthorize("hasAuthority('dict-type:modify')")
	@OperateLog(module = "修改字典类型", operation = "修改字典类型")
	@Operation(summary = "修改字典类型", description = "修改字典类型")
	public void modifyV3(@RequestBody DictTypeModifyCmd cmd) {
		dictTypesServiceI.modify(cmd);
	}

	@DeleteMapping
	@PreAuthorize("hasAuthority('dict-type:remove')")
	@OperateLog(module = "删除字典类型", operation = "删除字典类型")
	@Operation(summary = "删除字典类型", description = "删除字典类型")
	public void removeV3(@RequestBody Long[] ids) {
		dictTypesServiceI.remove(new DictTypeRemoveCmd(ids));
	}

	@PostMapping(value = "import", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	@PreAuthorize("hasAuthority('dict-type:import')")
	@Operation(summary = "导入字典类型", description = "导入字典类型")
	@OperateLog(module = "导入字典类型", operation = "导入字典类型")
	public void importV3(@RequestPart("file") MultipartFile[] files) {
		dictTypesServiceI.importI(new DictTypeImportCmd(files));
	}

	@PostMapping("export")
	@PreAuthorize("hasAuthority('dict-type:export')")
	@Operation(summary = "导出字典类型", description = "导出字典类型")
	@OperateLog(module = "导出字典类型", operation = "导出字典类型")
	public void exportV3(@RequestBody DictTypeExportCmd cmd) {
		dictTypesServiceI.export(cmd);
	}

	@TraceLog
	@PostMapping("page")
	@PreAuthorize("hasAuthority('dict-type:page')")
	@Operation(summary = "分页查询字典类型列表", description = "分页查询字典类型列表")
	public Result<Page<DictTypeCO>> pageV3(@RequestBody DictTypePageQry qry) {
		return dictTypesServiceI.page(qry);
	}

	@TraceLog
	@GetMapping("{id}")
	@Operation(summary = "查看字典类型详情", description = "查看字典类型详情")
	public Result<DictTypeCO> getByIdV3(@PathVariable("id") Long id) {
		return dictTypesServiceI.getById(new DictTypeGetQry(id));
	}

}
