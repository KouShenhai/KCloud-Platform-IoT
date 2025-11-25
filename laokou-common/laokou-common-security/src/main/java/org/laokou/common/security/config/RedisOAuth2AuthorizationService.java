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

/*
 * Copyright 2020-2024 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.laokou.common.security.config;

import org.jspecify.annotations.Nullable;
import org.laokou.common.security.config.entity.OAuth2AuthorizationGrantAuthorization;
import org.laokou.common.security.config.repository.OAuth2AuthorizationGrantAuthorizationRepository;
import org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames;
import org.springframework.security.oauth2.core.oidc.endpoint.OidcParameterNames;
import org.springframework.security.oauth2.server.authorization.OAuth2Authorization;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationService;
import org.springframework.security.oauth2.server.authorization.OAuth2TokenType;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.util.Assert;

/**
 * @author spring-authorization-server
 * @author laokou
 */
public record RedisOAuth2AuthorizationService(RegisteredClientRepository registeredClientRepository,
		OAuth2AuthorizationGrantAuthorizationRepository authorizationGrantAuthorizationRepository)
		implements
			OAuth2AuthorizationService {

	public RedisOAuth2AuthorizationService {
		Assert.notNull(registeredClientRepository, "RegisteredClientRepository cannot be null");
		Assert.notNull(authorizationGrantAuthorizationRepository,
				"AuthorizationGrantAuthorizationRepository cannot be null");
	}

	@Override
	public void save(OAuth2Authorization authorization) {
		Assert.notNull(authorization, "Authorization cannot be null");
		OAuth2AuthorizationGrantAuthorization authorizationGrantAuthorization = OAuth2ModelMapper
			.convertOAuth2AuthorizationGrantAuthorization(authorization);
		Assert.notNull(authorizationGrantAuthorization, "AuthorizationGrantAuthorization cannot be null");
		this.authorizationGrantAuthorizationRepository.save(authorizationGrantAuthorization);
	}

	@Override
	public void remove(OAuth2Authorization authorization) {
		Assert.notNull(authorization, "authorization cannot be null");
		this.authorizationGrantAuthorizationRepository.deleteById(authorization.getId());
	}

	@Nullable
	@Override
	public OAuth2Authorization findById(String id) {
		Assert.hasText(id, "id cannot be empty");
		return this.authorizationGrantAuthorizationRepository.findById(id)
			.map(this::toOAuth2Authorization)
			.orElse(null);
	}

	@Nullable
	@Override
	public OAuth2Authorization findByToken(String token, OAuth2TokenType tokenType) {
		Assert.hasText(token, "Token cannot be empty");
		OAuth2AuthorizationGrantAuthorization authorizationGrantAuthorization = null;
		if (tokenType == null) {
			authorizationGrantAuthorization = this.authorizationGrantAuthorizationRepository
				.findByAccessToken_TokenValueOrRefreshToken_TokenValue(token, token);
			if (authorizationGrantAuthorization == null) {
				authorizationGrantAuthorization = this.authorizationGrantAuthorizationRepository
					.findByStateOrAuthorizationCode_TokenValue(token, token);
			}
			if (authorizationGrantAuthorization == null) {
				authorizationGrantAuthorization = this.authorizationGrantAuthorizationRepository
					.findByIdToken_TokenValue(token);
			}
			if (authorizationGrantAuthorization == null) {
				authorizationGrantAuthorization = this.authorizationGrantAuthorizationRepository
					.findByDeviceStateOrDeviceCode_TokenValueOrUserCode_TokenValue(token, token, token);
			}
		}
		else if (OAuth2ParameterNames.STATE.equals(tokenType.getValue())) {
			authorizationGrantAuthorization = this.authorizationGrantAuthorizationRepository.findByState(token);
			if (authorizationGrantAuthorization == null) {
				authorizationGrantAuthorization = this.authorizationGrantAuthorizationRepository
					.findByDeviceState(token);
			}
		}
		else if (OAuth2ParameterNames.CODE.equals(tokenType.getValue())) {
			authorizationGrantAuthorization = this.authorizationGrantAuthorizationRepository
				.findByAuthorizationCode_TokenValue(token);
		}
		else if (OAuth2TokenType.ACCESS_TOKEN.equals(tokenType)) {
			authorizationGrantAuthorization = this.authorizationGrantAuthorizationRepository
				.findByAccessToken_TokenValue(token);
		}
		else if (OidcParameterNames.ID_TOKEN.equals(tokenType.getValue())) {
			authorizationGrantAuthorization = this.authorizationGrantAuthorizationRepository
				.findByIdToken_TokenValue(token);
		}
		else if (OAuth2TokenType.REFRESH_TOKEN.equals(tokenType)) {
			authorizationGrantAuthorization = this.authorizationGrantAuthorizationRepository
				.findByRefreshToken_TokenValue(token);
		}
		else if (OAuth2ParameterNames.USER_CODE.equals(tokenType.getValue())) {
			authorizationGrantAuthorization = this.authorizationGrantAuthorizationRepository
				.findByUserCode_TokenValue(token);
		}
		else if (OAuth2ParameterNames.DEVICE_CODE.equals(tokenType.getValue())) {
			authorizationGrantAuthorization = this.authorizationGrantAuthorizationRepository
				.findByDeviceCode_TokenValue(token);
		}
		return authorizationGrantAuthorization != null ? toOAuth2Authorization(authorizationGrantAuthorization) : null;
	}

	private OAuth2Authorization toOAuth2Authorization(
			OAuth2AuthorizationGrantAuthorization authorizationGrantAuthorization) {
		RegisteredClient registeredClient = this.registeredClientRepository
			.findById(authorizationGrantAuthorization.getRegisteredClientId());
		return toOAuth2Authorization(authorizationGrantAuthorization, registeredClient);
	}

	public static OAuth2Authorization toOAuth2Authorization(
			OAuth2AuthorizationGrantAuthorization authorizationGrantAuthorization, RegisteredClient registeredClient) {
		Assert.notNull(registeredClient, "RegisteredClient cannot be null");
		OAuth2Authorization.Builder builder = OAuth2Authorization.withRegisteredClient(registeredClient);
		OAuth2ModelMapper.mapOAuth2AuthorizationGrantAuthorization(authorizationGrantAuthorization, builder);
		return builder.build();
	}

}
