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

import org.laokou.common.security.config.entity.OAuth2RegisteredClient;
import org.laokou.common.security.config.repository.OAuth2RegisteredClientRepository;
import org.springframework.lang.Nullable;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.util.Assert;

public class RedisRegisteredClientRepository implements RegisteredClientRepository {

	private final OAuth2RegisteredClientRepository registeredClientRepository;

	public RedisRegisteredClientRepository(OAuth2RegisteredClientRepository registeredClientRepository) {
		Assert.notNull(registeredClientRepository, "RegisteredClientRepository cannot be null");
		this.registeredClientRepository = registeredClientRepository;
	}

	@Override
	public void save(RegisteredClient registeredClient) {
		Assert.notNull(registeredClient, "RegisteredClient cannot be null");
		OAuth2RegisteredClient oauth2RegisteredClient = OAuth2ModelMapper
			.convertOAuth2RegisteredClient(registeredClient);
		this.registeredClientRepository.save(oauth2RegisteredClient);
	}

	@Nullable
	@Override
	public RegisteredClient findById(String id) {
		Assert.hasText(id, "Id cannot be empty");
		return this.registeredClientRepository.findById(id)
			.map(OAuth2ModelMapper::convertRegisteredClient)
			.orElse(null);
	}

	@Nullable
	@Override
	public RegisteredClient findByClientId(String clientId) {
		Assert.hasText(clientId, "ClientId cannot be empty");
		OAuth2RegisteredClient oauth2RegisteredClient = this.registeredClientRepository.findByClientId(clientId);
		return oauth2RegisteredClient != null ? OAuth2ModelMapper.convertRegisteredClient(oauth2RegisteredClient)
				: null;
	}

}
