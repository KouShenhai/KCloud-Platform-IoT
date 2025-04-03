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

package org.laokou.admin;

import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.laokou.common.core.util.OkHttpUtils;
import org.laokou.common.i18n.util.JacksonUtils;
import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.server.authorization.OAuth2Authorization;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationService;
import org.springframework.test.context.TestConstructor;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.security.Principal;
import java.util.Map;
import static org.laokou.common.security.config.GlobalOpaqueTokenIntrospector.FULL;

/**
 * @author laokou
 */
@SpringBootTest
@RequiredArgsConstructor
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
class CommonTest {

	private final WebApplicationContext webApplicationContext;

	private final OAuth2AuthorizationService oAuth2AuthorizationService;

	protected MockMvc mockMvc;

	private final ServerProperties serverProperties;

	@BeforeEach
	void setUp() throws JsonProcessingException {
		mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
		OAuth2Authorization authorization = oAuth2AuthorizationService.findByToken(getToken(), FULL);
		Assertions.assertNotNull(authorization);
		UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = authorization
			.getAttribute(Principal.class.getName());
		SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
	}

	private String getToken() throws JsonProcessingException {
		Map<String, String> params = Map.of("username", "admin", "password", "admin123", "tenant_code", "laokou",
				"grant_type", "test");
		Map<String, String> headers = Map.of("Authorization",
				"Basic OTVUeFNzVFBGQTN0RjEyVEJTTW1VVkswZGE6RnBId0lmdzR3WTkyZE8=");
		String json = OkHttpUtils.doFormDataPost(getOAuthApiUrl(), params, headers);
		return JacksonUtils.readTree(json).get("access_token").asText();
	}

	private String getOAuthApiUrl() {
		return getSchema(disabledSsl()) + "auth:1111/oauth2/token";
	}

	private String getSchema(boolean disabled) {
		return disabled ? "https://" : "http://";
	}

	private boolean disabledSsl() {
		return serverProperties.getSsl().isEnabled();
	}

}
