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
import lombok.SneakyThrows;
import org.apache.commons.collections.CollectionUtils;
import org.laokou.auth.client.constant.AuthConstant;
import org.laokou.auth.client.exception.CustomAuthExceptionHandler;
import org.laokou.auth.client.user.UserDetail;
import org.laokou.auth.server.domain.sys.repository.service.*;
import org.laokou.common.core.enums.ResultStatusEnum;
import org.laokou.common.core.utils.HttpContextUtil;
import org.laokou.common.i18n.core.StatusCode;
import org.laokou.common.i18n.utils.MessageUtil;
import org.laokou.common.log.utils.LoginLogUtil;
import org.laokou.redis.utils.RedisKeyUtil;
import org.laokou.redis.utils.RedisUtil;
import org.laokou.tenant.service.SysSourceService;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
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

import static org.laokou.auth.client.constant.AuthConstant.DEFAULT_SOURCE;
import static org.laokou.common.core.constant.Constant.DEFAULT;

/**
 * 邮件/手机/密码
 * @author laokou
 */
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
    protected final SysAuthenticationService sysAuthenticationService;
    public AbstractOAuth2BaseAuthenticationProvider(
            SysUserService sysUserService
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
        this.sysAuthenticationService = sysAuthenticationService;
    }


    @SneakyThrows
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        HttpServletRequest request = HttpContextUtil.getHttpServletRequest();
        Authentication usernamePasswordToken = login(request);
        return getToken(authentication,usernamePasswordToken,request);
    }

    /**
     * Token是否支持认证（provider）
     * @param authentication
     * @return
     */
    abstract public boolean supports(Class<?> authentication);

    /**
     * 登录
     * @param request
     * @return
     * @throws IOException
     */
    abstract Authentication login(HttpServletRequest request) throws IOException;

    /**
     * 认证类型
     * @return
     * @return
     */
    abstract AuthorizationGrantType getGrantType();

    /**
     * 仿照授权码模式
     * @param authentication
     * @param principal
     * @return
     */
    protected Authentication getToken(Authentication authentication,Authentication principal,HttpServletRequest request) throws IOException {
        // 生成token（access_token + refresh_token）
        AbstractOAuth2BaseAuthenticationToken abstractOAuth2BaseAuthenticationToken = (AbstractOAuth2BaseAuthenticationToken) authentication;
        OAuth2ClientAuthenticationToken clientPrincipal = getAuthenticatedClientElseThrowInvalidClient(abstractOAuth2BaseAuthenticationToken);
        RegisteredClient registeredClient = clientPrincipal.getRegisteredClient();
        // 获取认证范围
        Set<String> scopes = registeredClient.getScopes();
        String loginName = principal.getCredentials().toString();
        // 认证类型
        AuthorizationGrantType grantType = getGrantType();
        String loginType = grantType.getValue();
        // 获取上下文
        DefaultOAuth2TokenContext.Builder builder = DefaultOAuth2TokenContext.builder()
                .registeredClient(registeredClient)
                .principal(principal)
                .tokenType(OAuth2TokenType.ACCESS_TOKEN)
                .authorizedScopes(scopes)
                .authorizationServerContext(AuthorizationServerContextHolder.getContext())
                .authorizationGrantType(grantType);
        DefaultOAuth2TokenContext context = builder.build();
        OAuth2Authorization.Builder authorizationBuilder = OAuth2Authorization
                .withRegisteredClient(registeredClient)
                .principalName(loginName)
                .authorizedScopes(scopes)
                .authorizationGrantType(grantType);
        // 生成access_token
        OAuth2Token generatedOauth2AccessToken = Optional.ofNullable(tokenGenerator.generate(context)).orElseThrow(() -> CustomAuthExceptionHandler.getError(OAuth2ErrorCodes.SERVER_ERROR,"令牌生成器无法生成访问令牌",CustomAuthExceptionHandler.ERROR_URI));
        OAuth2AccessToken oAuth2AccessToken = new OAuth2AccessToken(
                OAuth2AccessToken.TokenType.BEARER
                , generatedOauth2AccessToken.getTokenValue()
                , generatedOauth2AccessToken.getIssuedAt()
                , generatedOauth2AccessToken.getExpiresAt()
                , context.getAuthorizedScopes());
        // jwt
        if (generatedOauth2AccessToken instanceof ClaimAccessor) {
            authorizationBuilder
                    .token(oAuth2AccessToken,
                            meta -> meta.put(OAuth2Authorization.Token.CLAIMS_METADATA_NAME
                                    ,((ClaimAccessor)generatedOauth2AccessToken).getClaims()))
                    .authorizedScopes(scopes)
                    // admin后台管理需要token，解析token获取用户信息，因此将用户信息存在数据库，下次直接查询数据库就可以获取用户信息
                    .attribute(Principal.class.getName(), principal);
        }else {
            authorizationBuilder.accessToken(oAuth2AccessToken);
        }
        // 生成refresh_token
        context = builder
                .authorizationGrantType(AuthorizationGrantType.REFRESH_TOKEN)
                .tokenType(OAuth2TokenType.REFRESH_TOKEN)
                .build();
        OAuth2Token generateOauth2RefreshToken = Optional.ofNullable(tokenGenerator.generate(context)).orElseThrow(() ->
                CustomAuthExceptionHandler.getError(OAuth2ErrorCodes.SERVER_ERROR,"令牌生成器无法生成刷新令牌",CustomAuthExceptionHandler.ERROR_URI));
        OAuth2RefreshToken oAuth2RefreshToken = (OAuth2RefreshToken) generateOauth2RefreshToken;
        authorizationBuilder.refreshToken(oAuth2RefreshToken);
        OAuth2Authorization oAuth2Authorization = authorizationBuilder.build();
        authorizationService.save(oAuth2Authorization);
        // 登录成功
        Long tenantId = Long.valueOf(request.getParameter(AuthConstant.TENANT_ID));
        loginLogUtil.recordLogin(loginName,loginType, ResultStatusEnum.SUCCESS.ordinal(), AuthConstant.LOGIN_SUCCESS_MSG,request,tenantId);
        // 账号是否已经登录并且未过期，是则强制踢出，反之不操作
        accountKill(oAuth2AccessToken.getTokenValue(),loginName);
        return new OAuth2AccessTokenAuthenticationToken(
                registeredClient, clientPrincipal, oAuth2AccessToken, oAuth2RefreshToken, Collections.emptyMap());
    }

    protected UsernamePasswordAuthenticationToken getUserInfo(String loginName, String password, HttpServletRequest request,String captcha,String uuid) throws IOException {
        AuthorizationGrantType grantType = getGrantType();
        String loginType = grantType.getValue();
        Long tenantId = Long.valueOf(request.getParameter(AuthConstant.TENANT_ID));
        // 验证验证码
        Boolean validate = sysCaptchaService.validate(uuid, captcha);
        if (!validate) {
            loginLogUtil.recordLogin(loginName,loginType, ResultStatusEnum.FAIL.ordinal(), MessageUtil.getMessage(StatusCode.CAPTCHA_ERROR),request,tenantId);
            CustomAuthExceptionHandler.throwError(StatusCode.CAPTCHA_ERROR, MessageUtil.getMessage(StatusCode.CAPTCHA_ERROR));
        }
        // 多租户查询
        UserDetail userDetail = sysUserService.getUserDetail(loginName,tenantId,loginType);
        if (userDetail == null) {
            loginLogUtil.recordLogin(loginName,loginType, ResultStatusEnum.FAIL.ordinal(), MessageUtil.getMessage(StatusCode.USERNAME_PASSWORD_ERROR),request,tenantId);
            CustomAuthExceptionHandler.throwError(StatusCode.USERNAME_PASSWORD_ERROR, MessageUtil.getMessage(StatusCode.USERNAME_PASSWORD_ERROR));
        }
        if (OAuth2PasswordAuthenticationProvider.GRANT_TYPE.equals(loginType)) {
            // 验证密码
            String clientPassword = userDetail.getPassword();
            if (!passwordEncoder.matches(password, clientPassword)) {
                loginLogUtil.recordLogin(loginName, loginType, ResultStatusEnum.FAIL.ordinal(), MessageUtil.getMessage(StatusCode.USERNAME_PASSWORD_ERROR), request,tenantId);
                CustomAuthExceptionHandler.throwError(StatusCode.USERNAME_PASSWORD_ERROR, MessageUtil.getMessage(StatusCode.USERNAME_PASSWORD_ERROR));
            }
        }
        // 是否锁定
        if (!userDetail.isEnabled()) {
            loginLogUtil.recordLogin(loginName,loginType, ResultStatusEnum.FAIL.ordinal(), MessageUtil.getMessage(StatusCode.USERNAME_DISABLE),request,tenantId);
            CustomAuthExceptionHandler.throwError(StatusCode.USERNAME_DISABLE, MessageUtil.getMessage(StatusCode.USERNAME_DISABLE));
        }
        Long userId = userDetail.getUserId();
        Integer superAdmin = userDetail.getSuperAdmin();
        // 权限标识列表
        List<String> permissionsList = sysMenuService.getPermissionsList(tenantId,superAdmin,userId);
        if (CollectionUtils.isEmpty(permissionsList)) {
            loginLogUtil.recordLogin(loginName,loginType, ResultStatusEnum.FAIL.ordinal(), MessageUtil.getMessage(StatusCode.USERNAME_NOT_PERMISSION),request,tenantId);
            CustomAuthExceptionHandler.throwError(StatusCode.USERNAME_NOT_PERMISSION, MessageUtil.getMessage(StatusCode.USERNAME_NOT_PERMISSION));
        }
        // 部门列表
        List<Long> deptIds = sysDeptService.getDeptIds(superAdmin,userId);
        userDetail.setDeptIds(deptIds);
        userDetail.setPermissionList(permissionsList);
        if (tenantId == DEFAULT) {
            // 默认数据源
            userDetail.setSourceName(DEFAULT_SOURCE);
        } else {
            // 租户数据源
            String sourceName = sysSourceService.querySourceName(tenantId);
            userDetail.setSourceName(sourceName);
        }
        return new UsernamePasswordAuthenticationToken(userDetail,loginName,userDetail.getAuthorities());
    }

    private OAuth2ClientAuthenticationToken getAuthenticatedClientElseThrowInvalidClient(Authentication authentication) {
        OAuth2ClientAuthenticationToken clientPrincipal = null;
        if (OAuth2ClientAuthenticationToken.class.isAssignableFrom(authentication.getPrincipal().getClass())) {
            clientPrincipal = (OAuth2ClientAuthenticationToken) authentication.getPrincipal();
        }
        if (clientPrincipal != null && clientPrincipal.isAuthenticated()) {
            return clientPrincipal;
        }
        throw new OAuth2AuthenticationException(OAuth2ErrorCodes.INVALID_CLIENT);
    }

    /**
     * 账号踢出
     * @param accessToken
     * @param loginName
     */
    private void accountKill(String accessToken,String loginName) {
        List<String> accessTokenList = sysAuthenticationService.getAccessTokenList(loginName, accessToken);
        if (CollectionUtils.isNotEmpty(accessTokenList)) {
            accessTokenList.forEach(item -> {
                String accountKillKey = RedisKeyUtil.getAccountKillKey(item);
                redisUtil.set(accountKillKey,DEFAULT,RedisUtil.HOUR_ONE_EXPIRE);
            });
        }
    }

}
