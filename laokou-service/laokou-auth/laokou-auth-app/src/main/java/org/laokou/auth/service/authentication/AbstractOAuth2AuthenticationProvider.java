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

package org.laokou.auth.service.authentication;

import com.fasterxml.jackson.core.JsonProcessingException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.laokou.auth.model.AuthA;
import org.laokou.common.core.util.RequestUtils;
import org.laokou.common.i18n.common.exception.GlobalException;
import org.laokou.common.i18n.util.ObjectUtils;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.*;
import org.springframework.security.oauth2.core.oidc.OidcIdToken;
import org.springframework.security.oauth2.core.oidc.OidcScopes;
import org.springframework.security.oauth2.core.oidc.endpoint.OidcParameterNames;
import org.springframework.security.oauth2.jwt.*;
import org.springframework.security.oauth2.server.authorization.OAuth2Authorization;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationService;
import org.springframework.security.oauth2.server.authorization.OAuth2TokenType;
import org.springframework.security.oauth2.server.authorization.authentication.*;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.context.AuthorizationServerContextHolder;
import org.springframework.security.oauth2.server.authorization.settings.OAuth2TokenFormat;
import org.springframework.security.oauth2.server.authorization.token.DefaultOAuth2TokenContext;
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenContext;
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenGenerator;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.security.Principal;
import java.util.*;

import static org.laokou.auth.model.OAuth2Constants.*;
import static org.laokou.common.security.handler.OAuth2ExceptionHandler.*;
import static org.springframework.security.oauth2.core.oidc.endpoint.OidcParameterNames.ID_TOKEN;
import static org.springframework.security.oauth2.server.authorization.OAuth2TokenType.ACCESS_TOKEN;

/**
 * 抽象认证处理器.
 *
 * @author laokou
 */
@Slf4j
@RequiredArgsConstructor
abstract class AbstractOAuth2AuthenticationProvider implements AuthenticationProvider {

	private static final OAuth2TokenType ID_TOKEN_TOKEN_TYPE = new OAuth2TokenType(ID_TOKEN);

	private final JwtDecoderFactory<DPoPProofContext> dPoPProofVerifierFactory = new DPoPProofJwtDecoderFactory();

	private final OAuth2AuthorizationService authorizationService;

	private final OAuth2TokenGenerator<? extends OAuth2Token> tokenGenerator;

	private final OAuth2AuthenticationProcessor authenticationProcessor;

