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

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.laokou.auth.domain.auth.CaptchaV;
import org.laokou.auth.domain.auth.AuthA;
import org.laokou.common.crypto.utils.AesUtil;
import org.laokou.common.i18n.common.exception.AuthException;
import org.laokou.common.i18n.utils.StringUtil;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.OAuth2Token;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationService;
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenGenerator;
import org.springframework.stereotype.Component;

import static org.laokou.auth.config.authentication.AbstractOAuth2AuthenticationConverter.MOBILE;
import static org.laokou.common.i18n.common.TenantConstant.TENANT_ID;
import static org.laokou.common.security.handler.OAuth2ExceptionHandler.ERROR_URL;
import static org.laokou.common.security.handler.OAuth2ExceptionHandler.getException;
import static org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames.CODE;

/**
 * 手机号处理器.
 *
 * @author laokou
 */
@Slf4j
@Component("mobileAuthenticationProvider")
public class OAuth2MobileAuthenticationProvider extends AbstractOAuth2AuthenticationProvider {

	public OAuth2MobileAuthenticationProvider(OAuth2AuthorizationService authorizationService,
			OAuth2TokenGenerator<? extends OAuth2Token> tokenGenerator, OAuth2AuthenticationProvider authProvider) {
		super(authorizationService, tokenGenerator, authProvider);
	}

	@Override
	public boolean supports(Class<?> authentication) {
		return OAuth2MobileAuthenticationToken.class.isAssignableFrom(authentication);
	}

	@Override
	Authentication principal(HttpServletRequest request) {
		try {
			String tenantId = request.getParameter(TENANT_ID);
			String code = request.getParameter(CODE);
			String mobile = request.getParameter(MOBILE);
			// log.info("租户ID：{}", tenantId);
			// log.info("验证码：{}", code);
			// log.info("手机：{}", SensitiveUtil.format(Type.MOBILE, mobile));
			CaptchaV captchaVObj = CaptchaV.builder().captcha(code).uuid(mobile).build();
			SecretKey secretKeyObj = SecretKey.builder().type(getGrantType().getValue()).secretKey(AesUtil.getSecretKeyStr()).build();
			AuthA authA = AuthA.builder()
				.tenantId(StringUtil.parseLong(tenantId))
				.mobile(mobile)
				.captcha(captchaVObj)
				.username(encryptAes(mobile))
				.secretKey(secretKeyObj)
				.build();
			authA.checkMobileAuth();
			// 获取用户信息,并认证信息
			return super.authenticationToken(authA, request);
		}
		catch (AuthException e) {
			throw getException(e.getCode(), e.getMsg(), ERROR_URL);
		}
	}

	@Override
	AuthorizationGrantType getGrantType() {
		return new AuthorizationGrantType(MOBILE);
	}

}
