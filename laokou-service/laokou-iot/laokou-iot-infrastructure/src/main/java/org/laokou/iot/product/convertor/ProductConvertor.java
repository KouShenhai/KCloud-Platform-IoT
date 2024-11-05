/*
 * Copyright (c) 2022-2024 KCloud-Platform-IoT Author or Authors. All Rights Reserved.
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

package org.laokou.iot.product.convertor;

import org.laokou.iot.product.gatewayimpl.database.dataobject.ProductDO;
import org.laokou.iot.product.dto.clientobject.ProductCO;
import org.laokou.iot.product.model.ProductE;

/**
 *
 * 产品转换器.
 *
 * @author laokou
 */
public class ProductConvertor {

	public static ProductDO toDataObject(ProductE productE, boolean isInsert) {
		ProductDO productDO = new ProductDO();
		if (isInsert) {
			productDO.generatorId();
		}
		else {
			productDO.setId(productE.getId());
		}
		productDO.setName(productE.getName());
		productDO.setCategoryId(productE.getCategoryId());
		productDO.setDeviceType(productE.getDeviceType());
		productDO.setImgUrl(productE.getImgUrl());
		productDO.setCpId(productE.getCpId());
		productDO.setTpId(productE.getTpId());
		productDO.setRemark(productE.getRemark());
		return productDO;
	}

	public static ProductCO toClientObject(ProductDO productDO) {
		ProductCO productCO = new ProductCO();
		productCO.setName(productDO.getName());
		productCO.setCategoryId(productDO.getCategoryId());
		productCO.setDeviceType(productDO.getDeviceType());
		productCO.setImgUrl(productDO.getImgUrl());
		productCO.setCpId(productDO.getCpId());
		productCO.setTpId(productDO.getTpId());
		productCO.setRemark(productDO.getRemark());
		return productCO;
	}

	public static ProductE toEntity(ProductCO productCO) {
		ProductE productE = new ProductE();
		productE.setName(productCO.getName());
		productE.setCategoryId(productCO.getCategoryId());
		productE.setDeviceType(productCO.getDeviceType());
		productE.setImgUrl(productCO.getImgUrl());
		productE.setCpId(productCO.getCpId());
		productE.setTpId(productCO.getTpId());
		productE.setRemark(productCO.getRemark());
		return productE;
	}

}
