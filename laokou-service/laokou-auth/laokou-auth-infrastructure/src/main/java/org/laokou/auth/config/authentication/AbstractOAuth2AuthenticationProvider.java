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

package org.laokou.auth.config.authentication;

import com.fasterxml.jackson.core.JsonProcessingException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.NonNull;
import org.laokou.auth.model.AuthA;
import org.laokou.auth.model.constant.OAuth2Constants;
import org.laokou.common.core.util.RequestUtils;
import org.laokou.common.i18n.common.constant.StringConstants;
import org.laokou.common.i18n.common.exception.GlobalException;
import org.laokou.common.i18n.util.ObjectUtils;
import org.laokou.common.security.handler.OAuth2ExceptionHandler;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClaimAccessor;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.OAuth2Error;
import org.springframework.security.oauth2.core.OAuth2ErrorCodes;
import org.springframework.security.oauth2.core.OAuth2RefreshToken;
import org.springframework.security.oauth2.core.OAuth2Token;
import org.springframework.security.oauth2.jwt.DPoPProofContext;
import org.springframework.security.oauth2.jwt.DPoPProofJwtDecoderFactory;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtDecoderFactory;
import org.springframework.security.oauth2.server.authorization.OAuth2Authorization;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationService;
import org.springframework.security.oauth2.server.authorization.OAuth2TokenType;
import org.springframework.security.oauth2.server.authorization.authentication.OAuth2AccessTokenAuthenticationToken;
import org.springframework.security.oauth2.server.authorization.authentication.OAuth2AuthorizationCodeAuthenticationProvider;
import org.springframework.security.oauth2.server.authorization.authentication.OAuth2AuthorizationGrantAuthenticationToken;
import org.springframework.security.oauth2.server.authorization.authentication.OAuth2ClientAuthenticationToken;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.context.AuthorizationServerContextHolder;
import org.springframework.security.oauth2.server.authorization.settings.OAuth2TokenFormat;
import org.springframework.security.oauth2.server.authorization.token.DefaultOAuth2TokenContext;
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenContext;
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenGenerator;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.security.Principal;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

/**
 * 抽象认证处理器.
 *
 * @author laokou
 */
@Slf4j
@RequiredArgsConstructor
abstract class AbstractOAuth2AuthenticationProvider implements AuthenticationProvider {

	private final JwtDecoderFactory<DPoPProofContext> dPoPProofVerifierFactory = new DPoPProofJwtDecoderFactory();

	private final OAuth2AuthorizationService authorizationService;

	private final OAuth2TokenGenerator<? extends OAuth2Token> tokenGenerator;

	private final OAuth2AuthenticationProcessor authenticationProcessor;

	/**
	 * 认证授权.
	 * @param authentication 认证对象
	 */
	@Override
	public Authentication authenticate(@NonNull Authentication authentication) {
		HttpServletRequest request = RequestUtils.getHttpServletRequest();
		try {
			return authentication(authentication, getPrincipal(request));
		}
		catch (GlobalException e) {
			// 抛出OAuth2认证异常，SpringSecurity全局异常处理并响应前端
			throw OAuth2ExceptionHandler.getOAuth2AuthenticationException(e.getCode(), e.getMsg(),
					OAuth2ExceptionHandler.ERROR_URL);
		}
		catch (OAuth2AuthenticationException e) {
			throw e;
		}
		catch (Exception e) {
			log.error("认证异常，错误信息：{}", e.getMessage(), e);
			throw new RuntimeException(e);
		}
	}

	/**
	 * 类型支持.
	 * @param authentication 类型
	 * @return 支持结果
	 * @see OAuth2AuthorizationCodeAuthenticationProvider#supports(Class)
	 * 是否支持认证（provider）.
	 */
	abstract public boolean supports(@NonNull Class<?> authentication);

	/**
	 * 认证.
	 * @param request 请求对象
	 */
	abstract Authentication getPrincipal(HttpServletRequest request) throws Exception;

