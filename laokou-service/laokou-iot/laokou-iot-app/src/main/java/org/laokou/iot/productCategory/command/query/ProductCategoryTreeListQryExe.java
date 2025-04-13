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

package org.laokou.iot.productCategory.command.query;

import com.baomidou.dynamic.datasource.toolkit.DynamicDataSourceContextHolder;
import lombok.RequiredArgsConstructor;
import org.laokou.common.core.util.TreeUtils;
import org.laokou.common.i18n.dto.Result;
import org.laokou.iot.productCategory.convertor.ProductCategoryConvertor;
import org.laokou.iot.productCategory.dto.ProductCategoryPageQry;
import org.laokou.iot.productCategory.dto.clientobject.ProductCategoryCO;
import org.laokou.iot.productCategory.gatewayimpl.database.ProductCategoryMapper;
import org.laokou.iot.productCategory.gatewayimpl.database.dataobject.ProductCategoryDO;
import org.springframework.stereotype.Component;

import java.util.List;

import static org.laokou.common.tenant.constant.DSConstants.IOT;

/**
 * @author laokou
 */
@Component
@RequiredArgsConstructor
public class ProductCategoryTreeListQryExe {

	private final ProductCategoryMapper productCategoryMapper;

	public Result<List<ProductCategoryCO>> execute(ProductCategoryPageQry qry) {
		try {
			DynamicDataSourceContextHolder.push(IOT);
			List<ProductCategoryDO> list = productCategoryMapper.selectObjectList(qry);
			ProductCategoryCO productCategory = TreeUtils.buildTreeNode(ProductCategoryConvertor.toClientObjs(list),
					ProductCategoryCO.class);
			return Result.ok(productCategory.getChildren());
		}
		finally {
			DynamicDataSourceContextHolder.clear();
		}
	}

}
