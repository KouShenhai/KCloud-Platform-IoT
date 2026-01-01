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

package org.laokou.auth.config;

import lombok.Data;
import org.jspecify.annotations.Nullable;
import org.laokou.common.core.util.CollectionExtUtils;
import org.laokou.common.core.util.MapUtils;
import org.laokou.common.i18n.util.StringExtUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;
import org.springframework.security.oauth2.server.authorization.settings.ConfigurationSettingNames;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * OAuth 2.0 Authorization Server properties. {@link ConfigurationSettingNames}
 *
 * @author Steve Riesenberg
 * @author laokou
 */
@Data
@Component
@ConfigurationProperties(prefix = "spring.security.oauth2.authorization-server")
final class OAuth2AuthorizationServerProperties implements InitializingBean {

	/**
	 * Registered clients of the Authorization Server.
	 */
	private final Map<String, Client> client = MapUtils.newHashMap(0);

	/**
	 * Authorization Server endpoints.
	 */
	private final Endpoint endpoint = new Endpoint();

	/**
	 * Open or close.
	 */
	private boolean enabled = true;

	/**
	 * URL of the Authorization Server's Issuer Identifier.
	 */
	private @Nullable String issuer;

	/**
	 * Whether multiple issuers are allowed per host. Using path components in the URL of
	 * the issuer identifier enables supporting multiple issuers per host in a
	 * multi-tenant hosting configuration.
	 */
	private boolean multipleIssuersAllowed = false;

	@Override
	public void afterPropertiesSet() {
		validate();
	}

	/**
	 *
	 */
	public void validate() {
		getClient().values().forEach(this::validateClient);
	}

	/**
	 * 校验客户端.
	 * @param client 客户端
	 */
	private void validateClient(Client client) {
		if (StringExtUtils.isEmpty(client.getRegistration().getClientId())) {
			throw new IllegalStateException("Client id must not be empty.");
		}
		if (CollectionExtUtils.isEmpty(client.getRegistration().getClientAuthenticationMethods())) {
			throw new IllegalStateException("Client authentication methods must not be empty.");
		}
		if (CollectionExtUtils.isEmpty(client.getRegistration().getAuthorizationGrantTypes())) {
			throw new IllegalStateException("Authorization grant types must not be empty.");
		}
	}

	/**
	 * Authorization Server endpoints.
	 */
	@Data
	public static class Endpoint {

		/**
		 * OpenID Connect 1.0 endpoints.
		 */
		@NestedConfigurationProperty
		private final OidcEndpoint oidc = new OidcEndpoint();

		/**
		 * Authorization Server's OAuth 2.0 Authorization Endpoint.
		 */
		private String authorizationUri = "/v1/oauth2/authorize";

		/**
		 * Authorization Server's OAuth 2.0 Device Authorization Endpoint.
		 */
		private String deviceAuthorizationUri = "/v1/oauth2/device_authorization";

		/**
		 * Authorization Server's OAuth 2.0 Device Verification Endpoint.
		 */
		private String deviceVerificationUri = "/v1/oauth2/device_verification";

		/**
		 * Authorization Server's OAuth 2.0 Token Endpoint.
		 */
		private String tokenUri = "/v1/oauth2/token";

		/**
		 * Authorization Server's JWK Set Endpoint.
		 */
		private @Nullable String jwkSetUri = "/v1/oauth2/jwks";

		/**
		 * Authorization Server's OAuth 2.0 Token Revocation Endpoint.
		 */
		private String tokenRevocationUri = "/v1/oauth2/revoke";

		/**
		 * Authorization Server's OAuth 2.0 Token Introspection Endpoint.
		 */
		private String tokenIntrospectionUri = "/v1/oauth2/introspect";

		/**
		 * Authorization Server's OAuth 2.0 Pushed Authorization Request Endpoint.
		 */
		private String pushedAuthorizationRequestUri = "/v1/oauth2/par";

	}

	/**
	 * OpenID Connect 1.0 endpoints.
	 */
	@Data
	public static class OidcEndpoint {

		/**
		 * Authorization Server's OpenID Connect 1.0 Logout Endpoint.
		 */
		private String logoutUri = "/v1/oidc/connect/logout";

		/**
		 * Authorization Server's OpenID Connect 1.0 Client Registration Endpoint.
		 */
		private String clientRegistrationUri = "/v1/oidc/connect/register";

		/**
		 * Authorization Server's OpenID Connect 1.0 UserInfo Endpoint.
		 */
		private String userInfoUri = "/v1/oidc/userinfo";

	}

	/**
	 * A registered client of the Authorization Server.
	 */
	@Data
	public static class Client {

		/**
		 * Client registration information.
		 */
		@NestedConfigurationProperty
		private final Registration registration = new Registration();

		/**
		 * Token settings of the registered client.
		 */
		@NestedConfigurationProperty
		private final Token token = new Token();

		/**
		 * Whether the client is required to provide a proof key challenge and verifier
		 * when performing the Authorization Code Grant flow.
		 */
		private boolean requireProofKey = false;

		/**
		 * Whether authorization consent is required when the client requests access.
		 */
		private boolean requireAuthorizationConsent = false;

		/**
		 * URL for the client's JSON Web Key Set.
		 */
		private String jwkSetUri;

		/**
		 * JWS algorithm that must be used for signing the JWT used to authenticate the
		 * client at the Token Endpoint for the {@code private_key_jwt} and
		 * {@code client_secret_jwt} authentication methods.
		 */
		private @Nullable String tokenEndpointAuthenticationSigningAlgorithm;

	}

	/**
	 * Client registration information.
	 */
	@Data
	public static class Registration {

		/**
		 * Client ID of the registration.
		 */
		private @Nullable String clientId;

		/**
		 * Client secret of the registration. May be left blank for a public client.
		 */
		private @Nullable String clientSecret;

		/**
		 * Name of the client.
		 */
		private @Nullable String clientName;

		/**
		 * Client authentication method(s) that the client may use.
		 */
		private Set<String> clientAuthenticationMethods = new HashSet<>(0);

		/**
		 * Authorization grant type(s) that the client may use.
		 */
		private Set<String> authorizationGrantTypes = new HashSet<>(0);

		/**
		 * Redirect URI(s) that the client may use in redirect-based flows.
		 */
		private Set<String> redirectUris = new HashSet<>(0);

		/**
		 * Redirect URI(s) that the client may use for logout.
		 */
		private Set<String> postLogoutRedirectUris = new HashSet<>(0);

		/**
		 * Scope(s) that the client may use.
		 */
		private Set<String> scopes = new HashSet<>(0);

	}

	/**
	 * Token settings of the registered client.
	 */
	@Data
	public static class Token {

		/**
		 * Time-to-live for an authorization code.
		 */
		private Duration authorizationCodeTimeToLive = Duration.ofMinutes(5);

		/**
		 * Time-to-live for an access token.
		 */
		private Duration accessTokenTimeToLive = Duration.ofMinutes(5);

		/**
		 * Token format for an access token.
		 */
		private String accessTokenFormat = "self-contained";

		/**
		 * Time-to-live for a device code.
		 */
		private Duration deviceCodeTimeToLive = Duration.ofMinutes(5);

		/**
		 * Whether refresh tokens are reused or a new refresh token is issued when
		 * returning the access token response.
		 */
		private boolean reuseRefreshTokens = true;

		/**
		 * Time-to-live for a refresh token.
		 */
		private Duration refreshTokenTimeToLive = Duration.ofMinutes(60);

		/**
		 * JWS algorithm for signing the ID Token.
		 */
		private String idTokenSignatureAlgorithm = "RS256";

	}

}
