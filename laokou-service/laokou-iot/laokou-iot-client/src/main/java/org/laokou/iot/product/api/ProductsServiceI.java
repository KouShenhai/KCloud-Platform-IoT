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

package org.laokou.iot.product.api;

import org.laokou.iot.product.dto.*;
import org.laokou.common.i18n.dto.Page;
import org.laokou.common.i18n.dto.Result;
import org.laokou.iot.product.dto.clientobject.ProductCO;

/**
 *
 * 产品接口.
 *
 * @author laokou
 */
public interface ProductsServiceI {

	/**
	 * 保存产品.
	 * @param cmd 保存命令
	 */
	void save(ProductSaveCmd cmd);

	/**
	 * 修改产品.
	 * @param cmd 修改命令
	 */
	void modify(ProductModifyCmd cmd);

	/**
	 * 删除产品.
	 * @param cmd 删除命令
	 */
	void remove(ProductRemoveCmd cmd);

	/**
	 * 导入产品.
	 * @param cmd 导入命令
	 */
	void importI(ProductImportCmd cmd);

	/**
	 * 导出产品.
	 * @param cmd 导出命令
	 */
	void export(ProductExportCmd cmd);

	/**
	 * 分页查询产品.
	 * @param qry 分页查询请求
	 */
	Result<Page<ProductCO>> page(ProductPageQry qry);

	/**
	 * 查看产品.
	 * @param qry 查看请求
	 */
	Result<ProductCO> getById(ProductGetQry qry);

}
