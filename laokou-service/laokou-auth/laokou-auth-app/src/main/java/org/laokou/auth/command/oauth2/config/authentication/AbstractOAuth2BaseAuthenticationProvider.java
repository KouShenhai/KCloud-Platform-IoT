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
package org.laokou.auth.command.oauth2.config.authentication;

import eu.bitwalker.useragentutils.UserAgent;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.laokou.auth.domain.gateway.CaptchaGateway;
import org.laokou.auth.domain.gateway.DeptGateway;
import org.laokou.auth.domain.gateway.MenuGateway;
import org.laokou.auth.domain.gateway.UserGateway;
import org.laokou.auth.domain.user.User;
import org.laokou.common.core.enums.ResultStatusEnum;
import org.laokou.common.core.utils.*;
import org.laokou.common.i18n.common.StatusCode;
import org.laokou.common.i18n.utils.MessageUtil;
import org.laokou.common.ip.region.utils.AddressUtil;
import org.laokou.common.jasypt.utils.AesUtil;
import org.laokou.auth.common.event.DomainEventPublisher;
import org.laokou.common.log.event.LoginLogEvent;
import org.laokou.common.redis.utils.RedisUtil;
import org.laokou.common.security.exception.handler.OAuth2ExceptionHandler;
import org.laokou.common.tenant.service.SysSourceService;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.*;
import org.springframework.security.oauth2.server.authorization.OAuth2Authorization;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationService;
import org.springframework.security.oauth2.server.authorization.OAuth2TokenType;
import org.springframework.security.oauth2.server.authorization.authentication.OAuth2AccessTokenAuthenticationToken;
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
import static org.laokou.auth.common.Constant.*;
import static org.laokou.auth.common.exception.ErrorCode.*;

/**
 * 邮件/手机/密码
 *
 * @author laokou
 */
@Slf4j
@RequiredArgsConstructor
public abstract class AbstractOAuth2BaseAuthenticationProvider implements AuthenticationProvider {

	protected final UserGateway userGateway;
	protected final MenuGateway menuGateway;
	protected final DeptGateway deptGateway;
	protected final DomainEventPublisher loginLogUtil;
	protected final PasswordEncoder passwordEncoder;
	protected final CaptchaGateway captchaGateway;
	protected final OAuth2AuthorizationService authorizationService;
	protected final OAuth2TokenGenerator<? extends OAuth2Token> tokenGenerator;
	protected final SysSourceService sysSourceService;
	protected final RedisUtil redisUtil;
	protected final DomainEventPublisher domainEventPublisher;

	@SneakyThrows
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {
		HttpServletRequest request = RequestUtil.getHttpServletRequest();
		return getToken(authentication, login(request));
	}

	/**
	 * Token是否支持认证（provider）
	 * @param authentication 类型
	 * @return boolean
	 */
	abstract public boolean supports(Class<?> authentication);

	/**
	 * 登录
	 * @param request request
	 * @return Authentication
	 * @throws IOException IOException
	 */
	abstract Authentication login(HttpServletRequest request) throws IOException;

	/**
	 * 认证类型
	 * @return AuthorizationGrantType
	 */
	abstract AuthorizationGrantType getGrantType();