	/**
	 * 认证授权.
	 * @param authentication 认证对象
	 */
	@Override
	public Authentication authenticate(Authentication authentication) {
		HttpServletRequest request = RequestUtils.getHttpServletRequest();
		try {
			return authentication(authentication, getPrincipal(request));
		}
		catch (GlobalException e) {
			// 抛出OAuth2认证异常，SpringSecurity全局异常处理并响应前端
			throw getOAuth2AuthenticationException(e.getCode(), e.getMsg(), ERROR_URL);
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
	abstract public boolean supports(Class<?> authentication);

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
	protected Authentication authentication(Authentication authentication, Authentication principal)
			throws JsonProcessingException {
		// see OAuth2AuthorizationCodeAuthenticationProvider#authenticate(Authentication)
		AbstractOAuth2AuthenticationToken abstractOAuth2Authentication = (AbstractOAuth2AuthenticationToken) authentication;
		OAuth2ClientAuthenticationToken clientPrincipal = getAuthenticatedClientElseThrowInvalidClient(abstractOAuth2Authentication);
		RegisteredClient registeredClient = clientPrincipal.getRegisteredClient();
		if (ObjectUtils.isNull(registeredClient)) {
			throw getException(REGISTERED_CLIENT_NOT_EXIST);
		}

		// 获取认证范围
		Set<String> authorizedScopes = new LinkedHashSet<>(registeredClient.getScopes());
		// 登录名称
		String loginName = principal.getCredentials().toString();
		// 认证类型
		AuthorizationGrantType grantType = getGrantType();
		Jwt dPoPProof = verifyIfAvailable(abstractOAuth2Authentication);
		// 获取上下文
		DefaultOAuth2TokenContext.Builder tokenContextBuilder = DefaultOAuth2TokenContext.builder()
			.registeredClient(registeredClient)
			.principal(principal)
			.tokenType(ACCESS_TOKEN)
			.authorizedScopes(authorizedScopes)
			.authorizationServerContext(AuthorizationServerContextHolder.getContext())
			.authorizationGrantType(grantType)
			.authorizationGrant(abstractOAuth2Authentication);
		// @formatter:on
		if (dPoPProof != null) {
			tokenContextBuilder.put(OAuth2TokenContext.DPOP_PROOF_KEY, dPoPProof);
		}

		OAuth2Authorization.Builder authorizationBuilder = OAuth2Authorization
			.withRegisteredClient(registeredClient)
			.principalName(loginName)
			.authorizedScopes(authorizedScopes)
			.authorizationGrantType(grantType);

		// ----- Access token -----
		OAuth2TokenContext tokenContext = tokenContextBuilder.tokenType(OAuth2TokenType.ACCESS_TOKEN).build();
		OAuth2Token generatedAccessToken = Optional.ofNullable(tokenGenerator.generate(tokenContext))
			.orElseThrow(() -> getException(GENERATE_ACCESS_TOKEN_FAIL));
		OAuth2AccessToken accessToken = accessToken(authorizationBuilder, generatedAccessToken, tokenContext);

		// ----- Refresh token -----
		OAuth2RefreshToken refreshToken = null;
		if (registeredClient.getAuthorizationGrantTypes().contains(AuthorizationGrantType.REFRESH_TOKEN)
				&& !clientPrincipal.getClientAuthenticationMethod().equals(ClientAuthenticationMethod.NONE)) {
			tokenContext = tokenContextBuilder.tokenType(OAuth2TokenType.REFRESH_TOKEN).build();
			OAuth2Token generatedRefreshToken = Optional.ofNullable(tokenGenerator.generate(tokenContext))
				.orElseThrow(() -> getException(GENERATE_REFRESH_TOKEN_FAIL));
			refreshToken = (OAuth2RefreshToken) generatedRefreshToken;
			authorizationBuilder.refreshToken(refreshToken);
		}

		// ----- ID token -----
		OidcIdToken idToken;
		if (authorizedScopes.contains(OidcScopes.OPENID)) {
			tokenContext = tokenContextBuilder.tokenType(ID_TOKEN_TOKEN_TYPE)
				// ID令牌定制器可能需要访问访问令牌、刷新令牌
				.authorization(authorizationBuilder.build())
				.build();
			OAuth2Token generatedIdToken = Optional.ofNullable(this.tokenGenerator.generate(tokenContext))
				.orElseThrow(() -> getException(GENERATE_ID_TOKEN_FAIL));
			// 生成id_token
			idToken = new OidcIdToken(generatedIdToken.getTokenValue(), generatedIdToken.getIssuedAt(),
					generatedIdToken.getExpiresAt(), ((Jwt) generatedIdToken).getClaims());
			authorizationBuilder.token(idToken,
					(metadata) -> metadata.put(OAuth2Authorization.Token.CLAIMS_METADATA_NAME, idToken.getClaims()));
		} else {
			idToken = null;
		}

		// 存储认证信息
		authorizationBuilder.attribute(Principal.class.getName(), principal);
		authorizationService.save(authorizationBuilder.build());

		Map<String, Object> additionalParameters = Collections.emptyMap();
		if (idToken != null) {
			additionalParameters = new HashMap<>();
			additionalParameters.put(OidcParameterNames.ID_TOKEN, idToken.getTokenValue());
		}
		return new OAuth2AccessTokenAuthenticationToken(registeredClient, clientPrincipal, accessToken, refreshToken, additionalParameters);
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
			Authentication authentication) {
		OAuth2ClientAuthenticationToken clientPrincipal = null;
		if (OAuth2ClientAuthenticationToken.class.isAssignableFrom(authentication.getPrincipal().getClass())) {
			clientPrincipal = (OAuth2ClientAuthenticationToken) authentication.getPrincipal();
		}
		if (ObjectUtils.isNotNull(clientPrincipal) && clientPrincipal.isAuthenticated()) {
			return clientPrincipal;
		}
		throw getException(INVALID_CLIENT);
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
