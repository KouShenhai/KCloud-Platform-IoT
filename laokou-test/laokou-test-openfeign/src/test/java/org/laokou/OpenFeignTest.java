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

package org.laokou;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.laokou.common.nacos.annotation.EnableNacosShutDown;
import org.laokou.test.openfeign.OpenfeignTestApp;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.test.context.TestConstructor;

/**
 * @author laokou
 */
@Slf4j
@EnableNacosShutDown
@EnableFeignClients
@SpringBootTest(classes = OpenfeignTestApp.class)
@RequiredArgsConstructor
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
class OpenFeignTest {

	private final UserFeignClient userFeignClient;

	private final UserShardingFeignClient userShardingFeignClient;

	@Test
	void testOpenFeign() {
		log.info("1 => OpenFeign获取数据：{}", userFeignClient.getUsername());
		log.info("2 => OpenFeign获取数据：{}", userShardingFeignClient.list());
	}

}
