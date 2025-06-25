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

package org.laokou.auth.service.authentication;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.laokou.auth.convertor.AuthConvertor;
import org.laokou.auth.model.AuthA;
import org.laokou.auth.model.GrantTypeEnum;
import org.laokou.common.dubbo.rpc.DistributedIdentifierRpc;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.OAuth2Token;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationService;
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenGenerator;
import org.springframework.stereotype.Component;

import static org.laokou.auth.factory.DomainFactory.*;
import static org.laokou.common.i18n.common.constant.StringConstants.EMPTY;

/**
 * 邮箱处理器.
 *
 * @author laokou
 */
@Slf4j
@Component("mailAuthenticationProvider")
final class OAuth2MailAuthenticationProvider extends AbstractOAuth2AuthenticationProvider {

	public OAuth2MailAuthenticationProvider(OAuth2AuthorizationService authorizationService,
			OAuth2TokenGenerator<? extends OAuth2Token> tokenGenerator, OAuth2AuthenticationProcessor authProcessor,
			DistributedIdentifierRpc distributedIdentifierRpc) {
		super(authorizationService, tokenGenerator, authProcessor, distributedIdentifierRpc);
	}

	@Override
	public boolean supports(Class<?> authentication) {
		return OAuth2MailAuthenticationToken.class.isAssignableFrom(authentication);
	}

	@Override
	Authentication getPrincipal(HttpServletRequest request) throws Exception {
		String code = request.getParameter(CODE);
		String mail = request.getParameter(MAIL);
		String tenantCode = request.getParameter(TENANT_CODE);
		AuthA authA = AuthConvertor.toEntity(distributedIdentifierRpc.getId(), EMPTY, EMPTY, tenantCode,
				GrantTypeEnum.MAIL, code, mail);
		authA.createUserByMail();
		return authenticationToken(authA, request);
	}

	@Override
	AuthorizationGrantType getGrantType() {
		return new AuthorizationGrantType(MAIL);
	}

}
