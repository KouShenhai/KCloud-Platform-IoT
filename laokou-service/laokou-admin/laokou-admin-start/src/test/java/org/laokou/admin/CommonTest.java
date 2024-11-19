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
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
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

import static org.laokou.common.security.config.GlobalOpaqueTokenIntrospector.FULL;

/**
 * @author laokou
 */
@SpringBootTest
@RequiredArgsConstructor
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
class CommonTest {

	private static final String TOKEN = "eyJraWQiOiI5ZmJhYjBhMi00ODEzLTRkMDUtYmY4OS05MzQzZTVmZDdlNzgiLCJhbGciOiJSUzI1NiJ9.eyJzdWIiOiJZbGg0UVRGMFltZEVXV0pSaW1GTVBHYUptbGR5dVdJYjlCTm1VTjFVTE1JNyIsImF1ZCI6Ijk1VHhTc1RQRkEzdEYxMlRCU01tVVZLMGRhIiwibmJmIjoxNzMyMDIwNzUwLCJzY29wZSI6WyJyZWFkIiwid3JpdGUiXSwiaXNzIjoiaHR0cHM6Ly9nYXRld2F5OjU1NTUvYXV0aCIsImV4cCI6MTczMjAyNDM1MCwiaWF0IjoxNzMyMDIwNzUwLCJqdGkiOiJjNWZmMTc0Ny0wMWJmLTQ2ZDYtOGI4Zi02MDBhNDE2ZmZmMWQifQ.jcfqP27WgVzo4M2ryZ5G1QOSWFYmWFaJntHulgMLowUUXMIiODicUMd5mrB7GjBKV_hYiVyz2Ls15Q_6--IQhlG9g96a9lw6ZsvSIpb4wr7zSodtwYjny_03BAjm_1GwngfKHZTfGR2VoAGjaWdNKWiRTyKWAmzcUPp7G4oovYQ5pYf-xHyVXaI2Wnd-GihpOQooIbFvlqZMAMIVT1yfv496UYpMUJnQnhAZaGaqocImjzclfCW5X69agnncH4tjacR4k-IOz3swTasAU53F0D-eaJC8ynOmnq-OcQi21sWyPP09Ju1n-_KzJMt94pT8iNun1GxZFmKuxBnBpCdPDQ";

	private final WebApplicationContext webApplicationContext;

	private final OAuth2AuthorizationService oAuth2AuthorizationService;

	protected MockMvc mockMvc;

	@BeforeEach
	public void setUp() {
		mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
		OAuth2Authorization authorization = oAuth2AuthorizationService.findByToken(TOKEN, FULL);
		Assertions.assertNotNull(authorization);
		UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = authorization
			.getAttribute(Principal.class.getName());
		SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
	}

}
