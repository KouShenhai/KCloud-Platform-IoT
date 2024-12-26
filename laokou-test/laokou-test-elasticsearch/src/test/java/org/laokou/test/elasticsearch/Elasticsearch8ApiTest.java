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

package org.laokou.test.elasticsearch;

import co.elastic.clients.elasticsearch.ElasticsearchAsyncClient;
import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch.indices.IndexState;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.laokou.common.core.annotation.EnableTaskExecutor;
import org.laokou.common.elasticsearch.entity.Search;
import org.laokou.common.elasticsearch.template.ElasticsearchTemplate;
import org.laokou.common.i18n.dto.Page;
import org.laokou.common.i18n.utils.JacksonUtil;
import org.laokou.test.elasticsearch.entity.Project;
import org.laokou.test.elasticsearch.entity.Resource;
import org.laokou.test.elasticsearch.entity.Resp;
import org.laokou.test.elasticsearch.entity.Result;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestConstructor;

import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;

/**
 * @author laokou
 */
@Slf4j
@SpringBootTest
@EnableTaskExecutor
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
	void testCreateIndexApi() {
		elasticsearchTemplate.createIndex("laokou_res_1", "laokou_res", Resource.class);
		elasticsearchTemplate.createIndex("laokou_pro_1", "laokou_pro", Project.class);
		elasticsearchTemplate.asyncCreateIndex("laokou_resp_1", "laokou_resp", Resp.class,
				Executors.newSingleThreadExecutor());
	}

	@Test
	void testAsyncCreateDocumentApi() {
		elasticsearchTemplate.asyncCreateDocument("laokou_res_1", "444", new Resource("8888"));
	}

	@Test
	void testAsyncBulkCreateDocumentApi() {
		elasticsearchTemplate.asyncBulkCreateDocument("laokou_res_1", Map.of("555", new Resource("6666")),
				Executors.newSingleThreadExecutor());
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
		}
		catch (Exception e) {
			log.error("{}", e.getMessage(), e);
		}
	}

	@Test
	void testHighlightSearch() {
		try {
			Search.Highlight highlight = new Search.Highlight();
			Search search = new Search(highlight, 1, 10, null);
			Page<Result> results = elasticsearchTemplate.search(List.of("laokou_res", "laokou_res_1"), search,
					Result.class);
			log.info("{}", results);
		}
		catch (Exception e) {
			log.error("{}", e.getMessage(), e);
		}
	}

	@Test
	void testDeleteIndexApi() {
		elasticsearchTemplate.deleteIndex(List.of("laokou_res_1", "laokou_pro_1", "laokou_resp_1"));
	}

}
