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

	private static final String TOKEN = "eyJraWQiOiIzYzBhOTQxMy0yNWFhLTRlOTAtODQzMi05YjE4M2YxYTRiMTAiLCJhbGciOiJSUzI1NiJ9.eyJzdWIiOiJhZG1pbiIsImF1ZCI6Ijk1VHhTc1RQRkEzdEYxMlRCU01tVVZLMGRhIiwibmJmIjoxNzA4MjM1NDEyLCJzY29wZSI6WyJwYXNzd29yZCIsIm1haWwiLCJvcGVuaWQiLCJtb2JpbGUiXSwiaXNzIjoiaHR0cDovLzEyNy4wLjAuMToxMTExIiwiZXhwIjoxNzA4MjM5MDEyLCJpYXQiOjE3MDgyMzU0MTIsImp0aSI6IjczYTI3ZDBhLWZlMTMtNDI5ZS04MmJhLTE5ZWUyYzFjMDBmZCJ9.G6WG7Em7MrBFmlJHcmhGzihQFSAujj9HztQREw_7HEEXRu39jDq7c6b9a3nxaN2Kz4kVSYUwBAVNtwbZwYfQwRLrn1_KXs0L6oJKtjLsdMIY-zzetNGr74OzPYNGx0txblWjR42jAs8yy6Kma5-DRrIq1c8DSHkEHeq-WZVma9RaooFEwqfrJNUJOP2WyZBuZHkGJKmh3U7iMeGCn7ulUST_saC5nyZl4ZtgBLtH-PyoTIRx3ssFQQDHGdausaQ2xAETy-vl79EerMd32QGtQTpcDYNhNbhBZVGnIiBzUqL93N4agFKfiGycyZUruEdXh6dubRW4soms1ddkuNnHKg";

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
