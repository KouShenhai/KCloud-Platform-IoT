/*
 * Copyright (c) 2022 KCloud-Platform-Alibaba Author or Authors. All Rights Reserved.
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
import lombok.Data;
import java.io.Serial;
import java.util.List;

/**
 * 搜索
 *
 * @author laokou
 */
@Data
public class SearchIndex extends DTO {

	@Serial
	private static final long serialVersionUID = 8362710467533113506L;

	@Min(value = 1)
	@Schema(name = "pageNum", description = "页码")
	private Integer pageNum = 1;

	@Schema(name = "pageSize", description = "条数")
	@Min(value = 1)
	private Integer pageSize = 10;

	@NotNull(message = "索引名称不能为空")
	@Schema(name = "indexNames", description = "索引名称")
	private String[] indexNames;

	/**
	 * 分词搜索
	 */
	private List<Search> queryStringList;

	/**
	 * 排序
	 */
	private List<Search> sortFieldList;

	/**
	 * 高亮搜索字段
	 */
	private List<String> highlightFieldList;

	/**
	 * or搜索-精准匹配
	 */
	private List<Search> orSearchList;

	/**
	 * 聚合字段
	 */
	private Aggregation aggregationKey;

	@Data
	public static class Aggregation {

		private String groupKey;

		private String field;

		private String script;

	}

	@Data
	public static class Search {

		private String field;

		private String value;

	}

}
