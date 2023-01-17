package org.laokou.auth.server.infrastructure.authentication;///**
/**
 * Copyright (c) 2022 KCloud-Platform-Alibaba Authors. All Rights Reserved.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.laokou.auth.client.constant.AuthConstant;
import org.laokou.auth.server.domain.sys.repository.service.SysCaptchaService;
import org.laokou.auth.server.domain.sys.repository.service.SysDeptService;
import org.laokou.auth.server.domain.sys.repository.service.SysMenuService;
import org.laokou.auth.server.domain.sys.repository.service.SysUserService;
import org.laokou.auth.server.infrastructure.log.LoginLogUtil;
import org.laokou.common.core.utils.RegexUtil;
import org.laokou.common.core.utils.StringUtil;
import org.laokou.common.swagger.exception.CustomException;
import org.laokou.common.swagger.exception.ErrorCode;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.OAuth2Token;
import org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationService;
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenGenerator;
/**
 * @author laokou
 */
@Slf4j
public class OAuth2SmsAuthenticationProvider extends AbstractOAuth2BaseAuthenticationProvider {

    public static final String GRANT_TYPE = "sms";

    public OAuth2SmsAuthenticationProvider(SysUserService sysUserService
            , SysMenuService sysMenuService
            , SysDeptService sysDeptService
            , LoginLogUtil loginLogUtil
            , PasswordEncoder passwordEncoder
            , SysCaptchaService sysCaptchaService
            , OAuth2AuthorizationService oAuth2AuthorizationService
            , OAuth2TokenGenerator<? extends OAuth2Token> tokenGenerator) {
        super(sysUserService, sysMenuService, sysDeptService, loginLogUtil, passwordEncoder,sysCaptchaService,oAuth2AuthorizationService,tokenGenerator);
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return OAuth2SmsAuthenticationToken.class.isAssignableFrom(authentication);
    }

    @Override
    Authentication login(HttpServletRequest request) {
        String code = request.getParameter(OAuth2ParameterNames.CODE);
        log.info("验证码：{}",code);
        if (StringUtil.isEmpty(code)) {
            throw new CustomException(ErrorCode.CAPTCHA_NOT_NULL);
        }
        String mobile = request.getParameter(AuthConstant.MOBILE);
        log.info("手机：{}",mobile);
        if (StringUtil.isEmpty(mobile)) {
            throw new CustomException("手机号不为空");
        }
        boolean isMobile = RegexUtil.mobileRegex(mobile);
        if (!isMobile) {
            throw new CustomException("手机号格式不对");
        }
        // TODO 验证验证码
        // 获取用户信息
        return super.getUserInfo(mobile, "", request);
    }

    @Override
    AuthorizationGrantType getGrantType() {
            return new AuthorizationGrantType(GRANT_TYPE);
    }
}
