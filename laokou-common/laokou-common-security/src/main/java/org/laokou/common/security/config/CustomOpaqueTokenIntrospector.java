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
package org.laokou.common.security.config;
import lombok.RequiredArgsConstructor;
import org.laokou.auth.client.exception.CustomAuthExceptionHandler;
import org.laokou.auth.client.user.UserDetail;
import org.laokou.common.i18n.core.StatusCode;
import org.laokou.common.i18n.utils.MessageUtil;
import org.laokou.redis.utils.RedisKeyUtil;
import org.laokou.redis.utils.RedisUtil;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.oauth2.core.OAuth2AuthenticatedPrincipal;
import org.springframework.security.oauth2.server.authorization.OAuth2Authorization;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationService;
import org.springframework.security.oauth2.server.authorization.OAuth2TokenType;
import org.springframework.security.oauth2.server.resource.introspection.OpaqueTokenIntrospector;
import org.springframework.stereotype.Component;
import java.security.Principal;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
/**
 * @author laokou
 */
@Component
@RequiredArgsConstructor
public class CustomOpaqueTokenIntrospector implements OpaqueTokenIntrospector {

    private final OAuth2AuthorizationService oAuth2AuthorizationService;
    private final RedisUtil redisUtil;

    @Override
    public OAuth2AuthenticatedPrincipal introspect(String token) {
        // 账号是否被强制踢出
        String accountKillKey = RedisKeyUtil.getAccountKillKey(token);
        Object value = redisUtil.get(accountKillKey);
        if (value != null) {
            CustomAuthExceptionHandler.throwError(401,"您的账号已在别处登录，请重新登录");
        }
        String userInfoKey = RedisKeyUtil.getUserInfoKey(token);
        Object obj = redisUtil.get(userInfoKey);
        if (obj != null) {
            return (UserDetail) obj;
        }
        OAuth2Authorization oAuth2Authorization = oAuth2AuthorizationService.findByToken(token, OAuth2TokenType.ACCESS_TOKEN);
        if (oAuth2Authorization == null) {
            CustomAuthExceptionHandler.throwError(StatusCode.UNAUTHORIZED, MessageUtil.getMessage(StatusCode.UNAUTHORIZED));
        }
        if (!oAuth2Authorization.getAccessToken().isActive()) {
            CustomAuthExceptionHandler.throwError(StatusCode.UNAUTHORIZED,MessageUtil.getMessage(StatusCode.UNAUTHORIZED));
        }
        Instant expiresAt = oAuth2Authorization.getAccessToken().getToken().getExpiresAt();
        Instant nowAt = Instant.now();
        long expireTime = ChronoUnit.SECONDS.between(nowAt, expiresAt);
        UserDetail userDetail = (UserDetail) ((UsernamePasswordAuthenticationToken) oAuth2Authorization.getAttribute(Principal.class.getName())).getPrincipal();
        redisUtil.set(userInfoKey,userDetail,expireTime);
        return userDetail;
    }
}
