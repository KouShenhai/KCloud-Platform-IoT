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
import org.laokou.admin.source.api.SourcesServiceI;
import org.laokou.admin.source.dto.*;
import org.laokou.admin.source.dto.clientobject.SourceCO;
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

import static org.laokou.common.data.cache.constant.NameConstant.SOURCES;
import static org.laokou.common.data.cache.constant.Type.DEL;

/**
 * 数据源管理控制器.
 *
 * @author laokou
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("v3/sources")
@Tag(name = "数据源管理", description = "数据源管理")
public class SourcesControllerV3 {

	private final SourcesServiceI sourcesServiceI;

	@Idempotent
	@PostMapping
	@PreAuthorize("hasAuthority('sys:source:save')")
	@OperateLog(module = "数据源管理", operation = "保存数据源")
	@Operation(summary = "保存数据源", description = "保存数据源")
	public void saveV3(@RequestBody SourceSaveCmd cmd) {
		sourcesServiceI.save(cmd);
	}

	@PutMapping
	@PreAuthorize("hasAuthority('sys:source:modify')")
	@OperateLog(module = "数据源管理", operation = "修改数据源")
	@Operation(summary = "修改数据源", description = "修改数据源")
	@DataCache(name = SOURCES, key = "#cmd.co.id", type = DEL)
	public void modifyV3(@RequestBody SourceModifyCmd cmd) {
		sourcesServiceI.modify(cmd);
	}

	@DeleteMapping
	@PreAuthorize("hasAuthority('sys:source:remove')")
	@OperateLog(module = "数据源管理", operation = "删除数据源")
	@Operation(summary = "删除数据源", description = "删除数据源")
	public void removeV3(@RequestBody Long[] ids) {
		sourcesServiceI.remove(new SourceRemoveCmd(ids));
	}

	@PostMapping(value = "import", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	@PreAuthorize("hasAuthority('sys:source:import')")
	@OperateLog(module = "数据源管理", operation = "导入数据源")
	@Operation(summary = "导入数据源", description = "导入数据源")
	public void importV3(@RequestPart("file") MultipartFile[] files) {
		sourcesServiceI.importI(new SourceImportCmd(files));
	}

	@PostMapping("export")
	@PreAuthorize("hasAuthority('sys:source:export')")
	@OperateLog(module = "数据源管理", operation = "导出数据源")
	@Operation(summary = "导出数据源", description = "导出数据源")
	public void exportV3(@RequestBody SourceExportCmd cmd) {
		sourcesServiceI.export(cmd);
	}

	@TraceLog
	@PostMapping("page")
	@PreAuthorize("hasAuthority('sys:source:page')")
	@Operation(summary = "分页查询数据源列表", description = "分页查询数据源列表")
	public Result<Page<SourceCO>> pageV3(@RequestBody SourcePageQry qry) {
		return sourcesServiceI.page(qry);
	}

	@TraceLog
	@GetMapping("{id}")
	@DataCache(name = SOURCES, key = "#id")
	@PreAuthorize("hasAuthority('sys:source:detail')")
	@Operation(summary = "查看数据源详情", description = "查看数据源详情")
	public Result<SourceCO> getByIdV3(@PathVariable("id") Long id) {
		return sourcesServiceI.getById(new SourceGetQry(id));
	}

}
