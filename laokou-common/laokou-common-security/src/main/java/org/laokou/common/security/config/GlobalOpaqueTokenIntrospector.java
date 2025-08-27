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
import org.laokou.common.i18n.common.exception.GlobalException;
import org.laokou.common.i18n.util.ObjectUtils;
import org.laokou.common.security.handler.OAuth2ExceptionHandler;
import org.laokou.common.context.util.UserDetails;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.security.oauth2.core.OAuth2AuthenticatedPrincipal;
import org.springframework.security.oauth2.core.OAuth2RefreshToken;
import org.springframework.security.oauth2.server.authorization.OAuth2Authorization;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationService;
import org.springframework.security.oauth2.server.authorization.OAuth2TokenType;
import org.springframework.security.oauth2.server.resource.introspection.OpaqueTokenIntrospector;
import org.springframework.util.Assert;
import java.security.Principal;
import static org.laokou.common.i18n.common.exception.StatusCode.UNAUTHORIZED;
import static org.laokou.common.security.handler.OAuth2ExceptionHandler.ERROR_URL;
import static org.laokou.common.security.handler.OAuth2ExceptionHandler.getOAuth2AuthenticationException;

/**
 * @author laokou
 */
@Slf4j
@NonNullApi
@RequiredArgsConstructor
@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET)
public class GlobalOpaqueTokenIntrospector implements OpaqueTokenIntrospector {

	private final OAuth2AuthorizationService oAuth2AuthorizationService;

	// @formatter:off
	@Override
	public OAuth2AuthenticatedPrincipal introspect(String token) {
		// 低命中率且数据庞大放redis稳妥，分布式集群需要通过redis实现数据共享
		OAuth2Authorization authorization = oAuth2AuthorizationService.findByToken(token, OAuth2TokenType.ACCESS_TOKEN);
		if (ObjectUtils.isNull(authorization)) {
			throw OAuth2ExceptionHandler.getException(UNAUTHORIZED);
		}
		OAuth2Authorization.Token<OAuth2RefreshToken> refreshToken = authorization.getRefreshToken();
		OAuth2Authorization.Token<OAuth2AccessToken> accessToken = authorization.getAccessToken();
		Assert.notNull(accessToken, "accessToken is null");
		Assert.notNull(refreshToken, "refreshToken is null");
		if (accessToken.isActive()) {
            Object obj = authorization.getAttribute(Principal.class.getName());
            if (ObjectUtils.isNotNull(obj)) {
                UserDetails userDetails = (UserDetails) ((UsernamePasswordAuthenticationToken) obj).getPrincipal();
                // 解密
                return decryptInfo(userDetails);
            }
		}
		if (!refreshToken.isActive()) {
			oAuth2AuthorizationService.remove(authorization);
		}
		throw OAuth2ExceptionHandler.getException(UNAUTHORIZED);
	}
	// @formatter:on

	/**
	 * 解密字段.
	 * @param userDetails 用户信息
	 * @return UserDetail
	 */
	public static UserDetails decryptInfo(UserDetails userDetails) {
		try {
			// 解密
			return userDetails.getDecryptInfo();
		}
		catch (GlobalException e) {
			throw getOAuth2AuthenticationException(e.getCode(), e.getMsg(), ERROR_URL);
		}
	}

}
