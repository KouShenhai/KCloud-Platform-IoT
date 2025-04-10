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
import org.laokou.iot.productCategory.dto.clientobject.ProductCategoryCO;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.laokou.iot.productCategory.api.ProductCategorysServiceI;
import org.laokou.iot.productCategory.dto.*;
import org.laokou.common.i18n.dto.Page;
import org.laokou.common.idempotent.annotation.Idempotent;
import org.laokou.common.log.annotation.OperateLog;
import org.laokou.common.trace.annotation.TraceLog;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.multipart.MultipartFile;

/**
 *
 * 产品类别管理控制器.
 *
 * @author laokou
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("v3/product-categorys")
@Tag(name = "产品类别管理", description = "产品类别管理")
public class ProductCategorysControllerV3 {

	private final ProductCategorysServiceI productCategorysServiceI;

	@Idempotent
	@PostMapping
	@PreAuthorize("hasAuthority('iot:product-category:save')")
	@OperateLog(module = "产品类别管理", operation = "保存产品类别")
	@Operation(summary = "保存产品类别", description = "保存产品类别")
	public void saveV3(@RequestBody ProductCategorySaveCmd cmd) {
		productCategorysServiceI.save(cmd);
	}

	@PutMapping
	@PreAuthorize("hasAuthority('iot:product-category:modify')")
	@OperateLog(module = "产品类别管理", operation = "修改产品类别")
	@Operation(summary = "修改产品类别", description = "修改产品类别")
	public void modifyV3(@RequestBody ProductCategoryModifyCmd cmd) {
		productCategorysServiceI.modify(cmd);
	}

	@DeleteMapping
	@PreAuthorize("hasAuthority('iot:product-category:remove')")
	@OperateLog(module = "产品类别管理", operation = "删除产品类别")
	@Operation(summary = "删除产品类别", description = "删除产品类别")
	public void removeV3(@RequestBody Long[] ids) {
		productCategorysServiceI.remove(new ProductCategoryRemoveCmd(ids));
	}

	@PostMapping(value = "import", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	@PreAuthorize("hasAuthority('iot:product-category:import')")
	@OperateLog(module = "产品类别管理", operation = "导入产品类别")
	@Operation(summary = "导入产品类别", description = "导入产品类别")
	public void importV3(@RequestPart("files") MultipartFile[] files) {
		productCategorysServiceI.importI(new ProductCategoryImportCmd(files));
	}

	@PostMapping("export")
	@PreAuthorize("hasAuthority('iot:product-category:export')")
	@OperateLog(module = "产品类别管理", operation = "导出产品类别")
	@Operation(summary = "导出产品类别", description = "导出产品类别")
	public void exportV3(@RequestBody ProductCategoryExportCmd cmd) {
		productCategorysServiceI.export(cmd);
	}

	@TraceLog
	@PostMapping("page")
	@PreAuthorize("hasAuthority('iot:product-category:page')")
	@Operation(summary = "分页查询产品类别列表", description = "分页查询产品类别列表")
	public Result<Page<ProductCategoryCO>> pageV3(@Validated @RequestBody ProductCategoryPageQry qry) {
		return productCategorysServiceI.page(qry);
	}

	@TraceLog
	@GetMapping("{id}")
	@PreAuthorize("hasAuthority('iot:product-category:detail')")
	@Operation(summary = "查看产品类别详情", description = "查看产品类别详情")
	public Result<ProductCategoryCO> getByIdV3(@PathVariable("id") Long id) {
		return productCategorysServiceI.getById(new ProductCategoryGetQry(id));
	}

}
