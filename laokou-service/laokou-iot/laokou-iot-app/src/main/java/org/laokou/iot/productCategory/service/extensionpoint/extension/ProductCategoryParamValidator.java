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

package org.laokou.iot.productCategory.service.extensionpoint.extension;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import org.laokou.common.i18n.util.ObjectUtils;
import org.laokou.common.i18n.util.ParamValidator;
import org.laokou.iot.productCategory.gatewayimpl.database.ProductCategoryMapper;
import org.laokou.iot.productCategory.gatewayimpl.database.dataobject.ProductCategoryDO;
import org.laokou.iot.productCategory.model.ProductCategoryE;
import static org.laokou.common.i18n.util.ParamValidator.invalidate;
import static org.laokou.common.i18n.util.ParamValidator.validate;

/**
 * @author laokou
 */
public final class ProductCategoryParamValidator {

	private ProductCategoryParamValidator() {
	}

	public static ParamValidator.Validate validateId(ProductCategoryE productCategoryE) {
		Long id = productCategoryE.getId();
		if (ObjectUtils.isNull(id)) {
			return invalidate("产品类别ID不能为空");
		}
		return validate();
	}

	public static ParamValidator.Validate validateName(ProductCategoryE productCategoryE, boolean isSave, ProductCategoryMapper productCategoryMapper) {
		String name = productCategoryE.getName();
		if (ObjectUtils.isNull(name)) {
			return invalidate("产品类别名称不能为空");
		}
		if (isSave && productCategoryMapper.selectCount(Wrappers.lambdaQuery(ProductCategoryDO.class)
			.eq(ProductCategoryDO::getName, name)) > 0) {
			return invalidate("产品类别名称已存在");
		}
		if (!isSave && productCategoryMapper.selectCount(Wrappers.lambdaQuery(ProductCategoryDO.class)
			.eq(ProductCategoryDO::getName, name)
			.ne(ProductCategoryDO::getId, productCategoryE.getId())) > 0) {
			return invalidate("产品类别名称已存在");
		}
		return validate();
	}

	public static ParamValidator.Validate validateSort(ProductCategoryE productCategoryE) {
		Integer sort = productCategoryE.getSort();
		if (ObjectUtils.isNull(sort)) {
			return invalidate("产品类别排序不能为空");
		}
		if (sort < 1 || sort > 99999) {
			return invalidate("产品类别排序范围1-99999");
		}
		return validate();
	}

	public static ParamValidator.Validate validateParentId(ProductCategoryE productCategoryE) {
		Long pid = productCategoryE.getPid();
		if (ObjectUtils.isNull(pid)) {
			return invalidate("产品类别父级ID不能为空");
		}
		return validate();
	}

}
