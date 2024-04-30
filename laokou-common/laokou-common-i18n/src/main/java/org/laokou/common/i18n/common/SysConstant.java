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

import java.util.regex.Pattern;

/**
 * @author laokou
 */
@Schema(name = "SysConstants", description = "系统变量")
public final class SysConstant {

	private SysConstant() {
	}

	@Schema(name = "VERSION", description = "版本")
	public static final String VERSION = "3.2.5";

	@Schema(name = "GRACEFUL_SHUTDOWN_URL", description = "优雅停机URL")
	public static final String GRACEFUL_SHUTDOWN_URL = "/graceful-shutdown";

	@Schema(name = "UNDEFINED", description = "UNDEFINED")
	public static final String UNDEFINED = "undefined";

	@Schema(name = "COMMON_DATA_ID", description = "Nacos公共配置标识")
	public static final String COMMON_DATA_ID = "application-common.yaml";

	@Schema(name = "APPLICATION", description = "应用")
	public static final String APPLICATION = "application";

	@Schema(name = "ALL_PATTERNS", description = "拦截所有路径")
	public static final String ALL_PATTERNS = "/**";

	@Schema(name = "EXCEL_EXT", description = "Excel文件后缀")
	public static final String EXCEL_EXT = ".xlsx";

	@Schema(name = "LINE_PATTERN", description = "下划线正则表达式")
	public static final Pattern LINE_PATTERN = Pattern.compile("_(\\w)");

}
