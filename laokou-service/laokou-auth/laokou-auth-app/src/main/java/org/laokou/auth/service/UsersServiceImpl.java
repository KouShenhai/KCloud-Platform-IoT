/*
 * Copyright (c) 2022 KCloud-Platform-Alibaba Authors. All Rights Reserved.
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
 *
 */

package org.laokou.auth.service;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.laokou.auth.domain.auth.Auth;
import org.laokou.auth.domain.gateway.DeptGateway;
import org.laokou.auth.domain.gateway.LoginLogGateway;
import org.laokou.auth.domain.gateway.MenuGateway;
import org.laokou.auth.domain.gateway.UserGateway;
import org.laokou.auth.domain.log.LoginLog;
import org.laokou.auth.domain.user.User;
import org.laokou.common.core.utils.CollectionUtil;
import org.laokou.common.i18n.utils.DateUtil;
import org.laokou.common.core.utils.IpUtil;
import org.laokou.common.core.utils.RequestUtil;
import org.laokou.common.i18n.utils.MessageUtil;
import org.laokou.common.jasypt.utils.AesUtil;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames;
import org.springframework.stereotype.Component;

import java.util.List;

import static org.laokou.auth.common.BizCode.LOGIN_SUCCEEDED;
import static org.laokou.auth.common.Constant.DEFAULT_SOURCE;
import static org.laokou.auth.common.Constant.DEFAULT_TENANT;
import static org.laokou.auth.common.exception.ErrorCode.*;
import static org.laokou.common.i18n.common.Constant.FAIL_STATUS;
import static org.laokou.common.i18n.common.Constant.SUCCESS_STATUS;

/**
 * @author laokou
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class UsersServiceImpl implements UserDetailsService {

	private final PasswordEncoder passwordEncoder;

	private final UserGateway userGateway;

	private final DeptGateway deptGateway;

	private final MenuGateway menuGateway;

	private final LoginLogGateway loginLogGateway;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		// 默认租户查询
		Long tenantId = DEFAULT_TENANT;
		String encryptName = AesUtil.encrypt(username);
		String type = AuthorizationGrantType.AUTHORIZATION_CODE.getValue();
		User user = userGateway.getUserByUsername(new Auth(encryptName, tenantId, type));
		HttpServletRequest request = RequestUtil.getHttpServletRequest();
		String ip = IpUtil.getIpAddr(request);
		if (user == null) {
			throw getException(USERNAME_PASSWORD_ERROR, username, type, tenantId, ip, null);
		}
		String password = request.getParameter(OAuth2ParameterNames.PASSWORD);
		String clientPassword = user.getPassword();
		if (!passwordEncoder.matches(password, clientPassword)) {
			throw getException(USERNAME_PASSWORD_ERROR, username, type, tenantId, ip, user);
		}
		// 是否锁定
		if (!user.isEnabled()) {
			throw getException(USERNAME_DISABLE, username, type, tenantId, ip, user);
		}
		// 用户ID
		Long userId = user.getId();
		Integer superAdmin = user.getSuperAdmin();
		// 权限标识列表
		User u = new User(userId, superAdmin, tenantId);
		List<String> permissionsList = menuGateway.getPermissions(u);
		if (CollectionUtil.isEmpty(permissionsList)) {
			throw getException(USERNAME_NOT_PERMISSION, username, type, tenantId, ip, user);
		}
		List<String> deptPaths = deptGateway.getDeptPaths(u);
		user.setDeptPaths(deptPaths);
		user.setPermissionList(permissionsList);
		// 登录IP
		user.setLoginIp(IpUtil.getIpAddr(request));
		// 登录时间
		user.setLoginDate(DateUtil.now());
		// 默认数据库
		user.setSourceName(DEFAULT_SOURCE);
		// 登录成功
		loginLogGateway.publish(new LoginLog(userId, username, type, tenantId, SUCCESS_STATUS,
				MessageUtil.getMessage(LOGIN_SUCCEEDED), ip, user.getDeptId(), user.getDeptPath()));
		return user;
	}

	private UsernameNotFoundException getException(int code, String username, String type, Long tenantId, String ip,
			User user) {
		String message = MessageUtil.getMessage(code);
		log.error("登录失败，状态码：{}，错误信息：{}", code, message);
		Long userId = null;
		Long deptId = null;
		String deptPath = null;
		if (user != null) {
			userId = user.getId();
			deptId = user.getDeptId();
			deptPath = user.getDeptPath();
		}
		loginLogGateway
			.publish(new LoginLog(userId, username, type, tenantId, FAIL_STATUS, message, ip, deptId, deptPath));
		throw new UsernameNotFoundException(message);
	}

}
