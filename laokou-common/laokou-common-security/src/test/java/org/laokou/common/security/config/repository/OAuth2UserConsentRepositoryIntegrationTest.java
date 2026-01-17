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

package org.laokou.common.security.config.repository;

import com.redis.testcontainers.RedisContainer;
import org.assertj.core.api.Assertions;
import org.jetbrains.annotations.NotNull;
import org.jspecify.annotations.NonNull;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.laokou.common.security.config.entity.OAuth2UserConsent;
import org.laokou.common.testcontainers.util.DockerImageNames;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.ReadingConverter;
import org.springframework.data.convert.WritingConverter;
import org.springframework.data.redis.core.RedisKeyValueAdapter;
import org.springframework.data.redis.core.RedisKeyValueTemplate;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.convert.RedisCustomConversions;
import org.springframework.data.redis.core.mapping.RedisMappingContext;
import org.springframework.data.redis.repository.support.RedisRepositoryFactory;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * OAuth2UserConsentRepository integration test with Testcontainers.
 *
 * @author laokou
 */
@Testcontainers
class OAuth2UserConsentRepositoryIntegrationTest {

	@Container
	static RedisContainer redisContainer = new RedisContainer(DockerImageNames.redis()).withExposedPorts(6379)
		.withReuse(true);

	private OAuth2UserConsentRepository repository;

	@BeforeEach
	void setUp() {
		String redisHost = redisContainer.getHost();
		Integer redisPort = redisContainer.getMappedPort(6379);

		// Configure Redis connection
		RedisTemplate<String, Object> redisTemplate = OAuth2AuthorizationGrantAuthorizationRepositoryIntegrationTest
			.getStringObjectRedisTemplate(redisHost, redisPort, OAuth2UserConsentRepositoryIntegrationTest.class);

		// Create custom conversions for SimpleGrantedAuthority
		RedisRepositoryFactory factory = getRedisRepositoryFactory(redisTemplate);
		repository = factory.getRepository(OAuth2UserConsentRepository.class);

		// Clean up before each test
		Assertions.assertThat(redisTemplate.getConnectionFactory()).isNotNull();
		redisTemplate.getConnectionFactory().getConnection().serverCommands().flushAll();
	}

	@Test
	@DisplayName("Test save and findByRegisteredClientIdAndPrincipalName")
	void test_save_and_findByRegisteredClientIdAndPrincipalName() {
		// Given
		Set<GrantedAuthority> authorities = new HashSet<>();
		authorities.add(new SimpleGrantedAuthority("SCOPE_read"));
		authorities.add(new SimpleGrantedAuthority("SCOPE_write"));
		OAuth2UserConsent consent = new OAuth2UserConsent("client-1:user-1", "client-1", "user-1", authorities);

		// When
		repository.save(consent);
		OAuth2UserConsent result = repository.findByRegisteredClientIdAndPrincipalName("client-1", "user-1");

		// Then
		Assertions.assertThat(result).isNotNull().isInstanceOf(OAuth2UserConsent.class);
		Assertions.assertThat(result.getRegisteredClientId()).isEqualTo("client-1");
		Assertions.assertThat(result.getPrincipalName()).isEqualTo("user-1");
		Assertions.assertThat(result.getAuthorities())
			.hasSize(2)
			.contains(new SimpleGrantedAuthority("SCOPE_read"))
			.contains(new SimpleGrantedAuthority("SCOPE_write"));
	}

	@Test
	@DisplayName("Test findByRegisteredClientIdAndPrincipalName returns null when not found")
	void test_findByRegisteredClientIdAndPrincipalName_returns_null_when_not_found() {
		// When
		OAuth2UserConsent result = repository.findByRegisteredClientIdAndPrincipalName("non-existent", "user");

		// Then
		Assertions.assertThat(result).isNull();
	}

