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

package org.laokou.iot.thingModel.model;

import lombok.Getter;
import org.laokou.common.i18n.annotation.Entity;
import org.laokou.common.i18n.common.IdGenerator;
import org.laokou.common.i18n.common.ValidateName;
import org.laokou.common.i18n.dto.AggregateRoot;
import org.laokou.common.i18n.util.InstantUtils;
import org.laokou.common.i18n.util.ObjectUtils;
import org.laokou.iot.thingModel.model.entity.ThingModelE;
import org.laokou.iot.thingModel.model.enums.OperateType;
import org.laokou.iot.thingModel.model.validator.ThingModelParamValidator;
import org.springframework.beans.factory.annotation.Qualifier;

import java.time.Instant;

/**
 * 物模型聚合根.
 *
 * @author laokou
 */
@Entity
@Getter
public class ThingModelA extends AggregateRoot implements ValidateName {

	private ThingModelE thingModelE;

	/**
	 * 操作类型【保存/修改】.
	 */
	private OperateType operateType;

	private final IdGenerator idGenerator;

	private final ThingModelParamValidator saveThingModelParamValidator;

	private final ThingModelParamValidator modifyThingModelParamValidator;

	public ThingModelA(IdGenerator idGenerator,
			@Qualifier("saveThingModelParamValidator") ThingModelParamValidator saveThingModelParamValidator,
			@Qualifier("modifyThingModelParamValidator") ThingModelParamValidator modifyThingModelParamValidator) {
		this.idGenerator = idGenerator;
		this.saveThingModelParamValidator = saveThingModelParamValidator;
		this.modifyThingModelParamValidator = modifyThingModelParamValidator;
	}

	public ThingModelA create(ThingModelE thingModelE) {
		this.thingModelE = thingModelE;
		Long primaryKey = this.thingModelE.getId();
		super.createTime = InstantUtils.now();
		super.id = ObjectUtils.isNotNull(primaryKey) ? primaryKey : idGenerator.getId();
		this.operateType = ObjectUtils.isNotNull(primaryKey) ? OperateType.MODIFY : OperateType.SAVE;
		return this;
	}

	public void checkDeptParam() {
		switch (operateType) {
			case SAVE -> saveThingModelParamValidator.validateThingModel(this);
			case MODIFY -> modifyThingModelParamValidator.validateThingModel(this);
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
		return "ThingModel";
	}

}
