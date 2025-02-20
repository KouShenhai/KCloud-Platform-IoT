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
import co.elastic.clients.elasticsearch._types.Refresh;
import co.elastic.clients.elasticsearch._types.SortOrder;
import co.elastic.clients.elasticsearch._types.analysis.TokenFilter;
import co.elastic.clients.elasticsearch._types.mapping.DynamicMapping;
import co.elastic.clients.elasticsearch._types.mapping.TypeMapping;
import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import co.elastic.clients.elasticsearch.core.IndexResponse;
import co.elastic.clients.elasticsearch.core.SearchRequest;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import co.elastic.clients.elasticsearch.core.bulk.BulkOperation;
import co.elastic.clients.elasticsearch.core.search.HitsMetadata;
import co.elastic.clients.elasticsearch.indices.*;
import co.elastic.clients.transport.endpoints.BooleanResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.laokou.common.i18n.utils.JacksonUtil;
import org.laokou.common.elasticsearch.annotation.*;
import org.laokou.common.elasticsearch.entity.Search;
import org.laokou.common.i18n.dto.Page;
import org.laokou.common.i18n.utils.ObjectUtil;
import org.laokou.common.i18n.utils.StringUtil;
import org.springframework.stereotype.Component;
import org.springframework.util.ReflectionUtils;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.stream.Collectors;

import static org.laokou.common.i18n.common.constant.StringConstant.COMMA;

