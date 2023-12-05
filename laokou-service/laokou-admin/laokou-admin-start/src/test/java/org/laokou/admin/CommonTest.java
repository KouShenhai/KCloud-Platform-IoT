/*
 * Copyright (c) 2022 KCloud-Platform-Alibaba Author or Authors. All Rights Reserved.
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

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.server.authorization.OAuth2Authorization;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationService;
import org.springframework.security.oauth2.server.authorization.OAuth2TokenType;
import org.springframework.test.context.TestConstructor;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.security.Principal;
import java.util.Objects;

/**
 * @author laokou
 */
@SpringBootTest
@RequiredArgsConstructor
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
public class CommonTest {

	private static final String TOKEN = "eyJraWQiOiJhZDExOTA2Zi0wYzdhLTQxN2QtYTEwMS1mNzQ3ZGE1NjM0OWYiLCJhbGciOiJSUzI1NiJ9.eyJzdWIiOiIxZnRrQkpFSXZtT0Z1eEp5Z3Myam5RPT0iLCJhdWQiOiI5NVR4U3NUUEZBM3RGMTJUQlNNbVVWSzBkYSIsIm5iZiI6MTcwMTY1OTE2MCwic2NvcGUiOlsicGFzc3dvcmQiLCJtYWlsIiwib3BlbmlkIiwibW9iaWxlIl0sImlzcyI6Imh0dHA6Ly8xMjcuMC4wLjE6MTExMSIsImV4cCI6MTcwMTY2Mjc2MCwiaWF0IjoxNzAxNjU5MTYwLCJqdGkiOiI3OGQ2ZDIzYy1iZDMzLTQxMzctYTgwNy1iYjNkNWQyZDQwNzQifQ.eysY_JakxikNyHgi5k5iMms0D-PNUXWPkYeEXunfAypuPLzPvJProsxahzvkx92CeFSOFTWuHAP0k9cAX67Cy247P0GDQx0_hbK3A_QGlWzZpxdu6xZmCiIVDt6iri-y67BEiIlvIP6xQgAmbNiV3fcDB5Bpr8FDfaV0p3pmDPfNcY_KLjvxuyjT0hssI2JPM9hEKsPqvLN8rcS6cET4abtHuKBhaoh7wCMeDU94oUjJFRHsqYGDfS9E8-w2ZQR1XZ8BlUY6qVETnoJTq18zoVnFVHNevqCHnx-R2aRkTIHeT27t5jPJcm8sJYYB8lna52p6x723VldtkY3X1KBmVA";

	private final WebApplicationContext webApplicationContext;

	private final OAuth2AuthorizationService oAuth2AuthorizationService;

	protected MockMvc mockMvc;

	@BeforeEach
	public void setUp() {
		mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
		OAuth2Authorization authorization = oAuth2AuthorizationService.findByToken(TOKEN, OAuth2TokenType.ACCESS_TOKEN);
		assert authorization != null;
		UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = Objects
			.requireNonNull(authorization.getAttribute(Principal.class.getName()));
		SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
	}

	protected String getToken() {
		return "Bearer " + TOKEN;
	}

}
