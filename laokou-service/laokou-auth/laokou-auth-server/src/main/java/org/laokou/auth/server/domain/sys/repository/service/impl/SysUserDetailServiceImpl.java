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
package org.laokou.auth.server.domain.sys.repository.service.impl;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections.CollectionUtils;
import org.laokou.auth.client.user.UserDetail;
import org.laokou.auth.server.domain.sys.repository.service.SysDeptService;
import org.laokou.auth.server.domain.sys.repository.service.SysMenuService;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import java.util.List;
/**
 * @author laokou
 */
@RequiredArgsConstructor
public class SysUserDetailServiceImpl implements UserDetailsService {

    private final SysUserServiceImpl sysUserService;
    private final SysMenuService sysMenuService;
    private final SysDeptService sysDeptService;

    @Override
    public UserDetails loadUserByUsername(String loginName) throws UsernameNotFoundException {
        // 多租户查询
        UserDetail userDetail = sysUserService.getUserDetail(loginName,0L);
        if (userDetail == null) {
            throw new BadCredentialsException("The account number or password is incorrect");
        }
        // 是否锁定
        if (!userDetail.isEnabled()) {
            throw new BadCredentialsException("Account has been deactivated");
        }
        Long userId = userDetail.getUserId();
        Integer superAdmin = userDetail.getSuperAdmin();
        // 权限标识列表
        List<String> permissionsList = sysMenuService.getPermissionsList(superAdmin,userId);
        if (CollectionUtils.isEmpty(permissionsList)) {
            throw new BadCredentialsException("You do not have permission to access, please contact the administrator");
        }
        userDetail.setPermissionList(permissionsList);
        userDetail.setDeptIds(sysDeptService.getDeptIds(superAdmin,userId));
        return userDetail;
    }
}
