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

package org.laokou.iot.productCategory.service.extensionpoint.extension;
import lombok.RequiredArgsConstructor;
import org.laokou.common.i18n.util.ParamValidator;
import org.laokou.iot.productCategory.gatewayimpl.database.ProductCategoryMapper;
import org.laokou.iot.productCategory.model.ProductCategoryE;
import org.laokou.iot.productCategory.service.extensionpoint.ProductCategoryParamValidatorExtPt;
import org.springframework.stereotype.Component;

/**
 * @author laokou
 */
@Component("saveProductCategoryParamValidator")
@RequiredArgsConstructor
public class SaveProductCategoryModelParamValidator implements ProductCategoryParamValidatorExtPt {

	private final ProductCategoryMapper productCategoryMapper;

	@Override
	public void validate(ProductCategoryE productCategoryE) {
		ParamValidator.validate(
				ProductCategoryParamValidator.validateName(productCategoryE, true, productCategoryMapper),
				ProductCategoryParamValidator.validateSort(productCategoryE),
				ProductCategoryParamValidator.validateParentId(productCategoryE)
		);
	}

}
