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

import co.elastic.clients.elasticsearch.indices.IndexState;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.RequiredArgsConstructor;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.laokou.common.core.util.ThreadUtils;
import org.laokou.common.elasticsearch.annotation.Analysis;
import org.laokou.common.elasticsearch.annotation.Analyzer;
import org.laokou.common.elasticsearch.annotation.Args;
import org.laokou.common.elasticsearch.annotation.Field;
import org.laokou.common.elasticsearch.annotation.Filter;
import org.laokou.common.elasticsearch.annotation.Index;
import org.laokou.common.elasticsearch.annotation.Option;
import org.laokou.common.elasticsearch.annotation.Setting;
import org.laokou.common.elasticsearch.annotation.Type;
import org.laokou.common.elasticsearch.config.ElasticsearchAutoConfig;
import org.laokou.common.elasticsearch.template.ElasticsearchDocumentTemplate;
import org.laokou.common.elasticsearch.template.ElasticsearchIndexTemplate;
import org.laokou.common.elasticsearch.template.ElasticsearchSearchTemplate;
import org.laokou.common.testcontainers.util.DockerImageNames;
import org.springframework.boot.ssl.DefaultSslBundleRegistry;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.TestConstructor;
import org.testcontainers.elasticsearch.ElasticsearchContainer;

import java.io.IOException;
import java.io.Serializable;
import java.time.Duration;
import java.util.List;
import java.util.Map;

