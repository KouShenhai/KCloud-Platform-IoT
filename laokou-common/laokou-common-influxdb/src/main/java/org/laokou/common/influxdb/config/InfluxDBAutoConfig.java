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

package org.laokou.common.influxdb.config;

import com.influxdb.client.InfluxDBClient;
import com.influxdb.client.InfluxDBClientFactory;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;

/**
 * @author laokou
 */
@AutoConfiguration
public class InfluxDBAutoConfig {

	@Bean(destroyMethod = "close")
	@ConditionalOnProperty(prefix = "spring.influx-db", matchIfMissing = true, name = "type", havingValue = "TOKEN")
	public InfluxDBClient tokenInfluxDBClient(SpringInfluxDBProperties springInfluxDBProperties) {
		return InfluxDBClientFactory.create(springInfluxDBProperties.getUrl(),
				springInfluxDBProperties.getToken().toCharArray(), springInfluxDBProperties.getOrg(),
				springInfluxDBProperties.getBucket());
	}

	@Bean(destroyMethod = "close")
	@ConditionalOnProperty(prefix = "spring.influx-db", matchIfMissing = true, name = "type",
			havingValue = "USERNAME_PASSWORD")
	public InfluxDBClient usernamePasswordInfluxDBClient(SpringInfluxDBProperties springInfluxDBProperties) {
		return InfluxDBClientFactory.create(springInfluxDBProperties.getUrl(), springInfluxDBProperties.getUsername(),
				springInfluxDBProperties.getPassword().toCharArray());
	}

}
