/*
 * Copyright (c) 2022 KCloud-Platform-Alibaba Authors. All Rights Reserved.
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

import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;
import org.laokou.common.easy.captcha.service.SysCaptchaService;
import org.laokou.common.log.utils.LoginLogUtil;
import org.laokou.common.redis.utils.RedisUtil;
import org.laokou.common.tenant.service.SysSourceService;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.autoconfigure.security.oauth2.server.servlet.OAuth2AuthorizationServerJwtAutoConfiguration;
import org.springframework.boot.context.properties.PropertyMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.security.oauth2.core.OAuth2Token;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;
import org.springframework.security.oauth2.server.authorization.JdbcOAuth2AuthorizationConsentService;
import org.springframework.security.oauth2.server.authorization.JdbcOAuth2AuthorizationService;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationConsentService;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationService;
import org.springframework.security.oauth2.server.authorization.client.JdbcRegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configuration.OAuth2AuthorizationServerConfiguration;
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configurers.OAuth2AuthorizationServerConfigurer;
import org.springframework.security.oauth2.server.authorization.settings.AuthorizationServerSettings;
import org.springframework.security.oauth2.server.authorization.settings.ClientSettings;
import org.springframework.security.oauth2.server.authorization.settings.TokenSettings;
import org.springframework.security.oauth2.server.authorization.token.DelegatingOAuth2TokenGenerator;
import org.springframework.security.oauth2.server.authorization.token.JwtGenerator;
import org.springframework.security.oauth2.server.authorization.token.OAuth2RefreshTokenGenerator;
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenGenerator;
import org.springframework.security.oauth2.server.authorization.web.authentication.*;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;

import java.util.List;

/**
 * 自动装配JWKSource {@link OAuth2AuthorizationServerJwtAutoConfiguration}
 *
 * @author laokou
 */
@Configuration
@ConditionalOnProperty(havingValue = "true", matchIfMissing = true, prefix = OAuth2AuthorizationServerProperties.PREFIX,
		name = "enabled")
@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET)
class OAuth2AuthorizationServerConfig {

	/**
	 * 登录
	 */
	private static final String LOGIN_PATTERN = "/login";