/**
 * @author laokou
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class ElasticsearchTemplate {

	private static final String PRIMARY_KEY = "id";

	private final ElasticsearchClient elasticsearchClient;

	private final ElasticsearchAsyncClient elasticsearchAsyncClient;

	public <TDocument> CompletableFuture<Void> asyncCreateIndex(String name, String alias, Class<TDocument> clazz,
			Executor executor) {
		return asyncExist(List.of(name), executor).thenApplyAsync(resp -> {
			if (resp) {
				log.info("索引：{} -> 创建索引失败，索引已存在", name);
				return Boolean.FALSE;
			}
			return Boolean.TRUE;
		}, executor).thenAcceptAsync(result -> {
			if (result) {
				elasticsearchAsyncClient.indices()
					.create(getCreateIndexRequest(convert(name, alias, clazz)))
					.thenAcceptAsync(response -> {
						if (response.acknowledged()) {
							log.info("索引：{} -> 创建索引成功", name);
						}
						else {
							log.info("索引：{} -> 创建索引失败", name);
						}
					});
			}
		}, executor);
	}

	public <TDocument> void createIndex(String name, String alias, Class<TDocument> clazz) throws IOException {
		// 判断索引是否存在
		if (exist(List.of(name))) {
			log.info("索引：{} -> 创建索引失败，索引已存在", name);
			return;
		}
		Document document = convert(name, alias, clazz);
		CreateIndexResponse createIndexResponse = elasticsearchClient.indices().create(getCreateIndexRequest(document));
		boolean acknowledged = createIndexResponse.acknowledged();
		if (acknowledged) {
			log.info("索引：{} -> 创建索引成功", name);
		}
		else {
			log.info("索引：{} -> 创建索引失败", name);
		}
	}

	public void deleteIndex(List<String> names) throws IOException {
		if (!exist(names)) {
			log.info("索引：{} -> 删除索引失败，索引不存在", StringUtil.collectionToDelimitedString(names, COMMA));
			return;
		}
		DeleteIndexResponse deleteIndexResponse = elasticsearchClient.indices().delete(getDeleteIndexRequest(names));
		boolean acknowledged = deleteIndexResponse.acknowledged();
		if (acknowledged) {
			log.info("索引：{} -> 删除索引成功", StringUtil.collectionToDelimitedString(names, COMMA));
		}
		else {
			log.info("索引：{} -> 删除索引失败", StringUtil.collectionToDelimitedString(names, COMMA));
		}
	}

	public Map<String, IndexState> getIndex(List<String> names) throws IOException {
		return elasticsearchClient.indices().get(getIndexRequest(names)).result();
	}

	public <T> void createDocument(String index, String id, T obj) throws IOException {
		IndexResponse response = elasticsearchClient
			.index(idx -> idx.index(index).refresh(Refresh.True).id(id).document(obj));
		if (StringUtil.isNotEmpty(response.result().jsonValue())) {
			log.info("索引：{} -> 同步索引成功", index);
		}
		else {
			log.info("索引：{} -> 同步索引失败", index);
		}
	}

	public <T> CompletableFuture<Void> asyncCreateDocument(String index, String id, T obj) {
		return elasticsearchAsyncClient.index(idx -> idx.index(index).refresh(Refresh.True).id(id).document(obj))
			.thenAcceptAsync(resp -> {
				if (StringUtil.isNotEmpty(resp.result().jsonValue())) {
					log.info("索引：{} -> 异步同步索引成功", index);
				}
				else {
					log.info("索引：{} -> 异步同步索引失败", index);
				}
			});
	}

	public <T> void bulkCreateDocument(String index, Map<String, T> map) {
		try {
			boolean errors = elasticsearchClient
				.bulk(bulk -> bulk.index(index).refresh(Refresh.True).operations(getBulkOperations(map)))
				.errors();
			if (errors) {
				log.info("索引：{} -> 批量同步索引失败", index);
			}
			else {
				log.info("索引：{} -> 批量同步索引成功", index);
			}
		}
		catch (Throwable e) {
			log.error("批量同步索引失败，错误信息：{}", e.getMessage());
		}
	}

	public <T> CompletableFuture<Void> asyncBulkCreateDocument(String index, Map<String, T> map, Executor executor) {
		return elasticsearchAsyncClient
			.bulk(bulk -> bulk.index(index).refresh(Refresh.True).operations(getBulkOperations(map)))
			.thenAcceptAsync(resp -> {
				if (resp.errors()) {
					log.info("索引：{} -> 异步批量同步索引失败", index);
				}
				else {
					log.info("索引：{} -> 异步批量同步索引成功", index);
				}
			}, executor);
	}

	public boolean exist(List<String> names) throws IOException {
		return elasticsearchClient.indices().exists(getExists(names)).value();
	}

	public <R> Page<R> search(List<String> names, Search search, Class<R> clazz) throws IOException {
		SearchRequest searchRequest = getSearchRequest(names, search);
		SearchResponse<R> response = elasticsearchClient.search(searchRequest, clazz);
		HitsMetadata<R> hits = response.hits();
		assert hits.total() != null;
		return Page.create(hits.hits().stream().map(item -> {
			R source = item.source();
			if (source != null) {
				// ID赋值
				setId(source, item.id());
				// 高亮字段赋值
				setHighlightFields(source, item.highlight());
			}
			return source;
		}).toList(), hits.total().value());
	}

	private <R> void setHighlightFields(R source, Map<String, List<String>> map) {
		Class<?> clazz = source.getClass();
		map.forEach((k, v) -> {
			try {
				Field field = clazz.getDeclaredField(k);
				field.setAccessible(true);
				ReflectionUtils.setField(field, source, v.getFirst());
			}
			catch (NoSuchFieldException e) {
				throw new RuntimeException(e);
			}
		});
	}

	private <R> void setId(R source, String id) {
		try {
			Field field = source.getClass().getDeclaredField(PRIMARY_KEY);
			field.setAccessible(true);
			ReflectionUtils.setField(field, source, id);
		}
		catch (Exception e) {
			log.error("ID赋值失败，错误信息：{}", e.getMessage());
		}
	}

	public CompletableFuture<Boolean> asyncExist(List<String> names, Executor executor) {
		return elasticsearchAsyncClient.indices()
			.exists(getExists(names))
			.thenApplyAsync(BooleanResponse::value, executor);
	}

	private SearchRequest getSearchRequest(List<String> names, Search search) {
		// 查询条件
		Integer pageNo = search.getPageNo();
		Integer pageSize = search.getPageSize();
		Query query = search.getQuery();
		SearchRequest.Builder builder = new SearchRequest.Builder();
		builder.index(names);
		builder.from((pageNo - 1) * pageSize);
		builder.size(pageSize);
		// 获取真实总数
		builder.trackTotalHits(fn -> fn.enabled(true));
		// 追踪分数开启
		builder.trackScores(true);
		// 注解
		builder.explain(true);
		// 匹配度倒排，数值越大匹配度越高
		builder.sort(fn -> fn.score(s -> s.order(SortOrder.Desc)));
		builder.highlight(getHighlight(search.getHighlight()));
		// bool查询 => 布尔查询，允许组合多个查询条件
		// must查询类似and查询
		// must_not查询类似not查询
		// should查询类似or查询
		// query_string查询text类型字段，不需要连续，顺序还可以调换（分词）
		// match_phrase查询text类型字段，顺序必须相同，而且必须都是连续的（分词）
		// term精准匹配
		// match模糊匹配（分词）
		if (ObjectUtil.isNotNull(query)) {
			builder.query(query);
		}
		return builder.build();
	}

	private co.elastic.clients.elasticsearch.core.search.Highlight getHighlight(Search.Highlight highlight) {
		if (highlight == null) {
			return new co.elastic.clients.elasticsearch.core.search.Highlight.Builder().build();
		}
		co.elastic.clients.elasticsearch.core.search.Highlight.Builder builder = new co.elastic.clients.elasticsearch.core.search.Highlight.Builder();
		builder.preTags(highlight.getPreTags());
		builder.postTags(highlight.getPostTags());
		// 多个字段高亮，需要设置false
		builder.requireFieldMatch(highlight.isRequireFieldMatch());
		// numberOfFragments => 获取高亮片段位置
		// fragmentSize => 最大高亮分片数
		builder.fields(getHighlightFieldMap(highlight.getFields()));
		return builder.build();
	}

	private Map<String, co.elastic.clients.elasticsearch.core.search.HighlightField> getHighlightFieldMap(
			List<Search.HighlightField> fields) {
		return fields.stream().collect(Collectors.toMap(Search.HighlightField::getName, j -> {
			co.elastic.clients.elasticsearch.core.search.HighlightField.Builder builder = new co.elastic.clients.elasticsearch.core.search.HighlightField.Builder();
			builder.fragmentSize(j.getFragmentSize());
			builder.numberOfFragments(j.getNumberOfFragments());
			return builder.build();
		}));
	}

	private <T> List<BulkOperation> getBulkOperations(Map<String, T> map) {
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
		CreateIndexRequest.Builder createIndexbuilder = new CreateIndexRequest.Builder();
		// 别名
		createIndexbuilder.aliases(document.getAlias(), fn -> fn.isWriteIndex(true));
		return createIndexbuilder.index(document.getName())
			.mappings(getMappings(document))
			.settings(getSettings(document))
			.build();
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
		mappings.forEach(item -> item.getType().setProperties(mappingBuilder, item));
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

	private <TDocument> Document convert(String name, String alias, Class<TDocument> clazz) {
		boolean annotationPresent = clazz.isAnnotationPresent(Index.class);
		if (annotationPresent) {
			Index index = clazz.getAnnotation(Index.class);
			alias = StringUtil.isNotEmpty(alias) ? alias : name;
			return new Document(name, alias, getMappings(clazz), getSetting(index), getAnalysis(index));
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
		boolean fieldData = field.fielddata();
		boolean eagerGlobalOrdinals = field.eagerGlobalOrdinals();
		String format = field.format();
		boolean isIndex = field.index();
		return new Document.Mapping(value, type, searchAnalyzer, analyzer, fieldData, eagerGlobalOrdinals, format,
				isIndex);
	}

}
