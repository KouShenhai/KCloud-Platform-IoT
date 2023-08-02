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
package org.laokou.auth.config.authentication;

import jakarta.servlet.http.HttpServletRequest;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.laokou.auth.client.constant.AuthConstant;
import org.laokou.auth.client.handler.CustomAuthExceptionHandler;
import org.laokou.auth.client.user.UserDetail;
import org.laokou.auth.server.domain.sys.repository.service.SysDeptService;
import org.laokou.auth.server.domain.sys.repository.service.SysMenuService;
import org.laokou.auth.server.domain.sys.repository.service.SysUserService;
import org.laokou.common.core.enums.ResultStatusEnum;
import org.laokou.common.core.utils.CollectionUtil;
import org.laokou.common.core.utils.DateUtil;
import org.laokou.common.core.utils.IpUtil;
import org.laokou.common.core.utils.RequestUtil;
import org.laokou.common.easy.captcha.service.SysCaptchaService;
import org.laokou.common.i18n.core.StatusCode;
import org.laokou.common.i18n.utils.MessageUtil;
import org.laokou.common.jasypt.utils.AesUtil;
import org.laokou.common.log.utils.LoginLogUtil;
import org.laokou.common.redis.utils.RedisUtil;
import org.laokou.common.tenant.service.SysSourceService;
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

import static org.laokou.common.core.constant.Constant.DEFAULT;
import static org.laokou.common.core.constant.Constant.DEFAULT_SOURCE;

/**
 * 邮件/手机/密码
 *
 * @author laokou
 */
@Slf4j
public abstract class AbstractOAuth2BaseAuthenticationProvider implements AuthenticationProvider {

	protected final SysUserService sysUserService;

	protected final SysMenuService sysMenuService;

	protected final SysDeptService sysDeptService;

	protected final LoginLogUtil loginLogUtil;

	protected final PasswordEncoder passwordEncoder;

	protected final SysCaptchaService sysCaptchaService;

	protected final OAuth2AuthorizationService authorizationService;

	protected final OAuth2TokenGenerator<? extends OAuth2Token> tokenGenerator;

	protected final SysSourceService sysSourceService;

	protected final RedisUtil redisUtil;

