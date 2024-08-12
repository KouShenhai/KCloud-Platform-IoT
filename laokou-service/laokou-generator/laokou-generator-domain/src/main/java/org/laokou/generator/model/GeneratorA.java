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

import lombok.Getter;
import org.laokou.common.core.utils.IdGenerator;
import org.laokou.common.core.utils.JacksonUtil;
import org.laokou.common.i18n.dto.AggregateRoot;

import java.util.List;
import java.util.Map;

/**
 * @author laokou
 */
@Getter
public class GeneratorA extends AggregateRoot<Long> {

	private final String author;

	private final String packageName;

	private final String moduleName;

	private final String version;

	private TableE tableE;

	private TableV tableV;

	private List<Template> templates;

	public GeneratorA(String author, String packageName, String moduleName, String version) {
		this.id = IdGenerator.defaultSnowflakeId();
		this.author = author;
		this.packageName = packageName;
		this.moduleName = moduleName;
		this.version = version;
	}

	public void updateTable(TableV tableV) {
		this.tableV = tableV;
	}

	public void updateTable(TableE tableE) {
		this.tableE = tableE;
	}

	public void updateTemplates(List<Template> templates) {
		this.templates = templates;
	}

	public Map<String, Object> toMap() {
		Map<String, Object> map = JacksonUtil.toMap(tableV, String.class, Object.class);
		map.put("author", author);
		map.put("version", version);
		map.put("packageName", packageName);
		return map;
	}

}
