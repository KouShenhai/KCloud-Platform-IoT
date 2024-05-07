/*
 * Copyright (c) 2022-2024 KCloud-Platform-IoT Author or Authors. All Rights Reserved.
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

import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationService;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;

import static org.springframework.data.redis.core.RedisKeyValueAdapter.EnableKeyspaceEvents.ON_STARTUP;

/**
 * @author laokou
 */
@AutoConfiguration
@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET)
@EnableRedisRepositories(enableKeyspaceEvents = ON_STARTUP, basePackages = { "org.laokou.common.security.config" })
public class OAuth2AuthorizationAutoConfig {

	/**
	 * 认证配置.
	 * @param redisOAuth2AuthorizationRepository Redis缓存
	 * @param registeredClientRepository 注册客户端
	 * @return OAuth2AuthorizationService
	 */
	@Bean
	@ConditionalOnMissingBean(OAuth2AuthorizationService.class)
	OAuth2AuthorizationService auth2AuthorizationService(
			RedisOAuth2AuthorizationRepository redisOAuth2AuthorizationRepository,
			RegisteredClientRepository registeredClientRepository) {
		return new RedisOAuth2AuthorizationService(redisOAuth2AuthorizationRepository, registeredClientRepository);
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