	/**
	 * <a href=
	 * "https://docs.spring.io/spring-authorization-server/docs/current/reference/html/configuration-model.html">...</a>
	 * OAuth2AuthorizationServer核心配置
	 */
	@Bean
	@Order(Ordered.HIGHEST_PRECEDENCE)
	SecurityFilterChain authorizationServerSecurityFilterChain(HttpSecurity http,
			AuthorizationServerSettings authorizationServerSettings, OAuth2AuthorizationService authorizationService,
			SysUserService sysUserService, SysMenuService sysMenuService, SysDeptService sysDeptService,
			LoginLogUtil loginLogUtil, PasswordEncoder passwordEncoder, SysCaptchaService sysCaptchaService,
			OAuth2TokenGenerator<? extends OAuth2Token> tokenGenerator, SysSourceService sysSourceService,
			RedisUtil redisUtil) throws Exception {
		OAuth2AuthorizationServerConfiguration.applyDefaultSecurity(http);
		OAuth2AuthorizationServerConfigurer oAuth2AuthorizationServerConfigurer = http
				.getConfigurer(OAuth2AuthorizationServerConfigurer.class)
				// https://docs.spring.io/spring-authorization-server/docs/current/reference/html/protocol-endpoints.html#oauth2-token-endpoint
				.tokenEndpoint((tokenEndpoint) -> tokenEndpoint
						.accessTokenRequestConverter(new DelegatingAuthenticationConverter(List.of(
								new OAuth2PasswordAuthenticationConverter(),
								new OAuth2DeviceCodeAuthenticationConverter(),
								new OAuth2MobileAuthenticationConverter(), new OAuth2MailAuthenticationConverter(),
								new OAuth2AuthorizationCodeAuthenticationConverter(),
								new OAuth2ClientCredentialsAuthenticationConverter(),
								new OAuth2RefreshTokenAuthenticationConverter(),
								new OAuth2TokenIntrospectionAuthenticationConverter(),
								new OAuth2TokenRevocationAuthenticationConverter(),
								new PublicClientAuthenticationConverter(),
								new ClientSecretBasicAuthenticationConverter(),
								new ClientSecretPostAuthenticationConverter(),
								new OAuth2AuthorizationConsentAuthenticationConverter(),
								new OAuth2DeviceAuthorizationConsentAuthenticationConverter(),
								new OAuth2DeviceAuthorizationRequestAuthenticationConverter(),
								new OAuth2DeviceVerificationAuthenticationConverter(),
								new JwtClientAssertionAuthenticationConverter(),
								new OAuth2AuthorizationCodeRequestAuthenticationConverter())))
						.authenticationProvider(new OAuth2PasswordAuthenticationProvider(sysUserService, sysMenuService,
								sysDeptService, loginLogUtil, passwordEncoder, sysCaptchaService, authorizationService,
								tokenGenerator, sysSourceService, redisUtil))
						.authenticationProvider(new OAuth2MobileAuthenticationProvider(sysUserService, sysMenuService,
								sysDeptService, loginLogUtil, passwordEncoder, sysCaptchaService, authorizationService,
								tokenGenerator, sysSourceService, redisUtil))
						.authenticationProvider(new OAuth2MailAuthenticationProvider(sysUserService, sysMenuService,
								sysDeptService, loginLogUtil, passwordEncoder, sysCaptchaService, authorizationService,
								tokenGenerator, sysSourceService, redisUtil)))
				.oidc(Customizer.withDefaults()).authorizationService(authorizationService)
				.authorizationServerSettings(authorizationServerSettings);
		http.apply(oAuth2AuthorizationServerConfigurer);
		return http.exceptionHandling(
				configurer -> configurer.authenticationEntryPoint(new LoginUrlAuthenticationEntryPoint(LOGIN_PATTERN)))
				.build();
	}

	/**
	 * 注册信息
	 */
	@Bean
	@ConditionalOnMissingBean(RegisteredClientRepository.class)
	RegisteredClientRepository registeredClientRepository(JdbcTemplate jdbcTemplate,
			OAuth2AuthorizationServerProperties properties) {
		OAuth2AuthorizationServerProperties.Client client = properties.getClient();
		OAuth2AuthorizationServerProperties.Token token = properties.getToken();
		OAuth2AuthorizationServerProperties.Registration registration = properties.getRegistration();
		RegisteredClient.Builder registrationBuilder = RegisteredClient.withId(registration.getId());
		TokenSettings.Builder tokenBuilder = TokenSettings.builder();
		ClientSettings.Builder clientBuilder = ClientSettings.builder();
		PropertyMapper map = PropertyMapper.get().alwaysApplyingWhenNonNull();
		// 令牌 => JWT配置
		map.from(token::getAccessTokenTimeToLive).to(tokenBuilder::accessTokenTimeToLive);
		map.from(token::getRefreshTokenTimeToLive).to(tokenBuilder::refreshTokenTimeToLive);
		map.from(token::getAuthorizationCodeTimeToLive).to(tokenBuilder::authorizationCodeTimeToLive);
		// 客户端配置，包括验证密钥或需要授权页面
		map.from(client::isRequireAuthorizationConsent).to(clientBuilder::requireAuthorizationConsent);
		// 注册
		// Base64编码
		// ClientAuthenticationMethod.CLIENT_SECRET_BASIC => client_id:client_secret
		map.from(registration::getClientId).to(registrationBuilder::clientId);
		map.from(registration::getClientName).to(registrationBuilder::clientName);
		map.from(registration::getClientSecret).to(registrationBuilder::clientSecret);
		registration.getClientAuthenticationMethods()
				.forEach(clientAuthenticationMethod -> map.from(clientAuthenticationMethod).whenNonNull()
						.as(ClientAuthenticationMethod::new).to(registrationBuilder::clientAuthenticationMethod));
		registration.getAuthorizationGrantTypes().forEach(authorizationGrantType -> map.from(authorizationGrantType)
				.whenNonNull().as(AuthorizationGrantType::new).to(registrationBuilder::authorizationGrantType));
		registration.getScopes().forEach(scope -> map.from(scope).whenNonNull().to(registrationBuilder::scope));
		registration.getRedirectUris()
				.forEach(redirectUri -> map.from(redirectUri).whenNonNull().to(registrationBuilder::redirectUri));
		registration.getPostLogoutRedirectUris().forEach(
				redirectUri -> map.from(redirectUri).whenNonNull().to(registrationBuilder::postLogoutRedirectUri));
		registrationBuilder.tokenSettings(tokenBuilder.build());
		registrationBuilder.clientSettings(clientBuilder.build());
		JdbcRegisteredClientRepository registeredClientRepository = new JdbcRegisteredClientRepository(jdbcTemplate);
		registeredClientRepository.save(registrationBuilder.build());
		return registeredClientRepository;
	}

