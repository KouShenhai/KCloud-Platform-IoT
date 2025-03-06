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

package org.laokou.logstash.common.support;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.laokou.common.core.utils.MapUtil;
import org.laokou.common.elasticsearch.template.ElasticsearchTemplate;
import org.laokou.logstash.gatewayimpl.database.dataobject.TraceLogIndex;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.function.Function;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
public class TraceLogElasticsearchStorage extends AbstractTraceLogStorage {

	private final ExecutorService virtualThreadExecutor;

	private final ElasticsearchTemplate elasticsearchTemplate;

	@Override
	public Mono<Void> batchSave(Flux<String> messages) {
		return messages.collectList().flatMap(item -> {
			Map<String, TraceLogIndex> dataMap = item.stream()
				.map(this::getTraceLogIndex)
				.filter(Objects::nonNull)
				.collect(Collectors.toMap(TraceLogIndex::getId, Function.identity(),
						(existing, replacement) -> existing));
			if (MapUtil.isEmpty(dataMap)) {
				return Mono.empty();
			}
			return Mono.fromFuture(elasticsearchTemplate
				.asyncCreateIndex(getIndexName(), TRACE_INDEX, TraceLogIndex.class, virtualThreadExecutor)
				.thenComposeAsync(result -> elasticsearchTemplate.asyncBulkCreateDocument(getIndexName(), dataMap,
						virtualThreadExecutor), virtualThreadExecutor));
		}).onErrorResume(e -> {
			log.error("分布式链路写入失败，错误信息：{}", e.getMessage(), e);
			return Mono.empty();
		});
	}

}
