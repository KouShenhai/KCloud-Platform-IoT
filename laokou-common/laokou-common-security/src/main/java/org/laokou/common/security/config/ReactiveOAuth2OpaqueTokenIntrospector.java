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

package org.laokou.common.security.config;

import io.micrometer.common.lang.NonNullApi;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.laokou.common.context.util.UserDetails;
import org.laokou.common.i18n.common.exception.StatusCode;
import org.laokou.common.i18n.util.ObjectUtils;
import org.laokou.common.security.config.entity.OAuth2AuthorizationGrantAuthorization;
import org.laokou.common.security.config.repository.ReactiveOAuth2AuthorizationGrantAuthorizationRepository;
import org.laokou.common.security.config.repository.ReactiveOAuth2RegisteredClientRepository;
import org.laokou.common.security.constant.OAuth2Constants;
import org.laokou.common.security.handler.OAuth2ExceptionHandler;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.security.oauth2.core.OAuth2AuthenticatedPrincipal;
import org.springframework.security.oauth2.core.OAuth2RefreshToken;
import org.springframework.security.oauth2.server.authorization.OAuth2Authorization;
import org.springframework.security.oauth2.server.resource.introspection.ReactiveOpaqueTokenIntrospector;
import reactor.core.publisher.Mono;

import java.security.Principal;

/**
 * @author laokou
 */
@Slf4j
@NonNullApi
@RequiredArgsConstructor
@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.REACTIVE)
public class ReactiveOAuth2OpaqueTokenIntrospector implements ReactiveOpaqueTokenIntrospector {

	private final ReactiveOAuth2AuthorizationGrantAuthorizationRepository reactiveOAuth2AuthorizationGrantAuthorizationRepository;

	private final ReactiveOAuth2RegisteredClientRepository  reactiveOAuth2RegisteredClientRepository;

	@Override
	public Mono<OAuth2AuthenticatedPrincipal> introspect(String token) {
		return reactiveOAuth2AuthorizationGrantAuthorizationRepository.findByAccessToken_TokenValue(token)
			.switchIfEmpty(Mono.error(OAuth2ExceptionHandler.getException(StatusCode.UNAUTHORIZED)))
			.flatMap(this::findRegisteredClient)
			.flatMap(this::validateAndExtractPrincipal);
	}

	private Mono<UserDetails> validateAndExtractPrincipal(OAuth2Authorization authorization) {
		OAuth2Authorization.Token<OAuth2RefreshToken> refreshToken = authorization.getRefreshToken();
		OAuth2Authorization.Token<OAuth2AccessToken> accessToken = authorization.getAccessToken();
		if (ObjectUtils.isNull(accessToken) || ObjectUtils.isNull(refreshToken)) {
			return Mono.error(OAuth2ExceptionHandler.getException(StatusCode.UNAUTHORIZED));
		}
		if (accessToken.isActive()) {
			return extractUserDetails(authorization);
		} else {
			return reactiveOAuth2AuthorizationGrantAuthorizationRepository
				.deleteById(authorization.getId())
				.then(Mono.error(() -> OAuth2ExceptionHandler.getException(StatusCode.UNAUTHORIZED)));
		}
	}

	private Mono<OAuth2Authorization> findRegisteredClient(OAuth2AuthorizationGrantAuthorization auth2AuthorizationGrantAuthorization) {
		return reactiveOAuth2RegisteredClientRepository.findByClientId(auth2AuthorizationGrantAuthorization.getRegisteredClientId())
			.switchIfEmpty(Mono.error(OAuth2ExceptionHandler.getException(OAuth2Constants.REGISTERED_CLIENT_NOT_EXIST)))
			.map(OAuth2ModelMapper::convertRegisteredClient)
			.map(registeredClient ->
				RedisOAuth2AuthorizationService.toOAuth2Authorization(auth2AuthorizationGrantAuthorization, registeredClient)
			);
	}

	private Mono<UserDetails> extractUserDetails(OAuth2Authorization authorization) {
		return Mono.justOrEmpty(authorization.getAttribute(Principal.class.getName()))
			.cast(UsernamePasswordAuthenticationToken.class)
			.map(UsernamePasswordAuthenticationToken::getPrincipal)
			.cast(UserDetails.class)
			.map(OAuth2OpaqueTokenIntrospector::decryptInfo);
	}

}
