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

package org.laokou.common.i18n.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.util.List;

/**
 * @author laokou
 */
@Data
@Schema(name = "Search", description = "搜索")
public class Search extends DTO {

	@Serial
	private static final long serialVersionUID = 8362710467533113506L;

	@Min(1)
	@Schema(name = "pageNum", description = "页码")
	private Integer pageNum = 1;

	@Min(1)
	@Schema(name = "pageSize", description = "条数")
	private Integer pageSize = 10;

	@NotNull(message = "索引名称不能为空")
	@Schema(name = "indexNames", description = "索引名称")
	private String[] indexNames;

	@Schema(name = "queryStringList", description = "分词搜索集合")
	private List<Query> queryStringList;

	@Schema(name = "sortFieldList", description = "排序属性集合")
	private List<Query> sortFieldList;

	@Schema(name = "orQueryList", description = "or查询集合")
	private List<Query> orQueryList;

	@Schema(name = "aggregationKey", description = "聚合字段")
	private Aggregation aggregationKey;

	@Data
	@AllArgsConstructor
	@NoArgsConstructor
	@Schema(name = "Aggregation", description = "聚合")
	public static class Aggregation {

		@Schema(name = "groupKey", description = "分组Key")
		private String groupKey;

		@Schema(name = "field", description = "属性")
		private String field;

		@Schema(name = "script", description = "脚本")
		private String script;

	}

	@Data
	@AllArgsConstructor
	@NoArgsConstructor
	@Schema(name = "Query", description = "查询")
	public static class Query {

		@Schema(name = "field", description = "属性")
		private String field;

		@Schema(name = "value", description = "值")
		private String value;

	}

}
