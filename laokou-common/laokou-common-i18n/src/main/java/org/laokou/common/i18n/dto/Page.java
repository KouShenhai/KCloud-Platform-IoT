/*
 * Copyright (c) 2022-2025 KCloud-Platform-IoT Author or Authors. All Rights Reserved.
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

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;

/**
 * 分页.
 *
 * @author laokou
 */
@Data
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public final class Page<T> implements Serializable {

	/**
	 * 总数.
	 */
	private long total;

	/**
	 * 数据项集合.
	 */
	private List<T> records;

	/**
	 * 构建空对象集合.
	 * @param <T> 泛型
	 * @return 空对象集合
	 */
	public static <T> Page<T> empty() {
		return new Page<>(0, Collections.emptyList());
	}

	public static <T> Page<T> create(List<T> list, long total) {
		return new Page<>(total, list);
	}

}
