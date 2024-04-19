/*
 * Copyright (c) 2022-2024 KCloud-Platform-Alibaba Author or Authors. All Rights Reserved.
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

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.laokou.auth.domain.model.auth.CaptchaV;
import org.laokou.auth.domain.model.auth.AuthA;
import org.laokou.common.crypto.utils.AesUtil;
import org.laokou.common.i18n.common.exception.AuthException;
import org.laokou.common.i18n.utils.StringUtil;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.OAuth2Token;
import org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationService;
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenGenerator;
import org.springframework.stereotype.Component;
import static org.laokou.common.i18n.common.TenantConstant.TENANT_ID;
import static org.laokou.common.security.handler.OAuth2ExceptionHandler.ERROR_URL;
import static org.laokou.common.security.handler.OAuth2ExceptionHandler.getException;

/**
 * 密码处理器.
 *
 * @author laokou
 */
@Slf4j
@Component("passwordAuthenticationProvider")
public class OAuth2PasswordAuthenticationProvider extends AbstractOAuth2AuthenticationProvider {

	@Schema(name = "CAPTCHA", description = "验证码")
	private static final String CAPTCHA = "captcha";

	@Schema(name = "UUID", description = "唯一标识")
	private static final String UUID = "uuid";

	public OAuth2PasswordAuthenticationProvider(OAuth2AuthorizationService authorizationService,
			OAuth2TokenGenerator<? extends OAuth2Token> tokenGenerator, OAuth2AuthenticationProvider authProvider) {
		super(authorizationService, tokenGenerator, authProvider);
	}

	@Override
	public boolean supports(Class<?> authentication) {
		return OAuth2PasswordAuthenticationToken.class.isAssignableFrom(authentication);
	}

	@Override
	Authentication principal(HttpServletRequest request) {
		try {
			String tenantId = request.getParameter(TENANT_ID);
			String uuid = request.getParameter(UUID);
			String captcha = request.getParameter(CAPTCHA);
			String username = request.getParameter(OAuth2ParameterNames.USERNAME);
			String password = request.getParameter(OAuth2ParameterNames.PASSWORD);
			// log.info("UUID：{}", uuid);
			// log.info("验证码：{}", captcha);
			// log.info("账号：{}", username);
			// log.info("密码：{}", password);
			// log.info("租户ID：{}", tenantId);
			CaptchaV captchaVObj = CaptchaV.builder().uuid(uuid).captcha(captcha).build();
			SecretKey secretKeyObj = SecretKey.builder()
				.secretKey(AesUtil.getSecretKeyStr())
				.type(getGrantType().getValue())
				.build();
			AuthA authA = AuthA.builder()
				.tenantId(StringUtil.parseLong(tenantId))
				.secretKey(secretKeyObj)
				.username(username)
				.password(password)
				.captchaV(captchaVObj)
				.build();
			authA.checkUsernamePasswordAuth();
			// 获取用户信息，并认证信息
			return super.authenticationToken(authA, request);
		}
		catch (AuthException e) {
			throw getException(e.getCode(), e.getMsg(), ERROR_URL);
		}
	}

	@Override
	AuthorizationGrantType getGrantType() {
		return new AuthorizationGrantType(OAuth2ParameterNames.PASSWORD);
	}

}
