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

	private static final String TOKEN = "eyJraWQiOiI3ZmMzYTM4Mi00NDkxLTRiMGMtYThiMC1lY2FmYWNmNWMxN2EiLCJhbGciOiJSUzI1NiJ9.eyJzdWIiOiIxZnRrQkpFSXZtT0Z1eEp5Z3Myam5RPT0iLCJhdWQiOiI5NVR4U3NUUEZBM3RGMTJUQlNNbVVWSzBkYSIsIm5iZiI6MTcxNjgyNTQ0MSwic2NvcGUiOlsicGFzc3dvcmQiLCJtYWlsIiwib3BlbmlkIiwibW9iaWxlIl0sImlzcyI6Imh0dHBzOi8vMTI3LjAuMC4xOjExMTEiLCJleHAiOjE3MTY4MjkwNDEsImlhdCI6MTcxNjgyNTQ0MSwianRpIjoiMTRhMDJjMDQtMTFjMC00ZDk5LWFlOTktN2Q1OTgzODhhODRlIn0.iDhDVRlx7jUT42TObyk7FwvTgXfDEwiofsSdbqdvRdsaGZ2W0noo0z4YzW1RXT2AF_qe78qdO8fx_rMk5_iHCSTN773b20EqdRGdETPuPV8zsOohqX3ksjJQvB63M26NV5jceOlA5tsgD1X0uddKSHVsUCdZHlyXtIA1pV86SBp3HVaxaY5P3jDwCq7Akp9A2TMaayeagxAhps07xKh_vtBjlwbq1YRBGwMql9eCRXAoJV6UwJzmyGPXA28ytDTrr73Aucol76QrwO68TY87TKHyytyUy9d1XU9-bnAfMil1hzIzwofIOXbUOUYElyd8eNk0ttpOKHTpSInjZJkUpQ";

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
