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
import org.laokou.logstash.common.config.LokiProperties;
import org.laokou.logstash.convertor.TraceLogConvertor;
import org.laokou.logstash.gatewayimpl.database.dataobject.TraceLogIndex;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import java.util.List;
import java.util.Objects;

@Slf4j
@Component
public class TraceLogLokiStorage extends AbstractTraceLogStorage {

	private final RestClient restClient;

	private final LokiProperties lokiProperties;

	public TraceLogLokiStorage(RestClient restClient, LokiProperties lokiProperties) {
		this.restClient = restClient;
		this.lokiProperties = lokiProperties;
	}

	@Override
	public void batchSave(List<Object> messages) {
		List<TraceLogIndex> list = messages.stream().map(this::getTraceLogIndex).filter(Objects::nonNull).toList();
		restClient.post()
			.uri(lokiProperties.getUrl())
			.accept(MediaType.APPLICATION_JSON)
			.body(TraceLogConvertor.toDTO(list))
			.retrieve()
			.toBodilessEntity();
	}
}
