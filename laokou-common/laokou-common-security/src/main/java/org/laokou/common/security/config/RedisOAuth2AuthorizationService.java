/*
 * Copyright (c) 2022 KCloud-Platform-Alibaba Author or Authors. All Rights Reserved.
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

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.MapType;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.laokou.common.i18n.utils.ObjectUtil;
import org.laokou.common.i18n.utils.StringUtil;
import org.laokou.common.redis.utils.RedisKeyUtil;
import org.laokou.common.redis.utils.RedisUtil;
import org.springframework.lang.Nullable;
import org.springframework.security.jackson2.SecurityJackson2Modules;
import org.springframework.security.oauth2.core.*;
import org.springframework.security.oauth2.core.oidc.OidcIdToken;
import org.springframework.security.oauth2.server.authorization.OAuth2Authorization;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationCode;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationService;
import org.springframework.security.oauth2.server.authorization.OAuth2TokenType;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.jackson2.OAuth2AuthorizationServerJackson2Module;
import org.springframework.util.Assert;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Map;

import static org.laokou.common.i18n.common.Constant.COMMA;
import static org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames.*;
import static org.springframework.security.oauth2.core.oidc.endpoint.OidcParameterNames.ID_TOKEN;

/**
 * @author laokou
 */
@RequiredArgsConstructor
public class RedisOAuth2AuthorizationService implements OAuth2AuthorizationService {

	private final RedisUtil redisUtil;

	private final RegisteredClientRepository registeredClientRepository;

	// @formatter:off
	private static final ObjectMapper MAPPER = new ObjectMapper()
		// https://docs.spring.io/spring-security/reference/servlet/integrations/jackson.html#page-title
		.registerModules(SecurityJackson2Modules.getModules(RedisOAuth2AuthorizationService.class.getClassLoader()))
		// https://docs.spring.io/spring-authorization-server/docs/current-SNAPSHOT/api/org/springframework/security/oauth2/server/authorization/jackson2/OAuth2AuthorizationServerJackson2Module.html
		.registerModule(new OAuth2AuthorizationServerJackson2Module());
	// @formatter:on

	// @formatter:off
	@Override
	public void save(OAuth2Authorization authorization) {
		Assert.isTrue(ObjectUtil.isNotNull(authorization), "authorization is null");
		// access_token
		OAuth2Authorization.Token<OAuth2AccessToken> accessToken = authorization.getAccessToken();
		if (ObjectUtil.isNotNull(accessToken)) {
			setToken(accessToken.getToken(), authorization, ACCESS_TOKEN);
		}
		// refresh token
		OAuth2Authorization.Token<OAuth2RefreshToken> refreshToken = authorization.getRefreshToken();
		if (ObjectUtil.isNotNull(refreshToken)) {
			setToken(refreshToken.getToken(), authorization, REFRESH_TOKEN);
		}
		// authorization code
		OAuth2Authorization.Token<OAuth2AuthorizationCode> authorizationCode = authorization.getToken(OAuth2AuthorizationCode.class);
		if (ObjectUtil.isNotNull(authorizationCode)) {
			setToken(authorizationCode.getToken(), authorization, CODE);
		}
		// oidc id token
		OAuth2Authorization.Token<OidcIdToken> oidcIdToken = authorization.getToken(OidcIdToken.class);
		if (ObjectUtil.isNotNull(oidcIdToken)) {
			setToken(oidcIdToken.getToken(), authorization, ID_TOKEN);
		}
		// user code
		OAuth2Authorization.Token<OAuth2UserCode> userCode = authorization.getToken(OAuth2UserCode.class);
		if (ObjectUtil.isNotNull(userCode)) {
			setToken(userCode.getToken(), authorization, USER_CODE);
		}
		// device code
		OAuth2Authorization.Token<OAuth2DeviceCode> deviceCode = authorization.getToken(OAuth2DeviceCode.class);
		if (ObjectUtil.isNotNull(deviceCode)) {
			setToken(deviceCode.getToken(), authorization, DEVICE_CODE);
		}
	}
	// @formatter:on

