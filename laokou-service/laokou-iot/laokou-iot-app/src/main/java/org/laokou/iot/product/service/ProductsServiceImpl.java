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

package org.laokou.iot.product.service;

import lombok.RequiredArgsConstructor;
import org.laokou.common.i18n.dto.Page;
import org.laokou.common.i18n.dto.Result;
import org.laokou.iot.product.api.ProductsServiceI;
import org.laokou.iot.product.command.ProductExportCmdExe;
import org.laokou.iot.product.command.ProductImportCmdExe;
import org.laokou.iot.product.command.ProductModifyCmdExe;
import org.laokou.iot.product.command.ProductRemoveCmdExe;
import org.laokou.iot.product.command.ProductSaveCmdExe;
import org.laokou.iot.product.command.query.ProductGetQryExe;
import org.laokou.iot.product.command.query.ProductPageQryExe;
import org.laokou.iot.product.dto.ProductExportCmd;
import org.laokou.iot.product.dto.ProductGetQry;
import org.laokou.iot.product.dto.ProductImportCmd;
import org.laokou.iot.product.dto.ProductModifyCmd;
import org.laokou.iot.product.dto.ProductPageQry;
import org.laokou.iot.product.dto.ProductRemoveCmd;
import org.laokou.iot.product.dto.ProductSaveCmd;
import org.laokou.iot.product.dto.clientobject.ProductCO;
import org.springframework.stereotype.Service;

/**
 *
 * 产品接口实现类.
 *
 * @author laokou
 */
@Service
@RequiredArgsConstructor
public class ProductsServiceImpl implements ProductsServiceI {

	private final ProductSaveCmdExe productSaveCmdExe;

	private final ProductModifyCmdExe productModifyCmdExe;

	private final ProductRemoveCmdExe productRemoveCmdExe;

	private final ProductImportCmdExe productImportCmdExe;

	private final ProductExportCmdExe productExportCmdExe;

	private final ProductPageQryExe productPageQryExe;

	private final ProductGetQryExe productGetQryExe;

	@Override
	public void saveProduct(ProductSaveCmd cmd) {
		productSaveCmdExe.executeVoid(cmd);
	}

	@Override
	public void modifyProduct(ProductModifyCmd cmd) {
		productModifyCmdExe.executeVoid(cmd);
	}

	@Override
	public void removeProduct(ProductRemoveCmd cmd) {
		productRemoveCmdExe.executeVoid(cmd);
	}

	@Override
	public void importProduct(ProductImportCmd cmd) {
		productImportCmdExe.executeVoid(cmd);
	}

	@Override
	public void exportProduct(ProductExportCmd cmd) {
		productExportCmdExe.executeVoid(cmd);
	}

	@Override
	public Result<Page<ProductCO>> pageProduct(ProductPageQry qry) {
		return productPageQryExe.execute(qry);
	}

	@Override
	public Result<ProductCO> getProductById(ProductGetQry qry) {
		return productGetQryExe.execute(qry);
	}

}
