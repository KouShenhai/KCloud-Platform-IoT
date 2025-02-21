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

package org.laokou.generator.web;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.laokou.common.i18n.dto.Result;
import org.springframework.http.MediaType;
import org.laokou.generator.column.dto.clientobject.ColumnCO;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.laokou.generator.column.api.ColumnsServiceI;
import org.laokou.generator.column.dto.*;
import org.laokou.common.i18n.dto.Page;
import org.laokou.common.idempotent.annotation.Idempotent;
import org.laokou.common.log.annotation.OperateLog;
import org.laokou.common.trace.annotation.TraceLog;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.multipart.MultipartFile;

/**
 *
 * 代码生成器字段管理控制器.
 *
 * @author laokou
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("v3/columns")
@Tag(name = "代码生成器字段管理", description = "代码生成器字段管理")
public class ColumnsControllerV3 {

	private final ColumnsServiceI columnsServiceI;

	@Idempotent
	@PostMapping
	@PreAuthorize("hasAuthority('generator:column:save')")
	@OperateLog(module = "代码生成器字段管理", operation = "保存代码生成器字段")
	@Operation(summary = "保存代码生成器字段", description = "保存代码生成器字段")
	public void saveV3(@RequestBody ColumnSaveCmd cmd) {
		columnsServiceI.save(cmd);
	}

	@PutMapping
	@PreAuthorize("hasAuthority('generator:column:modify')")
	@OperateLog(module = "代码生成器字段管理", operation = "修改代码生成器字段")
	@Operation(summary = "修改代码生成器字段", description = "修改代码生成器字段")
	public void modifyV3(@RequestBody ColumnModifyCmd cmd) {
		columnsServiceI.modify(cmd);
	}

	@DeleteMapping
	@PreAuthorize("hasAuthority('generator:column:remove')")
	@OperateLog(module = "代码生成器字段管理", operation = "删除代码生成器字段")
	@Operation(summary = "删除代码生成器字段", description = "删除代码生成器字段")
	public void removeV3(@RequestBody Long[] ids) {
		columnsServiceI.remove(new ColumnRemoveCmd(ids));
	}

	@PostMapping(value = "import", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	@PreAuthorize("hasAuthority('generator:column:import')")
	@OperateLog(module = "代码生成器字段管理", operation = "导入代码生成器字段")
	@Operation(summary = "导入代码生成器字段", description = "导入代码生成器字段")
	public void importV3(@RequestPart("files") MultipartFile[] files) {
		columnsServiceI.importI(new ColumnImportCmd(files));
	}

	@PostMapping("export")
	@PreAuthorize("hasAuthority('generator:column:export')")
	@OperateLog(module = "代码生成器字段管理", operation = "导出代码生成器字段")
	@Operation(summary = "导出代码生成器字段", description = "导出代码生成器字段")
	public void exportV3(@RequestBody ColumnExportCmd cmd) {
		columnsServiceI.export(cmd);
	}

	@TraceLog
	@PostMapping("page")
	@PreAuthorize("hasAuthority('generator:column:page')")
	@Operation(summary = "分页查询代码生成器字段列表", description = "分页查询代码生成器字段列表")
	public Result<Page<ColumnCO>> pageV3(@Validated @RequestBody ColumnPageQry qry) {
		return columnsServiceI.page(qry);
	}

	@TraceLog
	@GetMapping("{id}")
	@Operation(summary = "查看代码生成器字段详情", description = "查看代码生成器字段详情")
	public Result<ColumnCO> getByIdV3(@PathVariable("id") Long id) {
		return columnsServiceI.getById(new ColumnGetQry(id));
	}

}