	public AbstractOAuth2BaseAuthenticationProvider(SysUserService sysUserService, SysMenuService sysMenuService,
			SysDeptService sysDeptService, LoginLogUtil loginLogUtil, PasswordEncoder passwordEncoder,
			SysCaptchaService sysCaptchaService, OAuth2AuthorizationService authorizationService,
			OAuth2TokenGenerator<? extends OAuth2Token> tokenGenerator, SysSourceService sysSourceService,
			RedisUtil redisUtil) {
		this.sysDeptService = sysDeptService;
		this.sysMenuService = sysMenuService;
		this.loginLogUtil = loginLogUtil;
		this.sysUserService = sysUserService;
		this.passwordEncoder = passwordEncoder;
		this.sysCaptchaService = sysCaptchaService;
		this.tokenGenerator = tokenGenerator;
		this.authorizationService = authorizationService;
		this.sysSourceService = sysSourceService;
		this.redisUtil = redisUtil;
	}

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
	protected Authentication getToken(Authentication authentication, Authentication principal) throws IOException {
		// 仿照授权码模式
		// 生成token（access_token + refresh_token）
		AbstractOAuth2BaseAuthenticationToken auth2BaseAuthenticationToken = (AbstractOAuth2BaseAuthenticationToken) authentication;
		OAuth2ClientAuthenticationToken clientPrincipal = getAuthenticatedClientElseThrowInvalidClient(
				auth2BaseAuthenticationToken);
		RegisteredClient registeredClient = clientPrincipal.getRegisteredClient();
		if (registeredClient == null) {
			throw CustomAuthExceptionHandler.getError(StatusCode.INTERNAL_SERVER_ERROR, "registeredClient不存在");
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
				.orElseThrow(() -> CustomAuthExceptionHandler.getError(OAuth2ErrorCodes.SERVER_ERROR, "令牌生成器无法生成访问令牌",
						CustomAuthExceptionHandler.ERROR_URI));
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
		}
		else {
			authorizationBuilder.accessToken(oAuth2AccessToken);
		}
		// 生成refresh_token
		context = builder.authorizationGrantType(AuthorizationGrantType.REFRESH_TOKEN)
				.tokenType(OAuth2TokenType.REFRESH_TOKEN).build();
		OAuth2Token generateOauth2RefreshToken = Optional.ofNullable(tokenGenerator.generate(context))
				.orElseThrow(() -> CustomAuthExceptionHandler.getError(OAuth2ErrorCodes.SERVER_ERROR, "令牌生成器无法生成刷新令牌",
						CustomAuthExceptionHandler.ERROR_URI));
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
		Long tenantId = Long.valueOf(request.getParameter(AuthConstant.TENANT_ID));
		// 验证验证码
		Boolean validate = sysCaptchaService.validate(uuid, captcha);
		int code;
		String msg;
		if (null == validate) {
			code = StatusCode.CAPTCHA_EXPIRED;
			msg = MessageUtil.getMessage(code);
			log.info("登录失败，状态码：{}，错误信息：{}", code, msg);
			loginLogUtil.recordLogin(loginName, loginType, ResultStatusEnum.FAIL.ordinal(), msg, request, tenantId);
			throw CustomAuthExceptionHandler.getError(code, msg);
		}
		if (Boolean.FALSE.equals(validate)) {
			code = StatusCode.CAPTCHA_ERROR;
			msg = MessageUtil.getMessage(code);
			log.info("登录失败，状态码：{}，错误信息：{}", code, msg);
			loginLogUtil.recordLogin(loginName, loginType, ResultStatusEnum.FAIL.ordinal(), msg, request, tenantId);
			throw CustomAuthExceptionHandler.getError(code, msg);
		}
		// 加密
		String encryptName = AesUtil.encrypt(loginName);
		// 多租户查询
		UserDetail userDetail = sysUserService.getUserDetail(encryptName, tenantId, loginType);
		if (userDetail == null) {
			code = StatusCode.USERNAME_PASSWORD_ERROR;
			msg = MessageUtil.getMessage(code);
			log.info("登录失败，状态码：{}，错误信息：{}", code, msg);
			loginLogUtil.recordLogin(loginName, loginType, ResultStatusEnum.FAIL.ordinal(), msg, request, tenantId);
			throw CustomAuthExceptionHandler.getError(code, msg);
		}
		if (OAuth2PasswordAuthenticationProvider.GRANT_TYPE.equals(loginType)) {
			// 验证密码
			String clientPassword = userDetail.getPassword();
			if (!passwordEncoder.matches(password, clientPassword)) {
				code = StatusCode.USERNAME_PASSWORD_ERROR;
				msg = MessageUtil.getMessage(code);
				log.info("登录失败，状态码：{}，错误信息：{}", code, msg);
				loginLogUtil.recordLogin(loginName, loginType, ResultStatusEnum.FAIL.ordinal(), msg, request, tenantId);
				throw CustomAuthExceptionHandler.getError(code, msg);
			}
		}
		// 是否锁定
		if (!userDetail.isEnabled()) {
			code = StatusCode.USERNAME_DISABLE;
			msg = MessageUtil.getMessage(code);
			log.info("登录失败，状态码：{}，错误信息：{}", code, msg);
			loginLogUtil.recordLogin(loginName, loginType, ResultStatusEnum.FAIL.ordinal(), msg, request, tenantId);
			throw CustomAuthExceptionHandler.getError(code, msg);
		}
		Long userId = userDetail.getId();
		Integer superAdmin = userDetail.getSuperAdmin();
		// 权限标识列表
		List<String> permissionsList = sysMenuService.getPermissionsList(tenantId, superAdmin, userId);
		if (CollectionUtil.isEmpty(permissionsList)) {
			code = StatusCode.USERNAME_NOT_PERMISSION;
			msg = MessageUtil.getMessage(code);
			log.info("登录失败，状态码：{}，错误信息：{}", code, msg);
			loginLogUtil.recordLogin(loginName, loginType, ResultStatusEnum.FAIL.ordinal(), msg, request, tenantId);
			throw CustomAuthExceptionHandler.getError(code, msg);
		}
		// 部门列表
		List<Long> deptIds = sysDeptService.getDeptIds(superAdmin, userId, tenantId);
		userDetail.setDeptIds(deptIds);
		userDetail.setPermissionList(permissionsList);
		if (tenantId == DEFAULT) {
			// 默认数据源
			userDetail.setSourceName(DEFAULT_SOURCE);
		}
		else {
			// 租户数据源
			String sourceName = sysSourceService.querySourceName(tenantId);
			userDetail.setSourceName(sourceName);
		}
		// 登录IP
		userDetail.setLoginIp(IpUtil.getIpAddr(request));
		// 登录时间
		userDetail.setLoginDate(DateUtil.now());
		// 登录成功
		loginLogUtil.recordLogin(loginName, loginType, ResultStatusEnum.SUCCESS.ordinal(),
				AuthConstant.LOGIN_SUCCESS_MSG, request, tenantId);
		log.info(AuthConstant.LOGIN_SUCCESS_MSG);
		return new UsernamePasswordAuthenticationToken(userDetail, encryptName, userDetail.getAuthorities());
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
		throw CustomAuthExceptionHandler.getError(StatusCode.INVALID_CLIENT,
				MessageUtil.getMessage(StatusCode.INVALID_CLIENT));
	}

}
