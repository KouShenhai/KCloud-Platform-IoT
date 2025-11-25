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

package org.laokou.auth.config.authentication;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.laokou.auth.convertor.AuthConvertor;
import org.laokou.auth.model.AuthA;
import org.laokou.auth.model.Constants;
import org.laokou.auth.model.GrantTypeEnum;
import org.laokou.common.i18n.common.constant.StringConstants;
import org.laokou.common.security.config.OAuth2ModelMapper;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.OAuth2Token;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationService;
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenGenerator;
import org.springframework.stereotype.Component;

/**
 * 测试认证Provider.
 *
 * @author laokou
 */
@Slf4j
@Component
final class OAuth2TestAuthenticationProvider extends AbstractOAuth2AuthenticationProvider {

	public OAuth2TestAuthenticationProvider(OAuth2AuthorizationService authorizationService,
			OAuth2TokenGenerator<? extends OAuth2Token> tokenGenerator,
			OAuth2AuthenticationProcessor authenticationProcessor) {
		super(authorizationService, tokenGenerator, authenticationProcessor);
	}

	@Override
	public boolean supports(@NotNull Class<?> authentication) {
		return OAuth2TestAuthenticationToken.class.isAssignableFrom(authentication);
	}

	@Override
	Authentication getPrincipal(HttpServletRequest request) throws Exception {
		String username = request.getParameter(Constants.USERNAME);
		String password = request.getParameter(Constants.PASSWORD);
		String tenantCode = request.getParameter(Constants.TENANT_CODE);
		AuthA authA = AuthConvertor.toEntity(username, password, tenantCode, GrantTypeEnum.TEST, StringConstants.EMPTY,
				StringConstants.EMPTY);
		authA.createUserByTest();
		return authentication(authA, request);
	}

	@Override
	AuthorizationGrantType getGrantType() {
		return OAuth2ModelMapper.TEST;
	}

}
