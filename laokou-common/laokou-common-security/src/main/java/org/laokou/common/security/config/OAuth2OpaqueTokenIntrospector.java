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

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.jspecify.annotations.NonNull;
import org.laokou.common.context.util.OAuth2Authentication;
import org.laokou.common.context.util.UserConvertor;
import org.laokou.common.context.util.UserExtDetails;
import org.laokou.common.i18n.common.exception.StatusCode;
import org.laokou.common.i18n.util.RedisKeyUtils;
import org.laokou.common.redis.util.RedisUtils;
import org.laokou.common.security.handler.OAuth2ExceptionHandler;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.security.oauth2.core.OAuth2AuthenticatedPrincipal;
import org.springframework.security.oauth2.core.OAuth2RefreshToken;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.server.authorization.OAuth2Authorization;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationService;
import org.springframework.security.oauth2.server.authorization.OAuth2TokenType;
import org.springframework.security.oauth2.server.resource.introspection.OpaqueTokenIntrospector;

import java.security.Principal;
import java.time.Instant;
import java.util.concurrent.TimeUnit;

/**
 * @author laokou
 */
@Slf4j
public record OAuth2OpaqueTokenIntrospector(OAuth2AuthorizationService authorizationService, RedisUtils redisUtils,
		JwtDecoder jwtDecoder) implements OpaqueTokenIntrospector {

	private static final Cache<@NonNull String, @NonNull CachedPrincipal> PRINCIPAL_CACHE = Caffeine.newBuilder()
		.initialCapacity(100000)
		.maximumSize(300000)
		.expireAfterWrite(5, TimeUnit.MINUTES)
		.build();

	// @formatter:off
	@NotNull
	@Override
	public OAuth2AuthenticatedPrincipal introspect(@NonNull String token) {
		try {
			CachedPrincipal cachedPrincipal = PRINCIPAL_CACHE.get(token, _ -> getCachedPrincipal(token));
			Instant expiresAt = cachedPrincipal.expiresAt();
			if (expiresAt != null && expiresAt.isBefore(Instant.now())) {
				invalidate(token);
				throw OAuth2ExceptionHandler.getException(StatusCode.UNAUTHORIZED);
			}
			return cachedPrincipal.principal();
		} catch (Exception ex) {
			log.debug("JWT解析失败，错误信息：{}", ex.getMessage(), ex);
			invalidate(token);
			throw OAuth2ExceptionHandler.getException(StatusCode.UNAUTHORIZED);
		}
	}

	private CachedPrincipal getCachedPrincipal(@NonNull String token) {
		Jwt jwt = jwtDecoder.decode(token);
		Instant expiresAt = jwt.getExpiresAt();
		if (expiresAt != null && expiresAt.isBefore(Instant.now())) {
			throw OAuth2ExceptionHandler.getException(StatusCode.UNAUTHORIZED);
		}
		OAuth2Authorization authorization = authorizationService.findByToken(token, OAuth2TokenType.ACCESS_TOKEN);
		if (authorization == null) {
			throw OAuth2ExceptionHandler.getException(StatusCode.UNAUTHORIZED);
		}
		OAuth2Authorization.Token<@NonNull OAuth2AccessToken> accessToken = authorization.getAccessToken();
		OAuth2Authorization.Token<@NonNull OAuth2RefreshToken> refreshToken = authorization.getRefreshToken();
		if (accessToken == null || refreshToken == null) {
			throw OAuth2ExceptionHandler.getException(StatusCode.UNAUTHORIZED);
		}
		if (accessToken.isActive() && refreshToken.isActive()
			&& authorization.getAttribute(Principal.class.getName()) instanceof OAuth2Authentication authentication) {
			return new CachedPrincipal(UserConvertor.toPrincipal(authentication, authorization.getAuthorizedScopes()), expiresAt);
		}
		if (accessToken.isActive() && refreshToken.isActive()
			&& authorization.getAttribute(Principal.class.getName()) instanceof UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken
			&& usernamePasswordAuthenticationToken.getPrincipal() instanceof User user
			&& redisUtils.get(RedisKeyUtils.getUserDetailKey(user.getUsername())) instanceof UserExtDetails userExtDetails) {
			return new CachedPrincipal(UserConvertor.toPrincipal(userExtDetails, authorization.getAuthorizedScopes()), expiresAt);
		}
		authorizationService.remove(authorization);
		throw OAuth2ExceptionHandler.getException(StatusCode.UNAUTHORIZED);
	}

	private void invalidate(String token) {
		PRINCIPAL_CACHE.invalidate(token);
		OAuth2Authorization authorization = authorizationService.findByToken(token, OAuth2TokenType.ACCESS_TOKEN);
		if (authorization != null) {
			authorizationService.remove(authorization);
		}
	}
	// @formatter:on

}
