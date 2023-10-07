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
package org.laokou.auth.module.oauth2.config;

import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;
import org.laokou.auth.module.oauth2.authentication.*;
import org.laokou.auth.service.UsersServiceImpl;
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
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.security.oauth2.core.OAuth2Token;
import org.springframework.security.oauth2.jwt.JwtDecoder;
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
import org.springframework.security.oauth2.server.authorization.oidc.web.authentication.OidcClientRegistrationAuthenticationConverter;
import org.springframework.security.oauth2.server.authorization.oidc.web.authentication.OidcLogoutAuthenticationConverter;
import org.springframework.security.oauth2.server.authorization.settings.AuthorizationServerSettings;
import org.springframework.security.oauth2.server.authorization.settings.ClientSettings;
import org.springframework.security.oauth2.server.authorization.settings.TokenSettings;
import org.springframework.security.oauth2.server.authorization.token.*;
import org.springframework.security.oauth2.server.authorization.web.authentication.*;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.util.List;
import java.util.UUID;

import static org.laokou.auth.common.Constant.LOGIN_PATTERN;
import static org.laokou.common.i18n.common.Constant.ALGORITHM_RSA;

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
	 * OAuth2AuthorizationServer核心配置
	 * @param http http
	 * @param passwordAuthenticationProvider 密码认证Provider
	 * @param mailAuthenticationProvider 邮箱认证Provider
	 * @param mobileAuthenticationProvider 手机号认证Provider
	 * @param authorizationServerSettings OAuth2配置
	 * @param authorizationService 认证配置
	 * @return SecurityFilterChain
	 * @throws Exception Exception
	 */
	@Bean
	@Order(Ordered.HIGHEST_PRECEDENCE)
	SecurityFilterChain authorizationServerSecurityFilterChain(HttpSecurity http,
			OAuth2PasswordAuthenticationProvider passwordAuthenticationProvider,
			OAuth2MailAuthenticationProvider mailAuthenticationProvider,
			OAuth2MobileAuthenticationProvider mobileAuthenticationProvider,
			AuthorizationServerSettings authorizationServerSettings, OAuth2AuthorizationService authorizationService)
			throws Exception {
		// https://docs.spring.io/spring-authorization-server/docs/current/reference/html/configuration-model.html
		OAuth2AuthorizationServerConfiguration.applyDefaultSecurity(http);
		OAuth2AuthorizationServerConfigurer oAuth2AuthorizationServerConfigurer = http
			.getConfigurer(OAuth2AuthorizationServerConfigurer.class)
			// https://docs.spring.io/spring-authorization-server/docs/current/reference/html/protocol-endpoints.html#oauth2-token-endpoint
			.tokenEndpoint((tokenEndpoint) -> tokenEndpoint
				.accessTokenRequestConverter(new DelegatingAuthenticationConverter(List.of(
						new OAuth2MailAuthenticationConverter(), new OAuth2PasswordAuthenticationConverter(),
						new OAuth2DeviceCodeAuthenticationConverter(), new OAuth2MobileAuthenticationConverter(),
						new OAuth2AuthorizationCodeAuthenticationConverter(),
						new OAuth2ClientCredentialsAuthenticationConverter(),
						new OAuth2RefreshTokenAuthenticationConverter(),
						new OAuth2TokenIntrospectionAuthenticationConverter(),
						new OAuth2TokenRevocationAuthenticationConverter(), new PublicClientAuthenticationConverter(),
						new OidcLogoutAuthenticationConverter(), new OidcClientRegistrationAuthenticationConverter(),
						new ClientSecretBasicAuthenticationConverter(), new ClientSecretPostAuthenticationConverter(),
						new OAuth2AuthorizationConsentAuthenticationConverter(),
						new OAuth2DeviceAuthorizationConsentAuthenticationConverter(),
						new OAuth2DeviceAuthorizationRequestAuthenticationConverter(),
						new OAuth2DeviceVerificationAuthenticationConverter(),
						new JwtClientAssertionAuthenticationConverter(),
						new OAuth2AuthorizationCodeRequestAuthenticationConverter())))
				.authenticationProvider(passwordAuthenticationProvider)
				.authenticationProvider(mobileAuthenticationProvider)
				.authenticationProvider(mailAuthenticationProvider))
			.oidc(Customizer.withDefaults())
			.authorizationService(authorizationService)
			.authorizationServerSettings(authorizationServerSettings);
		http.apply(oAuth2AuthorizationServerConfigurer);
		return http.exceptionHandling(
				configurer -> configurer.authenticationEntryPoint(new LoginUrlAuthenticationEntryPoint(LOGIN_PATTERN)))
			.build();
	}

	/**
	 * 注册信息
	 * @param properties 配置
	 * @param jdbcTemplate JDBC模板
	 * @return RegisteredClientRepository
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
			.forEach(clientAuthenticationMethod -> map.from(clientAuthenticationMethod)
				.whenNonNull()
				.as(ClientAuthenticationMethod::new)
				.to(registrationBuilder::clientAuthenticationMethod));
		registration.getAuthorizationGrantTypes()
			.forEach(authorizationGrantType -> map.from(authorizationGrantType)
				.whenNonNull()
				.as(AuthorizationGrantType::new)
				.to(registrationBuilder::authorizationGrantType));
		registration.getScopes().forEach(scope -> map.from(scope).whenNonNull().to(registrationBuilder::scope));
		registration.getRedirectUris()
			.forEach(redirectUri -> map.from(redirectUri).whenNonNull().to(registrationBuilder::redirectUri));
		registration.getPostLogoutRedirectUris()
			.forEach(redirectUri -> map.from(redirectUri).whenNonNull().to(registrationBuilder::postLogoutRedirectUri));
		registrationBuilder.tokenSettings(tokenBuilder.build());
		registrationBuilder.clientSettings(clientBuilder.build());
		JdbcRegisteredClientRepository registeredClientRepository = new JdbcRegisteredClientRepository(jdbcTemplate);
		registeredClientRepository.save(registrationBuilder.build());
		return registeredClientRepository;
	}

	/**
	 * 配置
	 * @param jwkSource 加密源
	 * @return JwtEncoder
	 */
	@Bean
	JwtEncoder jwtEncoder(JWKSource<SecurityContext> jwkSource) {
		return new NimbusJwtEncoder(jwkSource);
	}

	@Bean
	JwtDecoder jwtDecoder(JWKSource<SecurityContext> jwkSource) {
		return OAuth2AuthorizationServerConfiguration.jwtDecoder(jwkSource);
	}

	@Bean
	JWKSource<SecurityContext> jwkSource() {
		RSAKey rsaKey = getRsaKey();
		JWKSet jwkSet = new JWKSet(rsaKey);
		return (jwkSelector, securityContext) -> jwkSelector.select(jwkSet);
	}

	/**
	 * 配置
	 * @param jwtEncoder 加密编码
	 * @return OAuth2TokenGenerator<OAuth2Token>
	 */
	@Bean
	OAuth2TokenGenerator<OAuth2Token> tokenGenerator(JwtEncoder jwtEncoder) {
		JwtGenerator generator = new JwtGenerator(jwtEncoder);
		return new DelegatingOAuth2TokenGenerator(generator, new OAuth2AccessTokenGenerator(),
				new OAuth2RefreshTokenGenerator());
	}

	/**
	 * 配置
	 * @return AuthorizationServerSettings
	 */
	@Bean
	@ConditionalOnMissingBean(AuthorizationServerSettings.class)
	AuthorizationServerSettings authorizationServerSettings() {
		return AuthorizationServerSettings.builder().build();
	}

	/**
	 * 密码编码
	 * @return PasswordEncoder
	 */
	@Bean
	@ConditionalOnMissingBean(PasswordEncoder.class)
	PasswordEncoder passwordEncoder() {
		return PasswordEncoderFactories.createDelegatingPasswordEncoder();
	}

	/**
	 * @param jdbcTemplate JDBC模板
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
	 * @param passwordEncoder 密码编码
	 * @param usersServiceImpl 用户认证
	 * @return AuthenticationProvider
	 */
	@Bean
	AuthenticationProvider authenticationProvider(PasswordEncoder passwordEncoder, UsersServiceImpl usersServiceImpl) {
		DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
		daoAuthenticationProvider.setPasswordEncoder(passwordEncoder);
		daoAuthenticationProvider.setUserDetailsService(usersServiceImpl);
		return daoAuthenticationProvider;
	}

	/**
	 * 配置
	 * @param jdbcTemplate JDBC模板
	 * @param registeredClientRepository 注册信息
	 * @return OAuth2AuthorizationConsentService
	 */
	@Bean
	@ConditionalOnMissingBean(OAuth2AuthorizationConsentService.class)
	OAuth2AuthorizationConsentService authorizationConsentService(JdbcTemplate jdbcTemplate,
			RegisteredClientRepository registeredClientRepository) {
		return new JdbcOAuth2AuthorizationConsentService(jdbcTemplate, registeredClientRepository);
	}

	private RSAKey getRsaKey() {
		KeyPair keyPair = generateRsaKey();
		RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();
		RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();
		return (new RSAKey.Builder(publicKey)).privateKey(privateKey).keyID(UUID.randomUUID().toString()).build();
	}

	private KeyPair generateRsaKey() {
		try {
			KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance(ALGORITHM_RSA);
			keyPairGenerator.initialize(2048);
			return keyPairGenerator.generateKeyPair();
		}
		catch (Exception var2) {
			throw new IllegalStateException(var2);
		}
	}

}
