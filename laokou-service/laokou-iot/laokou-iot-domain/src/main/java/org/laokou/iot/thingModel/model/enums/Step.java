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

package org.laokou.iot.thingModel.model.enums;

import lombok.Getter;
import org.laokou.common.i18n.util.EnumParser;

@Getter
public enum Step {

	_1("1", "1(整数)"),

	_0_1("0.1", "0.1(保留1位小数)"),

	_0_01("0.01", "0.01(保留2位小数)"),

	_0_001("0.001", "0.001(保留3位小数)"),

	_0_0001("0.0001", "0.0001(保留4位小数)"),

	_0_00001("0.00001", "0.00001(保留5位小数)"),

	_0_000001("0.000001", "0.000001(保留6位小数)");

	private final String code;

	private final String desc;

	Step(String code, String desc) {
		this.code = code;
		this.desc = desc;
	}

	public static Step getByCode(String code) {
		return EnumParser.parse(Step.class, Step::getCode, code);
	}

}
