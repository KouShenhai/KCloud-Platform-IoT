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

package org.laokou.common.security.config.convertor;

import com.redis.testcontainers.RedisContainer;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.laokou.common.testcontainers.util.DockerImageNames;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.Arrays;
import java.util.Collections;

/**
 * UsernamePasswordAuthenticationToken转换器集成测试类 - 使用Testcontainers.
 *
 * @author laokou
 */
@Testcontainers
class UsernamePasswordAuthenticationTokenConverterIntegrationTest {

	@Container
	static RedisContainer redisContainer = new RedisContainer(DockerImageNames.redis()).withExposedPorts(6379)
		.withReuse(true);

	private RedisTemplate<String, byte[]> redisTemplate;

	private UsernamePasswordAuthenticationTokenToBytesConverter toBytesConverter;

	private BytesToUsernamePasswordAuthenticationTokenConverter fromBytesConverter;

	@BeforeEach
	void setUp() {
		String redisHost = redisContainer.getHost();
		Integer redisPort = redisContainer.getMappedPort(6379);

		// 配置 Redis 连接
		RedisStandaloneConfiguration redisConfig = new RedisStandaloneConfiguration(redisHost, redisPort);
		LettuceConnectionFactory connectionFactory = new LettuceConnectionFactory(redisConfig);
		connectionFactory.afterPropertiesSet();

		// 配置 RedisTemplate
		redisTemplate = new RedisTemplate<>();
		redisTemplate.setConnectionFactory(connectionFactory);
		redisTemplate.setKeySerializer(new StringRedisSerializer());
		redisTemplate.afterPropertiesSet();

		// 初始化转换器
		toBytesConverter = new UsernamePasswordAuthenticationTokenToBytesConverter();
		fromBytesConverter = new BytesToUsernamePasswordAuthenticationTokenConverter();
	}

	@Test
	@DisplayName("Test authenticated token serialize and store to Redis then read back")
	void test_authenticated_token_serialize_and_store_to_redis() {
		// Given
		String key = "test:auth:token";
		UsernamePasswordAuthenticationToken token = UsernamePasswordAuthenticationToken.authenticated("admin", null,
				Arrays.asList(new SimpleGrantedAuthority("ROLE_ADMIN"), new SimpleGrantedAuthority("ROLE_USER")));

		// When - 序列化并存储
		byte[] bytes = toBytesConverter.convert(token);
		redisTemplate.opsForValue().set(key, bytes);

		// Then - 读取并反序列化
		byte[] storedBytes = redisTemplate.opsForValue().get(key);
		Assertions.assertThat(storedBytes).isNotNull();

		UsernamePasswordAuthenticationToken result = fromBytesConverter.convert(storedBytes);
		Assertions.assertThat(result).isNotNull();
		Assertions.assertThat(result.getName()).isEqualTo("admin");
		Assertions.assertThat(result.isAuthenticated()).isTrue();
		Assertions.assertThat(result.getAuthorities()).hasSize(2);
	}

	@Test
	@DisplayName("Test token full serialization roundtrip")
	void test_token_full_roundtrip() {
		// Given
		UsernamePasswordAuthenticationToken original = UsernamePasswordAuthenticationToken.authenticated("user123",
				null, Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER")));

		// When
		byte[] bytes = toBytesConverter.convert(original);
		UsernamePasswordAuthenticationToken result = fromBytesConverter.convert(bytes);

		// Then
		Assertions.assertThat(result).isNotNull();
		Assertions.assertThat(result.getName()).isEqualTo("user123");
		Assertions.assertThat(result.getAuthorities().stream().map(auth -> auth.getAuthority()))
			.containsExactly("ROLE_USER");
	}

	@Test
	@DisplayName("Test unauthenticated token serialization roundtrip")
	void test_unauthenticated_token_roundtrip() {
		// Given
		UsernamePasswordAuthenticationToken original = UsernamePasswordAuthenticationToken.unauthenticated("guest",
				null);

		// When
		byte[] bytes = toBytesConverter.convert(original);
		UsernamePasswordAuthenticationToken result = fromBytesConverter.convert(bytes);

		// Then
		Assertions.assertThat(result).isNotNull();
		Assertions.assertThat(result.getName()).isEqualTo("guest");
	}

	@Test
	@DisplayName("Test token with multiple authorities serialization roundtrip")
	void test_token_with_multiple_authorities_roundtrip() {
		// Given
		UsernamePasswordAuthenticationToken original = UsernamePasswordAuthenticationToken.authenticated("superadmin",
				null, Arrays.asList(new SimpleGrantedAuthority("ROLE_ADMIN"), new SimpleGrantedAuthority("ROLE_USER"),
						new SimpleGrantedAuthority("SCOPE_read"), new SimpleGrantedAuthority("SCOPE_write")));

		// When
		byte[] bytes = toBytesConverter.convert(original);
		UsernamePasswordAuthenticationToken result = fromBytesConverter.convert(bytes);

		// Then
		Assertions.assertThat(result).isNotNull();
		Assertions.assertThat(result.getAuthorities()).hasSize(4);
		Assertions.assertThat(result.getAuthorities().stream().map(auth -> auth.getAuthority()))
			.containsExactlyInAnyOrder("ROLE_ADMIN", "ROLE_USER", "SCOPE_read", "SCOPE_write");
	}

}
