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

import lombok.extern.slf4j.Slf4j;
import org.laokou.common.lock.support.IdentifierGenerator;
import org.laokou.logstash.common.config.LokiProperties;
import org.laokou.logstash.convertor.TraceLogConvertor;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Objects;

@Slf4j
@Component
public class TraceLogLokiStorage extends AbstractTraceLogStorage {

	private final WebClient webClient;

	private final LokiProperties lokiProperties;

	public TraceLogLokiStorage(IdentifierGenerator distributedIdentifierGenerator, WebClient webClient,
			LokiProperties lokiProperties) {
		super(distributedIdentifierGenerator);
		this.webClient = webClient;
		this.lokiProperties = lokiProperties;
	}

	@Override
	public Mono<Void> batchSave(Flux<String> messages) {
		return messages.collectList()
			.map(item -> item.stream().map(this::getTraceLogIndex).filter(Objects::nonNull).toList())
			.map(TraceLogConvertor::toDTO)
			.flatMap(item -> webClient.post()
				.uri(lokiProperties.getUrl())
				.accept(MediaType.APPLICATION_JSON)
				.bodyValue(item)
				.retrieve()
				.toBodilessEntity()
				.then())
			.onErrorResume(e -> {
				log.error("分布式链路写入失败，错误信息：{}", e.getMessage(), e);
				return Mono.error(e);
			});
	}

}
