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

package org.laokou.common.elasticsearch.template;

import co.elastic.clients.elasticsearch.ElasticsearchAsyncClient;
import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.Refresh;
import co.elastic.clients.elasticsearch._types.SortOrder;
import co.elastic.clients.elasticsearch._types.analysis.TokenFilter;
import co.elastic.clients.elasticsearch._types.mapping.DynamicMapping;
import co.elastic.clients.elasticsearch._types.mapping.TypeMapping;
import co.elastic.clients.elasticsearch._types.query_dsl.BoolQuery;
import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import co.elastic.clients.elasticsearch._types.query_dsl.RangeQuery;
import co.elastic.clients.elasticsearch.core.IndexResponse;
import co.elastic.clients.elasticsearch.core.SearchRequest;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import co.elastic.clients.elasticsearch.core.bulk.BulkOperation;
import co.elastic.clients.elasticsearch.core.search.HitsMetadata;
import co.elastic.clients.elasticsearch.indices.*;
import co.elastic.clients.json.JsonData;
import co.elastic.clients.transport.endpoints.BooleanResponse;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.laokou.common.core.utils.CollectionUtil;
import org.laokou.common.core.utils.JacksonUtil;
import org.laokou.common.elasticsearch.annotation.*;
import org.laokou.common.i18n.dto.Datas;
import org.laokou.common.i18n.utils.ObjectUtil;
import org.laokou.common.i18n.utils.StringUtil;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.io.ByteArrayInputStream;
import java.lang.reflect.Field;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

