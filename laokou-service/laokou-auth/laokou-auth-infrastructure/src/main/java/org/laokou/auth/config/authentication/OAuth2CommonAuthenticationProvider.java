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
import org.laokou.common.domain.context.DomainEventContextHolder;
import org.laokou.common.domain.publish.DomainEventPublisher;
import org.laokou.common.domain.service.DomainEventService;
import org.laokou.common.i18n.common.exception.GlobalException;
import org.laokou.common.i18n.utils.DateUtil;
import org.laokou.common.i18n.utils.ObjectUtil;
import org.laokou.common.security.utils.UserDetail;
import org.springframework.core.env.Environment;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Set;

import static org.laokou.common.i18n.common.JobModeEnums.SYNC;
import static org.laokou.common.i18n.common.PropertiesConstants.SPRING_APPLICATION_NAME;

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

	private final DomainEventPublisher domainEventPublisher;

	private final Environment environment;

	public UsernamePasswordAuthenticationToken authenticationToken(User user, HttpServletRequest request) {
		try {
			// 认证类型
			String authType = user.getAuth().getType();
			// 应用名称
			String appName = environment.getProperty(SPRING_APPLICATION_NAME);
			Captcha captchaObj = user.getCaptcha();
			Long tenantId = user.getTenantId();
			String clientPassword = user.getPassword();
			// 数据源名称
			String sourceName = sourceGateway.findSourceNameByTenantId(tenantId);
			// 检查验证码
			checkCaptcha(user, captchaObj, request, sourceName, appName, authType);
			User u = userGateway.find(user);
			// 检查空对象
			user = user.copy(u, request, sourceName, appName, authType);
			// 检查密码
			user.checkPassword(clientPassword, passwordEncoder, request, sourceName, appName, authType);
			// 检查状态
			user.checkStatus(request, sourceName, appName, authType);
			Set<String> permissions = menuGateway.findPermissions(user);
			// 检查权限标识集合
			user.checkNullPermissions(permissions, request, sourceName, appName, authType);
			Set<String> deptPaths = deptGateway.findDeptPaths(user);
			UserDetail userDetail = convert(user, request, deptPaths, permissions, sourceName);
			// 登录成功
			user.loginSuccess(request, sourceName, appName, authType);
			return new UsernamePasswordAuthenticationToken(userDetail, userDetail.getUsername(),
					userDetail.getAuthorities());
		}
		catch (GlobalException e) {
			throw OAuth2ExceptionHandler.getException(e.getCode(), e.getMsg());
		}
		finally {
			// 保存领域事件（事件溯源）
			domainEventService.create(user.getEvents());
			// 清除数据源上下文
			DynamicDataSourceContextHolder.clear();
			// 发布当前线程的领域事件(同步发布)
			domainEventPublisher.publish(SYNC);
			// 清空领域事件
			user.clearEvents();
			// 清除领域事件上下文
			DomainEventContextHolder.clear();
		}
	}

	private UserDetail convert(User user, HttpServletRequest request, Set<String> deptPaths, Set<String> permissions,
			String sourceName) {
		return UserDetail.builder()
			.username(user.getUsername())
			.loginDate(DateUtil.now())
			.loginIp(IpUtil.getIpAddr(request))
			.id(user.getId())
			.deptId(user.getDeptId())
			.tenantId(user.getTenantId())
			.deptPath(user.getDeptPath())
			.sourceName(sourceName)
			.deptPaths(deptPaths)
			.permissions(permissions)
			.avatar(user.getAvatar())
			.password(user.getPassword())
			.superAdmin(user.getSuperAdmin())
			.mail(user.getMail())
			.mobile(user.getMobile())
			.status(user.getStatus())
			.build();
	}

	private void checkCaptcha(User user, Captcha captchaObj, HttpServletRequest request, String sourceName,
			String appName, String authType) {
		if (ObjectUtil.isNotNull(captchaObj)) {
			Boolean checkResult = captchaGateway.check(captchaObj.getUuid(), captchaObj.getCaptcha());
			// 检查验证码
			user.checkCaptcha(checkResult, request, sourceName, appName, authType);
		}
	}

}