	// @formatter:off
	@Override
	public void remove(OAuth2Authorization authorization) {
		Assert.isTrue(ObjectUtil.isNotNull(authorization), "authorization is null");
		// access_token
		OAuth2Authorization.Token<OAuth2AccessToken> accessToken = authorization.getAccessToken();
		if (ObjectUtil.isNotNull(accessToken)) {
			redisUtil.hDel(RedisKeyUtil.getOAuth2AuthorizationHashKey(ACCESS_TOKEN), accessToken.getToken().getTokenValue());
		}
		// refresh token
		OAuth2Authorization.Token<OAuth2RefreshToken> refreshToken = authorization.getRefreshToken();
		if (ObjectUtil.isNotNull(refreshToken)) {
			redisUtil.hDel(RedisKeyUtil.getOAuth2AuthorizationHashKey(REFRESH_TOKEN), refreshToken.getToken().getTokenValue());
		}
		// authorization code
		OAuth2Authorization.Token<OAuth2AuthorizationCode> authorizationCode = authorization.getToken(OAuth2AuthorizationCode.class);
		if (ObjectUtil.isNotNull(authorizationCode)) {
			redisUtil.hDel(RedisKeyUtil.getOAuth2AuthorizationHashKey(CODE), authorizationCode.getToken().getTokenValue());
		}
		// oidc id token
		OAuth2Authorization.Token<OidcIdToken> oidcIdToken = authorization.getToken(OidcIdToken.class);
		if (ObjectUtil.isNotNull(oidcIdToken)) {
			redisUtil.hDel(RedisKeyUtil.getOAuth2AuthorizationHashKey(ID_TOKEN), oidcIdToken.getToken().getTokenValue());
		}
		// user code
		OAuth2Authorization.Token<OAuth2UserCode> userCode = authorization.getToken(OAuth2UserCode.class);
		if (ObjectUtil.isNotNull(userCode)) {
			redisUtil.hDel(RedisKeyUtil.getOAuth2AuthorizationHashKey(USER_CODE), userCode.getToken().getTokenValue());
		}
		// device code
		OAuth2Authorization.Token<OAuth2DeviceCode> deviceCode = authorization.getToken(OAuth2DeviceCode.class);
		if (ObjectUtil.isNotNull(deviceCode)) {
			redisUtil.hDel(RedisKeyUtil.getOAuth2AuthorizationHashKey(DEVICE_CODE), deviceCode.getToken().getTokenValue());
		}
	}
	// @formatter:on

	@Nullable
	@Override
	public OAuth2Authorization findById(String id) {
		throw new UnsupportedOperationException();
	}

	@Nullable
	@Override
	public OAuth2Authorization findByToken(String token, @Nullable OAuth2TokenType tokenType) {
		Assert.isTrue(StringUtil.isNotEmpty(token), "token is empty");
		Assert.isTrue(ObjectUtil.isNotNull(tokenType), "tokenType is null");
		String authorizationHashKey = RedisKeyUtil.getOAuth2AuthorizationHashKey(tokenType.getValue());
		Object obj = redisUtil.hGet(authorizationHashKey, token);
		if (ObjectUtil.isNotNull(obj)) {
			return parse((RedisOAuth2Authorization) obj);
		}
		return null;
	}

	private void setToken(AbstractOAuth2Token token, OAuth2Authorization authorization, String type) {
		Instant issuedAt = ObjectUtil.requireNotNull(token.getIssuedAt());
		Instant expiresAt = ObjectUtil.requireNotNull(token.getExpiresAt());
		String tokenValue = token.getTokenValue();
		long expireTime = ChronoUnit.SECONDS.between(issuedAt, expiresAt);
		String authorizationHashKey = RedisKeyUtil.getOAuth2AuthorizationHashKey(type);
		RedisOAuth2Authorization redisOAuth2Authorization = convert(authorization);
		if (redisUtil.hasHashKey(authorizationHashKey, tokenValue)) {
			redisUtil.hDel(authorizationHashKey, tokenValue);
		}
		redisUtil.hSet(authorizationHashKey, tokenValue, redisOAuth2Authorization, expireTime);
	}

