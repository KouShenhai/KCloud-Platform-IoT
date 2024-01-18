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

	private static final String TOKEN = "eyJraWQiOiJhMWUyNGQ1Ni02MWU0LTQ5MDMtYWZmZS02Y2EzNWY3M2YyZjAiLCJhbGciOiJSUzI1NiJ9.eyJzdWIiOiIxZnRrQkpFSXZtT0Z1eEp5Z3Myam5RPT0iLCJhdWQiOiI5NVR4U3NUUEZBM3RGMTJUQlNNbVVWSzBkYSIsIm5iZiI6MTcwMzQ5Mzk2MCwic2NvcGUiOlsicGFzc3dvcmQiLCJtYWlsIiwib3BlbmlkIiwibW9iaWxlIl0sImlzcyI6Imh0dHA6Ly8xMjcuMC4wLjE6MTExMSIsImV4cCI6MTcwMzQ5NzU2MCwiaWF0IjoxNzAzNDkzOTYwLCJqdGkiOiJjZjI1NjI2ZS0wYTQ4LTQ5MjktOTQ2MC01Y2I1YzNmODEzZmQifQ.TksT1G9ExFiAg7e4r93AbtTX3hzv2OypSR0EKSfXVkefQ-nP1_ERliUcwYs7ZPP1Edv6OXpwctQ_0OtY6gitdN90_EigCioeAuZbxGYFHHZWCi5l8Ik7Bc4FdOv3o2GLr3OXx_vrGmXnqbklil9fPzb6aibS7tBxmf-23KxH9Gndh3lAt3H_40cLviI-60dJ6QVqPQ3TAv-T7bEY9-sRo4W-HHYnRJ2mDfUXC6H8aC2n7z2uQu4n4bkPqRvfQdXHSGsYHSzAg8Tp8hSFxXlIWGSYzm5J-zovdiq4dXsqcsMwjAXK92BFlxeCAZzag3shOQqy2HxY7_8N0-MVB55bUg";

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
