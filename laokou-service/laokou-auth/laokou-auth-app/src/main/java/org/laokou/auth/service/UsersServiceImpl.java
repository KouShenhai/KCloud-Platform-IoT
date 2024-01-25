/*
 * Copyright (c) 2022-2024 KCloud-Platform-Alibaba Author or Authors. All Rights Reserved.
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
import org.laokou.auth.domain.gateway.LoginLogGateway;
import org.laokou.auth.domain.gateway.MenuGateway;
import org.laokou.auth.domain.gateway.UserGateway;
import org.laokou.auth.domain.log.LoginLog;
import org.laokou.auth.domain.user.Auth;
import org.laokou.auth.domain.user.User;
import org.laokou.common.core.utils.CollectionUtil;
import org.laokou.common.core.utils.IpUtil;
import org.laokou.common.core.utils.RequestUtil;
import org.laokou.common.crypto.utils.AesUtil;
import org.laokou.common.i18n.utils.DateUtil;
import org.laokou.common.i18n.utils.MessageUtil;
import org.laokou.common.i18n.utils.ObjectUtil;
import org.laokou.common.security.utils.UserDetail;
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
 * 用户认证.
 *
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

	/**
	 * 获取用户信息.
	 * @param username 用户名
	 * @return 用户信息
	 * @throws UsernameNotFoundException 异常
	 */
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		// 默认租户查询
		HttpServletRequest request = RequestUtil.getHttpServletRequest();
		String ip = IpUtil.getIpAddr(request);
		String authType = AUTHORIZATION_CODE.getValue();
		String publicKey = AesUtil.getKey();
		Auth auth = Auth.builder().type(authType).publicKey(publicKey).build();
		User user = User.builder().username(username).tenantId(DEFAULT).auth(auth).build();
		User u = userGateway.findOne(user);
		if (ObjectUtil.isNull(u)) {
			throw usernameNotFoundException(ACCOUNT_PASSWORD_ERROR, UserDetail.copy(user), authType, ip);
		}
		UserDetail userDetail = UserDetail.copy(u);
		String password = request.getParameter(PASSWORD);
		String clientPassword = userDetail.getPassword();
		if (!passwordEncoder.matches(password, clientPassword)) {
			throw usernameNotFoundException(ACCOUNT_PASSWORD_ERROR, userDetail, authType, ip);
		}
		// 是否锁定
		if (!userDetail.isEnabled()) {
			throw usernameNotFoundException(ACCOUNT_DISABLE, userDetail, authType, ip);
		}
		// 权限标识列表
		List<String> permissionsList = menuGateway.getPermissions(userDetail);
		if (CollectionUtil.isEmpty(permissionsList)) {
			throw usernameNotFoundException(FORBIDDEN, userDetail, authType, ip);
		}
		List<String> deptPaths = deptGateway.getDeptPaths(userDetail);
		userDetail.setDeptPaths(deptPaths);
		userDetail.setPermissionList(permissionsList);
		// 登录IP
		userDetail.setLoginIp(IpUtil.getIpAddr(request));
		// 登录时间
		userDetail.setLoginDate(DateUtil.now());
		// 默认数据库
		userDetail.setSourceName(MASTER);
		// 登录成功
		loginLogGateway.publish(new LoginLog(userDetail.getId(), username, type, tenantId, SUCCESS,
				MessageUtil.getMessage(LOGIN_SUCCEEDED), ip, userDetail.getDeptId(), userDetail.getDeptPath()));
		return userDetail;
	}

	private UsernameNotFoundException usernameNotFoundException(int code, UserDetail userDetail, String authType,
			String ip) {
		String message = MessageUtil.getMessage(code);
		log.error("登录失败，状态码：{}，错误信息：{}", code, message);
		loginLogGateway.publish(new LoginLog(userDetail.getId(), userDetail.getUsername(), authType,
				userDetail.getTenantId(), FAIL, message, ip, userDetail.getDeptId(), userDetail.getDeptPath()));
		throw new UsernameNotFoundException(message);
	}

}
