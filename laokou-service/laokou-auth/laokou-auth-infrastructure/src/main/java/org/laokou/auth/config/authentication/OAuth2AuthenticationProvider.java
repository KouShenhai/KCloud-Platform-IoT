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
import org.laokou.auth.domain.model.auth.AuthA;
import org.laokou.auth.domain.gateway.*;
import org.laokou.auth.domain.model.auth.CaptchaV;
import org.laokou.common.core.utils.IpUtil;
import org.laokou.common.domain.context.DomainEventContextHolder;
import org.laokou.common.domain.publish.DomainEventPublisher;
import org.laokou.common.domain.service.DomainEventService;
import org.laokou.common.i18n.common.exception.AuthException;
import org.laokou.common.i18n.utils.DateUtil;
import org.laokou.common.i18n.utils.ObjectUtil;
import org.laokou.common.security.utils.UserDetail;
import org.springframework.core.env.Environment;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Set;

import static org.laokou.common.i18n.common.JobModeEnum.SYNC;
import static org.laokou.common.i18n.common.PropertiesConstant.SPRING_APPLICATION_NAME;
import static org.laokou.common.security.handler.OAuth2ExceptionHandler.ERROR_URL;
import static org.laokou.common.security.handler.OAuth2ExceptionHandler.getException;

/**
 * @author laokou
 */
@RequiredArgsConstructor
@Component("authProvider")
public class OAuth2AuthenticationProvider {

	private final UserGateway userGateway;

	private final MenuGateway menuGateway;

	private final DeptGateway deptGateway;

	private final PasswordEncoder passwordEncoder;

	private final CaptchaGateway captchaGateway;

	private final SourceGateway sourceGateway;

	private final DomainEventService domainEventService;

	private final DomainEventPublisher domainEventPublisher;

	private final Environment environment;

	public UsernamePasswordAuthenticationToken authenticationToken(AuthA authA, HttpServletRequest request) {
		try {
			// 认证类型
			String authType = authA.getSecretKey().getType();
			// 应用名称
			String appName = environment.getProperty(SPRING_APPLICATION_NAME);
			CaptchaV captchaVObj = authA.getCaptchaV();
			Long tenantId = authA.getTenantId();
			String clientPassword = authA.getPassword();
			// 数据源名称
			String sourceName = sourceGateway.findSourceNameByTenantId(tenantId);
			// 检查验证码
			checkCaptcha(authA, captchaVObj, request, sourceName, appName, authType);
			AuthA u = userGateway.find(authA);
			// 检查空对象
			authA = authA.create(u, request, sourceName, appName, authType);
			// 检查密码
			authA.checkPassword(clientPassword, passwordEncoder, request, sourceName, appName, authType);
			// 检查状态
			authA.checkStatus(request, sourceName, appName, authType);
			Set<String> permissions = menuGateway.findPermissions(authA);
			// 检查权限标识集合
			authA.checkNullPermissions(permissions, request, sourceName, appName, authType);
			Set<String> deptPaths = deptGateway.findDeptPaths(authA);
			UserDetail userDetail = convert(authA, request, deptPaths, permissions, sourceName);
			// 登录成功
			authA.loginSuccess(request, sourceName, appName, authType);
			return new UsernamePasswordAuthenticationToken(userDetail, userDetail.getUsername(),
					userDetail.getAuthorities());
		}
		catch (AuthException e) {
			throw getException(e.getCode(), e.getMsg(), ERROR_URL);
		}
		finally {
			// 保存领域事件（事件溯源）
			domainEventService.create(authA.getEvents());
			// 清除数据源上下文
			DynamicDataSourceContextHolder.clear();
			// 发布当前线程的领域事件(同步发布)
			domainEventPublisher.publish(SYNC);
			// 清除领域事件上下文
			DomainEventContextHolder.clear();
			// 清空领域事件
			authA.clearEvents();
		}
	}

	private UserDetail convert(AuthA authA, HttpServletRequest request, Set<String> deptPaths, Set<String> permissions,
			String sourceName) {
		return UserDetail.builder()
			.username(authA.getUsername())
			.loginDate(DateUtil.now())
			.loginIp(IpUtil.getIpAddr(request))
			.id(authA.getId())
			.deptId(authA.getDeptId())
			.tenantId(authA.getTenantId())
			.deptPath(authA.getDeptPath())
			.sourceName(sourceName)
			.deptPaths(deptPaths)
			.permissions(permissions)
			.avatar(authA.getAvatar())
			.password(authA.getPassword())
			.superAdmin(authA.getSuperAdmin())
			.mail(authA.getMail())
			.mobile(authA.getMobile())
			.status(authA.getStatus())
			.build();
	}

	private void checkCaptcha(AuthA authA, CaptchaV captchaVObj, HttpServletRequest request, String sourceName,
			String appName, String authType) {
		if (ObjectUtil.isNotNull(captchaVObj)) {
			Boolean checkResult = captchaGateway.check(captchaVObj.uuid(), captchaVObj.captcha());
			// 检查验证码
			authA.checkCaptcha(checkResult, request, sourceName, appName, authType);
		}
	}

}
