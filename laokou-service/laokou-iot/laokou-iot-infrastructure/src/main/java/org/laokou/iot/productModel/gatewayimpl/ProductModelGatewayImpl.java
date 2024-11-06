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

package org.laokou.iot.productModel.gatewayimpl;

import lombok.RequiredArgsConstructor;
import org.laokou.iot.productModel.model.ProductModelE;
import org.springframework.stereotype.Component;
import org.laokou.iot.productModel.gateway.ProductModelGateway;
import org.laokou.iot.productModel.gatewayimpl.database.ProductModelMapper;
import java.util.Arrays;
import org.laokou.iot.productModel.convertor.ProductModelConvertor;
import org.laokou.iot.productModel.gatewayimpl.database.dataobject.ProductModelDO;

/**
 *
 * 产品模型网关实现.
 *
 * @author laokou
 */
@Component
@RequiredArgsConstructor
public class ProductModelGatewayImpl implements ProductModelGateway {

	private final ProductModelMapper productModelMapper;

	@Override
	public void create(ProductModelE productModelE) {
		productModelMapper.insert(ProductModelConvertor.toDataObject(productModelE, true));
	}

	@Override
	public void update(ProductModelE productModelE) {
		ProductModelDO productModelDO = ProductModelConvertor.toDataObject(productModelE, false);
		productModelDO.setVersion(productModelMapper.selectVersion(productModelE.getId()));
		productModelMapper.updateById(productModelDO);
	}

	@Override
	public void delete(Long[] ids) {
		productModelMapper.deleteByIds(Arrays.asList(ids));
	}

}
