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

package org.laokou.common.security.config.convertor;

import org.jetbrains.annotations.NotNull;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.WritingConverter;
import org.springframework.data.redis.serializer.Jackson3JsonRedisSerializer;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import tools.jackson.databind.ObjectMapper;
import tools.jackson.databind.json.JsonMapper;
import java.util.HashSet;

/**
 * @author spring-authorization-server
 * @author laokou
 */
@WritingConverter
public final class UsernamePasswordAuthenticationTokenToBytesConverter
		implements Converter<UsernamePasswordAuthenticationToken, byte[]> {

	private final Jackson3JsonRedisSerializer<UsernamePasswordAuthenticationToken> serializer;

	public UsernamePasswordAuthenticationTokenToBytesConverter() {
		ObjectMapper objectMapper = JsonMapper.builder()
			.addModule(SecurityJackson3Modules.getModules(BytesToUsernamePasswordAuthenticationTokenConverter.class.getClassLoader()))
			.addMixIn(HashSet.class, HashSetMixin.class)
			.build();

		this.serializer = new Jackson3JsonRedisSerializer<>(objectMapper, UsernamePasswordAuthenticationToken.class);
	}

	@Override
	public byte[] convert(@NotNull UsernamePasswordAuthenticationToken value) {
		return this.serializer.serialize(value);
	}

}
