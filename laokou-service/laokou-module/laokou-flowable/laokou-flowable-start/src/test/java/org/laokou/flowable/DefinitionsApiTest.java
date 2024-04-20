/*
 * Copyright (c) 2022-2024 KCloud-Platform-Alibaba Author or Authors. All Rights Reserved.
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

package org.laokou.flowable;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.laokou.common.core.utils.JacksonUtil;
import org.laokou.common.i18n.utils.StringUtil;
import org.laokou.flowable.dto.definition.DefinitionListQry;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationService;
import org.springframework.test.context.TestConstructor;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.util.Assert;
import org.springframework.web.context.WebApplicationContext;

import java.nio.charset.StandardCharsets;

import static org.laokou.common.i18n.common.RequestHeaderConstant.AUTHORIZATION;

/**
 * @author laokou
 */
@Slf4j
@SpringBootTest
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
class DefinitionsApiTest extends CommonTest {

	private static final String API_PREFIX = "/v1/definitions/";

	DefinitionsApiTest(WebApplicationContext webApplicationContext,
			OAuth2AuthorizationService oAuth2AuthorizationService) {
		super(webApplicationContext, oAuth2AuthorizationService);
	}

	@Test
	@SneakyThrows
	void definitionsListTest() {
		String apiUrl = API_PREFIX + "list";
		MvcResult mvcResult = super.mockMvc
			.perform(MockMvcRequestBuilders.post(apiUrl)
				.accept(MediaType.APPLICATION_JSON)
				.contentType(MediaType.APPLICATION_JSON)
				.header(AUTHORIZATION, getToken())
				.content(JacksonUtil.toJsonStr(new DefinitionListQry())))
			.andExpect(MockMvcResultMatchers.status().isOk())
			.andDo(MockMvcResultHandlers.print())
			.andReturn();
		String body = mvcResult.getResponse().getContentAsString(StandardCharsets.UTF_8);
		Assert.isTrue(StringUtil.isNotEmpty(body), "response body is empty");
		log.info("返回值：{}", body);
	}

}
