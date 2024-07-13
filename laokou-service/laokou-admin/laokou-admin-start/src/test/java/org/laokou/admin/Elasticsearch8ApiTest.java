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

package org.laokou.admin;

import co.elastic.clients.elasticsearch.ElasticsearchAsyncClient;
import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch.indices.IndexState;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.laokou.common.core.utils.JacksonUtil;
import org.laokou.common.elasticsearch.annotation.*;
import org.laokou.common.elasticsearch.template.ElasticsearchTemplate;
import org.laokou.common.i18n.dto.Datas;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationService;
import org.springframework.test.context.TestConstructor;
import org.springframework.web.context.WebApplicationContext;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * @author laokou
 */
@Slf4j
@SpringBootTest
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
class Elasticsearch8ApiTest extends CommonTest {

	private final ElasticsearchClient elasticsearchClient;
	private final ElasticsearchTemplate elasticsearchTemplate;
	private final ElasticsearchAsyncClient elasticsearchAsyncClient;

	Elasticsearch8ApiTest(WebApplicationContext webApplicationContext,
						  OAuth2AuthorizationService oAuth2AuthorizationService, ElasticsearchClient elasticsearchClient,
						  ElasticsearchTemplate elasticsearchTemplate, ElasticsearchAsyncClient elasticsearchAsyncClient) {
		super(webApplicationContext, oAuth2AuthorizationService);
		this.elasticsearchClient = elasticsearchClient;
		this.elasticsearchTemplate = elasticsearchTemplate;
		this.elasticsearchAsyncClient = elasticsearchAsyncClient;
	}

	@BeforeEach
	void contextLoads() {
		assertNotNull(elasticsearchClient);
		assertNotNull(elasticsearchAsyncClient);
		assertNotNull(elasticsearchTemplate);
	}

	@Test
	void testCreateIndexApi() {
		elasticsearchTemplate.createIndex("laokou_res_1", "laokou_res", Resource.class);
		elasticsearchTemplate.createIndex("laokou_pro_1", "laokou_pro", Project.class);
		elasticsearchTemplate.asyncCreateIndex("laokou_resp_1", "laokou_resp", Resp.class);
	}

	@Test
	void testAsyncCreateDocumentApi() {
		elasticsearchTemplate.asyncCreateDocument("laokou_res_1", "444", new Resource("8888"));
	}

	@Test
	void testAsyncBulkCreateDocumentApi() {
		elasticsearchTemplate.asyncBulkCreateDocument("laokou_res_1", Map.of("555", new Resource("6666")));
	}

	@Test
	void testCreateDocumentApi() {
		elasticsearchTemplate.createDocument("laokou_res_1", "222", new Resource("3333"));
	}

	@Test
	void testBulkCreateDocumentApi() {
		elasticsearchTemplate.bulkCreateDocument("laokou_res_1", Map.of("333", new Resource("5555")));
	}

	@Test
	void testGetIndexApi() {
		try {
			Map<String, IndexState> result = elasticsearchTemplate
				.getIndex(List.of("laokou_res_1", "laokou_pro_1", "laokou_resp_1"));
			log.info("索引信息：{}", JacksonUtil.toJsonStr(result));
		} catch (Exception e) {
			log.error("{}", e.getMessage(), e);
		}
	}

	@Test
	void testHighlightSearch() {
		try {
			ResourceSearch resource = new ResourceSearch("任贤齐");
			Datas<Result> results = elasticsearchTemplate.search(List.of("laokou_res", "laokou_res_1"), 1, 1, resource,
				Result.class);
			log.info("{}", results);
		} catch (Exception e) {
			log.error("{}", e.getMessage(), e);
		}
	}

	@Test
	void testDeleteIndexApi() {
		elasticsearchTemplate.deleteIndex(List.of("laokou_res_1", "laokou_pro_1", "laokou_resp_1"));
	}

	@Data
	@Index(setting = @Setting(refreshInterval = "-1"),
		analysis = @Analysis(
			filters = {@Filter(name = "pinyin_filter",
				options = {@Option(key = "type", value = "pinyin"),
					@Option(key = "keep_full_pinyin", value = "false"),
					@Option(key = "keep_joined_full_pinyin", value = "true"),
					@Option(key = "keep_original", value = "true"),
					@Option(key = "limit_first_letter_length", value = "16"),
					@Option(key = "remove_duplicated_term", value = "true"),
					@Option(key = "none_chinese_pinyin_tokenize", value = "false")})},
			analyzers = {@Analyzer(name = "ik_pinyin",
				args = @Args(filter = "pinyin_filter", tokenizer = "ik_max_word"))}))
	@NoArgsConstructor
	@AllArgsConstructor
	static class Resource implements Serializable {

		@Field(type = Type.TEXT, searchAnalyzer = "ik_smart", analyzer = "ik_pinyin")
		private String name;

	}

	@Data
	@Index
	static class Project implements Serializable {

		@JsonSerialize(using = ToStringSerializer.class)
		@Field(type = Type.LONG)
		private Long businessKey;

	}

	@Data
	@Index
	static class Resp implements Serializable {

		@Field(type = Type.KEYWORD)
		private String key;

	}

	@Data
	@Highlight(fields = {@HighlightField(name = "name")})
	@AllArgsConstructor
	@NoArgsConstructor
	static class ResourceSearch implements Serializable {

		@SearchField(names = {"name"}, type = QueryType.QUERY_STRING, query = Query.SHOULD)
		private String name;

	}

	@Data
	static class Result implements Serializable {

		private String id;

		private String name;

	}

}
