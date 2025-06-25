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

import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.admin.NewTopic;
import org.laokou.auth.model.CaptchaValidator;
import org.laokou.auth.model.PasswordValidator;
import org.laokou.common.fory.config.ForyFactory;
import org.laokou.common.i18n.util.ObjectUtils;
import org.laokou.common.redis.util.RedisUtils;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.autoconfigure.security.oauth2.server.servlet.OAuth2AuthorizationServerJwtAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.kafka.core.KafkaAdmin;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.OAuth2Token;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;
import org.springframework.security.oauth2.server.authorization.JdbcOAuth2AuthorizationConsentService;
import org.springframework.security.oauth2.server.authorization.OAuth2Authorization;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationConsentService;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationService;
import org.springframework.security.oauth2.server.authorization.client.JdbcRegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configuration.OAuth2AuthorizationServerConfiguration;
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configurers.OAuth2AuthorizationServerConfigurer;
import org.springframework.security.oauth2.server.authorization.settings.AuthorizationServerSettings;
import org.springframework.security.oauth2.server.authorization.token.*;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationConverter;
import org.springframework.security.web.authentication.DelegatingAuthenticationConverter;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;
import org.springframework.security.web.util.matcher.MediaTypeRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import static org.laokou.auth.model.MqEnum.*;
import static org.laokou.common.crypto.util.RSAUtils.RSA;
import static org.springframework.core.Ordered.HIGHEST_PRECEDENCE;

// @formatter:off
/**
 * 认证服务器配置. 自动装配JWKSource {@link OAuth2AuthorizationServerJwtAutoConfiguration}.
 *
 * @author laokou
 */
@RequiredArgsConstructor
@Configuration(proxyBeanMethods = false)
@ConditionalOnClass({OAuth2Authorization.class, OAuth2AuthorizationServerPropertiesMapper.class})
@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET)
@ConditionalOnProperty(havingValue = "true", matchIfMissing = true, prefix = "spring.security.oauth2.authorization-server", name = "enabled")
class OAuth2AuthorizationServerConfig {

	private final RedisUtils redisUtils;

	static {
		ForyFactory.INSTANCE.register(org.laokou.auth.dto.domainevent.LoginEvent.class);
		ForyFactory.INSTANCE.register(org.laokou.auth.dto.domainevent.SendCaptchaEvent.class);
	}

	private static void applyDefaultSecurity(HttpSecurity http) throws Exception {
        OAuth2AuthorizationServerConfigurer authorizationServerConfigurer = OAuth2AuthorizationServerConfigurer.authorizationServer();
        http.securityMatcher(authorizationServerConfigurer.getEndpointsMatcher())
                .with(authorizationServerConfigurer, Customizer.withDefaults())
                .authorizeHttpRequests((authorize) -> authorize.anyRequest().authenticated());
	}
	// @formatter:on

	// @formatter:off
	/**
	 * OAuth2AuthorizationServer核心配置.
	 * @param http http配置
	 * @param usernamePasswordAuthenticationProvider 用户名密码认证Provider
	 * @param mailAuthenticationProvider 邮箱认证Provider
	 * @param mobileAuthenticationProvider 手机号认证Provider
	 * @param testAuthenticationProvider 测试认证Provider
	 * @param authorizationServerSettings OAuth2配置
	 * @param authorizationService 认证配置
	 * @param mailAuthenticationConverter 邮箱认证Converter
	 * @param mobileAuthenticationConverter 手机号认证Converter
	 * @param testAuthenticationConverter 测试认证Converter
	 * @param usernamePasswordAuthenticationConverter 用户名密码认证Converter
	 * @return 认证过滤器
	 * @throws Exception 异常
	 */
	@Bean
	@Order(HIGHEST_PRECEDENCE)
	SecurityFilterChain authorizationServerSecurityFilterChain(HttpSecurity http,
			AuthenticationProvider usernamePasswordAuthenticationProvider,
			AuthenticationProvider testAuthenticationProvider,
			AuthenticationProvider mailAuthenticationProvider,
			AuthenticationProvider mobileAuthenticationProvider,
			AuthenticationConverter usernamePasswordAuthenticationConverter,
			AuthenticationConverter testAuthenticationConverter,
			AuthenticationConverter mailAuthenticationConverter,
			AuthenticationConverter mobileAuthenticationConverter,
			AuthorizationServerSettings authorizationServerSettings,
			OAuth2AuthorizationService authorizationService)
			throws Exception {
		// https://docs.spring.io/spring-authorization-server/docs/current/reference/html/configuration-model.html
		OAuth2AuthorizationServerConfig.applyDefaultSecurity(http);
		http.getConfigurer(OAuth2AuthorizationServerConfigurer.class)
			// https://docs.spring.io/spring-authorization-server/docs/current/reference/html/protocol-endpoints.html#oauth2-token-endpoint
			.tokenEndpoint((tokenEndpoint) -> tokenEndpoint
				.accessTokenRequestConverter(
					new DelegatingAuthenticationConverter(List.of(usernamePasswordAuthenticationConverter,
							testAuthenticationConverter,
							mobileAuthenticationConverter,
							mailAuthenticationConverter)
					)
				)
				.authenticationProvider(usernamePasswordAuthenticationProvider)
				.authenticationProvider(mobileAuthenticationProvider)
				.authenticationProvider(testAuthenticationProvider)
				.authenticationProvider(mailAuthenticationProvider))
			.oidc(Customizer.withDefaults())
			.authorizationService(authorizationService)
			.authorizationServerSettings(authorizationServerSettings);
		return http.exceptionHandling(configurer -> configurer.defaultAuthenticationEntryPointFor(new LoginUrlAuthenticationEntryPoint("/login"), createRequestMatcher()))
			.build();
	}
	// @formatter:on

