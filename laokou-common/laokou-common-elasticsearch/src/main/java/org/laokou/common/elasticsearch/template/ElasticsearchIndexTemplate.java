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

package org.laokou.common.elasticsearch.template;

import co.elastic.clients.elasticsearch.ElasticsearchAsyncClient;
import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.analysis.TokenFilter;
import co.elastic.clients.elasticsearch._types.mapping.DynamicMapping;
import co.elastic.clients.elasticsearch._types.mapping.TypeMapping;
import co.elastic.clients.elasticsearch.indices.CreateIndexRequest;
import co.elastic.clients.elasticsearch.indices.CreateIndexResponse;
import co.elastic.clients.elasticsearch.indices.DeleteIndexResponse;
import co.elastic.clients.elasticsearch.indices.GetIndexResponse;
import co.elastic.clients.elasticsearch.indices.IndexSettings;
import co.elastic.clients.elasticsearch.indices.IndexSettingsAnalysis;
import co.elastic.clients.transport.endpoints.BooleanResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.laokou.common.elasticsearch.annotation.Analysis;
import org.laokou.common.elasticsearch.annotation.Analyzer;
import org.laokou.common.elasticsearch.annotation.Args;
import org.laokou.common.elasticsearch.annotation.Document;
import org.laokou.common.elasticsearch.annotation.Filter;
import org.laokou.common.elasticsearch.annotation.Index;
import org.laokou.common.elasticsearch.annotation.Setting;
import org.laokou.common.elasticsearch.annotation.Type;
import org.laokou.common.i18n.common.constant.StringConstants;
import org.laokou.common.i18n.util.JacksonUtils;
import org.laokou.common.i18n.util.StringUtils;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.stream.Collectors;

/**
 * @author laokou
 */
@Slf4j
@RequiredArgsConstructor
public final class ElasticsearchIndexTemplate {

	private final ElasticsearchClient elasticsearchClient;

	private final ElasticsearchAsyncClient elasticsearchAsyncClient;

	public <TDocument> void createIndex(String name, String alias, Class<TDocument> clazz) throws IOException {
		// 判断索引是否存在
		if (exist(List.of(name))) {
			log.debug("index name: 【{}】 -> Sync index create failed, index already exists", name);
			return;
		}
		CreateIndexResponse createIndexResponse = elasticsearchClient.indices().create(getCreateIndexRequest(getDocument(name, alias, clazz)));
		printLog(name, createIndexResponse.acknowledged() ? "Sync index create succeeded" : "Sync index create failed");
	}

	public <TDocument> CompletableFuture<Void> asyncCreateIndex(String name, String alias, Class<TDocument> clazz,
																Executor executor) {
		return asyncExist(List.of(name), executor).thenApplyAsync(resp -> {
			if (resp) {
				log.debug("index name: 【{}】 -> Async index create failed, index already exists", name);
				return Boolean.FALSE;
			}
			return Boolean.TRUE;
		}, executor).thenAcceptAsync(result -> {
			if (result) {
				elasticsearchAsyncClient.indices()
					.create(getCreateIndexRequest(getDocument(name, alias, clazz)))
					.thenAcceptAsync(response -> printLog(name, response.acknowledged() ? "Async index create succeeded" : "Async index create failed"));
			}
		}, executor);
	}

	public void deleteIndex(List<String> names) throws IOException {
		if (!exist(names)) {
			log.debug("index name: 【{}】 -> Sync index delete failed, index already exists", StringUtils.collectionToDelimitedString(names, StringConstants.DROP));
			return;
		}
		DeleteIndexResponse deleteIndexResponse = elasticsearchClient.indices().delete(fn -> fn.index(names));
		printLog(StringUtils.collectionToDelimitedString(names, StringConstants.DROP), deleteIndexResponse.acknowledged() ? "Sync index delete succeeded" : "Sync index delete failed");
	}

