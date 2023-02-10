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
import org.laokou.auth.client.exception.CustomAuthExceptionHandler;
import org.laokou.auth.server.domain.sys.repository.service.SysCaptchaService;
import org.laokou.auth.server.domain.sys.repository.service.SysDeptService;
import org.laokou.auth.server.domain.sys.repository.service.SysMenuService;
import org.laokou.auth.server.domain.sys.repository.service.SysUserService;
import org.laokou.common.core.utils.MessageUtil;
import org.laokou.common.core.utils.RegexUtil;
import org.laokou.common.core.utils.StringUtil;
import org.laokou.common.log.utils.LoginLogUtil;
import org.laokou.common.swagger.exception.CustomException;
import org.laokou.common.swagger.exception.ErrorCode;
import org.laokou.redis.utils.RedisUtil;
import org.laokou.tenant.service.SysSourceService;
import org.springframework.jdbc.core.JdbcTemplate;
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
public class OAuth2EmailAuthenticationProvider extends AbstractOAuth2BaseAuthenticationProvider {

    public static final String GRANT_TYPE = "email";

    public OAuth2EmailAuthenticationProvider(SysUserService sysUserService
            , SysMenuService sysMenuService
            , SysDeptService sysDeptService
            , LoginLogUtil loginLogUtil
            , PasswordEncoder passwordEncoder
            , SysCaptchaService sysCaptchaService
            , OAuth2AuthorizationService oAuth2AuthorizationService
            , OAuth2TokenGenerator<? extends OAuth2Token> tokenGenerator
            , SysSourceService sysSourceService
            , JdbcTemplate jdbcTemplate
            , RedisUtil redisUtil) {
        super(sysUserService, sysMenuService, sysDeptService, loginLogUtil, passwordEncoder,sysCaptchaService,oAuth2AuthorizationService,tokenGenerator, sysSourceService,jdbcTemplate,redisUtil);
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return OAuth2EmailAuthenticationToken.class.isAssignableFrom(authentication);
    }

    @Override
    Authentication login(HttpServletRequest request) throws IOException {
        // 判断验证码
        String code = request.getParameter(OAuth2ParameterNames.CODE);
        log.info("验证码：{}",code);
        if (StringUtil.isEmpty(code)) {
            CustomAuthExceptionHandler.throwError(ErrorCode.CAPTCHA_NOT_NULL, MessageUtil.getMessage(ErrorCode.CAPTCHA_NOT_NULL));
        }
        String email = request.getParameter(AuthConstant.EMAIL);
        log.info("邮箱：{}",email);
        if (StringUtil.isEmpty(email)) {
            throw new CustomException("邮箱不为空");
        }
        boolean isEmail = RegexUtil.emailRegex(email);
        if (!isEmail) {
            throw new CustomException("邮箱格式不对");
        }
        // TODO 验证验证码
        // 获取用户信息
        return super.getUserInfo(email, "", request);
    }

    @Override
    AuthorizationGrantType getGrantType() {
        return new AuthorizationGrantType(GRANT_TYPE);
    }
}
