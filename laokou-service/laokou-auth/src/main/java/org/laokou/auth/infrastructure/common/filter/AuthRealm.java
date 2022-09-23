/**
 * Copyright (c) 2022 KCloud-Platform Authors. All Rights Reserved.
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
 */
package org.laokou.auth.infrastructure.common.filter;
import org.laokou.auth.application.service.SysAuthApplicationService;
import org.laokou.common.exception.ErrorCode;
import org.laokou.common.user.UserDetail;
import org.laokou.common.utils.JacksonUtil;
import org.laokou.common.utils.MessageUtil;
import org.laokou.common.utils.TokenUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.util.HashSet;
import java.util.Set;

/**
 * 认证授权
 * @author Kou Shenhai
 */
@Component
@Slf4j
public class AuthRealm extends AuthorizingRealm {

    @Autowired
    private SysAuthApplicationService sysAuthApplicationService;

    /**
     * 授权(验证权限时调用)
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        UserDetail user = (UserDetail) principalCollection.getPrimaryPrincipal();
        //用户权限列表
        Set<String> permsSet = new HashSet<>(user.getPermissionsList());
        log.info("获取权限标识:{}", JacksonUtil.toJsonStr(permsSet));
        SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
        info.setStringPermissions(permsSet);
        return info;
    }

    @Override
    public boolean supports(AuthenticationToken token) {
        return token instanceof AuthToken;
    }

    /**
     * addStringPermissions
     * 认证(登录时调用)
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        String accessToken = (String) authenticationToken.getPrincipal();
        //token失效
        boolean expiration = TokenUtil.isExpiration(accessToken);
        if (expiration) {
            throw new IncorrectCredentialsException(MessageUtil.getMessage(ErrorCode.AUTHORIZATION_INVALID));
        }
        //查询用户信息
        Long userId = TokenUtil.getUserId(accessToken);
        UserDetail userDetail = sysAuthApplicationService.getUserDetail(userId);
        SimpleAuthenticationInfo info = new SimpleAuthenticationInfo(userDetail, accessToken, getName());
        return info;
    }
}
