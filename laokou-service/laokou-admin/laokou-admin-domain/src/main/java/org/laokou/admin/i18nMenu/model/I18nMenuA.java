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

package org.laokou.admin.i18nMenu.model;

import lombok.Getter;
import org.laokou.admin.i18nMenu.model.entity.I18nMenuE;
import org.laokou.admin.i18nMenu.model.enums.OperateType;
import org.laokou.admin.i18nMenu.model.validator.I18nMenuParamValidator;
import org.laokou.common.i18n.annotation.Entity;
import org.laokou.common.i18n.common.IdGenerator;
import org.laokou.common.i18n.common.ValidateName;
import org.laokou.common.i18n.common.enums.BizType;
import org.laokou.common.i18n.dto.AggregateRoot;
import org.laokou.common.i18n.util.InstantUtils;
import org.laokou.common.i18n.util.ObjectUtils;
import org.springframework.beans.factory.annotation.Qualifier;

import java.time.Instant;

/**
 * 国际化菜单聚合根.
 * @author laokou
 */
@Entity
@Getter
public class I18nMenuA extends AggregateRoot implements ValidateName {

	private I18nMenuE i18nMenuE;

	/**
	 * 操作类型【保存/修改】.
	 */
	private OperateType operateType;

	private final IdGenerator commonIdGenerator;

	private final I18nMenuParamValidator saveI18nMenuParamValidator;

	private final I18nMenuParamValidator modifyI18nMenuParamValidator;

	public I18nMenuA(@Qualifier("commonIdGenerator") IdGenerator commonIdGenerator,
					 @Qualifier("saveI18nMenuParamValidator") I18nMenuParamValidator saveI18nMenuParamValidator,
					 @Qualifier("modifyI18nMenuParamValidator") I18nMenuParamValidator modifyI18nMenuParamValidator) {
		this.commonIdGenerator = commonIdGenerator;
		this.saveI18nMenuParamValidator = saveI18nMenuParamValidator;
		this.modifyI18nMenuParamValidator = modifyI18nMenuParamValidator;
	}

	public I18nMenuA create(I18nMenuE i18nMenuE) {
		this.i18nMenuE = i18nMenuE;
		Long primaryKey = this.i18nMenuE.getId();
		super.createTime = InstantUtils.now();
		super.id = ObjectUtils.isNotNull(primaryKey) ? primaryKey : commonIdGenerator.getId(BizType.I18N_MENU);
		this.operateType = ObjectUtils.isNotNull(primaryKey) ? OperateType.MODIFY : OperateType.SAVE;
		return this;
	}

	public void checkDeptParam() {
		switch (operateType) {
			case SAVE -> saveI18nMenuParamValidator.validateI18nMenu(this);
			case MODIFY -> modifyI18nMenuParamValidator.validateI18nMenu(this);
			default -> throw new UnsupportedOperationException("Unsupported operation type");
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
		return "I18nMenu";
	}

}
