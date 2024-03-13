/*
 * Copyright (c) 2022-2024 KCloud-Platform-Alibaba Author or Authors. All Rights Reserved.
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

package org.laokou.common.elasticsearch.template;

import co.elastic.clients.elasticsearch.ElasticsearchAsyncClient;
import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.Refresh;
import co.elastic.clients.elasticsearch._types.analysis.TokenFilter;
import co.elastic.clients.elasticsearch._types.mapping.DynamicMapping;
import co.elastic.clients.elasticsearch._types.mapping.TypeMapping;
import co.elastic.clients.elasticsearch.core.IndexResponse;
import co.elastic.clients.elasticsearch.core.bulk.BulkOperation;
import co.elastic.clients.elasticsearch.indices.*;
import co.elastic.clients.transport.endpoints.BooleanResponse;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.laokou.common.core.utils.JacksonUtil;
import org.laokou.common.elasticsearch.annotation.*;
import org.laokou.common.i18n.utils.StringUtil;
import org.springframework.stereotype.Component;

import java.io.ByteArrayInputStream;
import java.lang.reflect.Field;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

import static org.laokou.common.i18n.common.StringConstants.COMMA;

/**
 * @author laokou
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class ElasticsearchTemplate {

	private final ElasticsearchClient elasticsearchClient;

	private final ElasticsearchAsyncClient elasticsearchAsyncClient;

	@SneakyThrows
	public <TDocument> CompletableFuture<Boolean> asyncCreateIndex(String name, String alias, Class<TDocument> clazz) {
		return asyncExist(List.of(name)).thenApplyAsync(resp -> {
			if (resp) {
				log.error("索引：{} -> 创建索引失败，索引已存在", name);
				return Boolean.FALSE;
			}
			return Boolean.TRUE;
		}).thenApplyAsync(resp -> {
			if (resp) {
				Document document = convert(name, alias, clazz);
				elasticsearchAsyncClient.indices().create(getCreateIndexRequest(document)).thenApplyAsync(response -> {
					if (response.acknowledged()) {
						log.info("索引：{} -> 创建索引成功", name);
						return Boolean.TRUE;
					}
					else {
						log.error("索引：{} -> 创建索引失败", name);
						return Boolean.FALSE;
					}
				});
			}
			return Boolean.FALSE;
		});
	}

	@SneakyThrows
	public <TDocument> void createIndex(String name, String alias, Class<TDocument> clazz) {
		// 判断索引是否存在
		if (exist(List.of(name))) {
			log.error("索引：{} -> 创建索引失败，索引已存在", name);
			return;
		}
		Document document = convert(name, alias, clazz);
		CreateIndexResponse createIndexResponse = elasticsearchClient.indices().create(getCreateIndexRequest(document));
		boolean acknowledged = createIndexResponse.acknowledged();
		if (acknowledged) {
			log.info("索引：{} -> 创建索引成功", name);
		}
		else {
			log.error("索引：{} -> 创建索引失败", name);
		}
	}

	@SneakyThrows
	public void deleteIndex(List<String> names) {
		if (!exist(names)) {
			log.error("索引：{} -> 删除索引失败，索引不存在", StringUtil.collectionToDelimitedString(names, COMMA));
			return;
		}
		DeleteIndexResponse deleteIndexResponse = elasticsearchClient.indices().delete(getDeleteIndexRequest(names));
		boolean acknowledged = deleteIndexResponse.acknowledged();
		if (acknowledged) {
			log.info("索引：{} -> 删除索引成功", StringUtil.collectionToDelimitedString(names, COMMA));
		}
		else {
			log.error("索引：{} -> 删除索引失败", StringUtil.collectionToDelimitedString(names, COMMA));
		}
	}

	@SneakyThrows
	public Map<String, IndexState> getIndex(List<String> names) {
		return elasticsearchClient.indices().get(getIndexRequest(names)).result();
	}

	@SneakyThrows
	public void createDocument(String index, String id, Object obj) {
		IndexResponse response = elasticsearchClient
			.index(idx -> idx.index(index).refresh(Refresh.True).id(id).document(obj));
		if (StringUtil.isNotEmpty(response.result().jsonValue())) {
			log.info("索引：{} -> 同步索引成功", index);
		}
		else {
			log.error("索引：{} -> 同步索引失败", index);
		}
	}

	@SneakyThrows
	public CompletableFuture<Boolean> asyncCreateDocument(String index, String id, Object obj) {
		return elasticsearchAsyncClient.index(idx -> idx.index(index).refresh(Refresh.True).id(id).document(obj))
			.thenApplyAsync(resp -> {
				if (StringUtil.isNotEmpty(resp.result().jsonValue())) {
					log.info("索引：{} -> 异步同步索引成功", index);
					return Boolean.TRUE;
				}
				else {
					log.error("索引：{} -> 异步同步索引失败", index);
					return Boolean.FALSE;
				}
			});
	}

	@SneakyThrows
	public void bulkCreateDocument(String index, Map<String, Object> map) {
		boolean errors = elasticsearchClient
			.bulk(bulk -> bulk.index(index).refresh(Refresh.True).operations(getBulkOperations(map)))
			.errors();
		if (errors) {
			log.error("索引：{} -> 批量同步索引失败", index);
		}
		else {
			log.info("索引：{} -> 批量同步索引成功", index);
		}
	}

	@SneakyThrows
	public CompletableFuture<Boolean> asyncBulkCreateDocument(String index, Map<String, Object> map) {
		return elasticsearchAsyncClient
			.bulk(bulk -> bulk.index(index).refresh(Refresh.True).operations(getBulkOperations(map)))
			.thenApplyAsync(resp -> {
				if (resp.errors()) {
					log.error("索引：{} -> 异步批量同步索引失败", index);
					return Boolean.FALSE;
				}
				else {
					log.info("索引：{} -> 异步批量同步索引成功", index);
					return Boolean.TRUE;
				}
			});
	}

	@SneakyThrows
	public boolean exist(List<String> names) {
		return elasticsearchClient.indices().exists(getExists(names)).value();
	}

	public CompletableFuture<Boolean> asyncExist(List<String> names) {
		return elasticsearchAsyncClient.indices().exists(getExists(names)).thenApplyAsync(BooleanResponse::value);
	}

	private List<BulkOperation> getBulkOperations(Map<String, Object> map) {
		return map.entrySet()
			.stream()
			.map(entry -> BulkOperation.of(idx -> idx.index(fn -> fn.id(entry.getKey()).document(entry.getValue()))))
			.toList();
	}

	private ExistsRequest getExists(List<String> names) {
		ExistsRequest.Builder existBuilder = new ExistsRequest.Builder();
		existBuilder.index(names);
		return existBuilder.build();
	}

	private GetIndexRequest getIndexRequest(List<String> names) {
		GetIndexRequest.Builder getIndexbuilder = new GetIndexRequest.Builder();
		getIndexbuilder.index(names);
		return getIndexbuilder.build();
	}

	private DeleteIndexRequest getDeleteIndexRequest(List<String> names) {
		DeleteIndexRequest.Builder deleteIndexBuilder = new DeleteIndexRequest.Builder();
		deleteIndexBuilder.index(names);
		return deleteIndexBuilder.build();
	}

	private CreateIndexRequest getCreateIndexRequest(Document document) {
		String name = document.getName();
		String alias = document.getAlias();
		CreateIndexRequest.Builder createIndexbuilder = new CreateIndexRequest.Builder();
		if (StringUtil.isNotEmpty(alias)) {
			// 别名
			createIndexbuilder.aliases(alias, fn -> fn.isWriteIndex(true));
		}
		return createIndexbuilder.index(name).mappings(getMappings(document)).settings(getSettings(document)).build();
	}

	private IndexSettings getSettings(Document document) {
		Document.Setting setting = document.getSetting();
		IndexSettings.Builder settingBuilder = new IndexSettings.Builder();
		settingBuilder.numberOfShards(String.valueOf(setting.getShards()));
		settingBuilder.numberOfReplicas(String.valueOf(setting.getReplicas()));
		settingBuilder.refreshInterval(fn -> fn.time(setting.getRefreshInterval()));
		settingBuilder.analysis(getAnalysisBuilder(document));
		return settingBuilder.build();
	}

	private IndexSettingsAnalysis getAnalysisBuilder(Document document) {
		IndexSettingsAnalysis.Builder settingsAnalysisBuilder = new IndexSettingsAnalysis.Builder();
		Document.Analysis analysis = document.getAnalysis();
		List<Document.Filter> filters = analysis.getFilters();
		List<Document.Analyzer> analyzers = analysis.getAnalyzers();
		analyzers.forEach(item -> settingsAnalysisBuilder.analyzer(item.getName(), getAnalyzer(item.getArgs())));
		filters.forEach(item -> settingsAnalysisBuilder.filter(item.getName(), getFilter(item.getOptions())));
		return settingsAnalysisBuilder.build();
	}

	private TypeMapping getMappings(Document document) {
		TypeMapping.Builder mappingBuilder = new TypeMapping.Builder();
		mappingBuilder.dynamic(DynamicMapping.True);
		List<Document.Mapping> mappings = document.getMappings();
		mappings.forEach(item -> setProperties(mappingBuilder, item));
		return mappingBuilder.build();
	}

	private TokenFilter getFilter(List<Document.Option> options) {
		TokenFilter.Builder filterBuilder = new TokenFilter.Builder();
		Map<String, String> map = options.stream()
			.collect(Collectors.toMap(Document.Option::getKey, Document.Option::getValue));
		filterBuilder.definition(fn -> fn
			.withJson(new ByteArrayInputStream(JacksonUtil.toJsonStr(map).getBytes(StandardCharsets.UTF_8))));
		return filterBuilder.build();
	}

	private co.elastic.clients.elasticsearch._types.analysis.Analyzer getAnalyzer(Document.Args args) {
		co.elastic.clients.elasticsearch._types.analysis.Analyzer.Builder analyzerBuilder = new co.elastic.clients.elasticsearch._types.analysis.Analyzer.Builder();
		analyzerBuilder.custom(fn -> fn.filter(args.getFilter()).tokenizer(args.getTokenizer()));
		return analyzerBuilder.build();
	}

	private void setProperties(TypeMapping.Builder mappingBuilder, Document.Mapping mapping) {
		Type type = mapping.getType();
		String field = mapping.getField();
		String analyzer = mapping.getAnalyzer();
		boolean fielddata = mapping.isFielddata();
		String searchAnalyzer = mapping.getSearchAnalyzer();
		boolean eagerGlobalOrdinals = mapping.isEagerGlobalOrdinals();
		switch (type) {
			case TEXT -> mappingBuilder.properties(field,
					fn -> fn.text(t -> t.index(true)
						.fielddata(fielddata)
						.eagerGlobalOrdinals(eagerGlobalOrdinals)
						.searchAnalyzer(searchAnalyzer)
						.analyzer(analyzer)));
			case KEYWORD ->
				mappingBuilder.properties(field, fn -> fn.keyword(t -> t.eagerGlobalOrdinals(eagerGlobalOrdinals)));
			case LONG -> mappingBuilder.properties(field, fn -> fn.long_(t -> t));
			default -> {
			}
		}
	}

	private <TDocument> Document convert(String name, String alias, Class<TDocument> clazz) {
		boolean annotationPresent = clazz.isAnnotationPresent(Index.class);
		if (annotationPresent) {
			Index index = clazz.getAnnotation(Index.class);
			return Document.builder()
				.name(name)
				.alias(alias)
				.mappings(getMappings(clazz))
				.setting(getSetting(index))
				.analysis(getAnalysis(index))
				.build();
		}
		throw new RuntimeException("Not found @Index");
	}

	private Document.Analysis getAnalysis(Index index) {
		Analysis analysis = index.analysis();
		Analyzer[] analyzers = analysis.analyzers();
		Filter[] filters = analysis.filters();
		return new Document.Analysis(getFilters(filters), getAnalyzer(analyzers));
	}

	private List<Document.Analyzer> getAnalyzer(Analyzer[] analyzers) {
		return Arrays.stream(analyzers).map(item -> new Document.Analyzer(item.name(), getArgs(item.args()))).toList();
	}

	private Document.Args getArgs(Args args) {
		return new Document.Args(args.filter(), args.tokenizer());
	}

	private List<Document.Filter> getFilters(Filter[] filters) {
		return Arrays.stream(filters).map(item -> new Document.Filter(item.name(), getOptions(item))).toList();
	}

	private List<Document.Option> getOptions(Filter filter) {
		return Arrays.stream(filter.options()).map(item -> new Document.Option(item.key(), item.value())).toList();
	}

	private Document.Setting getSetting(Index index) {
		Setting setting = index.setting();
		return new Document.Setting(setting.shards(), setting.replicas(), setting.refreshInterval());
	}

	private <TDocument> List<Document.Mapping> getMappings(Class<TDocument> clazz) {
		// 获取所有字段（包括私有字段）
		Field[] fields = clazz.getDeclaredFields();
		return Arrays.stream(fields)
			.filter(item -> item.isAnnotationPresent(org.laokou.common.elasticsearch.annotation.Field.class))
			.map(item -> getMapping(item, item.getAnnotation(org.laokou.common.elasticsearch.annotation.Field.class)))
			.toList();
	}

	private Document.Mapping getMapping(Field item, org.laokou.common.elasticsearch.annotation.Field field) {
		String value = field.value();
		value = StringUtil.isEmpty(value) ? item.getName() : value;
		Type type = field.type();
		String searchAnalyzer = field.searchAnalyzer();
		String analyzer = field.analyzer();
		boolean fielddata = field.fielddata();
		boolean eagerGlobalOrdinals = field.eagerGlobalOrdinals();
		return new Document.Mapping(value, type, searchAnalyzer, analyzer, fielddata, eagerGlobalOrdinals);
	}

}
