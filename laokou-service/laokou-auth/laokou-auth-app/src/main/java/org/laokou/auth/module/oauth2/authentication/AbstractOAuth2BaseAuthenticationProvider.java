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
package org.laokou.auth.module.oauth2.authentication;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.laokou.auth.common.exception.handler.OAuth2ExceptionHandler;
import org.laokou.auth.domain.auth.Auth;
import org.laokou.auth.domain.gateway.*;
import org.laokou.auth.domain.log.LoginLog;
import org.laokou.auth.domain.user.User;
import org.laokou.common.core.utils.CollectionUtil;
import org.laokou.common.i18n.utils.DateUtil;
import org.laokou.common.core.utils.IpUtil;
import org.laokou.common.core.utils.RequestUtil;
import org.laokou.common.i18n.utils.MessageUtil;
import org.laokou.common.jasypt.utils.AesUtil;
import org.laokou.common.redis.utils.RedisUtil;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.*;
import org.springframework.security.oauth2.core.oidc.OidcIdToken;
import org.springframework.security.oauth2.core.oidc.OidcScopes;
import org.springframework.security.oauth2.core.oidc.endpoint.OidcParameterNames;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.authorization.OAuth2Authorization;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationService;
import org.springframework.security.oauth2.server.authorization.OAuth2TokenType;
import org.springframework.security.oauth2.server.authorization.authentication.OAuth2AccessTokenAuthenticationToken;
import org.springframework.security.oauth2.server.authorization.authentication.OAuth2AuthorizationCodeAuthenticationProvider;
import org.springframework.security.oauth2.server.authorization.authentication.OAuth2ClientAuthenticationToken;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.context.AuthorizationServerContextHolder;
import org.springframework.security.oauth2.server.authorization.token.DefaultOAuth2TokenContext;
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenGenerator;

import java.io.IOException;
import java.security.Principal;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.laokou.auth.common.BizCode.LOGIN_SUCCEEDED;
import static org.laokou.auth.common.Constant.*;
import static org.laokou.auth.common.exception.ErrorCode.*;
import static org.laokou.common.i18n.common.Constant.FAIL_STATUS;
import static org.laokou.common.i18n.common.Constant.SUCCESS_STATUS;

/**
 * @author laokou
 */
@Slf4j
@RequiredArgsConstructor
public abstract class AbstractOAuth2BaseAuthenticationProvider implements AuthenticationProvider {

	protected final UserGateway userGateway;

	protected final MenuGateway menuGateway;

	protected final DeptGateway deptGateway;

	protected final PasswordEncoder passwordEncoder;

	protected final CaptchaGateway captchaGateway;

	protected final OAuth2AuthorizationService authorizationService;

	protected final OAuth2TokenGenerator<? extends OAuth2Token> tokenGenerator;

	protected final SourceGateway sourceGateway;

	protected final RedisUtil redisUtil;

	protected final LoginLogGateway loginLogGateway;

	private static final OAuth2TokenType ID_TOKEN_TOKEN_TYPE = new OAuth2TokenType(OidcParameterNames.ID_TOKEN);

	/**
	 * 认证
	 */
	@SneakyThrows
	public Authentication authenticate(Authentication authentication) {
		HttpServletRequest request = RequestUtil.getHttpServletRequest();
		return authenticate(authentication, principal(request));
	}

	/**
	 * @see OAuth2AuthorizationCodeAuthenticationProvider#supports(Class) 是否支持认证（provider）
	 * @param authentication 类型
	 * @return boolean
	 */
	abstract public boolean supports(Class<?> authentication);

	/**
	 * 认证
	 */
	abstract Authentication principal(HttpServletRequest request) throws IOException;

	/**
	 * 认证类型
	 * @return AuthorizationGrantType
	 */
	abstract AuthorizationGrantType getGrantType();

