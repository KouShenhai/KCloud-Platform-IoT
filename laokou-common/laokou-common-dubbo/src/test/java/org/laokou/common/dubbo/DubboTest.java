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

package org.laokou.common.dubbo;

import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.dubbo.config.spring.context.annotation.DubboComponentScan;
import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestConstructor;

/**
 * @author laokou
 */
@Slf4j
@SpringBootTest
@EnableDubbo(scanBasePackages = { "org.laokou.common.dubbo" })
@DubboComponentScan(basePackages = "org.laokou.common.dubbo")
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
class DubboTest {

	@DubboReference
	private TestUserService testUserService;

	@Test
	void test() {
		String username = testUserService.getUsername();
		log.info("Dubbo调用服务，获取用户名:{}", username);
		Assertions.assertNotNull(username);
		Assertions.assertEquals("laokou", username);
	}

}
