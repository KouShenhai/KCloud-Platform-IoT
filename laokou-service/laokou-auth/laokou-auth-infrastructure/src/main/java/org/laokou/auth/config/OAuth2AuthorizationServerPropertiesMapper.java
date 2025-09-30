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

package org.laokou.auth.config;

import lombok.RequiredArgsConstructor;
import org.laokou.common.i18n.util.ObjectUtils;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.boot.context.properties.PropertyMapper;
import org.springframework.boot.web.server.Ssl;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.security.oauth2.jose.jws.JwsAlgorithm;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jose.jws.SignatureAlgorithm;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.settings.AuthorizationServerSettings;
import org.springframework.security.oauth2.server.authorization.settings.ClientSettings;
import org.springframework.security.oauth2.server.authorization.settings.OAuth2TokenFormat;
import org.springframework.security.oauth2.server.authorization.settings.TokenSettings;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.function.Supplier;

/**
 * Maps {@link OAuth2AuthorizationServerProperties } to Authorization Server types.
 *
 * @author Steve Riesenberg
 * @author laokou
 */
@RequiredArgsConstructor
@Component("propertiesMapper")
@ConditionalOnProperty(havingValue = "true", matchIfMissing = true,
		prefix = "spring.security.oauth2.authorization-server", name = "enabled")
final class OAuth2AuthorizationServerPropertiesMapper {

	private final OAuth2AuthorizationServerProperties properties;

	private final ServerProperties serverProperties;

	AuthorizationServerSettings asAuthorizationServerSettings() {
		PropertyMapper map = PropertyMapper.get();
		OAuth2AuthorizationServerProperties.Endpoint endpoint = this.properties.getEndpoint();
		OAuth2AuthorizationServerProperties.OidcEndpoint oidc = endpoint.getOidc();
		AuthorizationServerSettings.Builder builder = AuthorizationServerSettings.builder();
		map.from(this.properties::getIssuer).to(builder::issuer);
		map.from(this.properties::isMultipleIssuersAllowed).to(builder::multipleIssuersAllowed);
		map.from(mergeApiPath(endpoint::getAuthorizationUri)).to(builder::authorizationEndpoint);
		map.from(mergeApiPath(endpoint::getDeviceAuthorizationUri)).to(builder::deviceAuthorizationEndpoint);
		map.from(mergeApiPath(endpoint::getDeviceVerificationUri)).to(builder::deviceVerificationEndpoint);
		map.from(mergeApiPath(endpoint::getTokenUri)).to(builder::tokenEndpoint);
		map.from(mergeApiPath(endpoint::getJwkSetUri)).to(builder::jwkSetEndpoint);
		map.from(mergeApiPath(endpoint::getTokenRevocationUri)).to(builder::tokenRevocationEndpoint);
		map.from(mergeApiPath(endpoint::getTokenIntrospectionUri)).to(builder::tokenIntrospectionEndpoint);
		map.from(mergeApiPath(endpoint::getPushedAuthorizationRequestUri))
			.to(builder::pushedAuthorizationRequestEndpoint);
		map.from(mergeApiPath(oidc::getLogoutUri)).to(builder::oidcLogoutEndpoint);
		map.from(mergeApiPath(oidc::getClientRegistrationUri)).to(builder::oidcClientRegistrationEndpoint);
		map.from(mergeApiPath(oidc::getUserInfoUri)).to(builder::oidcUserInfoEndpoint);
		return builder.build();
	}

	List<RegisteredClient> asRegisteredClients() {
		Map<String, OAuth2AuthorizationServerProperties.Client> clientMap = this.properties.getClient();
		List<RegisteredClient> registeredClients = new ArrayList<>(clientMap.size());
		clientMap
			.forEach((registrationId, client) -> registeredClients.add(getRegisteredClient(registrationId, client)));
		return registeredClients;
	}

