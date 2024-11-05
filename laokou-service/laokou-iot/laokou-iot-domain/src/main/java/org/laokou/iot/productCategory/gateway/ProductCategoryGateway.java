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

package org.laokou.iot.productCategory.gateway;

import org.laokou.iot.productCategory.model.ProductCategoryE;

/**
 *
 * 产品类别网关【防腐】.
 *
 * @author laokou
 */
public interface ProductCategoryGateway {

	/**
	 * 新增产品类别.
	 */
	void create(ProductCategoryE productCategoryE);

	/**
	 * 修改产品类别.
	 */
	void update(ProductCategoryE productCategoryE);

	/**
	 * 删除产品类别.
	 */
	void delete(Long[] ids);

}
