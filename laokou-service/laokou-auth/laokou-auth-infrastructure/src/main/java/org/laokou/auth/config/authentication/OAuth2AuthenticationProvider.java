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
import org.laokou.auth.domain.auth.Auth;
import org.laokou.auth.domain.gateway.*;
import org.laokou.auth.domain.auth.Captcha;
import org.laokou.common.core.utils.IpUtil;
import org.laokou.common.domain.context.DomainEventContextHolder;
import org.laokou.common.domain.publish.DomainEventPublisher;
import org.laokou.common.domain.service.DomainEventService;
import org.laokou.common.i18n.common.exception.AuthException;
import org.laokou.common.i18n.utils.DateUtils;
import org.laokou.common.i18n.utils.ObjectUtils;
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

	public UsernamePasswordAuthenticationToken authenticationToken(Auth auth, HttpServletRequest request) {
		try {
			// 认证类型
			String authType = auth.getSecretKey().getType();
			// 应用名称
			String appName = environment.getProperty(SPRING_APPLICATION_NAME);
			Captcha captchaObj = auth.getCaptcha();
			Long tenantId = auth.getTenantId();
			String clientPassword = auth.getPassword();
			// 数据源名称
			String sourceName = sourceGateway.findSourceNameByTenantId(tenantId);
			// 检查验证码
			checkCaptcha(auth, captchaObj, request, sourceName, appName, authType);
			Auth u = userGateway.find(auth);
			// 检查空对象
			auth = auth.create(u, request, sourceName, appName, authType);
			// 检查密码
			auth.checkPassword(clientPassword, passwordEncoder, request, sourceName, appName, authType);
			// 检查状态
			auth.checkStatus(request, sourceName, appName, authType);
			Set<String> permissions = menuGateway.findPermissions(auth);
			// 检查权限标识集合
			auth.checkNullPermissions(permissions, request, sourceName, appName, authType);
			Set<String> deptPaths = deptGateway.findDeptPaths(auth);
			UserDetail userDetail = convert(auth, request, deptPaths, permissions, sourceName);
			// 登录成功
			auth.loginSuccess(request, sourceName, appName, authType);
			return new UsernamePasswordAuthenticationToken(userDetail, userDetail.getUsername(),
					userDetail.getAuthorities());
		}
		catch (AuthException e) {
			throw getException(e.getCode(), e.getMsg(), ERROR_URL);
		}
		finally {
			// 保存领域事件（事件溯源）
			domainEventService.create(auth.getEvents());
			// 清除数据源上下文
			DynamicDataSourceContextHolder.clear();
			// 发布当前线程的领域事件(同步发布)
			domainEventPublisher.publish(SYNC);
			// 清除领域事件上下文
			DomainEventContextHolder.clear();
			// 清空领域事件
			auth.clearEvents();
		}
	}

	private UserDetail convert(Auth auth, HttpServletRequest request, Set<String> deptPaths, Set<String> permissions,
							   String sourceName) {
		return UserDetail.builder()
			.username(auth.getUsername())
			.loginDate(DateUtils.now())
			.loginIp(IpUtil.getIpAddr(request))
			.id(auth.getId())
			.deptId(auth.getDeptId())
			.tenantId(auth.getTenantId())
			.deptPath(auth.getDeptPath())
			.sourceName(sourceName)
			.deptPaths(deptPaths)
			.permissions(permissions)
			.avatar(auth.getAvatar())
			.password(auth.getPassword())
			.superAdmin(auth.getSuperAdmin())
			.mail(auth.getMail())
			.mobile(auth.getMobile())
			.status(auth.getStatus())
			.build();
	}

	private void checkCaptcha(Auth auth, Captcha captchaObj, HttpServletRequest request, String sourceName,
							  String appName, String authType) {
		if (ObjectUtils.isNotNull(captchaObj)) {
			Boolean checkResult = captchaGateway.check(captchaObj.getUuid(), captchaObj.getCaptcha());
			// 检查验证码
			auth.checkCaptcha(checkResult, request, sourceName, appName, authType);
		}
	}

}
