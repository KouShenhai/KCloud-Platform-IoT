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

package org.laokou.common.security.config.convertor;

import org.jetbrains.annotations.NotNull;
import org.laokou.common.security.config.entity.OAuth2AuthorizationGrantAuthorization;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.WritingConverter;
import org.springframework.data.redis.serializer.JacksonJsonRedisSerializer;
import org.springframework.security.jackson.SecurityJacksonModules;
import org.springframework.security.oauth2.server.authorization.jackson.OAuth2AuthorizationServerJacksonModule;
import tools.jackson.databind.ObjectMapper;
import tools.jackson.databind.json.JsonMapper;

/**
 * @author spring-authorization-server
 * @author laokou
 */
@WritingConverter
public final class ClaimsHolderToBytesConverter
		implements Converter<OAuth2AuthorizationGrantAuthorization.ClaimsHolder, byte[]> {

	private final JacksonJsonRedisSerializer<OAuth2AuthorizationGrantAuthorization.ClaimsHolder> serializer;

	public ClaimsHolderToBytesConverter() {
		ObjectMapper objectMapper = JsonMapper.builder()
			.addModules(SecurityJacksonModules.getModules(ClaimsHolderToBytesConverter.class.getClassLoader()))
			.addModule(new OAuth2AuthorizationServerJacksonModule())
			.addMixIn(OAuth2AuthorizationGrantAuthorization.ClaimsHolder.class, ClaimsHolderMixin.class)
			.build();
		this.serializer = new JacksonJsonRedisSerializer<>(objectMapper,
				OAuth2AuthorizationGrantAuthorization.ClaimsHolder.class);
	}

	@Override
	public byte[] convert(@NotNull OAuth2AuthorizationGrantAuthorization.ClaimsHolder value) {
		return this.serializer.serialize(value);
	}

}
