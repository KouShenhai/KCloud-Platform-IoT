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

package org.laokou.iot.productCategory.convertor;

import org.laokou.iot.productCategory.dto.clientobject.ProductCategoryCO;
import org.laokou.iot.productCategory.factory.ProductCategoryDomainFactory;
import org.laokou.iot.productCategory.gatewayimpl.database.dataobject.ProductCategoryDO;
import org.laokou.iot.productCategory.model.ProductCategoryA;
import org.laokou.iot.productCategory.model.entity.ProductCategoryE;

import java.util.List;

/**
 *
 * 产品类别转换器.
 *
 * @author laokou
 */
public final class ProductCategoryConvertor {

	private ProductCategoryConvertor() {
	}

	public static ProductCategoryDO toDataObject(ProductCategoryA productCategoryA) {
		ProductCategoryDO productCategoryDO = new ProductCategoryDO();
		ProductCategoryE productCategoryE = productCategoryA.getProductCategoryE();
		productCategoryDO.setId(productCategoryA.getId());
		productCategoryDO.setName(productCategoryE.getName());
		productCategoryDO.setSort(productCategoryE.getSort());
		productCategoryDO.setPid(productCategoryE.getPid());
		productCategoryDO.setRemark(productCategoryE.getRemark());
		return productCategoryDO;
	}

	public static List<ProductCategoryCO> toClientObjects(List<ProductCategoryDO> list) {
		return list.stream().map(ProductCategoryConvertor::toClientObject).toList();
	}

	public static ProductCategoryCO toClientObject(ProductCategoryDO productCategoryDO) {
		ProductCategoryCO productCategoryCO = new ProductCategoryCO();
		productCategoryCO.setId(productCategoryDO.getId());
		productCategoryCO.setName(productCategoryDO.getName());
		productCategoryCO.setSort(productCategoryDO.getSort());
		productCategoryCO.setRemark(productCategoryDO.getRemark());
		productCategoryCO.setPid(productCategoryDO.getPid());
		productCategoryCO.setCreateTime(productCategoryDO.getCreateTime());
		return productCategoryCO;
	}

	public static ProductCategoryE toEntity(ProductCategoryCO productCategoryCO) {
		return ProductCategoryDomainFactory.createProductCategoryE()
			.toBuilder()
			.id(productCategoryCO.getId())
			.name(productCategoryCO.getName())
			.sort(productCategoryCO.getSort())
			.pid(productCategoryCO.getPid())
			.remark(productCategoryCO.getRemark())
			.build();
	}

}