	@SneakyThrows
	private OAuth2Authorization parse(RedisOAuth2Authorization redisOAuth2Authorization) {
		RegisteredClient registeredClient = registeredClientRepository
			.findByClientId(redisOAuth2Authorization.getRegisteredClientId());
		Assert.isTrue(ObjectUtil.isNotNull(registeredClient), "registeredClient is null");
		MapType mapType = MAPPER.getTypeFactory().constructMapType(Map.class, String.class, Object.class);
		OAuth2Authorization.Builder builder = OAuth2Authorization.withRegisteredClient(registeredClient)
			.id(redisOAuth2Authorization.getId())
			.principalName(redisOAuth2Authorization.getPrincipalName())
			.authorizationGrantType(new AuthorizationGrantType(redisOAuth2Authorization.getAuthorizationGrantType()))
			.authorizedScopes(StringUtil.commaDelimitedListToSet(redisOAuth2Authorization.getAuthorizedScopes()))
			.attributes(attributesConsumer -> attributesConsumer
				.putAll(parseMap(redisOAuth2Authorization.getAttributes(), mapType)));
		if (StringUtil.isNotEmpty(redisOAuth2Authorization.getState())) {
			builder.attribute(STATE, redisOAuth2Authorization.getState());
		}
		// access token
		parseAccessToken(builder, redisOAuth2Authorization, mapType);
		// refresh token
		parseRefreshToken(builder, redisOAuth2Authorization, mapType);
		// authorization code
		parseAuthorizationCode(builder, redisOAuth2Authorization, mapType);
		// oidc id token
		parseOidcIdToken(builder, redisOAuth2Authorization, mapType);
		// user code
		parseUserCode(builder, redisOAuth2Authorization, mapType);
		// device code
		parseDeviceCode(builder, redisOAuth2Authorization, mapType);
		return builder.build();
	}

	private Map<String, Object> parseMap(String json, MapType mapType) {
		try {
			return MAPPER.readValue(json, mapType);
		}
		catch (JsonProcessingException e) {
			throw new RuntimeException(e);
		}
	}

	private void parseAccessToken(OAuth2Authorization.Builder builder,
			RedisOAuth2Authorization redisOAuth2Authorization, MapType mapType) {
		String accessTokenValue = redisOAuth2Authorization.getAccessTokenValue();
		if (ObjectUtil.isNotNull(accessTokenValue)) {
			OAuth2AccessToken accessToken = new OAuth2AccessToken(OAuth2AccessToken.TokenType.BEARER, accessTokenValue,
					redisOAuth2Authorization.getAccessTokenIssuedAt(),
					redisOAuth2Authorization.getAccessTokenExpiresAt(),
					StringUtil.commaDelimitedListToSet(redisOAuth2Authorization.getAccessTokenScopes()));
			builder.token(accessToken,
					metadata -> metadata.putAll(parseMap(redisOAuth2Authorization.getAccessTokenMetadata(), mapType)));
		}
	}

	private void parseRefreshToken(OAuth2Authorization.Builder builder,
			RedisOAuth2Authorization redisOAuth2Authorization, MapType mapType) {
		String refreshTokenValue = redisOAuth2Authorization.getRefreshTokenValue();
		if (ObjectUtil.isNotNull(refreshTokenValue)) {
			OAuth2RefreshToken refreshToken = new OAuth2RefreshToken(refreshTokenValue,
					redisOAuth2Authorization.getRefreshTokenIssuedAt(),
					redisOAuth2Authorization.getRefreshTokenExpiresAt());
			builder.token(refreshToken,
					metadata -> metadata.putAll(parseMap(redisOAuth2Authorization.getRefreshTokenMetadata(), mapType)));
		}
	}

	private void parseAuthorizationCode(OAuth2Authorization.Builder builder,
			RedisOAuth2Authorization redisOAuth2Authorization, MapType mapType) {
		String authorizationCodeValue = redisOAuth2Authorization.getAuthorizationCodeValue();
		if (ObjectUtil.isNotNull(authorizationCodeValue)) {
			OAuth2AuthorizationCode authorizationCode = new OAuth2AuthorizationCode(authorizationCodeValue,
					redisOAuth2Authorization.getAuthorizationCodeIssuedAt(),
					redisOAuth2Authorization.getAuthorizationCodeExpiresAt());
			builder.token(authorizationCode, metadata -> metadata
				.putAll(parseMap(redisOAuth2Authorization.getAuthorizationCodeMetadata(), mapType)));
		}
	}

