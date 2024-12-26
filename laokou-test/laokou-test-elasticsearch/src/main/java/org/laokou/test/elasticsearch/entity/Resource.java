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

package org.laokou.test.elasticsearch.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.laokou.common.elasticsearch.annotation.*;

import java.io.Serializable;

@Data
@Index(setting = @Setting(refreshInterval = "-1"), analysis = @Analysis(
		filters = { @Filter(name = "pinyin_filter",
				options = { @Option(key = "type", value = "pinyin"), @Option(key = "keep_full_pinyin", value = "false"),
						@Option(key = "keep_joined_full_pinyin", value = "true"),
						@Option(key = "keep_original", value = "true"),
						@Option(key = "limit_first_letter_length", value = "16"),
						@Option(key = "remove_duplicated_term", value = "true"),
						@Option(key = "none_chinese_pinyin_tokenize", value = "false") }) },
		analyzers = {
				@Analyzer(name = "ik_pinyin", args = @Args(filter = "pinyin_filter", tokenizer = "ik_max_word")) }))
@NoArgsConstructor
@AllArgsConstructor
public class Resource implements Serializable {

	@Field(type = Type.TEXT, searchAnalyzer = "ik_smart", analyzer = "ik_pinyin")
	private String name;

}