	/**
	 * 获取认证类型.
	 * @return 认证类型
	 */
	abstract AuthorizationGrantType getGrantType();

	/**
	 * 获取令牌.
	 * @param authentication 认证对象
	 * @param principal 认证对象
	 * @return 令牌
	 */
	protected Authentication authentication(Authentication authentication, @NonNull Authentication principal)
			throws JsonProcessingException {
		// 查看 OAuth2DeviceCodeAuthenticationProvider#authenticate(Authentication)
		AbstractOAuth2AuthenticationToken abstractOAuth2Authentication = (AbstractOAuth2AuthenticationToken) authentication;
		OAuth2ClientAuthenticationToken clientPrincipal = getAuthenticatedClientElseThrowInvalidClient(
				abstractOAuth2Authentication);
		RegisteredClient registeredClient = clientPrincipal.getRegisteredClient();
		if (ObjectUtils.isNull(registeredClient)) {
			throw OAuth2ExceptionHandler.getException(OAuth2Constants.REGISTERED_CLIENT_NOT_FOUND);
		}

		// 获取认证范围
		Set<String> authorizedScopes = new LinkedHashSet<>(registeredClient.getScopes());
		// 登录名称
		String loginName = Optional.ofNullable(principal.getCredentials())
			.map(Object::toString)
			.orElse(StringConstants.EMPTY);
		// 认证类型
		AuthorizationGrantType grantType = getGrantType();
		// JWT
		Jwt dPoPProof = verifyIfAvailable(abstractOAuth2Authentication);
		// 获取上下文
		DefaultOAuth2TokenContext.Builder tokenContextBuilder = DefaultOAuth2TokenContext.builder()
			.registeredClient(registeredClient)
			.principal(principal)
			.tokenType(OAuth2TokenType.ACCESS_TOKEN)
			.authorizedScopes(authorizedScopes)
			.authorizationServerContext(AuthorizationServerContextHolder.getContext())
			.authorizationGrantType(grantType)
			.authorizationGrant(abstractOAuth2Authentication);
		// @formatter:on
		if (dPoPProof != null) {
			tokenContextBuilder.put(OAuth2TokenContext.DPOP_PROOF_KEY, dPoPProof);
		}

		OAuth2Authorization.Builder authorizationBuilder = OAuth2Authorization.withRegisteredClient(registeredClient)
			.principalName(loginName)
			.authorizedScopes(authorizedScopes)
			.authorizationGrantType(grantType);

		// ----- Access token -----
		OAuth2TokenContext tokenContext = tokenContextBuilder.tokenType(OAuth2TokenType.ACCESS_TOKEN).build();
		OAuth2Token generatedAccessToken = Optional.ofNullable(tokenGenerator.generate(tokenContext))
			.orElseThrow(() -> OAuth2ExceptionHandler.getException(OAuth2Constants.GENERATE_ACCESS_TOKEN_FAIL));
		OAuth2AccessToken accessToken = accessToken(authorizationBuilder, generatedAccessToken, tokenContext);

		// ----- Refresh token -----
		OAuth2RefreshToken refreshToken = null;
		if (registeredClient.getAuthorizationGrantTypes().contains(AuthorizationGrantType.REFRESH_TOKEN) && !ObjectUtils
			.equals(clientPrincipal.getClientAuthenticationMethod(), ClientAuthenticationMethod.NONE)) {
			tokenContext = tokenContextBuilder.tokenType(OAuth2TokenType.REFRESH_TOKEN).build();
			OAuth2Token generatedRefreshToken = Optional.ofNullable(tokenGenerator.generate(tokenContext))
				.orElseThrow(() -> OAuth2ExceptionHandler.getException(OAuth2Constants.GENERATE_REFRESH_TOKEN_FAIL));
			refreshToken = (OAuth2RefreshToken) generatedRefreshToken;
			authorizationBuilder.refreshToken(refreshToken);
		}
		// 存储认证信息
		authorizationBuilder.attribute(Principal.class.getName(), principal.getPrincipal());
		authorizationService.save(authorizationBuilder.build());
		return new OAuth2AccessTokenAuthenticationToken(registeredClient, clientPrincipal, accessToken, refreshToken);
	}

