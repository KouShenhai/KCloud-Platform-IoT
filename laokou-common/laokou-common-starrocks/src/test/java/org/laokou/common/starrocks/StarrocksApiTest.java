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

package org.laokou.common.starrocks;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.laokou.common.testcontainers.container.StarrocksContainer;
import org.laokou.common.testcontainers.util.DockerImageNames;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;

/**
 * @author laokou
 */
@TestConfiguration
@SpringBootConfiguration
class StarrocksApiTest {

	static final StarrocksContainer starrocks = new StarrocksContainer(DockerImageNames.starrocks())
			.withDatabase("test")
			.withPassword("laokou123")
			.withScriptPaths("init.sql");

	@BeforeAll
	static void beforeAll() {
		starrocks.start();
	}

	@AfterAll
	static void afterAll() {
		starrocks.stop();
	}


	@DynamicPropertySource
	static void configureProperties(DynamicPropertyRegistry registry) {
	}

	@Test
	void test() {

	}

}
