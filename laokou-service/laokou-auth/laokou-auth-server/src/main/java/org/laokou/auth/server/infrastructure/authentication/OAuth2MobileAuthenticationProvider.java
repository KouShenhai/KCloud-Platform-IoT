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
package org.laokou.auth.server.infrastructure.authentication;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.laokou.auth.client.constant.AuthConstant;
import org.laokou.auth.client.handler.CustomAuthExceptionHandler;
import org.laokou.auth.server.domain.sys.repository.service.*;
import org.laokou.common.easy.captcha.service.SysCaptchaService;
import org.laokou.common.i18n.core.StatusCode;
import org.laokou.common.i18n.utils.MessageUtil;
import org.laokou.common.core.utils.RegexUtil;
import org.laokou.common.i18n.utils.StringUtil;
import org.laokou.common.log.utils.LoginLogUtil;
import org.laokou.common.redis.utils.RedisUtil;
import org.laokou.common.sensitive.enums.TypeEnum;
import org.laokou.common.sensitive.utils.SensitiveUtil;
import org.laokou.common.tenant.service.SysSourceService;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.OAuth2Token;
import org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationService;
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenGenerator;

import java.io.IOException;

/**
 * @author laokou
 */
@Slf4j
public class OAuth2MobileAuthenticationProvider extends AbstractOAuth2BaseAuthenticationProvider {

	public static final String GRANT_TYPE = "mobile";

	public OAuth2MobileAuthenticationProvider(SysUserService sysUserService, SysMenuService sysMenuService,
			SysDeptService sysDeptService, LoginLogUtil loginLogUtil, PasswordEncoder passwordEncoder,
			SysCaptchaService sysCaptchaService, OAuth2AuthorizationService oAuth2AuthorizationService,
			OAuth2TokenGenerator<? extends OAuth2Token> tokenGenerator, SysSourceService sysSourceService,
			RedisUtil redisUtil) {
		super(sysUserService, sysMenuService, sysDeptService, loginLogUtil, passwordEncoder, sysCaptchaService,
				oAuth2AuthorizationService, tokenGenerator, sysSourceService, redisUtil);
	}

	@Override
	public boolean supports(Class<?> authentication) {
		return OAuth2MobileAuthenticationToken.class.isAssignableFrom(authentication);
	}

	@Override
	Authentication login(HttpServletRequest request) throws IOException {
		String code = request.getParameter(OAuth2ParameterNames.CODE);
		log.info("验证码：{}", code);
		if (StringUtil.isEmpty(code)) {
			throw CustomAuthExceptionHandler.getError(StatusCode.CAPTCHA_NOT_NULL,
					MessageUtil.getMessage(StatusCode.CAPTCHA_NOT_NULL));
		}
		String mobile = request.getParameter(AuthConstant.MOBILE);
		log.info("手机：{}", SensitiveUtil.format(TypeEnum.MOBILE, mobile));
		if (StringUtil.isEmpty(mobile)) {
			throw CustomAuthExceptionHandler.getError(StatusCode.MOBILE_NOT_NULL,
					MessageUtil.getMessage(StatusCode.MOBILE_NOT_NULL));
		}
		boolean isMobile = RegexUtil.mobileRegex(mobile);
		if (!isMobile) {
			throw CustomAuthExceptionHandler.getError(StatusCode.MOBILE_ERROR,
					MessageUtil.getMessage(StatusCode.MOBILE_ERROR));
		}
		// 获取用户信息,并认证信息
		return super.getUserInfo(mobile, "", request, code, mobile);
	}

	@Override
	AuthorizationGrantType getGrantType() {
		return new AuthorizationGrantType(GRANT_TYPE);
	}

}