/**
 * @author laokou
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@RequiredArgsConstructor
@TestConfiguration
@ContextConfiguration(classes = { ElasticsearchAutoConfig.class, DefaultSslBundleRegistry.class })
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
class ElasticsearchApiTest {

	private final ElasticsearchIndexTemplate elasticsearchIndexTemplate;

	private final ElasticsearchSearchTemplate elasticsearchSearchTemplate;

	private final ElasticsearchDocumentTemplate elasticsearchDocumentTemplate;

	static final ElasticsearchContainer elasticsearch = new ElasticsearchContainer(DockerImageNames.elasticsearch())
		.withPassword("laokou123");

	@BeforeAll
	static void beforeAll() throws InterruptedException {
		elasticsearch.start();
		Thread.sleep(Duration.ofSeconds(10L));
	}

	@AfterAll
	static void afterAll() {
		elasticsearch.stop();
	}

	@DynamicPropertySource
	static void configureProperties(DynamicPropertyRegistry registry) {
		registry.add("spring.data.elasticsearch.uris", () -> "https://" + elasticsearch.getHttpHostAddress());
		registry.add("spring.data.elasticsearch.username", () -> "elastic");
		registry.add("spring.data.elasticsearch.password", () -> "laokou123");
		registry.add("spring.data.elasticsearch.client-version", () -> "9.1.4");
		registry.add("spring.data.elasticsearch.version", () -> "9.1.3");
		registry.add("spring.data.elasticsearch.connection-timeout", () -> "60s");
		registry.add("spring.data.elasticsearch.socket-timeout", () -> "60s");
	}

	@Test
	void test_asyncApi() throws InterruptedException {
		// 异步创建索引
		Assertions.assertThatNoException().isThrownBy(() -> elasticsearchIndexTemplate.asyncCreateIndex("iot_async_plugin_idx_1", "iot_async_plugin_idx", Test1.class, ThreadUtils.newVirtualTaskExecutor()).join());
		Thread.sleep(Duration.ofSeconds(1));
		// 异步查看索引
		Map<String, IndexState> map = elasticsearchIndexTemplate.asyncGetIndex(List.of("iot_async_plugin_idx_1")).join().indices();
		Assertions.assertThat(map).hasSize(1);
		Assertions.assertThat(map.get("iot_async_plugin_idx_1")).isNotNull();
		// 异步创建文档
		Assertions.assertThatNoException().isThrownBy(() -> elasticsearchDocumentTemplate.asyncCreateDocument("iot_async_plugin_idx_1", "1", new Test1(1L, "laokou"), ThreadUtils.newVirtualTaskExecutor()).join());
		// 异步搜索文档【分词匹配】
//		Query.Builder queryBuilder = new Query.Builder();
//		TermQuery.Builder termQueryBuilder = new TermQuery.Builder();
//		termQueryBuilder.field("name").value("laokou");
//		queryBuilder.term(termQueryBuilder.build());
//		Search search = new Search(null, 1, 10, queryBuilder.build());
//		Page<Test1> page = elasticsearchSearchTemplate.asyncSearch(List.of("iot_async_plugin_idx_1"), search, Test1.class).join();
//		Assertions.assertThat(page.getTotal()).isEqualTo(1L);
//		Test1 test1 = page.getRecords().getFirst();
//		Assertions.assertThat(test1.name()).isEqualTo("laokou");
		// 异步查看文档
		Test1 test2 = elasticsearchDocumentTemplate.asyncGetDocument("iot_async_plugin_idx_1", "1", Test1.class, ThreadUtils.newVirtualTaskExecutor()).join();
		Assertions.assertThat(test2).isNotNull();
		Assertions.assertThat(test2.name()).isEqualTo("laokou");
		// 异步删除文档
		Assertions.assertThatNoException().isThrownBy(() -> elasticsearchDocumentTemplate.asyncDeleteDocument("iot_async_plugin_idx_1", test2.id().toString(), ThreadUtils.newVirtualTaskExecutor()).join());
		// 异步查看文档
		Test1 test3 = elasticsearchDocumentTemplate.asyncGetDocument("iot_async_plugin_idx_1", "1", Test1.class, ThreadUtils.newVirtualTaskExecutor()).join();
		Assertions.assertThat(test3).isNull();
		// 异步批量创建文档
		Assertions.assertThatNoException().isThrownBy(() -> elasticsearchDocumentTemplate.asyncBulkCreateDocuments("iot_async_plugin_idx_1", List.of(new Test1(2L, "KK")), ThreadUtils.newVirtualTaskExecutor()).join());
		// 异步搜索文档
//		Page<Test1> page2 = elasticsearchSearchTemplate.asyncSearch(List.of("iot_async_plugin_idx_1"), new Search(null, 1, 10, null), Test1.class).join();
//		Assertions.assertThat(page2.getTotal()).isEqualTo(1L);
		// 异步删除索引
		Assertions.assertThatNoException().isThrownBy(() -> elasticsearchIndexTemplate.asyncDeleteIndex(List.of("iot_async_plugin_idx_1"), ThreadUtils.newVirtualTaskExecutor()).join());
		Thread.sleep(Duration.ofSeconds(1));
		// 异步判断索引是否存在
		Assertions.assertThat(elasticsearchIndexTemplate.asyncExist(List.of("iot_async_plugin_idx_1"),  ThreadUtils.newVirtualTaskExecutor()).join()).isFalse();
	}

	@Test
	void test_syncApi() throws IOException {
		// 同步创建索引
		Assertions.assertThatNoException().isThrownBy(() -> elasticsearchIndexTemplate.createIndex("iot_plugin_idx_1", "iot_plugin_idx", Test1.class));
		// 同步查看索引
		Map<String, IndexState> map = elasticsearchIndexTemplate.getIndex(List.of("iot_plugin_idx_1")).indices();
		Assertions.assertThat(map).hasSize(1);
		Assertions.assertThat(map.get("iot_plugin_idx_1")).isNotNull();
		// 同步创建文档
		Assertions.assertThatNoException().isThrownBy(() -> elasticsearchDocumentTemplate.createDocument("iot_plugin_idx_1", "1", new Test1(1L, "laokou")));
		// 同步搜索文档【分词匹配】
//		Search.Highlight highlight = new Search.Highlight();
//		highlight.setFields(Set.of(new Search.HighlightField("name", 0, 0)));
//		Query.Builder queryBuilder = new Query.Builder();
//		MatchQuery.Builder matchQueryBuilder = new MatchQuery.Builder();
//		matchQueryBuilder.field("name").query("lao").fuzziness("AUTO");
//		queryBuilder.match(matchQueryBuilder.build());
//		Search search = new Search(highlight, 1, 10, queryBuilder.build());
//		Page<Test1> page = elasticsearchSearchTemplate.search(List.of("iot_plugin_idx_1"), search, Test1.class);
//		Assertions.assertThat(page.getTotal()).isEqualTo(1L);
//		Test1 test1 = page.getRecords().getFirst();
//		Assertions.assertThat(test1.name()).isEqualTo("laokou");
		// 同步查看文档
		Test1 test2 = elasticsearchDocumentTemplate.getDocument("iot_plugin_idx_1", "1", Test1.class);
		Assertions.assertThat(test2).isNotNull();
		Assertions.assertThat(test2.name()).isEqualTo("laokou");
		// 同步删除文档
		Assertions.assertThatNoException().isThrownBy(() -> elasticsearchDocumentTemplate.deleteDocument("iot_plugin_idx_1", test2.id().toString()));
		// 同步查看文档
		Test1 test3 = elasticsearchDocumentTemplate.getDocument("iot_plugin_idx_1", "1", Test1.class);
		Assertions.assertThat(test3).isNull();
		// 同步批量创建文档
		Assertions.assertThatNoException().isThrownBy(() -> elasticsearchDocumentTemplate.bulkCreateDocuments("iot_plugin_idx_1", List.of(new Test1(2L, "KK"))));
		// 同步搜索文档
//		Page<Test1> page2 = elasticsearchSearchTemplate.search(List.of("iot_plugin_idx_1"), new Search(null, 1, 10, null), Test1.class);
//		Assertions.assertThat(page2.getTotal()).isEqualTo(1L);
		// 同步删除索引
		Assertions.assertThatNoException().isThrownBy(() -> elasticsearchIndexTemplate.deleteIndex(List.of("iot_plugin_idx_1")));
		// 同步判断索引是否存在
		Assertions.assertThat(elasticsearchIndexTemplate.exist(List.of("iot_plugin_idx_1"))).isFalse();
	}

	@Index(setting = @Setting(refreshInterval = "-1"),
		analysis = @Analysis(filters = {
			@Filter(name = "pinyin_filter",
				options = {
					@Option(key = "type", value = "pinyin"),
					@Option(key = "keep_full_pinyin", value = "false"),
					@Option(key = "keep_joined_full_pinyin", value = "true"),
					@Option(key = "keep_original", value = "true"),
					@Option(key = "limit_first_letter_length", value = "16"),
					@Option(key = "remove_duplicated_term", value = "true"),
					@Option(key = "none_chinese_pinyin_tokenize", value = "false")}
			)
		},
		analyzers = { @Analyzer(name = "ik_pinyin", args = @Args(filter = "pinyin_filter", tokenizer = "ik_max_word")) })
	)
	record Test1(
		@JsonSerialize(using = ToStringSerializer.class) @Field(type = Type.LONG) Long id,
		@Field(type = Type.TEXT, searchAnalyzer = "ik_smart", analyzer = "ik_pinyin") String name) implements Serializable {
	}

//	@Data
//	@Index
//	static class Test2 implements Serializable {
//
//		@JsonSerialize(using = ToStringSerializer.class)
//		@Field(type = Type.LONG)
//		private Long id;
//
//		@Field(type = Type.KEYWORD)
//		private String name;
//
//	}

}
