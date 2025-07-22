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

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.laokou.common.core.util.SpringUtils;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestConstructor;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author laokou
 */
@Slf4j
@SpringBootTest
@RequiredArgsConstructor
@ContextConfiguration(classes = { SpringUtils.class })
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
class SpringUtilsTest {

	private final SpringUtils springUtils;

	@Test
	void testGetServiceId() {
		assertThat(springUtils).isNotNull();
		String serviceId = springUtils.getServiceId();
		log.info("应用名称：{}", serviceId);
		assertThat(serviceId).isNotBlank().isEqualTo("laokou-common-core");
	}

}
