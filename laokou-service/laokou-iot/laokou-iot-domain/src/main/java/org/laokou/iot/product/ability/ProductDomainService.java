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

package org.laokou.iot.product.ability;

import lombok.RequiredArgsConstructor;
import org.laokou.iot.product.gateway.*;
import org.laokou.iot.product.model.ProductE;
import org.springframework.stereotype.Component;

/**
 *
 * 产品领域服务.
 *
 * @author laokou
 */
@Component
@RequiredArgsConstructor
public class ProductDomainService {

	private final ProductGateway productGateway;

	public void create(ProductE productE) {
		productGateway.create(productE);
	}

	public void update(ProductE productE) {
		productGateway.update(productE);
	}

	public void delete(Long[] ids) {
		productGateway.delete(ids);
	}

}
