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
import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import co.elastic.clients.elasticsearch.core.SearchRequest;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import co.elastic.clients.elasticsearch.core.search.HighlightField;
import co.elastic.clients.elasticsearch.core.search.Hit;
import co.elastic.clients.elasticsearch.core.search.HitsMetadata;
import co.elastic.clients.util.NamedValue;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.laokou.common.elasticsearch.entity.Search;
import org.laokou.common.i18n.dto.Page;
import org.laokou.common.i18n.util.ObjectUtils;

import java.io.IOException;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

/**
 * @author laokou
 */
@Slf4j
@RequiredArgsConstructor
public final class ElasticsearchSearchTemplate {

	private final ElasticsearchClient elasticsearchClient;

	private final ElasticsearchAsyncClient elasticsearchAsyncClient;

	public <S> Page<S> search(List<String> names, Search search, Class<S> clazz) throws IOException {
		SearchRequest searchRequest = getSearchRequest(names, search);
		SearchResponse<S> response = elasticsearchClient.search(searchRequest, clazz);
		HitsMetadata<S> hits = response.hits();
		assert hits.total() != null;
		return Page.create(hits.hits().stream().map(Hit::source).toList(), hits.total().value());
	}

	public <S> CompletableFuture<Page<S>> asyncSearch(List<String> names, Search search, Class<S> clazz) {
		SearchRequest searchRequest = getSearchRequest(names, search);
		return elasticsearchAsyncClient.search(searchRequest, clazz).thenApplyAsync(response -> {
			HitsMetadata<S> hits = response.hits();
			assert hits.total() != null;
			return Page.create(hits.hits().stream().map(Hit::source).toList(), hits.total().value());
		});
	}

	private SearchRequest getSearchRequest(List<String> indexNames, Search search) {
		// 查询条件
		Integer pageNo = search.getPageNo();
		Integer pageSize = search.getPageSize();
		Query query = search.getQuery();
		SearchRequest.Builder builder = new SearchRequest.Builder();
		builder.index(indexNames);
		builder.from((pageNo - 1) * pageSize);
		builder.size(pageSize);
		// 获取真实总数
		builder.trackTotalHits(fn -> fn.enabled(true));
		// 追踪分数开启
		// builder.trackScores(true);
		// 注解
		// builder.explain(true);
		// 匹配度倒排，数值越大匹配度越高
		// builder.sort(fn -> fn.score(s -> s.order(SortOrder.Desc)));
		Search.Highlight highlight = search.getHighlight();
		if (ObjectUtils.isNotNull(highlight)) {
			builder.highlight(getHighlight(highlight));
		}
		// bool查询 => 布尔查询，允许组合多个查询条件
		// must查询类似and查询
		// must_not查询类似not查询
		// should查询类似or查询
		// query_string查询text类型字段，不需要连续，顺序还可以调换（分词）
		// match_phrase查询text类型字段，顺序必须相同，而且必须都是连续的（分词）
		// term精准匹配
		// match模糊匹配（分词）
		if (ObjectUtils.isNotNull(query)) {
			builder.query(query);
		}
		return builder.build();
	}

	private co.elastic.clients.elasticsearch.core.search.Highlight getHighlight(Search.Highlight highlight) {
		co.elastic.clients.elasticsearch.core.search.Highlight.Builder builder = new co.elastic.clients.elasticsearch.core.search.Highlight.Builder();
		builder.preTags(highlight.getPreTags());
		builder.postTags(highlight.getPostTags());
		// 多个字段高亮，需要设置false
		builder.requireFieldMatch(highlight.isRequireFieldMatch());
		builder.fields(getHighlightFields(highlight.getFields()));
		return builder.build();
	}

	private List<NamedValue<HighlightField>> getHighlightFields(Set<Search.HighlightField> fields) {
		return fields.stream()
			.map(item -> NamedValue.of(item.getName(), new co.elastic.clients.elasticsearch.core.search.HighlightField.Builder()
				// fragmentSize => 最大高亮分片数
				.fragmentSize(item.getFragmentSize())
				// numberOfFragments => 获取高亮片段位置
				.numberOfFragments(item.getNumberOfFragments())
				.build()))
			.toList();
	}

}
