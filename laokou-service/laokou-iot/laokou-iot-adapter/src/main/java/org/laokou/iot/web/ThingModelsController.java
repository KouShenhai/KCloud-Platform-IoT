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

package org.laokou.iot.web;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.laokou.common.i18n.dto.Page;
import org.laokou.common.i18n.dto.Result;
import org.laokou.common.idempotent.annotation.Idempotent;
import org.laokou.common.log.annotation.OperateLog;
import org.laokou.common.trace.annotation.TraceLog;
import org.laokou.iot.thingModel.api.ThingModelsServiceI;
import org.laokou.iot.thingModel.dto.ThingModelGetQry;
import org.laokou.iot.thingModel.dto.ThingModelImportCmd;
import org.laokou.iot.thingModel.dto.ThingModelModifyCmd;
import org.laokou.iot.thingModel.dto.ThingModelPageQry;
import org.laokou.iot.thingModel.dto.ThingModelRemoveCmd;
import org.laokou.iot.thingModel.dto.ThingModelSaveCmd;
import org.laokou.iot.thingModel.dto.ThingThingModelExportCmd;
import org.laokou.iot.thingModel.dto.clientobject.ThingModelCO;
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
 * 物模型管理控制器.
 *
 * @author laokou
 */
@RestController
@RequiredArgsConstructor
@Tag(name = "物模型管理", description = "物模型管理")
public class ThingModelsController {

	private final ThingModelsServiceI thingModelsServiceI;

	@Idempotent
	@PostMapping("/v1/thing-models")
	@PreAuthorize("hasAuthority('iot:thing-model:save')")
	@OperateLog(module = "物模型管理", operation = "保存物模型")
	@Operation(summary = "保存物模型", description = "保存物模型")
	public void saveThingModel(@RequestBody ThingModelSaveCmd cmd) throws Exception {
		thingModelsServiceI.saveThingModel(cmd);
	}

	@PutMapping("/v1/thing-models")
	@PreAuthorize("hasAuthority('iot:thing-model:modify')")
	@OperateLog(module = "物模型管理", operation = "修改物模型")
	@Operation(summary = "修改物模型", description = "修改物模型")
	public void modifyThingModel(@RequestBody ThingModelModifyCmd cmd) throws Exception {
		thingModelsServiceI.modifyThingModel(cmd);
	}

	@DeleteMapping("/v1/thing-models")
	@PreAuthorize("hasAuthority('iot:thing-model:remove')")
	@OperateLog(module = "物模型管理", operation = "删除物模型")
	@Operation(summary = "删除物模型", description = "删除物模型")
	public void removeThingModel(@RequestBody Long[] ids) {
		thingModelsServiceI.removeThingModel(new ThingModelRemoveCmd(ids));
	}

	@PostMapping(value = "/v1/thing-models/import", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	@PreAuthorize("hasAuthority('iot:thing-model:import')")
	@OperateLog(module = "物模型管理", operation = "导入物模型")
	@Operation(summary = "导入物模型", description = "导入物模型")
	public void importThingModel(@RequestPart("files") MultipartFile[] files) {
		thingModelsServiceI.importThingModel(new ThingModelImportCmd(files));
	}

	@PostMapping("/v1/thing-models/export")
	@PreAuthorize("hasAuthority('iot:thing-model:export')")
	@OperateLog(module = "物模型管理", operation = "导出物模型")
	@Operation(summary = "导出物模型", description = "导出物模型")
	public void exportThingModel(@RequestBody ThingThingModelExportCmd cmd) {
		thingModelsServiceI.exportThingModel(cmd);
	}

	@TraceLog
	@PostMapping("/v1/thing-models/page")
	@PreAuthorize("hasAuthority('iot:thing-model:page')")
	@Operation(summary = "分页查询物模型列表", description = "分页查询物模型列表")
	public Result<Page<ThingModelCO>> pageThingModel(@Validated @RequestBody ThingModelPageQry qry) {
		return thingModelsServiceI.pageThingModel(qry);
	}

	@TraceLog
	@GetMapping("/v1/thing-models/{id}")
	@PreAuthorize("hasAuthority('iot:thing-model:detail')")
	@Operation(summary = "查看物模型详情", description = "查看物模型详情")
	public Result<ThingModelCO> getThingModelById(@PathVariable("id") Long id) {
		return thingModelsServiceI.getThingModelById(new ThingModelGetQry(id));
	}

}
