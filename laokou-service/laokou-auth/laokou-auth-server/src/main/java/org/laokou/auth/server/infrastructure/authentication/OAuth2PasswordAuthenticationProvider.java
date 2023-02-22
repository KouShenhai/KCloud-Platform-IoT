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
package org.laokou.auth.server.infrastructure.authentication;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.laokou.auth.client.constant.AuthConstant;
import org.laokou.auth.client.exception.CustomAuthExceptionHandler;
import org.laokou.auth.server.domain.sys.repository.service.*;
import org.laokou.common.core.enums.ResultStatusEnum;
import org.laokou.common.core.utils.StringUtil;
import org.laokou.common.i18n.core.StatusCode;
import org.laokou.common.i18n.utils.MessageUtil;
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
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * 密码模式
 * @author laokou
 */
@Component
@Slf4j
public class OAuth2PasswordAuthenticationProvider extends AbstractOAuth2BaseAuthenticationProvider {

    public static final String GRANT_TYPE = "password";

    public OAuth2PasswordAuthenticationProvider(SysUserService sysUserService
            , SysMenuService sysMenuService
            , SysDeptService sysDeptService
            , LoginLogUtil loginLogUtil
            , PasswordEncoder passwordEncoder
            , SysCaptchaService sysCaptchaService
            , OAuth2AuthorizationService authorizationService
            , OAuth2TokenGenerator<? extends OAuth2Token> tokenGenerator
            , SysSourceService sysSourceService
            , SysAuthenticationService sysAuthenticationService
            , RedisUtil redisUtil) {
        super(sysUserService, sysMenuService, sysDeptService, loginLogUtil, passwordEncoder,sysCaptchaService,authorizationService,tokenGenerator, sysSourceService,sysAuthenticationService,redisUtil);
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return OAuth2PasswordAuthenticationToken.class.isAssignableFrom(authentication);
    }

    @Override
    Authentication login(HttpServletRequest request) throws IOException {
        // 判断唯一标识是否为空
        String uuid = request.getParameter(AuthConstant.UUID);
        log.info("唯一标识：{}",uuid);
        if (StringUtil.isEmpty(uuid)) {
            CustomAuthExceptionHandler.throwError(StatusCode.IDENTIFIER_NOT_NULL, MessageUtil.getMessage(StatusCode.IDENTIFIER_NOT_NULL));
        }
        // 判断验证码是否为空
        String captcha = request.getParameter(AuthConstant.CAPTCHA);
        log.info("验证码：{}",captcha);
        if (StringUtil.isEmpty(captcha)) {
            CustomAuthExceptionHandler.throwError(StatusCode.CAPTCHA_NOT_NULL, MessageUtil.getMessage(StatusCode.CAPTCHA_NOT_NULL));
        }
        // 验证账号是否为空
        String username = request.getParameter(OAuth2ParameterNames.USERNAME);
        log.info("账号：{}",username);
        if (StringUtil.isEmpty(username)) {
            CustomAuthExceptionHandler.throwError(StatusCode.USERNAME_NOT_NULL, MessageUtil.getMessage(StatusCode.USERNAME_NOT_NULL));
        }
        // 验证密码是否为空
        String password = request.getParameter(OAuth2ParameterNames.PASSWORD);
        log.info("密码：{}",password);
        if (StringUtil.isEmpty(password)) {
            CustomAuthExceptionHandler.throwError(StatusCode.PASSWORD_NOT_NULL, MessageUtil.getMessage(StatusCode.PASSWORD_NOT_NULL));
        }
        // 获取用户信息,并认证信息
        return super.getUserInfo(username, password, request,captcha,uuid);
    }

    @Override
    AuthorizationGrantType getGrantType() {
        return new AuthorizationGrantType(GRANT_TYPE);
    }
}