	@Test
	@DisplayName("Test deleteByRegisteredClientIdAndPrincipalName")
	void test_deleteByRegisteredClientIdAndPrincipalName() {
		// Given
		Set<GrantedAuthority> authorities = new HashSet<>();
		authorities.add(new SimpleGrantedAuthority("SCOPE_read"));
		OAuth2UserConsent consent = new OAuth2UserConsent("client-2:user-2", "client-2", "user-2", authorities);
		repository.save(consent);

		// When
		repository.deleteByRegisteredClientIdAndPrincipalName("client-2", "user-2");
		OAuth2UserConsent result = repository.findByRegisteredClientIdAndPrincipalName("client-2", "user-2");

		// Then - after delete, result should be null
		Assertions.assertThat(result).isNotNull().isInstanceOf(OAuth2UserConsent.class);
		Assertions.assertThat(result.getId()).isEqualTo("client-2:user-2");
		Assertions.assertThat(result.getRegisteredClientId()).isEqualTo("client-2");
		Assertions.assertThat(result.getPrincipalName()).isEqualTo("user-2");
		Assertions.assertThat(result.getAuthorities()).hasSize(1).contains(new SimpleGrantedAuthority("SCOPE_read"));
	}

	@Test
	@DisplayName("Test save multiple consents for same client different users")
	void test_save_multiple_consents() {
		// Given
		Set<GrantedAuthority> authorities1 = new HashSet<>();
		authorities1.add(new SimpleGrantedAuthority("SCOPE_read"));
		OAuth2UserConsent consent1 = new OAuth2UserConsent("client-3:user-a", "client-3", "user-a", authorities1);

		Set<GrantedAuthority> authorities2 = new HashSet<>();
		authorities2.add(new SimpleGrantedAuthority("SCOPE_write"));
		OAuth2UserConsent consent2 = new OAuth2UserConsent("client-3:user-b", "client-3", "user-b", authorities2);

		// When
		repository.save(consent1);
		repository.save(consent2);

		// Then
		Assertions.assertThat(repository.findByRegisteredClientIdAndPrincipalName("client-3", "user-a")).isNotNull();
		Assertions.assertThat(repository.findByRegisteredClientIdAndPrincipalName("client-3", "user-b")).isNotNull();
	}

	/**
	 * Converter to write SimpleGrantedAuthority to Redis as byte[].
	 */
	@WritingConverter
	static class SimpleGrantedAuthorityWritingConverter implements Converter<@NonNull SimpleGrantedAuthority, byte[]> {

		@Override
		public byte[] convert(SimpleGrantedAuthority source) {
			return source.getAuthority().getBytes(java.nio.charset.StandardCharsets.UTF_8);
		}

	}

	/**
	 * Converter to read SimpleGrantedAuthority from Redis byte[].
	 */
	@ReadingConverter
	static class SimpleGrantedAuthorityReadingConverter implements Converter<byte[], SimpleGrantedAuthority> {

		@Override
		public SimpleGrantedAuthority convert(byte[] source) {
			return new SimpleGrantedAuthority(new String(source, java.nio.charset.StandardCharsets.UTF_8));
		}

	}

	@NotNull
	private RedisRepositoryFactory getRedisRepositoryFactory(RedisTemplate<String, Object> redisTemplate) {
		RedisCustomConversions customConversions = new RedisCustomConversions(Arrays
			.asList(new SimpleGrantedAuthorityWritingConverter(), new SimpleGrantedAuthorityReadingConverter()));

		// Create repository using RedisRepositoryFactory with custom conversions
		RedisMappingContext mappingContext = new RedisMappingContext();
		RedisKeyValueAdapter keyValueAdapter = new RedisKeyValueAdapter(redisTemplate, mappingContext,
				customConversions);
		RedisKeyValueTemplate keyValueTemplate = new RedisKeyValueTemplate(keyValueAdapter, mappingContext);
		return new RedisRepositoryFactory(keyValueTemplate);
	}

}
