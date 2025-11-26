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
import org.laokou.common.context.util.UserExtDetails;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.ReadingConverter;
import org.springframework.data.redis.serializer.JacksonJsonRedisSerializer;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.jackson.SecurityJacksonModules;
import tools.jackson.databind.MapperFeature;
import tools.jackson.databind.ObjectMapper;
import tools.jackson.databind.json.JsonMapper;

import java.util.HashSet;

/**
 * @author spring-authorization-server
 * @author laokou
 */
@ReadingConverter
public final class BytesToUsernamePasswordAuthenticationTokenConverter
		implements Converter<byte[], UsernamePasswordAuthenticationToken> {

	private final JacksonJsonRedisSerializer<UsernamePasswordAuthenticationToken> serializer;

	public BytesToUsernamePasswordAuthenticationTokenConverter() {
		ObjectMapper objectMapper = JsonMapper.builder()
			.configure(MapperFeature.PROPAGATE_TRANSIENT_MARKER, true)
			.addModules(SecurityJacksonModules
				.getModules(BytesToUsernamePasswordAuthenticationTokenConverter.class.getClassLoader()))
			.addMixIn(HashSet.class, HashSetMixin.class)
			.addMixIn(UserExtDetails.class, UserExtDetailsMixin.class)
			.build();
		this.serializer = new JacksonJsonRedisSerializer<>(objectMapper, UsernamePasswordAuthenticationToken.class);
	}

	@Override
	public UsernamePasswordAuthenticationToken convert(@NotNull byte[] value) {
		return this.serializer.deserialize(value);
	}

}
