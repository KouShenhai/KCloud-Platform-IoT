/*
 * Copyright (c) 2022-2026 KCloud-Platform-IoT Author or Authors. All Rights Reserved.
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

package org.laokou.common.elasticsearch.annotation;

import java.util.List;

/**
 * @author laokou
 */
public record Document(String name, String alias, List<Mapping> mappings,
		org.laokou.common.elasticsearch.annotation.Document.Setting setting,
		org.laokou.common.elasticsearch.annotation.Document.Analysis analysis) {

	public record Mapping(String field, Type type, String searchAnalyzer, String analyzer, boolean eagerGlobalOrdinals,
			String format, boolean index, SubField subField) {

	}

	public record SubField(Integer ignoreAbove) {

	}

	public record Setting(short shards, short replicas, String refreshInterval) {

	}

	public record Analysis(List<Filter> filters, List<Analyzer> analyzers) {

	}

	public record Filter(String name, List<Option> options) {

	}

	public record Args(String filter, String tokenizer) {

	}

	public record Option(String key, String value) {

	}

	public record Analyzer(String name, Args args) {

	}

}
