/*
 * Copyright (c) 2022-2024 KCloud-Platform-Alibaba Author or Authors. All Rights Reserved.
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
 * @author laokou
 */
@Schema(name = "ElasticsearchFieldTypeEnums", description = "Elasticsearch属性类型枚举")
public enum ElasticsearchFieldTypeEnums {

	@Schema(name = "TEXT", description = "文本")
	TEXT,

	@Schema(name = "KEYWORD", description = "文本")
	KEYWORD,

	@Schema(name = "LONG", description = "长整型")
	LONG,

	@Schema(name = "INTEGER", description = "整型")
	INTEGER,

	@Schema(name = "SHORT", description = "整形")
	SHORT,

	@Schema(name = "BYTE", description = "字节")
	BYTE,

	@Schema(name = "DOUBLE", description = "双浮点数")
	DOUBLE,

	@Schema(name = "FLOAT", description = "单浮点数")
	FLOAT,

	@Schema(name = "DATE", description = "时间")
	DATE,

	@Schema(name = "BOOLEAN", description = "布尔")
	BOOLEAN

}