	/**
	 * 获取token
	 * @param authentication authentication
	 * @param principal principal
	 * @return Authentication
	 */
	protected Authentication getToken(Authentication authentication, Authentication principal) {
		// 仿照授权码模式
		// 生成token（access_token + refresh_token）
		AbstractOAuth2BaseAuthenticationToken auth2BaseAuthenticationToken = (AbstractOAuth2BaseAuthenticationToken) authentication;
		OAuth2ClientAuthenticationToken clientPrincipal = getAuthenticatedClientElseThrowInvalidClient(
				auth2BaseAuthenticationToken);
		RegisteredClient registeredClient = clientPrincipal.getRegisteredClient();
		if (registeredClient == null) {
			throw OAuth2ExceptionHandler.getException(StatusCode.INTERNAL_SERVER_ERROR, "registeredClient不存在");
		}
		// 获取认证范围
		Set<String> scopes = registeredClient.getScopes();
		String loginName = principal.getCredentials().toString();
		// 认证类型
		AuthorizationGrantType grantType = getGrantType();
		// 获取上下文
		DefaultOAuth2TokenContext.Builder builder = DefaultOAuth2TokenContext.builder()
				.registeredClient(registeredClient).principal(principal).tokenType(OAuth2TokenType.ACCESS_TOKEN)
				.authorizedScopes(scopes).authorizationServerContext(AuthorizationServerContextHolder.getContext())
				.authorizationGrantType(grantType);
		DefaultOAuth2TokenContext context = builder.build();
		OAuth2Authorization.Builder authorizationBuilder = OAuth2Authorization.withRegisteredClient(registeredClient)
				.principalName(loginName).authorizedScopes(scopes).authorizationGrantType(grantType);
		// 生成access_token
		OAuth2Token generatedOauth2AccessToken = Optional.ofNullable(tokenGenerator.generate(context))
				.orElseThrow(() -> OAuth2ExceptionHandler.getException(OAuth2ErrorCodes.SERVER_ERROR, "令牌生成器无法生成访问令牌",
						OAuth2ExceptionHandler.ERROR_URI));
		OAuth2AccessToken oAuth2AccessToken = new OAuth2AccessToken(OAuth2AccessToken.TokenType.BEARER,
				generatedOauth2AccessToken.getTokenValue(), generatedOauth2AccessToken.getIssuedAt(),
				generatedOauth2AccessToken.getExpiresAt(), context.getAuthorizedScopes());
		// jwt
		if (generatedOauth2AccessToken instanceof ClaimAccessor) {
			authorizationBuilder
					.token(oAuth2AccessToken,
							meta -> meta.put(OAuth2Authorization.Token.CLAIMS_METADATA_NAME,
									((ClaimAccessor) generatedOauth2AccessToken).getClaims()))
					.authorizedScopes(scopes)
					// admin后台管理需要token，解析token获取用户信息，因此将用户信息存在数据库，下次直接查询数据库就可以获取用户信息
					.attribute(Principal.class.getName(), principal);
		} else {
			authorizationBuilder.accessToken(oAuth2AccessToken);
		}
		// 生成refresh_token
		context = builder.authorizationGrantType(AuthorizationGrantType.REFRESH_TOKEN)
				.tokenType(OAuth2TokenType.REFRESH_TOKEN).build();
		OAuth2Token generateOauth2RefreshToken = Optional.ofNullable(tokenGenerator.generate(context))
				.orElseThrow(() -> OAuth2ExceptionHandler.getException(OAuth2ErrorCodes.SERVER_ERROR, "令牌生成器无法生成刷新令牌",
						OAuth2ExceptionHandler.ERROR_URI));
		OAuth2RefreshToken oAuth2RefreshToken = (OAuth2RefreshToken) generateOauth2RefreshToken;
		authorizationBuilder.refreshToken(oAuth2RefreshToken);
		OAuth2Authorization oAuth2Authorization = authorizationBuilder.build();
		authorizationService.save(oAuth2Authorization);
		// 清空上下文
		SecurityContextHolder.clearContext();
		return new OAuth2AccessTokenAuthenticationToken(registeredClient, clientPrincipal, oAuth2AccessToken,
				oAuth2RefreshToken, Collections.emptyMap());
	}

