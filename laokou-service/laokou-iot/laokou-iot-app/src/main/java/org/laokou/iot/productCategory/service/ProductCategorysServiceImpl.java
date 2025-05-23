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

package org.laokou.iot.productCategory.service;

import lombok.RequiredArgsConstructor;
import org.laokou.iot.productCategory.api.ProductCategorysServiceI;
import org.laokou.iot.productCategory.command.*;
import org.laokou.iot.productCategory.command.query.ProductCategoryGetQryExe;
import org.laokou.iot.productCategory.command.query.ProductCategoryPageQryExe;
import org.laokou.iot.productCategory.command.query.ProductCategoryTreeListQryExe;
import org.laokou.iot.productCategory.dto.*;
import org.laokou.iot.productCategory.dto.clientobject.ProductCategoryCO;
import org.laokou.common.i18n.dto.Page;
import org.laokou.common.i18n.dto.Result;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 *
 * 产品类别接口实现类.
 *
 * @author laokou
 */
@Service
@RequiredArgsConstructor
public class ProductCategorysServiceImpl implements ProductCategorysServiceI {

	private final ProductCategorySaveCmdExe productCategorySaveCmdExe;

	private final ProductCategoryModifyCmdExe productCategoryModifyCmdExe;

	private final ProductCategoryRemoveCmdExe productCategoryRemoveCmdExe;

	private final ProductCategoryImportCmdExe productCategoryImportCmdExe;

	private final ProductCategoryExportCmdExe productCategoryExportCmdExe;

	private final ProductCategoryPageQryExe productCategoryPageQryExe;

	private final ProductCategoryGetQryExe productCategoryGetQryExe;

	private final ProductCategoryTreeListQryExe productCategoryTreeListQryExe;

	@Override
	public void saveProductCategory(ProductCategorySaveCmd cmd) {
		productCategorySaveCmdExe.executeVoid(cmd);
	}

	@Override
	public void modifyProductCategory(ProductCategoryModifyCmd cmd) {
		productCategoryModifyCmdExe.executeVoid(cmd);
	}

	@Override
	public void removeProductCategory(ProductCategoryRemoveCmd cmd) {
		productCategoryRemoveCmdExe.executeVoid(cmd);
	}

	@Override
	public void importProductCategory(ProductCategoryImportCmd cmd) {
		productCategoryImportCmdExe.executeVoid(cmd);
	}

	@Override
	public void exportProductCategory(ProductCategoryExportCmd cmd) {
		productCategoryExportCmdExe.executeVoid(cmd);
	}

	@Override
	public Result<Page<ProductCategoryCO>> pageProductCategory(ProductCategoryPageQry qry) {
		return productCategoryPageQryExe.execute(qry);
	}

	@Override
	public Result<List<ProductCategoryCO>> listTreeProductCategory(ProductCategoryTreeListQry qry) {
		return productCategoryTreeListQryExe.execute(qry);
	}

	@Override
	public Result<ProductCategoryCO> getByIdProductCategory(ProductCategoryGetQry qry) {
		return productCategoryGetQryExe.execute(qry);
	}

}
