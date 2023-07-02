/*
 * Copyright (c) 2022 KCloud-Platform-Alibaba Authors. All Rights Reserved.
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
package org.laokou.common.elasticsearch.qo;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.laokou.common.elasticsearch.dto.AggregationDTO;
import org.laokou.common.elasticsearch.dto.SearchDTO;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

/**
 * 搜索
 *
 * @author laokou
 */
@Data
public class SearchQo implements Serializable {

	@Serial
	private static final long serialVersionUID = 8362710467533113506L;

	/**
	 * 页码
	 */
	@NotNull(message = "显示页码不为空")
	private Integer pageNum;

	/**
	 * 条数
	 */
	@NotNull(message = "显示条数不为空")
	private Integer pageSize;

	/**
	 * 是否分页
	 */
	@NotNull(message = "分页标识不为空")
	private boolean needPage;

	/**
	 * 查询索引名称
	 */
	@NotNull(message = "索引名称不为空")
	private String[] indexNames;

	/**
	 * 分词搜索
	 */
	private List<SearchDTO> queryStringList;

	/**
	 * 排序
	 */
	private List<SearchDTO> sortFieldList;

	/**
	 * 高亮搜索字段
	 */
	private List<String> highlightFieldList;

	/**
	 * or搜索-精准匹配
	 */
	private List<SearchDTO> orSearchList;

	/**
	 * 聚合字段
	 */
	private AggregationDTO aggregationKey;

}