	/**
	 * 配置
	 */
	@Bean
	OAuth2TokenGenerator<OAuth2Token> oAuth2TokenGenerator(JwtEncoder jwtEncoder) {
		JwtGenerator generator = new JwtGenerator(jwtEncoder);
		return new DelegatingOAuth2TokenGenerator(generator, new OAuth2RefreshTokenGenerator());
	}

	/**
	 * 配置
	 */
	@Bean
	@ConditionalOnMissingBean(AuthorizationServerSettings.class)
	AuthorizationServerSettings authorizationServerSettings() {
		return AuthorizationServerSettings.builder().build();
	}

	/**
	 * 密码编码器
	 * @return PasswordEncoder
	 */
	@Bean
	@ConditionalOnMissingBean(PasswordEncoder.class)
	PasswordEncoder passwordEncoder() {
		return PasswordEncoderFactories.createDelegatingPasswordEncoder();
	}

	/**
	 * @param jdbcTemplate jdbc模板
	 * @param registeredClientRepository 注册信息
	 * @return OAuth2AuthorizationService
	 */
	@Bean
	@ConditionalOnMissingBean(OAuth2AuthorizationService.class)
	OAuth2AuthorizationService auth2AuthorizationService(JdbcTemplate jdbcTemplate,
			RegisteredClientRepository registeredClientRepository) {
		return new JdbcOAuth2AuthorizationService(jdbcTemplate, registeredClientRepository);
	}

	/**
	 * 配置
	 */
	@Bean
	AuthenticationProvider authenticationProvider(PasswordEncoder passwordEncoder,
			UserDetailsService userDetailsService) {
		DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
		daoAuthenticationProvider.setPasswordEncoder(passwordEncoder);
		daoAuthenticationProvider.setUserDetailsService(userDetailsService);
		return daoAuthenticationProvider;
	}

	/**
	 * 配置
	 */
	@Bean
	JwtEncoder jwtEncoder(JWKSource<SecurityContext> jwkSource) {
		return new NimbusJwtEncoder(jwkSource);
	}

	/**
	 * 配置
	 * @param jdbcTemplate jdbc模板
	 * @param registeredClientRepository 注册信息
	 * @return OAuth2AuthorizationConsentService
	 */
	@Bean
	@ConditionalOnMissingBean(OAuth2AuthorizationConsentService.class)
	OAuth2AuthorizationConsentService authorizationConsentService(JdbcTemplate jdbcTemplate,
			RegisteredClientRepository registeredClientRepository) {
		return new JdbcOAuth2AuthorizationConsentService(jdbcTemplate, registeredClientRepository);
	}

}
