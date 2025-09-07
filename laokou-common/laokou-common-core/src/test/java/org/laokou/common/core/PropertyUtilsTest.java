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

package org.laokou.common.core;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.laokou.common.core.util.PropertyUtils;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.stereotype.Component;
import org.springframework.test.context.TestConstructor;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author laokou
 */
@TestConfiguration
@SpringBootConfiguration
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@RequiredArgsConstructor
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
class PropertyUtilsTest {

	@Test
	void test_bindProperties() throws IOException {
		SpringDisruptorProperties properties = PropertyUtils.bindOrCreate("spring.disruptor",
				SpringDisruptorProperties.class, "application.yml", "yaml");
		assertThat(properties).isNotNull();
		assertThat(properties.getBufferSize()).isEqualTo(1024);
	}

	@Data
	@Component
	@ConfigurationProperties(prefix = "spring.disruptor")
	static class SpringDisruptorProperties {

		private int bufferSize = 1024;

	}

}
