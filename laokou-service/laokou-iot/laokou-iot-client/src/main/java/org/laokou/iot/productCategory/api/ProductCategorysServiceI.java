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

package org.laokou.iot.productCategory.api;

import org.laokou.iot.productCategory.dto.*;
import org.laokou.common.i18n.dto.Page;
import org.laokou.common.i18n.dto.Result;
import org.laokou.iot.productCategory.dto.clientobject.ProductCategoryCO;

import java.util.List;

/**
 *
 * 产品类别接口.
 *
 * @author laokou
 */
public interface ProductCategorysServiceI {

	/**
	 * 保存产品类别.
	 * @param cmd 保存命令
	 */
	void saveProductCategory(ProductCategorySaveCmd cmd);

	/**
	 * 修改产品类别.
	 * @param cmd 修改命令
	 */
	void modifyProductCategory(ProductCategoryModifyCmd cmd);

	/**
	 * 删除产品类别.
	 * @param cmd 删除命令
	 */
	void removeProductCategory(ProductCategoryRemoveCmd cmd);

	/**
	 * 导入产品类别.
	 * @param cmd 导入命令
	 */
	void importProductCategory(ProductCategoryImportCmd cmd);

	/**
	 * 导出产品类别.
	 * @param cmd 导出命令
	 */
	void exportProductCategory(ProductCategoryExportCmd cmd);

	/**
	 * 分页查询产品类别.
	 * @param qry 分页查询请求
	 */
	Result<Page<ProductCategoryCO>> pageProductCategory(ProductCategoryPageQry qry);

	/**
	 * 查询产品类别树列表.
	 * @param qry 查询请求
	 */
	Result<List<ProductCategoryCO>> listTreeProductCategory(ProductCategoryTreeListQry qry);

	/**
	 * 查看产品类别.
	 * @param qry 查看请求
	 */
	Result<ProductCategoryCO> getProductCategoryById(ProductCategoryGetQry qry);

}
