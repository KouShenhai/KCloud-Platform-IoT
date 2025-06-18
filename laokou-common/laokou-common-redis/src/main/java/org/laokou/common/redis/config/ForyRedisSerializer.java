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

import org.laokou.common.fory.config.ForyFactory;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.SerializationException;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.nio.charset.StandardCharsets;

/**
 * @author laokou
 */
public final class ForyRedisSerializer implements RedisSerializer<Object> {

	@Override
	public byte[] serialize(Object obj) throws SerializationException {
		return ForyFactory.INSTANCE.serialize(obj);
	}

	@Override
	public Object deserialize(byte[] bytes) throws SerializationException {
		return ForyFactory.INSTANCE.deserialize(bytes);
	}

	public static StringRedisSerializer getStringRedisSerializer() {
		return new Md5DigestStringRedisSerializer(StandardCharsets.UTF_8);
	}

	public static ForyRedisSerializer foryRedisSerializer() {
		// Json序列化配置
		return new ForyRedisSerializer();
	}

}
