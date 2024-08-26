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

package org.laokou.generator.model;

import lombok.Getter;

/**
 * @author laokou
 */
@Getter
public enum DataType {

	// @formatter:off
	TINYINT("Integer"),
	SMALLINT("Integer"),
	MEDIUMINT("Integer"),
	INT("Integer"),
	INTEGER("Integer"),
	BIGINT("Long"),
	FLOAT("Float"),
	DOUBLE("Double"),
	DECIMAL("BigDecimal"),
	BIT("Boolean"),
	CHAR("String"),
	VARCHAR("String"),
	TINYTEXT("String"),
	TEXT("String"),
	MEDIUMTEXT("String"),
	LONGTEXT("String"),
	DATE("Instant"),
	DATETIME("Instant"),
	NUMBER("Integer"),
	BINARY_INTEGER("Integer"),
	LONG("String"),
	BINARY_FLOAT("Float"),
	BINARY_DOUBLE("Double"),
	VARCHAR2("String"),
	NVARCHAR("String"),
	NVARCHAR2("String"),
	CLOB("String"),
	BLOB("String"),
	TIMESTAMP("Instant"),
	INT8("Long"),
	INT4("Integer"),
	INT2("Integer"),
	JSON("String"),
	NUMERIC("BigDecimal");
	// @formatter:on

	private final String value;

	DataType(String value) {
		this.value = value;
	}

}
