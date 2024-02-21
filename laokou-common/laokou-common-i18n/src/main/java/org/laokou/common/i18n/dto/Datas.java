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

package org.laokou.common.i18n.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

/**
 * @author laokou
 */
@Data
@Builder
// TODO
//@AllArgsConstructor(access = PRIVATE)
//@NoArgsConstructor(access = PRIVATE)
@AllArgsConstructor
@NoArgsConstructor
@Schema(name = "Datas", description = "对象集合")
public class Datas<T> extends DTO {

	@Schema(name = "total", description = "总数")
	private long total;

	@Schema(name = "records", description = "对象集合")
	private List<T> records;

	/**
	 * 构建空对象集合.
	 * @param <T> 泛型
	 * @return 空对象集合
	 */
	public static <T> Datas<T> of() {
		return new Datas<>(0, new ArrayList<>(0));
	}

	public static <T> Datas<T> of(List<T> list, long total) {
		return new Datas<>(total, list);
	}

}
