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
import org.laokou.common.i18n.dto.Page;
import org.laokou.common.i18n.dto.Result;
import org.laokou.common.idempotent.annotation.Idempotent;
import org.laokou.common.log.annotation.OperateLog;
import org.laokou.common.trace.annotation.TraceLog;
import org.laokou.iot.product.api.ProductsServiceI;
import org.laokou.iot.product.dto.ProductExportCmd;
import org.laokou.iot.product.dto.ProductGetQry;
import org.laokou.iot.product.dto.ProductImportCmd;
import org.laokou.iot.product.dto.ProductModifyCmd;
import org.laokou.iot.product.dto.ProductPageQry;
import org.laokou.iot.product.dto.ProductRemoveCmd;
import org.laokou.iot.product.dto.ProductSaveCmd;
import org.laokou.iot.product.dto.clientobject.ProductCO;
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
 * 产品管理控制器.
 *
 * @author laokou
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("v3/products")
@Tag(name = "产品管理", description = "产品管理")
public class ProductsControllerV3 {

	private final ProductsServiceI productsServiceI;

	@Idempotent
	@PostMapping
	@PreAuthorize("hasAuthority('iot:product:save')")
	@OperateLog(module = "产品管理", operation = "保存产品")
	@Operation(summary = "保存产品", description = "保存产品")
	public void saveProduct(@RequestBody ProductSaveCmd cmd) {
		productsServiceI.saveProduct(cmd);
	}

	@PutMapping
	@PreAuthorize("hasAuthority('iot:product:modify')")
	@OperateLog(module = "产品管理", operation = "修改产品")
	@Operation(summary = "修改产品", description = "修改产品")
	public void modifyProduct(@RequestBody ProductModifyCmd cmd) {
		productsServiceI.modifyProduct(cmd);
	}

	@DeleteMapping
	@PreAuthorize("hasAuthority('iot:product:remove')")
	@OperateLog(module = "产品管理", operation = "删除产品")
	@Operation(summary = "删除产品", description = "删除产品")
	public void removeProduct(@RequestBody Long[] ids) {
		productsServiceI.removeProduct(new ProductRemoveCmd(ids));
	}

	@PostMapping(value = "import", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	@PreAuthorize("hasAuthority('iot:product:import')")
	@OperateLog(module = "产品管理", operation = "导入产品")
	@Operation(summary = "导入产品", description = "导入产品")
	public void importProduct(@RequestPart("files") MultipartFile[] files) {
		productsServiceI.importProduct(new ProductImportCmd(files));
	}

	@PostMapping("export")
	@PreAuthorize("hasAuthority('iot:product:export')")
	@OperateLog(module = "产品管理", operation = "导出产品")
	@Operation(summary = "导出产品", description = "导出产品")
	public void exportProduct(@RequestBody ProductExportCmd cmd) {
		productsServiceI.exportProduct(cmd);
	}

	@TraceLog
	@PostMapping("page")
	@PreAuthorize("hasAuthority('iot:product:page')")
	@Operation(summary = "分页查询产品列表", description = "分页查询产品列表")
	public Result<Page<ProductCO>> pageProduct(@Validated @RequestBody ProductPageQry qry) {
		return productsServiceI.pageProduct(qry);
	}

	@TraceLog
	@GetMapping("{id}")
	@PreAuthorize("hasAuthority('iot:product:detail')")
	@Operation(summary = "查看产品详情", description = "查看产品详情")
	public Result<ProductCO> getProductById(@PathVariable("id") Long id) {
		return productsServiceI.getProductById(new ProductGetQry(id));
	}

}