import static org.laokou.common.i18n.common.constants.StringConstant.COMMA;

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

	public <S, R> Datas<R> search(List<String> names, int pageNum, int pageSize, S obj, Class<R> clazz) {
		return search(names, pageNum, pageSize, obj, null, clazz);
	}

	@SneakyThrows
	public <S, R> Datas<R> search(List<String> names, int pageNum, int pageSize, S obj, Search search, Class<R> clazz) {
		if (ObjectUtil.isNull(search)){
			search = convert(obj);
		}
		SearchRequest searchRequest = getSearchRequest(names, pageNum, pageSize, search);
		SearchResponse<R> response = elasticsearchClient.search(searchRequest, clazz);
		HitsMetadata<R> hits = response.hits();
		assert hits.total() != null;
		return Datas.create(hits.hits().stream().map(item -> {
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
				field.set(source, v.getFirst());
			}
			catch (NoSuchFieldException | IllegalAccessException e) {
				throw new RuntimeException(e);
			}
		});
	}

	private <R> void setId(R source, String id) {
		try {
			Field field = source.getClass().getDeclaredField(PRIMARY_KEY);
			field.setAccessible(true);
			field.set(source, id);
		}
		catch (Exception e) {
			log.error("ID赋值失败，错误信息：{}", e.getMessage(), e);
		}
	}

	public CompletableFuture<Boolean> asyncExist(List<String> names) {
		return elasticsearchAsyncClient.indices().exists(getExists(names)).thenApplyAsync(BooleanResponse::value);
	}

	private SearchRequest getSearchRequest(List<String> names, int pageNum, int pageSize, Search search) {
		SearchRequest.Builder builder = new SearchRequest.Builder();
		builder.index(names);
		builder.from((pageNum - 1) * pageSize);
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
		builder.query(getQuery(search.getFields()));
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

	private co.elastic.clients.elasticsearch._types.query_dsl.Query getQuery(List<Search.Field> fields) {
		co.elastic.clients.elasticsearch._types.query_dsl.Query.Builder builder = new co.elastic.clients.elasticsearch._types.query_dsl.Query.Builder();
		BoolQuery.Builder boolBuilder = new BoolQuery.Builder();
		builder.bool(getBoolQuery(fields, boolBuilder));
		return builder.build();
	}

	private BoolQuery getBoolQuery(List<Search.Field> fields, BoolQuery.Builder boolBuilder) {
		// bool查询 => 布尔查询，允许组合多个查询条件
		// must查询类似and查询
		// must_not查询类似not查询
		// should查询类似or查询
		// query_string查询text类型字段，不需要连续，顺序还可以调换（分词）
		// match_phrase查询text类型字段，顺序必须相同，而且必须都是连续的（分词）
		// term精准匹配
		// match模糊匹配（分词）
		fields.forEach(item -> {
			switch (item.getQuery()) {
				case MUST -> boolBuilder.must(getQuery(item));
				case SHOULD -> boolBuilder.should(getQuery(item));
				case MUST_NOT -> boolBuilder.mustNot(getQuery(item));
			}
		});
		return boolBuilder.build();
	}

	private co.elastic.clients.elasticsearch._types.query_dsl.Query getQuery(Search.Field field) {
		co.elastic.clients.elasticsearch._types.query_dsl.Query.Builder builder = new Query.Builder();
		List<String> names = field.getNames();
		String value = field.getValue();
		Condition condition = field.getCondition();
		List<Search.Field> children = field.getChildren();
		if (CollectionUtil.isNotEmpty(children)) {
			builder.bool(getBoolQuery(children, new BoolQuery.Builder()));
		}
		switch (field.getType()) {
			case TERM -> builder.term(fn -> fn.field(names.getFirst()).value(value));
			case MATCH -> builder.match(fn -> fn.field(names.getFirst()).query(value));
			case MATCH_PHRASE -> builder.matchPhrase(fn -> fn.field(names.getFirst()).query(value));
			case QUERY_STRING -> builder.queryString(fn -> fn.fields(names).query(value));
			case RANGE -> builder.range(getRangeQuery(names, value, condition));
		}
		return builder.build();
	}

	private RangeQuery getRangeQuery(List<String> names, String value, Condition condition) {
		RangeQuery.Builder rangeQueryBuilder = new RangeQuery.Builder();
		switch (condition) {
			case GT -> rangeQueryBuilder.queryName(names.getFirst()).gt(JsonData.fromJson(value));
			case LT -> rangeQueryBuilder.queryName(names.getFirst()).lt(JsonData.fromJson(value));
			case GTE -> rangeQueryBuilder.queryName(names.getFirst()).gte(JsonData.fromJson(value));
			case LTE -> rangeQueryBuilder.queryName(names.getFirst()).lte(JsonData.fromJson(value));
		}
		return rangeQueryBuilder.build();
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
		}
	}

	private <TDocument> Document convert(String name, String alias, Class<TDocument> clazz) {
		boolean annotationPresent = clazz.isAnnotationPresent(Index.class);
		if (annotationPresent) {
			Index index = clazz.getAnnotation(Index.class);
			return Document.builder()
				.name(name)
				.alias(StringUtil.isNotEmpty(alias) ? alias : name)
				.mappings(getMappings(clazz))
				.setting(getSetting(index))
				.analysis(getAnalysis(index))
				.build();
		}
		throw new RuntimeException("Not found @Index");
	}

	private <S> Search convert(S obj) {
		Class<?> clazz = obj.getClass();
		boolean annotationPresent = clazz.isAnnotationPresent(Highlight.class);
		Search.Highlight h = null;
		if (annotationPresent) {
			Highlight highlight = clazz.getAnnotation(Highlight.class);
			h = getHighlight(highlight);
		}
		return new Search(h, getSearchFields(obj, clazz));
	}

	private <S> List<Search.Field> getSearchFields(S obj, Class<?> clazz) {
		Field[] fields = clazz.getDeclaredFields();
		List<Search.Field> list = Arrays.stream(fields)
			.filter(item -> item.isAnnotationPresent(SearchField.class))
			.map(item -> getSearchField(item, item.getAnnotation(SearchField.class), obj))
			.toList();
		if (CollectionUtils.isEmpty(list)) {
			throw new RuntimeException("@SearchField not found");
		}
		return list;
	}

	@SneakyThrows
	private <S> Search.Field getSearchField(Field field, SearchField searchField, S obj) {
		String[] names = searchField.names();
		// 允许访问私有属性
		field.setAccessible(true);
		String value = String.valueOf(field.get(obj));
		return new Search.Field(Arrays.asList(names), value, searchField.type(), searchField.query(),
				searchField.condition(), Collections.emptyList());
	}

	private Search.Highlight getHighlight(Highlight highlight) {
		return new Search.Highlight(Arrays.asList(highlight.preTags()), Arrays.asList(highlight.postTags()),
				highlight.requireFieldMatch(), getHighlightField(highlight.fields()));
	}

	private List<Search.HighlightField> getHighlightField(HighlightField[] fields) {
		return Arrays.stream(fields)
			.map(item -> new Search.HighlightField(item.name(), item.numberOfFragments(), item.fragmentSize()))
			.toList();
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
		return new Document.Mapping(value, type, searchAnalyzer, analyzer, fieldData, eagerGlobalOrdinals);
	}

}