	private RegisteredClient getRegisteredClient(String registrationId,
			OAuth2AuthorizationServerProperties.Client client) {
		OAuth2AuthorizationServerProperties.Registration registration = client.getRegistration();
		PropertyMapper map = PropertyMapper.get();
		RegisteredClient.Builder builder = RegisteredClient.withId(registrationId);
		// 注册
		// Base64编码
		// ClientAuthenticationMethod.CLIENT_SECRET_BASIC => client_id:client_secret
		map.from(registration::getClientId).to(builder::clientId);
		map.from(registration::getClientSecret).to(builder::clientSecret);
		map.from(registration::getClientName).to(builder::clientName);
		registration.getClientAuthenticationMethods()
			.forEach((clientAuthenticationMethod) -> map.from(clientAuthenticationMethod)
				.as(ClientAuthenticationMethod::new)
				.to(builder::clientAuthenticationMethod));
		registration.getAuthorizationGrantTypes()
			.forEach((authorizationGrantType) -> map.from(authorizationGrantType)
				.as(AuthorizationGrantType::new)
				.to(builder::authorizationGrantType));
		registration.getRedirectUris().forEach((redirectUri) -> map.from(redirectUri).to(builder::redirectUri));
		registration.getPostLogoutRedirectUris()
			.forEach((redirectUri) -> map.from(redirectUri).to(builder::postLogoutRedirectUri));
		registration.getScopes().forEach((scope) -> map.from(scope).to(builder::scope));
		builder.clientSettings(getClientSettings(client, map));
		builder.tokenSettings(getTokenSettings(client, map));
		return builder.build();
	}

	private ClientSettings getClientSettings(OAuth2AuthorizationServerProperties.Client client, PropertyMapper map) {
		ClientSettings.Builder builder = ClientSettings.builder();
		map.from(client::isRequireProofKey).to(builder::requireProofKey);
		// 客户端配置，包括验证密钥或需要授权页面
		map.from(client::isRequireAuthorizationConsent).to(builder::requireAuthorizationConsent);
		map.from(client::getJwkSetUri).to(builder::jwkSetUrl);
		map.from(client::getTokenEndpointAuthenticationSigningAlgorithm)
			.as(this::jwsAlgorithm)
			.to(builder::tokenEndpointAuthenticationSigningAlgorithm);
		return builder.build();
	}

	private TokenSettings getTokenSettings(OAuth2AuthorizationServerProperties.Client client, PropertyMapper map) {
		OAuth2AuthorizationServerProperties.Token token = client.getToken();
		TokenSettings.Builder builder = TokenSettings.builder();
		map.from(token::getAuthorizationCodeTimeToLive).to(builder::authorizationCodeTimeToLive);
		// 令牌 => JWT配置
		map.from(token::getAccessTokenTimeToLive).to(builder::accessTokenTimeToLive);
		map.from(token::getAccessTokenFormat).as(OAuth2TokenFormat::new).to(builder::accessTokenFormat);
		map.from(token::getDeviceCodeTimeToLive).to(builder::deviceCodeTimeToLive);
		map.from(token::isReuseRefreshTokens).to(builder::reuseRefreshTokens);
		map.from(token::getRefreshTokenTimeToLive).to(builder::refreshTokenTimeToLive);
		map.from(token::getIdTokenSignatureAlgorithm)
			.as(this::signatureAlgorithm)
			.to(builder::idTokenSignatureAlgorithm);
		Ssl ssl = serverProperties.getSsl();
		if (ObjectUtils.isNull(ssl)) {
			return builder.build();
		}
		return ssl.isEnabled() ? builder.x509CertificateBoundAccessTokens(true).build() : builder.build();
	}

	private JwsAlgorithm jwsAlgorithm(String signingAlgorithm) {
		String name = signingAlgorithm.toUpperCase(Locale.ROOT);
		JwsAlgorithm jwsAlgorithm = SignatureAlgorithm.from(name);
		if (ObjectUtils.isNull(jwsAlgorithm)) {
			jwsAlgorithm = MacAlgorithm.from(name);
		}
		return jwsAlgorithm;
	}

	private SignatureAlgorithm signatureAlgorithm(String signatureAlgorithm) {
		return SignatureAlgorithm.from(signatureAlgorithm.toUpperCase(Locale.ROOT));
	}

	private String mergeApiPath(Supplier<String> supplier) {
		return serverProperties.getServlet().getContextPath() + supplier.get();
	}

}
