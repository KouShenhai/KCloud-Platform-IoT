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

package org.laokou.iot.productModel.ability;

import lombok.RequiredArgsConstructor;
import org.laokou.iot.productModel.gateway.*;
import org.laokou.iot.productModel.model.ProductModelE;
import org.springframework.stereotype.Component;

/**
 *
 * 产品模型领域服务.
 *
 * @author laokou
 */
@Component
@RequiredArgsConstructor
public class ProductModelDomainService {

	private final ProductModelGateway productModelGateway;

	public void create(ProductModelE productModelE) {
		productModelGateway.create(productModelE);
	}

	public void update(ProductModelE productModelE) {
		productModelGateway.update(productModelE);
	}

	public void delete(Long[] ids) {
		productModelGateway.delete(ids);
	}

}