	/**
	 * 获取用户信息.
	 * @param authA 认证聚合根
	 * @return 用户信息
	 */
	protected UsernamePasswordAuthenticationToken authentication(AuthA authA, HttpServletRequest request)
			throws Exception {
		return authenticationProcessor.authentication(authA, request);
	}

	private OAuth2ClientAuthenticationToken getAuthenticatedClientElseThrowInvalidClient(
			@NonNull Authentication authentication) {
		OAuth2ClientAuthenticationToken clientPrincipal = null;
		if (authentication.getPrincipal() instanceof OAuth2ClientAuthenticationToken oAuth2ClientAuthenticationToken) {
			clientPrincipal = oAuth2ClientAuthenticationToken;
		}
		if (ObjectUtils.isNotNull(clientPrincipal) && clientPrincipal.isAuthenticated()) {
			return clientPrincipal;
		}
		throw OAuth2ExceptionHandler.getException(OAuth2Constants.INVALID_CLIENT);
	}

	private Jwt verifyIfAvailable(OAuth2AuthorizationGrantAuthenticationToken authorizationGrantAuthentication) {
		String dPoPProof = (String) authorizationGrantAuthentication.getAdditionalParameters().get("dpop_proof");
		if (!StringUtils.hasText(dPoPProof)) {
			return null;
		}
		String method = (String) authorizationGrantAuthentication.getAdditionalParameters().get("dpop_method");
		String targetUri = (String) authorizationGrantAuthentication.getAdditionalParameters().get("dpop_target_uri");
		Jwt dPoPProofJwt;
		try {
			// @formatter:off
			DPoPProofContext dPoPProofContext = DPoPProofContext.withDPoPProof(dPoPProof)
				.method(method)
				.targetUri(targetUri)
				.build();
			// @formatter:on
			JwtDecoder dPoPProofVerifier = dPoPProofVerifierFactory.createDecoder(dPoPProofContext);
			dPoPProofJwt = dPoPProofVerifier.decode(dPoPProof);
		}
		catch (Exception ex) {
			throw new OAuth2AuthenticationException(new OAuth2Error(OAuth2ErrorCodes.INVALID_DPOP_PROOF), ex);
		}
		return dPoPProofJwt;
	}

	private <T extends OAuth2Token> OAuth2AccessToken accessToken(OAuth2Authorization.Builder builder, T token,
			OAuth2TokenContext accessTokenContext) {
		OAuth2AccessToken.TokenType tokenType = OAuth2AccessToken.TokenType.BEARER;
		if (token instanceof ClaimAccessor claimAccessor) {
			Map<String, Object> cnfClaims = claimAccessor.getClaimAsMap("cnf");
			if (!CollectionUtils.isEmpty(cnfClaims) && cnfClaims.containsKey("jkt")) {
				tokenType = OAuth2AccessToken.TokenType.DPOP;
			}
		}
		OAuth2AccessToken accessToken = new OAuth2AccessToken(tokenType, token.getTokenValue(), token.getIssuedAt(),
				token.getExpiresAt(), accessTokenContext.getAuthorizedScopes());
		OAuth2TokenFormat accessTokenFormat = accessTokenContext.getRegisteredClient()
			.getTokenSettings()
			.getAccessTokenFormat();
		builder.token(accessToken, (metadata) -> {
			if (token instanceof ClaimAccessor claimAccessor) {
				metadata.put(OAuth2Authorization.Token.CLAIMS_METADATA_NAME, claimAccessor.getClaims());
			}
			metadata.put(OAuth2Authorization.Token.INVALIDATED_METADATA_NAME, false);
			metadata.put(OAuth2TokenFormat.class.getName(), accessTokenFormat.getValue());
		});
		return accessToken;
	}

}
