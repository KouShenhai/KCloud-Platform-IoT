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

package org.laokou.common.security.config;

import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;
import org.laokou.common.crypto.util.RSAUtils;
import org.laokou.common.security.config.convertor.BytesToClaimsHolderConverter;
import org.laokou.common.security.config.convertor.BytesToOAuth2AuthorizationRequestConverter;
import org.laokou.common.security.config.convertor.BytesToUsernamePasswordAuthenticationTokenConverter;
import org.laokou.common.security.config.convertor.ClaimsHolderToBytesConverter;
import org.laokou.common.security.config.convertor.OAuth2AuthorizationRequestToBytesConverter;
import org.laokou.common.security.config.convertor.UsernamePasswordAuthenticationTokenToBytesConverter;
import org.laokou.common.security.config.repository.OAuth2AuthorizationGrantAuthorizationRepository;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.convert.RedisCustomConversions;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;
import org.springframework.security.config.annotation.web.configuration.OAuth2AuthorizationServerConfiguration;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationService;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;

import java.io.IOException;
import java.security.KeyPair;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.util.Arrays;

/**
 * @author laokou
 */
@Configuration(proxyBeanMethods = false)
@EnableRedisRepositories(basePackages = { "org.laokou.common.security.config.repository" })
public class OAuth2AuthorizationConfig {

	/**
	 * 认证配置.
	 * @param authorizationGrantAuthorizationRepository 认证
	 * @param registeredClientRepository 注册客户端
	 * @return OAuth2AuthorizationService
	 */
	@Bean
	@ConditionalOnMissingBean(OAuth2AuthorizationService.class)
	OAuth2AuthorizationService auth2AuthorizationService(RegisteredClientRepository registeredClientRepository,
			OAuth2AuthorizationGrantAuthorizationRepository authorizationGrantAuthorizationRepository) {
		return new RedisOAuth2AuthorizationService(registeredClientRepository,
				authorizationGrantAuthorizationRepository);
	}

	@Bean
	public RedisCustomConversions redisCustomConversions() {
		return new RedisCustomConversions(Arrays.asList(new UsernamePasswordAuthenticationTokenToBytesConverter(),
				new BytesToUsernamePasswordAuthenticationTokenConverter(),
				new OAuth2AuthorizationRequestToBytesConverter(), new BytesToOAuth2AuthorizationRequestConverter(),
				new ClaimsHolderToBytesConverter(), new BytesToClaimsHolderConverter()));
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

	/**
	 * 获取jwk来源.
	 * @return jwk来源
	 */
	@Bean
	JWKSource<SecurityContext> jwkSource()
			throws NoSuchAlgorithmException, IOException, InvalidKeySpecException, NoSuchProviderException {
		RSAKey rsaKey = getRsaKey();
		JWKSet jwkSet = new JWKSet(rsaKey);
		return (jwkSelector, _) -> jwkSelector.select(jwkSet);
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
	 * 获取RSA加密Key.
	 * @return RSA加密Key
	 */
	private RSAKey getRsaKey()
			throws NoSuchAlgorithmException, IOException, InvalidKeySpecException, NoSuchProviderException {
		KeyPair keyPair = getKeyPair();
		RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();
		RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();
		return new RSAKey.Builder(publicKey).privateKey(privateKey).keyID(RSAUtils.getKey("key.pem")).build();
	}

	/**
	 * 生成RSA加密Key.
	 * @return 生成结果
	 */
	private KeyPair getKeyPair()
			throws NoSuchAlgorithmException, NoSuchProviderException, IOException, InvalidKeySpecException {
		PublicKey publicKey = RSAUtils.getRSAPublicKey(RSAUtils.getKey("public.pem"), RSAUtils.getKeyFactory());
		PrivateKey privateKey = RSAUtils.getRSAPrivateKey(RSAUtils.getKey("private.pem"), RSAUtils.getKeyFactory());
		return new KeyPair(publicKey, privateKey);
	}

}
