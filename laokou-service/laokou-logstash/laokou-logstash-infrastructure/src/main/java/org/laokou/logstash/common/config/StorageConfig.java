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

package org.laokou.logstash.common.config;

import org.laokou.common.elasticsearch.template.ElasticsearchTemplate;
import org.laokou.logstash.common.support.TraceLogElasticsearchStorage;
import org.laokou.logstash.common.support.TraceLogLokiStorage;
import org.laokou.logstash.common.support.TraceLogStorage;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

/**
 * @author laokou
 */
@Component
public class StorageConfig {

	@Bean("traceLogStorage")
	@ConditionalOnProperty(prefix = "storage", matchIfMissing = true, name = "type", havingValue = "ELASTICSEARCH")
	public TraceLogStorage traceLogElasticsearchStorage(ElasticsearchTemplate elasticsearchTemplate) {
		return new TraceLogElasticsearchStorage(elasticsearchTemplate);
	}

	@Bean("traceLogStorage")
	@ConditionalOnProperty(prefix = "storage", matchIfMissing = true, name = "type", havingValue = "LOKI")
	public TraceLogStorage traceLogLokiStorage(RestClient restClient, LokiProperties lokiProperties) {
		return new TraceLogLokiStorage(restClient, lokiProperties);
	}

}