	private void parseOidcIdToken(OAuth2Authorization.Builder builder,
			RedisOAuth2Authorization redisOAuth2Authorization, MapType mapType) {
		String oidcIdTokenValue = redisOAuth2Authorization.getOidcIdTokenValue();
		if (ObjectUtil.isNotNull(oidcIdTokenValue)) {
			OidcIdToken oidcIdToken = new OidcIdToken(oidcIdTokenValue,
					redisOAuth2Authorization.getOidcIdTokenIssuedAt(),
					redisOAuth2Authorization.getOidcIdTokenExpiresAt(),
					parseMap(redisOAuth2Authorization.getOidcIdTokenClaims(), mapType));
			builder.token(oidcIdToken,
					metadata -> metadata.putAll(parseMap(redisOAuth2Authorization.getOidcIdTokenMetadata(), mapType)));
		}
	}

	private void parseUserCode(OAuth2Authorization.Builder builder, RedisOAuth2Authorization redisOAuth2Authorization,
			MapType mapType) {
		String userCodeValue = redisOAuth2Authorization.getUserCodeValue();
		if (ObjectUtil.isNotNull(userCodeValue)) {
			OAuth2UserCode userCode = new OAuth2UserCode(userCodeValue, redisOAuth2Authorization.getUserCodeIssuedAt(),
					redisOAuth2Authorization.getUserCodeExpiresAt());
			builder.token(userCode,
					metadata -> metadata.putAll(parseMap(redisOAuth2Authorization.getUserCodeMetadata(), mapType)));
		}
	}

	private void parseDeviceCode(OAuth2Authorization.Builder builder, RedisOAuth2Authorization redisOAuth2Authorization,
			MapType mapType) {
		String deviceCodeValue = redisOAuth2Authorization.getDeviceCodeValue();
		if (ObjectUtil.isNotNull(deviceCodeValue)) {
			OAuth2DeviceCode deviceCode = new OAuth2DeviceCode(deviceCodeValue,
					redisOAuth2Authorization.getDeviceCodeIssuedAt(),
					redisOAuth2Authorization.getDeviceCodeExpiresAt());
			builder.token(deviceCode,
					metadata -> metadata.putAll(parseMap(redisOAuth2Authorization.getDeviceCodeMetadata(), mapType)));
		}
	}

	@SneakyThrows
	private RedisOAuth2Authorization convert(OAuth2Authorization authorization) {
		RedisOAuth2Authorization redisOAuth2Authorization = new RedisOAuth2Authorization();
		redisOAuth2Authorization.setId(authorization.getId());
		redisOAuth2Authorization.setRegisteredClientId(authorization.getRegisteredClientId());
		redisOAuth2Authorization.setPrincipalName(authorization.getPrincipalName());
		redisOAuth2Authorization.setAuthorizationGrantType(authorization.getAuthorizationGrantType().getValue());
		redisOAuth2Authorization
			.setAuthorizedScopes(StringUtil.collectionToDelimitedString(authorization.getAuthorizedScopes(), COMMA));
		redisOAuth2Authorization.setAttributes(MAPPER.writeValueAsString(authorization.getAttributes()));
		redisOAuth2Authorization.setState(authorization.getAttribute(STATE));
		// access token
		setAccessToken(authorization, redisOAuth2Authorization);
		// refresh token
		setRefreshToken(authorization, redisOAuth2Authorization);
		// authorization code
		setAuthorizationCode(authorization, redisOAuth2Authorization);
		// oidc id token
		setOidcIdToken(authorization, redisOAuth2Authorization);
		// user code
		setUserCode(authorization, redisOAuth2Authorization);
		// device code
		setDeviceCode(authorization, redisOAuth2Authorization);
		return redisOAuth2Authorization;
	}

	@SneakyThrows
	private void setAccessToken(OAuth2Authorization authorization, RedisOAuth2Authorization redisOAuth2Authorization) {
		OAuth2Authorization.Token<OAuth2AccessToken> token = authorization.getAccessToken();
		if (ObjectUtil.isNotNull(token)) {
			OAuth2AccessToken accessToken = token.getToken();
			redisOAuth2Authorization.setAccessTokenValue(accessToken.getTokenValue());
			redisOAuth2Authorization.setAccessTokenIssuedAt(accessToken.getIssuedAt());
			redisOAuth2Authorization.setAccessTokenExpiresAt(accessToken.getExpiresAt());
			redisOAuth2Authorization.setAccessTokenMetadata(MAPPER.writeValueAsString(token.getMetadata()));
			redisOAuth2Authorization
				.setAccessTokenScopes(StringUtil.collectionToDelimitedString(accessToken.getScopes(), COMMA));
			redisOAuth2Authorization.setAccessTokenType(accessToken.getTokenType().getValue());
		}
	}

