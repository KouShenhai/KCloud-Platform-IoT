/**
 * Copyright (c) 2022 KCloud-Platform-Alibaba Authors. All Rights Reserved.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 *   http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.laokou.auth.server.infrastructure.authentication;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.laokou.auth.client.constant.AuthConstant;
import org.laokou.auth.client.exception.CustomAuthExceptionHandler;
import org.laokou.auth.server.domain.sys.repository.service.*;
import org.laokou.common.i18n.core.StatusCode;
import org.laokou.common.i18n.utils.MessageUtil;
import org.laokou.common.core.utils.RegexUtil;
import org.laokou.common.core.utils.StringUtil;
import org.laokou.common.log.utils.LoginLogUtil;
import org.laokou.redis.utils.RedisUtil;
import org.laokou.tenant.service.SysSourceService;
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
public class OAuth2MailAuthenticationProvider extends AbstractOAuth2BaseAuthenticationProvider {

    public static final String GRANT_TYPE = "mail";

    public OAuth2MailAuthenticationProvider(SysUserService sysUserService
            , SysMenuService sysMenuService
            , SysDeptService sysDeptService
            , LoginLogUtil loginLogUtil
            , PasswordEncoder passwordEncoder
            , SysCaptchaService sysCaptchaService
            , OAuth2AuthorizationService oAuth2AuthorizationService
            , OAuth2TokenGenerator<? extends OAuth2Token> tokenGenerator
            , SysSourceService sysSourceService
            , SysAuthenticationService sysAuthenticationService
            , RedisUtil redisUtil) {
        super(sysUserService, sysMenuService, sysDeptService, loginLogUtil, passwordEncoder,sysCaptchaService,oAuth2AuthorizationService,tokenGenerator, sysSourceService,sysAuthenticationService,redisUtil);
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return OAuth2MailAuthenticationToken.class.isAssignableFrom(authentication);
    }

    @Override
    Authentication login(HttpServletRequest request) throws IOException {
        // 判断验证码
        String code = request.getParameter(OAuth2ParameterNames.CODE);
        log.info("验证码：{}",code);
        if (StringUtil.isEmpty(code)) {
            CustomAuthExceptionHandler.throwError(StatusCode.CAPTCHA_NOT_NULL, MessageUtil.getMessage(StatusCode.CAPTCHA_NOT_NULL));
        }
        String mail = request.getParameter(AuthConstant.MAIL);
        log.info("邮箱：{}",mail);
        if (StringUtil.isEmpty(mail)) {
            CustomAuthExceptionHandler.throwError(StatusCode.MAIL_NOT_NULL, MessageUtil.getMessage(StatusCode.MAIL_NOT_NULL));
        }
        boolean isMail = RegexUtil.mailRegex(mail);
        if (!isMail) {
            CustomAuthExceptionHandler.throwError(StatusCode.MAIL_ERROR, MessageUtil.getMessage(StatusCode.MAIL_ERROR));
        }
        // 获取用户信息,并认证信息
        return super.getUserInfo(mail, "", request,code,mail);
    }

    @Override
    AuthorizationGrantType getGrantType() {
        return new AuthorizationGrantType(GRANT_TYPE);
    }
}