	/**
	 * 获取令牌
	 * @param authentication authentication
	 * @param principal principal
	 * @return Authentication
	 */
	protected Authentication authenticate(Authentication authentication, Authentication principal) {
		// 仿照授权码模式
		// 生成token（access_token + refresh_token）
		AbstractOAuth2BaseAuthenticationToken auth2BaseAuthenticationToken = (AbstractOAuth2BaseAuthenticationToken) authentication;
		OAuth2ClientAuthenticationToken clientPrincipal = getAuthenticatedClientElseThrowInvalidClient(
				auth2BaseAuthenticationToken);
		RegisteredClient registeredClient = clientPrincipal.getRegisteredClient();
		if (registeredClient == null) {
			throw OAuth2ExceptionHandler.getException(REGISTERED_CLIENT_NOT_EXIST,
					MessageUtil.getMessage(REGISTERED_CLIENT_NOT_EXIST));
		}
		// 获取认证范围
		Set<String> scopes = registeredClient.getScopes();
		String loginName = principal.getCredentials().toString();
		// 认证类型
		AuthorizationGrantType grantType = getGrantType();
		// 获取上下文
		DefaultOAuth2TokenContext.Builder tokenContextBuilder = DefaultOAuth2TokenContext.builder()
			.registeredClient(registeredClient)
			.principal(principal)
			.tokenType(OAuth2TokenType.ACCESS_TOKEN)
			.authorizedScopes(scopes)
			.authorizationServerContext(AuthorizationServerContextHolder.getContext())
			.authorizationGrantType(grantType)
			.authorizationGrant(auth2BaseAuthenticationToken);
		DefaultOAuth2TokenContext tokenContext = tokenContextBuilder.tokenType(OAuth2TokenType.ACCESS_TOKEN).build();
		// 生成access_token
		OAuth2Token generatedAccessToken = Optional.ofNullable(tokenGenerator.generate(tokenContext))
			.orElseThrow(() -> OAuth2ExceptionHandler.getException(GENERATE_ACCESS_TOKEN_FAIL,
					MessageUtil.getMessage(GENERATE_ACCESS_TOKEN_FAIL)));
		OAuth2AccessToken accessToken = new OAuth2AccessToken(OAuth2AccessToken.TokenType.BEARER,
				generatedAccessToken.getTokenValue(), generatedAccessToken.getIssuedAt(),
				generatedAccessToken.getExpiresAt(), tokenContext.getAuthorizedScopes());
		// jwt
		OAuth2Authorization.Builder authorizationBuilder = OAuth2Authorization.withRegisteredClient(registeredClient)
			.principalName(loginName)
			.authorizedScopes(scopes)
			.authorizationGrantType(grantType);
		if (generatedAccessToken instanceof ClaimAccessor) {
			authorizationBuilder
				.token(accessToken,
						(metadata) -> metadata.put(OAuth2Authorization.Token.CLAIMS_METADATA_NAME,
								((ClaimAccessor) generatedAccessToken).getClaims()))
				.authorizedScopes(scopes)
				// admin后台管理需要token，解析token获取用户信息，因此将用户信息存在数据库，下次直接查询数据库就可以获取用户信息
				.attribute(Principal.class.getName(), principal);
		}
		else {
			authorizationBuilder.accessToken(accessToken);
		}
		// 生成refresh_token
		OAuth2RefreshToken refreshToken = null;
		if (registeredClient.getAuthorizationGrantTypes().contains(AuthorizationGrantType.REFRESH_TOKEN)
				&& !clientPrincipal.getClientAuthenticationMethod().equals(ClientAuthenticationMethod.NONE)) {
			tokenContext = tokenContextBuilder.tokenType(OAuth2TokenType.REFRESH_TOKEN).build();
			OAuth2Token generatedRefreshToken = Optional.ofNullable(tokenGenerator.generate(tokenContext))
				.orElseThrow(() -> OAuth2ExceptionHandler.getException(GENERATE_REFRESH_TOKEN_FAIL,
						MessageUtil.getMessage(GENERATE_REFRESH_TOKEN_FAIL)));
			refreshToken = (OAuth2RefreshToken) generatedRefreshToken;
			authorizationBuilder.refreshToken(refreshToken);
		}
		// 生成id_token
		OidcIdToken idToken;
		if (scopes.contains(OidcScopes.OPENID)) {
			tokenContext = tokenContextBuilder.tokenType(ID_TOKEN_TOKEN_TYPE)
				// ID令牌定制器可能需要访问访问令牌、刷新令牌
				.authorization(authorizationBuilder.build())
				.build();
			OAuth2Token generatedIdToken = Optional.ofNullable(this.tokenGenerator.generate(tokenContext))
				.orElseThrow(() -> OAuth2ExceptionHandler.getException(GENERATE_ID_TOKEN_FAIL,
						MessageUtil.getMessage(GENERATE_ID_TOKEN_FAIL)));
			idToken = new OidcIdToken(generatedIdToken.getTokenValue(), generatedIdToken.getIssuedAt(),
					generatedIdToken.getExpiresAt(), ((Jwt) generatedIdToken).getClaims());
			authorizationBuilder.token(idToken,
					(metadata) -> metadata.put(OAuth2Authorization.Token.CLAIMS_METADATA_NAME, idToken.getClaims()));
		}
		else {
			idToken = null;
		}
		OAuth2Authorization authorization = authorizationBuilder.build();
		authorizationService.save(authorization);
		// 清空上下文
		SecurityContextHolder.clearContext();
		return new OAuth2AccessTokenAuthenticationToken(registeredClient, clientPrincipal, accessToken, refreshToken,
				Collections.emptyMap());
	}

