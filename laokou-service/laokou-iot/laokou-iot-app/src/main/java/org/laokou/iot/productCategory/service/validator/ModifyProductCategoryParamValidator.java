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

package org.laokou.iot.productCategory.service.validator;

import lombok.RequiredArgsConstructor;
import org.laokou.common.i18n.util.ParamValidator;
import org.laokou.iot.productCategory.gatewayimpl.database.ProductCategoryMapper;
import org.laokou.iot.productCategory.model.ProductCategoryE;
import org.laokou.iot.productCategory.model.ProductCategoryParamValidator;
import org.springframework.stereotype.Component;

/**
 * @author laokou
 */
@Component("modifyProductCategoryParamValidator")
@RequiredArgsConstructor
public class ModifyProductCategoryParamValidator implements ProductCategoryParamValidator {

	private final ProductCategoryMapper productCategoryMapper;

	@Override
	public void validateProductCategory(ProductCategoryE productCategoryE) {
		ParamValidator.validate(
				org.laokou.iot.productCategory.service.validator.ProductCategoryParamValidator
					.validateId(productCategoryE),
				org.laokou.iot.productCategory.service.validator.ProductCategoryParamValidator
					.validateName(productCategoryE, false, productCategoryMapper),
				org.laokou.iot.productCategory.service.validator.ProductCategoryParamValidator
					.validateSort(productCategoryE),
				org.laokou.iot.productCategory.service.validator.ProductCategoryParamValidator
					.validateParentId(productCategoryE));
	}

}
