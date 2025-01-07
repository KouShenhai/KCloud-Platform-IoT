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

package org.laokou.logstash.common.support;

import lombok.RequiredArgsConstructor;
import org.laokou.common.core.utils.MapUtil;
import org.laokou.common.core.utils.ThreadUtil;
import org.laokou.common.elasticsearch.template.ElasticsearchTemplate;
import org.laokou.logstash.gateway.database.dataobject.TraceLogIndex;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class TraceLogElasticsearchStorage extends AbstractTraceLogStorage {

	private final ElasticsearchTemplate elasticsearchTemplate;

	@Override
	public CompletableFuture<Void> batchSave(List<String> messages) {
		Map<String, Object> dataMap = messages.parallelStream()
			.map(this::getTraceLogIndex)
			.filter(Objects::nonNull)
			.collect(Collectors.toMap(TraceLogIndex::getId, traceLogIndex -> traceLogIndex));
		if (MapUtil.isNotEmpty(dataMap)) {
			try (ExecutorService executor = ThreadUtil.newVirtualTaskExecutor()) {
				return elasticsearchTemplate
					.asyncCreateIndex(getIndexName(), TRACE_INDEX, TraceLogIndex.class, executor)
					.thenAcceptAsync(
							res -> elasticsearchTemplate.asyncBulkCreateDocument(getIndexName(), dataMap, executor),
							executor);
			}
		}
		return null;
	}

	@Override
	public CompletableFuture<Void> save(String obj) {
		throw new UnsupportedOperationException();
	}

}
