/*
 * Copyright (c) 2022-2024 KCloud-Platform-IOT Author or Authors. All Rights Reserved.
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

package org.laokou.admin;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.laokou.common.core.utils.JacksonUtil;
import org.laokou.common.nacos.utils.ServiceUtil;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationService;
import org.springframework.test.context.TestConstructor;
import org.springframework.web.context.WebApplicationContext;

import java.util.List;

/**
 * @author laokou
 */
@Slf4j
@SpringBootTest
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
class ClusterApiTest extends CommonTest {

	private final ServiceUtil serviceUtil;

	ClusterApiTest(WebApplicationContext webApplicationContext, OAuth2AuthorizationService oAuth2AuthorizationService,
			ServiceUtil serviceUtil) {
		super(webApplicationContext, oAuth2AuthorizationService);
		this.serviceUtil = serviceUtil;
	}

	@Test
	void serviceInstanceTest() {
		List<ServiceInstance> instances = serviceUtil.getInstances("laokou-admin");
		log.info("获取数据：{}", JacksonUtil.toJsonStr(instances));
	}

}