	@SneakyThrows
	private void setRefreshToken(OAuth2Authorization authorization, RedisOAuth2Authorization redisOAuth2Authorization) {
		OAuth2Authorization.Token<OAuth2RefreshToken> token = authorization.getRefreshToken();
		if (ObjectUtil.isNotNull(token)) {
			OAuth2RefreshToken refreshToken = token.getToken();
			redisOAuth2Authorization.setRefreshTokenValue(refreshToken.getTokenValue());
			redisOAuth2Authorization.setRefreshTokenIssuedAt(refreshToken.getIssuedAt());
			redisOAuth2Authorization.setRefreshTokenExpiresAt(refreshToken.getExpiresAt());
			redisOAuth2Authorization.setRefreshTokenMetadata(MAPPER.writeValueAsString(token.getMetadata()));
		}
	}

	@SneakyThrows
	private void setAuthorizationCode(OAuth2Authorization authorization,
			RedisOAuth2Authorization redisOAuth2Authorization) {
		OAuth2Authorization.Token<OAuth2AuthorizationCode> token = authorization
			.getToken(OAuth2AuthorizationCode.class);
		if (ObjectUtil.isNotNull(token)) {
			OAuth2AuthorizationCode authorizationCode = token.getToken();
			redisOAuth2Authorization.setAuthorizationCodeValue(authorizationCode.getTokenValue());
			redisOAuth2Authorization.setAuthorizationCodeIssuedAt(authorizationCode.getIssuedAt());
			redisOAuth2Authorization.setAuthorizationCodeExpiresAt(authorizationCode.getExpiresAt());
			redisOAuth2Authorization.setAuthorizationCodeMetadata(MAPPER.writeValueAsString(token.getMetadata()));
		}
	}

	@SneakyThrows
	private void setOidcIdToken(OAuth2Authorization authorization, RedisOAuth2Authorization redisOAuth2Authorization) {
		OAuth2Authorization.Token<OidcIdToken> token = authorization.getToken(OidcIdToken.class);
		if (ObjectUtil.isNotNull(token)) {
			OidcIdToken oidcIdToken = token.getToken();
			redisOAuth2Authorization.setOidcIdTokenValue(oidcIdToken.getTokenValue());
			redisOAuth2Authorization.setOidcIdTokenIssuedAt(oidcIdToken.getIssuedAt());
			redisOAuth2Authorization.setOidcIdTokenExpiresAt(oidcIdToken.getExpiresAt());
			redisOAuth2Authorization.setOidcIdTokenMetadata(MAPPER.writeValueAsString(token.getMetadata()));
			redisOAuth2Authorization.setOidcIdTokenClaims(MAPPER.writeValueAsString(oidcIdToken.getClaims()));
		}
	}

	@SneakyThrows
	private void setUserCode(OAuth2Authorization authorization, RedisOAuth2Authorization redisOAuth2Authorization) {
		OAuth2Authorization.Token<OAuth2UserCode> token = authorization.getToken(OAuth2UserCode.class);
		if (ObjectUtil.isNotNull(token)) {
			OAuth2UserCode userCode = token.getToken();
			redisOAuth2Authorization.setUserCodeValue(userCode.getTokenValue());
			redisOAuth2Authorization.setUserCodeIssuedAt(userCode.getIssuedAt());
			redisOAuth2Authorization.setUserCodeExpiresAt(userCode.getExpiresAt());
			redisOAuth2Authorization.setUserCodeMetadata(MAPPER.writeValueAsString(token.getMetadata()));
		}
	}

	@SneakyThrows
	private void setDeviceCode(OAuth2Authorization authorization, RedisOAuth2Authorization redisOAuth2Authorization) {
		OAuth2Authorization.Token<OAuth2DeviceCode> token = authorization.getToken(OAuth2DeviceCode.class);
		if (ObjectUtil.isNotNull(token)) {
			OAuth2DeviceCode deviceCode = token.getToken();
			redisOAuth2Authorization.setDeviceCodeValue(deviceCode.getTokenValue());
			redisOAuth2Authorization.setDeviceCodeIssuedAt(deviceCode.getIssuedAt());
			redisOAuth2Authorization.setDeviceCodeExpiresAt(deviceCode.getExpiresAt());
			redisOAuth2Authorization.setDeviceCodeMetadata(MAPPER.writeValueAsString(token.getMetadata()));
		}
	}

}
