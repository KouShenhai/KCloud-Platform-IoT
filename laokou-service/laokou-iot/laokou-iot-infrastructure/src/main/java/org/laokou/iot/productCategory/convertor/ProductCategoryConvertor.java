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

package org.laokou.iot.productCategory.convertor;

import org.laokou.common.core.util.IdGenerator;
import org.laokou.iot.productCategory.dto.clientobject.ProductCategoryCO;
import org.laokou.iot.productCategory.gatewayimpl.database.dataobject.ProductCategoryDO;
import org.laokou.iot.productCategory.model.ProductCategoryE;

import java.util.List;

/**
 *
 * 产品类别转换器.
 *
 * @author laokou
 */
public class ProductCategoryConvertor {

	public static ProductCategoryDO toDataObject(ProductCategoryE productCategoryE, boolean isInsert) {
		ProductCategoryDO productCategoryDO = new ProductCategoryDO();
		if (isInsert) {
			productCategoryDO.setId(IdGenerator.defaultSnowflakeId());
		}
		else {
			productCategoryDO.setId(productCategoryE.getId());
		}
		productCategoryDO.setName(productCategoryE.getName());
		productCategoryDO.setSort(productCategoryE.getSort());
		productCategoryDO.setPid(productCategoryE.getPid());
		productCategoryDO.setRemark(productCategoryE.getRemark());
		return productCategoryDO;
	}

	public static ProductCategoryCO toClientObject(ProductCategoryDO productCategoryDO) {
		ProductCategoryCO productCategoryCO = new ProductCategoryCO();
		productCategoryCO.setName(productCategoryDO.getName());
		productCategoryCO.setSort(productCategoryDO.getSort());
		productCategoryCO.setPid(productCategoryDO.getPid());
		productCategoryCO.setRemark(productCategoryDO.getRemark());
		productCategoryCO.setId(productCategoryDO.getId());
		productCategoryCO.setCreateTime(productCategoryDO.getCreateTime());
		return productCategoryCO;
	}

	public static ProductCategoryE toEntity(ProductCategoryCO productCategoryCO) {
		ProductCategoryE productCategoryE = new ProductCategoryE();
		productCategoryE.setName(productCategoryCO.getName());
		productCategoryE.setSort(productCategoryCO.getSort());
		productCategoryE.setPid(productCategoryCO.getPid());
		productCategoryE.setRemark(productCategoryCO.getRemark());
		productCategoryE.setId(productCategoryCO.getId());
		return productCategoryE;
	}

	public static ProductCategoryCO toClientObj(ProductCategoryDO productCategoryDO) {
		ProductCategoryCO co = new ProductCategoryCO();
		co.setId(productCategoryDO.getId());
		co.setName(productCategoryDO.getName());
		co.setPid(productCategoryDO.getPid());
		co.setSort(productCategoryDO.getSort());
		co.setCreateTime(productCategoryDO.getCreateTime());
		co.setRemark(productCategoryDO.getRemark());
		return co;

	}

	public static List<ProductCategoryCO> toClientObjs(List<ProductCategoryDO> list) {
		return list.stream().map(ProductCategoryConvertor::toClientObj).toList();
	}

}
