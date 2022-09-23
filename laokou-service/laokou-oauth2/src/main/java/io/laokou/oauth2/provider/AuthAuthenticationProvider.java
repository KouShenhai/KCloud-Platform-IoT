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
package io.laokou.oauth2.provider;
import com.google.common.collect.Lists;
import org.laokou.common.enums.UserStatusEnum;
import org.laokou.common.exception.ErrorCode;
import org.laokou.common.password.PasswordUtil;
import org.laokou.common.user.UserDetail;
import org.laokou.common.utils.MessageUtil;
import io.laokou.oauth2.service.SysUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
/**
 * @author Kou Shenhai
 * @version 1.0
 * @date 2021/4/16 0016 上午 9:45
 */
@Component
@Slf4j
public class AuthAuthenticationProvider implements AuthenticationProvider {

    @Autowired
    private SysUserService sysUserService;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String username = authentication.getName();
        String password = (String)authentication.getCredentials();
        log.info("username：{}",username);
        log.info("password：{}",password);
        //查询数据库
        UserDetail userDetail = sysUserService.getUserDetail(null, username);
        log.info("查询的数据：{}",userDetail);
        //用户实体对象
        if (null == userDetail){
            throw new BadCredentialsException(MessageUtil.getMessage(ErrorCode.ACCOUNT_PASSWORD_ERROR));
        }
        if(!PasswordUtil.matches(password, userDetail.getPassword())){
            throw new BadCredentialsException(MessageUtil.getMessage(ErrorCode.ACCOUNT_PASSWORD_ERROR));
        }
        if (UserStatusEnum.DISABLE.ordinal() == userDetail.getStatus()) {
            throw new BadCredentialsException(MessageUtil.getMessage(ErrorCode.ACCOUNT_DISABLE));
        }
        UserDetails userDetails = new User(username,password, Lists.newArrayList());
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(userDetails,authentication.getCredentials(),userDetails.getAuthorities());
        authenticationToken.setDetails(authentication.getDetails());
        return authenticationToken;
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return true;
    }
}
