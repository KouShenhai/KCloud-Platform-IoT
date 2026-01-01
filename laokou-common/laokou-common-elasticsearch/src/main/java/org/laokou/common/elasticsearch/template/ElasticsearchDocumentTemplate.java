/*
 * Copyright (c) 2022-2026 KCloud-Platform-IoT Author or Authors. All Rights Reserved.
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
import co.elastic.clients.elasticsearch.core.BulkResponse;
import co.elastic.clients.elasticsearch.core.DeleteResponse;
import co.elastic.clients.elasticsearch.core.GetResponse;
import co.elastic.clients.elasticsearch.core.IndexResponse;
import co.elastic.clients.elasticsearch.core.bulk.BulkOperation;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

/**
 * @author laokou
 */
@Slf4j
public record ElasticsearchDocumentTemplate(ElasticsearchClient elasticsearchClient,
		ElasticsearchAsyncClient elasticsearchAsyncClient) {

	public <T> void createDocument(String indexName, String id, T obj) throws IOException {
		IndexResponse response = elasticsearchClient
			.index(idx -> idx.index(indexName).id(id).refresh(Refresh.True).document(obj));
		ElasticsearchIndexTemplate.printLog(indexName, response.result().jsonValue());
	}

	public <T> CompletableFuture<Void> asyncCreateDocument(String indexName, String id, T obj, Executor executor) {
		return elasticsearchAsyncClient.index(idx -> idx.index(indexName).id(id).refresh(Refresh.True).document(obj))
			.thenAcceptAsync(response -> ElasticsearchIndexTemplate.printLog(indexName, response.result().jsonValue()),
					executor);
	}

	public <T> void bulkCreateDocuments(String indexName, List<T> list) {
		try {
			BulkResponse response = elasticsearchClient
				.bulk(bulk -> bulk.index(indexName).refresh(Refresh.True).operations(getBulkOperations(list)));
			ElasticsearchIndexTemplate.printLog(indexName,
					response.errors() ? "Sync Bulk create documents failed" : "Sync Bulk create documents succeeded");
		}
		catch (Throwable e) {
			log.error("index name: 【{}】 -> Sync Bulk create documents failed, error message: {}", indexName,
					e.getMessage());
		}
	}

	public <T> CompletableFuture<Void> asyncBulkCreateDocuments(String indexName, List<T> list, Executor executor) {
		return elasticsearchAsyncClient
			.bulk(bulk -> bulk.index(indexName).refresh(Refresh.True).operations(getBulkOperations(list)))
			.thenAcceptAsync(
					response -> ElasticsearchIndexTemplate.printLog(indexName, response.errors()
							? "Async Bulk create documents failed" : "Async Bulk create documents succeeded"),
					executor);
	}

	public void deleteDocument(String indexName, String id) throws IOException {
		DeleteResponse response = elasticsearchClient.delete(fn -> fn.index(indexName).refresh(Refresh.True).id(id));
		ElasticsearchIndexTemplate.printLog(indexName, response.result().jsonValue());
	}

	public CompletableFuture<Void> asyncDeleteDocument(String indexName, String id, Executor executor) {
		return elasticsearchAsyncClient.delete(fn -> fn.index(indexName).refresh(Refresh.True).id(id))
			.thenAcceptAsync(response -> ElasticsearchIndexTemplate.printLog(indexName, response.result().jsonValue()),
					executor);
	}

	public <T> T getDocument(String indexName, String id, Class<T> clazz) throws IOException {
		GetResponse<T> response = elasticsearchClient.get(fn -> fn.index(indexName).id(id), clazz);
		if (response.found()) {
			return response.source();
		}
		return null;
	}

	public <T> CompletableFuture<T> asyncGetDocument(String indexName, String id, Class<T> clazz, Executor executor) {
		return elasticsearchAsyncClient.get(fn -> fn.index(indexName).id(id), clazz).thenApplyAsync(response -> {
			if (response.found()) {
				return response.source();
			}
			return null;
		}, executor);
	}

	private <T> List<BulkOperation> getBulkOperations(List<T> list) {
		return list.stream().map(item -> BulkOperation.of(idx -> idx.index(fn -> fn.document(item)))).toList();
	}

}
