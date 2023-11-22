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

	private static final String TOKEN = "eyJraWQiOiIzZGRlY2U2MC1jMDYzLTRmMjItYjNkNS1kMTE5NTdmMjVmZWIiLCJhbGciOiJSUzI1NiJ9.eyJzdWIiOiIxZnRrQkpFSXZtT0Z1eEp5Z3Myam5RPT0iLCJhdWQiOiI5NVR4U3NUUEZBM3RGMTJUQlNNbVVWSzBkYSIsIm5iZiI6MTcwMDY1NjM4MSwic2NvcGUiOlsicGFzc3dvcmQiLCJtYWlsIiwib3BlbmlkIiwibW9iaWxlIl0sImlzcyI6Imh0dHA6Ly8xMjcuMC4wLjE6MTExMSIsImV4cCI6MTcwMDY1OTk4MSwiaWF0IjoxNzAwNjU2MzgxLCJqdGkiOiI3YTQwMjQzZi00MmQ3LTQ0ZjAtOGUwMy1hOWE4M2JhY2FlOTgifQ.EXEF2_mBBDJKI2db7oDjzCD9H3CzKpffoyhOhnnUf_ubcMxt5WgigMCDmHTjqP2q_6lysCL_lzWKP5qNZVp59OQpFqMM7LDUIQW9xLcjVJy_JN5Mc7Tf3cW9-lOp_RIkrXAQRp_fvzU3SuY2fOo53foDosZ9nSOtXKICErSC86GjcZy0XATeBw0JAzWPRNRFkqeu2svGeRP_sdhQrT3pYRmjvdTJlfGu_tL2CtqVOAys2wPIl5Ckb8LNbjCTDU-DLPI7epK8Y_glRw-p1fZBmzcyBODd0PrRPIwJ7qs9xaxZaiFe1Vp7eSOI3Q6S4jz_u9TNl9Y4Thn8uYjIj0s_pw";

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
