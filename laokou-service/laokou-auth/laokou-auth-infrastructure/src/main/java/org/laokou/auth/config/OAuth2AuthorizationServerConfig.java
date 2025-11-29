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

package org.laokou.auth.config;

import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.admin.NewTopic;
import org.laokou.auth.config.authentication.OAuth2MailAuthenticationConverter;
import org.laokou.auth.config.authentication.OAuth2MobileAuthenticationConverter;
import org.laokou.auth.config.authentication.OAuth2TestAuthenticationConverter;
import org.laokou.auth.config.authentication.OAuth2UsernamePasswordAuthenticationConverter;
import org.laokou.auth.model.CaptchaValidator;
import org.laokou.auth.model.MqEnum;
import org.laokou.auth.model.PasswordValidator;
import org.laokou.common.context.util.User;
import org.laokou.common.fory.config.ForyFactory;
import org.laokou.common.i18n.dto.IdGenerator;
import org.laokou.common.i18n.util.ObjectUtils;
import org.laokou.common.redis.util.RedisUtils;
import org.laokou.common.security.config.RedisOAuth2AuthorizationConsentService;
import org.laokou.common.security.config.RedisRegisteredClientRepository;
import org.laokou.common.security.config.repository.OAuth2RegisteredClientRepository;
import org.laokou.common.security.config.repository.OAuth2UserConsentRepository;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.MediaType;
import org.springframework.kafka.core.KafkaAdmin;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.OAuth2Token;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.server.authorization.OAuth2Authorization;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationConsentService;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationService;
import org.springframework.security.oauth2.server.authorization.OAuth2TokenType;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.settings.AuthorizationServerSettings;
import org.springframework.security.oauth2.server.authorization.token.DelegatingOAuth2TokenGenerator;
import org.springframework.security.oauth2.server.authorization.token.JwtEncodingContext;
import org.springframework.security.oauth2.server.authorization.token.JwtGenerator;
import org.springframework.security.oauth2.server.authorization.token.OAuth2AccessTokenGenerator;
import org.springframework.security.oauth2.server.authorization.token.OAuth2RefreshTokenGenerator;
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenCustomizer;
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenGenerator;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.DelegatingAuthenticationConverter;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;
import org.springframework.security.web.util.matcher.MediaTypeRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;

import java.util.List;
import java.util.Set;

// @formatter:off
/**
 * 认证服务器配置. 自动装配JWKSource.
 *
 * @author laokou
 */
@RequiredArgsConstructor
@Configuration(proxyBeanMethods = false)
@ConditionalOnClass({OAuth2Authorization.class, OAuth2AuthorizationServerPropertiesMapper.class})
@ConditionalOnProperty(havingValue = "true", matchIfMissing = true, prefix = "spring.security.oauth2.authorization-server", name = "enabled")
class OAuth2AuthorizationServerConfig {

	private final RedisUtils redisUtils;

	static {
		ForyFactory.INSTANCE.register(org.laokou.auth.dto.domainevent.LoginEvent.class);
		ForyFactory.INSTANCE.register(org.laokou.auth.dto.domainevent.SendCaptchaEvent.class);
		ForyFactory.INSTANCE.register(java.net.URL.class);
		ForyFactory.INSTANCE.register(org.springframework.security.core.authority.SimpleGrantedAuthority.class);
	}

	// @formatter:on

	// @formatter:off
	/**
	 * OAuth2AuthorizationServer核心配置.
	 * @param http http配置
	 * @param authenticationProviders Provider集合
	 * @param authorizationServerSettings OAuth2配置
	 * @param authorizationService 认证配置
	 * @return 认证过滤器
	 */
	@Bean
	@Order(Ordered.HIGHEST_PRECEDENCE)
	SecurityFilterChain authorizationServerSecurityFilterChain(HttpSecurity http,
			List<AuthenticationProvider> authenticationProviders,
			AuthorizationServerSettings authorizationServerSettings,
			OAuth2AuthorizationService authorizationService)
			{
		return http.oauth2AuthorizationServer((authorizationServer) -> {
				http.securityMatcher(authorizationServer.getEndpointsMatcher());
				authorizationServer.oidc(Customizer.withDefaults())
					// https://docs.spring.io/spring-authorization-server/docs/current/reference/html/protocol-endpoints.html#oauth2-token-endpoint
					.tokenEndpoint((tokenEndpoint) -> tokenEndpoint.accessTokenRequestConverter(new DelegatingAuthenticationConverter(
							new OAuth2UsernamePasswordAuthenticationConverter(),
							new OAuth2TestAuthenticationConverter(),
							new OAuth2MobileAuthenticationConverter(),
							new OAuth2MailAuthenticationConverter()))
						.authenticationProviders(providers -> providers.addAll(authenticationProviders)))
					.authorizationService(authorizationService)
					.authorizationServerSettings(authorizationServerSettings);
			}).exceptionHandling(configurer -> configurer.defaultAuthenticationEntryPointFor(new LoginUrlAuthenticationEntryPoint("/login"), createRequestMatcher()))
			.build();
	}
	// @formatter:on

