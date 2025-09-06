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
import com.ulisesbocchio.jasyptspringboot.annotation.EnableEncryptableProperties;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.laokou.common.elasticsearch.annotation.*;
import org.laokou.common.elasticsearch.entity.Search;
import org.laokou.common.elasticsearch.template.ElasticsearchTemplate;
import org.laokou.common.i18n.dto.Page;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.TestConstructor;
import org.testcontainers.elasticsearch.ElasticsearchContainer;
import org.testcontainers.utility.DockerImageName;

import java.io.IOException;
import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Executors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatNoException;

/**
 * @author laokou
 */
@SpringBootTest
@RequiredArgsConstructor
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
class Elasticsearch8ApiTest {

	private final ElasticsearchTemplate elasticsearchTemplate;

//	static ElasticsearchContainer elasticsearch = new ElasticsearchContainer(DockerImageName.parse("registry.cn-shenzhen.aliyuncs.com/koushenhai/elasticsearch8:8.19.3").asCompatibleSubstituteFor("docker.elastic.co/elasticsearch/elasticsearch"))
//		.withPassword("laokou123");
//
//	@BeforeAll
//	static void beforeAll() {
//		elasticsearch.start();
//	}
//
//	@AfterAll
//	static void afterAll() {
//		elasticsearch.stop();
//	}
//
//	@DynamicPropertySource
//	static void configureProperties(DynamicPropertyRegistry registry) {
//		registry.add("spring.elasticsearch.uris", () -> List.of("https://" + elasticsearch.getHttpHostAddress()));
//	}

	@Test
	void test_elasticsearch() throws IOException {
		assertThatNoException()
			.isThrownBy(() -> elasticsearchTemplate.createIndex("iot_res_1", "iot_res", TestResource.class));
		assertThatNoException()
			.isThrownBy(() -> elasticsearchTemplate.createIndex("iot_pro_1", "iot_pro", TestProject.class));
		assertThatNoException().isThrownBy(() -> elasticsearchTemplate.asyncCreateIndex("iot_resp_1", "iot_resp",
				TestResp.class, Executors.newSingleThreadExecutor()));
		assertThatNoException().isThrownBy(
				() -> elasticsearchTemplate.asyncCreateDocument("iot_res_1", "222", new TestResource("8888")).join());
		assertThatNoException().isThrownBy(
				() -> elasticsearchTemplate
					.asyncBulkCreateDocument("iot_res_1", Map.of("555", new TestResource("6666")),
							Executors.newSingleThreadExecutor())
					.join());
		assertThatNoException().isThrownBy(
			() -> elasticsearchTemplate
				.asyncBulkCreateDocument("iot_res_1", List.of(new TestResource("6666")),
					Executors.newSingleThreadExecutor())
				.join());
		assertThatNoException()
			.isThrownBy(() -> elasticsearchTemplate.createDocument("iot_res_1", "444", new TestResource("3333")));
		assertThatNoException().isThrownBy(
				() -> elasticsearchTemplate.bulkCreateDocument("iot_res_1", Map.of("333", new TestResource("5555"))));
		assertThatNoException().isThrownBy(
			() -> elasticsearchTemplate.bulkCreateDocument("iot_res_1", List.of(new TestResource("5555"))));
		assertThat(elasticsearchTemplate.exist(List.of("iot_res_1", "iot_pro_1", "iot_resp_1"))).isTrue();
		Search.Highlight highlight = new Search.Highlight();
		highlight.setFields(Set.of(new Search.HighlightField("name", 0, 0)));
		Search search = new Search(highlight, 1, 10, null);
		Page<TestResult> results = elasticsearchTemplate.search(List.of("iot_res", "iot_res_1"), search,
				TestResult.class);
		assertThat(results).isNotNull();
		assertThat(results.getTotal() > 0).isTrue();
		Map<String, IndexState> result = elasticsearchTemplate
			.getIndex(List.of("iot_res_1", "iot_pro_1", "iot_resp_1"));
		assertThat(result).isNotNull();
		assertThat(result.isEmpty()).isFalse();
		assertThatNoException().isThrownBy(
				() -> elasticsearchTemplate.deleteIndex(List.of("laokou_res_1", "laokou_pro_1", "laokou_resp_1")));
	}

}

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
record TestResource(@Field(type = Type.TEXT, searchAnalyzer = "ik_smart", analyzer = "ik_pinyin") String name)
	implements
	Serializable {
}

@Data
@Index
class TestResp implements Serializable {
	@Field(type = Type.KEYWORD) private String key;
}

@Data
@Index
class TestProject implements Serializable {
	@JsonSerialize(using = ToStringSerializer.class)
	@Field(type = Type.LONG)
	private Long businessKey;
}

@Data
class TestResult implements Serializable {

	private String id;
	private String name;
}

@EnableEncryptableProperties
@EnableConfigurationProperties
@SpringBootApplication(scanBasePackages = "org.laokou")
class AppTest {

	public static void main(String[] args) {
		new SpringApplicationBuilder(AppTest.class).web(WebApplicationType.SERVLET).run(args);
	}

}
