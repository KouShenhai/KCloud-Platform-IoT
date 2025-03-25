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

package org.laokou.iot.productModel.convertor;

import org.laokou.common.core.util.IdGenerator;
import org.laokou.iot.productModel.gatewayimpl.database.dataobject.ProductModelDO;
import org.laokou.iot.productModel.model.ProductModelE;

/**
 *
 * 产品模型转换器.
 *
 * @author laokou
 */
public class ProductModelConvertor {

	public static ProductModelDO toDataObject(ProductModelE productModelE, boolean isInsert) {
		ProductModelDO productModelDO = new ProductModelDO();
		if (isInsert) {
			productModelDO.setId(IdGenerator.defaultSnowflakeId());
		}
		else {
			productModelDO.setId(productModelE.getId());
		}
		productModelDO.setProductId(productModelE.getProductId());
		productModelDO.setModelId(productModelE.getModelId());
		return productModelDO;
	}

}
