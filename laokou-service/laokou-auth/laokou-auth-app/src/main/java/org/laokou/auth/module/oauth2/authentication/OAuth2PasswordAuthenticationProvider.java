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

import static org.laokou.auth.common.Constant.CAPTCHA;
import static org.laokou.auth.common.Constant.UUID;
import static org.laokou.common.i18n.common.Constant.PASSWORD;
import static org.laokou.common.i18n.common.StatusCode.CUSTOM_SERVER_ERROR;
import static org.laokou.common.i18n.common.ValCode.*;

/**
 * @author laokou
 */
@Component
@Slf4j
public class OAuth2PasswordAuthenticationProvider extends AbstractOAuth2BaseAuthenticationProvider {

	public OAuth2PasswordAuthenticationProvider(UserGateway userGateway, MenuGateway menuGateway,
			DeptGateway deptGateway, PasswordEncoder passwordEncoder, CaptchaGateway captchaGateway,
			OAuth2AuthorizationService authorizationService, OAuth2TokenGenerator<? extends OAuth2Token> tokenGenerator,
			SourceGateway sourceGateway, RedisUtil redisUtil, DynamicUtil dynamicUtil,
			LoginLogGateway loginLogGateway) {
		super(userGateway, menuGateway, deptGateway, passwordEncoder, captchaGateway, authorizationService,
				tokenGenerator, sourceGateway, redisUtil, dynamicUtil, loginLogGateway);
	}

	@Override
	public boolean supports(Class<?> authentication) {
		return OAuth2PasswordAuthenticationToken.class.isAssignableFrom(authentication);
	}

	@Override
	Authentication principal(HttpServletRequest request) {
		// 判断UUID是否为空
		String uuid = request.getParameter(UUID);
		log.info("UUID：{}", uuid);
		if (StringUtil.isEmpty(uuid)) {
			throw OAuth2ExceptionHandler.getException(CUSTOM_SERVER_ERROR,
					ValidatorUtil.getMessage(OAUTH2_UUID_REQUIRE));
		}
		// 判断验证码是否为空
		String captcha = request.getParameter(CAPTCHA);
		log.info("验证码：{}", captcha);
		if (StringUtil.isEmpty(captcha)) {
			throw OAuth2ExceptionHandler.getException(CUSTOM_SERVER_ERROR,
					ValidatorUtil.getMessage(OAUTH2_CAPTCHA_REQUIRE));
		}
		// 验证账号是否为空
		String username = request.getParameter(OAuth2ParameterNames.USERNAME);
		log.info("账号：{}", username);
		if (StringUtil.isEmpty(username)) {
			throw OAuth2ExceptionHandler.getException(CUSTOM_SERVER_ERROR,
					ValidatorUtil.getMessage(OAUTH2_USERNAME_REQUIRE));
		}
		// 验证密码是否为空
		String password = request.getParameter(OAuth2ParameterNames.PASSWORD);
		log.info("密码：{}", password);
		if (StringUtil.isEmpty(password)) {
			throw OAuth2ExceptionHandler.getException(CUSTOM_SERVER_ERROR,
					ValidatorUtil.getMessage(OAUTH2_PASSWORD_REQUIRE));
		}
		// 获取用户信息,并认证信息
		return super.authenticationToken(username, password, request, captcha, uuid);
	}

	@Override
	AuthorizationGrantType getGrantType() {
		return new AuthorizationGrantType(PASSWORD);
	}

}
