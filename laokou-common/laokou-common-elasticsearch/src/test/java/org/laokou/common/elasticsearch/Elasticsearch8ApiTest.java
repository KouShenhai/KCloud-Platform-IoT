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

package org.laokou.common.elasticsearch;

import co.elastic.clients.elasticsearch.ElasticsearchAsyncClient;
import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch.indices.IndexState;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.laokou.common.elasticsearch.entity.Search;
import org.laokou.common.elasticsearch.template.ElasticsearchTemplate;
import org.laokou.common.i18n.dto.Page;
import org.laokou.common.i18n.util.JacksonUtils;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestConstructor;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;

/**
 * @author laokou
 */
@Slf4j
@SpringBootTest
@RequiredArgsConstructor
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
class Elasticsearch8ApiTest {

	private final ElasticsearchClient elasticsearchClient;

	private final ElasticsearchTemplate elasticsearchTemplate;

	private final ElasticsearchAsyncClient elasticsearchAsyncClient;

	@BeforeEach
	void contextLoads() {
		Assertions.assertNotNull(elasticsearchClient);
		Assertions.assertNotNull(elasticsearchAsyncClient);
		Assertions.assertNotNull(elasticsearchTemplate);
	}

	@Test
	void test() throws IOException {
		Assertions.assertDoesNotThrow(
				() -> elasticsearchTemplate.createIndex("laokou_res_1", "laokou_res", TestResource.class));
		Assertions.assertDoesNotThrow(
				() -> elasticsearchTemplate.createIndex("laokou_pro_1", "laokou_pro", TestProject.class));
		Assertions.assertDoesNotThrow(() -> elasticsearchTemplate.asyncCreateIndex("laokou_resp_1", "laokou_resp",
				TestResp.class, Executors.newSingleThreadExecutor()));
		Assertions.assertDoesNotThrow(
				() -> elasticsearchTemplate.asyncCreateDocument("laokou_res_1", "222", new TestResource("8888"))
					.join());
		Assertions.assertDoesNotThrow(
				() -> elasticsearchTemplate
					.asyncBulkCreateDocument("laokou_res_1", Map.of("555", new TestResource("6666")),
							Executors.newSingleThreadExecutor())
					.join());
		Assertions.assertDoesNotThrow(
				() -> elasticsearchTemplate.createDocument("laokou_res_1", "444", new TestResource("3333")));
		Assertions.assertDoesNotThrow(() -> elasticsearchTemplate.bulkCreateDocument("laokou_res_1",
				Map.of("333", new TestResource("5555"))));
		Search.Highlight highlight = new Search.Highlight();
		highlight.setFields(List.of(new Search.HighlightField("name", 0, 0)));
		Search search = new Search(highlight, 1, 10, null);
		Page<TestResult> results = elasticsearchTemplate.search(List.of("laokou_res", "laokou_res_1"), search,
				TestResult.class);
		log.info("{}", results);
		Assertions.assertNotNull(results);
		Assertions.assertTrue(results.getTotal() > 0);
		Map<String, IndexState> result = elasticsearchTemplate
			.getIndex(List.of("laokou_res_1", "laokou_pro_1", "laokou_resp_1"));
		log.info("索引信息：{}", JacksonUtils.toJsonStr(result));
		Assertions.assertNotNull(result);
		Assertions.assertFalse(result.isEmpty());
		Assertions.assertDoesNotThrow(
				() -> elasticsearchTemplate.deleteIndex(List.of("laokou_res_1", "laokou_pro_1", "laokou_resp_1")));
	}

}
