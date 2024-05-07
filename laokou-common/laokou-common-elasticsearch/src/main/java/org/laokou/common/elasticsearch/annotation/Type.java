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

package org.laokou.common.elasticsearch.annotation;

import lombok.Getter;

/**
 * @author laokou
 */
@Getter
public enum Type {

	AUTO("auto"), TEXT("text"), KEYWORD("keyword"), LONG("long"), INTEGER("integer"), SHORT("short"), BYTE("byte"),
	DOUBLE("double"), FLOAT("float"), DATE("date"), BOOLEAN("boolean"), BINARY("binary"),
	INTEGER_RANGE("integer_range"), FLOAT_RANGE("float_range"), LONG_RANGE("long_range"), DOUBLE_RANGE("double_range"),
	DATE_RANGE("date_range"), OBJECT("object"), IP("ip");

	private final String value;

	Type(String value) {
		this.value = value;
	}

}
