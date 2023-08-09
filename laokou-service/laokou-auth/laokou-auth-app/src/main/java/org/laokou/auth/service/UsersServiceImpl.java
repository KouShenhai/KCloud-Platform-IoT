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
import org.laokou.auth.domain.gateway.DeptGateway;
import org.laokou.auth.domain.gateway.MenuGateway;
import org.laokou.auth.domain.gateway.UserGateway;
import org.laokou.auth.domain.user.User;
import org.laokou.auth.event.handler.LoginHandler;
import org.laokou.common.core.enums.ResultStatusEnum;
import org.laokou.common.core.utils.CollectionUtil;
import org.laokou.common.core.utils.DateUtil;
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
import static org.laokou.auth.common.Constant.*;
import static org.laokou.auth.common.exception.ErrorCode.*;

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

	private final LoginHandler loginHandler;

	@Override
	public UserDetails loadUserByUsername(String loginName) throws UsernameNotFoundException {
		// 默认租户查询
		Long tenantId = DEFAULT_TENANT;
		String encryptName = AesUtil.encrypt(loginName);
		String loginType = AuthorizationGrantType.AUTHORIZATION_CODE.getValue();
		User user = userGateway.getUserByUsername(encryptName, tenantId, AUTH_PASSWORD);
		HttpServletRequest request = RequestUtil.getHttpServletRequest();
		if (user == null) {
			throw getException(USERNAME_PASSWORD_ERROR, loginName, loginType, request, tenantId);
		}
		String password = request.getParameter(OAuth2ParameterNames.PASSWORD);
		String clientPassword = user.getPassword();
		if (!passwordEncoder.matches(password, clientPassword)) {
			throw getException(USERNAME_PASSWORD_ERROR, loginName, loginType, request, tenantId);
		}
		// 是否锁定
		if (!user.isEnabled()) {
			throw getException(USERNAME_DISABLE, loginName, loginType, request, tenantId);
		}
		// 用户ID
		Long userId = user.getId();
		Integer superAdmin = user.getSuperAdmin();
		// 权限标识列表
		List<String> permissionsList = menuGateway.getPermissions(userId, tenantId, superAdmin);
		if (CollectionUtil.isEmpty(permissionsList)) {
			throw getException(USERNAME_NOT_PERMISSION, loginName, loginType, request, tenantId);
		}
		List<Long> deptIds = deptGateway.getDeptIds(userId, tenantId, superAdmin);
		user.setDeptIds(deptIds);
		user.setPermissionList(permissionsList);
		// 登录IP
		user.setLoginIp(IpUtil.getIpAddr(request));
		// 登录时间
		user.setLoginDate(DateUtil.now());
		// 默认数据库
		user.setSourceName(DEFAULT_SOURCE);
		// 登录成功
		loginHandler.handleEvent(loginName, loginType,
				ResultStatusEnum.SUCCESS.ordinal(), MessageUtil.getMessage(LOGIN_SUCCEEDED), request, tenantId);
		return user;
	}

	private UsernameNotFoundException getException(int code, String loginName, String loginType,
			HttpServletRequest request, Long tenantId) {
		String msg = MessageUtil.getMessage(code);
		log.error("登录失败，状态码：{}，错误信息：{}", code, msg);
		loginHandler.handleEvent(loginName, loginType, ResultStatusEnum.FAIL.ordinal(),
				msg, request, tenantId);
		throw new UsernameNotFoundException(msg);
	}

}
