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

package org.laokou.generator.web;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.laokou.common.i18n.dto.Page;
import org.laokou.common.i18n.dto.Result;
import org.laokou.common.idempotent.annotation.Idempotent;
import org.laokou.common.log.annotation.OperateLog;
import org.laokou.common.trace.annotation.TraceLog;
import org.laokou.generator.column.api.ColumnsServiceI;
import org.laokou.generator.column.dto.ColumnExportCmd;
import org.laokou.generator.column.dto.ColumnGetQry;
import org.laokou.generator.column.dto.ColumnImportCmd;
import org.laokou.generator.column.dto.ColumnModifyCmd;
import org.laokou.generator.column.dto.ColumnPageQry;
import org.laokou.generator.column.dto.ColumnRemoveCmd;
import org.laokou.generator.column.dto.ColumnSaveCmd;
import org.laokou.generator.column.dto.clientobject.ColumnCO;
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
 *
 * 代码生成器字段管理控制器.
 *
 * @author laokou
 */
@RestController
@RequiredArgsConstructor
@Tag(name = "代码生成器字段管理", description = "代码生成器字段管理")
public class ColumnsController {

	private final ColumnsServiceI columnsServiceI;

	@Idempotent
	@PostMapping("/v1/columns")
	@PreAuthorize("hasAuthority('generator:column:save')")
	@OperateLog(module = "代码生成器字段管理", operation = "保存代码生成器字段")
	@Operation(summary = "保存代码生成器字段", description = "保存代码生成器字段")
	public void saveColumn(@RequestBody ColumnSaveCmd cmd) {
		columnsServiceI.saveColumn(cmd);
	}

	@PutMapping("/v1/columns")
	@PreAuthorize("hasAuthority('generator:column:modify')")
	@OperateLog(module = "代码生成器字段管理", operation = "修改代码生成器字段")
	@Operation(summary = "修改代码生成器字段", description = "修改代码生成器字段")
	public void modifyColumn(@RequestBody ColumnModifyCmd cmd) {
		columnsServiceI.modifyColumn(cmd);
	}

	@DeleteMapping("/v1/columns/")
	@PreAuthorize("hasAuthority('generator:column:remove')")
	@OperateLog(module = "代码生成器字段管理", operation = "删除代码生成器字段")
	@Operation(summary = "删除代码生成器字段", description = "删除代码生成器字段")
	public void removeColumn(@RequestBody Long[] ids) {
		columnsServiceI.removeColumn(new ColumnRemoveCmd(ids));
	}

	@PostMapping(value = "/v1/columns/import", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	@PreAuthorize("hasAuthority('generator:column:import')")
	@OperateLog(module = "代码生成器字段管理", operation = "导入代码生成器字段")
	@Operation(summary = "导入代码生成器字段", description = "导入代码生成器字段")
	public void importColumn(@RequestPart("files") MultipartFile[] files) {
		columnsServiceI.importColumn(new ColumnImportCmd(files));
	}

	@PostMapping("/v1/columns/export")
	@PreAuthorize("hasAuthority('generator:column:export')")
	@OperateLog(module = "代码生成器字段管理", operation = "导出代码生成器字段")
	@Operation(summary = "导出代码生成器字段", description = "导出代码生成器字段")
	public void exportColumn(@RequestBody ColumnExportCmd cmd) {
		columnsServiceI.exportColumn(cmd);
	}

	@TraceLog
	@PostMapping("/v1/columns/page")
	@PreAuthorize("hasAuthority('generator:column:page')")
	@Operation(summary = "分页查询代码生成器字段列表", description = "分页查询代码生成器字段列表")
	public Result<Page<ColumnCO>> pageColumn(@Validated @RequestBody ColumnPageQry qry) {
		return columnsServiceI.pageColumn(qry);
	}

	@TraceLog
	@GetMapping("/v1/columns/{id}")
	@Operation(summary = "查看代码生成器字段详情", description = "查看代码生成器字段详情")
	public Result<ColumnCO> getColumnById(@PathVariable("id") Long id) {
		return columnsServiceI.getColumnById(new ColumnGetQry(id));
	}

}
