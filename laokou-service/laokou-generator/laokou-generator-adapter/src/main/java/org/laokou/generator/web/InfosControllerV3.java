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
import org.laokou.common.i18n.dto.Page;
import org.laokou.common.i18n.dto.Result;
import org.laokou.common.idempotent.annotation.Idempotent;
import org.laokou.common.log.annotation.OperateLog;
import org.laokou.common.trace.annotation.TraceLog;
import org.laokou.generator.info.api.InfosServiceI;
import org.laokou.generator.info.dto.InfoExportCmd;
import org.laokou.generator.info.dto.InfoGetQry;
import org.laokou.generator.info.dto.InfoImportCmd;
import org.laokou.generator.info.dto.InfoModifyCmd;
import org.laokou.generator.info.dto.InfoPageQry;
import org.laokou.generator.info.dto.InfoRemoveCmd;
import org.laokou.generator.info.dto.InfoSaveCmd;
import org.laokou.generator.info.dto.clientobject.InfoCO;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
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
	public void saveInfo(@RequestBody InfoSaveCmd cmd) {
		infosServiceI.saveInfo(cmd);
	}

	@PutMapping
	@PreAuthorize("hasAuthority('generator:info:modify')")
	@OperateLog(module = "代码生成器信息管理", operation = "修改代码生成器信息")
	@Operation(summary = "修改代码生成器信息", description = "修改代码生成器信息")
	public void modifyInfo(@RequestBody InfoModifyCmd cmd) {
		infosServiceI.modifyInfo(cmd);
	}

	@DeleteMapping
	@PreAuthorize("hasAuthority('generator:info:remove')")
	@OperateLog(module = "代码生成器信息管理", operation = "删除代码生成器信息")
	@Operation(summary = "删除代码生成器信息", description = "删除代码生成器信息")
	public void removeInfo(@RequestBody Long[] ids) {
		infosServiceI.removeInfo(new InfoRemoveCmd(ids));
	}

	@PostMapping(value = "import", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	@PreAuthorize("hasAuthority('generator:info:import')")
	@OperateLog(module = "代码生成器信息管理", operation = "导入代码生成器信息")
	@Operation(summary = "导入代码生成器信息", description = "导入代码生成器信息")
	public void importInfo(@RequestPart("files") MultipartFile[] files) {
		infosServiceI.importInfo(new InfoImportCmd(files));
	}

	@PostMapping("export")
	@PreAuthorize("hasAuthority('generator:info:export')")
	@OperateLog(module = "代码生成器信息管理", operation = "导出代码生成器信息")
	@Operation(summary = "导出代码生成器信息", description = "导出代码生成器信息")
	public void exportInfo(@RequestBody InfoExportCmd cmd) {
		infosServiceI.exportInfo(cmd);
	}

	@TraceLog
	@PostMapping("page")
	@PreAuthorize("hasAuthority('generator:info:page')")
	@Operation(summary = "分页查询代码生成器信息列表", description = "分页查询代码生成器信息列表")
	public Result<Page<InfoCO>> pageInfo(@Validated @RequestBody InfoPageQry qry) {
		return infosServiceI.pageInfo(qry);
	}

	@TraceLog
	@GetMapping("{id}")
	@Operation(summary = "查看代码生成器信息详情", description = "查看代码生成器信息详情")
	public Result<InfoCO> getInfoById(@PathVariable("id") Long id) {
		return infosServiceI.getInfoById(new InfoGetQry(id));
	}

}
