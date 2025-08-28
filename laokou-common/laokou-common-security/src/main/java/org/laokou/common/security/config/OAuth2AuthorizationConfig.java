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

package org.laokou.common.security.config;

import org.laokou.common.fory.config.ForyFactory;
import org.laokou.common.security.config.convertor.*;
import org.laokou.common.security.config.repository.OAuth2AuthorizationGrantAuthorizationRepository;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.convert.RedisCustomConversions;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationService;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;

import java.util.Arrays;

/**
 * @author laokou
 */
@Configuration(proxyBeanMethods = false)
@EnableRedisRepositories(basePackages = { "org.laokou.common.security.config.repository" })
public class OAuth2AuthorizationConfig {

	static {
		ForyFactory.INSTANCE.register(org.laokou.common.context.util.UserDetails.class);
	}

	/**
	 * 认证配置.
	 * @param authorizationGrantAuthorizationRepository 认证
	 * @param registeredClientRepository 注册客户端
	 * @return OAuth2AuthorizationService
	 */
	@Bean
	@ConditionalOnMissingBean(OAuth2AuthorizationService.class)
	OAuth2AuthorizationService auth2AuthorizationService(
		RegisteredClientRepository registeredClientRepository,
		OAuth2AuthorizationGrantAuthorizationRepository authorizationGrantAuthorizationRepository) {
		return new RedisOAuth2AuthorizationService(registeredClientRepository, authorizationGrantAuthorizationRepository);
	}

	@Bean
	public RedisCustomConversions redisCustomConversions() {
		return new RedisCustomConversions(Arrays.asList(new UsernamePasswordAuthenticationTokenToBytesConverter(),
			new BytesToUsernamePasswordAuthenticationTokenConverter(),
			new OAuth2AuthorizationRequestToBytesConverter(),
			new BytesToOAuth2AuthorizationRequestConverter(),
			new ClaimsHolderToBytesConverter(),
			new BytesToClaimsHolderConverter()));
	}

	/**
	 * 密码编码.
	 * @return 密码编码器
	 */
	@Bean
	@ConditionalOnMissingBean(PasswordEncoder.class)
	PasswordEncoder passwordEncoder() {
		return PasswordEncoderFactories.createDelegatingPasswordEncoder();
	}

}
