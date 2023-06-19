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

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.laokou.auth.client.user.UserDetail;
import org.laokou.auth.server.domain.sys.repository.service.SysDeptService;
import org.laokou.auth.server.domain.sys.repository.service.SysMenuService;
import org.laokou.auth.server.domain.sys.repository.service.SysUserService;
import org.laokou.auth.server.infrastructure.authentication.OAuth2PasswordAuthenticationProvider;
import org.laokou.common.core.utils.CollectionUtil;
import org.laokou.common.core.utils.DateUtil;
import org.laokou.common.core.utils.RequestUtil;
import org.laokou.common.core.utils.IpUtil;
import org.laokou.common.i18n.core.StatusCode;
import org.laokou.common.i18n.utils.MessageUtil;
import org.laokou.common.jasypt.utils.AesUtil;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames;
import org.springframework.stereotype.Service;
import java.util.List;
import static org.laokou.common.core.constant.Constant.DEFAULT_SOURCE;

/**
 * @author laokou
 */
@Service
@RequiredArgsConstructor
public class SysUserDetailServiceImpl implements UserDetailsService {

	private final SysUserService sysUserService;

	private final SysMenuService sysMenuService;

	private final SysDeptService sysDeptService;

	private final PasswordEncoder passwordEncoder;

	@Override
	public UserDetails loadUserByUsername(String loginName) throws UsernameNotFoundException {
		// 默认租户查询
		String encryptName = AesUtil.encrypt(loginName);
		UserDetail userDetail = sysUserService.getUserDetail(encryptName, 0L,
				OAuth2PasswordAuthenticationProvider.GRANT_TYPE);
		HttpServletRequest request = RequestUtil.getHttpServletRequest();
		if (userDetail == null) {
			throw new UsernameNotFoundException(MessageUtil.getMessage(StatusCode.USERNAME_PASSWORD_ERROR));
		}
		String password = request.getParameter(OAuth2ParameterNames.PASSWORD);
		String clientPassword = userDetail.getPassword();
		if (!passwordEncoder.matches(password, clientPassword)) {
			throw new UsernameNotFoundException(MessageUtil.getMessage(StatusCode.USERNAME_PASSWORD_ERROR));
		}
		// 是否锁定
		if (!userDetail.isEnabled()) {
			throw new UsernameNotFoundException(MessageUtil.getMessage(StatusCode.USERNAME_DISABLE));
		}
		Long userId = userDetail.getId();
		Integer superAdmin = userDetail.getSuperAdmin();
		// 权限标识列表
		List<String> permissionsList = sysMenuService.getPermissionsList(0L, superAdmin, userId);
		if (CollectionUtil.isEmpty(permissionsList)) {
			throw new UsernameNotFoundException(MessageUtil.getMessage(StatusCode.USERNAME_NOT_PERMISSION));
		}
		List<Long> deptIds = sysDeptService.getDeptIds(superAdmin, userId, 0L);
		userDetail.setDeptIds(deptIds);
		userDetail.setPermissionList(permissionsList);
		// 登录IP
		userDetail.setLoginIp(IpUtil.getIpAddr(request));
		// 登录时间
		userDetail.setLoginDate(DateUtil.now());
		userDetail.setSourceName(DEFAULT_SOURCE);
		return userDetail;
	}

}
