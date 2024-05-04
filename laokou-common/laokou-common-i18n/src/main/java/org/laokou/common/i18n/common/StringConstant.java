/*
 * Copyright (c) 2022-2024 KCloud-Platform-IOT Author or Authors. All Rights Reserved.
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

package org.laokou.common.i18n.common;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * 字符串常量.
 * @author laokou
 */
public final class StringConstant {

	private StringConstant() {
	}

	@Schema(name = "EMPTY", description = "空字符串")
	public static final String EMPTY = "";

	@Schema(name = "AT", description = "@")
	public static final String AT = "@";

	@Schema(name = "TRUE", description = "TRUE")
	public static final String TRUE = "true";

	@Schema(name = "FALSE", description = "FALSE")
	public static final String FALSE = "false";

	@Schema(name = "NULL", description = "NULL")
	public static final String NULL = null;

	@Schema(name = "SLASH", description = "分割参数")
	public static final String SLASH = "/";

	@Schema(name = "ERECT", description = "分割参数")
	public static final String ERECT = "|";

	@Schema(name = "DROP", description = "分割参数")
	public static final String DROP = "、";

	@Schema(name = "BACKSLASH", description = "分割参数")
	public static final String BACKSLASH = "\\";

	@Schema(name = "STAR", description = "分割参数")
	public static final String STAR = "*";

	@Schema(name = "PERCENT", description = "百分号")
	public static final String PERCENT = "%";

	@Schema(name = "DOT", description = "分割参数")
	public static final String DOT = ".";

	@Schema(name = "RISK", description = "分割参数")
	public static final String RISK = ":";

	@Schema(name = "COMMA", description = "分割参数")
	public static final String COMMA = ",";

	@Schema(name = "CHINESE_COMMA", description = "分割参数")
	public static final String CHINESE_COMMA = "，";

	@Schema(name = "LEFT", description = "左括号")
	public static final String LEFT = "(";

	@Schema(name = "RIGHT", description = "右括号")
	public static final String RIGHT = ")";

	@Schema(name = "SPACE", description = "空格")
	public static final String SPACE = " ";

	@Schema(name = "EQUAL", description = "等于")
	public static final String EQUAL = "=";

	@Schema(name = "UNDER", description = "分割参数")
	public static final String UNDER = "_";

	@Schema(name = "UNDER", description = "分割参数")
	public static final Character CHAR_UNDER = '_';

	@Schema(name = "DOUBLE_QUOT", description = "双引号")
	public static final String DOUBLE_QUOT = "\"";

	@Schema(name = "SINGLE_QUOT", description = "单引号")
	public static final String SINGLE_QUOT = "'";

	@Schema(name = "AND", description = "并")
	public static final String AND = "&";

	@Schema(name = "MARK", description = "分割参数")
	public static final String MARK = "?";

	@Schema(name = "ROD", description = "分割参数")
	public static final String ROD = "-";

	@Schema(name = "DOLLAR", description = "分割参数")
	public static final String DOLLAR = "$";

	@Schema(name = "START_START", description = "分割参数")
	public static final String START_START = "**";

}