	/**
	 * 获取用户信息
	 * @param username 登录名
	 * @param password 密码
	 * @param request 请求参数
	 * @param captcha 验证码
	 * @param uuid 唯一标识
	 * @return UsernamePasswordAuthenticationToken
	 */
	protected UsernamePasswordAuthenticationToken authenticationToken(String username, String password,
			HttpServletRequest request, String captcha, String uuid) {
		AuthorizationGrantType grantType = getGrantType();
		String type = grantType.getValue();
		Long tenantId = Long.valueOf(request.getParameter(TENANT_ID));
		String ip = IpUtil.getIpAddr(request);
		// 验证验证码
		Boolean validate = captchaGateway.validate(uuid, captcha);
		if (validate == null) {
			throw getException(CAPTCHA_EXPIRED, username, type, tenantId, ip, null);
		}
		if (Boolean.FALSE.equals(validate)) {
			throw getException(CAPTCHA_ERROR, username, type, tenantId, ip, null);
		}
		// 加密
		String encryptName = AesUtil.encrypt(username);
		// 多租户查询
		User user = userGateway.getUserByUsername(new Auth(encryptName, tenantId, type));
		if (user == null) {
			throw getException(USERNAME_PASSWORD_ERROR, username, type, tenantId, ip, null);
		}
		if (AUTH_PASSWORD.equals(type)) {
			// 验证密码
			String clientPassword = user.getPassword();
			if (!passwordEncoder.matches(password, clientPassword)) {
				throw getException(USERNAME_PASSWORD_ERROR, username, type, tenantId, ip, user);
			}
		}
		// 是否锁定
		if (!user.isEnabled()) {
			throw getException(USERNAME_DISABLE, username, type, tenantId, ip, user);
		}
		Long userId = user.getId();
		Integer superAdmin = user.getSuperAdmin();
		// 权限标识列表
		User u = new User(userId, superAdmin, tenantId);
		List<String> permissionsList = menuGateway.getPermissions(u);
		if (CollectionUtil.isEmpty(permissionsList)) {
			throw getException(USERNAME_NOT_PERMISSION, username, type, tenantId, ip, user);
		}
		// 部门列表
		List<String> deptPaths = deptGateway.getDeptPaths(u);
		user.setDeptPaths(deptPaths);
		user.setPermissionList(permissionsList);
		if (tenantId == DEFAULT_TENANT) {
			// 默认数据源
			user.setSourceName(DEFAULT_SOURCE);
		}
		else {
			// 租户数据源
			String sourceName = sourceGateway.getSourceName(tenantId);
			user.setSourceName(sourceName);
		}
		// 登录IP
		user.setLoginIp(ip);
		// 登录时间
		user.setLoginDate(DateUtil.now());
		// 登录成功
		loginLogGateway.publish(new LoginLog(userId, username, type, tenantId, SUCCESS_STATUS,
				MessageUtil.getMessage(LOGIN_SUCCEEDED), ip, user.getDeptId(), user.getDeptPath()));
		return new UsernamePasswordAuthenticationToken(user, encryptName, user.getAuthorities());
	}

	private OAuth2ClientAuthenticationToken getAuthenticatedClientElseThrowInvalidClient(
			Authentication authentication) {
		OAuth2ClientAuthenticationToken clientPrincipal = null;
		if (OAuth2ClientAuthenticationToken.class.isAssignableFrom(authentication.getPrincipal().getClass())) {
			clientPrincipal = (OAuth2ClientAuthenticationToken) authentication.getPrincipal();
		}
		if (clientPrincipal != null && clientPrincipal.isAuthenticated()) {
			return clientPrincipal;
		}
		throw OAuth2ExceptionHandler.getException(INVALID_CLIENT, MessageUtil.getMessage(INVALID_CLIENT));
	}

	private OAuth2AuthenticationException getException(int code, String username, String type, Long tenantId, String ip,
			User user) {
		String message = MessageUtil.getMessage(code);
		log.error("登录失败，状态码：{}，错误信息：{}", code, message);
		Long userId = null;
		Long deptId = null;
		String deptPath = null;
		if (user != null) {
			userId = user.getId();
			deptId = user.getDeptId();
			deptPath = user.getDeptPath();
		}
		loginLogGateway
			.publish(new LoginLog(userId, username, type, tenantId, FAIL_STATUS, message, ip, deptId, deptPath));
		throw OAuth2ExceptionHandler.getException(code, message);
	}

}
