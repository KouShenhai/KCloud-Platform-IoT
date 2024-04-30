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

package org.laokou.common.mongodb.clientobject;

import lombok.Data;
import org.laokou.common.i18n.dto.Query;

import java.util.List;

/**
 * @author laokou
 */
@Data
public class SearchCO extends Query {

	/**
	 * 页码.
	 */
	private Integer pageNum = 1;

	/**
	 * 条数.
	 */
	private Integer pageSize = 10;

	/**
	 * 模糊条件查询.
	 */
	private List<Search> likeSearchList;

	/**
	 * 表名.
	 */
	private String collectionName;

	/**
	 * 是否分页.
	 */
	private boolean needPage = false;

	@Data
	public static class Search {

		private String field;

		private String value;

	}

}
