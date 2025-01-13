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

package org.laokou.iot.web;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.laokou.common.i18n.dto.Result;
import org.springframework.http.MediaType;
import org.laokou.iot.productModel.dto.clientobject.ProductModelCO;
import org.springframework.web.bind.annotation.*;
import org.laokou.iot.productModel.api.ProductModelsServiceI;
import org.laokou.iot.productModel.dto.*;
import org.laokou.common.i18n.dto.Page;
import org.laokou.common.idempotent.annotation.Idempotent;
import org.laokou.common.log.annotation.OperateLog;
import org.laokou.common.trace.annotation.TraceLog;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.multipart.MultipartFile;

/**
 *
 * 产品模型管理控制器.
 *
 * @author laokou
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("v3/product-models")
@Tag(name = "产品模型管理", description = "产品模型管理")
public class ProductModelsControllerV3 {

	private final ProductModelsServiceI productModelsServiceI;

	@Idempotent
	@PostMapping
	@PreAuthorize("hasAuthority('iot:product-model:save')")
	@OperateLog(module = "产品模型管理", operation = "保存产品模型")
	@Operation(summary = "保存产品模型", description = "保存产品模型")
	public void saveV3(@RequestBody ProductModelSaveCmd cmd) {
		productModelsServiceI.save(cmd);
	}

	@PutMapping
	@PreAuthorize("hasAuthority('iot:product-model:modify')")
	@OperateLog(module = "产品模型管理", operation = "修改产品模型")
	@Operation(summary = "修改产品模型", description = "修改产品模型")
	public void modifyV3(@RequestBody ProductModelModifyCmd cmd) {
		productModelsServiceI.modify(cmd);
	}

	@DeleteMapping
	@PreAuthorize("hasAuthority('iot:product-model:remove')")
	@OperateLog(module = "产品模型管理", operation = "删除产品模型")
	@Operation(summary = "删除产品模型", description = "删除产品模型")
	public void removeV3(@RequestBody Long[] ids) {
		productModelsServiceI.remove(new ProductModelRemoveCmd(ids));
	}

	@PostMapping(value = "import", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	@PreAuthorize("hasAuthority('iot:product-model:import')")
	@OperateLog(module = "产品模型管理", operation = "导入产品模型")
	@Operation(summary = "导入产品模型", description = "导入产品模型")
	public void importV3(@RequestPart("file") MultipartFile[] files) {
		productModelsServiceI.importI(new ProductModelImportCmd(files));
	}

	@PostMapping("export")
	@PreAuthorize("hasAuthority('iot:product-model:export')")
	@OperateLog(module = "产品模型管理", operation = "导出产品模型")
	@Operation(summary = "导出产品模型", description = "导出产品模型")
	public void exportV3(@RequestBody ProductModelExportCmd cmd) {
		productModelsServiceI.export(cmd);
	}

	@TraceLog
	@PostMapping("page")
	@PreAuthorize("hasAuthority('iot:product-model:page')")
	@Operation(summary = "分页查询产品模型列表", description = "分页查询产品模型列表")
	public Result<Page<ProductModelCO>> pageV3(@RequestBody ProductModelPageQry qry) {
		return productModelsServiceI.page(qry);
	}

	@TraceLog
	@GetMapping("{id}")
	@PreAuthorize("hasAuthority('sys:product-model:detail')")
	@Operation(summary = "查看产品模型详情", description = "查看产品模型详情")
	public Result<ProductModelCO> getByIdV3(@PathVariable("id") Long id) {
		return productModelsServiceI.getById(new ProductModelGetQry(id));
	}

}
