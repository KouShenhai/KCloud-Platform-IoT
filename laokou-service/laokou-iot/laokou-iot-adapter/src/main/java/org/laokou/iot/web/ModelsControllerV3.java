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

package org.laokou.iot.web;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.laokou.common.i18n.dto.Result;
import org.springframework.http.MediaType;
import org.laokou.iot.model.dto.clientobject.ModelCO;
import org.springframework.web.bind.annotation.*;
import org.laokou.iot.model.api.ModelsServiceI;
import org.laokou.iot.model.dto.*;
import org.laokou.common.i18n.dto.Page;
import org.laokou.common.idempotent.annotation.Idempotent;
import org.laokou.common.log.annotation.OperateLog;
import org.laokou.common.trace.annotation.TraceLog;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.multipart.MultipartFile;

/**
 *
 * 模型管理控制器.
 *
 * @author laokou
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("v3/models")
@Tag(name = "模型管理", description = "模型管理")
public class ModelsControllerV3 {

	private final ModelsServiceI modelsServiceI;

	@Idempotent
	@PostMapping
	@PreAuthorize("hasAuthority('iot:model:save')")
	@OperateLog(module = "模型管理", operation = "保存模型")
	@Operation(summary = "保存模型", description = "保存模型")
	public void saveV3(@RequestBody ModelSaveCmd cmd) {
		modelsServiceI.save(cmd);
	}

	@PutMapping
	@PreAuthorize("hasAuthority('iot:model:modify')")
	@OperateLog(module = "模型管理", operation = "修改模型")
	@Operation(summary = "修改模型", description = "修改模型")
	public void modifyV3(@RequestBody ModelModifyCmd cmd) {
		modelsServiceI.modify(cmd);
	}

	@DeleteMapping
	@PreAuthorize("hasAuthority('iot:model:remove')")
	@OperateLog(module = "模型管理", operation = "删除模型")
	@Operation(summary = "删除模型", description = "删除模型")
	public void removeV3(@RequestBody Long[] ids) {
		modelsServiceI.remove(new ModelRemoveCmd(ids));
	}

	@PostMapping(value = "import", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	@PreAuthorize("hasAuthority('iot:model:import')")
	@OperateLog(module = "模型管理", operation = "导入模型")
	@Operation(summary = "导入模型", description = "导入模型")
	public void importV3(@RequestPart("file") MultipartFile[] files) {
		modelsServiceI.importI(new ModelImportCmd(files));
	}

	@PostMapping("export")
	@PreAuthorize("hasAuthority('iot:model:export')")
	@OperateLog(module = "模型管理", operation = "导出模型")
	@Operation(summary = "导出模型", description = "导出模型")
	public void exportV3(@RequestBody ModelExportCmd cmd) {
		modelsServiceI.export(cmd);
	}

	@TraceLog
	@PostMapping("page")
	@PreAuthorize("hasAuthority('iot:model:page')")
	@Operation(summary = "分页查询模型列表", description = "分页查询模型列表")
	public Result<Page<ModelCO>> pageV3(@RequestBody ModelPageQry qry) {
		return modelsServiceI.page(qry);
	}

	@TraceLog
	@GetMapping("{id}")
	@Operation(summary = "查看模型详情", description = "查看模型详情")
	public Result<ModelCO> getByIdV3(@PathVariable("id") Long id) {
		return modelsServiceI.getById(new ModelGetQry(id));
	}

}
