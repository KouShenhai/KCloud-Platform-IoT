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
package org.laokou.auth.module.oauth2.authentication;

import com.baomidou.dynamic.datasource.creator.DataSourceProperty;
import com.baomidou.dynamic.datasource.creator.hikaricp.HikariDataSourceCreator;
import com.baomidou.dynamic.datasource.toolkit.DynamicDataSourceContextHolder;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.laokou.auth.common.exception.handler.OAuth2ExceptionHandler;
import org.laokou.auth.domain.auth.Auth;
import org.laokou.auth.domain.gateway.*;
import org.laokou.auth.domain.log.LoginLog;
import org.laokou.auth.domain.source.Source;
import org.laokou.auth.domain.user.User;
import org.laokou.common.core.holder.UserContextHolder;
import org.laokou.common.core.utils.CollectionUtil;
import org.laokou.common.core.utils.IpUtil;
import org.laokou.common.core.utils.RequestUtil;
import org.laokou.common.crypto.utils.AesUtil;
import org.laokou.common.i18n.utils.DateUtil;
import org.laokou.common.i18n.utils.LogUtil;
import org.laokou.common.i18n.utils.MessageUtil;
import org.laokou.common.i18n.utils.ObjectUtil;
import org.laokou.common.mybatisplus.utils.DynamicUtil;
import org.laokou.common.redis.utils.RedisUtil;
import org.springframework.jdbc.BadSqlGrammarException;
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

