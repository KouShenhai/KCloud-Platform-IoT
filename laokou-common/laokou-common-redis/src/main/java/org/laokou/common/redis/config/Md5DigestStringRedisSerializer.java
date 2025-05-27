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

package org.laokou.common.redis.config;

import io.micrometer.common.lang.Nullable;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.util.Assert;
import org.springframework.util.DigestUtils;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

/**
 * @author laokou
 */
public class Md5DigestStringRedisSerializer extends StringRedisSerializer {

	public Md5DigestStringRedisSerializer(Charset charset) {
		super(charset);
	}

	@Nullable
	@Override
	public byte[] serialize(@Nullable String value) {
		Assert.notNull(value, "Cannot serialize null");
		return super.serialize(DigestUtils.md5DigestAsHex(value.getBytes(StandardCharsets.UTF_8)));
	}

}
