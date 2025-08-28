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

package org.laokou.common.security.config.entity;

import java.time.Duration;
import java.time.Instant;
import java.util.Set;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.index.Indexed;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.security.oauth2.jose.jws.JwsAlgorithm;
import org.springframework.security.oauth2.jose.jws.SignatureAlgorithm;
import org.springframework.security.oauth2.server.authorization.settings.OAuth2TokenFormat;

/**
 * @author spring-authorization-server
 * @author laokou
 */
@Getter
@RedisHash("oauth2_registered_client")
@RequiredArgsConstructor
public final class OAuth2RegisteredClient {

	@Id
	private final String id;

	@Indexed
	private final String clientId;

	private final Instant clientIdIssuedAt;

	private final String clientSecret;

	private final Instant clientSecretExpiresAt;

	private final String clientName;

	private final Set<ClientAuthenticationMethod> clientAuthenticationMethods;

	private final Set<AuthorizationGrantType> authorizationGrantTypes;

	private final Set<String> redirectUris;

	private final Set<String> postLogoutRedirectUris;

	private final Set<String> scopes;

	private final ClientSettings clientSettings;

	private final TokenSettings tokenSettings;

	public record ClientSettings(boolean requireProofKey, boolean requireAuthorizationConsent, String jwkSetUrl,
									 JwsAlgorithm tokenEndpointAuthenticationSigningAlgorithm,
									 String x509CertificateSubjectDN) {
	}

	public record TokenSettings(Duration authorizationCodeTimeToLive, Duration accessTokenTimeToLive,
									OAuth2TokenFormat accessTokenFormat, Duration deviceCodeTimeToLive,
									boolean reuseRefreshTokens, Duration refreshTokenTimeToLive,
									SignatureAlgorithm idTokenSignatureAlgorithm,
									boolean x509CertificateBoundAccessTokens) {
	}

}
