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
class CommonTest {

	private static final String TOKEN = "eyJraWQiOiI5MDVkOGQyNi00MTAyLTRlM2UtODhmMy1hN2UwNjU5YmExYmIiLCJhbGciOiJSUzI1NiJ9.eyJzdWIiOiJLQnNubVNnNlMxTDFRNDNwREZmSUwvbnA5QzNCeWp2YStPcERnbnlHcGlRPSIsImF1ZCI6Ijk1VHhTc1RQRkEzdEYxMlRCU01tVVZLMGRhIiwibmJmIjoxNzE4Mjg0NTk4LCJzY29wZSI6WyJwYXNzd29yZCIsIm1haWwiLCJvcGVuaWQiLCJtb2JpbGUiXSwiaXNzIjoiaHR0cHM6Ly8xMjcuMC4wLjE6MTExMSIsImV4cCI6MTcxODI4ODE5OCwiaWF0IjoxNzE4Mjg0NTk4LCJqdGkiOiI3OGY3YjZiYy03NTYwLTQ0NDQtOWZiMi1jMGQ1M2Y3ZDQ5YTQifQ.sJRRdXuhdCUm92G2eBdTehCdtPnrhO2vFzOM0go0XRzspNmvOzMcN6DAl-wPy1-IcxksCckDOmwbcOmERs7xBl01hK3DkJdwc-GtejPPMZT8BDcOgL_5w-AHcV1uTr0bm1T8Tl4J9YaXwsYOKUgHdE4qRj_rchDkG1D4hnJrIYLsOeqFKOkRS7_FQuJIrG7HwXWcAV6jTYGQ8Zz9TIm1SimVv_zScnZqhkmA2VkhUBG2JVuJpHqi3brPTOADBdzpeBIL4RujYigEPo4zRUHBO-qyje3Moa3c17vnR3Dnen401PlAmt_w63PpWERh_jiQNdYdhi_pVN51JMOJcon26g";

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
