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

package org.laokou.common.storage.model.enums;

import lombok.Getter;

/**
 * @author laokou
 */
@Getter
public enum IoTDBColumnType {

	BOOLEAN("boolean", "Boolean", "BOOLEAN", "RLE"), INT32("int", "32-bit integer", "INT32", "TS_2DIFF"),
	INT64("long", "64-bit integer", "INT64", "TS_2DIFF"), FLOAT("float", "Single-precision number", "FLOAT", "GORILLA"),
	DOUBLE("double", "Double-precision number", "DOUBLE", "GORILLA"), TEXT("text", "Text", "TEXT", "PLAIN"),
	STRING("string", "String", "STRING", "PLAIN"), BLOB("blob", "Binary value", "BLOB", "PLAIN"),
	DATE("date", "Date", "DATE", "TS_2DIFF"), TIMESTAMP("timestamp", "Timestamp", "TIMESTAMP", "TS_2DIFF");

	private final String code;

	private final String desc;

	private final String dataType;

	private final String encoding;

	IoTDBColumnType(String code, String desc, String dataType, String encoding) {
		this.code = code;
		this.desc = desc;
		this.dataType = dataType;
		this.encoding = encoding;
	}

	public static IoTDBColumnType fromCode(String code) {
		if (code == null || code.isBlank()) {
			throw new IllegalArgumentException("IoTDB measurement type is required");
		}
		return switch (code.trim().toUpperCase(java.util.Locale.ROOT)) {
			case "BOOL", "BOOLEAN" -> BOOLEAN;
			case "INT", "INTEGER", "INT32" -> INT32;
			case "LONG", "INT64" -> INT64;
			case "FLOAT" -> FLOAT;
			case "DOUBLE" -> DOUBLE;
			case "TEXT" -> TEXT;
			case "STRING", "ENUM" -> STRING;
			case "BLOB" -> BLOB;
			case "DATE" -> DATE;
			case "TIMESTAMP" -> TIMESTAMP;
			default -> throw new IllegalArgumentException("Unsupported IoTDB measurement type: " + code);
		};
	}

}
