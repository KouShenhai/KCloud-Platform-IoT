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

import org.laokou.common.security.config.entity.OAuth2UserConsent;
import org.laokou.common.security.config.repository.OAuth2UserConsentRepository;
import org.springframework.lang.Nullable;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationConsent;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationConsentService;
import org.springframework.util.Assert;

/**
 * @author spring-authorization-server
 * @author laokou
 */
public class RedisOAuth2AuthorizationConsentService implements OAuth2AuthorizationConsentService {

	private final OAuth2UserConsentRepository userConsentRepository;

	public RedisOAuth2AuthorizationConsentService(OAuth2UserConsentRepository userConsentRepository) {
		Assert.notNull(userConsentRepository, "UserConsentRepository cannot be null");
		this.userConsentRepository = userConsentRepository;
	}

	@Override
	public void save(OAuth2AuthorizationConsent authorizationConsent) {
		Assert.notNull(authorizationConsent, "AuthorizationConsent cannot be null");
		OAuth2UserConsent oauth2UserConsent = OAuth2ModelMapper.convertOAuth2UserConsent(authorizationConsent);
		this.userConsentRepository.save(oauth2UserConsent);
	}

	@Override
	public void remove(OAuth2AuthorizationConsent authorizationConsent) {
		Assert.notNull(authorizationConsent, "AuthorizationConsent cannot be null");
		this.userConsentRepository.deleteByRegisteredClientIdAndPrincipalName(
				authorizationConsent.getRegisteredClientId(), authorizationConsent.getPrincipalName());
	}

	@Nullable
	@Override
	public OAuth2AuthorizationConsent findById(String registeredClientId, String principalName) {
		Assert.hasText(registeredClientId, "RegisteredClientId cannot be empty");
		Assert.hasText(principalName, "PrincipalName cannot be empty");
		OAuth2UserConsent oauth2UserConsent = this.userConsentRepository
			.findByRegisteredClientIdAndPrincipalName(registeredClientId, principalName);
		return oauth2UserConsent != null ? OAuth2ModelMapper.convertOAuth2AuthorizationConsent(oauth2UserConsent)
				: null;
	}

}
