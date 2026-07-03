/*
 * Copyright (c) 2022-2026 KCloud-Platform-IoT Author or Authors. All Rights Reserved.
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
import org.laokou.common.i18n.annotation.Entity;
import org.laokou.common.i18n.common.IdGenerator;
import org.laokou.common.i18n.common.ValidateName;
import org.laokou.common.i18n.dto.AggregateRoot;
import org.laokou.common.i18n.util.InstantUtils;
import org.laokou.common.i18n.util.ObjectUtils;
import org.laokou.iot.productCategory.model.entity.ProductCategoryE;
import org.laokou.iot.productCategory.model.validator.ProductCategoryParamValidator;
import org.laokou.iot.thingModel.model.enums.OperateType;
import org.springframework.beans.factory.annotation.Qualifier;

import java.time.Instant;

/**
 *
 * 产品类别领域对象【实体】.
 *
 * @author laokou
 */
@Entity
@Getter
public class ProductCategoryA extends AggregateRoot implements ValidateName {

	private ProductCategoryE productCategoryE;

	/**
	 * 操作类型【保存/修改】.
	 */
	private OperateType operateType;

	private final ProductCategoryParamValidator saveProductCategoryParamValidator;

	private final ProductCategoryParamValidator modifyProductCategoryParamValidator;

	private final IdGenerator idGenerator;

	public ProductCategoryA(IdGenerator idGenerator,
			@Qualifier("saveProductCategoryParamValidator") ProductCategoryParamValidator saveProductCategoryParamValidator,
			@Qualifier("modifyProductCategoryParamValidator") ProductCategoryParamValidator modifyProductCategoryParamValidator) {
		this.idGenerator = idGenerator;
		this.saveProductCategoryParamValidator = saveProductCategoryParamValidator;
		this.modifyProductCategoryParamValidator = modifyProductCategoryParamValidator;
	}

	public ProductCategoryA create(ProductCategoryE productCategoryE) {
		this.productCategoryE = productCategoryE;
		Long primaryKey = this.productCategoryE.getId();
		super.createTime = InstantUtils.now();
		super.id = ObjectUtils.isNotNull(primaryKey) ? primaryKey : idGenerator.getId();
		this.operateType = ObjectUtils.isNotNull(primaryKey) ? OperateType.MODIFY : OperateType.SAVE;
		return this;
	}

	public void checkProductCategoryParam() {
		switch (operateType) {
			case SAVE -> saveProductCategoryParamValidator.validateProductCategory(this);
			case MODIFY -> modifyProductCategoryParamValidator.validateProductCategory(this);
			default -> throw new UnsupportedOperationException("Unsupported operation");
		}
	}

	public boolean isModify() {
		return ObjectUtils.equals(OperateType.MODIFY, this.operateType);
	}

	public boolean isSave() {
		return ObjectUtils.equals(OperateType.SAVE, this.operateType);
	}

	public Instant getCreateTime() {
		return isSave() ? super.createTime : null;
	}

	@Override
	public String getValidateName() {
		return "ProductCategory";
	}

}