	public CompletableFuture<Void> asyncDeleteIndex(List<String> names, Executor executor) {
		return asyncExist(names, executor).thenApplyAsync(resp -> {
			if (!resp) {
				log.debug("index name: 【{}】 -> Async index delete failed, index already exists", StringUtils.collectionToDelimitedString(names, StringConstants.DROP));
				return Boolean.FALSE;
			}
			return Boolean.TRUE;
		}, executor).thenAcceptAsync(result -> {
			if (result) {
				elasticsearchAsyncClient.indices().delete(fn -> fn.index(names))
					.thenAcceptAsync(response -> printLog(StringUtils.collectionToDelimitedString(names, StringConstants.DROP), response.acknowledged() ? "Async index delete succeeded" : "Async index delete failed"));
			}
		});
	}

	public GetIndexResponse getIndex(List<String> names) throws IOException {
		return elasticsearchClient.indices().get(fn -> fn.index(names));
	}

	public CompletableFuture<GetIndexResponse> asyncGetIndex(List<String> names) {
		return elasticsearchAsyncClient.indices().get(fn -> fn.index(names));
	}

	public boolean exist(List<String> names) {
		try {
			return elasticsearchClient.indices().exists(fn -> fn.index(names)).value();
		} catch (Exception ex) {
			log.error("index name: 【{}】 -> Failed to determine if the index exists, error message: {}", StringUtils.collectionToDelimitedString(names, StringConstants.DROP), ex.getMessage(), ex);
			return false;
		}
	}

	public CompletableFuture<Boolean> asyncExist(List<String> names, Executor executor) {
		return elasticsearchAsyncClient.indices()
			.exists(fn -> fn.index(names))
			.thenApplyAsync(BooleanResponse::value, executor);
	}

	public static void printLog(String indexNames, String msg) {
		log.debug("index name: 【{}】 -> {}", indexNames, msg);
	}

	private CreateIndexRequest getCreateIndexRequest(Document document) {
		CreateIndexRequest.Builder createIndexbuilder = new CreateIndexRequest.Builder();
		// 别名
		createIndexbuilder.aliases(document.getAlias(), fn -> fn.isWriteIndex(true));
		return createIndexbuilder.index(document.getName())
			.mappings(getMappings(document))
			.settings(getSettings(document))
			.build();
	}

	private <TDocument> Document getDocument(String name, String alias, Class<TDocument> clazz) {
		boolean annotationPresent = clazz.isAnnotationPresent(Index.class);
		if (annotationPresent) {
			Index index = clazz.getAnnotation(Index.class);
			alias = StringUtils.isNotEmpty(alias) ? alias : name;
			return new Document(name, alias, getMappings(clazz), getSetting(index), getAnalysis(index));
		}
		throw new IllegalArgumentException("Entity class not configured with @Index annotation");
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

	private TokenFilter getFilter(List<Document.Option> options) {
		TokenFilter.Builder filterBuilder = new TokenFilter.Builder();
		Map<String, String> map = options.stream()
			.collect(Collectors.toMap(Document.Option::getKey, Document.Option::getValue));
		filterBuilder.definition(fn -> fn
			.withJson(new ByteArrayInputStream(JacksonUtils.toBytes(map))));
		return filterBuilder.build();
	}

	private co.elastic.clients.elasticsearch._types.analysis.Analyzer getAnalyzer(Document.Args args) {
		co.elastic.clients.elasticsearch._types.analysis.Analyzer.Builder analyzerBuilder = new co.elastic.clients.elasticsearch._types.analysis.Analyzer.Builder();
		analyzerBuilder.custom(fn -> fn.filter(args.getFilter()).tokenizer(args.getTokenizer()));
		return analyzerBuilder.build();
	}

	private TypeMapping getMappings(Document document) {
		TypeMapping.Builder mappingBuilder = new TypeMapping.Builder();
		mappingBuilder.dynamic(DynamicMapping.True);
		List<Document.Mapping> mappings = document.getMappings();
		mappings.forEach(item -> item.getType().setProperties(mappingBuilder, item));
		return mappingBuilder.build();
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
		value = StringUtils.isEmpty(value) ? item.getName() : value;
		Type type = field.type();
		String searchAnalyzer = field.searchAnalyzer();
		String analyzer = field.analyzer();
		boolean fieldData = field.fielddata();
		boolean eagerGlobalOrdinals = field.eagerGlobalOrdinals();
		String format = field.format();
		boolean isIndex = field.index();
		return new Document.Mapping(value, type, searchAnalyzer, analyzer, fieldData, eagerGlobalOrdinals, format,
			isIndex);
	}

}
