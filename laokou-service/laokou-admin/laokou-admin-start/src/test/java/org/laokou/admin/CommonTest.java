/*
 * Copyright (c) 2022-2024 KCloud-Platform-IoT Author or Authors. All Rights Reserved.
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

	private static final String TOKEN = "eyJraWQiOiI2MTIyYjcyOC0xOTMxLTQ3NWMtYjMyMS0yYjdmYmVjMGQ0OTEiLCJhbGciOiJSUzI1NiJ9.eyJzdWIiOiJhZG1pbiIsImF1ZCI6Ijk1VHhTc1RQRkEzdEYxMlRCU01tVVZLMGRhIiwibmJmIjoxNzEwMzEzMTEyLCJzY29wZSI6WyJwYXNzd29yZCIsIm1haWwiLCJvcGVuaWQiLCJtb2JpbGUiXSwiaXNzIjoiaHR0cDovLzEyNy4wLjAuMTo1NTU1L2F1dGgiLCJleHAiOjE3MTAzMTY3MTIsImlhdCI6MTcxMDMxMzExMiwianRpIjoiZjRlYWU1YjctOWQzNy00NTM1LWEyODgtNWFjNWEwNzc2MjU1In0.Sg4LYn6hoYKB3vDM4NnFfDd3MBxpu-Bja-iYTNDDVBTkDMPjWXdbSTpupplud5aQ-mwRMhSuMF_ctzMFT5So1VckhNV8dg35DhKsRzEYfLaya_vk4eiFUaSU8ibfSPSEACa524L01SHb8wgb04LnvVAuJnPEzDZNRZxwHKbxA0irqwCafuTax8EFKGxHskHsxeuaaCvQdGLKSbYCdC3tHA85SIUKdsnm8fSS4_5El9gztbFUxDHZWRgagN_fHRqyDSd32PCulPeG3uOut-uUwC2Dv4xodLuaCYEouyn0aMY_juz2uHkpf1MnLh74caeE30lmbqBF5tv2ErOsqdMIaw";

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

}
