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

import lombok.extern.slf4j.Slf4j;
import org.laokou.common.context.util.User;
import org.laokou.common.context.util.UserExtDetails;
import org.laokou.common.core.util.RequestUtils;
import org.laokou.common.i18n.common.exception.StatusCode;
import org.laokou.common.i18n.util.InstantUtils;
import org.laokou.common.i18n.util.ObjectUtils;
import org.laokou.common.i18n.util.RedisKeyUtils;
import org.laokou.common.redis.util.RedisUtils;
import org.laokou.common.security.handler.OAuth2ExceptionHandler;
import org.springframework.security.oauth2.core.OAuth2AuthenticatedPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.server.authorization.OAuth2Authorization;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationService;
import org.springframework.security.oauth2.server.authorization.OAuth2TokenType;
import org.springframework.security.oauth2.server.resource.introspection.OpaqueTokenIntrospector;

import java.time.Instant;

/**
 * @author laokou
 */
@Slf4j
public record OAuth2OpaqueTokenIntrospector(OAuth2AuthorizationService authorizationService, JwtDecoder jwtDecoder,
		RedisUtils redisUtils) implements OpaqueTokenIntrospector {

	// @formatter:off
	@Override
	public OAuth2AuthenticatedPrincipal introspect(String token) {
		Jwt jwt = jwtDecoder.decode(token);
		Instant expiresAt = jwt.getExpiresAt();
		if (ObjectUtils.isNotNull(expiresAt) && expiresAt.isBefore(InstantUtils.now())) {
			OAuth2Authorization authorization = authorizationService.findByToken(token, OAuth2TokenType.ACCESS_TOKEN);
			if (ObjectUtils.isNull(authorization)) {
				throw OAuth2ExceptionHandler.getException(StatusCode.UNAUTHORIZED);
			}
			authorizationService.remove(authorization);
			throw OAuth2ExceptionHandler.getException(StatusCode.UNAUTHORIZED);
		}
		String jwtType = RequestUtils.getParamValue(RequestUtils.getHttpServletRequest(), "jwt_type");
		String id = jwt.getClaimAsString("id");
		if (ObjectUtils.equals(jwtType, "part")) {
			return new UserExtDetails(Long.valueOf(id), Long.valueOf(jwt.getClaimAsString("tenant_id")));
		}
		if (redisUtils.hGet(RedisKeyUtils.getUserDetailHashKey(), id) instanceof User user) {
			return new UserExtDetails(user);
		}
		throw OAuth2ExceptionHandler.getException(StatusCode.UNAUTHORIZED);
	}
	// @formatter:on

}
