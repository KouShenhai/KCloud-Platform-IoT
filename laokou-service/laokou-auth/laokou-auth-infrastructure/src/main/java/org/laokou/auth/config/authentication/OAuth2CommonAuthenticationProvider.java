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

package org.laokou.auth.config.authentication;

import com.baomidou.dynamic.datasource.toolkit.DynamicDataSourceContextHolder;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.laokou.auth.common.exception.handler.OAuth2ExceptionHandler;
import org.laokou.auth.domain.gateway.*;
import org.laokou.auth.domain.user.Captcha;
import org.laokou.auth.domain.user.User;
import org.laokou.common.core.utils.IpUtil;
import org.laokou.common.domain.service.DomainEventService;
import org.laokou.common.i18n.common.exception.GlobalException;
import org.laokou.common.i18n.utils.DateUtil;
import org.laokou.common.i18n.utils.ObjectUtil;
import org.laokou.common.security.utils.UserDetail;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Set;

import static com.baomidou.dynamic.datasource.enums.DdConstants.MASTER;
import static org.laokou.common.i18n.common.NumberConstants.DEFAULT;

/**
 * @author laokou
 */
@Component
@RequiredArgsConstructor
public class OAuth2CommonAuthenticationProvider {

	private final UserGateway userGateway;

	private final MenuGateway menuGateway;

	private final DeptGateway deptGateway;

	private final PasswordEncoder passwordEncoder;

	private final CaptchaGateway captchaGateway;

	private final SourceGateway sourceGateway;

	private final DomainEventService domainEventService;

	public UsernamePasswordAuthenticationToken authenticationToken(User user, HttpServletRequest request) {
		try {
			Captcha captchaObj = user.getCaptcha();
			String ip = IpUtil.getIpAddr(request);
			Long tenantId = user.getTenantId();
			String clientPassword = user.getPassword();
			// 检查验证码
			checkCaptcha(user, captchaObj, request);
			// 数据源名称
			String sourceName = getSourceName(tenantId);
			User u = userGateway.findOne(user);
			// 检查空对象
			user = user.copy(u, request);
			// 检查密码
			user.checkPassword(clientPassword, passwordEncoder, request);
			// 检查状态
			user.checkStatus(request);
			Set<String> permissions = menuGateway.findPermissions(user);
			// 检查权限标识集合
			user.checkNullPermissions(permissions, request);
			Set<String> deptPaths = deptGateway.findDeptPaths(user);
			UserDetail userDetail = UserDetail.copy(user);
			// 部门PATH集合
			userDetail.setDeptPaths(deptPaths);
			// 权限标识集合
			userDetail.setPermissions(permissions);
			// 数据源名称
			userDetail.setSourceName(sourceName);
			// 登录IP
			userDetail.setLoginIp(ip);
			// 登录时间
			userDetail.setLoginDate(DateUtil.now());
			// 登录成功
			user.loginSuccess(request);
			return new UsernamePasswordAuthenticationToken(userDetail, userDetail.getUsername(),
					userDetail.getAuthorities());
		}
		catch (GlobalException e) {
			throw OAuth2ExceptionHandler.getException(e.getCode(), e.getMsg());
		}
		finally {
			// 清除数据源
			DynamicDataSourceContextHolder.clear();
			// 保存领域事件
			domainEventService.create(user.getEvents().getFirst());
		}
	}

	private void checkCaptcha(User user, Captcha captchaObj, HttpServletRequest request) {
		if (ObjectUtil.isNotNull(captchaObj)) {
			Boolean checkResult = captchaGateway.check(captchaObj.getUuid(), captchaObj.getCaptcha());
			// 检查验证码
			user.checkCaptcha(checkResult, request);
		}
	}

	private String getSourceName(Long tenantId) {
		// 默认主表
		if (DEFAULT == tenantId) {
			return MASTER;
		}
		return sourceGateway.findSourceNameByTenantId(tenantId);
	}

}
