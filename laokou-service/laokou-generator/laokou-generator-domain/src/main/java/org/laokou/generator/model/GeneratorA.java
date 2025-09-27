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

package org.laokou.generator.model;

import lombok.Getter;
import org.laokou.common.i18n.common.constant.StringConstants;
import org.laokou.common.i18n.dto.AggregateRoot;
import org.laokou.common.i18n.util.JacksonUtils;

import java.util.Map;

/**
 * @author laokou
 */
@Getter
public class GeneratorA extends AggregateRoot {

	private final String author;

	private final String packageName;

	private final String moduleName;

	// private final String version;

	private final TableE tableE;

	private final App app;

	private TableV tableV;

	public GeneratorA(String author, String packageName, String moduleName, String version, TableE tableE, App app) {
		this.author = author;
		this.packageName = packageName;
		this.moduleName = moduleName;
		// this.version = version;
		this.tableE = tableE;
		this.app = app;
	}

	public String getDomainPackagePath() {
		return getPackagePath() + StringConstants.SLASH + tableV.instanceName();
	}

	public String getPackagePath() {
		return packageName.replaceAll("\\.", StringConstants.SLASH);
	}

	public void updateTable(TableV tableV) {
		this.tableV = tableV;
	}

	public Map<String, Object> toMap() {
		Map<String, Object> map = JacksonUtils.toMap(tableV, String.class, Object.class);
		map.put("id", "#{id}");
		map.put("author", author);
		// map.put("version", version);
		map.put("pageQuery", "${pageQuery");
		map.put("packageName", packageName);
		map.put("app", app.name());
		return map;
	}

}
