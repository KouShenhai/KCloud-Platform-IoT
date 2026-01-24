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
import org.laokou.admin.source.api.SourcesServiceI;
import org.laokou.admin.source.dto.SourceExportCmd;
import org.laokou.admin.source.dto.SourceGetQry;
import org.laokou.admin.source.dto.SourceImportCmd;
import org.laokou.admin.source.dto.SourceModifyCmd;
import org.laokou.admin.source.dto.SourcePageQry;
import org.laokou.admin.source.dto.SourceRemoveCmd;
import org.laokou.admin.source.dto.SourceSaveCmd;
import org.laokou.admin.source.dto.clientobject.SourceCO;
import org.laokou.common.data.cache.annotation.DistributedCache;
import org.laokou.common.data.cache.constant.NameConstants;
import org.laokou.common.data.cache.model.OperateType;
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

/**
 * 数据源管理控制器.
 *
 * @author laokou
 */
@RestController
@RequiredArgsConstructor
@Tag(name = "数据源管理", description = "数据源管理")
public class SourcesController {

	private final SourcesServiceI sourcesServiceI;

	@Idempotent
	@PostMapping("/v1/sources")
	@PreAuthorize("hasAuthority('sys:source:save')")
	@OperateLog(module = "数据源管理", operation = "保存数据源")
	@Operation(summary = "保存数据源", description = "保存数据源")
	public void saveSource(@RequestBody SourceSaveCmd cmd) {
		sourcesServiceI.saveSource(cmd);
	}

	@PutMapping("/v1/sources")
	@PreAuthorize("hasAuthority('sys:source:modify')")
	@OperateLog(module = "数据源管理", operation = "修改数据源")
	@Operation(summary = "修改数据源", description = "修改数据源")
	@DistributedCache(name = NameConstants.SOURCES, key = "#cmd.co.id", operateType = OperateType.DEL)
	public void modifySource(@RequestBody SourceModifyCmd cmd) {
		sourcesServiceI.modifySource(cmd);
	}

	@DeleteMapping("/v1/sources")
	@PreAuthorize("hasAuthority('sys:source:remove')")
	@OperateLog(module = "数据源管理", operation = "删除数据源")
	@Operation(summary = "删除数据源", description = "删除数据源")
	public void removeSource(@RequestBody Long[] ids) {
		sourcesServiceI.removeSource(new SourceRemoveCmd(ids));
	}

	@PostMapping(value = "/v1/sources/import", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	@PreAuthorize("hasAuthority('sys:source:import')")
	@OperateLog(module = "数据源管理", operation = "导入数据源")
	@Operation(summary = "导入数据源", description = "导入数据源")
	public void importSource(@RequestPart("files") MultipartFile[] files) {
		sourcesServiceI.importSource(new SourceImportCmd(files));
	}

	@PostMapping("/v1/sources/export")
	@PreAuthorize("hasAuthority('sys:source:export')")
	@OperateLog(module = "数据源管理", operation = "导出数据源")
	@Operation(summary = "导出数据源", description = "导出数据源")
	public void exportSource(@RequestBody SourceExportCmd cmd) {
		sourcesServiceI.exportSource(cmd);
	}

	@TraceLog
	@PostMapping("/v1/sources/page")
	@PreAuthorize("hasAuthority('sys:source:page')")
	@Operation(summary = "分页查询数据源列表", description = "分页查询数据源列表")
	public Result<Page<SourceCO>> pageSource(@Validated @RequestBody SourcePageQry qry) {
		return sourcesServiceI.pageSource(qry);
	}

	@TraceLog
	@GetMapping("/v1/sources/{id}")
	@DistributedCache(name = NameConstants.SOURCES, key = "#id")
	@PreAuthorize("hasAuthority('sys:source:detail')")
	@Operation(summary = "查看数据源详情", description = "查看数据源详情")
	public Result<SourceCO> getSourceById(@PathVariable("id") Long id) {
		return sourcesServiceI.getSourceById(new SourceGetQry(id));
	}

}
