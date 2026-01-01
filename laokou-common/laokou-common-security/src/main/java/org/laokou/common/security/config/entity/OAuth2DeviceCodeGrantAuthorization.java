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

import java.security.Principal;
import java.time.Instant;
import java.util.Set;

import lombok.Getter;
import org.springframework.data.redis.core.index.Indexed;

/**
 * @author spring-authorization-server
 * @author laokou
 */
@Getter
public final class OAuth2DeviceCodeGrantAuthorization extends OAuth2AuthorizationGrantAuthorization {

	private final Principal principal;

	private final DeviceCode deviceCode;

	private final UserCode userCode;

	private final Set<String> requestedScopes;

	@Indexed
	private final String deviceState; // Used to correlate the request during the
										// authorization consent flow

	public OAuth2DeviceCodeGrantAuthorization(String id, String registeredClientId, String principalName,
			Set<String> authorizedScopes, AccessToken accessToken, RefreshToken refreshToken, Principal principal,
			DeviceCode deviceCode, UserCode userCode, Set<String> requestedScopes, String deviceState) {
		super(id, registeredClientId, principalName, authorizedScopes, accessToken, refreshToken);
		this.principal = principal;
		this.deviceCode = deviceCode;
		this.userCode = userCode;
		this.requestedScopes = requestedScopes;
		this.deviceState = deviceState;
	}

	public static class DeviceCode extends AbstractToken {

		public DeviceCode(String tokenValue, Instant issuedAt, Instant expiresAt, boolean invalidated) {
			super(tokenValue, issuedAt, expiresAt, invalidated);
		}

	}

	public static class UserCode extends AbstractToken {

		public UserCode(String tokenValue, Instant issuedAt, Instant expiresAt, boolean invalidated) {
			super(tokenValue, issuedAt, expiresAt, invalidated);
		}

	}

}
