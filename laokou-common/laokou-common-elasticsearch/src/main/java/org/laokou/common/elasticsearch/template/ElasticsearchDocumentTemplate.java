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
import co.elastic.clients.elasticsearch.core.BulkResponse;
import co.elastic.clients.elasticsearch.core.DeleteResponse;
import co.elastic.clients.elasticsearch.core.IndexResponse;
import co.elastic.clients.elasticsearch.core.bulk.BulkOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

/**
 * @author laokou
 */
@Slf4j
@RequiredArgsConstructor
public final class ElasticsearchDocumentTemplate {

	private final ElasticsearchClient elasticsearchClient;

	private final ElasticsearchAsyncClient elasticsearchAsyncClient;

	public <T> void createDocument(String indexName, T obj) throws IOException {
		IndexResponse response = elasticsearchClient.index(idx -> idx.index(indexName).refresh(Refresh.WaitFor).document(obj));
		ElasticsearchIndexTemplate.printLog(indexName, response.result().jsonValue());
	}

	public <T> CompletableFuture<Void> asyncCreateDocument(String indexName, T obj) {
		return elasticsearchAsyncClient.index(idx -> idx.index(indexName).refresh(Refresh.WaitFor).document(obj))
			.thenAcceptAsync(response -> ElasticsearchIndexTemplate.printLog(indexName, response.result().jsonValue()));
	}

	public <T> void bulkCreateDocument(String indexName, List<T> list) {
		try {
			BulkResponse response = elasticsearchClient
				.bulk(bulk -> bulk.index(indexName).refresh(Refresh.WaitFor).operations(getBulkOperations(list)));
			ElasticsearchIndexTemplate.printLog(indexName, response.errors() ? "批量创建文档失败" : "批量创建文档成功");
		}
		catch (Throwable e) {
			log.error("索引：【{}】 -> 批量创建文档失败，错误信息：{}", indexName, e.getMessage());
		}
	}

	public <T> CompletableFuture<Void> asyncBulkCreateDocument(String indexName, List<T> list, Executor executor) {
		return elasticsearchAsyncClient
			.bulk(bulk -> bulk.index(indexName).refresh(Refresh.WaitFor).operations(getBulkOperations(list)))
			.thenAcceptAsync(response -> ElasticsearchIndexTemplate.printLog(indexName, response.errors() ? "异步批量创建文档成功" : "异步批量创建文档失败"), executor);
	}

	public void deleteDocument(String indexName, String id) throws IOException {
		DeleteResponse response = elasticsearchClient.delete(fn -> fn.index(indexName).id(id).refresh(Refresh.WaitFor));
		ElasticsearchIndexTemplate.printLog(indexName, response.result().jsonValue());
	}

	public CompletableFuture<Void> asyncDeleteDocument(String indexName, String id) {
		return elasticsearchAsyncClient.delete(fn -> fn.index(indexName).id(id).refresh(Refresh.WaitFor))
			.thenAcceptAsync(response -> ElasticsearchIndexTemplate.printLog(indexName, response.result().jsonValue()));
	}

	private <T> List<BulkOperation> getBulkOperations(List<T> list) {
		return list.stream().map(item -> BulkOperation.of(idx -> idx.index(fn -> fn.document(item)))).toList();
	}

}
