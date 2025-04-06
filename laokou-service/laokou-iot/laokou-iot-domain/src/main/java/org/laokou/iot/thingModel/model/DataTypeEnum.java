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

import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.Getter;
import org.laokou.common.i18n.util.EnumParser;
import org.laokou.common.i18n.util.JacksonUtils;
import org.laokou.common.i18n.util.ParamValidator;

/**
 * @author laokou
 */
@Getter
public enum DataTypeEnum {

	INTEGER("integer", "整数型") {
		@Override
		public ParamValidator.Validate validate(String specs) throws JsonProcessingException {
			return JacksonUtils.toBean(specs, IntegerType.class).checkValue();
		}
	},
	DECIMAL("decimal", "小数型") {
		@Override
		public ParamValidator.Validate validate(String specs) {
			return null;
		}
	},
	STRING("string", "字符串型") {
		@Override
		public ParamValidator.Validate validate(String specs) throws JsonProcessingException {
			StringType stringType = JacksonUtils.toBean(specs, StringType.class);
			return ParamValidator.validate();
		}
	},
	BOOLEAN("boolean", "布尔型") {
		@Override
		public ParamValidator.Validate validate(String specs) {
			return null;
		}
	};

	private final String code;

	private final String desc;

	DataTypeEnum(String code, String desc) {
		this.code = code;
		this.desc = desc;
	}

	public abstract ParamValidator.Validate validate(String specs) throws JsonProcessingException;

	public static DataTypeEnum getByCode(String code) {
		return EnumParser.parse(DataTypeEnum.class, DataTypeEnum::getCode, code);
	}

}
