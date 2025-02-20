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
import org.laokou.generator.info.dto.clientobject.InfoCO;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.laokou.generator.info.api.InfosServiceI;
import org.laokou.generator.info.dto.*;
import org.laokou.common.i18n.dto.Page;
import org.laokou.common.idempotent.annotation.Idempotent;
import org.laokou.common.log.annotation.OperateLog;
import org.laokou.common.trace.annotation.TraceLog;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.multipart.MultipartFile;

/**
 *
 * 代码生成器信息管理控制器.
 *
 * @author laokou
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("v3/infos")
@Tag(name = "代码生成器信息管理", description = "代码生成器信息管理")
public class InfosControllerV3 {

	private final InfosServiceI infosServiceI;

	@Idempotent
	@PostMapping
	@PreAuthorize("hasAuthority('generator:info:save')")
	@OperateLog(module = "代码生成器信息管理", operation = "保存代码生成器信息")
	@Operation(summary = "保存代码生成器信息", description = "保存代码生成器信息")
	public void saveV3(@RequestBody InfoSaveCmd cmd) {
		infosServiceI.save(cmd);
	}

	@PutMapping
	@PreAuthorize("hasAuthority('generator:info:modify')")
	@OperateLog(module = "代码生成器信息管理", operation = "修改代码生成器信息")
	@Operation(summary = "修改代码生成器信息", description = "修改代码生成器信息")
	public void modifyV3(@RequestBody InfoModifyCmd cmd) {
		infosServiceI.modify(cmd);
	}

	@DeleteMapping
	@PreAuthorize("hasAuthority('generator:info:remove')")
	@OperateLog(module = "代码生成器信息管理", operation = "删除代码生成器信息")
	@Operation(summary = "删除代码生成器信息", description = "删除代码生成器信息")
	public void removeV3(@RequestBody Long[] ids) {
		infosServiceI.remove(new InfoRemoveCmd(ids));
	}

	@PostMapping(value = "import", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	@PreAuthorize("hasAuthority('generator:info:import')")
	@OperateLog(module = "代码生成器信息管理", operation = "导入代码生成器信息")
	@Operation(summary = "导入代码生成器信息", description = "导入代码生成器信息")
	public void importV3(@RequestPart("file") MultipartFile[] files) {
		infosServiceI.importI(new InfoImportCmd(files));
	}

	@PostMapping("export")
	@PreAuthorize("hasAuthority('generator:info:export')")
	@OperateLog(module = "代码生成器信息管理", operation = "导出代码生成器信息")
	@Operation(summary = "导出代码生成器信息", description = "导出代码生成器信息")
	public void exportV3(@RequestBody InfoExportCmd cmd) {
		infosServiceI.export(cmd);
	}

	@TraceLog
	@PostMapping("page")
	@PreAuthorize("hasAuthority('generator:info:page')")
	@Operation(summary = "分页查询代码生成器信息列表", description = "分页查询代码生成器信息列表")
	public Result<Page<InfoCO>> pageV3(@Validated @RequestBody InfoPageQry qry) {
		return infosServiceI.page(qry);
	}

	@TraceLog
	@GetMapping("{id}")
	@Operation(summary = "查看代码生成器信息详情", description = "查看代码生成器信息详情")
	public Result<InfoCO> getByIdV3(@PathVariable("id") Long id) {
		return infosServiceI.getById(new InfoGetQry(id));
	}

}
