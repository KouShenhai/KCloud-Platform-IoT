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

package org.laokou.common.redis.config;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.redisson.config.Config;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author laokou
 */
class NodeTypeTest {

	@Test
	void shouldNotConfigureBlankPasswordOrExternalEventLoopGroup() {
		SpringRedissonProperties properties = properties(" ");
		try (ExecutorService executor = Executors.newVirtualThreadPerTaskExecutor()) {
			Config config = properties.getConfig(executor);
			Assertions.assertThat(config.getPassword()).isNull();
			Assertions.assertThat(config.getEventLoopGroup()).isNull();
			Assertions.assertThat(config.getExecutor()).isSameAs(executor);
			Assertions.assertThat(config.getNettyExecutor()).isSameAs(executor);
		}
	}

	@Test
	void shouldConfigureNonBlankPassword() {
		SpringRedissonProperties properties = properties("password");
		try (ExecutorService executor = Executors.newVirtualThreadPerTaskExecutor()) {
			Config config = properties.getConfig(executor);
			Assertions.assertThat(config.getPassword()).isEqualTo("password");
		}
	}

	private SpringRedissonProperties properties(String password) {
		SpringRedissonProperties properties = new SpringRedissonProperties();
		SpringRedissonProperties.Single single = new SpringRedissonProperties.Single();
		single.setAddress("redis://localhost:6379");
		properties.setPassword(password);
		properties.getNode().setSingle(single);
		return properties;
	}

}
