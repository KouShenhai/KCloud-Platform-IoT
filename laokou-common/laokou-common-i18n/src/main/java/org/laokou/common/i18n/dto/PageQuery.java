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

import jakarta.validation.constraints.Min;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;

/**
 * 分页查询参数.
 * @author laokou
 */
@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class PageQuery extends Query {

	@Serial
	private static final long serialVersionUID = 6412915892334241813L;

	/**
	 * 分页参数.
	 */
	public static final String PAGE_QUERY = "pageQuery";

	/**
	 * 页码.
	 */
	@Min(1)
	private Integer pageNum = 1;

	/**
	 * 条数.
	 */
	@Min(1)
	private Integer pageSize = 10;

	/**
	 * 索引.
	 */
	private Integer pageIndex;

	private String sqlFilter;

	private String startTime;

	private String endTime;

	public void setPageIndex(Integer pageIndex) {
		this.pageIndex = (pageNum - 1) * pageSize;
	}

}
