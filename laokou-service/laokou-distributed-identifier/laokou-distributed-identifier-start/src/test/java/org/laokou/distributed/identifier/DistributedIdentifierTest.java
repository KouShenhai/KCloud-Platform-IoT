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

package org.laokou.distributed.identifier;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.laokou.common.i18n.util.JacksonUtils;
import org.laokou.common.secret.aop.ApiSecretAop;
import org.laokou.common.secret.util.SecretUtils;
import org.laokou.distributed.identifier.config.SnowflakeGenerator;
import org.laokou.distributed.identifier.dto.clientobject.DistributedIdentifierCO;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestConstructor;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.time.Instant;

import static org.laokou.common.i18n.common.constant.StringConstants.EMPTY;
import static org.laokou.common.secret.util.SecretUtils.APP_KEY;
import static org.laokou.common.secret.util.SecretUtils.APP_SECRET;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author laokou
 */
@SpringBootTest
@ExtendWith(MockitoExtension.class)
@RequiredArgsConstructor
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
class DistributedIdentifierTest {

	private final WebApplicationContext webApplicationContext;

	private final SnowflakeGenerator snowflakeGenerator;

	private MockMvc mockMvc;

	@BeforeEach
	void setUp() {
		mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
	}

	@Test
	void test() throws Exception {
		String nonce = "test";
		long timestamp = System.currentTimeMillis();
		String sign = SecretUtils.sign(APP_KEY, APP_SECRET, nonce, timestamp, EMPTY);
		String contentAsString = mockMvc
			.perform(post("/v3/distributed-identifiers/snowflake").header(ApiSecretAop.APP_KEY, APP_KEY)
				.header(ApiSecretAop.APP_SECRET, APP_SECRET)
				.header(ApiSecretAop.NONCE, nonce)
				.header(ApiSecretAop.TIMESTAMP, timestamp)
				.header(ApiSecretAop.SIGN, sign))
			.andExpect(status().isOk())
			.andReturn()
			.getResponse()
			.getContentAsString();
		JsonNode jsonNode = JacksonUtils.readTree(contentAsString);
		String data = jsonNode.get("data").toString();
		DistributedIdentifierCO co = JacksonUtils.toBean(data, DistributedIdentifierCO.class);
		Assertions.assertNotNull(co);
		Assertions.assertNotNull(co.getId());
		Assertions.assertNotNull(co.getTime());
		Instant instant = snowflakeGenerator.getInstant(co.getId());
		Assertions.assertEquals(co.getTime(), instant);
	}

}