import javax.sql.DataSource;
import java.io.IOException;
import java.security.Principal;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static com.baomidou.dynamic.datasource.enums.DdConstants.MASTER;
import static org.laokou.common.i18n.common.BizCodes.LOGIN_SUCCEEDED;
import static org.laokou.common.i18n.common.ErrorCodes.*;
import static org.laokou.common.i18n.common.NumberConstants.FAIL;
import static org.laokou.common.i18n.common.NumberConstants.SUCCESS;
import static org.laokou.common.i18n.common.OAuth2Constants.PASSWORD;
import static org.laokou.common.i18n.common.StatusCodes.CUSTOM_SERVER_ERROR;
import static org.laokou.common.i18n.common.StatusCodes.FORBIDDEN;
import static org.laokou.common.i18n.common.TenantConstants.DEFAULT;
import static org.laokou.common.i18n.common.TenantConstants.TENANT_ID;

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

	protected final DynamicUtil dynamicUtil;

	protected final LoginLogGateway loginLogGateway;

	private static final OAuth2TokenType ID_TOKEN_TOKEN_TYPE = new OAuth2TokenType(OidcParameterNames.ID_TOKEN);

	/**
	 * 认证.
	 */
	@SneakyThrows
	public Authentication authenticate(Authentication authentication) {
		HttpServletRequest request = RequestUtil.getHttpServletRequest();
		return authenticate(authentication, principal(request));
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
	 */
	abstract Authentication principal(HttpServletRequest request) throws IOException;

	/**
	 * 认证类型.
	 * @return AuthorizationGrantType
	 */
	abstract AuthorizationGrantType getGrantType();

	/**
	 * 获取令牌.
	 * @param authentication authentication
	 * @param principal principal
	 * @return Authentication
	 */
	protected Authentication authenticate(Authentication authentication, Authentication principal) {
		try {
			// 仿照授权码模式
			// 生成token（access_token + refresh_token）
			AbstractOAuth2BaseAuthenticationToken auth2BaseAuthenticationToken = (AbstractOAuth2BaseAuthenticationToken) authentication;
			OAuth2ClientAuthenticationToken clientPrincipal = getAuthenticatedClientElseThrowInvalidClient(
					auth2BaseAuthenticationToken);
			RegisteredClient registeredClient = clientPrincipal.getRegisteredClient();
			if (ObjectUtil.isNull(registeredClient)) {
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
			DefaultOAuth2TokenContext tokenContext = tokenContextBuilder.tokenType(OAuth2TokenType.ACCESS_TOKEN)
				.build();
			// 生成access_token
			OAuth2Token generatedAccessToken = Optional.ofNullable(tokenGenerator.generate(tokenContext))
				.orElseThrow(() -> OAuth2ExceptionHandler.getException(GENERATE_ACCESS_TOKEN_FAIL,
						MessageUtil.getMessage(GENERATE_ACCESS_TOKEN_FAIL)));
			OAuth2AccessToken accessToken = new OAuth2AccessToken(OAuth2AccessToken.TokenType.BEARER,
					generatedAccessToken.getTokenValue(), generatedAccessToken.getIssuedAt(),
					generatedAccessToken.getExpiresAt(), tokenContext.getAuthorizedScopes());
			// jwt
			OAuth2Authorization.Builder authorizationBuilder = OAuth2Authorization
				.withRegisteredClient(registeredClient)
				.principalName(loginName)
				.authorizedScopes(scopes)
				.authorizationGrantType(grantType);
			if (generatedAccessToken instanceof ClaimAccessor claimAccessor) {
				authorizationBuilder
					.token(accessToken,
							(metadata) -> metadata.put(OAuth2Authorization.Token.CLAIMS_METADATA_NAME,
									claimAccessor.getClaims()))
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
			if (scopes.contains(OidcScopes.OPENID)) {
				tokenContext = tokenContextBuilder.tokenType(ID_TOKEN_TOKEN_TYPE)
					// ID令牌定制器可能需要访问访问令牌、刷新令牌
					.authorization(authorizationBuilder.build())
					.build();
				OAuth2Token generatedIdToken = Optional.ofNullable(this.tokenGenerator.generate(tokenContext))
					.orElseThrow(() -> OAuth2ExceptionHandler.getException(GENERATE_ID_TOKEN_FAIL,
							MessageUtil.getMessage(GENERATE_ID_TOKEN_FAIL)));
				// 生成id_token
				OidcIdToken idToken = new OidcIdToken(generatedIdToken.getTokenValue(), generatedIdToken.getIssuedAt(),
						generatedIdToken.getExpiresAt(), ((Jwt) generatedIdToken).getClaims());
				authorizationBuilder.token(idToken, (metadata) -> metadata
					.put(OAuth2Authorization.Token.CLAIMS_METADATA_NAME, idToken.getClaims()));
			}
			OAuth2Authorization authorization = authorizationBuilder.build();
			authorizationService.save(authorization);
			return new OAuth2AccessTokenAuthenticationToken(registeredClient, clientPrincipal, accessToken,
					refreshToken, Collections.emptyMap());
		}
		finally {
			// 清空上下文
			SecurityContextHolder.clearContext();
		}
	}

	/**
	 * 获取用户信息.
	 * @param username 登录名
	 * @param password 密码
	 * @param request 请求参数
	 * @param captcha 验证码
	 * @param uuid 唯一标识
	 * @return UsernamePasswordAuthenticationToken
	 */
	protected UsernamePasswordAuthenticationToken authenticationToken(String username, String password,
			HttpServletRequest request, String captcha, String uuid) {
		try {
			AuthorizationGrantType grantType = getGrantType();
			String type = grantType.getValue();
			long tenantId = Long.parseLong(request.getParameter(TENANT_ID));
			String ip = IpUtil.getIpAddr(request);
			// 验证验证码
			Boolean validate = captchaGateway.validate(uuid, captcha);
			if (ObjectUtil.isNull(validate)) {
				throw authenticationException(CAPTCHA_EXPIRED, new User(username, tenantId), type, ip);
			}
			if (!validate) {
				throw authenticationException(CAPTCHA_ERROR, new User(username, tenantId), type, ip);
			}
			// 初始化（多数据源切换）
			String sourceName = getSourceName(tenantId);
			// 加密
			String encryptName;
			boolean passwordFlag = false;
			// 密码登录无需二次加密
			if (PASSWORD.equals(type)) {
				encryptName = username;
				passwordFlag = true;
			}
			else {
				encryptName = AesUtil.encrypt(username);
			}
			User user;
			try {
				// 多租户查询（多数据源）
				user = userGateway.getUserByUsername(new Auth(encryptName, type, AesUtil.getKey()));
			}
			catch (BadSqlGrammarException e) {
				log.error("表 boot_sys_user 不存在，错误信息：{}，详情见日志", LogUtil.result(e.getMessage()), e);
				throw OAuth2ExceptionHandler.getException(CUSTOM_SERVER_ERROR, "表 boot_sys_user 不存在");
			}
			if (ObjectUtil.isNull(user)) {
				throw authenticationException(ACCOUNT_PASSWORD_ERROR, new User(username, tenantId), type, ip);
			}
			if (passwordFlag) {
				// 验证密码
				String clientPassword = user.getPassword();
				if (!passwordEncoder.matches(password, clientPassword)) {
					throw authenticationException(ACCOUNT_PASSWORD_ERROR, user, type, ip);
				}
			}
			// 是否锁定
			if (!user.isEnabled()) {
				throw authenticationException(ACCOUNT_DISABLE, user, type, ip);
			}
			List<String> permissionsList;
			try {
				// 权限标识列表
				permissionsList = menuGateway.getPermissions(user);
			}
			catch (BadSqlGrammarException e) {
				log.error("表 boot_sys_menu 不存在，错误信息：{}，详情见日志", LogUtil.result(e.getMessage()), e);
				throw OAuth2ExceptionHandler.getException(CUSTOM_SERVER_ERROR, "表 boot_sys_menu 不存在");
			}
			if (CollectionUtil.isEmpty(permissionsList)) {
				throw authenticationException(FORBIDDEN, user, type, ip);
			}
			List<String> deptPaths;
			try {
				// 部门列表
				deptPaths = deptGateway.getDeptPaths(user);
			}
			catch (BadSqlGrammarException e) {
				log.error("表 boot_sys_dept 不存在，错误信息：{}，详情见日志", LogUtil.result(e.getMessage()), e);
				throw OAuth2ExceptionHandler.getException(CUSTOM_SERVER_ERROR, "表 boot_sys_dept 不存在");
			}
			user.setDeptPaths(deptPaths);
			user.setPermissionList(permissionsList);
			// 数据源
			user.setSourceName(sourceName);
			// 登录IP
			user.setLoginIp(ip);
			// 登录时间
			user.setLoginDate(DateUtil.now());
			// 登录成功
			loginLogGateway.publish(new LoginLog(user.getId(), username, type, tenantId, SUCCESS,
					MessageUtil.getMessage(LOGIN_SUCCEEDED), ip, user.getDeptId(), user.getDeptPath()));
			return new UsernamePasswordAuthenticationToken(user, encryptName, user.getAuthorities());
		}
		finally {
			DynamicDataSourceContextHolder.clear();
			UserContextHolder.clear();
		}
	}

	private OAuth2ClientAuthenticationToken getAuthenticatedClientElseThrowInvalidClient(
			Authentication authentication) {
		OAuth2ClientAuthenticationToken clientPrincipal = null;
		if (OAuth2ClientAuthenticationToken.class.isAssignableFrom(authentication.getPrincipal().getClass())) {
			clientPrincipal = (OAuth2ClientAuthenticationToken) authentication.getPrincipal();
		}
		if (ObjectUtil.isNotNull(clientPrincipal) && clientPrincipal.isAuthenticated()) {
			return clientPrincipal;
		}
		throw OAuth2ExceptionHandler.getException(INVALID_CLIENT, MessageUtil.getMessage(INVALID_CLIENT));
	}

	private OAuth2AuthenticationException authenticationException(int code, User user, String type, String ip) {
		String message = MessageUtil.getMessage(code);
		// log.error("登录失败，状态码：{}，错误信息：{}", code, message);
		loginLogGateway.publish(new LoginLog(user.getId(), user.getUsername(), type, user.getTenantId(), FAIL, message,
				ip, user.getDeptId(), user.getDeptPath()));
		throw OAuth2ExceptionHandler.getException(code, message);
	}

	private String getSourceName(Long tenantId) {
		// 默认数据源
		String sourceName = MASTER;
		try {
			if (tenantId != DEFAULT) {
				// 租户数据源
				Source source = sourceGateway.getSourceName(tenantId);
				sourceName = source.getName();
				if (!dynamicUtil.getDataSources().containsKey(sourceName)) {
					DataSourceProperty properties = properties(source);
					// 验证连接
					validate(properties);
					dynamicUtil.getDataSource().addDataSource(sourceName, dataSource(properties));
				}
			}
		}
		finally {
			DynamicDataSourceContextHolder.push(sourceName);
			UserContextHolder.set(new UserContextHolder.User(tenantId, sourceName));
		}
		return sourceName;
	}

	private DataSource dataSource(DataSourceProperty properties) {
		HikariDataSourceCreator hikariDataSourceCreator = dynamicUtil.getHikariDataSourceCreator();
		return hikariDataSourceCreator.createDataSource(properties);
	}

	private DataSourceProperty properties(Source source) {
		DataSourceProperty properties = new DataSourceProperty();
		properties.setUsername(source.getUsername());
		properties.setPassword(source.getPassword());
		properties.setUrl(source.getUrl());
		properties.setDriverClassName(source.getDriverClassName());
		return properties;
	}

	@SneakyThrows
	private void validate(DataSourceProperty properties) {
		Connection connection = null;
		try {
			Class.forName(properties.getDriverClassName());
		}
		catch (Exception e) {
			log.error("加载数据源驱动失败，错误信息：{}，详情见日志", LogUtil.result(e.getMessage()), e);
			throw OAuth2ExceptionHandler.getException(CUSTOM_SERVER_ERROR, "加载数据源驱动失败");
		}
		try {
			// 1秒后连接超时
			DriverManager.setLoginTimeout(1);
			connection = DriverManager.getConnection(properties.getUrl(), properties.getUsername(),
					properties.getPassword());
		}
		catch (Exception e) {
			log.error("数据源连接超时，错误信息：{}，详情见日志", LogUtil.result(e.getMessage()), e);
			throw OAuth2ExceptionHandler.getException(CUSTOM_SERVER_ERROR, "数据源连接超时");
		}
		finally {
			if (ObjectUtil.isNotNull(connection)) {
				connection.close();
			}
		}
	}

}