	/**
	 * 构造注册信息.
	 * @param propertiesMapper 配置
	 * @param jdbcTemplate JDBC模板
	 * @return 注册信息
	 */
	@Bean
	@ConditionalOnMissingBean(RegisteredClientRepository.class)
	RegisteredClientRepository registeredClientRepository(JdbcTemplate jdbcTemplate,
			OAuth2AuthorizationServerPropertiesMapper propertiesMapper) {
		JdbcRegisteredClientRepository registeredClientRepository = new JdbcRegisteredClientRepository(jdbcTemplate);
		propertiesMapper.asRegisteredClients().forEach(registeredClientRepository::save);
		return registeredClientRepository;
	}

	/**
	 * jwt编码器.
	 * @param jwkSource jwk来源
	 * @return jwt编码器
	 */
	@Bean
	JwtEncoder jwtEncoder(JWKSource<SecurityContext> jwkSource) {
		return new NimbusJwtEncoder(jwkSource);
	}

	/**
	 * jwt解码器.
	 * @param jwkSource jwk来源
	 * @return jwt解码器
	 */
	@Bean
	JwtDecoder jwtDecoder(JWKSource<SecurityContext> jwkSource) {
		return OAuth2AuthorizationServerConfiguration.jwtDecoder(jwkSource);
	}

	/**
	 * 获取jwk来源.
	 * @return jwk来源
	 */
	@Bean
	JWKSource<SecurityContext> jwkSource() {
		RSAKey rsaKey = getRsaKey();
		JWKSet jwkSet = new JWKSet(rsaKey);
		return (jwkSelector, securityContext) -> jwkSelector.select(jwkSet);
	}

	/**
	 * 构建令牌生成器.
	 * @param jwtEncoder 加密编码
	 * @return 令牌生成器
	 */
	@Bean
	OAuth2TokenGenerator<OAuth2Token> tokenGenerator(JwtEncoder jwtEncoder) {
		JwtGenerator generator = new JwtGenerator(jwtEncoder);
		return new DelegatingOAuth2TokenGenerator(generator, new OAuth2AccessTokenGenerator(),
				new OAuth2RefreshTokenGenerator());
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
	 * @param jdbcTemplate JDBC模板
	 * @param registeredClientRepository 注册信息
	 * @return 认证授权配置
	 */
	@Bean
	@ConditionalOnMissingBean(OAuth2AuthorizationConsentService.class)
	OAuth2AuthorizationConsentService authorizationConsentService(JdbcTemplate jdbcTemplate,
			RegisteredClientRepository registeredClientRepository) {
		return new JdbcOAuth2AuthorizationConsentService(jdbcTemplate, registeredClientRepository);
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
		return new KafkaAdmin.NewTopics(new NewTopic(LOGIN_LOG_TOPIC, 3, (short) 1),
				new NewTopic(MAIL_CAPTCHA_TOPIC, 3, (short) 1), new NewTopic(MOBILE_CAPTCHA_TOPIC, 3, (short) 1));
	}

	/**
	 * 获取RSA加密Key.
	 * @return RSA加密Key
	 */
	private RSAKey getRsaKey() {
		KeyPair keyPair = generateRsaKey();
		RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();
		RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();
		return (new RSAKey.Builder(publicKey)).privateKey(privateKey).keyID(UUID.randomUUID().toString()).build();
	}

	/**
	 * 生成RSA加密Key.
	 * @return 生成结果
	 */
	private KeyPair generateRsaKey() {
		try {
			KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance(RSA);
			keyPairGenerator.initialize(2048);
			return keyPairGenerator.generateKeyPair();
		}
		catch (Exception var2) {
			throw new IllegalStateException(var2);
		}
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

	private static RequestMatcher createRequestMatcher() {
		MediaTypeRequestMatcher requestMatcher = new MediaTypeRequestMatcher(MediaType.TEXT_HTML);
		requestMatcher.setIgnoredMediaTypes(Set.of(MediaType.ALL));
		return requestMatcher;
	}

}
