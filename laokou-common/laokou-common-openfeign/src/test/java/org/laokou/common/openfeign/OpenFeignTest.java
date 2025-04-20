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

package org.laokou.common.openfeign;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.laokou.common.i18n.util.ObjectUtils;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.test.context.TestConstructor;

/**
 * @author laokou
 */
@SpringBootTest
@RequiredArgsConstructor
@EnableFeignClients(basePackages = { "org.laokou.common.openfeign" })
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
class OpenFeignTest {

	private final TestUserFeignClient testUserFeignClient;

	@Test
	void test() {
		Assertions.assertNotNull(testUserFeignClient);
		TestUser user = testUserFeignClient.getUser();
		if (ObjectUtils.isNotNull(user)) {
			Assertions.assertEquals("laokou", user.getUsername());
			Assertions.assertEquals(1L, user.getId());
		}
		else {
			Assertions.fail("OpenFeign调用失败，请启动AppTest服务");
		}
	}

}
