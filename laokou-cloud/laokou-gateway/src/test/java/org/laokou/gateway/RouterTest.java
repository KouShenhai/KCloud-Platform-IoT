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

package org.laokou.gateway;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.laokou.gateway.repository.NacosRouteDefinitionRepository;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestConstructor;

/**
 * 同步Nacos路由配置到Redis.
 *
 * @author laokou
 */
@Slf4j
@SpringBootTest
@RequiredArgsConstructor
@AutoConfigureWebTestClient
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
class RouterTest {

	private final NacosRouteDefinitionRepository nacosRouteDefinitionRepository;

	@Test
	void testRouter() {
		// 删除路由
		nacosRouteDefinitionRepository.removeRouters().subscribe(delFlag -> {
			Assertions.assertNotNull(delFlag);
			if (delFlag) {
				log.info("删除路由成功");
			}
			else {
				log.error("删除路由失败");
			}
		});
		// 保存路由
		nacosRouteDefinitionRepository.saveRouters().subscribe(saveFlag -> {
			Assertions.assertNotNull(saveFlag);
			if (saveFlag) {
				log.info("保存路由成功");
			}
			else {
				log.error("保存路由失败");
			}
		});
	}

}
