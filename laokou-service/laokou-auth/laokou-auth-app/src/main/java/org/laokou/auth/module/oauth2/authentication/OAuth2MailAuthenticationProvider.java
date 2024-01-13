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

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.laokou.auth.common.exception.handler.OAuth2ExceptionHandler;
import org.laokou.auth.domain.gateway.*;
import org.laokou.common.core.utils.RegexUtil;
import org.laokou.common.i18n.utils.MessageUtil;
import org.laokou.common.i18n.utils.StringUtil;
import org.laokou.common.i18n.utils.ValidatorUtil;
import org.laokou.common.mybatisplus.utils.DynamicUtil;
import org.laokou.common.redis.utils.RedisUtil;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.OAuth2Token;
import org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationService;
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenGenerator;
import org.springframework.stereotype.Component;

import static org.laokou.common.i18n.common.ErrorCodes.MAIL_ERROR;
import static org.laokou.common.i18n.common.OAuth2Constants.MAIL;
import static org.laokou.common.i18n.common.StatusCodes.CUSTOM_SERVER_ERROR;
import static org.laokou.common.i18n.common.StringConstants.EMPTY;
import static org.laokou.common.i18n.common.ValCodes.OAUTH2_CAPTCHA_REQUIRE;
import static org.laokou.common.i18n.common.ValCodes.OAUTH2_MAIL_REQUIRE;

/**
 * @author laokou
 */
@Slf4j
@Component
public class OAuth2MailAuthenticationProvider extends AbstractOAuth2BaseAuthenticationProvider {

	public OAuth2MailAuthenticationProvider(UserGateway userGateway, MenuGateway menuGateway, DeptGateway deptGateway,
			PasswordEncoder passwordEncoder, CaptchaGateway captchaGateway,
			OAuth2AuthorizationService authorizationService, OAuth2TokenGenerator<? extends OAuth2Token> tokenGenerator,
			SourceGateway sourceGateway, RedisUtil redisUtil, DynamicUtil dynamicUtil,
			LoginLogGateway loginLogGateway) {
		super(userGateway, menuGateway, deptGateway, passwordEncoder, captchaGateway, authorizationService,
				tokenGenerator, sourceGateway, redisUtil, dynamicUtil, loginLogGateway);
	}

	@Override
	public boolean supports(Class<?> authentication) {
		return OAuth2MailAuthenticationToken.class.isAssignableFrom(authentication);
	}

	@Override
	Authentication principal(HttpServletRequest request) {
		// 判断验证码
		String code = request.getParameter(OAuth2ParameterNames.CODE);
		// log.info("验证码：{}", code);
		if (StringUtil.isEmpty(code)) {
			throw OAuth2ExceptionHandler.getException(CUSTOM_SERVER_ERROR,
					ValidatorUtil.getMessage(OAUTH2_CAPTCHA_REQUIRE));
		}
		String mail = request.getParameter(MAIL);
		// log.info("邮箱：{}", SensitiveUtil.format(Type.MAIL, mail));
		if (StringUtil.isEmpty(mail)) {
			throw OAuth2ExceptionHandler.getException(CUSTOM_SERVER_ERROR,
					ValidatorUtil.getMessage(OAUTH2_MAIL_REQUIRE));
		}
		boolean isMail = RegexUtil.mailRegex(mail);
		if (!isMail) {
			throw OAuth2ExceptionHandler.getException(MAIL_ERROR, MessageUtil.getMessage(MAIL_ERROR));
		}
		// 获取用户信息,并认证信息
		return super.authenticationToken(mail, EMPTY, request, code, mail);
	}

	@Override
	AuthorizationGrantType getGrantType() {
		return new AuthorizationGrantType(MAIL);
	}

}
