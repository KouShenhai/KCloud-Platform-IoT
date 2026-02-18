/*
 * Copyright (c) 2022-2026 KCloud-Platform-IoT Author or Authors. All Rights Reserved.
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

package org.laokou.common.security.config;

import lombok.extern.slf4j.Slf4j;
import org.laokou.common.context.util.User;
import org.laokou.common.context.util.UserConvertor;
import org.laokou.common.i18n.common.exception.StatusCode;
import org.laokou.common.i18n.util.ObjectUtils;
import org.laokou.common.security.handler.OAuth2ExceptionHandler;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.security.oauth2.core.OAuth2AuthenticatedPrincipal;
import org.springframework.security.oauth2.core.OAuth2RefreshToken;
import org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.server.authorization.OAuth2Authorization;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationService;
import org.springframework.security.oauth2.server.authorization.OAuth2TokenType;
import org.springframework.security.oauth2.server.resource.introspection.OpaqueTokenIntrospector;

import java.security.Principal;
import java.util.Collections;
import java.util.List;

/**
 * @author laokou
 */
@Slf4j
public record OAuth2OpaqueTokenIntrospector(OAuth2AuthorizationService authorizationService,
		JwtDecoder jwtDecoder) implements OpaqueTokenIntrospector {

	// @formatter:off
	@Override
	public OAuth2AuthenticatedPrincipal introspect(String token) {
		OAuth2Authorization authorization = authorizationService.findByToken(token, OAuth2TokenType.ACCESS_TOKEN);
		if (ObjectUtils.isNull(authorization)) {
			throw OAuth2ExceptionHandler.getException(StatusCode.UNAUTHORIZED);
		}
		OAuth2Authorization.Token<OAuth2AccessToken> accessToken = authorization.getAccessToken();
		OAuth2Authorization.Token<OAuth2RefreshToken> refreshToken = authorization.getRefreshToken();
		if (ObjectUtils.isNull(accessToken) || ObjectUtils.isNull(refreshToken)) {
			throw OAuth2ExceptionHandler.getException(StatusCode.UNAUTHORIZED);
		}
		if (accessToken.isActive() && refreshToken.isActive() && authorization.getAttribute(Principal.class.getName()) instanceof User user) {
			return UserConvertor.toUserDetails(user, () -> (List<String>) jwtDecoder.decode(token).getClaims().getOrDefault(OAuth2ParameterNames.SCOPE, Collections.emptyList()));
		}
		authorizationService.remove(authorization);
		throw OAuth2ExceptionHandler.getException(StatusCode.UNAUTHORIZED);
	}
	// @formatter:on

}
