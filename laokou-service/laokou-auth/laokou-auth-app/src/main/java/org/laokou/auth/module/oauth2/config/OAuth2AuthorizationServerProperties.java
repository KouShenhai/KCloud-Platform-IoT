/*
 * Copyright (c) 2022 KCloud-Platform-Alibaba Authors. All Rights Reserved.
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

package org.laokou.auth.module.oauth2.config;

import lombok.Data;
import org.laokou.common.core.utils.CollectionUtil;
import org.laokou.common.i18n.utils.StringUtil;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.settings.ConfigurationSettingNames;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.Set;

/**
 * {@link org.springframework.boot.autoconfigure.security.oauth2.server.servlet.OAuth2AuthorizationServerProperties}
 * {@link ConfigurationSettingNames}
 * @author laokou
 */
@Data
@Component
@ConfigurationProperties(prefix = OAuth2AuthorizationServerProperties.PREFIX)
public class OAuth2AuthorizationServerProperties implements InitializingBean {

	public static final String PREFIX = "spring.security.oauth2.authorization-server";

	private boolean enabled = true;

	private Token token;

	private Client client;

	private Registration registration;

	private RequestMatcher requestMatcher;

	@Override
	public void afterPropertiesSet() {
		validateRegistration(getRegistration());
	}

	private void validateRegistration(Registration registration) {
		if (StringUtil.isEmpty(registration.clientId)) {
			throw new IllegalStateException("客户端ID不能为空");
		}
		if (CollectionUtil.isEmpty(registration.clientAuthenticationMethods)) {
			throw new IllegalStateException("客户端身份验证方法不能为空");
		}
		if (CollectionUtil.isEmpty(registration.authorizationGrantTypes)) {
			throw new IllegalStateException("授权认证类型不能为空");
		}
	}

	@Data
	public static class Token {

		/**
		 * Set the time-to-live for a refresh token.
		 */
		private Duration refreshTokenTimeToLive;

		/**
		 * Set the time-to-live for an access token.
		 */
		private Duration accessTokenTimeToLive;

		/**
		 * Set the time-to-live for an authorization code.
		 */
		private Duration authorizationCodeTimeToLive;

	}

	@Data
	public static class Client {

		/**
		 * Set to {@code true} if authorization consent is required when the client
		 * requests access. This applies to all interactive flows (e.g.
		 * {@code authorization_code} and {@code device_code}).
		 */
		private boolean requireAuthorizationConsent;

	}

	/**
	 * {@link RegisteredClient}
	 */
	@Data
	public static class Registration {

		private String id;

		private String clientId;

		private String clientName;

		private String clientSecret;

		private Set<String> clientAuthenticationMethods;

		private Set<String> authorizationGrantTypes;

		private Set<String> scopes;

		private Set<String> redirectUris;

		private Set<String> postLogoutRedirectUris;

	}

	@Data
	public static class RequestMatcher {

		private Set<String> patterns;

	}

}
