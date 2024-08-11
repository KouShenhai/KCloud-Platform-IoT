/*
 * Copyright (c) 2022-2024 KCloud-Platform-IoT Author or Authors. All Rights Reserved.
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

package org.laokou.generator.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.laokou.common.core.utils.JacksonUtil;
import org.laokou.common.i18n.dto.AggregateRoot;

import java.util.Map;

/**
 * @author laokou
 */
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class GenerateCodeA extends AggregateRoot<Long> {

	private String author;

	private String packageName;

	private String moduleName;

	private TableV tableV;

	public Map<String, Object> toMap() {
		Map<String, Object> map = JacksonUtil.toMap(tableV, String.class, Object.class);
		map.put("author", author);
		map.put("packageName", packageName);
		return map;
	}

}
