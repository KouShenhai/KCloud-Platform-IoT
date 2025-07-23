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

package org.laokou.iot.thingModel.model;

import lombok.Getter;
import lombok.Setter;
import org.laokou.common.i18n.annotation.Entity;
import org.laokou.common.i18n.dto.IdGenerator;
import org.laokou.common.i18n.dto.Identifier;
import org.springframework.beans.factory.annotation.Qualifier;

/**
 *
 * 物模型领域对象【实体】.
 *
 * @author laokou
 */
@Entity
public class ThingModelE extends Identifier {

	/**
	 * 物模型名称.
	 */
	@Getter
	@Setter
	private String name;

	/**
	 * 物模型编码.
	 */
	@Getter
	@Setter
	private String code;

	/**
	 * 数据类型 integer string decimal boolean.
	 */
	@Getter
	@Setter
	private String dataType;

	/**
	 * 物模型类别 1属性 2事件.
	 */
	@Getter
	@Setter
	private Integer category;

	/**
	 * 物模型类型 read读 write写 report上报.
	 */
	@Getter
	@Setter
	private String type;

	/**
	 * 物模型排序.
	 */
	@Getter
	@Setter
	private Integer sort;

	/**
	 * 物模型规则说明.
	 */
	@Getter
	@Setter
	private String specs;

	/**
	 * 物模型备注.
	 */
	@Getter
	@Setter
	private String remark;

	@Setter
	@Getter
	private ThingModelOperateTypeEnum thingModelOperateTypeEnum;

	private final ThingModelParamValidator saveThingModelParamValidator;

	private final ThingModelParamValidator modifyThingModelParamValidator;

	private final IdGenerator idGenerator;

	public ThingModelE(@Qualifier("saveThingModelParamValidator") ThingModelParamValidator saveThingModelParamValidator,
			@Qualifier("modifyThingModelParamValidator") ThingModelParamValidator modifyThingModelParamValidator,
			IdGenerator idGenerator) {
		this.saveThingModelParamValidator = saveThingModelParamValidator;
		this.modifyThingModelParamValidator = modifyThingModelParamValidator;
		this.idGenerator = idGenerator;
	}

	public Long getPrimaryKey() {
		return idGenerator.getId();
	}

	public void checkThingModelParam() throws Exception {
		switch (thingModelOperateTypeEnum) {
			case SAVE -> saveThingModelParamValidator.validateThingModel(this);
			case MODIFY -> modifyThingModelParamValidator.validateThingModel(this);
			default -> {
			}
		}
	}

}
