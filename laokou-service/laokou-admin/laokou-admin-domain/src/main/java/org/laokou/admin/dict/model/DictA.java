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

package org.laokou.admin.dict.model;

import lombok.Getter;
import org.laokou.admin.dict.model.entity.DictE;
import org.laokou.admin.dict.model.validator.DictParamValidator;
import org.laokou.admin.i18nMenu.model.enums.OperateType;
import org.laokou.common.i18n.annotation.Entity;
import org.laokou.common.i18n.common.IdGenerator;
import org.laokou.common.i18n.common.ValidateName;
import org.laokou.common.i18n.dto.AggregateRoot;
import org.laokou.common.i18n.util.InstantUtils;
import org.laokou.common.i18n.util.ObjectUtils;
import org.springframework.beans.factory.annotation.Qualifier;

import java.time.Instant;

/**
 * 字典聚合根.
 *
 * @author laokou
 */
@Entity
@Getter
public class DictA extends AggregateRoot implements ValidateName {

	private DictE dictE;

	/**
	 * 操作类型【保存/修改】.
	 */
	private OperateType operateType;

	private final IdGenerator idGenerator;

	private final DictParamValidator saveDictParamValidator;

	private final DictParamValidator modifyDictParamValidator;

	public DictA(IdGenerator idGenerator,
			@Qualifier("saveDictParamValidator") DictParamValidator saveDictParamValidator,
			@Qualifier("modifyDictParamValidator") DictParamValidator modifyDictParamValidator) {
		this.idGenerator = idGenerator;
		this.saveDictParamValidator = saveDictParamValidator;
		this.modifyDictParamValidator = modifyDictParamValidator;
	}

	public DictA create(DictE dictE) {
		this.dictE = dictE;
		Long primaryKey = this.dictE.getId();
		super.createTime = InstantUtils.now();
		super.id = ObjectUtils.isNotNull(primaryKey) ? primaryKey : idGenerator.getId();
		this.operateType = ObjectUtils.isNotNull(primaryKey) ? OperateType.MODIFY : OperateType.SAVE;
		return this;
	}

	public void checkDeptParam() {
		switch (operateType) {
			case SAVE -> saveDictParamValidator.validateDict(this);
			case MODIFY -> modifyDictParamValidator.validateDict(this);
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
		return "Dict";
	}

}
