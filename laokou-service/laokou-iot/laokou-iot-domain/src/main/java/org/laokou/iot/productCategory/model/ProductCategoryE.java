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

package org.laokou.iot.productCategory.model;

import lombok.Getter;
import lombok.Setter;
import org.laokou.common.i18n.annotation.Entity;
import org.laokou.common.i18n.dto.Identifier;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

/**
 *
 * 产品类别领域对象【实体】.
 *
 * @author laokou
 */
@Entity
public class ProductCategoryE extends Identifier {

	/**
	 * 产品类别名称.
	 */
	@Getter
	@Setter
	private String name;

	/**
	 * 产品类别排序.
	 */
	@Getter
	@Setter
	private Integer sort;

	/**
	 * 产品类别父节点ID.
	 */
	@Getter
	@Setter
	private Long pid;

	/**
	 * 产品类别备注.
	 */
	@Getter
	@Setter
	private String remark;

	@Setter
	private ProductCategoryOperateTypeEnum productCategoryOperateTypeEnum;

	@Autowired
	@Qualifier("saveProductCategoryParamValidator")
	private ProductCategoryParamValidator saveProductCategoryParamValidator;

	@Autowired
	@Qualifier("modifyProductCategoryParamValidator")
	private ProductCategoryParamValidator modifyProductCategoryParamValidator;

	public void checkProductCategoryParam() {
		switch (productCategoryOperateTypeEnum) {
			case SAVE -> saveProductCategoryParamValidator.validateProductCategory(this);
			case MODIFY -> modifyProductCategoryParamValidator.validateProductCategory(this);
			default -> {
			}
		}
	}

}