	/**
	 * 构造注册信息.
	 * @param authRegisteredClientRepository 注册信息
	 * @param propertiesMapper 配置映射
	 * @return 注册信息
	 */
	@Bean
	@ConditionalOnMissingBean(RegisteredClientRepository.class)
	RegisteredClientRepository registeredClientRepository(
			OAuth2RegisteredClientRepository authRegisteredClientRepository,
			OAuth2AuthorizationServerPropertiesMapper propertiesMapper) {
		RedisRegisteredClientRepository redisRegisteredClientRepository = new RedisRegisteredClientRepository(
				authRegisteredClientRepository);
		propertiesMapper.asRegisteredClients().forEach(redisRegisteredClientRepository::save);
		return redisRegisteredClientRepository;
	}

	/**
	 * 构建令牌生成器.
	 * @param jwtEncoder 加密编码
	 * @return 令牌生成器
	 */
	@Bean
	OAuth2TokenGenerator<OAuth2Token> tokenGenerator(JwtEncoder jwtEncoder) {
		JwtGenerator jwtGenerator = new JwtGenerator(jwtEncoder);
		jwtGenerator.setJwtCustomizer(jwtCustomizer());
		OAuth2AccessTokenGenerator accessTokenGenerator = new OAuth2AccessTokenGenerator();
		OAuth2RefreshTokenGenerator refreshTokenGenerator = new OAuth2RefreshTokenGenerator();
		return new DelegatingOAuth2TokenGenerator(jwtGenerator, accessTokenGenerator, refreshTokenGenerator);
	}

	/**
	 * 分布式ID生成器.
	 * @return 分布式ID生成器
	 */
	@Bean
	IdGenerator distributedIdentifierGenerator() {
		return System::currentTimeMillis;
	}

	/**
	 * 认证端点配置.
	 * @param propertiesMapper 属性映射器
	 * @return 认证端点配置
	 */
	@Bean
	@ConditionalOnMissingBean(AuthorizationServerSettings.class)
	AuthorizationServerSettings authorizationServerSettings(
			OAuth2AuthorizationServerPropertiesMapper propertiesMapper) {
		return propertiesMapper.asAuthorizationServerSettings();
	}

	/**
	 * 单点登录配置.
	 * @param passwordEncoder 密码编码器
	 * @param userDetailsServiceImpl 用户认证对象
	 * @return 单点登录配置
	 */
	@Bean
	AuthenticationProvider authenticationProvider(PasswordEncoder passwordEncoder,
			UserDetailsService userDetailsServiceImpl) {
		DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider(userDetailsServiceImpl);
		daoAuthenticationProvider.setPasswordEncoder(passwordEncoder);
		return daoAuthenticationProvider;
	}

	/**
	 * 认证授权配置.
	 * @param userConsentRepository 用户同意存储库
	 * @return 认证授权配置
	 */
	@Bean
	@ConditionalOnMissingBean(OAuth2AuthorizationConsentService.class)
	OAuth2AuthorizationConsentService authorizationConsentService(OAuth2UserConsentRepository userConsentRepository) {
		return new RedisOAuth2AuthorizationConsentService(userConsentRepository);
	}

	@Bean
	PasswordValidator passwordValidator(PasswordEncoder passwordEncoder) {
		return passwordEncoder::matches;
	}

	@Bean
	CaptchaValidator captchaValidator() {
		return this::validateCaptcha;
	}

	@Bean("authNewTopics")
	KafkaAdmin.NewTopics newTopics() {
		return new KafkaAdmin.NewTopics(new NewTopic(MqEnum.LOGIN_LOG_TOPIC, 3, (short) 1),
				new NewTopic(MqEnum.MAIL_CAPTCHA_TOPIC, 3, (short) 1),
				new NewTopic(MqEnum.MOBILE_CAPTCHA_TOPIC, 3, (short) 1));
	}

	private Boolean validateCaptcha(String key, String code) {
		// 获取验证码
		Object captcha = redisUtils.get(key);
		if (ObjectUtils.isNotNull(captcha)) {
			redisUtils.del(key);
		}
		else {
			return null;
		}
		return code.equalsIgnoreCase(captcha.toString());
	}

	private RequestMatcher createRequestMatcher() {
		MediaTypeRequestMatcher requestMatcher = new MediaTypeRequestMatcher(MediaType.TEXT_HTML);
		requestMatcher.setIgnoredMediaTypes(Set.of(MediaType.ALL));
		return requestMatcher;
	}

	// @formatter:off
	private OAuth2TokenCustomizer<JwtEncodingContext> jwtCustomizer() {
		// https://docs.spring.io/spring-security/reference/servlet/oauth2/authorization-server/core-model-components.html#oauth2AuthorizationServer-oauth2-token-customizer
		return context -> {
			if (ObjectUtils.equals(context.getTokenType(), OAuth2TokenType.ACCESS_TOKEN)
				&& context.getPrincipal().getPrincipal() instanceof User user) {
				JwtClaimsSet.Builder claims = context.getClaims();
				claims.claim("id", user.id().toString());
			}
		};
	}
	// @formatter:on

}
