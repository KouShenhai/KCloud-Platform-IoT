/*
 * Copyright (c) 2022 KCloud-Platform-Alibaba Author or Authors. All Rights Reserved.
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
import org.laokou.common.core.utils.CollectionUtil;
import org.laokou.common.core.utils.IpUtil;
import org.laokou.common.core.utils.RequestUtil;
import org.laokou.common.crypto.utils.AesUtil;
import org.laokou.common.i18n.utils.DateUtil;
import org.laokou.common.i18n.utils.MessageUtil;
import org.laokou.common.i18n.utils.ObjectUtil;
import org.laokou.common.security.utils.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.List;

import static com.baomidou.dynamic.datasource.enums.DdConstants.MASTER;
import static org.laokou.common.i18n.common.BizCodes.LOGIN_SUCCEEDED;
import static org.laokou.common.i18n.common.ErrorCodes.ACCOUNT_DISABLE;
import static org.laokou.common.i18n.common.ErrorCodes.ACCOUNT_PASSWORD_ERROR;
import static org.laokou.common.i18n.common.NumberConstants.FAIL;
import static org.laokou.common.i18n.common.NumberConstants.SUCCESS;
import static org.laokou.common.i18n.common.OAuth2Constants.PASSWORD;
import static org.laokou.common.i18n.common.StatusCodes.FORBIDDEN;
import static org.laokou.common.i18n.common.TenantConstants.DEFAULT;
import static org.springframework.security.oauth2.core.AuthorizationGrantType.AUTHORIZATION_CODE;

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
		Long tenantId = DEFAULT;
		String type = AUTHORIZATION_CODE.getValue();
		User user = userGateway.getUserByUsername(new Auth(username, type, AesUtil.getKey()));
		HttpServletRequest request = RequestUtil.getHttpServletRequest();
		String ip = IpUtil.getIpAddr(request);
		if (ObjectUtil.isNull(user)) {
			throw usernameNotFoundException(ACCOUNT_PASSWORD_ERROR, new User(username, tenantId), type, ip);
		}
		String password = request.getParameter(PASSWORD);
		String clientPassword = user.getPassword();
		if (!passwordEncoder.matches(password, clientPassword)) {
			throw usernameNotFoundException(ACCOUNT_PASSWORD_ERROR, user, type, ip);
		}
		// 是否锁定
		if (!user.isEnabled()) {
			throw usernameNotFoundException(ACCOUNT_DISABLE, user, type, ip);
		}
		// 权限标识列表
		List<String> permissionsList = menuGateway.getPermissions(user);
		if (CollectionUtil.isEmpty(permissionsList)) {
			throw usernameNotFoundException(FORBIDDEN, user, type, ip);
		}
		List<String> deptPaths = deptGateway.getDeptPaths(user);
		user.setDeptPaths(deptPaths);
		user.setPermissionList(permissionsList);
		// 登录IP
		user.setLoginIp(IpUtil.getIpAddr(request));
		// 登录时间
		user.setLoginDate(DateUtil.now());
		// 默认数据库
		user.setSourceName(MASTER);
		// 登录成功
		loginLogGateway.publish(new LoginLog(user.getId(), username, type, tenantId, SUCCESS,
				MessageUtil.getMessage(LOGIN_SUCCEEDED), ip, user.getDeptId(), user.getDeptPath()));
		return user;
	}

	private UsernameNotFoundException usernameNotFoundException(int code, User user, String type, String ip) {
		String message = MessageUtil.getMessage(code);
		log.error("登录失败，状态码：{}，错误信息：{}", code, message);
		loginLogGateway.publish(new LoginLog(user.getId(), user.getUsername(), type, user.getTenantId(), FAIL, message,
				ip, user.getDeptId(), user.getDeptPath()));
		throw new UsernameNotFoundException(message);
	}

}
