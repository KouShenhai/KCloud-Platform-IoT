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

	private static final String TOKEN = "eyJraWQiOiI1YjRiYTkwMi1kMDhhLTRhZjItYWE4YS1lMmQ1OTBlMzE5ZTIiLCJhbGciOiJSUzI1NiJ9.eyJzdWIiOiIxZnRrQkpFSXZtT0Z1eEp5Z3Myam5RPT0iLCJhdWQiOiI5NVR4U3NUUEZBM3RGMTJUQlNNbVVWSzBkYSIsIm5iZiI6MTcwMDYzODg2Mywic2NvcGUiOlsicGFzc3dvcmQiLCJtYWlsIiwib3BlbmlkIiwibW9iaWxlIl0sImlzcyI6Imh0dHA6Ly8xMjcuMC4wLjE6MTExMSIsImV4cCI6MTcwMDY0MjQ2MywiaWF0IjoxNzAwNjM4ODYzLCJqdGkiOiIwMDJjYWMzMi0yYjdiLTRiMmMtOTU5MC01YjRmNzRkOTI1OTUifQ.5bH5_Dnj-6gdKgfMSlRig4bF-EmUMZq1_xNIhDHXaNLFpkd-0HUuJCY0LujuZbAuZqdcUh808BczomgNwp6VxAPyzWHYrFsH1lD6VQHjNCaTKG9y9t4T_o1jXTl8BcCVSwX7GR7HXwVIWplIoWYqD3h5KDQ7ZO53U2kIDYYl7AVMRq5HMmxiGaToygVvOWIGW22D7nb-vX2yg_snqNPpOpkPiweyYE2ibkICgzXEh7of7Sdw9oBEaJyKqfGsRVB5jBYjPLVDhWeE1UMYPztak8kUZA24sx7Wn1x6Kx7qoMtQy9GeMPhbMIj0EEdEphWjwK_gyruCxG8yuxwdrWUCjw";

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
