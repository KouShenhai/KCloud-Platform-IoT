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

package org.laokou.common.id.generator.snowflake;

import com.alibaba.cloud.nacos.NacosConfigManager;
import com.alibaba.cloud.nacos.NacosConfigProperties;
import com.alibaba.cloud.nacos.NacosServiceManager;
import com.alibaba.nacos.api.naming.NamingService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.laokou.common.i18n.common.enums.BizType;
import org.laokou.common.nacos.util.NamingUtils;
import org.laokou.common.testcontainers.container.NacosContainer;
import org.laokou.common.testcontainers.util.DockerImageNames;
import org.mockito.Mockito;
import org.springframework.core.env.Environment;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.time.Instant;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Integration tests for {@link NacosSnowflakeIdGenerator} using Testcontainers.
 *
 * @author laokou
 */
@Testcontainers
class NacosSnowflakeIdGeneratorTest {

	@Container
	static final NacosContainer nacos = new NacosContainer(DockerImageNames.nacos(), 28848, 29848);

	private NacosSnowflakeIdGenerator generator;

	@BeforeEach
	void setUp() throws Exception {
		// Create a real NamingService pointing to the Nacos Testcontainer
		NamingService namingService = NamingUtils.createNamingService(nacos.getServerAddr());
		Assertions.assertThat(namingService.getServerStatus()).isEqualTo("UP");

		// Mock NacosConfigManager
		NacosConfigManager nacosConfigManager = Mockito.mock(NacosConfigManager.class);
		NacosConfigProperties nacosConfigProperties = Mockito.mock(NacosConfigProperties.class);
		Mockito.when(nacosConfigManager.getNacosConfigProperties()).thenReturn(nacosConfigProperties);
		Mockito.when(nacosConfigProperties.getGroup()).thenReturn("DEFAULT_GROUP");

		// Mock NacosServiceManager
		NacosServiceManager nacosServiceManager = Mockito.mock(NacosServiceManager.class);
		Mockito.when(nacosServiceManager.getNamingService()).thenReturn(namingService);

		// Mock Environment
		Environment environment = Mockito.mock(Environment.class);
		Mockito.when(environment.getProperty("server.port", System.getProperty("server.port", "9094")))
			.thenReturn("9094");
		Mockito
			.when(environment.getProperty("spring.grpc.server.port",
					System.getProperty("spring.grpc.server.port", "10111")))
			.thenReturn("10111");
		Mockito
			.when(environment.getProperty("spring.application.name",
					System.getProperty("spring.application.name", "laokou-distributed-id-snowflake")))
			.thenReturn("id-generator-test");
		Mockito
			.when(environment.getProperty("spring.cloud.nacos.discovery.cluster-name",
					System.getProperty("spring.cloud.nacos.discovery.cluster-name", "iot-cluster")))
			.thenReturn("iot-cluster");

		// Configure start timestamp
		SpringSnowflakeProperties springSnowflakeProperties = new SpringSnowflakeProperties();
		springSnowflakeProperties.setStartTimestamp(1609459200000L); // 2021-01-01
																		// 00:00:00 UTC

		generator = new NacosSnowflakeIdGenerator(nacosConfigManager, nacosServiceManager, springSnowflakeProperties,
				environment);
		generator.init();
	}

	@AfterEach
	void tearDown() throws Exception {
		if (generator != null) {
			generator.close();
		}
	}

	@Test
	@DisplayName("Test init success - allocate datacenterId and machineId")
	void test_init_success() {
		// init already called in setUp, verify generator can generate IDs (i.e.
		// initialization succeeded)
		Assertions.assertThatNoException().isThrownBy(() -> generator.nextId(BizType.AUTH));
	}

	@Test
	@DisplayName("Test nextId generates a unique positive snowflake ID")
	void test_nextId_generateUniqueId() {
		long id = generator.nextId(BizType.AUTH);
		Assertions.assertThat(id).isPositive();
	}

	@Test
	@DisplayName("Test nextId generates unique IDs consecutively")
	void test_nextId_uniqueness() {
		Set<Long> ids = new HashSet<>();
		for (int i = 0; i < 1000; i++) {
			long id = generator.nextId(BizType.AUTH);
			Assertions.assertThat(ids.add(id)).isTrue();
		}
		Assertions.assertThat(ids).hasSize(1000);
	}

	@Test
	@DisplayName("Test nextIds generates a batch of unique IDs")
	void test_nextIds_batch() {
		int batchSize = 100;
		List<Long> ids = generator.nextIds(BizType.AUTH, batchSize);
		Assertions.assertThat(ids).hasSize(batchSize);
		// All IDs should be positive
		Assertions.assertThat(ids).allMatch(id -> id > 0);
		// All IDs should be unique
		Set<Long> uniqueIds = new HashSet<>(ids);
		Assertions.assertThat(uniqueIds).hasSize(batchSize);
	}

	@Test
	@DisplayName("Test nextId throws IllegalStateException when not initialized")
	void test_nextId_notInitialized_throwsException() throws Exception {
		// Close then call
		generator.close();
		Assertions.assertThatThrownBy(() -> generator.nextId(BizType.AUTH))
			.isInstanceOf(IllegalStateException.class)
			.hasMessageContaining("not initialized");
	}

	@Test
	@DisplayName("Test getDatacenterId extracts correct value from snowflake ID")
	void test_getDatacenterId_fromSnowflakeId() {
		long id = generator.nextId(BizType.AUTH);
		long datacenterId = generator.getDatacenterId(id);
		// datacenterId should be in the range [0, 31]
		Assertions.assertThat(datacenterId).isBetween(0L, 31L);
	}

	@Test
	@DisplayName("Test getWorkerId extracts correct value from snowflake ID")
	void test_getWorkerId_fromSnowflakeId() {
		long id = generator.nextId(BizType.AUTH);
		long workerId = generator.getWorkerId(id);
		// workerId should be in the range [0, 31]
		Assertions.assertThat(workerId).isBetween(0L, 31L);
	}

	@Test
	@DisplayName("Test getSequence extracts correct value from snowflake ID")
	void test_getSequence_fromSnowflakeId() {
		long id = generator.nextId(BizType.AUTH);
		long sequence = generator.getSequence(id);
		// Sequence should be in the range [0, 8191]
		Assertions.assertThat(sequence).isBetween(0L, 8191L);
	}

	@Test
	@DisplayName("Test getInstant extracts generation time from snowflake ID")
	void test_getInstant_fromSnowflakeId() {
		Instant before = Instant.now();
		long id = generator.nextId(BizType.AUTH);
		Instant after = Instant.now();

		Instant idInstant = generator.getInstant(id);
		Assertions.assertThat(idInstant).isNotNull();
		// Tolerance within 1 second
		Assertions.assertThat(idInstant).isBetween(before.minusSeconds(1), after.plusSeconds(1));
	}

	@Test
	@DisplayName("Test close resets state")
	void test_close() throws Exception {
		// Normal generation
		long id = generator.nextId(BizType.AUTH);
		Assertions.assertThat(id).isPositive();

		// Close
		generator.close();

		// Should throw exception after close
		Assertions.assertThatThrownBy(() -> generator.nextId(BizType.AUTH)).isInstanceOf(IllegalStateException.class);
	}

}