	/**
	 * 获取用户信息
	 * @param loginName 登录名
	 * @param password 密码
	 * @param request 请求参数
	 * @param captcha 验证码
	 * @param uuid 唯一标识
	 * @return UsernamePasswordAuthenticationToken
	 */
	protected UsernamePasswordAuthenticationToken getUserInfo(String loginName, String password,
															  HttpServletRequest request, String captcha, String uuid) {
		AuthorizationGrantType grantType = getGrantType();
		String loginType = grantType.getValue();
		Long tenantId = Long.valueOf(request.getParameter(TENANT_ID));
		// 验证验证码
		Boolean validate = captchaGateway.validate(uuid, captcha);
		if (validate == null) {
			throw getException(CAPTCHA_EXPIRED,loginName,loginType,request,tenantId);
		}
		if (Boolean.FALSE.equals(validate)) {
			throw getException(CAPTCHA_ERROR,loginName,loginType,request,tenantId);
		}
		// 加密
		String encryptName = AesUtil.encrypt(loginName);
		// 多租户查询
		User user = userGateway.getUserByUsername(encryptName, tenantId, loginType);
		if (user == null) {
			throw getException(USERNAME_PASSWORD_ERROR,loginName,loginType,request,tenantId);
		}
		if (AUTH_PASSWORD.equals(loginType)) {
			// 验证密码
			String clientPassword = user.getPassword();
			if (!passwordEncoder.matches(password, clientPassword)) {
				throw getException(USERNAME_PASSWORD_ERROR,loginName,loginType,request,tenantId);
			}
		}
		// 是否锁定
		if (!user.isEnabled()) {
			throw getException(USERNAME_DISABLE,loginName,loginType,request,tenantId);
		}
		Long userId = user.getId();
		Integer superAdmin = user.getSuperAdmin();
		// 权限标识列表
		List<String> permissionsList = menuGateway.getPermissions(userId,tenantId,superAdmin);
		if (CollectionUtil.isEmpty(permissionsList)) {
			throw getException(USERNAME_NOT_PERMISSION,loginName,loginType,request,tenantId);
		}
		// 部门列表
		List<Long> deptIds = deptGateway.getDeptIds(userId,tenantId,superAdmin);
		user.setDeptIds(deptIds);
		user.setPermissionList(permissionsList);
		if (tenantId == DEFAULT_TENANT) {
			// 默认数据源
			user.setSourceName(DEFAULT_SOURCE);
		} else {
			// 租户数据源
			String sourceName = sysSourceService.querySourceName(tenantId);
			user.setSourceName(sourceName);
		}
		// 登录IP
		user.setLoginIp(IpUtil.getIpAddr(request));
		// 登录时间
		user.setLoginDate(DateUtil.now());
		// 登录成功
		domainEventPublisher.publish(getLoginLogEvent(loginName, loginType, ResultStatusEnum.SUCCESS.ordinal(), "登录成功", request, tenantId));
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
		throw OAuth2ExceptionHandler.getException(INVALID_CLIENT,
				MessageUtil.getMessage(INVALID_CLIENT));
	}

	private OAuth2AuthenticationException getException(int code,String loginName,String loginType,HttpServletRequest request,Long tenantId) {
		String msg = MessageUtil.getMessage(code);
		log.info("登录失败，状态码：{}，错误信息：{}", code, msg);
		domainEventPublisher.publish(getLoginLogEvent(loginName, loginType, ResultStatusEnum.FAIL.ordinal(), msg, request, tenantId));
		throw OAuth2ExceptionHandler.getException(code, msg);
	}

	private LoginLogEvent getLoginLogEvent(String username, String loginType, Integer status, String msg, HttpServletRequest request, Long tenantId) {
		UserAgent userAgent = UserAgent.parseUserAgentString(request.getHeader(HttpHeaders.USER_AGENT));
		// IP地址
		String ip = IpUtil.getIpAddr(request);
		// 获取客户端操作系统
		String os = userAgent.getOperatingSystem().getName();
		// 获取客户端浏览器
		String browser = userAgent.getBrowser().getName();
		LoginLogEvent event = new LoginLogEvent(this);
		event.setLoginName(username);
		event.setRequestIp(ip);
		event.setRequestAddress(AddressUtil.getRealAddress(ip));
		event.setBrowser(browser);
		event.setOs(os);
		event.setMsg(msg);
		event.setLoginType(loginType);
		event.setRequestStatus(status);
		event.setTenantId(tenantId);
		return event;
	}

}
